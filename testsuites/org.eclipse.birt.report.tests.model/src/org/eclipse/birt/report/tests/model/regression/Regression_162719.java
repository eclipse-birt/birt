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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * Support widows/orphans properties on block text item(label, text, data etc),
 * About the definition of widows/orphans, please refer to CSS spec.
 * <p>
 * <b>Test Description:</b>
 * <ol>
 * <li>setRowFetchLimit()
 * <li>getRowFetchLimit()
 * </ol>
 */
public class Regression_162719 extends BaseTestCase {

	private final static String REPORT = "regression_162719.xml"; //$NON-NLS-1$

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(REPORT, REPORT);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * setRowFetchLimit()/getRowFetchLimit()
	 *
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_162719() throws DesignFileException, SemanticException {
		openDesign(REPORT);
		OdaDataSetHandle dataset = (OdaDataSetHandle) designHandle.findDataSet("Data Set");
		assertNotNull(dataset);

		dataset.setRowFetchLimit(10);
		assertEquals(10, dataset.getRowFetchLimit());
	}
}
