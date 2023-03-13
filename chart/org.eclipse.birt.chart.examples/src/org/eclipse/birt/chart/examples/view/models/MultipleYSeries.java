/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view.models;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Position;
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

public class MultipleYSeries {

	public static final Chart createMultipleYSeries() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setType("Bar Chart"); //$NON-NLS-1$
		cwaBar.setSubType("Side-by-side"); //$NON-NLS-1$

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		p.getOutline().setVisible(true);

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Bar Chart with Multiple Y Series");//$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.getText().getFont().setBold(true);
		lg.getInsets().set(10, 5, 0, 0);
		lg.setAnchor(Anchor.NORTH_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Europe", "Asia", "North America" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] { 26.17, 34.21, 21.5 });
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] { 4.81, 3.55, -5.26 });

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

		cwaBar.setSampleData(sd);

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
		bs2.getLabel().setVisible(true);
		bs2.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create();
		sdY2.getSeriesPalette().shift(-3);
		yAxisPrimary.getSeriesDefinitions().add(sdY2);
		sdY2.getSeries().add(bs2);

		return cwaBar;
	}

}
