/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.chart.interactivity;

import org.eclipse.birt.chart.extension.datafeed.StockEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.URLValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.component.impl.DialRegionImpl;
import org.eclipse.birt.chart.model.component.impl.MarkerLineImpl;
import org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.StockDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

import com.ibm.icu.util.Calendar;

/**
 * The class cannot be run individually. It provides sample model
 * implementations for viewer selector classes in the package.
 */

public final class PrimitiveCharts extends ChartTestCase {

	/**
	 * Creates a area chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */

	public static final Chart showTooltip_AreaChart() {
		ChartWithAxes cwaArea = ChartWithAxesImpl.create();

		// Chart Type
		cwaArea.setType("Area Chart");

		// Title
		cwaArea.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaArea.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaArea.getPlot().getClientArea().getOutline().setVisible(false);
		cwaArea.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwaArea.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.EAST_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.RIGHT_LITERAL);
		lg.setOrientation(Orientation.VERTICAL_LITERAL);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerRange mr = MarkerRangeImpl.create(xAxisPrimary, NumberDataElementImpl.create(2.0),
				NumberDataElementImpl.create(3.0), null);
		mr.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary, NumberDataElementImpl.create(60.0));
		ml.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		AreaSeries as = (AreaSeries) AreaSeriesImpl.create();
		as.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		as.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		as.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		as.getLabel().setVisible(true);
		as.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(207, 41, 207), LineStyle.SOLID_LITERAL, 1));
		as.setDataSet(dsNumericValues1);
		for (int i = 0; i < as.getMarkers().size(); i++) {
			((Marker) as.getMarkers().get(i)).setVisible(true);
		}
		as.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(0, "abc\nedd"))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		sdY.getSeries().add(as);

		return cwaArea;
	}

	/**
	 * Creates a bar chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */

	public static final Chart showTooltip_BarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Chart Type
		cwaBar.setType("Bar Chart");
		cwaBar.setSubType("Stacked");

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaBar.getPlot().getClientArea().getOutline().setVisible(false);
		cwaBar.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		FontDefinition fd = FontDefinitionImpl.create("Arial", (float) 30.0, true, true, false, true, false, 30.0,
				TextAlignmentImpl.create());
		yAxisPrimary.getLabel().getCaption().setFont(fd);
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { 15.29, -14.53, -47.05, 32.55 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		bs.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs.getLabel().setVisible(true);
		bs.setDataSet(dsNumericValues1);
		bs.setStacked(true);
		bs.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEMOVE_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(bs);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Micorsoft"); //$NON-NLS-1$
		bs2.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs2.getLabel().setVisible(true);
		bs2.setDataSet(dsNumericValues2);
		bs2.setStacked(true);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.PINK());
		sdY2.getSeries().add(bs2);

		return cwaBar;
	}

	/**
	 * Creates a line chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */

	public static final Chart showTooltip_LineChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();

		// Chart Type
		cwaLine.setType("Line Chart");

		// Title
		cwaLine.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaLine.getPlot().getClientArea().getOutline().setVisible(false);
		cwaLine.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwaLine.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.SOUTH_LITERAL);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setOrientation(Orientation.HORIZONTAL_LITERAL);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.SOLID_LITERAL, 1));

		MarkerLine ml = MarkerLineImpl.create(xAxisPrimary, NumberDataElementImpl.create(2.0));
		ml.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerRange mr = MarkerRangeImpl.create(yAxisPrimary, NumberDataElementImpl.create(60.0),
				NumberDataElementImpl.create(90.0), null);
		mr.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		ls.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ls.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(220, 50, 227), LineStyle.DOTTED_LITERAL, 3));
		ls.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ls.getLabel().setVisible(true);
		ls.setDataSet(dsNumericValues1);
		for (int i = 0; i < ls.getMarkers().size(); i++) {
			((Marker) ls.getMarkers().get(i)).setType(MarkerType.ICON_LITERAL);
//			( (Marker) ls.getMarkers( ).get( i ) ).setFill( ImageImpl
//					.create( "file:///" + System.getProperty( "user.dir" )
//							+ "/27.gif" ) );
			((Marker) ls.getMarkers().get(i)).setFill(ImageImpl
					.create("file:src/" + getClassName().replace('.', '/') + "/" + INPUT_FOLDER + "/" + "27.gif"));

		}

		ls.setStacked(true);
		ls.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEMOVE_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(ls);

		return cwaLine;
	}

	/**
	 * Creates a meter chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */

	public static final Chart showTooltip_MeterChart() {
		DialChart dChart = (DialChart) DialChartImpl.create();
		dChart.setDialSuperimposition(false);
		dChart.setGridColumnCount(2);
		dChart.setSeriesThickness(25);

		// Title/Plot
		dChart.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = dChart.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.CREAM());
		p.getClientArea().getOutline().setVisible(false);
		p.getOutline().setVisible(false);

		dChart.getTitle().getLabel().getCaption().setValue("Meter Chart");
		dChart.getTitle().getOutline().setVisible(false);

		// Legend
		Legend lg = dChart.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().setLeft(10);
		lg.getInsets().setRight(10);
		lg.setBackground(null);
		lg.getOutline().setVisible(false);
		lg.setShowValue(true);
		lg.getClientArea().setBackground(ColorDefinitionImpl.PINK());

		lg.getClientArea().getOutline().setVisible(true);
		lg.getTitle().getCaption().getFont().setSize(20);
		lg.getTitle().setInsets(InsetsImpl.create(10, 10, 10, 10));
		lg.setTitlePosition(Position.ABOVE_LITERAL);
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Moto" });

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sd);
		Series seCategory = (Series) SeriesImpl.create();

		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

		final Fill[] fiaOrth = { ColorDefinitionImpl.PINK(), ColorDefinitionImpl.ORANGE(),
				ColorDefinitionImpl.WHITE() };
		sdCity.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaOrth.length; i++) {
			sdCity.getSeriesPalette().getEntries().add(fiaOrth[i]);
		}

		// Dial 1
		DialSeries seDial1 = (DialSeries) DialSeriesImpl.create();
		seDial1.setDataSet(NumberDataSetImpl.create(new double[] { 20 }));
		seDial1.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial1.setSeriesIdentifier("Temperature");
		seDial1.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial1.getDial().setStartAngle(-45);
		seDial1.getDial().setStopAngle(225);
		seDial1.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial1.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial1.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial1.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial1.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial1.getDial().getScale().setStep(10);
		seDial1.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial1.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());
		seDial1.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		DialRegion dregion1 = DialRegionImpl.create();
		dregion1.setFill(ColorDefinitionImpl.GREEN());
		dregion1.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion1.setStartValue(NumberDataElementImpl.create(70));
		dregion1.setEndValue(NumberDataElementImpl.create(90));
		dregion1.setInnerRadius(40);
		dregion1.setOuterRadius(-1);
		seDial1.getDial().getDialRegions().add(dregion1);

		DialRegion dregion2 = DialRegionImpl.create();
		dregion2.setFill(ColorDefinitionImpl.YELLOW());
		dregion2.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion2.setStartValue(NumberDataElementImpl.create(40));
		dregion2.setEndValue(NumberDataElementImpl.create(70));
		dregion2.setOuterRadius(70);
		seDial1.getDial().getDialRegions().add(dregion2);

		DialRegion dregion3 = DialRegionImpl.create();
		dregion3.setFill(ColorDefinitionImpl.RED());
		dregion3.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion3.setStartValue(NumberDataElementImpl.create(0));
		dregion3.setEndValue(NumberDataElementImpl.create(40));
		dregion3.setInnerRadius(40);
		dregion3.setOuterRadius(90);
		seDial1.getDial().getDialRegions().add(dregion3);

		// Dial 2
		DialSeries seDial2 = (DialSeries) DialSeriesImpl.create();
		seDial2.setDataSet(NumberDataSetImpl.create(new double[] { 58 }));
		seDial2.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial2.setSeriesIdentifier("Wind Speed");
		seDial2.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial2.getDial().setStartAngle(-45);
		seDial2.getDial().setStopAngle(225);
		seDial2.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial2.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial2.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial2.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial2.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial2.getDial().getScale().setStep(10);
		seDial2.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial2.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());
		seDial2.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		seDial2.getDial().getDialRegions().add(dregion1);
		seDial2.getDial().getDialRegions().add(dregion2);
		seDial2.getDial().getDialRegions().add(dregion3);

		// Dial 3
		DialSeries seDial3 = (DialSeries) DialSeriesImpl.create();
		seDial3.setDataSet(NumberDataSetImpl.create(new double[] { 80 }));
		seDial3.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial3.setSeriesIdentifier("Viscosity");
		seDial3.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial3.getDial().setStartAngle(-45);
		seDial3.getDial().setStopAngle(225);
		seDial3.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial3.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial3.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial3.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial3.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial3.getDial().getScale().setStep(10);
		seDial3.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial3.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());
		seDial3.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		seDial3.getDial().getDialRegions().add(dregion1);
		seDial3.getDial().getDialRegions().add(dregion2);
		seDial3.getDial().getDialRegions().add(dregion3);

		dChart.setDialSuperimposition(true);
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial1);
		sdCity.getSeries().add(seDial2);
		sdCity.getSeries().add(seDial3);

		return dChart;

	}

	/**
	 * Creates a pie chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */

	public static final Chart showTooltip_PieChart() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();

		// Chart Type
		cwoaPie.setType("Pie Chart");
		cwoaPie.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);

		// Title
		cwoaPie.getTitle().getLabel().getCaption().setValue("Sample Pie Chart");
		cwoaPie.getBlock().setBounds(BoundsImpl.create(0, 0, 252, 288));
		cwoaPie.getBlock().getOutline().setVisible(true);

		// Plot
		cwoaPie.getPlot().getClientArea().getOutline().setVisible(false);
		cwoaPie.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwoaPie.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DASH_DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.EAST_LITERAL);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.LEFT_LITERAL);
		lg.setOrientation(Orientation.VERTICAL_LITERAL);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });

		// Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(dsStringValue);

		SeriesDefinition series = SeriesDefinitionImpl.create();
		series.getSeries().add(seCategory);
		cwoaPie.getSeriesDefinitions().add(series);

		PieSeries ps = (PieSeries) PieSeriesImpl.create();
		ps.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ps.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ps.getLabel().setVisible(true);
		ps.setSeriesIdentifier("Actuate");
		ps.setDataSet(dsNumericValues1);
		ps.setLeaderLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DASH_DOTTED_LITERAL, 3));
		ps.setLeaderLineStyle(LeaderLineStyle.FIXED_LENGTH_LITERAL);
		ps.setExplosion(5);
		ps.setSliceOutline(ColorDefinitionImpl.BLACK());
		ps.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		SeriesDefinition seGroup1 = SeriesDefinitionImpl.create();
		series.getSeriesPalette().update(-2);
		series.getSeriesDefinitions().add(seGroup1);
		seGroup1.getSeries().add(ps);

		return cwoaPie;

	}

	/**
	 * Creates a scatter chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart showTooltip_ScatterChart() {
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create();

		// Title
		cwaScatter.getTitle().getLabel().getCaption().setValue("Numeric Scatter Chart"); //$NON-NLS-1$
		cwaScatter.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaScatter.getPlot().getClientArea().getOutline().setVisible(false);
		cwaScatter.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Data Set
		NumberDataSet dsNumericValues1 = NumberDataSetImpl
				.create(new double[] { -46.55, 25.32, 84.46, 125.95, 38.65, -54.32, 30 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl
				.create(new double[] { 125.99, 352.95, -201.95, 299.95, -95.95, 65.95, 58.95 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsNumericValues1);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create();
		ss.setSeriesIdentifier("Unit Price"); //$NON-NLS-1$
		for (int i = 0; i < ss.getMarkers().size(); i++) {
			((Marker) ss.getMarkers().get(i)).setType(MarkerType.CIRCLE_LITERAL);
		}
		DataPoint dp = ss.getDataPoint();
		dp.getComponents().clear();
		dp.setPrefix("("); //$NON-NLS-1$
		dp.setSuffix(")"); //$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.BASE_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$

		ss.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ss.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ss.getLabel().setVisible(true);
		ss.setDataSet(dsNumericValues2);
		ss.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEMOVE_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLACK());
		sdY.getSeries().add(ss);

		return cwaScatter;
	}

	/**
	 * Creates a stock chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */

	public static final Chart showTooltip_StockChart() {
		ChartWithAxes cwaStock = ChartWithAxesImpl.create();

		// Title
		cwaStock.getTitle().getLabel().getCaption().setValue("Stock Chart");//$NON-NLS-1$
		TitleBlock tb = cwaStock.getTitle();
		tb.setBackground(GradientImpl.create(ColorDefinitionImpl.create(0, 128, 0),
				ColorDefinitionImpl.create(128, 0, 0), 0, false));
		tb.getLabel().getCaption().setColor(ColorDefinitionImpl.WHITE());

		// Plot
		cwaStock.getBlock().setBackground(
				GradientImpl.create(ColorDefinitionImpl.create(196, 196, 196), ColorDefinitionImpl.WHITE(), 90, false));
		cwaStock.getPlot().getClientArea().getInsets().set(10, 10, 10, 10);

		// Legend
		cwaStock.getLegend().setBackground(ColorDefinitionImpl.ORANGE());

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaStock).getPrimaryBaseAxes()[0];

		xAxisPrimary.getTitle().getCaption().setValue("X Axis");//$NON-NLS-1$
		xAxisPrimary.getTitle().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getTitle().getCaption().setValue("Date");//$NON-NLS-1$
		xAxisPrimary.setTitlePosition(Position.ABOVE_LITERAL);

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(65);
		xAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);

		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(255, 196, 196));
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		xAxisPrimary.setCategoryAxis(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaStock).getPrimaryOrthogonalAxis(xAxisPrimary);

		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");//$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);

		yAxisPrimary.getTitle().getCaption().setValue("Microsoft ($ Stock Price)");//$NON-NLS-1$
		yAxisPrimary.getTitle().getCaption().setColor(ColorDefinitionImpl.BLUE());
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);

		yAxisPrimary.getScale().setMin(NumberDataElementImpl.create(24.5));
		yAxisPrimary.getScale().setMax(NumberDataElementImpl.create(27.5));
		yAxisPrimary.getScale().setStep(0.5);

		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(196, 196, 255));
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Data Set
		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2004, 12, 27), new CDateTime(2004, 12, 23),
						new CDateTime(2004, 12, 22), new CDateTime(2004, 12, 21), new CDateTime(2004, 12, 20),
						new CDateTime(2004, 12, 17), new CDateTime(2004, 12, 16), new CDateTime(2004, 12, 15) });

		StockDataSet dsStockValues = StockDataSetImpl.create(new StockEntry[] {
				new StockEntry(27.01, 27.10, 26.82, 26.85), new StockEntry(26.87, 27.15, 26.83, 27.01),
				new StockEntry(26.84, 27.15, 26.78, 26.97), new StockEntry(27.00, 27.17, 26.94, 27.07),
				new StockEntry(27.01, 27.15, 26.89, 26.95), new StockEntry(27.00, 27.32, 26.80, 26.96),
				new StockEntry(27.15, 27.28, 27.01, 27.16), new StockEntry(27.22, 27.40, 27.07, 27.11), });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(1);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		StockSeries ss = (StockSeries) StockSeriesImpl.create();
		ss.setSeriesIdentifier("Stock Price");//$NON-NLS-1$
		ss.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		ss.setDataSet(dsStockValues);
		ss.setCurveFitting(CurveFittingImpl.create());
		ss.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(ColorDefinitionImpl.CYAN());
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ss);

		return cwaStock;
	}

	public static final Chart toggleVisibility_AreaChart() {
		ChartWithAxes cwaArea = ChartWithAxesImpl.create();

		// Chart Type
		cwaArea.setType("Area Chart");
		cwaArea.setDimension(ChartDimension.THREE_DIMENSIONAL_LITERAL);
		cwaArea.setRotation(
				Rotation3DImpl.create(new Angle3D[] { Angle3DImpl.createY(45), Angle3DImpl.createX(-20), }));

		// Title
		cwaArea.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaArea.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaArea.getPlot().getClientArea().getOutline().setVisible(false);
		cwaArea.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwaArea.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.EAST_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.RIGHT_LITERAL);
		lg.setOrientation(Orientation.VERTICAL_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.TOGGLE_VISIBILITY_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerRange mr = MarkerRangeImpl.create(xAxisPrimary, NumberDataElementImpl.create(2.0),
				NumberDataElementImpl.create(3.0), null);
		mr.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary, NumberDataElementImpl.create(60.0));
		ml.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Z-Axis
		Axis zAxisPrimary = AxisImpl.create(Axis.ANCILLARY_BASE);
		zAxisPrimary.setTitlePosition(Position.ABOVE_LITERAL);
		zAxisPrimary.getTitle().getCaption().setValue("Z Axis Title"); //$NON-NLS-1$
		zAxisPrimary.getTitle().setVisible(true);
		zAxisPrimary.setPrimaryAxis(true);
		FontDefinition fd1 = FontDefinitionImpl.create("Arial", (float) 20.0, true, true, false, true, false, 30.0,
				TextAlignmentImpl.create());
		zAxisPrimary.getLabel().getCaption().setFont(fd1);
		zAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);
		zAxisPrimary.setOrientation(Orientation.HORIZONTAL_LITERAL);
		zAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		zAxisPrimary.getOrigin().setValue(NumberDataElementImpl.create(0));
		zAxisPrimary.getTitle().setVisible(true);
		zAxisPrimary.setType(AxisType.TEXT_LITERAL);
		zAxisPrimary.getMajorGrid().setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.SOLID_LITERAL, 2));
		zAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		zAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		cwaArea.getPrimaryBaseAxes()[0].getAncillaryAxes().add(zAxisPrimary);

		cwaArea.getPrimaryOrthogonalAxis(cwaArea.getPrimaryBaseAxes()[0]).getTitle().getCaption().getFont()
				.setRotation(0);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		AreaSeries as = (AreaSeries) AreaSeriesImpl.create();
		as.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		as.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		as.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		as.getLabel().setVisible(true);
		as.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(207, 41, 207), LineStyle.SOLID_LITERAL, 1));
		as.setDataSet(dsNumericValues1);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		sdY.getSeries().add(as);

		return cwaArea;
	}

	public static final Chart toggleVisibility_BarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Chart Type
		cwaBar.setType("Bar Chart");
		cwaBar.setSubType("Stacked");

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Legend
		Legend lg = cwaBar.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(false);
		lg.setAnchor(Anchor.NORTH_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.TOGGLE_VISIBILITY_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// Plot
		cwaBar.getPlot().getClientArea().getOutline().setVisible(false);
		cwaBar.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		FontDefinition fd = FontDefinitionImpl.create("Arial", (float) 30.0, true, true, false, true, false, 30.0,
				TextAlignmentImpl.create());
		yAxisPrimary.getLabel().getCaption().setFont(fd);
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { 15.29, -14.53, -47.05, 32.55 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		bs.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs.getLabel().setVisible(true);
		bs.setDataSet(dsNumericValues1);
		bs.setStacked(true);
		bs.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEMOVE_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(bs);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Micorsoft"); //$NON-NLS-1$
		bs2.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs2.getLabel().setVisible(true);
		bs2.setDataSet(dsNumericValues2);
		bs2.setStacked(true);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.PINK());
		sdY2.getSeries().add(bs2);

		return cwaBar;
	}

	public static final Chart toggleVisibility_3DLineChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();

		// Chart Type
		cwaLine.setType("Line Chart");
		cwaLine.setDimension(ChartDimension.THREE_DIMENSIONAL_LITERAL);
		cwaLine.setRotation(
				Rotation3DImpl.create(new Angle3D[] { Angle3DImpl.createY(45), Angle3DImpl.createX(-20), }));

		// Title
		cwaLine.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaLine.getPlot().getClientArea().getOutline().setVisible(false);
		cwaLine.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwaLine.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DASH_DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.EAST_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.LEFT_LITERAL);
		lg.setOrientation(Orientation.VERTICAL_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.TOGGLE_VISIBILITY_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.SOLID_LITERAL, 1));

		MarkerLine ml = MarkerLineImpl.create(xAxisPrimary, NumberDataElementImpl.create(2.0));
		ml.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerRange mr = MarkerRangeImpl.create(yAxisPrimary, NumberDataElementImpl.create(60.0),
				NumberDataElementImpl.create(90.0), null);
		mr.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));

		// Z-Axis
		Axis zAxisPrimary = AxisImpl.create(Axis.ANCILLARY_BASE);
		zAxisPrimary.setTitlePosition(Position.ABOVE_LITERAL);
		zAxisPrimary.getTitle().getCaption().setValue("Z Axis Title"); //$NON-NLS-1$
		zAxisPrimary.getTitle().setVisible(true);
		zAxisPrimary.setPrimaryAxis(true);
		FontDefinition fd1 = FontDefinitionImpl.create("Arial", (float) 20.0, true, true, false, true, false, 30.0,
				TextAlignmentImpl.create());
		zAxisPrimary.getLabel().getCaption().setFont(fd1);
		zAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);
		zAxisPrimary.setOrientation(Orientation.HORIZONTAL_LITERAL);
		zAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		zAxisPrimary.getOrigin().setValue(NumberDataElementImpl.create(0));
		zAxisPrimary.getTitle().setVisible(true);
		zAxisPrimary.setType(AxisType.TEXT_LITERAL);
		zAxisPrimary.getMajorGrid().setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.SOLID_LITERAL, 2));
		zAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		zAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		cwaLine.getPrimaryBaseAxes()[0].getAncillaryAxes().add(zAxisPrimary);

		cwaLine.getPrimaryOrthogonalAxis(cwaLine.getPrimaryBaseAxes()[0]).getTitle().getCaption().getFont()
				.setRotation(0);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { 15.29, -14.53, -47.05, 32.55 });
		TextDataSet dsStringValue1 = TextDataSetImpl.create(new String[] { "Actuate", "Microsoft" });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		ls.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ls.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(207, 41, 207), LineStyle.SOLID_LITERAL, 1));
		ls.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ls.getLabel().setVisible(true);
		ls.setDataSet(dsNumericValues1);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(ls);

		LineSeries ls2 = (LineSeries) LineSeriesImpl.create();
		ls2.setSeriesIdentifier("Micorsoft"); //$NON-NLS-1$
		ls2.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());
		ls2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ls2.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(122, 169, 168), LineStyle.DOTTED_LITERAL, 1));
		ls2.getLabel().setVisible(true);
		ls2.setDataSet(dsNumericValues2);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.PINK());
		sdY2.getSeries().add(ls2);

		Series seZ = SeriesImpl.create();
		seZ.setDataSet(dsStringValue1);

		SeriesDefinition sdZ = SeriesDefinitionImpl.create();
		sdZ.getSeriesPalette().update(0);
		sdZ.getSeries().add(SeriesImpl.create());
		zAxisPrimary.getSeriesDefinitions().add(sdZ);
		sdZ.getSeries().add(seZ);
		return cwaLine;

	}

	public static final Chart toggleVisibility_MeterChart() {
		DialChart dChart = (DialChart) DialChartImpl.create();
		dChart.setDialSuperimposition(false);
		dChart.setGridColumnCount(2);
		dChart.setSeriesThickness(25);

		// Title/Plot
		dChart.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = dChart.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.CREAM());
		p.getClientArea().getOutline().setVisible(false);
		p.getOutline().setVisible(false);

		dChart.getTitle().getLabel().getCaption().setValue("Meter Chart");
		dChart.getTitle().getOutline().setVisible(false);

		// Legend
		Legend lg = dChart.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().setLeft(10);
		lg.getInsets().setRight(10);
		lg.setBackground(null);
		lg.getOutline().setVisible(false);
		lg.setShowValue(true);
		lg.getClientArea().setBackground(ColorDefinitionImpl.PINK());

		lg.getClientArea().getOutline().setVisible(true);
		lg.getTitle().getCaption().getFont().setSize(20);
		lg.getTitle().setInsets(InsetsImpl.create(10, 10, 10, 10));
		lg.setTitlePosition(Position.ABOVE_LITERAL);
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.TOGGLE_VISIBILITY_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Moto" });

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sd);
		Series seCategory = (Series) SeriesImpl.create();

		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

		final Fill[] fiaOrth = { ColorDefinitionImpl.PINK(), ColorDefinitionImpl.ORANGE(),
				ColorDefinitionImpl.WHITE() };
		sdCity.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaOrth.length; i++) {
			sdCity.getSeriesPalette().getEntries().add(fiaOrth[i]);
		}

		// Dial 1
		DialSeries seDial1 = (DialSeries) DialSeriesImpl.create();
		seDial1.setDataSet(NumberDataSetImpl.create(new double[] { 20 }));
		seDial1.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial1.setSeriesIdentifier("Temperature");
		seDial1.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial1.getDial().setStartAngle(-45);
		seDial1.getDial().setStopAngle(225);
		seDial1.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial1.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial1.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial1.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial1.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial1.getDial().getScale().setStep(10);
		seDial1.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial1.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		DialRegion dregion1 = DialRegionImpl.create();
		dregion1.setFill(ColorDefinitionImpl.GREEN());
		dregion1.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion1.setStartValue(NumberDataElementImpl.create(70));
		dregion1.setEndValue(NumberDataElementImpl.create(90));
		dregion1.setInnerRadius(40);
		dregion1.setOuterRadius(-1);
		seDial1.getDial().getDialRegions().add(dregion1);

		DialRegion dregion2 = DialRegionImpl.create();
		dregion2.setFill(ColorDefinitionImpl.YELLOW());
		dregion2.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion2.setStartValue(NumberDataElementImpl.create(40));
		dregion2.setEndValue(NumberDataElementImpl.create(70));
		dregion2.setOuterRadius(70);
		seDial1.getDial().getDialRegions().add(dregion2);

		DialRegion dregion3 = DialRegionImpl.create();
		dregion3.setFill(ColorDefinitionImpl.RED());
		dregion3.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion3.setStartValue(NumberDataElementImpl.create(0));
		dregion3.setEndValue(NumberDataElementImpl.create(40));
		dregion3.setInnerRadius(40);
		dregion3.setOuterRadius(90);
		seDial1.getDial().getDialRegions().add(dregion3);

		// Dial 2
		DialSeries seDial2 = (DialSeries) DialSeriesImpl.create();
		seDial2.setDataSet(NumberDataSetImpl.create(new double[] { 58 }));
		seDial2.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial2.setSeriesIdentifier("Wind Speed");
		seDial2.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial2.getDial().setStartAngle(-45);
		seDial2.getDial().setStopAngle(225);
		seDial2.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial2.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial2.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial2.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial2.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial2.getDial().getScale().setStep(10);
		seDial2.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial2.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		seDial2.getDial().getDialRegions().add(dregion1);
		seDial2.getDial().getDialRegions().add(dregion2);
		seDial2.getDial().getDialRegions().add(dregion3);

		// Dial 3
		DialSeries seDial3 = (DialSeries) DialSeriesImpl.create();
		seDial3.setDataSet(NumberDataSetImpl.create(new double[] { 80 }));
		seDial3.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial3.setSeriesIdentifier("Viscosity");
		seDial3.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial3.getDial().setStartAngle(-45);
		seDial3.getDial().setStopAngle(225);
		seDial3.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial3.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial3.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial3.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial3.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial3.getDial().getScale().setStep(10);
		seDial3.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial3.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		seDial3.getDial().getDialRegions().add(dregion1);
		seDial3.getDial().getDialRegions().add(dregion2);
		seDial3.getDial().getDialRegions().add(dregion3);

		dChart.setDialSuperimposition(true);
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial1);
		sdCity.getSeries().add(seDial2);
		sdCity.getSeries().add(seDial3);

		return dChart;

	}

	public static final Chart toggleVisibility_PieChart() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();

		// Chart Type
		cwoaPie.setType("Pie Chart");

		// Title
		cwoaPie.getTitle().getLabel().getCaption().setValue("Sample Pie Chart");
		cwoaPie.getBlock().setBounds(BoundsImpl.create(0, 0, 252, 288));
		cwoaPie.getBlock().getOutline().setVisible(true);

		// Plot
		cwoaPie.getPlot().getClientArea().getOutline().setVisible(false);
		cwoaPie.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwoaPie.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DASH_DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.EAST_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.LEFT_LITERAL);
		lg.setOrientation(Orientation.VERTICAL_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.TOGGLE_VISIBILITY_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });

		// Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(dsStringValue);

		SeriesDefinition series = SeriesDefinitionImpl.create();
		series.getSeries().add(seCategory);
		cwoaPie.getSeriesDefinitions().add(series);

		PieSeries ps = (PieSeries) PieSeriesImpl.create();
		ps.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ps.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ps.getLabel().setVisible(true);
		// ps.getTitle().getCaption().setValue("sss");
		ps.setSeriesIdentifier("Actuate");
		ps.setDataSet(dsNumericValues1);
		ps.setLeaderLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DASH_DOTTED_LITERAL, 3));
		ps.setLeaderLineStyle(LeaderLineStyle.FIXED_LENGTH_LITERAL);
		ps.setExplosion(0);
		ps.setSliceOutline(ColorDefinitionImpl.BLACK());

		SeriesDefinition seGroup1 = SeriesDefinitionImpl.create();
		series.getSeriesPalette().update(-2);
		series.getSeriesDefinitions().add(seGroup1);
		seGroup1.getSeries().add(ps);

		return cwoaPie;

	}

	public static final Chart toggleVisibility_ScatterChart() {
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create();

		// Chart Type
		cwaScatter.setType("Scatter Chart");

		// Title
		cwaScatter.getTitle().getLabel().getCaption().setValue("Sample Scatter Chart"); //$NON-NLS-1$
		cwaScatter.getBlock().setBackground(ColorDefinitionImpl.GREY());

		// Plot
		Plot p = cwaScatter.getPlot();

		p.getOutline().setStyle(LineStyle.DASH_DOTTED_LITERAL);
		p.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		p.getOutline().setVisible(true);

		p.setBackground(ColorDefinitionImpl.CREAM());
		p.setAnchor(Anchor.NORTH_LITERAL);
		p.getClientArea().getOutline().setVisible(true);

		// Legend
		Legend lg = cwaScatter.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.SOUTH_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setOrientation(Orientation.HORIZONTAL_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.TOGGLE_VISIBILITY_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLACK().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().setTickAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerRange mr = MarkerRangeImpl.create(xAxisPrimary, NumberDataElementImpl.create(2.0),
				NumberDataElementImpl.create(3.0), null);
		mr.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue(""); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setPercent(true);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary, NumberDataElementImpl.create(60.0));
		ml.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Data Set
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 22.49, 163.55, -65.43, 0.0, -107.0 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { -36.53, 43.9, 8.29, 97.45, 32.0 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsNumericValues1);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(3);

		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create();

		DataPoint dp = ss.getDataPoint();
		dp.getComponents().clear();
		dp.setPrefix("("); //$NON-NLS-1$
		dp.setSuffix(")"); //$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.BASE_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$

		ss.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ss.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ss.getLabel().setVisible(true);

		for (int i = 0; i < ss.getMarkers().size(); i++) {
			((Marker) ss.getMarkers().get(i)).setVisible(true);
		}

		ss.setDataSet(dsNumericValues2);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		sdY.getSeries().add(ss);

		return cwaScatter;

	}

	public static final Chart toggleVisibility_StockChart() {
		ChartWithAxes cwaStock = ChartWithAxesImpl.create();

		// Title
		cwaStock.getTitle().getLabel().getCaption().setValue("Stock Chart");//$NON-NLS-1$
		TitleBlock tb = cwaStock.getTitle();
		tb.setBackground(GradientImpl.create(ColorDefinitionImpl.create(0, 128, 0),
				ColorDefinitionImpl.create(128, 0, 0), 0, false));
		tb.getLabel().getCaption().setColor(ColorDefinitionImpl.WHITE());

		// Plot
		cwaStock.getBlock().setBackground(
				GradientImpl.create(ColorDefinitionImpl.create(196, 196, 196), ColorDefinitionImpl.WHITE(), 90, false));
		cwaStock.getPlot().getClientArea().getInsets().set(10, 10, 10, 10);

		// Legend
		Legend lg = cwaStock.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setOrientation(Orientation.HORIZONTAL_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.TOGGLE_VISIBILITY_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaStock).getPrimaryBaseAxes()[0];

		xAxisPrimary.getTitle().getCaption().setValue("X Axis");//$NON-NLS-1$
		xAxisPrimary.getTitle().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getTitle().getCaption().setValue("Date");//$NON-NLS-1$
		xAxisPrimary.setTitlePosition(Position.ABOVE_LITERAL);

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(65);
		xAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);

		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(255, 196, 196));
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		xAxisPrimary.setCategoryAxis(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaStock).getPrimaryOrthogonalAxis(xAxisPrimary);

		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");//$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);

		yAxisPrimary.getTitle().getCaption().setValue("Microsoft ($ Stock Price)");//$NON-NLS-1$
		yAxisPrimary.getTitle().getCaption().setColor(ColorDefinitionImpl.BLUE());
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);

		yAxisPrimary.getScale().setMin(NumberDataElementImpl.create(24.5));
		yAxisPrimary.getScale().setMax(NumberDataElementImpl.create(27.5));
		yAxisPrimary.getScale().setStep(0.5);

		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(196, 196, 255));
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Data Set
		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2004, 12, 27), new CDateTime(2004, 12, 23),
						new CDateTime(2004, 12, 22), new CDateTime(2004, 12, 21), new CDateTime(2004, 12, 20),
						new CDateTime(2004, 12, 17), new CDateTime(2004, 12, 16), new CDateTime(2004, 12, 15) });

		StockDataSet dsStockValues = StockDataSetImpl.create(new StockEntry[] {
				new StockEntry(27.01, 27.10, 26.82, 26.85), new StockEntry(26.87, 27.15, 26.83, 27.01),
				new StockEntry(26.84, 27.15, 26.78, 26.97), new StockEntry(27.00, 27.17, 26.94, 27.07),
				new StockEntry(27.01, 27.15, 26.89, 26.95), new StockEntry(27.00, 27.32, 26.80, 26.96),
				new StockEntry(27.15, 27.28, 27.01, 27.16), new StockEntry(27.22, 27.40, 27.07, 27.11), });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(1);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		StockSeries ss = (StockSeries) StockSeriesImpl.create();
		ss.setSeriesIdentifier("Stock Price");//$NON-NLS-1$
		ss.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		ss.setDataSet(dsStockValues);
		ss.setCurveFitting(CurveFittingImpl.create());

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(ColorDefinitionImpl.CYAN());
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ss);

		return cwaStock;
	}

	public static final Chart highlight_BarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Chart Type
		cwaBar.setType("Bar Chart");
		cwaBar.setSubType("Stacked");

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Legend
		Legend lg = cwaBar.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(false);
		lg.setAnchor(Anchor.NORTH_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// Plot
		cwaBar.getPlot().getClientArea().getOutline().setVisible(false);
		cwaBar.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		FontDefinition fd = FontDefinitionImpl.create("Arial", (float) 30.0, true, true, false, true, false, 30.0,
				TextAlignmentImpl.create());
		yAxisPrimary.getLabel().getCaption().setFont(fd);
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { 15.29, -14.53, -47.05, 32.55 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		bs.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs.getLabel().setVisible(true);
		bs.setDataSet(dsNumericValues1);
		bs.setStacked(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(bs);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Micorsoft"); //$NON-NLS-1$
		bs2.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs2.getLabel().setVisible(true);
		bs2.setDataSet(dsNumericValues2);
		bs2.setStacked(true);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.PINK());
		sdY2.getSeries().add(bs2);

		return cwaBar;
	}

	public static final Chart highlight_PieChart() {

		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();

		// Plot
		cwoaPie.setSeriesThickness(25);
		cwoaPie.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwoaPie.getPlot();
		p.getClientArea().setBackground(null);
		p.getClientArea().getOutline().setVisible(true);
		p.getOutline().setVisible(true);

		// Legend
		Legend lg = cwoaPie.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setBackground(null);
		lg.getOutline().setVisible(true);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// Title
		cwoaPie.getTitle().getLabel().getCaption().setValue("Pie Chart"); //$NON-NLS-1$
		cwoaPie.getTitle().getOutline().setVisible(true);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "New York", "Boston", "Chicago", "San Francisco", "Dallas" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				});
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] { 54.65, 21, 75.95, 91.28, 37.43 });

		// Base Series
		Series seCategory = (Series) SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().update(0);
		sd.getSeries().add(seCategory);

		// Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesOneValues);
		sePie.setSeriesIdentifier("Cities"); //$NON-NLS-1$

		Trigger triger = TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create("not-used")));
		sePie.getTriggers().add(triger);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sdCity.getQuery().setDefinition("Census.City"); //$NON-NLS-1$
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie);

		return cwoaPie;
	}

	public static final Chart highlight_AreaChart() {
		ChartWithAxes cwaArea = ChartWithAxesImpl.create();

		// Chart Type
		cwaArea.setType("Area Chart");

		// Title
		cwaArea.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaArea.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaArea.getPlot().getClientArea().getOutline().setVisible(false);
		cwaArea.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwaArea.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.EAST_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.RIGHT_LITERAL);
		lg.setOrientation(Orientation.VERTICAL_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerRange mr = MarkerRangeImpl.create(xAxisPrimary, NumberDataElementImpl.create(2.0),
				NumberDataElementImpl.create(3.0), null);
		mr.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary, NumberDataElementImpl.create(60.0));
		ml.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		AreaSeries as = (AreaSeries) AreaSeriesImpl.create();
		as.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		as.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		as.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		as.getLabel().setVisible(true);
		as.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(207, 41, 207), LineStyle.SOLID_LITERAL, 1));
		as.setDataSet(dsNumericValues1);

		for (int i = 0; i < as.getMarkers().size(); i++) {
			((Marker) as.getMarkers().get(i)).setVisible(true);
		}

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		sdY.getSeries().add(as);

		return cwaArea;
	}

	public static final Chart highlight_3DLineChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();

		// Chart Type
		cwaLine.setType("Line Chart");
		cwaLine.setDimension(ChartDimension.THREE_DIMENSIONAL_LITERAL);
		cwaLine.setRotation(
				Rotation3DImpl.create(new Angle3D[] { Angle3DImpl.createY(45), Angle3DImpl.createX(-20), }));

		// Title
		cwaLine.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaLine.getPlot().getClientArea().getOutline().setVisible(false);
		cwaLine.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwaLine.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DASH_DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.EAST_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.LEFT_LITERAL);
		lg.setOrientation(Orientation.VERTICAL_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.SOLID_LITERAL, 1));

		MarkerLine ml = MarkerLineImpl.create(xAxisPrimary, NumberDataElementImpl.create(2.0));
		ml.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerRange mr = MarkerRangeImpl.create(yAxisPrimary, NumberDataElementImpl.create(60.0),
				NumberDataElementImpl.create(90.0), null);
		mr.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));

		// Z-Axis
		Axis zAxisPrimary = AxisImpl.create(Axis.ANCILLARY_BASE);
		zAxisPrimary.setTitlePosition(Position.ABOVE_LITERAL);
		zAxisPrimary.getTitle().getCaption().setValue("Z Axis Title"); //$NON-NLS-1$
		zAxisPrimary.getTitle().setVisible(true);
		zAxisPrimary.setPrimaryAxis(true);
		FontDefinition fd1 = FontDefinitionImpl.create("Arial", (float) 20.0, true, true, false, true, false, 30.0,
				TextAlignmentImpl.create());
		zAxisPrimary.getLabel().getCaption().setFont(fd1);
		zAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);
		zAxisPrimary.setOrientation(Orientation.HORIZONTAL_LITERAL);
		zAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		zAxisPrimary.getOrigin().setValue(NumberDataElementImpl.create(0));
		zAxisPrimary.getTitle().setVisible(true);
		zAxisPrimary.setType(AxisType.TEXT_LITERAL);
		zAxisPrimary.getMajorGrid().setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.SOLID_LITERAL, 2));
		zAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		zAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		cwaLine.getPrimaryBaseAxes()[0].getAncillaryAxes().add(zAxisPrimary);

		cwaLine.getPrimaryOrthogonalAxis(cwaLine.getPrimaryBaseAxes()[0]).getTitle().getCaption().getFont()
				.setRotation(0);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { 15.29, -14.53, -47.05, 32.55 });
		TextDataSet dsStringValue1 = TextDataSetImpl.create(new String[] { "Actuate", "Microsoft" });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setSeriesIdentifier("Actuate"); //$NON-NLS-1$

		for (int i = 0; i < ls.getMarkers().size(); i++) {
			((Marker) ls.getMarkers().get(i)).setType(MarkerType.ICON_LITERAL);
//			( (Marker) ls.getMarkers( ).get( i ) ).setFill( ImageImpl
//					.create( "file:///" + System.getProperty( "user.dir" )
//							+ "/27.gif" ) );
			((Marker) ls.getMarkers().get(i)).setFill(ImageImpl
					.create("file:src/" + getClassName().replace('.', '/') + "/" + INPUT_FOLDER + "/" + "27.gif"));
		}

		ls.setDataSet(dsNumericValues1);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(ls);

		LineSeries ls2 = (LineSeries) LineSeriesImpl.create();
		ls2.setSeriesIdentifier("Micorsoft"); //$NON-NLS-1$
		ls2.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());
		ls2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ls2.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(122, 169, 168), LineStyle.DOTTED_LITERAL, 1));
		ls2.getLabel().setVisible(true);
		ls2.setDataSet(dsNumericValues2);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.PINK());
		sdY2.getSeries().add(ls2);

		Series seZ = SeriesImpl.create();
		seZ.setDataSet(dsStringValue1);

		SeriesDefinition sdZ = SeriesDefinitionImpl.create();
		sdZ.getSeriesPalette().update(0);
		sdZ.getSeries().add(SeriesImpl.create());
		zAxisPrimary.getSeriesDefinitions().add(sdZ);
		sdZ.getSeries().add(seZ);
		return cwaLine;

	}

	public static final Chart highlight_MeterChart() {
		DialChart dChart = (DialChart) DialChartImpl.create();
		dChart.setDialSuperimposition(false);
		dChart.setGridColumnCount(2);
		dChart.setSeriesThickness(25);

		// Title/Plot
		dChart.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = dChart.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.CREAM());
		p.getClientArea().getOutline().setVisible(false);
		p.getOutline().setVisible(false);

		dChart.getTitle().getLabel().getCaption().setValue("Meter Chart");
		dChart.getTitle().getOutline().setVisible(false);

		// Legend
		Legend lg = dChart.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().setLeft(10);
		lg.getInsets().setRight(10);
		lg.setBackground(null);
		lg.getOutline().setVisible(false);
		lg.setShowValue(true);
		lg.getClientArea().setBackground(ColorDefinitionImpl.PINK());

		lg.getClientArea().getOutline().setVisible(true);
		lg.getTitle().getCaption().getFont().setSize(20);
		lg.getTitle().setInsets(InsetsImpl.create(10, 10, 10, 10));
		lg.setTitlePosition(Position.ABOVE_LITERAL);
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Moto" });

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sd);
		Series seCategory = (Series) SeriesImpl.create();

		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

		final Fill[] fiaOrth = { ColorDefinitionImpl.PINK(), ColorDefinitionImpl.ORANGE(),
				ColorDefinitionImpl.WHITE() };
		sdCity.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaOrth.length; i++) {
			sdCity.getSeriesPalette().getEntries().add(fiaOrth[i]);
		}

		// Dial 1
		DialSeries seDial1 = (DialSeries) DialSeriesImpl.create();
		seDial1.setDataSet(NumberDataSetImpl.create(new double[] { 20 }));
		seDial1.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial1.setSeriesIdentifier("Temperature");
		seDial1.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial1.getDial().setStartAngle(-45);
		seDial1.getDial().setStopAngle(225);
		seDial1.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial1.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial1.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial1.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial1.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial1.getDial().getScale().setStep(10);
		seDial1.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial1.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		DialRegion dregion1 = DialRegionImpl.create();
		dregion1.setFill(ColorDefinitionImpl.GREEN());
		dregion1.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion1.setStartValue(NumberDataElementImpl.create(70));
		dregion1.setEndValue(NumberDataElementImpl.create(90));
		dregion1.setInnerRadius(40);
		dregion1.setOuterRadius(-1);
		seDial1.getDial().getDialRegions().add(dregion1);

		DialRegion dregion2 = DialRegionImpl.create();
		dregion2.setFill(ColorDefinitionImpl.YELLOW());
		dregion2.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion2.setStartValue(NumberDataElementImpl.create(40));
		dregion2.setEndValue(NumberDataElementImpl.create(70));
		dregion2.setOuterRadius(70);
		seDial1.getDial().getDialRegions().add(dregion2);

		DialRegion dregion3 = DialRegionImpl.create();
		dregion3.setFill(ColorDefinitionImpl.RED());
		dregion3.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion3.setStartValue(NumberDataElementImpl.create(0));
		dregion3.setEndValue(NumberDataElementImpl.create(40));
		dregion3.setInnerRadius(40);
		dregion3.setOuterRadius(90);
		seDial1.getDial().getDialRegions().add(dregion3);

		// Dial 2
		DialSeries seDial2 = (DialSeries) DialSeriesImpl.create();
		seDial2.setDataSet(NumberDataSetImpl.create(new double[] { 58 }));
		seDial2.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial2.setSeriesIdentifier("Wind Speed");
		seDial2.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial2.getDial().setStartAngle(-45);
		seDial2.getDial().setStopAngle(225);
		seDial2.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial2.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial2.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial2.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial2.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial2.getDial().getScale().setStep(10);
		seDial2.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial2.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		seDial2.getDial().getDialRegions().add(dregion1);
		seDial2.getDial().getDialRegions().add(dregion2);
		seDial2.getDial().getDialRegions().add(dregion3);

		// Dial 3
		DialSeries seDial3 = (DialSeries) DialSeriesImpl.create();
		seDial3.setDataSet(NumberDataSetImpl.create(new double[] { 80 }));
		seDial3.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial3.setSeriesIdentifier("Viscosity");
		seDial3.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial3.getDial().setStartAngle(-45);
		seDial3.getDial().setStopAngle(225);
		seDial3.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial3.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial3.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial3.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial3.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial3.getDial().getScale().setStep(10);
		seDial3.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial3.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		seDial3.getDial().getDialRegions().add(dregion1);
		seDial3.getDial().getDialRegions().add(dregion2);
		seDial3.getDial().getDialRegions().add(dregion3);

		dChart.setDialSuperimposition(true);
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial1);
		sdCity.getSeries().add(seDial2);
		sdCity.getSeries().add(seDial3);

		return dChart;

	}

	public static final Chart highlight_ScatterChart() {
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create();

		// Title
		cwaScatter.getTitle().getLabel().getCaption().setValue("Numeric Scatter Chart"); //$NON-NLS-1$
		cwaScatter.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaScatter.getPlot().getClientArea().getOutline().setVisible(false);
		cwaScatter.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Plot p = cwaScatter.getPlot();

		p.getOutline().setStyle(LineStyle.DASH_DOTTED_LITERAL);
		p.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		p.getOutline().setVisible(true);

		p.setBackground(ColorDefinitionImpl.CREAM());
		p.setAnchor(Anchor.NORTH_LITERAL);
		p.getClientArea().getOutline().setVisible(true);

		// Legend
		Legend lg = cwaScatter.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.SOUTH_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setOrientation(Orientation.HORIZONTAL_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Data Set
		NumberDataSet dsNumericValues1 = NumberDataSetImpl
				.create(new double[] { -46.55, 25.32, 84.46, 125.95, 38.65, -54.32, 30 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl
				.create(new double[] { 125.99, 352.95, -201.95, 299.95, -95.95, 65.95, 58.95 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsNumericValues1);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create();
		ss.setSeriesIdentifier("Unit Price"); //$NON-NLS-1$

		for (int i = 0; i < ss.getMarkers().size(); i++) {
			((Marker) ss.getMarkers().get(i)).setType(MarkerType.CIRCLE_LITERAL);
		}

		DataPoint dp = ss.getDataPoint();
		dp.getComponents().clear();
		dp.setPrefix("("); //$NON-NLS-1$
		dp.setSuffix(")"); //$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.BASE_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$

		ss.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ss.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ss.getLabel().setVisible(true);
		ss.setDataSet(dsNumericValues2);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLACK());
		sdY.getSeries().add(ss);

		return cwaScatter;
	}

	public static final Chart highlight_StockChart() {
		ChartWithAxes cwaStock = ChartWithAxesImpl.create();

		// Title
		cwaStock.getTitle().getLabel().getCaption().setValue("Stock Chart");//$NON-NLS-1$
		TitleBlock tb = cwaStock.getTitle();
		tb.setBackground(GradientImpl.create(ColorDefinitionImpl.create(0, 128, 0),
				ColorDefinitionImpl.create(128, 0, 0), 0, false));
		tb.getLabel().getCaption().setColor(ColorDefinitionImpl.WHITE());

		// Plot
		cwaStock.getBlock().setBackground(
				GradientImpl.create(ColorDefinitionImpl.create(196, 196, 196), ColorDefinitionImpl.WHITE(), 90, false));
		cwaStock.getPlot().getClientArea().getInsets().set(10, 10, 10, 10);

		// Legend
		Legend lg = cwaStock.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setOrientation(Orientation.HORIZONTAL_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaStock).getPrimaryBaseAxes()[0];

		xAxisPrimary.getTitle().getCaption().setValue("X Axis");//$NON-NLS-1$
		xAxisPrimary.getTitle().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getTitle().getCaption().setValue("Date");//$NON-NLS-1$
		xAxisPrimary.setTitlePosition(Position.ABOVE_LITERAL);

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(65);
		xAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);

		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(255, 196, 196));
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		xAxisPrimary.setCategoryAxis(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaStock).getPrimaryOrthogonalAxis(xAxisPrimary);

		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");//$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);

		yAxisPrimary.getTitle().getCaption().setValue("Microsoft ($ Stock Price)");//$NON-NLS-1$
		yAxisPrimary.getTitle().getCaption().setColor(ColorDefinitionImpl.BLUE());
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);

		yAxisPrimary.getScale().setMin(NumberDataElementImpl.create(24.5));
		yAxisPrimary.getScale().setMax(NumberDataElementImpl.create(27.5));
		yAxisPrimary.getScale().setStep(0.5);

		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(196, 196, 255));
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Data Set
		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2004, 12, 27), new CDateTime(2004, 12, 23),
						new CDateTime(2004, 12, 22), new CDateTime(2004, 12, 21), new CDateTime(2004, 12, 20),
						new CDateTime(2004, 12, 17), new CDateTime(2004, 12, 16), new CDateTime(2004, 12, 15) });

		StockDataSet dsStockValues = StockDataSetImpl.create(new StockEntry[] {
				new StockEntry(27.01, 27.10, 26.82, 26.85), new StockEntry(26.87, 27.15, 26.83, 27.01),
				new StockEntry(26.84, 27.15, 26.78, 26.97), new StockEntry(27.00, 27.17, 26.94, 27.07),
				new StockEntry(27.01, 27.15, 26.89, 26.95), new StockEntry(27.00, 27.32, 26.80, 26.96),
				new StockEntry(27.15, 27.28, 27.01, 27.16), new StockEntry(27.22, 27.40, 27.07, 27.11), });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(1);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		StockSeries ss = (StockSeries) StockSeriesImpl.create();
		ss.setSeriesIdentifier("Stock Price");//$NON-NLS-1$
		ss.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		ss.setDataSet(dsStockValues);
		ss.setCurveFitting(CurveFittingImpl.create());

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(ColorDefinitionImpl.CYAN());
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ss);

		return cwaStock;
	}

	public static final Chart URLRedirect_PieChart() {

		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();

		// Plot
		cwoaPie.setSeriesThickness(25);
		cwoaPie.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwoaPie.getPlot();
		p.getClientArea().setBackground(null);
		p.getClientArea().getOutline().setVisible(true);
		p.getOutline().setVisible(true);

		// Legend
		Legend lg = cwoaPie.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setBackground(null);
		lg.getOutline().setVisible(true);

		// Title
		cwoaPie.getTitle().getLabel().getCaption().setValue("Pie Chart"); //$NON-NLS-1$
		cwoaPie.getTitle().getOutline().setVisible(true);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "New York", "Boston", "Chicago", "San Francisco", "Dallas" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				});
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] { 54.65, 21, 75.95, 91.28, 37.43 });

		// Base Series
		Series seCategory = (Series) SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().update(0);
		sd.getSeries().add(seCategory);

		// Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesOneValues);
		sePie.setSeriesIdentifier("Cities"); //$NON-NLS-1$

		Trigger triger = TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.URL_REDIRECT_LITERAL, URLValueImpl.create("http://www.actuate.com", //$NON-NLS-1$
						null, null, // $NON-NLS-1$
						null, // $NON-NLS-1$
						null)));
		sePie.getTriggers().add(triger);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sdCity.getQuery().setDefinition("Census.City"); //$NON-NLS-1$
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie);

		return cwoaPie;
	}

	public static final Chart URLRedirect_AreaChart() {
		ChartWithAxes cwaArea = ChartWithAxesImpl.create();

		// Chart Type
		cwaArea.setType("Area Chart");

		// Title
		cwaArea.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaArea.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaArea.getPlot().getClientArea().getOutline().setVisible(false);
		cwaArea.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwaArea.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.EAST_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.RIGHT_LITERAL);
		lg.setOrientation(Orientation.VERTICAL_LITERAL);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerRange mr = MarkerRangeImpl.create(xAxisPrimary, NumberDataElementImpl.create(2.0),
				NumberDataElementImpl.create(3.0), null);
		mr.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaArea).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary, NumberDataElementImpl.create(60.0));
		ml.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		AreaSeries as = (AreaSeries) AreaSeriesImpl.create();
		as.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		as.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		as.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		as.getLabel().setVisible(true);
		as.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(207, 41, 207), LineStyle.SOLID_LITERAL, 1));
		as.setDataSet(dsNumericValues1);

		for (int i = 0; i < as.getMarkers().size(); i++) {
			((Marker) as.getMarkers().get(i)).setType(MarkerType.CIRCLE_LITERAL);
		}

		Trigger triger = TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.URL_REDIRECT_LITERAL, URLValueImpl.create("http://www.actuate.com", //$NON-NLS-1$
						null, null, // $NON-NLS-1$
						null, // $NON-NLS-1$
						null)));
		as.getTriggers().add(triger);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		sdY.getSeries().add(as);

		return cwaArea;
	}

	public static final Chart URLRedirect_BarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Chart Type
		cwaBar.setType("Bar Chart");
		cwaBar.setSubType("Stacked");

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaBar.getPlot().getClientArea().getOutline().setVisible(false);
		cwaBar.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		FontDefinition fd = FontDefinitionImpl.create("Arial", (float) 30.0, true, true, false, true, false, 30.0,
				TextAlignmentImpl.create());
		yAxisPrimary.getLabel().getCaption().setFont(fd);
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { 15.29, -14.53, -47.05, 32.55 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		bs.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs.getLabel().setVisible(true);
		bs.setDataSet(dsNumericValues1);
		bs.setStacked(true);
		Trigger triger = TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.URL_REDIRECT_LITERAL, URLValueImpl.create("http://www.actuate.com", //$NON-NLS-1$
						null, null, // $NON-NLS-1$
						null, // $NON-NLS-1$
						null)));
		bs.getTriggers().add(triger);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(bs);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Micorsoft"); //$NON-NLS-1$
		bs2.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs2.getLabel().setVisible(true);
		bs2.setDataSet(dsNumericValues2);
		bs2.setStacked(true);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.PINK());
		sdY2.getSeries().add(bs2);

		return cwaBar;
	}

	public static final Chart URLRedirect_LineChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();

		// Chart Type
		cwaLine.setType("Line Chart");

		// Title
		cwaLine.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaLine.getPlot().getClientArea().getOutline().setVisible(false);
		cwaLine.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		Legend lg = cwaLine.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.SOUTH_LITERAL);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setOrientation(Orientation.HORIZONTAL_LITERAL);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.SOLID_LITERAL, 1));

		MarkerLine ml = MarkerLineImpl.create(xAxisPrimary, NumberDataElementImpl.create(2.0));
		ml.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(17, 37, 223), LineStyle.SOLID_LITERAL, 1));

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLine).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		MarkerRange mr = MarkerRangeImpl.create(yAxisPrimary, NumberDataElementImpl.create(60.0),
				NumberDataElementImpl.create(90.0), null);
		mr.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.create(239, 33, 3), LineStyle.DOTTED_LITERAL, 2));

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		ls.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ls.setLineAttributes(
				LineAttributesImpl.create(ColorDefinitionImpl.create(220, 50, 227), LineStyle.DOTTED_LITERAL, 3));
		ls.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ls.getLabel().setVisible(true);
		ls.setDataSet(dsNumericValues1);
		ls.setStacked(true);
		Trigger triger = TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.URL_REDIRECT_LITERAL, URLValueImpl.create("http://www.actuate.com", //$NON-NLS-1$
						null, null, // $NON-NLS-1$
						null, // $NON-NLS-1$
						null)));
		ls.getTriggers().add(triger);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(ls);

		return cwaLine;
	}

	public static final Chart URLRedirect_MeterChart() {
		DialChart dChart = (DialChart) DialChartImpl.create();
		dChart.setDialSuperimposition(false);
		dChart.setGridColumnCount(2);
		dChart.setSeriesThickness(25);

		// Title/Plot
		dChart.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = dChart.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.CREAM());
		p.getClientArea().getOutline().setVisible(false);
		p.getOutline().setVisible(false);

		dChart.getTitle().getLabel().getCaption().setValue("Meter Chart");
		dChart.getTitle().getOutline().setVisible(false);

		// Legend
		Legend lg = dChart.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().setLeft(10);
		lg.getInsets().setRight(10);
		lg.setBackground(null);
		lg.getOutline().setVisible(false);
		lg.setShowValue(true);
		lg.getClientArea().setBackground(ColorDefinitionImpl.PINK());

		lg.getClientArea().getOutline().setVisible(true);
		lg.getTitle().getCaption().getFont().setSize(20);
		lg.getTitle().setInsets(InsetsImpl.create(10, 10, 10, 10));
		lg.setTitlePosition(Position.ABOVE_LITERAL);
		lg.setPosition(Position.BELOW_LITERAL);
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Moto" });

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sd);
		Series seCategory = (Series) SeriesImpl.create();

		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

		final Fill[] fiaOrth = { ColorDefinitionImpl.PINK(), ColorDefinitionImpl.ORANGE(),
				ColorDefinitionImpl.WHITE() };
		sdCity.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaOrth.length; i++) {
			sdCity.getSeriesPalette().getEntries().add(fiaOrth[i]);
		}

		// Dial 1
		DialSeries seDial1 = (DialSeries) DialSeriesImpl.create();
		seDial1.setDataSet(NumberDataSetImpl.create(new double[] { 20 }));
		seDial1.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial1.setSeriesIdentifier("Temperature");
		seDial1.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial1.getDial().setStartAngle(-45);
		seDial1.getDial().setStopAngle(225);
		seDial1.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial1.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial1.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial1.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial1.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial1.getDial().getScale().setStep(10);
		seDial1.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial1.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());
		Trigger triger = TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.URL_REDIRECT_LITERAL, URLValueImpl.create("http://www.actuate.com", //$NON-NLS-1$
						null, null, // $NON-NLS-1$
						null, // $NON-NLS-1$
						null)));
		seDial1.getTriggers().add(triger);

		DialRegion dregion1 = DialRegionImpl.create();
		dregion1.setFill(ColorDefinitionImpl.GREEN());
		dregion1.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion1.setStartValue(NumberDataElementImpl.create(70));
		dregion1.setEndValue(NumberDataElementImpl.create(90));
		dregion1.setInnerRadius(40);
		dregion1.setOuterRadius(-1);
		seDial1.getDial().getDialRegions().add(dregion1);

		DialRegion dregion2 = DialRegionImpl.create();
		dregion2.setFill(ColorDefinitionImpl.YELLOW());
		dregion2.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion2.setStartValue(NumberDataElementImpl.create(40));
		dregion2.setEndValue(NumberDataElementImpl.create(70));
		dregion2.setOuterRadius(70);
		seDial1.getDial().getDialRegions().add(dregion2);

		DialRegion dregion3 = DialRegionImpl.create();
		dregion3.setFill(ColorDefinitionImpl.RED());
		dregion3.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion3.setStartValue(NumberDataElementImpl.create(0));
		dregion3.setEndValue(NumberDataElementImpl.create(40));
		dregion3.setInnerRadius(40);
		dregion3.setOuterRadius(90);
		seDial1.getDial().getDialRegions().add(dregion3);

		// Dial 2
		DialSeries seDial2 = (DialSeries) DialSeriesImpl.create();
		seDial2.setDataSet(NumberDataSetImpl.create(new double[] { 58 }));
		seDial2.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial2.setSeriesIdentifier("Wind Speed");
		seDial2.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial2.getDial().setStartAngle(-45);
		seDial2.getDial().setStopAngle(225);
		seDial2.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial2.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial2.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial2.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial2.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial2.getDial().getScale().setStep(10);
		seDial2.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial2.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());
		seDial2.getTriggers().add(triger);

		seDial2.getDial().getDialRegions().add(dregion1);
		seDial2.getDial().getDialRegions().add(dregion2);
		seDial2.getDial().getDialRegions().add(dregion3);

		// Dial 3
		DialSeries seDial3 = (DialSeries) DialSeriesImpl.create();
		seDial3.setDataSet(NumberDataSetImpl.create(new double[] { 80 }));
		seDial3.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial3.setSeriesIdentifier("Viscosity");
		seDial3.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial3.getDial().setStartAngle(-45);
		seDial3.getDial().setStopAngle(225);
		seDial3.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial3.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial3.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial3.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial3.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial3.getDial().getScale().setStep(10);
		seDial3.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial3.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());
		seDial3.getTriggers().add(triger);

		seDial3.getDial().getDialRegions().add(dregion1);
		seDial3.getDial().getDialRegions().add(dregion2);
		seDial3.getDial().getDialRegions().add(dregion3);

		dChart.setDialSuperimposition(true);
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial1);
		sdCity.getSeries().add(seDial2);
		sdCity.getSeries().add(seDial3);

		return dChart;

	}

	public static final Chart URLRedirect_ScatterChart() {
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create();

		// Title
		cwaScatter.getTitle().getLabel().getCaption().setValue("Numeric Scatter Chart"); //$NON-NLS-1$
		cwaScatter.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaScatter.getPlot().getClientArea().getOutline().setVisible(false);
		cwaScatter.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis"); //$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Data Set
		NumberDataSet dsNumericValues1 = NumberDataSetImpl
				.create(new double[] { -46.55, 25.32, 84.46, 125.95, 38.65, -54.32, 30 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl
				.create(new double[] { 125.99, 352.95, -201.95, 299.95, -95.95, 65.95, 58.95 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsNumericValues1);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create();
		ss.setSeriesIdentifier("Unit Price"); //$NON-NLS-1$

		for (int i = 0; i < ss.getMarkers().size(); i++) {
			((Marker) ss.getMarkers().get(i)).setType(MarkerType.CIRCLE_LITERAL);
		}

		DataPoint dp = ss.getDataPoint();
		dp.getComponents().clear();
		dp.setPrefix("("); //$NON-NLS-1$
		dp.setSuffix(")"); //$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.BASE_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00"))); //$NON-NLS-1$

		ss.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ss.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		ss.getLabel().setVisible(true);
		ss.setDataSet(dsNumericValues2);
		Trigger triger = TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.URL_REDIRECT_LITERAL, URLValueImpl.create("http://www.actuate.com", //$NON-NLS-1$
						null, null, // $NON-NLS-1$
						null, // $NON-NLS-1$
						null)));
		ss.getTriggers().add(triger);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLACK());
		sdY.getSeries().add(ss);

		return cwaScatter;
	}

	public static final Chart URLRedirect_StockChart() {
		ChartWithAxes cwaStock = ChartWithAxesImpl.create();

		// Title
		cwaStock.getTitle().getLabel().getCaption().setValue("Stock Chart");//$NON-NLS-1$
		TitleBlock tb = cwaStock.getTitle();
		tb.setBackground(GradientImpl.create(ColorDefinitionImpl.create(0, 128, 0),
				ColorDefinitionImpl.create(128, 0, 0), 0, false));
		tb.getLabel().getCaption().setColor(ColorDefinitionImpl.WHITE());

		// Plot
		cwaStock.getBlock().setBackground(
				GradientImpl.create(ColorDefinitionImpl.create(196, 196, 196), ColorDefinitionImpl.WHITE(), 90, false));
		cwaStock.getPlot().getClientArea().getInsets().set(10, 10, 10, 10);

		// Legend
		cwaStock.getLegend().setBackground(ColorDefinitionImpl.ORANGE());

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaStock).getPrimaryBaseAxes()[0];

		xAxisPrimary.getTitle().getCaption().setValue("X Axis");//$NON-NLS-1$
		xAxisPrimary.getTitle().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getTitle().getCaption().setValue("Date");//$NON-NLS-1$
		xAxisPrimary.setTitlePosition(Position.ABOVE_LITERAL);

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(65);
		xAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);

		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(255, 196, 196));
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		xAxisPrimary.setCategoryAxis(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaStock).getPrimaryOrthogonalAxis(xAxisPrimary);

		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");//$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);

		yAxisPrimary.getTitle().getCaption().setValue("Microsoft ($ Stock Price)");//$NON-NLS-1$
		yAxisPrimary.getTitle().getCaption().setColor(ColorDefinitionImpl.BLUE());
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);

		yAxisPrimary.getScale().setMin(NumberDataElementImpl.create(24.5));
		yAxisPrimary.getScale().setMax(NumberDataElementImpl.create(27.5));
		yAxisPrimary.getScale().setStep(0.5);

		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(196, 196, 255));
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Data Set
		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2004, 12, 27), new CDateTime(2004, 12, 23),
						new CDateTime(2004, 12, 22), new CDateTime(2004, 12, 21), new CDateTime(2004, 12, 20),
						new CDateTime(2004, 12, 17), new CDateTime(2004, 12, 16), new CDateTime(2004, 12, 15) });

		StockDataSet dsStockValues = StockDataSetImpl.create(new StockEntry[] {
				new StockEntry(27.01, 27.10, 26.82, 26.85), new StockEntry(26.87, 27.15, 26.83, 27.01),
				new StockEntry(26.84, 27.15, 26.78, 26.97), new StockEntry(27.00, 27.17, 26.94, 27.07),
				new StockEntry(27.01, 27.15, 26.89, 26.95), new StockEntry(27.00, 27.32, 26.80, 26.96),
				new StockEntry(27.15, 27.28, 27.01, 27.16), new StockEntry(27.22, 27.40, 27.07, 27.11), });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(1);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		StockSeries ss = (StockSeries) StockSeriesImpl.create();
		ss.setSeriesIdentifier("Stock Price");//$NON-NLS-1$
		ss.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		ss.setDataSet(dsStockValues);
		Trigger triger = TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.URL_REDIRECT_LITERAL, URLValueImpl.create("http://www.actuate.com", //$NON-NLS-1$
						null, null, // $NON-NLS-1$
						null, // $NON-NLS-1$
						null)));
		ss.getTriggers().add(triger);
		ss.setCurveFitting(CurveFittingImpl.create());

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(ColorDefinitionImpl.CYAN());
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ss);

		return cwaStock;
	}

	public static final Chart showTooltipURLRedirect_BarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Chart Type
		cwaBar.setType("Bar Chart");
		cwaBar.setSubType("Stacked");

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Computer Hardware Sales"); //$NON-NLS-1$
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Plot
		cwaBar.getPlot().getClientArea().getOutline().setVisible(false);
		cwaBar.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaBar).getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Sales Growth"); //$NON-NLS-1$
		FontDefinition fd = FontDefinitionImpl.create("Arial", (float) 30.0, true, true, false, true, false, 30.0,
				TextAlignmentImpl.create());
		yAxisPrimary.getLabel().getCaption().setFont(fd);
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());

		yAxisPrimary.getTitle().setVisible(false);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl
				.create(new String[] { "Keyboards", "Moritors", "Printers", "Mortherboards" });
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { 143.26, 156.55, 95.25, 47.56 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] { 15.29, -14.53, -47.05, 32.55 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsStringValue);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setSeriesIdentifier("Actuate"); //$NON-NLS-1$
		bs.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs.getLabel().setVisible(true);
		bs.setDataSet(dsNumericValues1);
		bs.setStacked(true);
		bs.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEMOVE_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));
		Trigger triger = TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL,
				ActionImpl.create(ActionType.URL_REDIRECT_LITERAL, URLValueImpl.create("http://www.actuate.com", //$NON-NLS-1$
						null, null, // $NON-NLS-1$
						null, // $NON-NLS-1$
						null)));
		bs.getTriggers().add(triger);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(bs);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Micorsoft"); //$NON-NLS-1$
		bs2.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		bs2.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		bs2.getLabel().setVisible(true);
		bs2.setDataSet(dsNumericValues2);
		bs2.setStacked(true);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.PINK());
		sdY2.getSeries().add(bs2);

		return cwaBar;
	}

	protected static String getClassName() {
		String className = MarkerShape_1.class.getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = className.substring(0, lastDotIndex);

		return className;
	}
}