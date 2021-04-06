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
import org.eclipse.birt.chart.model.attribute.Stretch;

public class StretchTest extends TestCase {

	public void testConstant() {
		assertEquals(Stretch.HORIZONTAL, Stretch.HORIZONTAL_LITERAL.getValue());
		assertEquals(Stretch.VERTICAL, Stretch.VERTICAL_LITERAL.getValue());
		assertEquals(Stretch.BOTH, Stretch.BOTH_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(Stretch.HORIZONTAL_LITERAL, Stretch.get(Stretch.HORIZONTAL));
		assertEquals(Stretch.VERTICAL_LITERAL, Stretch.get(Stretch.VERTICAL));

		assertEquals(Stretch.HORIZONTAL_LITERAL, Stretch.get("Horizontal")); //$NON-NLS-1$
		assertEquals(Stretch.VERTICAL_LITERAL, Stretch.get("Vertical")); //$NON-NLS-1$
		assertEquals(Stretch.BOTH_LITERAL, Stretch.get("Both")); //$NON-NLS-1$

		assertNull(Stretch.get("No Match")); //$NON-NLS-1$
	}
}