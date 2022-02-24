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

package org.eclipse.birt.report.model.api.metadata;

import java.util.List;

/**
 * Interface for a report element definition. This interface defines some
 * methods to get "meta-data" about an element.
 */

public interface IElementDefn extends IObjectDefn {

	/**
	 * Indicates if this element has a style.
	 * 
	 * @return Returns whether the element has a style.
	 */

	public boolean hasStyle();

	/**
	 * Returns the properties defined on this element. Each object in the list is
	 * instance of {@link IElementPropertyDefn}.
	 * 
	 * @return list of properties defined in this element and and all its parent
	 *         elements.
	 */

	public List<IElementPropertyDefn> getProperties();

	/**
	 * Returns properties definitions as a list. Each object in the list is instance
	 * of {@link IElementPropertyDefn}.
	 * 
	 * @return list of locally-defined properties.
	 */

	public List<IElementPropertyDefn> getLocalProperties();

	/**
	 * Gets a property definition given a property name.
	 * 
	 * @param propName The name of the property to get.
	 * @return The property with that name, or null if the property cannot be found.
	 */

	public IElementPropertyDefn getProperty(String propName);

	/**
	 * Returns the method definition list of this element definition and parent
	 * definition. Each object in the list is instance of
	 * {@link IElementPropertyDefn}.
	 * 
	 * @return the method definition list.
	 */

	public List<IElementPropertyDefn> getMethods();

	/**
	 * Returns the method definition list of this element definition. Each object in
	 * the list is instance of {@link IElementPropertyDefn}.
	 * 
	 * @return the method definition list.
	 */

	public List<IElementPropertyDefn> getLocalMethods();

	/**
	 * Returns the expression property definition list of this element definition
	 * and parent definition. Each object in the list is instance of
	 * {@link IElementPropertyDefn}.
	 * 
	 * @return the expression property definition list.
	 */

	public List<IElementPropertyDefn> getExpressions();

	/**
	 * Returns the expression property definition list of this element definition.
	 * Each object in the list is instance of {@link IElementPropertyDefn}.
	 * 
	 * @return the expression property definition list.
	 */

	public List<IElementPropertyDefn> getLocalExpressions();

	/**
	 * Returns a list of the localized property group names defined by this element
	 * and its parents.
	 * <p>
	 * The UI uses property groups to organize properties within the generic
	 * property sheet.
	 * 
	 * @return The list of group names. If there is no groups defined on the
	 *         element, the list will has no content.
	 */

	public List<String> getGroupNames();

	/**
	 * Determines if this element allows user properties.
	 * 
	 * @return Returns true if the element supports user-defined properties, false
	 *         if not.
	 */

	public boolean allowsUserProperties();

	/**
	 * Determines if this element acts as a container.
	 * 
	 * @return True if this element is a container, false otherwise.
	 */

	public boolean isContainer();

	/**
	 * Returns the number of slots in this container.
	 * 
	 * @return The number of slots. Returns 0 if this element is not a container.
	 */

	public int getSlotCount();

	/**
	 * Returns whether this element has the requested slot given the numeric
	 * identifier of the slot.
	 * 
	 * @param slotID The slotID to check.
	 * @return True if the slotID exists, false otherwise.
	 */

	public boolean hasSlot(int slotID);

	/**
	 * Returns the meta-data definition for a slot given its numeric slot
	 * identifier.
	 * 
	 * @param slotID The slot identifier.
	 * @return The slot information. Returns null if this element is not a
	 *         container, or if the ID is not valid for this container.
	 */

	public ISlotDefn getSlot(int slotID);

	/**
	 * Returns the property definitions for this element that can hold other
	 * elements. Each one in the list is instance of <code>IPropertyDefn</code>.
	 * 
	 * @return the list of the property definition that can hold other elements
	 */
	public List<IElementPropertyDefn> getContents();

	/**
	 * Reports whether the given slot can contain elements of the given type.
	 * 
	 * @param slot The slot to check.
	 * @param type The element type to check.
	 * @return True if the slot can contain that element type, false if the element
	 *         is not a container, if the slot does not exist, or if the slot can't
	 *         contain that type of element.
	 */

	public boolean canContain(int slot, IElementDefn type);

	/**
	 * Returns whether elements of this class can be extended.
	 * 
	 * @return True if the element can be extended, false if not.
	 */

	public boolean canExtend();

	/**
	 * Gets the name option that says how the element type handles names. One of the
	 * following defined in {@link MetaDataConstants}:
	 * <ul>
	 * <li>{@link MetaDataConstants#NO_NAME}-- The element cannot have a name.
	 * (Probably not used.)</li>
	 * <li>{@link MetaDataConstants#OPTIONAL_NAME}-- The element can optionally have
	 * a name, but a name is not required.</li>
	 * <li>{@link MetaDataConstants#REQUIRED_NAME}-- The element must have a
	 * name.</li>
	 * </ul>
	 * 
	 * @return the name option
	 */

	public int getNameOption();

	/**
	 * Checks whether the property is visible to the property sheet.
	 * 
	 * @param propName the property name
	 * 
	 * @return <code>true</code> if the element definition has the property
	 *         definition and it is visible, <code>false</code> otherwise.
	 */

	public boolean isPropertyVisible(String propName);

	/**
	 * Checks whether the property value is read-only in the property sheet.
	 * 
	 * @param propName the property name
	 * 
	 * @return <code>true</code> if the element definition has the property
	 *         definition and it is readonly, <code>false</code> otherwise.
	 */

	public boolean isPropertyReadOnly(String propName);

	/**
	 * Determines if the given element type is a kind of this type. It is if either
	 * the given type is the same as this one, or if the given type derives from
	 * this type.
	 * 
	 * @param type The element type to check.
	 * @return True if it is a kind of this element, false otherwise.
	 */

	public boolean isKindOf(IElementDefn type);

	/**
	 * Justifies whether this definition is extension element.
	 * 
	 * @return true if it is extension element
	 */
	public boolean isExtendedElement();

}
