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

import java.util.Properties;


import org.eclipse.datatools.connectivity.oda.OdaException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * 
 * The class implements the unit test for Connection
 * 
 */
public class ConnectionTest {

	/*
	 * @see TestCase#setUp()
	 */
	@Before
    public void connectionSetUp() throws Exception
	{
		TestUtil.createTestData( );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@After
    public void connectionTearDown() throws Exception
	{
		TestUtil.deleteTestData( );

	}

	/*
	 * Class under test for constructor Connection()
	 */
	@Test
    public void testConnection( ) throws Exception
	{
		Connection conn = new Connection( );
		assertFalse( conn.isOpen( ) );
	}

	/*
	 * Class under test for void close()
	 */
	@Test
    public void testClose( ) throws Exception
	{
		Connection conn = TestUtil.openConnection( );
		conn.close( );
		assertFalse( conn.isOpen( ) );

		/*
		 * Calling the method close on a Connection object that is already
		 * closed is a no-op
		 */
		try
		{
			conn.close( );

			Connection tempConn = new Connection( );
			tempConn.close( );

		}
		catch ( OdaException e )
		{
			fail( "Close the closed operation should do nothing, no exception should be thrown." );
		}
	}

	/*
	 * Class under test for void commit()
	 */
	@Test
    public void testCommit( ) throws Exception
	{
		Connection conn = TestUtil.openConnection( );
		Statement stmt = (Statement) conn.newQuery( "" );
 		String sql = "update " + TestUtil.TABLE_NAME
				+ " set col0 = 7777 where col3=1";
		stmt.prepare( sql );
		stmt.execute();
		conn.commit( );
		stmt.close( );

		sql = "select col0 from " + TestUtil.TABLE_NAME + " where col3 = 1";
		stmt.prepare( sql );
		ResultSet rs = (ResultSet) stmt.executeQuery();
		rs.next( );
		assertEquals( rs.getDouble( 1 ), 7777, Double.MIN_VALUE );

		rs.close( );
		stmt.close( );

		sql = "update " + TestUtil.TABLE_NAME + " set col0 = 1111 where col3=1";
		stmt.prepare( sql );
		stmt.execute();
		conn.commit( );

		sql = "select col0 from " + TestUtil.TABLE_NAME + " where col3 = 1";
		stmt.prepare( sql );
		rs = (ResultSet) stmt.executeQuery();
		rs.next( );
		assertEquals( rs.getDouble( 1 ), 1111, Double.MIN_VALUE );
		conn.close();
	}

	/*
	 * Class under test for IQuery createStatement()
	 */
	@Test
    public void testCreateStatement( ) throws Exception
	{
		Connection conn = TestUtil.openConnection( );
		assertNotNull( conn.newQuery( "" ) );

		/* test call createStatement on the closed connection. */
		conn.close( );
		try
		{
			assertNotNull( conn.newQuery( "" ) );
			fail( );
		}
		catch ( JDBCException e )
		{
			//passed.

		}
		catch ( Exception e )
		{
			fail( );
		}

	}

	/*
	 * Class under test for IDataSourceMetaData getMetaData(String)
	 */
	@Test
    public void testGetMetaDataString( ) throws Exception
	{
		Connection conn = TestUtil.openConnection( );
		assertNotNull( conn.getMetaData( "" ) );
		assertTrue( conn.getMetaData( "" ) instanceof DataSourceMetaData );
		conn.close();
	}

	/*
	 * Class under test for boolean isOpen()
	 */
	@Test
    public void testIsOpen( ) throws Exception
	{
		Connection conn = TestUtil.openConnection( );
		assertTrue( conn.isOpen( ) );
		conn.close( );
		assertFalse( conn.isOpen( ) );
	}

	/*
	 * Class under test for void open(Properties)
	 */
	@Test
    public void testOpen( ) throws Exception
	{
		//TODO should have more sets of properties. one is for normal, others
		// for special scenarios.

		/* open use the URL and other properties. */
		Connection conn = new Connection( );

		Properties props = new Properties( );
		props.setProperty( Connection.Constants.ODAURL, TestUtil.getURL( ) );
		props.setProperty( Connection.Constants.ODADriverClass, TestUtil.getDriverClassName( ) );
		props.setProperty( Connection.Constants.ODAUser, TestUtil.getUser( ) );
		props.setProperty( Connection.Constants.ODAPassword, TestUtil.getPassword( ) );
		conn.open( props );
		assertTrue( conn.isOpen( ) );
		conn.close();
		/*
		 * Calling the method open with null URL properties, should throw
		 * exception.
		 */
		try
		{
			conn = new Connection( );

			props = new Properties( );
			props.setProperty( Connection.Constants.ODADriverClass, TestUtil
					.getDriverClassName( ) );
			props.setProperty( Connection.Constants.ODAUser, TestUtil.getUser( ) );
			props.setProperty( Connection.Constants.ODAPassword, TestUtil.getPassword( ) );
			conn.open( props );
			fail( "Open Connection with null URL info should throw exception" );
		}
		catch ( OdaException e )
		{

		}


		/*
		 * Calling the method open with null user properties, should use the
		 * info in url.
		 */
		try
		{
			conn = new Connection( );

			props = new Properties( );
			props.setProperty( Connection.Constants.ODAURL, TestUtil.getURL( ) + ";user="
					+ TestUtil.getUser( ) + ";password=" + TestUtil.getPassword( ) );
			props.setProperty( Connection.Constants.ODADriverClass, TestUtil
					.getDriverClassName( ) );
			/* the password info should be ignored. */
			props.setProperty( Connection.Constants.ODAPassword, TestUtil.getPassword( ) );
			conn.open( props );
			conn.close();
		}
		catch ( OdaException e )
		{
			fail( "Open Connection should succeed" );
		}

		/*
		 * Calling the method open with wrong properties, should throw
		 * exception.
		 */
		try
		{
			conn = new Connection( );

			props = new Properties( );
			props.setProperty( Connection.Constants.ODAURL, "wrong url" );
			props.setProperty( Connection.Constants.ODADriverClass, TestUtil
					.getDriverClassName( ) );
			props.setProperty( Connection.Constants.ODAUser, TestUtil.getUser( ) );
			conn.open( props );
			fail( "Open Connection with wrong property should throw exception" );
		}
		catch ( Throwable e1 )
		{
			if ( !( e1 instanceof OdaException || e1 instanceof NoClassDefFoundError ) )
			{
				throw new OdaException(e1);
			}
		}
	}

	/*
	 * Class under test for void rollback()
	 */
	@Test
    public void testRollback( ) throws Exception
	{
		//auto commit, rollback will not work. Just test if there are Exceptions.
		Connection conn = TestUtil.openConnection();
		try 
		{
			conn.rollback();
		}
		catch(Exception e)
		{
			fail("Exception occurred when calling rollback()");
		}
		conn.close();
	}

}
