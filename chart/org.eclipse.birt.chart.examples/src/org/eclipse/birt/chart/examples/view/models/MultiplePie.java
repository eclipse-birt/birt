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
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
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
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;

public class MultiplePie {

	public static final Chart createMultiplePie() {
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();
		cwoaPie.setType("Pie Chart"); //$NON-NLS-1$
		cwoaPie.setSubType("Standard Pie Chart"); //$NON-NLS-1$

		// Plot
		cwoaPie.setSeriesThickness(25);
		cwoaPie.setGridColumnCount(2);
		cwoaPie.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwoaPie.getPlot();
		p.getClientArea().getOutline().setVisible(true);
		p.getOutline().setVisible(true);

		// Legend
		Legend lg = cwoaPie.getLegend();
		lg.getText().getFont().setSize(16);
		lg.setBackground(null);
		lg.getOutline().setVisible(true);

		// Title
		cwoaPie.getTitle().getLabel().getCaption().setValue("Multiple Pie Chart");//$NON-NLS-1$
		cwoaPie.getTitle().getOutline().setVisible(true);

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(new String[] { "Boston", "New York", "Chicago" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(new double[] { 54.65, 21, 75.95 });
		NumberDataSet seriesTwoValues = NumberDataSetImpl.create(new double[] { 15.65, 65, 25.95 });

		SampleData sdata = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sdata.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sdata.getOrthogonalSampleData().add(sdOrthogonal1);

		OrthogonalSampleData sdOrthogonal2 = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal2.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal2.setSeriesDefinitionIndex(1);
		sdata.getOrthogonalSampleData().add(sdOrthogonal2);

		cwoaPie.setSampleData(sdata);

		// Base Sereis
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sd = SeriesDefinitionImpl.create();
		cwoaPie.getSeriesDefinitions().add(sd);
		sd.getSeriesPalette().shift(-1);
		sd.getSeries().add(seCategory);

		// Orthogonal Series
		PieSeries sePie1 = (PieSeries) PieSeriesImpl.create();
		sePie1.setDataSet(seriesOneValues);
		sePie1.setSeriesIdentifier("2000");//$NON-NLS-1$
		sePie1.setExplosion(3);

		PieSeries sePie2 = (PieSeries) PieSeriesImpl.create();
		sePie2.setDataSet(seriesTwoValues);
		sePie2.setSeriesIdentifier("2001");//$NON-NLS-1$
		sePie2.getLabel().setBackground(ColorDefinitionImpl.YELLOW());
		sePie2.getLabel().setShadowColor(ColorDefinitionImpl.GREY());
		sePie2.setExplosion(3);

		SeriesDefinition sdCity = SeriesDefinitionImpl.create();
		sd.getSeriesDefinitions().add(sdCity);
		sdCity.getSeries().add(sePie1);
		sdCity.getSeries().add(sePie2);

		return cwoaPie;
	}

}
