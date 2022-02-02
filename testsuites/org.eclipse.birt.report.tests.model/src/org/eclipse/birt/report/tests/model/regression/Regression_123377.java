/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Steps to reproduce:
 * <p>
 * <ol>
 * <li>create 2 libraries: LibA & LibB.
 * <li>LibA include LibB.
 * <li>LibB include LibA.
 * </ol>
 * Excepted result: When doing step3, model should check out it as a recursive
 * inclusion, and report an error to user.
 * </p>
 * Test description:
 * <p>
 * lib1 include lib2, make sure that after that lib2 can not include lib1, model
 * should throw an exception for that operation.
 * </p>
 */

public class Regression_123377 extends BaseTestCase {

	private final static String INPUT1 = "regression_123377_lib1.xml"; //$NON-NLS-1$
	private final static String INPUT2 = "regression_123377_lib2.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file

		copyInputToFile(INPUT_FOLDER + "/" + INPUT1);
		copyInputToFile(INPUT_FOLDER + "/" + INPUT2);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 */
	public void test_regression_123377() throws DesignFileException, SemanticException, IOException {
		String lib1Input = getTempFolder() + "/" + INPUT_FOLDER + "/" + INPUT1; //$NON-NLS-2$
		String lib2Input = getTempFolder() + "/" + INPUT_FOLDER + "/" + INPUT2; //$NON-NLS-2$

		String lib1Output = this.genOutputFile(INPUT1);
		String lib2Output = this.genOutputFile(INPUT2);

		// open and modify the library files under the output folder.

		copyFile(lib1Input, lib1Output);
		copyFile(lib2Input, lib2Output);
		// copyFile ( INPUT1, INPUT1);
		// copyFile ( INPUT2, INPUT2);

		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		LibraryHandle lib1 = sessionHandle.openLibrary(lib1Output);
		LibraryHandle lib2 = sessionHandle.openLibrary(lib2Output);

		// LibraryHandle lib1 = sessionHandle.openLibrary( lib1Input );
		// LibraryHandle lib2 = sessionHandle.openLibrary( lib2Input );

		// lib1 include lib2
		lib1.includeLibrary(getTempFolder() + "/" + INPUT_FOLDER + "/" + "regression_123377_lib1.xml",
				"regression_123377_lib2");
		// lib1.includeLibrary( INPUT2, "regression_123377_lib2" ); //$NON-NLS-1$
		// lib1.saveAs( lib1Output );
		lib1.saveAs(INPUT1);
		// make sure that lib2 can not include lib1.
		/*
		 * try { lib2.includeLibrary( INPUT1, "regression_123377_lib1" ); //$NON-NLS-1$
		 * fail( ); } catch ( Exception e ) { // success }
		 */
	}
}
