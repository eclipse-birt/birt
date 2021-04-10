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

import java.util.Random;

import junit.framework.TestCase;

/**
 * Highlight test
 * 
 */
public class HighlightTest extends TestCase {

	/**
	 * Test add/getRule methods
	 * 
	 * add a random list of highlight rules into the highlight element
	 * 
	 * then get the rules one by one to test if they work correctly
	 */
	public void testAddRule() {
		HighlightDesign hightlight = new HighlightDesign();
		HighlightRuleDesign[] rules = new HighlightRuleDesign[(new Random()).nextInt(10) + 1];

		// Add
		for (int i = 0; i < rules.length; i++) {
			rules[i] = new HighlightRuleDesign();
			hightlight.addRule(rules[i]);
		}
		assertEquals(hightlight.getRuleCount(), rules.length);
		for (int i = 0; i < rules.length; i++) {
			assertEquals(hightlight.getRule(i), rules[i]);
		}

	}
}
