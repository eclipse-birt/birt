/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Properties;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiTransform;
import org.eclipse.birt.report.data.oda.jdbc.JDBCDriverManager;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.ui.PlatformUI;

/**
 * Driver load class
 *
 * @since 3.3
 *
 */
public final class DriverLoader {
	private DriverLoader() {
	}

	/**
	 * Get a JDBC connection
	 *
	 * @param driverClassName  driver class name
	 * @param connectionString connection string
	 * @param userId           login user name
	 * @param password         login password
	 * @return new JDBC connection
	 * @throws SQLException
	 * @throws OdaException
	 */
	public static Connection getConnection(String driverClassName, String connectionString, String userId,
			String password) throws SQLException, OdaException {
		return getConnection(driverClassName, connectionString, userId, password, null);
	}

	/**
	 * Get a JDBC connection
	 *
	 * @param driverClassName  driver class name
	 * @param connectionString connection string
	 * @param userId           login user name
	 * @param password         login password
	 * @param props            connection properties
	 * @return new JDBC connection
	 * @throws SQLException
	 * @throws OdaException
	 */
	public static Connection getConnection(String driverClassName, String connectionString, String userId,
			String password, Properties props) throws SQLException, OdaException {
		return JDBCDriverManager.getInstance().getConnection(driverClassName, connectionString, userId, password, null,
				props);
	}

	/**
	 * Get a JDBC connection
	 *
	 * @param driverClassName  driver class name
	 * @param connectionString connection string
	 * @param userId           login user name
	 * @param password         login password
	 * @param props            connection properties
	 * @return new JDBC connection
	 * @throws SQLException
	 */
	public static Connection getConnectionWithExceptionTip(String driverClassName, String connectionString,
			String userId, String password, Properties props) throws SQLException {
		try {
			return JDBCDriverManager.getInstance().getConnection(driverClassName, connectionString, userId, password,
					null, props);
		} catch (Exception e) {
			ExceptionHandler.showException(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					JdbcPlugin.getResourceString("exceptionHandler.title.error"), e.getLocalizedMessage(), e);
			return null;
		}
	}

	static String escapeCharacters(String value) {
		final StringCharacterIterator iterator = new StringCharacterIterator(value);
		char character = iterator.current();
		final StringBuilder result = new StringBuilder();

		while (character != CharacterIterator.DONE) {
			if (character == '\\') {
				result.append("\\"); //$NON-NLS-1$
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();

	}

	/**
	 * Tests whether the given connection properties can be used to create a
	 * connection.
	 *
	 * @param driverClassName  the name of driver class
	 * @param connectionString the connection URL
	 * @param userId           the user id
	 * @param password         the pass word
	 * @return boolean whether could the connection being created
	 * @throws OdaException
	 */
	public static boolean testConnection(String driverClassName, String connectionString, String userId,
			String password) throws OdaException {
		return testConnection(driverClassName, connectionString, null, userId, password, new Properties());
	}

	/**
	 * Tests whether the given connection properties can be used to obtain a
	 * connection.
	 *
	 * @param driverClassName  the name of driver class
	 * @param connectionString the JDBC driver connection URL
	 * @param userId           the login user id
	 * @param password         the login password
	 * @param props            connection properties
	 * @return true if the the specified properties are valid to obtain a
	 *         connection; false otherwise
	 * @throws OdaException
	 */
	public static boolean testConnection(String driverClassName, String connectionString, String userId,
			String password, Properties props) throws OdaException {
		return testConnection(driverClassName, connectionString, null, userId, password, props);
	}

	/**
	 * Tests whether the given connection properties can be used to obtain a
	 * connection.
	 *
	 * @param driverClassName  the name of driver class
	 * @param connectionString the JDBC driver connection URL
	 * @param jndiNameUrl      the JNDI name to look up a Data Source name service;
	 *                         may be null or empty
	 * @param userId           the login user id
	 * @param password         the login password
	 * @return true if the the specified properties are valid to obtain a
	 *         connection; false otherwise
	 * @throws OdaException
	 */
	public static boolean testConnection(String driverClassName, String connectionString, String jndiNameUrl,
			String userId, String password) throws OdaException {
		return testConnection(driverClassName, connectionString, jndiNameUrl, userId, password, new Properties());
	}

	/**
	 * Tests whether the given connection properties can be used to obtain a
	 * connection.
	 *
	 * @param driverClassName  the name of driver class
	 * @param connectionString the JDBC driver connection URL
	 * @param jndiNameUrl      the JNDI name to look up a Data Source name service;
	 *                         may be null or empty
	 * @param userId           the login user id
	 * @param password         the login password
	 * @param props            connection properties
	 * @return true if the the specified properties are valid to obtain a
	 *         connection; false otherwise
	 * @throws OdaException
	 */
	public static boolean testConnection(String driverClassName, String connectionString, String jndiNameUrl,
			String userId, String password, Properties props) throws OdaException {
		return JDBCDriverManager.getInstance().testConnection(driverClassName, connectionString, jndiNameUrl, userId,
				password, props);
	}

	// bidi_hcg: if Bidi format is defined - perform required Bidi transformations
	// on connection properties before testing the connection
	/**
	 * Tests whether the given connection properties can be used to obtain a
	 * connection.
	 *
	 * @param driverClassName  the name of driver class
	 * @param connectionString the JDBC driver connection URL
	 * @param jndiNameUrl      the JNDI name to look up a Data Source name service;
	 *                         may be null or empty
	 * @param userId           the login user id
	 * @param password         the login password
	 * @param bidiFormatStr    BIDI format string
	 * @return true if the the specified properties are valid to obtain a
	 *         connection; false otherwise
	 * @throws OdaException
	 */
	public static boolean testConnection(String driverClassName, String connectionString, String jndiNameUrl,
			String userId, String password, String bidiFormatStr) throws OdaException {

		return testConnection(driverClassName, connectionString, jndiNameUrl, userId, password, bidiFormatStr,
				new Properties());
	}

	/**
	 * Tests whether the given connection properties can be used to obtain a
	 * connection.
	 *
	 * @param driverClassName  the name of driver class
	 * @param connectionString the JDBC driver connection URL
	 * @param jndiNameUrl      the JNDI name to look up a Data Source name service;
	 *                         may be null or empty
	 * @param userId           the login user id
	 * @param password         the login password
	 * @param bidiFormatStr    BIDI format string
	 * @param props
	 * @return true if the the specified properties are valid to obtain a
	 *         connection; false otherwise
	 * @throws OdaException
	 */
	public static boolean testConnection(String driverClassName, String connectionString, String jndiNameUrl,
			String userId, String password, String bidiFormatStr, Properties props) throws OdaException {

		userId = BidiTransform.transform(userId, BidiConstants.DEFAULT_BIDI_FORMAT_STR, bidiFormatStr);
		password = BidiTransform.transform(password, BidiConstants.DEFAULT_BIDI_FORMAT_STR, bidiFormatStr);
		connectionString = BidiTransform.transformURL(connectionString, BidiConstants.DEFAULT_BIDI_FORMAT_STR,
				bidiFormatStr);

		return testConnection(driverClassName, connectionString, jndiNameUrl, userId, password, props);
	}
}
