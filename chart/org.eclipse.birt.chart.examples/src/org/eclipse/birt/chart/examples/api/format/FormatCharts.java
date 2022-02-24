/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.api.format;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;

public class FormatCharts {
	protected static final Chart createAxisFormatChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		p.getOutline().setVisible(true);

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Bar Chart with Formatted Axes");//$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setCategoryAxis(true);
		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().getCaption().setValue("Regional Markets"); //$NON-NLS-1$
		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);
		xAxisPrimary.setFormatSpecifier(JavaDateFormatSpecifierImpl.create("MM/dd/yyyy"));//$NON-NLS-1$

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getTitle().getCaption().setValue("Net Profit");//$NON-NLS-1$
		yAxisPrimary.setFormatSpecifier(JavaNumberFormatSpecifierImpl.create("$###,###"));//$NON-NLS-1$

		// Data Set
		DateTimeDataSet categoryValues = DateTimeDataSetImpl.create(
				new Calendar[] { new CDateTime(2001, 5, 1), new CDateTime(2001, 4, 11), new CDateTime(2001, 8, 23) });
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 16170, 24210, -4300 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(-2);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setSeriesIdentifier("Sales");//$NON-NLS-1$
		bs.setDataSet(orthoValues);
		bs.setRiserOutline(null);
		bs.getLabel().setVisible(true);
		bs.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createColoredByCategoryChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(false);
		cwaBar.getTitle().getLabel().getCaption().setValue("Bar Chart Colored by Category");//$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().getCaption().setValue("Products");//$NON-NLS-1$
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);
		yAxisPrimary.getTitle().getCaption().setValue("Sales");//$NON-NLS-1$

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 14.3, 20.9, -7.6 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.setRiserOutline(null);
		bs.getLabel().setVisible(true);
		bs.setLabelPosition(Position.INSIDE_LITERAL);
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createLegendTitleChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);
		Plot p = cwaBar.getPlot();
		p.getOutline().setVisible(false);

		// Title
		TitleBlock tb = cwaBar.getTitle();
		tb.setBackground(GradientImpl.create(ColorDefinitionImpl.create(200, 200, 244),
				ColorDefinitionImpl.create(250, 122, 253), 0, false));

		tb.getOutline().setStyle(LineStyle.DASHED_LITERAL);
		tb.getOutline().setColor(ColorDefinitionImpl.create(0, 100, 245));
		tb.getOutline().setThickness(1);
		tb.getOutline().setVisible(true);

		tb.getLabel().getCaption().setValue("Formatted Legend and Title"); //$NON-NLS-1$
		tb.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN());
		tb.getLabel().setShadowColor(ColorDefinitionImpl.YELLOW());
		tb.getLabel().getOutline().setVisible(true);

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);

		lg.getOutline().setStyle(LineStyle.DASH_DOTTED_LITERAL);
		lg.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		lg.getOutline().setVisible(true);

		lg.setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		lg.setAnchor(Anchor.NORTH_LITERAL);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		lg.getClientArea().setBackground(ColorDefinitionImpl.ORANGE());
		lg.setPosition(Position.LEFT_LITERAL);
		lg.setOrientation(Orientation.HORIZONTAL_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().getCaption().setValue("Products");//$NON-NLS-1$
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);
		yAxisPrimary.getTitle().getCaption().setValue("Sales");//$NON-NLS-1$

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 14.3, 20.9, -7.6 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(-4);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.setRiserOutline(null);
		bs.getLabel().setVisible(true);
		bs.setLabelPosition(Position.INSIDE_LITERAL);
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createPercentageValueChart() {
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
		cwoaPie.getTitle().getLabel().getCaption().setValue("Pie Chart with Percentage Values");//$NON-NLS-1$
		cwoaPie.getTitle().getOutline().setVisible(true);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "New York", "Boston", "Chicago", "San Francisco", "Dallas" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$

		NumberDataSet seriesValues = NumberDataSetImpl.create(new double[] { 54.65, 21, 75.95, 91.28, 37.43 });

		// Base Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().shift(0);
		sd.getSeries().add(seCategory);

		// Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesValues);
		sePie.setSeriesIdentifier("Cities");//$NON-NLS-1$

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sdCity.getQuery().setDefinition("Census.City");//$NON-NLS-1$
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie);

		DataPointComponent dpc = DataPointComponentImpl.create(
				DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("##.##%")); //$NON-NLS-1$
		sePie.getDataPoint().getComponents().clear();
		sePie.getDataPoint().getComponents().add(dpc);

		return cwoaPie;
	}

	protected static final Chart createPlotFormatChart() {
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create();

		// Plot
		Plot p = cwaScatter.getPlot();

		p.getOutline().setStyle(LineStyle.DASH_DOTTED_LITERAL);
		p.getOutline().setColor(ColorDefinitionImpl.create(214, 100, 12));
		p.getOutline().setVisible(true);

		p.setBackground(ColorDefinitionImpl.CREAM());
		p.setAnchor(Anchor.NORTH_LITERAL);

		p.getClientArea().setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 0, 255),
				ColorDefinitionImpl.create(255, 253, 200), -35, false));
		p.getClientArea().getOutline().setVisible(true);

		// Title
		cwaScatter.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaScatter.getTitle().getLabel().getCaption().setValue("Numeric Scatter Chart");//$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryBaseAxes()[0];

		xAxisPrimary.getTitle().getCaption().setValue("X Axis");//$NON-NLS-1$
		xAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaScatter).getPrimaryOrthogonalAxis(xAxisPrimary);

		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");//$NON-NLS-1$
		yAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.BLUE());
		yAxisPrimary.getTitle().setVisible(true);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		yAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

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
		ss.setSeriesIdentifier("Unit Price");//$NON-NLS-1$
		for (int i = 0; i < ss.getMarkers().size(); i++) {
			ss.getMarkers().get(i).setType(MarkerType.CIRCLE_LITERAL);
		}
		DataPoint dp = ss.getDataPoint();
		dp.getComponents().clear();
		dp.setPrefix("(");//$NON-NLS-1$
		dp.setSuffix(")");//$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.BASE_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00")));//$NON-NLS-1$
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00")));//$NON-NLS-1$
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

	protected static final Chart createSeriesFormatChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		p.getOutline().setVisible(true);

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Bar Chart with Formatted Series");//$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setCategoryAxis(true);
		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().getCaption().setValue("Regional Markets"); //$NON-NLS-1$
		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getTitle().getCaption().setValue("Net Profit");//$NON-NLS-1$

		// Data Set
		DateTimeDataSet categoryValues = DateTimeDataSetImpl.create(new Calendar[] { new CDateTime(2001, 5, 1),
				new CDateTime(2001, 4, 11), new CDateTime(2001, 8, 23), new CDateTime(2001, 10, 15) });
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 1620, 3630, 4600, -1800 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(-2);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);
		sdX.setFormatSpecifier(JavaDateFormatSpecifierImpl.create("MM/dd/yyyy"));//$NON-NLS-1$

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setSeriesIdentifier("Sales");//$NON-NLS-1$
		bs.setDataSet(orthoValues);
		bs.setRiserOutline(null);
		bs.getLabel().setVisible(true);
		bs.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		DataPointComponent dpc = DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("$###,###.00"));//$NON-NLS-1$
		bs.getDataPoint().getComponents().clear();
		bs.getDataPoint().getComponents().add(dpc);

		return cwaBar;
	}
}
