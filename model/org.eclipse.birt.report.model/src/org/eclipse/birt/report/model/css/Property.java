/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public String getName() {
		return name;
	}

	public CSSValue getValue() {
		return value;
	}

	public void setValue(CSSValue value) {
		this.value = value;
	}

	public String toString() {
		return name + ": " + value.toString(); //$NON-NLS-1$
	}
}