/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;

import junit.framework.TestCase;

/**
 * 
 * Testcase for ParameterMetaData
 *  
 */
public class ParameterMetaDataTest extends TestCase
{

	/** Connection object, used to create statement */
	private Connection conn = null;

	/** Statement object, used to get ParameterMetaData */
	private Statement stmt = null;

	/** the ParameterMetaData to test */
	private org.eclipse.birt.report.data.oda.jdbc.ParameterMetaData Pmd = null;

	/** JDBC Connection ,used to get JDBC PreparedStatement */
	private java.sql.Connection jdbcConn = null;

	/** JDBC PreparedStatement ,used to get JDBC ParameterMetaData */
	private java.sql.PreparedStatement jdbcPreparedStmt = null;

	/** the JDBC ParameterMetaData to compare with */
	private java.sql.ParameterMetaData jdbcPmd = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		TestUtil.createTestData( );
		String sql = "insert into "
				+ TestUtil.TABLE_NAME + " values(?,?,?,?,?,?,?)";

		/** Execute a insert action,get the ParameterMetaData to test */
		java.sql.Date datenow = new java.sql.Date( System.currentTimeMillis( ) );
		Time timenow = new Time( System.currentTimeMillis( ) );
		Timestamp timestampnow = new Timestamp( System.currentTimeMillis( ) );
		conn = TestUtil.openConnection( );
		stmt = (Statement) conn.newQuery( "" );
		stmt.prepare( sql );
		stmt.setBigDecimal( 1, new BigDecimal( 0 ) );
		stmt.setDate( 2, datenow );
		stmt.setDouble( 3, 1.01 );
		stmt.setInt( 4, 11 );
		stmt.setString( 5, "0asdas" );
		stmt.setTime( 6, timenow );
		stmt.setTimestamp( 7, timestampnow );
		stmt.execute( );
		Pmd = (org.eclipse.birt.report.data.oda.jdbc.ParameterMetaData) stmt.getParameterMetaData( );

		/**
		 * Execute a insert action,get the JDBC ParameterMetaData to compare
		 * with
		 */
		jdbcConn = TestUtil.openJDBCConnection( );
		jdbcPreparedStmt = jdbcConn.prepareStatement( sql );
		jdbcPreparedStmt.setBigDecimal( 1, new BigDecimal( 110 ) );
		jdbcPreparedStmt.setDate( 2, datenow );
		jdbcPreparedStmt.setDouble( 3, 1.012 );
		jdbcPreparedStmt.setInt( 4, 111 );
		jdbcPreparedStmt.setString( 5, "asdasd" );
		jdbcPreparedStmt.setTime( 6, timenow );
		jdbcPreparedStmt.setTimestamp( 7, timestampnow );
		jdbcPreparedStmt.execute( );
		jdbcPmd = jdbcPreparedStmt.getParameterMetaData( );

		super.setUp( );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		conn.close( );
		stmt.close( );
		jdbcConn.close( );
		jdbcPreparedStmt.close( );
		TestUtil.deleteTestData( );
		super.tearDown( );
	}

	public void testGetParameterCount( ) throws Exception
	{
		assertEquals( Pmd.getParameterCount( ), jdbcPmd.getParameterCount( ) );
	}

	public void testGetParameterMode( ) throws Exception
	{
		for ( int i = 1; i < Pmd.getParameterCount( ) + 1; i++ )
		{
			assertEquals( Pmd.getParameterMode( i ),
					jdbcPmd.getParameterMode( i ) );
		}
	}

	public void testGetParameterType( ) throws Exception
	{
		for ( int i = 1; i < Pmd.getParameterCount( ) + 1; i++ )
		{
			assertEquals( Pmd.getParameterType( i ),
					jdbcPmd.getParameterType( i ) );
		}
	}

	public void testGetParameterTypeName( ) throws Exception
	{
		for ( int i = 1; i < Pmd.getParameterCount( ) + 1; i++ )
		{
			assertEquals( Pmd.getParameterTypeName( i ),
					jdbcPmd.getParameterTypeName( i ) );
		}
	}

	public void testGetPrecision( ) throws Exception
	{
		for ( int i = 1; i < Pmd.getParameterCount( ) + 1; i++ )
		{
			assertEquals( Pmd.getPrecision( i ), jdbcPmd.getPrecision( i ) );
		}
	}

	public void testGetScale( ) throws Exception
	{
		for ( int i = 1; i < Pmd.getParameterCount( ) + 1; i++ )
		{
			assertEquals( Pmd.getScale( i ), jdbcPmd.getScale( i ) );
		}
	}

	public void testIsNullable( ) throws Exception
	{
		for ( int i = 1; i < Pmd.getParameterCount( ) + 1; i++ )
		{
			int result = java.sql.ParameterMetaData.parameterNullableUnknown;
			if ( Pmd.isNullable( i ) == IParameterMetaData.parameterNullable )
				result = java.sql.ParameterMetaData.parameterNullable;
			if ( Pmd.isNullable( i ) == IParameterMetaData.parameterNoNulls )
				result = java.sql.ParameterMetaData.parameterNoNulls;
			assertEquals( result, jdbcPmd.isNullable( i ) );
		}
	}

}