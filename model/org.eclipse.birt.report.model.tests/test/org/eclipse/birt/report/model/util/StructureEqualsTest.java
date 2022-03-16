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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;

/**
 * Tests for the structure equals.
 */

public class StructureEqualsTest extends BaseTestCase {

	/**
	 * Tests the equals of single structure.
	 *
	 * @throws Exception
	 */

	public void testSingleStructure() {
		String expr = "true"; //$NON-NLS-1$
		HideRule rule = createHideRule(DesignChoiceConstants.FORMAT_TYPE_ALL, expr);
		assertTrue(rule.equals(rule));
		assertTrue(rule.equals(rule.copy()));
		assertFalse(rule.equals(new ConfigVariable()));
		assertFalse(rule.equals(null));

		// compare two hide rules

		HideRule ruleOne = createHideRule(DesignChoiceConstants.FORMAT_TYPE_EXCEL, expr);
		assertFalse(rule.equals(ruleOne));
		ruleOne.setFormat(DesignChoiceConstants.FORMAT_TYPE_ALL);
		assertTrue(rule.equals(ruleOne));
		assertTrue(ruleOne.equals(rule));
	}

	/**
	 * Tests the equals of structure list.
	 *
	 */

	public void testStructureList() {
		String expr = "true"; //$NON-NLS-1$
		List rules = new ArrayList();
		List ruleOnes = new ArrayList();
		assertTrue(rules.equals(ruleOnes));

		// size not same, then equals false

		rules.add(createHideRule(DesignChoiceConstants.FORMAT_TYPE_ALL, expr));
		assertFalse(rules.equals(ruleOnes));
		assertFalse(ruleOnes.equals(rules));

		// contains one rule, and equals
		ruleOnes.add(createHideRule(DesignChoiceConstants.FORMAT_TYPE_ALL, expr));
		assertTrue(rules.equals(ruleOnes));
		assertTrue(ruleOnes.equals(rules));

		// contains two rules

		rules.add(createHideRule(DesignChoiceConstants.FORMAT_TYPE_EMAIL, expr));
		ruleOnes.add(createHideRule(DesignChoiceConstants.FORMAT_TYPE_ALL, expr));
		assertFalse(rules.equals(ruleOnes));
		assertFalse(ruleOnes.equals(rules));

		((HideRule) ruleOnes.get(1)).setFormat(DesignChoiceConstants.FORMAT_TYPE_EMAIL);
		assertTrue(rules.equals(ruleOnes));
		assertTrue(ruleOnes.equals(rules));

		// contains three rules

		rules.add(createHideRule(DesignChoiceConstants.FORMAT_TYPE_EXCEL, expr));
		ruleOnes.add(createHideRule(DesignChoiceConstants.FORMAT_TYPE_ALL, expr));
		assertFalse(rules.equals(ruleOnes));
		assertFalse(ruleOnes.equals(rules));

		((HideRule) ruleOnes.get(2)).setFormat(DesignChoiceConstants.FORMAT_TYPE_EXCEL);
		assertTrue(rules.equals(ruleOnes));
		assertTrue(ruleOnes.equals(rules));

	}

	/**
	 * Creates a hide rule with the format and expression.
	 *
	 * @param format
	 * @param expr
	 * @return
	 */

	private HideRule createHideRule(String format, String expr) {
		HideRule rule = new HideRule();
		rule.setFormat(format);
		rule.setExpression(expr);
		return rule;
	}
}
