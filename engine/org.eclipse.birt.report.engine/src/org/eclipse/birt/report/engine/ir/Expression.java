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

import org.eclipse.birt.core.data.DataType;


/**
 * Expression Type. see report design schema for reference.
 * 
 * @version $Revision: 1.4 $ $Date: 2005/02/21 01:14:42 $
 */
public class Expression
		implements

			org.eclipse.birt.data.engine.api.IScriptExpression
{

	String expression;
	int dataType = DataType.ANY_TYPE;
	Object handle = null;

	public Expression( String expression, int dataType )
	{
		this.expression = expression;
		this.dataType = dataType;
	}

	/**
	 * create an empty expression
	 */
	public Expression( )
	{
	}

	/**
	 * use expr as the expression.
	 * 
	 * @param expr
	 *            expression
	 */
	public Expression( String expr )
	{
		this( expr, DataType.ANY_TYPE );
	}

	/**
	 * get the expression
	 * 
	 * @return expression
	 */
	public String getExpr( )
	{
		return getExpression( );
	}

	/**
	 * set the expression
	 * 
	 * @param expr
	 *            expression
	 */
	public void setExpr( String expr )
	{
		setExpression( expr );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.analyzer.IExpression#getExpression()
	 */
	public String getExpression( )
	{
		return expression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.analyzer.IExpression#getDataType()
	 */
	public int getDataType( )
	{
		return dataType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.analyzer.IBaseExpression#getHandle()
	 */
	public Object getHandle( )
	{
		return handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.analyzer.IBaseExpression#setHandle(java.lang.Object)
	 */
	public void setHandle( Object handle )
	{
		this.handle = handle;
	}

	/**
	 * @param dataType
	 *            The dataType to set.
	 */
	public void setDataType( int dataType )
	{
		this.dataType = dataType;
	}

	/**
	 * @param expression
	 *            The expression to set.
	 */
	public void setExpression( String expression )
	{
		assert ( expression != null );
		this.expression = expression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.analyzer.IBaseExpression#getText()
	 */
	public String getText( )
	{
		return expression;
	}

}