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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.command.PropertyCommand;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Abstract class for working with properties that have internal structure, such
 * as a color, a dimension or a font.
 */

public abstract class ComplexValueHandle extends ValueHandle {

	/**
	 * Property definition.
	 */

	protected ElementPropertyDefn propDefn = null;

	/**
	 * Path to the property within an element, a list or a structure.
	 */

	protected StructureContext memberContext = null;

	/**
	 * Constructs a handle given an element handle and definition of a property. The
	 * element property definition cannot be null.
	 * 
	 * @param element     handle to the report element that contains the element
	 *                    property.
	 * @param thePropDefn element property definition.
	 */

	public ComplexValueHandle(DesignElementHandle element, ElementPropertyDefn thePropDefn) {
		super(element);
		assert thePropDefn != null;

		propDefn = thePropDefn;
	}

	/**
	 * Constructs a handle given an element handle and member reference. The element
	 * property definition can not be null.
	 * 
	 * @param element          handle to the report element that has the property
	 *                         that contains the structure that contains the member.
	 * @param theMemberContext The context to the member.
	 */

	public ComplexValueHandle(DesignElementHandle element, StructureContext theMemberContext) {
		super(element);
		assert theMemberContext != null;

		propDefn = theMemberContext.getElementProp();

		assert propDefn != null;

		memberContext = theMemberContext;
	}

	/**
	 * Constructs a handle given an element handle and member reference. The element
	 * property definition can not be null.
	 * 
	 * @param element      handle to the report element that has the property that
	 *                     contains the structure that contains the member.
	 * @param theMemberRef The reference to the member.
	 * @deprecated
	 */

	public ComplexValueHandle(DesignElementHandle element, MemberRef theMemberRef) {
		super(element);
		assert theMemberRef != null;

		memberContext = theMemberRef.getContext();
		assert memberContext != null;

		propDefn = memberContext.getElementProp();

		assert propDefn != null;
	}

	/**
	 * Sets the value of a property to the given value. If the value is null, then
	 * the property value is cleared.
	 * 
	 * @param value The new value.
	 * @throws SemanticException If the value is not valid for the property or
	 *                           member.
	 */

	public void setValue(Object value) throws SemanticException {
		PropertyCommand cmd = new PropertyCommand(getModule(), getElement());

		if (memberContext == null) {
			cmd.setProperty(propDefn, value);
		} else {
			cmd.setMember(memberContext, value);
		}
	}

	/**
	 * Gets the value of the property as a generic object.
	 * 
	 * @return The value of the property or member as a generic object.
	 */

	public Object getValue() {
		Object value = getRawValue();

		return ModelUtil.wrapPropertyValue(getModule(), (PropertyDefn) getDefn(), value);
	}

	/**
	 * Returns the value stored in the memory. The return value won't be wrapped.
	 * 
	 * @return the value
	 */

	protected final Object getRawValue() {
		Module tmpModule = getModule();
		Object value = null;
		if (memberContext == null) {
			value = getElement().getProperty(tmpModule, propDefn);
		} else {
			value = memberContext.getValue(tmpModule);
		}

		return value;
	}

	/**
	 * Sets the value of a property or member to a string. Call this method to set a
	 * input string from the user( localized or non-localized value).
	 * 
	 * @param value the value to set
	 * @throws SemanticException if the string value is not valid for the property
	 *                           or member.
	 */

	public void setStringValue(String value) throws SemanticException {
		setValue(value);
	}

	/**
	 * Gets the property value converted to a string value.
	 * 
	 * @return The property or member value as a string.
	 */

	public String getStringValue() {
		PropertyDefn prop = memberContext == null ? propDefn : memberContext.getPropDefn();
		return prop.getStringValue(getModule(), getValue());
	}

	/**
	 * Returns the value of the property or member in a localized format.
	 * 
	 * @return Returns the value of the property or member in a localized format.
	 */

	public String getDisplayValue() {
		PropertyDefn prop = memberContext == null ? propDefn : memberContext.getPropDefn();
		return prop.getDisplayValue(getModule(), getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ValueHandle#getPropertyDefn()
	 */

	public IElementPropertyDefn getPropertyDefn() {
		return this.propDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ValueHandle#getReference()
	 */

	public StructureContext getContext() {
		return this.memberContext;
	}

	/**
	 * Returns a definition for the handle.
	 * 
	 * @return the definition of the handle.
	 */

	protected IPropertyDefn getDefn() {
		// For a member in a structure, the return value
		// is <code>StructPropertyDefn</code>. For an element property, the
		// return value is <code>ElementPropertyDefn</code>.

		if (memberContext != null)
			return memberContext.getPropDefn();

		return propDefn;
	}

	/**
	 * Tests whether this property value is set for this element or the structure.
	 * <p>
	 * <ul>
	 * <li>For an element property, it is set if it is defined on this element
	 * property or any of its parents, or in the element's private style property.
	 * It is considered unset if it is set on a shared style.</li>
	 * <li>For a member, it is set if the value is not <code>null</code>, otherwise
	 * it is considered unset.</li>
	 * </ul>
	 * 
	 * @return <code>true</code> if the value is set, <code>false</code> if it is
	 *         not set
	 */

	public boolean isSet() {
		if (memberContext == null) {
			FactoryPropertyHandle handle = new FactoryPropertyHandle(elementHandle, propDefn);
			return handle.isSet();
		}

		Object value = memberContext.getLocalValue(getModule());
		return (value != null);
	}
}
