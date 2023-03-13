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
