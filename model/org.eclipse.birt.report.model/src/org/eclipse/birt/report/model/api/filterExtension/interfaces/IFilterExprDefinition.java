/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.filterExtension.interfaces;

import com.ibm.icu.util.ULocale;

/**
 * IFilterExprDefinition
 */

public interface IFilterExprDefinition {

	/**
	 * Indicates this FilterExpreDefinition is BIRT supported only, not mapped to a
	 * ODA extension Filter definition.
	 */
	static final int BIRT_SUPPORT_ONLY = 0;

	/**
	 * Indicates this FilterExpreDefinition is ODA extension side supported only,
	 * not mapped to a BIRT predefined Filter definition.
	 */
	static final int EXTENSION_SUPPORT_ONLY = 1;

	/**
	 * Indicates this FilterExpreDefinition is supported by both of ODA extension
	 * and BIRT predefined.
	 */
	static final int EXPR_MAPPING_SUPPORTED = 2;

	/**
	 * Returns the expression supported type of this FilterExprDefinition. The
	 * returned type could be:
	 * <ul>
	 * <li>BIRT_SUPPORT_ONLY
	 * <li>EXTENSION_SUPPORT_ONLY
	 * <li>EXPR_MAPPING_SUPPORTED
	 * </ul>
	 * 
	 * @return the expression supported type.
	 */
	public int expressionSupportedType();

	/**
	 * Return the corresponding BIRT predefined Filter expression operator display
	 * name.
	 * 
	 * @return BIRT predefined filter operator name, if there is. Null, if there is
	 *         no mapped one.
	 */
	public String getBirtFilterExprDisplayName();

	/**
	 * Return the corresponding BIRT predefined Filter expression operator display
	 * name.
	 * 
	 * @return BIRT predefined filter operator name, if there is. Null, if there is
	 *         no mapped one.
	 */
	public String getBirtFilterExprDisplayName(ULocale locale);

	/**
	 * Returns the BIRT predefined filter expression operator internal name.
	 * 
	 * @return BIRT predefined filter operator internal name, if there is. Return
	 *         Null if this definition is not mapped to a BIRT predefined filter
	 *         expression.
	 * 
	 */
	public String getBirtFilterExprId();

	/**
	 * Returns the ODA filter extension provider ID if there is.
	 * 
	 * @return ODA filter extension provider ID, if there is. Null, if there is no
	 *         ODA extension filter applicable.
	 */
	public String getProviderExtensionId();

	/**
	 * Returns the ODA filter extension filter expression id if there is.
	 * 
	 * @return ODA extension filter expression id if there is. Null, if not
	 *         applicable.
	 */
	public String getExtFilterExprId();

	/**
	 * Returns the display name of this Filter expression.
	 * 
	 * @return display name of the filter expression.
	 */
	public String getExtFilterDisplayName();

	/**
	 * Returns the min arguments required by this filter expression definition.
	 * 
	 * @return the min number of arguments that required by this filter definition.
	 */
	public Integer getMinArguments();

	/**
	 * Indicates if this filter definition support unbounded max arguments.
	 * 
	 * @return true if it supported unbounded max arguments, false, if it does not
	 *         support.
	 */
	public boolean supportsUnboundedMaxArguments();

	/**
	 * Returns the number of arguments that this filter definition can maximize
	 * supported.
	 * 
	 * @return the maximal number of arguments.
	 */
	public Integer getMaxArguments();

	/**
	 * Indicates whether the expression is mapped to the negated data base
	 * expression, i.e. the database expression should be nested within a
	 * NotExpression.
	 * <p>
	 * One example is that: the database may use eq and negated eq to represent BIRT
	 * EQ and NE.
	 * 
	 * @return <code>true</code> if mapped to a negated provider expression;
	 *         <code>false</code> otherwise.
	 */

	public boolean isNegatedExtExprId();

	/**
	 * Indicates whether the given API type is supported by this filter operator.
	 * 
	 * @param apiDataType the api data type
	 * @return <code>true</code> if it is supported. Otherwise, <code>false</code>.
	 */

	public boolean supportsAPIDataType(int apiDataType);
}
