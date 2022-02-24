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

import org.eclipse.birt.chart.script.api.component.IValueSeries;
import org.eclipse.birt.chart.script.api.series.data.ISimpleData;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IAction;

/**
 * 
 */

public class ValueSeriesTest extends BaseChartTestCase {

	public void testGetDataExpr() {
		assertTrue(getChartWithAxes().getValueSeries()[0][0].getDataExpr() instanceof ISimpleData);
	}

	public void testVisible() {
		assertTrue(getChartWithAxes().getValueSeries()[0][0].isVisible());
		assertTrue(getChartWithAxes().getValueSeries()[1][0].isVisible());

		getChartWithAxes().getValueSeries()[1][0].setVisible(false);
		assertFalse(getChartWithAxes().getValueSeries()[1][0].isVisible());
	}

	public void testTitle() {
		assertEquals(getChartWithAxes().getValueSeries()[0][0].getTitle(), "Date");
		assertEquals(getChartWithAxes().getValueSeries()[1][0].getTitle(), "Number1");
		assertEquals(getChartWithAxes().getValueSeries()[1][1].getTitle(), "");
	}

	public void testPercent() {
		IValueSeries series = getChartWithAxes().getValueSeries()[0][0];
		assertFalse("Percent for Bar", series.isPercent());

		series.setPercent(true);
		assertTrue(series.isPercent());

		series = getChartWithoutAxes().getValueSeries()[0];
		assertFalse("Percent for Bar", series.isPercent());

		series.setPercent(true);
		assertFalse("Pie doesn't support percent", series.isPercent());
	}

	public void testAggregateExpr() {
		assertEquals(getChartWithoutAxes().getValueSeries()[0].getAggregateExpr(), "Sum");
		assertEquals(getChartWithoutAxes().getValueSeries()[1].getAggregateExpr(), "Average");

		assertEquals(getChartWithAxes().getValueSeries()[0][0].getAggregateExpr(), "");
		getChartWithAxes().getValueSeries()[0][0].setAggregateExpr("Sum");
		assertEquals(getChartWithAxes().getValueSeries()[0][0].getAggregateExpr(), "Sum");

		getChartWithAxes().getValueSeries()[0][0].setAggregateExpr(null);
		assertEquals("Unset aggregate expression", getChartWithAxes().getValueSeries()[0][0].getAggregateExpr(), "");
	}

	public void testGetAction() {
		IAction action = getChartWithAxes().getValueSeries()[0][0].getAction();
		assertNotNull(action);
		assertEquals(action.getURI(), "http://www.actuate.com");
		assertEquals(action.getTargetWindow(), "_blank");
		try {
			action.setFormatType("jpg");
		} catch (SemanticException e) {
			e.printStackTrace();
		}
		assertEquals(action.getFormatType(), "jpg");

		action = getChartWithAxes().getValueSeries()[1][0].getAction();
		assertNull(action);
	}
}
