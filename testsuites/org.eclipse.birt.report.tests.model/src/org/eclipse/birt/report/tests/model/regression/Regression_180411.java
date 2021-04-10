
package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Error happens in XML Source when a report extends the library's cube
 * <p>
 * Test description: Test that extends the library's cube is correctly
 * <p>
 * </p>
 */

public class Regression_180411 extends BaseTestCase {

	private String report = "regression_180411.rptdesign";

	public void test_Regression_180411() throws Exception {
		openDesign(report);

		CubeHandle cubeHandle = (CubeHandle) designHandle.getCubes().get(0);
		assertNotNull(cubeHandle);

	}
}
