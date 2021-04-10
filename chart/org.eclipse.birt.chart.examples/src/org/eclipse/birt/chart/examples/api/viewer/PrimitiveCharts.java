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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.eclipse.birt.chart.extension.datafeed.BubbleEntry;
import org.eclipse.birt.chart.extension.datafeed.DifferenceEntry;
import org.eclipse.birt.chart.extension.datafeed.StockEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointImpl;
import org.eclipse.birt.chart.model.attribute.impl.EmbeddedImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.MultipleFillImpl;
import org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.component.impl.DialRegionImpl;
import org.eclipse.birt.chart.model.component.impl.MarkerLineImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BubbleDataSet;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.DifferenceDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.BubbleDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.DifferenceDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.StockDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;

import com.ibm.icu.util.Calendar;

/**
 * The class cannot be run individually. It provides sample model
 * implementations for viewer selector classes in the package.
 * 
 */

public final class PrimitiveCharts {
	/**
	 * Returns the names of available chart models for display
	 */
	public static final String[] getAvailableModelList() {
		return new String[] { "Bar Chart", //$NON-NLS-1$
				"Bar Chart(2 Series)", //$NON-NLS-1$
				"Pie Chart", //$NON-NLS-1$
				"Pie Chart(4 Series)", //$NON-NLS-1$
				"Line Chart", //$NON-NLS-1$
				"Bar/Line Stacked Chart", //$NON-NLS-1$
				"Scatter Chart", //$NON-NLS-1$
				"Stock Chart", //$NON-NLS-1$
				"Area Chart", //$NON-NLS-1$
				"Difference Chart", //$NON-NLS-1$
				"Bubble Chart", //$NON-NLS-1$
				// "Open Chart File" //$NON-NLS-1$
				"Cursor Example" //$NON-NLS-1$
		};
	}

	/**
	 * Creates chart model according to the selection index of available list.
	 * 
	 * @param index selection index
	 * @see #getAvailableModelList()
	 */
	public static final Chart createChart(int index) {
		Chart cm = null;
		switch (index) {
		case 0:
			cm = PrimitiveCharts.createBarChart();
			break;
		case 1:
			cm = PrimitiveCharts.createMultiBarChart();
			break;
		case 2:
			cm = PrimitiveCharts.createPieChart();
			break;
		case 3:
			cm = PrimitiveCharts.createMultiPieChart();
			break;
		case 4:
			cm = PrimitiveCharts.createLineChart();
			break;
		case 5:
			cm = PrimitiveCharts.createStackedChart();
			break;
		case 6:
			cm = PrimitiveCharts.createScatterChart();
			break;
		case 7:
			cm = PrimitiveCharts.createStockChart();
			break;
		case 8:
			cm = PrimitiveCharts.createAreaChart();
			break;
		case 9:
			cm = PrimitiveCharts.createDifferenceChart();
			break;
		case 10:
			cm = PrimitiveCharts.createBubbleChart();
			break;
		// case 9 :
		// cm = PrimitiveCharts.openChart( );
		// break;
		case 11:
			cm = PrimitiveCharts.createBarChartWithCursorExample();
			break;
		}
		return cm;
	}

	/**
	 * Creates a simple bar chart model
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createBarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(false);

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Bar Chart"); //$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
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
		bs.getLabel().setVisible(true);
		bs.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

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
	 * Creates a bar chart model with mutiple Y-series as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createMultiBarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		p.getOutline().setVisible(true);

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("2-Series Bar Chart");//$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.getTitle().setVisible(true);
		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] { 25, 35, 15 });
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] { 17, 63.55, 27.29 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("Series 1");//$NON-NLS-1$
		bs1.setDataSet(orthoValues1);
		bs1.setRiserOutline(null);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Series 2");//$NON-NLS-1$
		bs2.setDataSet(orthoValues2);
		bs2.setRiserOutline(null);
		bs2.getLabel().setVisible(true);
		bs2.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(bs2);

		return cwaBar;
	}

	/**
	 * Creates a line chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createLineChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();

		// Plot
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaLine.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwaLine.getTitle().getLabel().getCaption().setValue("Line Chart");//$NON-NLS-1$

		// Legend
		cwaLine.getLegend().setVisible(false);

		// X-Axis
		Axis xAxisPrimary = cwaLine.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaLine.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 25, 35, 15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();

		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Sereis
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setDataSet(orthoValues);
		ls.getLineAttributes().setColor(ColorDefinitionImpl.CREAM());
		for (int i = 0; i < ls.getMarkers().size(); i++) {
			ls.getMarkers().get(i).setType(MarkerType.TRIANGLE_LITERAL);
		}
		ls.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-2);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ls);

		return cwaLine;
	}

	/**
	 * Creates a difference chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createDifferenceChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();

		// Plot
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaLine.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwaLine.getTitle().getLabel().getCaption().setValue("Difference Chart");//$NON-NLS-1$

		// Legend
		cwaLine.getLegend().setVisible(false);

		// X-Axis
		Axis xAxisPrimary = cwaLine.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaLine.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		DifferenceDataSet orthoValues = DifferenceDataSetImpl.create(new DifferenceEntry[] {
//				new DifferenceEntry( 30, 50 ),
				new DifferenceEntry(50, 60), new DifferenceEntry(70, 70), new DifferenceEntry(15, 30),
				new DifferenceEntry(65, 20) });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();

		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Sereis
		DifferenceSeries ls = (DifferenceSeries) DifferenceSeriesImpl.create();
		ls.setDataSet(orthoValues);
		ls.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		for (int i = 0; i < ls.getMarkers().size(); i++) {
			ls.getMarkers().get(i).setType(MarkerType.TRIANGLE_LITERAL);
		}
		ls.getLabel().setVisible(true);
		ls.setCurve(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-2);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		MultipleFill fill = MultipleFillImpl.create();
		fill.getFills().add(ColorDefinitionImpl.CYAN());
		fill.getFills().add(ColorDefinitionImpl.RED());
		sdY.getSeriesPalette().getEntries().add(0, fill);
		sdY.getSeries().add(ls);

		return cwaLine;
	}

	/**
	 * Creates a pie chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createPieChart() {
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
		cwoaPie.getTitle().getLabel().getCaption().setValue("Pie Chart");//$NON-NLS-1$
		cwoaPie.getTitle().getOutline().setVisible(true);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "New York", "Boston", "Chicago", "San Francisco", "Dallas" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
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
		sePie.setSeriesIdentifier("Cities");//$NON-NLS-1$

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sdCity.getQuery().setDefinition("Census.City");//$NON-NLS-1$
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie);

		return cwoaPie;
	}

	/**
	 * Creates a chart model containing multiple pies (grouped by categories) as a
	 * reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createMultiPieChart() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();

		// Plot
		cwoaPie.setSeriesThickness(25);
		cwoaPie.setGridColumnCount(2);
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
		cwoaPie.getTitle().getLabel().getCaption().setValue("Multiple Series Pie Chart");//$NON-NLS-1$
		cwoaPie.getTitle().getOutline().setVisible(true);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "Boston", "New York", "Chicago", "San Francisco", "Seattle" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] { 54.65, 21, 75.95, 91.28, 37.43 });
		NumberDataSet seriesTwoValues = NumberDataSetImpl.create(new double[] { 15.65, 65, 25.95, 14.28, 37.43 });
		NumberDataSet seriesThreeValues = NumberDataSetImpl.create(new double[] { 25.65, 85, 45.95, 64.28, 6.43 });
		NumberDataSet seriesFourValues = NumberDataSetImpl.create(new double[] { 25.65, 55, 5.95, 14.28, 86.43 });

		// Base Sereis
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().shift(-1);
		sd.getSeries().add(seCategory);

		// Orthogonal Series
		PieSeries sePie1 = (PieSeries) PieSeriesImpl.create();
		sePie1.setDataSet(seriesOneValues);
		sePie1.setSeriesIdentifier("2000");//$NON-NLS-1$
		sePie1.getLabel().getCaption().getFont().setRotation(25);
		sePie1.getTitle().getCaption().getFont().setRotation(8);
		sePie1.setTitlePosition(Position.ABOVE_LITERAL);
		sePie1.getTitle().getInsets().set(8, 10, 0, 5);

		PieSeries sePie2 = (PieSeries) PieSeriesImpl.create();
		sePie2.setDataSet(seriesTwoValues);
		sePie2.setSeriesIdentifier("2001");//$NON-NLS-1$
		sePie2.getLabel().getCaption().getFont().setRotation(-65);
		sePie2.getTitle().getCaption().getFont().setRotation(28);
		sePie2.getLabel().setBackground(ColorDefinitionImpl.YELLOW());
		sePie2.getLabel().setShadowColor(ColorDefinitionImpl.GREY());
		sePie2.setTitlePosition(Position.RIGHT_LITERAL);

		PieSeries sePie3 = (PieSeries) PieSeriesImpl.create();
		sePie3.setDataSet(seriesThreeValues);
		sePie3.setSeriesIdentifier("2002");//$NON-NLS-1$
		sePie3.getTitle().getCaption().getFont().setRotation(75);
		sePie3.setTitlePosition(Position.LEFT_LITERAL);

		PieSeries sePie4 = (PieSeries) PieSeriesImpl.create();
		sePie4.setDataSet(seriesFourValues);
		sePie4.setSeriesIdentifier("2003");//$NON-NLS-1$
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
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createStackedChart() {
		ChartWithAxes cwaCombination = ChartWithAxesImpl.create();

		// Plot
		cwaCombination.setUnitSpacing(25);
		cwaCombination.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaCombination.getPlot();
		p.getClientArea().setBackground(GradientImpl.create(ColorDefinitionImpl.create(255, 235, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));

		p.getClientArea().getInsets().set(8, 8, 8, 8);
		p.getOutline().setVisible(true);

		// Legend
		Legend lg = cwaCombination.getLegend();
		lg.setBackground(ColorDefinitionImpl.YELLOW());
		lg.getOutline().setVisible(true);

		// Title
		cwaCombination.getTitle().getLabel().getCaption().setValue("Project Sales");//$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = cwaCombination.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);

		xAxisPrimary.getLabel().setBackground(ColorDefinitionImpl.create(255, 255, 235));
		xAxisPrimary.getLabel().setShadowColor(ColorDefinitionImpl.create(225, 225, 225));
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(25);

		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(64, 64, 64));
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		xAxisPrimary.getTitle().getCaption().setValue("Computer Components");//$NON-NLS-1$
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		xAxisPrimary.getMinorGrid().getLineAttributes().setColor(ColorDefinitionImpl.CYAN());
		xAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);

		// Y-Series
		Axis yAxisPrimary = cwaCombination.getPrimaryOrthogonalAxis(xAxisPrimary);

		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);
		yAxisPrimary.getTitle().getCaption().setValue("Actual Sales ($Millions)");//$NON-NLS-1$

		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(37);

		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.RED());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMinorGrid().setTickStyle(TickStyle.ACROSS_LITERAL);
		yAxisPrimary.getMinorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMinorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREEN());

		// Data Set
		String[] saTextValues = { "CPUs", "Keyboards", "Video Cards", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"Monitors", "Motherboards", "Memory", "Storage Devices", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				"Media", "Printers", "Scanners" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		TextDataSet categoryValues = TextDataSetImpl.create(saTextValues);
		NumberDataSet seriesOneValues = NumberDataSetImpl
				.create(new double[] { 56.99, 352.95, 201.95, 299.95, 95.95, 25.45, 129.33, 26.5, 43.5, 122 });
		NumberDataSet seriesTwoValues = NumberDataSetImpl
				.create(new double[] { 20, 35, 59, 105, 150, 37, 65, 99, 145, 185 });
		NumberDataSet seriesThreeValues = NumberDataSetImpl
				.create(new double[] { 54.99, 21, 75.95, 39.95, 7.95, 91.22, 33.45, 25.63, 40, 13 });
		NumberDataSet seriesFourValues = NumberDataSetImpl
				.create(new double[] { 15, 45, 43, 5, 19, 25, 35, 94, 15, 55 });
		NumberDataSet seriesFiveValues = NumberDataSetImpl
				.create(new double[] { 43, 65, 35, 41, 45, 55, 29, 15, 85, 65 });
		NumberDataSet seriesSixValues = NumberDataSetImpl
				.create(new double[] { 15, 45, 43, 5, 19, 25, 35, 94, 15, 55 });
		NumberDataSet seriesSevenValues = NumberDataSetImpl
				.create(new double[] { 43, 65, 35, 41, 45, 55, 29, 15, 85, 65 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("North America");//$NON-NLS-1$
		bs1.setDataSet(seriesOneValues);
		bs1.setRiserOutline(null);
		bs1.setRiser(RiserType.RECTANGLE_LITERAL);
		bs1.setStacked(true);
		DataPoint dp = DataPointImpl.create("(", ")", ", ");//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		dp.getComponents().clear();
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.BASE_VALUE_LITERAL, null));
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("0.00")));//$NON-NLS-1$
		bs1.setDataPoint(dp);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("South America");//$NON-NLS-1$
		bs2.setDataSet(seriesThreeValues);
		bs2.setRiserOutline(null);
		bs2.setRiser(RiserType.RECTANGLE_LITERAL);
		bs2.setStacked(true);
		dp = DataPointImpl.create("[", "]", ", ");//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		bs2.setDataPoint(dp);

		BarSeries bs3 = (BarSeries) BarSeriesImpl.create();
		bs3.setSeriesIdentifier("Eastern Europe");//$NON-NLS-1$
		bs3.setDataSet(seriesFourValues);
		bs3.setRiserOutline(null);
		bs3.setRiser(RiserType.RECTANGLE_LITERAL);
		bs3.setStacked(true);

		BarSeries bs4 = (BarSeries) BarSeriesImpl.create();
		bs4.setSeriesIdentifier("Western Europe");//$NON-NLS-1$
		bs4.setDataSet(seriesFiveValues);
		bs4.setRiserOutline(null);
		bs4.setRiser(RiserType.RECTANGLE_LITERAL);
		bs4.setStacked(true);

		BarSeries bs5 = (BarSeries) BarSeriesImpl.create();
		bs5.setSeriesIdentifier("Asia");//$NON-NLS-1$
		bs5.setDataSet(seriesSixValues);
		bs5.setRiserOutline(null);
		bs5.setRiser(RiserType.RECTANGLE_LITERAL);

		BarSeries bs6 = (BarSeries) BarSeriesImpl.create();
		bs6.setSeriesIdentifier("Australia");//$NON-NLS-1$
		bs6.setDataSet(seriesSevenValues);
		bs6.setRiserOutline(null);
		bs6.setRiser(RiserType.RECTANGLE_LITERAL);

		LineSeries ls1 = (LineSeries) LineSeriesImpl.create();
		ls1.setSeriesIdentifier("Expected Growth");//$NON-NLS-1$
		ls1.setDataSet(seriesTwoValues);
		for (int i = 0; i < ls1.getMarkers().size(); i++) {
			ls1.getMarkers().get(i).setType(MarkerType.BOX_LITERAL);
		}
		ls1.getLabel().setVisible(true);

		SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		sdY1.getSeriesPalette().shift(0);
		yAxisPrimary.getSeriesDefinitions().add(sdY1);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		sdY2.getSeriesPalette().shift(-1);
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
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createScatterChart() {
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create();

		// Plot
		cwaScatter.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaScatter.getPlot().getClientArea().getOutline().setVisible(false);
		cwaScatter.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Title
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

	/**
	 * Creates a numeric bubble chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createBubbleChart() {
		ChartWithAxes cwa = ChartWithAxesImpl.create();

		// Plot
		cwa.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwa.getPlot().getClientArea().getOutline().setVisible(false);
		cwa.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwa.getTitle().getLabel().getCaption().setValue("Bubble Chart");//$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = cwa.getPrimaryBaseAxes()[0];

		xAxisPrimary.getTitle().getCaption().setValue("X Axis");//$NON-NLS-1$
		xAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());
		xAxisPrimary.getTitle().setVisible(false);

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwa.getPrimaryOrthogonalAxis(xAxisPrimary);

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
		NumberDataSet dsNumericValues1 = NumberDataSetImpl.create(new double[] { -10, 20, 80, 90 });
		BubbleDataSet dsNumericValues2 = BubbleDataSetImpl
				.create(new BubbleEntry[] { new BubbleEntry(Integer.valueOf(20), Integer.valueOf(10)),
						new BubbleEntry(Integer.valueOf(30), Integer.valueOf(-10)), new BubbleEntry(null, null),
						new BubbleEntry(Integer.valueOf(-20), Integer.valueOf(30)) });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsNumericValues1);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		BubbleSeries ss = (BubbleSeries) BubbleSeriesImpl.create();
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
		dp.getComponents().add(DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL, null));
		ss.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		ss.getLabel().setBackground(ColorDefinitionImpl.TRANSPARENT());
		ss.getLabel().setVisible(true);
		ss.setDataSet(dsNumericValues2);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(ColorDefinitionImpl.BLACK());
		sdY.getSeries().add(ss);

		return cwa;
	}

	/**
	 * Creates a stock chart instance
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public final static Chart createStockChart() {
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

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(255, 196, 196));
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		xAxisPrimary.setCategoryAxis(true);

		// Y-Axis (1)
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

		// Y-Axis (2)
		Axis yAxisOverlay = AxisImpl.create(Axis.ORTHOGONAL);

		yAxisOverlay.getLabel().getCaption().setColor(ColorDefinitionImpl.create(0, 128, 0));
		yAxisOverlay.getLabel().getCaption().getFont().setRotation(-25);
		yAxisOverlay.setLabelPosition(Position.RIGHT_LITERAL);

		yAxisOverlay.getTitle().getCaption().setValue("Volume");//$NON-NLS-1$
		yAxisOverlay.getTitle().getCaption().setColor(ColorDefinitionImpl.GREEN().darker());
		yAxisOverlay.getTitle().getCaption().getFont().setRotation(90);
		yAxisOverlay.getTitle().getCaption().getFont().setSize(16);
		yAxisOverlay.getTitle().getCaption().getFont().setBold(true);
		yAxisOverlay.getTitle().setVisible(true);
		yAxisOverlay.setTitlePosition(Position.RIGHT_LITERAL);

		yAxisOverlay.getLineAttributes().setColor(ColorDefinitionImpl.create(0, 128, 0));

		yAxisOverlay.setType(AxisType.LINEAR_LITERAL);
		yAxisOverlay.setOrientation(Orientation.VERTICAL_LITERAL);

		yAxisOverlay.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(64, 196, 64));
		yAxisOverlay.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisOverlay.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisOverlay.getMajorGrid().setTickStyle(TickStyle.RIGHT_LITERAL);

		yAxisOverlay.getOrigin().setType(IntersectionType.MAX_LITERAL);
		yAxisOverlay.getScale().setMax(NumberDataElementImpl.create(180000000));
		yAxisOverlay.getScale().setMin(NumberDataElementImpl.create(20000000));

		xAxisPrimary.getAssociatedAxes().add(yAxisOverlay);

		// Data Set
		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2004, 12, 27), new CDateTime(2004, 12, 23),
						new CDateTime(2004, 12, 22), new CDateTime(2004, 12, 21), new CDateTime(2004, 12, 20),
						new CDateTime(2004, 12, 17), new CDateTime(2004, 12, 16), new CDateTime(2004, 12, 15) });

		StockDataSet dsStockValues = StockDataSetImpl.create(new StockEntry[] {
				new StockEntry(27.01, 26.82, 27.10, 26.85), new StockEntry(26.87, 26.83, 27.15, 27.01),
				new StockEntry(26.84, 26.78, 27.15, 26.97), new StockEntry(27.00, 26.94, 27.17, 27.07),
				new StockEntry(27.01, 26.89, 27.15, 26.95), new StockEntry(27.00, 26.80, 27.32, 26.96),
				new StockEntry(27.15, 27.01, 27.28, 27.16), new StockEntry(27.22, 27.07, 27.40, 27.11), });

		NumberDataSet dsStockVolume = NumberDataSetImpl.create(
				new double[] { 55958500, 65801900, 63651900, 94646096, 85552800, 126184400, 88997504, 106303904 });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(-1);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setRiserOutline(null);
		bs.setDataSet(dsStockVolume);

		StockSeries ss = (StockSeries) StockSeriesImpl.create();
		ss.setSeriesIdentifier("Stock Price");//$NON-NLS-1$
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

	/**
	 * Creates a Area chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createAreaChart() {
		ChartWithAxes cwaArea = ChartWithAxesImpl.create();

		// Plot/Title
		cwaArea.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaArea.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(225, 225, 225));
		cwaArea.getTitle().getLabel().getCaption().setValue("Area Chart");//$NON-NLS-1$
		cwaArea.getTitle().setVisible(true);

		// Legend
		Legend lg = cwaArea.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(false);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaArea.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid()
				.setLineAttributes(LineAttributesImpl.create(ColorDefinitionImpl.BLUE(), LineStyle.SOLID_LITERAL, 1));
		xAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		xAxisPrimary.getTitle().getCaption().setValue("Month");//$NON-NLS-1$
		xAxisPrimary.getTitle().setVisible(true);
		xAxisPrimary.getTitle().getCaption().getFont().setRotation(0);
		xAxisPrimary.getLabel().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaArea.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid()
				.setLineAttributes(LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1));
		yAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setPercent(false);
		yAxisPrimary.getTitle().getCaption().setValue("Net Profit");//$NON-NLS-1$
		yAxisPrimary.getTitle().setVisible(true);
		yAxisPrimary.getTitle().getCaption().getFont().setRotation(90);
		yAxisPrimary.getLabel().setVisible(true);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary, NumberDataElementImpl.create(2));
		yAxisPrimary.getMarkerLines().add(ml);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Jan.", "Feb.", "Mar.", "Apr", "May" }); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] { 14.32, -19.5, 8.38, 0.34, 9.22 });
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] { 4.2, -19.5, 0.0, 9.2, 7.6 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		AreaSeries as1 = (AreaSeries) AreaSeriesImpl.create();
		as1.setSeriesIdentifier("Series 1");//$NON-NLS-1$
		as1.setDataSet(orthoValues1);
		as1.setTranslucent(true);
		as1.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		as1.getLabel().setVisible(true);

		AreaSeries as2 = (AreaSeries) AreaSeriesImpl.create();
		as2.setSeriesIdentifier("Series 2");//$NON-NLS-1$
		as2.setDataSet(orthoValues2);
		as2.setTranslucent(true);
		as2.getLineAttributes().setColor(ColorDefinitionImpl.PINK());
		as2.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(as1);
		sdY.getSeries().add(as2);

		return cwaArea;

	}

	/**
	 * Creates a Single Dial, Multi Regions chart model as a reference
	 * implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createSDialMRegionChart() {
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

		dChart.getTitle().getLabel().getCaption().setValue("City Temperature");//$NON-NLS-1$
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
		lg.getTitle().getCaption().setValue("Weather");//$NON-NLS-1$
		lg.getTitle().setVisible(true);
		lg.setTitlePosition(Position.ABOVE_LITERAL);

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "London", "Madrid", "Rome", "Moscow" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
		NumberDataSet seriesValues = NumberDataSetImpl.create(new double[] { 21.0, 39.0, 30.0, 10.0 });

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sd);
		Series seCategory = SeriesImpl.create();

		final Fill[] fiaBase = { ColorDefinitionImpl.ORANGE(),
				GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
						ColorDefinitionImpl.create(255, 255, 225), -35, false),
				ColorDefinitionImpl.CREAM(), ColorDefinitionImpl.RED(), ColorDefinitionImpl.GREEN(),
				ColorDefinitionImpl.BLUE().brighter(), ColorDefinitionImpl.CYAN().darker(), };
		sd.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaBase.length; i++) {
			sd.getSeriesPalette().getEntries().add(fiaBase[i]);
		}

		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

		// Dial
		DialSeries seDial = (DialSeries) DialSeriesImpl.create();
		seDial.setDataSet(seriesValues);
		seDial.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		NumberFormatSpecifier nfs = NumberFormatSpecifierImpl.create();
		nfs.setSuffix("`C");//$NON-NLS-1$
		nfs.setFractionDigits(0);
		seDial.getDial().setFormatSpecifier(nfs);
		seDial.setSeriesIdentifier("Temperature");//$NON-NLS-1$
		seDial.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial.getDial().setStartAngle(-45);
		seDial.getDial().setStopAngle(225);
		seDial.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial.getDial().getScale().setStep(10);
		seDial.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		DialRegion dregion1 = DialRegionImpl.create();
		dregion1.setFill(ColorDefinitionImpl.GREEN());
		dregion1.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion1.setStartValue(NumberDataElementImpl.create(70));
		dregion1.setEndValue(NumberDataElementImpl.create(90));
		dregion1.setInnerRadius(40);
		dregion1.setOuterRadius(-1);
		seDial.getDial().getDialRegions().add(dregion1);

		DialRegion dregion2 = DialRegionImpl.create();
		dregion2.setFill(ColorDefinitionImpl.YELLOW());
		dregion2.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion2.setStartValue(NumberDataElementImpl.create(40));
		dregion2.setEndValue(NumberDataElementImpl.create(70));
		dregion2.setOuterRadius(70);
		seDial.getDial().getDialRegions().add(dregion2);

		DialRegion dregion3 = DialRegionImpl.create();
		dregion3.setFill(ColorDefinitionImpl.RED());
		dregion3.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion3.setStartValue(NumberDataElementImpl.create(0));
		dregion3.setEndValue(NumberDataElementImpl.create(40));
		dregion3.setInnerRadius(40);
		dregion3.setOuterRadius(90);
		seDial.getDial().getDialRegions().add(dregion3);

		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial);

		return dChart;
	}

	/**
	 * Creates a Multi Dials, Multi Regions chart model as a reference
	 * implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createMDialMRegionChart() {
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

		dChart.getTitle().getLabel().getCaption().setValue("Meter Chart");//$NON-NLS-1$
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

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Moto" });//$NON-NLS-1$

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sd);
		Series seCategory = SeriesImpl.create();

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
		seDial1.setSeriesIdentifier("Temperature");//$NON-NLS-1$
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
		seDial2.setSeriesIdentifier("Wind Speed");//$NON-NLS-1$
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
		seDial3.setSeriesIdentifier("Viscosity");//$NON-NLS-1$
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

	/**
	 * Creates a Single Dial, Single Region chart model as a reference
	 * implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createSDialSRegionChart() {
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

		dChart.getTitle().getLabel().getCaption().setValue("Meter Chart");//$NON-NLS-1$
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

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Speed" });//$NON-NLS-1$

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sd);
		Series seCategory = SeriesImpl.create();

		final Fill[] fiaBase = { ColorDefinitionImpl.ORANGE(),
				GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
						ColorDefinitionImpl.create(255, 255, 225), -35, false),
				ColorDefinitionImpl.CREAM(), ColorDefinitionImpl.RED(), ColorDefinitionImpl.GREEN(),
				ColorDefinitionImpl.BLUE().brighter(), ColorDefinitionImpl.CYAN().darker(), };
		sd.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaBase.length; i++) {
			sd.getSeriesPalette().getEntries().add(fiaBase[i]);
		}

		seCategory.setDataSet(categoryValues);
		sd.getSeries().add(seCategory);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

		// Dial
		DialSeries seDial = (DialSeries) DialSeriesImpl.create();
		seDial.setDataSet(NumberDataSetImpl.create(new double[] { 60 }));
		seDial.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		seDial.getNeedle().setDecorator(LineDecorator.ARROW_LITERAL);
		seDial.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.RED());
		seDial.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial.getDial().getScale().setMax(NumberDataElementImpl.create(180));
		seDial.getDial().getScale().setStep(30);
		seDial.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		DialRegion dregion21 = DialRegionImpl.create();
		dregion21.setFill(ColorDefinitionImpl.GREEN());
		dregion21.setStartValue(NumberDataElementImpl.create(0));
		dregion21.setEndValue(NumberDataElementImpl.create(80));
		seDial.getDial().getDialRegions().add(dregion21);

		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial);

		return dChart;
	}

	/**
	 * Creates a Multi Dials, Single Region chart model as a reference
	 * implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createMDialSRegionChart() {
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

		dChart.getTitle().getLabel().getCaption().setValue("Meter Chart");//$NON-NLS-1$
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
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Speed" });//$NON-NLS-1$

		SeriesDefinition sdBase = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sdBase);

		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		sdBase.getSeries().add(seCategory);

		SeriesDefinition sdOrth = SeriesDefinitionImpl.create();

		final Fill[] fiaOrth = { ColorDefinitionImpl.ORANGE(), ColorDefinitionImpl.RED(), ColorDefinitionImpl.GREEN() };

		sdOrth.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaOrth.length; i++) {
			sdOrth.getSeriesPalette().getEntries().add(fiaOrth[i]);
		}

		// Dial 1
		DialSeries seDial1 = (DialSeries) DialSeriesImpl.create();
		seDial1.setDataSet(NumberDataSetImpl.create(new double[] { 60 }));
		seDial1.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		seDial1.getNeedle().setDecorator(LineDecorator.ARROW_LITERAL);
		seDial1.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial1.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.RED());
		seDial1.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial1.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial1.getDial().getScale().setMax(NumberDataElementImpl.create(180));
		seDial1.getDial().getScale().setStep(30);
		seDial1.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial1.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());
		seDial1.setSeriesIdentifier("Speed 1");//$NON-NLS-1$

		// Dail 2
		DialSeries seDial2 = (DialSeries) DialSeriesImpl.create();
		seDial2.setDataSet(NumberDataSetImpl.create(new double[] { 90 }));
		seDial2.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		seDial2.getNeedle().setDecorator(LineDecorator.ARROW_LITERAL);
		seDial2.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial2.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.RED());
		seDial2.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial2.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial2.getDial().getScale().setMax(NumberDataElementImpl.create(180));
		seDial2.getDial().getScale().setStep(30);
		seDial2.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial2.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());
		seDial2.setSeriesIdentifier("Speed 2");//$NON-NLS-1$

		// Dial 3
		DialSeries seDial3 = (DialSeries) DialSeriesImpl.create();
		seDial3.setDataSet(NumberDataSetImpl.create(new double[] { 160 }));
		seDial3.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		seDial3.getNeedle().setDecorator(LineDecorator.ARROW_LITERAL);
		seDial3.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial3.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.RED());
		seDial3.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial3.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial3.getDial().getScale().setMax(NumberDataElementImpl.create(180));
		seDial3.getDial().getScale().setStep(30);
		seDial3.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial3.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());
		seDial3.setSeriesIdentifier("Speed 3");//$NON-NLS-1$

		dChart.setDialSuperimposition(true);
		sdBase.getSeriesDefinitions().add(sdOrth);
		sdOrth.getSeries().add(seDial1);
		sdOrth.getSeries().add(seDial2);
		sdOrth.getSeries().add(seDial3);

		return dChart;
	}

	/**
	 * Creates a bar chart model with curve fitting series.
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createCFBarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(false);

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Bar Chart");//$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
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
		bs.setCurveFitting(CurveFittingImpl.create());

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	/**
	 * Creates a line chart model with curve fitting series
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createCFLineChart() {
		ChartWithAxes cwaLine = ChartWithAxesImpl.create();

		// Plot
		cwaLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaLine.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwaLine.getTitle().getLabel().getCaption().setValue("Line Chart");//$NON-NLS-1$

		// Legend
		cwaLine.getLegend().setVisible(false);

		// X-Axis
		Axis xAxisPrimary = cwaLine.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaLine.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 25, 35, 15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();

		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Sereis
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setDataSet(orthoValues);
		ls.getLineAttributes().setColor(ColorDefinitionImpl.CREAM());
		for (int i = 0; i < ls.getMarkers().size(); i++) {
			ls.getMarkers().get(i).setType(MarkerType.TRIANGLE_LITERAL);
		}
		ls.getLabel().setVisible(true);
		ls.setCurve(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-2);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ls);

		return cwaLine;
	}

	/**
	 * Creates a stock chart model with curve fitting series.
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public final static Chart createCFStockChart() {
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
				new StockEntry(27.01, 26.82, 27.10, 26.85), new StockEntry(26.87, 26.83, 27.15, 27.01),
				new StockEntry(26.84, 26.78, 27.15, 26.97), new StockEntry(27.00, 26.94, 27.17, 27.07),
				new StockEntry(27.01, 26.89, 27.15, 26.95), new StockEntry(27.00, 26.80, 27.32, 26.96),
				new StockEntry(27.15, 27.01, 27.28, 27.16), new StockEntry(27.22, 27.07, 27.40, 27.11), });

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(-1);
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

	/**
	 * Creates a Area chart model with curve fitting series implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing filled
	 *         datasets)
	 */
	public static final Chart createCFAreaChart() {
		ChartWithAxes cwaArea = ChartWithAxesImpl.create();

		// Plot/Title
		cwaArea.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaArea.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(225, 225, 225));
		cwaArea.getTitle().getLabel().getCaption().setValue("Area Chart");//$NON-NLS-1$
		cwaArea.getTitle().setVisible(true);

		// Legend
		Legend lg = cwaArea.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(false);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaArea.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid()
				.setLineAttributes(LineAttributesImpl.create(ColorDefinitionImpl.BLUE(), LineStyle.SOLID_LITERAL, 1));
		xAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		xAxisPrimary.getTitle().getCaption().setValue("Month");//$NON-NLS-1$
		xAxisPrimary.getTitle().setVisible(true);
		xAxisPrimary.getTitle().getCaption().getFont().setRotation(0);
		xAxisPrimary.getLabel().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaArea.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid()
				.setLineAttributes(LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1));
		yAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setPercent(false);
		yAxisPrimary.getTitle().getCaption().setValue("Net Profit");//$NON-NLS-1$
		yAxisPrimary.getTitle().setVisible(true);
		yAxisPrimary.getTitle().getCaption().getFont().setRotation(90);
		yAxisPrimary.getLabel().setVisible(true);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary, NumberDataElementImpl.create(2));
		yAxisPrimary.getMarkerLines().add(ml);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Jan.", "Feb.", "Mar.", "Apr", "May" }); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 14.32, -19.5, 8.38, 0.34, 9.22 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		AreaSeries as = (AreaSeries) AreaSeriesImpl.create();
		as.setSeriesIdentifier("Series");//$NON-NLS-1$
		as.setDataSet(orthoValues);
		as.setTranslucent(true);
		as.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		as.getLabel().setVisible(true);
		as.setCurve(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(as);

		return cwaArea;
	}

	public static final Chart create3DBarChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setDimension(ChartDimension.THREE_DIMENSIONAL_LITERAL);

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(false);

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("3D Bar Chart");//$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
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

		// Z-Axis
		Axis zAxis = AxisImpl.create(Axis.ANCILLARY_BASE);
		zAxis.setType(AxisType.TEXT_LITERAL);
		zAxis.setLabelPosition(Position.BELOW_LITERAL);
		zAxis.setTitlePosition(Position.BELOW_LITERAL);
		zAxis.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		zAxis.setOrientation(Orientation.HORIZONTAL_LITERAL);
		xAxisPrimary.getAncillaryAxes().add(zAxis);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
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
		bs.setLabelPosition(Position.OUTSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		// Z-Series
		SeriesDefinition sdZ = SeriesDefinitionImpl.create();
		zAxis.getSeriesDefinitions().add(sdZ);

		// Rotate the chart
		cwaBar.setRotation(Rotation3DImpl.create(new Angle3D[] { Angle3DImpl.create(-20, 45, 0) }));

		return cwaBar;
	}

	public static final Chart create3DLineChart() {
		ChartWithAxes cwa3DLine = ChartWithAxesImpl.create();
		cwa3DLine.setDimension(ChartDimension.THREE_DIMENSIONAL_LITERAL);

		// Plot
		cwa3DLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwa3DLine.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwa3DLine.getTitle().getLabel().getCaption().setValue("Line Chart");//$NON-NLS-1$

		// Legend
		cwa3DLine.getLegend().setVisible(false);

		// X-Axis
		Axis xAxisPrimary = cwa3DLine.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwa3DLine.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		// Z-Axis
		Axis zAxis = AxisImpl.create(Axis.ANCILLARY_BASE);
		zAxis.setType(AxisType.TEXT_LITERAL);
		zAxis.setLabelPosition(Position.BELOW_LITERAL);
		zAxis.setTitlePosition(Position.BELOW_LITERAL);
		zAxis.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		zAxis.setOrientation(Orientation.HORIZONTAL_LITERAL);
		xAxisPrimary.getAncillaryAxes().add(zAxis);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 25, 35, 15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();

		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Sereis
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setDataSet(orthoValues);
		ls.getLineAttributes().setColor(ColorDefinitionImpl.CREAM());
		for (int i = 0; i < ls.getMarkers().size(); i++) {
			ls.getMarkers().get(i).setType(MarkerType.TRIANGLE_LITERAL);
		}
		ls.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-2);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ls);

		// Z-Series
		SeriesDefinition sdZ = SeriesDefinitionImpl.create();
		zAxis.getSeriesDefinitions().add(sdZ);

		// Rotate the chart
		cwa3DLine.setRotation(Rotation3DImpl.create(new Angle3D[] { Angle3DImpl.create(-20, 45, 0) }));

		return cwa3DLine;
	}

	public static final Chart create3DAreaChart() {
		ChartWithAxes cwa3DArea = ChartWithAxesImpl.create();
		cwa3DArea.setDimension(ChartDimension.THREE_DIMENSIONAL_LITERAL);

		// Plot/Title
		cwa3DArea.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwa3DArea.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(225, 225, 225));
		cwa3DArea.getTitle().getLabel().getCaption().setValue("Area Chart");//$NON-NLS-1$
		cwa3DArea.getTitle().setVisible(true);

		// Legend
		Legend lg = cwa3DArea.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(false);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwa3DArea.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid()
				.setLineAttributes(LineAttributesImpl.create(ColorDefinitionImpl.BLUE(), LineStyle.SOLID_LITERAL, 1));
		xAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		xAxisPrimary.getTitle().getCaption().setValue("Month");//$NON-NLS-1$
		xAxisPrimary.getTitle().setVisible(true);
		xAxisPrimary.getTitle().getCaption().getFont().setRotation(0);
		xAxisPrimary.getLabel().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwa3DArea.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid()
				.setLineAttributes(LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1));
		yAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setPercent(false);
		yAxisPrimary.getTitle().getCaption().setValue("Net Profit");//$NON-NLS-1$
		yAxisPrimary.getTitle().setVisible(true);
		yAxisPrimary.getTitle().getCaption().getFont().setRotation(90);
		yAxisPrimary.getLabel().setVisible(true);

		// Z-Axis
		Axis zAxis = AxisImpl.create(Axis.ANCILLARY_BASE);
		zAxis.setType(AxisType.TEXT_LITERAL);
		zAxis.setLabelPosition(Position.BELOW_LITERAL);
		zAxis.setTitlePosition(Position.BELOW_LITERAL);
		zAxis.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		zAxis.setOrientation(Orientation.HORIZONTAL_LITERAL);
		xAxisPrimary.getAncillaryAxes().add(zAxis);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Jan.", "Feb.", "Mar.", "Apr", "May" }); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] { 14.32, -19.5, 8.38, 0.34, 9.22 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		AreaSeries as1 = (AreaSeries) AreaSeriesImpl.create();
		as1.setSeriesIdentifier("Series 1");//$NON-NLS-1$
		as1.setDataSet(orthoValues1);
		as1.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		as1.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(as1);

		// Z-Series
		SeriesDefinition sdZ = SeriesDefinitionImpl.create();
		zAxis.getSeriesDefinitions().add(sdZ);

		// Rotate the chart
		cwa3DArea.setRotation(Rotation3DImpl.create(new Angle3D[] { Angle3DImpl.create(-20, 45, 0) }));

		return cwa3DArea;
	}

	public static final Chart openChart() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir")); //$NON-NLS-1$
		fileChooser.setFileFilter(new FileFilter() {

			public boolean accept(File f) {
				return f.isDirectory() || f.isFile() && f.getName().endsWith(".chart"); //$NON-NLS-1$
			}

			public String getDescription() {
				return "*.chart(Chart files)"; //$NON-NLS-1$
			}
		});

		Chart chart = null;
		Serializer serializer = null;
		fileChooser.showOpenDialog(fileChooser.getParent());
		File chartFile = fileChooser.getSelectedFile();
		if (chartFile == null) {
			chartFile = new File("testChart.chart"); //$NON-NLS-1$
		}

		// Reads the chart model
		InputStream is = null;
		try {
			serializer = SerializerImpl.instance();
			if (chartFile.exists()) {
				is = new FileInputStream(chartFile);
				chart = serializer.read(is);
			}
		} catch (Exception e) {
			WizardBase.displayException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
		}

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Jan.", "Feb.", "Mar.", "Apr", "May" }); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] { 14.32, -19.5, 8.38, 0.34, 9.22 });

		ChartUIUtil.getBaseSeriesDefinitions(chart).get(0).getDesignTimeSeries().setDataSet(categoryValues);
		ChartUIUtil.getOrthogonalSeriesDefinitions(chart, 0).get(0).getDesignTimeSeries().setDataSet(orthoValues1);
		return chart;
	}
}