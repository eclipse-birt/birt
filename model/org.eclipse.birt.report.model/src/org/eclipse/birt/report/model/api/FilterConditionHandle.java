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

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.util.OperatorUtil;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
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
 * <dt><strong>Filter Expression </strong></dt>
 * <dd>a filter condition has a required filter expression to test. Can be a
 * column or a complete boolean expression.</dd>
 * 
 * <dt><strong>Value 1 Expression </strong></dt>
 * <dd>a filter condition has an optional value 1 expression of the comparison
 * value for all but unary operators.</dd>
 * 
 * <dt><strong>Value 2 Expression </strong></dt>
 * <dd>a filter condition has an optional value 2 expression of the second
 * comparison value for trinary operators(between, not between).</dd>
 * </dl>
 * 
 */

public class FilterConditionHandle extends StructureHandle
{

	/**
	 * Constructs the handle of filter condition.
	 * 
	 * @param valueHandle
	 *            the value handle for filter condition list of one property
	 * @param index
	 *            the position of this filter condition in the list
	 */

	public FilterConditionHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Returns the filter expression.
	 * 
	 * @return the filter expression
	 */

	public String getExpr( )
	{
		return getStringProperty( FilterCondition.EXPR_MEMBER );
	}

	/**
	 * Sets the filter expression.
	 * 
	 * @param filterExpr
	 *            the filter expression to set
	 * @throws SemanticException
	 *             value required exception
	 */

	public void setExpr( String filterExpr ) throws SemanticException
	{
		setProperty( FilterCondition.EXPR_MEMBER, filterExpr );
	}

	/**
	 * Returns the operator of this filter condition. The possible values are
	 * defined in
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
	 * <li><code>
	 * FILTER_OPERATOR_NOT_BETWEEN</code>
	 * <li><code>FILTER_OPERATOR_NULL</code>
	 * <li><code>FILTER_OPERATOR_NOT_NULL</code>
	 * <li><code>FILTER_OPERATOR_TRUE
	 * </code>
	 * <li><code>FILTER_OPERATOR_FALSE</code>
	 * <li><code>
	 * FILTER_OPERATOR_LIKE</code>
	 * <li><code>FILTER_OPERATOR_TOP_N</code>
	 * <li>
	 * <code>FILTER_OPERATOR_BOTTOM_N</code>
	 * <li><code>
	 * FILTER_OPERATOR_TOP_PERCENT</code>
	 * <li><code>
	 * FILTER_OPERATOR_BOTTOM_PERCENT</code>
	 * <li><code>FILTER_OPERATOR_ANY
	 * </code>
	 * </ul>
	 * 
	 * @return the operator of this filter condition
	 */

	public String getOperator( )
	{
		return getStringProperty( FilterCondition.OPERATOR_MEMBER );
	}

	/**
	 * Sets the operator of this filter condition. The allowed values are
	 * defined in
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
	 * <li><code>
	 * FILTER_OPERATOR_NOT_BETWEEN</code>
	 * <li><code>FILTER_OPERATOR_NULL</code>
	 * <li><code>FILTER_OPERATOR_NOT_NULL</code>
	 * <li><code>FILTER_OPERATOR_TRUE
	 * </code>
	 * <li><code>FILTER_OPERATOR_FALSE</code>
	 * <li><code>
	 * FILTER_OPERATOR_LIKE</code>
	 * <li><code>FILTER_OPERATOR_TOP_N</code>
	 * <li>
	 * <code>FILTER_OPERATOR_BOTTOM_N</code>
	 * <li><code>
	 * FILTER_OPERATOR_TOP_PERCENT</code>
	 * <li><code>
	 * FILTER_OPERATOR_BOTTOM_PERCENT</code>
	 * <li><code>FILTER_OPERATOR_ANY
	 * </code>
	 * </ul>
	 * 
	 * @param operator
	 *            the operator to set
	 * @throws SemanticException
	 *             if operator is not in the choice list.
	 */

	public void setOperator( String operator ) throws SemanticException
	{

		ActivityStack stack = getModule( ).getActivityStack( );
		stack.startTrans( CommandLabelFactory.getCommandLabel(
				MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[]{IFilterConditionElementModel.OPERATOR_PROP} ) );
		try
		{
			setProperty( FilterCondition.OPERATOR_MEMBER, operator );
			int level = OperatorUtil.computeFilterOperatorLevel( operator );
			switch ( level )
			{
				case OperatorUtil.OPERATOR_LEVEL_ONE :
					setValue2( null );
					break;
				case OperatorUtil.OPERATOR_LEVEL_TWO :
					break;
				case OperatorUtil.OPERATOR_LEVEL_ZERO :
					setValue2( null );
					setValue1( (List) null );
					break;
				case OperatorUtil.OPERATOR_LEVEL_NOT_EXIST :
					break;
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/**
	 * Returns the value 1 expression of this filter condition.
	 * 
	 * @return the value 1 expression of this filter condition
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
		List valueList = (List) getProperty( FilterCondition.VALUE1_MEMBER );
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
	 * @return the value1 expression list handle of this filter condition.
	 */

	public ExpressionListHandle getValue1ExpressionList( )
	{
		MemberRef tmpRef = new CachedMemberRef( structRef,
				(StructPropertyDefn) getDefn( ).getMember(
						FilterCondition.VALUE1_MEMBER ) );

		return new ExpressionListHandle( elementHandle, tmpRef );
	}

	/**
	 * Sets the value 1 expression of this filter condition.
	 * 
	 * @param value1Expr
	 *            the value 1 expression to set
	 */

	public void setValue1( String value1Expr )
	{
		setPropertySilently( FilterCondition.VALUE1_MEMBER, value1Expr );
	}

	/**
	 * Sets the value 1 expression list of this filter condition.
	 * 
	 * @param value1List
	 *            the value 1 expression list to set
	 * @throws SemanticException
	 *             if the instance in the list is not valid
	 */

	public void setValue1( List value1List ) throws SemanticException
	{
		setProperty( FilterCondition.VALUE1_MEMBER, value1List );
	}

	/**
	 * Returns the value 2 expression of this filter condition.
	 * 
	 * @return the value 2 expression of this filter condition
	 */

	public String getValue2( )
	{
		return getStringProperty( FilterCondition.VALUE2_MEMBER );
	}

	/**
	 * Sets the value 2 expression of this filter condition.
	 * 
	 * @param value2Expr
	 *            the value 2 expression to set
	 */

	public void setValue2( String value2Expr )
	{
		setPropertySilently( FilterCondition.VALUE2_MEMBER, value2Expr );
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
	 * @return the expression for the filter.
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
	 * @throws SemanticException
	 *             value required exception
	 * @deprecated Replaced by the method {@link #setExpr(String)}
	 */

	public void setFilterExpr( String filterExpr ) throws SemanticException
	{
		setExpr( filterExpr );
	}

	/**
	 * Returns the value 1 expression of this filter condition.
	 * 
	 * @return the value 1 expression.
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
	 * @return the value 2 expression.
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
	 * <li><code>
	 * FILTER_TARGET_RESULT_SET</code>
	 * </ul>
	 * 
	 * @return the target type
	 */

	public String getFilterTarget( )
	{
		return (String) getProperty( FilterCondition.FILTER_TARGET_MEMBER );
	}

	/**
	 * Sets the filter target. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li><code>FILTER_TARGET_DATA_SET</code>
	 * <li><code>
	 * FILTER_TARGET_RESULT_SET</code>
	 * </ul>
	 * 
	 * @param filterTarget
	 *            the filter target to set
	 * 
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setFilterTarget( String filterTarget ) throws SemanticException
	{
		setProperty( FilterCondition.FILTER_TARGET_MEMBER, filterTarget );
	}

	/**
	 * Determines whether this filte rcondition is optional or not.
	 * 
	 * @return true if this filter is optional, otherwise false
	 */
	public boolean isOptional( )
	{
		Boolean isOptional = (Boolean) getProperty( FilterCondition.IS_OPTIONAL_MEMBER );
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
		setPropertySilently( FilterCondition.IS_OPTIONAL_MEMBER, Boolean
				.valueOf( isOptional ) );
	}
}