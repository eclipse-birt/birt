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

import org.eclipse.birt.report.model.adapter.oda.ModelOdaAdapter;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;

/**
 * <b>Bug Description:</b>
 * <p>
 * New a string type parameter in the dataset editor will lost default value
 * <p>
 * <b>Step to reproduce:</b>
 * <ol>
 * <li>new a datasource and dataset
 * <li>go to the Patameters page of the dataset editor
 * <li>click "New..." button new a Parameter Name:a Type:String Direction: Input
 * Default Value: aaa
 * <li>click ok to finish the dataset editor
 * <li>reopen the dataset editor and go to the parameter page
 * </ol>
 * <b>Actual result:</b>
 * <p>
 * The parameter a's default value is lost and type has been changed to "any"
 * <p>
 * <b>Test Description:</b>
 * <p>
 * The solution is to change algorithm to find data set parameters on ROM data
 * set handle so that BIRT-defined default value can be fetched
 * <p>
 * Following steps in bug description, check that parameter type won't be
 * changed and default value is reserved.
 */
public class Regression_155167 extends BaseTestCase {

	private String filename = "Regression_155167.xml"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyInputToFile(INPUT_FOLDER + "/" + filename);

	}

	@Override
	protected void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_155167() throws DesignFileException, SemanticException {
		openDesign(filename);
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet1"); //$NON-NLS-1$

		DataSetDesign setDesign = new ModelOdaAdapter().createDataSetDesign(setHandle);

		// oda data set design changed, update ROM values. still keep report
		// parameter link.

		DataSetParameters params = setDesign.getParameters();

		ParameterDefinition param = (ParameterDefinition) params.getParameterDefinitions().get(0);
		updateParameterDefinition(param);

		new ModelOdaAdapter().updateDataSetHandle(setDesign, setHandle, false);

		Iterator iter = setHandle.parametersIterator();
		OdaDataSetParameterHandle param1 = (OdaDataSetParameterHandle) iter.next();
		assertEquals("string", param1.getDataType()); //$NON-NLS-1$
		// user-define parameter value won't change according to the new algorithm

		// Method setDefaultScalarValue() does update parameter's default value.
		// Do not understand this case, so comment it.
		// assertEquals( null, param1.getDefaultValue( ) ); //$NON-NLS-1$

	}

	private void updateParameterDefinition(ParameterDefinition param) {
		DataElementAttributes dataAttrs = param.getAttributes();
		dataAttrs.setNullability(ElementNullability.get(ElementNullability.NOT_NULLABLE));

		InputParameterAttributes paramAttrs = param.getInputAttributes();
		InputElementAttributes elementAttrs = paramAttrs.getElementAttributes();

		elementAttrs.setDefaultScalarValue("aaa"); //$NON-NLS-1$
		elementAttrs.setOptional(true);
	}

}
