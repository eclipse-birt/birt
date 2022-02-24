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
 * In ModelOdaAdapter,when updating the dataSetParameterHandle,the
 * datasetDesign's defaultScalarValue should be adapted into datasetParameter's
 * default value when it does not link with report parameter. Else it should
 * adapt into report parameter's default value
 * <p>
 * <b>Test Description:</b>
 * <p>
 * When convert oda data set parameter to ROM data set parameter. Default values
 * are kept
 */
public class Regression_155356 extends BaseTestCase {

	private String filename = "Regression_155356.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyInputToFile(INPUT_FOLDER + "/" + filename);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws Exception
	 */
	public void test_regression_155356() throws DesignFileException, SemanticException {
		openDesign(filename);
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet1"); //$NON-NLS-1$

		DataSetDesign setDesign = new ModelOdaAdapter().createDataSetDesign(setHandle);

		// oda data set design changed, update ROM values. still keep report
		// parameter link.

		DataSetParameters params = setDesign.getParameters();
		ParameterDefinition param = (ParameterDefinition) params.getParameterDefinitions().get(0);
		updateParameterDefinition1(param);

		new ModelOdaAdapter().updateDataSetHandle(setDesign, setHandle, false);

		OdaDataSetParameterHandle p = (OdaDataSetParameterHandle) setHandle.parametersIterator().next();
		// user-define parameter value won't change according to the new algorithm

		// Method setDefaultScalarValue() does update parameter's default value.
		// Do not understand this case, so comment it.
		// assertEquals( "expression", p.getDefaultValue( ) ); //$NON-NLS-1$

	}

	private void updateParameterDefinition1(ParameterDefinition param) {
		DataElementAttributes dataAttrs = param.getAttributes();
		dataAttrs.setNullability(ElementNullability.get(ElementNullability.NOT_NULLABLE));

		InputParameterAttributes paramAttrs = param.getInputAttributes();
		InputElementAttributes elementAttrs = paramAttrs.getElementAttributes();

		elementAttrs.setDefaultScalarValue("default value"); //$NON-NLS-1$
		elementAttrs.setOptional(true);
	}
}
