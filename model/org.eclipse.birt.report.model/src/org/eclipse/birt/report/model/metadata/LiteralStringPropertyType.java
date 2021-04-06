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

import org.eclipse.birt.report.model.core.Module;

/**
 * Literal string property type.
 * <p>
 * All string values are valid. When <code>LiteralStringPropertyType</code>
 * receives an empty string it will return an empty string. It is different from
 * <code>StringPropertyType</code>
 * 
 */

public class LiteralStringPropertyType extends TextualPropertyType {

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.literalString"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public LiteralStringPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return LITERAL_STRING_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName() {
		return LITERAL_STRING_TYPE_NAME;
	}

	/**
	 * Converts the string property value to a double, this method will always
	 * return 0.
	 */

	public double toDouble(Module module, Object value) {
		// Strings cannot be converted to doubles because the conversion
		// rules are locale-dependent.

		return 0;
	}

	/**
	 * Converts the string property value to an integer.
	 * 
	 * @return integer value of the string representation, return <code>0</code> if
	 *         <code>value</code> is null.
	 */

	public int toInteger(Module module, Object value) {
		if (value == null)
			return 0;

		try {
			return Integer.decode((String) value).intValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}

}
