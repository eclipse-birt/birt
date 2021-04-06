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
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;

public class HorizontalAlignmentTest extends TestCase {

	public void testConstant() {
		assertEquals(HorizontalAlignment.LEFT, HorizontalAlignment.LEFT_LITERAL.getValue());
		assertEquals(HorizontalAlignment.CENTER, HorizontalAlignment.CENTER_LITERAL.getValue());
		assertEquals(HorizontalAlignment.RIGHT, HorizontalAlignment.RIGHT_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(HorizontalAlignment.LEFT_LITERAL, HorizontalAlignment.get(HorizontalAlignment.LEFT));

		assertEquals(HorizontalAlignment.LEFT_LITERAL, HorizontalAlignment.get("Left")); //$NON-NLS-1$
		assertEquals(HorizontalAlignment.CENTER_LITERAL, HorizontalAlignment.get("Center")); //$NON-NLS-1$
		assertEquals(HorizontalAlignment.RIGHT_LITERAL, HorizontalAlignment.get("Right")); //$NON-NLS-1$
		assertNull(HorizontalAlignment.get("No Match")); //$NON-NLS-1$
	}
}
