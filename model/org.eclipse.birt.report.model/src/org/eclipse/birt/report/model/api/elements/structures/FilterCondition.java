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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertyStructure;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Represents one filter in the filter list of List, Table or their Groups.
 * <p>
 * This is a managed object, meaning that all changes should be made though the
 * command layer so that they can be undone and redone. Each filter condition
 * has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Column </strong></dt>
 * <dd>a filter condition has a required column.</dd>
 * 
 * <dt><strong>Operator </strong></dt>
 * <dd>a filter condition has a required operator to compute.</dd>
 * 
 * <dt><strong>Filter Expr </strong></dt>
 * <dd>a filter condition has a required filter expression to test. Can be a
 * column or a complete boolean expression.</dd>
 * 
 * <dt><strong>Value 1 Expr </strong></dt>
 * <dd>a filter condition has an optional value 1 expression of the comparison
 * value for all but unary operators.</dd>
 * 
 * <dt><strong>Value 2 Expr </strong></dt>
 * <dd>a filter condition has an optional value 2 expression of the second
 * comparison value for trinary operators(between, not between).</dd>
 * </dl>
 * 
 */

public class FilterCondition extends PropertyStructure
{

	/**
	 * Name of this structure. Matches the definition in the meta-data
	 * dictionary.
	 */

	public static final String FILTER_COND_STRUCT = "FilterCondition"; //$NON-NLS-1$

	/**
	 * Name of the filter operator member.
	 */

	public static final String OPERATOR_MEMBER = "operator"; //$NON-NLS-1$

	/**
	 * Name of the filter expression member.
	 */

	public static final String EXPR_MEMBER = "expr"; //$NON-NLS-1$

	/**
	 * Name of the filter value 1 expression member.
	 */

	public static final String VALUE1_MEMBER = "value1"; //$NON-NLS-1$

	/**
	 * Name of the filter value 2 expression member.
	 */

	public static final String VALUE2_MEMBER = "value2"; //$NON-NLS-1$

	/**
	 * Name of the filter target member.
	 */

	public static final String FILTER_TARGET_MEMBER = "filterTarget"; //$NON-NLS-1$

	/**
	 * Name of the member that indicates whether this filter is optional or not.
	 */

	public static final String IS_OPTIONAL_MEMBER = "isOptional"; //$NON-NLS-1$

	/**
	 * Name of the member that indicates the extension name defined to map to a
	 * BIRT filter operator.
	 */

	public static final String EXTENSION_NAME_MEMBER = "extensionName"; //$NON-NLS-1$

	/**
	 * Name of the member that indicates the unique id of a custom filter
	 * expression contributed and defined by the extension.
	 */

	public static final String EXTENSION_EXPR_ID_MEMBER = "extensionExprId"; //$NON-NLS-1$

	/**
	 * Name of the member that indicates if the current filter condition will be
	 * pushed down to the database.
	 */

	public static final String PUSH_DOWN_MEMBER = "pushDown"; //$NON-NLS-1$

	/**
	 * Name of the member that indicates the name of the dynamic filter
	 * parameter to reference.
	 */

	public static final String DYNAMIC_FILTER_PARAMETER_MEMBER = "dynamicFilterParameter";//$NON-NLS-1$

	/**
	 * Name of the member that indicates the type of this filter condition. We
	 * define some choices for it.
	 * 
	 * @see DesignChoiceConstants#CHOICE_FILTER_CONDITION_TYPE
	 */
	public static final String TYPE_MEMBER = "type"; //$NON-NLS-1$
	
	/**
	 * Name of the member that indicates if the current filter condition need to update aggregation.
	 */
	public static final String UPDATE_AGGREGATION_MEMBER = "updateAggregation";

	/**
	 * Name of the member to save the any other user specified value.
	 */
	public static final String CUSTOM_VALUE = "customValue";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return FILTER_COND_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	/**
	 * Returns the filter expression.
	 * 
	 * @return the filter expression
	 */

	public String getExpr( )
	{
		return getStringProperty( null, EXPR_MEMBER );
	}

	/**
	 * Sets the filter expression.
	 * 
	 * @param expr
	 *            the filter expression to set
	 */

	public void setExpr( String expr )
	{
		setProperty( EXPR_MEMBER, expr );
	}

	/**
	 * Returns the operator. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li><code>FILTER_OPERATOR_EQ</code>
	 * <li><code>FILTER_OPERATOR_NE</code>
	 * <li><code>FILTER_OPERATOR_LT</code>
	 * <li><code>FILTER_OPERATOR_LE</code>
	 * <li><code>FILTER_OPERATOR_GE</code>
	 * <li><code>FILTER_OPERATOR_GT</code>
	 * <li><code>FILTER_OPERATOR_BETWEEN</code>
	 * <li><code>FILTER_OPERATOR_NOT_BETWEEN</code>
	 * <li><code>FILTER_OPERATOR_NULL</code>
	 * <li><code>FILTER_OPERATOR_NOT_NULL</code>
	 * <li><code>FILTER_OPERATOR_TRUE</code>
	 * <li><code>FILTER_OPERATOR_FALSE</code>
	 * <li><code>FILTER_OPERATOR_LIKE</code>
	 * <li><code>FILTER_OPERATOR_TOP_N</code>
	 * <li><code>FILTER_OPERATOR_BOTTOM_N</code>
	 * <li><code>FILTER_OPERATOR_TOP_PERCENT</code>
	 * <li><code>FILTER_OPERATOR_BOTTOM_PERCENT</code>
	 * <li><code>FILTER_OPERATOR_ANY</code>
	 * </ul>
	 * 
	 * @return the operator
	 */

	public String getOperator( )
	{
		return (String) getProperty( null, OPERATOR_MEMBER );
	}

	/**
	 * Sets the operator. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li><code>FILTER_OPERATOR_EQ</code>
	 * <li><code>FILTER_OPERATOR_NE</code>
	 * <li><code>FILTER_OPERATOR_LT</code>
	 * <li><code>FILTER_OPERATOR_LE</code>
	 * <li><code>FILTER_OPERATOR_GE</code>
	 * <li><code>FILTER_OPERATOR_GT</code>
	 * <li><code>FILTER_OPERATOR_BETWEEN</code>
	 * <li><code>FILTER_OPERATOR_NOT_BETWEEN</code>
	 * <li><code>FILTER_OPERATOR_NULL</code>
	 * <li><code>FILTER_OPERATOR_NOT_NULL</code>
	 * <li><code>FILTER_OPERATOR_TRUE</code>
	 * <li><code>FILTER_OPERATOR_FALSE</code>
	 * <li><code>FILTER_OPERATOR_LIKE</code>
	 * <li><code>FILTER_OPERATOR_TOP_N</code>
	 * <li><code>FILTER_OPERATOR_BOTTOM_N</code>
	 * <li><code>FILTER_OPERATOR_TOP_PERCENT</code>
	 * <li><code>FILTER_OPERATOR_BOTTOM_PERCENT</code>
	 * <li><code>FILTER_OPERATOR_ANY</code>
	 * </ul>
	 * 
	 * @param operator
	 *            the operator to set
	 */

	public void setOperator( String operator )
	{
		setProperty( OPERATOR_MEMBER, operator );
	}

	/**
	 * Returns the value 1 expression.
	 * 
	 * @return the value 1 expression
	 */

	public String getValue1( )
	{
		List valueList = getValue1List( );
		if ( valueList == null || valueList.isEmpty( ) )
			return null;

		return (String) valueList.get( 0 );
	}

	/**
	 * Gets the value1 expression list of this filter condition. For most filter
	 * operator, there is only one expression in the returned list. However,
	 * filter operator 'in' may contain more than one expression.
	 * 
	 * @return the value1 expression list of this filter condition.
	 * 
	 * @deprecated {@link #getValue1ExpressionList()}
	 */

	public List getValue1List( )
	{
		List<Expression> valueList = (List<Expression>) getProperty( null,
				VALUE1_MEMBER );
		if ( valueList == null || valueList.isEmpty( ) )
			return Collections.EMPTY_LIST;
		return Collections.unmodifiableList( ModelUtil
				.getExpressionCompatibleList( valueList ) );
	}

	/**
	 * Gets the value1 expression list of this filter condition. For most filter
	 * operator, there is only one expression in the returned list. However,
	 * filter operator 'in' may contain more than one expression.
	 * 
	 * @return the value1 expression list of this filter condition. Each item is
	 *         <code>Expression</code> object.
	 */

	public List getValue1ExpressionList( )
	{
		List<Expression> valueList = (List<Expression>) getProperty( null,
				VALUE1_MEMBER );
		if ( valueList == null || valueList.isEmpty( ) )
			return Collections.EMPTY_LIST;
		return Collections.unmodifiableList( valueList );
	}

	/**
	 * Sets the value 1 expression.
	 * 
	 * @param value1
	 *            the value 1 expression to set
	 */

	public void setValue1( String value1 )
	{
		if ( value1 == null )
		{
			setProperty( VALUE1_MEMBER, null );
			return;
		}
		List valueList = new ArrayList( );
		valueList.add( value1 );

		setProperty( VALUE1_MEMBER, valueList );
	}

	/**
	 * Sets the value 1 expression.
	 * 
	 * @param value1List
	 *            the value 1 expression list to set
	 */

	public void setValue1( List value1List )
	{
		setProperty( VALUE1_MEMBER, value1List );
	}

	/**
	 * Returns the value 2 expression.
	 * 
	 * @return the value 2 expression
	 */

	public String getValue2( )
	{
		return getStringProperty( null, VALUE2_MEMBER );
	}

	/**
	 * Sets the value 2 expression.
	 * 
	 * @param value2
	 *            the value 2 expression to set
	 */

	public void setValue2( String value2 )
	{
		setProperty( VALUE2_MEMBER, value2 );
	}

	/**
	 * Validates this structure. The following are the rules:
	 * <ul>
	 * <li>The filter expression is required.</li>
	 * </ul>
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(Module,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate( Module module, DesignElement element )
	{
		List list = super.validate( module, element );

		if ( StringUtil.isBlank( getFilterExpr( ) ) )
		{
			list.add( new PropertyValueException( element, getDefn( )
					.getMember( EXPR_MEMBER ), getFilterExpr( ),
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		return new FilterConditionHandle( valueHandle, index );
	}

	/**
	 * Returns the column name of this filter condition.
	 * 
	 * @return <code>null</code>. NOT support any more.
	 * 
	 * @deprecated This property has been removed.
	 */

	public String getColumn( )
	{
		return null;
	}

	/**
	 * Sets the column name of this filter condition. NOT support any more.
	 * 
	 * @param column
	 *            the column name to set
	 * 
	 * @deprecated This property has been removed.
	 * 
	 */
	public void setColumn( String column )
	{
	}

	/**
	 * Returns the filter expression.
	 * 
	 * @return the filter expression.
	 * 
	 * @deprecated Replaced by the method {@link #getExpr()}
	 */

	public String getFilterExpr( )
	{
		return getExpr( );
	}

	/**
	 * Sets the filter expression.
	 * 
	 * @param filterExpr
	 *            the filter expression to set
	 * 
	 * @deprecated Replaced by the method {@link #setExpr(String)}
	 */

	public void setFilterExpr( String filterExpr )
	{
		setExpr( filterExpr );
	}

	/**
	 * Returns the value 1 expression of this filter condition.
	 * 
	 * @return the expression of value 1.
	 * 
	 * @deprecated Replaced by the method {@link #getValue1()}
	 */

	public String getValue1Expr( )
	{
		return getValue1( );
	}

	/**
	 * Sets the value 1 expression of this filter condition.
	 * 
	 * @param value1Expr
	 *            the value 1 expression to set
	 * 
	 * @deprecated Replaced by the method {@link #setValue1(String)}
	 */

	public void setValue1Expr( String value1Expr )
	{
		setValue1( value1Expr );
	}

	/**
	 * Returns the value 2 expression of this filter condition.
	 * 
	 * @return the expression of value 1..
	 * 
	 * @deprecated Replaced by the method {@link #getValue2()}
	 */

	public String getValue2Expr( )
	{
		return getValue2( );
	}

	/**
	 * Sets the value 2 expression of this filter condition.
	 * 
	 * @param value2Expr
	 *            the value 2 expression to set
	 * 
	 * @deprecated Replaced by the method {@link #setValue2(String)}
	 */

	public void setValue2Expr( String value2Expr )
	{
		setValue2( value2Expr );
	}

	/**
	 * Returns the filter target. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li><code>FILTER_TARGET_DATA_SET</code>
	 * <li><code>FILTER_TARGET_RESULT_SET</code>
	 * </ul>
	 * 
	 * @return the operator
	 */

	public String getFilterTarget( )
	{
		return (String) getProperty( null, FILTER_TARGET_MEMBER );
	}

	/**
	 * Sets the filter target. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li><code>FILTER_TARGET_DATA_SET</code>
	 * <li><code>FILTER_TARGET_RESULT_SET</code>
	 * </ul>
	 * 
	 * @param filterTarget
	 *            the filter target to set
	 */

	public void setFilterTarget( String filterTarget )
	{
		setProperty( FILTER_TARGET_MEMBER, filterTarget );
	}

	/**
	 * Determines whether this filter condition is optional or not.
	 * 
	 * @return true if this filter is optional, otherwise false
	 */
	public boolean isOptional( )
	{
		Boolean isOptional = (Boolean) getProperty( null, IS_OPTIONAL_MEMBER );
		if ( isOptional == null )
			return false;
		return isOptional.booleanValue( );
	}

	/**
	 * Sets the optional status for this filter condition.
	 * 
	 * @param isOptional
	 *            true if this filter is optional, otherwise false
	 */
	public void setOptional( boolean isOptional )
	{
		setProperty( IS_OPTIONAL_MEMBER, Boolean.valueOf( isOptional ) );
	}

	/**
	 * Returns the unique id of an
	 * org.eclipse.datatools.connectivity.oda.filterExpressions extension to
	 * whose custom expressions are defined to map to a BIRT filter operator.
	 * 
	 * @return the extension name
	 */
	public String getExtensionName( )
	{
		return (String) getProperty( null, EXTENSION_NAME_MEMBER );
	}

	/**
	 * Returns the id of a custom filter expression contributed and defined by
	 * the extension identified in the consumerExpressionMapping.
	 * 
	 * @return the extension expression id
	 */

	public String getExtensionExprId( )
	{
		return (String) getProperty( null, EXTENSION_EXPR_ID_MEMBER );
	}

	/**
	 * Indicate if the current filter condition will be pushed down to the
	 * database. Default value is false. Only the oda extension provider
	 * supported operators can be pushed down to database. For those only BIRT
	 * supported operators even this property is set to true, will be ignored.
	 * 
	 * @return true if the current filter condition will be pushed down to the
	 *         database, otherwise false.
	 */

	public boolean pushDown( )
	{
		Boolean pushDown = (Boolean) getProperty( null, PUSH_DOWN_MEMBER );
		if ( pushDown == null )
		{
			return false;
		}
		return pushDown.booleanValue( );
	}

	/**
	 * Returns the name of the dynamic filter parameter to reference when the
	 * filter condition is dynamic.
	 * 
	 * @return the name to the dynamic filter parameter to reference.
	 */

	public String getDynamicFilterParameter( )
	{
		return getStringProperty( null, DYNAMIC_FILTER_PARAMETER_MEMBER );
	}

	/**
	 * Sets the unique id of an
	 * org.eclipse.datatools.connectivity.oda.filterExpressions extension to
	 * whose custom expressions are defined to map to a BIRT filter operator.
	 * 
	 * @param extensionName
	 *            the extension name to set
	 */

	public void setExtensionName( String extensionName )
	{
		setProperty( EXTENSION_NAME_MEMBER, extensionName );
	}

	/**
	 * Sets the id of a custom filter expression contributed and defined by the
	 * extension identified in the consumerExpressionMapping.
	 * 
	 * @param extensionExprId
	 *            the id to set
	 */

	public void setExtensionExprId( String extensionExprId )
	{
		setProperty( EXTENSION_EXPR_ID_MEMBER, extensionExprId );
	}

	/**
	 * Sets the push down status for this filter condition
	 * 
	 * @param pushDown
	 *            true if the current filter condition will be pushed down to
	 *            the database, otherwise false.
	 */
	public void setPushDown( boolean pushDown )
	{
		setProperty( PUSH_DOWN_MEMBER, Boolean.valueOf( pushDown ) );
	}

	/**
	 * Sets the name of the dynamic filter parameter to reference.
	 * 
	 * @param parameterName
	 *            the name of the dynamic filter parameter to set
	 */

	public void setDynamicFilterParameter( String parameterName )
	{
		setProperty( DYNAMIC_FILTER_PARAMETER_MEMBER, parameterName );
	}

	/**
	 * Returns the type. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li><code>FILTER_CONDITION_TYPE_SLICER</code>
	 * <li><code>FILTER_CONDITION_TYPE_SIMPLE</code>
	 * </ul>
	 * 
	 * @return the operator
	 */

	public String getType( )
	{
		return getStringProperty( null, TYPE_MEMBER );
	}

	/**
	 * Sets the type. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li><code>FILTER_CONDITION_TYPE_SLICER</code>
	 * <li><code>FILTER_CONDITION_TYPE_SIMPLE</code>
	 * </ul>
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType( String type )
	{
		setProperty( TYPE_MEMBER, type );
	}
	
	/**
	 * Sets the value 2 expression.
	 * 
	 * @param value1List
	 *            the value 2 expression list to set
	 */
	public void setValue2 ( Expression value )
	{
		setExpressionProperty( VALUE2_MEMBER, value );
	}
	
	/**
	 * Checks if this filter condition needs to update aggregation.
	 * 
	 * @return the flag to indicate updating aggregation or not.
	 */
	public boolean updateAggregation( )
	{
		Boolean updateAggregation = (Boolean) getProperty( null,
				UPDATE_AGGREGATION_MEMBER );
		if ( updateAggregation == null )
			return false;
		return updateAggregation.booleanValue( );
	}

	/**
	 * Sets the updateAggregation flag of the filter condition.
	 * 
	 * @param updateAggregation
	 *            the updateAggregation flag to set
	 * @throws SemanticException
	 */

	public void setUpdateAggregation( boolean updateAggregation )
	{
		setProperty( FilterCondition.UPDATE_AGGREGATION_MEMBER,
				Boolean.valueOf( updateAggregation ) );
	}

	/**
	 * Returns the user specified value.
	 * 
	 * @return the flag to indicate updating aggregation or not.
	 */
	public String getCustomValue( )
	{
		return getStringProperty(null, CUSTOM_VALUE);
	}

	/**
	 * Sets the user specified value.
	 * 
	 */

	public void setCustomValue( String customValue )
	{
		setProperty( CUSTOM_VALUE, customValue );
	}
}