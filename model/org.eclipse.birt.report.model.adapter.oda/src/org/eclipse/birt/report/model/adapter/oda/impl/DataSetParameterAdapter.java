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
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
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
	 * The data set handle.
	 */

	private OdaDataSetHandle setHandle = null;

	/**
	 * The data set design.
	 */

	private DataSetDesign setDesign = null;

	/**
	 * The user defined parameters. Only updated in
	 * {@link #updateUserDefinedParameter(DesignValues)}.
	 */

	private List<OdaDataSetParameterHandle> userDefinedParams = null;

	/**
	 * The data set handle defined parameters.
	 */

	private List<OdaDataSetParameterHandle> setDefinedParams = null;

	/**
	 * The constructor.
	 * 
	 * @param setHandle
	 *            the data set handle
	 * @param setDesign
	 *            the data set design
	 * 
	 */

	DataSetParameterAdapter( OdaDataSetHandle setHandle, DataSetDesign setDesign )
	{
		this.setHandle = setHandle;
		this.setDesign = setDesign;

		Iterator<OdaDataSetParameterHandle> tmpParams = setHandle
				.parametersIterator( );
		setDefinedParams = new ArrayList<OdaDataSetParameterHandle>( );
		while ( tmpParams.hasNext( ) )
			setDefinedParams.add( tmpParams.next( ) );
	}

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
			ParameterDefinition lastOdaParamDefn )
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
							reportParam, setDesign );

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
		{
			inputAttrs = ODADesignFactory.getFactory( )
					.createInputParameterAttributes( );
			odaParamDefn.setInputAttributes( inputAttrs );
		}

		inputAttrs.setElementAttributes( newInputElementAttrs( paramHandle,
				inputAttrs.getElementAttributes( ) ) );

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
			OdaDataSetParameter setParam, String dataSourceId, String dataSetId )
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
				setDefinedParams.iterator( ) );
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
			List retList )
	{
		if ( dataAttrs == null )
			return;

		String nativeName = dataAttrs.getName( );

		// make sure the OdaDataSetParameter must have a name. This is a
		// requirement in ROM. If the name in the data set handle is obsolete,
		// also need to be updated.

		String name = setParam.getName( );
		if ( StringUtil.isBlank( name ) || !name.equalsIgnoreCase( nativeName ) )
		{
			setParam.setName( nativeName );
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

		if ( !CompareUtil.isEquals( (String) oldValue, (String) newValue )
				&& !withLinkedParameter )
			setROMDefaultValue( setParam, (String) newValue );

		oldValue = cachedElementAttrs == null ? null : Boolean
				.valueOf( cachedElementAttrs.isOptional( ) );
		newValue = Boolean.valueOf( elementAttrs.isOptional( ) );

		if ( !CompareUtil.isEquals( (Boolean) oldValue, (Boolean) newValue ) )
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
			ParameterDefinition cachedParamDefn, String dataType )
			throws SemanticException
	{
		DataElementAttributes dataAttrs = odaParamDefn.getAttributes( );

		if ( dataAttrs == null )
		{
			return;
		}

		ModuleHandle module = setHandle.getModuleHandle( );

		OdaDataSetParameterHandle paramDefn = findDataSetParameterByName(
				dataAttrs.getName( ), new Integer( dataAttrs.getPosition( ) ),
				new Integer( dataAttrs.getNativeDataTypeCode( ) ),
				setDefinedParams.iterator( ) );
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

			// nativeName/name, position and nativeDataType should match.

			// case 1: if the native name is not blank, just use it.

			if ( !StringUtil.isBlank( tmpNativeName )
					&& tmpNativeName.equals( dataSetParamName ) )
				return param;

			// case 2: if the native name is blank, match native data type and
			// position

			if ( StringUtil.isBlank( tmpNativeName )
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

	private static OdaDataSetParameterHandle findDataSetParameterByPosition(
			Iterator<OdaDataSetParameterHandle> params, Integer position )
	{
		if ( position == null )
			return null;

		while ( params.hasNext( ) )
		{
			OdaDataSetParameterHandle param = params.next( );
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
	 * Creates a list containing <code>OdaDataSetParameter</code> with the given
	 * ODA data set parameter definition.
	 * 
	 * @param cachedDataSetParameters
	 *            cached dataset parameters.
	 * @return a list containing <code>DataSetParameter</code>.
	 * @throws SemanticException
	 */

	List newROMSetParams( DataSetParameters cachedDataSetParameters )
			throws SemanticException
	{
		if ( setDesign == null )
			return null;

		// create for updating.
		// Merge dataset design and user-defined parameter list. Now data
		// set design contains lastest driver-defined parameters

		List definedParamList = newRomSetParams( cachedDataSetParameters );

		if ( setDefinedParams.isEmpty( ) && userDefinedParams == null )
		{
			// use for creating rom parameter.

			return definedParamList;
		}

		// Merge userDefinedparamList and driverDefinedParamList

		return mergeUserDefindAndDriverDefinedParameter( definedParamList );

	}

	/**
	 * Creates a list containing <code>OdaDataSetParameter</code> with the given
	 * ODA data set parameter definition.
	 * 
	 * @param odaSetParams
	 *            ODA data set parameter definition
	 * @param setHandle
	 *            oda data set handle
	 * @param cachedDataSetParameters
	 *            cached dataset parameters.
	 * @return a list containing <code>DataSetParameter</code>.
	 */

	private List newRomSetParams( DataSetParameters cachedDataSetParameters )
			throws SemanticException
	{
		List retList = new ArrayList( );

		DataSetParameters odaSetParams = setDesign.getParameters( );
		if ( odaSetParams == null )
			return null;

		EList odaParams = odaSetParams.getParameterDefinitions( );
		if ( odaParams == null || odaParams.isEmpty( ) )
			return null;

		List positions = new ArrayList( );

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
								dataAttrs.getNativeDataTypeCode( ) ),
						setDefinedParams.iterator( ) );

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

			updateROMDataSetParameterName( dataAttrs, setParam, retList );

			updateROMDataSetParameterFromDataAttrs( dataAttrs,
					cachedParamDefn == null ? null : cachedParamDefn
							.getAttributes( ), setParam, setDesign
							.getOdaExtensionDataSourceId( ), setDesign
							.getOdaExtensionDataSetId( ) );

			updateAndCheckPosition( positions, setParam );

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

			updateReportParameter( odaParamDefn, cachedParamDefn, setParam
					.getParameterDataType( ) );

			retList.add( setParam );
		}

		// control name value here.

		IdentifierUtility.updateParams2UniqueName( retList );

		return retList;
	}

	/**
	 * Updates the position first if the value is null or less than 1. Checks
	 * whether the position duplicates with others. If so, throw exception.
	 * 
	 * @param positions
	 *            a list containing positions
	 * @param setParam
	 *            the current data set parameter
	 * @param newPos
	 *            the optional new position
	 * @throws SemanticException
	 *             if the position duplicates with others
	 */

	private void updateAndCheckPosition( List positions,
			DataSetParameter setParam ) throws SemanticException
	{

		// if the position is still null. This is possible in the oda design
		// spec. we should make the position as index+1.

		Integer newPos = new Integer( 0 );
		Integer pos = setParam.getPosition( );
		if ( pos == null || pos.intValue( ) < 0 )
		{
			setParam.setPosition( newPos );
		}
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
				if ( !CompareUtil.isEquals( paramName, dataAttrs.getName( ) ) )
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
	 * @param lastParameters
	 *            cached data set parameters.
	 * @return the created ODA data set parameters.
	 * 
	 */

	DataSetParameters newOdaDataSetParams( DataSetParameters lastParameters )
	{
		return newOdaDataSetParams( setDefinedParams, lastParameters );
	}

	/**
	 * Creates ODA data set parameters with the given ROM data set parameters
	 * and cached values in the last request.
	 * 
	 * @param odaParams
	 *            ROM data set parameters
	 * @param lastParameters
	 *            cached values in the last request
	 * @return ODA data set parameters
	 */

	private DataSetParameters newOdaDataSetParams(
			List<OdaDataSetParameterHandle> odaParams,
			DataSetParameters lastParameters )
	{
		if ( odaParams.isEmpty( ) )
			return null;

		DataSetParameters odaSetParams = ODADesignFactory.getFactory( )
				.createDataSetParameters( );

		List<ParameterDefinition> params = odaSetParams
				.getParameterDefinitions( );
		for ( int i = 0; i < odaParams.size( ); i++ )
		{
			OdaDataSetParameterHandle paramDefn = odaParams.get( i );

			String nativeName = paramDefn.getNativeName( );

			ParameterDefinition lastOdaParamDefn = null;

			if ( lastParameters != null )
			{
				lastOdaParamDefn = findParameterDefinition( lastParameters,
						nativeName, paramDefn.getPosition( ) );
			}

			ParameterDefinition odaParamDefn = newParameterDefinition(
					paramDefn, lastOdaParamDefn );

			if ( nativeName == null )
				nativeName = ""; //$NON-NLS-1$

			// update the name

			odaParamDefn.getAttributes( ).setName( nativeName );

			params.add( odaParamDefn );
		}

		return odaSetParams;
	}

	/**
	 * Creates ODA data set parameters with given ROM data set parameters.
	 * 
	 * @param romParams
	 *            cached data set parameters.
	 * @return the created ODA data set parameters.
	 * 
	 */

	DataSetParameters newOdaDataSetParams(
			List<OdaDataSetParameterHandle> romParams )
	{
		return newOdaDataSetParams( romParams, null );
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

		String originalValue = setParam.getDefaultValue( );
		String quotataionMark = null;
		if ( ParameterValueUtil.isQuoted( originalValue ) )
		{
			quotataionMark = originalValue.substring( 0, 1 );
		}

		String romDefaultValue = needsQuoteDelimiters( setParam
				.getParameterDataType( ) ) ? ParameterValueUtil.toJsExprValue(
				literalValue, quotataionMark ) : literalValue;
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
		else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( romDataType ) )
			needs = true;
		else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( romDataType ) )
			needs = true;
		else if ( DesignChoiceConstants.PARAM_TYPE_ANY.equals( romDataType ) )
			needs = true;
		return needs;
	}

	/**
	 * Merges user-defined and driver-defined parameters.
	 * 
	 * @param paramList
	 *            a list contains user-defined and driver-defined parameters.
	 *            Each item is <code>OdaDataSetParameter</code>. It is the new
	 *            instance.
	 * @return a list contains parameters.Each item is the copy of
	 *         <code>OdaDataSetParameter</code> instance.
	 * @throws SemanticException
	 */

	private List<OdaDataSetParameter> mergeUserDefindAndDriverDefinedParameter(
			List paramList ) throws SemanticException
	{
		List resultList = new ArrayList( );
		if ( paramList == null && userDefinedParams == null )
			return resultList;
		if ( paramList == null )
			return getCopy( userDefinedParams );
		if ( userDefinedParams == null )
			return paramList;

		Iterator iterator = paramList.iterator( );

		List positionList = new ArrayList( );
		while ( iterator.hasNext( ) )
		{
			OdaDataSetParameter param = (OdaDataSetParameter) iterator.next( );
			Integer pos = param.getPosition( );
			OdaDataSetParameterHandle userParam = findDataSetParameterByPosition(
					userDefinedParams.iterator( ), pos );
			positionList.add( pos );

			// use driver-defined to update user-defined. just update
			// parameterDataType property

			if ( userParam == null )
			{
				resultList.add( param );
			}
			else
			{
				if ( userParam.getNativeDataType( ) != null
						&& !userParam.getNativeDataType( ).equals(
								param.getNativeDataType( ) ) )
				{
					userParam.setParameterDataType( param
							.getParameterDataType( ) );
					userParam.setNativeDataType( param.getNativeDataType( ) );
				}

				resultList.add( userParam.getStructure( ) );
			}
		}

		// Add value in user list.

		Iterator<OdaDataSetParameterHandle> userIterator = userDefinedParams
				.iterator( );
		while ( userIterator.hasNext( ) )
		{
			OdaDataSetParameterHandle userParam = userIterator.next( );
			Integer pos = userParam.getPosition( );
			if ( !positionList.contains( pos ) )
			{
				resultList.add( userParam.getStructure( ) );
			}
		}

		return resultList;
	}

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
			OdaDataSetParameterHandle parameter = (OdaDataSetParameterHandle) paramIterator
					.next( );
			posList.add( parameter.getPosition( ) );
		}
		return posList;
	}

	/**
	 * Updates data set parameters on data set handle with updated values.
	 * 
	 * <ul>
	 * <li>if one parameter in newParams has the corresponding data set
	 * parameter in data set handle, use it to update the one on set handle.
	 * <li>if the new parameter on set handle doesn't exist, add it.
	 * <li>
	 * Otherwise, the parameter on the set handle should be removed.
	 * </ul>
	 * <p>
	 * see bugzilla 187775.
	 * 
	 * @param newParams
	 *            the updated data set parameter
	 * @throws SemanticException
	 */

	public void updateRomDataSetParamsWithNewParams( List newParams )
			throws SemanticException
	{
		PropertyHandle propertyHandle = setHandle
				.getPropertyHandle( DataSetHandle.PARAMETERS_PROP );

		Iterator iterator = setDefinedParams.iterator( );

		List nameList = new ArrayList( );
		List propList = null;
		List<IStructure> toRemovedList = new ArrayList<IStructure>( );

		// notice that this iterator is different that in the property handle.
		// So, there will no concurrent modification exception.

		while ( iterator.hasNext( ) )
		{
			DataSetParameterHandle dsParamHandle = (DataSetParameterHandle) iterator
					.next( );

			// initialize the property iterator

			if ( propList == null )
			{
				Iterator propIterator = dsParamHandle.getDefn( )
						.getPropertyIterator( );
				propList = new ArrayList( );

				while ( propIterator.hasNext( ) )
					propList.add( propIterator.next( ) );
			}

			// Check if new values exist the same name as
			// DataSetParameterHandle's.

			String name = dsParamHandle.getName( );
			OdaDataSetParameter odaDsParam = null;
			for ( int i = 0; i < newParams.size( ); ++i )
			{
				odaDsParam = (OdaDataSetParameter) newParams.get( i );

				String odaName = odaDsParam.getName( );
				if ( name.equalsIgnoreCase( odaName ) )
				{
					nameList.add( odaDsParam );
					break;
				}
				odaDsParam = null;
			}

			if ( odaDsParam != null )
			{
				// update dsParamHandle with odaDsParam

				updateROMDataSetParamWithNewParam( odaDsParam, dsParamHandle,
						propList );
			}
			else
			{
				// drop dsParamhandle

				toRemovedList.add( dsParamHandle.getStructure( ) );
			}
		}

		for ( int i = 0; i < toRemovedList.size( ); i++ )
		{
			IStructure toRemoved = toRemovedList.get( i );
			propertyHandle.removeItem( toRemoved );

		}

		// for others, should add them.

		for ( int i = 0; i < newParams.size( ); ++i )
		{
			OdaDataSetParameter odaDsParam = (OdaDataSetParameter) newParams
					.get( i );
			if ( !nameList.contains( odaDsParam ) )
			{
				// add odaDsParam
				propertyHandle.addItem( odaDsParam );
			}
		}

	}

	/**
	 * Updates the parameter on the data set handle with the updated data set
	 * parameter.
	 * 
	 * @param odaDsParam
	 *            the updated data set parameter
	 * @param dsParamHandle
	 *            the parameter on the data set handle
	 * @throws SemanticException
	 */

	private void updateROMDataSetParamWithNewParam(
			OdaDataSetParameter odaDsParam,
			DataSetParameterHandle dsParamHandle, List propList )
			throws SemanticException
	{
		// update dsParamHandle with odaDsParam

		for ( int i = 0; i < propList.size( ); i++ )
		{
			PropertyDefn propDefn = (PropertyDefn) propList.get( i );
			String memberName = propDefn.getName( );
			if ( DataSetHandle.NAME_PROP.equals( memberName ) )
				continue;
			Object value = odaDsParam.getLocalProperty( null, memberName );
			dsParamHandle.setProperty( memberName, value );
		}
	}

	/**
	 * Compare the DesignerValue and OdaDataSetParameter, if one parameter does
	 * not exist in DesignerValue, it must be user-defined one. Keep it in user
	 * defined parameter list.
	 * 
	 * @param parameters
	 * 
	 * @throws SemanticException
	 */

	void updateUserDefinedParameter( DataSetParameters parameters )
	{
		userDefinedParams = new ArrayList<OdaDataSetParameterHandle>( );
		if ( parameters == null )
		{
			for ( int i = 0; i < setDefinedParams.size( ); i++ )
			{
				userDefinedParams.add( ( setDefinedParams.get( i ) ) );
			}
		}

		// Compare designer value and data set handle.

		else
		{
			List posList = getPositions( parameters );

			for ( int i = 0; i < setDefinedParams.size( ); i++ )
			{
				OdaDataSetParameterHandle paramHandle = setDefinedParams
						.get( i );
				Integer position = paramHandle.getPosition( );
				if ( position == null )
					continue;
				if ( !posList.contains( position ) )
				{
					// User-defined parameter.

					userDefinedParams.add( paramHandle );
				}
			}
		}

	}

	/**
	 * Returns the cached data set handle.
	 * 
	 * @return the setHandle the data set handle
	 */

	OdaDataSetHandle getSetHandle( )
	{
		return setHandle;
	}

	/**
	 * Returns the user defined parameters.
	 * 
	 * @return the userDefinedParams the user defined parameters
	 */

	List<OdaDataSetParameterHandle> getUserDefinedParams( )
	{
		return userDefinedParams;
	}

	/**
	 * Creates a new copy with the given ROM defined data set parameter handle.
	 * 
	 * @param romParams
	 *            a list containing ROM defined data set parameter handle
	 * @return a list containing copied values
	 */

	private List<OdaDataSetParameter> getCopy(
			List<OdaDataSetParameterHandle> romParams )
	{
		if ( romParams == null )
			return null;

		List<OdaDataSetParameter> retList = new ArrayList<OdaDataSetParameter>( );
		for ( int i = 0; i < romParams.size( ); i++ )
		{
			OdaDataSetParameter copy = (OdaDataSetParameter) romParams.get( i )
					.getStructure( ).copy( );
			retList.add( copy );
		}

		return retList;

	}
}
