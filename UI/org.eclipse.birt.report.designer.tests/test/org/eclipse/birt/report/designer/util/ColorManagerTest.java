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

package org.eclipse.birt.report.designer.util;

import junit.framework.TestCase;

import org.eclipse.swt.graphics.Color;

/**
 *  
 */

public class ColorManagerTest extends TestCase {

	/**
	 * Test case for test getColor() method
	 */
	public final void testGetColor() {
		Color color1 = ColorManager.getColor(0);
		Color color2 = ColorManager.getColor(0);
		assertTrue(color1 == color2);

		color2 = ColorManager.getColor(255);
		assertTrue(!color1.equals(color2));
	}
}