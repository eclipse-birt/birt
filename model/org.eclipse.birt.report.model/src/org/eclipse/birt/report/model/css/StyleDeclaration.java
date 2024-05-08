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
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

/**
 * Implements CSSStyleDeclaration to store all the property values.
 */

public class StyleDeclaration implements CSSStyleDeclaration, Serializable {

	/**
	 * Document for <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = -1098407039390450722L;

	/**
	 * The properties of the declaration.
	 */

	private List<Property> properties = new ArrayList<Property>();

	/**
	 * Default constructor.
	 *
	 */

	public StyleDeclaration() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSStyleDeclaration#getLength()
	 */

	@Override
	public int getLength() {
		return properties.size();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSStyleDeclaration#getCssText()
	 */
	@Override
	public String getCssText() {
		StringBuilder sb = new StringBuilder();
		sb.append("{"); //$NON-NLS-1$

		// if newlines requested in text
		// sb.append("\n");

		for (int i = 0; i < properties.size(); ++i) {
			Property p = properties.get(i);
			if (p != null) {
				sb.append(p.toString());
			}
			if (i < properties.size() - 1) {
				sb.append("; "); //$NON-NLS-1$
			}

			// if newlines requested in text
			// sb.append("\n");
		}
		sb.append("}"); //$NON-NLS-1$
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSStyleDeclaration#item(int)
	 */

	@Override
	public String item(int index) {
		Property p = properties.get(index);
		return (p != null) ? p.getName() : ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSStyleDeclaration#setCssText(java.lang.String)
	 */
	@Override
	public void setCssText(String cssText) throws DOMException {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSStyleDeclaration#getParentRule()
	 */

	@Override
	public CSSRule getParentRule() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.w3c.dom.css.CSSStyleDeclaration#getPropertyPriority(java.lang.String)
	 */

	@Override
	public String getPropertyPriority(String propertyName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSStyleDeclaration#getPropertyValue(java.lang.String)
	 */

	@Override
	public String getPropertyValue(String propertyName) {
		Property p = getPropertyDeclaration(propertyName);
		return (p != null) ? p.getValue().toString() : ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSStyleDeclaration#removeProperty(java.lang.String)
	 */

	@Override
	public String removeProperty(String propertyName) throws DOMException {
		for (int i = 0; i < properties.size(); i++) {
			Property p = properties.get(i);
			if (p.getName().equalsIgnoreCase(propertyName)) {
				properties.get(i);
				return p.getValue().toString();
			}
		}
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.w3c.dom.css.CSSStyleDeclaration#getPropertyCSSValue(java.lang.String)
	 */

	@Override
	public CSSValue getPropertyCSSValue(String propertyName) {
		Property p = getPropertyDeclaration(propertyName);
		return (p != null) ? p.getValue() : null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.w3c.dom.css.CSSStyleDeclaration#setProperty(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void setProperty(String propertyName, String value, String priority) throws DOMException {

	}

	/**
	 * Gets the property with the given name.
	 *
	 * @param name the name of the property to get
	 * @return the property with the name, otherwise null
	 */

	private Property getPropertyDeclaration(String name) {
		for (int i = 0; i < properties.size(); i++) {
			Property p = properties.get(i);
			if (p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Adds a property into the style declaration.
	 *
	 * @param p the property to add
	 */

	public void addProperty(Property p) {
		properties.add(p);
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
