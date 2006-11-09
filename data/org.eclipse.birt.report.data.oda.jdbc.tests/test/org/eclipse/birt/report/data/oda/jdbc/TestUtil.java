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
import java.sql.DriverManager;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Utility class for ODA-JDBC driver test cases
 * 
 */
public class TestUtil
{

	/** Default JDBC Driver class name */
	final static String DEFAULT_DRIVER_CLASS = "net.sourceforge.jtds.jdbc.Driver";

	/** Data types to be tested */
	final static String[] DATA_TYPES = new String[]{
			"decimal", "date", "float", "int", "varchar(100)", "time", "timestamp"
	};

	/** Data values for the test */
	final static String[][] DATA_VALUES = new String[][]{
			{"NULL", "'2000-01-01 00:00:00'", "0.0",  "0",    "'00'", "'1900-01-01 12:00:01'", "'2000-01-01 12:00:00'"},
			{"1111", "NULL", 				  "1.1",  "1",    "'11'", "'1900-01-01 12:00:01'", "'2001-01-01 12:00:00'"},
			{"2222", "'2002-01-01 00:00:00'", "NULL", "2",    "'22'", "'1900-01-01 12:00:02'", "'2002-01-01 12:00:00'"},
			{"3333", "'2003-01-01 00:00:00'", "3.3",  "NULL", "'33'", "'1900-01-01 12:00:03'", "'2003-01-01 12:00:00'"},
			{"4444", "'2004-01-01 00:00:00'", "4.4",  "4", 	  "NULL", "'1900-01-01 12:00:04'", "'2004-01-01 12:00:00'"},
			{"5555", "'2005-01-01 00:00:00'", "5.5",  "5", 	  "'55'", "NULL", 				   "'2005-01-01 12:00:00'"},
			{"6666", "'2006-01-01 00:00:00'", "6.6",  "6", 	  "'66'", "'1900-01-01 12:00:06'", "NULL"}			
	};

	/** Test table name */
	final static String TABLE_NAME = "\"test_oda_jdbc\"";
	
	/** Test procedure name */
	final static String PROCEDURE_BASE_NAME = "testProc";
	
	static Connection openConnection( ) throws OdaException
	{
		Connection conn = new Connection( );
		Properties props = new Properties( );
		props.setProperty( Connection.Constants.ODAURL, getURL( ) );
		props.setProperty( Connection.Constants.ODADriverClass, getDriverClassName( ) );
		props.setProperty( Connection.Constants.ODAUser, getUser( ) );
		props.setProperty( Connection.Constants.ODAPassword, getPassword( ) );
		conn.open( props );
		return conn;
	}

	static java.sql.Connection openJDBCConnection( ) throws Exception
	{
		Class.forName( getDriverClassName( ) );
		java.sql.Connection jdbcConn = DriverManager.getConnection( getURL( ),
				getUser( ), getPassword( ) );
		return jdbcConn;
	}

	static void createTestData( ) throws Exception
	{
		java.sql.Connection jdbcConn = openJDBCConnection( );

		java.sql.Statement jdbcStmt = jdbcConn.createStatement( );
		String sql = "drop table " + TABLE_NAME;
		try 
		{
			jdbcStmt.execute( sql );
		}
		catch (Exception e)
		{
		}
		sql = "create table " + TABLE_NAME + "(";
		for ( int i = 0; i < DATA_TYPES.length; i++ )
		{
			if ( i > 0 )
			{
				sql += ", ";
			}
			sql += ( "col" + i + " " + DATA_TYPES[i] );
		}
		sql += ")";
		jdbcStmt.execute( sql );
		for ( int i = 0; i < DATA_VALUES.length; i++ )
		{
			sql = "insert into " + TABLE_NAME + " values(";
			for ( int j = 0; j < DATA_VALUES[i].length; j++ )
			{
				if ( j > 0 )
					sql += ", ";
				sql += DATA_VALUES[i][j];
			}
			sql += ")";
			jdbcStmt.execute( sql );
		}
		jdbcStmt.close( );
		jdbcConn.close( );
		
	}

	static void createTestProcedure( ) throws Exception
	{
		java.sql.Connection jdbcConn = openJDBCConnection( );

		String str="";
		java.sql.Statement jdbcStmt = jdbcConn.createStatement( );
		for( int i =0; i<DATA_TYPES.length;i++)
		{
			str = "drop procedure" + PROCEDURE_BASE_NAME + i;
			try
			{
				jdbcStmt.execute( str );
			}
			catch ( Exception e )
			{
			}

		}
			
		String str1 = "CREATE PROCEDURE ";
		String str2 = " PARAMETER STYLE JAVA READS SQL DATA LANGUAGE JAVA EXTERNAL NAME 'org.eclipse.birt.report.data.oda.jdbc.TestUtil.selectData'";
	    String sql="";
	
		for ( int i = 0; i < DATA_TYPES.length; i++ )
		{
			sql = str1
					+ PROCEDURE_BASE_NAME + i + " (IN param1 " + DATA_TYPES[i]
					+ " ,OUT param2 " + DATA_TYPES[i] + " )" + str2;

			try
			{
				jdbcStmt.execute( sql );
			}
			catch ( Exception e )
			{
			}
		}
		jdbcStmt.close( );
		jdbcConn.close( );
		
	}
	
	static void deleteTestData( ) throws Exception
	{
		java.sql.Connection jdbcConn = openJDBCConnection( );
		java.sql.Statement jdbcStmt = jdbcConn.createStatement( );
		String sql = "drop table " + TABLE_NAME;
		jdbcStmt.execute( sql );
		jdbcStmt.close( );
		jdbcConn.close( );
	}
	
	static void deleteTestProcedure( ) throws Exception
	{
		java.sql.Connection jdbcConn = openJDBCConnection( );
		java.sql.Statement jdbcStmt = jdbcConn.createStatement( );
		String sql = "";
		for ( int i = 0; i < DATA_TYPES.length; i++ )
		{
			sql = "drop procedure" + PROCEDURE_BASE_NAME + i;
			try
			{
				jdbcStmt.execute( sql );
			}
			catch ( Exception e )
			{
			}
		}
		jdbcStmt.close( );
		jdbcConn.close( );
	}

	static String getDriverClassName( )
	{
		return "org.apache.derby.jdbc.EmbeddedDriver";
	}

	static String getURL( )
	{
		String url = System.getProperty( "DTETest.url" );
		if ( url != null )
			return url;
		else
			return "jdbc:derby:"+getDatabase( )+";create=true;user="
					+ getUser( ) + ";password=" + getPassword( );
	}

	static String getUser( )
	{
		String user = System.getProperty( "DTETest.user" );
		if ( user != null )
			return user;
		else
			return "Actuate";
	}

	static String getPassword( )
	{
		String pwd = System.getProperty( "DTETest.password" );
		if ( pwd != null )
			return pwd;
		else
			return "Actuate";
	}

	static String getDatabase( )
	{
		String database = System.getProperty( "DTETest.database" );
		if ( database != null )
			return database;
		else
			return "DTETest";
	}
	
	public static void selectData( int a, int[] b )
	{
		assert a == 0;
		b[0] = 1;
	}
	
	public static void selectData( double a, double[] b )
	{
		assert a == 0.0;
		b[0] = 1.0;
	}
	
	public static void selectData( Date a, Date[] b )
	{
		assert a.equals( Date.valueOf( "2000-01-01" ) );
		b[0] = Date.valueOf( "2000-01-02" );
	}
	
	public static void selectData( BigDecimal a, BigDecimal[] b )
	{
		assert a.equals( new BigDecimal( "1111" ) );
		b[0] = new BigDecimal( "2222" );
	}
	
	public static void selectData(Timestamp a, Timestamp[] b)
	{
		assert a.equals( Timestamp.valueOf("2000-01-01 12:00:00.0000"));
		b[0] = Timestamp.valueOf("2000-01-02 12:00:00.0000");
	}
	
	public static void selectData( String a, String[] b )
	{
		assert a.equals( "00" );
		b[0] = "11";
	}
	
	public static void selectData(Time a, Time[] b)
	{
		assert a.equals(Time.valueOf("12:00:00"));
		b[0] = Time.valueOf( "12:00:01" );
	}
}