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
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.ParameterFormatValue;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;
import org.eclipse.birt.report.model.util.UnusedBoundColumnsMgr;

/**
 * Represents a scalar (single-value) report parameter. If the user enters no
 * value for a parameter, then the default value is used. If there is no default
 * value, then BIRT checks if <code>null</code> is allowed. If so, the value of
 * the parameter is null. If nulls are not allowed, then the user must enter a
 * value.
 * <p>
 * Scalar parameters can have static or dynamic selection lists.
 * <ul>
 * <li>The parameter static selection list provides a developer-defined list of
 * choices. Every choice has two parts: a choice and a label. The label can be
 * externalized and appears in the UI. The choice is the value passed to the
 * report.
 * <li>This parameter can define a dynamic selection list for the parameter. The
 * data set can reference other parameters by referring to a data set. The data
 * set must return a column that contains the choice values. It may also contain
 * a column that returns the labels for the values. All other columns are
 * ignored.
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
	 * <li><code>PARAM_TYPE_STRING</code> <li><code>PARAM_TYPE_FLOAT</code> <li>
	 * <code>PARAM_TYPE_DECIMAL</code> <li><code>PARAM_TYPE_INTEGER</code> <li>
	 * <code>PARAM_TYPE_DATETYPE</code> <li><code>PARAM_TYPE_BOOLEAN</code>
	 * </ul>
	 * 
	 * @return the type for the parameter
	 * 
	 * @see #setDataType(String)
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getDataType( )
	{
		return getStringProperty( DATA_TYPE_PROP );
	}

	/**
	 * Sets the data type for this parameter. The data type controls how the
	 * Requester formats, parses and validates the parameter. Types are defined
	 * in <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_STRING</code> <li><code>PARAM_TYPE_FLOAT</code> <li>
	 * <code>PARAM_TYPE_DECIMAL</code> <li><code>PARAM_TYPE_INTEGER</code> <li>
	 * <code>PARAM_TYPE_DATETYPE</code> <li><code>PARAM_TYPE_BOOLEAN</code>
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
		setStringProperty( DATA_TYPE_PROP, type );
	}

	/**
	 * Returns the first default value of the parameter. The default value can
	 * be an expression, but cannot reference any other parameters. The default
	 * value of this parameter can be a list. This method returns the first
	 * value if exists.
	 * 
	 * @return the default value
	 * @deprecated since 2.5 replaced by {@link #getDefaultValueList()}
	 */

	public String getDefaultValue( )
	{
		List<String> valueList = getDefaultValueList( );
		if ( valueList == null || valueList.isEmpty( ) )
			return null;
		return valueList.get( 0 );
	}

	/**
	 * Returns the default value list of the parameter. Each item in this list
	 * can be an expression, but cannot reference any other parameters.
	 * 
	 * @return the default value
	 */

	public List<String> getDefaultValueList( )
	{
		return getListProperty( DEFAULT_VALUE_PROP );
	}

	/**
	 * Sets the default value of the parameter. The default value can be an
	 * expression, but cannot reference any other parameters.
	 * 
	 * @param defaultValue
	 *            the default value for the parameter
	 * @throws SemanticException
	 *             if the property is locked.
	 * @deprecated since 2.5 replaced by {@link #setDefaultValueList(List)}
	 */

	public void setDefaultValue( String defaultValue ) throws SemanticException
	{
		setProperty( DEFAULT_VALUE_PROP, defaultValue );
	}

	/**
	 * Sets the default value list of the parameter. Each item in the list can
	 * be an expression, but cannot reference any other parameters.
	 * 
	 * @param defaultValueList
	 *            the default value for the parameter
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setDefaultValueList( List<? extends Object> defaultValueList )
			throws SemanticException
	{
		setProperty( DEFAULT_VALUE_PROP, defaultValueList );
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
		return getBooleanProperty( CONCEAL_VALUE_PROP );
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
		setProperty( CONCEAL_VALUE_PROP, Boolean.valueOf( concealValue ) );
	}

	/**
	 * Tests whether the value of the parameter can be <code>null</code>.
	 * 
	 * @return <code>true</code> if the value can be <code>null</code>,
	 *         <code>false</code> if the value can not be <code>null</code>.
	 * 
	 * @deprecated by {@link #isRequired()}
	 */

	public boolean allowNull( )
	{
		return !getBooleanProperty( IS_REQUIRED_PROP );
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
	 * 
	 * @deprecated by {@link #setIsRequired(boolean)}
	 */

	public void setAllowNull( boolean allowNull ) throws SemanticException
	{
		setProperty( ALLOW_NULL_PROP, Boolean.valueOf( allowNull ) );
	}

	/**
	 * Tests whether the string value of the parameter can be <code>null</code>.
	 * 
	 * @return <code>true</code> if the value can be <code>null</code>,
	 *         <code>false</code> if the value can not be <code>null</code>.
	 * 
	 * @deprecated by {@link #isRequired()}
	 */

	public boolean allowBlank( )
	{
		String dataType = getStringProperty( DATA_TYPE_PROP );
		if ( DesignChoiceConstants.PARAM_TYPE_STRING
				.equalsIgnoreCase( dataType ) )
			return !getBooleanProperty( IS_REQUIRED_PROP );

		return false;
	}

	/**
	 * Sets the flag that indicates whether the string value of the parameter
	 * can be <code>null</code>.
	 * 
	 * @param allowBlank
	 *            <code>true</code> if the value can be <code>null</code>,
	 *            <code>false</code> if the value can not be <code>null</code>.
	 * @throws SemanticException
	 *             if the property is locked.
	 * 
	 * @deprecated by {@link #setIsRequired(boolean)}
	 */

	public void setAllowBlank( boolean allowBlank ) throws SemanticException
	{
		setProperty( ALLOW_BLANK_PROP, Boolean.valueOf( allowBlank ) );
	}

	/**
	 * Returns the format instructions for the parameter value. The format is
	 * used by the UI to display the value.
	 * 
	 * @return the format for the parameter value
	 * @deprecated replaced by getPattern and getCategory.
	 */

	public String getFormat( )
	{
		return getPattern( );
	}

	/**
	 * Returns the pattern of format instructions for the parameter value. The
	 * format is used by the UI to display the value.
	 * 
	 * @return the pattern of format for the parameter value
	 */

	public String getPattern( )
	{
		Object value = getProperty( FORMAT_PROP );
		if ( value == null )
			return null;

		assert value instanceof ParameterFormatValue;

		return ( (ParameterFormatValue) value ).getPattern( );
	}

	/**
	 * Returns the category for the parameter format. The format is used by the
	 * UI to display the value.
	 * 
	 * @return the category for the parameter format
	 */

	public String getCategory( )
	{
		Object value = getProperty( FORMAT_PROP );
		if ( value == null )
			return null;

		assert value instanceof ParameterFormatValue;

		return ( (ParameterFormatValue) value ).getCategory( );
	}

	/**
	 * Sets the format instructions for the parameter value. The format is used
	 * by the UI to display the value.
	 * 
	 * @param format
	 *            the format for the parameter value
	 * @throws SemanticException
	 *             if the property is locked.
	 * @deprecated replaced by setPattern and setCategory.
	 */

	public void setFormat( String format ) throws SemanticException
	{
		setPattern( format );
	}

	/**
	 * Sets the pattern of format instructions for the parameter value. The
	 * format is used by the UI to display the value.
	 * 
	 * @param pattern
	 *            the format for the parameter value
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setPattern( String pattern ) throws SemanticException
	{

		Object value = element.getLocalProperty( module, FORMAT_PROP );

		if ( value == null )
		{
			FormatValue formatValueToSet = new ParameterFormatValue( );
			formatValueToSet.setPattern( pattern );
			setProperty( FORMAT_PROP, formatValueToSet );
		}
		else
		{
			PropertyHandle propHandle = getPropertyHandle( FORMAT_PROP );
			FormatValue formatValueToSet = (FormatValue) value;
			FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet
					.getHandle( propHandle );

			formatHandle.setPattern( pattern );
		}
	}

	/**
	 * Sets the category for the parameter format. The format is used by the UI
	 * to display the value.
	 * 
	 * @param category
	 *            the category for the format
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setCategory( String category ) throws SemanticException
	{

		Object value = element.getLocalProperty( module, FORMAT_PROP );

		if ( value == null )
		{
			FormatValue formatValueToSet = new ParameterFormatValue( );
			formatValueToSet.setCategory( category );
			setProperty( FORMAT_PROP, formatValueToSet );
		}
		else
		{
			PropertyHandle propHandle = getPropertyHandle( FORMAT_PROP );
			FormatValue formatValueToSet = (FormatValue) value;
			FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet
					.getHandle( propHandle );

			formatHandle.setCategory( category );
		}
	}

	/**
	 * Returns the control type for this parameter. Control types are one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li>PARAM_CONTROL_TEXT_BOX <li>PARAM_CONTROL_LIST_BOX <li>
	 * PARAM_CONTROL_COMBOBOX <li>PARAM_CONTROL_RADIO_BUTTON <li>
	 * PARAM_CONTROL_CHECK_BOX <li>PARAM_CONTROL_AUTO_SUGGEST
	 * </ul>
	 * 
	 * @return the control type for the UI to display the parameter
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getControlType( )
	{
		return getStringProperty( CONTROL_TYPE_PROP );
	}

	/**
	 * Sets the control type for this parameter. Control types are one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li>PARAM_CONTROL_TEXT_BOX <li>PARAM_CONTROL_LIST_BOX <li>
	 * PARAM_CONTROL_COMBOBOX <li>PARAM_CONTROL_RADIO_BUTTON <li>
	 * PARAM_CONTROL_CHECK_BOX <li>PARAM_CONTROL_AUTO_SUGGEST
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
		setStringProperty( CONTROL_TYPE_PROP, controlType );
	}

	/**
	 * Returns the alignment for this parameter. Alignments can be one of the
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>SCALAR_PARAM_ALIGN_AUTO</code> <li><code>
	 * SCALAR_PARAM_ALIGN_LEFT</code> <li><code>SCALAR_PARAM_ALIGN_CENTER</code>
	 * <li><code>SCALAR_PARAM_ALIGN_RIGHT</code>
	 * </ul>
	 * 
	 * @return the alignment for the UI to display the parameter
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getAlignment( )
	{
		return getStringProperty( ALIGNMENT_PROP );
	}

	/**
	 * Sets the alignment for this parameter. Alignments can be one of the
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>SCALAR_PARAM_ALIGN_AUTO</code> <li><code>
	 * SCALAR_PARAM_ALIGN_LEFT</code> <li><code>SCALAR_PARAM_ALIGN_CENTER</code>
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
		setStringProperty( ALIGNMENT_PROP, align );
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
		return getBooleanProperty( MUCH_MATCH_PROP );
	}

	/**
	 * Sets the flag indicates that whether the value must match one of values
	 * in the selection list.
	 * 
	 * @param mustMatch
	 *            <code>true</code> if the value must match one of values in the
	 *            list, otherwise <code>false</code>.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setMustMatch( boolean mustMatch ) throws SemanticException
	{
		setProperty( MUCH_MATCH_PROP, Boolean.valueOf( mustMatch ) );
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
		return getBooleanProperty( FIXED_ORDER_PROP );
	}

	/**
	 * Sets the flag indicates that whether to display values in the order
	 * defined in the list.
	 * 
	 * @param fixedOrder
	 *            <code>true</code> if to display values in the order, otherwise
	 *            <code>false</code>.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setFixedOrder( boolean fixedOrder ) throws SemanticException
	{
		setProperty( FIXED_ORDER_PROP, Boolean.valueOf( fixedOrder ) );
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
		PropertyHandle propHandle = getPropertyHandle( SELECTION_LIST_PROP );
		return propHandle.iterator( );
	}

	/**
	 * Returns the data set name of the dynamic list for this parameter.
	 * 
	 * @return the data set name of the dynamic list
	 */

	public String getDataSetName( )
	{
		return getStringProperty( DATASET_NAME_PROP );
	}

	/**
	 * Returns the handle for the data set defined on the parameter. If the
	 * parameter do not define the data set name or if the data set is not
	 * defined in the design/library scope, return <code>null</code>.
	 * 
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet( )
	{
		DesignElement dataSet = ( (ScalarParameter) getElement( ) )
				.getDataSetElement( module );
		if ( dataSet == null )
			return null;

		return (DataSetHandle) dataSet.getHandle( dataSet.getRoot( ) );
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
		setStringProperty( DATASET_NAME_PROP, dataSetName );
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
		return getStringProperty( VALUE_EXPR_PROP );
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
		setStringProperty( VALUE_EXPR_PROP, valueExpr );
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
		return getStringProperty( LABEL_EXPR_PROP );
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
		setStringProperty( LABEL_EXPR_PROP, labelExpr );
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
	 * <li><code>PARAM_TYPE_STRING</code> <li><code>PARAM_TYPE_FLOAT</code> <li>
	 * <code>PARAM_TYPE_DECIMAL</code> <li><code>PARAM_TYPE_INTEGER</code> <li>
	 * <code>PARAM_TYPE_DATETYPE</code> <li><code>PARAM_TYPE_BOOLEAN</code>
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
	 * <li><code>PARAM_TYPE_STRING</code> <li><code>PARAM_TYPE_FLOAT</code> <li>
	 * <code>PARAM_TYPE_DECIMAL</code> <li><code>PARAM_TYPE_INTEGER</code> <li>
	 * <code>PARAM_TYPE_DATETYPE</code> <li><code>PARAM_TYPE_BOOLEAN</code>
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
	 * <li><code>PARAM_VALUE_TYPE_STATIC</code> <li><code>
	 * PARAM_VALUE_TYPE_DYNAMIC</code>
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
		return getStringProperty( VALUE_TYPE_PROP );
	}

	/**
	 * Sets the parameter value type for this scalar parameter. Types are
	 * defined in <code>DesignChoiceConstants</code> can be one of the
	 * followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_STATIC</code> <li><code>PARAM_TYPE_DYNAMIC</code>
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
		setStringProperty( PROMPT_TEXT_PROP, promptValue );
	}

	/**
	 * get the display prompt text.
	 * 
	 * @return the display prompt.
	 * 
	 */

	public String getPromptText( )
	{
		return getStringProperty( PROMPT_TEXT_PROP );
	}

	/**
	 * Set the value for the prompt text ID.
	 * 
	 * @param promptIDValue
	 *            The prompt text ID.
	 * 
	 * @throws SemanticException
	 * 
	 */

	public void setPromptTextID( String promptIDValue )
			throws SemanticException
	{
		setStringProperty( PROMPT_TEXT_ID_PROP, promptIDValue );
	}

	/**
	 * Returns the prompt text ID.
	 * 
	 * @return the prompt text ID.
	 * 
	 */

	public String getPromptTextID( )
	{
		return getStringProperty( PROMPT_TEXT_ID_PROP );
	}

	/**
	 * Returns the localized text for prompt text. If the localized text for the
	 * text resource key is found, it will be returned. Otherwise, the static
	 * text will be returned.
	 * 
	 * @return the localized text for the prompt text
	 */

	public String getDisplayPromptText( )
	{
		return getExternalizedValue( PROMPT_TEXT_ID_PROP, PROMPT_TEXT_PROP );
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
		setIntProperty( LIST_LIMIT_PROP, listLimit );
	}

	/**
	 * get the list limited number.
	 * 
	 * @return the display prompt.
	 * 
	 */

	public int getListlimit( )
	{
		// if the parameter control type is auto-suggest, the list limit should
		// return 0.
		if ( DesignChoiceConstants.PARAM_CONTROL_AUTO_SUGGEST
				.equals( getControlType( ) ) )
			return 0;
		return getIntProperty( LIST_LIMIT_PROP );
	}

	/**
	 * Returns the bound columns that binds the data set columns. The item in
	 * the iterator is the corresponding <code>ComputedColumnHandle</code>.
	 * 
	 * @return a list containing the bound columns.
	 */

	public Iterator columnBindingsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( BOUND_DATA_COLUMNS_PROP );
		return propHandle.iterator( );
	}

	/**
	 * Get a handle to deal with the bound column.
	 * 
	 * @return a handle to deal with the bound data column.
	 */

	public PropertyHandle getColumnBindings( )
	{
		return getPropertyHandle( BOUND_DATA_COLUMNS_PROP );
	}

	/**
	 * Adds a bound column to the list.
	 * 
	 * @param addColumn
	 *            the bound column to add
	 * @param inForce
	 *            <code>true</code> the column is added to the list regardless
	 *            of duplicate expression. <code>false</code> do not add the
	 *            column if the expression already exist
	 * @param column
	 *            the bound column
	 * @return the newly created <code>ComputedColumnHandle</code> or the
	 *         existed <code>ComputedColumnHandle</code> in the list
	 * @throws SemanticException
	 *             if expression is not duplicate but the name duplicates the
	 *             existing bound column. Or, if the both name/expression are
	 *             duplicate, but <code>inForce</code> is <code>true</code>.
	 */

	public ComputedColumnHandle addColumnBinding( ComputedColumn addColumn,
			boolean inForce ) throws SemanticException
	{
		if ( addColumn == null )
			return null;

		String expr = addColumn.getExpression( );

		List columns = (List) getProperty( BOUND_DATA_COLUMNS_PROP );
		if ( columns == null )
			return (ComputedColumnHandle) getPropertyHandle(
					BOUND_DATA_COLUMNS_PROP ).addItem( addColumn );
		ComputedColumn column = BoundDataColumnUtil.getColumn( columns, expr,
				addColumn.getAggregateFunction( ), addColumn
						.getAggregateOnList( ) );
		if ( column != null && !inForce )
		{
			return (ComputedColumnHandle) column.handle(
					getPropertyHandle( BOUND_DATA_COLUMNS_PROP ), columns
							.indexOf( column ) );
		}
		return (ComputedColumnHandle) getPropertyHandle(
				BOUND_DATA_COLUMNS_PROP ).addItem( addColumn );
	}

	/**
	 * Removed unused bound columns from the parameter. Bound columns of nested
	 * elements will not be removed.
	 * 
	 * @throws SemanticException
	 *             if bound column property is locked.
	 */

	public void removedUnusedColumnBindings( ) throws SemanticException
	{
		UnusedBoundColumnsMgr.removedUnusedBoundColumns( this );
	}

	/**
	 * Sets the flag that indicates whether the value of the parameter is
	 * required. For string type parameter, if the value is required, it cannot
	 * be <code>null</code> or empty. For other type parameters, required value
	 * cannot be <code>null</code>.
	 * 
	 * @param isRequired
	 *            <code>true</code> if the value is required. Otherwise
	 *            <code>false</code>.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setIsRequired( boolean isRequired ) throws SemanticException
	{
		setProperty( IS_REQUIRED_PROP, Boolean.valueOf( isRequired ) );
	}

	/**
	 * Tests whether the string value of the parameter is required. For string
	 * type parameter, if the value is required, it cannot be <code>null</code>
	 * or empty. For other type parameters, required value cannot be
	 * <code>null</code>.
	 * 
	 * @return <code>true</code> if the value is required. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isRequired( )
	{
		return getBooleanProperty( IS_REQUIRED_PROP );
	}

	/**
	 * Sets the flag that indicates whether duplicate values should be shown
	 * when preview.
	 * 
	 * @param distinct
	 *            <code>true</code> if duplicate values only show once.
	 *            Otherwise <code>false</code>.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setDistinct( boolean distinct ) throws SemanticException
	{
		setProperty( DISTINCT_PROP, Boolean.valueOf( distinct ) );
	}

	/**
	 * Checks whether duplicate values should be shown when preview.
	 * 
	 * @return <code>true</code> if duplicate values only show once. Otherwise
	 *         <code>false</code>.
	 */

	public boolean distinct( )
	{
		return getBooleanProperty( DISTINCT_PROP );
	}

	/**
	 * Sets the sort order for parameter values when preview. The input argument
	 * can be
	 * 
	 * <ul>
	 * <li>DesignChoiceConstants.SORT_DIRECTION_ASC <li>
	 * DesignChoiceConstants.SORT_DIRECTION_DESC <li><code>null</code>
	 * </ul>
	 * 
	 * @param direction
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setSortDirection( String direction ) throws SemanticException
	{
		setProperty( SORT_DIRECTION_PROP, direction );
	}

	/**
	 * Gets the sort order for parameter values when preview. The return value
	 * can be
	 * 
	 * <ul>
	 * <li>DesignChoiceConstants.SORT_DIRECTION_ASC <li>
	 * DesignChoiceConstants.SORT_DIRECTION_DESC <li><code>null</code>
	 * </ul>
	 * 
	 * @return the sort order for parameter values
	 */

	public String getSortDirection( )
	{
		return getStringProperty( SORT_DIRECTION_PROP );
	}

	/**
	 * Sets the sort key for parameter values when preview. The input argument
	 * can be
	 * 
	 * <ul>
	 * <li>DesignChoiceConstants.PARAM_SORT_VALUES_VALUE <li>
	 * DesignChoiceConstants.PARAM_SORT_VALUES_LABEL
	 * </ul>
	 * 
	 * @param sortValue
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setSortBy( String sortValue ) throws SemanticException
	{
		setProperty( SORT_BY_PROP, sortValue );
	}

	/**
	 * Gets the sort key for parameter values when preview. The return value can
	 * be
	 * 
	 * <ul>
	 * <li>DesignChoiceConstants.PARAM_SORT_VALUES_VALUE <li>
	 * DesignChoiceConstants.PARAM_SORT_VALUES_LABEL
	 * </ul>
	 * 
	 * @return the sort key for parameter values
	 */

	public String getSortBy( )
	{
		return getStringProperty( SORT_BY_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.DesignElementHandle#setProperty(java
	 * .lang.String, java.lang.Object)
	 */

	public void setProperty( String propName, Object value )
			throws SemanticException
	{
		if ( ALLOW_BLANK_PROP.equalsIgnoreCase( propName )
				|| ALLOW_NULL_PROP.equalsIgnoreCase( propName ) )
		{
			Boolean newValue = (Boolean) value;
			if ( newValue != null )
			{
				newValue = Boolean
						.valueOf( !( (Boolean) value ).booleanValue( ) );
			}

			// allowBlank only applies to string type.

			if ( ALLOW_BLANK_PROP.equalsIgnoreCase( propName ) )
			{
				String dataType = super.getStringProperty( DATA_TYPE_PROP );
				if ( DesignChoiceConstants.PARAM_TYPE_STRING
						.equalsIgnoreCase( dataType ) )
					super.setProperty( IS_REQUIRED_PROP, newValue );

				return;
			}

			super.setProperty( IS_REQUIRED_PROP, newValue );

			return;
		}

		super.setProperty( propName, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.DesignElementHandle#getProperty(java
	 * .lang.String)
	 */

	public Object getProperty( String propName )
	{
		if ( ALLOW_BLANK_PROP.equalsIgnoreCase( propName ) )
		{
			Boolean retValue = null;
			String dataType = super.getStringProperty( DATA_TYPE_PROP );
			if ( DesignChoiceConstants.PARAM_TYPE_STRING
					.equalsIgnoreCase( dataType ) )
				retValue = Boolean
						.valueOf( !getBooleanProperty( IS_REQUIRED_PROP ) );
			else
				retValue = Boolean.FALSE;

			return retValue;
		}
		else if ( ALLOW_NULL_PROP.equalsIgnoreCase( propName ) )
		{
			return Boolean.valueOf( !getBooleanProperty( IS_REQUIRED_PROP ) );
		}

		return super.getProperty( propName );
	}

	/**
	 * Returns the parameter type for this scalar parameter. Types are defined
	 * in <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>SCALAR_PARAM_TYPE_SIMPLE</code> <li><code>
	 * SCALAR_PARAM_TYPE_MULTI_VALUE</code> <li><code>SCALAR_PARAM_TYPE_AD_HOC
	 * </code>
	 * </ul>
	 * 
	 * @return the type for the parameter
	 * 
	 * @see #setParamType(String)
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getParamType( )
	{
		return getStringProperty( PARAM_TYPE_PROP );
	}

	/**
	 * Sets the parameter type for this scalar parameter. Types are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>SCALAR_PARAM_TYPE_SIMPLE</code> <li><code>
	 * SCALAR_PARAM_TYPE_MULTI_VALUE</code> <li><code>SCALAR_PARAM_TYPE_AD_HOC
	 * </code>
	 * </ul>
	 * 
	 * @param type
	 *            the type for the parameter
	 * 
	 * @throws SemanticException
	 *             if the input type is not one of above choices.
	 * @see #getParamType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public void setParamType( String type ) throws SemanticException
	{
		setStringProperty( PARAM_TYPE_PROP, type );
	}

	/**
	 * Returns the maximal number of of entries a report parameter pick list can
	 * have.
	 * 
	 * @return the threshold number.
	 */

	public int getAutoSuggestThreshold( )
	{
		return getIntProperty( AUTO_SUGGEST_THRESHOLD_PROP );
	}

	/**
	 * Sets the maximal number of of entries a report parameter pick list can
	 * have.
	 * 
	 * @param number
	 *            the threshold number.
	 * @throws SemanticException
	 */

	public void setAutoSuggestThreshold( int number ) throws SemanticException
	{
		setIntProperty( AUTO_SUGGEST_THRESHOLD_PROP, number );
	}

	/**
	 * Sets the expression by which the result sorts.
	 * 
	 * @param sortByColumn
	 *            expression by which the result sorts
	 * @throws SemanticException
	 */
	public void setSortByColumn( String sortByColumn ) throws SemanticException
	{
		setStringProperty( SORT_BY_COLUMN_PROP, sortByColumn );
	}

	/**
	 * Gets the expression by which the result sorts.
	 * 
	 * @return the expression by which the result sorts
	 */
	public String getSortByColumn( )
	{
		return getStringProperty( SORT_BY_COLUMN_PROP );
	}

}