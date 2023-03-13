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

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * When deleting the data source/data set, property binding on it is not deleted
 * </p>
 * Test description:
 * <p>
 * Check property binding will be cleaned when data source/data set is removed
 * </p>
 */

public class Regression_121498 extends BaseTestCase {

	private String filename = "Regression_121498.xml"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_121498() throws DesignFileException, SemanticException {
		openDesign(filename);
		DataSourceHandle datasource = designHandle.findDataSource("dsource"); //$NON-NLS-1$
		DataSetHandle dataset = designHandle.findDataSet("dset"); //$NON-NLS-1$
		assertEquals(1, datasource.getPropertyBindings().size());
		assertEquals(1, dataset.getPropertyBindings().size());

		datasource.drop();
		dataset.drop();

		assertEquals(0, datasource.getPropertyBindings().size());
		assertEquals(0, dataset.getPropertyBindings().size());
	}
}
