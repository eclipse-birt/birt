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

package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.ir.HighlightDesign;
import org.eclipse.birt.report.engine.ir.HighlightRuleDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * 
 */
public class HighlightTest extends AbstractDesignTestCase {

	public void testHighlightDesign() throws Exception {
		loadDesign("highlight.xml");

		ReportItemDesign item = report.getContent(0);
		HighlightDesign highlight = item.getHighlight();
		assertTrue(highlight != null);

		assertEquals(highlight.getRuleCount(), 3);
		HighlightRuleDesign rule = highlight.getRule(0);
		assertTrue(rule != null);
		assertEquals(rule.getOperator(), "lt");
		assertEquals(rule.getValue1().getScriptText(), "row[\"COLUMN_29\"]");
		assertEquals(rule.getValue2(), null);
		assertEquals(rule.getStyle().getColor(), "red");

		rule = highlight.getRule(1);
		assertEquals(rule.getOperator(), "is-true");
		assertEquals(rule.getValue1(), null);
		assertEquals(rule.getValue2(), null);
		assertEquals(rule.getStyle().getColor(), "yellow");

		rule = highlight.getRule(2);
		assertEquals(rule.getOperator(), "between");
		assertEquals(rule.getValue1().getScriptText(), "row[\"COLUMN_29\"]");
		assertEquals(rule.getValue2().getScriptText(), "row[\"COLUMN_31\"]");
		assertEquals(rule.getStyle().getColor(), "blue");
	}

}
