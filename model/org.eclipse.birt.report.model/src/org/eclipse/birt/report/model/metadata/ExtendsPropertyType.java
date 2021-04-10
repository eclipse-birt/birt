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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Property type for the "extends" property of an element. The value is either
 * the unresolved name of the parent element, or a cached pointer to the parent
 * element. The parent element must always be of the same element type as the
 * derived element.
 * 
 */

public class ExtendsPropertyType extends PropertyType {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(ExtendsPropertyType.class.getName());
	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.extends"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public ExtendsPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return EXTENDS_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName() {
		return EXTENDS_TYPE_NAME;
	}

	/**
	 * Validates an extends property value of an element. The value can be a string
	 * value that takes the name of the target element, or it can be the instance of
	 * the target element.
	 * 
	 * @return An <code>ElementRefValue</code> that holds the target element, the
	 *         reference is resolved if the input value is the instance of the
	 *         target element.
	 */

	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null)
			return null;

		// This implementation assumes that the class-specific validation
		// was already done.

		if (value instanceof String) {
			String name = StringUtil.trimString((String) value);
			if (name == null) {
				return null;
			}

			// Element is unresolved.

			return validateStringValue(module, defn, (String) value);
		}
		if (value instanceof DesignElement) {

			// Resolved reference.

			return validateElementValue(module, defn, (DesignElement) value);
		}

		// Invalid property value.

		logger.log(Level.SEVERE, "The value of the extends property is not a valid type "); //$NON-NLS-1$
		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				IPropertyType.EXTENDS_TYPE);
	}

	/**
	 * Returns the referenced element name if the input value is an
	 * <code>ElementRefValue</code>, return <code>null</code> if the value is null.
	 */

	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value == null)
			return null;

		ElementRefValue refValue = (ElementRefValue) value;
		return refValue.getQualifiedReference();
	}

	/**
	 * Validates the element value.
	 * 
	 * @param module     report design
	 * @param targetDefn definition of target element
	 * @param target     target element
	 * @return the resolved element reference value
	 * @throws PropertyValueException if the type of target element is not that
	 *                                target definition.
	 */

	private ElementRefValue validateElementValue(Module module, PropertyDefn targetDefn, DesignElement target)
			throws PropertyValueException {
		// Element is unresolved.

		return new ElementRefValue(null, target);

	}

	/**
	 * Validates the element name.
	 * 
	 * @param module     report design
	 * @param targetDefn definition of target element
	 * @param name       element name
	 * @return the resolved element reference value
	 * @throws PropertyValueException if the type of target element is not that
	 *                                target definition, or the element with the
	 *                                given name is not in name space.
	 */

	private ElementRefValue validateStringValue(Module module, PropertyDefn targetDefn, String name)
			throws PropertyValueException {
		String namespace = StringUtil.extractNamespace(name);
		name = StringUtil.extractName(name);

		// Element is unresolved.

		return new ElementRefValue(namespace, name);
	}

}
