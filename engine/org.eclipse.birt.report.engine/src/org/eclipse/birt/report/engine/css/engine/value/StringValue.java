/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.DOMException;

/**
 * This class represents string values.
 * 
 */
public class StringValue extends Value {
	/**
	 * The value of the string
	 */
	protected String value;

	/**
	 * The unit type
	 */
	protected short unitType;

	/**
	 * Creates a new StringValue.
	 */
	public StringValue(short type, String s) {
		unitType = type;
		value = s;
	}

	/**
	 * The type of the value.
	 */
	public short getPrimitiveType() {
		return unitType;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj the reference object with which to compare.
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof StringValue)) {
			return false;
		}
		StringValue v = (StringValue) obj;
		if (unitType != v.unitType) {
			return false;
		}
		if (value != null) {
			return value.equals(v.value);
		} else {
			if (v.value == null) {
				return true;
			}
		}
		return false;

	}

	/**
	 * A string representation of the current value.
	 */
	public String getCssText() {
		return value;
	}

	/**
	 * This method is used to get the string value.
	 * 
	 * @exception DOMException INVALID_ACCESS_ERR: Raised if the value doesn't
	 *                         contain a string value.
	 */
	public String getStringValue() throws DOMException {
		return value;
	}

	/**
	 * Returns a printable representation of this value.
	 */
	public String toString() {
		return value;
	}
}
