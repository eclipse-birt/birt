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

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Element name property type. Represents the key member of a structure.
 * 
 */

public class MemberKeyPropertyType extends TextualPropertyType {

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.memberKey"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public MemberKeyPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return MEMBER_KEY_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName() {
		return MEMBER_KEY_NAME;
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
						MEMBER_KEY_TYPE);
			return null;
		}
		if (value instanceof String) {
			String stringValue = trimString((String) value, defn.getTrimOption());
			if (stringValue == null) {
				if (defn.isStructureMember())
					throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
							MEMBER_KEY_TYPE);
				return null;
			}

			return stringValue;
		}
		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, MEMBER_KEY_TYPE);
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
}