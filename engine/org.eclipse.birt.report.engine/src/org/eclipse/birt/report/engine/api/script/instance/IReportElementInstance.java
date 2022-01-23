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
package org.eclipse.birt.report.engine.api.script.instance;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.api.script.ScriptException;

public interface IReportElementInstance {

	/**
	 * Get the style of this element
	 * 
	 */
	IScriptStyle getStyle();

	/**
	 * Get the horizontal position
	 */
	String getHorizontalPosition();

	/**
	 * Set the horizontal position
	 */
	void setHorizontalPosition(String position);

	/**
	 * Get the vertical position
	 */
	String getVerticalPosition();

	/**
	 * Set the vertical position
	 */
	void setVerticalPosition(String position);

	/**
	 * Get the width of the element
	 * 
	 */
	String getWidth();

	/**
	 * Set the width of the element
	 * 
	 */
	void setWidth(String width);

	/**
	 * Get the height of the element
	 * 
	 */
	String getHeight();

	/**
	 * Set the height of the element
	 * 
	 */
	void setHeight(String height);

	/**
	 * Get the value of a named expression
	 * 
	 */
	Object getNamedExpressionValue(String name);

	/**
	 * Get the value of a user property
	 * 
	 */
	Object getUserPropertyValue(String name);

	/**
	 * Set the value of a user property
	 * 
	 */
	void setUserPropertyValue(String name, Object value) throws ScriptException;

	/**
	 * Get the parent (container) of this element
	 * 
	 * @throws ScriptException
	 * 
	 * @throws BirtException
	 * 
	 */
	IReportElementInstance getParent() throws ScriptException;

	/**
	 * Get the row data used to create the instance. The row data is defined by the
	 * column binding.
	 * 
	 * @throws ScriptException
	 * 
	 * @throws BirtException
	 */
	IRowData getRowData() throws ScriptException;

}
