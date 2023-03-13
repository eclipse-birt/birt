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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.PropertyStructure;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Base class for highlight and map rules in the style. Choices for the operand
 * are defined in <code>DesignChoiceConstants</code>.
 *
 * @see DesignChoiceConstants
 */

public abstract class StyleRule extends PropertyStructure {

	/**
	 * Name of the comparison operator member.
	 */

	public static final String OPERATOR_MEMBER = "operator"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the expression for the first operator
	 * operand.
	 */

	public static final String VALUE1_MEMBER = "value1"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the expression for the second operator
	 * operand.
	 */

	public static final String VALUE2_MEMBER = "value2"; //$NON-NLS-1$

	/**
	 * The expression for this rule.
	 */
	public static final String TEST_EXPR_MEMBER = "testExpr"; //$NON-NLS-1$

	/**
	 * Status that determines whether this structure is used in design time or not
	 */
	public static final String IS_DESIGN_TIME_MEMBER = "isDesignTime"; //$NON-NLS-1$

	/**
	 * The comparison operator. Default value is <code>MAP_OPERATOR_EQ</code>
	 */

	protected String operator = null;

	/**
	 * Expression for the first operand.
	 */

	protected List value1 = null;

	/**
	 * Expression for the second operand.
	 */

	protected Expression value2 = null;

	/**
	 * the test expression for this highlight rule.
	 */
	protected Expression testExpression = null;

	/**
	 *
	 */
	protected Boolean isDesignTime = null;

	/**
	 * Default constructor.
	 */

	public StyleRule() {
	}

	/**
	 * Constructs the style rule with an operator and its operands.
	 *
	 * @param op       the choice name for the operand
	 * @param v1       expression for the first operand
	 * @param v2       expression for the second operand
	 * @param testExpr the expression to check
	 */

	public StyleRule(String op, String v1, String v2, String testExpr) {
		operator = op;
		value1 = new ArrayList();
		value1.add(new Expression(v1, null));
		value2 = new Expression(v2, null);
		testExpression = new Expression(testExpr, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.PropertyStructure#getIntrinsicProperty
	 * (java.lang.String)
	 */

	@Override
	protected Object getIntrinsicProperty(String propName) {
		if (OPERATOR_MEMBER.equals(propName)) {
			return operator;
		} else if (VALUE1_MEMBER.equals(propName)) {
			return value1;
		} else if (VALUE2_MEMBER.equals(propName)) {
			return value2;
		} else if (TEST_EXPR_MEMBER.equals(propName)) {
			return testExpression;
		} else if (IS_DESIGN_TIME_MEMBER.equals(propName)) {
			return isDesignTime;
		}

		return super.getIntrinsicProperty(propName);
	} /*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.report.model.core.PropertyStructure#setIntrinsicProperty
		 * (java.lang.String, java.lang.Object)
		 */

	@Override
	protected void setIntrinsicProperty(String propName, Object value) {
		if (OPERATOR_MEMBER.equals(propName)) {
			operator = (String) value;
		} else if (VALUE1_MEMBER.equals(propName)) {
			if (value == null) {
				value1 = null;
				return;
			}

			if (value instanceof List) {
				value1 = (List<String>) value;
			} else {
				value1 = new ArrayList();
				value1.add(value);
			}
		} else if (VALUE2_MEMBER.equals(propName)) {
			value2 = (Expression) value;
		} else if (TEST_EXPR_MEMBER.equals(propName)) {
			testExpression = (Expression) value;
		} else if (IS_DESIGN_TIME_MEMBER.equals(propName)) {
			isDesignTime = (Boolean) value;
		} else {
			super.setIntrinsicProperty(propName, value);
		}
	}

	/**
	 * Returns the operator. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
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

	public String getOperator() {
		return (String) getProperty(null, OPERATOR_MEMBER);
	}

	/**
	 * Sets the operator. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
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
	 * @param operator the operator to set
	 */

	public void setOperator(String operator) {
		setProperty(OPERATOR_MEMBER, operator);
	}

	/**
	 * Returns the expression for the first operand.
	 *
	 * @return the first operand expression
	 */

	public String getValue1() {
		List valueList = getValue1List();
		if (valueList == null || valueList.isEmpty()) {
			return null;
		}
		return (String) valueList.get(0);
	}

	/**
	 * Gets the value1 expression list. For most map operator, there is only one
	 * expression in the returned list. However, map operator 'in' may contain more
	 * than one expression.
	 *
	 * @return the value1 expression list.
	 *
	 * @deprecated {@link #getValue1ExpressionList()}
	 */
	@Deprecated
	public List getValue1List() {
		List<Expression> valueList = (List<Expression>) getProperty(null, VALUE1_MEMBER);
		if (valueList == null || valueList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return Collections.unmodifiableList(ModelUtil.getExpressionCompatibleList(valueList));
	}

	/**
	 * Gets the value1 expression list. For most map operator, there is only one
	 * expression in the returned list. However, map operator 'in' may contain more
	 * than one expression.
	 *
	 * @return the value1 expression list. Each item is <code>Expression</code>
	 *         object.
	 */
	public List getValue1ExpressionList() {
		List<Expression> valueList = (List<Expression>) getProperty(null, VALUE1_MEMBER);
		if (valueList == null || valueList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return Collections.unmodifiableList(valueList);
	}

	/**
	 * Set expression for the first operand.
	 *
	 * @param value the first operand expression.
	 */

	public void setValue1(String value) {
		if (value == null) {
			setProperty(VALUE1_MEMBER, null);
			return;
		}
		List valueList = new ArrayList();
		valueList.add(value);
		setProperty(VALUE1_MEMBER, valueList);
	}

	/**
	 * Sets the value 1 expression.
	 *
	 * @param value1List the value 1 expression list to set
	 */

	public void setValue1(List value1List) {
		setProperty(VALUE1_MEMBER, value1List);
	}

	/**
	 * Returns the expression for the second operand.
	 *
	 * @return the second operand expression
	 */

	public String getValue2() {
		return getStringProperty(null, VALUE2_MEMBER);
	}

	/**
	 * Set expression for the second operand.
	 *
	 * @param value the second operand expression.
	 */

	public void setValue2(String value) {
		setProperty(VALUE2_MEMBER, value);
	}

	/**
	 * sets the test expression for the rule.
	 *
	 * @param expression the expression value
	 *
	 */
	public void setTestExpression(String expression) {
		setProperty(TEST_EXPR_MEMBER, expression);
	}

	/**
	 * gets the test expression value of this rule.
	 *
	 * @return the expression
	 */
	public String getTestExpression() {
		return testExpression == null ? null : testExpression.getStringExpression();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#isDesignTime()
	 */
	@Override
	public boolean isDesignTime() {
		Boolean isDesignTime = (Boolean) getProperty(null, IS_DESIGN_TIME_MEMBER);
		if (isDesignTime == null) {
			return true;
		}
		return isDesignTime.booleanValue();
	}

	/**
	 * Sets the design time status for this structure.
	 *
	 * @param isDesignTime
	 */
	public void setDesignTime(boolean isDesignTime) {
		setProperty(IS_DESIGN_TIME_MEMBER, Boolean.valueOf(isDesignTime));
	}

}
