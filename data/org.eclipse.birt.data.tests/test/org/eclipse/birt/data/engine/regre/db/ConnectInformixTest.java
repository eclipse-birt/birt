/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.regre.db;

import org.junit.Before;
import org.junit.Ignore;

import testutil.ConfigText;

/**
 * Run a query based on Informix database
 */
@Ignore("Ignore tests that require manual setup")
public class ConnectInformixTest extends ConnectionTest {

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void connectInformixSetUp() throws Exception {
		DriverClass = ConfigText.getString("Regre.Informix.DriverClass");
		URL = ConfigText.getString("Regre.Informix.URL");
		User = ConfigText.getString("Regre.Informix.User");
		Password = ConfigText.getString("Regre.Informix.Password");

	}

}
