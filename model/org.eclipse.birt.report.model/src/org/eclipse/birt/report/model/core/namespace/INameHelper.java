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

package org.eclipse.birt.report.model.core.namespace;

import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * 
 */
public interface INameHelper {

	/**
	 * Clears all the cached element names in this helper.
	 */
	public void clear();

	/**
	 * 
	 * Returns unique name of element.
	 * <tr>
	 * <td>if element is null or elements have a blank name</td>
	 * <td>return null</td>
	 * </tr>
	 * <tr>
	 * <td>if element is group element</td>
	 * <td>return name of group element</td>
	 * </tr>
	 * <tr>
	 * <td>if element is already has a unique name</td>
	 * <td>return it</td>
	 * </tr>
	 * <tr>
	 * <td>if element has no name in library</td>
	 * <td>return newly created unique name</td>
	 * </tr>
	 * 
	 * @param element the design element.
	 * @return unique name of element.
	 * 
	 */
	public String getUniqueName(String namespaceId, DesignElement element);

	/**
	 * 
	 * Returns unique name of element with the given name prefix.
	 * <tr>
	 * <td>if element is null or the given prefix is blank</td>
	 * <td>return null</td>
	 * </tr>
	 * <tr>
	 * <td>if element is group element</td>
	 * <td>return name of group element</td>
	 * </tr>
	 * <tr>
	 * <td>if element is already has a unique name</td>
	 * <td>return it</td>
	 * </tr>
	 * <tr>
	 * <td>if element has no name in library</td>
	 * <td>return newly created unique name</td>
	 * </tr>
	 * 
	 * @param element    the design element.
	 * @param namePrefix the name prefix
	 * @return unique name of element.
	 * 
	 */
	public String getUniqueName(String namespaceId, DesignElement element, String namePrefix);

	public void makeUniqueName(String namespaceId, DesignElement element);

	/**
	 * make the unique name of element with the given name prefix
	 * 
	 * @param namespaceId the name space id
	 * @param element     the design element.
	 * @param prefix      the name prefix
	 */
	public void makeUniqueName(String namespaceId, DesignElement element, String prefix);

	/**
	 * remove a element from cached namespace
	 * 
	 * @param namespace
	 * @param element
	 */
	public void dropElement(String namespaceId, DesignElement element);

	/**
	 * Adds a content name to this help. Generally, this content is not generated
	 * when the extension is not found. However, its name is reserved to avoid
	 * duplicate.
	 * 
	 * @param id
	 * @param name
	 */
	public void addContentName(String namespaceId, String name);

	/**
	 * Gets the holder element of this name helper.
	 * 
	 * @return the focus element that holds the name
	 */
	public DesignElement getElement();

	/**
	 * Gets the namespace of this context.
	 * 
	 * @param nameSpaceID
	 * 
	 * @return the namespce instance with the specified id
	 */
	public NameSpace getNameSpace(String nameSpaceID);

	/**
	 * Resolves the given element name to element reference value.
	 * <code>propDefn</code> gives the resolver information that how to resolve the
	 * <code>elementName</code>. For example, <code>extends</code> and
	 * <code>theme</code> property definitions must check elements in the included
	 * libraries. Other properties are not required such checks. The returned
	 * reference value might be resolved or unresolved.
	 * 
	 * 
	 * @param elementName the element name
	 * @param propDefn    the property definition
	 * @param elementDefn
	 * @return the element reference value.
	 */

	public ElementRefValue resolve(DesignElement focus, String elementName, PropertyDefn propDefn,
			IElementDefn elementDefn);

	/**
	 * Resolves the given element name to element reference value.
	 * <code>propDefn</code> gives the resolver information that how to resolve the
	 * <code>elementName</code>. For example, <code>extends</code> and
	 * <code>theme</code> property definitions must check elements in the included
	 * libraries. Other properties are not required such checks. The returned
	 * reference value might be resolved or unresolved.
	 * <p>
	 * If the <code>element</code> is not invalid in the current resolve scope, the
	 * return reference value is unresolved.
	 * <p>
	 * The namespace information may be lost.
	 * 
	 * @param element     the element to resolve
	 * @param propDefn    the property definition
	 * @param elementDefn
	 * @return the element reference value.
	 */

	public ElementRefValue resolve(DesignElement focus, DesignElement element, PropertyDefn propDefn,
			IElementDefn elementDefn);

	/**
	 * Returns all elements in the module this module namespace is associated and
	 * those in the included modules.
	 * 
	 * @param nameSpaceID
	 * 
	 * @param level       the depth of included libraries
	 * 
	 * @return all element in this namespace.
	 */

	public List<DesignElement> getElements(String nameSpaceID, int level);

	/**
	 * Checks whether the given element name is acceptable in this module name
	 * space.
	 * 
	 * @param nameSpaceID
	 * 
	 * @param elementName the element name
	 * @return true if the given element is accepted, otherwise, return false.
	 */

	public boolean canContain(String nameSpaceID, String elementName);

	/**
	 * Caches values for elements with names such as styles, etc.
	 */

	public void cacheValues();
}
