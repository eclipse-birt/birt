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

	@Override
	public String getName() {
		return dataSet.getName();
	}

	@Override
	public IDataSourceInstance getDataSource() {
		return new DataSourceInstance(dataSet.getDataSource());
	}

	@Override
	public String getExtensionID() {
		return dataSet.getExtensionID();
	}

	@Override
	public String getQueryText() throws ScriptException {
		return dataSet.getQueryText();
	}

	@Override
	public void setQueryText(String queryText) throws ScriptException {
		try {
			dataSet.setQueryText(queryText);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
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
	@Override
	public Map getAllExtensionProperties() {
		return dataSet.getAllExtensionProperties();
	}

	/**
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance#getExtensionProperty(java.lang.String)
	 */
	@Override
	public String getExtensionProperty(String name) {
		return dataSet.getExtensionProperty(name);
	}

	/**
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance#setExtensionProperty(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void setExtensionProperty(String name, String value) {
		dataSet.setExtensionProperty(name, value);
	}

	/**
	 * Gets value of a data set input parameter by name
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public Map getInputParameters() {
		return dataSet.getInputParameters();
	}

	/**
	 * Gets name, value of all data set output parameters
	 */
	@Override
	public Map getOutputParameters() {
		return dataSet.getOutputParameters();
	}

}
