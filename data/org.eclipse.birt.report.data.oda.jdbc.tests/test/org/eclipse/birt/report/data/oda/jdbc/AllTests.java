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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suit for data engine.
 */
public class AllTests
{
	
	/**
	 * @return
	 */
	public static Test suite( )
	{
		TestSuite suite = new TestSuite( "Test for org.eclipse.birt.report.data.oda.jdbc" );
		//The CallStatement test can only be run using unit test mode rather than plugin unit test
		//mode. The latter would change the system class path so that the Stored Procedures in 
		//CallStatement cannot be found.
		//suite.addTestSuite( org.eclipse.birt.report.data.oda.jdbc.CallStatementTest.class );
		suite.addTestSuite( org.eclipse.birt.report.data.oda.jdbc.ConnectionTest.class );
		suite.addTestSuite( org.eclipse.birt.report.data.oda.jdbc.DataSourceMetaDataTest.class );
		suite.addTestSuite( org.eclipse.birt.report.data.oda.jdbc.JDBCExceptionTest.class );
		suite.addTestSuite( org.eclipse.birt.report.data.oda.jdbc.LogConfigurationTest.class );
		suite.addTestSuite( org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriverTest.class );
		suite.addTestSuite( org.eclipse.birt.report.data.oda.jdbc.ParameterMetaDataTest.class );
		suite.addTestSuite( org.eclipse.birt.report.data.oda.jdbc.ResultSetMetaDataTest.class );
		suite.addTestSuite( org.eclipse.birt.report.data.oda.jdbc.ResultSetTest.class );
		suite.addTestSuite( org.eclipse.birt.report.data.oda.jdbc.StatementTest.class );
		suite.addTestSuite( org.eclipse.birt.report.data.oda.jdbc.DBConfigTest.class );
		return suite;
	}
}