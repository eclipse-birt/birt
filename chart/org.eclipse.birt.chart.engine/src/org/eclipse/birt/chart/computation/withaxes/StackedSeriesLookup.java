/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation.withaxes;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Implements a double lookup data structure for stacked series. It also
 * maintains a min/max value for each unit needed to build the scale.
 */
public final class StackedSeriesLookup {

	private final Hashtable<Axis, List<StackGroup>> htAxisToStackGroups;

	private final Hashtable<Series, StackGroup> htSeriesToStackGroup;

	private int iCachedUnitCount = 0;

	/**
	 * The constructor.
	 */
	StackedSeriesLookup(RunTimeContext rtc) {
		htAxisToStackGroups = SecurityUtil.newHashtable();
		htSeriesToStackGroup = SecurityUtil.newHashtable();
	}

	public final List<StackGroup> getStackGroups(Axis ax) {
		return htAxisToStackGroups.get(ax);
	}

	public final int getSeriesCount(Axis ax) {
		final List<StackGroup> alSG = htAxisToStackGroups.get(ax);
		if (alSG == null || alSG.isEmpty()) {
			return 0;
		}

		int iCount = 0;
		StackGroup sg;
		for (int i = 0; i < alSG.size(); i++) {
			sg = alSG.get(i);
			iCount += sg.alSeries.size();
		}
		return iCount;
	}

	/**
	 * @param se
	 * @return The stack group associated with a specified Series
	 */
	public final StackGroup getStackGroup(Series se) {
		return htSeriesToStackGroup.get(se);
	}

	/**
	 * @param sg
	 * @param iUnitIndex
	 * @return An AxisUnit corresponding to a given stack group and specified unit
	 *         index
	 */
	public final AxisSubUnit getSubUnit(StackGroup sg, int iUnitIndex) {
		if (sg == null || !htSeriesToStackGroup.contains(sg)) {
			return null;
		}

		// IF NOT YET INITIALIZED, DO SO LAZILY
		if (sg.alUnitPositions == null) {
			sg.alUnitPositions = new ArrayList<AxisSubUnit>(8);
		}

		// IF NOT YET CONTAINED, ADD LAZILY
		if (sg.alUnitPositions.size() <= iUnitIndex) {
			sg.alUnitPositions.add(new AxisSubUnit(sg.bStackTogether));
		}

		return sg.alUnitPositions.get(iUnitIndex);
	}

	/**
	 * Returns an AxisUnit needed to 'remember' the position of the next stacked bar
	 * to be rendered. If a series is not 'stackable' or not 'set as stacked', this
	 * method will return 'null'.
	 * 
	 * @param ax
	 * @param se
	 * @param iUnitIndex
	 * 
	 * @return unit
	 */
	public final AxisSubUnit getUnit(Series se, int iUnitIndex) {
		// LOOKUP STACKED GROUP FOR SERIES
		StackGroup sg = htSeriesToStackGroup.get(se);
		return getSubUnit(sg, iUnitIndex);
	}

	/**
	 * 
	 */
	public final void resetSubUnits() {
		Enumeration<StackGroup> e = htSeriesToStackGroup.elements();
		StackGroup sg;
		AxisSubUnit asu;

		while (e.hasMoreElements()) {
			sg = e.nextElement();
			if (sg.alUnitPositions != null) {
				for (int i = 0; i < sg.alUnitPositions.size(); i++) {
					asu = sg.alUnitPositions.get(i);
					asu.reset();
				}
			}
		}
	}

	/**
	 * 
	 * @param cwa
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	public static final StackedSeriesLookup create(ChartWithAxes cwa, RunTimeContext rtc)
			throws ChartException, IllegalArgumentException {
		if (cwa == null) // NPE CHECK
		{
			return null;
		}

		final StackedSeriesLookup ssl = new StackedSeriesLookup(rtc);
		final Axis axBase = cwa.getBaseAxes()[0];
		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axBase, true);

		int iSeriesCount;
		StackGroup sg, sgSingle;
		Series se;
		boolean bStackedSet;
		int iSharedUnitIndex, iSharedUnitCount, iDataSetCount;
		DataSetIterator dsi = null;

		for (int i = 0; i < axaOrthogonal.length; i++) // EACH AXIS
		{
			iSharedUnitIndex = 0;
			iSharedUnitCount = 0;
			sgSingle = null; // RESET PER AXIS
			EList<SeriesDefinition> sdList = axaOrthogonal[i].getSeriesDefinitions();
			List<StackGroup> alSGCopies = new ArrayList<StackGroup>(4);
			iSharedUnitCount = 0;

			for (SeriesDefinition sd : sdList) // EACH SERIES DEFINITION
			{
				List<Series> alSeries = sd.getRunTimeSeries();
				iSeriesCount = alSeries.size();
				if (iSeriesCount > 1) {
					bStackedSet = false;
					sg = null;

					for (int k = 0; k < iSeriesCount; k++) // EACH SERIES
					{
						se = alSeries.get(k);
						dsi = new DataSetIterator(se.getDataSet());
						iDataSetCount = dsi.size();
						if (ssl.iCachedUnitCount == 0) {
							ssl.iCachedUnitCount = iDataSetCount;
						} else if (ssl.iCachedUnitCount != iDataSetCount) {
							throw new IllegalArgumentException(MessageFormat.format(
									Messages.getResourceBundle(rtc.getULocale())
											.getString("exception.runtime.dataset.count.mismatch"), //$NON-NLS-1$
									new Object[] { Integer.valueOf(ssl.iCachedUnitCount),
											Integer.valueOf(iDataSetCount) })

							);
						}
						if (se.canBeStacked()) {
							if (se.canShareAxisUnit()) {
								if (se.isStacked()) {
									if (k > 0 && !bStackedSet) {
										throw new IllegalArgumentException(MessageFormat.format(
												Messages.getResourceBundle(rtc.getULocale())
														.getString("exception.stacked.unstacked.mix.series"), //$NON-NLS-1$
												new Object[] { sd })

										);
									}
									if (k == 0) // ONE GROUP FOR ALL STACKED
									// SERIES
									{
										sg = new StackGroup(iSharedUnitIndex++);
										alSGCopies.add(sg);
										iSharedUnitCount++;
									}
									bStackedSet = true;
									ssl.htSeriesToStackGroup.put(se, sg);
									sg.addSeries(se); // REQUIRE REVERSE
									// LOOKUP
								} else {
									if (k > 0 && bStackedSet) {
										throw new IllegalArgumentException(MessageFormat.format(
												Messages.getResourceBundle(rtc.getULocale())
														.getString("exception.stacked.unstacked.mix.series"), //$NON-NLS-1$
												new Object[] { sd })

										);
									}
									if (canSideBySide(se)) {
										sg = new StackGroup(iSharedUnitIndex++); // NEW
										// GROUP
										// FOR
										// EACH
										// UNSTACKED
										// SERIES
										alSGCopies.add(sg);
										iSharedUnitCount++;
										ssl.htSeriesToStackGroup.put(se, sg);
										sg.addSeries(se); // REQUIRE REVERSE
										// LOOKUP
									}
								}
							} else {
								if (se.isStacked()) {
									if (k > 0 && !bStackedSet) {
										throw new IllegalArgumentException(MessageFormat.format(
												Messages.getResourceBundle(rtc.getULocale())
														.getString("exception.stacked.unstacked.mix.series"), //$NON-NLS-1$
												new Object[] { sd })

										);
									}
									if (k == 0) // ONE GROUP FOR ALL STACKED
									// SERIES
									{
										sg = new StackGroup(-1); // UNSET
										// BECAUSE
										// DOESNT
										// SHARE AXIS
										// UNITS
										alSGCopies.add(sg);
									}
									bStackedSet = true;
									ssl.htSeriesToStackGroup.put(se, sg);
									sg.addSeries(se); // REQUIRE REVERSE
									// LOOKUP
								} else {
									if (k > 0 && bStackedSet) {
										throw new IllegalArgumentException(MessageFormat.format(
												Messages.getResourceBundle(rtc.getULocale())
														.getString("exception.stacked.unstacked.mix.series"), //$NON-NLS-1$
												new Object[] { sd })

										);
									}
									sg = new StackGroup(-1); // NEW GROUP FOR
									// EACH UNSTACKED
									// SERIES
									alSGCopies.add(sg);
									ssl.htSeriesToStackGroup.put(se, sg);
									sg.addSeries(se); // REQUIRE REVERSE
									// LOOKUP
								}
							}
						} else if (se.canShareAxisUnit()) {
							sg = new StackGroup(iSharedUnitIndex++); // NEW
							// GROUP
							// FOR
							// EACH
							// UNSTACKED
							// SERIES
							alSGCopies.add(sg);
							iSharedUnitCount++;
							ssl.htSeriesToStackGroup.put(se, sg);
							sg.addSeries(se); // REQUIRE REVERSE
							// LOOKUP
						} else
						// e.g. each custom series in its own stack (not stacked
						// but
						{
							sg = new StackGroup(-1); // ONE PER UNSTACKED
							// SERIES (SHARED INDEX
							// IS UNSET)
							alSGCopies.add(sg);
							ssl.htSeriesToStackGroup.put(se, sg);
							sg.addSeries(se); // REQUIRE REVERSE LOOKUP
						}
					}
				} else
				// ONE OR LESS SERIES USE THE SINGLE STACK GROUP
				{
					for (int k = 0; k < iSeriesCount; k++) // EACH SERIES
					// (iSeriesCount
					// SHOULD BE ONE)
					{
						se = alSeries.get(k);
						dsi = new DataSetIterator(se.getDataSet());
						iDataSetCount = dsi.size();
						if (ssl.iCachedUnitCount == 0) {
							ssl.iCachedUnitCount = iDataSetCount;
						} else if (ssl.iCachedUnitCount != iDataSetCount) {
							throw new IllegalArgumentException(MessageFormat.format(
									Messages.getResourceBundle(rtc.getULocale())
											.getString("exception.runtime.dataset.count.mismatch"), //$NON-NLS-1$
									new Object[] { Integer.valueOf(ssl.iCachedUnitCount),
											Integer.valueOf(iDataSetCount) })

							);
						}
						if (se.canBeStacked()) {
							if (se.canShareAxisUnit()) {
								if (se.isStacked()) {
									if (sgSingle == null) {
										sgSingle = new StackGroup(iSharedUnitIndex++);
										alSGCopies.add(sgSingle);
										iSharedUnitCount++;
									}
									ssl.htSeriesToStackGroup.put(se, sgSingle);
									sgSingle.addSeries(se); // REQUIRE
									// REVERSE
									// LOOKUP
								} else {
									if (canSideBySide(se)) {
										sg = new StackGroup(iSharedUnitIndex++); // ONE
										// PER
										// UNSTACKED
										// SERIES
										// (SHARED
										// INDEX
										// IS
										// SET)
										iSharedUnitCount++;
										alSGCopies.add(sg);
										ssl.htSeriesToStackGroup.put(se, sg);
										sg.addSeries(se); // REQUIRE REVERSE
										// LOOKUP
									}
								}
							} else
							// e.g. each line series in its own stack
							{
								sg = new StackGroup(-1); // ONE PER UNSTACKED
								// SERIES (SHARED
								// INDEX IS UNSET)
								alSGCopies.add(sg);
								ssl.htSeriesToStackGroup.put(se, sg);
								sg.addSeries(se); // REQUIRE REVERSE LOOKUP
							}
						} else
						// e.g. each custom series in its own stack (not stacked
						// but
						{
							sg = new StackGroup(-1); // ONE PER UNSTACKED
							// SERIES (SHARED INDEX
							// IS UNSET)
							alSGCopies.add(sg);
							ssl.htSeriesToStackGroup.put(se, sg);
							sg.addSeries(se); // REQUIRE REVERSE LOOKUP
						}
					}
				}
			}

			if (iSharedUnitCount < 1)
				iSharedUnitCount = 1;
			for (int j = 0; j < alSGCopies.size(); j++) {
				sg = alSGCopies.get(j);
				sg.updateCount(iSharedUnitCount);
			}

			ssl.htAxisToStackGroups.put(axaOrthogonal[i], alSGCopies);
		}

		return ssl;
	}

	public final int getUnitCount() {
		return iCachedUnitCount;
	}

	private static boolean canSideBySide(Series se) {
		return (se instanceof BarSeries) || (se instanceof StockSeries);
	}
}