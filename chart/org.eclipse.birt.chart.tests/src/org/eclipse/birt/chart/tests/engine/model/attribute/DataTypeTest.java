/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.chart.tests.engine.model.attribute;

import junit.framework.TestCase;
import org.eclipse.birt.chart.model.attribute.DataType;

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
