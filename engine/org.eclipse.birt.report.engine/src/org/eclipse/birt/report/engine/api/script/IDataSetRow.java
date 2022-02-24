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
package org.eclipse.birt.report.engine.api.script;

import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;

public interface IDataSetRow {
	/**
	 * Gets the data set runtime instance which contains this row
	 */
	IDataSetInstance getDataSet();

	/**
	 * Gets the column data by index. Data row column index starts from 1.
	 * 
	 * @param index 1-based index of column. If value is 0, an internal index of the
	 *              current row (if available) is returned
	 * @throws ScriptException
	 */
	Object getColumnValue(int index) throws ScriptException;

	/**
	 * Gets the column data by column name.
	 * 
	 * @param name of column
	 * @throws ScriptException
	 */
	Object getColumnValue(String name) throws ScriptException;

}
