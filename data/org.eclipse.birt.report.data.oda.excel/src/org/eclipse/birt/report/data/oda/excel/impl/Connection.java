/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *******************************************************************************/


package org.eclipse.birt.report.data.oda.excel.impl;

import java.io.File;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.excel.ExcelODAConstants;
import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.ibm.icu.util.ULocale;

/**
 * Implementation class of IConnection for an ODA runtime driver.
 */
public class Connection implements IConnection {
	private boolean isOpen = false;
	private Properties connProperties;

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties
	 * )
	 */
	public void open(Properties connProperties) throws OdaException {
		if (connProperties == null)
			throw new OdaException(
					Messages.getString("connection_CONNECTION_PROPERTIES_MISSING")); //$NON-NLS-1$

		this.connProperties = connProperties;
		validateHomeDir();

		isOpen = true;
	}

	private void validateHomeDir() throws OdaException {
		String homeDir = connProperties
				.getProperty(ExcelODAConstants.CONN_HOME_DIR_PROP);
		if (homeDir != null) // found property
		{
			File file = new File(homeDir);
			if (file.exists()) {
				this.isOpen = true;
				return; // is valid, done
			}
		}

		throw new OdaException(
				Messages.getString("connection_CANNOT_OPEN_EXCEL_FILE_DB_DIR") //$NON-NLS-1$
						+ homeDir);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java
	 * .lang.Object)
	 */
	public void setAppContext(Object context) throws OdaException {
		// do nothing; assumes no support for pass-through context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException {
		// TODO replace with data source specific implementation
		isOpen = false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen() throws OdaException {
		// TODO Auto-generated method stub
		return isOpen;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang
	 * .String)
	 */
	public IDataSetMetaData getMetaData(String dataSetType) throws OdaException {
		// assumes that this driver supports only one type of data set,
		// ignores the specified dataSetType
		return new DataSetMetaData(this);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang
	 * .String)
	 */
	public IQuery newQuery(String dataSetType) throws OdaException {
		// assumes that this driver supports only one type of data set,
		// ignores the specified dataSetType
		if (!isOpen())
			throw new OdaException(
					Messages.getString("common_CONNECTION_HAS_NOT_OPEN")); //$NON-NLS-1$

		return new ExcelFileQuery(connProperties, this);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMaxQueries()
	 */
	public int getMaxQueries() throws OdaException {
		return 0; // no limit
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#commit()
	 */
	public void commit() throws OdaException {
		// do nothing; assumes no transaction support needed
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#rollback()
	 */
	public void rollback() throws OdaException {
		// do nothing; assumes no transaction support needed
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#setLocale(com.ibm.
	 * icu.util.ULocale)
	 */
	public void setLocale(ULocale locale) throws OdaException {
		// do nothing; assumes no locale support
	}

}
