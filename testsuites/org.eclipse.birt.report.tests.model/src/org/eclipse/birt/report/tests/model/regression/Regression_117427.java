/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Detail: Library in design cannot remove
 * <p>
 * Step:
 * <ol>
 * <li>Create a library.
 * <li>Open a design and include a library, save.
 * <li>Select the library in layout and delete it.
 * </ol>
 * <b>Actual result:</b>
 * <p>
 * A message pop-up and must select if exit the workbench. Select "Yes", Eclipse
 * close. Select "No" the library cannot delete, and it will apear at reopen.
 * <p>
 * <b>Expected result:</b>
 * <p>
 * Library is deleted.
 * </p>
 * Test description:
 * <p>
 * Report include a library, open the design and drop the library, ensure that
 * there won't be exception
 * </p>
 */
public class Regression_117427 extends BaseTestCase {

	private final static String REPORT = "regression_117427.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(REPORT, REPORT);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 * 
	 */

	public void test_regression_117427() throws DesignFileException, SemanticException, IOException {
		openDesign(REPORT);
		LibraryHandle lib = designHandle.getLibrary("regression_117427_lib"); //$NON-NLS-1$
		designHandle.dropLibrary(lib);

		assertEquals(0, designHandle.getLibraries().size());
	}
}
