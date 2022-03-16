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

import org.eclipse.birt.report.model.adapter.oda.ModelOdaAdapter;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;

/**
 * <b>Bug Description:</b>
 * <p>
 * Extended data source is localized after creating a data set with it
 * <p>
 * <b>Steps to reproduce:</b>
 * <ol>
 * <li>New a sample data source in library
 * <li>New a report, includes the library, extends Lib.datasource
 * <li>New a data set with lib.datasource in report
 * </ol>
 * <b>Expected result:</b>
 * <p>
 * No local properties for data source in report
 * <p>
 * <b>Actual result:</b>
 * <p>
 * All data source properties are localized
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Follow the steps in bug description, make sure data set has no local
 * properties
 */
public class Regression_153233 extends BaseTestCase {

	private String filename = "Regression_153233.xml"; //$NON-NLS-1$
	private String libname = "Regression_153233_lib.xml"; //$NON-NLS-1$

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + filename);
		copyInputToFile(INPUT_FOLDER + "/" + libname);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws Exception
	 */
	public void test_regression_153233() throws Exception {
		openDesign(filename);
		designHandle.includeLibrary(libname, "lib"); //$NON-NLS-1$
		DataSourceHandle parent = designHandle.getLibrary("lib").findDataSource("Data Source"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(parent);
		ModelOdaAdapter adapter = new ModelOdaAdapter();

		OdaDataSourceHandle dataSource = (OdaDataSourceHandle) designHandle.getElementFactory().newElementFrom(parent,
				"testSource"); //$NON-NLS-1$
		designHandle.getDataSources().add(dataSource);
		assertEquals(designHandle, dataSource.getRoot());

		adapter.createDataSourceDesign(dataSource);

		OdaDataSetHandle dataSet = designHandle.getElementFactory().newOdaDataSet("testDataSet", //$NON-NLS-1$
				"org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet"); //$NON-NLS-1$
		dataSet.setDataSource("testSource"); //$NON-NLS-1$
		designHandle.getDataSets().add(dataSet);
		assertEquals(designHandle, dataSet.getRoot());

		DataSetDesign dataSetDesign = adapter.createDataSetDesign(dataSet);
		dataSetDesign.setQueryText("new query text"); //$NON-NLS-1$

		// update data set handle

		adapter.updateDataSetHandle(dataSetDesign, dataSet, false);

		assertFalse(dataSource.hasLocalProperties());

	}
}
