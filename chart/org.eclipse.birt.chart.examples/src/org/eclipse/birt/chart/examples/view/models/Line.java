/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
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
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;

public class Line {

	public static final Chart createLine() {
		ChartWithAxes cwa3DLine = ChartWithAxesImpl.create();
		cwa3DLine.setDimension(ChartDimension.THREE_DIMENSIONAL_LITERAL);
		cwa3DLine.setType("Line Chart"); //$NON-NLS-1$
		cwa3DLine.setSubType("Overlay"); //$NON-NLS-1$

		// Plot
		cwa3DLine.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwa3DLine.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwa3DLine.getTitle().getLabel().getCaption().setValue("3D Line Chart");//$NON-NLS-1$

		// X-Axis
		Axis xAxisPrimary = cwa3DLine.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

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
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(new double[] { 25, 35, 15 });
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] { 15, 10, 25 });

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

		cwa3DLine.setSampleData(sd);

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();

		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		LineSeries ls1 = (LineSeries) LineSeriesImpl.create();
		ls1.setDataSet(orthoValues1);
		ls1.getLineAttributes().setColor(ColorDefinitionImpl.CREAM());
		for (int i = 0; i < ls1.getMarkers().size(); i++) {
			((Marker) ls1.getMarkers().get(i)).setType(MarkerType.TRIANGLE_LITERAL);
		}
		ls1.getLabel().setVisible(true);
		ls1.setPaletteLineColor(true);

		// Y-Series
		LineSeries ls2 = (LineSeries) LineSeriesImpl.create();
		ls2.setDataSet(orthoValues2);
		ls2.getLineAttributes().setColor(ColorDefinitionImpl.CREAM());
		for (int i = 0; i < ls2.getMarkers().size(); i++) {
			((Marker) ls2.getMarkers().get(i)).setType(MarkerType.TRIANGLE_LITERAL);
		}
		ls2.getLabel().setVisible(true);
		ls2.setPaletteLineColor(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ls1);
		sdY.getSeries().add(ls2);

		// Z-Series
		SeriesDefinition sdZ = SeriesDefinitionImpl.create();
		zAxis.getSeriesDefinitions().add(sdZ);

		// Rotate the chart
		cwa3DLine.setRotation(Rotation3DImpl.create(new Angle3D[] { Angle3DImpl.create(-10, 25, 0) }));

		return cwa3DLine;
	}

}
