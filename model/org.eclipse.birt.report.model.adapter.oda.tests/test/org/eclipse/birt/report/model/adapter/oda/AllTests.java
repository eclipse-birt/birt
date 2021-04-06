
package org.eclipse.birt.report.model.adapter.oda;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.adapter.oda.api.AllApiTests;
import org.eclipse.birt.report.model.adapter.oda.util.AllUtilTests;

/**
 * Tests cases run in the build script.
 */

public class AllTests extends TestCase {

	/**
	 * @return test run in build script
	 */
	public static Test suite() {
		TestSuite test = new TestSuite();

		// add all package tests here
		test.addTest(AllUtilTests.suite());
		test.addTest(AllApiTests.suite());

		return test;
	}

}
