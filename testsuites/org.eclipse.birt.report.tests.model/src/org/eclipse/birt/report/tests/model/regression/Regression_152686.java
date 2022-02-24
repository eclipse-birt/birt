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

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Using todays BIRT source cvs head, and 1.4 compiler compliance. What I'm
 * seeing is the following: If I make the following report manipulation calls
 * using BIRT API.
 * <p>
 *
 * <PRE>
 *
 * String REPORT_LOC = "file:/c:/temp/birtbug/report.rptdesign";
 * DesignEngine designEngine = new DesignEngine(new DesignConfig());
 * SessionHandle session = designEngine.newSessionHandle(ULocale.getDefault()); // this works using a URL
 * string design = session.openDesign(REPORT_LOC);
 *
 * </PRE>
 *
 * <p>
 * ... do some work on the design and then call design.save() design.save calls
 * XMLWriter( File outputFile, String signature ) in here the writer constructor
 * tries to open a stream = new FileOutputStream( outputFile );
 * <p>
 * But this will fail, because the outputFile is a URL string and throws an
 * exception.
 * <p>
 * Expected: The report manipulation api, needs to handle URL strings
 * consistently, or they need to seperate out the API to deal with URLS to
 * reports seperately from api that just deals with java File objects.
 * </p>
 * Test description:
 * <p>
 * Make sure Model support URL file name when the protocal is file:/ in save as
 * well.
 * </p>
 */
public class Regression_152686 extends BaseTestCase {

	private final static String INPUT = "regression_152686.xml"; //$NON-NLS-1$

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyInputToFile(INPUT_FOLDER + "/" + INPUT);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * Tests the save to give a file name like "file:/c:/test" -- containing file
	 * schema.
	 *
	 * @throws Exception
	 */

	public void test_regression_152686() throws Exception {
		copyFile(getTempFolder() + "/" + INPUT_FOLDER + "/" + INPUT, this.genOutputFile(INPUT));

		String fileName = "file" //$NON-NLS-1$
				+ ":" + this.genOutputFile(INPUT); //$NON-NLS-1$

		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle(null);

		// did some changes and save the report.

		ReportDesignHandle reportHandle = session.openDesign(fileName);
		LabelHandle label = (LabelHandle) reportHandle.findElement("label1"); //$NON-NLS-1$
		label.setText("bingo"); //$NON-NLS-1$

		reportHandle.save();

		// make sure the change is saved correctly.

		reportHandle = session.openDesign(this.genOutputFile(INPUT));
		LabelHandle label2 = (LabelHandle) reportHandle.findElement("label1"); //$NON-NLS-1$
		assertEquals("bingo", label2.getText()); //$NON-NLS-1$
	}
}
