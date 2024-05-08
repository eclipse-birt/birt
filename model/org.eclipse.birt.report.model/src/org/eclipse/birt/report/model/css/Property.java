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

import org.w3c.dom.css.CSSValue;

/**
 * Provides the name/value pair of the property value in the style declaration.
 */

public class Property implements Serializable {

	/**
	 * Document for <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 3064124971792953691L;
	private String name;
	private CSSValue value;

	/**
	 * Creates new Property.
	 *
	 * @param name  name of the property
	 * @param value value of the property
	 */
	public Property(String name, CSSValue value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Get CSS property name
	 *
	 * @return Return the CSS property name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the CSS value
	 *
	 * @return Return the CSS value
	 */
	public CSSValue getValue() {
		return value;
	}

	/**
	 * Set the CSS value
	 *
	 * @param value CSS value
	 */
	public void setValue(CSSValue value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return name + ": " + value.toString(); //$NON-NLS-1$
	}
}
