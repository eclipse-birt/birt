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
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Error message appears when deleting a invalid data set.
 * <p>
 * Steps to reproduce:
 * <p>
 * <ol>
 * <li>Add a data source and related data set.
 * <li>Delete the data source and copy the data set.
 * <li>Delete these two invalid data sets.
 * </ol>
 * <p>
 * <b>Actual result:</b>
 * <p>
 * the origional data set can be deleted without error message but error message
 * pops up when deleting copied data set.
 * 
 * <p>
 * <b>Expected result:</b>
 * <p>
 * Both of them can be deleted.
 * </p>
 * Test description:
 * <p>
 * Follow the steps, ensure that there won't be exception when deleting the two
 * invalid data sets.
 * </p>
 */
public class Regression_102003 extends BaseTestCase {

	private final static String INPUT = "regression_102003.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyResource_INPUT(INPUT, INPUT);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_102003() throws DesignFileException, SemanticException {
		openDesign(INPUT);

		// delete the data source

		designHandle.findDataSource("Data Source").drop(); //$NON-NLS-1$
		assertNull(designHandle.findDataSet("Data Source")); //$NON-NLS-1$

		// copy and add the data set

		DataSetHandle dsetHandle = designHandle.findDataSet("Data Set"); //$NON-NLS-1$

		assertEquals("Data Source", dsetHandle.getDataSourceName()); //$NON-NLS-1$
		assertNull(dsetHandle.getDataSource());
		DataSetHandle copied = (DataSetHandle) dsetHandle.copy().getHandle(design);
		copied.setName("Data Set1"); //$NON-NLS-1$

		designHandle.getDataSets().add(copied);

		// Delete the two invalid data sets.

		designHandle.findDataSet("Data Set").drop(); //$NON-NLS-1$
		designHandle.findDataSet("Data Set1").drop(); //$NON-NLS-1$

		assertNull(designHandle.findDataSet("Data Set")); //$NON-NLS-1$
		assertNull(designHandle.findDataSet("Data Set1")); //$NON-NLS-1$
	}
}
