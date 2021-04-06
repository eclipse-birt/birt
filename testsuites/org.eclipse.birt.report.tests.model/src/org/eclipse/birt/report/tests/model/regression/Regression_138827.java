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
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Steps to reproduce:
 * <ol>
 * <li>New a library, add a selector "table", set background color to "red"
 * </p>
 * <li>New a report, include the library, add a table in the report
 * </p>
 * <li>Specify lib.theme to the report
 * </p>
 * <li>Preview in Web Viewer
 * </ol>
 * <b>Expected result:</b>
 * <p>
 * Table background color is red
 * </p>
 * <b>Actual result:</b>
 * <p>
 * Table background color is null
 * </p>
 * Test description:
 * <p>
 * Following the bug description, check table background color
 * </p>
 */

public class Regression_138827 extends BaseTestCase {

	private String filename = "Regression_138827.xml"; //$NON-NLS-1$
	private String libraryname = "Regression_138827_lib.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		// copyResource_INPUT( filename , filename );
		// copyResource_INPUT( libraryname , libraryname );
		copyInputToFile(INPUT_FOLDER + "/" + filename);
		copyInputToFile(INPUT_FOLDER + "/" + libraryname);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_138827() throws DesignFileException, SemanticException {
		openDesign(filename);
		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$
		designHandle.includeLibrary(libraryname, "Lib"); //$NON-NLS-1$

		openLibrary(libraryname, true);
		libraryHandle = designHandle.getLibrary("Lib"); //$NON-NLS-1$
		ThemeHandle theme = libraryHandle.findTheme("theme1"); //$NON-NLS-1$
		designHandle.setTheme(theme);
		assertEquals("red", table.getProperty(Style.BACKGROUND_COLOR_PROP)); //$NON-NLS-1$

	}
}
