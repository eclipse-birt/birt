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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;

/**
 * Represents the handle of visibility rule. The visibility rule says when a
 * report item should be hidden. It can be hidden based on the output type, an
 * expression, or both. For example, the browser control is normally hidden in
 * all output formats except HTML. A past-due item might be hidden if the
 * account is not past due.
 */

public class HideRuleHandle extends StructureHandle {

	/**
	 * Constructs the handle of visibility rule.
	 * 
	 * @param valueHandle the value handle for visibility rule list of one property
	 * @param index       the position of this visibility rule in the list
	 */

	public HideRuleHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the output format of this visibility rule. The default value is
	 * <code>FORMAT_TYPE_ALL</code>. The possible choices are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}and
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
	 * 
	 * Or can be user defined format.
	 * 
	 * @return the output format of this visibility rule
	 */

	public String getFormat() {
		return getStringProperty(HideRule.FORMAT_MEMBER);
	}

	/**
	 * Sets the output format of this visibility rule. The allowed choices are
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
	 * 
	 * Or can be user defined format.
	 * 
	 * @param format the output format to set
	 * @throws SemanticException if the property is locked.
	 */

	public void setFormat(String format) throws SemanticException {
		setProperty(HideRule.FORMAT_MEMBER, format);
	}

	/**
	 * Returns the value expression of this visibility rule.
	 * 
	 * @return the value expression of this visibility rule
	 */

	public String getExpression() {
		return getStringProperty(HideRule.VALUE_EXPR_MEMBER);
	}

	/**
	 * Sets the value expression of this visibility rule.
	 * 
	 * @param expression the value expression to set
	 */

	public void setExpression(String expression) {
		setPropertySilently(HideRule.VALUE_EXPR_MEMBER, expression);
	}
}