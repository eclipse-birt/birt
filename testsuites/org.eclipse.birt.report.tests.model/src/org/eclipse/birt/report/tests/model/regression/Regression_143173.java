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
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Steps to reproduce:
 * </p>
 * <ol>
 * <li>New a library named "Lib", add a lable in it
 * <li>New a report, extends Lib.label
 * <li>Export the report, choose target library "Lib"
 * </ol>
 * <b>Expected result:</b>
 * </p>
 * Error to warn the recursive including
 * </p>
 * <b>Actual result:</b>
 * </p>
 * The target library includes itself without error
 * </p>
 * Test description:
 * <p>
 * Following the steps in bug description, semantic error is thrown out
 * </p>
 */

public class Regression_143173 extends BaseTestCase {

	private String filename = "Regression_143173.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 */
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_143173() throws DesignFileException {
		openDesign(filename);

		libraryHandle = designHandle.getLibrary("Lib"); //$NON-NLS-1$
		try {
			ElementExportUtil.exportDesign(designHandle, libraryHandle, true, true);
			fail();
		} catch (SemanticException e) {
			assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY, e.getErrorCode());
		}
	}
}
