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
  *    Actuate Corporation - added support of relative file path
  *    Actuate Corporation - support defining an Excel input file path or URI as part of the data source definition
  *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.impl;

import java.util.Map;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.excel.ExcelODAConstants;
import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;

import com.ibm.icu.util.ULocale;

/**
 * Implementation class of IConnection for an ODA runtime driver.
 */
public class Connection implements IConnection {
	private boolean isOpen = false;
	private Properties connProperties;
	private Map appContext = null;

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties
	 * )
	 */
	@Override
	public void open(Properties connProperties) throws OdaException {
		if (connProperties == null) {
			throw new OdaException(Messages.getString("connection_CONNECTION_PROPERTIES_MISSING")); //$NON-NLS-1$
		}

		this.connProperties = connProperties;
		validateURI();
	}

	// move the test connection to
	// ExcelDataSourcePageHelper.createTestConnectionRunnable
	private void validateURI() throws OdaException {
		String uri = connProperties.getProperty(ExcelODAConstants.CONN_FILE_URI_PROP);

		if (uri != null && uri.trim().length() > 0) // found
		{
			this.isOpen = true;
			return; // is valid, done
		}

		throw new OdaException(Messages.getString("connection_MISSING_FILELOCATION"));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java
	 * .lang.Object)
	 */
	@Override
	public void setAppContext(Object context) throws OdaException {
		// do nothing; assumes no support for pass-through context
		this.appContext = (Map) context;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	@Override
	public void close() throws OdaException {
		isOpen = false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	@Override
	public boolean isOpen() throws OdaException {
		return isOpen;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang
	 * .String)
	 */
	@Override
	public IDataSetMetaData getMetaData(String dataSetType) throws OdaException {
		// assumes that this driver supports only one type of data set,
		// ignores the specified dataSetType
		return new DataSetMetaData(this);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang
	 * .String)
	 */
	@Override
	public IQuery newQuery(String dataSetType) throws OdaException {
		// assumes that this driver supports only one type of data set,
		// ignores the specified dataSetType
		if (!isOpen()) {
			throw new OdaException(Messages.getString("common_CONNECTION_HAS_NOT_OPEN")); //$NON-NLS-1$
		}
		ExcelFileQuery excelFileQuery = new ExcelFileQuery(connProperties);
		excelFileQuery.setAppContext(appContext);
		return excelFileQuery;
	}

	public ResourceIdentifiers getResourceIdentifiers() {
		return (ResourceIdentifiers) appContext.get(ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMaxQueries()
	 */
	@Override
	public int getMaxQueries() throws OdaException {
		return 0; // no limit
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#commit()
	 */
	@Override
	public void commit() throws OdaException {
		// do nothing; assumes no transaction support needed
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#rollback()
	 */
	@Override
	public void rollback() throws OdaException {
		// do nothing; assumes no transaction support needed
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setLocale(com.ibm.
	 * icu.util.ULocale)
	 */
	@Override
	public void setLocale(ULocale locale) throws OdaException {
		// do nothing; assumes no locale support
	}

}
