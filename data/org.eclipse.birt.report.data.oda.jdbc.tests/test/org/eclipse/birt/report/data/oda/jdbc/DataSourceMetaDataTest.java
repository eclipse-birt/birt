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

import java.sql.DatabaseMetaData;

import junit.framework.TestCase;

/**
 * 
 * The class implements the unit test for DataSourceMetaData
 * 
 */
public class DataSourceMetaDataTest extends TestCase
{

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
	public void setUp( ) throws Exception
	{
		super.setUp( );
		/* Get one opened Connection. */
		conn1 = TestUtil.openConnection( );

		/* Get one cloased Connection. */
		conn2 = TestUtil.openConnection( );
		conn2.close( );

		dbMeta1 = (DataSourceMetaData) conn1.getMetaData( "" );//since the
		// datasourceType
		// is ignored.

		dbMeta2 = (DataSourceMetaData) conn2.getMetaData( "" ); //since the
		// datasourceType
		// is ignored.

	}

	/*
	 * @see TestCase#tearDown()
	 */

	protected void tearDown( ) throws Exception
	{
		conn1.close( );
		super.tearDown( );

	}

	/**
	 * Constructor for DataSourceMetaDataTest.
	 * 
	 * @param arg0
	 */
	public DataSourceMetaDataTest( String arg0 ) throws Exception
	{
		super( arg0 );

	}

	/*
	 * Class under test for constructor DataSourceMetaData(IConnection,
	 * java.sql.DatabaseMetaData)
	 */
	public void testDataSourceMetaData( ) throws Exception
	{
		DataSourceMetaData tempMeta = new DataSourceMetaData(
				new Connection( ), TestUtil.openJDBCConnection( ).getMetaData( ) );
		assertNotNull( tempMeta );

	}

	public void testGetConnection( ) throws Exception
	{

		assertEquals( dbMeta1.getConnection( ), conn1 );
		assertEquals( dbMeta2.getConnection( ), conn2 );

	}

	public void testGetDataSourceObjects( ) throws Exception
	{
		assertNull( dbMeta1.getDataSourceObjects( "", "", "", "" ) );
		assertNull( dbMeta2.getDataSourceObjects( "", "", "", "" ) );

	}

	public void testGetDataSourceMajorVersion( ) throws Exception
	{
	    /*
		assertEquals( dbMeta1.getDataSourceMajorVersion( ),
				ConnectionMetaData.DRIVER_MAJOR_VERSION );
		assertEquals( dbMeta2.getDataSourceMajorVersion( ),
				ConnectionMetaData.DRIVER_MAJOR_VERSION );
        */
	}

	public void testGetDataSourceMinorVersion( ) throws Exception
	{
	    /*
		assertEquals( dbMeta1.getDataSourceMinorVersion( ),
				ConnectionMetaData.DRIVER_MINOR_VERSION );
		assertEquals( dbMeta2.getDataSourceMinorVersion( ),
				ConnectionMetaData.DRIVER_MINOR_VERSION );
        */
	}

	public void testGetDataSourceProductName( ) throws Exception
	{
	    /*
		assertEquals( dbMeta1.getDataSourceProductName( ),
				ConnectionMetaData.DRIVER_NAME );
		assertEquals( dbMeta2.getDataSourceProductName( ),
				ConnectionMetaData.DRIVER_NAME );
        */
	}

	public void testGetDataSourceProductVersion( ) throws Exception
	{
	    /*
		assertEquals( dbMeta1.getDataSourceProductVersion( ),
				ConnectionMetaData.DRIVER_VERSION );
		assertEquals( dbMeta2.getDataSourceProductVersion( ),
				ConnectionMetaData.DRIVER_VERSION );
        */
	}

	public void testGetSQLStateType( ) throws Exception
	{
		/*
		 * when the Connection is not open, the dbMeta is null and should throw
		 * exception for the getSQLStateType.
		 */
		try
		{
			this.dbMeta2.getSQLStateType( );
			fail( "call getSQLStateType from DataSourceMetaData which is from the non-opened connection should throw exception." );
		}
		catch ( JDBCException e )
		{

		}

		java.sql.Connection jdbcConn = TestUtil.openJDBCConnection( );

		DatabaseMetaData dbMeta = jdbcConn.getMetaData( );

		if ( dbMeta.getSQLStateType( ) == DatabaseMetaData.sqlStateSQL99 )
		{
			assertEquals( this.dbMeta1.getSQLStateType( ),
					DataSourceMetaData.sqlStateSQL99 );

		}
		else
		{
			assertEquals( this.dbMeta1.getSQLStateType( ),
					DataSourceMetaData.sqlStateXOpen );

		}

	}

	public void testSupportsMultipleOpenResults( ) throws Exception
	{
		assertFalse( dbMeta1.supportsMultipleOpenResults( ) );
		assertFalse( dbMeta2.supportsMultipleOpenResults( ) );

	}

	public void testSupportsMultipleResultSets( ) throws Exception
	{
	    assertTrue( dbMeta1.supportsMultipleResultSets( ) );
	    assertTrue( dbMeta2.supportsMultipleResultSets( ) );
	}

	public void testSupportsNamedResultSets( ) throws Exception
	{
		assertTrue( dbMeta1.supportsNamedResultSets( ) );
		assertTrue( dbMeta2.supportsNamedResultSets( ) );

	}

	public void testSupportsNamedParameters( ) throws Exception
	{
		assertFalse( dbMeta1.supportsNamedParameters( ) );
		assertFalse( dbMeta2.supportsNamedParameters( ) );
	}

	public void testSupportsInParameters( ) throws Exception
	{
		assertTrue( dbMeta1.supportsInParameters( ) );
		assertTrue( dbMeta2.supportsInParameters( ) );
	}

	public void testSupportsOutParameters( ) throws Exception
	{
		assertTrue( dbMeta1.supportsOutParameters( ) );
		assertTrue( dbMeta2.supportsOutParameters( ) );
	}

	public void testGetSortMode( ) throws Exception
	{
		assertEquals( dbMeta1.getSortMode( ), DataSourceMetaData.sortModeNone );
		assertEquals( dbMeta2.getSortMode( ), DataSourceMetaData.sortModeNone );
	}

}