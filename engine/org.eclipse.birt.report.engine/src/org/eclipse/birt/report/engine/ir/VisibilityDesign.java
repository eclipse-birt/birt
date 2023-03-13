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
 * Visibility Design.
 *
 *
 */
public class VisibilityDesign {
	/**
	 * Stores the VisibilityRuleDesign items.
	 */
	protected ArrayList<VisibilityRuleDesign> rules = new ArrayList<>();

	/**
	 * Adds the VisibilityRuleDesign
	 *
	 * @param rule the VisibilityRuleDesign
	 */
	public void addRule(VisibilityRuleDesign rule) {
		assert rule != null;
		rules.add(rule);
	}

	public Collection<VisibilityRuleDesign> getRules() {
		return rules;
	}

	/**
	 * Gets the count of items
	 *
	 * @return the count of the VisibilityRuleDesign
	 */
	public int count() {
		return rules.size();
	}

	/**
	 * Gets the VisibilityRuleDesign according to the specified index.
	 *
	 * @param index the specified index
	 * @return the VisibilityRuleDesign at the specified index
	 */
	public VisibilityRuleDesign getRule(int index) {
		assert index >= 0 && index < rules.size();
		return (VisibilityRuleDesign) rules.get(index);
	}
}
