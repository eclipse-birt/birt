/*******************************************************************************
 * Copyright (c) 2004,2011 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DBConfigTest {
	@Test
	public void testPoicyQulification() {
		// Policy 0
		String[] policy0Drivers = { "jTDS Type 4 JDBC Driver for MS SQL Server and Sybase",
				"PostgreSQL Native Driver" };

		for (int i = 0; i < policy0Drivers.length; i++) {
			assertTrue(DBConfig.getInstance().qualifyPolicy(policy0Drivers[i], 0));
		}

		// Policy 1
		String[] policy1Drivers = { "Oracle JDBC driver", "MySQL-AB JDBC Driver", "jConnect (TM) for JDBC (TM)" };
		for (int i = 0; i < policy1Drivers.length; i++) {
			assertTrue(DBConfig.getInstance().qualifyPolicy(policy1Drivers[i], 1));
		}

		// Policy 2
		String[] policy2Drivers = { "jConnect (TM) for JDBC (TM)" };
		for (int i = 0; i < policy2Drivers.length; i++) {
			assertFalse(DBConfig.getInstance().qualifyPolicy(policy2Drivers[i], 2));
		}

		// Policy 3
		String[] policy3Drivers = { "Hive" };

		for (int i = 0; i < policy3Drivers.length; i++) {
			assertTrue(DBConfig.getInstance().qualifyPolicy(policy3Drivers[i], 3));
		}
	}
}
