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
import org.eclipse.birt.chart.model.attribute.DateFormatType;

public class DateFormatTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(DateFormatType.LONG, DateFormatType.LONG_LITERAL.getValue());
		assertEquals(DateFormatType.SHORT, DateFormatType.SHORT_LITERAL.getValue());
		assertEquals(DateFormatType.MEDIUM, DateFormatType.MEDIUM_LITERAL.getValue());
		assertEquals(DateFormatType.FULL, DateFormatType.FULL_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(DateFormatType.MEDIUM_LITERAL, DateFormatType.get(DateFormatType.MEDIUM));
		assertEquals(DateFormatType.FULL_LITERAL, DateFormatType.get(DateFormatType.FULL));

		assertEquals(DateFormatType.LONG_LITERAL, DateFormatType.get("Long")); //$NON-NLS-1$
		assertEquals(DateFormatType.SHORT_LITERAL, DateFormatType.get("Short")); //$NON-NLS-1$
		assertEquals(DateFormatType.MEDIUM_LITERAL, DateFormatType.get("Medium")); //$NON-NLS-1$
		assertEquals(DateFormatType.FULL_LITERAL, DateFormatType.get("Full")); //$NON-NLS-1$

		assertNull(DateFormatType.get("No Match")); //$NON-NLS-1$
	}
}
