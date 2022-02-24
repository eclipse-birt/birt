/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.tests.chart.api.script;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Test decription:
 * </p>
 * Chart script: afterDataSetFilled()
 * </p>
 */

public class AfterDataSetFilled extends ChartTestCase {

	/**
	 * A chart model instance
	 */
	private Chart cm = null;

	/**
	 * execute application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new AfterDataSetFilled();
	}

	/**
	 * Constructor
	 */
	public AfterDataSetFilled() {
		cm = createChart();
		bindGroupingData(cm);
	}

	private void bindGroupingData(Chart chart)

	{

		// Data Set
		final Object[][] data = new Object[][] { { "x1", new Integer(1), "g1" }, { "x2", new Integer(2), "g2" },
				{ "x3", new Integer(3), "g1" }, { "x4", new Integer(4), "g3" }, { "x5", new Integer(5), "g2" },
				{ "x6", new Integer(6), "g1" }, { "x7", new Integer(7), "g3" }, { "x8", new Integer(8), "g2" },
				{ "x9", new Integer(9), "g2" }, { "x0", new Integer(0), "g2" }, };
		try {
			Generator gr = Generator.instance();
			gr.bindData(new IDataRowExpressionEvaluator() {

				int idx = 0;

				public void close() {
				}

				public Object evaluate(String expression) {
					if ("X".equals(expression)) {
						return data[idx][0];
					} else if ("Y".equals(expression)) {
						return data[idx][1];
					} else if ("G".equals(expression)) {
						return data[idx][2];
					}
					return null;
				}

				public Object evaluateGlobal(String expression) {
					return evaluate(expression);
				}

				public boolean first() {
					idx = 0;
					return true;
				}

				public boolean next() {
					idx++;
					return (idx < 9);
				}
			}, chart, new RunTimeContext());
		} catch (ChartException e) {
			e.printStackTrace();
		}
	}

	private Chart createChart()

	{
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();

		cwaBar.setScript("function afterDataSetFilled( series, idsp,Iicsc )" //$NON-NLS-1$
				+ "{importPackage(Packages.java.lang); " //$NON-NLS-1$
				+ "if (series.getLabel().isVisible() == true)" //$NON-NLS-1$
				+ "{System.out.println(\"ok\");} " //$NON-NLS-1$
				+ "else" //$NON-NLS-1$
				+ "{System.out.println(\"false\");}} " //$NON-NLS-1$
		);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

		// X-Series
		Series seCategory = SeriesImpl.create();
		Query xQ = QueryImpl.create("G");
		seCategory.getDataDefinition().add(xQ);
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// -------------------------------------------------------------

		sdX.setSorting(SortOption.ASCENDING_LITERAL);
		sdX.getGrouping().setEnabled(true);
		sdX.getGrouping().setGroupType(DataType.TEXT_LITERAL);
		sdX.getGrouping().setAggregateExpression("Sum");
		sdX.getGrouping().setGroupingInterval(0);

		// -------------------------------------------------------------

		// Y-Series
		LineSeries bs = (LineSeries) LineSeriesImpl.create();
		bs.getLabel().setVisible(true);
		Query yQ = QueryImpl.create("Y");
		bs.getDataDefinition().add(yQ);
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(0);
		sdY.getSeries().add(bs);

		return cwaBar;

	}
}
