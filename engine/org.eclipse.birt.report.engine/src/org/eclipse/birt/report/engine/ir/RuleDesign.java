/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

import java.util.List;


/**
 * 
 */
public abstract class RuleDesign
{
	protected Expression<String> testExpression = null;

	protected Expression<String> value1;
	protected Expression<String> value2;
	protected String operator;
	protected Object expr;
	protected boolean valueIsList = false;
	protected Expression<? extends List> value1List;

	public void setExpression( String operator, Expression<? extends List> value )
	{
		this.operator = operator;
		this.value1List = value;
		this.valueIsList = true;
	}

	public Expression<? extends List> getValue1List( )
	{
		return this.value1List;
	}

	public boolean ifValueIsList( )
	{
		return this.valueIsList;
	}

	public void setValueIsList( boolean valueIsList )
	{
		this.valueIsList = valueIsList;
	}

	public void setExpression( String operator, Expression<String> value1,
			Expression<String> value2 )
	{
		this.operator = operator;
		this.value1 = value1;
		this.value2 = value2;
		this.valueIsList = false;
	}
	
	public void setConditionExpr( Object expr )
	{
		this.expr = expr;
	}

	public Object getConditionExpr( )
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
	public Expression<String> getValue1( )
	{
		return value1;
	}

	/**
	 * @return Returns the value2.
	 */
	public Expression<String> getValue2( )
	{
		return value2;
	}
	
	/**
	 * @return Returns the testExpression.
	 */
	public Expression<String> getTestExpression() {
		return testExpression;
	}
	/**
	 * @param testExpression The testExpression to set.
	 */
	public void setTestExpression(Expression<String> testExpression) {
		this.testExpression = testExpression;
	}
}
