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

package org.eclipse.birt.report.model.css;

import java.io.Serializable;

import org.w3c.css.sac.SelectorList;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * Implements <code>CSSStyleRule</code> to represent a style rule in the CSS
 * style sheet.
 */

public class StyleRule implements CSSStyleRule, Serializable {

	/**
	 * Document for <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 5350775872002094538L;

	private SelectorList selectors = null;
	private CSSStyleDeclaration style = null;

	/**
	 * Constructor
	 *
	 * @param selectors selector list
	 */
	public StyleRule(SelectorList selectors) {
		this.selectors = selectors;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSStyleRule#getSelectorText()
	 */

	@Override
	public String getSelectorText() {
		return selectors.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSStyleRule#setSelectorText(java.lang.String)
	 */
	@Override
	public void setSelectorText(String selectorText) throws DOMException {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSStyleRule#getStyle()
	 */
	@Override
	public CSSStyleDeclaration getStyle() {
		return this.style;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSRule#getType()
	 */

	@Override
	public short getType() {
		return STYLE_RULE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSRule#getCssText()
	 */

	@Override
	public String getCssText() {
		return getSelectorText() + " " + getStyle().toString(); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSRule#setCssText(java.lang.String)
	 */

	@Override
	public void setCssText(String cssText) throws DOMException {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSRule#getParentRule()
	 */

	@Override
	public CSSRule getParentRule() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSRule#getParentStyleSheet()
	 */

	@Override
	public CSSStyleSheet getParentStyleSheet() {
		return null;
	}

	/**
	 * Sets the style declaration of the rule.
	 *
	 * @param style the style declaration to set
	 */

	public void setStyle(StyleDeclaration style) {
		this.style = style;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return getCssText();
	}

	/**
	 * Gets the selector list of the style rule.
	 *
	 * @return the selector list of the style rule
	 */

	public SelectorList getSelectorList() {
		return this.selectors;
	}
}
