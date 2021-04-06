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

package org.eclipse.birt.report.model.api.elements.structures;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.PropertyBindingHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.EncryptionUtil;

/**
 * Represents the property binding structure. The property binding defines the
 * overridable property value. It includes property name, element ID and
 * overridden value.
 */

public class PropertyBinding extends Structure {

	/**
	 * Name of the structure.
	 */

	public static final String PROPERTY_BINDING_STRUCT = "PropertyBinding"; //$NON-NLS-1$

	/**
	 * Name of the "name" property.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the "id" property.
	 */

	public static final String ID_MEMBER = "id"; //$NON-NLS-1$

	/**
	 * Name of the "value" property.
	 */

	public static final String VALUE_MEMBER = "value"; //$NON-NLS-1$

	/**
	 * Name of the property binding.
	 */

	protected String name = null;

	/**
	 * Element id of the property binding. Then Call
	 * {@link org.eclipse.birt.report.model.api.ModuleHandle#getElementByID(long)}
	 * to find the host element of this property binding defined.
	 */

	protected BigDecimal id = null;

	/**
	 * Value expression of this property binding.
	 */

	protected Expression value = null;

	/**
	 * The encryption id for the encrypted property value.
	 */
	protected String encryptionID = null;

	/**
	 * Constructs a PropertyMask.
	 */

	public PropertyBinding() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (NAME_MEMBER.equalsIgnoreCase(propName))
			return name;
		else if (ID_MEMBER.equalsIgnoreCase(propName))
			return id;
		else if (VALUE_MEMBER.equalsIgnoreCase(propName)) {
			if (encryptionID == null)
				return value;

			if (value != null) {
				Object decoded = EncryptionUtil.decrypt((PropertyDefn) getDefn().getMember(VALUE_MEMBER), encryptionID,
						value);
				return new Expression(decoded, value.getUserDefinedType());
			}

			return value;
		} else {
			assert false;
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (NAME_MEMBER.equalsIgnoreCase(propName))
			name = (String) value;
		else if (ID_MEMBER.equalsIgnoreCase(propName))
			id = (BigDecimal) value;
		else if (VALUE_MEMBER.equalsIgnoreCase(propName))
			this.value = (Expression) value;
		else
			assert false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */

	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new PropertyBindingHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#getStructName()
	 */
	public String getStructName() {
		return PROPERTY_BINDING_STRUCT;
	}

	/**
	 * Sets the name of the property binding. It must be one of the defined property
	 * in the element.
	 * 
	 * @param name the property name
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the property name of this binding.
	 * 
	 * @return the property name of this binding
	 */

	public String getName() {
		return name;
	}

	/**
	 * Sets the element id of this binding.
	 * 
	 * @param id the element id to set
	 */

	public void setID(long id) {
		this.id = new BigDecimal((double) id);
	}

	/**
	 * Gets the element id of the binding.
	 * 
	 * @return the element id of this binding
	 */

	public BigDecimal getID() {
		return this.id;
	}

	/**
	 * Gets the overridden value of this binding.
	 * 
	 * @return the overridden value of this binding.
	 */

	public String getValue() {
		Expression tmpValue = (Expression) getIntrinsicProperty(VALUE_MEMBER);
		return tmpValue == null ? null : tmpValue.getStringExpression();
	}

	/**
	 * Sets the overridden value of this binding.
	 * 
	 * @param expression the value expression to set
	 */

	public void setValue(String expression) {
		String tmpType = value == null ? null : value.getUserDefinedType();
		if (!StringUtil.isBlank(expression) || tmpType != null)
			value = new Expression(expression, tmpType);
		else
			value = null;
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
		} else if (id == null || module.getElementByID(id.longValue()) == null) {
			list.add(new SemanticError(element, new String[] { getName() },
					SemanticError.DESIGN_EXCEPTION_INVALID_PROPERTY_BINDING_ID));
		} else if (module.getElementByID(id.longValue()).getPropertyDefn(getName()) == null) {
			list.add(new SemanticError(element, new String[] { getName() },
					SemanticError.DESIGN_EXCEPTION_INVALID_PROPERTY_NAME));
		}

		return list;
	}

	/**
	 * Sets the encryption id for the encrypted value. This method is not
	 * recommended to be called by users. It is just called by Model inner APIs.
	 * Otherwise, if user sets a wrong id inconsistent with the value, they might
	 * get an odd value.
	 * 
	 * @param encryptionID
	 */
	public void setEncryption(String encryptionID) {
		this.encryptionID = encryptionID;
	}

	/**
	 * Returns the encryption id.
	 * 
	 * @return the encryption id.
	 * 
	 */

	public String getEncryption() {
		return this.encryptionID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (encryptionID == null)
			return ((PropertyBinding) obj).encryptionID == null;
		return encryptionID.equals(((PropertyBinding) obj).encryptionID);
	}

}
