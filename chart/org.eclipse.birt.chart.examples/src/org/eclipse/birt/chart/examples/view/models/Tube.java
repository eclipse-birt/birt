/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.view.models;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;

/**
 * 
 */

public class Tube {

	public static final Chart createTube() {
		ChartWithAxes cwaTube = ChartWithAxesImpl.create();
		cwaTube.setType("Tube Chart"); //$NON-NLS-1$
		cwaTube.setSubType("Side-by-side"); //$NON-NLS-1$
		// Plot
		cwaTube.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaTube.getBlock().getOutline().setVisible(true);
		Plot p = cwaTube.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwaTube.getTitle().getLabel().getCaption().setValue("Side-by-side Tube Chart"); //$NON-NLS-1$

		// Legend
		Legend lg = cwaTube.getLegend();
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaTube.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaTube.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] { 25, 35, 15, 5, 20 });
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] { 5, 10, 25, 10, 5 });

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal1);

		OrthogonalSampleData sdOrthogonal2 = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal2.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal2.setSeriesDefinitionIndex(1);
		sd.getOrthogonalSampleData().add(sdOrthogonal2);

		cwaTube.setSampleData(sd);

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setRiser(RiserType.TUBE_LITERAL);
		bs1.setDataSet(orthoValues1);
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create();
		bs2.setRiser(RiserType.TUBE_LITERAL);
		bs2.setDataSet(orthoValues2);
		bs2.getLabel().setVisible(true);
		bs2.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(bs2);

		return cwaTube;
	}
}
