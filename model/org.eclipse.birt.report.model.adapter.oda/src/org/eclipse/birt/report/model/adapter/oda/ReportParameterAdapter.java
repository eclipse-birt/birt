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

package org.eclipse.birt.report.model.adapter.oda;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.model.adapter.oda.DataSetParameterAdapter.ParameterValueUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SelectionChoiceHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.util.StringUtil;
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
import org.eclipse.datatools.connectivity.oda.design.InputParameterUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputPromptControlStyle;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueChoices;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueDefinition;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Converts values between a report parameter and ODA Design Session Request.
 * 
 */

public class ReportParameterAdapter
{

	/**
	 * Default constructor.
	 */

	public ReportParameterAdapter( )
	{

	}

	/**
	 * Checks whether the given report parameter is updated. This method checks
	 * values of report parameters and values in data set design.
	 * <p>
	 * If any input argument is null or the matched ODA parameter definition
	 * cannot be found, return <code>true</code>.
	 * 
	 * @param reportParam
	 *            the report parameter
	 * @param param
	 *            the ROM data set parameter
	 * @param dataSetDesign
	 *            the data set design
	 * @return <code>true</code> if the report paramter is updated or has no
	 *         parameter definition in the data set design. Otherwise
	 *         <code>false</code>.
	 */

	boolean isUpdatedReportParameter( ScalarParameterHandle reportParam,
			ParameterDefinition odaParam, String newDataType )
	{
		if ( reportParam == null || odaParam == null )
			return true;

		DataElementAttributes dataAttrs = odaParam.getAttributes( );
		boolean allowNull = reportParam.allowNull( );
		Boolean odaAllowNull = getROMNullability( dataAttrs.getNullability( ) );
		if ( odaAllowNull != null && allowNull != odaAllowNull.booleanValue( ) )
			return false;

		if ( !DesignChoiceConstants.PARAM_TYPE_ANY
				.equalsIgnoreCase( newDataType ) )
		{
			if ( !isEquals( newDataType, reportParam.getDataType( ) ) )
				return false;
		}

		DataElementUIHints dataUiHints = dataAttrs.getUiHints( );
		if ( dataUiHints != null )
		{
			String newPromptText = dataUiHints.getDisplayName( );
			String newHelpText = dataUiHints.getDescription( );

			if ( !isEquals( newPromptText, reportParam.getPromptText( ) ) )
				return false;

			if ( !isEquals( newHelpText, reportParam.getHelpText( ) ) )
				return false;
		}

		InputParameterAttributes paramAttrs = odaParam.getInputAttributes( );
		InputParameterAttributes tmpParamDefn = null;
		String tmpDataSetName = null;

		if ( paramAttrs != null )
		{
			tmpParamDefn = (InputParameterAttributes) EcoreUtil
					.copy( paramAttrs );

			DynamicValuesQuery tmpDynamicQuery = tmpParamDefn
					.getElementAttributes( ).getDynamicValueChoices( );

			if ( tmpDynamicQuery != null )
			{
				tmpDataSetName = tmpDynamicQuery.getDataSetDesign( ).getName( );
				tmpDynamicQuery.setDataSetDesign( null );
			}

			if ( tmpParamDefn.getUiHints( ) != null )
			{
				tmpParamDefn.setUiHints( null );
			}
		}
		else
			tmpParamDefn = DesignFactory.eINSTANCE
					.createInputParameterAttributes( );

		InputParameterAttributes tmpParamDefn1 = DesignFactory.eINSTANCE
				.createInputParameterAttributes( );

		updateInputElementAttrs( tmpParamDefn1, reportParam, null );
		if ( tmpParamDefn1.getUiHints( ) != null )
		{
			tmpParamDefn1.setUiHints( null );
		}
		DynamicValuesQuery tmpDynamicQuery1 = tmpParamDefn1
				.getElementAttributes( ).getDynamicValueChoices( );
		String tmpDataSetName1 = null;
		if ( tmpDynamicQuery1 != null )
		{
			tmpDataSetName1 = tmpDynamicQuery1.getDataSetDesign( ).getName( );
			tmpDynamicQuery1.setDataSetDesign( null );
		}

		if ( !isEquals( tmpDataSetName, tmpDataSetName1 ) )
			return false;

		return EcoreUtil.equals( tmpParamDefn, tmpParamDefn1 );
	}

	static boolean isEquals( String value1, String value2 )
	{
		// may be same string or both null.

		if ( value1 == value2 )
			return true;

		if ( value1 != null && value2 == null )
			return false;

		if ( value1 == null && value2 != null )
			return false;

		if ( !value1.equals( value2 ) )
			return false;

		return true;
	}

	/**
	 * Updates allowNull property for the given data set parameter definition.
	 * 
	 * @param romParamDefn
	 *            the data set parameter definition.
	 * @param nullability
	 *            the ODA object indicates nullability.
	 */

	static Boolean getROMNullability( ElementNullability nullability )
	{
		if ( nullability == null )
			return null;

		switch ( nullability.getValue( ) )
		{
			case ElementNullability.NULLABLE :
				return Boolean.TRUE;
			case ElementNullability.NOT_NULLABLE :
				return Boolean.FALSE;
			case ElementNullability.UNKNOWN :
				return null;
		}

		return null;
	}

	/**
	 * Refreshes property values of the given report parameter by the given data
	 * set design. This method first copies values from ROM data set parameter
	 * to report parameter, then copies values from DataSetDesign to the report
	 * parameter.
	 * <p>
	 * When copies values from DataSetDesign, cached value in
	 * OdaDataSetHandle.designerValues are also considerred.
	 * 
	 * 
	 * @param reportParam
	 *            the report parameter
	 * @param dataSetParam
	 *            the data set parameter
	 * @param dataSetDesign
	 *            the data set design
	 * @throws SemanticException
	 *             if value in the data set design is invalid
	 */

	public void updateLinkedReportParameter( ScalarParameterHandle reportParam,
			OdaDataSetParameterHandle dataSetParam, DataSetDesign dataSetDesign )
			throws SemanticException
	{
		if ( reportParam == null || dataSetParam == null )
			return;

		ParameterDefinition matchedParam = null;
		String dataType = null;

		OdaDataSetHandle setHandle = (OdaDataSetHandle) dataSetParam
				.getElementHandle( );

		if ( dataSetDesign != null )
		{
			matchedParam = getValidParameterDefinition( dataSetParam,
					dataSetDesign.getParameters( ) );

			dataType = DataSetParameterAdapter.getROMDataType( dataSetDesign
					.getOdaExtensionDataSourceId( ), dataSetDesign
					.getOdaExtensionDataSetId( ),
					(OdaDataSetParameter) dataSetParam.getStructure( ),
					setHandle == null ? null : setHandle.parametersIterator( ) );
		}

		CommandStack cmdStack = reportParam.getModuleHandle( )
				.getCommandStack( );

		cmdStack.startTrans( null );
		try
		{

			updateLinkedReportParameterFromROMParameter( reportParam,
					dataSetParam );

			if ( matchedParam != null )
				updateLinkedReportParameter( reportParam, matchedParam, null,
						dataType );
		}
		catch ( SemanticException e )
		{
			cmdStack.rollback( );
			throw e;
		}

		cmdStack.commit( );
	}

	/**
	 * Updates values in report parameter by given ROM data set parameter.
	 * 
	 * @param reportParam
	 *            the report parameter
	 * @param dataSetParam
	 *            the data set parameter
	 * @throws SemanticException
	 */

	private void updateLinkedReportParameterFromROMParameter(
			ScalarParameterHandle reportParam,
			OdaDataSetParameterHandle dataSetParam ) throws SemanticException
	{
		assert reportParam != null;

		if ( dataSetParam == null )
			return;

		String name = dataSetParam.getName( );
		if ( !StringUtil.isBlank( name ) )
			reportParam.setName( name );

		String dataType = dataSetParam.getParameterDataType( );
		if ( !StringUtil.isBlank( dataType ) )
			reportParam.setDataType( dataType );

		String defaultValue = dataSetParam.getDefaultValue( );
		String paramName = dataSetParam.getParamName( );

		if ( !StringUtil.isBlank( defaultValue )
				&& StringUtil.isBlank( paramName ) )
		{
			setROMDefaultValue( reportParam, defaultValue );
		}

		if ( StringUtil.isBlank( paramName ) )
		{
			dataSetParam.setParamName( reportParam.getName( ) );
		}

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

	private void setROMDefaultValue( ScalarParameterHandle setParam,
			String value ) throws SemanticException
	{
		String literalValue = value;

		if ( literalValue != null )
		{
			boolean match = ExpressionUtil
					.isScalarParamReference( literalValue );
			if ( match )
				return;
		}

		if ( DataSetParameterAdapter.BIRT_JS_EXPR
				.equalsIgnoreCase( literalValue ) )
		{
			return;
		}

		if ( DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase( setParam
				.getDataType( ) ) )
		{
			if ( ParameterValueUtil.isQuoted( value ) )
			{
				literalValue = ParameterValueUtil.toLiteralValue( value );
				setParam.setDefaultValue( literalValue );
			}
		}
	}

	/**
	 * Refreshes property values of the given report parameter by the given
	 * parameter definition and cached parameter definition. If values in cached
	 * parameter definition is null or values in cached parameter definition are
	 * not equal to values in parameter defnition, update values in given report
	 * parameter.
	 * 
	 * @param reportParam
	 *            the report parameter
	 * @param dataSetDesign
	 *            the data set design
	 * @throws SemanticException
	 *             if value in the data set design is invalid
	 */

	void updateLinkedReportParameter( ScalarParameterHandle reportParam,
			ParameterDefinition paramDefn, ParameterDefinition cachedParamDefn,
			String dataType ) throws SemanticException
	{
		if ( paramDefn == null )
			return;

		if ( isUpdatedReportParameter( reportParam, paramDefn, dataType ) )
		{
			return;
		}
		CommandStack cmdStack = reportParam.getModuleHandle( )
				.getCommandStack( );
		try
		{
			cmdStack.startTrans( null );

			// any type is not support in report parameter data type.

			if ( dataType == null
					|| !DesignChoiceConstants.PARAM_TYPE_ANY
							.equalsIgnoreCase( dataType ) )
				reportParam.setDataType( dataType );

			updateDataElementAttrsToReportParam( paramDefn.getAttributes( ),
					cachedParamDefn == null ? null : cachedParamDefn
							.getAttributes( ), reportParam );

			updateInputParameterAttrsToReportParam( paramDefn
					.getInputAttributes( ), cachedParamDefn == null
					? null
					: cachedParamDefn.getInputAttributes( ), reportParam );

		}
		catch ( SemanticException e )
		{
			cmdStack.rollback( );
			throw e;
		}

		cmdStack.commit( );
	}

	/**
	 * Refreshes property values of the given ROM ODA data set parameter.
	 * 
	 * 
	 * @param reportParam
	 *            the report parameter
	 * @param dataSetParam
	 *            the Oda data set parameter
	 * @throws SemanticException
	 *             if value in the data set design is invalid
	 */

	public void updateLinkedReportParameter( ScalarParameterHandle reportParam,
			OdaDataSetParameterHandle dataSetParam ) throws SemanticException
	{
		if ( reportParam == null || dataSetParam == null )
			return;

		updateLinkedReportParameterFromROMParameter( reportParam, dataSetParam );
	}

	/**
	 * Returns the matched ODA data set parameter by the given ROM data set
	 * parameter and data set design.
	 * 
	 * @param param
	 *            the ROM data set parameter
	 * @param dataSetDesign
	 *            the oda data set design
	 * @return the matched ODA parameter defintion
	 */

	private static ParameterDefinition getValidParameterDefinition(
			OdaDataSetParameterHandle param, DataSetParameters odaParams )
	{
		if ( param == null || odaParams == null )
			return null;

		if ( odaParams.getParameterDefinitions( ).isEmpty( ) )
			return null;

		ParameterDefinition matchedParam = DataSetParameterAdapter
				.findParameterDefinition( odaParams, param.getNativeName( ),
						param.getPosition( ) );
		return matchedParam;
	}

	/**
	 * Updates values in DataElementAttributes to the given report parameter.
	 * 
	 * @param dataAttrs
	 *            the latest data element attributes
	 * @param cachedDataAttrs
	 *            the cached data element attributes
	 * @param reportParam
	 *            the report parameter
	 * @throws SemanticException
	 */

	private void updateDataElementAttrsToReportParam(
			DataElementAttributes dataAttrs,
			DataElementAttributes cachedDataAttrs,
			ScalarParameterHandle reportParam ) throws SemanticException
	{

		if ( dataAttrs == null )
			return;

		boolean allowsNull = dataAttrs.allowsNull( );
		if ( cachedDataAttrs == null
				|| cachedDataAttrs.allowsNull( ) != allowsNull )
			reportParam.setAllowNull( dataAttrs.allowsNull( ) );

		DataElementUIHints dataUiHints = dataAttrs.getUiHints( );
		DataElementUIHints cachedDataUiHints = ( cachedDataAttrs == null
				? null
				: cachedDataAttrs.getUiHints( ) );
		if ( dataUiHints != null )
		{
			String displayName = dataUiHints.getDisplayName( );
			String cachedDisplayName = cachedDataUiHints == null
					? null
					: cachedDataUiHints.getDisplayName( );
			if ( cachedDisplayName == null
					|| !cachedDisplayName.equals( displayName ) )
				reportParam.setPromptText( displayName );

			String description = dataUiHints.getDescription( );
			String cachedDescription = cachedDataUiHints == null
					? null
					: cachedDataUiHints.getDescription( );
			if ( cachedDescription == null
					|| !cachedDescription.equals( description ) )
				reportParam.setHelpText( description );
		}

	}

	/**
	 * Updates values in InputParameterAttributes to the given report parameter.
	 * 
	 * @param dataAttrs
	 *            the latest input parameter attributes
	 * @param cachedDataAttrs
	 *            the cached input parameter attributes
	 * @param reportParam
	 *            the report parameter
	 * @throws SemanticException
	 */

	private void updateInputParameterAttrsToReportParam(
			InputParameterAttributes inputParamAttrs,
			InputParameterAttributes cachedInputParamAttrs,
			ScalarParameterHandle reportParam ) throws SemanticException
	{
		if ( inputParamAttrs == null )
			return;

		InputParameterUIHints paramUiHints = inputParamAttrs.getUiHints( );
		if ( paramUiHints != null
				&& reportParam.getContainer( ) instanceof ParameterGroupHandle )
		{
			ParameterGroupHandle paramGroup = (ParameterGroupHandle) reportParam
					.getContainer( );

			InputParameterUIHints cachedParamUiHints = cachedInputParamAttrs == null
					? null
					: cachedInputParamAttrs.getUiHints( );

			String cachedGroupPromptDisplayName = cachedParamUiHints == null
					? null
					: cachedParamUiHints.getGroupPromptDisplayName( );

			String groupPromptDisplayName = paramUiHints
					.getGroupPromptDisplayName( );

			if ( cachedGroupPromptDisplayName == null
					|| !cachedGroupPromptDisplayName
							.equals( groupPromptDisplayName ) )
				paramGroup.setDisplayName( paramUiHints
						.getGroupPromptDisplayName( ) );
		}

		updateInputElementAttrsToReportParam( inputParamAttrs
				.getElementAttributes( ), cachedInputParamAttrs == null
				? null
				: cachedInputParamAttrs.getElementAttributes( ), reportParam );
	}

	/**
	 * Updates values in InputElementAttributes to the given report parameter.
	 * 
	 * @param dataAttrs
	 *            the latest input element attributes
	 * @param cachedDataAttrs
	 *            the cached input element attributes
	 * @param reportParam
	 *            the report parameter
	 * @throws SemanticException
	 */

	private void updateInputElementAttrsToReportParam(
			InputElementAttributes elementAttrs,
			InputElementAttributes cachedElementAttrs,
			ScalarParameterHandle reportParam ) throws SemanticException
	{
		if ( elementAttrs == null )
			return;

		// update default values.

		String defaultValue = elementAttrs.getDefaultScalarValue( );
		String cachedDefaultValue = cachedElementAttrs == null
				? null
				: cachedElementAttrs.getDefaultScalarValue( );
		if ( cachedDefaultValue == null
				|| !cachedDefaultValue.equals( defaultValue ) )
		{
			// only update when the value is not internal value.

			if ( !DataSetParameterAdapter.BIRT_JS_EXPR.equals( defaultValue ) )
				reportParam.setDefaultValue( defaultValue );
		}

		// update isOptional value

		Boolean isOptional = Boolean.valueOf( elementAttrs.isOptional( ) );
		Boolean cachedIsOptional = cachedElementAttrs == null ? null : Boolean
				.valueOf( cachedElementAttrs.isOptional( ) );
		if ( cachedIsOptional == null || !cachedIsOptional.equals( isOptional ) )
			reportParam.setAllowBlank( isOptional.booleanValue( ) );

		// update conceal value

		Boolean masksValue = Boolean.valueOf( elementAttrs.isMasksValue( ) );
		Boolean cachedMasksValues = cachedElementAttrs == null ? null : Boolean
				.valueOf( cachedElementAttrs.isMasksValue( ) );
		if ( cachedMasksValues == null
				|| !cachedMasksValues.equals( masksValue ) )
			reportParam.setConcealValue( masksValue.booleanValue( ) );

		updateROMSelectionList( elementAttrs.getStaticValueChoices( ),
				cachedElementAttrs == null ? null : cachedElementAttrs
						.getStaticValueChoices( ), reportParam );

		updateROMDyanmicList( elementAttrs.getDynamicValueChoices( ),
				cachedElementAttrs == null ? null : cachedElementAttrs
						.getDynamicValueChoices( ), reportParam );

		InputElementUIHints uiHints = elementAttrs.getUiHints( );
		if ( uiHints != null )
		{
			InputElementUIHints cachedUiHints = cachedElementAttrs == null
					? null
					: cachedElementAttrs.getUiHints( );
			InputPromptControlStyle style = uiHints.getPromptStyle( );

			InputPromptControlStyle cachedStyle = cachedUiHints == null
					? null
					: cachedUiHints.getPromptStyle( );

			if ( cachedStyle == null
					|| ( style != null && cachedStyle.getValue( ) != style
							.getValue( ) ) )
				reportParam.setControlType( style == null
						? null
						: newROMControlType( style.getValue( ) ) );
		}
	}

	/**
	 * Returns ROM defined control type by given ODA defined prompt style.
	 * 
	 * @param promptStyle
	 *            the ODA defined prompt style
	 * @return the ROM defined control type
	 */

	private static String newROMControlType( int promptStyle )
	{
		switch ( promptStyle )
		{
			case InputPromptControlStyle.CHECK_BOX :
				return DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX;
			case InputPromptControlStyle.SELECTABLE_LIST :
			case InputPromptControlStyle.SELECTABLE_LIST_WITH_TEXT_FIELD :
				return DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;
			case InputPromptControlStyle.RADIO_BUTTON :
				return DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON;
			case InputPromptControlStyle.TEXT_FIELD :
				return DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX;
		}

		return null;
	}

	/**
	 * Updates values in ScalarValueChoices to the given report parameter.
	 * 
	 * @param dataAttrs
	 *            the latest scalar values
	 * @param cachedDataAttrs
	 *            the cached scalar value
	 * @param reportParam
	 *            the report parameter
	 * @throws SemanticException
	 */

	private void updateROMSelectionList( ScalarValueChoices staticChoices,
			ScalarValueChoices cachedStaticChoices,
			ScalarParameterHandle paramHandle ) throws SemanticException
	{
		if ( staticChoices == null )
			return;

		String newChoiceStr = DesignObjectSerializer
				.toExternalForm( staticChoices );
		String latestChoiceStr = DesignObjectSerializer
				.toExternalForm( cachedStaticChoices );

		if ( latestChoiceStr != null && latestChoiceStr.equals( newChoiceStr ) )
			return;

		List retList = new ArrayList( );

		EList choiceList = staticChoices.getScalarValues( );
		for ( int i = 0; i < choiceList.size( ); i++ )
		{
			ScalarValueDefinition valueDefn = (ScalarValueDefinition) choiceList
					.get( i );

			SelectionChoice choice = StructureFactory.createSelectionChoice( );

			choice.setValue( valueDefn.getValue( ) );
			choice.setLabel( valueDefn.getDisplayName( ) );
			retList.add( choice );
		}

		PropertyHandle propHandle = paramHandle
				.getPropertyHandle( ScalarParameterHandle.SELECTION_LIST_PROP );

		propHandle.clearValue( );

		for ( int i = 0; i < retList.size( ); i++ )
		{
			propHandle.addItem( retList.get( i ) );
		}
	}

	/**
	 * Updates values in DynamicValuesQuery to the given report parameter.
	 * 
	 * @param dataAttrs
	 *            the latest dynamic values
	 * @param cachedDataAttrs
	 *            the cached dynamic values
	 * @param reportParam
	 *            the report parameter
	 * @throws SemanticException
	 */

	private void updateROMDyanmicList( DynamicValuesQuery valueQuery,
			DynamicValuesQuery cachedValueQuery,
			ScalarParameterHandle reportParam ) throws SemanticException
	{
		if ( valueQuery == null )
			return;

		String value = valueQuery.getDataSetDesign( ).getName( );
		String cachedValue = cachedValueQuery == null ? null : cachedValueQuery
				.getDataSetDesign( ).getName( );
		if ( cachedValue == null || !cachedValue.equals( value ) )
			reportParam.setDataSetName( value );

		value = valueQuery.getValueColumn( );
		cachedValue = cachedValueQuery == null ? null : cachedValueQuery
				.getValueColumn( );
		if ( cachedValue == null || !cachedValue.equals( value ) )
			reportParam.setValueExpr( value );

		value = valueQuery.getDisplayNameColumn( );
		cachedValue = cachedValueQuery == null ? null : cachedValueQuery
				.getDisplayNameColumn( );
		if ( cachedValue == null || !cachedValue.equals( value ) )
			reportParam.setLabelExpr( value );
	}

	/**
	 * Creates an ParameterDefinition with the given report parameter.
	 * 
	 * @param paramDefn
	 *            the ROM report parameter.
	 * @return the created ParameterDefinition
	 */

	ParameterDefinition updateParameterDefinitionFromReportParam(
			ParameterDefinition paramDefn, ScalarParameterHandle paramHandle,
			DataSetDesign dataSetDesign )
	{

		assert paramHandle != null;
		if ( paramDefn == null )
			return null;

		paramDefn.setAttributes( updateDataElementAttrs( paramDefn
				.getAttributes( ), paramHandle ) );

		paramDefn.setInputAttributes( updateInputElementAttrs( paramDefn
				.getInputAttributes( ), paramHandle, dataSetDesign ) );
		return paramDefn;
	}

	/**
	 * Creates an DataElementAttributes with the given ROM report parameter.
	 * 
	 * @param paramHandle
	 *            the ROM report parameter.
	 * @return the created DataElementAttributes
	 */

	private DataElementAttributes updateDataElementAttrs(
			DataElementAttributes dataAttrs, ScalarParameterHandle paramHandle )
	{
		if ( dataAttrs == null )
			dataAttrs = DesignFactory.eINSTANCE.createDataElementAttributes( );

		dataAttrs.setNullability( DataSetParameterAdapter
				.newElementNullability( paramHandle.allowNull( ) ) );

		DataElementUIHints uiHints = DesignFactory.eINSTANCE
				.createDataElementUIHints( );
		uiHints.setDisplayName( paramHandle.getPromptText( ) );
		uiHints.setDescription( paramHandle.getHelpText( ) );

		dataAttrs.setUiHints( uiHints );

		return dataAttrs;

	}

	/**
	 * Creates a ODA InputParameterAttributes with the given ROM report
	 * parameter.
	 * 
	 * @param paramDefn
	 *            the ROM report parameter.
	 * @param dataSetDesign
	 * 
	 * @return the created <code>InputParameterAttributes</code>.
	 */

	private InputParameterAttributes updateInputElementAttrs(
			InputParameterAttributes inputParamAttrs,
			ScalarParameterHandle paramDefn, DataSetDesign dataSetDesign )
	{
		if ( inputParamAttrs == null )
			inputParamAttrs = DesignFactory.eINSTANCE
					.createInputParameterAttributes( );

		InputElementAttributes inputAttrs = inputParamAttrs
				.getElementAttributes( );
		if ( inputAttrs == null )
			inputAttrs = DesignFactory.eINSTANCE.createInputElementAttributes( );

		inputAttrs.setDefaultScalarValue( paramDefn.getDefaultValue( ) );
		inputAttrs.setOptional( paramDefn.allowBlank( ) );
		inputAttrs.setMasksValue( paramDefn.isConcealValue( ) );

		ScalarValueChoices staticChoices = null;
		Iterator selectionList = paramDefn.choiceIterator( );
		while ( selectionList.hasNext( ) )
		{
			if ( staticChoices == null )
				staticChoices = DesignFactory.eINSTANCE
						.createScalarValueChoices( );
			SelectionChoiceHandle choice = (SelectionChoiceHandle) selectionList
					.next( );

			ScalarValueDefinition valueDefn = DesignFactory.eINSTANCE
					.createScalarValueDefinition( );
			valueDefn.setValue( choice.getValue( ) );
			valueDefn.setDisplayName( choice.getLabel( ) );

			staticChoices.getScalarValues( ).add( valueDefn );
		}
		inputAttrs.setStaticValueChoices( staticChoices );

		DataSetHandle setHandle = paramDefn.getDataSet( );
		String valueExpr = paramDefn.getValueExpr( );
		String labelExpr = paramDefn.getLabelExpr( );

		if ( setHandle instanceof OdaDataSetHandle
				&& ( valueExpr != null || labelExpr != null ) )
		{
			DynamicValuesQuery valueQuery = DesignFactory.eINSTANCE
					.createDynamicValuesQuery( );
			if ( dataSetDesign != null )
			{
				DataSetDesign targetDataSetDesign = (DataSetDesign) EcoreUtil
						.copy( dataSetDesign );
				if ( !setHandle.getName( ).equals( dataSetDesign.getName( ) ) )
					targetDataSetDesign = new ModelOdaAdapter( )
							.createDataSetDesign( (OdaDataSetHandle) setHandle );
				valueQuery.setDataSetDesign( targetDataSetDesign );
			}
			else
			{
				DataSetDesign targetDataSetDesign = new ModelOdaAdapter( )
						.createDataSetDesign( (OdaDataSetHandle) setHandle );
				valueQuery.setDataSetDesign( targetDataSetDesign );
			}
			valueQuery.setDisplayNameColumn( labelExpr );
			valueQuery.setValueColumn( valueExpr );
			valueQuery.setEnabled( true );
			inputAttrs.setDynamicValueChoices( valueQuery );
		}

		InputElementUIHints uiHints = DesignFactory.eINSTANCE
				.createInputElementUIHints( );
		uiHints.setPromptStyle( newPromptStyle( paramDefn.getControlType( ),
				paramDefn.isMustMatch( ) ) );
		inputAttrs.setUiHints( uiHints );

		if ( paramDefn.getContainer( ) instanceof ParameterGroupHandle )
		{
			ParameterGroupHandle groupHandle = (ParameterGroupHandle) paramDefn
					.getContainer( );

			InputParameterUIHints paramUiHints = DesignFactory.eINSTANCE
					.createInputParameterUIHints( );
			paramUiHints.setGroupPromptDisplayName( groupHandle
					.getDisplayName( ) );

			inputParamAttrs.setUiHints( paramUiHints );
		}

		inputParamAttrs.setElementAttributes( inputAttrs );
		return inputParamAttrs;
	}

	/**
	 * Returns the prompty style with the given ROM defined parameter type.
	 * 
	 * @param romType
	 *            the ROM defined parameter type
	 * @param mustMatch
	 *            <code>true</code> if means list box, <code>false</code>
	 *            means combo box.
	 * @return the new InputPromptControlStyle
	 */

	private static InputPromptControlStyle newPromptStyle( String romType,
			boolean mustMatch )
	{
		if ( romType == null )
			return null;

		int type = -1;
		if ( DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX
				.equalsIgnoreCase( romType ) )
			type = InputPromptControlStyle.CHECK_BOX;
		else if ( DesignChoiceConstants.PARAM_CONTROL_LIST_BOX
				.equalsIgnoreCase( romType ) )
		{
			if ( mustMatch )
				type = InputPromptControlStyle.SELECTABLE_LIST;
			else
				type = InputPromptControlStyle.SELECTABLE_LIST_WITH_TEXT_FIELD;
		}
		else if ( DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON
				.equalsIgnoreCase( romType ) )
			type = InputPromptControlStyle.RADIO_BUTTON;
		else if ( DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX
				.equalsIgnoreCase( romType ) )
			type = InputPromptControlStyle.TEXT_FIELD;

		return InputPromptControlStyle.get( type );
	}
}
