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

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Group header row page break doesn't work as expected
 * <p>
 * Test description: The page break property has set on the row of group, but
 * the group.
 * <p>
 * </p>
 */

public class Regression_173242 extends BaseTestCase {

	private String filename = "regression_173242.xml";

	public void test_Regression_173242() throws Exception {

		openDesign(filename);
		TableHandle tableHandle = (TableHandle) designHandle.findElement("table");
		TableGroupHandle tableGroupHandle = (TableGroupHandle) tableHandle.getGroups().get(0);
		RowHandle tableGroupRowHandle = (RowHandle) tableGroupHandle.getHeader().get(0);
		tableGroupRowHandle.setProperty("pageBreakBefore", "always");

		assertEquals("auto", tableGroupRowHandle.getProperty(DesignChoiceConstants.CHOICE_PAGE_BREAK_AFTER));
		assertNotSame("always", tableGroupHandle.getProperty("pageBreakBefore"));

	}
}
