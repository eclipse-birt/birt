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

	String getTestExpression();

	/**
	 * Sets Test Expression
	 *
	 * @param expression
	 * @throws ScriptException
	 */

	void setTestExpression(String expression) throws ScriptException;

	/**
	 * Returns font style
	 *
	 * @return font style
	 */

	String getFontStyle();

	/**
	 * Sets font style
	 *
	 * @param style
	 * @throws ScriptException
	 */

	void setFontStyle(String style) throws ScriptException;

	/**
	 * Returns font weight.
	 *
	 * @return font weight.
	 */

	String getFontWeight();

	/**
	 * Sets font weight.
	 *
	 * @param weight
	 * @throws ScriptException
	 */

	void setFontWeight(String weight) throws ScriptException;

	/**
	 * Return date time format.
	 *
	 * @return date time format.
	 */

	String getDateTimeFormat();

	/**
	 * Sets date time format
	 *
	 * @param format
	 * @throws ScriptException
	 */

	void setDateTimeFormat(String format) throws ScriptException;

	/**
	 * Returns string format
	 *
	 * @return string format
	 */
	String getStringFormat();

	/**
	 * Sets string format
	 *
	 * @param format
	 * @throws ScriptException
	 */

	void setStringFormat(String format) throws ScriptException;

	/**
	 * Returns color
	 *
	 * @return color
	 */

	String getColor();

	/**
	 * Returns value1
	 *
	 * @return value1
	 */

	String getValue1();

	/**
	 * Returns value2
	 *
	 * @return value2
	 */

	String getValue2();

	/**
	 * Returns Operator
	 *
	 * @return operator
	 */

	String getOperator();

	/**
	 * Returns backgroudcolor
	 *
	 * @return backgroudcolor
	 */

	String getBackGroundColor() throws ScriptException;

	/**
	 * Sets color
	 *
	 * @param color
	 * @throws ScriptException
	 */

	void setColor(String color) throws ScriptException;

	/**
	 * Sets Value1
	 *
	 * @param value1
	 */

	void setValue1(String value1) throws ScriptException;

	/**
	 * Sets Value2
	 *
	 * @param value2
	 */

	void setValue2(String value2) throws ScriptException;

	/**
	 * Sets Operator
	 *
	 * @param operator
	 */

	void setOperator(String operator) throws ScriptException;

	/**
	 * Sets backgroudcolor
	 *
	 * @param color
	 */

	void setBackGroundColor(String color) throws ScriptException;

	/**
	 * Returns structure.
	 *
	 * @return structure
	 */

	IStructure getStructure();

}
