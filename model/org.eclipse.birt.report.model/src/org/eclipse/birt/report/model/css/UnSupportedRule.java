/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
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

	@Override
	public short getType() {
		return UNKNOWN_RULE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSRule#getCssText()
	 */

	@Override
	public String getCssText() {
		return text;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return getCssText();
	}

}
