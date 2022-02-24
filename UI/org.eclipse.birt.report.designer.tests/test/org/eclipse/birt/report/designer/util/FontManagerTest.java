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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.swt.graphics.Font;

/**
 *  
 */

public class FontManagerTest extends TestCase {

	/**
	 * Test case for test getFont() method
	 */
	public final void testGetFont() {
		Font font1 = FontManager.getFont(DesignChoiceConstants.FONT_FAMILY_SERIF, 9, 0);
		Font font2 = FontManager.getFont(DesignChoiceConstants.FONT_FAMILY_SERIF, 9, 0);
		assertTrue(font1 == font2);

		font2 = FontManager.getFont(DesignChoiceConstants.FONT_FAMILY_SANS_SERIF, 9, 0);
		assertTrue(!font1.equals(font2));

		font2 = FontManager.getFont(DesignChoiceConstants.FONT_FAMILY_SERIF, 10, 0);
		assertTrue(!font1.equals(font2));

		font2 = FontManager.getFont(DesignChoiceConstants.FONT_FAMILY_SERIF, 9, 1);
		assertTrue(!font1.equals(font2));
	}
}
