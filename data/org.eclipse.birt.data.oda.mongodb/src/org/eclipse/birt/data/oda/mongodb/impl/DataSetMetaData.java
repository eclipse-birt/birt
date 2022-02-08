/*
 *************************************************************************
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
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.oda.mongodb.impl;

import org.eclipse.birt.data.oda.mongodb.nls.Messages;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implementation class of IDataSetMetaData for the MongoDB ODA runtime driver.
 */
public class DataSetMetaData implements IDataSetMetaData {
	private IConnection m_connection;

	DataSetMetaData(IConnection connection) {
		m_connection = connection;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getConnection()
	 */
	public IConnection getConnection() throws OdaException {
		return m_connection;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceObjects(
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public IResultSet getDataSourceObjects(String catalog, String schema, String object, String version)
			throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
	 * getDataSourceMajorVersion()
	 */
	public int getDataSourceMajorVersion() throws OdaException {
		// Mongo Java driver doesn't provide the version any more
		// https://jira.mongodb.org/browse/JAVA-1605 Remove Mongo#getVersion
		return 3;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
	 * getDataSourceMinorVersion()
	 */
	public int getDataSourceMinorVersion() throws OdaException {
		return 2; // see Major version comment
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
	 * getDataSourceProductName()
	 */
	public String getDataSourceProductName() throws OdaException {
		return Messages.mDbDSMetaData_dataSourceName;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
	 * getDataSourceProductVersion()
	 */
	public String getDataSourceProductVersion() throws OdaException {
		return Integer.toString(getDataSourceMajorVersion()) + "." + //$NON-NLS-1$
				Integer.toString(getDataSourceMinorVersion());
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getSQLStateType()
	 */
	public int getSQLStateType() throws OdaException {
		return -1; // n/a
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
	 * supportsMultipleResultSets()
	 */
	public boolean supportsMultipleResultSets() throws OdaException {
		return false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
	 * supportsMultipleOpenResults()
	 */
	public boolean supportsMultipleOpenResults() throws OdaException {
		return false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
	 * supportsNamedResultSets()
	 */
	public boolean supportsNamedResultSets() throws OdaException {
		return false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
	 * supportsNamedParameters()
	 */
	public boolean supportsNamedParameters() throws OdaException {
		return false;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsInParameters(
	 * )
	 */
	public boolean supportsInParameters() throws OdaException {
		return false;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsOutParameters
	 * ()
	 */
	public boolean supportsOutParameters() throws OdaException {
		return false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getSortMode()
	 */
	public int getSortMode() {
		return IDataSetMetaData.sortModeColumnOrder;
	}

}
