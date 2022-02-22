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

package org.eclipse.birt.report.model.metadata;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * Represents the integer property type. Integer property values are stored as
 * <code>java.lang.Integer</code> objects.
 */

public class IntegerPropertyType extends PropertyType {

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.integer"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public IntegerPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	@Override
	public int getTypeCode() {
		return INTEGER_TYPE;
	}

	/**
	 * Ensures that the value is a valid integer. Can be any of the following:
	 * <li>Integer object.</li>
	 * <li>Float or Double object. Truncate the decimal portion. Ensure that the
	 * value is within the integer range.</li>
	 * <li>BigDecimal object. Truncate the decimal portion. Ensure that the value is
	 * is within the integer range.</li>
	 * <li>A Boolean object, <code>TRUE</code> will be converted into Integer(1),
	 * <code>FALST</code> will be converted into Integer(0)</li>
	 * <li>String that must evaluate to an integer in either of the two Java forms:
	 * decimal [1-9][0-9]* or hexadecimal format &[hH]xxxx.</li>
	 * <li>String that must evaluate to an HTML hexidecimal: #xxxxx.</li>.
	 * <p>
	 *
	 * @return object of type Integer or null if value is null..
	 */

	@Override
	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null) {
			return null;
		}
		if (value instanceof Integer) {
			return value;
		}
		if (value instanceof Float) {
			return Integer.valueOf(((Float) value).intValue());
		}
		if (value instanceof Double) {
			return Integer.valueOf(((Double) value).intValue());
		}
		if (value instanceof String) {
			if (StringUtil.trimString((String) value) == null) {
				return null;
			}

			return validateInputString(module, element, defn, ((String) value).trim());
		}
		if (value instanceof BigDecimal) {
			return Integer.valueOf(((BigDecimal) value).intValue());
		}
		if (value instanceof Boolean) {
			return Integer.valueOf(
					((Boolean) value).booleanValue() ? BooleanPropertyType.INT_TRUE : BooleanPropertyType.INT_FALSE);
		}

		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, INTEGER_TYPE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateInputString
	 * (org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.String)
	 */

	@Override
	public Object validateInputString(Module module, DesignElement element, PropertyDefn defn, String value)
			throws PropertyValueException {
		value = StringUtil.trimString(value);
		if (value == null) {
			return null;
		}

		ULocale locale = module == null ? ThreadResources.getLocale() : module.getLocale();
		NumberFormat localeFormatter = NumberFormat.getIntegerInstance(locale.toLocale());
		Number number = null;
		try {
			// Parse in locale-dependent way.
			// Use the decimal separator from the locale.
			number = localeFormatter.parse(value);
		} catch (ParseException e) {
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					INTEGER_TYPE);
		}

		return Integer.valueOf(number.intValue());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyType#validateXml(org.eclipse
	 * .birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */
	@Override
	public Object validateXml(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		assert value == null || value instanceof String;
		String tmpValue = (String) value;

		tmpValue = StringUtil.trimString(tmpValue);
		if (tmpValue == null) {
			return null;
		}

		return parseInteger(tmpValue);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	@Override
	public String getName() {
		return INTEGER_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#toString(java
	 * .lang.Object)
	 */

	@Override
	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return (String) value;
		}

		return ((Integer) value).toString();
	}

	/**
	 * Returns the localized string representation of the input integer property
	 * value. The integer property value is represented by a <code>Integer</code>
	 * object. The value will be formatted in a locale-dependent way.
	 *
	 * @return locale-dependent string representation of the integer property value.
	 *         Return null if value is null.
	 */

	@Override
	public String toDisplayString(Module module, PropertyDefn defn, Object value) {
		if (value == null) {
			return null;
		}

		ULocale locale = module == null ? ThreadResources.getLocale() : module.getLocale();
		NumberFormat formatter = NumberFormat.getIntegerInstance(locale.toLocale());
		return formatter.format(((Integer) value).doubleValue());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#toInteger(
	 * java.lang.Object)
	 */

	@Override
	public int toInteger(Module module, Object value) {
		if (value == null) {
			return 0;
		}

		if (value instanceof String) {
			try {
				return Integer.decode((String) value).intValue();
			} catch (NumberFormatException e) {
				return 0;
			}
		}

		return ((Integer) value).intValue();
	}

	/**
	 * Returns a new <code>Integer</code> initialized to the value represented by
	 * the specified <code>String</code>.
	 *
	 * @param value the string representing an integer
	 * @return Returns the <code>Integer</code> represented by the string argument
	 * @throws PropertyValueException if the string can not be parsed to an integer
	 */

	protected Integer parseInteger(String value) throws PropertyValueException {
		try {
			return Integer.decode(value);
		} catch (NumberFormatException e) {
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					INTEGER_TYPE);
		}
	}

}
