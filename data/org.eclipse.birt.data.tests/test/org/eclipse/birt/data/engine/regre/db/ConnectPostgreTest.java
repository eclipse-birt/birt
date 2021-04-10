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
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
@Ignore("Ignore tests that require manual setup")
public class ConnectPostgreTest extends ConnectionTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void connectPostgreSetUp() throws Exception {
		DriverClass = ConfigText.getString("Regre.Postgre.DriverClass");
		URL = ConfigText.getString("Regre.Postgre.URL");
		User = ConfigText.getString("Regre.Postgre.User");
		Password = ConfigText.getString("Regre.Postgre.Password");

	}
}
