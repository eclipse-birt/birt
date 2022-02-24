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

package org.eclipse.birt.report.engine.api.script.eventhandler;

import org.eclipse.birt.report.engine.api.script.IScriptedDataSetMetaData;
import org.eclipse.birt.report.engine.api.script.IUpdatableDataSetRow;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;

/**
 * Script event handler interface for a scripted data set
 */
public interface IScriptedDataSetEventHandler extends IDataSetEventHandler {
	/**
	 * Handle the open event
	 */
	void open(IDataSetInstance dataSet) throws ScriptException;

	/**
	 * Handle the fetch event. Implementation should call methods on the row object
	 * to set data of the current row being fetched.
	 *
	 * @return true if current data row has been populated. false if the last call
	 *         to fetch has returned the last data row, and no more data is
	 *         available.
	 */
	boolean fetch(IDataSetInstance dataSet, IUpdatableDataSetRow row) throws ScriptException;

	/**
	 * Handle the close event
	 */
	void close(IDataSetInstance dataSet) throws ScriptException;

	/**
	 * Method for Script Data Set to return dynamically generated data set metadata.
	 * This method is called before the open event is fired. If the data set
	 * implementation has dynamically generated metadata, it should call the
	 * addColumn method on the metaData object to add all its column definition,
	 * then return true. If the data set implementation uses the static metadata
	 * defined in the data set design , it should return false.
	 */
	boolean describe(IDataSetInstance dataSet, IScriptedDataSetMetaData metaData) throws ScriptException;

}
