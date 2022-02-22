/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.OdaException;

public interface IConnectionPoolManager {

	/**
	 * Get connection from IConnectionPoolManager.
	 *
	 * @param driverClass
	 * @param url
	 * @param connectionProps
	 * @param driverClassPath
	 * @param appContext
	 * @return
	 * @throws SQLException
	 * @throws OdaException
	 */
	java.sql.Connection getConnection(String driverClass, String url, Properties connectionProps,
			Collection<String> driverClassPath, Map appContext) throws SQLException, OdaException;

	void closeConnection(java.sql.Connection connection) throws OdaException, SQLException;
}
