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

package org.eclipse.birt.report.model.command;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in command package.
 */

public class AllCommandTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		// add all test classes here
		test.addTestSuite(ContentCommandTest.class);
		test.addTestSuite(ContentExceptionTest.class);
		test.addTestSuite(CustomMsgCommandTest.class);
		test.addTestSuite(CustomMsgExceptionTest.class);
		test.addTestSuite(ExtendsCommandTest.class);
		test.addTestSuite(ExtendsExceptionTest.class);
		test.addTestSuite(NameCommandTest.class);
		test.addTestSuite(NameExceptionTest.class);
		test.addTestSuite(PropertyCommandTest.class);
		test.addTestSuite(PropertyNameExceptionTest.class);
		test.addTestSuite(StyleCommandTest.class);
		test.addTestSuite(StyleExceptionTest.class);
		test.addTestSuite(TemplateCommandTest.class);
		test.addTestSuite(UserPropertyCommandTest.class);
		test.addTestSuite(UserPropertyExceptionTest.class);
		test.addTestSuite(CssCommandTest.class);

		return test;
	}
}
