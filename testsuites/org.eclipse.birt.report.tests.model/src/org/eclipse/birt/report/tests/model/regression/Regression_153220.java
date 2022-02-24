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
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Steps to reproduce:</b>
 * <ol>
 * <li>New a library "Lib1", add a table
 * <li>New a library "Lib2", extends Lib1.table
 * <li>New a report, extends Lib2.table
 * <li>Delete table in Lib1, and publish the library
 * <li>Refresh the report
 * </ol>
 * <b>Actual result:</b>
 * <p>
 * org.eclipse.swt.SWTException: Failed to execute runnable
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Follow the steps in bug description, no error thrown out. and the reference
 * of table in report is unresolved
 */
public class Regression_153220 extends BaseTestCase {

	private final static String INPUT = "Regression_153220.xml"; //$NON-NLS-1$
	private final static String LIBRARY_A = "Regression_153220_lib1.xml"; //$NON-NLS-1$
	private final static String LIBRARY_B = "Regression_153220_lib2.xml"; //$NON-NLS-1$

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + INPUT);
		copyInputToFile(INPUT_FOLDER + "/" + LIBRARY_A);
		copyInputToFile(INPUT_FOLDER + "/" + LIBRARY_B);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws IOException
	 * @throws SemanticException
	 * @throws DesignFileException
	 */
	public void test_Regression_153220() throws IOException, DesignFileException, SemanticException {
		String report = getTempFolder() + "/" + INPUT_FOLDER + "/" + INPUT;
		String libA = getTempFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY_A;
		String libB = getTempFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY_B;

		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = sessionHandle.openDesign(report);

		designHandle.includeLibrary(LIBRARY_B, "lib2"); //$NON-NLS-1$
		libraryHandle = designHandle.getLibrary("lib2"); //$NON-NLS-1$
		TableHandle table = (TableHandle) libraryHandle.findElement("table2"); //$NON-NLS-1$
		assertNotNull(table);

		ElementFactory factory = designHandle.getElementFactory();
		TableHandle rtable = (TableHandle) factory.newElementFrom(table, "Rtable"); //$NON-NLS-1$
		designHandle.getBody().add(rtable);
		designHandle.saveAs(getTempFolder() + "/" + INPUT_FOLDER + "/" + INPUT);

		// drop table in lib1 and save the library
		libraryHandle = sessionHandle.openLibrary(libA);
		TableHandle table1 = (TableHandle) libraryHandle.findElement("table1"); //$NON-NLS-1$
		table1.drop();
		// libraryHandle.save( );
		libraryHandle.saveAs(getTempFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY_A);

		// reload lib2, no error, and table reference is unresolved
		designHandle.reloadLibrary(libraryHandle);
		assertFalse(rtable.isValidReferenceForCompoundElement());
	}
}
