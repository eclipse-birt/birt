/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v2.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-2.0.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *    Actuate Corporation - code cleanup
  *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.data.oda.excel.ResultSetMetaDataHelper;
import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.birt.report.data.oda.excel.impl.util.DataTypes;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implementation class of IResultSetMetaData for an ODA runtime driver. <br>
 * For demo purpose, the auto-generated method stubs have hard-coded
 * implementation that returns a pre-defined set of meta-data and query results.
 * A custom ODA driver is expected to implement own data source specific
 * behavior in its place.
 */
public class ResultSetMetaData implements IResultSetMetaData {

	private String[] columnNames = null;
	private String[] columnTypeNames = null;

	private Map<String, Integer> columnNameIndexMap = new HashMap<String, Integer>();

	public ResultSetMetaData(String[] columnNames, String[] columnTypeNames) {
		this.columnNames = columnNames;
		this.columnTypeNames = columnTypeNames;

		for (int i = 0; i < columnNames.length; i++) {
			columnNameIndexMap.put(columnNames[i].toUpperCase(), Integer.valueOf(i + 1));
		}
	}

	public ResultSetMetaData(ResultSetMetaDataHelper rsmdHelper) throws OdaException {
		if (rsmdHelper == null)
			throw new OdaException(Messages.getString("common_ARGUMENT_CANNOT_BE_NULL")); //$NON-NLS-1$

		this.columnNames = rsmdHelper.getColumnNames();
		this.columnTypeNames = rsmdHelper.getColumnTypes();

		for (int i = 0; i < columnNames.length; i++) {
			columnNameIndexMap.put(columnNames[i].toUpperCase(), Integer.valueOf(i + 1));
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnCount
	 * ()
	 */
	public int getColumnCount() throws OdaException {
		return this.columnNames.length;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnName
	 * (int)
	 */
	public String getColumnName(int index) throws OdaException {
		validateColumnIndex(index);
		return this.columnNames[index - 1].trim();
	}

	/**
	 * Evaluate whether the value of given column number is valid.
	 *
	 * @param index column number (1-based)
	 * @throws OdaException if the given index value is invalid
	 */
	private void validateColumnIndex(int index) throws OdaException {
		if (index > getColumnCount() || index < 1)
			throw new OdaException("INVALID_COLUMN_INDEX" + index); //$NON-NLS-1$
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnLabel
	 * (int)
	 */
	public String getColumnLabel(int index) throws OdaException {
		return getColumnName(index); // default
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnType
	 * (int)
	 */
	public int getColumnType(int index) throws OdaException {
		validateColumnIndex(index);
		return DataTypes.getTypeCode(columnTypeNames[index - 1]);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnTypeName
	 * (int)
	 */
	public String getColumnTypeName(int index) throws OdaException {
		int nativeTypeCode = getColumnType(index);
		return DataTypes.getNativeDataTypeName(nativeTypeCode);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#
	 * getColumnDisplayLength(int)
	 */
	public int getColumnDisplayLength(int index) throws OdaException {
		return (0);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getPrecision
	 * (int)
	 */
	public int getPrecision(int index) throws OdaException {
		return -1;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getScale(int)
	 */
	public int getScale(int index) throws OdaException {
		return -1;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSetMetaData#isNullable(int)
	 */
	public int isNullable(int index) throws OdaException {
		// TODO Auto-generated method stub
		return IResultSetMetaData.columnNullableUnknown;
	}

	public int findColumn(String columnName) throws OdaException {
		String trimmedColumnName = columnName.trim();
		Integer index = (Integer) (columnNameIndexMap.get(trimmedColumnName.toUpperCase()));
		if (index == null) {
			throw new OdaException("resultSet_COLUMN_NOT_FOUND " + columnName); //$NON-NLS-1$
		} else {
			return index.intValue();
		}
	}
}
