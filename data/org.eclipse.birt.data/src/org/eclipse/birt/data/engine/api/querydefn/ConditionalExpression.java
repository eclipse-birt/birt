/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.api.querydefn;



import org.eclipse.birt.data.engine.api.DataType;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IJSExpression;

/**
 * 
 */
public class ConditionalExpression extends BaseExpression implements IConditionalExpression
{
	protected IJSExpression		expr;
	protected int				operator;
	protected IJSExpression		op1;
	protected IJSExpression 	op2;
	
	public ConditionalExpression( String expr, int operator  )
	{
		this( expr, operator, null, null);
	}
	
	public ConditionalExpression( String expr, int operator, String operand1  )
	{
		this(expr, operator, operand1, null);
	}
	
	public ConditionalExpression( String expr, int operator, String operand1, String operand2 )
	{
		this( newJSExpression(expr), operator,
				newJSExpression(operand1), newJSExpression(operand2) );
	}
	
	public ConditionalExpression(IJSExpression expr, int operator, 
			IJSExpression op1, IJSExpression op2)
	{
		this.expr = expr;
		this.operator = operator;
		this.op1 = op1;
		this.op2 = op2;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IConditionalExpression#getExpression()
	 */
	public IJSExpression getExpression()
	{
		return expr;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IConditionalExpression#getOperator()
	 */
	public int getOperator()
	{
		return operator;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IConditionalExpression#getOperand1()
	 */
	public IJSExpression getOperand1()
	{
		return op1;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IConditionalExpression#getOperand2()
	 */
	public IJSExpression getOperand2()
	{
		return op2;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#getDataType()
	 */
	public int getDataType()
	{
		// Conditional expression are expected to be Boolean type
		return DataType.BOOLEAN_TYPE;
	}
	
	public void setDataType( int dataType )
	{
		if ( dataType != DataType.BOOLEAN_TYPE )
			throw new UnsupportedOperationException("setDataType not supported for conditional expression.");
	}
	
	private static JSExpression newJSExpression(String expr){
		return expr==null?null:new JSExpression(expr);
	}
}
