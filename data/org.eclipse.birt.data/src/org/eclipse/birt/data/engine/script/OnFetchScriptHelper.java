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
package org.eclipse.birt.data.engine.script;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;

/**
 * Class to implement an event sink for dataSet.onFetch event
 */
public class OnFetchScriptHelper implements IResultObjectEvent {
	private DataSetRuntime dataSet;

	public OnFetchScriptHelper(DataSetRuntime dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * @see org.eclipse.birt.data.engine.odi.IResultObjectEvent#process(org.eclipse.birt.data.engine.odi.IResultObject)
	 */
	public boolean process(IResultObject resultObject, int rowIndex) throws DataException {
		IResultIterator resultSet = dataSet.getResultSet();
		// bind new object to row script object
		dataSet.setRowObject(resultObject, true);
		dataSet.setCurrentRowIndex(rowIndex);
		dataSet.onFetch();
		if (resultSet != null)
			dataSet.setResultSet(resultSet, true);
		return true;
	}
}
