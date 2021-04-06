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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Base class for property types stored as literal strings.
 * 
 */

public abstract class TextualPropertyType extends PropertyType {

	/**
	 * The no trim value.
	 */
	static final int NO_VALUE = 0;

	/**
	 * The value of the operation which won't trim the input string.
	 */
	static final int NO_TRIM_VALUE = 1;

	/**
	 * The value of the operation which will trim the space.
	 */
	static final int TRIM_SPACE_VALUE = 2;

	/**
	 * The value of the operation which will normalizes the empty string to an null
	 * string.
	 */
	static final int TRIM_EMPTY_TO_NULL_VALUE = 4;

	/**
	 * Constructor
	 * 
	 * @param displayNameID display name id of the property type.
	 */

	TextualPropertyType(String displayNameID) {
		super(displayNameID);
	}

	/**
	 * Validates a generic string. An empty string will never be returned.
	 * 
	 * @return the value as a string
	 */

	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null)
			return null;
		if (value instanceof String) {
			return trimString((String) value, defn.getTrimOption());
		}
		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#toString(java
	 * .lang.Object)
	 */

	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value instanceof Expression) {
			return ((Expression) value).toString();
		}
		return (String) value;
	}

	/**
	 * Trims a string according to the trim option.
	 * 
	 * @param value      the input value.
	 * @param trimOption the trim option.
	 * @return the output value.
	 */
	protected String trimString(String value, int trimOption) {
		if (value == null)
			return null;

		if ((trimOption & TRIM_SPACE_VALUE) != 0)
			value = value.trim();
		if ((trimOption & TRIM_EMPTY_TO_NULL_VALUE) != 0) {
			if (value.length() == 0)
				value = null;
		}
		return value;
	}

}
