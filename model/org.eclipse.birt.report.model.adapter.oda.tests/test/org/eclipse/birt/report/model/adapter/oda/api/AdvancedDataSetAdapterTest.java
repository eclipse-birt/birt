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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousOption;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousParameterNode;
import org.eclipse.birt.report.model.adapter.oda.ModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.util.BaseTestCase;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputPromptControlStyle;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;

public class AdvancedDataSetAdapterTest extends BaseTestCase
{

	/**
	 * Creates a new <code>DataSetDesign</code>.
	 * 
	 * @return an object of <code>DataSetDesign</code>.
	 */

	private DataSetDesign createDataSetDesign( )
	{
		DataSetDesign setDesign = DesignFactory.eINSTANCE.createDataSetDesign( );
		setDesign.setName( "myDataSet1" ); //$NON-NLS-1$
		setDesign.setDisplayName( "data set display name" ); //$NON-NLS-1$
		setDesign
				.setOdaExtensionDataSetId( OdaDataSetAdapterTest.DATA_SET_EXTENSIONID );

		Properties props = DesignFactory.eINSTANCE.createProperties( );
		props.setProperty( "queryTimeOut", "new public query time out" ); //$NON-NLS-1$//$NON-NLS-2$
		setDesign.setPublicProperties( props );

		props = DesignFactory.eINSTANCE.createProperties( );
		props.setProperty( "queryTimeOut", "new private query time out" ); //$NON-NLS-1$ //$NON-NLS-2$
		setDesign.setPrivateProperties( props );

		DataSetParameters params = DesignFactory.eINSTANCE
				.createDataSetParameters( );
		ParameterDefinition paramDefn = DesignFactory.eINSTANCE
				.createParameterDefinition( );
		DataElementAttributes dataAttrs = DesignFactory.eINSTANCE
				.createDataElementAttributes( );
		dataAttrs.setName( "param1" ); //$NON-NLS-1$
		dataAttrs.setNullability( ElementNullability
				.get( ElementNullability.NOT_NULLABLE ) );
		dataAttrs.setPosition( 1 );
		dataAttrs.setNativeDataTypeCode( 2 );
		paramDefn.setInOutMode( ParameterMode.get( ParameterMode.IN_OUT ) );
		DataElementUIHints uiHints = DesignFactory.eINSTANCE
				.createDataElementUIHints( );
		uiHints.setDescription( "updated param help text" ); //$NON-NLS-1$
		uiHints.setDisplayName( "updated param prompt text" ); //$NON-NLS-1$
		dataAttrs.setUiHints( uiHints );
		paramDefn.setAttributes( dataAttrs );

		InputParameterAttributes inParamAttrs = DesignFactory.eINSTANCE
				.createInputParameterAttributes( );
		InputElementAttributes inputElementAttrs = DesignFactory.eINSTANCE
				.createInputElementAttributes( );
		inputElementAttrs.setOptional( false );
		inputElementAttrs.setDefaultScalarValue( "default param value" ); //$NON-NLS-1$
		inputElementAttrs.setMasksValue( false );

		InputElementUIHints hints = DesignFactory.eINSTANCE
				.createInputElementUIHints( );
		hints.setAutoSuggestThreshold( 100 );
		hints.setPromptStyle( InputPromptControlStyle.SELECTABLE_LIST_LITERAL );
		inputElementAttrs.setUiHints( hints );

		inParamAttrs.setElementAttributes( inputElementAttrs );

		InputParameterUIHints parameterUIHints = DesignFactory.eINSTANCE
				.createInputParameterUIHints( );
		parameterUIHints.setGroupPromptDisplayName( "updated group" ); //$NON-NLS-1$
		inParamAttrs.setUiHints( parameterUIHints );

		paramDefn.setInputAttributes( inParamAttrs );

		params.getParameterDefinitions( ).add( paramDefn );

		// add another parameter definition, this have the same name so that we
		// can find a structure with same native name
		paramDefn = DesignFactory.eINSTANCE.createParameterDefinition( );
		dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes( );
		dataAttrs.setName( "param2" ); //$NON-NLS-1$
		paramDefn.setAttributes( dataAttrs );
		params.getParameterDefinitions( ).add( paramDefn );

		// add the third one without linked parameter
		paramDefn = DesignFactory.eINSTANCE.createParameterDefinition( );
		dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes( );
		dataAttrs.setName( "param3" ); //$NON-NLS-1$
		dataAttrs.setPosition( 3 );
		paramDefn.setAttributes( dataAttrs );
		params.getParameterDefinitions( ).add( paramDefn );

		// add the 4th that has no corresponding parameter structure in the data
		// set handle
		paramDefn = DesignFactory.eINSTANCE.createParameterDefinition( );
		dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes( );
		dataAttrs.setName( "param4" ); //$NON-NLS-1$
		dataAttrs.setNativeDataTypeCode( 4 );
		dataAttrs.setPosition( 4 );
		paramDefn.setAttributes( dataAttrs );
		params.getParameterDefinitions( ).add( paramDefn );

		setDesign.setParameters( params );

		// create some result set columns
		ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE
				.createResultSetDefinition( );
		ResultSetColumns setColumns = DesignFactory.eINSTANCE
				.createResultSetColumns( );
		ColumnDefinition columnDefn = DesignFactory.eINSTANCE
				.createColumnDefinition( );
		dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes( );
		dataAttrs.setName( "column1" ); //$NON-NLS-1$
		dataAttrs.setPosition( 2 );
		dataAttrs.setNativeDataTypeCode( 3 );
		columnDefn.setAttributes( dataAttrs );
		setColumns.getResultColumnDefinitions( ).add( columnDefn );
		resultSetDefn.setResultSetColumns( setColumns );
		setDesign.setPrimaryResultSet( resultSetDefn );
		setDesign.setPrimaryResultSetName( "resultset1" ); //$NON-NLS-1$

		setDesign.setQueryText( "new query text" ); //$NON-NLS-1$

		// create the corresponding data source design
		setDesign.setDataSourceDesign( createDataSourceDesign( ) );

		return setDesign;
	}

	/**
	 * Creates a new <code>DataSourceDesign</code>.
	 * 
	 * @return an object of <code>DataSourceDesign</code>.
	 */

	private DataSourceDesign createDataSourceDesign( )
	{
		DataSourceDesign sourceDesign = DesignFactory.eINSTANCE
				.createDataSourceDesign( );
		sourceDesign.setName( "myDataSource1" ); //$NON-NLS-1$
		sourceDesign.setDisplayName( "data source display name" ); //$NON-NLS-1$
		sourceDesign
				.setOdaExtensionId( OdaDataSetAdapterTest.DATA_SOURCE_EXTENSIONID );

		Properties props = DesignFactory.eINSTANCE.createProperties( );
		props.setProperty( "odaDriverClass", "new drivers" ); //$NON-NLS-1$//$NON-NLS-2$
		props.setProperty( "odaURL", "jdbc:sqlserver://localhost" ); //$NON-NLS-1$//$NON-NLS-2$
		props.setProperty( "odaUser", "new user" ); //$NON-NLS-1$ //$NON-NLS-2$
		sourceDesign.setPublicProperties( props );

		props = DesignFactory.eINSTANCE.createProperties( );
		props.setProperty( "odaDriverClass", "new drivers" ); //$NON-NLS-1$ //$NON-NLS-2$
		props.setProperty( "odaPassword", "new password" ); //$NON-NLS-1$ //$NON-NLS-2$
		sourceDesign.setPrivateProperties( props );

		return sourceDesign;
	}

	/**
	 * Tests the getAmbiguousParameters in ModelOdaAdapter.
	 * 
	 * @throws Exception
	 */
	public void testGetAmbiguousParameters( ) throws Exception
	{
		DataSetDesign setDesign = createDataSetDesign( );
		openDesign( "AdvancedDataSetAdapterTest.xml" ); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		IAmbiguousOption option = new ModelOdaAdapter( ).getAmbiguousOption(
				setDesign, setHandle );
		List<IAmbiguousParameterNode> ambiguousParameters = option
				.getAmbiguousParameters( );
		assertEquals( 2, ambiguousParameters.size( ) );

		IAmbiguousParameterNode node = ambiguousParameters.get( 0 );
		assertEquals(
				"dataSetParam1", node.getOdaDataSetParameterHandle( ).getName( ) ); //$NON-NLS-1$
		List<IAmbiguousAttribute> attributes = node.getAmbiguousAttributes( );
		assertNotNull( attributes );
		IAmbiguousAttribute attr = attributes.get( 0 );
		assertEquals( OdaDataSetParameter.NATIVE_NAME_MEMBER, attr
				.getAttributeName( ) );
		assertNull( attr.getPreviousValue( ) );
		assertEquals( "param1", attr.getRevisedValue( ) ); //$NON-NLS-1$
		attr = attributes.get( 1 );
		assertEquals( OdaDataSetParameter.ALLOW_NULL_MEMBER, attr
				.getAttributeName( ) );
		assertEquals( Boolean.TRUE, attr.getPreviousValue( ) );
		assertEquals( Boolean.FALSE, attr.getRevisedValue( ) );
		attr = attributes.get( 2 );
		assertEquals( OdaDataSetParameter.IS_INPUT_MEMBER, attr
				.getAttributeName( ) );
		assertEquals( Boolean.FALSE, attr.getPreviousValue( ) );
		assertEquals( Boolean.TRUE, attr.getRevisedValue( ) );
		attr = attributes.get( 3 );
		assertEquals( OdaDataSetParameter.IS_OUTPUT_MEMBER, attr
				.getAttributeName( ) );
		assertEquals( Boolean.FALSE, attr.getPreviousValue( ) );
		assertEquals( Boolean.TRUE, attr.getRevisedValue( ) );
		attr = attributes.get( 4 );
		assertEquals( OdaDataSetParameter.IS_OPTIONAL_MEMBER, attr
				.getAttributeName( ) );
		assertEquals( Boolean.TRUE, attr.getPreviousValue( ) );
		assertEquals( Boolean.FALSE, attr.getRevisedValue( ) );
		attr = attributes.get( 5 );
		assertEquals( ScalarParameterHandle.IS_REQUIRED_PROP, attr
				.getAttributeName( ) );
		assertEquals( Boolean.FALSE, attr.getPreviousValue( ) );
		assertEquals( Boolean.TRUE, attr.getRevisedValue( ) );
		assertTrue( attr.isLinkedReportParameterAttribute( ) );
		attr = attributes.get( 6 );
		assertEquals( ScalarParameterHandle.PROMPT_TEXT_PROP, attr
				.getAttributeName( ) );
		assertEquals( "param1 prompt text", attr.getPreviousValue( ) ); //$NON-NLS-1$
		assertEquals( "updated param prompt text", attr.getRevisedValue( ) ); //$NON-NLS-1$
		assertTrue( attr.isLinkedReportParameterAttribute( ) );
		attr = attributes.get( 7 );
		assertEquals( ScalarParameterHandle.HELP_TEXT_PROP, attr
				.getAttributeName( ) );
		assertEquals( "scalar para help", attr.getPreviousValue( ) ); //$NON-NLS-1$
		assertEquals( "updated param help text", attr.getRevisedValue( ) ); //$NON-NLS-1$
		assertTrue( attr.isLinkedReportParameterAttribute( ) );
		attr = attributes.get( 8 );
		assertEquals( ScalarParameterHandle.CONCEAL_VALUE_PROP, attr
				.getAttributeName( ) );
		assertEquals( Boolean.TRUE, attr.getPreviousValue( ) );
		assertEquals( Boolean.FALSE, attr.getRevisedValue( ) );
		assertTrue( attr.isLinkedReportParameterAttribute( ) );
		attr = attributes.get( 9 );
		assertEquals( ScalarParameterHandle.AUTO_SUGGEST_THRESHOLD_PROP, attr
				.getAttributeName( ) );
		assertEquals( 112, attr.getPreviousValue( ) );
		assertEquals( 100, attr.getRevisedValue( ) );
		assertTrue( attr.isLinkedReportParameterAttribute( ) );
		attr = attributes.get( 10 );
		assertEquals( ScalarParameterHandle.CONTROL_TYPE_PROP, attr
				.getAttributeName( ) );
		assertEquals( DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX, attr
				.getPreviousValue( ) );
		assertEquals( DesignChoiceConstants.PARAM_CONTROL_LIST_BOX, attr
				.getRevisedValue( ) );
		assertTrue( attr.isLinkedReportParameterAttribute( ) );
		attr = attributes.get( 11 );
		assertEquals( ParameterGroupHandle.DISPLAY_NAME_PROP, attr
				.getAttributeName( ) );
		assertEquals( "Group 1", attr.getPreviousValue( ) ); //$NON-NLS-1$
		assertEquals( "updated group", attr.getRevisedValue( ) ); //$NON-NLS-1$
		assertTrue( attr.isLinkedReportParameterAttribute( ) );

		// test the 'defaultvalue': only the structure has no linked parameter,
		// we will add it to the list
		node = ambiguousParameters.get( 1 );
		assertEquals(
				"dataSetParam3", node.getOdaDataSetParameterHandle( ).getName( ) ); //$NON-NLS-1$
		attributes = node.getAmbiguousAttributes( );
		assertEquals( 2, attributes.size( ) );
		attr = attributes.get( 0 );
		assertEquals( OdaDataSetParameter.NATIVE_NAME_MEMBER, attr
				.getAttributeName( ) );
		attr = attributes.get( 1 );
		assertEquals( OdaDataSetParameter.DEFAULT_VALUE_MEMBER, attr
				.getAttributeName( ) );
		assertNull( attr.getRevisedValue( ) );
		Expression oldValue = (Expression) attr.getPreviousValue( );
		assertEquals(
				"data set param default value 3", oldValue.getStringExpression( ) ); //$NON-NLS-1$
	}

	public void testUpdateDataSetHandle( ) throws Exception
	{
		DataSetDesign setDesign = createDataSetDesign( );
		openDesign( "AdvancedDataSetAdapterTest.xml" ); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$
		List<OdaDataSetParameter> parameterList = new ArrayList<OdaDataSetParameter>( );
		List setDefinedParams = setHandle
				.getListProperty( OdaDataSetHandle.PARAMETERS_PROP );
		parameterList.add( (OdaDataSetParameter) setDefinedParams.get( 0 ) );

		new ModelOdaAdapter( ).updateDataSetHandle( setDesign, setHandle,
				parameterList, null, true );
		save( );

		assertTrue( compareTextFile( "AdvancedDataSetAdapterTest_golden.xml" ) ); //$NON-NLS-1$
	}
}
