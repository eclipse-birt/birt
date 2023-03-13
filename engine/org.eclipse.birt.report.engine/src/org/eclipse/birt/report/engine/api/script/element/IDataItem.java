/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.model.api.ActionHandle;

/**
 * Represents a the design of a DataItem in the scripting environment
 */
public interface IDataItem extends IReportItem {

	/**
	 * Returns a handle to work with the action property, action is a structure that
	 * defines a hyperlink.
	 *
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the data item.
	 * @see ActionHandle
	 */
	IAction getAction();

	/**
	 * Returns the help text of this data item.
	 *
	 * @return the help text
	 */
	String getHelpText();

	/**
	 * Sets the help text of this data item.
	 *
	 * @param value the help text
	 *
	 * @throws ScriptException if the property is locked.
	 */
	void setHelpText(String value) throws ScriptException;

	/**
	 * Returns the help text resource key of this data item.
	 *
	 * @return the help text key
	 */
	String getHelpTextKey();

	/**
	 * Sets the resource key of the help text of this data item.
	 *
	 * @param value the resource key of the help text
	 *
	 * @throws ScriptException if the property is locked.
	 */
	void setHelpTextKey(String value) throws ScriptException;

	/**
	 * Adds the action structure to this data item.
	 *
	 * @param action action structure
	 */
	void addAction(IAction action);

	/**
	 * Gets the value of the result set column name property on this data item.
	 *
	 * @return the value of the property.
	 */

	String getResultSetColumn();

	/**
	 * Sets the value of the column name property.
	 *
	 * @param columnName the value to set.
	 * @throws ScriptException
	 */

	void setResultSetColumn(String columnName) throws ScriptException;

}
