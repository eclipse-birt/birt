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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * A handle for working with a top-level property of a collection of elements.
 * Use this handle to set/get values of a property if this property is common
 * across the given collection of elements.
 */

public class GroupPropertyHandle {

	/**
	 * Definition of the property.
	 */

	protected ElementPropertyDefn propDefn = null;

	/**
	 * Handle to a collection of elements.
	 */

	protected GroupElementHandle handle = null;

	/**
	 * Constructs a handle to deal with an common property within a group of
	 * elements. The given property definition should be a common property shared by
	 * the collection of elements.
	 *
	 * @param handle   Handles to a collection of elements.
	 * @param propDefn definition of the property.
	 */

	GroupPropertyHandle(GroupElementHandle handle, ElementPropertyDefn propDefn) {
		assert propDefn != null;
		assert handle instanceof SimpleGroupElementHandle;

		this.handle = handle;
		this.propDefn = propDefn;
	}

	/**
	 * Indicates whether the group of element share the same value for this
	 * property.
	 * <p>
	 * If all element has a <code>null</code> value for this property, it is
	 * considered that they share the same value.
	 *
	 * @return <code>true</code> if the group of element share the same value.
	 */

	public final boolean shareSameValue() {
		Iterator iter = handle.getElements().iterator();

		// List with no content.

		if (!iter.hasNext()) {
			return false;
		}

		DesignElementHandle elemHandle = (DesignElementHandle) iter.next();

		// use the value set on the first element as the base value.

		Object baseValue = getPropertyValue(elemHandle);

		while (iter.hasNext()) {
			elemHandle = (DesignElementHandle) iter.next();
			Object value = getPropertyValue(elemHandle);

			if (!Objects.equals(baseValue, value)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Gets the property value of the given element
	 *
	 * @param elementHandle the handle of the given element
	 *
	 * @return the property value
	 */
	private Object getPropertyValue(DesignElementHandle elementHandle) {
		if (propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE) {
			return elementHandle.getProperty(propDefn.getName());
		} else if (propDefn.allowExpression()) {
			return elementHandle.getElement().getProperty(elementHandle.module, propDefn);
		} else {
			return elementHandle.getStringProperty(propDefn.getName());
		}
	}

	/**
	 * Value will be returned as string only if all values of this property are
	 * equal within the collection of elements.
	 *
	 * @return The value as string if all the element values for the property are
	 *         equal. Return null, if elements have different value for the
	 *         property.
	 * @see SimpleValueHandle#getStringValue()
	 */

	public String getStringValue() {
		DesignElementHandle element = getSameValueElementHandle();

		if (element == null) {
			return null;
		}

		return element.getStringProperty(propDefn.getName());
	}

	/**
	 * Value will be returned as string only if all values of this property are
	 * equal within the collection of elements and one of them has a local value.
	 *
	 * @return The value as string if all the element values for the property are
	 *         equal and one of them has a local value. Return null, if elements
	 *         have different value for the property or none of them has a local
	 *         value.
	 */

	public String getLocalStringValue() {
		Object value = getLocalValue();
		if (value == null) {
			return null;
		}

		DesignElementHandle element = (DesignElementHandle) handle.getElements().get(0);

		String localValue = propDefn.getStringValue(element.getEffectiveModule(), value);
		return localValue;
	}

	/**
	 * Value will be returned only if all values of this property are equal within
	 * the collection of elements and one of them has a local value.
	 *
	 * @return The value if all the element values for the property are equal and
	 *         one of them has a local value. Return null, if elements have
	 *         different value for the property or none of them has a local value.
	 */

	protected Object getLocalValue() {
		if (!shareSameValue()) {
			return null;
		}
		List elements = handle.getElements();
		for (int i = 0; i < elements.size(); i++) {
			DesignElementHandle element = (DesignElementHandle) elements.get(i);
			Object value = element.getElement().getLocalProperty(element.getModule(), propDefn);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	/**
	 * Value will be returned as string only if all values of this property are
	 * equal within the collection of elements. The value return are localized.
	 *
	 * @return The localized value as string if all the element values for the
	 *         property are equal. Return null, if elements have different value for
	 *         the property.
	 * @see SimpleValueHandle#getDisplayValue()
	 */

	public String getDisplayValue() {
		if (!shareSameValue()) {
			return null;
		}

		// List must contain at least one element.
		// return the property value from the first element.

		List elements = handle.getElements();

		return ((DesignElementHandle) elements.get(0)).getDisplayProperty(propDefn.getName());
	}

	/**
	 * Set the object value on a group of elements. This operation will be executed
	 * within a transaction, it will be rollbacked if any set operation failed.
	 *
	 * @param value the object value to set
	 * @throws SemanticException if the property is undefined on an element or the
	 *                           value is invalid.
	 * @see PropertyHandle#setValue(Object)
	 */

	public void setValue(Object value) throws SemanticException {
		assert handle.getModule() != null;
		ActivityStack actStack = handle.getModule().getActivityStack();

		actStack.startTrans(changePropertyMessage());

		try {
			for (Iterator iter = handle.getElements().iterator(); iter.hasNext();) {
				DesignElementHandle elemHandle = (DesignElementHandle) iter.next();
				elemHandle.setProperty(propDefn.getName(), value);
			}
		} catch (SemanticException e) {
			actStack.rollback();
			throw e;
		}

		actStack.commit();
	}

	/**
	 * Set the string value on a group of elements. This operation will be executed
	 * within a transaction, it will be rollbacked if any set operation failed.
	 *
	 * @param value the string value to set
	 * @throws SemanticException if the property is undefined on an element or the
	 *                           string value is invalid.
	 * @see SimpleValueHandle#setStringValue(String)
	 */

	public void setStringValue(String value) throws SemanticException {
		setValue(value);
	}

	/**
	 * Return the property definition.
	 *
	 * @return the property definition.
	 */

	public IElementPropertyDefn getPropertyDefn() {
		return this.propDefn;
	}

	/**
	 * Clears the value of the property on every element.
	 *
	 * @throws SemanticException If the value cannot be cleared.
	 */

	public void clearValue() throws SemanticException {
		if (isExtensionXMLProperty()) {
			ActivityStack actStack = handle.getModule().getActivityStack();
			actStack.startTrans(changePropertyMessage());

			try {
				for (Iterator iter = handle.getElements().iterator(); iter.hasNext();) {
					DesignElementHandle elemHandle = (DesignElementHandle) iter.next();
					assert elemHandle instanceof ExtendedItemHandle;

					ExtendedItem parent = (ExtendedItem) ModelUtil.getParent(elemHandle.getElement());
					elemHandle.setProperty(propDefn.getName(), parent.getLocalProperty(parent.getRoot(), propDefn));
				}
			} catch (SemanticException e) {
				actStack.rollback();
				throw e;
			}

			actStack.commit();
		} else {
			setValue(null);
		}
	}

	/**
	 * Tests if this property is an extension defined property.
	 *
	 * @return <code>true</code> if this property is an extension defined property,
	 *         <code>false</code> otherwise.
	 */

	boolean isExtensionModelProperty() {
		for (Iterator iter = handle.getElements().iterator(); iter.hasNext();) {
			DesignElementHandle elemHandle = (DesignElementHandle) iter.next();
			if (elemHandle instanceof ExtendedItemHandle
					&& ((ExtendedItem) elemHandle.getElement()).isExtensionModelProperty(propDefn.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tests if this property is an extension defined xml property.
	 *
	 * @return <code>true</code> if this property is an extension defined xml
	 *         property, <code>false</code> otherwise.
	 */

	boolean isExtensionXMLProperty() {
		for (Iterator iter = handle.getElements().iterator(); iter.hasNext();) {
			DesignElementHandle elemHandle = (DesignElementHandle) iter.next();
			if (elemHandle instanceof ExtendedItemHandle
					&& ((ExtendedItem) elemHandle.getElement()).isExtensionXMLProperty(propDefn.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the element reference value list if the property is element
	 * referenceable type. The list of available elements are sorted by their names
	 * lexicographically.
	 *
	 * @return list of the reference element value.
	 */

	public List getReferenceableElementList() {
		List elements = handle.getElements();
		if (elements == null || elements.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		DesignElementHandle element = (DesignElementHandle) elements.get(0);
		PropertyHandle propHandle = element.getPropertyHandle(propDefn.getName());

		List retList = propHandle.getReferenceableElementList();

		return ModelUtil.sortElementsByName(retList);
	}

	/**
	 * Compares the specified Object with this <code>GroupPropertyHandle</code> for
	 * equality. Returns <code>true</code> in the following cases:
	 * <ul>
	 * <li><code>target</code> is a <code>PropertyHandle</code>. The element of
	 * <code>target</code> is in the <code>GroupElementHandle</code> and two
	 * property definitions are same.</li>
	 * <li><code>target</code> is a <code>
	 * GroupPropertyHandle</code>. <code>GroupElementHandle</code> and the the
	 * property definition are same.</li>
	 * </ul>
	 *
	 * @param target the property or group property handle
	 * @return <code>true</code> if the two property handles are considerred as
	 *         same. Otherwise <code>false</code>.
	 */

	@Override
	public boolean equals(Object target) {
		if (!(target instanceof PropertyHandle) && !(target instanceof GroupPropertyHandle)) {
			return false;
		}

		if (target instanceof PropertyHandle) {
			DesignElementHandle targetElement = ((PropertyHandle) target).getElementHandle();
			IPropertyDefn targetPropDefn = ((PropertyHandle) target).getDefn();
			return (handle.isInGroup(targetElement) && targetPropDefn == this.propDefn);
		}

		GroupPropertyHandle propHandle = (GroupPropertyHandle) target;
		return (propHandle.handle == this.handle && propHandle.propDefn == getPropertyDefn());
	}

	/**
	 * Checks whether a property is visible in the property sheet. The visible
	 * property is visible in all <code>elements</code>.
	 *
	 * @return <code>true</code> if it is visible. Otherwise <code>false</code>.
	 */

	public boolean isVisible() {
		return handle.isPropertyVisible(propDefn.getName());
	}

	/**
	 * Checks whether a property is read-only in the property sheet. The read-only
	 * property is read-only in all <code>elements</code>.
	 *
	 * @return <code>true</code> if it is read-only. Otherwise <code>false</code>.
	 */

	public boolean isReadOnly() {
		return handle.isPropertyReadOnly(propDefn.getName());
	}

	/**
	 * Gets the property message.
	 *
	 * @return the property message.
	 */
	private String changePropertyMessage() {
		return CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[] { propDefn.getDisplayName() });
	}

	/**
	 * Gets the value of the property. Value will be returned as object only if all
	 * values of this property are equal within the collection of elements.
	 *
	 * @return The value if all the element values for the property are equal.
	 *         Return null, if elements have different value for the property.
	 * @see SimpleValueHandle#getValue()
	 */

	public Object getValue() {
		DesignElementHandle element = getSameValueElementHandle();

		if (element == null) {
			return null;
		}

		if (propDefn.allowExpression()) {
			if (propDefn.isListType()) {
				// No such case in rom now
				assert false;
				return null;
			}
			return element.getExpressionProperty(propDefn.getName()).getValue();
		} else {
			return element.getProperty(propDefn.getName());
		}

	}

	/**
	 * Gets the first handle of the design elements which contain at least one
	 * element and share the same value of the property.
	 */
	private DesignElementHandle getSameValueElementHandle() {
		if (!shareSameValue()) {
			return null;
		}

		// List must contain at least one element.
		// return the property value from the first element.

		return (DesignElementHandle) handle.getElements().get(0);
	}
}
