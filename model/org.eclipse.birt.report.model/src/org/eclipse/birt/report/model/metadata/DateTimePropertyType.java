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

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.ULocale;

/**
 * Date-time property type. Date-time property is stored as
 * <code>java.util.Date</code>
 * 
 */

public class DateTimePropertyType extends PropertyType {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(DateTimePropertyType.class.getName());

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.dateTime"; //$NON-NLS-1$

	/**
	 * Fixed formatter for datetime expression in xml.
	 */

	private static final SimpleDateFormat formatter;

	// Set default time zone and initialize the formatter. Due to the bug of
	// icu, if default time zone is not set, the <code>SimpleDateForma</code>
	// cann't be initialized. When ICU fixed this bug, removes this codes.

	static {
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DEFAULT_LOCALE); //$NON-NLS-1$
	}

	/**
	 * Constructor.
	 */

	public DateTimePropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/**
	 * Validates the date time property value,the value is either a Java Date
	 * object, or a string with a date and/or time validated for the current locale.
	 * <p>
	 * Date-time property is stored as <code>java.util.Date</code>
	 * <p>
	 * 
	 * @return object of type Date or null if <code>value</code> is null.
	 */

	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {

		if (value == null) {
			return null;
		}
		if (value instanceof Date) {
			return value;
		}
		if (value instanceof String) {
			return validateInputString(module, element, defn, (String) value);
		}

		logger.log(Level.SEVERE, "Invalid date value type:" + value); //$NON-NLS-1$

		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, DATE_TIME_TYPE);
	}

	/**
	 * Validates the XML representation of the date property value. Xml date time
	 * format should in the fixed pattern "yyyy-MM-dd HH:mm:ss".
	 * 
	 * @return object of type Date or null if <code>value</code> is null.
	 */

	public Object validateXml(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		assert value == null || value instanceof String;
		String tmpValue = (String) value;

		tmpValue = StringUtil.trimString(tmpValue);
		if (tmpValue == null) {
			return null;
		}

		// fixed xml format.
		try {
			return formatter.parse(tmpValue);
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "Invalid date value:" + tmpValue); //$NON-NLS-1$
			throw new PropertyValueException(tmpValue, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					getTypeCode());
		}

	}

	/**
	 * Returns the display string for the Date object in the current locale.
	 * 
	 * @return display string for the date object in the current locale.
	 */

	public String toDisplayString(Module module, PropertyDefn defn, Object value) {
		if (value == null)
			return null;

		assert value instanceof Date;

		// Convert to Locale-specific format.
		ULocale locale = module == null ? ThreadResources.getLocale() : module.getLocale();
		DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		return formatter.format((Date) value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return DATE_TIME_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName() {
		return DATE_TIME_TYPE_NAME;
	}

	/**
	 * Validates the locale-dependent value for the date time type, validate the
	 * <code>value</code> in the locale-dependent way and convert the
	 * <code>value</code> into a Date object.
	 * 
	 * @return object of type Date or null if <code>value</code> is null.
	 */

	public Object validateInputString(Module module, DesignElement element, PropertyDefn defn, String value)
			throws PropertyValueException {
		if (StringUtil.isBlank(value)) {
			return null;
		}

		// Parse the input in locale-dependent way.
		ULocale locale = module == null ? ThreadResources.getLocale() : module.getLocale();
		DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		try {
			return formatter.parse(value);
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "Invalid date value:" + value); //$NON-NLS-1$
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					DATE_TIME_TYPE);
		}
	}

	/**
	 * Converts the Date object into a string presentation in a fixed xml format
	 * "yyyy-MM-dd HH:mm:ss".
	 */

	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value == null)
			return null;

		if (value instanceof String)
			return (String) value;

		return formatter.format((Date) value);
	}

}