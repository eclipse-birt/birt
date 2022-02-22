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

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Design become invalid when I extend a masterpage from Library
 * <p>
 * Reproduce:
 * <p>
 * <ol>
 * <li>Deploy the library("lib"), and open the report.
 * <li>Drag Masterpage("page1") from "lib" to report.
 * <li>Reopen the report, it become invalid.
 * </ol>
 * <p>
 * Test description:
 * <p>
 * Follow the steps, and make sure that design is valid when reopened.
 * <p>
 */
public class Regression_148747 extends BaseTestCase {

	private final static String REPORT = "regression_148747.xml"; //$NON-NLS-1$
	private final static String LIB = "regression_148747_lib.xml"; //$NON-NLS-1$

	/**
	 * @throws IOException
	 * @throws DesignFileException
	 * @throws ExtendsException
	 * @throws ContentException
	 * @throws NameException
	 * @throws IOException
	 * @throws DesignFileException
	 * @throws ExtendsException
	 * @throws NameException
	 * @throws ContentException
	 */

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( REPORT , REPORT );
		// copyResource_INPUT( LIB , LIB );
		copyInputToFile(INPUT_FOLDER + "/" + REPORT);
		copyInputToFile(INPUT_FOLDER + "/" + LIB);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	public void test_regression_148747()
			throws IOException, DesignFileException, ExtendsException, ContentException, NameException {
		// we do the operation in the output folder.

		String tempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		if (!tempDir.endsWith(File.separator)) {
			tempDir += File.separator;
		}
		String outputFolder = tempDir + getFullQualifiedClassName() // $NON-NLS-1$
				+ File.separator + OUTPUT_FOLDER + File.separator;

		String inputFolder = getTempFolder() + File.separator + INPUT_FOLDER + File.separator;

//		String outputFolder = getTempFolder()  + File.separator + OUTPUT_FOLDER;

		copyFile(inputFolder + REPORT, outputFolder + REPORT);

		copyFile(inputFolder + LIB, outputFolder + LIB);

		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle(null);

		// Extends a master page from lib and save the report.

		ReportDesignHandle reportHandle = session.openDesign(outputFolder + "/" + REPORT);

		LibraryHandle lib = reportHandle.getLibrary("regression_148747_lib"); //$NON-NLS-1$
		assertNotNull(lib);

		MasterPageHandle page1 = lib.findMasterPage("page1"); //$NON-NLS-1$
		MasterPageHandle extendPage = (MasterPageHandle) reportHandle.getElementFactory().newElementFrom(page1,
				"childPage"); //$NON-NLS-1$

		reportHandle.getMasterPages().add(extendPage);

		// save and reopen, make sure the file is valid.

		reportHandle.save();
		reportHandle = session.openDesign(outputFolder + "/" + REPORT);

		assertNotNull(reportHandle.findMasterPage("childPage")); //$NON-NLS-1$
		assertEquals(0, reportHandle.getErrorList().size());
		assertTrue(reportHandle.isValid());

	}
}
