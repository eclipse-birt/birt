/*******************************************************************************
 * Copyright (c) 2004 ,2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.regre.db;

import org.eclipse.birt.data.engine.regre.FeatureTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This test suite is designed to run all DtE regression test cases.
 */
public class AllConnectionTests
{

	public static Test suite( )
	{
		TestSuite suite = new TestSuite( "Test for org.eclipse.birt.data.engine.regre" );
		//$JUnit-BEGIN$

		//connect to various Database
		suite.addTestSuite( ConnectInformixTest.class );
		suite.addTestSuite( ConnectOracleTest.class );
		suite.addTestSuite( ConnectSybaseTest.class );
		suite.addTestSuite( ConnectMySQLTest.class );
		suite.addTestSuite( ConnectPostgreTest.class );
		suite.addTestSuite( ConnectDB2Test.class );
		suite.addTestSuite( ConnectionSQLServerTest.class );
		
		//test all features cuurently supported
		suite.addTestSuite( FeatureTest.class );
		//$JUnit-END$
		return suite;
	}
}
