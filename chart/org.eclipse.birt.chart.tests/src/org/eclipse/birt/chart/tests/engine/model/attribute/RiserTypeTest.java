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
import org.eclipse.birt.chart.model.attribute.RiserType;

public class RiserTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(RiserType.RECTANGLE, RiserType.RECTANGLE_LITERAL.getValue());
		assertEquals(RiserType.TRIANGLE, RiserType.TRIANGLE_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(RiserType.RECTANGLE_LITERAL, RiserType.get(RiserType.RECTANGLE));

		assertEquals(RiserType.RECTANGLE_LITERAL, RiserType.get("Rectangle")); //$NON-NLS-1$
		assertEquals(RiserType.TRIANGLE_LITERAL, RiserType.get("Triangle")); //$NON-NLS-1$

		assertNull(RiserType.get("No Match")); //$NON-NLS-1$
	}
}
