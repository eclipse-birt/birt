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

import junit.framework.TestCase;
import java.util.List;
import java.util.ArrayList;

/**
 * Base class of rule tests
 * 
 */
abstract public class RuleTestCase extends TestCase
{

	protected RuleDesign rule;

	public RuleTestCase( RuleDesign rl )
	{
		rule = rl;
	}

	/**
	 * Test all get/set accessors in base class
	 * 
	 * set values of the rule
	 * 
	 * then get the values one by one to test if they work correctly
	 */
	public void testBaseRule( )
	{
		String[] operator = {EngineIRConstants.MAP_OPERATOR_ANY,
				EngineIRConstants.MAP_OPERATOR_BETWEEN,
				EngineIRConstants.MAP_OPERATOR_EQ,
				EngineIRConstants.MAP_OPERATOR_FALSE,
				EngineIRConstants.MAP_OPERATOR_GE,
				EngineIRConstants.MAP_OPERATOR_GT,
				EngineIRConstants.MAP_OPERATOR_LE,
				EngineIRConstants.MAP_OPERATOR_LIKE,
				EngineIRConstants.MAP_OPERATOR_LT,
				EngineIRConstants.MAP_OPERATOR_NE,
				EngineIRConstants.MAP_OPERATOR_NOT_BETWEEN,
				EngineIRConstants.MAP_OPERATOR_NOT_NULL,
				EngineIRConstants.MAP_OPERATOR_NULL,
				EngineIRConstants.MAP_OPERATOR_TRUE};

		for ( int i = 0; i < operator.length; i++ )
		{
			//Set
			rule.setExpression( operator[i], "exp1", "exp2" );

			//Get
			assertEquals( rule.getOperator( ), operator[i] );
			assertEquals( rule.getValue1( ), "exp1" );
			assertEquals( rule.getValue2( ), "exp2" );
		}
		
		// special operators
		// IN
		String operator1 = EngineIRConstants.MAP_OPERATOR_IN;
		ArrayList values = new ArrayList();
		values.add("exp1");
		values.add("exp2");
		values.add("exp3");
		values.add("exp4");
		
		rule.setExpression(operator1, values);
		List vs = rule.getValue1List();
		
		assertEquals(rule.getOperator(), operator1);
		assertEquals(values.size(), vs.size());
		assertEquals(values.get(0), vs.get(0));
		assertEquals(values.get(1), vs.get(1));
		assertEquals(values.get(2), vs.get(2));
		assertEquals(values.get(3), vs.get(3));
	}
}
