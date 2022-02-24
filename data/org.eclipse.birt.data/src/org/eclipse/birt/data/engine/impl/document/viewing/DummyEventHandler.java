/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl.document.viewing;

import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */
public class DummyEventHandler implements IEventHandler {
	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.IEventHandler#handleEndOfDataSetProcess(org.
	 * eclipse.birt.data.engine.odi.IResultIterator)
	 */
	public void handleEndOfDataSetProcess(IResultIterator resultIterator) {
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.IEventHandler#getValue(org.eclipse.birt.data
	 * .engine.odi.IResultObject, int, java.lang.String)
	 */
	public Object getValue(IResultObject rsObject, int index, String name) throws DataException {
		return rsObject.getFieldValue(index);
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IEventHandler#isRowID(int,
	 * java.lang.String)
	 */
	public boolean isRowID(int index, String columnName) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IEventHandler#getExecutorHelper()
	 */
	public IExecutorHelper getExecutorHelper() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.odi.IEventHandler#setExecutorHelper(org.eclipse.
	 * birt.data.engine.impl.ExecutorHelper)
	 */
	public void setExecutorHelper(IExecutorHelper helper) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IEventHandler#getExprs()
	 */
	public Map getColumnBindings() {
		return null;
	}

	public List<IBinding> getAllColumnBindings() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getAppContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getAggrDefinitions() throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

	public IBinding getBinding(String name) throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

	public DataSetRuntime getDataSetRuntime() {
		// TODO Auto-generated method stub
		return null;
	}

}
