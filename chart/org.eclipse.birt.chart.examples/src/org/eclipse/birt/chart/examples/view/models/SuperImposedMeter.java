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
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.DialRegionImpl;
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
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;

public class SuperImposedMeter {

	public static final Chart createSuperImposedMeter() {
		DialChart dChart = (DialChart) DialChartImpl.create();
		dChart.setType("Meter Chart"); //$NON-NLS-1$
		dChart.setSubType("Superimposed Meter Chart"); //$NON-NLS-1$

		dChart.setDialSuperimposition(true);
		dChart.setGridColumnCount(2);
		dChart.setSeriesThickness(25);

		// Title/Plot
		dChart.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		dChart.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		dChart.getTitle().getLabel().getCaption().setValue("Super Imposed Meter Chart");//$NON-NLS-1$
		dChart.getTitle().getOutline().setVisible(false);

		// Legend
		dChart.getLegend().setVisible(false);

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Moto" });//$NON-NLS-1$

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

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

		final Fill[] fiaOrth = { ColorDefinitionImpl.PINK(), ColorDefinitionImpl.ORANGE(),
				ColorDefinitionImpl.WHITE() };
		sdCity.getSeriesPalette().getEntries().clear();
		for (int i = 0; i < fiaOrth.length; i++) {
			sdCity.getSeriesPalette().getEntries().add(fiaOrth[i]);
		}

		// Dial 1
		DialSeries seDial1 = (DialSeries) DialSeriesImpl.create();
		seDial1.setDataSet(NumberDataSetImpl.create(new double[] { 20 }));
		seDial1.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial1.setSeriesIdentifier("Temperature");//$NON-NLS-1$
		seDial1.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial1.getDial().setStartAngle(-45);
		seDial1.getDial().setStopAngle(225);
		seDial1.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial1.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial1.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial1.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial1.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial1.getDial().getScale().setStep(10);
		seDial1.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial1.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		DialRegion dregion1 = DialRegionImpl.create();
		dregion1.setFill(ColorDefinitionImpl.GREEN());
		dregion1.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion1.setStartValue(NumberDataElementImpl.create(70));
		dregion1.setEndValue(NumberDataElementImpl.create(90));
		dregion1.setInnerRadius(40);
		dregion1.setOuterRadius(-1);
		seDial1.getDial().getDialRegions().add(dregion1);

		DialRegion dregion2 = DialRegionImpl.create();
		dregion2.setFill(ColorDefinitionImpl.YELLOW());
		dregion2.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion2.setStartValue(NumberDataElementImpl.create(40));
		dregion2.setEndValue(NumberDataElementImpl.create(70));
		dregion2.setOuterRadius(70);
		seDial1.getDial().getDialRegions().add(dregion2);

		DialRegion dregion3 = DialRegionImpl.create();
		dregion3.setFill(ColorDefinitionImpl.RED());
		dregion3.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion3.setStartValue(NumberDataElementImpl.create(0));
		dregion3.setEndValue(NumberDataElementImpl.create(40));
		dregion3.setInnerRadius(40);
		dregion3.setOuterRadius(90);
		seDial1.getDial().getDialRegions().add(dregion3);

		// Dial 2
		DialSeries seDial2 = (DialSeries) DialSeriesImpl.create();
		seDial2.setDataSet(NumberDataSetImpl.create(new double[] { 58 }));
		seDial2.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial2.setSeriesIdentifier("Wind Speed");//$NON-NLS-1$
		seDial2.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial2.getDial().setStartAngle(-45);
		seDial2.getDial().setStopAngle(225);
		seDial2.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial2.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial2.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial2.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial2.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial2.getDial().getScale().setStep(10);
		seDial2.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial2.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		seDial2.getDial().getDialRegions().add(dregion1);
		seDial2.getDial().getDialRegions().add(dregion2);
		seDial2.getDial().getDialRegions().add(dregion3);

		// Dial 3
		DialSeries seDial3 = (DialSeries) DialSeriesImpl.create();
		seDial3.setDataSet(NumberDataSetImpl.create(new double[] { 80 }));
		seDial3.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		seDial3.setSeriesIdentifier("Viscosity");//$NON-NLS-1$
		seDial3.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial3.getDial().setStartAngle(-45);
		seDial3.getDial().setStopAngle(225);
		seDial3.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial3.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial3.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial3.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial3.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial3.getDial().getScale().setStep(10);
		seDial3.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial3.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		seDial3.getDial().getDialRegions().add(dregion1);
		seDial3.getDial().getDialRegions().add(dregion2);
		seDial3.getDial().getDialRegions().add(dregion3);

		dChart.setDialSuperimposition(true);
		sdBase.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial1);
		sdCity.getSeries().add(seDial2);
		sdCity.getSeries().add(seDial3);

		return dChart;
	}

}
