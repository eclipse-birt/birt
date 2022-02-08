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
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Steps to reproduce:
 * <ol>
 * <li>New a library "LibB", and a label with content "bbb"
 * <li>New a library "LibA", includes LibB, and extends LibB.label
 * <li>New a report, includes LibA, and extends LibA.label
 * <li>Open LibB, change the content of label to "aaa"
 * <li>Switch to the report, refresh it
 * </ol>
 * <p>
 * Expected result: The content of label in report is "aaa" Actual result:
 * java.lang.NullPointerException
 * </p>
 * Test description:
 * <p>
 * Report include LibA, LibA include LibB, and extends an label from LibB.
 * Follow the description ensure no exception occur when doing refresh.
 * </p>
 */
public class Regression_132938 extends BaseTestCase {

	private final static String INPUT = "regression_132938.xml"; //$NON-NLS-1$
	private final static String LIBRARY_A = "regression_132938_libA.xml";
	private final static String LIBRARY_B = "regression_132938_libB.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file

		copyInputToFile(INPUT_FOLDER + "/" + INPUT);
		copyInputToFile(INPUT_FOLDER + "/" + LIBRARY_A);
		copyInputToFile(INPUT_FOLDER + "/" + LIBRARY_B);
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 */
	public void test_regression_132938() throws DesignFileException, SemanticException, IOException {
		String report = getTempFolder() + "/" + INPUT_FOLDER + "/" + INPUT;
		String libA = getTempFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY_A;
		String libB = getTempFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY_B;

		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = sessionHandle.openDesign(report);

		designHandle.includeLibrary(LIBRARY_A, "regression_132938_libA"); //$NON-NLS-1$
		libraryHandle = designHandle.getLibrary("regression_132938_libA"); //$NON-NLS-1$

		LabelHandle label = (LabelHandle) libraryHandle.findElement("NewLabel");
		assertNotNull(label);

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle rLabel = (LabelHandle) factory.newElementFrom(label, "rLabel");
		assertEquals("bbb", rLabel.getText());
		designHandle.saveAs(getTempFolder() + "/" + INPUT_FOLDER + "/" + INPUT);

		libraryHandle = sessionHandle.openLibrary(libB);
		LabelHandle label_lib2 = (LabelHandle) libraryHandle.findElement("NewLabel");
		label_lib2.setText("aaa");
		libraryHandle.saveAs(getTempFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY_B);

		designHandle.reloadLibrary(libraryHandle);

		ElementFactory factory1 = designHandle.getElementFactory();
		LabelHandle l = (LabelHandle) factory1.newElementFrom(label, "rlabel");
		assertEquals("aaa", l.getText());

	}
}
