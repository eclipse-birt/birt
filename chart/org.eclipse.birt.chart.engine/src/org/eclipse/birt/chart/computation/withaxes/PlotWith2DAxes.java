/***********************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation.withaxes;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.LabelLimiter;
import org.eclipse.birt.chart.computation.LegendItemRenderingHints;
import org.eclipse.birt.chart.computation.UserDataSetHints;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.NullDataSet;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.AxesRenderer;
import org.eclipse.birt.chart.render.IAxesDecorator;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.util.BigNumber;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.NumberUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.util.Calendar;

/**
 * This class is capable of computing the content of a chart (with axes) based
 * on preferred sizes, text rotation, fit ability, scaling, etc and prepares it
 * for rendering.
 *
 * WARNING: This is an internal class and subject to change
 */
public final class PlotWith2DAxes extends PlotWithAxes {

	/**
	 * This complex reference is used in rendering stacked series otherwise unused.
	 */
	private StackedSeriesLookup ssl = null;

	private int iMarginPercent = 0;

	private Bounds boPlotWithMargin = goFactory.createBounds(0, 0, 100, 100);

	/**
	 * The default constructor
	 *
	 * @param _ids The display server using which the chart is computed
	 * @param _cwa An instance of the model (ChartWithAxes)
	 * @throws IllegalArgumentException
	 * @throws ChartException
	 */
	public PlotWith2DAxes(IDisplayServer _ids, ChartWithAxes _cwa, RunTimeContext _rtc)
			throws IllegalArgumentException, ChartException {
		super(_ids, _rtc, _cwa);
		ssl = new StackedSeriesLookup(_rtc);
		buildAxes(); // CREATED ONCE
		initAlignZeroHelper();
	}

	/**
	 * Internally maps the EMF model to internal (non-public) rendering fast data
	 * structures
	 */
	@Override
	void buildAxes() throws IllegalArgumentException, ChartException {
		ChartWithAxes cwa = getModel();
		final Axis[] axa = cwa.getPrimaryBaseAxes();
		// NOTE: FOR REL 1 AXIS RENDERS, WE SUPPORT A SINGLE PRIMARY BASE AXIS
		// ONLY
		final Axis axPrimaryBase = axa[0];
		validateAxis(axPrimaryBase);

		final Axis axPrimaryOrthogonal = cwa.getPrimaryOrthogonalAxis(axPrimaryBase);
		validateAxis(axPrimaryOrthogonal);

		final Axis[] axaOverlayOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, false);
		aax = new AllAxes(goFactory.scaleInsets(cwa.getPlot().getClientArea().getInsets(), dPointToPixel)); // CONVERSION
		insCA = aax.getInsets();

		final boolean isTransposed = cwa.isTransposed();

		aax.swapAxes(isTransposed);

		// SETUP THE PRIMARY BASE-AXIS PROPERTIES AND ITS SCALE
		final OneAxis oaxPrimaryBase = new OneAxis(axPrimaryBase);
		oaxPrimaryBase.set(getOrientation(IConstants.BASE), transposeLabelPosition(IConstants.BASE, getLabelPosition(
				// isTransposed ? switchPosition(
				// axPrimaryBase.getLabelPosition( ) )
				// :
				axPrimaryBase.getLabelPosition())), transposeLabelPosition(IConstants.BASE,
						getLabelPosition(
								// isTransposed ? switchPosition(
								// axPrimaryBase.getTitlePosition( ) )
								// :
								axPrimaryBase.getTitlePosition())),
				axPrimaryBase.isCategoryAxis(), axPrimaryBase.getScale().isTickBetweenCategories());
		oaxPrimaryBase.setGridProperties(axPrimaryBase.getMajorGrid().getLineAttributes(),
				axPrimaryBase.getMinorGrid().getLineAttributes(), axPrimaryBase.getMajorGrid().getTickAttributes(),
				axPrimaryBase.getMinorGrid().getTickAttributes(), transposeTickStyle(IConstants.BASE,
						// isTransposed ? switchTickStyle( getTickStyle( axPrimaryBase,
						// MAJOR ) )
						// :
						getTickStyle(axPrimaryBase, MAJOR)),
				transposeTickStyle(IConstants.BASE,
						// isTransposed ? switchTickStyle( getTickStyle( axPrimaryBase,
						// MINOR ) )
						// :
						getTickStyle(axPrimaryBase, MINOR)),
				axPrimaryBase.getScale().getMinorGridsPerUnit());

		oaxPrimaryBase.set(axPrimaryBase.getLabel(), axPrimaryBase.getTitle());
		oaxPrimaryBase.set(
				// isTransposed ? switchIntersection( getIntersection( axPrimaryBase ) )
				// :
				getIntersection(axPrimaryBase));
		oaxPrimaryBase.set(axPrimaryBase.getLineAttributes());
		aax.definePrimary(oaxPrimaryBase); // ADD TO AXIS SET

		// SETUP THE PRIMARY ORTHOGONAL-AXIS PROPERTIES AND ITS SCALE
		final OneAxis oaxPrimaryOrthogonal = new OneAxis(axPrimaryOrthogonal);
		oaxPrimaryOrthogonal.set(getOrientation(IConstants.ORTHOGONAL),
				transposeLabelPosition(IConstants.ORTHOGONAL,
						getLabelPosition(!isTransposed ? axPrimaryOrthogonal.getLabelPosition()
								: axPrimaryOrthogonal.getLabelPosition())),
				transposeLabelPosition(IConstants.ORTHOGONAL,
						getLabelPosition(!isTransposed ? axPrimaryOrthogonal.getTitlePosition()
								: axPrimaryOrthogonal.getTitlePosition())),
				axPrimaryOrthogonal.isCategoryAxis(), axPrimaryOrthogonal.getScale().isTickBetweenCategories());
		oaxPrimaryOrthogonal.setGridProperties(axPrimaryOrthogonal.getMajorGrid().getLineAttributes(),
				axPrimaryOrthogonal.getMinorGrid().getLineAttributes(),
				axPrimaryOrthogonal.getMajorGrid().getTickAttributes(),
				axPrimaryOrthogonal.getMinorGrid().getTickAttributes(),
				transposeTickStyle(IConstants.ORTHOGONAL,
						!isTransposed ? getTickStyle(axPrimaryOrthogonal, MAJOR)
								: getTickStyle(axPrimaryOrthogonal, MAJOR)),
				transposeTickStyle(IConstants.ORTHOGONAL,
						!isTransposed ? getTickStyle(axPrimaryOrthogonal, MINOR)
								: getTickStyle(axPrimaryOrthogonal, MINOR)),
				axPrimaryOrthogonal.getScale().getMinorGridsPerUnit());

		oaxPrimaryOrthogonal.set(axPrimaryOrthogonal.getLabel(), axPrimaryOrthogonal.getTitle());
		oaxPrimaryOrthogonal.set(
				// !isTransposed ?
				getIntersection(axPrimaryOrthogonal)
		// : getIntersection( axPrimaryOrthogonal )
		);
		oaxPrimaryOrthogonal.set(axPrimaryOrthogonal.getLineAttributes());
		aax.definePrimary(oaxPrimaryOrthogonal); // ADD TO AXIS SET

		// SETUP THE OVERLAY AXES
		aax.initOverlays(axaOverlayOrthogonal.length, getOrientation(IConstants.ORTHOGONAL));
		OneAxis oaxOverlayOrthogonal;
		for (int i = 0; i < axaOverlayOrthogonal.length; i++) {
			validateAxis(axaOverlayOrthogonal[i]);

			oaxOverlayOrthogonal = new OneAxis(axaOverlayOrthogonal[i]);
			oaxOverlayOrthogonal.set(getOrientation(IConstants.ORTHOGONAL),
					transposeLabelPosition(IConstants.ORTHOGONAL,
							getLabelPosition(!isTransposed ? axaOverlayOrthogonal[i].getLabelPosition()
									: axaOverlayOrthogonal[i].getLabelPosition())),
					transposeLabelPosition(IConstants.ORTHOGONAL,
							getLabelPosition(!isTransposed ? axaOverlayOrthogonal[i].getTitlePosition()
									: axaOverlayOrthogonal[i].getTitlePosition())),
					axaOverlayOrthogonal[i].isCategoryAxis(),
					axaOverlayOrthogonal[i].getScale().isTickBetweenCategories());
			oaxOverlayOrthogonal.setGridProperties(axaOverlayOrthogonal[i].getMajorGrid().getLineAttributes(),
					axaOverlayOrthogonal[i].getMinorGrid().getLineAttributes(),
					axaOverlayOrthogonal[i].getMajorGrid().getTickAttributes(),
					axaOverlayOrthogonal[i].getMinorGrid().getTickAttributes(),
					transposeTickStyle(IConstants.ORTHOGONAL,
							!isTransposed ? getTickStyle(axaOverlayOrthogonal[i], MAJOR)
									: getTickStyle(axaOverlayOrthogonal[i], MAJOR)),
					transposeTickStyle(IConstants.ORTHOGONAL,
							!isTransposed ? getTickStyle(axaOverlayOrthogonal[i], MINOR)
									: getTickStyle(axaOverlayOrthogonal[i], MINOR)),
					axaOverlayOrthogonal[i].getScale().getMinorGridsPerUnit());

			oaxOverlayOrthogonal.set(axaOverlayOrthogonal[i].getLabel(), axaOverlayOrthogonal[i].getTitle());
			oaxOverlayOrthogonal.set(axaOverlayOrthogonal[i].getLineAttributes());
			oaxOverlayOrthogonal.set(
					// !isTransposed ?
					getIntersection(axaOverlayOrthogonal[i])
			// : getIntersection( axaOverlayOrthogonal[i] )
			);
			aax.defineOverlay(i, oaxOverlayOrthogonal);
		}

		// BUILD STACKED STRUCTURE (FOR STACKED SERIES) ASSOCIATED WITH EACH
		// ORTHOGONAL AXIS
		ssl = StackedSeriesLookup.create(cwa, rtc);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.computation.withaxes.PlotWithAxes#getMinMax(org
	 * .eclipse.birt.chart.model.component.Axis, int)
	 */
	@Override
	protected Object getMinMax(Axis ax, int iType) throws ChartException, IllegalArgumentException {
		if (ax.getType().getValue() == AxisType.LINEAR && ax.isAligned() && azHelper != null) {
			double[] minmax = azHelper.getCachedMinMax(ax);
			if (minmax != null) {
				return minmax;
			}
		}

		ChartWithAxes cwa = getModel();
		final Series[] sea = ax.getRuntimeSeries();
		final int iSeriesCount = sea.length;
		Series se;
		DataSet ds = null;

		Object oV1, oV2, oMin = null, oMax = null;

		PluginSettings ps = PluginSettings.instance();
		IDataSetProcessor iDSP = null;
		// ANY STACKED SERIES ASSOCIATED WITH AXIS 'ax'
		boolean bAnyStacked = false;

		for (int i = 0; i < iSeriesCount; i++) {
			if (sea[i].isStacked()) {
				if (sea[i].canBeStacked()) {
					bAnyStacked = true;
					continue;
				} else {
					throw new IllegalArgumentException(MessageFormat.format(
							Messages.getResourceBundle(rtc.getULocale()).getString("exception.unstackable.is.stacked"), //$NON-NLS-1$
							new Object[] { sea[i] }));
				}
			}

			iDSP = ps.getDataSetProcessor(sea[i].getClass());
			ds = sea[i].getDataSet();

			if (ds instanceof NullDataSet && rtc.getSharedScale() != null) {
				// Bugzilla#271740 If no data but shared scale used, get min/max
				// value from scale directly.
				oMin = rtc.getSharedScale().getScaleContext().getMin();
				oMax = rtc.getSharedScale().getScaleContext().getMax();
			} else {
				oV1 = iDSP.getMinimum(ds);
				oV2 = iDSP.getMaximum(ds);

				if ((iType & NUMERICAL) == NUMERICAL) {
					try {
						if (oV1 != null) // SETUP THE MINIMUM VALUE FOR ALL
						// DATASETS
						{
							if (oMin == null) {
								oMin = oV1;
							} else if (NumberUtil.isBigNumber(oV1)) {
								oMin = ((BigNumber) oMin).min((BigNumber) oV1);
							} else {
								final double dV1 = asDouble(oV1).doubleValue();
								if (Math.min(asDouble(oMin).doubleValue(), dV1) == dV1) {
									oMin = oV1;
								}
							}
						}

						if (oV2 != null) // SETUP THE MAXIMUM VALUE FOR ALL
						// DATASETS
						{
							if (oMax == null) {
								oMax = oV2;
							} else if (NumberUtil.isBigNumber(oV2)) {
								oMax = ((BigNumber) oMax).max((BigNumber) oV2);
							} else {
								final double dV2 = asDouble(oV2).doubleValue();

								if (Math.max(asDouble(oMax).doubleValue(), dV2) == dV2) {
									oMax = oV2;
								}
							}
						}
					} catch (ClassCastException ex) {
						throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
								"exception.datetime.data.numerical.axis", //$NON-NLS-1$
								Messages.getResourceBundle(rtc.getULocale()));
					}
				} else if ((iType & DATE_TIME) == DATE_TIME) {
					try {
						if (oV1 != null) // SETUP THE MINIMUM VALUE FOR ALL
						// DATASETS
						{
							if (oMin == null) {
								oMin = oV1;
							} else {
								final CDateTime cdtV1 = asDateTime(oV1);
								final CDateTime cdtMin = asDateTime(oMin);
								if (cdtV1.before(cdtMin)) {
									oMin = cdtV1;
								}
							}
						}

						if (oV2 != null) // SETUP THE MAXIMUM VALUE FOR ALL
						// DATASETS
						{
							if (oMax == null) {
								oMax = oV2;
							} else {
								final CDateTime cdtV2 = asDateTime(oV2);
								final CDateTime cdtMax = asDateTime(oMax);
								if (cdtV2.after(cdtMax)) {
									oMax = cdtV2;
								}
							}
						}
					} catch (ClassCastException ex) {
						throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
								"exception.numerical.data.datetime.axis", //$NON-NLS-1$
								Messages.getResourceBundle(rtc.getULocale()));

					}
				}
			}
		}

		boolean isbignumber = false;
		BigDecimal bigDivisor = null;
		// ONLY NUMERIC VALUES ARE SUPPORTED IN STACKED ELEMENT COMPUTATIONS
		if (bAnyStacked || ax.isPercent()) {
			if (ax.getType().getValue() == AxisType.DATE_TIME) {
				throw new IllegalArgumentException(Messages.getResourceBundle(rtc.getULocale())
						.getString("exception.stacked.datetime.axis.series"));//$NON-NLS-1$
			}
			Object oValue;
			int iSeriesPerGroup;
			double dPercentMax = 0, dPercentMin = 0;
			double dAxisMin = Double.MAX_VALUE, dAxisMax = -Double.MAX_VALUE;
			List<StackGroup> alSeriesGroupsPerAxis = ssl.getStackGroups(ax);
			ArrayList<Series> alSeriesPerGroup;
			StackGroup sg;
			DataSetIterator[] dsi = new DataSetIterator[ssl.getSeriesCount(ax)];

			if (alSeriesGroupsPerAxis == null) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
						"exception.internal.stack.series.setup", //$NON-NLS-1$
						new Object[] { ax }, Messages.getResourceBundle(rtc.getULocale()));
			}
			logger.log(ILogger.INFORMATION, Messages.getString("info.processing.stacked.info", //$NON-NLS-1$
					new Object[] { ax }, rtc.getULocale()));
			int iSeriesIndex, iDataSetCount = ssl.getUnitCount();

			for (int k = 0; k < iDataSetCount; k++) // PER UNIT
			{
				iSeriesIndex = 0;
				for (int i = 0; i < alSeriesGroupsPerAxis.size(); i++) {
					sg = alSeriesGroupsPerAxis.get(i);
					alSeriesPerGroup = sg.getSeries();
					iSeriesPerGroup = alSeriesPerGroup.size();

					if (iSeriesPerGroup > 0) {
						for (int j = 0; j < iSeriesPerGroup; j++) {
							se = alSeriesPerGroup.get(j);
							ds = se.getDataSet();

							if (ds instanceof NullDataSet) {
								// Ignore stacking null data
								ds = null;
								continue;
							} else {
								break;
							}
						}

						if (ds == null) {
							continue;
						}

						if (dsi[iSeriesIndex] == null) {
							dsi[iSeriesIndex] = new DataSetIterator(ds);
							// Reverse the series categories if needed.
							dsi[iSeriesIndex].reverse(cwa.isReverseCategory());

							if ((dsi[iSeriesIndex].getDataType() & IConstants.NUMERICAL) != IConstants.NUMERICAL) {
								throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
										"exception.percent.stacked.non.numerical", //$NON-NLS-1$
										Messages.getResourceBundle(rtc.getULocale()));
							}
						}

						// ALL SERIES MUST HAVE THE SAME DATASET ELEMENT COUNT
						iDataSetCount = dsi[iSeriesIndex].size();

						final AxisSubUnit au = ssl.getSubUnit(sg, k);
						for (int j = 0; j < iSeriesPerGroup; j++) {
							se = alSeriesPerGroup.get(j);
							if (j > 0) // ALREADY DONE FOR '0'
							{
								if (dsi[iSeriesIndex] == null) {
									ds = se.getDataSet(); // DATA SET
									dsi[iSeriesIndex] = new DataSetIterator(ds);
									// Reverse the series categories if needed.
									dsi[iSeriesIndex].reverse(cwa.isReverseCategory());

									if ((dsi[iSeriesIndex].getDataType()
											& IConstants.NUMERICAL) != IConstants.NUMERICAL) {
										throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
												"exception.percent.stacked.non.numerical", //$NON-NLS-1$
												Messages.getResourceBundle(rtc.getULocale()));
									}
								}
							}
							// EACH ROW OF DATA
							oValue = dsi[iSeriesIndex].next();
							if (oValue != null) // NULL CHECK
							{
								// EXTRACT WRAPPED VALUE
								double dValue = ((Number) oValue).doubleValue();
								au.computeTotal(dValue);
								if (NumberUtil.isBigNumber(oValue) && !isbignumber) {
									isbignumber = true;
									bigDivisor = ((BigNumber) oValue).getDivisor();
								}

							}
							iSeriesIndex++;
						}

						// FOR EACH UNIT, UPDATE THE MIN/MAX BASED ON ALL
						// STACKED SERIES
						if (ax.isPercent()) {
							double dAbsTotal = au.getPositiveTotal() - au.getNegativeTotal();
							if (dAbsTotal != 0d) {
								dPercentMax = Math.max((au.getTotalMax() / dAbsTotal) * 100d, dPercentMax);
								dPercentMin = Math.min((au.getTotalMin() / dAbsTotal) * 100d, dPercentMin);
							}
						} else {
							dAxisMax = Math.max(au.getTotalMax(), dAxisMax);
							dAxisMin = Math.min(au.getTotalMin(), dAxisMin);
						}
					}
				}
			}
			if (ax.isPercent()) // HANDLE PERCENT
			{
				if (dPercentMax > 100) {
					dPercentMax = 100;
				}
				if (dPercentMin < -100) {
					dPercentMin = -100;
				}
				if (dPercentMax == 0 && dPercentMin == 0) {
					dPercentMax = 100;
				}
				dAxisMin = dPercentMin;
				dAxisMax = dPercentMax;
			}
			if ((iType & LOGARITHMIC) == LOGARITHMIC) {
				dAxisMin = 1;
			}
			// If dAxisMin or dAxisMax is not changed, do not set oMin or oMax
			if (dAxisMin != Double.MAX_VALUE
					&& (oMin == null || Double.compare(dAxisMin, ((Number) oMin).doubleValue()) < 0)) {
				if (isbignumber && !ax.isPercent()) {
					oMin = new BigNumber(
							BigDecimal.valueOf(dAxisMin).multiply(bigDivisor, NumberUtil.DEFAULT_MATHCONTEXT),
							bigDivisor);
				} else {
					oMin = new Double(dAxisMin);
				}
			}
			if (dAxisMax != -Double.MAX_VALUE
					&& (oMax == null || Double.compare(dAxisMax, ((Number) oMax).doubleValue()) > 0)) {
				if (isbignumber && !ax.isPercent()) {
					oMax = new BigNumber(
							BigDecimal.valueOf(dAxisMax).multiply(bigDivisor, NumberUtil.DEFAULT_MATHCONTEXT),
							bigDivisor);
				} else {
					oMax = new Double(dAxisMax);
				}
			}
		}

		// IF NO DATASET WAS FOUND BECAUSE NO SERIES WERE ATTACHED TO AXES,
		// SIMULATE MIN/MAX VALUES
		if (oMin == null && oMax == null) {
			if (iType == DATE_TIME) {
				int year = Calendar.getInstance().get(Calendar.YEAR);
				oMin = new CDateTime(year, 1, 1);
				oMax = new CDateTime(year, 12, 31);
			} else if ((iType & NUMERICAL) == NUMERICAL) {
				if ((iType & PERCENT) == PERCENT) {
					oMin = new Double(0);
					oMax = new Double(100);
				} else if ((iType & LOGARITHMIC) == LOGARITHMIC) {
					oMin = new Double(1);
					oMax = new Double(1000);
				} else {
					oMin = new Double(-1);
					oMax = new Double(1);
				}
			}
		}

		if (iType == DATE_TIME) {
			try {
				CDateTime dtMin = asDateTime(oMin);
				CDateTime dtMax = asDateTime(oMax);

				return new Calendar[] { dtMin, dtMax };
			} catch (ClassCastException ex) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
						"exception.numerical.data.datetime.axis", //$NON-NLS-1$
						Messages.getResourceBundle(rtc.getULocale()));

			}
		} else if ((iType & NUMERICAL) == NUMERICAL) {
			try {
				return adjustMinMax(oMin, oMax);
			} catch (ClassCastException ex) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
						"exception.datetime.data.numerical.axis", //$NON-NLS-1$
						Messages.getResourceBundle(rtc.getULocale()));
			}
		}
		return null;
	}

	private Object adjustMinMax(Object oMin, Object oMax) {
		if (NumberUtil.isBigNumber(oMin) || NumberUtil.isBigNumber(oMax)) {
			if (((BigNumber) oMin).compareTo(oMax) == 0) {
				if (((BigNumber) oMin).getValue().compareTo(BigDecimal.ZERO) > 0) {
					oMin = new BigNumber(BigDecimal.ZERO, ((BigNumber) oMin).getDivisor());
				}
				if (((BigNumber) oMax).getValue().compareTo(BigDecimal.ZERO) < 0) {
					oMax = new BigNumber(BigDecimal.ZERO, ((BigNumber) oMax).getDivisor());
				}
			}
			return new BigNumber[] { (BigNumber) oMin, (BigNumber) oMax };
		} else {
			double dMin = asDouble(oMin).doubleValue();
			double dMax = asDouble(oMax).doubleValue();

			if (dMin == dMax) {
				if (dMin > 0) {
					dMin = 0;
				}
				if (dMax < 0) {
					dMax = 0;
				}
			}

			return new double[] { dMin, dMax };
		}
	}

	/**
	 * Computes while current is study layout.
	 *
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	private void computeWithStudyLayout() throws ChartException, IllegalArgumentException {
		ChartWithAxes cwa = getModel();
		// 1. Computes properties and scale of primary base axis.
		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];
		updatePrimaryBaseAxis(axPrimaryBase, boPlot);

		// 2. Computes properties and scale of orthogonal-axes
		ValueAxesHelper valueAxesHelper = new ValueAxesHelper(this, aax, boPlot);
		updateValueAxes(valueAxesHelper, cwa);

		// 3. Adjusts axes position due to scale, start/end labels.
		new AxesAdjuster(this, valueAxesHelper, boPlot).adjust();

		// 4. Updates plot bounds
		computePlotBackground(aax.getPrimaryBase(), valueAxesHelper.getValueAxes());
		boPlotWithMargin = boPlotBackground.copyInstance();
	}

	/**
	 * @param bo
	 * @param cwa
	 */
	private void initInnerFields(Bounds bo, ChartWithAxes cwa) {
		boPlot = goFactory.scaleBounds(bo, dPointToPixel); // CONVERSION
		dSeriesThickness = (ids.getDpiResolution() / 72d) * cwa.getSeriesThickness();

		// Fix Bugzilla#219292 render 2d+ only if plot is visible
		dSeriesThickness = cwa.getPlot().getClientArea().isVisible() ? dSeriesThickness : 0;

		// MAINTAIN IN LOCAL VARIABLES FOR PERFORMANCE/CONVENIENCE
		boPlot.adjust(insCA);
		double dY = boPlot.getTop();
		double dW = boPlot.getWidth();
		double dH = boPlot.getHeight();

		iDimension = getDimension(cwa.getDimension());
		dXAxisPlotSpacing = cwa.getPlot().getHorizontalSpacing() * dPointToPixel; // CONVERSION
		dYAxisPlotSpacing = cwa.getPlot().getVerticalSpacing() * dPointToPixel; // CONVERSION

		if (iDimension == TWO_5_D) {
			dY += dSeriesThickness;
			dH -= dSeriesThickness;
			dW -= dSeriesThickness;
		}

		boPlot.setTop(dY);
		boPlot.setHeight(Math.max(dH, 0));
		boPlot.setWidth(Math.max(dW, 0));
	}

	/**
	 * @param valueAxesHelper
	 * @param cwa
	 * @throws ChartException
	 */
	private void updateValueAxes(ValueAxesHelper valueAxesHelper, ChartWithAxes cwa) throws ChartException {
		OneAxis[] allYAxes = valueAxesHelper.getValueAxes();
		int i = 0;
		for (OneAxis axis : allYAxes) {
			updateValueAxis(axis, valueAxesHelper.getStart(i), valueAxesHelper.getEnd(i), cwa.isReverseCategory());
			i++;
		}
	}

	/**
	 * This method computes the entire chart within the given bounds. If the dataset
	 * has changed but none of the axis attributes have changed, simply re-compute
	 * without 'rebuilding axes'.
	 *
	 * @param bo
	 *
	 */
	@Override
	public void compute(Bounds bo) throws ChartException, IllegalArgumentException {
		ChartWithAxes cwa = getModel();
		initInnerFields(bo, cwa);

		// If plot bounds is less than zero, Do not compute.
		if (boPlot.getWidth() < 0 || boPlot.getHeight() < 0) {
			return;
		}

		if (ChartUtil.isStudyLayout(getModel())) {
			computeWithStudyLayout();
		} else {
			computeCommon();
		}
	}

	/**
	 * @param axPrimaryBase
	 * @param boPlot
	 * @throws ChartException
	 */
	private void updatePrimaryBaseAxis(final Axis axPrimaryBase, Bounds boPlot) throws ChartException {
		double dX = boPlot.getLeft();
		double dY = boPlot.getTop();
		double dW = boPlot.getWidth();
		double dH = boPlot.getHeight();

		// COMPUTE PRIMARY-BASE-AXIS PROPERTIES AND ITS SCALE
		// 3. Compute primary-base-axis properties and its scale
		OneAxis oaxPrimaryBase = aax.getPrimaryBase();
		int iAxisType = getAxisType(axPrimaryBase);

		Object oaData = null;
		if (iAxisType == TEXT || oaxPrimaryBase.isCategoryScale()) {
			oaData = getTypedDataSet(axPrimaryBase, iAxisType, 0);
		} else if ((iAxisType & NUMERICAL) == NUMERICAL) {
			oaData = getMinMax(axPrimaryBase, iAxisType);
		} else if ((iAxisType & DATE_TIME) == DATE_TIME) {
			oaData = getMinMax(axPrimaryBase, iAxisType);
		}

		DataSetIterator dsi = (oaData instanceof DataSetIterator) ? (DataSetIterator) oaData
				: new DataSetIterator(oaData, iAxisType);
		double dStart, dEnd;
		dStart = (aax.areAxesSwapped()) ? dY + dH : dX;
		dEnd = (aax.areAxesSwapped()) ? dY : dStart + dW;

		updateAxisScale(oaxPrimaryBase, iAxisType, dsi, dStart, dEnd);
	}

	/**
	 * Computes for non-study-layout case.
	 *
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	private void computeCommon() throws ChartException, IllegalArgumentException {
		ChartWithAxes cwa = getModel();
		double dX = boPlot.getLeft();
		double dY = boPlot.getTop();
		double dW = boPlot.getWidth();
		double dH = boPlot.getHeight();

		// 1. Update scales of overlay axes.
		// Place overlays first to reduce virtual plot bounds.
		if (aax.getOverlayCount() > 0) {
			if (aax.areAxesSwapped()) // ORTHOGONAL OVERLAYS = HORIZONTAL
			{
				updateOverlayScales(aax, dX, dX + dW, dY, dH);
				dY = aax.getStart();
				dH = aax.getLength();
			} else
			// ORTHOGONAL OVERLAYS = VERTICAL
			{
				updateOverlayScales(aax, dY - dH, dY, dX, dW);
				dX = aax.getStart();
				dW = aax.getLength();
			}
		}

		double dStart, dEnd;
		final Axis[] axa = cwa.getPrimaryBaseAxes();
		final Axis axPrimaryBase = axa[0];
		final Axis axPrimaryOrthogonal = cwa.getPrimaryOrthogonalAxis(axPrimaryBase);

		// 2. Compute primary-base-axis properties and its scale
		AutoScale scPrimaryBase;
		OneAxis oaxPrimaryBase = aax.getPrimaryBase();
		int iAxisType = getAxisType(axPrimaryBase);

		Object oaData = null;
		if (iAxisType == TEXT || oaxPrimaryBase.isCategoryScale()) {
			oaData = getTypedDataSet(axPrimaryBase, iAxisType, 0);
		} else if ((iAxisType & NUMERICAL) == NUMERICAL) {
			oaData = getMinMax(axPrimaryBase, iAxisType);
		} else if ((iAxisType & DATE_TIME) == DATE_TIME) {
			oaData = getMinMax(axPrimaryBase, iAxisType);
		}

		DataSetIterator dsi = (oaData instanceof DataSetIterator) ? (DataSetIterator) oaData
				: new DataSetIterator(oaData, iAxisType);
		oaData = null;

		dStart = (aax.areAxesSwapped()) ? dY + dH : dX;
		dEnd = (aax.areAxesSwapped()) ? dY : dStart + dW;

		int iDirection = AUTO;

		scPrimaryBase = AutoScale.computeScale(ids, oaxPrimaryBase, dsi, iAxisType, dStart, dEnd, rtc, iDirection, 1,
				iMarginPercent, this);

		// Update scale on primary-base axis
		oaxPrimaryBase.set(scPrimaryBase);

		// 3. Compute primary-orthogonal-axis properties and its scale
		AutoScale scPrimaryOrthogonal;
		OneAxis oaxPrimaryOrthogonal = aax.getPrimaryOrthogonal();
		iAxisType = getAxisType(axPrimaryOrthogonal);
		oaData = null;
		if ((iAxisType & NUMERICAL) == NUMERICAL || (iAxisType & DATE_TIME) == DATE_TIME) {
			Object minMax = getMinMax(axPrimaryOrthogonal, iAxisType);
			if (rtc.getSharedScale() != null) {
				dsi = getSharedScaleMinMax(iAxisType, minMax);
			} else {
				dsi = new DataSetIterator(minMax, iAxisType);
			}
			// Reverse the series categories if needed.
			dsi.reverse(cwa.isReverseCategory());
		} else {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_FORMAT,
					"exception.orthogonal.axis.numerical.datetime", //$NON-NLS-1$
					Messages.getResourceBundle(rtc.getULocale()));
		}

		dStart = (aax.areAxesSwapped()) ? dX : dY + dH;
		dEnd = (aax.areAxesSwapped()) ? dX + dW : dY;
		scPrimaryOrthogonal = AutoScale.computeScale(ids, oaxPrimaryOrthogonal, dsi, iAxisType, dStart, dEnd,
				oaxPrimaryBase.getModelAxis().getOrigin(), rtc, AUTO, 1, iMarginPercent, this);

		// Update scale on primary-orthogonal axis
		oaxPrimaryOrthogonal.set(scPrimaryOrthogonal);

		// 4. adjust axis position due to scale, start/end labels.
		// 4.1 Iteratively adjust the primary orthogonal axis position due to the scale,
		// start/end lables.
		double dYAxisLocation = adjustHorizontal(dX, dW, aax, aax.getPrimaryOrthogonal());

		// 4.2 Iteratively adjust the primary base axis position due to the scale,
		// start/end labels.
		double dXAxisLocation = adjustVerticalDueToHorizontal(dY, dH, aax, aax.getPrimaryOrthogonal());

		// 5. Set dataset
		// Setup the full dataset for the primary orthogonal axis.
		iAxisType = getAxisType(axPrimaryOrthogonal);
		oaData = getTypedDataSet(axPrimaryOrthogonal, iAxisType, 0);
		scPrimaryOrthogonal.setData(dsi);

		// Setup the full dataset for the primary orthogonal aixs
		iAxisType = getAxisType(axPrimaryBase);
		if (iAxisType != IConstants.TEXT) {
			scPrimaryBase.setData(getTypedDataSet(axPrimaryBase, iAxisType, 0));
		}

		scPrimaryBase.resetShifts();
		scPrimaryOrthogonal.resetShifts();

		// 6.
		// Update the sizes of the overlay axes.
		updateOverlayAxes(aax);
		// #9026, pass the bounds which takes the overlay axes into accounts
		growBaseAxis(aax, goFactory.createBounds(dX, dY, dW, dH));

		adjustOverlayAxesDueToEndShifts();

		// Update for overlays
		final OneAxis axPH = aax.areAxesSwapped() ? aax.getPrimaryOrthogonal() : aax.getPrimaryBase();
		final OneAxis axPV = aax.areAxesSwapped() ? aax.getPrimaryBase() : aax.getPrimaryOrthogonal();

		axPH.setAxisCoordinate(dXAxisLocation);
		axPV.setAxisCoordinate(dYAxisLocation);

		// 7. Update plot bounds.
		computePlotBackground(aax.getPrimaryBase(), new OneAxis[] { aax.getPrimaryOrthogonal() });
		computePlotWithMargin(axPH, axPV);
	}

	private void adjustOverlayAxesDueToEndShifts() {
		if (aax != null && aax.getOverlayCount() > 0 && !aax.getPrimaryBase().isCategoryScale()) {
			int iCount = aax.getOverlayCount();
			AutoScale scBase = aax.getPrimaryBase().getScale();

			for (int i = 0; i < iCount; i++) {
				OneAxis oax = aax.getOverlay(i);
				if (oax.getIntersectionValue() == IntersectionValue.MAX_VALUE) {
					oax.setAxisCoordinate(oax.getAxisCoordinate() - scBase.getEndShift());
				}
			}
		}
	}

	/**
	 * @param axPH
	 * @param axPV
	 */
	private void computePlotWithMargin(final OneAxis axPH, final OneAxis axPV) {
		double[] daX = axPH.getScale().getEndPoints();

		boPlotWithMargin = goFactory.copyOf(boPlotBackground);
		if (iMarginPercent > 0) {
			// TODO do we need to add margin support for datetime scale?
			AutoScale scale = axPH.getScale();
			if (scale.getMaxWithMargin() != null || scale.getMinWithMargin() != null) {
				if ((scale.getType() & LINEAR) == LINEAR) {
					double factor = Math.abs(daX[1] - daX[0])
							/ (asDouble(scale.getMaximum()).doubleValue() - asDouble(scale.getMinimum()).doubleValue());
					if (scale.getMinWithMargin() != null) {
						boPlotWithMargin.setLeft(
								boPlotWithMargin.getLeft() - factor * (asDouble(scale.getMinimum()).doubleValue()
										- asDouble(scale.getMinWithMargin()).doubleValue()));
					}
					boPlotWithMargin.setWidth(factor * (asDouble(
							scale.getMaxWithMargin() == null ? scale.getMaximum() : scale.getMaxWithMargin())
									.doubleValue()
							- asDouble(scale.getMinWithMargin() == null ? scale.getMinimum() : scale.getMinWithMargin())
									.doubleValue())
							+ insCA.getLeft() + insCA.getRight() + 1);
				}
			}

			scale = axPV.getScale();
			if (scale.getMaxWithMargin() != null || scale.getMinWithMargin() != null) {
				if ((scale.getType() & LINEAR) == LINEAR) {
					double factor = Math.abs(daX[1] - daX[0])
							/ (asDouble(scale.getMaximum()).doubleValue() - asDouble(scale.getMinimum()).doubleValue());
					if (scale.getMaxWithMargin() != null) {
						boPlotWithMargin.setTop(
								boPlotWithMargin.getTop() - factor * (asDouble(scale.getMaxWithMargin()).doubleValue()
										- asDouble(scale.getMaximum()).doubleValue()));
					}
					boPlotWithMargin.setHeight(factor * (asDouble(
							scale.getMaxWithMargin() == null ? scale.getMaximum() : scale.getMaxWithMargin())
									.doubleValue()
							- asDouble(scale.getMinWithMargin() == null ? scale.getMinimum() : scale.getMinWithMargin())
									.doubleValue())
							+ insCA.getTop() + insCA.getBottom() + 1);
				}
			}
		}
	}

	/**
	 * Compute plot background by related <code>OneAxis</code>.
	 *
	 * @param axPrimaryBase the primary base OneAxis.
	 * @param axOrthogonals The array of related orthogonal OneAxis[s] to used to
	 *                      compute plot bounds.
	 */
	private void computePlotBackground(final OneAxis axPrimaryBase, final OneAxis[] axOrthogonals) {
		double yAxesLength;
		double left, top, width, height;
		if (!aax.areAxesSwapped()) {
			int size = axOrthogonals.length;
			if (size > 1) {
				yAxesLength = axOrthogonals[0].getScale().getEndPoints()[0]
						- axOrthogonals[size - 1].getScale().getEndPoints()[1];
			} else {
				yAxesLength = axOrthogonals[0].getScale().getEndPoints()[0]
						- axOrthogonals[0].getScale().getEndPoints()[1];
			}

			// Get left and width.
			double[] point = axPrimaryBase.getScale().getEndPoints();
			if (axPrimaryBase.getScale().getDirection() != BACKWARD) {
				left = point[0] - insCA.getLeft();
				width = point[1] - point[0] + insCA.getLeft() + insCA.getRight() + 1;
			} else {
				left = point[1] - insCA.getLeft();
				width = point[0] - point[1] + insCA.getLeft() + insCA.getRight() + 1;
			}

			// Get top and height.
			if (axOrthogonals[0].getScale().getDirection() == FORWARD) {
				top = axOrthogonals[axOrthogonals.length - 1].getScale().getEndPoints()[0] - insCA.getTop();
				height = -yAxesLength + insCA.getTop() + insCA.getBottom() + 1;
			} else {
				top = axOrthogonals[axOrthogonals.length - 1].getScale().getEndPoints()[1] - insCA.getTop();
				height = yAxesLength + insCA.getTop() + insCA.getBottom() + 1;
			}

		} else {
			int size = axOrthogonals.length;
			if (size > 1) {
				yAxesLength = axOrthogonals[size - 1].getScale().getEndPoints()[1]
						- axOrthogonals[0].getScale().getEndPoints()[0];
			} else {
				yAxesLength = axOrthogonals[0].getScale().getEndPoints()[1]
						- axOrthogonals[0].getScale().getEndPoints()[0];
			}

			// Get left and width.
			if (axOrthogonals[0].getScale().getDirection() != BACKWARD) {
				left = axOrthogonals[0].getScale().getEndPoints()[0] - insCA.getLeft();
				width = yAxesLength + insCA.getLeft() + insCA.getRight() + 1;
			} else {
				left = axOrthogonals[axOrthogonals.length - 1].getScale().getEndPoints()[0] - insCA.getLeft();
				width = -yAxesLength + insCA.getLeft() + insCA.getRight() + 1;
			}

			// Get top and height.
			if (axPrimaryBase.getScale().getDirection() == FORWARD) {
				top = axPrimaryBase.getScale().getEndPoints()[0] - insCA.getTop();
				height = axPrimaryBase.getScale().getEndPoints()[1] - axPrimaryBase.getScale().getEndPoints()[0]
						+ insCA.getTop() + insCA.getBottom() + 1;
			} else {
				top = axPrimaryBase.getScale().getEndPoints()[1] - insCA.getTop();
				height = axPrimaryBase.getScale().getEndPoints()[0] - axPrimaryBase.getScale().getEndPoints()[1]
						+ insCA.getTop() + insCA.getBottom() + 1;
			}
		}

		boPlotBackground.setLeft(left);
		boPlotBackground.setTop(top);
		boPlotBackground.setWidth(width);
		boPlotBackground.setHeight(height);
		if (iDimension == TWO_5_D) {
			boPlotBackground.delta(dSeriesThickness, -dSeriesThickness, 0, 0);
		}
	}

	/**
	 * Returns the plot bounds with margin area. Only valid when margin percent is
	 * set, otherwise will return plot bounds.
	 *
	 * @return The plot bounds with margin area
	 */
	public Bounds getPlotBoundsWithMargin() {
		return boPlotWithMargin;
	}

	/**
	 * This method attempts to stretch the base axis so it fits snugly (w.r.t.
	 * horizontal/vertical spacing) with the overlay axes (if any)
	 *
	 * @param aax
	 */
	private void growBaseAxis(AllAxes aax, Bounds bo) throws ChartException {
		OneAxis oaxBase = aax.getPrimaryBase();
		OneAxis oaxOrthogonal = aax.getPrimaryOrthogonal();
		AutoScale scBase = oaxBase.getScale();

		if (aax.getOverlayCount() <= 0) {
			// no overlay, just return.
			return;
		}

		if (!aax.areAxesSwapped()) // STANDARD ORIENTATION
		{
			// IF PRIMARY ORTHOGONAL AXIS IS NOT ON THE RIGHT
			// If primary orthogonal axis is not on the right.
			if (oaxOrthogonal.getIntersectionValue().getType() != IConstants.MAX) {
				// IF ANY OVERLAY ORTHOGONAL AXES ARE ON THE RIGHT
				// If any overlay orthogonal arex are on the right.
				if (aax.anyOverlayPositionedAt(IConstants.MAX)) {
					scBase.computeAxisStartEndShifts(ids, oaxBase.getLabel(), HORIZONTAL, oaxBase.getLabelPosition(),
							aax);
					{
						double dRightThreshold = bo.getLeft() + bo.getWidth();
						double dEnd = scBase.getEnd();
						final double dEndShift = scBase.getEndShift();
						if (dEnd + dEndShift < dRightThreshold) {
							dEnd += dEndShift;
							scBase.computeTicks(ids, oaxBase.getLabel(), oaxBase.getLabelPosition(), HORIZONTAL,
									scBase.getStart(), dEnd, false, null);
						}
					}
				}
			}
			// IF PRIMARY ORTHOGONAL AXIS IS NOT ON THE LEFT
			// If primary orthogonal axis is not on the left.
			else if (oaxOrthogonal.getIntersectionValue().getType() != IConstants.MIN) {
				// IF ANY OVERLAY ORTHOGONAL AXES ARE ON THE LEFT
				// If any overlay orthogonal axes are ont he left.
				if (aax.anyOverlayPositionedAt(IConstants.MIN)) {
					scBase.computeAxisStartEndShifts(ids, oaxBase.getLabel(), HORIZONTAL, oaxBase.getLabelPosition(),
							aax);
					{
						double dLeftThreshold = bo.getLeft();
						double dStart = scBase.getStart();
						final double dEndShift = scBase.getEndShift();
						final double dStartShift = scBase.getStartShift();
						if (dStart - dStartShift > dLeftThreshold) {
							dStart -= dStartShift;
							final double dEnd = scBase.getEnd() + dEndShift;
							scBase.computeTicks(ids, oaxBase.getLabel(), oaxBase.getLabelPosition(), HORIZONTAL, dStart,
									dEnd, false, null);
						}
					}
				}
			}
		} else // IF PRIMARY ORTHOGONAL AXIS IS NOT AT THE TOP
		// If primary orthogonal axis is not at the top.
		if (oaxOrthogonal.getIntersectionValue().getType() != IConstants.MAX) {
			// IF ANY OVERLAY ORTHOGONAL AXES ARE AT THE TOP
			if (aax.anyOverlayPositionedAt(IConstants.MAX)) {
				scBase.computeAxisStartEndShifts(ids, oaxBase.getLabel(), VERTICAL, oaxBase.getLabelPosition(), aax);
				{
					double dTopThreshold = bo.getTop();
					double dEnd = scBase.getEnd();
					final double dEndShift = Math.floor(scBase.getEndShift());
					if (dEnd - dEndShift > dTopThreshold) {
						dEnd = dEnd - dEndShift;
						final double dStart = scBase.getStart();
						scBase.computeTicks(ids, oaxBase.getLabel(), oaxBase.getLabelPosition(), VERTICAL, dStart, dEnd,
								false, null);
					}
				}
			}
		}

		// IF PRIMARY ORTHOGONAL AXIS IS NOT AT THE BOTTOM
		// If primary orthogonal axis is not at the bottom.
		else if (oaxOrthogonal.getIntersectionValue().getType() != IConstants.MIN) {
			// IF ANY OVERLAY ORTHOGONAL AXES IS AT THE BOTTOM
			if (aax.anyOverlayPositionedAt(IConstants.MIN)) {
				scBase.computeAxisStartEndShifts(ids, oaxBase.getLabel(), VERTICAL, oaxBase.getLabelPosition(), aax);
				{
					double dBottomThreshold = bo.getTop() + bo.getHeight();
					double dStart = scBase.getStart();
					final double dStartShift = scBase.getStartShift();
					if (dStart + dStartShift < dBottomThreshold) {
						dStart += dStartShift;
						final double dEnd = scBase.getEnd();
						scBase.computeTicks(ids, oaxBase.getLabel(), oaxBase.getLabelPosition(), VERTICAL, dStart, dEnd,
								false, null);
					}
				}
			}
		}
	}

	/**
	 *
	 * @param aax
	 * @param dAxisStart
	 * @param dAxisEnd
	 * @param dBlockStart
	 * @param dBlockLength
	 *
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	private void updateOverlayScales(AllAxes aax, double dAxisStart, double dAxisEnd, double dBlockStart,
			double dBlockLength) throws ChartException, IllegalArgumentException {
		final Axis[] axa = ((ChartWithAxesImpl) getModel()).getPrimaryBaseAxes();
		final Axis axPrimaryBase = axa[0];
		final Axis[] axaOrthogonal = ((ChartWithAxesImpl) getModel()).getOrthogonalAxes(axPrimaryBase, false);

		IntersectionValue iv;
		AutoScale sc = null;
		OneAxis oaxOverlay = null;
		int iTickStyle, iAxisType, j, iTitleLocation;
		int iOverlayCount = aax.getOverlayCount();
		int iOrientation = aax.getOrientation();
		double dStart, dEnd, dAxisLabelsThickness;
		Label laAxisTitle;

		Series[] sea = getModel().getSeries(IConstants.ORTHOGONAL);
		Map<Series, LegendItemRenderingHints> seriesRenderingHints = rtc.getSeriesRenderers();

		// ITERATE THROUGH EACH OVERLAY ORTHOGONAL AXIS
		// Iterate through each overlay orthogonal axis.
		for (int i = 0; i < iOverlayCount; i++) {
			// GO BACKWARDS TO ENSURE CORRECT RENDERING ORDER
			j = iOverlayCount - i - 1;
			// UPDATE A PREVIOUSLY DEFINED OVERLAY AXIS AUTO COMPUTE SCALE
			oaxOverlay = aax.getOverlay(j);
			iTickStyle = oaxOverlay.getCombinedTickStyle();
			iTitleLocation = oaxOverlay.getTitlePosition();
			laAxisTitle = oaxOverlay.getTitle();
			iAxisType = getAxisType(axaOrthogonal[j]);

			sc = AutoScale.computeScale(ids, oaxOverlay,
					new DataSetIterator(getMinMax(axaOrthogonal[j], iAxisType), iAxisType), iAxisType, dAxisStart,
					dAxisEnd, rtc, AUTO, 1, iMarginPercent, this);

			oaxOverlay.set(sc);
			iv = oaxOverlay.getIntersectionValue();

			// UPDATE AXIS ENDPOINTS DUE TO ITS AXIS LABEL SHIFTS
			dStart = sc.getStart();
			dEnd = sc.getEnd();
			sc.computeTicks(ids, oaxOverlay.getLabel(), oaxOverlay.getLabelPosition(), iOrientation, dStart, dEnd, true,
					null);
			if (!sc.isStepFixed()) {
				final Object[] oaMinMax = sc.getMinMax();
				while (!sc.checkFit(ids, oaxOverlay.getLabel(), oaxOverlay.getLabelPosition())) {
					if (!sc.zoomOut()) {
						break;
					}
					sc.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
					sc.computeTicks(ids, oaxOverlay.getLabel(), oaxOverlay.getLabelPosition(), iOrientation, dStart,
							dEnd, true, null);
				}
			}
			dAxisLabelsThickness = sc.computeAxisLabelThickness(ids, oaxOverlay.getLabel(), iOrientation);

			// Compute axes decoration thickness, the value sequence is either
			// [left,right] or
			// [top, bottom]
			double[] dDecorationThickness = { 0, 0 };

			for (int t = 0; t < sea.length; t++) {
				LegendItemRenderingHints lirh = seriesRenderingHints.get(sea[t]);

				if (lirh != null && lirh.getRenderer() instanceof AxesRenderer) {
					IAxesDecorator iad = ((AxesRenderer) lirh.getRenderer()).getAxesDecorator(oaxOverlay);

					if (iad != null) {
						double[] thickness = iad.computeDecorationThickness(ids, oaxOverlay);

						if (thickness[0] > dDecorationThickness[0]) {
							dDecorationThickness[0] = thickness[0];
						}
						if (thickness[1] > dDecorationThickness[1]) {
							dDecorationThickness[1] = thickness[1];
						}
					}
				}
			}

			double dAxisTitleThickness = 0;
			sc.resetShifts();

			if (iOrientation == VERTICAL) {
				// COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS
				// AND AXIS-PLOT SPACING
				double dX = 0, dX1 = 0, dX2 = 0;
				final boolean bTicksLeft = (iTickStyle & TICK_LEFT) == TICK_LEFT;
				final boolean bTicksRight = (iTickStyle & TICK_RIGHT) == TICK_RIGHT;
				final double dAppliedYAxisPlotSpacing = dYAxisPlotSpacing;
				if (laAxisTitle.isVisible()) {
					final String sPreviousValue = laAxisTitle.getCaption().getValue();
					laAxisTitle.getCaption().setValue(rtc.externalizedMessage(sPreviousValue));

					double maxHeight = boPlot.getHeight();
					double maxWidth = boPlot.getWidth() * AXIS_TITLE_PERCENT;
					LabelLimiter lbLimit = new LabelLimiter(maxWidth, maxHeight, 0);
					lbLimit.computeWrapping(ids, laAxisTitle);
					lbLimit = lbLimit.limitLabelSize(cComp, ids, laAxisTitle,
							EnumSet.of(LabelLimiter.Option.FIX_HEIGHT));
					dAxisTitleThickness = lbLimit.getMaxWidth();
					putLabelLimiter(oaxOverlay.getModelAxis().getTitle(), lbLimit);

					laAxisTitle.getCaption().setValue(sPreviousValue);
				}

				// handle fixed label thickness #177744
				if (oaxOverlay.getModelAxis().isSetLabelSpan()) {
					double dFixedLabelThickness = oaxOverlay.getModelAxis().getLabelSpan() * dPointToPixel;

					// if the fixed label thickness is to greate, it will not
					// take affect.
					double dWTotal = dBlockLength;
					if (dFixedLabelThickness < dWTotal - 2 * getTickSize()) {
						if (dAxisLabelsThickness + dAxisTitleThickness > dFixedLabelThickness) {
							oaxOverlay.setShowLabels(false);
						}
						if (dAxisTitleThickness > dFixedLabelThickness) {
							laAxisTitle.setVisible(false);
							dAxisTitleThickness = 0;
						}
						dAxisLabelsThickness = dFixedLabelThickness;
					}
				}

				// COMPUTE VALUES FOR x1, x, x2
				// x = HORIZONTAL LOCATION OF Y-AXIS ALONG PLOT
				// x1 = LEFT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TICKS,
				// SPACING)
				// x2 = RIGHT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TICKS,
				// SPACING)
				if (iv.getType() == IConstants.MIN) {
					// NOTE: ENSURE CODE SYMMETRY WITH 'iaLabelPositions[i] ==
					// RIGHT'
					dX = dBlockStart;
					dX -= dAppliedYAxisPlotSpacing;
					dX1 = dX;
					dX2 = dX;
					if (bTicksLeft) {
						dX1 -= getTickSize();
					}
					if (oaxOverlay.getLabelPosition() == LEFT) {
						dX1 -= Math.max(dAxisLabelsThickness, dDecorationThickness[0]);
						dX2 += Math.max(bTicksRight ? getTickSize() : 0, dAppliedYAxisPlotSpacing);
					} else if (oaxOverlay.getLabelPosition() == RIGHT) {
						dX1 -= dDecorationThickness[0];
						dX2 += Math.max((bTicksRight ? getTickSize() : 0) + dAxisLabelsThickness,
								dAppliedYAxisPlotSpacing);
					}

					if (iTitleLocation == LEFT) {
						dX1 -= dAxisTitleThickness;
					} else if (iTitleLocation == RIGHT) {
						dX2 += dAxisTitleThickness;
					}

					// ENSURE THAT WE DON'T GO BEHIND THE LEFT PLOT BLOCK EDGE
					if (dX1 < dBlockStart) {
						final double dDelta = (dBlockStart - dX1);
						dX1 = dBlockStart;
						dX += dDelta;
						dX2 += dDelta;
					}
					dBlockStart += (dX2 - dX1); // SHIFT LEFT EDGE >>
				} else if (iv.getType() == IConstants.MAX) {
					// NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'
					dX = dBlockStart + dBlockLength;
					dX += dAppliedYAxisPlotSpacing;
					dX1 = dX;
					dX2 = dX;
					if (bTicksRight) {
						dX2 += getTickSize();
					}

					if (oaxOverlay.getLabelPosition() == RIGHT) {
						dX2 += Math.max(dAxisLabelsThickness, dDecorationThickness[1]);
						dX1 -= Math.max(bTicksLeft ? getTickSize() : 0, dAppliedYAxisPlotSpacing);
					} else if (oaxOverlay.getLabelPosition() == LEFT) {
						dX1 -= Math.max((bTicksLeft ? getTickSize() : 0) + dAxisLabelsThickness,
								dAppliedYAxisPlotSpacing);
						dX2 += dDecorationThickness[1];
					}

					if (iTitleLocation == LEFT) {
						dX1 -= dAxisTitleThickness;
					} else if (iTitleLocation == RIGHT) {
						dX2 += dAxisTitleThickness;
					}

					// ENSURE THAT WE DON'T GO AHEAD OF THE RIGHT PLOT BLOCK
					// EDGE
					if (dX2 > dBlockStart + dBlockLength) {
						final double dDelta = dX2 - (dBlockStart + dBlockLength);
						dX2 = dBlockStart + dBlockLength;
						dX -= dDelta;
						dX1 -= dDelta;
					}
				} else {
					if (oaxOverlay.getLabelPosition() == RIGHT) {
						dX2 += Math.max(dAxisLabelsThickness, dDecorationThickness[1]);
						dX1 -= Math.max(bTicksLeft ? getTickSize() : 0, dAppliedYAxisPlotSpacing);
					} else if (oaxOverlay.getLabelPosition() == LEFT) {
						dX1 -= Math.max((bTicksLeft ? getTickSize() : 0) + dAxisLabelsThickness,
								dAppliedYAxisPlotSpacing);
						dX2 += dDecorationThickness[1];
					}

					if (iTitleLocation == LEFT) {
						dX1 -= dAxisTitleThickness;
					} else if (iTitleLocation == RIGHT) {
						dX2 += dAxisTitleThickness;
					}
				}

				dBlockLength -= dX2 - dX1; // SHIFT RIGHT EDGE <<

				double dDelta = 0;
				if (iv.getType() == IConstants.MIN) {
					dDelta = -insCA.getLeft();
				} else if (iv.getType() == IConstants.MAX) {
					dDelta = insCA.getRight();
				}

				oaxOverlay.setAxisCoordinate(dX + dDelta);
				// dX1<=>dX<=>dX2 INCORPORATES TITLE
				oaxOverlay.setTitleCoordinate(
						(iTitleLocation == LEFT) ? dX1 + dDelta - 1 : dX2 + 1 - dAxisTitleThickness + dDelta);
			} else if (iOrientation == HORIZONTAL) {
				// COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS
				// AND AXIS-PLOT SPACING
				double dY = 0, dY1 = dY, dY2 = dY;
				final boolean bTicksAbove = (iTickStyle & TICK_ABOVE) == TICK_ABOVE;
				final boolean bTicksBelow = (iTickStyle & TICK_BELOW) == TICK_BELOW;
				final double dAppliedXAxisPlotSpacing = dXAxisPlotSpacing;
				if (laAxisTitle.isVisible()) {
					final String sPreviousValue = laAxisTitle.getCaption().getValue();
					laAxisTitle.getCaption().setValue(rtc.externalizedMessage(sPreviousValue));
					double maxHeight = boPlot.getHeight() * AXIS_TITLE_PERCENT;
					double maxWidth = boPlot.getWidth();

					// compute width of vertical axis title
					Label laAxisTitleV = aax.getPrimaryOrthogonal().getTitle();
					if (laAxisTitleV.isVisible()) {
						laAxisTitleV = goFactory.copyOf(laAxisTitleV);
						laAxisTitleV.getCaption()
								.setValue(rtc.externalizedMessage(laAxisTitleV.getCaption().getValue()));
						LabelLimiter lbLimitV = new LabelLimiter(boPlot.getWidth() * AXIS_TITLE_PERCENT,
								boPlot.getWidth(), 0);
						lbLimitV.computeWrapping(ids, laAxisTitleV);
						lbLimitV.limitLabelSize(cComp, ids, laAxisTitleV);
						maxWidth -= lbLimitV.getMaxWidth();
					}

					LabelLimiter lbLimit = new LabelLimiter(maxWidth, maxHeight, 0);
					lbLimit.computeWrapping(ids, laAxisTitle);
					lbLimit = lbLimit.limitLabelSize(cComp, ids, laAxisTitle,
							EnumSet.of(LabelLimiter.Option.FIX_WIDTH));
					dAxisTitleThickness = lbLimit.getMaxHeight();
					putLabelLimiter(oaxOverlay.getModelAxis().getTitle(), lbLimit);

					laAxisTitle.getCaption().setValue(sPreviousValue);
				}

				// handle fixed label thickness #177744
				if (oaxOverlay.getModelAxis().isSetLabelSpan()) {
					double dFixedLabelThickness = oaxOverlay.getModelAxis().getLabelSpan() * dPointToPixel;

					// if the fixed label thickness is to greate, it will not
					// take affect.
					double dWTotal = dBlockLength;
					if (dFixedLabelThickness < dWTotal - 2 * getTickSize()) {
						if (dAxisLabelsThickness + dAxisTitleThickness > dFixedLabelThickness) {
							oaxOverlay.setShowLabels(false);
						}
						if (dAxisTitleThickness > dFixedLabelThickness) {
							laAxisTitle.setVisible(false);
							dAxisTitleThickness = 0;
						}
						dAxisLabelsThickness = dFixedLabelThickness;
					}
				}

				// COMPUTE VALUES FOR y1, y, y2
				// y = VERTICAL LOCATION OF X-AXIS ALONG PLOT
				// y1 = UPPER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS,
				// SPACING)
				// y2 = LOWER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS,
				// SPACING)
				if (iv.getType() == IConstants.MAX) // ABOVE
				// THE
				// PLOT
				{
					dY = dBlockStart;
					dY -= dAppliedXAxisPlotSpacing;
					dY1 = dY;
					dY2 = dY;
					if (bTicksAbove) {
						dY1 -= getTickSize();
					}
					if (oaxOverlay.getLabelPosition() == ABOVE) {
						dY1 -= Math.max(dAxisLabelsThickness, dDecorationThickness[0]);
						dY2 += Math.max(bTicksBelow ? getTickSize() : 0, dAppliedXAxisPlotSpacing);
					} else if (oaxOverlay.getLabelPosition() == BELOW) {
						dY1 -= dDecorationThickness[0];
						dY2 += Math.max((bTicksBelow ? getTickSize() : 0) + dAxisLabelsThickness,
								dAppliedXAxisPlotSpacing);
					}

					if (iTitleLocation == ABOVE) {
						dY1 -= dAxisTitleThickness;
					} else if (iTitleLocation == BELOW) {
						dY2 += dAxisTitleThickness;
					}

					// ENSURE THAT WE DON'T GO BEHIND THE LEFT PLOT BLOCK EDGE
					if (dY1 < dBlockStart) {
						final double dDelta = (dBlockStart - dY1);
						dY1 = dBlockStart;
						dY += dDelta;
						dY2 += dDelta;
					}
					dBlockStart += (dY2 - dY1); // SHIFT TOP EDGE >>
				} else if (iv.getType() == IConstants.MIN)
				// BELOW THE PLOT
				{
					// NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'
					dY = dBlockStart + dBlockLength;
					dY += dAppliedXAxisPlotSpacing;
					dY1 = dY;
					dY2 = dY;
					if (bTicksBelow) {
						dY2 += getTickSize();
					}

					if (oaxOverlay.getLabelPosition() == BELOW) {
						dY2 += Math.max(dAxisLabelsThickness, dDecorationThickness[1]);
						dY1 -= Math.max(bTicksAbove ? getTickSize() : 0, dAppliedXAxisPlotSpacing);
					} else if (oaxOverlay.getLabelPosition() == ABOVE) {
						dY1 -= Math.max((bTicksAbove ? getTickSize() : 0) + dAxisLabelsThickness,
								dAppliedXAxisPlotSpacing);
						dY2 += dDecorationThickness[1];
					}

					if (iTitleLocation == ABOVE) {
						dY1 -= dAxisTitleThickness;
					} else if (iTitleLocation == BELOW) {
						dY2 += dAxisTitleThickness;
					}

					// ENSURE THAT WE DON'T GO AHEAD OF THE RIGHT PLOT BLOCK
					// EDGE
					if (dY2 > dBlockStart + dBlockLength) {
						final double dDelta = dY2 - (dBlockStart + dBlockLength);
						dY2 = dBlockStart + dBlockLength;
						dY -= dDelta;
						dY1 -= dDelta;
					}
				} else {
					if (oaxOverlay.getLabelPosition() == BELOW) {
						dY2 += Math.max(dAxisLabelsThickness, dDecorationThickness[1]);
						dY1 -= Math.max(bTicksAbove ? getTickSize() : 0, dAppliedXAxisPlotSpacing);
					} else if (oaxOverlay.getLabelPosition() == ABOVE) {
						dY1 -= Math.max((bTicksAbove ? getTickSize() : 0) + dAxisLabelsThickness,
								dAppliedXAxisPlotSpacing);
						dY2 += dDecorationThickness[1];
					}

					if (iTitleLocation == ABOVE) {
						dY1 -= dAxisTitleThickness;
					} else if (iTitleLocation == BELOW) {
						dY2 += dAxisTitleThickness;
					}
				}

				double dDelta = 0;
				if (iv.getType() == IConstants.MAX) {
					dDelta = -insCA.getTop();
				} else if (iv.getType() == IConstants.MIN) {
					dDelta = insCA.getBottom();
				}

				oaxOverlay.setAxisCoordinate(dY + dDelta);
				oaxOverlay.setTitleCoordinate(
						(iTitleLocation == ABOVE) ? dY1 + dDelta - 1 : dY2 + 1 - dAxisTitleThickness + dDelta // dY1<=>dX<=>dY2
				// INCORPORATES
				// TITLE
				);

				dBlockLength -= (dY2 - dY1); // SHIFT BOTTOM EDGE <<
			}
		}

		aax.setBlockCordinates(dBlockStart, dBlockLength);
	}

	private void updateOverlayAxes(AllAxes aax) throws ChartException, IllegalArgumentException {
		int iDirection = (aax.getOrientation() == HORIZONTAL) ? 1 : -1;
		final Axis[] axa = getModel().getPrimaryBaseAxes();
		final Axis axPrimaryBase = axa[0]; // NOTE: FOR REL 1 AXIS RENDERS, WE
		// SUPPORT A SINGLE PRIMARY BASE AXIS ONLY
		final Axis[] axaOverlayOrthogonal = getModel().getOrthogonalAxes(axPrimaryBase, false);

		OneAxis axOverlay, axPrimary = aax.getPrimaryOrthogonal();
		AutoScale scOA, sc = axPrimary.getScale();
		double dStart, dEnd;
		Object[] oaMinMax;

		Label la;
		for (int i = 0; i < aax.getOverlayCount(); i++) {
			axOverlay = aax.getOverlay(i);
			int iAxisType = axOverlay.getScale().getType();

			la = axOverlay.getLabel();
			scOA = axOverlay.getScale();
			scOA.setEndPoints(scOA.getStart() - scOA.getStartShift() * iDirection,
					scOA.getEnd() + scOA.getEndShift() * iDirection);

			dStart = sc.getStart();
			dEnd = sc.getEnd();

			scOA.setEndPoints(dStart, dEnd);
			scOA.computeTicks(ids, la, axOverlay.getLabelPosition(), aax.getOrientation(), dStart, dEnd, false, null);
			if (!scOA.isStepFixed()) {
				oaMinMax = scOA.getMinMax();
				while (!scOA.checkFit(ids, la, axOverlay.getLabelPosition())) {
					if (!scOA.zoomOut()) {
						break;
					}
					scOA.updateAxisMinMax(oaMinMax[0], oaMinMax[1]);
					scOA.computeTicks(ids, la, axOverlay.getLabelPosition(), aax.getOrientation(), dStart, dEnd, false,
							null);
				}
			}

			// SETUP THE FULL DATASET FOR THE PRIMARY ORTHOGONAL AXIS
			scOA.setData(getTypedDataSet(axaOverlayOrthogonal[i], iAxisType, 0));

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.computation.withaxes.PlotWithAxes#
	 * getSeriesRenderingHints(org.eclipse.birt.chart.model.data.SeriesDefinition,
	 * org.eclipse.birt.chart.model.component.Series)
	 */
	@Override
	public ISeriesRenderingHints getSeriesRenderingHints(SeriesDefinition sdOrthogonal, Series seOrthogonal)
			throws ChartException, IllegalArgumentException {
		if (seOrthogonal == null || seOrthogonal.getClass() == SeriesImpl.class) {
			// EMPTY PLOT RENDERING TECHNIQUE
			return null;
		}
		OneAxis oaxOrthogonal = findOrthogonalAxis(seOrthogonal);
		if (oaxOrthogonal == null) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.NOT_FOUND,
					"exception.axis.series.link.broken", //$NON-NLS-1$
					new Object[] { seOrthogonal }, Messages.getResourceBundle(rtc.getULocale()));
		}
		final OneAxis oaxBase = aax.getPrimaryBase();
		final SeriesDefinition sdBase = oaxBase.getModelAxis().getSeriesDefinitions().get(0);

		final AutoScale scBase = oaxBase.getScale();
		final AutoScale scOrthogonal = oaxOrthogonal.getScale();
		final int iTickCount = scBase.getTickCount();
		int iUnitCount = iTickCount;
		final int iDirection = scBase.getDirection();

		// convert to signed unit size.
		final double dUnitSize = (iDirection == BACKWARD || (iDirection == AUTO && aax.areAxesSwapped()))
				? -scBase.getUnitSize()
				: scBase.getUnitSize();

		if (scBase.getType() == IConstants.DATE_TIME) {
			// TBD: HANDLE DATETIME VALUE VS TEXT AXIS
		}

		AxisTickCoordinates daTickCoordinates = scBase.getTickCordinates();
		Object oDataBase = null;
		DataSetIterator dsiDataBase = scBase.getData();
		Object oDataOrthogonal;
		DataSetIterator dsiDataOrthogonal = getTypedDataSet(seOrthogonal, oaxOrthogonal.getScale().getType());
		double dOrthogonalZero = 0;
		if ((scOrthogonal.getType() & NUMERICAL) == NUMERICAL) {
			dOrthogonalZero = getLocation(scOrthogonal, 0);
		} else {
			dOrthogonalZero = scOrthogonal.getStart();
		}
		double dBaseZero = 0;
		if ((scBase.getType() & NUMERICAL) == IConstants.NUMERICAL && !oaxBase.isCategoryScale()) {
			dBaseZero = getLocation(scBase, 0);
		} else if (oaxBase.isTickBwtweenCategories()) {
			dBaseZero = scBase.getStart();
		} else {
			dBaseZero = scBase.getStart() + scBase.getStartShift();
		}

		if (scBase.getType() == TEXT || oaxBase.isCategoryScale()) {
			iUnitCount--;
			if (oaxBase.isTickBwtweenCategories()) {
				iUnitCount--;
			}
		}

		double dX = 0, dY = 0;
		Location lo;

		final int iBaseCount = dsiDataBase.size();
		final int iOrthogonalCount = dsiDataOrthogonal.size();
		DataPointHints[] dpa = null;

		// DO NOT COMPUTE DATA POINT HINTS FOR OUT-OF-SYNC DATA
		if (iBaseCount != iOrthogonalCount) {
			logger.log(ILogger.INFORMATION, Messages.getString("exception.base.orthogonal.inconsistent.count", //$NON-NLS-1$
					new Object[] { Integer.valueOf(iBaseCount), Integer.valueOf(iOrthogonalCount) }, rtc.getULocale()));
		} else {
			dpa = new DataPointHints[iBaseCount];
			final boolean bScatter = (oaxBase.getScale().getType() != IConstants.TEXT && !oaxBase.isCategoryScale());

			// OPTIMIZED PRE-FETCH FORMAT SPECIFIERS FOR ALL DATA POINTS
			final DataPoint dp = seOrthogonal.getDataPoint();
			final EList<?> el = dp.getComponents();
			DataPointComponent dpc;
			DataPointComponentType dpct;
			FormatSpecifier fsBase = null, fsOrthogonal = null, fsSeries = null, fsPercentile = null;
			for (int i = 0; i < el.size(); i++) {
				dpc = (DataPointComponent) el.get(i);
				dpct = dpc.getType();
				if (DataPointComponentType.BASE_VALUE_LITERAL.equals(dpct)) {
					fsBase = dpc.getFormatSpecifier();
					if (fsBase == null) // BACKUP
					{
						fsBase = sdBase.getFormatSpecifier();
					}
				}
				if (DataPointComponentType.ORTHOGONAL_VALUE_LITERAL.equals(dpct)) {
					fsOrthogonal = dpc.getFormatSpecifier();
					if (fsOrthogonal == null && seOrthogonal.eContainer() instanceof SeriesDefinition) {
						fsOrthogonal = ((SeriesDefinition) seOrthogonal.eContainer()).getFormatSpecifier();
					}
				}
				if (DataPointComponentType.SERIES_VALUE_LITERAL.equals(dpct)) {
					fsSeries = dpc.getFormatSpecifier();
				}
				if (DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL.equals(dpct)) {
					fsPercentile = dpc.getFormatSpecifier();
				}
			}

			dsiDataBase.reset();
			dsiDataOrthogonal.reset();

			UserDataSetHints udsh = new UserDataSetHints(seOrthogonal.getDataSets(), getModel().isReverseCategory());
			udsh.reset();

			double total = 0;
			boolean isZeroValue = true;

			// get total orthogonal value.
			for (int i = 0; i < iOrthogonalCount; i++) {
				Object v = dsiDataOrthogonal.next();

				if (v instanceof Number) {
					if (((Number) v).doubleValue() != 0.0) {
						isZeroValue = false;
					}
					total += ((Number) v).doubleValue();
				} else if (v instanceof NumberDataElement) {
					if (((NumberDataElement) v).getValue() != 0.0) {
						isZeroValue = false;
					}
					total += ((NumberDataElement) v).getValue();
				}
			}

			dsiDataOrthogonal.reset();

			for (int i = 0; i < iBaseCount; i++) {
				oDataBase = dsiDataBase.next();
				oDataOrthogonal = dsiDataOrthogonal.next();

				if (!bScatter) {
					if (aax.areAxesSwapped()) {
						dY = daTickCoordinates.getStart() + dUnitSize * i;
						try {
							dX = getLocation(scOrthogonal, oDataOrthogonal);
						} catch (IllegalArgumentException nvex) {
							// dX = dOrthogonalZero;
							dX = Double.NaN;
						} catch (ChartException dfex) {
							dX = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
						}
					} else {

						dX = daTickCoordinates.getStart() + dUnitSize * i;
						try {
							dY = getLocation(scOrthogonal, oDataOrthogonal);
						} catch (IllegalArgumentException nvex) {
							// dY = dOrthogonalZero;
							dY = Double.NaN;
						} catch (ChartException dfex) {
							dY = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
						}
					}
				} else {
					// SCATTER CHARTS (BASE AXIS != CATEGORY AXIS)
					try {
						dX = getLocation(scBase, oDataBase);
					} catch (IllegalArgumentException | ChartException dfex) {
						dX = dBaseZero; // FOR CUSTOM DATA ELEMENTS
					}

					try {
						dY = getLocation(scOrthogonal, oDataOrthogonal);
					} catch (IllegalArgumentException | ChartException dfex) {
						dY = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
					}

					if (aax.areAxesSwapped()) {
						final double dTemp = dX;
						dX = dY;
						dY = dTemp;
					}
				}
				lo = goFactory.createLocation(dX, dY);

				// Compute the offset between two ticks
				double dLength = 0;
				if (!bScatter) {
					dLength = dUnitSize;
				} else {
					for (int j = 0; j < iTickCount - 1; j++) {
						if (aax.areAxesSwapped()) {
							// Coordinates array is ordered by descending
							if ((dY <= daTickCoordinates.getCoordinate(j)
									&& dY >= daTickCoordinates.getCoordinate(j + 1))
									|| (dY <= daTickCoordinates.getCoordinate(j + 1)
											&& dY >= daTickCoordinates.getCoordinate(j))) {
								// Keep the negative value
								dLength = daTickCoordinates.getCoordinate(j + 1) - daTickCoordinates.getCoordinate(j);
								break;
							}
						} else if ((dX <= daTickCoordinates.getCoordinate(j + 1)
								&& dX >= daTickCoordinates.getCoordinate(j))
								|| (dX <= daTickCoordinates.getCoordinate(j)
										&& dX >= daTickCoordinates.getCoordinate(j + 1))) {
							dLength = daTickCoordinates.getCoordinate(j + 1) - daTickCoordinates.getCoordinate(j);
							break;
						}
					}
				}

				Object percentileValue = null;

				final boolean bIsPercent = oaxOrthogonal.getModelAxis().isPercent();
				if (bIsPercent) {
					// #224410
					AxisSubUnit au = ssl.getUnit(seOrthogonal, i);

					if (oDataOrthogonal instanceof Number) {
						percentileValue = new Double(
								au.valuePercentage(((Number) oDataOrthogonal).doubleValue()) / 100);
					} else if (oDataOrthogonal instanceof NumberDataElement) {
						percentileValue = new Double(
								au.valuePercentage(((NumberDataElement) oDataOrthogonal).getValue()) / 100);
					}
				} else if (total != 0) {
					if (oDataOrthogonal instanceof Number) {
						percentileValue = new Double(((Number) oDataOrthogonal).doubleValue() / total);
					} else if (oDataOrthogonal instanceof NumberDataElement) {
						percentileValue = new Double(((NumberDataElement) oDataOrthogonal).getValue() / total);
					}
				} else if (isZeroValue) {
					percentileValue = new Double(1.0 / iOrthogonalCount);
				}

				dpa[i] = new DataPointHints(oDataBase, oDataOrthogonal, seOrthogonal.getSeriesIdentifier(),
						percentileValue, seOrthogonal.getDataPoint(), fsBase, fsOrthogonal, fsSeries, fsPercentile, i,
						lo, dLength, rtc);

				udsh.next(dpa[i]);
			}
		}

		SeriesRenderingHints srh = new SeriesRenderingHints(this, oaxBase.getAxisCoordinate(), scOrthogonal.getStart(),
				dOrthogonalZero, dSeriesThickness, daTickCoordinates, dpa, scBase, scOrthogonal, ssl, dsiDataBase,
				dsiDataOrthogonal);

		// Set client area bounds, it will be used to clip valid area for blocks
		// rendering of chart.
		setClientAreaBounds(oaxOrthogonal, seOrthogonal, srh);

		return srh;
	}

	/**
	 * @param oaxOrthogonal
	 * @param seOrthogonal
	 * @param srh
	 */
	private void setClientAreaBounds(OneAxis oaxOrthogonal, Series seOrthogonal, SeriesRenderingHints srh) {
		Bounds boClientArea = goFactory.copyOf(getPlotBounds());

		// The study layout case, each axis should have own client area.
		if (ChartUtil.hasMultipleYAxes(getModel()) && getModel().isStudyLayout()) {
			double points[] = oaxOrthogonal.getScale().getEndPoints();
			double start = points[0];
			double end = points[1];
			if (!aax.areAxesSwapped()) {
				boClientArea.setTop(end);
				boClientArea.setHeight(start - end);
			} else {
				boClientArea.setLeft(start);
				boClientArea.setWidth(end - start);
			}

			// Since line chart has no 2D+, so here doesn't adjust client area for line
			// chart in combine 2D+ case.
			boolean isLineSeries = (seOrthogonal instanceof LineSeries);
			boolean isJustAreaSeries = (seOrthogonal instanceof AreaSeries
					&& !(seOrthogonal instanceof DifferenceSeries));

			if (iDimension == TWO_5_D && (!isLineSeries || isJustAreaSeries)) // 2D+
			{
				boClientArea.delta(dSeriesThickness, -dSeriesThickness, 0, 0);
			}
		}

		srh.setClientAreaBounds(boClientArea);
	}

	public StackedSeriesLookup getStackedSeriesLookup() {
		return ssl;
	}

	public void addMargin(int percent) {
		if (percent > 0) {
			iMarginPercent = percent;
		}
	}

	/**
	 * Initializes the chart plot bounds for the dynamic size case.
	 *
	 * @param bo bounds with dynamic size, such as 0 or negative value
	 * @throws ChartException
	 * @since 2.3
	 */
	public void initDynamicPlotBounds(Bounds bo) throws ChartException {
		// If one of the dimension is zero, to replace it with axis label height
		if (bo.getWidth() * bo.getHeight() == 0) {
			final Axis[] axa = getModel().getPrimaryBaseAxes();
			final Axis axPrimaryBase = axa[0];
			final Axis axPrimaryOrthogonal = getModel().getPrimaryOrthogonalAxis(axPrimaryBase);
			int iAxisType = getAxisType(axPrimaryOrthogonal);
			DataSetIterator dsi = new DataSetIterator(getMinMax(axPrimaryOrthogonal, iAxisType), iAxisType);

			OneAxis oaxPrimaryOrthogonal = getAxes().getPrimaryOrthogonal();
			AutoScale scPrimaryOrthogonal = AutoScale.computeScale(ids, oaxPrimaryOrthogonal, dsi, iAxisType, 0, // dStart,
					0, // dEnd,
					axPrimaryBase.getOrigin(), rtc, IConstants.AUTO, 1, iMarginPercent, this);
			// Compute the axis label height to replace the zero
			double axisHeight = scPrimaryOrthogonal.computeAxisLabelThickness(ids, axPrimaryOrthogonal.getLabel(),
					getAxes().getOrientation()) * 72 / ids.getDpiResolution() + IConstants.TICK_SIZE;
			if (bo.getWidth() == 0) {
				bo.setWidth(axisHeight);
			} else {
				bo.setHeight(axisHeight);
			}
		}

		// If one of the dimension is negative, to replace it with data point
		// total width
		else if (bo.getWidth() * bo.getHeight() < 0) {
			// Get the base size
			double dBase = bo.getHeight() < 0 ? Math.abs(bo.getHeight()) : Math.abs(bo.getWidth());
			// Get the data count
			Series baseSeries = getModel().getSeries(IConstants.BASE)[0];
			Object[] values = (Object[]) baseSeries.getDataSet().getValues();
			int iDPCount = values.length;
			// Compute the total width by multiplying the data point count
			double dTotalWidth = dBase * iDPCount;
			if (bo.getWidth() < 0) {
				bo.setWidth(dTotalWidth);
			} else {
				bo.setHeight(dTotalWidth);
			}
		}
	}

	void updateValueAxis(OneAxis valueAxis, double dStart, double dEnd, boolean isReverseCategory)
			throws ChartException, IllegalArgumentException {
		final Axis yAxis = valueAxis.getModelAxis();
		int iAxisType = getAxisType(yAxis);
		DataSetIterator dsi = null;
		if ((iAxisType & NUMERICAL) == NUMERICAL || (iAxisType & DATE_TIME) == DATE_TIME) {
			Object minMax = getMinMax(yAxis, iAxisType);
			if (rtc.getSharedScale() != null) {
				dsi = getSharedScaleMinMax(iAxisType, minMax);
			} else {
				dsi = new DataSetIterator(minMax, iAxisType);
			}
			// Reverse the series categories if needed.
			dsi.reverse(isReverseCategory);
		} else {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_FORMAT,
					"exception.orthogonal.axis.numerical.datetime", //$NON-NLS-1$
					Messages.getResourceBundle(rtc.getULocale()));
		}

		updateAxisScale(valueAxis, iAxisType, dsi, dStart, dEnd);
	}

	private DataSetIterator getSharedScaleMinMax(int iAxisType, Object minMax) throws ChartException {
		DataSetIterator dsi;
		if (minMax instanceof BigNumber[]) {
			dsi = rtc.getSharedScale().createDataSetIterator(iAxisType, true, ((BigNumber[]) minMax)[1].getDivisor());
		} else {
			dsi = rtc.getSharedScale().createDataSetIterator(iAxisType, false, null);
		}

		if ((iAxisType & NUMERICAL) == NUMERICAL) {
			Object min = dsi.first();
			Object max = dsi.next();
			dsi = new DataSetIterator(adjustMinMax(min, max), iAxisType);
		}

		return dsi;
	}

	/**
	 * @param oax
	 * @param dStart
	 * @param dEnd
	 * @param yAxis
	 * @param iAxisType
	 * @param dsi
	 * @throws ChartException
	 */
	private void updateAxisScale(OneAxis oax, int iAxisType, DataSetIterator dsi, double dStart, double dEnd)
			throws ChartException {
		boolean isPrimaryOrthogonal = (aax.getPrimaryOrthogonal() == oax);
		AutoScale scValueAxis = AutoScale.computeScale(ids, oax, dsi, iAxisType, dStart, dEnd,
				isPrimaryOrthogonal ? aax.getPrimaryBase().getModelAxis().getOrigin() : oax.getModelAxis().getOrigin(),
				rtc, AUTO, 1, iMarginPercent, this);
		// Update scale.
		oax.set(scValueAxis);
		scValueAxis.resetShifts();
	}
}
