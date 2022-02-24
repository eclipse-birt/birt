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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.impl.ResultMetaData;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Implements IDataRow interface to allow Java script code to access the current
 * data row. This object can be bound to either an odi result set (in which case
 * it maps to the current row object in the result set), or an individual
 * IResultObject.
 */
public class DataRow implements IDataRow {
	protected DataSetRuntime dataSet;
	protected IResultMetaData metaData;

	/**
	 * Constructor. Creates an empty row object with no binding.
	 */
	public DataRow(DataSetRuntime dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataRow#getDataSet()
	 */
	public IDataSetInstanceHandle getDataSet() {
		return dataSet;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataRow#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData() throws BirtException {
		if (this.metaData == null) {
			IResultObject obj = dataSet.getCurrentRow();
			if (obj != null && obj.getResultClass() != null) {
				this.metaData = new ResultMetaData(obj.getResultClass());
			}
		}
		return this.metaData;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataRow#getColumnValue(int)
	 */
	public Object getColumnValue(int index) throws BirtException {
		if (index == 0) {
			return Integer.valueOf(dataSet.getCurrentRowIndex());
		} else {
			return getAndCheckResultObject().getFieldValue(index);
		}
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataRow#getColumnValue(java.lang.String)
	 */
	public Object getColumnValue(String name) throws BirtException {
		return getAndCheckResultObject().getFieldValue(name);
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataRow#setColumnValue(int,
	 *      java.lang.Object)
	 */
	public void setColumnValue(int index, Object value) throws BirtException {
		if (!dataSet.allowUpdateRowData())
			throw new DataException(ResourceConstants.NO_ROW_UPDATE);

		IResultObject obj = getAndCheckResultObject();
		// Observe the type restriction on the column
		Class fieldClass = obj.getResultClass().getFieldValueClass(index);
		if (fieldClass != DataType.AnyType.class) {
			try {
				value = DataTypeUtil.convert(value, fieldClass);
			} catch (BirtException e) {
				if (obj.getResultClass() instanceof ResultClass) {
					if (obj.getResultClass().wasAnyType(index))
						throw new IllegalArgumentException(DataResourceHandle.getInstance()
								.getMessage(ResourceConstants.POSSIBLE_MIXED_DATA_TYPE_IN_COLUMN));
				} else {
					throw e;
				}

			}
		}
		obj.setCustomFieldValue(index, value);
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.script.IDataRow#setColumnValue(int,
	 *      java.lang.Object)
	 */
	public void setColumnValue(String name, Object value) throws BirtException {
		if (!dataSet.allowUpdateRowData())
			throw new DataException(ResourceConstants.NO_ROW_UPDATE);

		IResultObject obj = getAndCheckResultObject();
		// Observe the type restriction on the column
		Class fieldClass = obj.getResultClass().getFieldValueClass(name);
		if (fieldClass != DataType.AnyType.class) {
			try {
				value = DataTypeUtil.convert(value, fieldClass);
			} catch (BirtException e) {
				if (obj.getResultClass() instanceof ResultClass) {
					if (obj.getResultClass().wasAnyType(name))
						throw new IllegalArgumentException(DataResourceHandle.getInstance()
								.getMessage(ResourceConstants.POSSIBLE_MIXED_DATA_TYPE_IN_COLUMN));
				} else {
					throw e;
				}

			}
		}
		obj.setCustomFieldValue(name, value);
	}

	/**
	 * Get result object from IResultObject or IResultSetIterator. Throws error if
	 * no current result object is available
	 */
	protected IResultObject getAndCheckResultObject() throws DataException {
		IResultObject resultObject = dataSet.getCurrentRow();
		if (resultObject == null)
			throw new DataException(ResourceConstants.NO_CURRENT_ROW);
		return resultObject;
	}

}
