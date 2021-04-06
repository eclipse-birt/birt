/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.script;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.script.IJavascriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.impl.ScriptDataSetRuntime;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Implements BIRT Script's Data Set object. This native Java class is made
 * available to the Rhino engine via it's NativeJavaObject wrapper
 */
public class JSDataSetImpl {
	protected DataSetRuntime dataSet;

	public JSDataSetImpl(DataSetRuntime dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * Implements DataSet.dataSource
	 * 
	 * @throws DataException
	 */
	public Scriptable getDataSource() throws DataException {
		IJavascriptContext dataSource = dataSet.getDataSource();
		Scriptable ret = null;
		if (dataSource != null)
			ret = dataSource.getScriptScope();
		return ret;
	}

	/**
	 * Implements DataSet.extensionID
	 */
	public String getExtensionID() {
		return dataSet.getExtensionID();
	}

	/**
	 * Implements DataSet.extensionID (setter)
	 */
	public void setExtensionID(String id) {
		// To preserve backward compatibility with 1.0 (where setting
		// extension ID call is silently ignored), no error is reported

	}

	/**
	 * Implements DataSet.extensionProperties
	 */
	public Scriptable getExtensionProperties() {
		Map props = dataSet.getAllExtensionProperties();

		if (props != null) {
			// Data Source's publicproperties is a String->Collection map
			return new JSStringMap(props);
		} else {
			return null;
		}
	}

	public Map getAllExtensionProperties() {
		return dataSet.getAllExtensionProperties();
	}

	/**
	 * Implements DataSet.getExtensionProperty(name)
	 */
	public String getExtensionProperty(String name) {
		return dataSet.getExtensionProperty(name);
	}

	/**
	 * Implements DataSet.name (getter)
	 */
	public String getName() {
		return dataSet.getName();
	}

	/**
	 * Implements DataSet.name (setter)
	 */
	public void setName(String name) {
		// Name cannot be changed; ignore this call
	}

	/**
	 * Implements DataSet.queryText [getter]
	 */
	public String getQueryText() throws BirtException {
		return dataSet.getQueryText();
	}

	/**
	 * Implements DataSet.queryText [setter]
	 */

	public void setQueryText(String queryText) throws BirtException {
		dataSet.setQueryText(queryText);
	}

	/**
	 * Implements DataSet.setExtensionProperty( name, value )
	 */
	public void setExtensionProperty(String name, String value) {
		dataSet.setExtensionProperty(name, value);
	}

	/**
	 * Implements DataSet.row
	 */
	public Scriptable getRow() {
		return dataSet.getJSResultRowObject();
	}

	/**
	 * @return
	 * @throws BirtException
	 */
	public Scriptable getDataSetRow() throws BirtException {
		return dataSet.getJSDataSetRowObject();
	}

	/**
	 * Implements DataSet.rows
	 */
	public Scriptable getRows() throws BirtException {
		return dataSet.getJSRowsObject();
	}

	/**
	 * Implements DataSet.columnDefns
	 */
	public Scriptable getColumnDefns() throws BirtException {
		if (dataSet.getJSRowObject() != null) {
			return ((JSRowObject) dataSet.getJSRowObject()).getColumnMetadataScriptable();
		} else
			return null;
	}

	/**
	 * Implements DataSet.outputParams
	 */
	public Scriptable getOutputParams() throws BirtException {
		return dataSet.getJSOutputParamsObject();
	}

	/**
	 * Implements DataSet.inputParams
	 */
	public Scriptable getInputParams() throws BirtException {
		return dataSet.getJSInputParamsObject();
	}

	/**
	 * Implements DataSet._aggr_value (internal object)
	 */
	public Scriptable get_aggr_value() {
		return dataSet.getJSAggrValueObject();
	}

	/**
	 * Implements DataSet._temp_aggr_value (internal object)
	 */
	public Scriptable get_temp_aggr_value() {
		return dataSet.getJSTempAggrValueObject();
	}

	/**
	 * Implements DataSet addDataSetColumn(name, dataType)
	 */
	public void addDataSetColumn(String name, String type) {
		// This function is only available for script data set runtime
		if (!(dataSet instanceof ScriptDataSetRuntime))
			return;

		// Only the following data types can be returned
		Class clazz;
		if ("INTEGER".equals(type))
			clazz = Integer.class;
		else if ("DOUBLE".equals(type))
			clazz = Double.class;
		else if ("DECIMAL".equals(type))
			clazz = BigDecimal.class;
		else if ("DATE".equals(type))
			clazz = Date.class;
		else if ("STRING".equals(type))
			clazz = String.class;
		else if ("ANY".equals(type))
			clazz = DataType.AnyType.class;
		else
			throw Context.reportRuntimeError("Invalid data type " + type);

		try {
			((ScriptDataSetRuntime) dataSet).addColumn(name, clazz);
		} catch (BirtException e) {
			throw Context.reportRuntimeError(e.getLocalizedMessage());
		}
	}
}
