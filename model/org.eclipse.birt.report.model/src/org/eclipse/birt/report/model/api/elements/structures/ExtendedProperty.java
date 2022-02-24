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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * This class represents one Extended property.
 * 
 */

public class ExtendedProperty extends Structure {

	/**
	 * The Extended property structure name.
	 */

	public static final String Extended_PROPERTY_STRUCT = "ExtendedProperty"; //$NON-NLS-1$

	/**
	 * The member name of the name of Extended property.
	 */

	public final static String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * The member name of the value of Extended property.
	 */

	public final static String VALUE_MEMBER = "value"; //$NON-NLS-1$

	public final static String ENCRYPTION_ID_MEMBER = "encryptionID"; //$NON-NLS-1$

	/**
	 * The Extended property name.
	 */

	private String name = null;

	/**
	 * The Extended property value.
	 */

	private String value = null;

	private String encryptionID = null;

	/**
	 * Default constructor.
	 */

	public ExtendedProperty() {
	}

	/**
	 * Constructs the extended property with the given name and value.
	 * 
	 * @param name  the name of a extended property
	 * @param value the value of a extended property
	 */

	public ExtendedProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public ExtendedProperty(String name, String value, String encryptionID) {
		this.name = name;
		this.value = value;
		this.encryptionID = encryptionID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return Extended_PROPERTY_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.
	 * String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (propName.equals(NAME_MEMBER))
			return name;
		else if (propName.equals(VALUE_MEMBER))
			return value;
		else if (propName.equals(encryptionID))
			this.encryptionID = (String) value;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.
	 * String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (propName.equals(NAME_MEMBER))
			name = (String) value;
		else if (propName.equals(VALUE_MEMBER))
			this.value = (String) value;
		else if (propName.equals(encryptionID))
			this.encryptionID = (String) value;
		else
			assert false;
	}

	/**
	 * Returns the Extended property name.
	 * 
	 * @return the Extended property name
	 */

	public String getName() {
		return name;
	}

	/**
	 * Sets the Extended property name.
	 * 
	 * @param name the Extended property name to set
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the Extended property value.
	 * 
	 * @return the Extended property value
	 */

	public String getValue() {
		return value;
	}

	/**
	 * Sets the Extended property value.
	 * 
	 * @param value the Extended property value to set
	 */

	public void setValue(String value) {
		this.value = value;
	}

	public String getEncryptionID() {
		return encryptionID;
	}

	public void setEncryptionID(String encryptionID) {
		this.encryptionID = encryptionID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt.report
	 * .model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate(Module module, DesignElement element) {
		ArrayList list = new ArrayList();

		if (StringUtil.isBlank(name)) {
			list.add(new PropertyValueException(element, getDefn().getMember(NAME_MEMBER), name,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new ExtendedPropertyHandle(valueHandle, index);
	}
}
