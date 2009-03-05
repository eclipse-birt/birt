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
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

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

public class FilterCondition extends Structure
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
	 * The filter operator.
	 */

	private String operator;

	/**
	 * The filter expression.
	 */

	private Expression expr;

	/**
	 * The filter value 1 expression.
	 */

	private List value1;

	/**
	 * The filter value 2 expression.
	 */

	private Expression value2;

	/**
	 * Filter target. It can either <code>DataSet</code> or
	 * <code>ResultSet</code>>.
	 */

	private String filterTarget;

	/**
	 * Member value that stores whether this filter is optional or not.
	 */

	private Boolean isOptional;

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
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( OPERATOR_MEMBER.equals( propName ) )
			return operator;
		else if ( EXPR_MEMBER.equals( propName ) )
			return expr;
		else if ( VALUE1_MEMBER.equals( propName ) )
			return value1;
		else if ( VALUE2_MEMBER.equals( propName ) )
			return value2;
		else if ( FILTER_TARGET_MEMBER.equals( propName ) )
			return filterTarget;
		else if ( IS_OPTIONAL_MEMBER.equals( propName ) )
			return isOptional;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( OPERATOR_MEMBER.equals( propName ) )
			operator = (String) value;
		else if ( EXPR_MEMBER.equals( propName ) )
			expr = convertObjectToExpression( value );
		else if ( VALUE1_MEMBER.equals( propName ) )
		{
			if ( value == null )
				value1 = null;
			else if ( value instanceof List<?> )
				value1 = convertListToExpressionList( (List<String>) value );
		}
		else if ( VALUE2_MEMBER.equals( propName ) )
			value2 = convertObjectToExpression( value );
		else if ( FILTER_TARGET_MEMBER.equals( propName ) )
			filterTarget = (String) value;
		else if ( IS_OPTIONAL_MEMBER.equals( propName ) )
			isOptional = (Boolean) value;
		else
			assert false;
	}

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

		Expression tmpExpr = (Expression) valueList.get( 0 );
		return tmpExpr.getStringExpression( );
	}

	/**
	 * Gets the value1 expression list of this filter condition. For most filter
	 * operator, there is only one expression in the returned list. However,
	 * filter operator 'in' may contain more than one expression.
	 * 
	 * @return the value1 expression list of this filter condition.
	 */
	public List getValue1List( )
	{
		List valueList = (List) getProperty( null, VALUE1_MEMBER );
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
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.model.api.SimpleValueHandle,
	 *      int)
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
	 * Determines whether this filte rcondition is optional or not.
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
}