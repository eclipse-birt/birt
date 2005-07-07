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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import org.eclipse.birt.chart.datafeed.StockEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.URLValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
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
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);

		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(false);
		cwaBar.getTitle().getLabel().getCaption().setValue("Bar Chart");

		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		//yAxisPrimary.getLabel().getInsets().setTop(10);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);
		yAxisPrimary.setFormatSpecifier(JavaNumberFormatSpecifierImpl
				.create("AgI\n##"));

		Vector vs = new Vector();
		vs.add("zero");
		vs.add("one");
		vs.add("two");

		ArrayList vn1 = new ArrayList();
		vn1.add(new Double(25));
		vn1.add(new Double(35));
		vn1.add(new Double(-45));

		TextDataSet categoryValues = TextDataSetImpl.create(vs);
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(vn1);

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("My Bar Series");
		bs1.setDataSet(orthoValues1);
		bs1.setRiserOutline(null);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getQuery().setDefinition("someConst");
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getQuery().setDefinition("someExpr(abc)"); // NEEDED FOR DATA
		sdY.getSeriesPalette().update(1); // SET THE COLOR IN THE PALETTE
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		sdX.getSeries().add(seCategory);
		sdY.getSeries().add(bs1);

		return cwaBar;
	}

	/**
	 * Creates a bar chart model (data grouped by series) as a reference
	 * implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createMultiBarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();

		p.getClientArea().setBackground(
				GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
						ColorDefinitionImpl.create(255, 255, 225), -35, false));

		p.getOutline().setVisible(true);
		cwaBar.getTitle().getLabel().getCaption().setValue("2-Series Bar Chart");

		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);
		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		Vector vs = new Vector();
		vs.add("one");
		vs.add("two");
		vs.add("three");

		ArrayList vn1 = new ArrayList();
		vn1.add(new Double(25));
		vn1.add(new Double(35));
		vn1.add(new Double(-45));

		TextDataSet categoryValues = TextDataSetImpl.create(vs);
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(vn1);
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] {
				17, 63.55, 27.29 });

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("Bar Series 1");
		bs1.setDataSet(orthoValues1);
		bs1.setRiserOutline(null);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Bar Series 2");
		bs2.setDataSet(orthoValues2);
		bs2.setRiserOutline(null);
		bs2.getLabel().setVisible(true);
		bs2.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getQuery().setDefinition("someConst");
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getQuery().setDefinition("someExpr(abc)"); // NEEDED FOR DATA
		sdY.getSeriesPalette().update(1); // SET THE COLOR IN THE PALETTE
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		sdX.getSeries().add(seCategory);
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
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		cwaBar.getTitle().getLabel().getCaption().setValue("Line Chart");

		Legend lg = cwaBar.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(true);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setPercent(true);

		Vector vs = new Vector();
		vs.add("one");
		vs.add("two");
		vs.add("three");

		ArrayList vn1 = new ArrayList();
		vn1.add(new Double(25));
		vn1.add(new Double(35));
		vn1.add(new Double(-45));

		TextDataSet categoryValues = TextDataSetImpl.create(vs);
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(vn1);

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setSeriesIdentifier("My Line Series");
		ls.setDataSet(orthoValues1);
		ls.getLineAttributes().setColor(ColorDefinitionImpl.CREAM());
		ls.getMarker().setType(MarkerType.TRIANGLE_LITERAL);
		ls.getLabel().setVisible(true);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(1); // SET THE COLOR IN THE PALETTE
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		sdX.getSeries().add(seCategory);
		sdY.getSeries().add(ls);

		return cwaBar;
	}

	/**
	 * Creates a pie chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createPieChart() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();
		cwoaPie.setSeriesThickness(25);

		cwoaPie.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwoaPie.getPlot();
		p.getClientArea().setBackground(null);
		p.getClientArea().getOutline().setVisible(true);
		p.getOutline().setVisible(true);

		Legend lg = cwoaPie.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().setLeft(10);
		lg.getInsets().setRight(10);
		lg.setBackground(null);
		lg.getOutline().setVisible(true);

		// CHART TITLE
		cwoaPie.getTitle().getLabel().getCaption().setValue("Pie Chart");
		cwoaPie.getTitle().getOutline().setVisible(true);

		// ASSOCIATE THE DATA
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] {
				"New York", "Boston", "Chicago", "San Francisco", "Dallas" });
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] {
				54.65, 21, 75.95, 91.28, 37.43 });

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);
		Series seCategory = (Series) SeriesImpl.create();

		final Fill[] fiaBase = {
				ImageImpl
						.create("file:///C:\\actuate\\iard\\org.eclipse.birt.chart.demo\\images\\fishing.gif"),
				GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
						ColorDefinitionImpl.create(255, 255, 225), -35, false),
				ColorDefinitionImpl.CREAM() };
		sd.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaBase.length; i++) {
			sd.getSeriesPalette().getEntries().add(fiaBase[i]);
		}

		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sdCity.getQuery().setDefinition("Census.City");
		final Fill[] fiaOrthogonal = {
				ImageImpl
						.create("file:///C:\\actuate\\iard\\org.eclipse.birt.chart.demo\\images\\greenstone.gif"),
				GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
						ColorDefinitionImpl.create(225, 225, 255), 45, false),
				ColorDefinitionImpl.ORANGE() };
		sdCity.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaBase.length; i++) {
			sdCity.getSeriesPalette().getEntries().add(fiaOrthogonal[i]);
		}

		sd.getSeriesDefinitions().add(sdCity);
		PieSeries sePie1 = (PieSeries) PieSeriesImpl.create();
		sePie1.setDataSet(seriesOneValues);
		sePie1.setSeriesIdentifier("New York");
		sdCity.getSeries().add(sePie1);

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
		cwoaPie.setSeriesThickness(25);
		cwoaPie.setGridColumnCount(2);

		cwoaPie.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwoaPie.getPlot();
		p.getClientArea().setBackground(null);
		p.getClientArea().getOutline().setVisible(true);
		p.getOutline().setVisible(true);

		Legend lg = cwoaPie.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().setLeft(10);
		lg.getInsets().setRight(10);
		lg.setBackground(null);
		lg.getOutline().setVisible(true);

		// CHART TITLE
		cwoaPie.getTitle().getLabel().getCaption().setValue(
				"Multiple Pie Series");
		cwoaPie.getTitle().getOutline().setVisible(true);

		// ASSOCIATE THE DATA
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

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().update(1);
		Series seCategory = (Series) SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sdCity.getSeriesPalette().update(0);
		sd.getSeriesDefinitions().add(sdCity);

		PieSeries sePie1 = (PieSeries) PieSeriesImpl.create();
		sePie1.setDataSet(seriesOneValues);
		sePie1.setSeriesIdentifier("2000");
		sePie1.getLabel().getCaption().getFont().setRotation(25);
		sePie1.getTitle().getCaption().getFont().setRotation(8);
		sePie1.setTitlePosition(Position.ABOVE_LITERAL);
		sePie1.getTitle().getInsets().set(8, 10, 0, 5);
		sdCity.getSeries().add(sePie1);

		PieSeries sePie2 = (PieSeries) PieSeriesImpl.create();
		sePie2.setDataSet(seriesTwoValues);
		sePie2.setSeriesIdentifier("2001");
		sePie2.getLabel().getCaption().getFont().setRotation(-65);
		sePie2.getTitle().getCaption().getFont().setRotation(28);
		sePie2.getLabel().setBackground(ColorDefinitionImpl.YELLOW());
		sePie2.getLabel().setShadowColor(ColorDefinitionImpl.GREY());
		sePie2.setTitlePosition(Position.RIGHT_LITERAL);
		sdCity.getSeries().add(sePie2);

		PieSeries sePie3 = (PieSeries) PieSeriesImpl.create();
		sePie3.setDataSet(seriesThreeValues);
		sePie3.setSeriesIdentifier("2002");
		sePie3.getTitle().getCaption().getFont().setRotation(73);
		sePie3.setTitlePosition(Position.LEFT_LITERAL);
		Trigger triger = TriggerImpl.create(TriggerCondition.MOUSE_CLICK_LITERAL,
						ActionImpl.create(ActionType.URL_REDIRECT_LITERAL,
								URLValueImpl.create("http://www.actuate.com",
										null, "city", "population", null)));
		sePie3.getTriggers().add(triger);
		sdCity.getSeries().add(sePie3);

		PieSeries sePie4 = (PieSeries) PieSeriesImpl.create();
		sePie4.setDataSet(seriesFourValues);
		sePie4.setSeriesIdentifier("2003");
		sdCity.getSeries().add(sePie4);
		sePie4.setLabelPosition(Position.INSIDE_LITERAL);

		return cwoaPie;
	}

	/**
	 * Creates a sample stacked chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createStackedChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setUnitSpacing(25);

		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();

		p.getClientArea().setBackground(
				GradientImpl.create(ColorDefinitionImpl.create(255, 235, 255),
						ColorDefinitionImpl.create(255, 255, 225), -35, false));

		p.getClientArea().getInsets().set(8, 8, 8, 8);
		p.getOutline().setVisible(true);

		Legend lg = cwaBar.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.setBackground(ColorDefinitionImpl.BLACK());
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(12, 5, 0, 0);
		lg.getOutline().setVisible(true);
		lg.setAnchor(Anchor.NORTH_LITERAL);
		lg.getTriggers().add(
				TriggerImpl.create(TriggerCondition.MOUSE_CLICK_LITERAL,
						ActionImpl.create(ActionType.TOGGLE_VISIBILITY_LITERAL,
								null)));

		// CHART TITLE
		cwaBar.getTitle().getLabel().getCaption().setValue("Projected Sales");

		// X-AXIS
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getLabel().setBackground(
				ColorDefinitionImpl.create(255, 255, 235));
		xAxisPrimary.getLabel().setShadowColor(
				ColorDefinitionImpl.create(225, 225, 225));
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(25);
		//xAxisPrimary.getTitle().getCaption().getFont().setBold(false);
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

		// Y-AXIS (PRIMARY)
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);

		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);
		yAxisPrimary.getTitle().getCaption().setValue(
				"Actual Sales Growth (in Thousands)");

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


		// ASSOCIATE THE DATA

		String[] saTextValues = { "CPUs", "Keyboards", "Video Cards",
				"Monitors", "Motherboards", "Memory", "Storage Devices",
				"Media", "Printers", "Scanners" };

		TextDataSet categoryValues = TextDataSetImpl.create(saTextValues);
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(da1);
		NumberDataSet seriesTwoValues = NumberDataSetImpl.create(da2);
		NumberDataSet seriesThreeValues = NumberDataSetImpl
				.create(new double[] { 54.99, 21, 75.95, -39.95, -7.95, 91.22,
						33.45, -25.63, 40, 13 });
		NumberDataSet seriesFourValues = NumberDataSetImpl.create(new double[] {
				15, -45, 43, 5, 19, 25, 35, 94, -15, -55 });
		NumberDataSet seriesFiveValues = NumberDataSetImpl.create(new double[] {
				-43, -65, -35, 41, 45, 55, -29, 15, 85, 65 });
		NumberDataSet seriesSixValues = NumberDataSetImpl.create(new double[] {
				15, -45, 43, 5, 19, 25, 35, 94, -15, -55 });
		NumberDataSet seriesSevenValues = NumberDataSetImpl
				.create(new double[] { -43, -65, -35, 41, 45, 55, -29, 15, 85,
						65 });

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		Trigger tg = TriggerImpl.create(TriggerCondition.MOUSE_HOVER_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL,
						TooltipValueImpl.create(500, null)));
		bs1.getTriggers().add(tg);
		bs1.setSeriesIdentifier("North America");
		bs1.setDataSet(seriesOneValues);
		bs1.setRiserOutline(null);
		bs1.setRiser(RiserType.RECTANGLE_LITERAL);
		bs1.setStacked(true);
		DataPoint dp = DataPointImpl.create("(", ")", ", ");
		dp.getComponents().clear();
		dp.getComponents().add(
				DataPointComponentImpl.create(
						DataPointComponentType.BASE_VALUE_LITERAL, null // NO
						));
		dp.getComponents().add(
				DataPointComponentImpl.create(
						DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
						JavaNumberFormatSpecifierImpl.create("0.00")));
		bs1.setDataPoint(dp);

		// CREATE THE SECONDARY DATASET
		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		tg = TriggerImpl.create(TriggerCondition.MOUSE_HOVER_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL,
						TooltipValueImpl.create(500, null)));
		bs2.getTriggers().add(tg);
		bs2.setSeriesIdentifier("South America");
		bs2.setDataSet(seriesThreeValues);
		bs2.setRiserOutline(null);
		bs2.setRiser(RiserType.RECTANGLE_LITERAL);
		bs2.setStacked(true);
		dp = DataPointImpl.create("[", "]", ", ");
		bs2.setDataPoint(dp);

		// CREATE THE PRIMARY DATASET
		BarSeries bs3 = (BarSeries) BarSeriesImpl.create();
		bs3.setSeriesIdentifier("Eastern Europe");
		bs3.setDataSet(seriesFourValues);
		bs3.setRiserOutline(null);
		bs3.setRiser(RiserType.RECTANGLE_LITERAL);
		bs3.setStacked(true);

		// CREATE THE SECONDARY DATASET
		BarSeries bs4 = (BarSeries) BarSeriesImpl.create();
		bs4.setSeriesIdentifier("Western Europe");
		bs4.setDataSet(seriesFiveValues);
		bs4.setRiserOutline(null);
		bs4.setRiser(RiserType.RECTANGLE_LITERAL);
		bs4.setStacked(true);

		// CREATE THE PRIMARY DATASET
		BarSeries bs5 = (BarSeries) BarSeriesImpl.create();
		bs5.setSeriesIdentifier("Asia");
		bs5.setDataSet(seriesSixValues);
		bs5.setRiserOutline(null);
		bs5.setRiser(RiserType.RECTANGLE_LITERAL);
		//bs5.setStacked(true);

		// CREATE THE SECONDARY DATASET
		BarSeries bs6 = (BarSeries) BarSeriesImpl.create();
		bs6.setSeriesIdentifier("Australia");
		bs6.setDataSet(seriesSevenValues);
		bs6.setRiserOutline(null);
		bs6.setRiser(RiserType.RECTANGLE_LITERAL);
		//bs6.setStacked(true);

		// CREATE THE OVERLAY DATASET - SERIES1
		LineSeries ls1 = (LineSeries) LineSeriesImpl.create();
		ls1.setSeriesIdentifier("Expected Growth");
		ls1.setDataSet(seriesTwoValues);
		//ls1.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		ls1.getMarker().setType(MarkerType.BOX_LITERAL);
		ls1.getLabel().setVisible(true);
		//ls1.setCurve(true);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getQuery().setDefinition("");
		sdX.getSeriesPalette().update(1);
		xAxisPrimary.getSeriesDefinitions().add(sdX);

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

		sdX.getSeries().add(seCategory);
		sdY1.getSeries().add(bs1);
		sdY1.getSeries().add(bs2);
		sdY2.getSeries().add(bs3);
		sdY2.getSeries().add(bs4);
		sdY3.getSeries().add(bs5);
		sdY4.getSeries().add(bs6);
		sdY5.getSeries().add(ls1);

		return cwaBar;
	}

	/**
	 * Creates a sample numeric scatter chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createScatterChart() {
		System.getProperty("user.dir");
		ChartWithAxes cwaCombination = ChartWithAxesImpl.create();
		cwaCombination.getTitle().getLabel().getCaption().setValue(
				"Numeric Scatter Chart");
		cwaCombination.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaCombination.getPlot().getClientArea().getOutline().setVisible(false);
		cwaCombination.getPlot().getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));

		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaCombination)
				.getPrimaryBaseAxes()[0];
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

		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaCombination)
				.getPrimaryOrthogonalAxis(xAxisPrimary);
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

		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] {
				-46.55, 25.32, 84.46, 125.95, 38.65, -54.32, 30 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] {
				125.99, 352.95, -201.95, 299.95, -95.95, 65.95, 58.95 });

		// CREATE THE CATEGORY SERIES
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsNumericValues1);

		// CREATE THE PRIMARY DATASET
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
		ss.getTriggers().add(
				TriggerImpl.create(TriggerCondition.MOUSE_CLICK_LITERAL,
						ActionImpl.create(ActionType.URL_REDIRECT_LITERAL,
								URLValueImpl.create("http://www.actuate.com",
										null, "x", "y", null))));

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getQuery().setDefinition(""); // NEEDED FOR DATA EXTRACTION
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLACK());
		sdY.getSeries().add(ss);

		return cwaCombination;
	}

	
	/**
	 * Creates a sample stock chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public final static Chart createStockChart() {
		System.getProperty("user.dir");
		ChartWithAxes cwaCombination = ChartWithAxesImpl.create();
		cwaCombination.getTitle().getLabel().getCaption().setValue(
				"Stock Chart");
		cwaCombination.getBlock().setBackground(
				GradientImpl.create(ColorDefinitionImpl.create(196, 196, 196),
						ColorDefinitionImpl.WHITE(), 90, false));

		TitleBlock tb = cwaCombination.getTitle();
		tb.setBackground(GradientImpl.create(ColorDefinitionImpl.create(0, 128,
				0), ColorDefinitionImpl.create(128, 0, 0), 0, false));
		tb.getLabel().getCaption().setColor(ColorDefinitionImpl.WHITE());
		cwaCombination.getLegend().setBackground(ColorDefinitionImpl.ORANGE());
		cwaCombination.getPlot().getClientArea().getInsets()
				.set(10, 10, 10, 10);

		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaCombination)
				.getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().getCaption().setValue("X Axis");
		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(65);
		xAxisPrimary.getTitle().getCaption()
				.setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getTitle().getCaption().setValue("Date");
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);
		xAxisPrimary.setTitlePosition(Position.ABOVE_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(255, 196, 196));
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.setCategoryAxis(true);

		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaCombination)
				.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisPrimary.getTitle().getCaption().setValue(
				"Microsoft (Stock Price in $$$)");
		yAxisPrimary.getTitle().getCaption().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getScale().setMin(NumberDataElementImpl.create(24.5));
		yAxisPrimary.getScale().setMax(NumberDataElementImpl.create(27.5));
		yAxisPrimary.getScale().setStep(0.5);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(196, 196, 255));
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		Axis yAxisOverlay1 = AxisImpl.create(Axis.ORTHOGONAL);
		yAxisOverlay1.getLabel().getCaption().setColor(
				ColorDefinitionImpl.create(0, 128, 0));
		yAxisOverlay1.getLabel().getCaption().getFont().setRotation(-25);
		yAxisOverlay1.getTitle().getCaption().setValue("Volume");
		yAxisOverlay1.getTitle().getCaption().setColor(
				ColorDefinitionImpl.GREEN().darker());
		yAxisOverlay1.getTitle().getCaption().getFont().setRotation(90);
		yAxisOverlay1.getTitle().getCaption().getFont().setSize(16);
		yAxisOverlay1.getTitle().getCaption().getFont().setBold(true);
		yAxisOverlay1.getTitle().setVisible(true);
		yAxisOverlay1.getLineAttributes().setColor(
				ColorDefinitionImpl.create(0, 128, 0));
		yAxisOverlay1.setType(AxisType.LINEAR_LITERAL);
		yAxisOverlay1.setOrientation(Orientation.VERTICAL_LITERAL);
		yAxisOverlay1.setType(AxisType.LINEAR_LITERAL);
		yAxisOverlay1.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(64, 196, 64));
		yAxisOverlay1.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisOverlay1.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisOverlay1.setTitlePosition(Position.RIGHT_LITERAL);
		yAxisOverlay1.getMajorGrid().setTickStyle(TickStyle.RIGHT_LITERAL);
		yAxisOverlay1.setLabelPosition(Position.RIGHT_LITERAL);
		yAxisOverlay1.getOrigin().setType(IntersectionType.MAX_LITERAL);
		yAxisOverlay1.getScale()
				.setMax(NumberDataElementImpl.create(180000000));
		yAxisOverlay1.getScale().setMin(NumberDataElementImpl.create(20000000));

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

		// CREATE THE CATEGORY SERIES
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		// CREATE THE PRIMARY DATASET
		StockSeries ss = (StockSeries) StockSeriesImpl.create();
		ss.setSeriesIdentifier("Stock Price");
		ss.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		ss.setDataSet(dsStockValues);

		// CREATE THE OVERLAY DATASET
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setSeriesIdentifier("");
		bs.setRiserOutline(null);
		bs.setDataSet(dsStockVolume);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(1);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		// EXTRACTION
		sdY.getSeriesPalette().update(ColorDefinitionImpl.CYAN());
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ss);

		SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		// EXTRACTION
		sdY1.getSeriesPalette().update(ColorDefinitionImpl.GREEN());
		yAxisOverlay1.getSeriesDefinitions().add(sdY1);
		sdY1.getSeries().add(bs);

		// SETUP AXIS HIERARCHY/DEPENDENCY FOR OVERLAY AXES ONLY
		xAxisPrimary.getAssociatedAxes().add(yAxisOverlay1); // ADD THE VALUE
		// AXIS TO THE
		// CATEGORY AXIS

		return cwaCombination;
	}

	// DATA SETS USED WITH THE LIVE/ANIMATED CHART MODEL:

	private static final String[] sa = { "One", "Two", "Three", "Four", "Five",
			"Six", "Seven", "Eight", "Nine", "Ten" };

	private static final double[] da1 = { 56.99, 352.95, -201.95, 299.95,
			-95.95, 25.45, 129.33, -26.5, 43.5, 122 };

	private static final double[] da2 = { 20, 35, 59, 105, 150, -37, -65, -99,
			-145, -185 };

	/**
	 * Creates a sample chart instance that may be used to demo live/animated
	 * charts with scrolling data
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createLiveChart() {
		System.getProperty("user.dir");
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));

		Legend lg = cwaBar.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().setLeft(10);
		lg.getInsets().setRight(10);

		// CHART TITLE
		cwaBar.getTitle().getLabel().getCaption().setValue("Live Chart Demo");

		// X-AXIS
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(75);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(64, 64, 64));
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getTitle().getCaption().setValue("Category Text X-Axis");
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Y-AXIS (PRIMARY)
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);
		yAxisPrimary.getTitle().getCaption().setValue("Linear Value Y-Axis");
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(37);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.RED());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// ASSOCIATE THE DATA
		TextDataSet categoryValues = TextDataSetImpl.create(sa);
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(da1);
		NumberDataSet seriesTwoValues = NumberDataSetImpl.create(da2);

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("Unit Price");
		bs1.setDataSet(seriesOneValues);
		bs1.setRiserOutline(null);
		bs1.setRiser(RiserType.RECTANGLE_LITERAL);

		// CREATE THE OVERLAY DATASET - SERIES1
		LineSeries ls1 = (LineSeries) LineSeriesImpl.create();
		ls1.setSeriesIdentifier("Quantity");
		ls1.setDataSet(seriesTwoValues);
		ls1.getLineAttributes().setColor(ColorDefinitionImpl.GREEN());
		ls1.getMarker().setType(MarkerType.BOX_LITERAL);
		ls1.setCurve(true);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		// EXTRACTION
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(1);
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(ls1);

		return cwaBar;
	}

	/**
	 * 
	 * @param cwa
	 * @param iOffset
	 */
	static final void scrollData(ChartWithAxes cwa) {
		// SCROLL THE BAR SERIES
		double dTemp = da1[0];
		for (int i = 0; i < da1.length - 1; i++) {
			da1[i] = da1[i + 1];
		}
		da1[da1.length - 1] = dTemp;

		// SCROLL THE LINE SERIES
		dTemp = da2[0];
		for (int i = 0; i < da2.length - 1; i++) {
			da2[i] = da2[i + 1];
		}
		da2[da2.length - 1] = dTemp;

		// SCROLL THE CATEGORY BASE SERIES
		String sTemp = sa[0];
		for (int i = 0; i < sa.length - 1; i++) {
			sa[i] = sa[i + 1];
		}
		sa[sa.length - 1] = sTemp;
	}
}