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
package org.eclipse.birt.data.engine.api.script;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Event handler for a Script Data Set
 */
public interface IScriptDataSetEventHandler extends IBaseDataSetEventHandler {
	public void handleOpen(IDataSetInstanceHandle dataSet) throws BirtException;

	public void handleClose(IDataSetInstanceHandle dataSet) throws BirtException;

	/**
	 * Called by data engine to obtain the next data row. Implementation should fill
	 * in row data by using the IDataRow interface.
	 * 
	 * @return true if current data row is available and has been populated; false
	 *         if no more data row is unavailable; row has not been populated
	 */
	public boolean handleFetch(IDataSetInstanceHandle dataSet, IDataRow row) throws BirtException;

	/**
	 * Method for Script Data Set to return dynamically generated data set metadata.
	 * This method is called by data engine before the open() event is fired. If the
	 * data set implementation has dynamically generated metadata, it should call
	 * the addColumn method on the metaData object to add all its column definition,
	 * then return true. If the data set implementation uses the static metadata
	 * defined in the data set definition, it should return false.
	 */
	public boolean handleDescribe(IDataSetInstanceHandle dataSet, IScriptDataSetMetaDataDefinition metaData)
			throws BirtException;
}
