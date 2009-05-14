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

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.model.adapter.oda.IODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.ODADesignFactory;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SelectionChoiceHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DynamicValuesQuery;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputPromptControlStyle;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueChoices;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueDefinition;
import org.eclipse.datatools.connectivity.oda.design.StaticValues;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Converts values between a report parameter and ODA Design Session Request.
 * 
 */

class AbstractReportParameterAdapter
{

	/**
	 * Deprecated allowNull property.
	 * 
	 * @deprecated
	 */

	protected static final String ALLOW_NULL_PROP_NAME = IScalarParameterModel.ALLOW_NULL_PROP;

	/**
	 * Deprecated allowBlank property.
	 * 
	 * @deprecated
	 */

	protected static final String ALLOW_BLANK_PROP_NAME = IScalarParameterModel.ALLOW_BLANK_PROP;

	/**
	 * 
	 */

	protected final IODADesignFactory designFactory;

	/**
	 * Default constructor.
	 */

	AbstractReportParameterAdapter( )
	{
		designFactory = ODADesignFactory.getFactory( );
	}

	/**
	 * Updates allowNull property for the given data set parameter definition.
	 * 
	 * @param romParamDefn
	 *            the data set parameter definition.
	 * @param nullability
	 *            the ODA object indicates nullability.
	 * @return <code>true</code> if is nullable. <code>false</code> if not
	 *         nullable.
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
	 * Updates values in report parameter by given ROM data set parameter.
	 * 
	 * @param reportParam
	 *            the report parameter
	 * @param dataSetParam
	 *            the data set parameter
	 * @throws SemanticException
	 */

	protected void updateLinkedReportParameterFromROMParameter(
			AbstractScalarParameterHandle reportParam,
			OdaDataSetParameterHandle dataSetParam ) throws SemanticException
	{
		assert reportParam != null;

		if ( dataSetParam == null )
			return;

		// should not convert report parameter name here.

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
	 * Sets the default value for ROM data set parameter.
	 * 
	 * @param setParam
	 *            the ROM data set parameter
	 * @param literalValue
	 *            the value
	 */

	private void setROMDefaultValue( AbstractScalarParameterHandle setParam,
			String value ) throws SemanticException
	{
		String literalValue = getROMDefaultValueLiteral( setParam, value );

		if ( literalValue != null )
		{
			List<Expression> newValues = new ArrayList<Expression>( );
			newValues.add( new Expression( literalValue,
					ExpressionType.CONSTANT ) );

			setParam.setDefaultValueList( newValues );
		}
	}

	/**
	 * Returns the literal default value for ROM data set parameter.
	 * 
	 * @param setParam
	 *            the ROM data set parameter
	 * @param literalValue
	 *            the value
	 * 
	 * @return the literal default value for ROM data set parameter, or null if
	 *         no default value.
	 */
	protected String getROMDefaultValueLiteral(
			AbstractScalarParameterHandle setParam, String value )
	{
		String literalValue = value;

		if ( literalValue != null )
		{
			boolean match = ExpressionUtil.isScalarParamReference( literalValue );
			if ( match )
				return null;
		}

		if ( DataSetParameterAdapter.BIRT_JS_EXPR.equalsIgnoreCase( literalValue ) )
		{
			return null;
		}
		return literalValue;
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
	 * @param paramDefn
	 *            the ODA parameter definition
	 * @param cachedParamDefn
	 *            the cached ODA parameter definition in designerValues
	 * @param dataType
	 *            the updated data type
	 * @param setHandle
	 *            the ROM data set that has the corresponding data set parameter
	 * @throws SemanticException
	 *             if value in the data set design is invalid
	 */

	void updateLinkedReportParameter(
			AbstractScalarParameterHandle reportParam,
			ParameterDefinition paramDefn, ParameterDefinition cachedParamDefn,
			OdaDataSetHandle setHandle ) throws SemanticException
	{
		if ( paramDefn == null )
			return;

		CommandStack cmdStack = reportParam.getModuleHandle( )
				.getCommandStack( );
		try
		{
			cmdStack.startTrans( null );

			updateAbstractScalarParameter( reportParam,
					paramDefn,
					cachedParamDefn,
					setHandle );

		}
		catch ( SemanticException e )
		{
			cmdStack.rollback( );
			throw e;
		}

		cmdStack.commit( );
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
	 * @param paramDefn
	 *            the ODA parameter definition
	 * @param cachedParamDefn
	 *            the cached ODA parameter definition in designerValues
	 * @param setHandle
	 *            the ROM data set that has the corresponding data set parameter
	 * @throws SemanticException
	 *             if value in the data set design is invalid
	 */
	protected void updateAbstractScalarParameter(
			AbstractScalarParameterHandle reportParam,
			ParameterDefinition paramDefn, ParameterDefinition cachedParamDefn,
			OdaDataSetHandle setHandle ) throws SemanticException
	{
		updateDataElementAttrsToReportParam( paramDefn.getAttributes( ),
				cachedParamDefn == null ? null
						: cachedParamDefn.getAttributes( ),
				reportParam );

		updateInputParameterAttrsToReportParam( paramDefn.getInputAttributes( ),
				cachedParamDefn == null ? null
						: cachedParamDefn.getInputAttributes( ),
				reportParam,
				setHandle );
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

	protected static ParameterDefinition getValidParameterDefinition(
			OdaDataSetParameterHandle param, DataSetParameters odaParams )
	{
		if ( param == null || odaParams == null )
			return null;

		if ( odaParams.getParameterDefinitions( ).isEmpty( ) )
			return null;

		ParameterDefinition matchedParam = DataSetParameterAdapter.findParameterDefinition( odaParams,
				param.getNativeName( ),
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
			AbstractScalarParameterHandle reportParam )
			throws SemanticException
	{

		if ( dataAttrs == null )
			return;

		boolean allowsNull = dataAttrs.allowsNull( );
		if ( cachedDataAttrs == null
				|| cachedDataAttrs.allowsNull( ) != allowsNull )
			setReportParamIsRequired( reportParam,
					ALLOW_NULL_PROP_NAME,
					dataAttrs.allowsNull( ) );

		// reportParam.setAllowNull( dataAttrs.allowsNull( ) );

		DataElementUIHints dataUiHints = dataAttrs.getUiHints( );
		DataElementUIHints cachedDataUiHints = ( cachedDataAttrs == null ? null
				: cachedDataAttrs.getUiHints( ) );
		if ( dataUiHints != null )
		{
			String displayName = dataUiHints.getDisplayName( );
			String cachedDisplayName = cachedDataUiHints == null ? null
					: cachedDataUiHints.getDisplayName( );
			if ( cachedDisplayName == null
					|| !cachedDisplayName.equals( displayName ) )
				reportParam.setPromptText( displayName );

			String description = dataUiHints.getDescription( );
			String cachedDescription = cachedDataUiHints == null ? null
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
	 * @param setHandle
	 *            the ROM data set that has the corresponding data set parameter
	 * @throws SemanticException
	 */

	private void updateInputParameterAttrsToReportParam(
			InputParameterAttributes inputParamAttrs,
			InputParameterAttributes cachedInputParamAttrs,
			AbstractScalarParameterHandle reportParam,
			OdaDataSetHandle setHandle ) throws SemanticException
	{
		if ( inputParamAttrs == null )
			return;

		InputParameterUIHints paramUiHints = inputParamAttrs.getUiHints( );
		if ( paramUiHints != null
				&& reportParam.getContainer( ) instanceof ParameterGroupHandle )
		{
			ParameterGroupHandle paramGroup = (ParameterGroupHandle) reportParam.getContainer( );

			InputParameterUIHints cachedParamUiHints = cachedInputParamAttrs == null ? null
					: cachedInputParamAttrs.getUiHints( );

			String cachedGroupPromptDisplayName = cachedParamUiHints == null ? null
					: cachedParamUiHints.getGroupPromptDisplayName( );

			String groupPromptDisplayName = paramUiHints.getGroupPromptDisplayName( );

			if ( cachedGroupPromptDisplayName == null
					|| !cachedGroupPromptDisplayName.equals( groupPromptDisplayName ) )
				paramGroup.setDisplayName( paramUiHints.getGroupPromptDisplayName( ) );
		}

		updateInputElementAttrsToReportParam( inputParamAttrs.getElementAttributes( ),
				cachedInputParamAttrs == null ? null
						: cachedInputParamAttrs.getElementAttributes( ),
				reportParam,
				setHandle );
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
	 * @param setHandle
	 *            the ROM data set that has the corresponding data set parameter
	 * @throws SemanticException
	 */

	protected void updateInputElementAttrsToReportParam(
			InputElementAttributes elementAttrs,
			InputElementAttributes cachedElementAttrs,
			AbstractScalarParameterHandle reportParam,
			OdaDataSetHandle setHandle ) throws SemanticException
	{
		if ( elementAttrs == null )
			return;

		// update default values.

		StaticValues defaultValues = elementAttrs.getDefaultValues( );
		StaticValues cachedDefaultValues = cachedElementAttrs == null ? null
				: cachedElementAttrs.getDefaultValues( );

		if ( new EcoreUtil.EqualityHelper( ).equals( cachedDefaultValues,
				defaultValues ) == false )
		{
			List<String> newValues = null;

			if ( defaultValues != null )
			{
				newValues = new ArrayList<String>( );
				List<Object> tmpValues = defaultValues.getValues( );

				for ( int i = 0; i < tmpValues.size( ); i++ )
				{
					String tmpValue = (String) tmpValues.get( i );

					// only update when the value is not internal value.

					if ( !DataSetParameterAdapter.BIRT_JS_EXPR.equals( tmpValue ) )
						newValues.add( tmpValue );
				}
			}

			reportParam.setDefaultValueList( newValues );
		}

		// update isOptional value

		Boolean isOptional = Boolean.valueOf( elementAttrs.isOptional( ) );
		Boolean cachedIsOptional = cachedElementAttrs == null ? null
				: Boolean.valueOf( cachedElementAttrs.isOptional( ) );
		if ( !CompareUtil.isEquals( cachedIsOptional, isOptional ) )
			setReportParamIsRequired( reportParam,
					ALLOW_BLANK_PROP_NAME,
					isOptional.booleanValue( ) );

		// reportParam.setAllowBlank( isOptional.booleanValue( ) );

		updateROMSelectionList( elementAttrs.getStaticValueChoices( ),
				cachedElementAttrs == null ? null
						: cachedElementAttrs.getStaticValueChoices( ),
				reportParam );

		DynamicValuesQuery valueQuery = elementAttrs.getDynamicValueChoices( );
		updateROMDyanmicList( valueQuery,
				cachedElementAttrs == null ? null
						: cachedElementAttrs.getDynamicValueChoices( ),
				reportParam,
				setHandle );

		// for both dynamic and static parameter, the flag is in
		// DynamicValuesQuery

		DynamicValuesQuery cachedValueQuery = cachedElementAttrs == null ? null
				: cachedElementAttrs.getDynamicValueChoices( );

		if ( valueQuery == null && cachedValueQuery == null )
			return;

		// please note that new dynamic values query's isEnabled flag is true

		if ( valueQuery == null )
			valueQuery = designFactory.createDynamicValuesQuery( );

		boolean isEnabled = valueQuery.isEnabled( );
		boolean cachedIsEnabled = cachedValueQuery == null ? false
				: cachedValueQuery.isEnabled( );
		if ( ( cachedValueQuery == null || cachedIsEnabled != isEnabled )
				&& isEnabled )
			reportParam.setValueType( DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC );
		else if ( ( cachedValueQuery == null || cachedIsEnabled != isEnabled )
				&& !isEnabled )
			reportParam.setValueType( DesignChoiceConstants.PARAM_VALUE_TYPE_STATIC );

	}

	/**
	 * Returns ROM defined control type by given ODA defined prompt style.
	 * 
	 * @param promptStyle
	 *            the ODA defined prompt style
	 * @return the ROM defined control type
	 */

	protected static String newROMControlType( int promptStyle )
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
			AbstractScalarParameterHandle paramHandle )
			throws SemanticException
	{
		if ( staticChoices == null )
			return;

		String newChoiceStr = DesignObjectSerializer.toExternalForm( staticChoices );
		String latestChoiceStr = DesignObjectSerializer.toExternalForm( cachedStaticChoices );

		if ( latestChoiceStr != null && latestChoiceStr.equals( newChoiceStr ) )
			return;

		List retList = new ArrayList( );

		EList choiceList = staticChoices.getScalarValues( );
		for ( int i = 0; i < choiceList.size( ); i++ )
		{
			ScalarValueDefinition valueDefn = (ScalarValueDefinition) choiceList.get( i );

			SelectionChoice choice = StructureFactory.createSelectionChoice( );

			choice.setValue( valueDefn.getValue( ) );
			choice.setLabel( valueDefn.getDisplayName( ) );
			retList.add( choice );
		}

		PropertyHandle propHandle = paramHandle.getPropertyHandle( AbstractScalarParameterHandle.SELECTION_LIST_PROP );

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
			AbstractScalarParameterHandle reportParam,
			OdaDataSetHandle setHandle ) throws SemanticException
	{
		if ( valueQuery == null )
			return;

		String value = valueQuery.getDataSetDesign( ).getName( );
		String cachedValue = cachedValueQuery == null ? null
				: cachedValueQuery.getDataSetDesign( ).getName( );
		if ( cachedValue == null || !cachedValue.equals( value ) )
		{

			reportParam.setDataSetName( value );

			// update the data set instance. To avoid recursivly convert,
			// compare set handle instances.

			ModuleHandle module = setHandle.getModuleHandle( );
			DataSetHandle target = module.findDataSet( value );
			if ( target instanceof OdaDataSetHandle && target != setHandle )
				new ModelOdaAdapter( ).updateDataSetHandle( valueQuery.getDataSetDesign( ),
						(OdaDataSetHandle) target,
						false );

			// if there is no corresponding data set, creates a new one.

			if ( target == null )
			{
				OdaDataSetHandle nestedDataSet = new ModelOdaAdapter( ).createDataSetHandle( valueQuery.getDataSetDesign( ),
						module );
				module.getDataSets( ).add( nestedDataSet );
			}
		}

		value = valueQuery.getValueColumn( );
		cachedValue = cachedValueQuery == null ? null
				: cachedValueQuery.getValueColumn( );
		if ( cachedValue == null || !cachedValue.equals( value ) )
			reportParam.setValueExpr( value );

		value = valueQuery.getDisplayNameColumn( );
		cachedValue = cachedValueQuery == null ? null
				: cachedValueQuery.getDisplayNameColumn( );
		if ( cachedValue == null || !cachedValue.equals( value ) )
			reportParam.setLabelExpr( value );
	}

	/**
	 * Creates an ParameterDefinition with the given report parameter.
	 * 
	 * @param paramDefn
	 *            the ROM report parameter.
	 * @param paramHandle
	 *            the report parameter
	 * @param dataSetDesign
	 *            the data set design
	 * @return the created ParameterDefinition
	 */

	ParameterDefinition updateParameterDefinitionFromReportParam(
			ParameterDefinition paramDefn,
			AbstractScalarParameterHandle paramHandle,
			DataSetDesign dataSetDesign )
	{

		assert paramHandle != null;
		if ( paramDefn == null )
			return null;

		paramDefn.setAttributes( updateDataElementAttrs( paramDefn.getAttributes( ),
				paramHandle ) );

		paramDefn.setInputAttributes( updateInputElementAttrs( paramDefn.getInputAttributes( ),
				paramHandle,
				dataSetDesign ) );
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
			DataElementAttributes dataAttrs,
			AbstractScalarParameterHandle paramHandle )
	{
		DataElementAttributes retDataAttrs = dataAttrs;

		if ( retDataAttrs == null )
			retDataAttrs = designFactory.createDataElementAttributes( );

		// retDataAttrs.setNullability( DataSetParameterAdapter
		// .newElementNullability( paramHandle.allowNll( ) ) );

		retDataAttrs.setNullability( DataSetParameterAdapter.newElementNullability( getReportParamAllowMumble( paramHandle,
				ALLOW_NULL_PROP_NAME ) ) );

		DataElementUIHints uiHints = designFactory.createDataElementUIHints( );
		uiHints.setDisplayName( paramHandle.getPromptText( ) );
		uiHints.setDescription( paramHandle.getHelpText( ) );

		retDataAttrs.setUiHints( uiHints );

		return retDataAttrs;

	}

	/**
	 * Creates a ODA InputParameterAttributes with the given ROM report
	 * parameter.
	 * 
	 * @param paramHandle
	 *            the ROM report parameter.
	 * @param dataSetDesign
	 * 
	 * @return the created <code>InputParameterAttributes</code>.
	 */

	protected InputParameterAttributes updateInputElementAttrs(
			InputParameterAttributes inputParamAttrs,
			AbstractScalarParameterHandle paramHandle,
			DataSetDesign dataSetDesign )
	{
		InputParameterAttributes retInputParamAttrs = inputParamAttrs;

		if ( inputParamAttrs == null )
			retInputParamAttrs = designFactory.createInputParameterAttributes( );

		InputElementAttributes inputAttrs = retInputParamAttrs.getElementAttributes( );
		if ( inputAttrs == null )
			inputAttrs = designFactory.createInputElementAttributes( );

		StaticValues newValues = null;
		List<Expression> tmpValues = paramHandle.getDefaultValueList( );
		if ( tmpValues != null )
		{
			for ( int i = 0; i < tmpValues.size( ); i++ )
			{
				if ( newValues == null )
					newValues = designFactory.createStaticValues( );
				newValues.add( tmpValues.get( i ).getStringExpression( ) );
			}
		}
		inputAttrs.setDefaultValues( newValues );

		// inputAttrs.setOptional( paramHandle.allowBlank( ) );
		inputAttrs.setOptional( getReportParamAllowMumble( paramHandle,
				ALLOW_BLANK_PROP_NAME ) );

		ScalarValueChoices staticChoices = null;
		Iterator selectionList = paramHandle.choiceIterator( );
		while ( selectionList.hasNext( ) )
		{
			if ( staticChoices == null )
				staticChoices = designFactory.createScalarValueChoices( );
			SelectionChoiceHandle choice = (SelectionChoiceHandle) selectionList.next( );

			ScalarValueDefinition valueDefn = designFactory.createScalarValueDefinition( );
			valueDefn.setValue( choice.getValue( ) );
			valueDefn.setDisplayName( choice.getLabel( ) );

			staticChoices.getScalarValues( ).add( valueDefn );
		}
		inputAttrs.setStaticValueChoices( staticChoices );

		DataSetHandle setHandle = paramHandle.getDataSet( );
		String valueExpr = paramHandle.getValueExpr( );
		String labelExpr = paramHandle.getLabelExpr( );

		if ( setHandle instanceof OdaDataSetHandle && valueExpr != null )
		{
			DynamicValuesQuery valueQuery = designFactory.createDynamicValuesQuery( );

			if ( dataSetDesign != null )
			{
				DataSetDesign targetDataSetDesign = (DataSetDesign) EcoreUtil.copy( dataSetDesign );
				if ( !setHandle.getName( ).equals( dataSetDesign.getName( ) ) )
					targetDataSetDesign = new ModelOdaAdapter( ).createDataSetDesign( (OdaDataSetHandle) setHandle );
				valueQuery.setDataSetDesign( targetDataSetDesign );
			}
			else
			{
				DataSetDesign targetDataSetDesign = new ModelOdaAdapter( ).createDataSetDesign( (OdaDataSetHandle) setHandle );
				valueQuery.setDataSetDesign( targetDataSetDesign );
			}
			valueQuery.setDisplayNameColumn( labelExpr );
			valueQuery.setValueColumn( valueExpr );

			boolean isEnabled = DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC.equalsIgnoreCase( paramHandle.getValueType( ) );

			valueQuery.setEnabled( isEnabled );
			inputAttrs.setDynamicValueChoices( valueQuery );
		}

		if ( paramHandle.getContainer( ) instanceof ParameterGroupHandle )
		{
			ParameterGroupHandle groupHandle = (ParameterGroupHandle) paramHandle.getContainer( );

			InputParameterUIHints paramUiHints = designFactory.createInputParameterUIHints( );
			paramUiHints.setGroupPromptDisplayName( groupHandle.getDisplayName( ) );

			retInputParamAttrs.setUiHints( paramUiHints );
		}

		retInputParamAttrs.setElementAttributes( inputAttrs );
		return retInputParamAttrs;
	}

	/**
	 * Returns the prompty style with the given ROM defined parameter type.
	 * 
	 * @param romType
	 *            the ROM defined parameter type
	 * @param mustMatch
	 *            <code>true</code> if means list box, <code>false</code> means
	 *            combo box.
	 * @return the new InputPromptControlStyle
	 */

	protected static InputPromptControlStyle newPromptStyle( String romType,
			boolean mustMatch )
	{
		if ( romType == null )
			return null;

		int type = -1;
		if ( DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX.equalsIgnoreCase( romType ) )
			type = InputPromptControlStyle.CHECK_BOX;
		else if ( DesignChoiceConstants.PARAM_CONTROL_LIST_BOX.equalsIgnoreCase( romType ) )
		{
			if ( mustMatch )
				type = InputPromptControlStyle.SELECTABLE_LIST;
			else
				type = InputPromptControlStyle.SELECTABLE_LIST_WITH_TEXT_FIELD;
		}
		else if ( DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equalsIgnoreCase( romType ) )
			type = InputPromptControlStyle.RADIO_BUTTON;
		else if ( DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equalsIgnoreCase( romType ) )
			type = InputPromptControlStyle.TEXT_FIELD;

		return InputPromptControlStyle.get( type );
	}

	/**
	 * Returns the boolean value of allowMumble properties. Only support
	 * "allowNull" and "allowBlank" properties.
	 * <p>
	 * "allowMumble" properties has been removed ROM. However, to do conversion,
	 * still need to know their values.
	 * 
	 * @param param
	 *            the parameter
	 * @param propName
	 *            either "allowNull" or "allowBlank".
	 * @return <code>true</code> if the parameter allows the value. Otherwise
	 *         <code>false</code>.
	 */

	protected boolean getReportParamAllowMumble(
			AbstractScalarParameterHandle param, String propName )
	{
		if ( ALLOW_NULL_PROP_NAME.equalsIgnoreCase( propName )
				|| ALLOW_BLANK_PROP_NAME.equalsIgnoreCase( propName ) )
			return !param.isRequired( );
		else
		{
			assert false;
			return false;
		}
	}

	/**
	 * Returns the boolean value of allowMumble properties. Only support
	 * "allowNull" and "allowBlank" properties.
	 * <p>
	 * "allowMumble" properties has been removed ROM. However, to do conversion,
	 * still need to know their values.
	 * 
	 * @param param
	 *            the parameter
	 * @param obsoletePropName
	 *            either "allowNull" or "allowBlank".
	 */

	protected void setReportParamIsRequired(
			AbstractScalarParameterHandle param, String obsoletePropName,
			boolean value ) throws SemanticException
	{
		if ( ALLOW_NULL_PROP_NAME.equalsIgnoreCase( obsoletePropName )
				|| ALLOW_BLANK_PROP_NAME.equalsIgnoreCase( obsoletePropName ) )
			param.setIsRequired( !value );
		else
			assert false;
	}
}
