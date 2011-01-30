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

import org.eclipse.birt.report.model.adapter.oda.ModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.birt.report.model.adapter.oda.model.util.SchemaConversionUtil;
import org.eclipse.birt.report.model.adapter.oda.util.BaseTestCase;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.DynamicValuesQuery;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputPromptControlStyle;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueChoices;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueDefinition;
import org.eclipse.datatools.connectivity.oda.design.util.DesignUtil;

/**
 * Test cases to convert Oda data set parameters and ROM data set parameter and
 * linked report parameters.
 * 
 */

public class ReportParameterAdapterTest extends BaseTestCase
{

	/**
	 * Converts ROM data set parameters with report parameters to ODA data set
	 * parameters.
	 * 
	 * @throws Exception
	 */

	public void testToODADataSetParamWithReportParam( ) throws Exception
	{
		// create oda set handle.

		openDesign( "OdaDataSetConvertReportParamTest.xml" ); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		DataSetDesign setDesign = new ModelOdaAdapter( )
				.createDataSetDesign( setHandle );

		DesignValues values = ModelFactory.eINSTANCE.createDesignValues( );
		values.setDataSetParameters( SchemaConversionUtil
				.convertToAdapterParameters( setDesign.getParameters( ) ) );

		saveDesignValuesToFile( values );

		assertTrue( compareTextFile( "DataSetParamWithReportParam_golden.xml" ) ); //$NON-NLS-1$

	}

	/**
	 * Converts ODA data set parameters to ROM data set parameters.
	 * 
	 * <ul>
	 * <li>no the latest design session response, update ROM values. Default
	 * value is not copied to data set parameter since it has linked report
	 * parameter.
	 * <li>if the latest design session response changed, update ROM values.
	 * <li>if the latest design session response didn't change and ROM values
	 * changed, don't update ROM values.
	 * <li>update native type and default value on data set parameter
	 * <li>update the data set handle in the reportParameter.dataSet value
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testToROMDataSetParamWithReportParam( ) throws Exception
	{
		// create oda set handle.

		openDesign( "OdaDataSetConvertReportParamTest.xml" ); //$NON-NLS-1$
		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		// get the latest data set design.

		DataSetDesign setDesign = new ModelOdaAdapter( )
				.createDataSetDesign( setHandle );

		// oda data set design changed, update ROM values. still keep report
		// parameter link.

		DataSetParameters params = setDesign.getParameters( );
		ParameterDefinition param = (ParameterDefinition) params
				.getParameterDefinitions( ).get( 0 );

		updateParameterDefinition1( param );

		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		save( );
		compareTextFile( "OdaDataSetConvertReportParamTest_golden.xml" ); //$NON-NLS-1$

		openDesign( "OdaDataSetConvertReportParamTest_1.xml" ); //$NON-NLS-1$
		setHandle = (OdaDataSetHandle) designHandle.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		// get the latest data set design.

		setDesign = new ModelOdaAdapter( ).createDataSetDesign( setHandle );

		DesignUtil.validateObject( setDesign );

		// oda data set design changed, update ROM values. still keep report
		// parameter link.

		params = setDesign.getParameters( );
		param = (ParameterDefinition) params.getParameterDefinitions( ).get( 0 );

		// oda data set design changed, update ROM values. still keep report
		// parameter link.

		updateParameterDefinition1( param );
		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		save( );

		compareTextFile( "OdaDataSetConvertReportParamTest_1_golden.xml" ); //$NON-NLS-1$

		openDesign( "OdaDataSetConvertReportParamTest_1.xml" ); //$NON-NLS-1$
		setHandle = (OdaDataSetHandle) designHandle.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		// get the latest data set design.

		setDesign = new ModelOdaAdapter( ).createDataSetDesign( setHandle );

		// oda data set design changed, update ROM values. still keep report
		// parameter link.

		params = setDesign.getParameters( );
		param = params.getParameterDefinitions( ).get( 0 );

		// change the direction of parameter, do not keep report parameter link.

		updateParameterDefinition2( param );
		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		save( );
		compareTextFile( "OdaDataSetConvertReportParamTest_2_golden.xml" ); //$NON-NLS-1$

		// the oda data set design is not changed. ROM values are changed.
		// Should keep rom values.

		openDesign( "OdaDataSetConvertReportParamTest_1.xml" ); //$NON-NLS-1$
		setHandle = (OdaDataSetHandle) designHandle.findDataSet( "myDataSet1" ); //$NON-NLS-1$

		// get the latest data set design.

		setDesign = new ModelOdaAdapter( ).createDataSetDesign( setHandle );

		Iterator paramHandles = setHandle.parametersIterator( );
		OdaDataSetParameterHandle paramHandle = (OdaDataSetParameterHandle) paramHandles
				.next( );
		paramHandle.setIsOutput( true );
		paramHandle.setIsInput( false );
		paramHandle.setDefaultValue( "not updated default value" ); //$NON-NLS-1$

		String reportParamName = paramHandle.getParamName( );
		ScalarParameterHandle reportParam = (ScalarParameterHandle) designHandle
				.findParameter( reportParamName );
		reportParam.setPromptText( "not updated prompt text" ); //$NON-NLS-1$
		reportParam.setDefaultValue( "not updated default value" ); //$NON-NLS-1$

		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		save( );
		compareTextFile( "OdaDataSetConvertReportParamTest_3_golden.xml" ); //$NON-NLS-1$

		// when convert data set design to data set handle, report parameters's
		// data set should also be updated.

		openDesign( "OdaDataSetConvertReportParamTest_2.xml" ); //$NON-NLS-1$
		setHandle = (OdaDataSetHandle) designHandle.findDataSet( "sqlSet1" ); //$NON-NLS-1$

		// get the latest data set design.

		setDesign = new ModelOdaAdapter( ).createDataSetDesign( setHandle );

		params = setDesign.getParameters( );
		param = (ParameterDefinition) params.getParameterDefinitions( ).get( 0 );

		// change the direction of parameter, do not keep report parameter link.

		updateParameterDefinition3( param );

		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		checkUpdateParameterDefinition3( param );

		// when convert data set design to data set handle, report parameters's
		// data set should be created if no such data set.

		openDesign( "OdaDataSetConvertReportParamTest_2.xml" ); //$NON-NLS-1$
		setHandle = (OdaDataSetHandle) designHandle.findDataSet( "sqlSet1" ); //$NON-NLS-1$

		// get the latest data set design.

		setDesign = new ModelOdaAdapter( ).createDataSetDesign( setHandle );
		params = setDesign.getParameters( );
		param = (ParameterDefinition) params.getParameterDefinitions( ).get( 0 );

		// change the direction of parameter, do not keep report parameter link.

		updateParameterDefinition3( param );

		// delete the current "Data Set" to test whether a new data set is
		// created.

		DataSetHandle dataSetToDelete = designHandle.findDataSet( "Data Set" ); //$NON-NLS-1$
		dataSetToDelete.drop( );

		dataSetToDelete = designHandle.findDataSet( "Data Set" ); //$NON-NLS-1$
		assertNull( dataSetToDelete );

		new ModelOdaAdapter( )
				.updateDataSetHandle( setDesign, setHandle, false );

		checkUpdateParameterDefinition3( param );

		dataSetToDelete = designHandle.findDataSet( "Data Set" ); //$NON-NLS-1$
		assertNull( dataSetToDelete );

	}

	/**
	 * Updates a oda parameter definition. Keep the direction.
	 * 
	 * @param param
	 */

	private void updateParameterDefinition1( ParameterDefinition param )
	{
		DataElementAttributes dataAttrs = param.getAttributes( );
		dataAttrs.setNullability( ElementNullability
				.get( ElementNullability.NOT_NULLABLE ) );

		DataElementUIHints dataUIHints = DesignFactory.eINSTANCE
				.createDataElementUIHints( );
		dataAttrs.setUiHints( dataUIHints );

		dataUIHints.setDisplayName( "new prompt text for report param 1" ); //$NON-NLS-1$
		dataUIHints.setDisplayNameKey( "newPromptTextKeyParam1" ); //$NON-NLS-1$
		dataUIHints.setDescription( "new help text for report param 1" ); //$NON-NLS-1$
		dataUIHints.setDescriptionKey( "newHelpTextKeyParam1" ); //$NON-NLS-1$

		InputParameterAttributes paramAttrs = param.getInputAttributes( );
		InputElementAttributes elementAttrs = paramAttrs.getElementAttributes( );

		elementAttrs
				.setDefaultScalarValue( "new default value for report param 1" ); //$NON-NLS-1$
		elementAttrs.setOptional( true );
		elementAttrs.setMasksValue( false );

		ScalarValueChoices chocies = DesignFactory.eINSTANCE
				.createScalarValueChoices( );

		ScalarValueDefinition choice = DesignFactory.eINSTANCE
				.createScalarValueDefinition( );
		choice.setDisplayName( "new choice display name 1" ); //$NON-NLS-1$
		choice.setDisplayNameKey( "newChoiceDisplayName1" ); //$NON-NLS-1$
		choice.setValue( "new choice value 1" ); //$NON-NLS-1$

		chocies.getScalarValues( ).add( choice );

		elementAttrs.setStaticValueChoices( chocies );

		DynamicValuesQuery dynamicValue = DesignFactory.eINSTANCE
				.createDynamicValuesQuery( );
		dynamicValue.setValueColumn( "new value column 1" ); //$NON-NLS-1$
		dynamicValue.setDisplayNameColumn( "new lable column 1" ); //$NON-NLS-1$
		dynamicValue.setDataSetDesign( OdaDataSetAdapterTest
				.createDataSetDesign( ) );

		elementAttrs.setDynamicValueChoices( dynamicValue );

		InputElementUIHints elementUIHints = DesignFactory.eINSTANCE
				.createInputElementUIHints( );
		elementUIHints.setPromptStyle( InputPromptControlStyle
				.get( InputPromptControlStyle.RADIO_BUTTON ) );
		elementUIHints.setAutoSuggestThreshold( 111 );
		elementAttrs.setUiHints( elementUIHints );

	}

	/**
	 * Updates a oda parameter definition. Change the direction from input to
	 * output.
	 * 
	 * @param param
	 */

	private void updateParameterDefinition2( ParameterDefinition param )
	{
		param.setInOutMode( ParameterMode.get( ParameterMode.OUT ) );
	}

	/**
	 * Updates a oda parameter definition. Change the direction from input to
	 * output.
	 * 
	 * @param param
	 */

	private void updateParameterDefinition3( ParameterDefinition param )
	{
		DynamicValuesQuery dynamicValue = param.getInputAttributes( )
				.getElementAttributes( ).getDynamicValueChoices( );
		if ( dynamicValue == null )
		{
			dynamicValue = DesignFactory.eINSTANCE.createDynamicValuesQuery( );
			dynamicValue.setValueColumn( "new value column 1" ); //$NON-NLS-1$
			dynamicValue.setDisplayNameColumn( "new lable column 1" ); //$NON-NLS-1$
			dynamicValue.setDataSetDesign( OdaDataSetAdapterTest
					.createDataSetDesign( ) );
			param.getInputAttributes( ).getElementAttributes( )
					.setDynamicValueChoices( dynamicValue );
		}
		DataSetDesign setDesign = dynamicValue.getDataSetDesign( );
		setDesign.setDisplayName( "new display name" ); //$NON-NLS-1$

		setDesign.setQueryText( "select * from CLASSICMODELS.CUSTOMERS" ); //$NON-NLS-1$
	}

	/**
	 * Checks whether update parameter definition3 method works OK.
	 * 
	 * @param param
	 */

	private void checkUpdateParameterDefinition3( ParameterDefinition param )
	{
		DynamicValuesQuery dynamicValue = param.getInputAttributes( )
				.getElementAttributes( ).getDynamicValueChoices( );

		DataSetDesign setDesign = dynamicValue.getDataSetDesign( );
		assertEquals( "myDataSet1", setDesign.getName( ) ); //$NON-NLS-1$
		assertEquals( "new display name", setDesign.getDisplayName( ) ); //$NON-NLS-1$

		assertEquals( "select * from CLASSICMODELS.CUSTOMERS", setDesign //$NON-NLS-1$
				.getQueryText( ) );

	}
}
