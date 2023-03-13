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
 * This class extends the abstract class TestDataSource by using ODA.JDBC
 * includes: jdbcDataSourceUtil jdbcOdaDataSource
 */
public class JDBCDataSource implements TestDataSource {
	/** delegate to JDBCDataSourceUtil for table creation and drop */
	private JDBCDataSourceUtil jdbcDataSourceUtil;

	/** delegate to dteDataSource for DtE datasource and data set */
	private JDBCOdaDataSource jdbcOdaDataSource;

	/**
	 * @return one instance
	 * @throws Exception
	 */
	public static TestDataSource newInstance() throws Exception {
		return new JDBCDataSource();
	}

	/**
	 * @throws Exception
	 */
	private JDBCDataSource() throws Exception {
		jdbcDataSourceUtil = new JDBCDataSourceUtil();
		jdbcOdaDataSource = new JDBCOdaDataSource(JDBCDataSourceUtil.getURL(), JDBCDataSourceUtil.getDriverClassName(),
				JDBCDataSourceUtil.getUser(), JDBCDataSourceUtil.getPassword());
	}

	/*
	 * @see testutil.TestDataSource#createTestTable(java.lang.String,
	 * java.lang.String, boolean)
	 */
	@Override
	public void createTable(String tableName, String metaInfo, boolean dropTable) throws SQLException {
		jdbcDataSourceUtil.createTable(tableName, metaInfo, dropTable);
	}

	/*
	 * @see testutil.TestDataSource#createTestProcedure(java.lang.String,
	 * java.lang.String, boolean)
	 */
	@Override
	public void createStoredProcedure(String proName, String metaInfo, boolean dropProc) throws SQLException {
		jdbcDataSourceUtil.createStoredProcedure(proName, metaInfo, dropProc);
	}

	/*
	 * @see testutil.TestDataSource#populateTestTable(java.lang.String,
	 * java.io.File)
	 */
	@Override
	public void populateTable(String testTableName, InputStream stream) throws SQLException, IOException {
		jdbcDataSourceUtil.populateTable(testTableName, stream);
	}

	/*
	 * @see testutil.TestDataSource#dropTable(java.lang.String)
	 */
	@Override
	public void dropTable(String tableName) throws SQLException {
		jdbcDataSourceUtil.dropTable(tableName);
	}

	/*
	 * @see testutil.TestDataSource#close(boolean)
	 */
	@Override
	public void close(boolean dropTable) throws SQLException {
		jdbcDataSourceUtil.close(dropTable);
	}

	/*
	 * @see testutil.TestDataSource#getExtendedDataSourceDesign()
	 */
	@Override
	public OdaDataSourceDesign getOdaDataSourceDesign() {
		return jdbcOdaDataSource.getOdaDataSourceDesign();
	}

	/*
	 * @see testutil.TestDataSource#getExtendedDataSetDesign()
	 */
	@Override
	public OdaDataSetDesign getOdaDataSetDesign() {
		return jdbcOdaDataSource.getOdaDataSetDesign();
	}

}
