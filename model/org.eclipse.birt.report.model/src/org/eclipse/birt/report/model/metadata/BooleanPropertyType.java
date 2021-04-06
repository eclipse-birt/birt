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

import java.math.BigDecimal;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Boolean property type. Boolean properties are stored as
 * <code>java.lang.Boolean</code> internally.
 */

public class BooleanPropertyType extends PropertyType {

	/**
	 * XML value for the true value.
	 */

	public static final String TRUE = "true"; //$NON-NLS-1$

	/**
	 * XML value for the false value.
	 */

	public static final String FALSE = "false"; //$NON-NLS-1$

	/**
	 * resourceKey for "True" that is defined in message files.
	 */

	public static final String BOOLEAN_TRUE_RESOURCE_KEY = "Property.Boolean.True"; //$NON-NLS-1$

	/**
	 * resourceKey for "False" that is defined in message files.
	 */

	public static final String BOOLEAN_FALSE_RESOURCE_KEY = "Property.Boolean.False"; //$NON-NLS-1$

	/**
	 * Integer value for true.
	 */

	public static final int INT_TRUE = 1;

	/**
	 * Integer value for false.
	 */

	public static final int INT_FALSE = 0;

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.boolean"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public BooleanPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/**
	 * Validates the boolean property value. Possible valid boolean values include:
	 * <ul>
	 * <li>Null, meaning to clear the property value.</li>
	 * <li>"true" and "false" in English, or the equivalents in the current
	 * locale.</li>
	 * <li>"true" and "false" (Java and XML constants).</li>
	 * <li>A Boolean object</li>
	 * <li>An Integer object 0 (false) and non-zero (true).</li>
	 * <li>A Double,Float or BigDecimal object with int value 0 (false) and non-zero
	 * (true).</li>
	 * </ul>
	 * <p>
	 * Boolean property type is stored as <code>java.lang.Boolean</code> internally.
	 * 
	 * @return the value(Boolean Type) to store for the property of type Boolean.
	 *         Returns <code>null</code> if the <code>value</code> parameter is
	 *         null.
	 * 
	 */

	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null)
			return null;

		if (value instanceof String)
			return validateInputString(module, element, defn, (String) value);
		if (value instanceof Boolean)
			return value;

		if (value instanceof Integer || value instanceof Double || value instanceof Float
				|| value instanceof BigDecimal)
			return ((Number) value).intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;

		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, getTypeCode());
	}

	/**
	 * Validates the XML representation of the boolean property value. Possible
	 * valid XML boolean values include:
	 * <ul>
	 * <li>Null or a blank string.</li>
	 * <li>"true" and "false" (Java and XML constants).</li>
	 * </ul>
	 * <p>
	 * 
	 * @return the value(Boolean Type) to store for the property from xml of type
	 *         Boolean. Returns <code>null</code> if the <code>value</code>
	 *         parameter is null or a blank string.
	 */

	public Object validateXml(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		assert value == null || value instanceof String;
		String tmpValue = (String) value;

		tmpValue = StringUtil.trimString(tmpValue);

		if (tmpValue == null)
			return null;

		if (tmpValue.equalsIgnoreCase(TRUE))
			return Boolean.TRUE;
		else if (tmpValue.equalsIgnoreCase(FALSE))
			return Boolean.FALSE;

		throw new PropertyValueException(tmpValue, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return BOOLEAN_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#getName()
	 */

	public String getName() {
		return BOOLEAN_TYPE_NAME;
	}

	/**
	 * Converts a property of the boolean type to a integer value.
	 * <ul>
	 * <li>Value <code>null</code> will be convert into 0.</li>
	 * <li>Value <code>true</code> will be convert into {@link #INT_TRUE}</li>
	 * <li>Value <code>false</code> will be convert into {@link #INT_FALSE}</li>
	 * </ul>
	 * 
	 * @return The boolean value as an integer, return <code>0</code> if value is
	 *         null; return {@link #INT_TRUE}if value is true; return
	 *         {@link #INT_FALSE}if value is false.
	 */

	public int toInteger(Module module, Object value) {
		if (value == null)
			return 0;

		return ((Boolean) value).booleanValue() ? INT_TRUE : INT_FALSE;
	}

	/**
	 * Converts the boolean property value to a locale-independent string.
	 * <ul>
	 * <li>Value <code>null</code> will be convert into null.</li>
	 * <li>Value <code>true</code> will be convert into "true"</li>
	 * <li>Value <code>false</code> will be convert into "false"</li>
	 * </ul>
	 * 
	 * @return The boolean value as a string, return null if value is null; return
	 *         "true" if value is true; return "false" if value is false.
	 */

	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value == null)
			return null;

		return ((Boolean) value).booleanValue() ? TRUE : FALSE;
	}

	/**
	 * Converts the boolean property value to a <code>Boolean</code>, return the
	 * internal representation as a boolean.
	 * <ul>
	 * <li>Value <code>null</code> will be convert into <code>false</code>.</li>
	 * <li>Value <code>true</code> will be convert into <code>true</code></li>
	 * <li>Value <code>false</code> will be convert into <code>false</code></li>
	 * </ul>
	 * 
	 * @return The value as a <code>boolean</code>, return <code>false</code> if
	 *         value is null; return <code>true</code> if value is true; return
	 *         <code>false</code> if value is false.
	 * 
	 */

	public boolean toBoolean(Module module, Object value) {
		if (value == null)
			return false;

		return ((Boolean) value).booleanValue();
	}

	/**
	 * Validates the locale-dependent value for this type. Convert into a standard
	 * internal <code>Boolean</code> representation.Possible valid input values
	 * include:
	 * <ul>
	 * <li>Null or a blank string.</li>
	 * <li>"true" and "false" (Java and XML constants).</li>
	 * <li>"true" and "false" in the current locale.</li>
	 * </ul>
	 * <p>
	 * Boolean property type is stored as <code>java.lang.Boolean</code> internally.
	 * 
	 * @return the value(Boolean Type) to store for the property of type Boolean.
	 *         Returns <code>null</code> if the <code>value</code> parameter is null
	 *         or a blank string.
	 * 
	 */

	public Object validateInputString(Module module, DesignElement element, PropertyDefn defn, String value)
			throws PropertyValueException {
		if (StringUtil.isBlank(value))
			return null;

		// 1. Internal boolean name.

		if (value.equalsIgnoreCase(TRUE))
			return Boolean.TRUE;
		else if (value.equalsIgnoreCase(FALSE))
			return Boolean.FALSE;

		// 2. A localized Boolean name. Convert the localized
		// Boolean name into Boolean instance.

		if (value.equalsIgnoreCase(ModelMessages.getMessage(BOOLEAN_TRUE_RESOURCE_KEY))) {
			return Boolean.TRUE;
		} else if (value.equalsIgnoreCase(ModelMessages.getMessage(BOOLEAN_FALSE_RESOURCE_KEY))) {
			return Boolean.FALSE;
		}

		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, BOOLEAN_TYPE);
	}

	/**
	 * Returns a localized display name of the boolean value. The value should be a
	 * <code>Boolean</code> type or it can be null.
	 * 
	 * @return the display string for the boolean value; return <code>null</code> if
	 *         the value is null.
	 */

	public String toDisplayString(Module module, PropertyDefn defn, Object value) {
		if (value == null)
			return null;

		// return a localized name for True or False.

		if (((Boolean) value).booleanValue()) {
			return ModelMessages.getMessage(BOOLEAN_TRUE_RESOURCE_KEY);
		}

		return ModelMessages.getMessage(BOOLEAN_FALSE_RESOURCE_KEY);

	}

}