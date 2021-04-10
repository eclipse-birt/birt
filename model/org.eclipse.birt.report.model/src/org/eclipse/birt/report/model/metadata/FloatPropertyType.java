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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * Represents a double-precision floating point property type. Float property
 * values are stored as <code>java.lang.Double</code>.
 * <p>
 */

public class FloatPropertyType extends PropertyType {

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.float"; //$NON-NLS-1$

	/**
	 * The decimal formatter for locale independent string.
	 */

	private static final DecimalFormat formatter = new DecimalFormat("#0.0#", new DecimalFormatSymbols(DEFAULT_LOCALE)); //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public FloatPropertyType() {
		super(DISPLAY_NAME_KEY);

		formatter.setMaximumFractionDigits(32);
		formatter.setMaximumIntegerDigits(32);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return FLOAT_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName() {
		return FLOAT_TYPE_NAME;
	}

	/**
	 * Validates the float property value. The property value can be one of the
	 * following:
	 * <ul>
	 * <li>Null, meaning to clear the property value.</li>
	 * <li>A Double, Integer, Float object</li>
	 * <li>A Boolean object, <code>TRUE</code> will be converted into Double(1.0),
	 * <code>FALSE</code> will be converted into Double(0.0)</li>
	 * <li>A string object represents a float value in the current locale.</li>
	 * </ul>
	 * <p>
	 * Float property type is stored as <code>java.lang.Double</code> internally.
	 * 
	 * @return A <code>Double</code> value that store the value. Return
	 *         <code>null</code> if value is null.
	 */

	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null)
			return null;
		if (value instanceof Double)
			return value;
		if (value instanceof Float)
			return new Double(((Float) value).doubleValue());
		if (value instanceof Integer)
			return new Double(((Integer) value).intValue());
		if (value instanceof BigDecimal)
			return new Double(((BigDecimal) value).doubleValue());
		if (value instanceof Boolean)
			return new Double(
					((Boolean) value).booleanValue() ? BooleanPropertyType.INT_TRUE : BooleanPropertyType.INT_FALSE);
		if (value instanceof String)
			return validateInputString(module, element, defn, (String) value);

		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, FLOAT_TYPE);
	}

	/**
	 * Validates the XML representation of the float property value. The string
	 * value will be parsed into a <code>Double</code> in a locale-independent way.
	 * <p>
	 * 
	 * @return Returns the <code>Double</code> represented by the string argument.
	 *         Return <code>null</code> if value is null or a blank string.
	 */

	public Object validateXml(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		assert value == null || value instanceof String;
		String tmpValue = (String) value;

		tmpValue = StringUtil.trimString(tmpValue);
		if (tmpValue == null)
			return null;

		return parseDouble(tmpValue);
	}

	/**
	 * Converts the float property value into a double. Return its double value if
	 * the input <code>value</code> is a Double, return 0.0 if value is null.
	 * 
	 */

	public double toDouble(Module module, Object value) {
		if (value == null)
			return 0;

		if (value instanceof String) {
			try {
				return Double.valueOf((String) value).doubleValue();
			} catch (NumberFormatException e) {
				return 0.0;
			}
		}

		return ((Double) value).doubleValue();
	}

	/**
	 * Converts the float property value into an integer. Return its integer value
	 * if the input <code>value</code> is a <code>Double</code>, return 0 if value
	 * is null.
	 * 
	 */

	public int toInteger(Module module, Object value) {
		if (value == null)
			return 0;

		if (value instanceof String) {
			try {
				return Double.valueOf((String) value).intValue();
			} catch (NumberFormatException e) {
				return 0;
			}
		}

		return ((Double) value).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#toString(java
	 * .lang.Object)
	 */

	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value == null)
			return null;

		if (value instanceof String)
			return (String) value;

		return formatter.format(((Double) value).doubleValue());
	}

	/**
	 * Converts the float property value into an localized formatter, e.g: return
	 * "12,000.123" for a Double(12000.123d) in US locale. Return <code>null</code>
	 * if the value is null.
	 * 
	 */

	public String toDisplayString(Module module, PropertyDefn defn, Object value) {
		if (value == null)
			return null;

		ULocale locale = module == null ? ThreadResources.getLocale() : module.getLocale();
		NumberFormat formatter = NumberFormat.getNumberInstance(locale.toLocale());
		return formatter.format(((Double) value).doubleValue());
	}

	/**
	 * Validates the float property value in locale-dependent way. The string value
	 * will be parsed in the in the current locale, it will be stored internally as
	 * a <code>Double</code>.
	 * 
	 * @return Double value of the input string. Return null if input value is null
	 *         or a blank string.
	 * 
	 * @throws PropertyValueException if the input string is not valid for the
	 *                                current locale.
	 */

	public Object validateInputString(Module module, DesignElement element, PropertyDefn defn, String value)
			throws PropertyValueException {
		value = StringUtil.trimString(value);
		if (value == null)
			return null;

		ULocale locale = module == null ? ThreadResources.getLocale() : module.getLocale();
		NumberFormat localeFormatter = NumberFormat.getNumberInstance(locale.toLocale());
		Number number = null;
		try {
			// Parse in locale-dependent way.
			// Use the decimal separator from the locale.
			number = localeFormatter.parse(value);
		} catch (ParseException e) {
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, NUMBER_TYPE);
		}

		return new Double(number.doubleValue());
	}

	/**
	 * Returns a new <code>Double</code> initialized to the value represented by the
	 * specified <code>String</code>.
	 * 
	 * @param value the string representint a double
	 * @return Returns the <code>Double</code> represented by the string argument
	 * @throws PropertyValueException if the string can not be parsed to a double
	 */

	protected Double parseDouble(String value) throws PropertyValueException {
		try {
			// Locale-independent way

			return Double.valueOf(value);
		} catch (NumberFormatException e) {
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, FLOAT_TYPE);
		}
	}
}