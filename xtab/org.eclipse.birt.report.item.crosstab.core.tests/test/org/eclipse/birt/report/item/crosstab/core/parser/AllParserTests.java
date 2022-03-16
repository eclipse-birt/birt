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

package org.eclipse.birt.report.item.crosstab.core.parser;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * All tests in parser package.
 *
 */
public class AllParserTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		test.addTestSuite(AggregationCellParseTest.class);
		test.addTestSuite(CrosstabCellParseTest.class);
		test.addTestSuite(CrosstabParseTest.class);
		test.addTestSuite(CrosstabViewParseTest.class);
		test.addTestSuite(DimensionViewParseTest.class);
		test.addTestSuite(LevelViewParseTest.class);
		test.addTestSuite(MeasureViewParseTest.class);

		// add all test classes here

		return test;
	}
}
