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

import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.elements.structures.StyleRule;

/**
 * Represents the handle of style rule. This abstract class provides the common
 * methods for <code>HighlightRuleHandle</code> and <code>MapRuleHandle</code>.
 * The style rule provides the operator, value1, and value2 to compute boolean
 * result.
 */

public abstract class StyleRuleHandle extends StructureHandle
{

	/**
	 * Constructs the handle of style rule.
	 * 
	 * @param valueHandle
	 *            the value handle for style rule list of one property
	 * @param index
	 *            the position of this style rule in the list
	 */

	public StyleRuleHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Returns the operator. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.elements.DesignChoiceConstants}, and they
	 * are:
	 * <ul>
	 * <li>MAP_OPERATOR_EQ
	 * <li>MAP_OPERATOR_NE
	 * <li>MAP_OPERATOR_LT
	 * <li>MAP_OPERATOR_LE
	 * <li>MAP_OPERATOR_GE
	 * <li>MAP_OPERATOR_GT
	 * <li>MAP_OPERATOR_BETWEEN
	 * <li>MAP_OPERATOR_NOT_BETWEEN
	 * <li>MAP_OPERATOR_NULL
	 * <li>MAP_OPERATOR_NOT_NULL
	 * <li>MAP_OPERATOR_TRUE
	 * <li>MAP_OPERATOR_FALSE
	 * <li>MAP_OPERATOR_LIKE
	 * <li>MAP_OPERATOR_ANY
	 * </ul>
	 * 
	 * @return the operator
	 */

	public String getOperator( )
	{
		return getStringProperty( StyleRule.OPERATOR_MEMBER );
	}

	/**
	 * Sets the operator. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.elements.DesignChoiceConstants}, and they
	 * are:
	 * <ul>
	 * <li>MAP_OPERATOR_EQ
	 * <li>MAP_OPERATOR_NE
	 * <li>MAP_OPERATOR_LT
	 * <li>MAP_OPERATOR_LE
	 * <li>MAP_OPERATOR_GE
	 * <li>MAP_OPERATOR_GT
	 * <li>MAP_OPERATOR_BETWEEN
	 * <li>MAP_OPERATOR_NOT_BETWEEN
	 * <li>MAP_OPERATOR_NULL
	 * <li>MAP_OPERATOR_NOT_NULL
	 * <li>MAP_OPERATOR_TRUE
	 * <li>MAP_OPERATOR_FALSE
	 * <li>MAP_OPERATOR_LIKE
	 * <li>MAP_OPERATOR_ANY
	 * </ul>
	 * 
	 * @param operator
	 *            the operator to set
	 * @throws SemanticException
	 *             if operator is not in the choice list.
	 */

	public void setOperator( String operator ) throws SemanticException
	{
		setProperty( StyleRule.OPERATOR_MEMBER, operator );
	}

	/**
	 * Returns the value 1.
	 * 
	 * @return the value 1
	 */

	public String getValue1( )
	{
		return getStringProperty( StyleRule.VALUE1_MEMBER );
	}

	/**
	 * Sets the value 1.
	 * 
	 * @param value1
	 *            the value 1 to set
	 */

	public void setValue1( String value1 )
	{
		setPropertySilently( StyleRule.VALUE1_MEMBER, value1 );
	}

	/**
	 * Returns the value 2.
	 * 
	 * @return the value 2
	 */

	public String getValue2( )
	{
		return getStringProperty( StyleRule.VALUE2_MEMBER );
	}

	/**
	 * Sets the value 2.
	 * 
	 * @param value2
	 *            the value 2 to set
	 */

	public void setValue2( String value2 )
	{
		setPropertySilently( StyleRule.VALUE2_MEMBER, value2 );
	}
}