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
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;

public class MDialSRegionMeter {

	public static final Chart createMDialSRegionMeter() {
		DialChart dChart = (DialChart) DialChartImpl.create();
		dChart.setType("Meter Chart"); //$NON-NLS-1$
		dChart.setSubType("Superimposed Meter Chart"); //$NON-NLS-1$

		dChart.setDialSuperimposition(true);
		dChart.setGridColumnCount(2);
		dChart.setSeriesThickness(25);

		// Title/Plot
		dChart.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		dChart.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		dChart.getTitle().getLabel().getCaption().setValue("Multiple Dial Single Region Meter Chart");//$NON-NLS-1$
		dChart.getTitle().getOutline().setVisible(false);

		// Legend
		Legend lg = dChart.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().setLeft(10);
		lg.getInsets().setRight(10);
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

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData base = DataFactory.eINSTANCE.createBaseSampleData();
		base.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(base);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal1);

		OrthogonalSampleData sdOrthogonal2 = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal2.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal2.setSeriesDefinitionIndex(1);
		sd.getOrthogonalSampleData().add(sdOrthogonal2);

		OrthogonalSampleData sdOrthogonal3 = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal3.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal3.setSeriesDefinitionIndex(2);
		sd.getOrthogonalSampleData().add(sdOrthogonal3);

		dChart.setSampleData(sd);

		SeriesDefinition sdBase = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sdBase);

		Series seCategory = (Series) SeriesImpl.create();
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

}
