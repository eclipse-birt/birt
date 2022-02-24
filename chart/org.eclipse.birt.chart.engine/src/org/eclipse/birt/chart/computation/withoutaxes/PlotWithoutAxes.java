/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.computation.withoutaxes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.PlotComputation;
import org.eclipse.birt.chart.computation.UserDataSetHints;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.emf.common.util.EList;

/**
 * This class is capable of computing the content of a chart (without axes)
 * based on preferred sizes, text rotation, fit ability, scaling, etc and
 * prepares it for rendering.
 *
 * WARNING: This is an internal class and subject to change
 */
public final class PlotWithoutAxes extends PlotComputation {

	private transient Size szCell = null;

	private transient int iRows = 0, iColumns = 0, iSeries = 0;

	/**
	 * The constructor.
	 *
	 * @param xs
	 * @param cwoa
	 * @param rtc
	 */
	public PlotWithoutAxes(IDisplayServer xs, ChartWithoutAxes cwoa, RunTimeContext rtc) {
		super(xs, rtc, cwoa);
	}

	@Override
	public void compute(Bounds bo) {
		// bo.adjustDueToInsets(cwoa.getPlot().getInsets()); // INSETS DEFINED
		// IN POINTS: ALREADY COMPENSATED IN GENERATOR!
		boPlotBackground = goFactory.scaleBounds(bo, dPointToPixel); // CONVERSION
																		// TO
																		// PIXELS
		// final Series[] sea = cwoa.getRunTimeSeries();

		EList<SeriesDefinition> el = getModel().getSeriesDefinitions();
		ArrayList<Series> al = new ArrayList<>();
		((ChartWithoutAxesImpl) getModel()).recursivelyGetSeries(el, al, 0, 0);
		final Series[] sea = al.toArray(new Series[al.size()]);

		iSeries = sea.length;
		iColumns = getModel().getGridColumnCount();
		if (iColumns == 0) {
			iColumns = getAutoColumCount(boPlotBackground, iSeries);
		}

		iRows = (iSeries - 1) / iColumns + 1;

		if (getModel() instanceof DialChart) {
			DialChart dcw = (DialChart) getModel();
			if (dcw.isDialSuperimposition()) {
				iColumns = 1;
				iRows = 1;
			}
		}

		szCell = SizeImpl.create(boPlotBackground.getWidth() / iColumns, boPlotBackground.getHeight() / iRows);
		insCA = goFactory.scaleInsets(getModel().getPlot().getClientArea().getInsets(), dPointToPixel);
	}

	private static int getAutoColumCount(Bounds boPlot, int iSeries) {
		double rat = boPlot.getHeight() / boPlot.getWidth();
		int colums = (int) Math.round(Math.sqrt(iSeries / rat));
		colums = Math.min(colums, iSeries);
		if (colums == 0) {
			colums++;
		}
		return colums;
	}

	public Size getCellSize() {
		return szCell;
	}

	public Coordinates getCellCoordinates(int iCell) {
		return new Coordinates(iCell % iColumns, iCell / iColumns);
	}

	public int getColumnCount() {
		return iColumns;
	}

	public int getRowCount() {
		return iRows;
	}

	@Override
	public ChartWithoutAxes getModel() {
		return (ChartWithoutAxes) cm;
	}

	@Override
	public SeriesRenderingHints getSeriesRenderingHints(SeriesDefinition sdOrthogonal, Series seOrthogonal)
			throws ChartException, IllegalArgumentException {
		if (seOrthogonal == null || seOrthogonal.getClass() == SeriesImpl.class) {
			// EMPTY PLOT RENDERING TECHNIQUE
			return null;
		}

		final EList<SeriesDefinition> elCategories = getModel().getSeriesDefinitions();
		if (elCategories.size() != 1) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_FORMAT,
					"exception.cwoa.single.series.definition", //$NON-NLS-1$
					Messages.getResourceBundle(rtc.getULocale()));
		}
		final SeriesDefinition sd = elCategories.get(0);
		final List<Series> al = sd.getRunTimeSeries();
		if (al.size() != 1) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_FORMAT,
					"exception.cwoa.single.runtime.series", //$NON-NLS-1$
					Messages.getResourceBundle(rtc.getULocale()));
		}
		final Series seBase = al.get(0);
		final DataSetIterator dsiBaseValues = new DataSetIterator(seBase.getDataSet());
		final DataSetIterator dsiOrthogonalValues = new DataSetIterator(seOrthogonal.getDataSet());
		DataPointHints[] dpha = null;

		if (dsiBaseValues.size() != dsiOrthogonalValues.size()) // DO NOT
		// COMPUTE
		// DATA
		// POINT
		// HINTS FOR
		// OUT-OF-SYNC
		// DATA
		{
			logger.log(ILogger.INFORMATION, Messages.getString("exception.data.outofsync", //$NON-NLS-1$
					new Object[] { Integer.valueOf(dsiBaseValues.size()), Integer.valueOf(dsiOrthogonalValues.size()) },
					rtc.getULocale()));
		} else {

			final int iCount = dsiOrthogonalValues.size();
			dpha = new DataPointHints[iCount];

			// OPTIMIZED PRE-FETCH FORMAT SPECIFIERS FOR ALL DATA POINTS
			final DataPoint dp = seOrthogonal.getDataPoint();
			final EList<DataPointComponent> el = dp.getComponents();
			DataPointComponent dpc;
			DataPointComponentType dpct;
			FormatSpecifier fsBase = null, fsOrthogonal = null, fsSeries = null, fsPercentile = null;
			for (int i = 0; i < el.size(); i++) {
				dpc = el.get(i);
				dpct = dpc.getType();
				if (DataPointComponentType.BASE_VALUE_LITERAL.equals(dpct)) {
					fsBase = dpc.getFormatSpecifier();
					if (fsBase == null) // BACKUP
					{
						fsBase = sd.getFormatSpecifier();
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

			UserDataSetHints udsh = new UserDataSetHints(seOrthogonal.getDataSets());
			udsh.reset();

			double total = 0;

			// get total orthogonal value.
			for (int i = 0; i < iCount; i++) {
				Object v = dsiOrthogonalValues.next();

				if (v instanceof Number) {
					total += ((Number) v).doubleValue();
				} else if (v instanceof NumberDataElement) {
					total += ((NumberDataElement) v).getValue();
				}
			}

			dsiOrthogonalValues.reset();

			for (int i = 0; i < iCount; i++) {
				Object orthValue = dsiOrthogonalValues.next();

				Object percentileValue = null;

				if (total != 0) {
					if (orthValue instanceof Number) {
						percentileValue = new Double(((Number) orthValue).doubleValue() / total);
					} else if (orthValue instanceof NumberDataElement) {
						percentileValue = new Double(((NumberDataElement) orthValue).getValue() / total);
					}
				} else {
					percentileValue = new Double(0);
				}

				dpha[i] = new DataPointHints(dsiBaseValues.next(), orthValue, seOrthogonal.getSeriesIdentifier(),
						percentileValue, seOrthogonal.getDataPoint(), fsBase, fsOrthogonal, fsSeries, fsPercentile, i,
						null, -1, rtc);

				udsh.next(dpha[i]);
			}
		}

		return new SeriesRenderingHints(this, dpha, dsiBaseValues, dsiOrthogonalValues);
	}

}
