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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * Represents the property type for a list of objects (structures) or a simple
 * structure.
 * 
 */

public class StructPropertyType extends PropertyType {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(StructPropertyType.class.getName());
	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.struct"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public StructPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return STRUCT_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName() {
		return STRUCT_TYPE_NAME;
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

		if (value == null)
			return null;

		// Now support empty list if structure property is list.

		if (defn.isList()) {
			if (value instanceof List) {
				if (((List<Structure>) value).isEmpty()) {
					return value;
				}
			}
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, STRUCT_TYPE);
		}

		if (value instanceof Structure) {
			Iterator<IPropertyDefn> iter = ((Structure) value).getDefn().propertiesIterator();
			while (iter.hasNext()) {
				PropertyDefn memberDefn = (PropertyDefn) iter.next();
				if (!memberDefn.isList()) {
					Object propValue = ((Structure) value).getProperty(module, memberDefn);
					memberDefn.validateValue(module, element, propValue);
				}
			}

			return value;
		}

		// exception
		logger.log(Level.SEVERE, "The value of this structure property: " + defn.getName() + " is not a valid type"); //$NON-NLS-1$ //$NON-NLS-2$
		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, STRUCT_TYPE);
	}

	/**
	 * Converts the structure list property type into an integer. If value is null,
	 * return 0, else return the size of the list value.
	 * 
	 * @return the integer value of the structure list property type.
	 */

	public int toInteger(Module module, Object value) {
		// Return the list size as the int value.

		if (value == null)
			return 0;
		if (value instanceof ArrayList)
			return ((ArrayList) value).size();
		return 1;
	}

	/**
	 * Can not convert a list property type to a string. This method will always
	 * return null.
	 * 
	 */

	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value == null)
			return null;

		return value.toString();
	}

}
