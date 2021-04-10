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
import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * This class presents the parameter binding that bind data set input parameter
 * to expression by position. Order of these bindings must match the order of
 * parameter markers ("?"") in the statement. Each parameter binding has the
 * following properties:
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

public class ParamBinding extends Structure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String PARAM_BINDING_STRUCT = "ParamBinding"; //$NON-NLS-1$

	/**
	 * Name of the parameter name member.
	 */

	public static final String PARAM_NAME_MEMBER = "paramName"; //$NON-NLS-1$

	/**
	 * Name of the parameter binding expression member.
	 */

	public static final String EXPRESSION_MEMBER = "expression"; //$NON-NLS-1$

	/**
	 * The parameter name.
	 */

	private String paramName = null;

	/**
	 * The parameter expression expression.
	 */

	private List<Expression> expressions = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return PARAM_BINDING_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (PARAM_NAME_MEMBER.equals(propName))
			return paramName;
		if (EXPRESSION_MEMBER.equals(propName))
			return expressions;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (PARAM_NAME_MEMBER.equals(propName))
			paramName = (String) value;
		else if (EXPRESSION_MEMBER.equals(propName)) {
			expressions = (List) value;
		} else
			assert false;

	}

	/**
	 * Returns the parameter name of this binding.
	 * 
	 * @return the parameter name of this binding
	 */

	public String getParamName() {
		return (String) getProperty(null, PARAM_NAME_MEMBER);
	}

	/**
	 * Sets the parameter name of this binding.
	 * 
	 * @param name the parameter name to set
	 */

	public void setParamName(String name) {
		setProperty(PARAM_NAME_MEMBER, name);
	}

	/**
	 * Returns the binding expression.
	 * 
	 * @return the binding expression
	 * @deprecated replaced by {@link #getExpressionList()}
	 */

	public String getExpression() {
		List<Expression> values = getExpressionList();
		if (values == null || values.isEmpty())
			return null;
		return values.get(0).getStringExpression();
	}

	/**
	 * Returns the list of the expressions.
	 * 
	 * @return
	 */
	public List<Expression> getExpressionList() {
		return (List<Expression>) getProperty(null, EXPRESSION_MEMBER);
	}

	/**
	 * Sets the binding expression.
	 * 
	 * @param expression the expression to set
	 * @deprecated by {@link #setExpression(List)}
	 */

	public void setExpression(String expression) {
		if (expression == null) {
			setProperty(EXPRESSION_MEMBER, null);
			return;
		}

		List<String> values = new ArrayList<String>();
		values.add(expression);
		setProperty(EXPRESSION_MEMBER, values);
	}

	/**
	 * Sets the binding expression list.
	 * 
	 * @param values the list of expressions to set
	 */

	public void setExpression(List<Expression> values) {
		setProperty(EXPRESSION_MEMBER, values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt
	 * .report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate(Module module, DesignElement element) {
		ArrayList list = new ArrayList();

		if (StringUtil.isBlank(getParamName())) {
			list.add(new PropertyValueException(element, getDefn().getMember(PARAM_NAME_MEMBER), getParamName(),
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new ParamBindingHandle(valueHandle, index);
	}

}