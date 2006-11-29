/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.model.api.core.IStructure;

/**
 * Represents the design of an HighLightRule in the scripting environment
 * 
 */
public interface IHighlightRule
{

	/**
	 * Returns Test Expression
	 * 
	 * @return Test Expression
	 */

	public String getTestExpression( );

	/**
	 * Sets Test Expression
	 * 
	 * @param expression
	 * @throws ScriptException
	 */

	public void setTestExpression( String expression );

	/**
	 * Returns font style
	 * 
	 * @return font style
	 */

	public String getFontStyle( );

	/**
	 * Sets font style
	 * 
	 * @param style
	 * @throws ScriptException
	 */

	public void setFontStyle( String style );

	/**
	 * Returns font weight.
	 * 
	 * @return font weight.
	 */

	public String getFontWeight( );

	/**
	 * Sets font weight.
	 * 
	 * @param weight
	 * @throws ScriptException
	 */

	public void setFontWeight( String weight );

	/**
	 * Return date time format.
	 * 
	 * @return date time format.
	 */

	public String getDateTimeFormat( );

	/**
	 * Sets date time format
	 * 
	 * @param format
	 * @throws ScriptException
	 */

	public void setDateTimeFormat( String format );

	/**
	 * Returns string format
	 * 
	 * @return string format
	 */
	public String getStringFormat( );

	/**
	 * Sets string format
	 * 
	 * @param format
	 * @throws ScriptException
	 */

	public void setStringFormat( String format );

	/**
	 * Returns color
	 * 
	 * @return color
	 */

	public String getColor( );

	/**
	 * Returns value1
	 * 
	 * @return value1
	 */

	public String getValue1( );

	/**
	 * Returns value2
	 * 
	 * @return value2
	 */

	public String getValue2( );

	/**
	 * Returns Operator
	 * 
	 * @return operator
	 */

	public String getOperator( );

	/**
	 * Returns backgroudcolor
	 * 
	 * @return backgroudcolor
	 */

	public String getBackGroudnColor( );

	/**
	 * Sets color
	 * 
	 * @param color
	 * @throws ScriptException
	 */

	public void setColor( String color );

	/**
	 * Sets Value1
	 * 
	 * @param value1
	 */

	public void setValue1( String value1 );

	/**
	 * Sets Value2
	 * 
	 * @param value2
	 */

	public void setValue2( String value2 );

	/**
	 * Sets Operator
	 * 
	 * @param operator
	 */

	public void setOperator( String operator );

	/**
	 * Sets backgroudcolor
	 * 
	 * @param color
	 */

	public void setBackGroudnColor( String color );

	/**
	 * Returns structure.
	 * 
	 * @return structure
	 */

	public IStructure getStructure( );

}
