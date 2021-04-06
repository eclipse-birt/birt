/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * DataException when using ReportRunner to run attached report
 * <p>
 * <b>Test Description:</b>
 * <p>
 * The solution is When did backward compatibility for rows[], should not create
 * bound columns for the design with version between 3.2.0 and 3.2.3. Added
 * version control for this.
 * <p>
 * Open old design file and save it. New parsed design should have no change
 */
public class Regression_149618 extends BaseTestCase {

	private String filename = "Regression_149618.xml"; //$NON-NLS-1$
	private String outfile = "Regression_149618_out.xml"; //$NON-NLS-1$
	private String goldenfile = "Regression_149618_golden.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( filename , filename );
		// copyResource_GOLDEN( goldenfile , goldenfile );
		copyInputToFile(INPUT_FOLDER + "/" + filename);
		copyGoldenToFile(GOLDEN_FOLDER + "/" + goldenfile);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws Exception
	 */
	public void test_regression_149618() throws Exception {
		openDesign(filename);
		saveAs(outfile);

		// No change between input file and parsed file
		// assertTrue( compareTextFile( goldenfile, outfile ) );

		String TempFile = this.genOutputFile(outfile);
		designHandle.saveAs(TempFile);
		assertTrue(compareTextFile(goldenfile, outfile));
	}

}
