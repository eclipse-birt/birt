/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.DatabaseMetaData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * The class implements the unit test for DataSourceMetaData
 *
 */
public class DataSourceMetaDataTest {

	/** ConnectionFactory used to create Connection */
	OdaJdbcDriver connFact;

	/** Connection object, used to create statement, the opened one */
	Connection conn1;

	/** DataSourceMetaData object for the opened Connection. */
	DataSourceMetaData dbMeta1;

	/** Connection object, used to create statement, the not opened one */
	Connection conn2;

	/** DataSourceMetaData object for the not opened Connection. */
	DataSourceMetaData dbMeta2;

	/*
	 * @see TestCase#setUp()
	 */
	@Before
	public void dataSourceMetaDataSetUp() throws Exception {

		/* Get one opened Connection. */
		conn1 = TestUtil.openConnection();

		/* Get one cloased Connection. */
		conn2 = TestUtil.openConnection();
		conn2.close();

		dbMeta1 = (DataSourceMetaData) conn1.getMetaData("");// since the
		// datasourceType
		// is ignored.

		dbMeta2 = (DataSourceMetaData) conn2.getMetaData(""); // since the
		// datasourceType
		// is ignored.

	}

	/*
	 * @see TestCase#tearDown()
	 */
	@After
	public void dataSourceMetaDataTearDown() throws Exception {
		conn1.close();
	}

	/*
	 * Class under test for constructor DataSourceMetaData(IConnection,
	 * java.sql.DatabaseMetaData)
	 */
	@Test
	public void testDataSourceMetaData() throws Exception {
		DataSourceMetaData tempMeta = new DataSourceMetaData(new Connection(),
				TestUtil.openJDBCConnection().getMetaData());
		assertNotNull(tempMeta);

	}

	@Test
	public void testGetConnection() throws Exception {

		assertEquals(dbMeta1.getConnection(), conn1);
		assertEquals(dbMeta2.getConnection(), conn2);

	}

	@Test
	public void testGetDataSourceObjects() throws Exception {
		assertNull(dbMeta1.getDataSourceObjects("", "", "", ""));
		assertNull(dbMeta2.getDataSourceObjects("", "", "", ""));

	}

	@Test
	public void testGetDataSourceMajorVersion() throws Exception {
		/*
		 * assertEquals( dbMeta1.getDataSourceMajorVersion( ),
		 * ConnectionMetaData.DRIVER_MAJOR_VERSION ); assertEquals(
		 * dbMeta2.getDataSourceMajorVersion( ), ConnectionMetaData.DRIVER_MAJOR_VERSION
		 * );
		 */
	}

	@Test
	public void testGetDataSourceMinorVersion() throws Exception {
		/*
		 * assertEquals( dbMeta1.getDataSourceMinorVersion( ),
		 * ConnectionMetaData.DRIVER_MINOR_VERSION ); assertEquals(
		 * dbMeta2.getDataSourceMinorVersion( ), ConnectionMetaData.DRIVER_MINOR_VERSION
		 * );
		 */
	}

	@Test
	public void testGetDataSourceProductName() throws Exception {
		/*
		 * assertEquals( dbMeta1.getDataSourceProductName( ),
		 * ConnectionMetaData.DRIVER_NAME ); assertEquals(
		 * dbMeta2.getDataSourceProductName( ), ConnectionMetaData.DRIVER_NAME );
		 */
	}

	@Test
	public void testGetDataSourceProductVersion() throws Exception {
		/*
		 * assertEquals( dbMeta1.getDataSourceProductVersion( ),
		 * ConnectionMetaData.DRIVER_VERSION ); assertEquals(
		 * dbMeta2.getDataSourceProductVersion( ), ConnectionMetaData.DRIVER_VERSION );
		 */
	}

	@Test
	public void testGetSQLStateType() throws Exception {
		/*
		 * when the Connection is not open, the dbMeta is null and should throw
		 * exception for the getSQLStateType.
		 */
		try {
			this.dbMeta2.getSQLStateType();
			fail("call getSQLStateType from DataSourceMetaData which is from the non-opened connection should throw exception.");
		} catch (JDBCException e) {

		}

		java.sql.Connection jdbcConn = TestUtil.openJDBCConnection();

		DatabaseMetaData dbMeta = jdbcConn.getMetaData();

		if (dbMeta.getSQLStateType() == DatabaseMetaData.sqlStateSQL99) {
			assertEquals(this.dbMeta1.getSQLStateType(), DataSourceMetaData.sqlStateSQL99);

		} else {
			assertEquals(this.dbMeta1.getSQLStateType(), DataSourceMetaData.sqlStateXOpen);

		}

	}

	@Test
	public void testSupportsMultipleOpenResults() throws Exception {
		assertFalse(dbMeta1.supportsMultipleOpenResults());
		assertFalse(dbMeta2.supportsMultipleOpenResults());

	}

	@Test
	public void testSupportsMultipleResultSets() throws Exception {
		assertTrue(dbMeta1.supportsMultipleResultSets());
		assertTrue(dbMeta2.supportsMultipleResultSets());
	}

	@Test
	public void testSupportsNamedResultSets() throws Exception {
		assertTrue(dbMeta1.supportsNamedResultSets());
		assertTrue(dbMeta2.supportsNamedResultSets());

	}

	@Test
	public void testSupportsNamedParameters() throws Exception {
		assertFalse(dbMeta1.supportsNamedParameters());
		assertFalse(dbMeta2.supportsNamedParameters());
	}

	@Test
	public void testSupportsInParameters() throws Exception {
		assertTrue(dbMeta1.supportsInParameters());
		assertTrue(dbMeta2.supportsInParameters());
	}

	@Test
	public void testSupportsOutParameters() throws Exception {
		assertTrue(dbMeta1.supportsOutParameters());
		assertTrue(dbMeta2.supportsOutParameters());
	}

	@Test
	public void testGetSortMode() throws Exception {
		assertEquals(dbMeta1.getSortMode(), DataSourceMetaData.sortModeNone);
		assertEquals(dbMeta2.getSortMode(), DataSourceMetaData.sortModeNone);
	}

}
