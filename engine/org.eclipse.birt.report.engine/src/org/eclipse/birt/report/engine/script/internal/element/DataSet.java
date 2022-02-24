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

package org.eclipse.birt.report.engine.script.internal.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IDataSet;
import org.eclipse.birt.report.engine.api.script.element.IDataSource;
import org.eclipse.birt.report.engine.api.script.element.IResultSetColumn;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

public class DataSet implements IDataSet {
	private org.eclipse.birt.report.model.api.simpleapi.IDataSet dataSetImpl;

	public DataSet(DataSetHandle dataSet) {
		dataSetImpl = SimpleElementFactory.getInstance().createDataSet(dataSet);
	}

	public DataSet(org.eclipse.birt.report.model.api.simpleapi.IDataSet dataSet) {
		dataSetImpl = dataSet;
	}

	public IDataSource getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getQueryText() {
		return dataSetImpl.getQueryText();
	}

	public void setQueryText(String query) throws ScriptException {
		try {
			dataSetImpl.setQueryText(query);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	public String getPrivateDriverProperty(String name) {
		return dataSetImpl.getPrivateDriverProperty(name);
	}

	public void setPrivateDriverProperty(String name, String value) throws ScriptException {
		try {
			dataSetImpl.setPrivateDriverProperty(name, value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IDataSet#getResultSetColumn
	 * ()
	 */

	public List getCachedResultSetColumns() {
		List values = dataSetImpl.getCachedResultSetColumns();
		List rtnValues = new ArrayList();

		Iterator iterator = values.iterator();
		while (iterator.hasNext()) {
			IResultSetColumn column = new ResultSetColumnImpl(
					(org.eclipse.birt.report.model.api.simpleapi.IResultSetColumn) iterator.next());
			rtnValues.add(column);
		}
		return Collections.unmodifiableList(rtnValues);
	}

}
