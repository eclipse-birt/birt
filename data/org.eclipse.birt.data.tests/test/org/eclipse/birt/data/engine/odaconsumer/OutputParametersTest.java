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

import junit.framework.TestCase;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Test ODA Consumer handling of output parameters.
 */
public class OutputParametersTest extends TestCase
{
    private final String TEST_DRIVER_ID = 
        "org.eclipse.birt.data.engine.odaconsumer.testdriver";
	private ConnectionManager sm_connManager;

    static
    {
		if ( System.getProperty( "BIRT_HOME" ) == null )
			System.setProperty( "BIRT_HOME", "./test" );
		System.setProperty( "PROPERTY_RUN_UNDER_ECLIPSE", "false" );
		Platform.initialize( null );
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
	    if( sm_connManager == null )
	        sm_connManager = ConnectionManager.getInstance();
    }
    
    public void testOutputParamDataTypeMapping()
    {
        Connection hostConn = null;
        PreparedStatement hostStmt = null;
        Object outParam2Value = null;
        Object outParam3Value = null;
        
        try
        {
            hostConn = sm_connManager.openConnection( TEST_DRIVER_ID, null );
            assertTrue( hostConn != null );
            
            // uses default dataSetType in plugin.xml
            hostStmt = hostConn.prepareStatement( null, null ); 
            assertTrue( hostStmt != null );
            
            boolean execStatus = hostStmt.execute();
            assertTrue( execStatus );
            
            outParam2Value = hostStmt.getParameterValue( 2 );
            outParam3Value = hostStmt.getParameterValue( 3 );
        }
        catch( DataException e1 )
        {
            fail( "testOutputParamDataTypeMapping failed: " + e1.toString() );
        }

        // parameter 2 is expected to have a data type with mapping
        // in test driver's plugin.xml, and would thus trigger the
        // correct call to getDate, returning a Date value
        assertTrue( outParam2Value != null );
        assertTrue( outParam2Value instanceof java.util.Date );

        // parameter 3 is not expected to have a data type with mapping
        // in test driver's plugin.xml; so it will be mapped to a String by default,
        // and getString will be called, returning a String value
        assertTrue( outParam3Value != null );
        assertTrue( outParam3Value instanceof String );
    } 
       
}
