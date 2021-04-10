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
import org.eclipse.birt.chart.model.attribute.Orientation;

public class OrientationTest extends TestCase {

	public void testConstant() {
		assertEquals(Orientation.HORIZONTAL, Orientation.HORIZONTAL_LITERAL.getValue());
		assertEquals(Orientation.VERTICAL, Orientation.VERTICAL_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(Orientation.HORIZONTAL_LITERAL, Orientation.get(Orientation.HORIZONTAL));

		assertEquals(Orientation.HORIZONTAL_LITERAL, Orientation.get("Horizontal")); //$NON-NLS-1$
		assertEquals(Orientation.VERTICAL_LITERAL, Orientation.get("Vertical")); //$NON-NLS-1$

		assertNull(Orientation.get("No Match")); //$NON-NLS-1$
	}
}