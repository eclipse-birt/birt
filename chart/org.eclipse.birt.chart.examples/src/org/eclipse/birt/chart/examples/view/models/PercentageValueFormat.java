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
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
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
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;

public class PercentageValueFormat {

	public static final Chart createPercentageValueFormat() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();
		cwoaPie.setType("Pie Chart"); //$NON-NLS-1$
		cwoaPie.setSubType("Standard Pie Chart"); //$NON-NLS-1$

		// Plot
		cwoaPie.setSeriesThickness(25);
		cwoaPie.getBlock().setBackground(ColorDefinitionImpl.WHITE());

		// Legend
		cwoaPie.getLegend().getText().getFont().setBold(true);

		// Title
		cwoaPie.getTitle().getLabel().getCaption().setValue("Pie Chart with Percentage Values");//$NON-NLS-1$
		cwoaPie.getTitle().getOutline().setVisible(true);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl
				.create(new String[] { "New York", "Boston", "Chicago", "San Francisco", "Dallas" });//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$

		NumberDataSet seriesValues = NumberDataSetImpl.create(new double[] { 54.65, 21, 75.95, 91.28, 37.43 });

		SampleData sdata = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sdata.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sdata.getOrthogonalSampleData().add(sdOrthogonal);

		cwoaPie.setSampleData(sdata);

		// Base Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().shift(0);
		sd.getSeries().add(seCategory);

		// Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create();
		sePie.setDataSet(seriesValues);
		sePie.setExplosion(3);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie);

		DataPointComponent dpc = DataPointComponentImpl.create(
				DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("##.##%")); //$NON-NLS-1$
		sePie.getDataPoint().getComponents().clear();
		sePie.getDataPoint().getComponents().add(dpc);

		return cwoaPie;
	}

}
