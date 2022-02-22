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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Represents the property type for a list of some simple property values, such
 * as integer, float, dateTime and so on.
 *
 */

public class ListPropertyType extends PropertyType {

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.list"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public ListPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#getTypeCode()
	 */

	@Override
	public int getTypeCode() {
		return LIST_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#getName()
	 */

	@Override
	public String getName() {
		return LIST_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateValue(org
	 * .eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */
	@Override
	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null) {
			return null;
		}
		if (value instanceof List) {
			List<Object> items = (List<Object>) value;
			List<Object> validatedItems = new ArrayList<>();

			for (int i = 0; i < items.size(); i++) {
				Object item = items.get(i);

				Object toValidate = defn.doValidateValueWithExpression(module, element, defn.getSubType(), item);

				validatedItems.add(toValidate);
			}

			return validatedItems;
		}

		List<Object> listValue = new ArrayList<>();

		Object validatedValue = defn.doValidateValueWithExpression(module, element, defn.getSubType(), value);
		listValue.add(validatedValue);
		return listValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#toString(org.eclipse
	 * .birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */

	@Override
	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value == null) {
			return null;
		}

		assert value instanceof List;

		List<Object> valueList = (List<Object>) value;
		if (valueList.isEmpty()) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		PropertyType type = defn.getSubType();
		assert type != null;
		for (int i = 0; i < valueList.size(); i++) {
			Object item = valueList.get(i);

			String stringValue = type.toString(module, defn, item);
			if (sb.length() > 0) {
				sb.append("; "); //$NON-NLS-1$
			}
			if (stringValue != null) {
				sb.append(stringValue);
			}
		}

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyType#toInteger(org.eclipse
	 * .birt.report.model.core.Module, java.lang.Object)
	 */

	@Override
	public int toInteger(Module module, Object value) {
		// Return the list size as the int value.

		if (value == null) {
			return 0;
		}
		return ((ArrayList<Object>) value).size();
	}
}
