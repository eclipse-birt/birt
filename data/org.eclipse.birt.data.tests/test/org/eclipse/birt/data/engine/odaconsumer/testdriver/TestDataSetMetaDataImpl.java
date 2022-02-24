/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.engine.odaconsumer.testdriver;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Default implementation of IDataSetMetaData for a simple ODA runtime driver.
 */
public class TestDataSetMetaDataImpl implements IDataSetMetaData {
	private IConnection m_connection;
	private int m_currentTestCase = 0;

	protected TestDataSetMetaDataImpl(IConnection connection) {
		m_connection = connection;
		m_currentTestCase = ((TestConnectionImpl) connection).getCurrentTestCase();
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getConnection()
	 */
	public IConnection getConnection() throws OdaException {
		return m_connection;
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceObjects(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public IResultSet getDataSourceObjects(String catalog, String schema, String object, String version)
			throws OdaException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceMajorVersion()
	 */
	public int getDataSourceMajorVersion() throws OdaException {
		return 0;
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceMinorVersion()
	 */
	public int getDataSourceMinorVersion() throws OdaException {
		return 0;
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceProductName()
	 */
	public String getDataSourceProductName() throws OdaException {
		return "Simple Data Source"; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceProductVersion()
	 */
	public String getDataSourceProductVersion() throws OdaException {
		return Integer.toString(getDataSourceMajorVersion()) + "." + //$NON-NLS-1$
				Integer.toString(getDataSourceMinorVersion());
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getSQLStateType()
	 */
	public int getSQLStateType() throws OdaException {
		return IDataSetMetaData.sqlStateSQL99;
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsMultipleResultSets()
	 */
	public boolean supportsMultipleResultSets() throws OdaException {
		if (m_currentTestCase == (new Integer(TestAdvQueryImpl.TEST_CASE_SEQ_RESULT_SETS)).intValue())
			return true;
		return false;
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsMultipleOpenResults()
	 */
	public boolean supportsMultipleOpenResults() throws OdaException {
		return false;
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsNamedResultSets()
	 */
	public boolean supportsNamedResultSets() throws OdaException {
		if (m_currentTestCase == (new Integer(TestAdvQueryImpl.TEST_CASE_NAMED_RESULT_SETS)).intValue())
			return true;
		return false;
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsNamedParameters()
	 */
	public boolean supportsNamedParameters() throws OdaException {
		if (m_currentTestCase == (new Integer(TestAdvQueryImpl.TEST_CASE_IN_PARAM_NAME)).intValue())
			return true;
		if (m_currentTestCase == (new Integer(TestAdvQueryImpl.TEST_CASE_OUTPUTPARAM)).intValue())
			return true;

		return false;
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsInParameters()
	 */
	public boolean supportsInParameters() throws OdaException {
		return false;
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsOutParameters()
	 */
	public boolean supportsOutParameters() throws OdaException {
		return true;
	}

	/**
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getSortMode()
	 */
	public int getSortMode() {
		return IDataSetMetaData.sortModeNone;
	}
}
