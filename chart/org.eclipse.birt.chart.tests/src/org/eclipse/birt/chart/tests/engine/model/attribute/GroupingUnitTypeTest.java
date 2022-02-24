/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.chart.tests.engine.model.attribute;

import junit.framework.TestCase;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;

public class GroupingUnitTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(GroupingUnitType.SECONDS, GroupingUnitType.SECONDS_LITERAL.getValue());
		assertEquals(GroupingUnitType.MINUTES, GroupingUnitType.MINUTES_LITERAL.getValue());
		assertEquals(GroupingUnitType.HOURS, GroupingUnitType.HOURS_LITERAL.getValue());
		assertEquals(GroupingUnitType.DAYS, GroupingUnitType.DAYS_LITERAL.getValue());
		assertEquals(GroupingUnitType.WEEKS, GroupingUnitType.WEEKS_LITERAL.getValue());
		assertEquals(GroupingUnitType.MONTHS, GroupingUnitType.MONTHS_LITERAL.getValue());
		assertEquals(GroupingUnitType.YEARS, GroupingUnitType.YEARS_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(GroupingUnitType.SECONDS_LITERAL, GroupingUnitType.get(GroupingUnitType.SECONDS));
		assertEquals(GroupingUnitType.MINUTES_LITERAL, GroupingUnitType.get(GroupingUnitType.MINUTES));
		assertEquals(GroupingUnitType.HOURS_LITERAL, GroupingUnitType.get(GroupingUnitType.HOURS));
		assertEquals(GroupingUnitType.DAYS_LITERAL, GroupingUnitType.get(GroupingUnitType.DAYS));

		assertEquals(GroupingUnitType.SECONDS_LITERAL, GroupingUnitType.get("Seconds")); //$NON-NLS-1$
		assertEquals(GroupingUnitType.MINUTES_LITERAL, GroupingUnitType.get("Minutes")); //$NON-NLS-1$
		assertEquals(GroupingUnitType.HOURS_LITERAL, GroupingUnitType.get("Hours")); //$NON-NLS-1$
		assertEquals(GroupingUnitType.DAYS_LITERAL, GroupingUnitType.get("Days")); //$NON-NLS-1$
		assertEquals(GroupingUnitType.WEEKS_LITERAL, GroupingUnitType.get("Weeks")); //$NON-NLS-1$
		assertEquals(GroupingUnitType.MONTHS_LITERAL, GroupingUnitType.get("Months")); //$NON-NLS-1$
		assertEquals(GroupingUnitType.YEARS_LITERAL, GroupingUnitType.get("Years")); //$NON-NLS-1$
		assertNull(GroupingUnitType.get("No Match")); //$NON-NLS-1$
	}
}
