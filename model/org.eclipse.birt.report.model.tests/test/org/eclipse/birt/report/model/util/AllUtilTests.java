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

package org.eclipse.birt.report.model.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in extension package.
 */

public class AllUtilTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		test.addTestSuite(ContentIteratorTest.class);
		test.addTestSuite(CssPropertyUtilTest.class);
		test.addTestSuite(CubeUtilTest.class);
		test.addTestSuite(DimensionUtilTest.class);
		test.addTestSuite(EventFilterTest.class);
		test.addTestSuite(ModelUtilTest.class);
		test.addTestSuite(StringUtilTest.class);
		test.addTestSuite(StructureEqualsTest.class);
		test.addTestSuite(URIUtilTest.class);
		test.addTestSuite(VersionUtilTest.class);
		test.addTestSuite(XPathUtilTest.class);
		test.addTestSuite(CopyUtilTest.class);
		test.addTestSuite(ColumnBindingUtilTest.class);
		// add all test classes here

		return test;
	}
}
