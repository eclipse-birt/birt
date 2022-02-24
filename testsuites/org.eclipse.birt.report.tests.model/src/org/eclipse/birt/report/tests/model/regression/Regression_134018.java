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
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * <p>
 * Report item in extended master page lost when refreshing the report
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>New a library, add a label with content "aaa" in master page header
 * <li>New a report, extend lib.masterpage
 * <li>Switch to library, change content of label to "bbb"
 * <li>Switch to report, refresh it
 * </ol>
 * <b>Expected result:</b>
 * <p>
 * The content of label in report is changed to "bbb"
 * <p>
 * <b>Actual result:</b>
 * <p>
 * Label lost in outline untill reopen the report
 * <p>
 * Test description:
 * <p>
 * Follow the steps, make sure that label still exist in report when refreshing
 * the report
 * </p>
 */
public class Regression_134018 extends BaseTestCase {

	private final static String INPUT = "regression_134018.xml";
	private final static String LIBRARY = "regression_134018_lib.xml";//$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyInputToFile(INPUT_FOLDER + "/" + INPUT);
		copyInputToFile(INPUT_FOLDER + "/" + LIBRARY);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 */

	public void test_regression_134018() throws DesignFileException, SemanticException, IOException {

		String report = getTempFolder() + "/" + INPUT_FOLDER + "/" + INPUT;
		String libA = getTempFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY;

		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = sessionHandle.openDesign(report);

		designHandle.includeLibrary(LIBRARY, "regression_134018_lib"); //$NON-NLS-1$
		libraryHandle = designHandle.getLibrary("regression_134018_lib");

		MasterPageHandle basePage = libraryHandle.findMasterPage("basePage");
		assertNotNull(basePage);
		ElementFactory factory = designHandle.getElementFactory();
		SimpleMasterPageHandle newPage = (SimpleMasterPageHandle) factory.newElementFrom(basePage, "rBasePage");
		assertNotNull(newPage);
		designHandle.getMasterPages().add(newPage);
		designHandle.saveAs(report);

		libraryHandle = sessionHandle.openLibrary(libA);
		LabelHandle baseLabel = (LabelHandle) ((SimpleMasterPageHandle) libraryHandle.findMasterPage("basePage"))
				.getPageHeader().get(0);
		assertNotNull(baseLabel);
		assertEquals("baseLabel", baseLabel.getName()); //$NON-NLS-1$
		assertEquals("aaa", baseLabel.getText()); //$NON-NLS-1$

		baseLabel.setText("bbb");
		libraryHandle.saveAs(getTempFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY);
		designHandle.reloadLibrary(libraryHandle);

		LabelHandle baseLabel1 = (LabelHandle) ((SimpleMasterPageHandle) libraryHandle.findMasterPage("basePage"))
				.getPageHeader().get(0);
		assertNotNull(baseLabel1);
		assertEquals("bbb", baseLabel1.getText());

	}
}
