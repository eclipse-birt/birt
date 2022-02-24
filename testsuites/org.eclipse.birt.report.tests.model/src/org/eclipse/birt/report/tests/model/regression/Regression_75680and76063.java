/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * 76063: Can't create style "report" 75680:Add selector "report" to the report,
 * the report items didn't display style properties in layout
 * </p>
 * Test description:
 * <p>
 * Add selector "report", check report item style property search algorithm
 * </p>
 */
public class Regression_75680and76063 extends BaseTestCase {

	private String filename = "Regression_75680.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
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

	public void test_regression_75680and76063() throws DesignFileException, SemanticException {
		openDesign(filename);

		// Create style "report"

		SharedStyleHandle style = designHandle.getElementFactory().newStyle("report"); //$NON-NLS-1$
		style.setProperty(Style.FONT_FAMILY_PROP, "sans-serif"); //$NON-NLS-1$
		designHandle.getStyles().add(style);

		LabelHandle label = (LabelHandle) designHandle.findElement("label"); //$NON-NLS-1$
		assertEquals("sans-serif", label.getProperty(Style.FONT_FAMILY_PROP)); //$NON-NLS-1$
	}
}
