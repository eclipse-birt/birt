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
 * Number property type. A number is represented internally by a
 * <code>BigDecimal</code> object, and represents money and similar business
 * values.
 */

public class NumberPropertyType extends PropertyType {

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.number"; //$NON-NLS-1$

	/**
	 * A default fixed-locale number formatter.
	 */

	private static final NumberFormat formatter = NumberFormat.getNumberInstance(DEFAULT_LOCALE);
	static {
		formatter.setGroupingUsed(false);
	}

	/**
	 * Constructor.
	 */

	public NumberPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/**
	 * Validates that the number represents an BIRT number type (represented as a
	 * BigDecimal). Value can be one of the following:
	 * <p>
	 * <ul>
	 * <li>null, meaning to clear up the property.</li>
	 * <li>A BigDecimal object.</li>
	 * <li>An Integer or Long object.</li>
	 * <li>A Double or Float object. The object must be within the range of a
	 * BigDecimal.</li>
	 * <li>A string that parses to a number. The decimal separator is locale
	 * specific.</li>
	 * </ul>
	 * <p>
	 * A number is represented internally by a <code>BigDecimal</code> object
	 *
	 * @return object of type <code>BigDecimal</code> or null.
	 */

	@Override
	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null) {
			return null;
		}
		if (value instanceof BigDecimal) {
			return value;
		}
		if (value instanceof Double) {
			return new BigDecimal(((Double) value).doubleValue());
		}
		if (value instanceof Integer) {
			return new BigDecimal(((Integer) value).intValue());
		}
		if (value instanceof String) {
			return validateInputString(module, element, defn, (String) value);
		}

		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, NUMBER_TYPE);
	}

	/**
	 * Validates the xml representation of this number property value. The xml value
	 * will be translated into a BigDecimal object.
	 *
	 * @return a <code>BigDecimal</code> object that is translated from the string
	 *         value. Return null if value is null or blank string.
	 * @throws PropertyValueException if the xml value can not be properly
	 *                                translated into a BigDecimal object.
	 *
	 * @see BigDecimal#BigDecimal(java.lang.String)
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

		try {
			return new BigDecimal(tmpValue);
		} catch (NumberFormatException e) {
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, NUMBER_TYPE);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	@Override
	public int getTypeCode() {
		return NUMBER_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	@Override
	public String getName() {
		return NUMBER_TYPE_NAME;
	}

	/**
	 * Returns the double value of the input number property value. The number
	 * property value is represented by a <code>BigDecimal</code> object.
	 *
	 * @return double value of the input number property value. Return 0.0 if value
	 *         is null;
	 *
	 */

	@Override
	public double toDouble(Module module, Object value) {
		if (value == null) {
			return 0.0;
		}

		if (value instanceof String) {
			try {
				return Double.parseDouble((String) value);
			} catch (NumberFormatException e) {
				return 0.0;
			}
		}

		return ((BigDecimal) value).doubleValue();
	}

	/**
	 * Returns the locale-independent string representation of the input number
	 * property value. The number property value is represented by a
	 * <code>BigDecimal</code> object. The value will be formatted in a
	 * locale-independent way.
	 *
	 * @return locale-independent string representation of the number property
	 *         value. Return null if value is null.
	 */

	@Override
	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return (String) value;
		}

		return formatter.format(((BigDecimal) value).doubleValue());
	}

	/**
	 * Returns the integer value of the input number property value. The number
	 * property value is represented by a <code>BigDecimal</code> object.
	 *
	 * @return integer value of the input number property value. Return 0 if input
	 *         value is null.
	 *
	 */

	@Override
	public int toInteger(Module module, Object value) {
		if (value == null) {
			return 0;
		}

		if (value instanceof String) {
			try {
				return Double.valueOf((String) value).intValue();
			} catch (NumberFormatException e) {
				return 0;
			}
		}

		return ((BigDecimal) value).intValue();
	}

	/**
	 * Converts the input number property value into a <code>BigDecimal</code>.
	 *
	 * @return return the number property value as a <code>BigDecimal</code>.
	 */

	@Override
	public BigDecimal toNumber(Module module, Object value) {
		if (value instanceof String) {
			try {
				return new BigDecimal(formatter.parse((String) value).doubleValue());
			} catch (ParseException e) {
				return new BigDecimal(0.0);
			}
		}
		return (BigDecimal) value;
	}

	/**
	 * Returns the localized string representation of the input number property
	 * value. The number property value is represented by a <code>BigDecimal</code>
	 * object. The value will be formatted in a locale-dependent way.
	 *
	 * @return locale-dependent string representation of the number property value.
	 *         Return null if value is null.
	 */

	@Override
	public String toDisplayString(Module module, PropertyDefn defn, Object value) {
		if (value == null) {
			return null;
		}

		ULocale locale = module == null ? ThreadResources.getLocale() : module.getLocale();
		NumberFormat formatter = NumberFormat.getNumberInstance(locale.toLocale());
		return formatter.format(((BigDecimal) value).doubleValue());
	}

	/**
	 * Validates the number property value in a locale-specific way. The string
	 * value will be parsed into a BigDecimal object.
	 *
	 * @return a <code>BigDecimal</code> object that is translated from the string
	 *         value. Return null if value is null or blank string.
	 * @throws PropertyValueException if the value can not be properly parsed in the
	 *                                current locale.
	 */

	@Override
	public Object validateInputString(Module module, DesignElement element, PropertyDefn defn, String value)
			throws PropertyValueException {
		if (StringUtil.isBlank(value)) {
			return null;
		}

		ULocale locale = module == null ? ThreadResources.getLocale() : module.getLocale();
		NumberFormat formatter = NumberFormat.getNumberInstance(locale.toLocale());
		Number number = null;
		try {
			// Parse in locale-dependent way.
			number = formatter.parse(value);

			// TODO: current NumberFormat( even DecimalFormmater ) do not
			// provide means to parse a
			// input string in arbitrary-precision way. It does not express the
			// accurate value.

		} catch (ParseException e) {
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, NUMBER_TYPE);
		}

		return new BigDecimal(number.doubleValue());
	}
}
