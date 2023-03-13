/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.jdbc;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Special case: if the stored procedure does not return resultset, a faked
 * resultset should be constructed
 */

public class SPResultSet implements IResultSet {
	/** the JDBC ResultSet object */
	// private java.sql.ResultSet rs;

	/**
	 *
	 * Constructor ResultSet(java.sql.ResultSet jrs) use JDBC's ResultSet to
	 * construct it.
	 *
	 */
	public SPResultSet(java.sql.ResultSet jrs) throws OdaException {

		/* record down the JDBC ResultSet object */
		// this.rs = jrs;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getMetaData()
	 */
	@Override
	public IResultSetMetaData getMetaData() throws OdaException {
		/* redirect the call to JDBC ResultSet.getMetaData() */
		SPResultSetMetaData rsMeta = new SPResultSetMetaData(null);
		return rsMeta;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#close()
	 */
	@Override
	public void close() throws OdaException {
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#setMaxRows(int)
	 */
	@Override
	public void setMaxRows(int max) {
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#next()
	 */
	@Override
	public boolean next() throws OdaException {
		return false;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getRow()
	 */
	@Override
	public int getRow() throws OdaException {
		return 0;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getString(int)
	 */
	@Override
	public String getString(int index) throws OdaException {
		return null;
	}

	/*
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSet#getString(java.lang.String)
	 */
	@Override
	public String getString(String columnName) throws OdaException {
		return null;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getInt(int)
	 */
	@Override
	public int getInt(int index) throws OdaException {
		return 0;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String columnName) throws OdaException {
		return 0;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getDouble(int)
	 */
	@Override
	public double getDouble(int index) throws OdaException {
		return 0.0;
	}

	/*
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSet#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String columnName) throws OdaException {
		return 0.0;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(int index) throws OdaException {
		return null;
	}

	/*
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSet#getBigDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getBigDecimal(String columnName) throws OdaException {
		return null;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getDate(int)
	 */
	@Override
	public Date getDate(int index) throws OdaException {
		return null;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(String columnName) throws OdaException {
		return null;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getTime(int)
	 */
	@Override
	public Time getTime(int index) throws OdaException {
		return null;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getTime(java.lang.String)
	 */
	@Override
	public Time getTime(String columnName) throws OdaException {
		return null;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(int index) throws OdaException {
		return null;
	}

	/*
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSet#getTimestamp(java.lang.String)
	 */
	@Override
	public Timestamp getTimestamp(String columnName) throws OdaException {
		return null;
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#wasNull()
	 */
	@Override
	public boolean wasNull() throws OdaException {
		return true;
	}

	/*
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSet#findColumn(java.lang.String)
	 */
	@Override
	public int findColumn(String columnName) throws OdaException {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getBlob(int)
	 */
	@Override
	public IBlob getBlob(int index) throws OdaException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getBlob(java.lang.String)
	 */
	@Override
	public IBlob getBlob(String columnName) throws OdaException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getClob(int)
	 */
	@Override
	public IClob getClob(int index) throws OdaException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.IResultSet#getClob(java.lang.String)
	 */
	@Override
	public IClob getClob(String columnName) throws OdaException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int index) throws OdaException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(java.lang.
	 * String)
	 */
	@Override
	public boolean getBoolean(String columnName) throws OdaException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getObject(int)
	 */
	@Override
	public Object getObject(int index) throws OdaException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(String columnName) throws OdaException {
		return null;
	}

}
