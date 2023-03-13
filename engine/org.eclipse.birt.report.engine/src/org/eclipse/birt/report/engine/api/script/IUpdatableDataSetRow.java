/*
 *************************************************************************
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
 *
 *************************************************************************
 */
package org.eclipse.birt.report.engine.api.script;

/**
 * A Data Set Row which allows its column values to be updated.
 *
 */
public interface IUpdatableDataSetRow extends IDataSetRow {
	/**
	 * Sets the column data by index. Column index starts from 1.
	 *
	 * @param index 1-based index of column. Value must be between 1 and the number
	 *              of columns
	 * @param value New value for column (can be null)
	 * @throws ScriptException if index is out of range, or if value cannot be
	 *                         converted to the data type of the column
	 */
	void setColumnValue(int index, Object value) throws ScriptException;

	/**
	 * Sets the column data by column name.
	 *
	 * @param name name of column
	 * @throws ScriptException Named column does not exist, or if value cannot be
	 *                         converted to the data type of the column
	 */
	void setColumnValue(String name, Object value) throws ScriptException;
}
