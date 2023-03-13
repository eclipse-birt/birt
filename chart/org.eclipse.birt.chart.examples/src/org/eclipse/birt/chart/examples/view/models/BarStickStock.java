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

import org.eclipse.birt.chart.extension.datafeed.StockEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.StockDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;

public class BarStickStock {

	public final static Chart createBarStickStock() {
		ChartWithAxes cwaStock = ChartWithAxesImpl.create();
		cwaStock.setType("Stock Chart"); //$NON-NLS-1$
		cwaStock.setSubType("Bar Stick Stock Chart"); //$NON-NLS-1$

		// Title
		cwaStock.getTitle().getLabel().getCaption().setValue("Bar-stick Stock Chart");//$NON-NLS-1$
		cwaStock.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Legend
		cwaStock.getLegend().setVisible(false);

		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl) cwaStock).getPrimaryBaseAxes()[0];

		xAxisPrimary.getLabel().getCaption().setColor(ColorDefinitionImpl.RED());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(30);
		xAxisPrimary.setLabelPosition(Position.ABOVE_LITERAL);

		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(255, 196, 196));
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.setFormatSpecifier(JavaDateFormatSpecifierImpl.create("MM/dd/yyyy"));//$NON-NLS-1$
		xAxisPrimary.setCategoryAxis(true);

		// Y-Axis
		Axis yAxisPrimary = ((ChartWithAxesImpl) cwaStock).getPrimaryOrthogonalAxis(xAxisPrimary);

		yAxisPrimary.getScale().setMin(NumberDataElementImpl.create(24));
		yAxisPrimary.getScale().setMax(NumberDataElementImpl.create(29));
		yAxisPrimary.getScale().setStep(0.5);

		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.create(196, 196, 255));
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);

		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Data Set
		DateTimeDataSet dsDateValues = DateTimeDataSetImpl
				.create(new Calendar[] { new CDateTime(2004, 12, 27), new CDateTime(2004, 12, 23),
						new CDateTime(2004, 12, 22), new CDateTime(2004, 12, 21), new CDateTime(2004, 12, 20),
						new CDateTime(2004, 12, 17), new CDateTime(2004, 12, 16), new CDateTime(2004, 12, 15) });

		StockDataSet dsStockValues = StockDataSetImpl.create(new StockEntry[] {
				new StockEntry(27.01, 27.00, 28.42, 27.85), new StockEntry(26.87, 25.15, 27.83, 27.01),
				new StockEntry(26.84, 26.00, 27.78, 26.97), new StockEntry(27.00, 25.17, 27.94, 27.07),
				new StockEntry(26.01, 25.15, 28.39, 26.95), new StockEntry(27.00, 24.76, 27.80, 26.96),
				new StockEntry(27.15, 25.28, 28.01, 27.16), new StockEntry(27.22, 24.80, 28.07, 27.11), });

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);

		cwaStock.setSampleData(sd);

		// X-Series
		Series seBase = SeriesImpl.create();
		seBase.setDataSet(dsDateValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(-1);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seBase);

		// Y-Series
		StockSeries ss = (StockSeries) StockSeriesImpl.create();
		ss.setShowAsBarStick(true);
		ss.getLineAttributes().setColor(ColorDefinitionImpl.BLUE());
		ss.setDataSet(dsStockValues);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(ColorDefinitionImpl.CYAN());
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ss);

		return cwaStock;
	}

}
