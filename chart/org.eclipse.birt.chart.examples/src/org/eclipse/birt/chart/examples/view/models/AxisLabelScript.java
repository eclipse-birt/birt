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
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
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
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;

public class AxisLabelScript {

	public static final Chart createAxisLabelScript() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setScript("function beforeDrawAxisLabel(axis, label, scriptContext)" //$NON-NLS-1$
				+ "{label.getCaption( ).getColor( ).set( 140, 198, 62 );" //$NON-NLS-1$
				+ "}\n" //$NON-NLS-1$
				+ "function beforeGeneration( chart, scriptContext )" //$NON-NLS-1$
				+ "{baseAxises = chart.getBaseAxes();" //$NON-NLS-1$
				+ "for (i = 0; i < baseAxises.length; i++ )" //$NON-NLS-1$
				+ "{font = baseAxises[i].getLabel().getCaption( ).getFont( );" //$NON-NLS-1$
				+ "if ( font == null )" //$NON-NLS-1$
				+ "{font = FontDefinition.createEmpty();" //$NON-NLS-1$
				+ "baseAxises[i].getLabel().getCaption().setFont( font );" //$NON-NLS-1$
				+ "}" //$NON-NLS-1$
				+ "font.setSize( 20 );" //$NON-NLS-1$
				+ "orthAxises = chart.getOrthogonalAxes( baseAxises[i], true );" //$NON-NLS-1$
				+ "if ( orthAxises == null )" //$NON-NLS-1$
				+ "{continue;}" //$NON-NLS-1$
				+ "for (j = 0; j < orthAxises.length; j++ )" //$NON-NLS-1$
				+ "{font = orthAxises[j].getLabel().getCaption( ).getFont( );" //$NON-NLS-1$
				+ "if ( font == null )" //$NON-NLS-1$
				+ "{font = FontDefinition.createEmpty();" //$NON-NLS-1$
				+ "orthAxises[j].getLabel().getCaption().setFont( font );" //$NON-NLS-1$
				+ "}" //$NON-NLS-1$
				+ "font.setSize( 20 );" //$NON-NLS-1$
				+ "}}}" //$NON-NLS-1$
		);

		cwaBar.setType("Bar Chart"); //$NON-NLS-1$
		cwaBar.setSubType("Side-by-side"); //$NON-NLS-1$

		cwaBar.getTitle().getLabel().getCaption().setValue("Axis Label Script Chart"); //$NON-NLS-1$
		cwaBar.getLegend().setVisible(false);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		NumberDataSet orthoValues = NumberDataSetImpl.create(new double[] { 8, 18, -15, -8, 10 });

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);

		cwaBar.setSampleData(sd);

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
