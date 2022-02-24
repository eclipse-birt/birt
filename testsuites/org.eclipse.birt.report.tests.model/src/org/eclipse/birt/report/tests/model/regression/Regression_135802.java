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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Steps to reproduce:
 * <ol>
 * <li>New a library with default theme
 * <li>New a report, drag lib.defaulttheme from library explorer to report
 * layout
 * <li>Open outline view, note that default theme has been added into outline
 * <li>Refresh the report
 * </ol>
 * <b>Expected result</b>:
 * <p>
 * Theme is still there
 * 
 * <b>Actual result</b>:
 * <p>
 * Theme disappears
 * </p>
 * Test description:
 * <p>
 * Open a report which include a library, apply a theme from the library, then
 * refresh the design and make sure that the theme is still there.
 * </p>
 */
public class Regression_135802 extends BaseTestCase {

	private final static String INPUT = "regression_135802.xml"; //$NON-NLS-1$
	private final static String LibraryName = "regression_121844_lib.xml";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);
		copyResource_INPUT(LibraryName, LibraryName);
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * 
	 */

	public void test_regression_135802() throws DesignFileException, SemanticException {
		// open a report which include a library.

		openDesign(INPUT);

		LibraryHandle lib = designHandle.getLibrary("regression_121844_lib"); //$NON-NLS-1$
		ThemeHandle theme = lib.findTheme("defaultTheme"); //$NON-NLS-1$

		// apply the theme

		designHandle.setTheme(theme);

		// refresh the report and make sure the theme is still in the report.

		for (Iterator iter = designHandle.getLibraries().iterator(); iter.hasNext();) {
			LibraryHandle library = (LibraryHandle) iter.next();
			try {
				designHandle.reloadLibrary(library);
			} catch (Exception e) {
			}
		}

		assertEquals("regression_121844_lib.defaultTheme", //$NON-NLS-1$
				designHandle.getStringProperty(ReportDesignHandle.THEME_PROP));

	}
}
