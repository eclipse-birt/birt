
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.odaconsumer;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.engine.odaconsumer.testutil.TestSetup;
import org.junit.Rule;
import org.junit.rules.TestName;

/**
 * Base class for ODA consumer test cases.
 */

public class OdaconsumerTestCase {

	@Rule
	public TestName testName = new TestName();

	static {
		if (System.getProperty("BIRT_HOME") == null) {
			System.setProperty("BIRT_HOME", "./test");
		}
		System.setProperty("PROPERTY_RUN_UNDER_ECLIPSE", "false");
		try {
			Platform.startup(null);
		} catch (BirtException ex) {
			ex.printStackTrace();
		}

		try {
			TestSetup.createTestTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// return test case name
	public String getTestName() {
		return this.testName.getMethodName();
	}

	// dummy test case to avoid warning on empty test
	public final void testDummy() {
	}

}
