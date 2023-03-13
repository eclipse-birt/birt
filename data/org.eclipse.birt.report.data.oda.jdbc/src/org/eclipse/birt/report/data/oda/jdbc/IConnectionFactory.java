/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.report.data.oda.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Defines a source for obtaining JDBC connections. An extension may implement
 * this interface to provide connections to its custom data source as an
 * alternative to the java.sql.DriverManager facility.
 */
public interface IConnectionFactory {
	String DRIVER_CLASSPATH = "OdaJDBCDriverClassPath";

	String PASS_IN_CONNECTION = "OdaJDBCDriverPassInConnection";

	String CLOSE_PASS_IN_CONNECTION = "OdaJDBCDriverPassInConnectionCloseAfterUse";

	/**
	 * Establishes a connection to the given database URL.
	 *
	 * @param driverClass          driverClass defined in the extension
	 * @param url                  a database url
	 * @param connectionProperties a list of arbitrary string tag/value pairs as
	 *                             connection arguments; normally at least a "user"
	 *                             and "password" property should be included
	 * @return a Connection to the URL
	 * @exception SQLException if a database access error occurs
	 */
	Connection getConnection(String driverClass, String url, Properties connectionProperties) throws SQLException;

}
