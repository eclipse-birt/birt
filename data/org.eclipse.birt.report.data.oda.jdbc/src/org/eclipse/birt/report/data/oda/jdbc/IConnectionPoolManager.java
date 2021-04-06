/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public void closeConnection(java.sql.Connection connection) throws OdaException, SQLException;
}
