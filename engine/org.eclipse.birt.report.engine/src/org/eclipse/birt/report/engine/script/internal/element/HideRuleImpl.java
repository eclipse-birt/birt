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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IHideRule;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * Implements of Hide Rule.
 */

public class HideRuleImpl implements IHideRule {

	private org.eclipse.birt.report.model.api.simpleapi.IHideRule hideRuleImpl;

	/**
	 * Constructor
	 * 
	 * @param ruleHandle
	 */

	public HideRuleImpl() {
		hideRuleImpl = SimpleElementFactory.getInstance().createHideRule();
	}

	/**
	 * Constructor
	 * 
	 * @param ruleHandle
	 */

	public HideRuleImpl(HideRuleHandle ruleHandle) {
		hideRuleImpl = SimpleElementFactory.getInstance().createHideRule(ruleHandle);
	}

	/**
	 * Constructor
	 * 
	 * @param rule
	 */
	public HideRuleImpl(HideRule rule) {
		hideRuleImpl = SimpleElementFactory.getInstance().createHideRule(rule);
	}

	public HideRuleImpl(org.eclipse.birt.report.model.api.simpleapi.IHideRule hideRule) {
		hideRuleImpl = hideRule;
	}

	public String getFormat() {
		return hideRuleImpl.getFormat();
	}

	public String getValueExpr() {
		return hideRuleImpl.getValueExpr();
	}

	public void setFormat(String format) throws ScriptException {
		try {
			hideRuleImpl.setFormat(format);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	public void setValueExpr(String valueExpr) throws ScriptException {
		try {

			hideRuleImpl.setValueExpr(valueExpr);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	public IStructure getStructure() {
		return hideRuleImpl.getStructure();
	}

}
