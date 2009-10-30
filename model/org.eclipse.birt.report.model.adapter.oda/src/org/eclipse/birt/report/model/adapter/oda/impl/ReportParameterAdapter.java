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

import org.eclipse.birt.report.model.adapter.oda.IReportParameterAdapter;
import org.eclipse.birt.report.model.adapter.oda.util.ParameterValueUtil;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DynamicValuesQuery;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputPromptControlStyle;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Converts values between a report scalar parameter and ODA Design Session
 * Request.
 * 
 */

class ReportParameterAdapter extends AbstractReportParameterAdapter
		implements
			IReportParameterAdapter
{

	/**
	 * The data type of the scalar parameter
	 */
	private String dataType;

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IReportParameterAdapter #
	 * updateLinkedReportParameter
	 * (org.eclipse.birt.report.model.api.ScalarParameterHandle,
	 * org.eclipse.birt.report.model.api.OdaDataSetParameterHandle)
	 */
	public void updateLinkedReportParameter( ScalarParameterHandle reportParam,
			OdaDataSetParameterHandle dataSetParam ) throws SemanticException
	{
		if ( reportParam == null || dataSetParam == null )
			return;

		updateLinkedReportParameterFromROMParameter( reportParam, dataSetParam );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IReportParameterAdapter#
	 * updateLinkedReportParameter
	 * (org.eclipse.birt.report.model.api.ScalarParameterHandle,
	 * org.eclipse.birt.report.model.api.OdaDataSetParameterHandle,
	 * org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
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
			if ( matchedParam != null )
				updateLinkedReportParameter( reportParam, matchedParam, null,
						dataType, (OdaDataSetHandle) dataSetParam
								.getElementHandle( ) );

			updateLinkedReportParameterFromROMParameter( reportParam,
					dataSetParam );
		}
		catch ( SemanticException e )
		{
			cmdStack.rollback( );
			throw e;
		}

		cmdStack.commit( );
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
	 * @param odaParam
	 *            the ODA parameter definition
	 * @param newDataType
	 *            the data type
	 * 
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
		Boolean odaAllowNull = AdapterUtil.getROMNullability( dataAttrs
				.getNullability( ) );
		boolean allowNull = getReportParamAllowMumble( reportParam,
				ALLOW_NULL_PROP_NAME );

		if ( odaAllowNull != null && allowNull != odaAllowNull.booleanValue( ) )
			return false;

		if ( !DesignChoiceConstants.PARAM_TYPE_ANY
				.equalsIgnoreCase( newDataType ) )
		{
			if ( !CompareUtil
					.isEquals( newDataType, reportParam.getDataType( ) ) )
				return false;
		}

		DataElementUIHints dataUiHints = dataAttrs.getUiHints( );
		if ( dataUiHints != null )
		{
			String newPromptText = dataUiHints.getDisplayName( );
			String newHelpText = dataUiHints.getDescription( );

			if ( !CompareUtil.isEquals( newPromptText, reportParam
					.getPromptText( ) ) )
				return false;

			if ( !CompareUtil
					.isEquals( newHelpText, reportParam.getHelpText( ) ) )
				return false;
		}

		InputParameterAttributes paramAttrs = odaParam.getInputAttributes( );
		InputParameterAttributes tmpParamDefn = null;
		DataSetDesign tmpDataSet = null;

		if ( paramAttrs != null )
		{
			tmpParamDefn = (InputParameterAttributes) EcoreUtil
					.copy( paramAttrs );

			DynamicValuesQuery tmpDynamicQuery = tmpParamDefn
					.getElementAttributes( ).getDynamicValueChoices( );

			if ( tmpDynamicQuery != null )
			{
				tmpDataSet = tmpDynamicQuery.getDataSetDesign( );
				tmpDynamicQuery.setDataSetDesign( null );
			}

			if ( tmpParamDefn.getUiHints( ) != null )
			{
				tmpParamDefn.setUiHints( null );
			}
		}
		else
			tmpParamDefn = designFactory.createInputParameterAttributes( );

		InputParameterAttributes tmpParamDefn1 = designFactory
				.createInputParameterAttributes( );

		updateInputElementAttrs( tmpParamDefn1, reportParam, null );
		if ( tmpParamDefn1.getUiHints( ) != null )
		{
			tmpParamDefn1.setUiHints( null );
		}
		DynamicValuesQuery tmpDynamicQuery1 = tmpParamDefn1
				.getElementAttributes( ).getDynamicValueChoices( );
		DataSetDesign tmpDataSet1 = null;
		if ( tmpDynamicQuery1 != null )
		{
			tmpDataSet1 = tmpDynamicQuery1.getDataSetDesign( );
			tmpDynamicQuery1.setDataSetDesign( null );
		}

		if ( !EcoreUtil.equals( tmpDataSet, tmpDataSet1 ) )
			return false;

		return EcoreUtil.equals( tmpParamDefn, tmpParamDefn1 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #
	 * updateLinkedReportParameterFromROMParameter(org.eclipse.birt.report.model
	 * .api.AbstractScalarParameterHandle,
	 * org.eclipse.birt.report.model.api.OdaDataSetParameterHandle)
	 */

	protected void updateLinkedReportParameterFromROMParameter(
			AbstractScalarParameterHandle reportParam,
			OdaDataSetParameterHandle dataSetParam ) throws SemanticException
	{
		assert reportParam != null
				&& reportParam instanceof ScalarParameterHandle;

		ScalarParameterHandle scalarParam = (ScalarParameterHandle) reportParam;

		String dataType = dataSetParam.getParameterDataType( );
		if ( !StringUtil.isBlank( dataType ) )
		{

			if ( !DesignChoiceConstants.PARAM_TYPE_ANY
					.equalsIgnoreCase( dataType ) )
			{
				scalarParam.setDataType( dataType );
			}
			else
			{
				scalarParam
						.setDataType( DesignChoiceConstants.PARAM_TYPE_STRING );
			}
		}
		super.updateLinkedReportParameterFromROMParameter( reportParam,
				dataSetParam );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #updateInputElementAttrs(org.eclipse.datatools.connectivity.oda.design.
	 * InputParameterAttributes,
	 * org.eclipse.birt.report.model.api.AbstractScalarParameterHandle,
	 * org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */
	protected InputParameterAttributes updateInputElementAttrs(
			InputParameterAttributes inputParamAttrs,
			AbstractScalarParameterHandle paramHandle,
			DataSetDesign dataSetDesign )
	{
		assert paramHandle instanceof ScalarParameterHandle;
		ScalarParameterHandle scalarParam = (ScalarParameterHandle) paramHandle;

		InputParameterAttributes retInputParamAttrs = super
				.updateInputElementAttrs( inputParamAttrs, paramHandle,
						dataSetDesign );
		InputElementAttributes inputAttrs = retInputParamAttrs
				.getElementAttributes( );
		inputAttrs.setMasksValue( scalarParam.isConcealValue( ) );

		InputElementUIHints uiHints = designFactory.createInputElementUIHints( );
		uiHints.setPromptStyle( AdapterUtil.newPromptStyle( scalarParam
				.getControlType( ), scalarParam.isMustMatch( ) ) );

		// not set the ROM default value on ODA objects.

		PropertyHandle tmpPropHandle = paramHandle
				.getPropertyHandle( ScalarParameterHandle.AUTO_SUGGEST_THRESHOLD_PROP );
		if ( tmpPropHandle.isSet( ) )
			uiHints.setAutoSuggestThreshold( scalarParam
					.getAutoSuggestThreshold( ) );
		inputAttrs.setUiHints( uiHints );

		return retInputParamAttrs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #setReportParamIsRequired(org.eclipse.birt.report.model.api.
	 * AbstractScalarParameterHandle, java.lang.String, boolean)
	 */
	protected void setReportParamIsRequired(
			AbstractScalarParameterHandle param, String obsoletePropName,
			boolean value ) throws SemanticException
	{
		assert param instanceof ScalarParameterHandle;
		if ( ALLOW_NULL_PROP_NAME.equalsIgnoreCase( obsoletePropName ) )
			( (ScalarParameterHandle) param ).setAllowNull( value );
		else if ( ALLOW_BLANK_PROP_NAME.equalsIgnoreCase( obsoletePropName ) )
			( (ScalarParameterHandle) param ).setAllowBlank( value );
		else
			super.setReportParamIsRequired( param, obsoletePropName, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #getReportParamAllowMumble(org.eclipse.birt.report.model.api.
	 * AbstractScalarParameterHandle, java.lang.String)
	 */
	protected boolean getReportParamAllowMumble(
			AbstractScalarParameterHandle param, String propName )
	{
		assert param instanceof ScalarParameterHandle;
		if ( ALLOW_NULL_PROP_NAME.equalsIgnoreCase( propName ) )
			return ( (ScalarParameterHandle) param ).allowNull( );
		else if ( ALLOW_BLANK_PROP_NAME.equalsIgnoreCase( propName ) )
			return ( (ScalarParameterHandle) param ).allowBlank( );
		return super.getReportParamAllowMumble( param, propName );
	}

	/**
	 * Returns the literal default value for ROM data set parameter.Should add
	 * quotes for the value if the data type is string.
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
		assert setParam instanceof ScalarParameterHandle;

		String literalValue = super.getROMDefaultValueLiteral( setParam, value );

		if ( literalValue != null
				&& AdapterUtil
						.needsQuoteDelimiters( ( (ScalarParameterHandle) setParam )
								.getDataType( ) ) )
		{
			if ( ParameterValueUtil.isQuoted( value ) )
			{
				literalValue = ParameterValueUtil.toLiteralValue( value );
			}
			else
				literalValue = null;
		}
		return literalValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #updateLinkedReportParameter(org.eclipse.birt.report.model.api.
	 * AbstractScalarParameterHandle,
	 * org.eclipse.datatools.connectivity.oda.design.ParameterDefinition,
	 * org.eclipse.datatools.connectivity.oda.design.ParameterDefinition,
	 * java.lang.String, org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */
	void updateLinkedReportParameter( ScalarParameterHandle reportParam,
			ParameterDefinition paramDefn, ParameterDefinition cachedParamDefn,
			String dataType, OdaDataSetHandle setHandle )
			throws SemanticException
	{

		if ( isUpdatedReportParameter( reportParam, paramDefn, dataType ) )
		{
			return;
		}
		this.dataType = dataType;

		updateLinkedReportParameter( reportParam, paramDefn, cachedParamDefn,
				setHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #updateAbstractScalarParameter(org.eclipse.birt.report.model.api.
	 * AbstractScalarParameterHandle,
	 * org.eclipse.datatools.connectivity.oda.design.ParameterDefinition,
	 * org.eclipse.datatools.connectivity.oda.design.ParameterDefinition,
	 * org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */
	protected void updateAbstractScalarParameter(
			AbstractScalarParameterHandle reportParam,
			ParameterDefinition paramDefn, ParameterDefinition cachedParamDefn,
			OdaDataSetHandle setHandle ) throws SemanticException
	{
		assert reportParam instanceof ScalarParameterHandle;
		// any type is not support in report parameter data type.

		if ( dataType == null )
		{
			if ( !DesignChoiceConstants.PARAM_TYPE_ANY
					.equalsIgnoreCase( dataType ) )
			{
				( (ScalarParameterHandle) reportParam ).setDataType( dataType );
			}
			else
			{
				( (ScalarParameterHandle) reportParam )
						.setDataType( DesignChoiceConstants.PARAM_TYPE_STRING );
			}
		}
		super.updateAbstractScalarParameter( reportParam, paramDefn,
				cachedParamDefn, setHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #
	 * updateInputElementAttrsToReportParam(org.eclipse.datatools.connectivity.
	 * oda .design.InputElementAttributes,
	 * org.eclipse.datatools.connectivity.oda.design.InputElementAttributes,
	 * org.eclipse.birt.report.model.api.AbstractScalarParameterHandle,
	 * org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */
	protected void updateInputElementAttrsToReportParam(
			InputElementAttributes elementAttrs,
			InputElementAttributes cachedElementAttrs,
			AbstractScalarParameterHandle reportParam,
			OdaDataSetHandle setHandle ) throws SemanticException
	{
		assert reportParam instanceof ScalarParameterHandle;

		// update conceal value

		Boolean masksValue = Boolean.valueOf( elementAttrs.isMasksValue( ) );
		Boolean cachedMasksValues = cachedElementAttrs == null ? null : Boolean
				.valueOf( cachedElementAttrs.isMasksValue( ) );

		if ( !CompareUtil.isEquals( cachedMasksValues, masksValue ) )
			( (ScalarParameterHandle) reportParam ).setConcealValue( masksValue
					.booleanValue( ) );

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
				( (ScalarParameterHandle) reportParam )
						.setControlType( style == null ? null : AdapterUtil
								.newROMControlType( style ) );

			( (ScalarParameterHandle) reportParam )
					.setAutoSuggestThreshold( uiHints.getAutoSuggestThreshold( ) );
		}

		super.updateInputElementAttrsToReportParam( elementAttrs,
				cachedElementAttrs, reportParam, setHandle );
	}

}
