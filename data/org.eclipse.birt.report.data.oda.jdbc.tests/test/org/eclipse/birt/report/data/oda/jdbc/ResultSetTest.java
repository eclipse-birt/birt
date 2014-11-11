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

import java.sql.SQLException;

import junit.framework.TestCase;

/**
 * Test case for ResultSet
 *  
 */
public class ResultSetTest extends TestCase
{

	private Connection conn = null;

	private Statement stmt = null;

	/** the result set to test */
	private ResultSet rs = null;

	private java.sql.Connection jdbcConn = null;

	private java.sql.Statement jdbcStmt = null;

	/** the JDBC result set to compare with */
	private java.sql.ResultSet jdbcRs = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		TestUtil.createTestData( );
		conn = TestUtil.openConnection( );
		stmt = (Statement) conn.newQuery( "" );
		String sql = "select * from " + TestUtil.TABLE_NAME;
		stmt.prepare( sql );
		rs = (ResultSet) stmt.executeQuery( );
		jdbcConn = TestUtil.openJDBCConnection( );
		jdbcStmt = jdbcConn.createStatement( java.sql.ResultSet.TYPE_SCROLL_SENSITIVE,
				java.sql.ResultSet.CONCUR_UPDATABLE );
		jdbcRs = jdbcStmt.executeQuery( sql );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		try
		{
			rs.close( );
			jdbcRs.close( );
		}
		catch ( Exception e )
		{
			// might have already been closed in testClose().
			System.out.println( "Close result set failed. Might have already been closed in testClose()." );
		}
		stmt.close( );
		conn.close( );
		jdbcStmt.close( );
		jdbcConn.close( );
		TestUtil.deleteTestData( );
		super.tearDown( );
	}

	/**
	 * Constructor for ResultSetTest.
	 * 
	 * @param arg0
	 */
	public ResultSetTest( String arg0 )
	{
		super( arg0 );
	}

	/**
	 * Including next(), getRow(), getDATATYPE(index), wasNull(). Compare with
	 * the results got from JDBC ResultSet
	 * 
	 * @throws Exception
	 */
	public void testResultValuesByIndex( ) throws Exception
	{
		assertEquals( rs.getRow( ), jdbcRs.getRow( ) );
		/*
		 * Behavior change, resultset will not throw exception for not invoking next() before getString()
		 */
		/*
		String sqlState = null;
		try
		{
			rs.getString( 1 );
			fail( "Call \"getString\" before \"next\". Should have thrown JDBCException, but havn't." );
		}
		catch ( JDBCException e )
		{
			sqlState = e.getSQLState( );
		}
		try
		{
			jdbcRs.getString( 1 );
			fail( );
		}
		catch ( SQLException e )
		{
			assertEquals( e.getSQLState( ), sqlState );
		}*/
		while ( rs.next( ) )
		{
			jdbcRs.next( );
			assertEquals( rs.wasNull( ), jdbcRs.wasNull( ) );
			assertEquals( rs.getRow( ), jdbcRs.getRow( ) );
			assertEquals( rs.getBigDecimal( 1 ), jdbcRs.getBigDecimal( 1 ) );
			assertEquals( rs.getDate( 2 ), jdbcRs.getDate( 2 ) );
			assertEquals( rs.getDouble( 3 ),
					jdbcRs.getDouble( 3 ),
					Double.MIN_VALUE );
			assertEquals( rs.getInt( 4 ), jdbcRs.getInt( 4 ) );
			assertEquals( rs.getString( 5 ), jdbcRs.getString( 5 ) );
			assertEquals( rs.getTime( 6 ), jdbcRs.getTime( 6 ) );
			assertEquals( rs.getTimestamp( 7 ), jdbcRs.getTimestamp( 7 ) );
		}
	}

	/**
	 * Including getString(columnName). Compare with the results got from JDBC
	 * ResultSet
	 * 
	 * @throws Exception
	 */
	public void testResultValuesByColName( ) throws Exception
	{
		int i = 0;
		while ( rs.next( ) )
		{
			jdbcRs.next( );
			String colName = "col" + i;
			i++;
			assertEquals( rs.getString( colName ), jdbcRs.getString( colName ) );
		}
	}

	public void testGetMetaData( ) throws Exception
	{
		assertNotNull( rs.getMetaData( ) );
	}

	public void testClose( ) throws Exception
	{
		rs.close( );
		jdbcRs.close( );
		String sqlState = null;
		try
		{
			rs.next( );
			fail( "Call \"getRow\" when the result set is closed. Should have thrown JDBCException, but havn't." );
		}
		catch ( JDBCException e )
		{
			sqlState = e.getSQLState( );
		}
		try
		{
			jdbcRs.next( );
			fail( );
		}
		catch ( SQLException e )
		{
			assertEquals( e.getSQLState( ), sqlState );
		}
	}

	/**
	 * Test function for setMaxRows.
	 * 
	 * @throws Exception
	 */
	public void testSetMaxRows( ) throws Exception
	{
		// test max = 0
		int max = 0;
		rs.setMaxRows( max );
		int rowCount = 0;
		while ( jdbcRs.next( ) )
		{
			assertTrue( rs.next( ) );
			rowCount++;
		}
		assertFalse( rs.next( ) );

		// test 0 < max < rowCount
		rs.close( );
		stmt.close( );
		String sql = "select * from " + TestUtil.TABLE_NAME;
		stmt.prepare( sql );
		rs = (ResultSet) stmt.executeQuery( );
		max = 1;
		rs.setMaxRows( max );
		assertTrue( rs.next( ) );
		assertFalse( rs.next( ) );

		// test max = rowCount
		rs.close( );
		stmt.close( );
		sql = "select * from " + TestUtil.TABLE_NAME;
		stmt.prepare( sql );
		rs = (ResultSet) stmt.executeQuery( );
		max = rowCount;
		rs.setMaxRows( max );
		for ( int i = 0; i < rowCount; i++ )
		{
			assertTrue( rs.next( ) );
		}
		assertFalse( rs.next( ) );

		// test max > rowCount
		rs.close( );
		stmt.close( );
		sql = "select * from " + TestUtil.TABLE_NAME;
		stmt.prepare( sql );
		rs = (ResultSet) stmt.executeQuery( );
		max = rowCount + 10;
		rs.setMaxRows( max );
		for ( int i = 0; i < rowCount; i++ )
		{
			assertTrue( rs.next( ) );
		}
		assertFalse( rs.next( ) );

		// test max < 0 -- should have no effect
		rs.close( );
		stmt.close( );
		sql = "select * from " + TestUtil.TABLE_NAME;
		stmt.prepare( sql );
		rs = (ResultSet) stmt.executeQuery( );
		max = -1;
		rs.setMaxRows( max );
		for ( int i = 0; i < rowCount; i++ )
		{
			assertTrue( rs.next( ) );
		}
		assertFalse( rs.next( ) );

	}

	public void testFindColumn( ) throws Exception
	{
		int columnCount = jdbcRs.getMetaData( ).getColumnCount( );
		for ( int i = 0; i < columnCount; i++ )
		{
			assertEquals( rs.findColumn( jdbcRs.getMetaData( )
					.getColumnName( i + 1 ) ), i + 1 );
		}
	}

}