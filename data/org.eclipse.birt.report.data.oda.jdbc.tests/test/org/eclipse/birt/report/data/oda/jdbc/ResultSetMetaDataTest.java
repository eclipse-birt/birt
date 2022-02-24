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

import java.sql.Types;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * Test case for ResultSetMetaData
 * 
 */
public class ResultSetMetaDataTest {

	/** Connection object, used to create statement stmt1 */
	private Connection conn1 = null;

	/** Connection object, used to create statement stmt2 */
	private Connection conn2 = null;

	/** Statement object, used to create ResultSet */
	private Statement stmt1 = null;

	/** Statement object, used to get ResultSetMetaData rsmd_Statement */
	private Statement stmt2 = null;

	/** ResultSet object, used to get ResultSetMetaData rsmd_ResultSet */
	private ResultSet rs = null;

	/** the result set meta data to test get from ResultSet */
	private ResultSetMetaData rsmd_ResultSet = null;

	/** the result set meta data to test get from Statement */
	private ResultSetMetaData rsmd_Statement = null;

	/** JDBC Connection object, used to create statement jdbcStmt */
	private java.sql.Connection jdbcConn1 = null;

	/** JDBC Connection object, used to create PreparedStatement jdbcPreparedStmt */
	private java.sql.Connection jdbcConn2 = null;

	/** JDBC Statement object, used to create ResultSet jdbcRs */
	private java.sql.Statement jdbcStmt = null;

	/**
	 * JDBC PreparedStatement object, used to create ResultSetMetaData
	 * jdbcRsmd_PreparedStmt
	 */
	private java.sql.PreparedStatement jdbcPreparedStmt = null;

	/**
	 * JDBC ResultSet object, used to create ResultSetMetaData jdbcRsmd_ResultSet
	 */
	private java.sql.ResultSet jdbcRs = null;

	/**
	 * the JDBC result set meta data to compare with ,which is get from ResultSet
	 */
	private java.sql.ResultSetMetaData jdbcRsmd_ResultSet = null;

	/**
	 * the JDBC result set meta data to compare with ,which is get from
	 * PreparedStatement
	 */
	private java.sql.ResultSetMetaData jdbcRsmd_PreparedStmt = null;

	/*
	 * @see TestCase#setUp()
	 */
	@Before
	public void resultSetMetaDataSetUp() throws Exception {
		TestUtil.createTestData();

		conn1 = TestUtil.openConnection();
		conn2 = TestUtil.openConnection();
		stmt1 = (Statement) conn1.newQuery("");
		stmt2 = (Statement) conn2.newQuery("");
		String sql = "select * from " + TestUtil.TABLE_NAME;

		stmt1.prepare(sql);
		rs = (ResultSet) stmt1.executeQuery();
		rsmd_ResultSet = (ResultSetMetaData) rs.getMetaData();

		stmt2.prepare(sql);
		rsmd_Statement = (ResultSetMetaData) stmt2.getMetaData();

		jdbcConn1 = TestUtil.openJDBCConnection();
		jdbcConn2 = TestUtil.openJDBCConnection();

		jdbcStmt = jdbcConn1.createStatement();
		jdbcRs = jdbcStmt.executeQuery(sql);
		jdbcRsmd_ResultSet = jdbcRs.getMetaData();

		jdbcPreparedStmt = jdbcConn2.prepareStatement(sql);
		jdbcRsmd_PreparedStmt = jdbcPreparedStmt.getMetaData();

	}

	/*
	 * @see TestCase#tearDown()
	 */
	@After
	public void resultSetMetaDataTearDown() throws Exception {
		rs.close();
		stmt1.close();
		stmt2.close();
		conn1.close();
		conn2.close();

		jdbcRs.close();
		jdbcStmt.close();
		jdbcConn1.close();
		jdbcConn2.close();

		TestUtil.deleteTestData();
	}

	@Test
	public void testGetColumnCount_ResultSet() throws Exception {
		assertEquals(rsmd_ResultSet.getColumnCount(), jdbcRsmd_ResultSet.getColumnCount());
	}

	@Test
	public void testGetColumnName_ResultSet() throws Exception {
		for (int i = 1; i < rsmd_ResultSet.getColumnCount() + 1; i++) {
			System.out.println("i = " + i);
			System.out.println("rsmd_ResultSet getColumnName  = " + rsmd_ResultSet.getColumnName(i));
			assertEquals(rsmd_ResultSet.getColumnName(i), jdbcRsmd_ResultSet.getColumnName(i));
		}
	}

	@Test
	public void testGetColumnLabel_ResultSet() throws Exception {
		for (int i = 1; i < rsmd_ResultSet.getColumnCount() + 1; i++) {
			assertEquals(rsmd_ResultSet.getColumnLabel(i), jdbcRsmd_ResultSet.getColumnLabel(i));
		}
	}

	@Test
	public void testGetColumnType_ResultSet() throws Exception {
		for (int i = 1; i < rsmd_ResultSet.getColumnCount() + 1; i++) {
			if (jdbcRsmd_ResultSet.getColumnType(i) == Types.DECIMAL) {
				assertEquals(rsmd_ResultSet.getColumnType(i),
						testDataTypeConversion(jdbcRsmd_ResultSet.getColumnType(i), jdbcRsmd_ResultSet.getScale(i),
								jdbcRsmd_ResultSet.getPrecision(i)));
			} else {
				assertEquals(rsmd_ResultSet.getColumnType(i), jdbcRsmd_ResultSet.getColumnType(i));
			}
		}
	}

	@Test
	public void testGetColumnTypeName_ResultSet() throws Exception {
		for (int i = 1; i < rsmd_ResultSet.getColumnCount() + 1; i++) {
			assertEquals(rsmd_ResultSet.getColumnTypeName(i), jdbcRsmd_ResultSet.getColumnTypeName(i));
		}
	}

	@Test
	public void testGetColumnDisplayLength_ResultSet() throws Exception {
		for (int i = 1; i < rsmd_ResultSet.getColumnCount() + 1; i++) {
			assertEquals(rsmd_ResultSet.getColumnDisplayLength(i), jdbcRsmd_ResultSet.getColumnDisplaySize(i));
		}
	}

	@Test
	public void testGetPrecision_ResultSet() throws Exception {
		for (int i = 1; i < rsmd_ResultSet.getColumnCount() + 1; i++) {
			assertEquals(rsmd_ResultSet.getPrecision(i), jdbcRsmd_ResultSet.getPrecision(i));
		}
	}

	@Test
	public void testGetScale_ResultSet() throws Exception {
		for (int i = 1; i < rsmd_ResultSet.getColumnCount() + 1; i++) {
			assertEquals(rsmd_ResultSet.getScale(i), jdbcRsmd_ResultSet.getScale(i));
		}
	}

	@Test
	public void testIsNullable_ResultSet() throws Exception {
		for (int i = 1; i < rsmd_ResultSet.getColumnCount() + 1; i++) {
			assertEquals(rsmd_ResultSet.isNullable(i), jdbcRsmd_ResultSet.isNullable(i));
		}
	}

	@Test
	public void testGetColumnCount_Statment() throws Exception {
		assertEquals(rsmd_Statement.getColumnCount(), jdbcRsmd_PreparedStmt.getColumnCount());
	}

	@Test
	public void testGetColumnName_Statment() throws Exception {
		for (int i = 1; i < rsmd_Statement.getColumnCount() + 1; i++) {
			assertEquals(rsmd_Statement.getColumnName(i), jdbcRsmd_PreparedStmt.getColumnName(i));
		}
	}

	@Test
	public void testGetColumnLabel_Statment() throws Exception {
		for (int i = 1; i < rsmd_Statement.getColumnCount() + 1; i++) {
			assertEquals(rsmd_Statement.getColumnLabel(i), jdbcRsmd_PreparedStmt.getColumnLabel(i));
		}
	}

	@Test
	public void testGetColumnType_Statment() throws Exception {
		for (int i = 1; i < rsmd_Statement.getColumnCount() + 1; i++) {
			if (jdbcRsmd_PreparedStmt.getColumnType(i) == Types.DECIMAL) {
				assertEquals(rsmd_Statement.getColumnType(i),
						testDataTypeConversion(jdbcRsmd_PreparedStmt.getColumnType(i),
								jdbcRsmd_PreparedStmt.getScale(i), jdbcRsmd_PreparedStmt.getPrecision(i)));
			} else {
				assertEquals(rsmd_Statement.getColumnType(i), jdbcRsmd_PreparedStmt.getColumnType(i));
			}
		}
	}

	public int testDataTypeConversion(int reType, int scale, int precision) {
		if ((scale == 0) && (precision > 0) && (precision <= 9)) {
			reType = Types.INTEGER;
		} else if (precision > 9 && precision < 308) {
			reType = Types.DOUBLE;
		} else if (precision >= 308) {
			reType = Types.DECIMAL;
		}

		return reType;
	}

	@Test
	public void testGetColumnTypeName_Statment() throws Exception {
		for (int i = 1; i < rsmd_Statement.getColumnCount() + 1; i++) {
			assertEquals(rsmd_Statement.getColumnTypeName(i), jdbcRsmd_PreparedStmt.getColumnTypeName(i));
		}
	}

	@Test
	public void testGetColumnDisplayLength_Statment() throws Exception {
		for (int i = 1; i < rsmd_Statement.getColumnCount() + 1; i++) {
			assertEquals(rsmd_Statement.getColumnDisplayLength(i), jdbcRsmd_PreparedStmt.getColumnDisplaySize(i));
		}
	}

	@Test
	public void testGetPrecision_Statment() throws Exception {
		for (int i = 1; i < rsmd_Statement.getColumnCount() + 1; i++) {
			assertEquals(rsmd_Statement.getPrecision(i), jdbcRsmd_PreparedStmt.getPrecision(i));
		}
	}

	@Test
	public void testGetScale_Statment() throws Exception {
		for (int i = 1; i < rsmd_Statement.getColumnCount() + 1; i++) {
			assertEquals(rsmd_Statement.getScale(i), jdbcRsmd_PreparedStmt.getScale(i));
		}
	}

	@Test
	public void testIsNullable_Statment() throws Exception {
		for (int i = 1; i < rsmd_Statement.getColumnCount() + 1; i++) {
			assertEquals(rsmd_Statement.isNullable(i), jdbcRsmd_PreparedStmt.isNullable(i));
		}
	}
}
