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
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.util.ParameterValueUtil;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.datatools.connectivity.oda.design.DynamicValuesQuery;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputPromptControlStyle;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueChoices;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueDefinition;
import org.eclipse.datatools.connectivity.oda.design.StaticValues;
import org.eclipse.emf.common.util.EList;

/**
 * Utility class to provide some method used for adapter classes.
 * 
 */
public class AdapterUtil
{

	/**
	 * Creates a ODA ParameterMode with the given parameter input/output flags.
	 * 
	 * @param isInput
	 *            the parameter is inputable.
	 * @param isOutput
	 *            the parameter is outputable
	 * @return the created <code>ParameterMode</code>.
	 */

	static ParameterMode newParameterMode( boolean isInput, boolean isOutput )
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
	 * Returns the prompty style with the given ROM defined parameter type.
	 * 
	 * @param controlType
	 *            the ROM defined parameter type
	 * @param mustMatch
	 *            <code>true</code> if means list box, <code>false</code> means
	 *            combo box.
	 * @return the new InputPromptControlStyle
	 */

	static InputPromptControlStyle newPromptStyle( String controlType,
			boolean mustMatch )
	{
		if ( controlType == null )
			return null;

		int type = -1;
		if ( DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX
				.equalsIgnoreCase( controlType ) )
			type = InputPromptControlStyle.CHECK_BOX;
		else if ( DesignChoiceConstants.PARAM_CONTROL_LIST_BOX
				.equalsIgnoreCase( controlType ) )
		{
			if ( mustMatch )
				type = InputPromptControlStyle.SELECTABLE_LIST;
			else
				type = InputPromptControlStyle.SELECTABLE_LIST_WITH_TEXT_FIELD;
		}
		else if ( DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON
				.equalsIgnoreCase( controlType ) )
			type = InputPromptControlStyle.RADIO_BUTTON;
		else if ( DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX
				.equalsIgnoreCase( controlType ) )
			type = InputPromptControlStyle.TEXT_FIELD;

		return InputPromptControlStyle.get( type );
	}

	/**
	 * Returns ROM defined control type by given ODA defined prompt style.
	 * 
	 * @param promptStyle
	 *            the ODA defined prompt style
	 * @return the ROM defined control type
	 */

	static String newROMControlType( InputPromptControlStyle style )
	{
		if ( style == null )
			return null;
		switch ( style.getValue( ) )
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
	 * Sets the default value for ROM data set parameter. Should add quotes for
	 * the value if the data type is string.
	 * 
	 * @param setParam
	 *            the ROM data set parameter
	 * @param literalValue
	 *            the value
	 */

	static String getROMDefaultValue( DataSetParameter setParam,
			String literalValue )
	{
		if ( DataSetParameterUpdater.BIRT_JS_EXPR
				.equalsIgnoreCase( literalValue ) )
		{
			return null;
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
		return romDefaultValue;
	}

	/**
	 * Updates the static values to report parameter handle.
	 * 
	 * @param defaultValues
	 * @param reportParam
	 * @throws SemanticException
	 */
	static void updateROMDefaultValues( StaticValues defaultValues,
			AbstractScalarParameterHandle reportParam )
			throws SemanticException
	{
		if ( defaultValues == null || reportParam == null )
			return;

		List<Expression> newValues = null;

		if ( defaultValues != null )
		{
			newValues = new ArrayList<Expression>( );
			List<Object> tmpValues = defaultValues.getValues( );

			for ( int i = 0; i < tmpValues.size( ); i++ )
			{
				String tmpValue = (String) tmpValues.get( i );

				// only update when the value is not internal value.

				if ( !DataSetParameterAdapter.BIRT_JS_EXPR.equals( tmpValue ) )
					newValues.add( new Expression( tmpValue,
							ExpressionType.CONSTANT ) );
			}
		}

		reportParam.setDefaultValueList( newValues );
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

	static void updateROMSelectionList( ScalarValueChoices staticChoices,
			AbstractScalarParameterHandle paramHandle )
			throws SemanticException
	{
		if ( staticChoices == null || paramHandle == null )
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
				.getPropertyHandle( AbstractScalarParameterHandle.SELECTION_LIST_PROP );

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

	static void updateROMDyanmicList( DynamicValuesQuery valueQuery,
			DynamicValuesQuery cachedValueQuery,
			AbstractScalarParameterHandle reportParam,
			OdaDataSetHandle setHandle ) throws SemanticException
	{
		if ( valueQuery == null )
			return;

		String value = valueQuery.getDataSetDesign( ).getName( );
		String cachedValue = cachedValueQuery == null ? null : cachedValueQuery
				.getDataSetDesign( ).getName( );
		if ( cachedValue == null || !cachedValue.equals( value ) )
		{

			reportParam.setDataSetName( value );

			// update the data set instance. To avoid recursively convert,
			// compare set handle instances.

			ModuleHandle module = setHandle.getModuleHandle( );
			DataSetHandle target = module.findDataSet( value );
			if ( target instanceof OdaDataSetHandle && target != setHandle )
				new ModelOdaAdapter( ).updateDataSetHandle( valueQuery
						.getDataSetDesign( ), (OdaDataSetHandle) target, false );

			// if there is no corresponding data set, creates a new one.

			if ( target == null )
			{
				OdaDataSetHandle nestedDataSet = new ModelOdaAdapter( )
						.createDataSetHandle( valueQuery.getDataSetDesign( ),
								module );
				module.getDataSets( ).add( nestedDataSet );
			}
		}

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

}
