/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * The name of extended data set is "newodadataset1"
 * </p>
 * Test description:
 * <p>
 * The name of extended data set should be the same as base element if it's
 * unique in the report
 * </p>
 */

public class Regression_123058 extends BaseTestCase {

	private String filename = "Regression_123058.xml"; //$NON-NLS-1$
	private String libraryname = "Regression_123058_Lib.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);
		copyResource_INPUT(libraryname, libraryname);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_123058() throws DesignFileException, SemanticException {
		openDesign(filename);

		designHandle.includeLibrary(libraryname, "Lib"); //$NON-NLS-1$
		DataSetHandle dataset = designHandle.getLibrary("Lib").findDataSet( //$NON-NLS-1$
				"Data Set"); //$NON-NLS-1$

		DataSetHandle extenddataset = (DataSetHandle) designHandle.getElementFactory().newElementFrom(dataset,
				dataset.getName());
		designHandle.getDataSets().add(extenddataset);
		assertEquals("Data Set", extenddataset.getName()); //$NON-NLS-1$
	}
}
