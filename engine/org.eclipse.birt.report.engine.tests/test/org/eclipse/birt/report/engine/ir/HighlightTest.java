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
