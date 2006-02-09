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
package org.eclipse.birt.report.engine.script.internal.instance;

import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.report.engine.api.script.IColumnMetaData;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;
import org.eclipse.birt.report.engine.api.script.instance.IDataSourceInstance;
import org.eclipse.birt.report.engine.script.internal.ColumnMetaData;

public class DataSetInstance implements IDataSetInstance {

	private IDataSetInstanceHandle dataSet;

	public DataSetInstance(IDataSetInstanceHandle dataSet) {
		this.dataSet = dataSet;
	}

	public String getName() {
		return dataSet.getName();
	}

	public IDataSourceInstance getDataSource() {
		return new DataSourceInstance(dataSet.getDataSource());
	}

	public String getExtensionID() {
		return dataSet.getExtensionID();
	}

	public String getQueryText() throws ScriptException {
		try {
			return dataSet.getQueryText();
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public void setQueryText(String queryText) throws ScriptException {
		try {
			dataSet.setQueryText(queryText);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public IColumnMetaData getColumnMetaData() throws ScriptException {
		try {
			return new ColumnMetaData(dataSet.getResultMetaData());
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance#getAllExtensionProperties()
	 */
	public Map getAllExtensionProperties() {
		return dataSet.getAllExtensionProperties();
	}

	/**
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance#getExtensionProperty(java.lang.String)
	 */
	public String getExtensionProperty(String name) {
		return dataSet.getExtensionProperty(name);
	}

	/**
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance#setExtensionProperty(java.lang.String,
	 *      java.lang.String)
	 */
	public void setExtensionProperty(String name, String value) {
		dataSet.setExtensionProperty(name, value);
	}

}
