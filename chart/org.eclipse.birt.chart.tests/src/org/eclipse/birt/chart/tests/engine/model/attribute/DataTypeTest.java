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

import org.eclipse.birt.chart.model.attribute.DataType;

import junit.framework.TestCase;

public class DataTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(DataType.NUMERIC, DataType.NUMERIC_LITERAL.getValue());
		assertEquals(DataType.DATE_TIME, DataType.DATE_TIME_LITERAL.getValue());
		assertEquals(DataType.TEXT, DataType.TEXT_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(DataType.NUMERIC_LITERAL, DataType.get(DataType.NUMERIC));
		assertEquals(DataType.TEXT_LITERAL, DataType.get(DataType.TEXT));

		assertEquals(DataType.NUMERIC_LITERAL, DataType.get("Numeric")); //$NON-NLS-1$
		assertEquals(DataType.DATE_TIME_LITERAL, DataType.get("DateTime")); //$NON-NLS-1$
		assertEquals(DataType.TEXT_LITERAL, DataType.get("Text")); //$NON-NLS-1$

		assertNull(DataType.get("No Match")); //$NON-NLS-1$
	}
}
