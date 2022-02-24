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

import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.util.StructureContextUtil;

/**
 * Represents the handle of parameter binding. The parameter binding binds data
 * set input parameter to expression by position. Order of these bindings must
 * match the order of parameter markers ("?"") in the statement. Each parameter
 * binding has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Parameter Name </strong></dt>
 * <dd>a parameter bing has a required parameter name to bind.</dd>
 * 
 * <dt><strong>Expression </strong></dt>
 * <dd>associated an expression with a named input parameter.</dd>
 * </dl>
 * 
 */

public class ParamBindingHandle extends StructureHandle {

	/**
	 * Constructs the handle of parameter binding.
	 * 
	 * @param valueHandle the value handle for parameter binding list of one
	 *                    property
	 * @param index       the position of this parameter binding in the list
	 */

	public ParamBindingHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the expression the parameter is binded to.
	 * 
	 * @return the expression the parameter is binded to
	 */

	public String getExpression() {
		List<Expression> values = (List<Expression>) getProperty(ParamBinding.EXPRESSION_MEMBER);
		if (values == null || values.isEmpty())
			return null;
		return values.get(0).getStringExpression();
	}

	/**
	 * Sets the expression the parameter is binded to.
	 * 
	 * @param expression the expression to bind
	 */

	public void setExpression(String expression) {
		setPropertySilently(ParamBinding.EXPRESSION_MEMBER, expression);
	}

	/**
	 * Gets the expression list of this parameter binding. * @return the value1
	 * expression list handle of this filter condition.
	 */

	public ExpressionListHandle getExpressionListHandle() {
		return new ExpressionListHandle(elementHandle,
				StructureContextUtil.createStructureContext(this, ParamBinding.EXPRESSION_MEMBER));
	}

	/**
	 * Returns the parameter name.
	 * 
	 * @return the parameter name
	 */

	public String getParamName() {
		return getStringProperty(ParamBinding.PARAM_NAME_MEMBER);
	}

	/**
	 * Sets the parameter name.
	 * 
	 * @param name the parameter name to set
	 */

	public void setParamName(String name) {
		setPropertySilently(ParamBinding.PARAM_NAME_MEMBER, name);
	}
}
