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

import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Implements hide rules of a <code>ReportItem</code>.
 * <p>
 * Member properties in <code>Hide</code> are all intrinsic properties. That is,
 * values of "format" and "valueExpr" properties can be unset.
 * 
 * Choices for the "format" member are defined in
 * <code>DesignChoiceConstants</code>.
 * 
 * @see DesignChoiceConstants
 */

public class HideRule extends Structure {

	/**
	 * Name of the format member.
	 */

	public static final String FORMAT_MEMBER = "format"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the expression for the format member.
	 */

	public static final String VALUE_EXPR_MEMBER = "valueExpr"; //$NON-NLS-1$

	/**
	 * Name of this structure within the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "HideRule"; //$NON-NLS-1$

	/**
	 * The comparison operator. The default value is <code>FORMAT_TYPE_ALL</code>.
	 * The allowed choices are:
	 * <ul>
	 * <li><code>FORMAT_TYPE_ALL</code>
	 * <li><code>FORMAT_TYPE_VIEWER</code>
	 * <li><code>FORMAT_TYPE_EMAIL</code>
	 * <li><code>FORMAT_TYPE_PRINT</code>
	 * <li><code>FORMAT_TYPE_PDF</code>
	 * <li><code>FORMAT_TYPE_RTF</code>
	 * <li><code>FORMAT_TYPE_REPORTLET</code>
	 * <li><code>FORMAT_TYPE_EXCEL</code>
	 * <li><code>FORMAT_TYPE_WORD</code>
	 * <li><code>FORMAT_TYPE_POWERPOINT</code>
	 * </ul>
	 */

	protected String format = null;

	/**
	 * The expression for the visibility rule.
	 */

	protected Expression expression = null;

	/**
	 * The default Constructor.
	 */

	public HideRule() {
		super();
	}

	/**
	 * Constructs the hide structure with the format choice and expression.
	 * 
	 * @param format     the choice name for the format
	 * @param expression the expression for the visibility rule
	 */

	public HideRule(String format, String expression) {
		this.format = format;
		this.expression = new Expression(expression, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (FORMAT_MEMBER.equalsIgnoreCase(propName))
			format = (String) value;
		else if (VALUE_EXPR_MEMBER.equalsIgnoreCase(propName)) {
			expression = (Expression) value;
		} else
			assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (FORMAT_MEMBER.equals(propName))
			return format;
		if (VALUE_EXPR_MEMBER.equals(propName))
			return expression;

		assert false;
		return null;
	}

	/**
	 * Returns the format as an internal choice name. The possible choices are
	 * defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants} and
	 * they are:
	 * <ul>
	 * <li><code>FORMAT_TYPE_ALL</code>
	 * <li><code>FORMAT_TYPE_VIEWER</code>
	 * <li><code>FORMAT_TYPE_EMAIL</code>
	 * <li><code>FORMAT_TYPE_PRINT</code>
	 * <li><code>FORMAT_TYPE_PDF</code>
	 * <li><code>FORMAT_TYPE_RTF</code>
	 * <li><code>FORMAT_TYPE_REPORTLET</code>
	 * <li><code>FORMAT_TYPE_EXCEL</code>
	 * <li><code>FORMAT_TYPE_WORD</code>
	 * <li><code>FORMAT_TYPE_POWERPOINT</code>
	 * </ul>
	 * Or can be user defined format.
	 * 
	 * @return the choice code for the format
	 */

	public String getFormat() {
		return (String) getProperty(null, FORMAT_MEMBER);
	}

	/**
	 * Sets the output format. The allowed choices are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants} and
	 * they are:
	 * <ul>
	 * <li><code>FORMAT_TYPE_ALL</code>
	 * <li><code>FORMAT_TYPE_VIEWER</code>
	 * <li><code>FORMAT_TYPE_EMAIL</code>
	 * <li><code>FORMAT_TYPE_PRINT</code>
	 * <li><code>FORMAT_TYPE_PDF</code>
	 * <li><code>FORMAT_TYPE_RTF</code>
	 * <li><code>FORMAT_TYPE_REPORTLET</code>
	 * <li><code>FORMAT_TYPE_EXCEL</code>
	 * <li><code>FORMAT_TYPE_WORD</code>
	 * <li><code>FORMAT_TYPE_POWERPOINT</code>
	 * </ul>
	 * Or can be user defined format.
	 * 
	 * @param format the output format to set
	 */

	public void setFormat(String format) {
		setProperty(FORMAT_MEMBER, format);
	}

	/**
	 * Returns the expression of the rule.
	 * 
	 * @return the value expression
	 */

	public String getExpression() {
		return getStringProperty(null, VALUE_EXPR_MEMBER);
	}

	/**
	 * Sets the expression for this visibility rule.
	 * 
	 * @param expression the value expression to set
	 */

	public void setExpression(String expression) {
		setProperty(VALUE_EXPR_MEMBER, expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new HideRuleHandle(valueHandle, index);
	}

	/**
	 * Validates this structure. The following are the rules:
	 * <ul>
	 * <li>The column name is required.
	 * </ul>
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(Module,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List<SemanticException> validate(Module module, DesignElement element) {
		List<SemanticException> list = super.validate(module, element);

		PropertyDefn propDefn = (PropertyDefn) getDefn().getMember(FORMAT_MEMBER);
		try {
			propDefn.validateValue(module, element, format);
		} catch (PropertyValueException e) {
			list.add(e);
		}

		return list;
	}

}
