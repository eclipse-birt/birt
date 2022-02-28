/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class HighlightDesign {

	/**
	 * rules in this highlight
	 */
	protected ArrayList<HighlightRuleDesign> rules = new ArrayList<>();

	/**
	 * get rule count.
	 *
	 * @return rule count
	 */
	public int getRuleCount() {
		return this.rules.size();
	}

	/**
	 * add rule into this hightlight.
	 *
	 * @param rule rule to be added
	 */
	public void addRule(HighlightRuleDesign rule) {
		this.rules.add(rule);
	}

	public Collection<HighlightRuleDesign> getRules() {
		return rules;
	}

	/**
	 * get rule at index.
	 *
	 * @param index index of the rule
	 * @return rule defined in this hightlight
	 */
	public HighlightRuleDesign getRule(int index) {
		assert (index >= 0 && index < this.rules.size());
		return (HighlightRuleDesign) this.rules.get(index);
	}
}
