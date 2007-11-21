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
	protected String testExpression = null;

	protected String value1;
	protected String value2;
	protected String operator;
	Object expr;
	protected boolean valueIsList = false;
	protected List value1List;

	public void setExpression( String operator, List value )
	{
		this.operator = operator;
		this.value1List = value;
		this.valueIsList = true;
	}

	public List getValue1List( )
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

	public void setExpression( String operator, String value1, String value2 )
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
