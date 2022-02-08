/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Export table with group to library will generate redundant bindings
 * </p>
 * Test description:
 * <p>
 * Make sure generated bindings in library are the same as report.
 * </p>
 */
public class Regression_236825 extends BaseTestCase {
	private final static String REPORT = "regression_236825.xml";
	private final static String LIBRARY = "regression_236825_lib.xml";
	private final static String GOLDEN = "regression_236825_lib_golden.xml";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyInputToFile(INPUT_FOLDER + "/" + REPORT);
		copyInputToFile(INPUT_FOLDER + "/" + LIBRARY);
		copyGoldenToFile(GOLDEN_FOLDER + "/" + GOLDEN);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws Exception
	 * 
	 */

	public void test_regression_236825() throws Exception {
		openDesign(REPORT);
		openLibrary(LIBRARY);
		String outputfile = genOutputFile(LIBRARY);
		String fileName = getTempFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY;
		ElementExportUtil.exportDesign(designHandle, fileName, false, true);
		copyFile(fileName, outputfile);
		assertTrue(compareTextFile(GOLDEN, LIBRARY));
	}
}
