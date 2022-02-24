/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.util.OperatorUtil;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.StructureContextUtil;

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

public class FilterConditionHandle extends StructureHandle {

	/**
	 * Constructs the handle of filter condition.
	 *
	 * @param valueHandle the value handle for filter condition list of one property
	 * @param index       the position of this filter condition in the list
	 */

	public FilterConditionHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the filter expression.
	 *
	 * @return the filter expression
	 */

	public String getExpr() {
		return getStringProperty(FilterCondition.EXPR_MEMBER);
	}

	/**
	 * Sets the filter expression.
	 *
	 * @param filterExpr the filter expression to set
	 * @throws SemanticException value required exception
	 */

	public void setExpr(String filterExpr) throws SemanticException {
		setProperty(FilterCondition.EXPR_MEMBER, filterExpr);
	}

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
	 * <li><code>FILTER_OPERATOR_BOTTOM_N</code>
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

	public String getOperator() {
		return getStringProperty(FilterCondition.OPERATOR_MEMBER);
	}

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
	 * <li><code>FILTER_OPERATOR_BOTTOM_N</code>
	 * <li><code>
	 * FILTER_OPERATOR_TOP_PERCENT</code>
	 * <li><code>
	 * FILTER_OPERATOR_BOTTOM_PERCENT</code>
	 * <li><code>FILTER_OPERATOR_ANY
	 * </code>
	 * </ul>
	 *
	 * @param operator the operator to set
	 * @throws SemanticException if operator is not in the choice list.
	 */

	public void setOperator(String operator) throws SemanticException {

		ActivityStack stack = getModule().getActivityStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[] { IFilterConditionElementModel.OPERATOR_PROP }));
		try {
			setProperty(FilterCondition.OPERATOR_MEMBER, operator);
			int level = OperatorUtil.computeFilterOperatorLevel(operator);
			switch (level) {
			case OperatorUtil.OPERATOR_LEVEL_ONE:
				setValue2((Expression) null);
				break;
			case OperatorUtil.OPERATOR_LEVEL_TWO:
				break;
			case OperatorUtil.OPERATOR_LEVEL_ZERO:
				setValue2((Expression) null);
				setValue1((List) null);
				break;
			case OperatorUtil.OPERATOR_LEVEL_NOT_EXIST:
				break;
			}
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

	/**
	 * Returns the value 1 expression of this filter condition.
	 *
	 * @return the value 1 expression of this filter condition
	 */

	public String getValue1() {
		List valueList = getValue1List();
		if (valueList == null || valueList.isEmpty()) {
			return null;
		}

		return (String) valueList.get(0);
	}

	/**
	 * Gets the value1 expression list of this filter condition. For most filter
	 * operator, there is only one expression in the returned list. However, filter
	 * operator 'in' may contain more than one expression.
	 *
	 * @return the value1 expression list of this filter condition.
	 *
	 * @deprecated {@link #getValue1ExpressionList()}
	 */
	@Deprecated
	public List getValue1List() {
		List valueList = (List) getProperty(FilterCondition.VALUE1_MEMBER);
		if (valueList == null || valueList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return Collections.unmodifiableList(ModelUtil.getExpressionCompatibleList(valueList));
	}

	/**
	 * Gets the value1 expression list of this filter condition. For most filter
	 * operator, there is only one expression in the returned list. However, filter
	 * operator 'in' may contain more than one expression.
	 *
	 * @return the value1 expression list handle of this filter condition.
	 */

	public ExpressionListHandle getValue1ExpressionList() {
		return new ExpressionListHandle(elementHandle,
				StructureContextUtil.createStructureContext(this, FilterCondition.VALUE1_MEMBER));
	}

	/**
	 * Sets the value 1 expression of this filter condition.
	 *
	 * @param value1Expr the value 1 expression to set
	 */

	public void setValue1(String value1Expr) {
		setPropertySilently(FilterCondition.VALUE1_MEMBER, value1Expr);
	}

	/**
	 * Sets the value 1 expression list of this filter condition.
	 *
	 * @param value1List the value 1 expression list to set
	 * @throws SemanticException if the instance in the list is not valid
	 */

	public void setValue1(List value1List) throws SemanticException {
		setProperty(FilterCondition.VALUE1_MEMBER, value1List);
	}

	/**
	 * Returns the value 2 expression of this filter condition.
	 *
	 * @return the value 2 expression of this filter condition
	 */

	public String getValue2() {
		return getStringProperty(FilterCondition.VALUE2_MEMBER);
	}

	/**
	 * Sets the value 2 expression of this filter condition.
	 *
	 * @param value2Expr the value 2 expression to set
	 */

	public void setValue2(String value2Expr) {
		setPropertySilently(FilterCondition.VALUE2_MEMBER, value2Expr);
	}

	/**
	 * Returns the column name of this filter condition.
	 *
	 * @return <code>null</code>. NOT support any more.
	 *
	 * @deprecated This property has been removed.
	 */

	@Deprecated
	public String getColumn() {
		return null;
	}

	/**
	 * Sets the column name of this filter condition. NOT support any more.
	 *
	 * @param column the column name to set
	 *
	 * @deprecated This property has been removed.
	 *
	 */
	@Deprecated
	public void setColumn(String column) {
	}

	/**
	 * Returns the filter expression.
	 *
	 * @return the expression for the filter.
	 *
	 * @deprecated Replaced by the method {@link #getExpr()}
	 */

	@Deprecated
	public String getFilterExpr() {
		return getExpr();
	}

	/**
	 * Sets the filter expression.
	 *
	 * @param filterExpr the filter expression to set
	 * @throws SemanticException value required exception
	 * @deprecated Replaced by the method {@link #setExpr(String)}
	 */

	@Deprecated
	public void setFilterExpr(String filterExpr) throws SemanticException {
		setExpr(filterExpr);
	}

	/**
	 * Returns the value 1 expression of this filter condition.
	 *
	 * @return the value 1 expression.
	 *
	 * @deprecated Replaced by the method {@link #getValue1()}
	 */

	@Deprecated
	public String getValue1Expr() {
		return getValue1();
	}

	/**
	 * Sets the value 1 expression of this filter condition.
	 *
	 * @param value1Expr the value 1 expression to set
	 *
	 * @deprecated Replaced by the method {@link #setValue1(String)}
	 */

	@Deprecated
	public void setValue1Expr(String value1Expr) {
		setValue1(value1Expr);
	}

	/**
	 * Returns the value 2 expression of this filter condition.
	 *
	 * @return the value 2 expression.
	 *
	 * @deprecated Replaced by the method {@link #getValue2()}
	 */

	@Deprecated
	public String getValue2Expr() {
		return getValue2();
	}

	/**
	 * Sets the value 2 expression of this filter condition.
	 *
	 * @param value2Expr the value 2 expression to set
	 *
	 * @deprecated Replaced by the method {@link #setValue2(String)}
	 */

	@Deprecated
	public void setValue2Expr(String value2Expr) {
		setValue2(value2Expr);
	}

	/**
	 * Returns the filter target. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li><code>FILTER_TARGET_DATA_SET</code>
	 * <li><code>
	 * FILTER_TARGET_RESULT_SET</code>
	 * </ul>
	 *
	 * @return the target type
	 */

	public String getFilterTarget() {
		return (String) getProperty(FilterCondition.FILTER_TARGET_MEMBER);
	}

	/**
	 * Sets the filter target. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li><code>FILTER_TARGET_DATA_SET</code>
	 * <li><code>
	 * FILTER_TARGET_RESULT_SET</code>
	 * </ul>
	 *
	 * @param filterTarget the filter target to set
	 *
	 * @throws SemanticException if the value is not one of the above.
	 */

	public void setFilterTarget(String filterTarget) throws SemanticException {
		setProperty(FilterCondition.FILTER_TARGET_MEMBER, filterTarget);
	}

	/**
	 * Determines whether this filte rcondition is optional or not.
	 *
	 * @return true if this filter is optional, otherwise false
	 */
	public boolean isOptional() {
		Boolean isOptional = (Boolean) getProperty(FilterCondition.IS_OPTIONAL_MEMBER);
		if (isOptional == null) {
			return false;
		}
		return isOptional.booleanValue();
	}

	/**
	 * Sets the optional status for this filter condition.
	 *
	 * @param isOptional true if this filter is optional, otherwise false
	 */
	public void setOptional(boolean isOptional) {
		setPropertySilently(FilterCondition.IS_OPTIONAL_MEMBER, Boolean.valueOf(isOptional));
	}

	/**
	 * Returns the unique id of an
	 * org.eclipse.datatools.connectivity.oda.filterExpressions extension to whose
	 * custom expressions are defined to map to a BIRT filter operator.
	 *
	 * @return the extension name
	 */
	public String getExtensionName() {
		return getStringProperty(FilterCondition.EXTENSION_NAME_MEMBER);
	}

	/**
	 * Returns the id of a custom filter expression contributed and defined by the
	 * extension identified in the consumerExpressionMapping.
	 *
	 * @return the extension expression id
	 */

	public String getExtensionExprId() {
		return getStringProperty(FilterCondition.EXTENSION_EXPR_ID_MEMBER);
	}

	/**
	 * Indicate if the current filter condition will be pushed down to the database.
	 * Default value is false. Only the oda extension provider supported operators
	 * can be pushed down to database. For those only BIRT supported operators even
	 * this property is set to true, will be ignored.
	 *
	 * @return true if the current filter condition will be pushed down to the
	 *         database, otherwise false.
	 */

	public boolean pushDown() {
		Boolean pushDown = (Boolean) getProperty(FilterCondition.PUSH_DOWN_MEMBER);
		if (pushDown == null) {
			return false;
		}
		return pushDown.booleanValue();
	}

	/**
	 * Returns the name of the dynamic filter parameter to reference when the filter
	 * condition is dynamic.
	 *
	 * @return the name to the dynamic filter parameter to reference.
	 */

	public String getDynamicFilterParameter() {
		return getStringProperty(FilterCondition.DYNAMIC_FILTER_PARAMETER_MEMBER);
	}

	/**
	 * Sets the unique id of an
	 * org.eclipse.datatools.connectivity.oda.filterExpressions extension to whose
	 * custom expressions are defined to map to a BIRT filter operator.
	 *
	 * @param extensionName the extension name to set
	 */

	public void setExtensionName(String extensionName) {
		setPropertySilently(FilterCondition.EXTENSION_NAME_MEMBER, extensionName);
	}

	/**
	 * Sets the id of a custom filter expression contributed and defined by the
	 * extension identified in the consumerExpressionMapping.
	 *
	 * @param extensionExprId the id to set
	 */

	public void setExtensionExprId(String extensionExprId) {
		setPropertySilently(FilterCondition.EXTENSION_EXPR_ID_MEMBER, extensionExprId);
	}

	/**
	 * Sets the push down status for this filter condition
	 *
	 * @param pushDown true if the current filter condition will be pushed down to
	 *                 the database, otherwise false.
	 */
	public void setPushDown(boolean pushDown) {
		setPropertySilently(FilterCondition.PUSH_DOWN_MEMBER, Boolean.valueOf(pushDown));
	}

	/**
	 * Sets the name of the dynamic filter parameter to reference.
	 *
	 * @param parameterName the name of the dynamic filter parameter to set
	 */

	public void setDynamicFilterParameter(String parameterName) {
		setPropertySilently(FilterCondition.DYNAMIC_FILTER_PARAMETER_MEMBER, parameterName);
	}

	/**
	 * Returns the type of this filter condition. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li><code>FILTER_CONDITION_TYPE_SLICER</code>
	 * <li><code>FILTER_CONDITION_TYPE_SIMPLE</code>
	 * </ul>
	 *
	 * @return the operator of this filter condition
	 */

	public String getType() {
		return getStringProperty(FilterCondition.TYPE_MEMBER);
	}

	/**
	 * Sets the type of this filter condition. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li><code>FILTER_CONDITION_TYPE_SLICER</code>
	 * <li><code>FILTER_CONDITION_TYPE_SIMPLE</code>
	 * </ul>
	 *
	 * @param type the type to set
	 * @throws SemanticException if type is not in the choice list.
	 */

	public void setType(String type) throws SemanticException {

		setProperty(FilterCondition.TYPE_MEMBER, type);
	}

	/**
	 * Sets the value 2 expression of this filter condition.
	 *
	 * @param value2Expr the value 2 expression to set
	 * @throws SemanticException
	 */

	public void setValue2(Expression value2Expr) throws SemanticException {
		setExpressionProperty(FilterCondition.VALUE2_MEMBER, value2Expr);
	}

	/**
	 * Checks if this filter condition needs to update aggregation.
	 *
	 * @return the flag to indicate updating aggregation or not.
	 */
	public boolean updateAggregation() {
		Boolean updateAggregation = (Boolean) getProperty(FilterCondition.UPDATE_AGGREGATION_MEMBER);
		if (updateAggregation == null) {
			return false;
		}
		return updateAggregation.booleanValue();
	}

	/**
	 * Sets the updateAggregation flag of the filter condition.
	 *
	 * @param updateAggregation the updateAggregation flag to set
	 * @throws SemanticException
	 */

	public void setUpdateAggregation(boolean updateAggregation) throws SemanticException {
		setProperty(FilterCondition.UPDATE_AGGREGATION_MEMBER, Boolean.valueOf(updateAggregation));
	}

	/**
	 * Returns the user specified value.
	 *
	 * @return the flag to indicate updating aggregation or not.
	 */
	public String getCustomValue() {
		return getStringProperty(FilterCondition.CUSTOM_VALUE);
	}

	/**
	 * Sets the user specified value.
	 *
	 */

	public void setCustomValue(String customValue) throws SemanticException {
		setProperty(FilterCondition.CUSTOM_VALUE, customValue);
	}
}
