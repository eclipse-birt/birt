/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.regre.db;

import testutil.ConfigText;

import org.junit.Before;
import org.junit.Ignore;

/**
 * Run a query based on DB2 database
 */
@Ignore("Ignore tests that require manual setup")
public class ConnectDB2Test extends ConnectionTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void connectDB2SetUp() throws Exception {
		DriverClass = ConfigText.getString("Regre.DB2.DriverClass");
		URL = ConfigText.getString("Regre.DB2.URL");
		User = ConfigText.getString("Regre.DB2.User");
		Password = ConfigText.getString("Regre.DB2.Password");

	}
}
