/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.tests.script.data;

import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.script.api.data.ISeriesGrouping;
import org.eclipse.birt.chart.tests.script.BaseChartTestCase;

/**
 * 
 */

public class SeriesGroupingTest extends BaseChartTestCase {

	public void testGroupInterval() {
		ISeriesGrouping grouping = getChartWithoutAxes().getCategory().getGrouping();

		assertEquals(grouping.getGroupInterval(), 0, 0.00001f);

		grouping.setGroupInterval(1);
		assertEquals(grouping.getGroupInterval(), 1, 0.00001f);
	}

	public void testGroupType() {
		ISeriesGrouping grouping = getChartWithoutAxes().getCategory().getGrouping();

		assertEquals(grouping.getGroupType(), DataType.TEXT_LITERAL.getLiteral());

		grouping.setGroupType(DataType.NUMERIC_LITERAL.getLiteral());
		assertEquals("Test setting group type", //$NON-NLS-1$
				grouping.getGroupType(), DataType.NUMERIC_LITERAL.getLiteral());

		grouping.setGroupType("Num");//$NON-NLS-1$
		assertEquals("Test invalid group type", //$NON-NLS-1$
				grouping.getGroupType(), DataType.TEXT_LITERAL.getLiteral());
	}

	public void testGroupUnit() {
		ISeriesGrouping grouping = getChartWithoutAxes().getCategory().getGrouping();

		assertEquals(grouping.getGroupUnit(), GroupingUnitType.DAYS_LITERAL.getLiteral());

		grouping.setGroupUnit(GroupingUnitType.SECONDS_LITERAL.getLiteral());
		assertEquals("Test setting group unit", //$NON-NLS-1$
				grouping.getGroupUnit(), GroupingUnitType.SECONDS_LITERAL.getLiteral());

		grouping.setGroupUnit("dd");//$NON-NLS-1$
		assertEquals("Test invalid group unit", //$NON-NLS-1$
				grouping.getGroupUnit(), GroupingUnitType.DAYS_LITERAL.getLiteral());
	}

	public void testEnabled() {
		ISeriesGrouping grouping = getChartWithoutAxes().getCategory().getGrouping();

		assertEquals(grouping.isEnabled(), true);

		grouping.setEnabled(false);
		assertEquals(grouping.isEnabled(), false);
	}
}
