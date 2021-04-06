/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Bug Description:</b>
 * <p>
 * Set the preferences->Report Design->Resource Fold as its default
 * value(blank).Drag a something from library to a report which is in a subfold
 * of a project.It will get an error.
 * <p>
 * <b>Step to reproduce:</b>
 * <ol>
 * <li>go to preferences->Report Design->Resource, in that page click the
 * "Restore Defaults"
 * <li>New a report project
 * <li>New a fold in the new project(a yellow fold)
 * <li>New a library in the project and new a datasource in the library
 * <li>New a report "New_report1.rptdesign" under the project fold(the blue one)
 * <li>New another report "New_report2.rptdesign" under the subfold of the
 * project (the yellow fold)
 * <li>Drag the datasource frow the new library to this two report.
 * </ol>
 * <b>Actual result:</b>
 * <p>
 * drag to the New_report1.rptdesign is ok, but to the "New_report2.rptdesign"
 * will get an error
 * <p>
 * <b>Test Desciption:</b>
 * <p>
 * Follwing the steps in bug description, no error
 */
public class Regression_157135 extends BaseTestCase {

	private String filename2 = "Regression_157135_2.xml"; //$NON-NLS-1$ //$NON-NLS-2$
	private String libname = "Regression_157135_lib.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( "sub/" + filename2, "sub/" + filename2 );
		// copyResource_INPUT( libname, libname );
		copyInputToFile(INPUT_FOLDER + "/" + filename2);
		copyInputToFile(INPUT_FOLDER + "/" + libname);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws Exception
	 */
	public void test_regression_157135() throws DesignFileException, SemanticException {
		filename2 = getTempFolder() + "/" + INPUT_FOLDER + "/" + "Regression_157135_2.xml"; //$NON-NLS-2$ //$NON-NLS-3$
		libname = getTempFolder() + "/" + INPUT_FOLDER + "/" + libname; //$NON-NLS-2$

		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		assertNotNull(sessionHandle);

		sessionHandle.setResourceFolder(""); //$NON-NLS-1$
		designHandle = sessionHandle.openDesign(filename2);
		libraryHandle = sessionHandle.openLibrary(libname);
		designHandle.includeLibrary(libname, "lib"); //$NON-NLS-1$
		assertNotNull(designHandle.getLibrary("lib")); //$NON-NLS-1$

	}
}
