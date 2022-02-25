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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Represents the property type for a list of objects (elements) or a simple
 * element.
 *
 */

public class ContentElementPropertyType extends PropertyType {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(ContentElementPropertyType.class.getName());
	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.contentElement"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public ContentElementPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#getName()
	 */
	@Override
	public String getName() {
		return CONTENT_ELEMENT_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#getTypeCode()
	 */
	@Override
	public int getTypeCode() {
		return CONTENT_ELEMENT_TYPE;
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

		// Now support empty list if structure property is list.

		if (defn.isList()) {
			if (value instanceof List) {
				if (((List<Object>) value).isEmpty()) {
					return value;
				}
			}
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					CONTENT_ELEMENT_TYPE);
		}

		if (value instanceof DesignElement) {
			return value;
		}

		// exception
		logger.log(Level.SEVERE, "The value of this element property: " + defn.getName() + " is not a valid type"); //$NON-NLS-1$ //$NON-NLS-2$
		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				CONTENT_ELEMENT_TYPE);
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

		return value.toString();
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
		if (value instanceof ArrayList) {
			return ((ArrayList<Object>) value).size();
		}
		return 1;
	}

}
