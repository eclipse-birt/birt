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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

/**
 * Base class for structures that store some or all of their properties in a
 * hash table. Such properties can have an "unset" state.
 * 
 */

public abstract class PropertyStructure extends Structure {

	/**
	 * The Hashmap to store value for non-intrinsic property values. The contents
	 * are of type Object.
	 */

	protected HashMap<String, Object> propValues = new HashMap<String, Object>();

	/**
	 * Gets the locale value of a property.
	 * 
	 * @param module   the module
	 * 
	 * @param propDefn definition of the property to get
	 * @return value of the item as an object, or null if the item is not set
	 *         locally or is not found.
	 */

	public Object getLocalProperty(Module module, PropertyDefn propDefn) {
		Object value = null;
		if (propDefn.isIntrinsic())
			value = getIntrinsicProperty(propDefn.getName());
		else
			value = propValues.get(propDefn.getName());

		if (propDefn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE)
			return ReferenceValueUtil.resolveElementReference(this, module, (StructPropertyDefn) propDefn, value);
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getLocalProperty(org.eclipse
	 * .birt.report.model.core.Module, java.lang.String)
	 */

	public Object getLocalProperty(Module module, String memberName) {
		PropertyDefn prop = (PropertyDefn) getDefn().getMember(memberName);
		if (prop == null)
			return null;
		return getLocalProperty(module, prop);
	}

	/**
	 * Sets the value of the property.
	 * 
	 * @param prop  the property definition
	 * 
	 * @param value the value to set.
	 * 
	 */

	public final void setProperty(PropertyDefn prop, Object value) {
		assert prop != null;

		Object newValue = getCompatibleValue(prop, value);

		updateReference(prop, newValue);
		setupContext(prop, newValue);

		if (prop.isIntrinsic())
			setIntrinsicProperty(prop.getName(), newValue);
		else if (value == null)
			propValues.remove(prop.getName());
		else
			propValues.put(prop.getName(), newValue);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
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
		assert false;
	}

	/**
	 * Makes a copy of this property structure map.
	 * 
	 * @return IStructure of this property.
	 * @throws CloneNotSupportedException
	 * 
	 */

	public Object clone() throws CloneNotSupportedException {
		PropertyStructure clone = (PropertyStructure) super.clone();
		clone.propValues = new HashMap<String, Object>();

		for (Iterator<String> iter = propValues.keySet().iterator(); iter.hasNext();) {
			String memberName = iter.next();
			IPropertyDefn memberDefn = getDefn().getMember(memberName);

			Object value = null;
			if (memberDefn.getTypeCode() == IPropertyType.STRUCT_TYPE) {
				if (memberDefn.isList()) {
					value = ModelUtil.cloneStructList((ArrayList) propValues.get(memberName));
				} else {
					// must be a structure.
					Structure struct = (Structure) propValues.get(memberName);
					value = struct == null ? null : struct.copy();
				}
			} else if (memberDefn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
				ElementRefValue refValue = (ElementRefValue) propValues.get(memberName);
				value = refValue == null ? null : refValue.copy();
			} else {
				// Primitive or immutable values

				value = propValues.get(memberName);
			}

			clone.propValues.put(memberName, value);
		}

		return clone;
	}
}
