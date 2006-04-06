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
 * @version $Revision: 1.6 $  $Date: 2005/11/11 06:26:41 $
 */
public class VisibilityRuleDesign
{
	/** expression */
	protected String expression;
	/** format */
	protected String format;
	/**
	 * @return Returns the expression.
	 */
	public String getExpression( )
	{
		return expression;
	}
	/**
	 * @param expression The expression to set.
	 */
	public void setExpression( String expression )
	{
		this.expression = expression;
	}
	/**
	 * @return Returns the format.
	 */
	public String getFormat( )
	{
		return format;
	}
	/**
	 * @param format The format to set.
	 */
	public void setFormat( String format )
	{
		this.format = format;
	}
}
