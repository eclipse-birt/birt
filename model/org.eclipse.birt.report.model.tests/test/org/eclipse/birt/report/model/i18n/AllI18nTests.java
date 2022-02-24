/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.i18n;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in i18n package.
 */

public class AllI18nTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		// add all test classes here
		test.addTestSuite(MessageFileTest.class);
		test.addTestSuite(PropertyLocalizationTest.class);
		test.addTestSuite(ResourceHandleTest.class);
		test.addTestSuite(ThreadResourcesTest.class);

		return test;
	}
}
