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
		return dataSet.getQueryText();
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

	/**
	 * Gets value of a data set input parameter by name
	 */
	public Object getInputParameterValue(String paramName) throws ScriptException {
		try {
			return dataSet.getInputParameterValue(paramName);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Gets value of a data set output parameter by name
	 */
	public Object getOutputParameterValue(String paramName) throws ScriptException {
		try {
			return dataSet.getOutputParameterValue(paramName);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Sets value of a data set input parameter
	 */
	public void setInputParameterValue(String paramName, Object paramValue) throws ScriptException {
		try {
			dataSet.setInputParameterValue(paramName, paramValue);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/**
	 * Sets value of a data set output parameter
	 */
	public void setOutputParameterValue(String paramName, Object paramValue) throws ScriptException {
		try {
			dataSet.setOutputParameterValue(paramName, paramValue);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Gets name, value of all data set input parameters
	 */
	public Map getInputParameters() {
		return dataSet.getInputParameters();
	}

	/**
	 * Gets name, value of all data set output parameters
	 */
	public Map getOutputParameters() {
		return dataSet.getOutputParameters();
	}

}
