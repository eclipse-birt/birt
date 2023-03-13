/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.birt.core.framework.URLClassLoader;
import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;
import org.eclipse.birt.data.oda.pojo.impl.internal.ResultSetFromPojoInstance;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.birt.data.oda.pojo.util.DataTypeUtil;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implementation class of IResultSet for POJO ODA runtime driver.
 */
public class ResultSet implements IResultSet {
	private int maxRows;
	private int currentRowId;

	private PojoQuery query;

	private IPojoDataSet pojoDataSet;

	private URLClassLoader pojoClassLoader;

	// The result set from current POJO instance
	private ResultSetFromPojoInstance subResultSet;

	private Object columnValue; // the value returned from the last "getColumnValue( int index )" call

	public ResultSet(PojoQuery query, IPojoDataSet pojoDataSet, URLClassLoader pojoClassLoader) throws OdaException {
		assert query != null && pojoDataSet != null && pojoClassLoader != null;

		this.query = query;
		this.pojoDataSet = pojoDataSet;
		this.pojoClassLoader = pojoClassLoader;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getMetaData()
	 */
	@Override
	public IResultSetMetaData getMetaData() throws OdaException {
		return new ResultSetMetaData(query.getReferenceGraph());
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#setMaxRows(int)
	 */
	@Override
	public void setMaxRows(int max) throws OdaException {
		maxRows = max > 0 ? max : 0;
	}

	/**
	 * Returns the maximum number of rows that can be fetched from this result set.
	 *
	 * @return the maximum number of rows to fetch.
	 */
	protected int getMaxRows() {
		return maxRows;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#next()
	 */
	@Override
	public boolean next() throws OdaException {
		if (maxRows > 0 && currentRowId >= maxRows) {
			subResultSet = null;
			return false;
		}
		if (subResultSet == null) // the next() is first called
		{
			Object pojo = pojoDataSet.next();
			if (pojo == null) {
				subResultSet = null;
				return false;
			}
			subResultSet = new ResultSetFromPojoInstance(pojo, query, pojoClassLoader);
		}
		if (subResultSet.next()) {
			currentRowId++;
			return true;
		} else {
			// reach the end of the this subResultSet, need to prepare the next subResultSet
			Object pojo = pojoDataSet.next();
			if (pojo == null) {
				subResultSet = null;
				return false;
			}
			subResultSet = new ResultSetFromPojoInstance(pojo, query, pojoClassLoader);

			// Always succeed in going to the first row of new created subResultSet
			subResultSet.next();
			currentRowId++;
			return true;
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#close()
	 */
	@Override
	public void close() throws OdaException {
		currentRowId = 0; // reset row counter
		if (pojoDataSet != null) {
			pojoDataSet.close();
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getRow()
	 */
	@Override
	public int getRow() throws OdaException {
		return currentRowId;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getString(int)
	 */
	@Override
	public String getString(int index) throws OdaException {
		return DataTypeUtil.toString(getColumnValue(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getString(java.lang.String)
	 */
	@Override
	public String getString(String columnName) throws OdaException {
		return getString(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getInt(int)
	 */
	@Override
	public int getInt(int index) throws OdaException {
		return DataTypeUtil.toInt(getColumnValue(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String columnName) throws OdaException {
		return getInt(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(int)
	 */
	@Override
	public double getDouble(int index) throws OdaException {
		return DataTypeUtil.toDouble(getColumnValue(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String columnName) throws OdaException {
		return getDouble(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(int index) throws OdaException {
		return DataTypeUtil.toBigDecimal(getColumnValue(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(java.lang.
	 * String)
	 */
	@Override
	public BigDecimal getBigDecimal(String columnName) throws OdaException {
		return getBigDecimal(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDate(int)
	 */
	@Override
	public Date getDate(int index) throws OdaException {
		return DataTypeUtil.toDate(getColumnValue(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(String columnName) throws OdaException {
		return getDate(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTime(int)
	 */
	@Override
	public Time getTime(int index) throws OdaException {
		return DataTypeUtil.toTime(getColumnValue(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getTime(java.lang.String)
	 */
	@Override
	public Time getTime(String columnName) throws OdaException {
		return getTime(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(int index) throws OdaException {
		return DataTypeUtil.toTimestamp(getColumnValue(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(java.lang.
	 * String)
	 */
	@Override
	public Timestamp getTimestamp(String columnName) throws OdaException {
		return getTimestamp(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(int)
	 */
	@Override
	public IBlob getBlob(int index) throws OdaException {
		return DataTypeUtil.toBlob(getColumnValue(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(java.lang.String)
	 */
	@Override
	public IBlob getBlob(String columnName) throws OdaException {
		return getBlob(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getClob(int)
	 */
	@Override
	public IClob getClob(int index) throws OdaException {
		return DataTypeUtil.toClob(getColumnValue(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getClob(java.lang.String)
	 */
	@Override
	public IClob getClob(String columnName) throws OdaException {
		return getClob(findColumn(columnName));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int index) throws OdaException {
		return DataTypeUtil.toBoolean(getColumnValue(index));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(java.lang.
	 * String)
	 */
	@Override
	public boolean getBoolean(String columnName) throws OdaException {
		return getBoolean(findColumn(columnName));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getObject(int)
	 */
	@Override
	public Object getObject(int index) throws OdaException {
		return getColumnValue(index);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(String columnName) throws OdaException {
		return getObject(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#wasNull()
	 */
	@Override
	public boolean wasNull() throws OdaException {
		return columnValue == null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#findColumn(java.lang.
	 * String)
	 */
	@Override
	public int findColumn(String columnName) throws OdaException {
		return query.getReferenceGraph().findColumn(columnName);
	}

	/**
	 * set to "public" only for exposing it to test cases project
	 *
	 * @param index: 1-based
	 * @return
	 */
	public Object getColumnValue(int index) throws OdaException {
		columnValue = null;
		if (subResultSet != null) {
			columnValue = subResultSet.getColumnValue(index);
		}
		return columnValue;
	}
}
