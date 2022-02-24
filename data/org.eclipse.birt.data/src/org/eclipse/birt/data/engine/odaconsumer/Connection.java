/*
 *****************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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
 ******************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.util.Hashtable;
import java.util.logging.Level;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.PropertySecurity;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

import com.ibm.icu.util.ULocale;

/**
 * A runtime connection of a specific data source extension.
 */
public class Connection extends ExceptionHandler {
	private String m_dataSourceId;
	private IConnection m_connection;
	private Hashtable<String, DataSetCapabilities> m_cachedDsMetaData;

	// trace logging variables
	private static String sm_className = Connection.class.getName();

	Connection(IConnection connection, String dataSourceId) throws OdaException {
		super(sm_className);
		final String methodName = "Connection"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { connection, dataSourceId });

		assert (connection != null && connection.isOpen());
		m_dataSourceId = dataSourceId;
		m_connection = connection;

		getLogger().exiting(sm_className, methodName, this);
	}

	/**
	 * Checks whether this has an open connection.
	 * 
	 * @return true if this connection is open; false otherwise
	 * @throws DataException if data source error occurs
	 * @since 2.5.2
	 */
	public boolean isOpen() throws DataException {
		final String methodName = "isOpen"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		try {
			boolean ret = m_connection.isOpen();

			getLogger().exiting(sm_className, methodName, Boolean.valueOf(ret));
			return ret;
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_CHECK_CONN_ISOPEN, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, ResourceConstants.CANNOT_CHECK_CONN_ISOPEN, methodName);
		}
		return false;
	}

	/**
	 * Returns the maximum number of active queries of any data set types that the
	 * driver can support per active connection.
	 * 
	 * @return the maximum number of any type of queries that can be supported
	 *         concurrently, or 0 if there is no limit or the limit is unknown.
	 * @throws DataException if data source error occurs.
	 */
	public int getMaxQueries() throws DataException {
		final String methodName = "getMaxQueries"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		try {
			int ret = m_connection.getMaxQueries();

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_MAX_QUERIES, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName, "Cannot get max queries.", ex); //$NON-NLS-1$
		}
		return 0;
	}

	/**
	 * Returns the <code>DataSetCapabilities</code> based on the data set type.
	 * 
	 * @param dataSetType name of the data set type.
	 * @return the <code>DataSetCapabilities</code> instance reflecting the
	 *         specified data set type.
	 * @throws DataException if data source error occurs.
	 */
	public DataSetCapabilities getMetaData(String dataSetType) throws DataException {
		final String methodName = "getMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, dataSetType);

		String cachedKey = (dataSetType == null) ? getDataSourceId() : dataSetType;

		DataSetCapabilities capabilities = getCachedDsMetaData().get(cachedKey);

		if (capabilities == null) {
			IDataSetMetaData dsMetaData = null;
			try {
				dsMetaData = m_connection.getMetaData(dataSetType);
			} catch (OdaException ex) {
				throwException(ex, ResourceConstants.CANNOT_GET_DS_METADATA, dataSetType, methodName);
			} catch (UnsupportedOperationException ex) {
				throwException(ex, ResourceConstants.CANNOT_GET_DS_METADATA, dataSetType, methodName);
			}

			capabilities = new DataSetCapabilities(dsMetaData);
			getCachedDsMetaData().put(cachedKey, capabilities);
		}

		getLogger().exiting(sm_className, methodName, capabilities);

		return capabilities;
	}

	/**
	 * Creates a {@link PreparedStatement} instance of the specified data set type
	 * and query text.
	 * 
	 * @param query       the statement query text to be prepared and executed
	 * @param dataSetType name of the data set type
	 * @return a {@link PreparedStatement} of the specified type with the specified
	 *         query text.
	 * @throws DataException if data source error occurs
	 */
	public PreparedStatement prepareStatement(String query, String dataSetType) throws DataException {
		return prepareStatement(query, dataSetType, null);
	}

	/**
	 * Creates a {@link PreparedStatement} instance of the specified data set type
	 * with the query text and query specification.
	 * 
	 * @param query       the statement query text to be prepared and executed
	 * @param dataSetType name of the data set type
	 * @param querySpec   query specification for the query preparation; may be null
	 * @return a {@link PreparedStatement} of the specified type with the specified
	 *         query text and query specification
	 * @throws DataException if data source error occurs
	 */
	public PreparedStatement prepareStatement(String query, String dataSetType, QuerySpecification querySpec)
			throws DataException {
		final String methodName = "prepareStatement(String,String,QuerySpecification)"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { query, dataSetType });

		IQuery statement = prepareOdaQuery(query, dataSetType, querySpec);
		PreparedStatement ret = (new PreparedStatement(statement, dataSetType, this, query));

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	/**
	 * Specifies the locale setting for all locale-sensitive tasks in this
	 * connection. An optional method. This setting, if specified, overrides the
	 * driver's default locale setting.
	 * 
	 * @param locale
	 * @throws DataException
	 */
	public void setLocale(ULocale locale) throws DataException {
		final String methodName = "setLocale(ULocale)"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		try {
			m_connection.setLocale(locale);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_SET_CONN_LOCALE, locale, methodName);
		} catch (UnsupportedOperationException ex) {
			// log warning and ignore exception
			getLogger().logp(Level.WARNING, sm_className, methodName,
					"Unable to set locale: " + locale + ". Using default locale instead.", ex); //$NON-NLS-1$ //$NON-NLS-2$
		}

		getLogger().exiting(sm_className, methodName);
	}

	/**
	 * Closes this <code>Connection</code>.
	 * 
	 * @throws DataException if data source error occurs.
	 */
	public void close() throws DataException {
		final String methodName = "close"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		try {
			m_connection.close();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_CLOSE_CONNECTION, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.WARNING, sm_className, methodName, "Cannot close connection.", ex); //$NON-NLS-1$
		}

		getLogger().exiting(sm_className, methodName);
	}

	// cache the metadata since it's the same for the lifetime of this connection,
	// and we'll lazily instantiate it since it may not be needed
	private Hashtable<String, DataSetCapabilities> getCachedDsMetaData() {
		if (m_cachedDsMetaData == null)
			m_cachedDsMetaData = PropertySecurity.createHashtable();

		return m_cachedDsMetaData;
	}

	String getDataSourceId() {
		return m_dataSourceId;
	}

	IQuery prepareOdaQuery(String query, String dataSetType, QuerySpecification querySpec) throws DataException {
		final String methodName = "prepareOdaQuery"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { query, dataSetType, querySpec });

		try {
			assert (m_connection.isOpen());
			IQuery statement = m_connection.newQuery(dataSetType);

			// set the query spec, if exists, before prepare
			setOdaQuerySpec(statement, querySpec);

			statement.prepare(query);

			getLogger().exiting(sm_className, methodName, statement);

			return statement;
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_PREPARE_STATEMENT, new Object[] { query, dataSetType },
					methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, ResourceConstants.CANNOT_PREPARE_STATEMENT, new Object[] { query, dataSetType },
					methodName);
		}
		return null;
	}

	private void setOdaQuerySpec(IQuery statement, QuerySpecification querySpec) throws OdaException {
		final String methodName = "setOdaQuerySpec"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, querySpec);

		try {
			if (querySpec != null)
				statement.setSpecification(querySpec);
		} catch (UnsupportedOperationException ex) {
			// log and ignore optional processing, so not to stop query preparation
			getLogger().logp(Level.FINE, sm_className, methodName,
					"Ignoring the UnsupportedOperationException thrown by ODA driver (" + getDataSourceId() + //$NON-NLS-1$
							") on IQuery#setSpecification.  This call is optional and does not affect query processing."); //$NON-NLS-1$
		}

		getLogger().exiting(sm_className, methodName);
	}

}
