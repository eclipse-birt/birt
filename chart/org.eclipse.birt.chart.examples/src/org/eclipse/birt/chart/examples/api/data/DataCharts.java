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

package org.eclipse.birt.chart.examples.api.data;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.DataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.util.BigNumber;
import org.eclipse.birt.chart.util.ChartUtil;

public class DataCharts {

	protected static final Chart createMinSliceChart() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();
		cwoaPie.getBlock().setBackground(ColorDefinitionImpl.PINK());

		// Plot
		Plot p = cwoaPie.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.PINK());
		p.getClientArea().getOutline().setVisible(false);
		p.getOutline().setVisible(false);

		// Legend
		Legend lg = cwoaPie.getLegend();
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);
		lg.getClientArea().getOutline().setVisible(true);
		lg.getTitle().setVisible(false);

		// Title
		cwoaPie.getTitle().getLabel().getCaption().setValue("Explosion & Min Slice"); //$NON-NLS-1$
		cwoaPie.getTitle().getOutline().setVisible(false);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "New York", "Boston", "Chicago", "San Francisco", "Dallas", "Miami"//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$//$NON-NLS-6$
				});
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] { 24, 9, 30, 36, 8, 51 });

		// Base Series
		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);

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

		// Orthogonal Series
		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdCity);

		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesOneValues);
		sePie.setLabelPosition(Position.INSIDE_LITERAL);
		sePie.setSeriesIdentifier("Cities"); //$NON-NLS-1$

		// Explosion
		sePie.setExplosion(30);
		sePie.setExplosionExpression("valueData<20 ||valueData>50");//$NON-NLS-1$

		sdCity.getSeries().add(sePie);

		// Min Slice
		cwoaPie.setMinSlice(10);
		cwoaPie.setMinSlicePercent(false);
		cwoaPie.setMinSliceLabel("Others");//$NON-NLS-1$

		return cwoaPie;
	}

	protected static final Chart createMultiYAxisChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 245, 255));

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Line Chart with Multiple Y Axis");//$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().set(10, 5, 0, 0);
		lg.getOutline().setVisible(false);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().setVisible(false);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getTitle().getCaption().setValue("Sales Growth ($Million)");//$NON-NLS-1$

		// Y-Axis (2)
		Axis yAxis = AxisImpl.create(Axis.ORTHOGONAL);
		yAxis.setType(AxisType.LINEAR_LITERAL);
		yAxis.getMajorGrid().setTickStyle(TickStyle.RIGHT_LITERAL);
		yAxis.setLabelPosition(Position.RIGHT_LITERAL);
		xAxisPrimary.getAssociatedAxes().add(yAxis);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "March", "April", "May", "June", "July" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] { 12.5, 19.6, 18.3, 13.2, 26.5 });
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] { 22.7, 23.6, 38.3, 43.2, 40.5 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series (1)
		LineSeries ls1 = (LineSeries) LineSeriesImpl.create();
		ls1.setSeriesIdentifier("A Corp.");//$NON-NLS-1$
		ls1.setDataSet(orthoValues1);
		ls1.getLineAttributes().setColor(ColorDefinitionImpl.CREAM());
		for (int i = 0; i < ls1.getMarkers().size(); i++) {
			ls1.getMarkers().get(i).setType(MarkerType.TRIANGLE_LITERAL);
			ls1.getMarkers().get(i).setSize(10);
		}
		ls1.getLabel().setVisible(true);

		SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		sdY1.getSeriesPalette().shift(-2);
		yAxisPrimary.getSeriesDefinitions().add(sdY1);
		sdY1.getSeries().add(ls1);

		// Y-Serires (2)
		LineSeries ls2 = (LineSeries) LineSeriesImpl.create();
		ls2.setSeriesIdentifier("B Corp.");//$NON-NLS-1$
		ls2.setDataSet(orthoValues2);
		ls2.getLineAttributes().setColor(ColorDefinitionImpl.CREAM());
		for (int i = 0; i < ls2.getMarkers().size(); i++) {
			ls2.getMarkers().get(i).setType(MarkerType.CIRCLE_LITERAL);
			ls2.getMarkers().get(i).setSize(10);
		}
		ls2.getLabel().setVisible(true);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		sdY2.getSeriesPalette().shift(-3);
		yAxis.getSeriesDefinitions().add(sdY2);
		sdY2.getSeries().add(ls2);

		return cwaBar;
	}

	protected static final Chart createMultiYSeriesChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(GradientImpl.create(ColorDefinitionImpl.create(225, 225, 255),
				ColorDefinitionImpl.create(255, 255, 225), -35, false));
		p.getOutline().setVisible(true);

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Bar Chart with Multiple Y Series");//$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setSize(16);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getTitle().getCaption().setValue("Regional Markets"); //$NON-NLS-1$
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);
		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getTitle().getCaption().setValue("Sales vs. Net Profit ($Million)");//$NON-NLS-1$

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Europe", "Asia", "North America" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] { 26.17, 34.21, 21.5 });
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] { 4.81, 3.55, -5.26 });

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series (1)
		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		bs.setSeriesIdentifier("Sales");//$NON-NLS-1$
		bs.setDataSet(orthoValues1);
		bs.setRiserOutline(null);
		bs.getLabel().setVisible(true);
		bs.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY1 = SeriesDefinitionImpl.create();
		sdY1.getSeriesPalette().shift(-2);
		yAxisPrimary.getSeriesDefinitions().add(sdY1);
		sdY1.getSeries().add(bs);

		// Y-Series (2)
		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setSeriesIdentifier("Net Profit");//$NON-NLS-1$
		bs2.setDataSet(orthoValues2);
		bs2.setRiserOutline(null);
		bs2.getLabel().setVisible(true);
		bs2.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		sdY2.getSeriesPalette().shift(-3);
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeries().add(bs2);

		return cwaBar;
	}

	/**
	 * To support big number by chart API, there are two steps:
	 * <p>
	 * 1. Create big number data set and set big number flag as true in the data
	 * set. When you create instance of NumberDataSet, the
	 * <code>org.eclipse.birt.chart.util.BigNubmer</code> class should be used .
	 * <P>
	 * 2. Adjust big number data set before doing chart layout and rendering. You
	 * need to call ChartUtil.adjustBigNumberWithinDataSets(chart_model).
	 * 
	 * @return
	 */
	protected static final Chart createBigNumberSliceChart() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();
		cwoaPie.getBlock().setBackground(ColorDefinitionImpl.PINK());

		// Plot
		Plot p = cwoaPie.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.PINK());
		p.getClientArea().getOutline().setVisible(false);
		p.getOutline().setVisible(false);

		// Legend
		Legend lg = cwoaPie.getLegend();
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);
		lg.getClientArea().getOutline().setVisible(true);
		lg.getTitle().setVisible(false);

		// Title
		cwoaPie.getTitle().getLabel().getCaption().setValue("Big Number Pie Chart"); //$NON-NLS-1$
		cwoaPie.getTitle().getOutline().setVisible(false);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "New York", "Boston", "Chicago", "San Francisco", "Dallas", "Miami"//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$//$NON-NLS-6$
				});

		// 1. Create a big number data set and set big number flag as true.
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new BigNumber[] { new BigNumber("3E314"), //$NON-NLS-1$
				new BigNumber("5E315"), //$NON-NLS-1$
				new BigNumber("8E314"), //$NON-NLS-1$
				new BigNumber("7E315"), //$NON-NLS-1$
				new BigNumber("5.5E315"), //$NON-NLS-1$
				new BigNumber("3.5E316")//$NON-NLS-1$
		});
		((DataSetImpl) seriesOneValues).setIsBigNumber(true);

		// Base Series
		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);

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

		// Orthogonal Series
		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdCity);

		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesOneValues);
		sePie.setLabelPosition(Position.INSIDE_LITERAL);
		sePie.setSeriesIdentifier("Cities"); //$NON-NLS-1$

		sdCity.getSeries().add(sePie);

		// Min Slice
		cwoaPie.setMinSlice(10);
		cwoaPie.setMinSlicePercent(false);
		cwoaPie.setMinSliceLabel("Others");//$NON-NLS-1$

		// 2. Adjust big number data sets for runtime chart model.
		try {
			ChartUtil.adjustBigNumberWithinDataSets(cwoaPie);
		} catch (ChartException e) {
			e.printStackTrace();
		}
		return cwoaPie;
	}
}
