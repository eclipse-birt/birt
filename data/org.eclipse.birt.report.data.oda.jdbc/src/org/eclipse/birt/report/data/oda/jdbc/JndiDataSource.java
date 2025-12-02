/*
 *************************************************************************
 * Copyright (c) 2006, 2011 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.naming.Context;
import jakarta.naming.InitialContext;
import jakarta.sql.DataSource;

import org.eclipse.birt.report.data.oda.i18n.JdbcResourceHandle;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.util.ULocale;

/**
 * Internal implementation class for JNDI Data Source connection factory. <br>
 * This supports the use of a JNDI Name Service to look up a Data Source
 * resource factory to get a JDBC pooled connection.
 * <p>
 * The ODA JDBC data source definition includes a connection property for the
 * JNDI name to look up a Data Source name service. This optional property
 * expects a full name path, for use by a JNDI initial context to look up a Data
 * Source resource factory. A JNDI name path can be specific to individual JNDI
 * service provider, and/or custom data source configuration. For example,
 * "java:comp/env/jdbc/%dataSourceName%".
 * <p>
 * The ODA JDBC UI data source designer pages include a text field for user
 * input of the JNDI data source name.
 * <p>
 * <i>JNDI name service vs. JDBC driver URL</i>
 * <p>
 * Some JNDI service providers do not support client-side access.<br>
 * At design time, when using the BIRT report designer, a JDBC data set still
 * needs to be designed using direct access to a JDBC driver connection. The ODA
 * JDBC data set query builder continues to use a direct JDBC connection to
 * obtain its metadata. Only those design functions directly related to a data
 * source design, such as Test Connection and Preview Results of a data set,
 * would first attempt to use a JNDI name, if specified. And if not successful
 * for any reason, it falls back to use the JDBC driver URL.
 * <p>
 * Similarly at report runtime, such as during Report Preview, when a JNDI name
 * is specified, this oda.jdbc driver attempts to look up its JNDI data source
 * name service to get a pooled JDBC connection. If such lookup is not
 * successful for any reason, it falls back to use the JDBC driver URL directly
 * to create a JDBC connection.
 * <p>
 * <i>Context Environment Properties</i>
 * <p>
 * To simplify the task of setting up the JNDI initial context environment
 * required by individual JNDI application, the oda.jdbc JNDI feature supports
 * the use of a "jndi.properties" resource file, installed in the drivers
 * sub-folder of the oda.jdbc plugin. When deployed within a web application, it
 * looks for the file under the web application's folder tree in the oda.jdbc's
 * drivers sub-folder. <br>
 * Its use is optional. When such file is not found or problem reading from it,
 * an initial context adopts the default behavior to locate any JNDI resource
 * files, as defined by <code>jakarta.naming.Context</code>. <br>
 * Note that it is the user responsibility to configure the classpath to include
 * the classes referenced by the environment properties.
 * <p>
 * <i>oda.jdbc.driverinfo extensions</i>
 * <p>
 * As of BIRT 2.5.2, the default JNDI data source handling, if specified, takes
 * precedence over a custom connection factory implemented in an
 * oda.jdbc.driverinfo extension.
 */
class JndiDataSource implements IConnectionFactory {
	private static final String JNDI_PROPERTIES = "jndi.properties"; //$NON-NLS-1$

	private static final Logger sm_logger = Logger.getLogger(JndiDataSource.class.getName());
	private static final String sm_sourceClass = "JndiDataSource"; //$NON-NLS-1$
	private static JdbcResourceHandle sm_resourceHandle;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.data.oda.jdbc.IConnectionFactory#getConnection(java.
	 * lang.String, java.lang.String, java.util.Properties)
	 */
	@Override
	public Connection getConnection(String driverClass, String jndiNameUrl, Properties connectionProperties)
			throws SQLException {
		final String methodName = "getConnection"; //$NON-NLS-1$
		sm_logger.entering(sm_sourceClass, methodName, jndiNameUrl);

		// perform JNDI lookup to obtain resource manager connection factory
		Context initCtx = null;
		Object namedObject = null;
		try {
			initCtx = new InitialContext(getDriverJndiProperties());
			namedObject = initCtx.lookup(jndiNameUrl);
		} catch (Exception ex) {
			sm_logger.info(ex.toString());
			sm_logger.exiting(sm_sourceClass, methodName, null);

			SQLException sqlEx = new SQLException(ex.getLocalizedMessage());
			sqlEx.initCause(ex);
			throw sqlEx;
		} finally {
			closeContext(initCtx);
		}

		// check if specified url's object is of a DataSource type
		validateDataSourceType(namedObject, jndiNameUrl);

		// obtain a java.sql.Connection resource from the data source pool
		Connection conn = getDataSourceConnection((DataSource) namedObject, connectionProperties);

		sm_logger.exiting(sm_sourceClass, methodName, conn);
		return conn;
	}

	/**
	 * Invoke factory to obtain a java.sql.Connection resource from the data source
	 * pool.
	 */
	private Connection getDataSourceConnection(DataSource ds, Properties connProps) throws SQLException {
		Exception error = null;

		// First try to obtain connection without user credential.
		if (sm_logger.isLoggable(Level.FINER)) {
			sm_logger.finer("getDataSourceConnection: using getConnection() from data source pool."); //$NON-NLS-1$
		}

		try {
			return ds.getConnection();
		} catch (Exception ex) {
			error = ex;
			sm_logger.info(ex.toString());
		}

		// check if specified connection properties contain user authentication
		// properties
		String username = connProps.getProperty(JDBCDriverManager.JDBC_USER_PROP_NAME);
		String password = connProps.getProperty(JDBCDriverManager.JDBC_PASSWORD_PROP_NAME);

		// Try obtain connection with user credential if username/passwords are
		// available.
		if (username != null && username.length() > 0) // user name is explicitly specified
		{
			if (sm_logger.isLoggable(Level.FINER)) {
				sm_logger.finer(
						"getDataSourceConnection: using getConnection( username, password ) from data source pool."); //$NON-NLS-1$
			}

			try {
				return ds.getConnection(username, password);
			} catch (Exception ex) {
				error = ex;
				sm_logger.info(ex.toString());
			}
		}

		SQLException sqlEx = null;
		// All attempts failed, report error.
		if (error instanceof SQLException) {
			sqlEx = (SQLException) error;
		} else {
			sqlEx = new SQLException(error.getLocalizedMessage());
			sqlEx.initCause(error);
		}
		throw sqlEx;
	}

	/**
	 * Validate whether specified url's object is of a DataSource type.
	 *
	 * @throws SQLException if unexpected resource type is found
	 */
	private void validateDataSourceType(Object namedObject, String jndiNameUrl) throws SQLException {
		if (namedObject instanceof DataSource) {
			return; // is of expected resource type
		}

		// format exception message
		String localizedMsg = getMessage(ResourceConstants.CONN_GET_ERROR, jndiNameUrl);
		localizedMsg += ". "; //$NON-NLS-1$
		localizedMsg += getMessage(ResourceConstants.JNDI_INVALID_RESOURCE,
				(namedObject != null) ? namedObject.getClass().getName() : "null"); //$NON-NLS-1$

		if (sm_logger.isLoggable(Level.INFO)) {
			sm_logger.info(localizedMsg);
		}
		throw new SQLException(localizedMsg);
	}

	private void closeContext(Context ctx) {
		if (ctx == null) {
			return; // nothing to close
		}

		try {
			ctx.close();
		} catch (Exception e) {
			// log and ignore exception
			if (sm_logger.isLoggable(Level.INFO)) {
				sm_logger.info("closeContext(): " + e.toString()); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Obtains the JNDI initial context environment properties.
	 *
	 * @return the jndi properties specified in the plugin drivers sub-directory;
	 *         may return null if no such file exists or have problem reading file
	 */
	protected Properties getDriverJndiProperties() {
		File jndiPropFile = getDriverJndiPropertyFile();
		if (jndiPropFile == null) { // no readable properties file found
			return null;
		}

		Properties jndiProps = new Properties();
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(jndiPropFile);
			jndiProps.load(inputStream);
		} catch (Exception ex) {
			// log and ignore exception
			if (sm_logger.isLoggable(Level.INFO)) {
				sm_logger.info("getDriverJndiProperties(): " + ex.toString()); //$NON-NLS-1$
			}
			jndiProps = null;
		}

		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				if (sm_logger.isLoggable(Level.INFO)) {
					sm_logger.info("getDriverJndiProperties(): " + e.toString()); //$NON-NLS-1$
				}
				inputStream = null;
			}
		}

		if (sm_logger.isLoggable(Level.CONFIG)) {
			int propertyCount = (jndiProps == null) ? 0 : jndiProps.size();
			sm_logger.config("Driver JNDI property count: " + propertyCount); //$NON-NLS-1$
		}

		return jndiProps;
	}

	/**
	 * Finds and returns the file representation of the jndi.properties file in the
	 * oda.jdbc plugin's drivers sub-directory. Validates that the file exists and
	 * readable.
	 *
	 * @return the jndi.properties file that is readable and exists in the drivers
	 *         sub-directory
	 */
	protected File getDriverJndiPropertyFile() {
		final String methodName = "getDriverJndiPropertyFile() "; //$NON-NLS-1$
		File driversDir = null;
		try {
			driversDir = OdaJdbcDriver.getDriverDirectory();
		} catch (OdaException | IOException ioEx) {
			// log and ignore exception
			sm_logger.info(methodName + ioEx.toString());
		}

		if (driversDir == null || !driversDir.isDirectory()) {
			return null;
		}

		// jndi properties file in bundle's drivers sub-directory

		// TODO - add support of driver-specific property file name
		File jndiPropFile = new File(driversDir, JNDI_PROPERTIES);

		boolean canReadFile = false;
		try {
			canReadFile = (jndiPropFile.isFile() && jndiPropFile.canRead());
		} catch (SecurityException e) {
			// log and ignore exception
			sm_logger.info(methodName + e.toString());
		}

		if (sm_logger.isLoggable(Level.CONFIG)) {
			sm_logger.config(
					methodName + jndiPropFile.getAbsolutePath() + " canReadFile = " + Boolean.valueOf(canReadFile)); //$NON-NLS-1$
		}

		return canReadFile ? jndiPropFile : null;
	}

	/**
	 * Utility method to format externalized message, without using JDBCException.
	 */
	private String getMessage(String errorCode, String argument) {
		if (sm_resourceHandle == null) {
			sm_resourceHandle = new JdbcResourceHandle(ULocale.getDefault());
		}

		String msgText = sm_resourceHandle.getMessage(errorCode);
		if (argument == null) {
			return msgText;
		}
		return MessageFormat.format(msgText, new Object[] { argument });
	}

}
