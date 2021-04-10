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
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Represents the module name space. The module name space is similar with
 * {@link org.eclipse.birt.report.model.core.NameSpace}, but it includes all
 * elements of the same type in one moudle and its included modules. This class
 * processes the different logic for different element type.
 */
public interface INameContext {

	/**
	 * Returns all elements in the module this module namespace is assocaited and
	 * those in the included modules.
	 * 
	 * @param level the depth of included libraries
	 * 
	 * @return all element in this namespace.
	 */

	public List<DesignElement> getElements(int level);

	/**
	 * Checks whether the given element name is acceptable in this module name
	 * space.
	 * 
	 * @param elementName the element name
	 * @return true if the given element is accepted, otherwise, return false.
	 */

	public boolean canContain(String elementName);

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
	 * @return the element reference value.
	 */

	public ElementRefValue resolve(DesignElement focus, String elementName, PropertyDefn propDefn,
			ElementDefn elementDefn);

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
	 * @param element  the element to resolve
	 * @param propDefn the property definition
	 * @return the element reference value.
	 */

	public ElementRefValue resolve(DesignElement focus, DesignElement element, PropertyDefn propDefn,
			ElementDefn elementDefn);

	/**
	 * Gets the namespace of this context.
	 * 
	 * @return the name space
	 */
	public NameSpace getNameSpace();

	/**
	 * Finds an element whose name is the specified and definition is kind of the
	 * given type.
	 * 
	 * @param elementName the qualified name of the element, such as lib.dimension,
	 *                    hierarchy, lib.dimensionA/levelA
	 * @param elementDefn
	 * @return the element with the specified name
	 */
	public DesignElement findElement(String elementName, IElementDefn elementDefn);

	/**
	 * Gets the design element where this name context effects.
	 * 
	 * @return the focus element
	 */
	public DesignElement getElement();

	/**
	 * Gets the identifier of this name context in its host design element.
	 * 
	 * @return the name space id
	 */
	public String getNameSpaceID();
}
