/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.simpleapi;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 *
 */

public interface IFilterConditionElement extends IDesignElement {

	/**
	 * Returns the filter expression.
	 *
	 * @return the filter expression
	 */

	String getExpr();

	/**
	 * Sets the filter expression.
	 *
	 * @param filterExpr the filter expression to set
	 * @throws SemanticException value required exception
	 */

	void setExpr(String filterExpr) throws SemanticException;

	/**
	 * Returns the operator of this filter condition. The possible values are
	 * defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
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

	String getOperator();

	/**
	 * Sets the operator of this filter condition. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
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
	 * @param operator the operator to set
	 * @throws SemanticException if operator is not in the choice list.
	 */

	void setOperator(String operator) throws SemanticException;

	/**
	 * Gets the value1 expression list of this filter condition. For most filter
	 * operator, there is only one expression in the returned list. However, filter
	 * operator 'in' may contain more than one expression.
	 *
	 * @return the value1 expression list of this filter condition.
	 */

	List getValue1List();

	/**
	 * Sets the value 1 expression list of this filter condition.
	 *
	 * @param value1List the value 1 expression list to set
	 * @throws SemanticException if the instance in the list is not valid
	 */

	void setValue1(List value1List) throws SemanticException;

	/**
	 * Returns the value 2 expression of this filter condition.
	 *
	 * @return the value 2 expression of this filter condition
	 */

	String getValue2();

	/**
	 * Sets the value 2 expression of this filter condition.
	 *
	 * @param value2Expr the value 2 expression to set
	 * @throws SemanticException
	 */

	void setValue2(String value2Expr) throws SemanticException;

	/**
	 * Returns the filter target. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li><code>FILTER_TARGET_DATA_SET</code>
	 * <li><code>FILTER_TARGET_RESULT_SET</code>
	 * </ul>
	 *
	 * @return the target type
	 */

	String getFilterTarget();

	/**
	 * Sets the filter target. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li><code>FILTER_TARGET_DATA_SET</code>
	 * <li><code>FILTER_TARGET_RESULT_SET</code>
	 * </ul>
	 *
	 * @param filterTarget the filter target to set
	 *
	 * @throws SemanticException if the value is not one of the above.
	 */

	void setFilterTarget(String filterTarget) throws SemanticException;

	/**
	 * Determines whether this filter condition is optional or not.
	 *
	 * @return true if this filter is optional, otherwise false
	 */

	boolean isOptional();

	/**
	 * Sets the optional status for this filter condition.
	 *
	 * @param isOptional true if this filter is optional, otherwise false
	 * @throws SemanticException
	 */

	void setOptional(boolean isOptional) throws SemanticException;
}
