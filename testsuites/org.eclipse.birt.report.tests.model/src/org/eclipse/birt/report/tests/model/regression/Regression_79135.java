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

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * "target" is not displayed properly in html file.
 * <p>
 * Steps to reproduce:
 * <p>
 * Add an image and use default target.
 * <p>
 * <b>Actual result:</b>
 * <p>
 * "target" is not correct in html file.
 * </p>
 * Test description:
 * <p>
 * Check default target window.
 * </p>
 */

public class Regression_79135 extends BaseTestCase {

	private String filename = "Regression_79135.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
		// copyResource_INPUT( INPUT2, INPUT2 );
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_79135() throws DesignFileException {
		openDesign(filename);
		ImageHandle image = (ImageHandle) designHandle.findElement("image"); //$NON-NLS-1$
		ActionHandle action = image.getActionHandle();
		assertEquals(DesignChoiceConstants.TARGET_NAMES_TYPE_BLANK, action.getTargetWindow());
		assertNull(action.getTargetFileType());

	}
}
