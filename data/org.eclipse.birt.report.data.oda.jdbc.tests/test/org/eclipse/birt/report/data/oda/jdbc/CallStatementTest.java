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
 * Test case for callableStatement
 * 
 */
public class CallStatementTest extends TestCase
{

	private Connection conn = null;

	/** The Statement to test */
	private CallStatement stmt = null;

	//private java.sql.Connection jdbcConn = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		TestUtil.createTestData( );
		TestUtil.createTestProcedure( );
		conn = TestUtil.openConnection( );
		stmt = (CallStatement) conn.newQuery( "org.eclipse.birt.report.data.oda.jdbc.SPSelectDataSet" );
		//jdbcConn = 
		TestUtil.openJDBCConnection( );
	}

	/*
	 * Class under test for IIResultSetMetaData getMetaData()
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
	}
	

	/*
	 * Class under test for void setBigDecimal(int, BigDecimal)
	 */
	public void testSetBigDecimalintBigDecimal( ) throws Exception
	{
		stmt.prepare( "call testProc0(?,?)" );
		stmt.setBigDecimal( 1, new BigDecimal( "1111" ) );
		stmt.registerOutParameter( 2, java.sql.Types.DECIMAL );
		//IResultSet rs = (IResultSet) 
		stmt.executeQuery( );
		assert stmt.getBigDecimal( 2 ).equals( new BigDecimal( "2222" ) );
	}

	/*
	 * Class under test for void setDate(int, Date)
	 */
	public void testSetDateintDate( ) throws Exception
	{
		stmt.prepare("call testProc1(?,?)");
		stmt.setDate(1, Date.valueOf("2000-01-01"));
		stmt.registerOutParameter(2,java.sql.Types.DATE);
		//IResultSet rs = (IResultSet)
		stmt.executeQuery();
		assert stmt.getDate(2).equals(Date.valueOf("2000-01-02"));
	}
	
	/*
	 * Class under test for void setDouble(int, double)
	 */
	public void testSetDoubleintdouble( ) throws Exception
	{
		stmt.prepare( "call testProc2(?,?)" );
		stmt.setDouble( 1, 0.0 );
		stmt.registerOutParameter( 2, java.sql.Types.DOUBLE);
		//IResultSet rs = (IResultSet) 
		stmt.executeQuery( );
		assert stmt.getDouble( 2 ) == 1.0;
	}

	/*
	 * Class under test for void setInt(int, int)
	 */
	public void testSetIntintint( ) throws Exception
	{
		stmt.prepare( "call testProc3(?,?)" );
		stmt.setInt( 1, 0 );
		stmt.registerOutParameter( 2, java.sql.Types.TINYINT );
		//IResultSet rs = (IResultSet) 
		stmt.executeQuery( );
		assert stmt.getInt( 2 ) == 1;
	}

	/*
	 * Class under test for void setString(int, String)
	 */
	public void testSetStringintString( ) throws Exception
	{
		stmt.prepare( "call testProc4(?,?)" );
		stmt.setString( 1, "00" );
		stmt.registerOutParameter(2,java.sql.Types.VARCHAR);
		//IResultSet rs = (IResultSet) 
		stmt.executeQuery( );
        assert stmt.getString(2).equals("11");
	}

	/*
	 * Class under test for void setTime(int, Time)
	 */
	public void testSetTimeintTime( ) throws Exception
	{
		stmt.prepare("call testProc5(?,?)");
		stmt.setTime( 1, Time.valueOf( "12:00:00" ) );
		stmt.registerOutParameter( 2, java.sql.Types.TIME );
		//IResultSet rs = (IResultSet)
		stmt.executeQuery();
		assert stmt.getTime(2).equals(Time.valueOf("12:00:01"));
	}

	/*
	 * Class under test for void setTimestamp(int, Timestamp)
	 */
	public void testSetTimestampintTimestamp( ) throws Exception
	{
		stmt.prepare( "call testProc6(?,?)" );
		stmt.setTimestamp( 1, Timestamp.valueOf("2000-01-01 12:00:00.0000"));
		stmt.registerOutParameter(2,java.sql.Types.TIMESTAMP);
		//IResultSet rs = (IResultSet) 
		stmt.executeQuery( );
        assert stmt.getTimestamp(2).equals(Timestamp.valueOf("2000-01-02 12:00:00.0000"));
	}

	public void testGetParameterMetaData( ) throws Exception
	{
		stmt.prepare( "call testProc0(?,?)" );
		
		IParameterMetaData md = stmt.getParameterMetaData();
		assertNotNull( md);
		
		/*for ( int i = 1; i <= 7; i++ )
		{
			assertEquals( md.getParameterType( i ), 
					jdbcPrepStmt.getParameterMetaData( ).getParameterType( i ) );
		}*/
	}

	public void testClearInParameters() throws Exception
	{
		stmt.prepare( "call testProc3(?,?)" );
		stmt.setInt( 1, 0 );
		stmt.registerOutParameter( 2, java.sql.Types.INTEGER );
		//IResultSet rs = (IResultSet) 
		stmt.executeQuery( );
			
		stmt.clearInParameters();	
		try
		{
			//rs = (IResultSet) 
			stmt.executeQuery( );
			fail();		// shouldn't get here
		}
		catch( JDBCException ex )
		{
			
		}
	}

}