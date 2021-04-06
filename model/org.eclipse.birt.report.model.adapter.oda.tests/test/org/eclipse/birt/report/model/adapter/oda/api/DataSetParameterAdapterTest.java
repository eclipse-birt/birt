/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.ModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.birt.report.model.adapter.oda.model.util.SchemaConversionUtil;
import org.eclipse.birt.report.model.adapter.oda.util.BaseTestCase;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Test cases to convert Oda data set parameters and ROM data set parameter and
 * linked report parameters.
 * 
 */

public class DataSetParameterAdapterTest extends BaseTestCase {

	/**
	 * Test parameter count.
	 * 
	 * @param setHandle oda dataset handle.
	 */

	private void testParametersCount(OdaDataSetHandle setHandle, int expectValue) {
		int count = 0;
		Iterator iterator = setHandle.parametersIterator();
		while (iterator.hasNext()) {
			iterator.next();
			++count;
		}
		assertEquals(expectValue, count);
	}

	/**
	 * Test New rule of merge parameter between <code>DataSetDesign</code> and
	 * <code>DataSetHandle</code>
	 * 
	 * @throws Exception
	 */

	public void testMergeParamDefnFromDesignToHandle() throws Exception {

		openDesign("DataSetMergeTest.xml"); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("Data Set"); //$NON-NLS-1$

		DataSetDesign setDesign = new ModelOdaAdapter().createDataSetDesign(setHandle);
		setHandle.setProperty(OdaDataSetHandle.PARAMETERS_PROP, null);

		// Parameters defined in DataSetDesign , but not in DataSetHandle

		IModelOdaAdapter adapter = new ModelOdaAdapter();
		adapter.updateDataSetHandle(setDesign, setHandle, false);
		testParametersCount(setHandle, 3);

		// Parameter 2 defined in DataSetHandle , but not in DataSetDesign.

		setHandle.setDesignerValues(null);
		PropertyHandle propHandle = setHandle.getPropertyHandle(OdaDataSetHandle.PARAMETERS_PROP);
		OdaDataSetParameter parameter = (OdaDataSetParameter) propHandle.getAt(2).getStructure().copy();
		propHandle.removeItem(2);

		DataSetDesign setDesign2 = new ModelOdaAdapter().createDataSetDesign(setHandle);
		propHandle.addItem(parameter);

		adapter.updateDataSetHandle(setDesign2, setHandle, false);
		testParametersCount(setHandle, 3);

	}

	private void verifyDataSetParams(OdaDataSetHandle oldSetHandle) throws Exception {
		saveAndOpenDesign();
		OdaDataSetHandle newSetHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet1");
		List newParams = (List) newSetHandle.getElement().getProperty(null, "parameters");
		List oldParams = (List) oldSetHandle.getElement().getProperty(null, "parameters");

		assertEquals(oldParams.size(), newParams.size());
		for (int i = 0; i < oldParams.size(); i++) {
			OdaDataSetParameter oldParam = (OdaDataSetParameter) oldParams.get(i);
			OdaDataSetParameter newParam = (OdaDataSetParameter) newParams.get(i);
			assertEquals(oldParam.getPosition(), newParam.getPosition());
			assertEquals(oldParam.getDataType(), newParam.getDataType());
			assertEquals(oldParam.isInput(), newParam.isInput());
			assertEquals(oldParam.getName(), newParam.getName());
			assertEquals(oldParam.getNativeName(), newParam.getNativeName());
			assertEquals(oldParam.getNativeDataType(), newParam.getNativeDataType());
			assertEquals(oldParam.getDefaultValue(), newParam.getDefaultValue());
			assertEquals(oldParam.getParamName(), newParam.getParamName());
			assertEquals(oldParam.isOptional(), newParam.isOptional());
			assertEquals(oldParam.allowNull(), newParam.allowNull());
		}
	}

	/**
	 * Test case:
	 * 
	 * <ul>
	 * <li>when convert default value, string type should distinguish expression and
	 * literal.
	 * <li>When convert oda data set parameter to ROM data set parameter. Default
	 * values are kept. <br>
	 * And the data type is kept even the native data type is unknown.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testDataTypeConversion() throws Exception {
		// create oda set handle.

		openDesign("DataSetParamConvertTest_1.xml"); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet1"); //$NON-NLS-1$

		DataSetDesign setDesign = new ModelOdaAdapter().createDataSetDesign(setHandle);

		DesignValues values = ModelFactory.eINSTANCE.createDesignValues();
		values.setDataSetParameters(
				SchemaConversionUtil.convertToAdapterParameters(EcoreUtil.copy(setDesign.getParameters())));

		saveDesignValuesToFile(values);
		assertTrue(compareTextFile("DataSetParamConvertTest_golden_1.xml")); //$NON-NLS-1$

		new ModelOdaAdapter().updateDataSetHandle(setDesign, setHandle, false);

		verifyDataSetParams(setHandle);
		/*
		 * save( ); assertTrue( compareTextFile( "DataSetParamConvertTest_golden_2.xml"
		 * ) ); //$NON-NLS-1$
		 */
	}

	/**
	 * Test case:
	 * 
	 * <ul>
	 * <li>Convert values from ODA parameter definition to ROM data set parameter.
	 * Focus on default values.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testValuesConversion() throws Exception {
		// create oda set handle.

		openDesign("DataSetParamConvertTest_1.xml"); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet1"); //$NON-NLS-1$

		DataSetDesign setDesign = new ModelOdaAdapter().createDataSetDesign(setHandle);

		// oda data set design changed, update ROM values. still keep report
		// parameter link.

		DataSetParameters params = setDesign.getParameters();
		ParameterDefinition param = (ParameterDefinition) params.getParameterDefinitions().get(0);
		updateParameterDefinition1(param);

		new ModelOdaAdapter().updateDataSetHandle(setDesign, setHandle, false);

		verifyParameterDefinition1();
		/*
		 * save(); assertTrue( compareTextFile( "DataSetParamConvertTest_golden_3.xml" )
		 * );
		 */
	}

	/**
	 * Updates a oda parameter definition. Keep the direction.
	 * 
	 * @param param
	 */

	private void updateParameterDefinition1(ParameterDefinition param) {
		DataElementAttributes dataAttrs = param.getAttributes();
		dataAttrs.setNullability(ElementNullability.get(ElementNullability.NOT_NULLABLE));

		InputParameterAttributes paramAttrs = param.getInputAttributes();
		InputElementAttributes elementAttrs = paramAttrs.getElementAttributes();

		elementAttrs.setDefaultScalarValue("new default value for report param 1"); //$NON-NLS-1$
		elementAttrs.setOptional(true);
	}

	private void verifyParameterDefinition1() throws Exception {
		saveAndOpenDesign();
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet1"); //$NON-NLS-1$
		List<Object> params = (List<Object>) setHandle.getElement().getProperty(null, "parameters");
		OdaDataSetParameter param = (OdaDataSetParameter) params.get(0);
		assertFalse(param.allowNull());
		assertEquals("new default value for report param 1", param.getDefaultValue());
		assertTrue(param.isOptional());
	}

	/**
	 * Tests the algorithm to create unique data set parameter names.
	 * 
	 * @throws Exception
	 */

	public void testDataSetParamNames() throws Exception {
		DataSetDesign setDesign = createDataSetDesignForParamNames();

		createDesign();
		OdaDataSetHandle setHandle = new ModelOdaAdapter().createDataSetHandle(setDesign, designHandle);

		designHandle.getDataSets().add(setHandle);

		/*
		 * save( ); assertTrue( compareTextFile( "DataSetParameterNameTest_golden.xml" )
		 * ); //$NON-NLS-1$
		 */

		verifyDataSetDesignForParamNames();

		setDesign = createDataSetDesignForParamNames1();

		setHandle = new ModelOdaAdapter().createDataSetHandle(setDesign, designHandle);

		List params = (List) setHandle.getProperty(OdaDataSetHandle.PARAMETERS_PROP);
		OdaDataSetParameter param = (OdaDataSetParameter) params.get(0);
		assertEquals(0, param.getPosition().intValue());

		param = (OdaDataSetParameter) params.get(1);
		assertEquals(1, param.getPosition().intValue());

		setDesign = createDataSetDesignForParamNames1();

		// should not exception

		setHandle = new ModelOdaAdapter().createDataSetHandle(setDesign, designHandle);
	}

	/**
	 * Creates a new <code>DataSetDesign</code>.
	 * 
	 * @return an object of <code>DataSetDesign</code>.
	 */

	static DataSetDesign createDataSetDesignForParamNames() {
		DataSetDesign setDesign = DesignFactory.eINSTANCE.createDataSetDesign();
		setDesign.setName("myDataSet1"); //$NON-NLS-1$
		setDesign.setDisplayName("data set display name"); //$NON-NLS-1$
		setDesign.setOdaExtensionDataSetId(OdaDataSetAdapterTest.DATA_SET_EXTENSIONID);

		Properties props = DesignFactory.eINSTANCE.createProperties();
		props.setProperty("queryTimeOut", "new public query time out"); //$NON-NLS-1$//$NON-NLS-2$
		setDesign.setPublicProperties(props);

		props = DesignFactory.eINSTANCE.createProperties();
		props.setProperty("queryTimeOut", "new private query time out"); //$NON-NLS-1$ //$NON-NLS-2$
		setDesign.setPrivateProperties(props);

		DataSetParameters params = DesignFactory.eINSTANCE.createDataSetParameters();
		ParameterDefinition paramDefn = DesignFactory.eINSTANCE.createParameterDefinition();
		DataElementAttributes dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes();
		dataAttrs.setName("nativeName1"); //$NON-NLS-1$
		dataAttrs.setPosition(1);
		dataAttrs.setNativeDataTypeCode(1);
		paramDefn.setAttributes(dataAttrs);
		paramDefn.setInOutMode(ParameterMode.get(ParameterMode.IN_OUT));

		params.getParameterDefinitions().add(paramDefn);

		paramDefn = DesignFactory.eINSTANCE.createParameterDefinition();
		dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes();
		dataAttrs.setName(""); //$NON-NLS-1$
		dataAttrs.setPosition(2);
		dataAttrs.setNativeDataTypeCode(1);
		paramDefn.setAttributes(dataAttrs);
		paramDefn.setInOutMode(ParameterMode.get(ParameterMode.IN_OUT));
		params.getParameterDefinitions().add(paramDefn);

		paramDefn = DesignFactory.eINSTANCE.createParameterDefinition();
		dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes();
		dataAttrs.setName("nativeName1"); //$NON-NLS-1$
		dataAttrs.setPosition(3);
		dataAttrs.setNativeDataTypeCode(1);
		paramDefn.setAttributes(dataAttrs);
		paramDefn.setInOutMode(ParameterMode.get(ParameterMode.IN_OUT));

		params.getParameterDefinitions().add(paramDefn);
		setDesign.setParameters(params);

		setDesign.setPrimaryResultSetName("resultset1"); //$NON-NLS-1$

		setDesign.setQueryText("new query text"); //$NON-NLS-1$

		// create the corresponding data source design

		setDesign.setDataSourceDesign(createDataSourceDesign());
		return setDesign;
	}

	private void verifyDataSetDesignForParamNames() throws Exception {
		saveAndOpenDesign();
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet1"); //$NON-NLS-1$
		assertNotNull(setHandle);
		assertEquals("data set display name", setHandle.getDisplayName());
		assertEquals("new query text", setHandle.getQueryText());

		OdaDataSet odaDataSet = (OdaDataSet) setHandle.getElement();
		assertEquals(OdaDataSetAdapterTest.DATA_SET_EXTENSIONID, odaDataSet.getProperty(null, "extensionID"));
		assertEquals("new public query time out", odaDataSet.getProperty(null, "queryTimeOut"));
		assertEquals("resultset1", odaDataSet.getProperty(null, "resultSetName"));

		List<Object> privateProps = (List<Object>) odaDataSet.getProperty(null, "privateDriverProperties");
		ExtendedProperty prop = (ExtendedProperty) privateProps.get(0);
		assertEquals("new private query time out", prop.getValue());

		List<Object> params = (List<Object>) odaDataSet.getProperty(null, "parameters");
		OdaDataSetParameter param = (OdaDataSetParameter) params.get(0);
		verifyParam(param, "nativeName1", 1, 1, true, true);
		param = (OdaDataSetParameter) params.get(1);
		verifyParam(param, "", 2, 1, true, true);
		param = (OdaDataSetParameter) params.get(2);
		verifyParam(param, "nativeName1", 3, 1, true, true);
	}

	private void verifyParam(OdaDataSetParameter param, String name, int position, int dataType, boolean in,
			boolean out) {
		assertEquals(name, param.getNativeName());
		assertEquals(Integer.valueOf(position), param.getPosition());
		assertEquals(Integer.valueOf(dataType), param.getNativeDataType());
		assertEquals(in, param.isInput());
		assertEquals(out, param.isOutput());
	}

	/**
	 * Creates a new <code>DataSourceDesign</code>.
	 * 
	 * @return an object of <code>DataSourceDesign</code>.
	 */

	private static DataSourceDesign createDataSourceDesign() {
		DataSourceDesign sourceDesign = DesignFactory.eINSTANCE.createDataSourceDesign();
		sourceDesign.setName("myDataSource1"); //$NON-NLS-1$
		sourceDesign.setDisplayName("data source display name"); //$NON-NLS-1$
		sourceDesign.setOdaExtensionId(OdaDataSetAdapterTest.DATA_SOURCE_EXTENSIONID);

		Properties props = DesignFactory.eINSTANCE.createProperties();
		props.setProperty("odaDriverClass", "new drivers"); //$NON-NLS-1$//$NON-NLS-2$
		props.setProperty("odaURL", "jdbc:sqlserver://localhost"); //$NON-NLS-1$//$NON-NLS-2$
		props.setProperty("odaUser", "new user"); //$NON-NLS-1$ //$NON-NLS-2$
		sourceDesign.setPublicProperties(props);

		props = DesignFactory.eINSTANCE.createProperties();
		props.setProperty("odaDriverClass", "new drivers"); //$NON-NLS-1$ //$NON-NLS-2$
		props.setProperty("odaPassword", "new password"); //$NON-NLS-1$ //$NON-NLS-2$
		sourceDesign.setPrivateProperties(props);

		return sourceDesign;
	}

	/**
	 * Creates a new <code>DataSetDesign</code>. Parameter positions are not set.
	 * 
	 * @return an object of <code>DataSetDesign</code>.
	 */

	static DataSetDesign createDataSetDesignForParamNames1() {
		DataSetDesign setDesign = DesignFactory.eINSTANCE.createDataSetDesign();
		setDesign.setName("myDataSet1"); //$NON-NLS-1$
		setDesign.setDisplayName("data set display name"); //$NON-NLS-1$
		setDesign.setOdaExtensionDataSetId(OdaDataSetAdapterTest.DATA_SET_EXTENSIONID);

		Properties props = DesignFactory.eINSTANCE.createProperties();
		props.setProperty("queryTimeOut", "new public query time out"); //$NON-NLS-1$//$NON-NLS-2$
		setDesign.setPublicProperties(props);

		props = DesignFactory.eINSTANCE.createProperties();
		props.setProperty("queryTimeOut", "new private query time out"); //$NON-NLS-1$ //$NON-NLS-2$
		setDesign.setPrivateProperties(props);

		DataSetParameters params = DesignFactory.eINSTANCE.createDataSetParameters();
		ParameterDefinition paramDefn = DesignFactory.eINSTANCE.createParameterDefinition();
		DataElementAttributes dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes();
		dataAttrs.setName("nativeName1"); //$NON-NLS-1$
		dataAttrs.setPosition(0);
		dataAttrs.setNativeDataTypeCode(1);
		paramDefn.setAttributes(dataAttrs);
		paramDefn.setInOutMode(ParameterMode.get(ParameterMode.IN_OUT));

		params.getParameterDefinitions().add(paramDefn);

		paramDefn = DesignFactory.eINSTANCE.createParameterDefinition();
		dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes();
		dataAttrs.setName(""); //$NON-NLS-1$
		dataAttrs.setPosition(1);
		dataAttrs.setNativeDataTypeCode(1);
		paramDefn.setAttributes(dataAttrs);
		paramDefn.setInOutMode(ParameterMode.get(ParameterMode.IN_OUT));
		params.getParameterDefinitions().add(paramDefn);

		params.getParameterDefinitions().add(paramDefn);
		setDesign.setParameters(params);

		setDesign.setPrimaryResultSetName("resultset1"); //$NON-NLS-1$

		setDesign.setQueryText("new query text"); //$NON-NLS-1$

		// create the corresponding data source design

		setDesign.setDataSourceDesign(createDataSourceDesign());
		return setDesign;
	}

	/**
	 * Creates a new <code>DataSetDesign</code>. Parameters positions are duplicate.
	 * 
	 * @return an object of <code>DataSetDesign</code>.
	 */

	static DataSetDesign createDataSetDesignForParamNames2() {
		DataSetDesign setDesign = DesignFactory.eINSTANCE.createDataSetDesign();
		setDesign.setName("myDataSet1"); //$NON-NLS-1$
		setDesign.setDisplayName("data set display name"); //$NON-NLS-1$
		setDesign.setOdaExtensionDataSetId(OdaDataSetAdapterTest.DATA_SET_EXTENSIONID);

		Properties props = DesignFactory.eINSTANCE.createProperties();
		props.setProperty("queryTimeOut", "new public query time out"); //$NON-NLS-1$//$NON-NLS-2$
		setDesign.setPublicProperties(props);

		props = DesignFactory.eINSTANCE.createProperties();
		props.setProperty("queryTimeOut", "new private query time out"); //$NON-NLS-1$ //$NON-NLS-2$
		setDesign.setPrivateProperties(props);

		DataSetParameters params = DesignFactory.eINSTANCE.createDataSetParameters();
		ParameterDefinition paramDefn = DesignFactory.eINSTANCE.createParameterDefinition();
		DataElementAttributes dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes();
		dataAttrs.setName("nativeName1"); //$NON-NLS-1$
		dataAttrs.setPosition(1);
		dataAttrs.setNativeDataTypeCode(1);
		paramDefn.setAttributes(dataAttrs);
		paramDefn.setInOutMode(ParameterMode.get(ParameterMode.IN_OUT));

		params.getParameterDefinitions().add(paramDefn);

		paramDefn = DesignFactory.eINSTANCE.createParameterDefinition();
		dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes();
		dataAttrs.setName(""); //$NON-NLS-1$
		dataAttrs.setPosition(1);
		dataAttrs.setNativeDataTypeCode(1);
		paramDefn.setAttributes(dataAttrs);
		paramDefn.setInOutMode(ParameterMode.get(ParameterMode.IN_OUT));
		params.getParameterDefinitions().add(paramDefn);

		paramDefn = DesignFactory.eINSTANCE.createParameterDefinition();
		dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes();
		dataAttrs.setName("nativeName1"); //$NON-NLS-1$
		dataAttrs.setPosition(3);
		dataAttrs.setNativeDataTypeCode(1);
		paramDefn.setAttributes(dataAttrs);
		paramDefn.setInOutMode(ParameterMode.get(ParameterMode.IN_OUT));

		params.getParameterDefinitions().add(paramDefn);
		setDesign.setParameters(params);

		setDesign.setPrimaryResultSetName("resultset1"); //$NON-NLS-1$

		setDesign.setQueryText("new query text"); //$NON-NLS-1$

		// create the corresponding data source design

		setDesign.setDataSourceDesign(createDataSourceDesign());
		return setDesign;
	}
}
