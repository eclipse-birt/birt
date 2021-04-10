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

package org.eclipse.birt.report.model.extension;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in extension package.
 */

public class AllExtensionTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		// add all test classes here
		test.addTestSuite(EncryptionHelperExtensionTest.class);
		test.addTestSuite(ReportItemExtensionTest.class);
		test.addTestSuite(PeerExtensionTest.class);

		return test;
	}
}
