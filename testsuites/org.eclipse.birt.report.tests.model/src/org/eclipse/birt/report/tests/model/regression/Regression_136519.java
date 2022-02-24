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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Description: Page break compatibility issue for left and right.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Open some old design files with page break set to left or right.
 * <li>Preview
 * </ol>
 * <p>
 * <b>Expected result:</b>
 * <p>
 * Since left and right are not supported now, they should be identified as
 * always.
 * <p>
 * <b>Actual result:</b>
 * <p>
 * They were omitted.
 * <p>
 * Test description:
 * <p>
 * Open an old report, the report contains a label with page break before/after
 * set to left and right respectively, make sure they are correctly parsed and
 * provide compatibility
 * <p>
 */
public class Regression_136519 extends BaseTestCase {

	private final static String INPUT = "regression_136519.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

	}

	/**
	 * @throws DesignFileException
	 */

	public void test_regression_136519() throws DesignFileException {
		openDesign(INPUT);
		LabelHandle label = (LabelHandle) designHandle.findElement("l1"); //$NON-NLS-1$

		assertEquals("always", label.getStringProperty(StyleHandle.PAGE_BREAK_AFTER_PROP)); //$NON-NLS-1$
		assertEquals("always", label.getStringProperty(StyleHandle.PAGE_BREAK_BEFORE_PROP)); //$NON-NLS-1$
	}
}
