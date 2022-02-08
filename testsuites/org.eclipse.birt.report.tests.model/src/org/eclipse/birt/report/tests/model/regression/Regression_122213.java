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

import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Description:
 * <p>
 * Theme in libB should be invisible for the report
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>LibA includes LibB
 * <li>Report includes LibA
 * <li>Choose themes for the report
 * </ol>
 * Expected result:
 * <p>
 * Only themes in LibA is availabe in the theme list
 * <p>
 * Actual result:
 * <p>
 * Both LibA.defaulttheme and LibB.defaulttheme are listed in the theme list
 * </p>
 * Test description:
 * <p>
 * Report include lib2(2 themes), lib2 include a lib1(2 thems), ensure only
 * themes from lib2 can be see from report design.
 * </p>
 */
public class Regression_122213 extends BaseTestCase {

	private final static String REPORT = "regression_122213.xml"; //$NON-NLS-1$

	private final static String libname1 = "regression_122213_lib1.xml";
	private final static String libname2 = "regression_122213_lib2.xml";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(REPORT, REPORT);
		copyResource_INPUT(libname1, libname1);
		copyResource_INPUT(libname2, libname2);

	}

	/**
	 * @throws DesignFileException
	 */
	public void test_regression_122213() throws DesignFileException {
		openDesign(REPORT);
		List libs = designHandle.getLibraries();

		assertEquals(1, libs.size());

		LibraryHandle includeLib = (LibraryHandle) libs.get(0);
		assertEquals(2, includeLib.getThemes().getCount());

		LibraryHandle lib2 = includeLib.getLibrary("regression_122213_lib1"); //$NON-NLS-1$
		List lib2Themes = lib2.getThemes().getContents();
		assertEquals(2, lib2Themes.size());

		List themes = designHandle.getVisibleThemes(IAccessControl.DIRECTLY_INCLUDED_LEVEL);

		assertEquals("regression_122213_lib2.defaultTheme", ((ThemeHandle) themes.get(0)).getQualifiedName()); //$NON-NLS-1$
		assertEquals("regression_122213_lib2.t2", ((ThemeHandle) themes.get(1)).getQualifiedName()); //$NON-NLS-1$
		assertEquals(2, themes.size());

		// ensure that themes in lib2 is invisible to report.

		assertFalse(themes.contains(lib2Themes.get(0)));
		assertFalse(themes.contains(lib2Themes.get(1)));

	}
}
