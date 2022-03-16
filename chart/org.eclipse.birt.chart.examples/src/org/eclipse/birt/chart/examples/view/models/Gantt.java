/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.view.models;

import org.eclipse.birt.chart.extension.datafeed.GanttEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.DateFormatType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.GanttDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.GanttDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl;
import org.eclipse.birt.chart.util.CDateTime;

/**
 *
 */

public class Gantt {

	public final static Chart createGantt() {
		ChartWithAxes cwaGantt = ChartWithAxesImpl.create();
		cwaGantt.setType("Gantt Chart"); //$NON-NLS-1$
		cwaGantt.setSubType("Standard Gantt Chart"); //$NON-NLS-1$
		cwaGantt.setOrientation(Orientation.HORIZONTAL_LITERAL);
		// Plot
		cwaGantt.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaGantt.getBlock().getOutline().setVisible(true);
		Plot p = cwaGantt.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwaGantt.getTitle().getLabel().getCaption().setValue("Gantt Chart"); //$NON-NLS-1$

		// Legend
		Legend lg = cwaGantt.getLegend();
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaGantt.getPrimaryBaseAxes()[0];
		xAxisPrimary.setCategoryAxis(true);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaGantt.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getScale().setMin(DateTimeDataElementImpl.create(new CDateTime(2008, 1, 1)));
		yAxisPrimary.getScale().setMax(DateTimeDataElementImpl.create(new CDateTime(2009, 1, 1)));
		DateFormatSpecifier dfs = AttributeFactory.eINSTANCE.createDateFormatSpecifier();
		dfs.setDetail(DateFormatDetail.DATE_LITERAL);
		dfs.setType(DateFormatType.MEDIUM_LITERAL);
		yAxisPrimary.setFormatSpecifier(dfs);

		// Data Set
		NumberDataSet categoryValues = NumberDataSetImpl.create(new double[] { 1, 2, 3 });
		GanttDataSet phase1 = GanttDataSetImpl.create(
				new GanttEntry[] { new GanttEntry(new CDateTime(2008, 1, 9), new CDateTime(2008, 6, 9), "Task A"), //$NON-NLS-1$
						new GanttEntry(new CDateTime(2008, 3, 9), new CDateTime(2008, 8, 9), "Task B"), //$NON-NLS-1$
						new GanttEntry(new CDateTime(2008, 5, 24), new CDateTime(2008, 9, 9), "Task C") //$NON-NLS-1$
				});
		GanttDataSet phase2 = GanttDataSetImpl.create(
				new GanttEntry[] { new GanttEntry(new CDateTime(2008, 6, 12), new CDateTime(2008, 9, 23), "Task A"), //$NON-NLS-1$
						new GanttEntry(new CDateTime(2008, 8, 9), new CDateTime(2008, 8, 9), "Task B"), //$NON-NLS-1$
						new GanttEntry(new CDateTime(2008, 9, 9), new CDateTime(2008, 12, 9), "Task C") //$NON-NLS-1$
				});
		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal1);

		cwaGantt.setSampleData(sd);

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		GanttSeries taskPhase1 = (GanttSeries) GanttSeriesImpl.create();
		taskPhase1.setDataSet(phase1);
		taskPhase1.getLabel().setVisible(false);

		GanttSeries taskPhase2 = (GanttSeries) GanttSeriesImpl.create();
		taskPhase2.setDataSet(phase2);
		taskPhase2.getLabel().setVisible(false);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(0);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(taskPhase1);
		sdY.getSeries().add(taskPhase2);

		return cwaGantt;
	}
}
