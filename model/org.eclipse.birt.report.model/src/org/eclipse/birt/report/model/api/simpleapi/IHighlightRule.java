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

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;
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
	 * @throws SemanticException
	 */

	public void setTestExpression(String expression) throws SemanticException;

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
	 * @throws SemanticException
	 */

	public void setFontStyle(String style) throws SemanticException;

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
	 * @throws SemanticException
	 */

	public void setFontWeight(String weight) throws SemanticException;

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
	 * @throws SemanticException
	 */

	public void setDateTimeFormat(String format) throws SemanticException;

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
	 * @throws SemanticException
	 */

	public void setStringFormat(String format) throws SemanticException;

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

	public String getBackGroundColor();

	/**
	 * Sets color
	 * 
	 * @param color
	 * @throws SemanticException
	 */

	public void setColor(String color) throws SemanticException;

	/**
	 * Sets Value1
	 * 
	 * @param value1
	 */

	public void setValue1(String value1) throws SemanticException;

	/**
	 * Sets Value2
	 * 
	 * @param value2
	 */

	public void setValue2(String value2) throws SemanticException;

	/**
	 * Sets Operator
	 * 
	 * @param operator
	 */

	public void setOperator(String operator) throws SemanticException;

	/**
	 * Sets backgroudcolor
	 * 
	 * @param color
	 */

	public void setBackGroundColor(String color) throws SemanticException;

	/**
	 * Returns structure.
	 * 
	 * @return structure
	 */

	public IStructure getStructure();

}
