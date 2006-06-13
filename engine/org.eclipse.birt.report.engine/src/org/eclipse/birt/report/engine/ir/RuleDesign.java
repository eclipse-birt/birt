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



/**
 * 
 * @version $Revision: 1.7 $ $Date: 2006/04/26 07:33:48 $
 */
public abstract class RuleDesign
{
	protected String testExpression = null;

	protected String value1;
	protected String value2;
	protected String operator;
	String expr;

	public void setExpression( String operator, String value1, String value2 )
	{
		this.operator = operator;
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public void setConditionExpr(String expr)
	{
		this.expr = expr;
	}
	
	public String getConditionExpr()
	{
		return expr;
	}

	/**
	 * @return Returns the operator.
	 */
	public String getOperator( )
	{
		return operator;
	}

	/**
	 * @return Returns the value1.
	 */
	public String getValue1( )
	{
		return value1;
	}

	/**
	 * @return Returns the value2.
	 */
	public String getValue2( )
	{
		return value2;
	}
	
	/**
	 * @return Returns the testExpression.
	 */
	public String getTestExpression() {
		return testExpression;
	}
	/**
	 * @param testExpression The testExpression to set.
	 */
	public void setTestExpression(String testExpression) {
		this.testExpression = testExpression;
	}
}
