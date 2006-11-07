/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.util.Properties;

import junit.framework.TestCase;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odaconsumer.testdriver.TestAdvQueryImpl;
import org.eclipse.birt.data.engine.odaconsumer.testdriver.TestConnectionImpl;
import org.eclipse.birt.data.engine.odaconsumer.testdriver.TestDriverImpl;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 *  Test ODA Consumer handling of passing thru application context objects
 *  to an underlying ODA driver.
 */
public class AppContextTest extends TestCase
{
    private final String TEST_DRIVER_ID = 
        "org.eclipse.birt.data.engine.odaconsumer.testdriver";

	private ConnectionManager sm_connManager;
    private Properties sm_appContextMap;
    
    static
    {
		if ( System.getProperty( "BIRT_HOME" ) == null )
			System.setProperty( "BIRT_HOME", "./test" );
		System.setProperty( "PROPERTY_RUN_UNDER_ECLIPSE", "false" );
		Platform.initialize( null );
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
	    if( sm_connManager == null )
	        sm_connManager = ConnectionManager.getInstance();
	    if( sm_appContextMap == null )
	    {
	        sm_appContextMap = new Properties();
	        sm_appContextMap.put( TEST_DRIVER_ID, sm_connManager );
	        sm_appContextMap.put( TestDriverImpl.TEST_DRIVER_CONN_STATE, "" );
	    }
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
        sm_appContextMap.put( TestDriverImpl.TEST_DRIVER_CONN_STATE, "" );
    }
  
    /*
     * Test that setAppContext is passed thru the ODA Consumer
     * to an ODA driver's IDriver, IConnection and IQuery setAppContext calls
     * in the normal ODA calls sequence.
     */
    public void testSetAppContext()
    {
        verifyDriverSetAppContext();
        
        Connection hostConn = null;
        PreparedStatement hostStmt = null;
        Object queryContext = null;
        
        try
        {
            // creates a new Oda connection, passing thru the app context
            hostConn = sm_connManager.openConnection( TEST_DRIVER_ID, null, sm_appContextMap );
            assertTrue( hostConn != null );
            
            // uses default dataSetType in plugin.xml
            hostStmt = hostConn.prepareStatement( null, null ); 
            assertTrue( hostStmt != null );
            
            boolean execStatus = hostStmt.execute();
            assertTrue( execStatus );
            
            queryContext = hostStmt.getParameterValue( 1 );
        }
        catch( DataException e1 )
        {
            fail( "testSetAppContext failed: " + e1.toString() );
        }

        assertTrue( queryContext != null );
        assertEquals( sm_appContextMap.toString(), queryContext.toString() );
    } 
       
    /*
     * Test that setAppContext is passed thru to an opened connection,
     * i.e. the driver's IConnection.isOpen returns true *before* IConnection.open is called.
     */
    public void testSetAppContextOpenConnection()
    {
        // indicate to test driver to set connection state to isOpen() == true
        // without waiting for the open call
        sm_appContextMap.put( TestDriverImpl.TEST_DRIVER_CONN_STATE, 
                			  TestDriverImpl.TEST_DRIVER_CONN_STATE_OPEN );
        
        Connection hostConn = null;
        PreparedStatement hostStmt = null;
        Object queryContext = null;
        
        try
        {
            // creates a new Oda connection, passing thru the app context
            hostConn = sm_connManager.openConnection( TEST_DRIVER_ID, null, sm_appContextMap );
            assertTrue( hostConn != null );
            
            // uses default dataSetType in plugin.xml
            hostStmt = hostConn.prepareStatement( null, null ); 
            assertTrue( hostStmt != null );
            
            boolean execStatus = hostStmt.execute();
            assertTrue( execStatus );
            
            queryContext = hostStmt.getParameterValue( 1 );
        }
        catch( DataException e1 )
        {
            fail( "testSetAppContextOpenConnection failed: " + e1.toString() );
        }

        // verify that the app context got passed to the query
        // even without having called IConnection.open
        assertTrue( queryContext != null );
        assertEquals( sm_appContextMap.toString(), queryContext.toString() );
    } 

    /*
     * Verify that the ODA driver tester has implemented
     * the setAppContext method.
     */
    private void verifyDriverSetAppContext()
    {
        // test call to IDriver.setAppContext
        IDriver odaDriver = new TestDriverImpl();
        try
        {
            odaDriver.setAppContext( sm_appContextMap );
        }
        catch( OdaException e )
        {
            fail( "The ODA driver tester is not properly setup.");
        }
        
        Object driverContext = ((TestDriverImpl) odaDriver).getAppContext();
        assertTrue( driverContext != null );
        assertTrue( driverContext == sm_appContextMap );
        assertEquals( sm_appContextMap.toString(), driverContext.toString() );
        
        // test call to IConnection.setAppContext
        IConnection odaConn = null;
        try
        {
            // test call to IConnection.setAppContext
            odaConn = odaDriver.getConnection( null );
            odaConn.setAppContext( sm_appContextMap );
        }
        catch( OdaException e1 )
        {
            fail( "The ODA driver tester is not properly setup.");
        }
        
        driverContext = ((TestConnectionImpl) odaConn).getAppContext();
        assertTrue( driverContext != null );
        assertTrue( driverContext == sm_appContextMap );
        assertEquals( sm_appContextMap.toString(), driverContext.toString() );
        
        // test call to IQuery.setAppContext
        IQuery odaQuery = null;
        try
        {
            // test call to IConnection.setAppContext
            odaQuery = odaConn.newQuery( "dummy" );
            odaQuery.setAppContext( sm_appContextMap );
        }
        catch( OdaException e1 )
        {
            fail( "The ODA driver tester is not properly setup.");
        }
        
        driverContext = ((TestAdvQueryImpl) odaQuery).getAppContext();
        assertTrue( driverContext != null );
        assertTrue( driverContext == sm_appContextMap );
        assertEquals( sm_appContextMap.toString(), driverContext.toString() );
    }

}
