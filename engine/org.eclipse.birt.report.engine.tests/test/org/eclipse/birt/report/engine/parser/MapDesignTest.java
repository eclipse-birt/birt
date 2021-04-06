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

package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.MapRuleDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * 
 */
public class MapDesignTest extends AbstractDesignTestCase {

	public void testMapDesign() throws Exception {
		loadDesign("map.xml");

		ReportItemDesign item = report.getContent(0);
		MapDesign map = item.getMap();

		assertEquals(map.getRuleCount(), 3);
		MapRuleDesign rule = map.getRule(0);
		assertTrue(rule != null);
		assertEquals(rule.getOperator(), "lt");
		assertEquals(rule.getValue1().getScriptText(), "row[\"COLUMN_12\"]");
		assertEquals(rule.getValue2(), null);
		assertEquals(rule.getDisplayKey(), "negative");
		assertEquals(rule.getDisplayText(), "NEGATIVE");

		rule = map.getRule(1);
		assertEquals(rule.getOperator(), "is-true");
		assertEquals(rule.getValue1(), null);
		assertEquals(rule.getValue2(), null);
		assertEquals(rule.getDisplayKey(), "true");
		assertEquals(rule.getDisplayText(), "TRUE");

		rule = map.getRule(2);
		assertEquals(rule.getOperator(), "between");
		assertEquals(rule.getValue1().getScriptText(), "row[\"COLUMN_12\"]");
		assertEquals(rule.getValue2().getScriptText(), "row[\"COLUMN_14\"]");
		assertEquals(rule.getDisplayKey(), "valid");
		assertEquals(rule.getDisplayText(), "VALID");
	}

}
