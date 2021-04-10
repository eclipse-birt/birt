/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * ModuleHandle.isInclude method use library.getLocation( ) to determine whether
 * if a library is included in a report. But when a library filename is
 * relative(either to resourc folder or to fragment host root), the isInclude
 * alway return false even a included library file with the same relative
 * filename exists.
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Test ModuleHandle.isInclude with relative library filename
 */
public class Regression_154987 extends BaseTestCase {

	private String filename = "Regression_154987.xml"; //$NON-NLS-1$
	private String libname = "Regression_154987_lib.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
		copyResource_INPUT(libname, libname);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 */
	public void test_regression_154987() throws DesignFileException {
		openDesign(filename);
		ModuleHandle moduleHandle = designHandle.getModuleHandle();

		moduleHandle.setResourceFolder(this.getFullQualifiedClassName() + "/" + INPUT_FOLDER);
		libraryHandle = designHandle.getLibrary("lib"); //$NON-NLS-1$
		libraryHandle.setFileName(libname);
		assertTrue(moduleHandle.isInclude(libraryHandle));

	}
}
