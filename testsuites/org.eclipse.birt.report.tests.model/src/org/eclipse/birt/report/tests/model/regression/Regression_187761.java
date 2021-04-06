
package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Table in grid which from library can not preview
 * <p>
 * Test description: Testing the getColumnPosition4Cell() method by
 * insertColumn(int,int)
 * <p>
 * </p>
 */

public class Regression_187761 extends BaseTestCase {

	public void test_Regression_187761() throws Exception {
		openDesign("regression_187761.rptdesign");
		TableHandle table = (TableHandle) designHandle.findElement("table");
		table.insertColumn(0, 1);
		assertEquals(2, table.getColumnCount());
	}

}
