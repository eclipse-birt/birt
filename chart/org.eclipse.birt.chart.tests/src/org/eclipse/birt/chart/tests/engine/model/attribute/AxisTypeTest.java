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

import org.eclipse.birt.chart.model.attribute.AxisType;

import junit.framework.TestCase;

public class AxisTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(AxisType.LINEAR, AxisType.LINEAR_LITERAL.getValue());
		assertEquals(AxisType.LOGARITHMIC, AxisType.LOGARITHMIC_LITERAL.getValue());
		assertEquals(AxisType.TEXT, AxisType.TEXT_LITERAL.getValue());
		assertEquals(AxisType.DATE_TIME, AxisType.DATE_TIME_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(AxisType.LINEAR_LITERAL, AxisType.get(AxisType.LINEAR));
		assertEquals(AxisType.LOGARITHMIC_LITERAL, AxisType.get(AxisType.LOGARITHMIC));

		assertEquals(AxisType.LINEAR_LITERAL, AxisType.get("Linear")); //$NON-NLS-1$
		assertEquals(AxisType.LOGARITHMIC_LITERAL, AxisType.get("Logarithmic")); //$NON-NLS-1$
		assertEquals(AxisType.TEXT_LITERAL, AxisType.get("Text")); //$NON-NLS-1$
		assertEquals(AxisType.DATE_TIME_LITERAL, AxisType.get("DateTime")); //$NON-NLS-1$
		assertNull(AxisType.get("No Match")); //$NON-NLS-1$

	}
}
