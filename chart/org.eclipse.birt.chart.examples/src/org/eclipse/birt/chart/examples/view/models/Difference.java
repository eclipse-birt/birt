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

import org.eclipse.birt.chart.extension.datafeed.DifferenceEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.MultipleFillImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DifferenceDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.DifferenceDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl;

/**
 * 
 */

public class Difference {

	public final static Chart createDifference() {
		ChartWithAxes cwaDifference = ChartWithAxesImpl.create();
		cwaDifference.setType("Difference Chart"); //$NON-NLS-1$
		cwaDifference.setSubType("Standard Difference Chart"); //$NON-NLS-1$
		// Plot
		cwaDifference.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaDifference.getBlock().getOutline().setVisible(true);
		Plot p = cwaDifference.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwaDifference.getTitle().getLabel().getCaption().setValue("Difference Chart"); //$NON-NLS-1$

		// Legend
		Legend lg = cwaDifference.getLegend();
		lg.setVisible(false);

		// X-Axis
		Axis xAxisPrimary = cwaDifference.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaDifference.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "2003", "2004", "2005", "2006" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
		DifferenceDataSet values = DifferenceDataSetImpl.create(new DifferenceEntry[] { new DifferenceEntry(12.3, 9.3),
				new DifferenceEntry(25.9, 15.7), new DifferenceEntry(15.9, 25.7), new DifferenceEntry(19.5, 14.5) });
		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal1);

		cwaDifference.setSampleData(sd);

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		DifferenceSeries bs1 = (DifferenceSeries) DifferenceSeriesImpl.create();
		bs1.setDataSet(values);
		bs1.getLabel().setVisible(false);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		MultipleFill mf = MultipleFillImpl.create();
		mf.getFills().add(ColorDefinitionImpl.GREEN());
		mf.getFills().add(ColorDefinitionImpl.RED());
		sdY.getSeriesPalette().update(mf);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);

		return cwaDifference;
	}
}
