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

import junit.framework.TestCase;

/**
 * 
 * The class implements the unit test for JDBCConnectionFactory
 * 
 */
public class OdaJdbcDriverTest extends TestCase
{

	/**
	 * Constructor for JDBCConnectionFactoryTest.
	 * 
	 * @param arg0
	 */
	public OdaJdbcDriverTest( String arg0 )
	{
		super( arg0 );
	}


	/*
	 * Class under test for Connection getConnection(String)
	 */
	public void testGetConnection( ) throws Exception
	{
		OdaJdbcDriver connFact = new OdaJdbcDriver( );
		Connection conn = (Connection) connFact.getConnection( "" );
		assertNotNull( conn );

	}

}