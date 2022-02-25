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

import org.eclipse.birt.report.model.api.elements.structures.Rule;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.StructureContext;

/**
 * Represents a rule handle.
 *
 */

public class RuleHandle extends StructureHandle {

	/**
	 * Construct an handle to deal with the action structure.
	 *
	 * @param element the element that defined the action.
	 * @param context context to the rule structure property.
	 */

	public RuleHandle(DesignElementHandle element, StructureContext context) {
		super(element, context);
	}

	/**
	 * Construct an handle to deal with the action structure.
	 *
	 * @param element the element that defined the action.
	 * @param context context to the rule structure property
	 * @deprecated
	 */

	@Deprecated
	public RuleHandle(DesignElementHandle element, MemberRef context) {
		super(element, context);
	}

	/**
	 * Constructs the handle of configuration variable.
	 *
	 * @param valueHandle the value handle for configuration variable list of one
	 *                    property
	 * @param index       the position of this configuration variable in the list
	 */

	public RuleHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the static value of this rule.
	 *
	 * @return the static value
	 */

	public String getRuleExpression() {
		return getStringProperty(Rule.RULE_EXPRE_MEMBER);
	}

	/**
	 * Sets the static value of the rule.
	 *
	 * @param expre the static value to set
	 *
	 */

	public void setRuleExpression(String expre) {
		setPropertySilently(Rule.RULE_EXPRE_MEMBER, expre);
	}

	/**
	 * Returns the display value of the rule.
	 *
	 * @return the display value
	 */

	public String getDisplayExpression() {
		return getStringProperty(Rule.DISPLAY_EXPRE_MEMBER);
	}

	/**
	 * Sets the display value of this rule.
	 *
	 * @param expre the display value to set
	 */

	public void setDisplayExpression(String expre) {
		setPropertySilently(Rule.DISPLAY_EXPRE_MEMBER, expre);
	}
}
