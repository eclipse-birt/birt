/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Description: Incorrect reference message
 * <p>
 * Steps to reproduce:
 * <p>
 * <ol>
 * <li>Add a data source and a data set
 * <li>Pull the data set into the report
 * <li>Delete the table in the report
 * <li>Delete the data set
 * <li>Delete the data source
 * </ol>
 * Expected result: No warning message
 * <p>
 * Actual result:
 * <ol>
 * <li>When deleting the data set, message says "ODA Data Set-Data set has
 * following clients:Table"
 * <li>When deleting the data source, message says "ODA Data Source-Data source
 * has following clients:Data set"
 * </ol>
 * Test description:
 * <p>
 * Follow the steps, see if exception throws.
 * </p>
 */

public class Regression_79040 extends BaseTestCase {

	private final static String INPUT = "regression_79040.rptdesign"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);

	}

	public void test_regression_79040() throws DesignFileException, SemanticException {
		openDesign(INPUT);
		TableHandle table1 = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$

		assertNotNull(table1);
		assertEquals("Data Set", table1.getDataSet().getName()); //$NON-NLS-1$

		// drop table, data set, data source in order.
		table1.drop();

		DataSetHandle dset = designHandle.findDataSet("Data Set"); //$NON-NLS-1$
		DataSourceHandle dsource = dset.getDataSource();

		dset.drop();
		dsource.drop();

		// success if no exception throws.
	}
}
