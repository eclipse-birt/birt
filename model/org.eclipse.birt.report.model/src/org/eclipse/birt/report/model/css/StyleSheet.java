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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.stylesheets.MediaList;

/**
 * Represents a style sheet from an external CSS2 file. It stores all the rules
 * loaded.
 */

public class StyleSheet implements CSSStyleSheet {

	/**
	 * The list to store all the rules.
	 */

	private List<CSSRule> rules = new ArrayList<CSSRule>();

	/**
	 * Default constructor.
	 * 
	 */

	public StyleSheet() {

	}

	/**
	 * Gets the rule list of the style sheet.
	 * 
	 * @return the rule list
	 */

	public List<CSSRule> getRules() {
		return rules;
	}

	/**
	 * Adds a rule into the tail of the style sheet.
	 * 
	 * @param rule the rule to add
	 */

	public void add(CSSRule rule) {
		rules.add(rule);
	}

	/**
	 * Inserts a rule to the given position of the style sheet.
	 * 
	 * @param rule  the rule to insert
	 * @param index the position to insert
	 */

	public void insert(CSSRule rule, int index) {
		rules.add(index, rule);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < rules.size(); i++) {
			sb.append(rules.get(i).toString()).append("\r\n"); //$NON-NLS-1$
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSStyleSheet#deleteRule(int)
	 */

	public void deleteRule(int index) throws DOMException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSStyleSheet#insertRule(java.lang.String, int)
	 */

	public int insertRule(String rule, int index) throws DOMException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSStyleSheet#getOwnerRule()
	 */

	public CSSRule getOwnerRule() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSStyleSheet#getCssRules()
	 */

	public CSSRuleList getCssRules() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.stylesheets.StyleSheet#getDisabled()
	 */

	public boolean getDisabled() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.stylesheets.StyleSheet#setDisabled(boolean)
	 */

	public void setDisabled(boolean disabled) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.stylesheets.StyleSheet#getHref()
	 */

	public String getHref() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.stylesheets.StyleSheet#getTitle()
	 */

	public String getTitle() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.stylesheets.StyleSheet#getType()
	 */

	public String getType() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.stylesheets.StyleSheet#getOwnerNode()
	 */

	public Node getOwnerNode() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.stylesheets.StyleSheet#getMedia()
	 */

	public MediaList getMedia() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.stylesheets.StyleSheet#getParentStyleSheet()
	 */

	public org.w3c.dom.stylesheets.StyleSheet getParentStyleSheet() {
		return null;
	}
}
