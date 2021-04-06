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
import org.eclipse.birt.chart.model.attribute.Position;

public class PositionTest extends TestCase {

	public void testConstant() {
		assertEquals(Position.ABOVE, Position.ABOVE_LITERAL.getValue());
		assertEquals(Position.BELOW, Position.BELOW_LITERAL.getValue());
		assertEquals(Position.LEFT, Position.LEFT_LITERAL.getValue());
		assertEquals(Position.RIGHT, Position.RIGHT_LITERAL.getValue());
		assertEquals(Position.INSIDE, Position.INSIDE_LITERAL.getValue());
		assertEquals(Position.OUTSIDE, Position.OUTSIDE_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(Position.ABOVE_LITERAL, Position.get(Position.ABOVE));
		assertEquals(Position.BELOW_LITERAL, Position.get(Position.BELOW));
		assertEquals(Position.LEFT_LITERAL, Position.get(Position.LEFT));
		assertEquals(Position.RIGHT_LITERAL, Position.get(Position.RIGHT));

		assertEquals(Position.ABOVE_LITERAL, Position.get("Above")); //$NON-NLS-1$
		assertEquals(Position.BELOW_LITERAL, Position.get("Below")); //$NON-NLS-1$
		assertEquals(Position.LEFT_LITERAL, Position.get("Left")); //$NON-NLS-1$
		assertEquals(Position.RIGHT_LITERAL, Position.get("Right")); //$NON-NLS-1$
		assertEquals(Position.INSIDE_LITERAL, Position.get("Inside")); //$NON-NLS-1$
		assertEquals(Position.OUTSIDE_LITERAL, Position.get("Outside")); //$NON-NLS-1$
		assertNull(Position.get("No Match")); //$NON-NLS-1$
	}
}
