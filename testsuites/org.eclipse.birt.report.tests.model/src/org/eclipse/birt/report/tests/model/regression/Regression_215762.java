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

import java.io.ByteArrayOutputStream;
import org.eclipse.birt.report.model.api.util.DocumentUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * Can not preview the attached report in Web Viewer.
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Check the design is ok after flattern it.
 *
 */
public class Regression_215762 extends BaseTestCase {

	private final static String REPORT = "regression_215762.rptdesign";
	private final static String LIBRARY = "regression_215762.rptlibrary";
	private final static String GOLDEN = "regression_215762_golden.rptdesign";

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyInputToFile(INPUT_FOLDER + "/" + REPORT);
		copyInputToFile(INPUT_FOLDER + "/" + LIBRARY);
		copyGoldenToFile(GOLDEN_FOLDER + "/" + GOLDEN);
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_215762() throws Exception {
		openDesign(REPORT);
		os = new ByteArrayOutputStream();
		designHandle = DocumentUtil.serialize(designHandle, os);

		String output = this.genOutputFile(REPORT);
		designHandle.saveAs(output);

		assertTrue(compareTextFile(GOLDEN, REPORT));
	}
}
