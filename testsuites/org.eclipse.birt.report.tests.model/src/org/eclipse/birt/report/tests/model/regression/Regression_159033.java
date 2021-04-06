/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Adding Datasource Property Bindings with 10,000+ ids causes incorrect XML
 * creation.
 * <p>
 * Test description:
 * <p>
 * This test make sure that the ids that larger than 10000 will not contain the
 * group symbol.
 * <p>
 */
public class Regression_159033 extends BaseTestCase {

	private final static String REPORT = "regression_159033.rptdesign";
	private final static String OUTPUT = "regression_159033.rptdesign";
	String outputFile = this.genOutputFile(OUTPUT);

	public void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + REPORT);
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_159033() throws Exception {
		openDesign(REPORT);

		DataSetHandle ds = designHandle.findDataSet("OFFICE");
		assertNotNull(ds);
		ds.setPropertyBinding("queryText", "c.c.c");
		// ds.setPropertyBinding( "queryTimeOut", "adsf" );
		designHandle.saveAs(outputFile);

		BufferedReader reader = new BufferedReader(new FileReader(new File(outputFile)));
		String str = null;
		while ((str = reader.readLine()) != null) {
			if (str.indexOf("14,832") > 0) {
				fail("ids are exist!");
			}
		}

	}
}
