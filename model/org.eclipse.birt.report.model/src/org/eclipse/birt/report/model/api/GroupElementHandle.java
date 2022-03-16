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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * This class provides services to deal with a group of elements. It is mostly
 * useful for some multiple-selection cases, elements of the same type(or share
 * the same base type) can be handled as a whole. User can perform some
 * operations on the collection of elements.
 * <p>
 * For our ease-of-use purpose, we support multiple selections across type.
 * Given a collection of elements, user can ask for their common
 * properties(including user property definitions). Also, return a list of
 * values that are identical for all items. Finally, return an indication of
 * whether all elements are of the same type.
 * <p>
 * This class also supports a collection of elements contains some objects that
 * are not a <code>DesignElementHandle</code> or its subclass. For this case,
 * <code>getCommonProperties</code> always returns an empty list.
 * <p>
 * For BIRT UI usage, the attributes view will go blank if the providing
 * elements are not of the same type, the property sheet will show the common
 * properties(including user property definitions).
 * <p>
 * This handle is mutable, it can be kept. The query results changed as the
 * given elements themselves changed.
 * <p>
 * Note that the Model special handling of the case where all elements are the
 * same type: in this case, by definition, all BIRT-defined properties are the
 * same. (User-defined properties may differ.)
 */

abstract public class GroupElementHandle {

	/**
	 * Default constructor.
	 */

	public GroupElementHandle() {
	}

	/**
	 * Returns the list that contains the group of design elements. Contents of it
	 * is <code>DesignElementHandle</code>
	 *
	 * @return the list that contains the group of design elements.
	 */

	abstract public List getElements();

	/**
	 * Returns the module.
	 *
	 * @return the module
	 */

	abstract public Module getModule();

	/**
	 * Returns the handle of module.
	 *
	 * @return the handle of module
	 */

	abstract public ModuleHandle getModuleHandle();

	/**
	 * Indicates that if the given elements are of the same definition. Elements are
	 * considered of same type if their element definitions are identical.
	 * <p>
	 * If elements have different definitions. Even the same element type, the
	 * return value is <code>false</code>. For example, if the list contains an
	 * <code>OdaDataSource</code> and a <code>OdaDataSource</code>, this method
	 * returns <code>false</code>.
	 *
	 * @return <code>true</code> if the given elements are of the same type; return
	 *         <code>false</code> if elements are of different element types, or the
	 *         given list is empty, or the list contains any object that is not an
	 *         instance of <code>DesignElementHandle</code>.
	 */

	abstract public boolean isSameType();

	/**
	 * Returns the common properties shared by the given group of elements(including
	 * user properties). Contents of the list is element property definitions. If
	 * elements do not share any common property, return an empty list.
	 *
	 * @return the common properties shared by the given group of elements. If
	 *         elements do not share any common property, or the given list is
	 *         empty, or the list contains any item that is not an instance of
	 *         <code>DesignElementHandle</code>, return an empty list.
	 */

	abstract public List getCommonProperties();

	/**
	 * Returns an iterator over the common properties. Contents of the iterator are
	 * handles to the common properties, type of them is
	 * <code>GroupPropertyHandle</code>. Note: remove is not support for the
	 * iterator.
	 *
	 * @return an iterator over the common properties. Contents of the iterator are
	 *         handles to the common properties, type of them is
	 *         <code>GroupPropertyHandle</code>
	 */

	public final Iterator propertyIterator() {
		return new GroupPropertyIterator(getCommonProperties());
	}

	/**
	 * Returns an iterator over the common properties that are visible. Contents of
	 * the iterator are handles to the common properties, type of them is
	 * <code>GroupPropertyHandle</code>. Note: remove is not support for the
	 * iterator.
	 *
	 * @return an iterator over the common properties. Contents of the iterator are
	 *         handles to the common properties, type of them is
	 *         <code>GroupPropertyHandle</code>
	 */

	abstract public Iterator visiblePropertyIterator();

	/**
	 * Checks whether a property is visible in the property sheet. The visible
	 * property is visible in all <code>elements</code>.
	 *
	 * @param propName the property name
	 * @return <code>true</code> if it is visible. Otherwise <code>false</code>.
	 */

	abstract protected boolean isPropertyVisible(String propName);

	/**
	 * Clears values of all common properties(except the extends property) for the
	 * given collection of elements. Clearing a property removes any value set for
	 * the property on this element. After this, the element will now inherit the
	 * property from its parent element, style, or from the default value for the
	 * property. Note: this method clear the values of local properties ( not
	 * include sub element)
	 *
	 * @throws SemanticException if the property is not defined on this element
	 */
	abstract public void clearLocalProperties() throws SemanticException;

	/**
	 * Clears values of all common properties(except the extends property) for the
	 * given collection of elements. Clearing a property removes any value set for
	 * the property on this element. After this, the element will now inherit the
	 * property from its parent element, style, or from the default value for the
	 * property. Note: this method clear all the values of local properties (include
	 * sub element)
	 *
	 * @throws SemanticException if the property is not defined on this element
	 */
	abstract public void clearLocalPropertiesIncludeSubElement() throws SemanticException;

	/**
	 * Returns <code>true</code> if each of the given collection of element extends
	 * has a parent. Returns <code>false</code> otherwise. If the collection has no
	 * elements, also return <code>false</code>
	 *
	 * @return <code>true</code> if each of the given collection of element extends
	 *         has a parent. Returns <code>false</code> otherwise. If the collection
	 *         has no elements, also return <code>false</code>
	 */

	abstract public boolean isExtendedElements();

	/**
	 * This method returnt <code>true</code> in following condition:
	 * <p>
	 * 1. The multi selected elements are same type.
	 * <p>
	 * 2. And the multi selected elements have extends.
	 * <p>
	 * 3. If any of the given elements has local properties.
	 * <p>
	 *
	 * @return <code>true</code> if the conditions is met.
	 */

	public final boolean hasLocalPropertiesForExtendedElements() {
		if (!isSameType() || !allExtendedElements()) {
			return false;
		}

		List elements = getElements();
		for (Iterator iter = elements.iterator(); iter.hasNext();) {
			DesignElementHandle elementHandle = (DesignElementHandle) iter.next();
			boolean hasLocalProperties = elementHandle.hasLocalProperties();
			if (hasLocalProperties) {
				return true;
			}

			continue;
		}

		return false;
	}

	/**
	 * This method returnt <code>true</code> in following condition:
	 * <p>
	 * 1. The multi selected elements are same type.
	 * <p>
	 * 2. And the multi selected elements have extends.
	 * <p>
	 * 3. If any of the given elements or their subElement has local properties.
	 * <p>
	 *
	 * @return <code>true</code> if the conditions is met.
	 */
	public final boolean hasLocalPropertiesIncludeSubElement() {
		if (!isSameType() || !allExtendedElements()) {
			return false;
		}

		List elements = getElements();
		for (Iterator iter = elements.iterator(); iter.hasNext();) {
			DesignElementHandle elementHandle = (DesignElementHandle) iter.next();
			boolean hasLocalProperties = hasLocalPropertiesIncludeSubElement(elementHandle);
			if (hasLocalProperties) {
				return true;
			}

			continue;
		}
		return false;
	}

	/**
	 * return true if the Element or subElement has local properties
	 *
	 * @param elementHandle
	 * @return true if the Element or his subElement has local properties
	 */
	protected boolean hasLocalPropertiesIncludeSubElement(DesignElementHandle elementHandle) {
		boolean hasLocalProperties = elementHandle.hasLocalProperties();
		if (hasLocalProperties) {
			return true;
		} else {
			Iterator iterator = elementHandle.getPropertyIterator();
			while (iterator.hasNext()) {
				PropertyHandle propertyHandler = (PropertyHandle) iterator.next();
				if (propertyHandler.getPropertyDefn().getTypeCode() == IPropertyType.ELEMENT_TYPE) {
					List list = propertyHandler.getContents();
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							DesignElementHandle handle = (DesignElementHandle) list.get(i);
							boolean hasProperties = hasLocalPropertiesIncludeSubElement(handle);
							if (hasProperties) {
								return true;
							}
							continue;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if all elements have extends parents or virtual parents.
	 *
	 * @return <code>true</code> If all elements have extend parents or virtual
	 *         parents. Otherwise <code>false</code>;
	 */

	protected abstract boolean allExtendedElements();

	/**
	 * Checks whether a property is read-only in the property sheet. The visible
	 * property is read-only in all <code>elements</code>.
	 *
	 * @param propName the property name
	 * @return <code>true</code> if it is read-only. Otherwise <code>false</code>.
	 */

	abstract protected boolean isPropertyReadOnly(String propName);

	/**
	 * If property is shared by the group of elements, return the corresponding
	 * <code>GroupPropertyHandle</code>, otherwise, return <code>null</code>.
	 *
	 * @param propName name of the property needs to be handled.
	 * @return If the property is a common property among the elements, return the
	 *         corresponding <code>GroupPropertyHandle</code>; Otherwise return
	 *         <code>null</code>.
	 */

	abstract public GroupPropertyHandle getPropertyHandle(String propName);

	/**
	 * If the given property is a common property, value will be returned as a
	 * string if all values within the group of elements are equal. If the property
	 * is not a common property, return <code>null</code>.
	 *
	 * @param propName name of the property.
	 * @return the value as a string if the property is a common property and all
	 *         the elements have the same value. Return null if the property is not
	 *         a common property or elements have different values for this
	 *         property.
	 * @see GroupPropertyHandle#getStringValue()
	 */

	public final String getStringProperty(String propName) {
		GroupPropertyHandle propHandle = getPropertyHandle(propName);
		if (propHandle == null) {
			return null;
		}

		return propHandle.getStringValue();
	}

	/**
	 * If the given property is a common property, value will be returned as a
	 * display value if all values within the group of elements are equal. If the
	 * property is not a common property, return <code>null</code>.
	 *
	 * @param propName name of the property.
	 * @return the value as a display value if the property is a common property and
	 *         all the elements have the same value. Return null if the property is
	 *         not a common property or elements have different values for this
	 *         property.
	 * @see GroupPropertyHandle#getDisplayProperty()
	 */

	public final String getDisplayProperty(String propName) {
		GroupPropertyHandle propHandle = getPropertyHandle(propName);
		if (propHandle == null) {
			return null;
		}

		return propHandle.getDisplayValue();
	}

	/**
	 * If the given property is a common property, value will be returned as a
	 * string if all values within the group of elements are equal and one of them
	 * has a local value. If the property is not a common property or none of them
	 * has a local value, return <code>null</code>.
	 *
	 * @param propName name of the property.
	 * @return the value as a string if the property is a common property, all the
	 *         elements have the same value and one of them has a local value.
	 *         Return null if the property is not a common property or elements have
	 *         different values for this property or none of them has a local value.
	 * @see GroupPropertyHandle#getLocalStringValue()
	 */

	public final String getLocalStringProperty(String propName) {
		GroupPropertyHandle propHandle = getPropertyHandle(propName);
		if (propHandle == null) {
			return null;
		}

		return propHandle.getLocalStringValue();
	}

	/**
	 * Indicates whether the group of element share the same value for this
	 * property.
	 * <p>
	 * If all element has a <code>null</code> value for this property, it is
	 * considered that they share the same value.
	 *
	 * @param propName name of the property.
	 * @return <code>true</code> if the group of element share the same value.
	 *         Return <code>false</code> if the property is not a common property or
	 *         elements have different values for this property.
	 */

	public final boolean shareSameValue(String propName) {
		GroupPropertyHandle propHandle = getPropertyHandle(propName);
		if (propHandle == null) {
			return false;
		}

		return propHandle.shareSameValue();
	}

	/**
	 * Set the value of a property on the given collection of elements. If the
	 * property provided is not a common property then this method simply return;
	 * Otherwise, the value will be set on the group of elements.
	 *
	 * @param propName name of the property.
	 * @param value    value needs to set.
	 * @throws SemanticException if the value is invalid for the property, or the
	 *                           property is undefined on the elements.
	 * @see GroupPropertyHandle#setValue(Object)
	 */

	public final void setProperty(String propName, Object value) throws SemanticException {
		GroupPropertyHandle propHandle = getPropertyHandle(propName);
		if (propHandle == null) {
			return;
		}

		propHandle.setValue(value);
	}

	/**
	 * Clears the value of a property on the given collection of elements if the
	 * property is a common property shared by each element. Clearing a property
	 * removes any value set for the property on this element. After this, the
	 * element will now inherit the property from its parent element, style, or from
	 * the default value for the property.
	 * <p>
	 * If the property provided is not a common property then this method simply
	 * return, else, the value will be cleared on the group of elements.
	 *
	 * @param propName the name of the property to clear.
	 * @throws SemanticException if the property is not defined on this element
	 */

	public final void clearProperty(String propName) throws SemanticException {
		setProperty(propName, null);
	}

	/**
	 * Set the value of a property to a string . If the property provided is not a
	 * common property then this method simply return; Else, the string value will
	 * be set on the group of element.
	 *
	 * @param propName name of the property.
	 * @param value    value needs to set.
	 * @throws SemanticException if the value is invalid for the property, or the
	 *                           property is undefined on the elements.
	 */

	public final void setStringProperty(String propName, String value) throws SemanticException {
		setProperty(propName, value);
	}

	/**
	 * Checks whether the <code>element</code> is a member of
	 * <code>GroupElementHandle</code>.
	 *
	 * @param element the element to check
	 * @return <code>true</code> if the element is in the list, otherwise
	 *         <code>false</code>.
	 */

	abstract protected boolean isInGroup(DesignElementHandle element);

	/**
	 * An iterator over the properties defined for elements that in the group
	 * element.
	 */

	class GroupPropertyIterator implements Iterator {

		/**
		 * The property iterator to traverse the properties.
		 */

		Iterator propIterator;

		/**
		 * Constructs the group property iterator with the common property list.
		 *
		 * @param list
		 */

		GroupPropertyIterator(List list) {
			propIterator = list.iterator();
		}

		@Override
		public void remove() {
			// not support.
		}

		@Override
		public boolean hasNext() {
			if (propIterator == null) {
				return false;
			}

			return propIterator.hasNext();
		}

		@Override
		public Object next() {
			if (!propIterator.hasNext()) {
				return null;
			}

			return new GroupPropertyHandle(GroupElementHandle.this, (ElementPropertyDefn) propIterator.next());
		}
	}
}
