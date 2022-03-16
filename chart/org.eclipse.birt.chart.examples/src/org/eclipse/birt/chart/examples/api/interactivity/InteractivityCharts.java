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

package org.eclipse.birt.chart.examples.api.interactivity;

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.impl.CallBackValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.EmbeddedImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.MultiURLValuesImpl;
import org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.URLValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class InteractivityCharts {
	protected static final Chart createHSChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getInteractivity().setLegendBehavior(LegendBehaviorType.HIGHLIGHT_SERIE_LITERAL);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		cwaBar.getTitle().getLabel().getCaption().setValue("Click \"Items\" to Highlight Seires"); //$NON-NLS-1$
		cwaBar.setUnitSpacing(20);

		Legend lg = cwaBar.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(false);
		lg.setAnchor(Anchor.NORTH_LITERAL);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 25, 35, 15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setStacked(true);
		bs.setDataSet(orthoValues);
		bs.setRiser(RiserType.TUBE_LITERAL);
		bs.setSeriesIdentifier("Highlight"); //$NON-NLS-1$
		bs.getLabel().setVisible(true);
		bs.setLabelPosition(Position.INSIDE_LITERAL);
		bs.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL, ActionImpl.create(
				ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create(String.valueOf(bs.getSeriesIdentifier())))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		Series bs2 = bs.copyInstance();
		bs2.setDataSet(NumberDataSetImpl.create(new double[] { 35, 30, 10 }));
		sdY.getSeries().add(bs2);

		Series bs3 = bs.copyInstance();
		bs3.setDataSet(NumberDataSetImpl.create(new double[] { 20, 10, 30 }));
		sdY.getSeries().add(bs3);

		return cwaBar;
	}

	protected static final Chart createSVGHSChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getInteractivity().setLegendBehavior(LegendBehaviorType.HIGHLIGHT_SERIE_LITERAL);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		cwaBar.getTitle().getLabel().getCaption().setValue("Click \"Items\" to Highlight Seires"); //$NON-NLS-1$
		cwaBar.setUnitSpacing(20);

		Legend lg = cwaBar.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(false);
		lg.setAnchor(Anchor.NORTH_LITERAL);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 25, 35, 15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setStacked(true);
		bs.setDataSet(orthoValues);
//		bs.setRiser( RiserType.TUBE_LITERAL );
		bs.setSeriesIdentifier("Highlight"); //$NON-NLS-1$
		bs.getLabel().setVisible(true);
		bs.setLabelPosition(Position.INSIDE_LITERAL);
		bs.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL, ActionImpl.create(
				ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create(String.valueOf(bs.getSeriesIdentifier())))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		Series bs2 = bs.copyInstance();
		bs2.setDataSet(NumberDataSetImpl.create(new double[] { 35, 30, 10 }));
		sdY.getSeries().add(bs2);

		Series bs3 = bs.copyInstance();
		bs3.setDataSet(NumberDataSetImpl.create(new double[] { 20, 10, 30 }));
		sdY.getSeries().add(bs3);

		return cwaBar;
	}

	protected static final Chart createSTChart() {
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create();

		// Title
		cwaScatter.getTitle().getLabel().getCaption().setValue("Mouse over the Data Points to Show Tooltips"); //$NON-NLS-1$
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
		sdX.getQuery().setDefinition(""); //$NON-NLS-1$
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create();
		ss.setSeriesIdentifier("Unit Price"); //$NON-NLS-1$
		for (int i = 0; i < ss.getMarkers().size(); i++) {
			ss.getMarkers().get(i).setType(MarkerType.CIRCLE_LITERAL);
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
		ss.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL,
				ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLACK());
		sdY.getSeries().add(ss);

		return cwaScatter;
	}

	protected static final Chart createTVChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaLine.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		cwaLine.getTitle().getLabel().getCaption().setValue("Click \"Line Series\" to Toggle Visibility"); //$NON-NLS-1$
		cwaLine.setUnitSpacing(20);

		Legend lg = cwaLine.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(false);
		lg.setAnchor(Anchor.NORTH_LITERAL);
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONMOUSEDOWN_LITERAL,
				ActionImpl.create(ActionType.TOGGLE_VISIBILITY_LITERAL, SeriesValueImpl.create("not-used")))); //$NON-NLS-1$

		Axis xAxisPrimary = cwaLine.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(false);

		Axis yAxisPrimary = cwaLine.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getTitle().setVisible(false);

		Vector<String> vs = new Vector<>();
		vs.add("one"); //$NON-NLS-1$
		vs.add("two"); //$NON-NLS-1$
		vs.add("three"); //$NON-NLS-1$

		ArrayList<Double> vn1 = new ArrayList<>();
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
		ls.setStacked(true);
		ls.setSeriesIdentifier("Line Series"); //$NON-NLS-1$
		ls.setDataSet(orthoValues1);
		ls.getLineAttributes().setColor(ColorDefinitionImpl.CREAM());

		for (int i = 0; i < ls.getMarkers().size(); i++) {
			ls.getMarkers().get(i).setType(MarkerType.FOUR_DIAMONDS_LITERAL);
			ls.getMarkers().get(i).setSize(8);
		}

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0); // SET THE COLOR IN THE PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1); // SET THE COLOR IN THE PALETTE
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		sdX.getSeries().add(seCategory);
		sdY.getSeries().add(ls);

		Series ls2 = EcoreUtil.copy(ls);
		ls2.setDataSet(NumberDataSetImpl.create(vn1));
		sdY.getSeries().add(ls2);

		Series ls3 = EcoreUtil.copy(ls);
		ls3.setDataSet(NumberDataSetImpl.create(vn1));
		sdY.getSeries().add(ls3);

		return cwaLine;
	}

	protected static final Chart createURChart() {
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
		cwoaPie.getTitle().getLabel().getCaption().setValue("Click Pie slice to Redirect URL"); //$NON-NLS-1$
		cwoaPie.getTitle().getOutline().setVisible(true);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "New York", "Boston", "Chicago", "San Francisco", "Dallas" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				});
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] { 54.65, 21, 75.95, 91.28, 37.43 });

		// Base Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().shift(0);
		sd.getSeries().add(seCategory);

		// Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesOneValues);
		sePie.setSeriesIdentifier("Cities"); //$NON-NLS-1$

		Trigger triger = TriggerImpl.create(TriggerCondition.ONMOUSEDOWN_LITERAL,
				ActionImpl.create(ActionType.URL_REDIRECT_LITERAL, URLValueImpl.create("http://www.actuate.com", //$NON-NLS-1$
						null, null, null, null)));
		sePie.getTriggers().add(triger);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sdCity.getQuery().setDefinition("Census.City"); //$NON-NLS-1$
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie);

		return cwoaPie;
	}

	protected static final Chart createCBChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getTitle().getLabel().getCaption().setValue("Click \"Items\" to Call Back"); //$NON-NLS-1$
		cwaBar.setUnitSpacing(20);

		Legend lg = cwaBar.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(false);
		lg.setAnchor(Anchor.NORTH_LITERAL);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 25, 35, 15 });

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
		bs.setSeriesIdentifier("Callback"); //$NON-NLS-1$
		bs.getLabel().setVisible(true);
		bs.setLabelPosition(Position.INSIDE_LITERAL);
		bs.getTriggers().add(TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL, ActionImpl.create(
				ActionType.CALL_BACK_LITERAL, CallBackValueImpl.create(String.valueOf(bs.getSeriesIdentifier())))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createRCChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getInteractivity().setLegendBehavior(LegendBehaviorType.HIGHLIGHT_SERIE_LITERAL);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		cwaBar.getTitle().getLabel().getCaption().setValue("Right Click \"Items\" to Highlight Seires"); //$NON-NLS-1$
		cwaBar.setUnitSpacing(20);

		Legend lg = cwaBar.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(false);
		lg.setAnchor(Anchor.NORTH_LITERAL);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 25, 35, 15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setStacked(true);
		bs.setDataSet(orthoValues);
		bs.setRiser(RiserType.TUBE_LITERAL);
		bs.setSeriesIdentifier("Highlight"); //$NON-NLS-1$
		bs.getLabel().setVisible(true);
		bs.setLabelPosition(Position.INSIDE_LITERAL);
		bs.getTriggers().add(TriggerImpl.create(TriggerCondition.ONRIGHTCLICK_LITERAL, ActionImpl.create(
				ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create(String.valueOf(bs.getSeriesIdentifier())))));
		lg.getTriggers().add(TriggerImpl.create(TriggerCondition.ONRIGHTCLICK_LITERAL, ActionImpl.create(
				ActionType.HIGHLIGHT_LITERAL, SeriesValueImpl.create(String.valueOf(bs.getSeriesIdentifier())))));

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		Series bs2 = EcoreUtil.copy(bs);
		bs2.setDataSet(NumberDataSetImpl.create(new double[] { 35, 30, 10 }));
		sdY.getSeries().add(bs2);

		Series bs3 = EcoreUtil.copy(bs);
		bs3.setDataSet(NumberDataSetImpl.create(new double[] { 20, 10, 30 }));
		sdY.getSeries().add(bs3);

		return cwaBar;
	}

	/**
	 * Creates a simple bar chart model and set cursors.
	 *
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createBarChartWithCursorExample() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(false);

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Bar Chart"); //$NON-NLS-1$

		// Add triggers and cursor to title.
		Trigger t = DataFactory.eINSTANCE.createTrigger();
		t.setCondition(TriggerCondition.ONMOUSEOVER_LITERAL);
		t.setAction(DataFactory.eINSTANCE.createAction());
		t.getAction().setType(ActionType.SHOW_TOOLTIP_LITERAL);
		t.getAction().setValue(AttributeFactory.eINSTANCE.createTooltipValue());
		((TooltipValue) t.getAction().getValue()).setText("Chart Title, Cursor: Move.");//$NON-NLS-1$
		cwaBar.getTitle().getTriggers().add(t);
		Cursor c = AttributeFactory.eINSTANCE.createCursor();
		c.setType(CursorType.MOVE);
		cwaBar.getTitle().setCursor(c);

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// Add triggers and cursor to Legend.
		t = DataFactory.eINSTANCE.createTrigger();
		t.setCondition(TriggerCondition.ONMOUSEOVER_LITERAL);
		t.setAction(DataFactory.eINSTANCE.createAction());
		t.getAction().setType(ActionType.SHOW_TOOLTIP_LITERAL);
		t.getAction().setValue(AttributeFactory.eINSTANCE.createTooltipValue());
		((TooltipValue) t.getAction().getValue()).setText("Chart Legend, Cursor: Crosshair.");//$NON-NLS-1$
		lg.getTriggers().add(t);
		c = AttributeFactory.eINSTANCE.createCursor();
		c.setType(CursorType.CROSSHAIR);
		lg.setCursor(c);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Add triggers and cursor to X axis.
		t = DataFactory.eINSTANCE.createTrigger();
		t.setCondition(TriggerCondition.ONMOUSEOVER_LITERAL);
		t.setAction(DataFactory.eINSTANCE.createAction());
		t.getAction().setType(ActionType.SHOW_TOOLTIP_LITERAL);
		t.getAction().setValue(AttributeFactory.eINSTANCE.createTooltipValue());
		((TooltipValue) t.getAction().getValue()).setText("X axis, Cursor: Wait.");//$NON-NLS-1$
		xAxisPrimary.getTriggers().add(t);
		c = AttributeFactory.eINSTANCE.createCursor();
		c.setType(CursorType.WAIT);
		xAxisPrimary.setCursor(c);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);

		// Add triggers and cursor to Y axis.
		t = DataFactory.eINSTANCE.createTrigger();
		t.setCondition(TriggerCondition.ONMOUSEOVER_LITERAL);
		t.setAction(DataFactory.eINSTANCE.createAction());
		t.getAction().setType(ActionType.SHOW_TOOLTIP_LITERAL);
		t.getAction().setValue(AttributeFactory.eINSTANCE.createTooltipValue());
		((TooltipValue) t.getAction().getValue()).setText("Y axis, Cursor: Text.");//$NON-NLS-1$
		yAxisPrimary.getTriggers().add(t);
		c = AttributeFactory.eINSTANCE.createCursor();
		c.setType(CursorType.TEXT);
		yAxisPrimary.setCursor(c);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 25, 35, 15 });

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

		// Add trigger and cursor to value series.
		t = DataFactory.eINSTANCE.createTrigger();
		t.setCondition(TriggerCondition.ONMOUSEOVER_LITERAL);
		t.setAction(DataFactory.eINSTANCE.createAction());
		t.getAction().setType(ActionType.SHOW_TOOLTIP_LITERAL);
		t.getAction().setValue(AttributeFactory.eINSTANCE.createTooltipValue());
		((TooltipValue) t.getAction().getValue()).setText("Y Series Values, Custom cursor, embedded image cursor.");//$NON-NLS-1$
		bs.getTriggers().add(t);
		c = AttributeFactory.eINSTANCE.createCursor();
		c.setType(CursorType.CUSTOM);
		c.getImage().add(EmbeddedImageImpl.create("Crosshair.gif", //$NON-NLS-1$
				"R0lGODlhCAAIALMAAGbNM2bMM2XJM2XGM2TDMmTBM2K2MmKxM2CtM16gMl6cMl2XMl2WMl2VMv///wAAACH5BAEAAA4ALAAAAAAIAAgAAAQc0MkQpHWhXEmQDcBwKM2yAIKRMOZiLczmsLJpRQA7"));//$NON-NLS-1$
		bs.setCursor(c);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	/**
	 * The method returns a chart model with multiple URL settings.
	 *
	 * @return chart model
	 */
	public static Chart createMultiURChart() {
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
		cwoaPie.getTitle().getLabel().getCaption().setValue("Click Pie slice to Redirect URL"); //$NON-NLS-1$
		cwoaPie.getTitle().getOutline().setVisible(true);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "New York", "Boston", "Chicago", "San Francisco", "Dallas" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				});
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] { 54.65, 21, 75.95, 91.28, 37.43 });

		// Base Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().shift(0);
		sd.getSeries().add(seCategory);

		// Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesOneValues);
		sePie.setSeriesIdentifier("Cities"); //$NON-NLS-1$

		MultiURLValues muv = MultiURLValuesImpl.create();

		URLValue uv = URLValueImpl.create("http://www.actuate.com", //$NON-NLS-1$
				null, null, null, null);
		Label l = LabelImpl.create();
		l.setCaption(TextImpl.create("www.actuate.com"));//$NON-NLS-1$
		uv.setLabel(l);
		muv.getURLValues().add(uv);

		uv = URLValueImpl.create("http://www.birt-exchange.com", //$NON-NLS-1$
				null, null, null, null);
		l = LabelImpl.create();
		l.setCaption(TextImpl.create("www.birt-exchange.com"));//$NON-NLS-1$
		uv.setLabel(l);
		muv.getURLValues().add(uv);

		uv = URLValueImpl.create("http://www.eclipse.org", //$NON-NLS-1$
				null, null, null, null);
		l = LabelImpl.create();
		l.setCaption(TextImpl.create("www.eclipse.org"));//$NON-NLS-1$
		uv.setLabel(l);
		muv.getURLValues().add(uv);

		Trigger triger = TriggerImpl.create(TriggerCondition.ONMOUSEDOWN_LITERAL,
				ActionImpl.create(ActionType.URL_REDIRECT_LITERAL, muv));
		sePie.getTriggers().add(triger);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sdCity.getQuery().setDefinition("Census.City"); //$NON-NLS-1$
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie);

		return cwoaPie;
	}
}
