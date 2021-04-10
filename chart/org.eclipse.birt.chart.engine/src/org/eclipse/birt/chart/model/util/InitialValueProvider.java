/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved.
 *******************************************************************************/

package org.eclipse.birt.chart.model.util;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;

/**
 * This class defines chart element instances with initial values, the 'isSet'
 * flag isn't initialized.
 * 
 * @since 3.7
 */

public class InitialValueProvider {

	/**
	 * 
	 * @return default instance of chart with axes.
	 */
	public static ChartWithAxes defChartWithAxes() {
		return defChartWithAxes;
	}

	/**
	 * 
	 * @return default instance of chart without axes.
	 */
	public static ChartWithoutAxes defChartWithoutAxes() {
		return defChartWithoutAxes;
	}

	/**
	 * 
	 * @return default instance of dial chart.
	 */
	public static DialChart defDialChart() {
		return defDialChart;
	}

	/**
	 * 
	 * @return default instance of title.
	 */
	public static TitleBlock defTitleBlock() {
		return defTitleBlock;
	}

	/**
	 * 
	 * @return default instance of plot.
	 */
	public static Plot defPlot() {
		return defPlot;
	}

	/**
	 * 
	 * @return default instance of legend.
	 */
	public static Legend defLegend() {
		return defLegend;
	}

	/**
	 * 
	 * @return default instance of series definition.
	 */
	public static SeriesDefinition defSeriesDefinition(int id) {
		return defSeriesDefinitions.get(id);
	}

	/**
	 * 
	 * @return default instance of series.
	 */
	public static Series defSeries() {
		return defSeries;
	}

	/**
	 * 
	 * @return default instance of x axis or cateogry axis.
	 */
	public static Axis defBaseAxis() {
		return defBaseAxis;
	}

	/**
	 * 
	 * @return default instance of y axis or value axis.
	 */
	public static Axis defOrthogonalAxis() {
		return defOrthAxis;
	}

	/**
	 * 
	 * @return default instance of z axis.
	 */
	public static Axis defAncillaryAxis() {
		return defAncillaryAxis;
	}

	/**
	 * 
	 * @return default instance of gantt series.
	 */
	public static GanttSeries defGanttSeries() {
		return defGanttSeries;
	}

	/**
	 * 
	 * @return default instance of scatter series.
	 */
	public static ScatterSeries defScatterSeries() {
		return defScatterSeries;
	}

	/**
	 * 
	 * @return default instance of dial series.
	 */
	public static DialSeries defDialSeries() {
		return defDialSeries;
	}

	/**
	 * 
	 * @return default instance of pie series.
	 */
	public static PieSeries defPieSeries() {
		return defPieSeries;
	}

	/**
	 * 
	 * @return default instance of difference series.
	 */
	public static DifferenceSeries defDifferenceSeries() {
		return defDifferenceSeries;
	}

	/**
	 * 
	 * @return default instance of stock series.
	 */
	public static StockSeries defStockSeries() {
		return defStockSeries;
	}

	/**
	 * 
	 * @return default instance of line series.
	 */
	public static LineSeries defLineSeries() {
		return defLineSeries;
	}

	/**
	 * 
	 * @return default instance of area series.
	 */
	public static AreaSeries defAreaSeries() {
		return defAreaSeries;
	}

	/**
	 * 
	 * @return default instance of bar series.
	 */
	public static BarSeries defBarSeries() {
		return defBarSeries;
	}

	/**
	 * 
	 * @return default instance of bubble series.
	 */
	public static BubbleSeries defBubbleSeries() {
		return defBubbleSeries;
	}

	private static final ChartWithAxes defChartWithAxes = createDefaultChartWithAxes();

	private static ChartWithAxes createDefaultChartWithAxes() {

		ChartWithAxes newChart = ChartWithAxesImpl.createDefault();

		Axis axBase = newChart.getAxes().get(0);
		try {
			ChartElementUtil.setDefaultValue(axBase, "categoryAxis", true); //$NON-NLS-1$
		} catch (ChartException e) {
		}
		axBase.getSeriesDefinitions().add(createDefaultSeriesDefinition(0));
		axBase.getLineAttributes().setColor(ColorDefinitionImpl.BLACK());

		Axis axOrth = axBase.getAssociatedAxes().get(0);
		axOrth.getSeriesDefinitions().add(createDefaultSeriesDefinition(0));
		axOrth.getLineAttributes().setColor(ColorDefinitionImpl.BLACK());
		createDefaultAncillaryAxis(newChart);
		return newChart;
	}

	private static Axis createDefaultAncillaryAxis(ChartWithAxes chart) {
		chart.setRotation(Rotation3DImpl.createDefault(new Angle3D[] { Angle3DImpl.createDefault(-20, 45, 0) }));

		try {
			chart.setUnitSpacing(50);

			Axis zAxisAncillary = AxisImpl.createDefault(Axis.ANCILLARY_BASE);
			ChartElementUtil.setDefaultValue(zAxisAncillary, "titlePosition", //$NON-NLS-1$
					Position.BELOW_LITERAL);
			zAxisAncillary.getTitle().getCaption().setValue(Messages.getString("ChartWithAxesImpl.Z_Axis.title")); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(zAxisAncillary.getTitle(), "visible", //$NON-NLS-1$
					false);
			ChartElementUtil.setDefaultValue(zAxisAncillary, "primaryAxis", //$NON-NLS-1$
					true);
			ChartElementUtil.setDefaultValue(zAxisAncillary, "labelPosition", //$NON-NLS-1$
					Position.BELOW_LITERAL);
			ChartElementUtil.setDefaultValue(zAxisAncillary, "orientation", //$NON-NLS-1$
					Orientation.HORIZONTAL_LITERAL);
			ChartElementUtil.setDefaultValue(zAxisAncillary.getOrigin(), "type", //$NON-NLS-1$
					IntersectionType.MIN_LITERAL);
			zAxisAncillary.getOrigin().setValue(NumberDataElementImpl.create(0));
			ChartElementUtil.setDefaultValue(zAxisAncillary, "type", //$NON-NLS-1$
					AxisType.TEXT_LITERAL);
			chart.getPrimaryBaseAxes()[0].getAncillaryAxes().add(zAxisAncillary);

			SeriesDefinition sdZ = SeriesDefinitionImpl.createDefault();
			sdZ.getSeriesPalette().shift(0);
			sdZ.getSeries().add(SeriesImpl.createDefault());
			zAxisAncillary.getSeriesDefinitions().add(sdZ);

			return zAxisAncillary;
		} catch (ChartException e) {
			// Do nothing.
		}
		return null;
	}

	private static final TitleBlock defTitleBlock = getTitleBlock(defChartWithAxes);
	private static final Plot defPlot = getPlot(defChartWithAxes);
	private static final Legend defLegend = getLegend(defChartWithAxes);
	private static final Series defSeries = SeriesImpl.createDefault();
	private static final GanttSeries defGanttSeries = (GanttSeries) GanttSeriesImpl.createDefault();
	private static final ScatterSeries defScatterSeries = (ScatterSeries) ScatterSeriesImpl.createDefault();
	private static final DialSeries defDialSeries = (DialSeries) DialSeriesImpl.createDefault();
	private static final PieSeries defPieSeries = (PieSeries) PieSeriesImpl.createDefault();
	private static final DifferenceSeries defDifferenceSeries = (DifferenceSeries) DifferenceSeriesImpl.createDefault();
	private static final StockSeries defStockSeries = (StockSeries) StockSeriesImpl.createDefault();
	private static final LineSeries defLineSeries = (LineSeries) LineSeriesImpl.createDefault();
	private static final AreaSeries defAreaSeries = (AreaSeries) AreaSeriesImpl.createDefault();
	private static final BarSeries defBarSeries = (BarSeries) BarSeriesImpl.createDefault();
	private static final BubbleSeries defBubbleSeries = (BubbleSeries) BubbleSeriesImpl.createDefault();
	private static final Axis defBaseAxis = defChartWithAxes.getAxes().get(0);
	private static final Axis defOrthAxis = defBaseAxis.getAssociatedAxes().get(0);
	private static final Axis defAncillaryAxis = defBaseAxis.getAncillaryAxes().get(0);

	public static Insets defLabelInsets() {
		return defLabelInsets;
	}

	private static final Insets defLabelInsets = InsetsImpl.createDefault(0, 2, 0, 3);

	public static Label defLabel() {
		return defLabel;
	}

	private static final Label defLabel = LabelImpl.createDefault();

	private static final DefSeriesDefinitionPool defSeriesDefinitions = new DefSeriesDefinitionPool();

	private static class DefSeriesDefinitionPool {

		private int size = 8;

		public SeriesDefinition get(int id) {
			if (id >= size) {
				int sizeNew = (id / 4 + 1) * 4;

				SeriesDefinition[] poolNew = new SeriesDefinition[sizeNew];
				System.arraycopy(pool, 0, poolNew, 0, size);

				for (int i = size; i < sizeNew; i++) {
					poolNew[i] = createDefaultSeriesDefinition(-i);
				}

				pool = poolNew;
				size = sizeNew;
			}
			return pool[id];
		}

		private SeriesDefinition[] pool = { createDefaultSeriesDefinition(0), createDefaultSeriesDefinition(-1),
				createDefaultSeriesDefinition(-2), createDefaultSeriesDefinition(-3), createDefaultSeriesDefinition(-4),
				createDefaultSeriesDefinition(-5), createDefaultSeriesDefinition(-6),
				createDefaultSeriesDefinition(-7), };

	}

	private static ChartWithoutAxes defChartWithoutAxes = createDefaultChartWithoutAxes();

	private static ChartWithoutAxes createDefaultChartWithoutAxes() {
		ChartWithoutAxes newChart = ChartWithoutAxesImpl.createDefault();
		newChart.setSubType("Standard"); //$NON-NLS-1$

		SeriesDefinition sdX = createDefaultSeriesDefinition(0);
		sdX.getQuery().setDefinition("Base Series"); //$NON-NLS-1$

		SeriesDefinition sdY = createDefaultSeriesDefinition(0);
		sdX.getSeriesDefinitions().add(sdY);
		newChart.getSeriesDefinitions().add(sdX);

		return newChart;
	}

	private static DialChart defDialChart = createDefaultDialChart();

	private static DialChart createDefaultDialChart() {
		DialChart newChart = (DialChart) DialChartImpl.createDefault();
		newChart.setSubType("Standard Meter Chart"); //$NON-NLS-1$
		newChart.setUnits("Points"); //$NON-NLS-1$
		newChart.setDialSuperimposition(false);
		newChart.getLegend().setItemType(LegendItemType.SERIES_LITERAL);

		SeriesDefinition sdX = createDefaultSeriesDefinition(0);
		sdX.getQuery().setDefinition("Base Series"); //$NON-NLS-1$

		SeriesDefinition sdY = createDefaultSeriesDefinition(0);
		sdX.getSeriesDefinitions().add(sdY);

		newChart.getSeriesDefinitions().add(sdX);

		return newChart;
	}

	private static TitleBlock getTitleBlock(Chart chart) {
		for (Block b : chart.getBlock().getChildren()) {
			if (b instanceof TitleBlock) {
				return (TitleBlock) b;
			}
		}
		return null;
	}

	private static Plot getPlot(Chart chart) {
		for (Block b : chart.getBlock().getChildren()) {
			if (b instanceof Plot) {
				return (Plot) b;
			}
		}
		return null;
	}

	private static Legend getLegend(Chart chart) {
		for (Block b : chart.getBlock().getChildren()) {
			if (b instanceof Legend) {
				return (Legend) b;
			}
		}
		return null;
	}

	protected static SeriesDefinition createDefaultSeriesDefinition(int paletteShift) {
		SeriesDefinition sd = SeriesDefinitionImpl.createDefault();
		sd.getSeries().add(SeriesImpl.createDefault());
		// Palette p = PaletteImpl.create( ColorDefinitionImpl.GREY( ) );
		// p.shift( paletteShift );
		// sd.setSeriesPalette( p );
		// sd.getSeriesPalette( ).shift( paletteShift );
		return sd;
	}

}
