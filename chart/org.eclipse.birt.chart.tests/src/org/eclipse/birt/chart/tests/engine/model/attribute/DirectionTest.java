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
import org.eclipse.birt.chart.model.attribute.Direction;

public class DirectionTest extends TestCase {

	public void testConstant() {
		assertEquals(Direction.LEFT_RIGHT, Direction.LEFT_RIGHT_LITERAL.getValue());
		assertEquals(Direction.TOP_BOTTOM, Direction.TOP_BOTTOM_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(Direction.LEFT_RIGHT_LITERAL, Direction.get(Direction.LEFT_RIGHT));

		assertEquals(Direction.LEFT_RIGHT_LITERAL, Direction.get("Left_Right")); //$NON-NLS-1$
		assertEquals(Direction.TOP_BOTTOM_LITERAL, Direction.get("Top_Bottom")); //$NON-NLS-1$

		assertNull(Direction.get("No Match")); //$NON-NLS-1$
	}
}
