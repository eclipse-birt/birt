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

package org.eclipse.birt.report.model.validators;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in extension package.
 */

public class AllValidatorTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		test.addTestSuite(CellOverlappingValidatorTest.class);
		test.addTestSuite(DataSetRequiredValidatorTest.class);
		test.addTestSuite(GroupNameValidatorTest.class);
		test.addTestSuite(InconsistentColumnsValidatorTest.class);
		test.addTestSuite(MasterPageMultiColumnValidatorTest.class);
		test.addTestSuite(MasterPageRequiredValidatorTest.class);
		test.addTestSuite(MasterPageSizeValidatorTest.class);
		test.addTestSuite(MasterPageTypeValidatorTest.class);
		test.addTestSuite(StructureListValidatorTest.class);
		test.addTestSuite(ValidationPerformanceTest.class);
		test.addTestSuite(ValueRequiredValidatorTest.class);
		test.addTestSuite(DataColumnNameValidatorTest.class);
		test.addTestSuite(ElementReferenceValidatorTest.class);

		// add all test classes here

		return test;
	}
}
