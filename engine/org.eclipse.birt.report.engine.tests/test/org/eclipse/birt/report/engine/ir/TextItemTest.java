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

package org.eclipse.birt.report.engine.ir;

/**
 * Text element test
 * 
 */
public class TextItemTest extends ReportItemTestCase {

	public TextItemTest() {
		super(new TextItemDesign());
	}

	/**
	 * Test get/setStaticText methods
	 * 
	 * set a static text
	 * 
	 * then get the texts and check the text type to test if they work correctly
	 */
	public void testStaticText() {
		TextItemDesign text = new TextItemDesign();
		// Set
		String testKey = "TestKey";
		String testText = "TestText";
		text.setText(testKey, testText);

		// Get
		assertEquals(text.getText(), testText);
		assertEquals(text.getTextKey(), testKey);
	}

}
