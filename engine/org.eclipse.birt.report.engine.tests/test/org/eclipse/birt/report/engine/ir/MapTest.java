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
 *
 */
public class MapTest extends TestCase {

	/**
	 * Test add/getRule methods
	 *
	 * add a random list of map rules into the map element
	 *
	 * then get the rules one by one to test if they work correctly
	 */
	public void testAddRule() {
		MapDesign map = new MapDesign();

		MapRuleDesign[] rules = new MapRuleDesign[(new Random()).nextInt(10) + 1];

		// Add
		for (int i = 0; i < rules.length; i++) {
			rules[i] = new MapRuleDesign();
			map.addRule(rules[i]);
		}

		// Get
		assertEquals(map.getRuleCount(), rules.length);
		for (int i = 0; i < rules.length; i++) {
			assertEquals(map.getRule(i), rules[i]);
		}
	}
}
