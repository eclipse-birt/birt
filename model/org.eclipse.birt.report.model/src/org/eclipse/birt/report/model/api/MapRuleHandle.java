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

import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;

/**
 * Represents the handle of map rule. The Map rule represents a style mapping
 * rule: a translation of a database value into a set of display values. A map
 * might translate DB status codes (O, S, P) into user-visible strings (Open,
 * Shipped, Paid). The mapping is driven of of a mapping test expression defined
 * on the style.
 */

public class MapRuleHandle extends StyleRuleHandle {

	/**
	 * Constructs the handle of map rule.
	 *
	 * @param valueHandle the value handle for map rule list of one property
	 * @param index       the position of this map rule in the list
	 */

	public MapRuleHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the display value when this rule applies.
	 *
	 * @return the display value when this rule applies
	 */

	public String getDisplay() {
		return getStringProperty(MapRule.DISPLAY_MEMBER);
	}

	/**
	 * Sets the display value when this rule applies.
	 *
	 * @param display the display value to set
	 */

	public void setDisplay(String display) {
		setPropertySilently(MapRule.DISPLAY_MEMBER, display);
	}

	/**
	 * Returns the resource key of display value.
	 *
	 * @return the resource key of display value.
	 */

	public String getDisplayKey() {
		return getStringProperty(MapRule.DISPLAY_ID_MEMBER);
	}

	/**
	 * Sets the resource key of display value.
	 *
	 * @param displayID the resource key to set
	 */

	public void setDisplayKey(String displayID) {
		setPropertySilently(MapRule.DISPLAY_ID_MEMBER, displayID);
	}

	/**
	 * sets the expression for this map rule.
	 *
	 * @param expression the expression
	 */

	public void setTestExpression(String expression) {

		setPropertySilently(StyleRule.TEST_EXPR_MEMBER, expression);
	}

	/**
	 * sets the test expression for this map rule.
	 *
	 * @return the expression
	 */
	public String getTestExpression() {

		return getStringProperty(StyleRule.TEST_EXPR_MEMBER);

	}
}
