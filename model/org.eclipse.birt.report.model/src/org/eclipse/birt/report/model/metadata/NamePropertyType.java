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

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Element name property type. Represents the name of an element.
 * 
 */

public class NamePropertyType extends TextualPropertyType {

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.name"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public NamePropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return NAME_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName() {
		return NAME_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateValue(org
	 * .eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */
	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		assert defn != null;
		if (value == null) {
			if (defn.isStructureMember())
				throw new PropertyValueException(null, PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
						NAME_TYPE);
			return null;
		}
		if (value instanceof String) {
			String stringValue = trimString((String) value, defn.getTrimOption());
			if (stringValue == null) {
				if (defn.isStructureMember())
					throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
							NAME_TYPE);
				return null;
			} else if (!isValidName(stringValue)) {
				throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
						NAME_TYPE);
			}

			return stringValue;
		}
		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, NAME_TYPE);
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
	public Object validateXml(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		assert value == null || value instanceof String;
		String tmpValue = (String) value;

		if (tmpValue == null)
			return null;
		return trimString(tmpValue, defn.getTrimOption());
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isValidName(String value) {
		if (value == null)
			return true;

		// can not contain: / \ . ! ; ,
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c == '/' || c == '\\' || c == '.' || c == '!' || c == ';' || c == ',')
				return false;
		}
		return true;
	}

	/**
	 * Validates the name. Replace all the illegal chars with '_'.
	 * 
	 * @param name
	 * @return the validated name
	 */
	public static String validateName(String name) {
		if (name == null)
			return null;

		String value = StringUtil.trimString(name);
		if (value == null)
			return null;

		value = value.replaceAll("/|\\\\|\\.|!|;|,", "_"); //$NON-NLS-1$ //$NON-NLS-2$
		return value;
	}
}
