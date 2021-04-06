/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.css;

import java.io.Serializable;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSUnknownRule;

/**
 * Implements an unSupported rule by Model.
 * 
 */

public class UnSupportedRule implements CSSUnknownRule, Serializable {

	/**
	 * Document for <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = -1643330060195374902L;

	/**
	 * The text of the rule.
	 */

	private String text = null;

	/**
	 * Constructs an unsupported rule with rule text.
	 * 
	 * @param text the rule text
	 */

	public UnSupportedRule(String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSRule#getType()
	 */

	public short getType() {
		return UNKNOWN_RULE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSRule#getCssText()
	 */

	public String getCssText() {
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSRule#setCssText(java.lang.String)
	 */

	public void setCssText(String cssText) throws DOMException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSRule#getParentRule()
	 */

	public CSSRule getParentRule() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSRule#getParentStyleSheet()
	 */

	public CSSStyleSheet getParentStyleSheet() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString() {
		return getCssText();
	}

}