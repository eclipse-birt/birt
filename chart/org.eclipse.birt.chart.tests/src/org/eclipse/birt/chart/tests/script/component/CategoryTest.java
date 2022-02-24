/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.tests.script.component;

import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.script.api.IChart;
import org.eclipse.birt.chart.script.api.series.data.ISimpleData;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 * 
 */

public class CategoryTest extends BaseChartTestCase {

	public void testGetDataExpr() {
		assertTrue(getChartWithAxes().getCategory().getDataExpr() instanceof ISimpleData);
	}

	public void testGetGrouping() {
		assertNotNull(getChartWithoutAxes().getCategory().getGrouping());
		// Detailed tests are in SeriesGroupingTest
	}

	public void testSorting() {
		assertEquals("Test the default sorting", getChartWithoutAxes().getCategory().getSorting(),
				SortOption.ASCENDING_LITERAL.getName());

		getChartWithoutAxes().getCategory().setSorting(SortOption.DESCENDING_LITERAL.getName());
		assertEquals("Test setting sorting", getChartWithoutAxes().getCategory().getSorting(),
				SortOption.DESCENDING_LITERAL.getName());

		getChartWithoutAxes().getCategory().setSorting("asc");
		assertEquals("Test invalid sorting", getChartWithoutAxes().getCategory().getSorting(),
				SortOption.ASCENDING_LITERAL.getName());

	}

	public void testOptionalValueGroupingExpr() {
		assertEquals(getChartWithoutAxes().getCategory().getOptionalValueGroupingExpr(), "");

		getChartWithoutAxes().getCategory().setOptionalValueGroupingExpr("grouping");
		assertEquals(getChartWithoutAxes().getCategory().getOptionalValueGroupingExpr(), "grouping");

		IChart chart = (IChart) getReportDesign().getReportElement(CHART_NAME_GROUPING);
		assertEquals("row[\"COUNTRY\"]", chart.getCategory().getOptionalValueGroupingExpr());
	}
}
