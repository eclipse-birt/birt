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

package org.eclipse.birt.report.engine.ir;

import org.eclipse.birt.report.engine.content.IStyle;

/**
 *
 */
public class HighlightRuleDesign extends RuleDesign {

	public HighlightRuleDesign(HighlightRuleDesign rule) {
		this.operator = rule.operator;
		this.expr = rule.expr;
		this.style = rule.style;
		this.testExpression = rule.testExpression;
		this.value1 = rule.value1;
		this.value2 = rule.value2;
	}

	public HighlightRuleDesign() {
	}

	/**
	 * style defined in this rule.
	 */
	protected IStyle style;

	/**
	 * @return Returns the style.
	 */
	public IStyle getStyle() {
		return style;
	}

	/**
	 * @param style The style to set.
	 */
	public void setStyle(IStyle style) {
		this.style = style;
	}

}
