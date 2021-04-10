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

package org.eclipse.birt.report.model.core;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in core package.
 */

public class AllCoreTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		// add all test classes here
		test.addTestSuite(DesignElementCloneForTemplateTest.class);
		test.addTestSuite(DesignElementCloneTest.class);
		test.addTestSuite(DesignElementCopyPropertyToTest.class);
		test.addTestSuite(DesignElementPropsTest.class);
		test.addTestSuite(DesignElementTest.class);
		test.addTestSuite(DesignSessionTest.class);
		test.addTestSuite(ModuleTest.class);
		test.addTestSuite(MultiElementSlotTest.class);
		test.addTestSuite(NameHelperTest.class);
		test.addTestSuite(NameSpaceTest.class);
		test.addTestSuite(ParameterNameTest.class);
		test.addTestSuite(PropertyStructureCloneTest.class);
		test.addTestSuite(SingleElementSlotTest.class);
		test.addTestSuite(StructRefTest.class);
		test.addTestSuite(StructureTest.class);
		test.addTestSuite(StyledElementTest.class);
		test.addTestSuite(StyleElementTest.class);
		test.addTestSuite(StyleNameTest.class);

		return test;
	}
}
