
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
