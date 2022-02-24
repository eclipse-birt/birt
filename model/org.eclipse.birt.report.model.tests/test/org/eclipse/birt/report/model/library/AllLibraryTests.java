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

package org.eclipse.birt.report.model.library;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in extension package.
 */

public class AllLibraryTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		test.addTestSuite(DesignLoadLibraryTest.class);
		test.addTestSuite(LibraryChangeChartDataSetTest.class);
		test.addTestSuite(LibraryCommandTest.class);
		test.addTestSuite(LibraryCompoundElementTest.class);
		test.addTestSuite(LibraryHandleTest.class);
		test.addTestSuite(LibraryJointDataSetTest.class);
		test.addTestSuite(LibraryParseTest.class);
		test.addTestSuite(LibraryStructureTest.class);
		test.addTestSuite(LibraryThemeTest.class);
		test.addTestSuite(LibraryWithPropertyBinding.class);
		test.addTestSuite(LibraryWithTableTest.class);
		test.addTestSuite(ReferenceValueUtilTest.class);
		test.addTestSuite(ReloadLibraryTest.class);
		test.addTestSuite(LibrarySharedResultSetTest.class);
		test.addTestSuite(LibraryWithCubeTest.class);

		// add all test classes here

		return test;
	}
}
