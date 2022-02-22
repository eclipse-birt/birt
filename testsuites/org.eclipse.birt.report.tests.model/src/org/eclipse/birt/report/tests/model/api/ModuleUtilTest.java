/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.tests.model.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TestCases for ModuleUtil.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 *
 * <tr>
 * <td>{@link #testReportValidation()}</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testLibraryValidation()}</td>
 * </tr>
 * </table>
 *
 */
public class ModuleUtilTest extends BaseTestCase {
	private final String reportName = "ModuleUtilTest_report.xml"; //$NON-NLS-1$
	private final String libraryName = "ModuleUtilTest_report.xml"; //$NON-NLS-1$
	private final String invalidreportName = "ModuleUtilTest_report_invalid.xml"; //$NON-NLS-1$
	private final String invalidlibraryName = "ModuleUtilTest_report_invalid.xml"; //$NON-NLS-1$

	public ModuleUtilTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static Test suite() {

		return new TestSuite(ModuleUtilTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( reportName , reportName );
		// copyResource_INPUT( libraryName , libraryName );
		// copyResource_INPUT( invalidreportName , invalidreportName );
		// copyResource_INPUT( invalidlibraryName , invalidlibraryName );
		copyInputToFile(INPUT_FOLDER + "/" + reportName);
		copyInputToFile(INPUT_FOLDER + "/" + libraryName);
		copyInputToFile(INPUT_FOLDER + "/" + invalidreportName);
		copyInputToFile(INPUT_FOLDER + "/" + invalidlibraryName);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * Test report validation
	 *
	 * @throws Exception
	 */
	public void testReportValidation() throws Exception {
		// test a valid report design

		openDesign(reportName);
		File file = new File(getTempFolder() + "/" + INPUT_FOLDER + "/" + reportName);
		InputStream is = new FileInputStream(file);
		SessionHandle session = DesignEngine.newSession(ULocale.ENGLISH);
		designHandle = session.openDesign(file.toString());
		assertTrue(ModuleUtil.isValidDesign(session, reportName, is));

		// test a invalid report design
		File file2 = new File(getTempFolder() + "/" + INPUT_FOLDER + "/" + invalidreportName);
		InputStream is2 = new FileInputStream(file2);
		SessionHandle session2 = DesignEngine.newSession(ULocale.ENGLISH);
		assertFalse(ModuleUtil.isValidDesign(session2, invalidreportName, is2));

	}

	/**
	 * Test library validation
	 *
	 * @throws Exception
	 */
	public void testLibraryValidation() throws Exception {
		// test a valid library
		openDesign(libraryName);
		File file = new File(getTempFolder() + "/" + INPUT_FOLDER + "/" + libraryName);
		InputStream is = new FileInputStream(file);
		SessionHandle session = DesignEngine.newSession(ULocale.ENGLISH);
		designHandle = session.openDesign(file.toString());
		assertTrue(ModuleUtil.isValidDesign(session, libraryName, is));

		// test a invalid library
		File file2 = new File(getTempFolder() + "/" + INPUT_FOLDER + "/" + invalidlibraryName);
		InputStream is2 = new FileInputStream(file2);
		SessionHandle session2 = DesignEngine.newSession(ULocale.ENGLISH);
		assertFalse(ModuleUtil.isValidDesign(session2, invalidlibraryName, is2));
	}
}
