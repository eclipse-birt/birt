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
