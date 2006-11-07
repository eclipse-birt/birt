
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
package org.eclipse.birt.data.engine.odaconsumer;

import junit.framework.TestCase;

import org.eclipse.birt.core.framework.Platform;

/**
 * 
 */

public class OdaconsumerTestCase extends TestCase {

    static
	{
		if ( System.getProperty( "BIRT_HOME" ) == null )
			System.setProperty( "BIRT_HOME", "./test" );
		System.setProperty( "PROPERTY_RUN_UNDER_ECLIPSE", "false" );
		Platform.initialize( null );
		try
		{
			TestSetup.createTestTable( );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}
    
}
