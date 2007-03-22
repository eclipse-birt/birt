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

package org.eclipse.birt.report.model.adapter.oda.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.adapter.oda.ODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.util.IdentifierUtility;
import org.eclipse.birt.report.model.adapter.oda.util.ParameterValueUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * An adapter class that converts between ROM DataSetParameterHandle and ODA ODA
 * ParameterDefinition.
 * 
 * @see DataSetParameterHandle
 * @see ParameterDefinition
 */

class DataSetParameterAdapter
{

	/**
	 * The constant that indicates the value is a BIRT defined java script
	 * expression.
	 * 
	 */

	static final String BIRT_JS_EXPR = "JS_EXPR"; //$NON-NLS-1$

	/**
	 * Creates an ParameterDefinition with the given ROM data set parameter
	 * definition.
	 * 
	 * @param paramHandle
	 *            the ROM data set parameter definition.
	 * @return the created ParameterDefinition
	 */

	private ParameterDefinition newParameterDefinition(
			OdaDataSetParameterHandle paramHandle,
			ParameterDefinition lastOdaParamDefn, DataSetDesign dataSetDesign )
	{
		if ( paramHandle == null )
			return null;

		String rptParamName = paramHandle.getParamName( );

		ParameterDefinition paramDefn = newParameterDefinitionFromDataSetParam(
				paramHandle, lastOdaParamDefn );

		if ( StringUtil.isBlank( rptParamName ) )
		{
			return paramDefn;
		}

		ModuleHandle module = paramHandle.getElementHandle( ).getModuleHandle( );
		ScalarParameterHandle reportParam = (ScalarParameterHandle) module
				.findParameter( rptParamName );

		if ( reportParam != null )
			paramDefn = new ReportParameterAdapter( )
					.updateParameterDefinitionFromReportParam( paramDefn,
							reportParam, dataSetDesign );

		return paramDefn;
	}

	/**
	 * Creates an ParameterDefinition with the given ROM data set parameter
	 * definition.
	 * 
	 * @param paramDefn
	 *            the ROM data set parameter definition.
	 * @return the created ParameterDefinition
	 */

	private ParameterDefinition newParameterDefinitionFromDataSetParam(
			OdaDataSetParameterHandle paramHandle,
			ParameterDefinition lastOdaParamDefn )
	{

		ParameterDefinition odaParamDefn = null;

		if ( lastOdaParamDefn == null )
			odaParamDefn = ODADesignFactory.getFactory( )
					.createParameterDefinition( );
		else
			odaParamDefn = (ParameterDefinition) EcoreUtil
					.copy( lastOdaParamDefn );

		odaParamDefn.setInOutMode( newParameterMode( paramHandle.isInput( ),
				paramHandle.isOutput( ) ) );
		odaParamDefn.setAttributes( newDataElementAttrs( paramHandle,
				odaParamDefn.getAttributes( ) ) );

		InputParameterAttributes inputAttrs = odaParamDefn.getInputAttributes( );
		if ( inputAttrs == null )
			inputAttrs = ODADesignFactory.getFactory( )
					.createInputParameterAttributes( );

		inputAttrs.setElementAttributes( newInputElementAttrs( paramHandle,
				inputAttrs.getElementAttributes( ) ) );
		odaParamDefn.setInputAttributes( inputAttrs );

		return odaParamDefn;
	}

	/**
	 * Creates a ODA ParameterMode with the given parameter input/output flags.
	 * 
	 * @param isInput
	 *            the parameter is inputable.
	 * @param isOutput
	 *            the parameter is outputable
	 * @return the created <code>ParameterMode</code>.
	 */

	private static ParameterMode newParameterMode( boolean isInput,
			boolean isOutput )
	{
		int mode = ParameterMode.IN;
		if ( isOutput && isInput )
			mode = ParameterMode.IN_OUT;
		else if ( isOutput )
			mode = ParameterMode.OUT;
		else if ( isInput )
			mode = ParameterMode.IN;

		return ParameterMode.get( mode );
	}

	/**
	 * Creates a ODA DataElementAttributes with the given ROM data set parameter
	 * definition.
	 * 
	 * @param paramDefn
	 *            the ROM data set parameter definition.
	 * 
	 * @return the created <code>DataElementAttributes</code>.
	 */

	private DataElementAttributes newDataElementAttrs(
			OdaDataSetParameterHandle paramDefn,
			DataElementAttributes lastDataAttrs )
	{
		DataElementAttributes dataAttrs = lastDataAttrs;
		if ( dataAttrs == null )
			dataAttrs = ODADesignFactory.getFactory( )
					.createDataElementAttributes( );

		dataAttrs
				.setNullability( newElementNullability( paramDefn.allowNull( ) ) );

		// control the name outside. not here.

		Integer position = paramDefn.getPosition( );
		if ( position != null )
			dataAttrs.setPosition( position.intValue( ) );

		Integer nativeDataType = paramDefn.getNativeDataType( );
		if ( nativeDataType != null )
			dataAttrs.setNativeDataTypeCode( nativeDataType.intValue( ) );

		dataAttrs.setName( paramDefn.getNativeName( ) );

		return dataAttrs;
	}

	/**
	 * Creates a ODA ElementNullability with the flag that indicates whether the
	 * parameter value can be <code>null</code>.
	 * 
	 * @param isNullable
	 *            <code>true</code> if the parameter value can be
	 *            <code>null</code>. Otherwise, <code>false</code>.
	 * @return the created <code>ElementNullabilityterMode</code>.
	 */

	static ElementNullability newElementNullability( boolean isNullable )
	{
		int nullAbility = ElementNullability.UNKNOWN;
		if ( isNullable )
			nullAbility = ElementNullability.NULLABLE;

		return ElementNullability.get( nullAbility );

	}

	/**
	 * Updates the parameter direction with the given parameter mode.
	 * <p>
	 * First check if the same parameter (w/ matching native name and position)
	 * already exists in the model dataSetHandle. Compare the ODA Parameter Mode
	 * (direction) in its previous ODA session response with the current
	 * response. If they are the same, preserve the current direction value in
	 * ROM Data Set Parameter. If different, update the Data Set Parameter
	 * direction to that in the current ODA session response.
	 * <p>
	 * In addition, if a Data Set Parameter direction was one of input modes,
	 * but is now updated to Output only, any link to a Report Parameter is no
	 * longer valid. When such update occurs, its link to a report parameter, if
	 * exists, should be automatically removed.
	 * 
	 * @param paramMode
	 *            the latest parameter mode
	 * @param cachedParamMode
	 *            the cached parameter mode
	 * @param setParam
	 *            the data set parameter to set mode
	 */

	private void updateROMDataSetParameterDirection( ParameterMode paramMode,
			ParameterMode cachedParamMode, OdaDataSetParameter setParam )
	{
		if ( cachedParamMode == null )
		{
			updateROMParameterMode( setParam, paramMode );
			return;
		}

		int newDirerction = paramMode.getValue( );
		int oldDirection = cachedParamMode.getValue( );
		if ( newDirerction != oldDirection )
			updateROMParameterMode( setParam, paramMode );

		if ( ( oldDirection == ParameterMode.IN || oldDirection == ParameterMode.IN_OUT )
				&& newDirerction == ParameterMode.OUT )
			setParam.setParamName( null );
	}

	/**
	 * Update data set parameters value from latest and last data element
	 * attributes.
	 * 
	 * @param dataAttrs
	 *            the latest data element attributes
	 * @param cachedDataAttrs
	 *            the cached data element attributes
	 * @param setParam
	 *            the data set parameter
	 * @param dataSourceId
	 *            the data source id
	 * @param dataSetId
	 *            the data set id
	 * @param params
	 *            the iterator of data set parameters
	 */

	private void updateROMDataSetParameterFromDataAttrs(
			DataElementAttributes dataAttrs,
			DataElementAttributes cachedDataAttrs,
			OdaDataSetParameter setParam, String dataSourceId,
			String dataSetId, Iterator params )
	{
		if ( dataAttrs == null )
		{
			return;
		}

		updateROMNullability( setParam, dataAttrs.getNullability( ),
				cachedDataAttrs == null ? null : cachedDataAttrs
						.getNullability( ) );

		Object oldValue = cachedDataAttrs == null ? null : cachedDataAttrs
				.getName( );
		Object newValue = dataAttrs.getName( );
		if ( oldValue == null || !oldValue.equals( newValue ) )
		{
			setParam.setNativeName( (String) newValue );
		}

		oldValue = cachedDataAttrs == null ? null : new Integer(
				cachedDataAttrs.getPosition( ) );
		newValue = new Integer( dataAttrs.getPosition( ) );
		if ( oldValue == null || !oldValue.equals( newValue ) )
		{
			setParam.setPosition( (Integer) newValue );
		}

		oldValue = cachedDataAttrs == null ? null : new Integer(
				cachedDataAttrs.getNativeDataTypeCode( ) );
		newValue = new Integer( dataAttrs.getNativeDataTypeCode( ) );
		if ( oldValue == null || !oldValue.equals( newValue )
				|| setParam.getNativeDataType( ) == null )
		{
			setParam.setNativeDataType( (Integer) newValue );
		}

		// boolean is not supported in data set parameter yet.

		String dataType = getROMDataType( dataSourceId, dataSetId, setParam,
				params );
		if ( dataType == null
				|| !DesignChoiceConstants.PARAM_TYPE_BOOLEAN
						.equalsIgnoreCase( dataType ) )
			setParam.setParameterDataType( dataType );

	}

	/**
	 * Update data set parameter name from latest data element attributes.
	 * 
	 * @param dataAttrs
	 *            the latest data element attributes
	 * @param setParam
	 *            the data set parameter
	 * @param params
	 *            the iterator of data set parameters
	 * @param retList
	 *            list contain data set parameter
	 */

	private void updateROMDataSetParameterName(
			DataElementAttributes dataAttrs, OdaDataSetParameter setParam,
			DataSetHandle handle, List retList )
	{
		if ( dataAttrs == null )
			return;

		String nativeName = dataAttrs.getName( );
		Integer position = new Integer( dataAttrs.getPosition( ) );

		// make sure the OdaDataSetParameter must have a name. This is a
		// requirement in ROM.

		String name = setParam.getName( );
		if ( StringUtil.isBlank( name ) )
		{
			setParam.setName( IdentifierUtility.getParamUniqueName( handle
					.parametersIterator( ), retList, position.intValue( ) ) );
		}

		setParam.setNativeName( nativeName );
	}

	/**
	 * Update data set parameter values from latest input parameter attributes.
	 * 
	 * @param paramAttrs
	 *            the latest parameter attributes
	 * @param cachedParamAttrs
	 *            the cached parameter attributes
	 * @param setParam
	 *            the oda data set parameter
	 */

	private void updateROMDataSetParameterFromInputParamAttrs(
			InputParameterAttributes paramAttrs,
			InputParameterAttributes cachedParamAttrs,
			OdaDataSetParameter setParam )
	{
		if ( paramAttrs == null )
		{
			return;
		}

		InputElementAttributes inputElementAttrs = paramAttrs
				.getElementAttributes( );
		if ( inputElementAttrs == null )
		{
			return;
		}

		updateROMDataSetParameterFromInputElementAttrs( inputElementAttrs,
				cachedParamAttrs == null ? null : cachedParamAttrs
						.getElementAttributes( ), setParam );
	}

	/**
	 * Update data set parameter values from latest input element attributes.
	 * 
	 * @param paramAttrs
	 *            the latest element attributes
	 * @param cachedParamAttrs
	 *            the cached element attributes
	 * @param setParam
	 *            the oda data set parameter
	 */

	private void updateROMDataSetParameterFromInputElementAttrs(
			InputElementAttributes elementAttrs,
			InputElementAttributes cachedElementAttrs,
			OdaDataSetParameter setParam )
	{
		if ( elementAttrs == null )
			return;

		Object oldValue = cachedElementAttrs == null
				? null
				: cachedElementAttrs.getDefaultScalarValue( );
		Object newValue = elementAttrs.getDefaultScalarValue( );

		boolean withLinkedParameter = !StringUtil.isBlank( setParam
				.getParamName( ) );
		if ( ( oldValue == null || !oldValue.equals( newValue ) )
				&& !withLinkedParameter )
			setROMDefaultValue( setParam, (String) newValue );

		oldValue = cachedElementAttrs == null ? null : Boolean
				.valueOf( cachedElementAttrs.isOptional( ) );
		newValue = Boolean.valueOf( elementAttrs.isOptional( ) );
		if ( oldValue == null || !oldValue.equals( newValue ) )
			setParam.setIsOptional( ( (Boolean) newValue ).booleanValue( ) );
	}

	/**
	 * Updates values related to a linked paraemter in the oda data set
	 * parameters.
	 * 
	 * @param odaParamDefn
	 *            the latest ODA parameter definition
	 * @param cachedParamDefn
	 *            the last(cached) ODA parameter definition
	 * @param setHandle
	 *            the data set handle
	 * @param dataType
	 *            the updated ROM data type for the linked parameter
	 */

	private void updateReportParameter( ParameterDefinition odaParamDefn,
			ParameterDefinition cachedParamDefn, OdaDataSetHandle setHandle,
			String dataType ) throws SemanticException
	{
		DataElementAttributes dataAttrs = odaParamDefn.getAttributes( );

		if ( dataAttrs == null )
		{
			return;
		}

		Iterator params = setHandle.parametersIterator( );
		ModuleHandle module = setHandle.getModuleHandle( );

		OdaDataSetParameterHandle paramDefn = findDataSetParameterByName(
				dataAttrs.getName( ), new Integer( dataAttrs.getPosition( ) ),
				new Integer( dataAttrs.getNativeDataTypeCode( ) ), params );
		if ( paramDefn != null )
		{
			String reportParamName = paramDefn.getParamName( );
			if ( !StringUtil.isBlank( reportParamName ) )
			{
				ScalarParameterHandle paramHandle = (ScalarParameterHandle) module
						.findParameter( reportParamName );

				if ( paramHandle != null )
					new ReportParameterAdapter( ).updateLinkedReportParameter(
							paramHandle, odaParamDefn, cachedParamDefn,
							dataType, setHandle );
			}
		}
	}

	/**
	 * Returns the matched data set parameter by given name and position.
	 * 
	 * @param dataSetParamName
	 *            the data set parameter name
	 * @param position
	 *            the position
	 * @param params
	 *            the iterator of data set parameters
	 * @return the matched data set parameter
	 */

	private static OdaDataSetParameterHandle findDataSetParameterByName(
			String dataSetParamName, Integer position, Integer nativeDataType,
			Iterator params )
	{
		if ( position == null )
			return null;

		while ( params.hasNext( ) )
		{
			OdaDataSetParameterHandle param = (OdaDataSetParameterHandle) params
					.next( );

			Integer tmpNativeDataType = param.getNativeDataType( );
			String tmpNativeName = param.getNativeName( );

			// nativeName/name, position and nativeDataType should match. If the
			// native name is blank, match native data type and position

			if ( ( StringUtil.isBlank( tmpNativeName ) || ( tmpNativeName != null && tmpNativeName
					.equals( dataSetParamName ) ) )
					&& position.equals( param.getPosition( ) )
					&& ( tmpNativeDataType == null || tmpNativeDataType
							.equals( nativeDataType ) ) )
				return param;
		}

		return null;
	}

	/**
	 * Returns the matched data set parameter handle by given position. *
	 * 
	 * @param params
	 *            the iterator of data set parameters
	 * @param position
	 *            the position
	 * 
	 * @return the matched data set parameter handle
	 */

	private static OdaDataSetParameter findDataSetParameterByPosition(
			Iterator params, Integer position )
	{
		if ( position == null )
			return null;

		while ( params.hasNext( ) )
		{
			OdaDataSetParameter param = (OdaDataSetParameter) params.next( );
			Integer pos = param.getPosition( );
			if ( position.equals( pos ) )
				return param;
		}

		return null;
	}

	/**
	 * Returns the rom data type in string.
	 * 
	 * @param dataSourceId
	 *            the id of the data source
	 * @param dataSetId
	 *            the ide of the data set
	 * @param param
	 *            the rom data set parameter
	 * @param setHandleParams
	 *            params defined in data set handle
	 * @return the rom data type in string
	 */

	static String getROMDataType( String dataSourceId, String dataSetId,
			OdaDataSetParameter param, Iterator setHandleParams )
	{
		String name = param.getNativeName( );
		Integer position = param.getPosition( );
		Integer nativeType = param.getNativeDataType( );
		if ( nativeType == null )
			return param.getParameterDataType( );

		OdaDataSetParameterHandle tmpParam = findDataSetParameterByName( name,
				position, nativeType, setHandleParams );

		if ( tmpParam == null )
			return convertNativeTypeToROMDataType( dataSourceId, dataSetId,
					nativeType.intValue( ) );

		Integer tmpPosition = tmpParam.getPosition( );
		if ( tmpPosition == null )
			return convertNativeTypeToROMDataType( dataSourceId, dataSetId,
					nativeType.intValue( ) );

		if ( !tmpPosition.equals( param.getPosition( ) ) )
			return convertNativeTypeToROMDataType( dataSourceId, dataSetId,
					nativeType.intValue( ) );

		// Compare its original native type in session request with the latest
		// native type in response. If they are the same, preserve the existing
		// ROM data type.

		Integer tmpNativeCodeType = tmpParam.getNativeDataType( );
		if ( tmpNativeCodeType == null || tmpNativeCodeType.equals( nativeType ) )
			return tmpParam.getParameterDataType( );

		// If they are different, check if the latest native type in response is
		// compatible/convertible to the existing ROM data type. If compatible
		// (e.g. an unnknown native type is always compatible to any one of the
		// ROM data types), it should preserve the parameter's existing ROM data
		// type value. If not compatible, update its ROM data type to the value
		// that maps from the latest native data type.

		String oldDataType = tmpParam.getParameterDataType( );
		return convertNativeTypeToROMDataType( dataSourceId, dataSetId,
				nativeType.intValue( ), oldDataType );
	}

	/**
	 * Converts the ODA native data type code to rom data type.
	 * 
	 * @param dataSourceId
	 *            the id of the data source
	 * @param dataSetId
	 *            the ide of the data set
	 * @param nativeDataTypeCode
	 *            the oda data type code
	 * @return the rom data type in string
	 */

	private static String convertNativeTypeToROMDataType( String dataSourceId,
			String dataSetId, int nativeDataTypeCode )
	{
		return convertNativeTypeToROMDataType( dataSourceId, dataSetId,
				nativeDataTypeCode, null );
	}

	/**
	 * Converts the ODA native data type code to rom data type.
	 * 
	 * @param dataSourceId
	 *            the id of the data source
	 * @param dataSetId
	 *            the ide of the data set
	 * @param nativeDataTypeCode
	 *            the oda data type code
	 * @return the rom data type in string
	 */

	private static String convertNativeTypeToROMDataType( String dataSourceId,
			String dataSetId, int nativeDataTypeCode, String romDataType )
	{
		String romNewDataType = null;

		try
		{
			romNewDataType = NativeDataTypeUtil.getUpdatedDataType(
					dataSourceId, dataSetId, nativeDataTypeCode, romDataType,
					DesignChoiceConstants.CHOICE_PARAM_TYPE );
		}
		catch ( BirtException e )
		{

		}

		return romNewDataType;
	}

	/**
	 * Updates input/output mode for the given data set parameter definition.
	 * 
	 * @param romParamDefn
	 *            the data set parameter definition
	 * @param odaMode
	 *            the ODA parameter input/output mode
	 */

	private void updateROMParameterMode( DataSetParameter romParamDefn,
			ParameterMode odaMode )
	{
		if ( odaMode == null )
			return;

		switch ( odaMode.getValue( ) )
		{
			case ParameterMode.IN_OUT :
				romParamDefn.setIsInput( true );
				romParamDefn.setIsOutput( true );
				break;
			case ParameterMode.IN :
				romParamDefn.setIsInput( true );
				break;
			case ParameterMode.OUT :
				romParamDefn.setIsOutput( true );
				break;
		}
	}

	/**
	 * Updates allowNull property for the given data set parameter definition.
	 * 
	 * @param romParamDefn
	 *            the data set parameter definition.
	 * @param nullability
	 *            the ODA object indicates nullability.
	 */

	private void updateROMNullability( DataSetParameter romParamDefn,
			ElementNullability nullability, ElementNullability cachedNullability )
	{
		if ( nullability == null )
			return;

		if ( cachedNullability != null
				&& cachedNullability.getValue( ) == nullability.getValue( ) )
			return;

		switch ( nullability.getValue( ) )
		{
			case ElementNullability.NULLABLE :
				romParamDefn.setAllowNull( true );
				break;
			case ElementNullability.NOT_NULLABLE :
				romParamDefn.setAllowNull( false );
				break;
			case ElementNullability.UNKNOWN :
				break;
		}
	}

	/**
	 * Creates a ODA InputElementAttributes with the given ROM data set
	 * parameter definition.
	 * 
	 * @param paramDefn
	 *            the ROM data set parameter definition.
	 * 
	 * @return the created <code>DataElementAttributes</code>.
	 */

	private InputElementAttributes newInputElementAttrs(
			DataSetParameterHandle paramDefn,
			InputElementAttributes lastInputAttrs )
	{
		InputElementAttributes inputAttrs = lastInputAttrs;

		if ( inputAttrs == null )
			inputAttrs = ODADesignFactory.getFactory( )
					.createInputElementAttributes( );

		setDefaultScalarValue( inputAttrs, paramDefn.getParameterDataType( ),
				paramDefn.getDefaultValue( ) );

		inputAttrs.setOptional( paramDefn.isOptional( ) );

		return inputAttrs;
	}

	/**
	 * Creates a list containing <code>OdaDataSetParameter</code> with the
	 * given ODA data set parameter definition.
	 * 
	 * @param odaSetParams
	 *            ODA data set parameter definition
	 * @param setHandle
	 *            oda data set handle
	 * @param cachedDataSetParameters
	 *            cached dataset parameters.
	 * @param userDefinedList
	 *            a list contains user-defined parameters.Each item is
	 *            <code>OdaDataSetParameter</code>.
	 * @return a list containing <code>DataSetParameter</code>.
	 */

	List newROMSetParams( DataSetDesign setDesign, OdaDataSetHandle setHandle,
			DataSetParameters cachedDataSetParameters, List userDefinedList )
			throws SemanticException
	{
		if ( setDesign == null )
			return null;

		List dsParamProp = setHandle.getElement( ).getListProperty(
				setHandle.getModule( ), IDataSetModel.PARAMETERS_PROP );
		if ( dsParamProp == null && userDefinedList == null )
		{
			// use for creating rom parameter.
			return newRomSetParams( setDesign, setHandle,
					cachedDataSetParameters );
		}

		// create for updating.
		// Merge dataset design and user-defined parameter list. Now data
		// set design contains lastest driver-defined parameters

		List definedParamList = newRomSetParams( setDesign, setHandle,
				cachedDataSetParameters );

		// Merge userDefinedparamList and driverDefinedParamList

		return mergeUserDefindAndDriverDefinedParameter( definedParamList,
				userDefinedList );

	}

	/**
	 * Creates a list containing <code>OdaDataSetParameter</code> with the
	 * given ODA data set parameter definition.
	 * 
	 * @param odaSetParams
	 *            ODA data set parameter definition
	 * @param setHandle
	 *            oda data set handle
	 * @param cachedDataSetParameters
	 *            cached dataset parameters.
	 * @return a list containing <code>DataSetParameter</code>.
	 */

	private List newRomSetParams( DataSetDesign setDesign,
			OdaDataSetHandle setHandle,
			DataSetParameters cachedDataSetParameters )
			throws SemanticException
	{
		List retList = new ArrayList( );

		DataSetParameters odaSetParams = setDesign.getParameters( );
		if ( odaSetParams == null )
			return null;

		EList odaParams = odaSetParams.getParameterDefinitions( );
		if ( odaParams == null || odaParams.isEmpty( ) )
			return null;

		for ( int i = 0; i < odaParams.size( ); i++ )
		{
			ParameterDefinition odaParamDefn = (ParameterDefinition) odaParams
					.get( i );

			DataElementAttributes dataAttrs = odaParamDefn.getAttributes( );

			ParameterDefinition cachedParamDefn = null;
			OdaDataSetParameterHandle oldSetParam = null;
			if ( dataAttrs != null )
			{
				cachedParamDefn = findParameterDefinition(
						cachedDataSetParameters, dataAttrs.getName( ),
						new Integer( dataAttrs.getPosition( ) ) );
				oldSetParam = findDataSetParameterByName( dataAttrs.getName( ),
						new Integer( dataAttrs.getPosition( ) ), new Integer(
								dataAttrs.getNativeDataTypeCode( ) ), setHandle
								.parametersIterator( ) );

			}

			OdaDataSetParameter setParam = null;

			// to use old values if applies

			if ( oldSetParam == null )
			{
				// if the old column is not found, this means it can be removed.
				// Only update.

				setParam = StructureFactory.createOdaDataSetParameter( );
				cachedParamDefn = null;
			}

			else
				setParam = (OdaDataSetParameter) oldSetParam.getStructure( )
						.copy( );

			// if the direction is from input to output, should not update
			// report parameter any more. clear values in ParameterDefinition

			updateROMDataSetParameterDirection( odaParamDefn.getInOutMode( ),
					cachedParamDefn == null ? null : cachedParamDefn
							.getInOutMode( ), setParam );

			// control name value here.

			updateROMDataSetParameterName( dataAttrs, setParam, setHandle,
					retList );

			updateROMDataSetParameterFromDataAttrs( dataAttrs,
					cachedParamDefn == null ? null : cachedParamDefn
							.getAttributes( ), setParam, setDesign
							.getOdaExtensionDataSourceId( ), setDesign
							.getOdaExtensionDataSetId( ), setHandle
							.parametersIterator( ) );

			updateROMDataSetParameterFromInputParamAttrs( odaParamDefn
					.getInputAttributes( ), cachedParamDefn == null
					? null
					: cachedParamDefn.getInputAttributes( ), setParam );

			// if the parameter has no link to report parameter.

			if ( setParam.getParamName( ) == null )
			{
				retList.add( setParam );
				continue;
			}

			updateReportParameter( odaParamDefn, cachedParamDefn, setHandle,
					setParam.getParameterDataType( ) );

			retList.add( setParam );
		}

		return retList;
	}

	/**
	 * Returns the matched parameter definition by given name and position.
	 * 
	 * @param params
	 *            the ODA data set parameters
	 * @param paramName
	 *            the parameter name
	 * @param position
	 *            the position of the parameter
	 * @return the matched parameter definition
	 */

	static ParameterDefinition findParameterDefinition(
			DataSetParameters params, String paramName, Integer position )
	{
		if ( params == null )
			return null;

		if ( StringUtil.isBlank( paramName ) && position == null )
			return null;

		EList odaParams = params.getParameterDefinitions( );
		if ( odaParams == null || odaParams.isEmpty( ) )
			return null;

		for ( int i = 0; i < odaParams.size( ); i++ )
		{
			ParameterDefinition odaParamDefn = (ParameterDefinition) odaParams
					.get( i );

			DataElementAttributes dataAttrs = odaParamDefn.getAttributes( );
			if ( dataAttrs == null )
				continue;

			if ( StringUtil.isBlank( paramName ) )
			{
				if ( !ReportParameterAdapter.isEquals( paramName, dataAttrs
						.getName( ) ) )
					continue;

				if ( position.intValue( ) == dataAttrs.getPosition( ) )
					return odaParamDefn;
			}
			else if ( paramName.equals( dataAttrs.getName( ) ) )
				return odaParamDefn;
		}

		return null;
	}

	/**
	 * Returns the matched parameter definition by given name and position.
	 * 
	 * @param params
	 *            the ODA data set parameters
	 * @param paramName
	 *            the parameter name
	 * @param position
	 *            the position of the parameter
	 * @return the matched parameter definition
	 */

	static ParameterDefinition findParameterDefinition(
			DataSetParameters params, Integer position )
	{
		if ( params == null )
			return null;
		if ( position == null )
			return null;

		EList odaParams = params.getParameterDefinitions( );
		if ( odaParams == null || odaParams.isEmpty( ) )
			return null;

		for ( int i = 0; i < odaParams.size( ); i++ )
		{
			ParameterDefinition odaParamDefn = (ParameterDefinition) odaParams
					.get( i );

			DataElementAttributes dataAttrs = odaParamDefn.getAttributes( );
			if ( dataAttrs == null )
				continue;

			if ( position.intValue( ) == dataAttrs.getPosition( ) )
				return odaParamDefn;

		}

		return null;
	}

	/**
	 * Creates ODA data set parameters with given ROM data set parameters.
	 * 
	 * @param romSetParams
	 *            ROM defined data set parameters.
	 * @return the created ODA data set parameters.
	 * 
	 */

	DataSetParameters newOdaDataSetParams( Iterator romSetParams,
			DataSetParameters lastParameters, DataSetDesign dataSetDesign )
	{
		if ( !romSetParams.hasNext( ) )
			return null;

		DataSetParameters odaSetParams = ODADesignFactory.getFactory( )
				.createDataSetParameters( );

		while ( romSetParams.hasNext( ) )
		{
			OdaDataSetParameterHandle paramDefn = (OdaDataSetParameterHandle) romSetParams
					.next( );

			ParameterDefinition lastOdaParamDefn = findParameterDefinition(
					lastParameters, paramDefn.getNativeName( ), paramDefn
							.getPosition( ) );

			ParameterDefinition odaParamDefn = newParameterDefinition(
					paramDefn, lastOdaParamDefn, dataSetDesign );

			// update the name

			String name = paramDefn.getNativeName( ) == null ? "" : paramDefn //$NON-NLS-1$
					.getNativeName( );
			odaParamDefn.getAttributes( ).setName( name );

			odaSetParams.getParameterDefinitions( ).add( odaParamDefn );
		}

		return odaSetParams;
	}

	/**
	 * Sets the default value for ROM data set parameter. Should add quotes for
	 * the value if the data type is string.
	 * 
	 * @param setParam
	 *            the ROM data set parameter
	 * @param literalValue
	 *            the value
	 */

	private void setROMDefaultValue( DataSetParameter setParam,
			String literalValue )
	{
		if ( BIRT_JS_EXPR.equalsIgnoreCase( literalValue ) )
		{
			return;
		}

		String romDefaultValue = needsQuoteDelimiters( setParam
				.getParameterDataType( ) ) ? ParameterValueUtil
				.toJsExprValue( literalValue ) : literalValue;
		setParam.setDefaultValue( romDefaultValue );
	}

	/**
	 * Sets the default value for ODA default scalar value. If the value is
	 * quoted, removed it and set it. Otherwise, the default value is treated as
	 * js expression and set the constant BIRT_JS_EXPR as value
	 * 
	 * @param elementAttrs
	 *            the input element attributes
	 * @param dataType
	 *            the data type
	 * @param value
	 *            the default value
	 */

	private void setDefaultScalarValue( InputElementAttributes elementAttrs,
			String dataType, String value )
	{
		String literalValue = value;

		if ( needsQuoteDelimiters( dataType ) )
		{
			if ( ParameterValueUtil.isQuoted( value ) )
				literalValue = ParameterValueUtil.toLiteralValue( value );
			else
				literalValue = BIRT_JS_EXPR;
		}

		elementAttrs.setDefaultScalarValue( literalValue );
	}

	/**
	 * Checks whether the data type needs quote.
	 * 
	 * @param romDataType
	 *            the ROM defined data type
	 * @return <code>true</code> if data type is string. Otherwise
	 *         <code>false</code>.
	 */

	static boolean needsQuoteDelimiters( String romDataType )
	{
		boolean needs = false;

		if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( romDataType ) )
			needs = true;
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME
				.equals( romDataType ) )
			needs = true;
		else if ( DesignChoiceConstants.PARAM_TYPE_DATE
				.equals( romDataType ) )
			needs = true;
		else if ( DesignChoiceConstants.PARAM_TYPE_TIME
				.equals( romDataType ) )
			needs = true;
		else if ( DesignChoiceConstants.PARAM_TYPE_ANY.equals( romDataType ) )
			needs = true;
		return needs;
	}

	/**
	 * Gets all parameters in data set handle.
	 * 
	 * @param setHandle
	 *            data set handle
	 * @return a list contains parameters. Each item is
	 *         <code>OdaDataSetParameter</code>.
	 */

	private static List getRomDataSetParameters( OdaDataSetHandle setHandle )
	{
		List resultList = new ArrayList( );

		// Get all parameters in data set handle.

		Iterator iterator = setHandle.parametersIterator( );
		while ( iterator.hasNext( ) )
		{
			OdaDataSetParameterHandle paramHandle = (OdaDataSetParameterHandle) iterator
					.next( );
			resultList.add( paramHandle.getStructure( ) );
		}
		return resultList;
	}

	/**
	 * Merges user-defined and driver-defined parameters.
	 * 
	 * @param paramList
	 *            a list contains user-defined and driver-defined parameters.
	 *            Each item is <code>OdaDataSetParameter</code>.
	 * @param userList
	 *            a list contains user-defined parameters. Each item is
	 *            <code>OdaDataSetParameter</code>.
	 * @return a list contains parameters.Each item is the copy of
	 *         <code>OdaDataSetParameter</code> instance.
	 * @throws SemanticException
	 */

	private List mergeUserDefindAndDriverDefinedParameter( List paramList,
			List userList ) throws SemanticException
	{
		List resultList = new ArrayList( );
		if ( paramList == null && userList == null )
			return resultList;
		if ( paramList == null )
			return userList;
		if ( userList == null )
			return paramList;

		Iterator iterator = paramList.iterator( );

		List positionList = new ArrayList( );
		while ( iterator.hasNext( ) )
		{
			OdaDataSetParameter param = (OdaDataSetParameter) iterator.next( );
			Integer pos = param.getPosition( );
			OdaDataSetParameter userParam = findDataSetParameterByPosition(
					userList.iterator( ), pos );
			positionList.add( pos );

			// use driver-defined to update user-defined. just update
			// parameterDataType property

			if ( userParam == null )
			{
				resultList.add( param.copy( ) );
			}
			else
			{
				OdaDataSetParameter copied = (OdaDataSetParameter) userParam
						.copy( );
				if ( copied.getNativeDataType( ) != null
						&& !copied.getNativeDataType( ).equals(
								param.getNativeDataType( ) ) )
				{
					copied.setParameterDataType( param.getParameterDataType( ) );
					copied.setNativeDataType( param.getNativeDataType( ) );
				}

				resultList.add( copied );
			}
		}

		// Add value in user list.

		Iterator userIterator = userList.iterator( );
		while ( userIterator.hasNext( ) )
		{
			OdaDataSetParameter userParam = (OdaDataSetParameter) userIterator
					.next( );
			Integer pos = userParam.getPosition( );
			if ( !positionList.contains( pos ) )
			{
				resultList.add( userParam.copy( ) );
			}
		}

		return resultList;
	}

	/**
	 * Gets all parameter from data set handle and then translate them to
	 * parameter definition.
	 * 
	 * @param setHandle
	 *            data set handle
	 * @param setDesign
	 *            data set design
	 * @return a list contains parameter definition. Each item is
	 *         <code>ParameterDefinition</code>
	 */

	// List getParamDefinitionFromHandle( OdaDataSetHandle setHandle,
	// DataSetDesign setDesign )
	// {
	// DataSetParameters dsParameters = newOdaDataSetParams( setHandle
	// .parametersIterator( ), null, setDesign );
	//
	// List resultList = new ArrayList( );
	// if ( dsParameters == null )
	// return resultList;
	// EList paramDefns = dsParameters.getParameterDefinitions( );
	// for ( int i = 0; paramDefns != null && i < paramDefns.size( ); ++i )
	// {
	// ParameterDefinition paramDefn = (ParameterDefinition) paramDefns
	// .get( i );
	// resultList.add( paramDefn );
	// }
	// return resultList;
	// }
	/**
	 * Gets all position values in <code>DataSetParameters</code>.
	 * 
	 * @param designValues
	 *            design values
	 * @return a list contains position.
	 */

	private List getPositions( DataSetParameters params )
	{
		List resultList = new ArrayList( );
		if ( params == null )
			return resultList;

		EList odaDefns = params.getParameterDefinitions( );
		for ( int i = 0; odaDefns != null && i < odaDefns.size( ); ++i )
		{
			ParameterDefinition paramDefn = (ParameterDefinition) odaDefns
					.get( i );
			DataElementAttributes dataAttrs = paramDefn.getAttributes( );
			if ( dataAttrs == null )
				continue;

			resultList.add( new Integer( dataAttrs.getPosition( ) ) );
		}
		return resultList;
	}

	/**
	 * Gets all driver-defined parameters.
	 * 
	 * @param designParams
	 *            a list contains <code>ParameterDefinition</code> instance.
	 * @param userDefinedList
	 *            a list contains user-defined parameter. Each item is
	 *            <code>OdaDataSetParameter</code>.
	 * 
	 * @return a list contains driver-defined parameter.Each item is copy of
	 *         <code>ParameterDefinition</code>.
	 * @throws SemanticException
	 */

	static List getDriverDefinedParameters( EList designParams,
			List userDefinedList ) throws SemanticException
	{
		List resultList = new ArrayList( );
		List posList = getPositions( userDefinedList );

		for ( int i = 0; designParams != null && i < designParams.size( ); ++i )
		{
			ParameterDefinition definition = (ParameterDefinition) designParams
					.get( i );
			DataElementAttributes dataAttrs = definition.getAttributes( );
			if ( dataAttrs == null )
				continue;

			int pos = dataAttrs.getPosition( );

			if ( !posList.contains( new Integer( pos ) ) )
			{
				// driver -defined parameter

				resultList.add( EcoreUtil.copy( definition ) );
			}
		}
		return resultList;
	}

	/**
	 * Gets all position property of parameter.
	 * 
	 * @param paramList
	 *            a list contains parameter.
	 * @return a list contains position. Each item is <code>Integer</code>.
	 */

	private static List getPositions( List paramList )
	{
		List posList = new ArrayList( );
		if ( paramList == null )
			return posList;

		Iterator paramIterator = paramList.iterator( );
		while ( paramIterator.hasNext( ) )
		{
			OdaDataSetParameter parameter = (OdaDataSetParameter) paramIterator
					.next( );
			posList.add( parameter.getPosition( ) );
		}
		return posList;
	}

	/**
	 * Compare the DesignerValue and OdaDataSetParameter, if one param does not
	 * exist in DesignerValue, it must be user-defined one. Keep it in
	 * user-defined-param-list.
	 * 
	 * @param setDesign
	 *            data set design
	 * @param setHandle
	 *            data set handle
	 * @return list contains user-defined parameter structure.Each item is
	 *         <code>OdaDataSetParameter</code>
	 * @throws SemanticException
	 */

	List getUserDefinedParameter( DesignValues designerValues,
			DataSetDesign setDesign, OdaDataSetHandle setHandle )
			throws SemanticException
	{
		List resultList = new ArrayList( );

		if ( designerValues == null )
		{
			resultList.addAll( DataSetParameterAdapter
					.getRomDataSetParameters( setHandle ) );
		}
		else
		{
			// Compare designvalue and dataset handle.

			Iterator iterator = setHandle.parametersIterator( );
			List posList = new DataSetParameterAdapter( )
					.getPositions( designerValues.getDataSetParameters( ) );

			while ( iterator.hasNext( ) )
			{
				OdaDataSetParameterHandle paramHandle = (OdaDataSetParameterHandle) iterator
						.next( );
				Integer position = paramHandle.getPosition( );
				if ( position == null )
					continue;
				if ( !posList.contains( position ) )
				{
					// User-defined parameter.

					resultList.add( paramHandle.getStructure( ) );
				}
			}
		}
		return resultList;
	}

}
