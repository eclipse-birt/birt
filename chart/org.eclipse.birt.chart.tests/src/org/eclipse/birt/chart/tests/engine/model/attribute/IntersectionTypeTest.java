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
import org.eclipse.birt.chart.model.attribute.IntersectionType;

public class IntersectionTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(IntersectionType.MIN, IntersectionType.MIN_LITERAL.getValue());
		assertEquals(IntersectionType.MAX, IntersectionType.MAX_LITERAL.getValue());
		assertEquals(IntersectionType.VALUE, IntersectionType.VALUE_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(IntersectionType.MIN_LITERAL, IntersectionType.get(IntersectionType.MIN));
		assertEquals(IntersectionType.MAX_LITERAL, IntersectionType.get(IntersectionType.MAX));

		assertEquals(IntersectionType.MIN_LITERAL, IntersectionType.get("Min")); //$NON-NLS-1$
		assertEquals(IntersectionType.MAX_LITERAL, IntersectionType.get("Max")); //$NON-NLS-1$
		assertEquals(IntersectionType.VALUE_LITERAL, IntersectionType.get("Value")); //$NON-NLS-1$
		assertNull(IntersectionType.get("No Match")); //$NON-NLS-1$
	}
}
