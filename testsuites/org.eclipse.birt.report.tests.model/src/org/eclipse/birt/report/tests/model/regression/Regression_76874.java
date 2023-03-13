/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Set font size to "", NPE is thrown out
 * </p>
 * Test description:
 * <p>
 * Use default size when font size is set to blank
 * </p>
 */

public class Regression_76874 extends BaseTestCase {

	private String filename = "Regression_76874.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
		// copyResource_INPUT( INPUT2, INPUT2 );
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	public void test_regression_76874() throws DesignFileException, SemanticException {
		openDesign(filename);

		LabelHandle label = (LabelHandle) designHandle.findElement("label"); //$NON-NLS-1$
		label.setProperty(Style.FONT_SIZE_PROP, ""); //$NON-NLS-1$

		// default value is returned.

		assertEquals("10pt", label.getProperty(Style.FONT_SIZE_PROP).toString()); //$NON-NLS-1$
	}
}
