/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
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
public interface IHighlightRule {

	/**
	 * Returns Test Expression
	 * 
	 * @return Test Expression
	 */

	public String getTestExpression();

	/**
	 * Sets Test Expression
	 * 
	 * @param expression
	 * @throws ScriptException
	 */

	public void setTestExpression(String expression) throws ScriptException;

	/**
	 * Returns font style
	 * 
	 * @return font style
	 */

	public String getFontStyle();

	/**
	 * Sets font style
	 * 
	 * @param style
	 * @throws ScriptException
	 */

	public void setFontStyle(String style) throws ScriptException;

	/**
	 * Returns font weight.
	 * 
	 * @return font weight.
	 */

	public String getFontWeight();

	/**
	 * Sets font weight.
	 * 
	 * @param weight
	 * @throws ScriptException
	 */

	public void setFontWeight(String weight) throws ScriptException;

	/**
	 * Return date time format.
	 * 
	 * @return date time format.
	 */

	public String getDateTimeFormat();

	/**
	 * Sets date time format
	 * 
	 * @param format
	 * @throws ScriptException
	 */

	public void setDateTimeFormat(String format) throws ScriptException;

	/**
	 * Returns string format
	 * 
	 * @return string format
	 */
	public String getStringFormat();

	/**
	 * Sets string format
	 * 
	 * @param format
	 * @throws ScriptException
	 */

	public void setStringFormat(String format) throws ScriptException;

	/**
	 * Returns color
	 * 
	 * @return color
	 */

	public String getColor();

	/**
	 * Returns value1
	 * 
	 * @return value1
	 */

	public String getValue1();

	/**
	 * Returns value2
	 * 
	 * @return value2
	 */

	public String getValue2();

	/**
	 * Returns Operator
	 * 
	 * @return operator
	 */

	public String getOperator();

	/**
	 * Returns backgroudcolor
	 * 
	 * @return backgroudcolor
	 */

	public String getBackGroundColor() throws ScriptException;

	/**
	 * Sets color
	 * 
	 * @param color
	 * @throws ScriptException
	 */

	public void setColor(String color) throws ScriptException;

	/**
	 * Sets Value1
	 * 
	 * @param value1
	 */

	public void setValue1(String value1) throws ScriptException;

	/**
	 * Sets Value2
	 * 
	 * @param value2
	 */

	public void setValue2(String value2) throws ScriptException;

	/**
	 * Sets Operator
	 * 
	 * @param operator
	 */

	public void setOperator(String operator) throws ScriptException;

	/**
	 * Sets backgroudcolor
	 * 
	 * @param color
	 */

	public void setBackGroundColor(String color) throws ScriptException;

	/**
	 * Returns structure.
	 * 
	 * @return structure
	 */

	public IStructure getStructure();

}
