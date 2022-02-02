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
	public IResultSetMetaData getMetaData() throws OdaException {
		/* redirect the call to JDBC ResultSet.getMetaData() */
		SPResultSetMetaData rsMeta = new SPResultSetMetaData(null);
		return rsMeta;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#close()
	 */
	public void close() throws OdaException {
		return;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#setMaxRows(int)
	 */
	public void setMaxRows(int max) {
		return;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#next()
	 */
	public boolean next() throws OdaException {
		return false;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getRow()
	 */
	public int getRow() throws OdaException {
		return 0;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getString(int)
	 */
	public String getString(int index) throws OdaException {
		return null;
	}

	/*
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSet#getString(java.lang.String)
	 */
	public String getString(String columnName) throws OdaException {
		return null;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getInt(int)
	 */
	public int getInt(int index) throws OdaException {
		return 0;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getInt(java.lang.String)
	 */
	public int getInt(String columnName) throws OdaException {
		return 0;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getDouble(int)
	 */
	public double getDouble(int index) throws OdaException {
		return 0.0;
	}

	/*
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSet#getDouble(java.lang.String)
	 */
	public double getDouble(String columnName) throws OdaException {
		return 0.0;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int index) throws OdaException {
		return null;
	}

	/*
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSet#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal(String columnName) throws OdaException {
		return null;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getDate(int)
	 */
	public Date getDate(int index) throws OdaException {
		return null;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getDate(java.lang.String)
	 */
	public Date getDate(String columnName) throws OdaException {
		return null;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getTime(int)
	 */
	public Time getTime(int index) throws OdaException {
		return null;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getTime(java.lang.String)
	 */
	public Time getTime(String columnName) throws OdaException {
		return null;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int index) throws OdaException {
		return null;
	}

	/*
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSet#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(String columnName) throws OdaException {
		return null;
	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#wasNull()
	 */
	public boolean wasNull() throws OdaException {
		return true;
	}

	/*
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSet#findColumn(java.lang.String)
	 */
	public int findColumn(String columnName) throws OdaException {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getBlob(int)
	 */
	public IBlob getBlob(int index) throws OdaException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getBlob(java.lang.String)
	 */
	public IBlob getBlob(String columnName) throws OdaException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getClob(int)
	 */
	public IClob getClob(int index) throws OdaException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.IResultSet#getClob(java.lang.String)
	 */
	public IClob getClob(String columnName) throws OdaException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(int)
	 */
	public boolean getBoolean(int index) throws OdaException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(java.lang.
	 * String)
	 */
	public boolean getBoolean(String columnName) throws OdaException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getObject(int)
	 */
	public Object getObject(int index) throws OdaException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getObject(java.lang.String)
	 */
	public Object getObject(String columnName) throws OdaException {
		return null;
	}

}
