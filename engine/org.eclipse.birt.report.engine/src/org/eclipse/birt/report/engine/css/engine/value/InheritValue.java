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

package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.css.CSSValue;

/**
 * This singleton class represents the 'inherit' value.
 * 
 */
public class InheritValue extends Value {

	/**
	 * The only instance of this class.
	 */
	public final static InheritValue INSTANCE = new InheritValue();

	/**
	 * Creates a new InheritValue object.
	 */
	protected InheritValue() {
	}

	/**
	 * A string representation of the current value.
	 */
	public String getCssText() {
		return "inherit";
	}

	/**
	 * A code defining the type of the value.
	 */
	public short getCssValueType() {
		return CSSValue.CSS_INHERIT;
	}

	/**
	 * Returns a printable representation of this object.
	 */
	public String toString() {
		return getCssText();
	}
}
