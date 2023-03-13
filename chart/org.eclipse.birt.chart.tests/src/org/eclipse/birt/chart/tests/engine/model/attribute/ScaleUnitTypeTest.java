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

import org.eclipse.birt.chart.model.attribute.ScaleUnitType;

import junit.framework.TestCase;

public class ScaleUnitTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(ScaleUnitType.SECONDS, ScaleUnitType.SECONDS_LITERAL.getValue());
		assertEquals(ScaleUnitType.MINUTES, ScaleUnitType.MINUTES_LITERAL.getValue());
		assertEquals(ScaleUnitType.HOURS, ScaleUnitType.HOURS_LITERAL.getValue());
		assertEquals(ScaleUnitType.DAYS, ScaleUnitType.DAYS_LITERAL.getValue());
		assertEquals(ScaleUnitType.WEEKS, ScaleUnitType.WEEKS_LITERAL.getValue());
		assertEquals(ScaleUnitType.MONTHS, ScaleUnitType.MONTHS_LITERAL.getValue());
		assertEquals(ScaleUnitType.YEARS, ScaleUnitType.YEARS_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(ScaleUnitType.SECONDS_LITERAL, ScaleUnitType.get(ScaleUnitType.SECONDS));
		assertEquals(ScaleUnitType.MINUTES_LITERAL, ScaleUnitType.get(ScaleUnitType.MINUTES));
		assertEquals(ScaleUnitType.HOURS_LITERAL, ScaleUnitType.get(ScaleUnitType.HOURS));
		assertEquals(ScaleUnitType.DAYS_LITERAL, ScaleUnitType.get(ScaleUnitType.DAYS));

		assertEquals(ScaleUnitType.SECONDS_LITERAL, ScaleUnitType.get("Seconds")); //$NON-NLS-1$
		assertEquals(ScaleUnitType.MINUTES_LITERAL, ScaleUnitType.get("Minutes")); //$NON-NLS-1$
		assertEquals(ScaleUnitType.HOURS_LITERAL, ScaleUnitType.get("Hours")); //$NON-NLS-1$
		assertEquals(ScaleUnitType.DAYS_LITERAL, ScaleUnitType.get("Days")); //$NON-NLS-1$
		assertEquals(ScaleUnitType.WEEKS_LITERAL, ScaleUnitType.get("Weeks")); //$NON-NLS-1$
		assertEquals(ScaleUnitType.MONTHS_LITERAL, ScaleUnitType.get("Months")); //$NON-NLS-1$
		assertEquals(ScaleUnitType.YEARS_LITERAL, ScaleUnitType.get("Years")); //$NON-NLS-1$
		assertNull(ScaleUnitType.get("No Match")); //$NON-NLS-1$
	}
}
