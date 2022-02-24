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
import org.eclipse.birt.chart.model.attribute.TickStyle;

public class TickStyleTest extends TestCase {

	public void testConstant() {
		assertEquals(TickStyle.LEFT, 0);
		assertEquals(TickStyle.RIGHT, 1);
		assertEquals(TickStyle.ABOVE, 2);
		assertEquals(TickStyle.BELOW, 3);
		assertEquals(TickStyle.ACROSS, 4);
	}

	public void testGet() {
		assertEquals(TickStyle.LEFT_LITERAL, TickStyle.get(TickStyle.LEFT));
		assertEquals(TickStyle.RIGHT_LITERAL, TickStyle.get(TickStyle.RIGHT));
		assertNull(TickStyle.get("-1")); //$NON-NLS-1$

		assertEquals(TickStyle.LEFT_LITERAL, TickStyle.get("Left")); //$NON-NLS-1$
		assertEquals(TickStyle.RIGHT_LITERAL, TickStyle.get("Right")); //$NON-NLS-1$
		assertEquals(TickStyle.ABOVE_LITERAL, TickStyle.get("Above")); //$NON-NLS-1$
		assertEquals(TickStyle.BELOW_LITERAL, TickStyle.get("Below")); //$NON-NLS-1$
		assertEquals(TickStyle.ACROSS_LITERAL, TickStyle.get("Across")); //$NON-NLS-1$
		assertNull(TickStyle.get("No Match")); //$NON-NLS-1$
	}
}
