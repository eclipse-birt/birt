
package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * BIRT Exception when transfer parameter from List Box to Radio Button
 * <p>
 * Test description: Test that transfer parameter from List Box to Radio Button
 * is correctly.
 * <p>
 * </p>
 */

public class Regression_176145 extends BaseTestCase {

	public void test_Regression_176145() throws Exception {

		openDesign("regression_176145.xml");

		try {
			ScalarParameterHandle param = (ScalarParameterHandle) designHandle.getParameters().get(0);
			param.setControlType("text-box");
			param.setControlType("list-box");
		} catch (Exception e) {
			fail();
		}

	}

}
