
package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * [Regression]Exception occurs when resize masterpage in library
 * <p>
 * Test description: Testing the getDefaultUnits() method can get the default
 * value.
 * <p>
 * </p>
 */

public class Regression_191168 extends BaseTestCase {

	public void test_Regression_191168() throws Exception {
		createLibrary();
		assertEquals("in", libraryHandle.getDefaultUnits());
	}
}
