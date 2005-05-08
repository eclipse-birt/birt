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

import org.eclipse.birt.data.engine.api.IConditionalExpression;


/**
 * 
 * @version $Revision: 1.4 $ $Date: 2005/05/08 06:08:26 $
 */
public abstract class RuleDesign
{

	protected String value1;
	protected String value2;
	protected String operator;
	IConditionalExpression expr;

	public void setExpression( String operator, String value1, String value2 )
	{
		this.operator = operator;
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public void setConditionExpr(IConditionalExpression expr)
	{
		this.expr = expr;
	}
	
	public IConditionalExpression getConditionExpr()
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
}
