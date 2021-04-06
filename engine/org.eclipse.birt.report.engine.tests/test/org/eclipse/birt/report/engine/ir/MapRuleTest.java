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
 * 
 */
public class MapRuleTest extends RuleTestCase {

	public MapRuleTest() {
		super(new MapRuleDesign());
	}

	/**
	 * Test get/setDisplayText methods
	 * 
	 * set the texts
	 * 
	 * then get them to test if they work correctly
	 */
	public void testAccessor() {
		// Set
		String key = "Key";
		String text = "Test";
		((MapRuleDesign) rule).setDisplayText(key, text);

		// Get
		assertEquals(((MapRuleDesign) rule).getDisplayKey(), key);
		assertEquals(((MapRuleDesign) rule).getDisplayText(), text);
	}

}
