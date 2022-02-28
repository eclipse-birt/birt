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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.simpleapi.IHideRule;

/**
 * Implements of Hide Rule.
 */

public class HideRuleImpl extends Structure implements IHideRule {

	private HideRule rule;

	/**
	 * Constructor
	 *
	 * @param ruleHandle
	 */

	public HideRuleImpl() {
		super(null);
		rule = createHideRule();
	}

	/**
	 * Constructor
	 *
	 * @param ruleHandle
	 */

	public HideRuleImpl(HideRuleHandle ruleHandle) {
		super(ruleHandle);
		if (ruleHandle == null) {
			rule = createHideRule();
		} else {
			structureHandle = ruleHandle;
			rule = (HideRule) ruleHandle.getStructure();
		}
	}

	/**
	 * Constructor
	 *
	 * @param rule
	 */
	public HideRuleImpl(HideRule rule) {
		super(null);
		if (rule == null) {
			this.rule = createHideRule();
		} else {
			this.rule = rule;
		}
	}

	/**
	 * Create instance of <code>HideRule</code>
	 *
	 * @return instance
	 */
	private HideRule createHideRule() {
		HideRule r = new HideRule();
		return r;
	}

	@Override
	public String getFormat() {
		return rule.getFormat();
	}

	@Override
	public String getValueExpr() {
		return rule.getExpression();
	}

	@Override
	public void setFormat(String format) throws SemanticException {
		if (structureHandle != null) {
			setProperty(HideRule.FORMAT_MEMBER, format);
			return;
		}

		rule.setFormat(format);
	}

	@Override
	public void setValueExpr(String valueExpr) throws SemanticException {
		if (structureHandle != null) {
			setProperty(HideRule.VALUE_EXPR_MEMBER, valueExpr);
			return;
		}

		rule.setExpression(valueExpr);
	}

	@Override
	public IStructure getStructure() {
		return rule;
	}

}
