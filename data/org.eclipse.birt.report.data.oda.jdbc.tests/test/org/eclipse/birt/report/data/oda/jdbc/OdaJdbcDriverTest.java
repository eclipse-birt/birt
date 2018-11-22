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

import java.util.HashMap;
import java.util.Properties;


import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.OdaException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * 
 * The class implements the unit test for JDBCConnectionFactory
 * 
 */
public class OdaJdbcDriverTest {

	/**
	 * Constructor for JDBCConnectionFactoryTest.
	 * 
	 * @param arg0
	 */



	/*
	 * Class under test for Connection getConnection(String)
	 */
	@Test
    public void testGetConnection( ) throws Exception
	{
		OdaJdbcDriver connFact = new OdaJdbcDriver( );
		Connection conn = (Connection) connFact.getConnection( "" );
		assertNotNull( conn );

	}
	
	/**
	 * Test setAppContext( ) of OdaJdbcDriver
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSetAppContext( ) throws Exception
	{
		OdaJdbcDriver connFact = new OdaJdbcDriver( );
		IConnection odaConnection = connFact.getConnection( "" );
  		assertFalse( odaConnection.isOpen( ) );  		
  		try
		{
			odaConnection.open( null );
			fail( "The connction should not have been opened!" );
		}
		catch ( IllegalArgumentException e )
		{
			odaConnection.close( );
		}

		Properties props = new Properties( );
		props.setProperty( Connection.Constants.ODAURL, TestUtil.getURL( ) );
		props.setProperty( Connection.Constants.ODADriverClass, TestUtil.getDriverClassName( ) );
		props.setProperty( Connection.Constants.ODAUser, TestUtil.getUser( ) );
		props.setProperty( Connection.Constants.ODAPassword, "" );
  		try
		{
			odaConnection.open( props );
			assertTrue( odaConnection.isOpen( ) );
		}
		catch ( IllegalArgumentException e )
		{
			odaConnection.close( );
		}

		HashMap context = new HashMap( );
		java.sql.Connection passInConn = TestUtil.openJDBCConnection( );
		context.put( IConnectionFactory.PASS_IN_CONNECTION, passInConn );
		IConnection connection = connFact.getConnection( "" );
		connection.setAppContext( context );
  		try
		{
  			connection.open( null );
			assertTrue( connection.isOpen( ) );
		}
		catch ( OdaException e )
		{
			fail( "Exception occurrs when opening a connection!" );
		}
	}

}
