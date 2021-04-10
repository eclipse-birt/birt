/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
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
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;

public class StackedArea {

	public static final Chart createStackedArea() {
		ChartWithAxes cwaArea = ChartWithAxesImpl.create();
		cwaArea.setType("Area Chart"); //$NON-NLS-1$
		cwaArea.setSubType("Stacked"); //$NON-NLS-1$

		// Plot/Title
		cwaArea.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaArea.getTitle().getLabel().getCaption().setValue("Stacked Area Chart");//$NON-NLS-1$

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
				.setLineAttributes(LineAttributesImpl.create(ColorDefinitionImpl.PINK(), LineStyle.SOLID_LITERAL, 1));
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaArea.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid()
				.setLineAttributes(LineAttributesImpl.create(ColorDefinitionImpl.PINK(), LineStyle.SOLID_LITERAL, 1));

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Jan.", "Feb.", "Mar.", "Apr", "May" }); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] { 14.32, -19.5, 8.38, 0.34, 9.22 });
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] { 4.2, -19.5, 0.0, 9.2, 7.6 });

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

		cwaArea.setSampleData(sd);

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
		as1.getLineAttributes().setColor(ColorDefinitionImpl.BLACK());
		as1.getLabel().setVisible(true);
		as1.setStacked(true);

		AreaSeries as2 = (AreaSeries) AreaSeriesImpl.create();
		as2.setSeriesIdentifier("Series 2");//$NON-NLS-1$
		as2.setDataSet(orthoValues2);
		as2.setTranslucent(true);
		as2.getLineAttributes().setColor(ColorDefinitionImpl.BLACK());
		as2.getLabel().setVisible(true);
		as2.setStacked(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(as1);
		sdY.getSeries().add(as2);

		return cwaArea;

	}
}
