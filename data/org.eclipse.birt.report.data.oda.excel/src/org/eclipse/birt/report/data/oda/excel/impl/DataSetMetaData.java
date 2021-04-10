/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *    Actuate Corporation - code cleanup
  *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.impl;

import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implementation class of IDataSetMetaData for the Excel ODA runtime driver.
 */
public class DataSetMetaData implements IDataSetMetaData {
	private IConnection connection;

	DataSetMetaData(IConnection connection) {
		this.connection = connection;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getConnection()
	 */
	public IConnection getConnection() throws OdaException {
		return connection;
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
		return 1;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
	 * getDataSourceMinorVersion()
	 */
	public int getDataSourceMinorVersion() throws OdaException {
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#
	 * getDataSourceProductName()
	 */
	public String getDataSourceProductName() throws OdaException {
		return Messages.getString("dataSetMetaData_PRODUCT_NAME"); //$NON-NLS-1$
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
		throw new UnsupportedOperationException();
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
		return IDataSetMetaData.sortModeNone;
	}

}
