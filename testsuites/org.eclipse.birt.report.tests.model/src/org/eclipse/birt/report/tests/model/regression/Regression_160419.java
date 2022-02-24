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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * Support double quotation in default value for data set parameter conversion
 * <p>
 * Steps to reproduce:<br>
 * 1.New a sample data source and a data set with below<br>
 * query: select * from CLASSICMODELS.CUSTOMERS where
 * CLASSICMODELS.CUSTOMERS.CUSTOMERNAME like ?<br>
 * 2.Switch to Parameters, add default value "A%" for the auto-generated
 * parameter<br>
 * 3.Click "..." for "Linked to Report Parameter" Expected result: Defautl value
 * "A%" is copied to report parameter without double quotation Actual result:
 * Default value is lost in report parameter
 * <p>
 * <b>Test description:</b>
 * <p>
 * New a dataset with parameter and set it default value to "A%", binding it to
 * the report parameter<br>
 * Check the default value
 * <p>
 */

public class Regression_160419 extends BaseTestCase {

	public final static String REPORT = "regression_160419.xml";

	public void test_regression_160419() throws Exception {
		// open the report design
		openDesign(REPORT);
		OdaDataSetHandle dataset = (OdaDataSetHandle) designHandle.findDataSet("Data Set");
		Iterator parameters = dataset.parametersIterator();
		// find the parameters
		OdaDataSetParameterHandle parameter = (OdaDataSetParameterHandle) parameters.next();

		assertNotNull(parameter);
		parameter.setDefaultValue("\"A%\"");

		// bind to the report parameter
		parameter.setParamName("p1");

		assertEquals("\"A%\"", parameter.getDefaultValue());
	}
}
