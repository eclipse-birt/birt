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
import java.text.*;
import java.util.Vector;

import org.eclipse.birt.chart.datafeed.StockEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Image;
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
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.URLValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
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
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
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
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * The class cannot be run individually, which provides sample model implementations
 * for all other classes in the package.
 * 
 */

public final class PrimitiveCharts {
	

	/**
	 * Creates an instance of a logarithmic stacked bar chart
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createLogarithmicStackedBarChartOrigin() {
		System.getProperty("user.dir");
		ChartWithAxes cwaLogarithmic = ChartWithAxesImpl.create();
		cwaLogarithmic.getTitle().getLabel().getCaption().setValue(
				"Logarithmic Bar Stacked Chart");
		cwaLogarithmic.getBlock().setBackground(
				GradientImpl.create(ColorDefinitionImpl.GREY(),
						ColorDefinitionImpl.create(225, 225, 255), -35, false));

		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLogarithmic)
				.getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().getCaption().setValue("X Axis");
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		
		xAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLACK());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(36);
		xAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.CREAM());
		xAxisPrimary.getTitle().getCaption().setValue(
				"This is the Primary X-Axis Title");
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.RED());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLogarithmic)
				.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLACK());
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(-53);
		yAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.ORANGE());
		yAxisPrimary.getTitle().getCaption().setValue(
				"This is the Primary Y-Axis Title");
		yAxisPrimary.setType(AxisType.LOGARITHMIC_LITERAL);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMinorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.CYAN());
		yAxisPrimary.getScale().setMinorGridsPerUnit(10);
		yAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		

		TextDataSet dsDateValues = TextDataSetImpl.create(new String[] { "one",
				"two", "three", "four", "five", "six", "seven" });

		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] {
				5.45, 352.02, 1005.36, 299.19, 0.43, 0.05, 58.62 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] {
				55.45, 52.02, 105.36, 799.19, 45, 21, 0.062 });

		// CREATE THE CATEGORY SERIES
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		// EXTRACTION
		sdY.getSeriesPalette().update(0);
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("Unit Price1");
		bs1.setDataSet(dsNumericValues1);
		bs1.getLabel().setVisible(true);
		bs1.getLabel().setBackground(ColorDefinitionImpl.WHITE());
		bs1.setRiserOutline(null);
		bs1.getLabel().getOutline().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);
		bs1.setStacked(true);

		// CREATE THE PRIMARY DATASET
		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Unit Price2");
		bs2.setDataSet(dsNumericValues2);
		bs2.setStacked(true);
		bs2.setRiserOutline(null);
		bs2.getLabel().setVisible(true);
		bs2.getLabel().setBackground(ColorDefinitionImpl.WHITE());
		bs2.setLabelPosition(Position.INSIDE_LITERAL);
		bs2.getLabel().getOutline().setVisible(true);
		
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(bs2);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		sdX.getSeries().add(seBase);

		// ADD THE AXES
		cwaLogarithmic.getAxes().add(xAxisPrimary);

		return cwaLogarithmic;
	}
	/**
	 * Creates a pie chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createSimplePieChartDataPoint() {
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
		cwoaPie.getTitle().getLabel().getCaption().setValue("Simple Pie Chart with data point set");
		cwoaPie.getTitle().getOutline().setVisible(true);
		cwoaPie.getTitle().getLabel().setShadowColor(ColorDefinitionImpl.GREY());
		cwoaPie.getTitle().getLabel().setBackground(ColorDefinitionImpl.GREEN());
		cwoaPie.getTitle().getLabel().getOutline().setVisible(true);
		cwoaPie.getTitle().getLabel().getOutline().setColor(ColorDefinitionImpl.PINK());
		cwoaPie.getTitle().getLabel().setInsets(InsetsImpl.create(100,100,10,5));

		
		//la.
		//cwoaPie.getTitle().getLabel().getOutline().setThickness(14);
		

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
						.create("file:///C:\\actuate\\iard\\org.eclipse.birt.chart.demo\\images\\greenstone.gif"),
				GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
						ColorDefinitionImpl.create(0, 0, 0), -35, false),
				ColorDefinitionImpl.CREAM() };
		sd.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaBase.length; i++) {
			sd.getSeriesPalette().getEntries().add(fiaBase[i]);
		}

		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);
		seCategory.setVisible(true);
		
		
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
		DataPoint dp = sePie1.getDataPoint();
		dp.getComponents().clear();
		dp.setPrefix("(");
		dp.setSuffix(")");
		dp.setSeparator(";");
		dp.getComponents().add(
				DataPointComponentImpl.create(
						DataPointComponentType.BASE_VALUE_LITERAL,
						JavaNumberFormatSpecifierImpl.create("0.00")));
		dp.getComponents().add(
				DataPointComponentImpl.create(
						DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
						JavaNumberFormatSpecifierImpl.create("0.00")));
		sdCity.getSeries().add(sePie1);

		return cwoaPie;
	}

	/**
	 * Creates a sample numeric scatter chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createNumericScatterChartMarker() {
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
		ss.getMarker().setType(MarkerType.BOX_LITERAL);
		ss.getLineAttributes().setVisible(true);
		ss.getLineAttributes().setStyle(LineStyle.DASHED_LITERAL);
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
	 * Creates a pie chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createSimplePieChartTitleLabel() {
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
		cwoaPie.getTitle().getLabel().getCaption().setValue("Simple Pie Chart");
		cwoaPie.getTitle().getOutline().setVisible(true);
		cwoaPie.getTitle().getLabel().setShadowColor(ColorDefinitionImpl.GREY());
		cwoaPie.getTitle().getLabel().setBackground(ColorDefinitionImpl.GREEN());
		cwoaPie.getTitle().getLabel().getOutline().setVisible(true);
		cwoaPie.getTitle().getLabel().getOutline().setColor(ColorDefinitionImpl.PINK());
		cwoaPie.getTitle().getLabel().setInsets(InsetsImpl.create(100,100,10,5));	

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
						.create("file:///C:\\actuate\\iard\\org.eclipse.birt.chart.demo\\images\\greenstone.gif"),
				GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
						ColorDefinitionImpl.create(0, 0, 0), -35, false),
				ColorDefinitionImpl.CREAM() };
		sd.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaBase.length; i++) {
			sd.getSeriesPalette().getEntries().add(fiaBase[i]);
		}

		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);
		seCategory.setVisible(true);
		
		
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
	 * Creates a pie chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createSimplePieChartExplosionLeaderLine() {
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
		cwoaPie.getTitle().getLabel().getCaption().setValue("Simple Pie Chart");
		cwoaPie.getTitle().getOutline().setVisible(true);
		cwoaPie.getTitle().getLabel().setShadowColor(ColorDefinitionImpl.GREY());
		cwoaPie.getTitle().getLabel().setBackground(ColorDefinitionImpl.GREEN());
		cwoaPie.getTitle().getLabel().getOutline().setVisible(true);
		cwoaPie.getTitle().getLabel().getOutline().setColor(ColorDefinitionImpl.PINK());
		cwoaPie.getTitle().getLabel().setInsets(InsetsImpl.create(100,100,10,5));
		

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
						.create("file:///C:\\actuate\\iard\\org.eclipse.birt.chart.demo\\images\\greenstone.gif"),
				GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
						ColorDefinitionImpl.create(0, 0, 0), -35, false),
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
		sePie1.setExplosion(90);
		sePie1.getLeaderLineAttributes().setStyle(LineStyle.SOLID_LITERAL);
		sePie1.getLeaderLineAttributes().setColor(ColorDefinitionImpl.PINK());
		sePie1.getLeaderLineAttributes().setVisible(true);
		sePie1.getLeaderLineAttributes().setThickness(10);
		
		sePie1.setSliceOutline(ColorDefinitionImpl.YELLOW());
		
		sdCity.getSeries().add(sePie1);

		return cwoaPie;
	}

	
	
	/**
	 * Creates an instance of a logarithmic stacked line chart
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createLinearStackedLineChartScale() {
		System.getProperty("user.dir");
		ChartWithAxes cwaLogarithmic = ChartWithAxesImpl.create();
		cwaLogarithmic.getTitle().getLabel().getCaption().setValue(
				"Linear Line Stacked Chart");
		cwaLogarithmic.getBlock().setBackground(
				GradientImpl.create(ColorDefinitionImpl.GREY(),
						ColorDefinitionImpl.create(225, 225, 255), -35, false));

		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLogarithmic)
				.getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().getCaption().setValue("X Axis");
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLACK());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(36);
		xAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.CREAM());
		xAxisPrimary.getTitle().setVisible(true);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.RED());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getOrigin().setValue(NumberDataElementImpl.create(10));

		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLogarithmic)
				.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().setVisible(true);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLACK());
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(-53);
		yAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.ORANGE());
		yAxisPrimary.getTitle().getCaption().setValue(
				"This is the Primary Y-Axis Title");
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		//yAxisPrimary.setType(AxisType.TEXT_LITERAL);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMinorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.CYAN());
		yAxisPrimary.getScale().setMinorGridsPerUnit(1);
		yAxisPrimary.getScale().setMax(NumberDataElementImpl.create(1500));
		yAxisPrimary.getScale().setMin(NumberDataElementImpl.create(1000));
		
		yAxisPrimary.getScale().setStep(100);
		yAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);

		TextDataSet dsDateValues = TextDataSetImpl.create(new String[] { "one",
				"two", "three", "four", "five", "six", "seven" });

		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] {
				5.45, 352.02, 1005.36, 299.19, 0.43, 0.05, 58.62 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] {
				55.45, 152.02, 205.36, 799.19, 45, 21, 0.062 });

		// CREATE THE CATEGORY SERIES
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(0);
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		// CREATE THE PRIMARY DATASET
		LineSeries ls1 = (LineSeries) LineSeriesImpl.create();
		ls1.setSeriesIdentifier("Unit Price1");
		ls1.setDataSet(dsNumericValues1);
		ls1.getLineAttributes().setVisible(true);
		ls1.getLineAttributes().setThickness(1);
		ls1.getMarker().setType(MarkerType.TRIANGLE_LITERAL);
		ls1.getLabel().getCaption().setColor(
				ColorDefinitionImpl.GREEN().darker());
		ls1.setLabelPosition(Position.BELOW_LITERAL);
		ls1.setStacked(true);

		// CREATE THE PRIMARY DATASET
		LineSeries ls2 = (LineSeries) LineSeriesImpl.create();
		ls2.setSeriesIdentifier("Unit Price2");
		ls2.setDataSet(dsNumericValues2);
		ls2.getLineAttributes().setVisible(true);
		ls2.getLineAttributes().setThickness(1);
		ls2.getMarker().setType(MarkerType.BOX_LITERAL);
		ls2.setStacked(true);
		ls2.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(ls1);
		sdY.getSeries().add(ls2);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		sdX.getSeries().add(seBase);

		// ADD THE AXES
		cwaLogarithmic.getAxes().add(xAxisPrimary);

		return cwaLogarithmic;
	}

	public static final Chart createDateValueBarChartScaleType() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);

		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(true);
		cwaBar.getTitle().getLabel().getCaption().setValue(
				"Test Grid and Ticks");

		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DASH_DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setThickness(0);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(0, 0, 0));
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getMajorGrid().getTickAttributes().setVisible(true);
		xAxisPrimary.getMajorGrid().getTickAttributes().setColor(
				ColorDefinitionImpl.RED());
		xAxisPrimary.getMajorGrid().getTickAttributes().setStyle(
				LineStyle.SOLID_LITERAL);
		xAxisPrimary.getMajorGrid().getTickAttributes().setThickness(2);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ACROSS_LITERAL);

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);	
		yAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		yAxisPrimary.getMajorGrid().getTickAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().getTickAttributes().setColor(
				ColorDefinitionImpl.RED());
		yAxisPrimary.getMajorGrid().getTickAttributes().setStyle(
				LineStyle.SOLID_LITERAL);
	
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.RIGHT_LITERAL);

		yAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);

		yAxisPrimary.getMinorGrid().getLineAttributes().setStyle(
				LineStyle.SOLID_LITERAL);
		yAxisPrimary.getMinorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.BLUE());
		
		yAxisPrimary.getMinorGrid().getTickAttributes().setVisible(true);

		yAxisPrimary.getMinorGrid().getTickAttributes().setColor(
				ColorDefinitionImpl.RED());
		yAxisPrimary.getMinorGrid().getTickAttributes().setStyle(
				LineStyle.DASHED_LITERAL);
		yAxisPrimary.getMinorGrid().setTickStyle(TickStyle.ACROSS_LITERAL);

		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new SimpleDateFormat("yyMMddHHmmss")
					.parse("010704120856"));
			yAxisPrimary.getScale().setMin(DateTimeDataElementImpl.create(cal));
			cal = Calendar.getInstance();
			cal.setTime(new SimpleDateFormat("yyMMddHHmmss")
					.parse("030704120856"));
			yAxisPrimary.getScale().setMax(DateTimeDataElementImpl.create(cal));
			
			MarkerRange mr;			
			
			yAxisPrimary.getLabel().getOutline().setVisible(true);			
			
			Calendar cal1, cal2;
			cal1 = Calendar.getInstance();
			cal2 = Calendar.getInstance();
			cal2.setTime(new SimpleDateFormat("yyMMddHHmmss")
					.parse("020704120856"));
			cal1.setTime(new SimpleDateFormat("yyMMddHHmmss")
					.parse("010804120856"));
			mr = MarkerRangeImpl.create(yAxisPrimary, DateTimeDataElementImpl
					.create(cal1), DateTimeDataElementImpl.create(cal2),
					ColorDefinitionImpl.RED().translucent());
			
			
			//			mr.setLabelPosition(Position.OUTSIDE_LITERAL);
			mr.setLabelAnchor(Anchor.NORTH_EAST_LITERAL);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		yAxisPrimary.getLabel().getCaption().getFont().setRotation(37);

		Vector vs = new Vector();
		vs.add("zero");
		vs.add("one");
		vs.add("two");

		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2001, 5, 1),
						new CDateTime(2001, 4, 11), new CDateTime(2001, 8, 23) });

		TextDataSet categoryValues = TextDataSetImpl.create(vs);

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("My Bar Series");
		bs1.setDataSet(dsDateValues);
		bs1.setRiserOutline(null);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getQuery().setDefinition("someConst");
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getQuery().setDefinition("someExpr(abc)"); // NEEDED FOR DATA
		// EXTRACTION AND RUNTIME
		// SERIES REPLICATION IN
		// AN APPLICATION CONTEXT
		sdY.getSeriesPalette().update(1); // SET THE COLOR IN THE PALETTE
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		sdX.getSeries().add(seCategory);
		sdY.getSeries().add(bs1);

		return cwaBar;
	}

	public static final Chart createDateValueBarChartGrid() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);

		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(true);
		cwaBar.getTitle().getLabel().getCaption().setValue(
				"Test Grid and Ticks");

		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DASH_DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setThickness(0);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(0, 0, 0));
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getMajorGrid().getTickAttributes().setVisible(true);
		xAxisPrimary.getMajorGrid().getTickAttributes().setColor(
				ColorDefinitionImpl.RED());
		xAxisPrimary.getMajorGrid().getTickAttributes().setStyle(
				LineStyle.SOLID_LITERAL);
		xAxisPrimary.getMajorGrid().getTickAttributes().setThickness(2);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ACROSS_LITERAL);

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		
		yAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		yAxisPrimary.getMajorGrid().getTickAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().getTickAttributes().setColor(
				ColorDefinitionImpl.RED());
		yAxisPrimary.getMajorGrid().getTickAttributes().setStyle(
				LineStyle.SOLID_LITERAL);
		
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.RIGHT_LITERAL);

		yAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);

		yAxisPrimary.getMinorGrid().getLineAttributes().setStyle(
				LineStyle.SOLID_LITERAL);
		yAxisPrimary.getMinorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.BLUE());
		
		yAxisPrimary.getMinorGrid().getTickAttributes().setVisible(true);

		yAxisPrimary.getMinorGrid().getTickAttributes().setColor(
				ColorDefinitionImpl.RED());
		yAxisPrimary.getMinorGrid().getTickAttributes().setStyle(
				LineStyle.DASHED_LITERAL);
		yAxisPrimary.getMinorGrid().setTickStyle(TickStyle.ACROSS_LITERAL);

		yAxisPrimary.getMinorGrid().unsetTickCount();
		yAxisPrimary.getMinorGrid().setTickCount(3);

		yAxisPrimary.getScale().setMinorGridsPerUnit(2);

		yAxisPrimary.getLabel().getCaption().getFont().setRotation(37);

		Vector vs = new Vector();
		vs.add("zero");
		vs.add("one");
		vs.add("two");

		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2001, 5, 1),
						new CDateTime(2001, 4, 11), new CDateTime(2001, 8, 23) });

		TextDataSet categoryValues = TextDataSetImpl.create(vs);

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("My Bar Series");
		bs1.setDataSet(dsDateValues);
		bs1.setRiserOutline(null);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getQuery().setDefinition("someConst");
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getQuery().setDefinition("someExpr(abc)"); // NEEDED FOR DATA
		// EXTRACTION AND RUNTIME
		// SERIES REPLICATION IN
		// AN APPLICATION CONTEXT
		sdY.getSeriesPalette().update(1); // SET THE COLOR IN THE PALETTE
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		sdX.getSeries().add(seCategory);
		sdY.getSeries().add(bs1);

		return cwaBar;
	}

	public static final Chart createDateValueBarChartFill() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);

		Plot p = cwaBar.getPlot();
		
		Gradient gr = GradientImpl.create(ColorDefinitionImpl.BLACK(),
				ColorDefinitionImpl.WHITE(), -45, false);

		p.getClientArea().setBackground(gr);
		p.getOutline().setVisible(true);
		cwaBar.getTitle().getLabel().getCaption().setValue("Test Gradient");

		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DASH_DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setThickness(0);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(0, 0, 0));
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getMajorGrid().getTickAttributes().setVisible(true);
		xAxisPrimary.getMajorGrid().getTickAttributes().setColor(
				ColorDefinitionImpl.RED());
		xAxisPrimary.getMajorGrid().getTickAttributes().setStyle(
				LineStyle.SOLID_LITERAL);
		xAxisPrimary.getMajorGrid().getTickAttributes().setThickness(2);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ACROSS_LITERAL);

		xAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getMinorGrid().getLineAttributes().setStyle(
				LineStyle.DASHED_LITERAL);
		
		xAxisPrimary.getMinorGrid().getTickAttributes().setVisible(true);
		xAxisPrimary.getMinorGrid().getTickAttributes().setColor(
				ColorDefinitionImpl.RED());
		xAxisPrimary.getMinorGrid().getTickAttributes().setStyle(
				LineStyle.SOLID_LITERAL);
		xAxisPrimary.getMinorGrid().getTickAttributes().setThickness(2);
		xAxisPrimary.getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);

		xAxisPrimary.getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMinorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.BLUE());

		xAxisPrimary.getTitle().setVisible(true);

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(37);

		Vector vs = new Vector();
		vs.add("zero");
		vs.add("one");
		vs.add("two");

		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2001, 5, 1),
						new CDateTime(2001, 4, 11), new CDateTime(2001, 8, 23) });

		TextDataSet categoryValues = TextDataSetImpl.create(vs);

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("My Bar Series");
		bs1.setDataSet(dsDateValues);
		bs1.setRiserOutline(null);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getQuery().setDefinition("someConst");

		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getQuery().setDefinition("someExpr(abc)"); // NEEDED FOR DATA
		// EXTRACTION AND RUNTIME
		// SERIES REPLICATION IN
		// AN APPLICATION CONTEXT
		sdY.getSeriesPalette().update(1); // SET THE COLOR IN THE PALETTE
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		sdX.getSeries().add(seCategory);
		sdY.getSeries().add(bs1);

		return cwaBar;
	}

	/**
	 * Creates a bar chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createDateValueBarChartFillImage() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);
	

		Plot p = cwaBar.getPlot();
	
		Image imgTiled = ImageImpl.create("http://qabee/BIRT/actuatetop.jpg");
		p.getClientArea().setBackground(imgTiled);
		p.getOutline().setVisible(false);
		cwaBar.getTitle().getLabel().getCaption().setValue(
				"test background image");
		

		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);

		xAxisPrimary.getTitle().setVisible(true);


		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);

		yAxisPrimary.getLabel().getCaption().getFont().setRotation(37);

		Vector vs = new Vector();
		vs.add("zero");
		vs.add("one");
		vs.add("two");

		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2001, 5, 1),
						new CDateTime(2001, 4, 11), new CDateTime(2001, 8, 23) });

		TextDataSet categoryValues = TextDataSetImpl.create(vs);

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("My Bar Series");
		bs1.setDataSet(dsDateValues);
		bs1.setRiserOutline(null);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getQuery().setDefinition("someConst");
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getQuery().setDefinition("someExpr(abc)"); // NEEDED FOR DATA
		// EXTRACTION AND RUNTIME
		// SERIES REPLICATION IN
		// AN APPLICATION CONTEXT
		sdY.getSeriesPalette().update(1); // SET THE COLOR IN THE PALETTE
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		sdX.getSeries().add(seCategory);
		sdY.getSeries().add(bs1);

		return cwaBar;
	}

	/**
	 * Creates a line chart model as a reference implementation with a marker
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createSimpleLineChartMarkerLine() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		cwaBar.getTitle().getLabel().getCaption().setValue("Simple Line Chart");

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

		yAxisPrimary.getLabel().getOutline().setVisible(true);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary,
				NumberDataElementImpl.create(20));
		ml.setLabelAnchor(Anchor.SOUTH_EAST_LITERAL);
		
		MarkerRange mr = MarkerRangeImpl.create(yAxisPrimary,
				NumberDataElementImpl.create(10), NumberDataElementImpl
						.create(20), ColorDefinitionImpl.GREEN().translucent());
		mr.setLabelAnchor(Anchor.SOUTH_EAST_LITERAL);
		mr.setFill(GradientImpl.create(ColorDefinitionImpl.RED(),
				ColorDefinitionImpl.create(0, 0, 0), 20, true));

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
	 * Creates a bar chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createDateValueBarChartMarkerLine() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);

		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));

		p.getOutline().setVisible(false);
		cwaBar.getTitle().getLabel().getCaption().setValue("test markerline");
	

		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		
		xAxisPrimary.getTitle().setVisible(true);
		MarkerRange mr = MarkerRangeImpl.create(xAxisPrimary,
				NumberDataElementImpl.create(0.5), NumberDataElementImpl
						.create(1.35), ColorDefinitionImpl.ORANGE()
						.translucent());
		mr.setLabelAnchor(Anchor.NORTH_EAST_LITERAL);

		MarkerLine ml = MarkerLineImpl.create(xAxisPrimary,
				NumberDataElementImpl.create(1.5));
		ml.setLabelAnchor(Anchor.NORTH_EAST_LITERAL);

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(37);

		Vector vs = new Vector();
		vs.add("zero");
		vs.add("one");
		vs.add("two");

		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2001, 5, 1),
						new CDateTime(2001, 4, 11), new CDateTime(2001, 8, 23) });

		TextDataSet categoryValues = TextDataSetImpl.create(vs);

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("My Bar Series");
		bs1.setDataSet(dsDateValues);
		bs1.setRiserOutline(null);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getQuery().setDefinition("someConst");
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getQuery().setDefinition("someExpr(abc)"); // NEEDED FOR DATA
		// EXTRACTION AND RUNTIME
		// SERIES REPLICATION IN
		// AN APPLICATION CONTEXT
		sdY.getSeriesPalette().update(1); // SET THE COLOR IN THE PALETTE
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		sdX.getSeries().add(seCategory);
		sdY.getSeries().add(bs1);

		return cwaBar;
	}

	/**
	 * Creates a bar chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createDateValueBarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);

		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(false);
		cwaBar.getTitle().getLabel().getCaption().setValue("Simple Bar Chart");
		

		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
	
		xAxisPrimary.getTitle().setVisible(true);

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(37);

		Vector vs = new Vector();
		vs.add("zero");
		vs.add("one");
		vs.add("two");

		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2001, 5, 1),
						new CDateTime(2001, 4, 11), new CDateTime(2001, 8, 23) });

		TextDataSet categoryValues = TextDataSetImpl.create(vs);

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("My Bar Series");
		bs1.setDataSet(dsDateValues);
		bs1.setRiserOutline(null);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getQuery().setDefinition("someConst");
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getQuery().setDefinition("someExpr(abc)"); // NEEDED FOR DATA
		// EXTRACTION AND RUNTIME
		// SERIES REPLICATION IN
		// AN APPLICATION CONTEXT
		sdY.getSeriesPalette().update(1); // SET THE COLOR IN THE PALETTE
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		sdX.getSeries().add(seCategory);
		sdY.getSeries().add(bs1);

		return cwaBar;
	}

	/**
	 * Creates a bar chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createSimpleBarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);

		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(false);
		cwaBar.getTitle().getLabel().getCaption().setValue("Simple Bar Chart");

		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);
		/*
		 * MarkerRange mr = MarkerRangeImpl.create( xAxisPrimary,
		 * NumberDataElementImpl.create(0.5),
		 * NumberDataElementImpl.create(1.35),
		 * ColorDefinitionImpl.ORANGE().translucent() );
		 * mr.setLabelAnchor(Anchor.NORTH_EAST_LITERAL);
		 * 
		 * MarkerLine ml = MarkerLineImpl.create(xAxisPrimary,
		 * NumberDataElementImpl.create(1.5));
		 * ml.setLabelAnchor(Anchor.NORTH_EAST_LITERAL);
		 */

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		//yAxisPrimary.getLabel().getInsets().setTop(10);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);
		yAxisPrimary.setFormatSpecifier(JavaNumberFormatSpecifierImpl
				.create("AgI\n##"));
		/*
		 * yAxisPrimary.getLabel().getOutline().setVisible(true); ml =
		 * MarkerLineImpl.create(yAxisPrimary,
		 * NumberDataElementImpl.create(23));
		 * ml.setLabelAnchor(Anchor.NORTH_EAST_LITERAL); mr =
		 * MarkerRangeImpl.create( yAxisPrimary,
		 * NumberDataElementImpl.create(-15), NumberDataElementImpl.create(-20),
		 * ColorDefinitionImpl.GREEN().translucent() );
		 * mr.setLabelPosition(Position.OUTSIDE_LITERAL);
		 * mr.setLabelAnchor(Anchor.NORTH_EAST_LITERAL);
		 */

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
		// EXTRACTION AND RUNTIME
		// SERIES REPLICATION IN
		// AN APPLICATION CONTEXT
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
		cwaBar.getTitle().getLabel().getCaption().setValue("Simple Bar Chart");

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
		// EXTRACTION AND
		// RUNTIME SERIES REPLICATION IN
		// AN APPLICATION CONTEXT
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
	public static final Chart createSimpleLineChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		cwaBar.getTitle().getLabel().getCaption().setValue("Simple Line Chart");

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
	public static final Chart createSimplePieChart() {
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
		cwoaPie.getTitle().getLabel().getCaption().setValue("Simple Pie Chart");
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
	 * Creates a sample chart with no series associated with the primary axes
	 * 
	 * @return An instance of the simulated chart model
	 */
	public static final Chart createEmptyChart() {
		ChartWithAxes cwaEmpty = ChartWithAxesImpl.create();
		cwaEmpty.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaEmpty.getTitle().getLabel().getCaption().setValue("Hello Chart");
		Axis axPrimaryBase = (Axis) cwaEmpty.getAxes().get(0);
		Axis axPrimaryOrthogonal = cwaEmpty
				.getPrimaryOrthogonalAxis(axPrimaryBase);

		axPrimaryBase.setType(AxisType.LOGARITHMIC_LITERAL);
		axPrimaryOrthogonal.setType(AxisType.LOGARITHMIC_LITERAL);

		// ADDS AN OVERLAY AXIS
		/*
		 * EList elBaseAxes = cwaEmpty.getAxes(); Axis axPrimaryBase = (Axis)
		 * elBaseAxes.get(0); Axis axPrimaryOrthogonal =
		 * cwaEmpty.getPrimaryOrthogonalAxis(axPrimaryBase);
		 * axPrimaryOrthogonal.getLabel().getCaption().getFont().setRotation(90);
		 * axPrimaryOrthogonal.getLabel().getOutline().setVisible(true); Axis
		 * axOverlayOrthogonal = AxisImpl.create(Axis.ORTHOGONAL);
		 * axOverlayOrthogonal.setType(AxisType.LINEAR_LITERAL);
		 * //axPrimaryBase.getAssociatedAxes().add(axOverlayOrthogonal);
		 * axOverlayOrthogonal = AxisImpl.create(Axis.ORTHOGONAL);
		 * axOverlayOrthogonal.setType(AxisType.LINEAR_LITERAL);
		 * //axPrimaryBase.getAssociatedAxes().add(axOverlayOrthogonal);
		 */

		return cwaEmpty;
	}

	/**
	 * Creates a sample percent stacked chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createPercentStackedChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.getTitle().getLabel().getCaption().setValue(
				"Stacked Bars (try Percent?)");
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		p.getClientArea().getInsets().set(5, 0, 5, 0);
		p.getOutline().setVisible(true);

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

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setFormatSpecifier(JavaNumberFormatSpecifierImpl
				.create("0'%'"));
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.RED());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setPercent(true);

		Vector vs = new Vector();
		vs.add("one");
		vs.add("two");
		vs.add("three");

		Vector vn1 = new Vector();
		vn1.add(new Double(25));
		vn1.add(new Double(35));
		vn1.add(new Double(45));

		TextDataSet categoryValues = TextDataSetImpl.create(vs);

		NumberDataSet orthoValues1 = NumberDataSetImpl.create(vn1);
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] {
				45, -15, -65 });

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setDataSet(orthoValues1);
		bs1.setStacked(true);
		bs1.setSeriesIdentifier("Bar 1");
		bs1.setRiserOutline(null);
		bs1.getLabel().setVisible(true);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setDataSet(orthoValues2);
		bs2.setStacked(true);
		bs2.setSeriesIdentifier("Bar 2");
		bs2.setRiserOutline(null);
		bs2.getLabel().setVisible(true);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().update(1);
		sdX.getQuery().setDefinition("exprX(Table.Column)");
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(0);
		sdY.getQuery().setDefinition("exprY(Table.Column)"); // NEEDED FOR DATA
		// EXTRACTION
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		sdX.getSeries().add(seCategory);
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(bs2);

		return cwaBar;
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
						// FORMAT
						// TO BE
						// SPECIFIED
						// FOR
						// TEXT
						// VALUES
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
		/*
		 * dp.getComponents().add( DataPointComponentImpl.create(
		 * DataPointComponentType.BASE_VALUE_LITERAL, null // NO FORMAT TO BE
		 * SPECIFIED FOR TEXT VALUES ) ); dp.getComponents().add(
		 * DataPointComponentImpl.create(
		 * DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
		 * JavaNumberFormatSpecifierImpl.create("0.00") ) );
		 */
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
	public static final Chart createNumericScatterChart() {
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
	 * Creates a sample date scatter chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createDateScatterChart() {
		System.getProperty("user.dir");
		ChartWithAxes cwaCombination = ChartWithAxesImpl.create();
		cwaCombination.getTitle().getLabel().getCaption().setValue(
				"Date/time Scatter Chart");
		cwaCombination.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaCombination.getPlot().getClientArea().setBackground(
				ColorDefinitionImpl.CYAN());

		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaCombination)
				.getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().getCaption().setValue("X Axis");
		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		//xAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		xAxisPrimary.getLabel().getCaption()
				.setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(-65);
		xAxisPrimary.getTitle().getCaption().setValue(
				"This is the Primary X-Axis Title");
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);

		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaCombination)
				.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisPrimary.getTitle().getCaption().setValue(
				"This is the Primary Y-Axis Title");
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2001, 5, 1),
						new CDateTime(2001, 4, 11), new CDateTime(2001, 8, 23),
						new CDateTime(2001, 1, 19), new CDateTime(2001, 6, 28),
						new CDateTime(2001, 8, 19), new CDateTime(2001, 3, 6) });
		NumberDataSet dsNumericValues = NumberDataSetImpl.create(new double[] {
				125.99, 352.95, -201.95, 299.95, -95.95, 25.95, 58.95 });

		// CREATE THE CATEGORY SERIES
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		// CREATE THE PRIMARY DATASET
		ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create();
		ss.setSeriesIdentifier("Unit Price");
		ss.setDataSet(dsNumericValues);
		ss.setLabelPosition(Position.BELOW_LITERAL);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ss);

		return cwaCombination;
	}

	/**
	 * Creates an instance of a logarithmic stacked line chart
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createLogarithmicStackedLineChart() {
		System.getProperty("user.dir");
		ChartWithAxes cwaLogarithmic = ChartWithAxesImpl.create();
		cwaLogarithmic.getTitle().getLabel().getCaption().setValue(
				"Logarithmic Line Stacked Chart");
		cwaLogarithmic.getBlock().setBackground(
				GradientImpl.create(ColorDefinitionImpl.GREY(),
						ColorDefinitionImpl.create(225, 225, 255), -35, false));

		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLogarithmic)
				.getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().getCaption().setValue("X Axis");
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		//xAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLACK());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(36);
		xAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.CREAM());
		xAxisPrimary.getTitle().setVisible(true);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.RED());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getOrigin().setValue(NumberDataElementImpl.create(10));

		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLogarithmic)
				.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLACK());
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(-53);
		yAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.ORANGE());
		yAxisPrimary.getTitle().getCaption().setValue(
				"This is the Primary Y-Axis Title");
		yAxisPrimary.setType(AxisType.LOGARITHMIC_LITERAL);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMinorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.CYAN());
		yAxisPrimary.getScale().setMinorGridsPerUnit(10);
		yAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		//yAxisPrimary.setPercent(true);

		TextDataSet dsDateValues = TextDataSetImpl.create(new String[] { "one",
				"two", "three", "four", "five", "six", "seven" });

		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] {
				5.45, 352.02, 1005.36, 299.19, 0.43, 0.05, 58.62 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] {
				55.45, 152.02, 205.36, 799.19, 45, 21, 0.062 });

		// CREATE THE CATEGORY SERIES
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(0);
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		// CREATE THE PRIMARY DATASET
		LineSeries ls1 = (LineSeries) LineSeriesImpl.create();
		ls1.setSeriesIdentifier("Unit Price1");
		ls1.setDataSet(dsNumericValues1);
		ls1.getLineAttributes().setVisible(true);
		ls1.getLineAttributes().setThickness(1);
		ls1.getMarker().setType(MarkerType.TRIANGLE_LITERAL);
		ls1.getLabel().getCaption().setColor(
				ColorDefinitionImpl.GREEN().darker());
		ls1.setLabelPosition(Position.BELOW_LITERAL);
		ls1.setStacked(true);

		// CREATE THE PRIMARY DATASET
		LineSeries ls2 = (LineSeries) LineSeriesImpl.create();
		ls2.setSeriesIdentifier("Unit Price2");
		ls2.setDataSet(dsNumericValues2);
		ls2.getLineAttributes().setVisible(true);
		ls2.getLineAttributes().setThickness(1);
		ls2.getMarker().setType(MarkerType.BOX_LITERAL);
		ls2.setStacked(true);
		ls2.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());
		sdY.getSeries().add(ls1);
		sdY.getSeries().add(ls2);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		sdX.getSeries().add(seBase);

		// ADD THE AXES
		cwaLogarithmic.getAxes().add(xAxisPrimary);

		return cwaLogarithmic;
	}

	/**
	 * Creates an instance of a logarithmic stacked bar chart
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createLogarithmicStackedBarChart() {
		System.getProperty("user.dir");
		ChartWithAxes cwaLogarithmic = ChartWithAxesImpl.create();
		cwaLogarithmic.getTitle().getLabel().getCaption().setValue(
				"Logarithmic Bar Stacked Chart");
		cwaLogarithmic.getBlock().setBackground(
				GradientImpl.create(ColorDefinitionImpl.GREY(),
						ColorDefinitionImpl.create(225, 225, 255), -35, false));

		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLogarithmic)
				.getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().getCaption().setValue("X Axis");
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		//xAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLACK());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(36);
		xAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.CREAM());
		xAxisPrimary.getTitle().getCaption().setValue(
				"This is the Primary X-Axis Title");
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.RED());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLogarithmic)
				.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLACK());
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(-53);
		yAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.ORANGE());
		yAxisPrimary.getTitle().getCaption().setValue(
				"This is the Primary Y-Axis Title");
		yAxisPrimary.setType(AxisType.LOGARITHMIC_LITERAL);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMinorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.CYAN());
		yAxisPrimary.getScale().setMinorGridsPerUnit(10);
		yAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		//yAxisPrimary.setPercent(true);

		TextDataSet dsDateValues = TextDataSetImpl.create(new String[] { "one",
				"two", "three", "four", "five", "six", "seven" });

		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] {
				5.45, 352.02, 1005.36, 299.19, 0.43, 0.05, 58.62 });
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] {
				55.45, 52.02, 105.36, 799.19, 45, 21, 0.062 });

		// CREATE THE CATEGORY SERIES
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		// EXTRACTION
		sdY.getSeriesPalette().update(0);
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("Unit Price1");
		bs1.setDataSet(dsNumericValues1);
		bs1.getLabel().setVisible(true);
		bs1.getLabel().setBackground(ColorDefinitionImpl.WHITE());
		bs1.setRiserOutline(null);
		bs1.getLabel().getOutline().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);
		bs1.setStacked(true);

		// CREATE THE PRIMARY DATASET
		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Unit Price2");
		bs2.setDataSet(dsNumericValues2);
		bs2.setStacked(true);
		bs2.setRiserOutline(null);
		bs2.getLabel().setVisible(true);
		bs2.getLabel().setBackground(ColorDefinitionImpl.WHITE());
		bs2.setLabelPosition(Position.INSIDE_LITERAL);
		bs2.getLabel().getOutline().setVisible(true);
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(bs2);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		sdX.getSeries().add(seBase);

		// ADD THE AXES
		cwaLogarithmic.getAxes().add(xAxisPrimary);

		return cwaLogarithmic;
	}

	/**
	 * Creates a sample scatter chart instance with a logarithmic scale
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createLogarithmicScatterChart() {
		ChartWithAxes cwaLogarithmic = ChartWithAxesImpl.create();
		cwaLogarithmic.getTitle().getLabel().getCaption().setValue(
				"Logarithmic Scatter Chart");
		cwaLogarithmic.getBlock().setBackground(
				GradientImpl.create(ColorDefinitionImpl.GREY(),
						ColorDefinitionImpl.create(225, 225, 255), -35, false));

		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaLogarithmic)
				.getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().getCaption().setValue("X Axis");
		xAxisPrimary.setType(AxisType.LOGARITHMIC_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLACK());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(36);
		xAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.CREAM());
		xAxisPrimary.getTitle().getCaption().setValue(
				"This is the Primary X-Axis Title");
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.RED());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getMinorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.CYAN());
		xAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.unsetOrientation();
		xAxisPrimary.getScale().setMinorGridsPerUnit(10);

		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaLogarithmic)
				.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLACK());
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(-53);
		yAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.ORANGE());
		yAxisPrimary.getTitle().getCaption().setValue(
				"This is the Primary Y-Axis Title");
		yAxisPrimary.setType(AxisType.LOGARITHMIC_LITERAL);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMinorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.CYAN());
		yAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getScale().setMinorGridsPerUnit(10);

		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] {
				5.45, 352.02, 1005.36, 299.19, 0.43, 0.05, 58.62 });

		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create(new double[] {
				5.45, 352.02, 1005.36, 299.19, 0.43, 0.05, 58.62 });

		// CREATE THE CATEGORY SERIES
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsNumericValues1);

		// CREATE THE PRIMARY DATASET
		ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create();
		ss.setSeriesIdentifier("Quantity");
		ss.setDataSet(dsNumericValues2);
		ss.getLineAttributes().setVisible(true);
		ss.getLineAttributes().setThickness(1);
		ss.getMarker().setType(MarkerType.TRIANGLE_LITERAL);
		//ss.setCurve(true);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		// EXTRACTION
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ss);

		// ADD THE AXES
		cwaLogarithmic.getAxes().add(xAxisPrimary);

		return cwaLogarithmic;
	}

	/**
	 * Creates a sample combination chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createCombinationChart() {
		System.getProperty("user.dir");
		ChartWithAxes cwaCombination = ChartWithAxesImpl.create();

		Plot p = cwaCombination.getPlot();
		p.setBackground(GradientImpl.create(ColorDefinitionImpl.ORANGE(),
				ColorDefinitionImpl.CYAN(), -35, false));

		p.getClientArea().setBackground(
				ImageImpl.create(getURL("/images/sumida.gif")));
		p.getClientArea().getInsets().set(10, 10, 20, 20);

		Legend lg = cwaCombination.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.RED(),
				ColorDefinitionImpl.BLUE(), -90, false));
		lg.getText().getFont().setName("Arial");
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().setLeft(10);
		lg.getInsets().setRight(10);

		lg.getClientArea().getInsets().set(2, 2, 2, 2);
		lg.getClientArea().setBackground(ColorDefinitionImpl.WHITE());

		cwaCombination.getTitle().setBackground(
				ImageImpl.create(getURL("/images/fishing.gif")));
		cwaCombination.getTitle().getLabel().getOutline().setStyle(
				LineStyle.SOLID_LITERAL);
		cwaCombination.getTitle().getLabel().setShadowColor(
				(ColorDefinition) EcoreUtil.copy(BaseRenderer.SHADOW));

		// CHART TITLE
		cwaCombination.getTitle().getLabel().getCaption().setValue(
				"Combination Chart\nwith Overlay Axes");
		cwaCombination.getTitle().getLabel().getCaption().getFont()
				.getAlignment().setHorizontalAlignment(
						HorizontalAlignment.CENTER_LITERAL);
		cwaCombination.getTitle().getLabel().setBackground(
				ColorDefinitionImpl.create(225, 225, 255));
		cwaCombination.getTitle().getLabel().getCaption().getFont()
				.setRotation(-5);
		cwaCombination.getTitle().getLabel().getCaption().getFont().setSize(36);
		cwaCombination.getTitle().getLabel().getCaption().getFont()
				.setStrikethrough(true);
		cwaCombination.getTitle().getLabel().getCaption().getFont()
				.setUnderline(true);
		cwaCombination.getTitle().getLabel().getInsets().set(0, 10, 5, 10);

		// X-AXIS
		Axis xAxisPrimary = cwaCombination.getPrimaryBaseAxes()[0];
		xAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.CYAN());
		xAxisPrimary.getLabel().setBackground(
				ColorDefinitionImpl.create(255, 255, 0, 127));
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(-90);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(64, 64, 64));
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getTitle().getCaption().setValue(
				"X-Axis Title sloping at 5?");
		xAxisPrimary.getTitle().getCaption().setColor(
				ColorDefinitionImpl.create(164, 164, 0));
		xAxisPrimary.getTitle().getCaption().getFont().setRotation(5);
		xAxisPrimary.getTitle().setBackground(
				ColorDefinitionImpl.create(255, 255, 255, 127));
		xAxisPrimary.getTitle().getOutline().setVisible(true);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getOrigin().setValue(NumberDataElementImpl.create(-100));
		xAxisPrimary.getLineAttributes().setColor(ColorDefinitionImpl.YELLOW());

		Axis yAxisPrimary = cwaCombination
				.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.getLabel().getCaption()
				.setColor(ColorDefinitionImpl.RED());

		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);
		yAxisPrimary.getTitle().getCaption().setValue(
				"This is the Primary Y-Axis Title");
		yAxisPrimary.getTitle().getCaption()
				.setColor(ColorDefinitionImpl.RED());

		yAxisPrimary.getLineAttributes().setColor(ColorDefinitionImpl.RED());
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);
		yAxisPrimary.getLabel().getOutline().setVisible(true);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.RED());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Y-AXIS (OVERLAY-1) - NEED TO BE CREATED MANUALLY
		Axis yAxisOverlay1 = AxisImpl.create(Axis.ORTHOGONAL);
		yAxisOverlay1.getLabel().getCaption().setValue("Quantity Axis");
		yAxisOverlay1.getLabel().setBackground(
				ColorDefinitionImpl.create(225, 255, 225));
		yAxisOverlay1.getLabel().getCaption().setColor(
				ColorDefinitionImpl.create(0, 128, 0));
		yAxisOverlay1.getLabel().setShadowColor(
				(ColorDefinitionImpl) EcoreUtil.copy(BaseRenderer.SHADOW));
		yAxisOverlay1.setTitlePosition(Position.RIGHT_LITERAL);
		yAxisOverlay1.getTitle().getCaption().setValue(
				"This is the Overlay Y-Axis1 Title");
		yAxisOverlay1.getTitle().getCaption().setColor(
				ColorDefinitionImpl.GREEN().darker());

		yAxisOverlay1.getTitle().getCaption().getFont().setRotation(-90);
		yAxisOverlay1.getTitle().getCaption().getFont().setBold(false);
		yAxisOverlay1.getTitle().getCaption().getFont().setSize(16);
		yAxisOverlay1.getTitle().setVisible(true);
		
		yAxisOverlay1.getLineAttributes().setColor(
				ColorDefinitionImpl.create(0, 128, 0));
		yAxisOverlay1.setType(AxisType.LINEAR_LITERAL);
		yAxisOverlay1.setOrientation(Orientation.VERTICAL_LITERAL);
		yAxisOverlay1.setType(AxisType.LINEAR_LITERAL);
		yAxisOverlay1.getMajorGrid().setTickStyle(TickStyle.RIGHT_LITERAL);
		yAxisOverlay1.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.SOLID_LITERAL);
		yAxisOverlay1.setLabelPosition(Position.RIGHT_LITERAL);
		yAxisOverlay1.getLabel().getCaption().getFont().setRotation(-25);
		yAxisOverlay1.getOrigin().setType(IntersectionType.MAX_LITERAL);

		Axis yAxisOverlay2 = AxisImpl.create(Axis.ORTHOGONAL);
		yAxisOverlay2.setTitlePosition(Position.RIGHT_LITERAL);
		yAxisOverlay2.getTitle().getCaption().setValue(
				"This is the Overlay Y-Axis2 Title");
		yAxisOverlay2.getTitle().getCaption().setColor(
				ColorDefinitionImpl.BLUE());

		yAxisOverlay2.getTitle().getCaption().getFont().setRotation(-90);
		yAxisOverlay2.getTitle().getCaption().getFont().setBold(false);
		yAxisOverlay2.getTitle().getCaption().getFont().setSize(16);
		yAxisOverlay2.getTitle().setVisible(true);
		//yAxisOverlay2.getTitle().getOutline().setVisible(true);
		yAxisOverlay2.getLabel().getCaption().setValue("Work Axis");
		yAxisOverlay2.getLabel().setBackground(
				ColorDefinitionImpl.create(225, 225, 255));
		yAxisOverlay2.getLabel().getCaption().setColor(
				ColorDefinitionImpl.BLUE());
		yAxisOverlay2.getLabel().getOutline().setVisible(true);
		yAxisOverlay2.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		yAxisOverlay2.setType(AxisType.LINEAR_LITERAL);
		yAxisOverlay2.setOrientation(Orientation.VERTICAL_LITERAL);
		yAxisOverlay2.setType(AxisType.LINEAR_LITERAL);
		yAxisOverlay2.getMajorGrid().setTickStyle(TickStyle.RIGHT_LITERAL);
		yAxisOverlay2.setLabelPosition(Position.RIGHT_LITERAL);
		yAxisOverlay2.getLabel().getCaption().getFont().setRotation(-90);
		yAxisOverlay2.getOrigin().setType(IntersectionType.MAX_LITERAL);

		// ASSOCIATE THE DATA
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] {
				"Hard Disk", "Camera", "CellPhone",
				"MP3 Player\nJukebox stuff",
				"This is a long\nline of text\nthat we split" });
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] {
				125.99, 352.16, -201.18, 299.22, -95.43 });
		NumberDataSet seriesFourValues = NumberDataSetImpl.create(new double[] {
				235.12, -70.24, 143.69, -125.43, 90.85 });
		NumberDataSet seriesTwoValues = NumberDataSetImpl.create(new double[] {
				-54.99, 301, 199.91, -99.98, 95.11 });
		NumberDataSet seriesThreeValues = NumberDataSetImpl
				.create(new double[] { 54.99, 21, 75.05, -39.49, -55.53 });

		// CREATE THE CATEGORY SERIES
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		// CREATE THE PRIMARY DATASET
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		Trigger tg = TriggerImpl.create(TriggerCondition.MOUSE_HOVER_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL,
						TooltipValueImpl.create(500, null)));
		bs1.getTriggers().add(tg);
		bs1.setSeriesIdentifier("Unit Price");
		bs1.setDataSet(seriesOneValues);
		bs1.setRiserOutline(ColorDefinitionImpl.BLACK());
		bs1.setRiser(RiserType.RECTANGLE_LITERAL);
		DataPoint dp = DataPointImpl.create("(", ")", ", ");
		dp.getComponents().clear();
		dp.getComponents().add(
				DataPointComponentImpl.create(
						DataPointComponentType.BASE_VALUE_LITERAL, null // NO
						// FORMAT
						// TO BE
						// SPECIFIED
						// FOR
						// TEXT
						// VALUES
						));
		dp.getComponents().add(
				DataPointComponentImpl.create(
						DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
						JavaNumberFormatSpecifierImpl.create("0.00")));
		bs1.setDataPoint(dp);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.getTriggers().add(
				TriggerImpl.create(TriggerCondition.MOUSE_CLICK_LITERAL,
						ActionImpl.create(ActionType.URL_REDIRECT_LITERAL,
								URLValueImpl.create("http://www.actuate.com",
										null, "base", "ortho", null))));
		bs2.setSeriesIdentifier("Price Growth");
		bs2.setDataSet(seriesFourValues);
		bs2.setRiserOutline(ColorDefinitionImpl.BLACK());
		bs2.setRiser(RiserType.TRIANGLE_LITERAL);

		// CREATE THE OVERLAY DATASET - SERIES1
		LineSeries ls1 = (LineSeries) LineSeriesImpl.create();
		ls1.setSeriesIdentifier("Quantity");
		ls1.setDataSet(seriesTwoValues);
		ls1.getLineAttributes().setColor(ColorDefinitionImpl.GREEN());
		ls1.getMarker().setType(MarkerType.BOX_LITERAL);
		ls1.setCurve(true);

		// CREATE THE OVERLAY DATASET - SERIES2
		LineSeries ls2 = (LineSeries) LineSeriesImpl.create();
		ls2.setSeriesIdentifier("Work");
		ls2.setDataSet(seriesThreeValues);
		ls2.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		ls2.getMarker().setType(MarkerType.TRIANGLE_LITERAL);
		tg = TriggerImpl.create(TriggerCondition.MOUSE_HOVER_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL,
						TooltipValueImpl.create(500, null)));
		ls2.getTriggers().add(tg);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeriesPalette().update(0); // SET THE COLOR IN THE PALETTE
		sdX.getSeries().add(seCategory);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(0);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(bs2);

		SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		sdY1.getSeriesPalette().update(ColorDefinitionImpl.RED());
		yAxisOverlay1.getSeriesDefinitions().add(sdY1);
		sdY1.getSeries().add(ls1);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		// EXTRACTION
		sdY2.getSeriesPalette().update(ColorDefinitionImpl.YELLOW());
		yAxisOverlay2.getSeriesDefinitions().add(sdY2);
		sdY2.getSeries().add(ls2);

		// SETUP AXIS HIERARCHY/DEPENDENCY FOR OVERLAY AXES ONLY
		xAxisPrimary.getAssociatedAxes().add(yAxisOverlay1); // ADD THE VALUE
		// AXIS TO THE
		// CATEGORY AXIS
		xAxisPrimary.getAssociatedAxes().add(yAxisOverlay2); // ADD THE VALUE
		// AXIS TO THE
		// CATEGORY AXIS

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

		// Y-AXIS (OVERLAY-1) - NEED TO BE CREATED MANUALLY
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

	/**
	 * 
	 * @param sPluginRelativePath
	 * @return
	 */
	public static final String getURL(String sPluginRelativePath) {

		return "";
	}

}