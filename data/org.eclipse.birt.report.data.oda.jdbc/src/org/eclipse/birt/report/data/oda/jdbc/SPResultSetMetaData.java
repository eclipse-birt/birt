/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * special case:if stored procedure does not return the resultset metadata, then
 * a fake resultsetmeta should be constructed
 * 
 */

public class SPResultSetMetaData implements IResultSetMetaData {
	/** the JDBC ResultSetMetaData object */
	// private java.sql.ResultSetMetaData rsMetadata;

	/**
	 * 
	 * Constructor SPResultSetMetaData(java.sql.ResultSetMetaData rsMeta) use JDBC's
	 * ResultSetMetaData to construct it.
	 * 
	 */
	public SPResultSetMetaData(java.sql.ResultSetMetaData rsMeta) throws OdaException {
		/* record down the JDBC ResultSetMetaData object */
		// this.rsMetadata = rsMeta;

	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnCount()
	 */
	public int getColumnCount() throws OdaException {
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnName(int)
	 */
	public String getColumnName(int index) throws OdaException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnLabel(int)
	 */
	public String getColumnLabel(int index) throws OdaException {
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnType(int)
	 */
	public int getColumnType(int index) throws OdaException {
		return 0;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnTypeName(int)
	 */
	public String getColumnTypeName(int index) throws OdaException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnDisplayLength(
	 * int)
	 */
	public int getColumnDisplayLength(int index) throws OdaException {
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#getPrecision(int)
	 */
	public int getPrecision(int index) throws OdaException {
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#getScale(int)
	 */
	public int getScale(int index) throws OdaException {
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#isNullable(int)
	 */
	public int isNullable(int index) throws OdaException {
		return 0;
	}

}
