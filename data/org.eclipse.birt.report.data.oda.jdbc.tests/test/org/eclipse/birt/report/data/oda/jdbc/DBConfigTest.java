/*******************************************************************************
 * Copyright (c) 2004,2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;


import org.junit.Test;
import static org.junit.Assert.*;

public class DBConfigTest {
	@Test
    public void testPoicyQulification()
	{
		//Policy 0
		String[] policy0Drivers = new String[]{
				"jTDS Type 4 JDBC Driver for MS SQL Server and Sybase",
				"PostgreSQL Native Driver"
		};
		
		for( int i = 0; i < policy0Drivers.length; i++ )
		{
			assertTrue( DBConfig.getInstance().qualifyPolicy(policy0Drivers[i], 0));
		}
		
		//Policy 1
		String[] policy1Drivers = new String[]{
				"Oracle JDBC driver",
				"MySQL-AB JDBC Driver",
				"jConnect (TM) for JDBC (TM)"
		};
		for( int i = 0; i < policy1Drivers.length; i++ )
		{
			assertTrue( DBConfig.getInstance().qualifyPolicy(policy1Drivers[i], 1));
		}
		
		//Policy 2
		String[] policy2Drivers = new String[]{
				"jConnect (TM) for JDBC (TM)"
		};
		for( int i = 0; i < policy2Drivers.length; i++ )
		{
			assertFalse( DBConfig.getInstance().qualifyPolicy(policy2Drivers[i], 2));
		}
		
		
		//Policy 3
		String[] policy3Drivers = new String[]{
				"Hive"
		};
		
		for( int i = 0; i < policy3Drivers.length; i++ )
		{
			assertTrue( DBConfig.getInstance().qualifyPolicy(policy3Drivers[i], 3));
		}
	}
}
