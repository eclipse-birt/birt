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
