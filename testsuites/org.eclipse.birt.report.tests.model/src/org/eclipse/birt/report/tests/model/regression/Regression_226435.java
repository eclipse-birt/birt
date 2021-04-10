
package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * Model lose the precision
 */

public class Regression_226435 extends BaseTestCase {

	public void test_regression_226435() {
		DimensionValue dv = new DimensionValue(0.20833333333333334, "in");
		assertEquals("0.20833333333333334in", dv.toString());
	}
}
