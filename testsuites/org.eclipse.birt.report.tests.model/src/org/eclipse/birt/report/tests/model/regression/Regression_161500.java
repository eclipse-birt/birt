/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * [Regression]The color of the word in layout didn't change after changing its
 * color in General tab(Only select color in the dropping list).
 * <ol>
 * 1, New a label in a new report.<br>
 * 2, Enter some words.<br>
 * 3, Select the label and set its color in General tab.<br>
 * 4, Only change the color from the dropping list. <br>
 * 5, Check the color in Layout, then preview it.<br>
 * </ol>
 * <p>
 * <b>Expected result:</b>
 * <p>
 * <ol>
 * <li>The word's color can change successfully both in layout and in preview
 * </ol>
 * <b>Actual result:</b>
 * <p>
 * There is no change in layout, but can be changed in preview. Error logs:no
 * <p>
 * Test description:
 * <p>
 * Yes, it's model's bug. There's some problem in PropertyHandle.getIntValue().
 * We will check the codes in soon. Set a color style and check whether the
 * getIntValue() works well
 * <p>
 */
public class Regression_161500 extends BaseTestCase {

	public void test_regression_161500() throws Exception {
		createDesign();
		ElementFactory factory = designHandle.getElementFactory();
		TableHandle table = factory.newTableItem("table");

		// Set the style on table
		table.setProperty(IStyleModel.COLOR_PROP, "red");
		assertEquals(16711680, table.getIntProperty(IStyleModel.COLOR_PROP));
		assertEquals(16711680, table.getPropertyHandle(IStyleModel.COLOR_PROP).getIntValue());
		assertEquals(IColorConstants.RED, table.getPropertyHandle(IStyleModel.COLOR_PROP).getStringValue());
		assertEquals(IColorConstants.RED, table.getStringProperty(IStyleModel.COLOR_PROP));

		// Set another style on table
		table.setProperty(IStyleModel.COLOR_PROP, "blue");
		assertEquals(255, table.getIntProperty(IStyleModel.COLOR_PROP));
		assertEquals(255, table.getPropertyHandle(IStyleModel.COLOR_PROP).getIntValue());
		assertEquals(IColorConstants.BLUE, table.getPropertyHandle(IStyleModel.COLOR_PROP).getStringValue());
	}
}
