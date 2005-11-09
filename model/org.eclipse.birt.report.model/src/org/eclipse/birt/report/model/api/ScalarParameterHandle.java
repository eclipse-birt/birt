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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Represents a scalar (single-value) report parameter. If the user enters no
 * value for a parameter, then the default value is used. If there is no default
 * value, then BIRT checks if <code>null</code> is allowed. If so, the value
 * of the parameter is null. If nulls are not allowed, then the user must enter
 * a value.
 * <p>
 * Scalar parameters can have static or dynamic selection lists.
 * <ul>
 * <li>The parameter static selection list provides a developer-defined list of
 * choices. Every choice has two parts: a choice and a label. The label can be
 * externalized and appears in the UI. The choice is the value passed to the
 * report.
 * <li>This parameter can define a dynamic selection list for the parameter.
 * The data set can reference other parameters by referring to a data set. The
 * data set must return a column that contains the choice values. It may also
 * contain a column that returns the labels for the values. All other columns
 * are ignored.
 * </ul>
 * <p>
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.ScalarParameter
 * @see ParameterHandle
 */

public class ScalarParameterHandle extends ParameterHandle
		implements
			IScalarParameterModel
{

	/**
	 * Constructs a handle for the ScalarParamter with the given design and the
	 * parameter. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public ScalarParameterHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the data type for this parameter. The data type controls how the
	 * requester formats, parses and validates the parameter. Types are defined
	 * in <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_STRING</code>
	 * <li><code>PARAM_TYPE_FLOAT</code>
	 * <li><code>PARAM_TYPE_DECIMAL</code>
	 * <li><code>PARAM_TYPE_DATETYPE</code>
	 * <li><code>PARAM_TYPE_BOOLEAN</code>
	 * </ul>
	 * 
	 * @return the type for the parameter
	 * 
	 * @see #setDataType(String)
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getDataType( )
	{
		return getStringProperty( ScalarParameter.DATA_TYPE_PROP );
	}

	/**
	 * Sets the data type for this parameter. The data type controls how the
	 * Requester formats, parses and validates the parameter. Types are defined
	 * in <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_STRING</code>
	 * <li><code>PARAM_TYPE_FLOAT</code>
	 * <li><code>PARAM_TYPE_DECIMAL</code>
	 * <li><code>PARAM_TYPE_DATETYPE</code>
	 * <li><code>PARAM_TYPE_BOOLEAN</code>
	 * </ul>
	 * 
	 * @param type
	 *            the type for the parameter
	 * 
	 * @throws SemanticException
	 *             if the input type is not one of above choices.
	 * @see #getDataType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public void setDataType( String type ) throws SemanticException
	{
		setStringProperty( ScalarParameter.DATA_TYPE_PROP, type );
	}

	/**
	 * Returns the default value of the parameter. The default value can be an
	 * expression, but cannot reference any other parameters.
	 * 
	 * @return the default value
	 */

	public String getDefaultValue( )
	{
		return getStringProperty( ScalarParameter.DEFAULT_VALUE_PROP );
	}

	/**
	 * Sets the default value of the parameter. The default value can be an
	 * expression, but cannot reference any other parameters.
	 * 
	 * @param defaultValue
	 *            the default value for the parameter
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setDefaultValue( String defaultValue ) throws SemanticException
	{
		setProperty( ScalarParameter.DEFAULT_VALUE_PROP, defaultValue );
	}

	/**
	 * Tests whether hides the user's entry by displaying asterisks. Often used
	 * for passwords.
	 * 
	 * @return <code>true</code> if hides the user's entry by asterisks,
	 *         <code>false</code> if shows characters as usual.
	 */

	public boolean isConcealValue( )
	{
		return getBooleanProperty( ScalarParameter.CONCEAL_VALUE_PROP );
	}

	/**
	 * Sets the attribute that's hides the user's entry by displaying asterisks.
	 * Often used for passwords.
	 * 
	 * @param concealValue
	 *            <code>true</code> if hides the user's entry by asterisks,
	 *            <code>false</code> if shows characters as usual.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setConcealValue( boolean concealValue )
			throws SemanticException
	{
		setProperty( ScalarParameter.CONCEAL_VALUE_PROP, Boolean
				.valueOf( concealValue ) );
	}

	/**
	 * Tests whether the value of the parameter can be <code>null</code>.
	 * 
	 * @return <code>true</code> if the value can be <code>null</code>,
	 *         <code>false</code> if the value can not be <code>null</code>.
	 */

	public boolean allowNull( )
	{
		return getBooleanProperty( ScalarParameter.ALLOW_NULL_PROP );
	}

	/**
	 * Sets the flag that indicates whether the value of the parameter can be
	 * <code>null</code>.
	 * 
	 * @param allowNull
	 *            <code>true</code> if the value can be <code>null</code>,
	 *            <code>false</code> if the value can not be <code>null</code>.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setAllowNull( boolean allowNull ) throws SemanticException
	{
		setProperty( ScalarParameter.ALLOW_NULL_PROP, Boolean
				.valueOf( allowNull ) );
	}

	/**
	 * Tests whether the string value of the parameter can be <code>null</code>.
	 * 
	 * @return <code>true</code> if the value can be <code>null</code>,
	 *         <code>false</code> if the value can not be <code>null</code>.
	 */

	public boolean allowBlank( )
	{
		return getBooleanProperty( ScalarParameter.ALLOW_BLANK_PROP );
	}

	/**
	 * Sets the flag that indicates whether the string value of the parameter
	 * can be <code>null</code>.
	 * 
	 * @param allowNull
	 *            <code>true</code> if the value can be <code>null</code>,
	 *            <code>false</code> if the value can not be <code>null</code>.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setAllowBlank( boolean allowNull ) throws SemanticException
	{
		setProperty( ScalarParameter.ALLOW_BLANK_PROP, Boolean
				.valueOf( allowNull ) );
	}

	/**
	 * Returns the format instructions for the parameter value. The format is
	 * used by the UI to display the value.
	 * 
	 * @return the format for the parameter value
	 */

	public String getFormat( )
	{
		return getStringProperty( ScalarParameter.FORMAT_PROP );
	}

	/**
	 * Sets the format instructions for the parameter value. The format is used
	 * by the UI to display the value.
	 * 
	 * @param format
	 *            the format for the parameter value
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setFormat( String format ) throws SemanticException
	{
		setStringProperty( ScalarParameter.FORMAT_PROP, format );
	}

	/**
	 * Returns the control type for this parameter. Control types are one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>PARAM_CONTROL_TEXT_BOX</code>
	 * <li><code>PARAM_CONTROL_LIST_BOX</code>
	 * <li><code>PARAM_CONTROL_COMBOBOX</code>
	 * <li><code>PARAM_CONTROL_RADIO_BUTTON</code>
	 * <li><code>PARAM_CONTROL_CHECK_BOX</code>
	 * </ul>
	 * 
	 * @return the control type for the UI to display the parameter
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getControlType( )
	{
		return getStringProperty( ScalarParameter.CONTROL_TYPE_PROP );
	}

	/**
	 * Sets the control type for this parameter. Control types are one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>PARAM_CONTROL_TEXT_BOX</code>
	 * <li><code>PARAM_CONTROL_LIST_BOX</code>
	 * <li><code>PARAM_CONTROL_COMBOBOX</code>
	 * <li><code>PARAM_CONTROL_RADIO_BUTTON</code>
	 * <li><code>PARAM_CONTROL_CHECK_BOX</code>
	 * </ul>
	 * 
	 * @param controlType
	 *            the control type for the UI to display the parameter
	 * 
	 * @throws SemanticException
	 *             if the input type is not one of above choices.
	 * @see #getDataType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public void setControlType( String controlType ) throws SemanticException
	{
		setStringProperty( ScalarParameter.CONTROL_TYPE_PROP, controlType );
	}

	/**
	 * Returns the alignment for this parameter. Alignments can be one of the
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>SCALAR_PARAM_ALIGN_AUTO</code>
	 * <li><code>SCALAR_PARAM_ALIGN_LEFT</code>
	 * <li><code>SCALAR_PARAM_ALIGN_CENTER</code>
	 * <li><code>SCALAR_PARAM_ALIGN_RIGHT</code>
	 * </ul>
	 * 
	 * @return the alignment for the UI to display the parameter
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getAlignment( )
	{
		return getStringProperty( ScalarParameter.ALIGNMENT_PROP );
	}

	/**
	 * Sets the alignment for this parameter. Alignments can be one of the
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>SCALAR_PARAM_ALIGN_AUTO</code>
	 * <li><code>SCALAR_PARAM_ALIGN_LEFT</code>
	 * <li><code>SCALAR_PARAM_ALIGN_CENTER</code>
	 * <li><code>SCALAR_PARAM_ALIGN_RIGHT</code>
	 * </ul>
	 * 
	 * @param align
	 *            the alignment for the UI to display the parameter
	 * 
	 * @throws SemanticException
	 *             if the input type is not one of above choices.
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public void setAlignment( String align ) throws SemanticException
	{
		setStringProperty( ScalarParameter.ALIGNMENT_PROP, align );
	}

	/**
	 * Tests whether the value must match one of values in the selection list.
	 * 
	 * 
	 * @return <code>true</code> if the value must match one of values in the
	 *         list, otherwise <code>false</code>.
	 */

	public boolean isMustMatch( )
	{
		return getBooleanProperty( ScalarParameter.MUCH_MATCH_PROP );
	}

	/**
	 * Sets the flag indicates that whether the value must match one of values
	 * in the selection list.
	 * 
	 * @param mustMatch
	 *            <code>true</code> if the value must match one of values in
	 *            the list, otherwise <code>false</code>.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setMustMatch( boolean mustMatch ) throws SemanticException
	{
		setProperty( ScalarParameter.MUCH_MATCH_PROP, Boolean
				.valueOf( mustMatch ) );
	}

	/**
	 * Tests whether to display values in the order defined in the list.
	 * 
	 * 
	 * @return <code>true</code> if to display value in the order, otherwise
	 *         <code>false</code>.
	 */

	public boolean isFixedOrder( )
	{
		return getBooleanProperty( ScalarParameter.FIXED_ORDER_PROP );
	}

	/**
	 * Sets the flag indicates that whether to display values in the order
	 * defined in the list.
	 * 
	 * @param fixedOrder
	 *            <code>true</code> if to display values in the order,
	 *            otherwise <code>false</code>.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setFixedOrder( boolean fixedOrder ) throws SemanticException
	{
		setProperty( ScalarParameter.FIXED_ORDER_PROP, Boolean
				.valueOf( fixedOrder ) );
	}

	/**
	 * Returns the iterator for the static selection list defined on this scalar
	 * parameter. Each element in the iterator is the an instance of
	 * <code>SelectionChoiceHandle</code>.
	 * 
	 * @return the iterator for selection list defined on this scalar parameter.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.SelectionChoice
	 */

	public Iterator choiceIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ScalarParameter.SELECTION_LIST_PROP );
		return propHandle.iterator( );
	}

	/**
	 * Returns the data set name of the dynamic list for this parameter.
	 * 
	 * @return the data set name of the dynamic list
	 */

	public String getDataSetName( )
	{
		if ( getElement( ).getLocalProperty( getModule( ),
				ScalarParameter.DATASET_NAME_PROP ) != null )
		{
			return ( (ElementRefValue) getElement( ).getLocalProperty(
					getModule( ), ScalarParameter.DATASET_NAME_PROP ) )
					.getName( );
		}

		String name = getStringProperty( ScalarParameter.DATASET_NAME_PROP );
		if ( name == null )
			return null;

		// must be extends

		Module module = getModule( );
		DesignElementHandle parent = getExtends( );

		while ( parent != null )
		{
			if ( parent.getElement( ).getLocalProperty( parent.getModule( ),
					ScalarParameter.DATASET_NAME_PROP ) != null )
			{
				module = (Module) parent.getRoot( ).getElement( );
				break;
			}

			parent = parent.getExtends( );
		}

		if ( module instanceof Library )
		{
			String namespace = ( (Library) module ).getNamespace( );
			return StringUtil.buildQualifiedReference( namespace, name );
		}

		return name;
	}

	/**
	 * Sets the data set name of the dynamic list for this parameter.
	 * 
	 * @param dataSetName
	 *            the data set name of the dynamic list
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setDataSetName( String dataSetName ) throws SemanticException
	{
		setStringProperty( ScalarParameter.DATASET_NAME_PROP, dataSetName );
	}

	/**
	 * Returns an expression on the data row from the dynamic list data set that
	 * returns the value for the choice.
	 * 
	 * @return the expression that returns the parameter value for each row in
	 *         the dynamic list.
	 */

	public String getValueExpr( )
	{
		return getStringProperty( ScalarParameter.VALUE_EXPR_PROP );
	}

	/**
	 * Sets an expression on the data row from the dynamic list data set that
	 * returns the value for the choice.
	 * 
	 * @param valueExpr
	 *            the expression that returns the parameter value for each row
	 *            in the dynamic list.
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setValueExpr( String valueExpr ) throws SemanticException
	{
		setStringProperty( ScalarParameter.VALUE_EXPR_PROP, valueExpr );
	}

	/**
	 * Returns an expression on the data row from the dynamic list data set that
	 * returns the prompt for the choice.
	 * 
	 * @return an expression that returns the display value for each row in the
	 *         dynamic list.
	 */

	public String getLabelExpr( )
	{
		return getStringProperty( ScalarParameter.LABEL_EXPR_PROP );
	}

	/**
	 * Sets an expression on the data row from the dynamic list data set that
	 * returns the prompt for the choice.
	 * 
	 * @param labelExpr
	 *            an expression that returns the display value for each row in
	 *            the dynamic list.
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setLabelExpr( String labelExpr ) throws SemanticException
	{
		setStringProperty( ScalarParameter.LABEL_EXPR_PROP, labelExpr );
	}

	/**
	 * Returns the name of the query column that returns values for the choice
	 * of the dynamic list for this parameter.
	 * 
	 * @return the the name of the query column
	 * 
	 * @deprecated Replaced by the method {@link #getValueExpr()}
	 */

	public String getValueColumn( )
	{
		return getValueExpr( );
	}

	/**
	 * Sets the name of the query column that returns values for the choice of
	 * the dynamic list for this parameter.
	 * 
	 * @param valueColumn
	 *            the name of the query column
	 * @throws SemanticException
	 *             if the property is locked.
	 * 
	 * @deprecated Replaced by the method {@link #setValueExpr(String)}
	 */

	public void setValueColumn( String valueColumn ) throws SemanticException
	{
		setValueExpr( valueColumn );
	}

	/**
	 * Returns the name of the query column that returns the prompt for the
	 * choice of the dynamic list for this parameter.
	 * 
	 * @return the the name of the query column
	 * 
	 * @deprecated Replaced by the method {@link #getLabelExpr()}
	 */

	public String getLabelColumn( )
	{
		return getLabelExpr( );
	}

	/**
	 * Sets the name of the query column that returns the prompt for the choice
	 * of the dynamic list for this parameter.
	 * 
	 * @param labelColumn
	 *            the name of the query column
	 * @throws SemanticException
	 *             if the property is locked.
	 * 
	 * @deprecated Replaced by the method {@link #setLabelExpr(String)}
	 */

	public void setLabelColumn( String labelColumn ) throws SemanticException
	{
		setLabelExpr( labelColumn );
	}

	/**
	 * Returns the data type for this parameter. The data type controls how the
	 * requester formats, parses and validates the parameter. Types are defined
	 * in <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_STRING</code>
	 * <li><code>PARAM_TYPE_FLOAT</code>
	 * <li><code>PARAM_TYPE_DECIMAL</code>
	 * <li><code>PARAM_TYPE_DATETYPE</code>
	 * <li><code>PARAM_TYPE_BOOLEAN</code>
	 * </ul>
	 * 
	 * @return the type for the parameter
	 * 
	 * @see #setType(String)
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 * 
	 * @deprecated Replaced by the method {@link #getDataType()}
	 */

	public String getType( )
	{
		return getDataType( );
	}

	/**
	 * Sets the data type for this parameter. The data type controls how the
	 * Requester formats, parses and validates the parameter. Types are defined
	 * in <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_STRING</code>
	 * <li><code>PARAM_TYPE_FLOAT</code>
	 * <li><code>PARAM_TYPE_DECIMAL</code>
	 * <li><code>PARAM_TYPE_DATETYPE</code>
	 * <li><code>PARAM_TYPE_BOOLEAN</code>
	 * </ul>
	 * 
	 * @param type
	 *            the type for the parameter
	 * 
	 * @throws SemanticException
	 *             if the input type is not one of above choices.
	 * @see #getType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 * 
	 * @deprecated Replaced by the method {@link #setDataType(String)}
	 */

	public void setType( String type ) throws SemanticException
	{
		setDataType( type );
	}

	/**
	 * Returns the parameter type for this scalar parameter. Types are defined
	 * in <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_VALUE_TYPE_STATIC</code>
	 * <li><code>PARAM_VALUE_TYPE_DYNAMIC</code>
	 * </ul>
	 * 
	 * @return the type for the scalar parameter
	 * 
	 * @see #setValueType(String)
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 * 
	 */

	public String getValueType( )
	{
		return getStringProperty( IScalarParameterModel.VALUE_TYPE_PROP );
	}

	/**
	 * Sets the parameter value type for this scalar parameter. Types are
	 * defined in <code>DesignChoiceConstants</code> can be one of the
	 * followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_STATIC</code>
	 * <li><code>PARAM_TYPE_DYNAMIC</code>
	 * </ul>
	 * 
	 * @param type
	 *            the type for the scalar parameter
	 * 
	 * @throws SemanticException
	 *             if the input type is not one of above choices.
	 * @see #getValueType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 * 
	 */

	public void setValueType( String type ) throws SemanticException
	{
		setStringProperty( IScalarParameterModel.VALUE_TYPE_PROP, type );
	}

	/**
	 * Set the value for the display prompt context.
	 * 
	 * @param promptValue
	 *            The display prompt context.
	 * 
	 * @throws SemanticException
	 * 
	 */

	public void setPromptText( String promptValue ) throws SemanticException
	{
		setStringProperty( IScalarParameterModel.PROMPT_TEXT_PROP, promptValue );
	}

	/**
	 * get the display prompt text.
	 * 
	 * @return the display prompt.
	 * 
	 */

	public String getPromptText( )
	{
		return getStringProperty( IScalarParameterModel.PROMPT_TEXT_PROP );
	}

	/**
	 * Set the value for the list limitation number. This property is used to
	 * limit the parameter display list.
	 * 
	 * @param listLimit
	 *            The limited number.
	 * 
	 * @throws SemanticException
	 * 
	 */

	public void setListlimit( int listLimit ) throws SemanticException
	{
		setIntProperty( IScalarParameterModel.LIST_LIMIT_PROP, listLimit );
	}

	/**
	 * get the list limited number.
	 * 
	 * @return the display prompt.
	 * 
	 */

	public int getListlimit( )
	{
		return getIntProperty( IScalarParameterModel.LIST_LIMIT_PROP );
	}

}