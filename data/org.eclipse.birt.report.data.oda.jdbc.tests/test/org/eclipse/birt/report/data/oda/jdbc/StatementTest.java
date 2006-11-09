/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import junit.framework.TestCase;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Test case for Statement
 * 
 */
public class StatementTest extends TestCase
{

	private final static String SELECT_SQL = "select * from "
			+ TestUtil.TABLE_NAME;

	private final static String SELECT_SQL_W_PARAMS = "select * from "
			+ TestUtil.TABLE_NAME +
			" WHERE   col0 = ? " +
			"     AND col1 = ? " +
			"     AND col2 = ? " +
			"     AND col3 = ? " +
			"     AND col4 = ? " +
			"     AND col5 = ? " +
			"     AND col6 = ? ";

	private Connection conn = null;

	/** The Statement to test */
	private Statement stmt = null;

	private java.sql.Connection jdbcConn = null;

	/** The JDBC statement to compare with */
	private java.sql.Statement jdbcStmt = null;

	/** The JDBC prepared statement to compare with */
	private java.sql.PreparedStatement jdbcPrepStmt = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		TestUtil.createTestData( );
		conn = TestUtil.openConnection( );
		stmt = (Statement) conn.newQuery( "" );
		jdbcConn = TestUtil.openJDBCConnection( );
		jdbcStmt = jdbcConn.createStatement( );
		jdbcPrepStmt = jdbcConn.prepareStatement( SELECT_SQL );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		stmt.close( );
		conn.close( );
		jdbcStmt.close( );
		jdbcPrepStmt.close( );
		jdbcConn.close( );
		TestUtil.deleteTestData( );
		super.tearDown( );
	}

	/**
	 * Constructor for StatementTest.
	 * 
	 * @param arg0
	 */
	public StatementTest( String arg0 )
	{
		super( arg0 );
	}

	public void testPrepare( ) throws Exception
	{
		stmt.prepare( SELECT_SQL );
		assertTrue( stmt.execute( ) );
	}

	public void testClose( )
	{
		try
		{
			// close multiple times should have no effect
			stmt.close( );
			stmt.close( );
		}
		catch ( Exception e )
		{
			fail( "Should not throw exception. Exception message: "
					+ e.getLocalizedMessage( ) );
		}
		try
		{
			stmt.prepare( SELECT_SQL );
			stmt.executeQuery();
			stmt.close( );
		}
		catch ( Exception e )
		{
			fail( "Should not throw exception. Exception message: "
					+ e.getLocalizedMessage( ) );
		}
		try
		{
			stmt.getMetaData( );
			fail( "Statement already closed. Should throw exception." );
		}
		catch ( Exception e )
		{
		}
	}

	public void testSetGetMaxRows( ) throws Exception
	{
		assertEquals( stmt.getMaxRows( ), -1 );
		stmt.setMaxRows( 1 );
		stmt.prepare( SELECT_SQL );
		ResultSet rs = (ResultSet) stmt.executeQuery();
		assertTrue( rs.next( ) );
		assertFalse( rs.next( ) );
		assertEquals( stmt.getMaxRows( ), 1 );
	}

	/*
	 * Class under test for IResultSetMetaData getMetaData()
	 */
	public void testGetMetaData( ) throws Exception
	{
		try
		{
			stmt.getMetaData( );
			fail( "Should throw DriverException" );
		}
		catch ( OdaException e )
		{
			assertTrue( e instanceof JDBCException );
		}
		stmt.prepare( SELECT_SQL );
		assertNotNull( stmt.getMetaData( ) );
	}

	/*
	 * Class under test for IResultSet executeQuery()
	 */
	public void testExecuteQuery( ) throws Exception
	{
		stmt.prepare( SELECT_SQL );
		assertNotNull( stmt.executeQuery( ) );
//		assertNotNull( stmt.getResultSet( ) );
	}

	/*
	 * Class under test for void setInt(int, int)
	 */
	public void testSetIntintint( ) throws Exception
	{
		stmt.prepare( SELECT_SQL + " where col3 = ?" );
		stmt.setInt( 1, 0 );
		ResultSet rs = (ResultSet) stmt.executeQuery( );
		assertTrue( rs.next( ) );
	}

	/*
	 * Class under test for void setDouble(int, double)
	 */
	public void testSetDoubleintdouble( ) throws Exception
	{
		stmt.prepare( SELECT_SQL + " where col2 = ?" );
		stmt.setDouble( 1, 0.0 );
		ResultSet rs = (ResultSet) stmt.executeQuery( );
		assertTrue( rs.next( ) );
	}

	/*
	 * Class under test for void setBigDecimal(int, BigDecimal)
	 */
	public void testSetBigDecimalintBigDecimal( ) throws Exception
	{
		stmt.prepare( SELECT_SQL + " where col0 = ?" );
		stmt.setBigDecimal( 1, new BigDecimal( "1111" ) );
		ResultSet rs = (ResultSet) stmt.executeQuery( );
		assertTrue( rs.next( ) );
	}

	/*
	 * Class under test for void setString(int, String)
	 */
	public void testSetStringintString( ) throws Exception
	{
		stmt.prepare( SELECT_SQL + " where col4 = ?" );
		stmt.setString( 1, "00" );
		ResultSet rs = (ResultSet) stmt.executeQuery( );
		assertTrue( rs.next( ) );
	}

	/*
	 * Class under test for void setDate(int, Date)
	 */
	public void testSetDateintDate( ) throws Exception
	{
		stmt.prepare(SELECT_SQL + " where col1 = ?");
		stmt.setDate(1, Date.valueOf("2000-01-01"));
		ResultSet rs = (ResultSet)stmt.executeQuery();
		assertTrue(rs.next());
	}

	/*
	 * Class under test for void setTime(int, Time)
	 */
	public void testSetTimeintTime( ) throws Exception
	{
		stmt.prepare(SELECT_SQL + " where col5 = ?");
		stmt.setTime(1, Time.valueOf("12:00:01"));
		ResultSet rs = (ResultSet)stmt.executeQuery();
		assertTrue(rs.next());
	}

	/*
	 * Class under test for void setTimestamp(int, Timestamp)
	 */
	public void testSetTimestampintTimestamp( ) throws Exception
	{
		stmt.prepare( SELECT_SQL + " where col6 = ?" );
		stmt.setTimestamp( 1, Timestamp.valueOf("2000-01-01 12:00:00.0000"));
		ResultSet rs = (ResultSet) stmt.executeQuery( );
		assertTrue( rs.next( ) );
	}

	public void testGetParameterMetaData( ) throws Exception
	{
		stmt.prepare( SELECT_SQL_W_PARAMS );
		jdbcPrepStmt = jdbcConn.prepareStatement( SELECT_SQL_W_PARAMS );
		
		IParameterMetaData md = stmt.getParameterMetaData();
		assertNotNull( md);
		
		for ( int i = 1; i <= 7; i++ )
		{
			assertEquals( md.getParameterType( i ), 
					jdbcPrepStmt.getParameterMetaData( ).getParameterType( i ) );
		}
	}

	public void testClearInParameters() throws Exception
	{
		stmt.prepare( SELECT_SQL + " where col6 = ?" );
		stmt.setTimestamp( 1, Timestamp.valueOf("2000-01-01 12:00:00.0000") );
		ResultSet rs = (ResultSet) stmt.executeQuery( );
		assertTrue( rs.next( ) );
		
		stmt.clearInParameters();
		
		try
		{
			rs = (ResultSet) stmt.executeQuery( );
			fail();		// shouldn't get here
		}
		catch( JDBCException ex )
		{
			
		}
	}

}