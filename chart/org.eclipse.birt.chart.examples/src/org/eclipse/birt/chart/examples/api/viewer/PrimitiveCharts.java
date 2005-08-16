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

package org.eclipse.birt.chart.examples.api.viewer;

import java.util.Calendar;

import org.eclipse.birt.chart.datafeed.StockEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.StockDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;
import org.eclipse.birt.chart.util.CDateTime;

/**
 * The class cannot be run individually. It provides sample model implementations
 * for viewer selector classes in the package.
 * 
 */

public final class PrimitiveCharts {
	
	/**
	 * Creates a simple bar chart model
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createBarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		
		//Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(false);
		
		//Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Bar Chart");

		//Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);
		
		//X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);
		
		//Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);		
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);

		//Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] {
				"Item 1", "Item 2", "Item 3"});
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] {
				25, 35, 15});

		//X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(0);		
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		//Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.setRiserOutline(null);
		bs.getLabel().setVisible(true);
		bs.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();		
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	/**
	 * Creates a bar chart model with mutiple Y-series as a reference
	 * implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createMultiBarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		
		//Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
						ColorDefinitionImpl.create(255, 255, 225), -35, false));
		p.getOutline().setVisible(true);
		
		//Title
		cwaBar.getTitle().getLabel().getCaption().setValue("2-Series Bar Chart");

		//Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);

		//X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(true);
		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);		
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);		
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);
		
		//Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		//Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] {
				"Item 1", "Item 2", "Item 3"});
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] {
				25, 35, 15});
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] {
				17, 63.55, 27.29 });

		//X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);		
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		//Y-Series
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("Series 1");
		bs1.setDataSet(orthoValues1);
		bs1.setRiserOutline(null);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Series 2");
		bs2.setDataSet(orthoValues2);
		bs2.setRiserOutline(null);
		bs2.getLabel().setVisible(true);
		bs2.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(1); 	
		yAxisPrimary.getSeriesDefinitions().add(sdY);		
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(bs2);

		return cwaBar;
	}

	/**
	 * Creates a line chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createLineChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();
		
		//Plot
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaLine.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		
		//Title
		cwaLine.getTitle().getLabel().getCaption().setValue("Line Chart");

		//Legend
		cwaLine.getLegend().setVisible(false);

		//X-Axis
		Axis xAxisPrimary = cwaLine.getPrimaryBaseAxes()[0];		
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		//Y-Axis
		Axis yAxisPrimary = cwaLine.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		//Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] {
				"Item 1", "Item 2", "Item 3"});
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] {
				25, 35, 15});

		//X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		SeriesDefinition sdX = SeriesDefinitionImpl.create(); 
		
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		//Y-Sereis
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setDataSet(orthoValues);
		ls.getLineAttributes().setColor(ColorDefinitionImpl.CREAM());
		ls.getMarker().setType(MarkerType.TRIANGLE_LITERAL);
		ls.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(-2); 		
		yAxisPrimary.getSeriesDefinitions().add(sdY);		
		sdY.getSeries().add(ls);

		return cwaLine;
	}

	/**
	 * Creates a pie chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createPieChart() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();
		
		//Plot
		cwoaPie.setSeriesThickness(25);
		cwoaPie.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwoaPie.getPlot();
		p.getClientArea().setBackground(null);
		p.getClientArea().getOutline().setVisible(true);
		p.getOutline().setVisible(true);

		//Legend
		Legend lg = cwoaPie.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setBackground(null);
		lg.getOutline().setVisible(true);

		//Title
		cwoaPie.getTitle().getLabel().getCaption().setValue("Pie Chart");
		cwoaPie.getTitle().getOutline().setVisible(true);

		//Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] {
				"New York", "Boston", "Chicago", "San Francisco", "Dallas" });
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] {
				54.65, 21, 75.95, 91.28, 37.43 });

		//Base Series
		Series seCategory = (Series) SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		
		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);		
		sd.getSeriesPalette().update(0);
		sd.getSeries().add(seCategory);

		//Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesOneValues);
		sePie.setSeriesIdentifier("Cities");	
		
		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sdCity.getQuery().setDefinition("Census.City");
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie);
		
		return cwoaPie;
	}

	/**
	 * Creates a chart model containing multiple pies (grouped by categories) as
	 * a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createMultiPieChart() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();
		
		//Plot
		cwoaPie.setSeriesThickness(25);
		cwoaPie.setGridColumnCount(2);
		cwoaPie.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwoaPie.getPlot();
		p.getClientArea().setBackground(null);
		p.getClientArea().getOutline().setVisible(true);
		p.getOutline().setVisible(true);

		//Legend
		Legend lg = cwoaPie.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setBackground(null);
		lg.getOutline().setVisible(true);

		//Title
		cwoaPie.getTitle().getLabel().getCaption().setValue(
				"Multiple Series Pie Chart");
		cwoaPie.getTitle().getOutline().setVisible(true);

		//Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] {
				"Boston", "New York", "Chicago", "San Francisco", "Seattle" });
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] {
				54.65, 21, 75.95, 91.28, 37.43 });
		NumberDataSet seriesTwoValues = NumberDataSetImpl.create(new double[] {
				15.65, 65, 25.95, 14.28, 37.43 });
		NumberDataSet seriesThreeValues = NumberDataSetImpl
				.create(new double[] { 25.65, 85, 45.95, 64.28, 6.43 });
		NumberDataSet seriesFourValues = NumberDataSetImpl.create(new double[] {
				25.65, 55, 5.95, 14.28, 86.43 });

		//Base Sereis
		Series seCategory = (Series) SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		
		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().update(1);	
		sd.getSeries().add(seCategory);

		//Orthogonal Series
		PieSeries sePie1 = (PieSeries) PieSeriesImpl.create();		
		sePie1.setDataSet(seriesOneValues);
		sePie1.setSeriesIdentifier("2000");
		sePie1.getLabel().getCaption().getFont().setRotation(25);		
		sePie1.getTitle().getCaption().getFont().setRotation(8);
		sePie1.setTitlePosition(Position.ABOVE_LITERAL);
		sePie1.getTitle().getInsets().set(8, 10, 0, 5);		

		PieSeries sePie2 = (PieSeries) PieSeriesImpl.create();
		sePie2.setDataSet(seriesTwoValues);
		sePie2.setSeriesIdentifier("2001");
		sePie2.getLabel().getCaption().getFont().setRotation(-65);
		sePie2.getTitle().getCaption().getFont().setRotation(28);
		sePie2.getLabel().setBackground(ColorDefinitionImpl.YELLOW());
		sePie2.getLabel().setShadowColor(ColorDefinitionImpl.GREY());
		sePie2.setTitlePosition(Position.RIGHT_LITERAL);	

		PieSeries sePie3 = (PieSeries) PieSeriesImpl.create();
		sePie3.setDataSet(seriesThreeValues);
		sePie3.setSeriesIdentifier("2002");
		sePie3.getTitle().getCaption().getFont().setRotation(75);
		sePie3.setTitlePosition(Position.LEFT_LITERAL);
		
		PieSeries sePie4 = (PieSeries) PieSeriesImpl.create();
		sePie4.setDataSet(seriesFourValues);
		sePie4.setSeriesIdentifier("2003");		
		sePie4.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie1);
		sdCity.getSeries().add(sePie2);
		sdCity.getSeries().add(sePie3);
		sdCity.getSeries().add(sePie4);

		return cwoaPie;
	}

	/**
	 * Creates a stacked Bar & Line combination chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createStackedChart() {
		ChartWithAxes cwaCombination = ChartWithAxesImpl.create();
		
		//Plot
		cwaCombination.setUnitSpacing(25);
		cwaCombination.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaCombination.getPlot();
		p.getClientArea().setBackground(
				GradientImpl.create(ColorDefinitionImpl.create(255, 235, 255),
						ColorDefinitionImpl.create(255, 255, 225), -35, false));

		p.getClientArea().getInsets().set(8, 8, 8, 8);
		p.getOutline().setVisible(true);

		//Legend
		Legend lg = cwaCombination.getLegend();
		lg.setBackground(ColorDefinitionImpl.YELLOW());
		lg.getOutline().setVisible(true);

		//Title
		cwaCombination.getTitle().getLabel().getCaption().setValue("Project Sales");

		//X-Axis
		Axis xAxisPrimary = cwaCombination.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		
		xAxisPrimary.getLabel().setBackground(
				ColorDefinitionImpl.create(255, 255, 235));
		xAxisPrimary.getLabel().setShadowColor(
				ColorDefinitionImpl.create(225, 225, 225));
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(25);

		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);
		
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(64, 64, 64));
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		
		xAxisPrimary.getTitle().getCaption().setValue("Computer Components");
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		xAxisPrimary.getMinorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.CYAN());
		xAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);

		//Y-Series 
		Axis yAxisPrimary = cwaCombination.getPrimaryOrthogonalAxis(xAxisPrimary);

		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);
		yAxisPrimary.getTitle().getCaption().setValue(
				"Actual Sales ($Millions)");

		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(37);
		
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.RED());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMinorGrid().setTickStyle(TickStyle.ACROSS_LITERAL);
		yAxisPrimary.getMinorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMinorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.GREEN());

		//Data Set
		String[] saTextValues = { "CPUs", "Keyboards", "Video Cards",
				"Monitors", "Motherboards", "Memory", "Storage Devices",
				"Media", "Printers", "Scanners" };

		TextDataSet categoryValues = TextDataSetImpl.create(saTextValues);
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] { 
				56.99, 352.95, 201.95, 299.95, 95.95, 25.45, 129.33, 26.5, 43.5, 122 });
		NumberDataSet seriesTwoValues = NumberDataSetImpl.create(new double[] { 
				20, 35, 59, 105, 150, 37, 65, 99, 145, 185 });
		NumberDataSet seriesThreeValues = NumberDataSetImpl.create(new double[] { 
				54.99, 21, 75.95, 39.95, 7.95, 91.22, 33.45, 25.63, 40, 13 });
		NumberDataSet seriesFourValues = NumberDataSetImpl.create(new double[] {
				15, 45, 43, 5, 19, 25, 35, 94, 15, 55 });
		NumberDataSet seriesFiveValues = NumberDataSetImpl.create(new double[] {
				43, 65, 35, 41, 45, 55, 29, 15, 85, 65 });
		NumberDataSet seriesSixValues = NumberDataSetImpl.create(new double[] {
				15, 45, 43, 5, 19, 25, 35, 94, 15, 55 });
		NumberDataSet seriesSevenValues = NumberDataSetImpl.create(new double[] {
				43, 65, 35, 41, 45, 55, 29, 15, 85,65 });

		//X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		//Y-Series
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("North America");
		bs1.setDataSet(seriesOneValues);
		bs1.setRiserOutline(null);
		bs1.setRiser(RiserType.RECTANGLE_LITERAL);
		bs1.setStacked(true);
		DataPoint dp = DataPointImpl.create("(", ")", ", ");
		dp.getComponents().clear();
		dp.getComponents().add(
				DataPointComponentImpl.create(
						DataPointComponentType.BASE_VALUE_LITERAL, null 
						));
		dp.getComponents().add(
				DataPointComponentImpl.create(
						DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
						JavaNumberFormatSpecifierImpl.create("0.00")));
		bs1.setDataPoint(dp);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("South America");
		bs2.setDataSet(seriesThreeValues);
		bs2.setRiserOutline(null);
		bs2.setRiser(RiserType.RECTANGLE_LITERAL);
		bs2.setStacked(true);
		dp = DataPointImpl.create("[", "]", ", ");
		bs2.setDataPoint(dp);

		BarSeries bs3 = (BarSeries) BarSeriesImpl.create();
		bs3.setSeriesIdentifier("Eastern Europe");
		bs3.setDataSet(seriesFourValues);
		bs3.setRiserOutline(null);
		bs3.setRiser(RiserType.RECTANGLE_LITERAL);
		bs3.setStacked(true);

		BarSeries bs4 = (BarSeries) BarSeriesImpl.create();
		bs4.setSeriesIdentifier("Western Europe");
		bs4.setDataSet(seriesFiveValues);
		bs4.setRiserOutline(null);
		bs4.setRiser(RiserType.RECTANGLE_LITERAL);
		bs4.setStacked(true);

		BarSeries bs5 = (BarSeries) BarSeriesImpl.create();
		bs5.setSeriesIdentifier("Asia");
		bs5.setDataSet(seriesSixValues);
		bs5.setRiserOutline(null);
		bs5.setRiser(RiserType.RECTANGLE_LITERAL);

		BarSeries bs6 = (BarSeries) BarSeriesImpl.create();
		bs6.setSeriesIdentifier("Australia");
		bs6.setDataSet(seriesSevenValues);
		bs6.setRiserOutline(null);
		bs6.setRiser(RiserType.RECTANGLE_LITERAL);

		LineSeries ls1 = (LineSeries) LineSeriesImpl.create();
		ls1.setSeriesIdentifier("Expected Growth");
		ls1.setDataSet(seriesTwoValues);
		ls1.getMarker().setType(MarkerType.BOX_LITERAL);
		ls1.getLabel().setVisible(true);		

		SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		sdY1.getSeriesPalette().update(0);
		yAxisPrimary.getSeriesDefinitions().add(sdY1);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		sdY2.getSeriesPalette().update(1);
		yAxisPrimary.getSeriesDefinitions().add(sdY2);

		SeriesDefinition sdY3 = SeriesDefinitionImpl.create();
		sdY3.getSeriesPalette().update(ColorDefinitionImpl.RED());
		yAxisPrimary.getSeriesDefinitions().add(sdY3);

		SeriesDefinition sdY4 = SeriesDefinitionImpl.create();
		sdY4.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		yAxisPrimary.getSeriesDefinitions().add(sdY4);

		SeriesDefinition sdY5 = SeriesDefinitionImpl.create();
		sdY5.getSeriesPalette().update(ColorDefinitionImpl.YELLOW());
		yAxisPrimary.getSeriesDefinitions().add(sdY5);
	
		sdY1.getSeries().add(bs1);
		sdY1.getSeries().add(bs2);
		sdY2.getSeries().add(bs3);
		sdY2.getSeries().add(bs4);
		sdY3.getSeries().add(bs5);
		sdY4.getSeries().add(bs6);
		sdY5.getSeries().add(ls1);

		return cwaCombination;
	}

	/**
	 * Creates a numeric scatter chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createScatterChart() {
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create();
		
		//Plot
		cwaScatter.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaScatter.getPlot().getClientArea().getOutline().setVisible(false);
		cwaScatter.getPlot().getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		
		//Title
		cwaScatter.getTitle().getLabel().getCaption().setValue(
		"Numeric Scatter Chart");

		//X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryBaseAxes()[0];
		
		xAxisPrimary.getTitle().getCaption().setValue("X Axis");
		xAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.GREEN().darker());
		xAxisPrimary.getTitle().setVisible(false);
		
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		//Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryOrthogonalAxis(xAxisPrimary);
		
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisPrimary.getTitle().setVisible(true);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		
		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		//Data Set
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] {
				-46.55, 25.32, 84.46, 125.95, 38.65, -54.32, 30 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] {
				125.99, 352.95, -201.95, 299.95, -95.95, 65.95, 58.95 });

		//X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsNumericValues1);
		
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		//Y-Series
		ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create();
		ss.setSeriesIdentifier("Unit Price");
		ss.getMarker().setType(MarkerType.CIRCLE_LITERAL);
		DataPoint dp = ss.getDataPoint();
		dp.getComponents().clear();
		dp.setPrefix("(");
		dp.setSuffix(")");
		dp.getComponents().add(
				DataPointComponentImpl.create(
						DataPointComponentType.BASE_VALUE_LITERAL,
						JavaNumberFormatSpecifierImpl.create("0.00")));
		dp.getComponents().add(
				DataPointComponentImpl.create(
						DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
						JavaNumberFormatSpecifierImpl.create("0.00")));
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

	
	/**
	 * Creates a stock chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public final static Chart createStockChart() {
		ChartWithAxes cwaStock = ChartWithAxesImpl.create();
		
		//Title
		cwaStock .getTitle().getLabel().getCaption().setValue(
				"Stock Chart");
		TitleBlock tb = cwaStock .getTitle();
		tb.setBackground(GradientImpl.create(ColorDefinitionImpl.create(0, 128,
				0), ColorDefinitionImpl.create(128, 0, 0), 0, false));
		tb.getLabel().getCaption().setColor(ColorDefinitionImpl.WHITE());
		
		//Plot
		cwaStock .getBlock().setBackground(
				GradientImpl.create(ColorDefinitionImpl.create(196, 196, 196),
						ColorDefinitionImpl.WHITE(), 90, false));
		cwaStock .getPlot().getClientArea().getInsets()
		.set(10, 10, 10, 10);

		//Legend
		cwaStock .getLegend().setBackground(ColorDefinitionImpl.ORANGE());

		//X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaStock ).getPrimaryBaseAxes()[0];
		
		xAxisPrimary.getTitle().getCaption().setValue("X Axis");
		xAxisPrimary.getTitle().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getTitle().getCaption().setValue("Date");
		xAxisPrimary.setTitlePosition(Position.ABOVE_LITERAL);
			
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(65);		
		xAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);
		
		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);
		
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(255, 196, 196));
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		
		xAxisPrimary.setCategoryAxis(true);

		//Y-Axis (1)
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaStock ).getPrimaryOrthogonalAxis(xAxisPrimary);
		
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		
		yAxisPrimary.getTitle().getCaption().setValue(
				"Microsoft ($ Stock Price)");
		yAxisPrimary.getTitle().getCaption().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);
			
		yAxisPrimary.getScale().setMin(NumberDataElementImpl.create(24.5));
		yAxisPrimary.getScale().setMax(NumberDataElementImpl.create(27.5));
		yAxisPrimary.getScale().setStep(0.5);
		
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(196, 196, 255));
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);	
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		//Y-Axis (2)
		Axis yAxisOverlay = AxisImpl.create(Axis.ORTHOGONAL);
		
		yAxisOverlay.getLabel().getCaption().setColor(
				ColorDefinitionImpl.create(0, 128, 0));
		yAxisOverlay.getLabel().getCaption().getFont().setRotation(-25);
		yAxisOverlay.setLabelPosition(Position.RIGHT_LITERAL);
		
		yAxisOverlay.getTitle().getCaption().setValue("Volume");
		yAxisOverlay.getTitle().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());
		yAxisOverlay.getTitle().getCaption().getFont().setRotation(90);
		yAxisOverlay.getTitle().getCaption().getFont().setSize(16);
		yAxisOverlay.getTitle().getCaption().getFont().setBold(true);
		yAxisOverlay.getTitle().setVisible(true);
		yAxisOverlay.setTitlePosition(Position.RIGHT_LITERAL);
		
		yAxisOverlay.getLineAttributes().setColor(
				ColorDefinitionImpl.create(0, 128, 0));
		
		yAxisOverlay.setType(AxisType.LINEAR_LITERAL);		
		yAxisOverlay.setOrientation(Orientation.VERTICAL_LITERAL);
		
		yAxisOverlay.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(64, 196, 64));
		yAxisOverlay.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisOverlay.getMajorGrid().getLineAttributes().setVisible(true);	
		yAxisOverlay.getMajorGrid().setTickStyle(TickStyle.RIGHT_LITERAL);	
		
		yAxisOverlay.getOrigin().setType(IntersectionType.MAX_LITERAL);
		yAxisOverlay.getScale()
				.setMax(NumberDataElementImpl.create(180000000));
		yAxisOverlay.getScale().setMin(NumberDataElementImpl.create(20000000));
		
		xAxisPrimary.getAssociatedAxes().add(yAxisOverlay);

		//Data Set
		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2004, 12, 27),
						new CDateTime(2004, 12, 23),
						new CDateTime(2004, 12, 22),
						new CDateTime(2004, 12, 21),
						new CDateTime(2004, 12, 20),
						new CDateTime(2004, 12, 17),
						new CDateTime(2004, 12, 16),
						new CDateTime(2004, 12, 15) });

		StockDataSet dsStockValues = StockDataSetImpl.create(new StockEntry[] {
				new StockEntry(27.01, 27.10, 26.82, 26.85),
				new StockEntry(26.87, 27.15, 26.83, 27.01),
				new StockEntry(26.84, 27.15, 26.78, 26.97),
				new StockEntry(27.00, 27.17, 26.94, 27.07),
				new StockEntry(27.01, 27.15, 26.89, 26.95),
				new StockEntry(27.00, 27.32, 26.80, 26.96),
				new StockEntry(27.15, 27.28, 27.01, 27.16),
				new StockEntry(27.22, 27.40, 27.07, 27.11), });

		NumberDataSet dsStockVolume = NumberDataSetImpl.create(new double[] {
				55958500, 65801900, 63651900, 94646096, 85552800, 126184400,
				88997504, 106303904 });

		//X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);
		
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(1);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		//Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setRiserOutline(null);
		bs.setDataSet(dsStockVolume);
		
		StockSeries ss = (StockSeries) StockSeriesImpl.create();
		ss.setSeriesIdentifier("Stock Price");
		ss.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		ss.setDataSet(dsStockValues);

		SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		sdY1.getSeriesPalette().update(ColorDefinitionImpl.CYAN());
		yAxisPrimary.getSeriesDefinitions().add(sdY1);
		sdY1.getSeries().add(ss);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		yAxisOverlay.getSeriesDefinitions().add(sdY2);
		sdY2.getSeries().add(bs);

		return cwaStock;
	}
}