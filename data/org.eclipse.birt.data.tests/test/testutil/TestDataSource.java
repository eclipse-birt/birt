/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
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

package testutil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;

/**
 * This abstract class is used to define the interface and shared behaviors of a
 * "data base visitor".
 *
 */
public interface TestDataSource {

	// JDBC test part
	/**
	 * Create table in test data source
	 *
	 * @param tableName
	 * @param createSql
	 * @param isOverwrite
	 * @throws SQLException
	 */
	void createTable(String tableName, String createSql, boolean isOverwrite) throws SQLException;

	/**
	 * Create stored procedure in test data source
	 *
	 * @param proName
	 * @param createSql
	 * @param isOverwrite
	 * @throws SQLException
	 */
	void createStoredProcedure(String proName, String createSql, boolean isOverwrite) throws SQLException;

	/**
	 * Insert data into table of test data source
	 *
	 * @param testTableName
	 * @param testTableDataFile
	 * @throws SQLException
	 * @throws IOException
	 */
	void populateTable(String testTableName, InputStream testTableDataFile) throws SQLException, IOException;

	/**
	 * Drop table from test data source
	 *
	 * @param tableName
	 * @throws SQLException
	 */
	void dropTable(String tableName) throws SQLException;

	/**
	 * Close data source
	 *
	 * @param dropTable
	 * @throws SQLException
	 */
	void close(boolean dropTable) throws SQLException;

	// Oda part
	/**
	 * @return ODA data source
	 */
	OdaDataSourceDesign getOdaDataSourceDesign();

	/**
	 * @return ODA data set
	 */
	OdaDataSetDesign getOdaDataSetDesign();

}
