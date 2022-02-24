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

import java.util.ArrayList;

import org.eclipse.birt.report.engine.EngineCase;

/**
 * Base class of rule tests
 *
 */
abstract public class RuleTestCase extends EngineCase {

	protected RuleDesign rule;

	public RuleTestCase(RuleDesign rl) {
		rule = rl;
	}

	/**
	 * Test all get/set accessors in base class
	 *
	 * set values of the rule
	 *
	 * then get the values one by one to test if they work correctly
	 */
	public void testBaseRule() {
		String[] operator = { EngineIRConstants.MAP_OPERATOR_ANY, EngineIRConstants.MAP_OPERATOR_BETWEEN,
				EngineIRConstants.MAP_OPERATOR_EQ, EngineIRConstants.MAP_OPERATOR_FALSE,
				EngineIRConstants.MAP_OPERATOR_GE, EngineIRConstants.MAP_OPERATOR_GT, EngineIRConstants.MAP_OPERATOR_LE,
				EngineIRConstants.MAP_OPERATOR_LIKE, EngineIRConstants.MAP_OPERATOR_LT,
				EngineIRConstants.MAP_OPERATOR_NE, EngineIRConstants.MAP_OPERATOR_NOT_BETWEEN,
				EngineIRConstants.MAP_OPERATOR_NOT_NULL, EngineIRConstants.MAP_OPERATOR_NULL,
				EngineIRConstants.MAP_OPERATOR_TRUE };

		Expression exp1 = Expression.newConstant("exp1");
		Expression exp2 = Expression.newConstant("exp2");
		for (int i = 0; i < operator.length; i++) {
			// Set
			rule.setExpression(operator[i], exp1, exp2);

			// Get
			assertFalse(rule.ifValueIsList());
			assertEquals(rule.getOperator(), operator[i]);
			assertEquals(rule.getValue1(), exp1);
			assertEquals(rule.getValue2(), exp2);
		}

		// special operators
		// IN
		String operator1 = EngineIRConstants.MAP_OPERATOR_IN;
		ArrayList<Expression> values = new ArrayList<>();
		values.add(Expression.newScript("exp1"));
		values.add(Expression.newScript("exp2"));
		values.add(Expression.newScript("exp3"));
		values.add(Expression.newScript("exp4"));

		rule.setExpression(operator1, values);

		assertTrue(rule.ifValueIsList());
		assertEquals(rule.getOperator(), operator1);
		assertEquals(values, rule.getValue1List());
	}
}
