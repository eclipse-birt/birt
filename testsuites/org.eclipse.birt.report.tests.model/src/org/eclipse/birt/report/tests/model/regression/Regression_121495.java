/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * FactoryHandle return NULL for the properties comes from style in library
 * </p>
 * Test description:
 * <p>
 * Report use style from library
 * </p>
 */

public class Regression_121495 extends BaseTestCase {

	private String INPUT = "Regression_121495.xml"; //$NON-NLS-1$
	private String Lib = "Regression_121495_Lib.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);
		copyResource_INPUT(Lib, Lib);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_121495() throws DesignFileException, SemanticException {
		openDesign(INPUT);
		LabelHandle label = (LabelHandle) designHandle.findElement("label"); //$NON-NLS-1$

		// report includes the library and use lib.theme
		designHandle.includeLibrary(Lib, "Lib"); //$NON-NLS-1$

		ThemeHandle theme = designHandle.getLibrary("Lib").findTheme("Theme1"); //$NON-NLS-1$ //$NON-NLS-2$
		SharedStyleHandle style = (SharedStyleHandle) theme.findStyle("Style1"); //$NON-NLS-1$

		designHandle.setThemeName("Lib.Theme1"); //$NON-NLS-1$
		label.setStyle(style);

		assertEquals("red", label.getProperty(Style.COLOR_PROP)); //$NON-NLS-1$

	}
}
