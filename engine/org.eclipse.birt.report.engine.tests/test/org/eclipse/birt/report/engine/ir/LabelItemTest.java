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

package org.eclipse.birt.report.engine.ir;

/**
 * Label test
 * 
 */
public class LabelItemTest extends ReportItemTestCase {

	public LabelItemTest() {
		super(new LabelItemDesign());
	}

	/**
	 * Test all get/set accessors
	 * 
	 * set values of the label
	 * 
	 * then get the values one by one to test if they work correctly
	 */
	public void testAccessor() {

		LabelItemDesign label = new LabelItemDesign();
		ActionDesign action = new ActionDesign();

		// Set
		label.setAction(action);
		String key = "TestKey";
		String text = "TestText";
		label.setText(key, text);

		// Get
		assertEquals(label.getAction(), action);
		assertEquals(label.getText(), text);
		assertEquals(label.getTextKey(), key);
	}

}
