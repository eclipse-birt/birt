/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * Lost Default Value after having bound a Data Set Parameter to a Report
 * Parameter
 * <p>
 * After using an ODA designer to define a data set design that has parameters
 * with default value, if one then binds this data set parameter to a report
 * parameter, the original literal constant default value provided automatically
 * on the data set parameter is lost. Now one has to manually re-enter such
 * constant value again in the report parameter.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Add a Data Set with a data set parameter, set its default value to String
 * "100". Bind it to a report parameter, make sure that the default value will
 * not lost.
 * <p>
 */
public class Regression_140287 extends BaseTestCase {

	private final static String REPORT = "regression_140287.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(REPORT, REPORT);

	}

	/**
	 * @throws DesignFileException
	 */

	public void test_regression_140287() throws DesignFileException {
		openDesign(REPORT);

		OdaDataSetHandle ds = (OdaDataSetHandle) designHandle.findDataSet("Data Set"); //$NON-NLS-1$
		Iterator parameters = ds.parametersIterator();
		OdaDataSetParameterHandle param1 = null;
		while (parameters.hasNext()) {
			OdaDataSetParameterHandle paramHandle = (OdaDataSetParameterHandle) parameters.next();
			if ("param1".equals(paramHandle.getName())) //$NON-NLS-1$
			{
				param1 = paramHandle;
				break;
			}
		}

		assertNotNull(param1);
		assertEquals("\"100\"", param1.getDefaultValue()); //$NON-NLS-1$

		// bind it to report parameter.

		param1.setParamName("p1"); //$NON-NLS-1$

		// make sure the default value is not cleared.

		assertEquals("\"100\"", param1.getDefaultValue()); //$NON-NLS-1$
	}
}
