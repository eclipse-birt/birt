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
 *  Visibility Rule Design
 * 
 * 
 * @version $Revision: #1 $  $Date: 2005/01/21 $
 */
public class VisibilityRuleDesign
{
	/** expression */
	protected Expression expression;
	/** format */
	protected int format;
	/**
	 * @return Returns the expression.
	 */
	public Expression getExpression( )
	{
		return expression;
	}
	/**
	 * @param expression The expression to set.
	 */
	public void setExpression( Expression expression )
	{
		this.expression = expression;
	}
	/**
	 * @return Returns the format.
	 */
	public int getFormat( )
	{
		return format;
	}
	/**
	 * @param format The format to set.
	 */
	public void setFormat( int format )
	{
		this.format = format;
	}
}
