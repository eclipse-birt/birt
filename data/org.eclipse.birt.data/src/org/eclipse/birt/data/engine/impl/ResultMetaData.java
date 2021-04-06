/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * Describes the metadata of a detail row expected in an IResultIterator.
 * Implements Data Engine API IResultMetaData.
 */
public class ResultMetaData implements IResultMetaData {
	IResultClass m_odiResultClass;
	protected static Logger logger = Logger.getLogger(ResultMetaData.class.getName());
	private int columnCount = -1;

	/**
	 * @param odiResultClass
	 */
	public ResultMetaData(IResultClass odiResultClass) {
		logger.entering(ResultMetaData.class.getName(), "ResultMetaData", odiResultClass);
		assert odiResultClass != null;
		m_odiResultClass = odiResultClass;
		logger.logp(Level.FINER, QueryResults.class.getName(), "QueryResults", "QueryResults starts up");
		logger.exiting(ResultMetaData.class.getName(), "ResultMetaData");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnCount()
	 */
	public int getColumnCount() {
		logger.logp(Level.FINEST, QueryResults.class.getName(), "getColumnCount", "");
		return doGetColumnCount();
	}

	private int doGetColumnCount() {
		if (columnCount != -1)
			return columnCount;

		int columnCount = m_odiResultClass.getFieldCount();
		for (int i = columnCount; i > 0; i--) {
			try {
				if (!isTemp(m_odiResultClass.getFieldName(i))) {
					columnCount = i;
					break;
				}
			} catch (DataException e) {
				return columnCount;
			}
		}

		return columnCount;
	}

	private boolean isTemp(String name) {
		return (name.matches("\\Q_{$TEMP_GROUP_\\E\\d*\\Q$}_\\E") || name.matches("\\Q_{$TEMP_SORT_\\E\\d*\\Q$}_\\E")
				|| name.matches("\\Q_{$TEMP_FILTER_\\E\\d*\\Q$}_\\E"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnName(int)
	 */
	public String getColumnName(int index) throws DataException {
		checkIndex(index);
		logger.logp(Level.FINEST, QueryResults.class.getName(), "getColumnName",
				"the column name at the specified index", Integer.valueOf(index));
		return m_odiResultClass.getFieldName(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnAlias(int)
	 */
	public String getColumnAlias(int index) throws DataException {
		checkIndex(index);
		logger.logp(Level.FINEST, QueryResults.class.getName(), "getColumnAlias",
				"the column alias at the specified index", Integer.valueOf(index));
		return m_odiResultClass.getFieldAlias(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnType(int)
	 */
	public int getColumnType(int index) throws DataException {
		checkIndex(index);
		logger.logp(Level.FINEST, QueryResults.class.getName(), "getColumnType",
				"the data type of the column at the specified index", Integer.valueOf(index));
		Class odiDataType = m_odiResultClass.getFieldValueClass(index);
		return DataTypeUtil.toApiDataType(odiDataType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnTypeName(int)
	 */
	public String getColumnTypeName(int index) throws DataException {
		checkIndex(index);
		logger.logp(Level.FINEST, QueryResults.class.getName(), "getColumnTypeName",
				"the Data Engine data type name of the column at the specified index", Integer.valueOf(index));
		return DataType.getName(getColumnType(index));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultMetaData#getColumnNativeTypeName(int)
	 */
	public String getColumnNativeTypeName(int index) throws DataException {
		checkIndex(index);
		logger.logp(Level.FINEST, QueryResults.class.getName(), "getColumnNativeTypeName",
				"the data provider specific data type name of the specified column", Integer.valueOf(index));
		return m_odiResultClass.getFieldNativeTypeName(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnLabel(int)
	 */
	public String getColumnLabel(int index) throws DataException {
		checkIndex(index);
		logger.logp(Level.FINEST, QueryResults.class.getName(), "getColumnLabel",
				"the label or display name of the column at the specified index", Integer.valueOf(index));
		return m_odiResultClass.getFieldLabel(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#isComputedColumn(int)
	 */
	public boolean isComputedColumn(int index) throws DataException {
		checkIndex(index);
		logger.logp(Level.FINEST, QueryResults.class.getName(), "isComputedColumn",
				"whether the specified projected column is defined as a computed column", Integer.valueOf(index));
		return m_odiResultClass.isCustomField(index);
	}

	/**
	 * Indicates whether index is out of bounds
	 * 
	 * @param index 1-based
	 * @throws DataException
	 */
	private void checkIndex(int index) throws DataException {
		if (index > doGetColumnCount())
			throw new DataException(ResourceConstants.INVALID_FIELD_INDEX, Integer.valueOf(index));
	}

}
