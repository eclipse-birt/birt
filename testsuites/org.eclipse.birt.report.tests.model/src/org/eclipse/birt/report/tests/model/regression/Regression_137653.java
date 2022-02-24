/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 *
 *  @author shu
 */
/**
 *
 *  @author shu
 */
/**
 * Regression description:
 * </p>
 * Change flatfile extension id "org.eclipse.birt.report.data.oda.flatfile" in
 * old design file to the new extension id
 * "org.eclipse.datatools.connectivity.oda.flatfile"
 * </p>
 * Test description:
 * <p>
 * Open old design file with flatfile extension id, check its new extension id
 * </p>
 */

public class Regression_137653 extends BaseTestCase {

	private String filename = "Regression_137653.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);

	}

	/**
	 * @throws DesignFileException
	 */

	public void test_regression_137653() throws DesignFileException {
		openDesign(filename);
		OdaDataSourceHandle source = (OdaDataSourceHandle) designHandle.findDataSource("Data Source1"); //$NON-NLS-1$
		assertEquals("org.eclipse.datatools.connectivity.oda.flatfile", source //$NON-NLS-1$
				.getExtensionID());

		OdaDataSetHandle set = (OdaDataSetHandle) designHandle.findDataSet("Data Set1"); //$NON-NLS-1$
		assertEquals("org.eclipse.datatools.connectivity.oda.flatfile.dataSet", set //$NON-NLS-1$
				.getExtensionID());
	}
}
