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

import org.eclipse.birt.report.model.api.PropertyMaskHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * This class provides property masks of system or user defined properties. Name
 * and Value members in <code>PropertyMask</code> are all intrinsic properties.
 * 
 * Choices for the mask value are defined in <code>DesignChoiceConstants</code>.
 * 
 * @see DesignChoiceConstants
 */

public class PropertyMask extends Structure {

	/**
	 * Name of the property name member.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the value for the mask.
	 */

	public static final String MASK_MEMBER = "mask"; //$NON-NLS-1$

	/**
	 * Name of this structure within the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "PropertyMask"; //$NON-NLS-1$

	/**
	 * The name of the property.
	 */

	protected String name = null;

	/**
	 * The value of the mask. The default value is "hide".
	 */

	protected String mask = null;

	/**
	 * Constructs a PropertyMask.
	 */

	public PropertyMask() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (NAME_MEMBER.equals(propName))
			return name;
		if (MASK_MEMBER.equals(propName))
			return mask;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (NAME_MEMBER.equals(propName))
			name = (String) value;
		else if (MASK_MEMBER.equals(propName))
			mask = (String) value;
		else
			assert false;
	}

	/**
	 * Returns the property name.
	 * 
	 * @return the property name
	 */

	public String getName() {
		return (String) getProperty(null, NAME_MEMBER);
	}

	/**
	 * Returns the mask of the property. The possible values are defined in
	 * {org.eclipse.birt.report.model.elements.DesignChoiceConstants}, and they are:
	 * <ul>
	 * <li>PROPERTY_MASK_TYPE_CHANGE
	 * <li>PROPERTY_MASK_TYPE_LOCK
	 * <li>PROPERTY_MASK_TYPE_HIDE
	 * </ul>
	 * 
	 * @return the mask of the property
	 */

	public String getMask() {
		return (String) getProperty(null, MASK_MEMBER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new PropertyMaskHandle(valueHandle, index);
	}

	/**
	 * Sets the property mask. The allowed values are defined in
	 * {org.eclipse.birt.report.model.elements.DesignChoiceConstants}, and they are:
	 * <ul>
	 * <li>PROPERTY_MASK_TYPE_CHANGE
	 * <li>PROPERTY_MASK_TYPE_LOCK
	 * <li>PROPERTY_MASK_TYPE_HIDE
	 * </ul>
	 * 
	 * @param mask the proeprty mask to set
	 */

	public void setMask(String mask) {
		this.mask = mask;
	}

	/**
	 * Sets the property name.
	 * 
	 * @param name the property name to set
	 */

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt
	 * .report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */
	public List<SemanticException> validate(Module module, DesignElement element) {
		ArrayList<SemanticException> list = new ArrayList<SemanticException>();

		if (StringUtil.isBlank(getName())) {
			list.add(new PropertyValueException(element, getDefn().getMember(NAME_MEMBER), null,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		} else if (element.getPropertyDefn(getName()) == null) {
			list.add(new SemanticError(element, new String[] { getName() },
					SemanticError.DESIGN_EXCEPTION_INVALID_PROPERTY_NAME));
		}

		return list;
	}
}
