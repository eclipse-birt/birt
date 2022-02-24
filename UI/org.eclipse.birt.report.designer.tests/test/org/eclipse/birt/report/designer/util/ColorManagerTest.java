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
