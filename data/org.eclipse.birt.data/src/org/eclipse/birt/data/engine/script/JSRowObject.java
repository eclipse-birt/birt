/*
 *************************************************************************
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.script;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Underlying implementation of the Javascript "row" object. The ROM scripts use
 * this JS object to access the current data row in a result set.
 * 
 * The JS row object can be bound to either an odi result set (in which case it
 * maps to the current row object in the result set), or an individual
 * IResultObject.
 */
public class JSRowObject extends ScriptableObject {
	static private final String DATA_SET = "dataSet";
	static private final String COLUMN_MD = "columnDefns";
	static public final String ROW_POSITION = "_rowPosition";

	private DataSetRuntime dataSet;
	private JSColumnMetaData cachedColumnMetaData;

	private static Logger logger = Logger.getLogger(JSRowObject.class.getName());
	private static final long serialVersionUID = 6087456639367600994L;

	/**
	 * Constructor. Creates an empty row object with no binding.
	 */
	public JSRowObject(DataSetRuntime dataSet) {
		logger.entering(JSRowObject.class.getName(), "JSRowObject");
		this.dataSet = dataSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#getClassName()
	 */
	public String getClassName() {
		return "DataRow";
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#getIds()
	 */
	public Object[] getIds() {
		IResultObject obj = dataSet.getCurrentRow();
		int columnCount = 0;
		if (obj != null) {
			columnCount = obj.getResultClass().getFieldCount();
		}
		// Each field can be accessed via index or name; hence 2 *
		// We also have "dataSet", "columnMetadata", "row[0]" and "_rowPosition"
		int count = 4 + 2 * columnCount;

		int next = 0;
		Object[] ids = new Object[count];
		ids[next++] = DATA_SET;
		ids[next++] = COLUMN_MD;
		ids[next++] = Integer.valueOf(0);
		ids[next++] = ROW_POSITION;
		if (columnCount > 0) {
			for (int i = 1; i <= columnCount; i++) {
				ids[next++] = Integer.valueOf(i);
				try {
					ids[next++] = obj.getResultClass().getFieldName(i);
				} catch (DataException e) {
					// Shouldn't get here really
					logger.logp(Level.FINER, JSColumnDefn.class.getName(), "getIds", e.getMessage(), e);
				}
			}
		}

		return ids;
	}

	/**
	 * Checks if an indexed property exists
	 */
	public boolean has(int index, Scriptable start) {
		logger.entering(JSRowObject.class.getName(), "has", Integer.valueOf(index));
		// We maintain indexes 0 to columnCount
		// Column 0 is internal row ID; column 1 - columnCount are actual columns
		IResultObject obj = dataSet.getCurrentRow();

		if (index >= 0 && obj != null && index <= obj.getResultClass().getFieldCount()) {
			logger.exiting(JSRowObject.class.getName(), "has", Boolean.valueOf(true));
			return true;
		}

		// Let super handle the rest; caller may have added properties
		if (logger.isLoggable(Level.FINER))
			logger.exiting(JSRowObject.class.getName(), "has", Boolean.valueOf(super.has(index, start)));
		return super.has(index, start);
	}

	/**
	 * Checks if named property exists.
	 */
	public boolean has(String name, Scriptable start) {
		logger.entering(JSRowObject.class.getName(), "has", name);
		if (name.equals(DATA_SET) || name.endsWith(COLUMN_MD) || name.equals(ROW_POSITION)) {
			logger.exiting(JSRowObject.class.getName(), "has", Boolean.valueOf(true));
			return true;
		}

		// Check if name is a valid column name or alias
		IResultObject obj = dataSet.getCurrentRow();
		if (obj != null && obj.getResultClass().getFieldIndex(name) >= 0) {
			logger.exiting(JSRowObject.class.getName(), "has", Boolean.valueOf(true));
			return true;
		}
		// Let super handle the rest; caller may have added properties
		if (logger.isLoggable(Level.FINER))
			logger.exiting(JSRowObject.class.getName(), "has", Boolean.valueOf(super.has(name, start)));
		return super.has(name, start);
	}

	/**
	 * Gets an indexed property
	 */
	public Object get(int index, Scriptable start) {
		logger.entering(JSRowObject.class.getName(), "get", Integer.valueOf(index));
		// Special case: row[0] refers to internal row ID
		// It has undefined meaning for standalone IResultObject (we will let
		// IResultObject handle it in such case)
		try {
			Object value = dataSet.getDataRow().getColumnValue(index);
			return JavascriptEvalUtil.convertToJavascriptValue(value, dataSet.getSharedScope());
		} catch (BirtException e) {
			logger.logp(Level.FINER, JSColumnDefn.class.getName(), "get", e.getMessage(), e);
			logger.exiting(JSRowObject.class.getName(), "get", null);
			return null;
		}
	}

	/**
	 * Gets a named property
	 */
	public Object get(String name, Scriptable start) {
		logger.entering(JSRowObject.class.getName(), "get", name);
		if (name.equals(DATA_SET)) {
			if (logger.isLoggable(Level.FINER))
				logger.exiting(JSRowObject.class.getName(), "get");
			try {
				return dataSet.getJSDataSetObject();
			} catch (DataException e) {
				throw new RuntimeException(e.getLocalizedMessage(), e);
			}
		} else if (name.equals(COLUMN_MD)) {
			if (logger.isLoggable(Level.FINER))
				logger.exiting(JSRowObject.class.getName(), "get", getColumnMetadataScriptable());
			return getColumnMetadataScriptable();
		} else if (name.equals(ROW_POSITION)) {
			try {
				return Integer.valueOf(dataSet.getCurrentRowIndex());
			} catch (DataException e) {
				// Fall through and let super return not-found
				logger.logp(Level.FINER, JSColumnDefn.class.getName(), "get", e.getMessage(), e);
			}
		}

		// Try column names
		try {
			if (dataSet.getCurrentRow() == null)
				return null;
			Object value = dataSet.getDataRow().getColumnValue(name);

			return JavascriptEvalUtil.convertToJavascriptValue(value, dataSet.getSharedScope());
		} catch (BirtException e) {
			// Fall through and let super return not-found
			logger.logp(Level.FINER, JSColumnDefn.class.getName(), "get", e.getMessage(), e);
			throw Context.reportRuntimeError(e.getLocalizedMessage());
		}
	}

	/** Gets a JS object that implements the ColumnDefn[] array */
	Scriptable getColumnMetadataScriptable() {
		IResultObject obj = dataSet.getCurrentRow();
		if (obj == null || obj.getResultClass() == null)
			return null;

		// If the result class has not changed since we last created
		// the JSColumnMetaData object, return the same object
		// Otherwise create a new one
		if (cachedColumnMetaData == null || cachedColumnMetaData.getResultClass() != obj.getResultClass()) {
			cachedColumnMetaData = new JSColumnMetaData(obj.getResultClass());
		}

		return cachedColumnMetaData;
	}

	/**
	 * Sets a named property
	 *
	 */
	public void put(String name, Scriptable start, Object value) {
		logger.entering(JSRowObject.class.getName(), "put", name);
		if (name.equals(DATA_SET) || name.equals(COLUMN_MD))
			// these two are not updatable
			return;

		value = JavascriptEvalUtil.convertJavascriptValue(value);
		try {
			dataSet.getDataRow().setColumnValue(name, value);
		} catch (BirtException e) {
			logger.logp(Level.FINER, JSColumnDefn.class.getName(), "put", e.getMessage(), e);
		}
		logger.exiting(JSRowObject.class.getName(), "put");
	}

	/**
	 * Sets an indexed property
	 *
	 */
	public void put(int index, Scriptable start, Object value) {
		logger.entering(JSRowObject.class.getName(), "put", Integer.valueOf(index));

		value = JavascriptEvalUtil.convertJavascriptValue(value);
		try {
			dataSet.getDataRow().setColumnValue(index, value);
		} catch (BirtException e) {
			logger.logp(Level.FINER, JSColumnDefn.class.getName(), "put", e.getMessage(), e);
		}
		logger.exiting(JSRowObject.class.getName(), "put");
	}

}
