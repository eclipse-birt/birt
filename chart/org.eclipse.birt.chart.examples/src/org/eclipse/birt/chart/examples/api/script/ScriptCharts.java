/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.api.script;

import java.util.Locale;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.MarkerLineImpl;
import org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;

public class ScriptCharts {

	protected static final Chart createChart_Axis() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("function beforeDrawAxisLabel(axis, label, scriptContext)" //$NON-NLS-1$
				+ "{importPackage(Packages.org.eclipse.birt.chart.model.attribute); " //$NON-NLS-1$
				+ "if (axis.getType() == AxisType.TEXT_LITERAL)" //$NON-NLS-1$
				+ "label.getCaption( ).getColor( ).set( 140, 198, 62 );" //$NON-NLS-1$
				+ "else label.getCaption().getColor( ).set( 208, 32, 0);}" //$NON-NLS-1$

				+ "function beforeDrawAxisTitle(axis, title, scriptContext)" //$NON-NLS-1$
				+ "{importPackage(Packages.org.eclipse.birt.chart.model.attribute);" //$NON-NLS-1$
				+ "{ if (axis.getType() == AxisType.LINEAR_LITERAL)" //$NON-NLS-1$
				+ "title.getCaption( ).setValue( \"Y-Axis Title By JavaScript\");}" //$NON-NLS-1$
				+ "title.getCaption( ).getColor( ).set( 32, 168, 255 );}" //$NON-NLS-1$
		);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getTitle().setVisible(true);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createChart_DataPoints() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("function beforeDrawDataPointLabel(dataPoints, label, scriptContext)" //$NON-NLS-1$
				+ "{val = dataPoints.getOrthogonalValue( );" //$NON-NLS-1$
				+ "clr = label.getCaption( ).getColor( );" //$NON-NLS-1$
				+ "if ( val < -10 ) clr.set( 32, 168, 255 );" //$NON-NLS-1$
				+ "else if ( ( val >= -10 ) & ( val <=10 ) ) clr.set( 168, 0, 208 );" //$NON-NLS-1$
				+ "else if ( val > 10 ) clr.set( 0, 208, 32 );}"//$NON-NLS-1$
		);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createChart_Marker() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		Locale.setDefault(Locale.US);
		cwaBar.setScript("function beforeDrawMarkerLine(axis, line, scriptContext)" //$NON-NLS-1$
				+ "{ importPackage(Packages.java.util);" //$NON-NLS-1$
				+ "if (scriptContext.getLocale().equals(Locale.US))" //$NON-NLS-1$
				+ "{line.getLabel().getCaption( ).getColor().set( 165, 184, 55 );" //$NON-NLS-1$
				+ "line.getLineAttributes().getColor().set( 165, 184, 55 );}}" //$NON-NLS-1$

				+ "function beforeDrawMarkerRange(axis, range, scriptContext)" //$NON-NLS-1$
				+ "{range.getLabel().getCaption().getColor().set( 225, 104, 105 );}" //$NON-NLS-1$
		);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary, NumberDataElementImpl.create(2));
		yAxisPrimary.getMarkerLines().add(ml);

		MarkerRange mr = MarkerRangeImpl.create(yAxisPrimary, NumberDataElementImpl.create(8),
				NumberDataElementImpl.create(12), ColorDefinitionImpl.PINK());
		yAxisPrimary.getMarkerRanges().add(mr);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createChart_Series() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("function beforeDrawSeries(series, renderer, scriptContext)" //$NON-NLS-1$
				+ "{importPackage(Packages.org.eclipse.birt.chart.model.component.impl);" //$NON-NLS-1$
				+ "series.setCurveFitting(CurveFittingImpl.create());" //$NON-NLS-1$
				+ "series.getLabel().getCaption().getColor().set(12, 232, 182);}" //$NON-NLS-1$
		);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createChart_SeriesTitle() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();
		cwoaPie.setScript("function beforeDrawSeriesTitle(series, label, scriptContext)" //$NON-NLS-1$
				+ "{label.setVisible(true);" //$NON-NLS-1$
				+ "label.getCaption().setValue(\"Cities\");" //$NON-NLS-1$
				+ "label.getCaption().getColor().set(222, 32, 182);" //$NON-NLS-1$
				+ "series.getLabel().getCaption().getColor().set(12, 232, 182);}" //$NON-NLS-1$
		);

		cwoaPie.setSeriesThickness(10);

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

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie);

		return cwoaPie;
	}

	protected static final Chart createChart_Block() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("function beforeDrawBlock(block, scriptContext)" //$NON-NLS-1$
				+ "{importPackage(Packages.org.eclipse.birt.chart.model.attribute.impl);" //$NON-NLS-1$
				+ "if (block.isLegend())" //$NON-NLS-1$
				+ "{block.getOutline().setVisible( true );" //$NON-NLS-1$
				+ "block.getOutline().getColor().set(21,244,231);}" //$NON-NLS-1$

				+ "else if (block.isPlot())" //$NON-NLS-1$
				+ "{block.getOutline().setVisible( true );" //$NON-NLS-1$
				+ "block.getOutline().getColor().set(244,21,231);}" //$NON-NLS-1$

				+ "else if (block.isTitle())" //$NON-NLS-1$
				+ "{block.getOutline().setVisible( true );" //$NON-NLS-1$
				+ "block.setBackground(ColorDefinitionImpl.CREAM());" //$NON-NLS-1$
				+ "block.getOutline().getColor().set(0,0,0);}}" //$NON-NLS-1$
		);

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createChart_Legend() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("function beforeDrawLegendEntry(label, scriptContext)" //$NON-NLS-1$
				+ "{label.getCaption( ).getColor().set( 35, 184, 245 );"//$NON-NLS-1$
				+ "label.getCaption().getFont().setBold(true);" //$NON-NLS-1$
				+ "label.getCaption().getFont().setItalic(true);" //$NON-NLS-1$
				+ "label.getOutline().setVisible(true);" //$NON-NLS-1$
				+ "label.getOutline().getColor().set( 177, 12, 187);}" //$NON-NLS-1$
		);
		cwaBar.getLegend().setVisible(true);
		cwaBar.getLegend().setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axisj
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createJChart_Axis() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("org.eclipse.birt.chart.examples.api.script.java.AxisScript");//$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(true);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getTitle().setVisible(true);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createJChart_DataPoints() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("org.eclipse.birt.chart.examples.api.script.java.DataPointsScript");//$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createJChart_Marker() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("org.eclipse.birt.chart.examples.api.script.java.MarkerScript"); //$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		MarkerLine ml = MarkerLineImpl.create(yAxisPrimary, NumberDataElementImpl.create(2));
		yAxisPrimary.getMarkerLines().add(ml);

		MarkerRange mr = MarkerRangeImpl.create(yAxisPrimary, NumberDataElementImpl.create(8),
				NumberDataElementImpl.create(12), ColorDefinitionImpl.PINK());
		yAxisPrimary.getMarkerRanges().add(mr);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createJChart_Series() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("org.eclipse.birt.chart.examples.api.script.java.SeriesScript");//$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createJChart_SeriesTitle() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();
		cwoaPie.setScript("org.eclipse.birt.chart.examples.api.script.java.SeriesTitleScript");//$NON-NLS-1$

		cwoaPie.setSeriesThickness(10);

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

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie);

		return cwoaPie;
	}

	protected static final Chart createJChart_Block() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("org.eclipse.birt.chart.examples.api.script.java.BlockScript");//$NON-NLS-1$

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}

	protected static final Chart createJChart_Legend() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("org.eclipse.birt.chart.examples.api.script.java.LegendScript");//$NON-NLS-1$

		cwaBar.getLegend().setVisible(true);
		cwaBar.getLegend().setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axisj
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Item 1", "Item 2", "Item 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setDataSet(orthoValues);
		bs.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs);

		return cwaBar;
	}
}
