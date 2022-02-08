/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda.api;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in api package.
 */

public class AllApiTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		test.addTestSuite(DataSetParameterAdapterTest.class);
		test.addTestSuite(OdaDataSetAdapterTest.class);
		test.addTestSuite(OdaDataSourceAdapterTest.class);
		test.addTestSuite(ReportParameterAdapterTest.class);
		test.addTestSuite(ResultSetColumnAdapterTest.class);
		test.addTestSuite(ResultSetCriteriaAdapterTest.class);
		test.addTestSuite(AdvancedDataSetAdapterTest.class);

		// add all test classes here

		return test;
	}
}
