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
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.DialRegionImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.NumberDataSet;
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

public class StandardMeter {

	public static final Chart createStandardMeter() {
		DialChart dChart = (DialChart) DialChartImpl.create();
		dChart.setType("Meter Chart"); //$NON-NLS-1$
		dChart.setSubType("Standard Meter Chart"); //$NON-NLS-1$

		dChart.setDialSuperimposition(false);
		dChart.setGridColumnCount(2);
		dChart.setSeriesThickness(25);

		dChart.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		dChart.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));
		dChart.getTitle().getLabel().getCaption().setValue("Standard Meter Chart");//$NON-NLS-1$

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "London", "Madrid", "Rome", "Moscow" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
		NumberDataSet seriesValues = NumberDataSetImpl.create(new double[] { 21.0, 39.0, 30.0, 10.0 });

		SampleData sdata = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sdata.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sdata.getOrthogonalSampleData().add(sdOrthogonal);

		dChart.setSampleData(sdata);

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		dChart.getSeriesDefinitions().add(sd);
		Series seCategory = (Series) SeriesImpl.create();

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

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();

		// Dial
		DialSeries seDial = (DialSeries) DialSeriesImpl.create();
		seDial.setDataSet(seriesValues);
		seDial.getDial().setFill(GradientImpl.create(ColorDefinitionImpl.create(225, 255, 225),
				ColorDefinitionImpl.create(225, 225, 255), 45, false));
		NumberFormatSpecifier nfs = NumberFormatSpecifierImpl.create();
		nfs.setSuffix("`C");//$NON-NLS-1$
		nfs.setFractionDigits(0);
		seDial.getDial().setFormatSpecifier(nfs);
		seDial.setSeriesIdentifier("Temperature");//$NON-NLS-1$
		seDial.getNeedle().setDecorator(LineDecorator.CIRCLE_LITERAL);
		seDial.getDial().setStartAngle(-45);
		seDial.getDial().setStopAngle(225);
		seDial.getDial().getMinorGrid().getTickAttributes().setVisible(true);
		seDial.getDial().getMinorGrid().getTickAttributes().setColor(ColorDefinitionImpl.BLACK());
		seDial.getDial().getMinorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		seDial.getDial().getScale().setMin(NumberDataElementImpl.create(0));
		seDial.getDial().getScale().setMax(NumberDataElementImpl.create(90));
		seDial.getDial().getScale().setStep(10);
		seDial.getLabel()
				.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.GREY().darker(), LineStyle.SOLID_LITERAL, 1));
		seDial.getLabel().setBackground(ColorDefinitionImpl.GREY().brighter());

		DialRegion dregion1 = DialRegionImpl.create();
		dregion1.setFill(ColorDefinitionImpl.GREEN());
		dregion1.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion1.setStartValue(NumberDataElementImpl.create(70));
		dregion1.setEndValue(NumberDataElementImpl.create(90));
		dregion1.setInnerRadius(40);
		dregion1.setOuterRadius(-1);
		seDial.getDial().getDialRegions().add(dregion1);

		DialRegion dregion2 = DialRegionImpl.create();
		dregion2.setFill(ColorDefinitionImpl.YELLOW());
		dregion2.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion2.setStartValue(NumberDataElementImpl.create(40));
		dregion2.setEndValue(NumberDataElementImpl.create(70));
		dregion2.setOuterRadius(70);
		seDial.getDial().getDialRegions().add(dregion2);

		DialRegion dregion3 = DialRegionImpl.create();
		dregion3.setFill(ColorDefinitionImpl.RED());
		dregion3.setOutline(
				LineAttributesImpl.create(ColorDefinitionImpl.BLACK().darker(), LineStyle.SOLID_LITERAL, 1));
		dregion3.setStartValue(NumberDataElementImpl.create(0));
		dregion3.setEndValue(NumberDataElementImpl.create(40));
		dregion3.setInnerRadius(40);
		dregion3.setOuterRadius(90);
		seDial.getDial().getDialRegions().add(dregion3);

		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(seDial);

		return dChart;
	}
}
