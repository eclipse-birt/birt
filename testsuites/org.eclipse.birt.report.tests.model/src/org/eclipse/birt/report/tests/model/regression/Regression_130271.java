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

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Extends lib.datasource and lib.dataset, lib.dataset can't be extended
 * </p>
 * Test description:
 * <p>
 * Extends lib.datasource and lib.dataset
 * </p>
 */

public class Regression_130271 extends BaseTestCase {

	private final static String INPUT = "Reg_130271.xml"; //$NON-NLS-1$
	private final static String LIBRARY = "Reg_130271_lib.xml";//$NON-NLS-1$
	private final static String OUTPUT = "Reg_130271_out.xml";//$NON-NLS-1$
	private final static String GOLDEN = "Reg_130271_golden.xml";//$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file

		copyInputToFile(INPUT_FOLDER + "/" + INPUT);
		copyInputToFile(INPUT_FOLDER + "/" + LIBRARY);
		copyGoldenToFile(GOLDEN_FOLDER + "/" + GOLDEN);
	}

	/**
	 * @throws Exception
	 * @throws Exception
	 */

	public void test_regression_130271() throws Exception {

		openLibrary(LIBRARY);
		DataSourceHandle datasource = libraryHandle.findDataSource("Data Source");//$NON-NLS-1$
		DataSetHandle dataset = libraryHandle.findDataSet("Data Set");//$NON-NLS-1$

		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = sessionHandle.createDesign();

		openDesign(INPUT);

		// String filename = this.getClassFolder() + "/" + INPUT_FOLDER + "/" + INPUT;
		// designHandle.setFileName( filename );

		designHandle.includeLibrary(LIBRARY, "lib");//$NON-NLS-1$
		DataSourceHandle dsource = (DataSourceHandle) designHandle.getElementFactory().newElementFrom(datasource,
				"dsource");//$NON-NLS-1$
		DataSetHandle dset = (DataSetHandle) designHandle.getElementFactory().newElementFrom(dataset, "dset");//$NON-NLS-1$

		designHandle.getDataSources().add(dsource);
		designHandle.getDataSets().add(dset);

		String TempFile = this.genOutputFile(OUTPUT);
		designHandle.saveAs(TempFile);
		assertTrue(super.compareTextFile(GOLDEN, OUTPUT));
	}
}
