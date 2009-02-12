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
import org.eclipse.birt.report.model.api.util.OperatorUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * 
 */
public class FilterConditionElementHandle extends ContentElementHandle
		implements
			IFilterConditionElementModel
{

	/**
	 * Constructs a filter condition handle with the given design and the
	 * element. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public FilterConditionElementHandle( Module module, DesignElement element )
	{
		super( module, element );

	}

	/**
	 * Returns the filter expression.
	 * 
	 * @return the filter expression
	 */

	public String getExpr( )
	{
		return getStringProperty( EXPR_PROP );
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
		setProperty( EXPR_PROP, filterExpr );
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
	 * @return the operator of this filter condition
	 */

	public String getOperator( )
	{
		return getStringProperty( OPERATOR_PROP );
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
	 * @throws SemanticException
	 *             if operator is not in the choice list.
	 */

	public void setOperator( String operator ) throws SemanticException
	{

		ActivityStack stack = getModule( ).getActivityStack( );
		stack.startTrans( CommandLabelFactory.getCommandLabel(
				MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[]{OPERATOR_PROP} ) );
		try
		{
			setProperty( OPERATOR_PROP, operator );
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
	 */
	public List getValue1List( )
	{
		List valueList = (List) getProperty( VALUE1_PROP );
		if ( valueList == null || valueList.isEmpty( ) )
			return Collections.EMPTY_LIST;
		return Collections.unmodifiableList( valueList );
	}

	/**
	 * Sets the value 1 expression of this filter condition.
	 * 
	 * @param value1Expr
	 *            the value 1 expression to set
	 * @throws SemanticException
	 */

	public void setValue1( String value1Expr ) throws SemanticException
	{
		setProperty( VALUE1_PROP, value1Expr );
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
		setProperty( VALUE1_PROP, value1List );
	}

	/**
	 * Returns the value 2 expression of this filter condition.
	 * 
	 * @return the value 2 expression of this filter condition
	 */

	public String getValue2( )
	{
		return getStringProperty( VALUE2_PROP );
	}

	/**
	 * Sets the value 2 expression of this filter condition.
	 * 
	 * @param value2Expr
	 *            the value 2 expression to set
	 * @throws SemanticException
	 */

	public void setValue2( String value2Expr ) throws SemanticException
	{
		setStringProperty( VALUE2_PROP, value2Expr );
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
	 * @return the target type
	 */

	public String getFilterTarget( )
	{
		return (String) getProperty( FILTER_TARGET_PROP );
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
	 * 
	 * @throws SemanticException
	 *             if the value is not one of the above.
	 */

	public void setFilterTarget( String filterTarget ) throws SemanticException
	{
		setStringProperty( FILTER_TARGET_PROP, filterTarget );
	}

	/**
	 * Gets the member value handle of this filter condition element if it sets.
	 * Otherwise return null.
	 * 
	 * @return
	 */
	public MemberValueHandle getMember( )
	{
		List contents = getContents( MEMBER_PROP );
		if ( contents != null && contents.size( ) > 0 )
			return (MemberValueHandle) contents.get( 0 );
		return null;
	}

	/**
	 * Determines whether this filte rcondition is optional or not.
	 * 
	 * @return true if this filter is optional, otherwise false
	 */
	public boolean isOptional( )
	{
		return getBooleanProperty( IS_OPTIONAL_PROP );
	}

	/**
	 * Sets the optional status for this filter condition.
	 * 
	 * @param isOptional
	 *            true if this filter is optional, otherwise false
	 * @throws SemanticException
	 */
	public void setOptional( boolean isOptional ) throws SemanticException
	{
		setProperty( IS_OPTIONAL_PROP, Boolean.valueOf( isOptional ) );
	}
}
