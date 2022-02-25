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
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
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
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;

public class Area {
	public static final Chart createArea() {
		ChartWithAxes cwa3DArea = ChartWithAxesImpl.create();
		cwa3DArea.setDimension(ChartDimension.THREE_DIMENSIONAL_LITERAL);
		cwa3DArea.setType("Area Chart"); //$NON-NLS-1$
		cwa3DArea.setSubType("Overlay"); //$NON-NLS-1$

		// Title
		cwa3DArea.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwa3DArea.getTitle().getLabel().getCaption().setValue("3D Area Chart");//$NON-NLS-1$
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

		// Y-Axis
		Axis yAxisPrimary = cwa3DArea.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid()
				.setLineAttributes(LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1));
		yAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setPercent(false);

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
		NumberDataSet orthoValues2 = NumberDataSetImpl.create(new double[] { 4.55, 2.61, -3.18, 14.2, 10.22 });
		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

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

		cwa3DArea.setSampleData(sd);

		// Y-Series (1)
		AreaSeries as1 = (AreaSeries) AreaSeriesImpl.create();
		as1.setSeriesIdentifier("Series 1");//$NON-NLS-1$
		as1.setDataSet(orthoValues1);

		as1.getLineAttributes().setVisible(false);
		as1.getLabel().setVisible(true);

		// Y-Series (2)
		AreaSeries as2 = (AreaSeries) AreaSeriesImpl.create();
		as2.setSeriesIdentifier("Series 2");//$NON-NLS-1$
		as2.setDataSet(orthoValues2);
		as2.getLineAttributes().setVisible(false);
		as2.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(as1);
		sdY.getSeries().add(as2);

		// Z-Series
		SeriesDefinition sdZ = SeriesDefinitionImpl.create();
		zAxis.getSeriesDefinitions().add(sdZ);

		// Rotate the chart
		cwa3DArea.setRotation(Rotation3DImpl.create(new Angle3D[] { Angle3DImpl.create(-10, 25, 0) }));

		return cwa3DArea;
	}
}
