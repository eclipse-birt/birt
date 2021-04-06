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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * An iterator over the properties defined for an element. Includes both user
 * and system properties. Includes both those defined on this element itself,
 * and those inherited from other elements. Includes style properties supported
 * by this element.
 * <p>
 * Properties held by this iterator is sorted based on their localized display
 * name.
 * <p>
 * Items returned by this iterator are of type PropertyHandle.
 * 
 * @see PropertyHandle
 */

class PropertyIterator implements Iterator {

	/**
	 * The element that holds these properties.
	 */

	protected DesignElementHandle elementHandle;

	/**
	 * Iterator over the underlying list.
	 */

	protected Iterator iter;

	/**
	 * Constructs the handle for a group parameters with the given element handle.
	 * The application does not normally create objects of this class directly.
	 * Instead, it uses the <code>iterator</code> method of an element handle to
	 * create the iterator.
	 * 
	 * @param handle a handle to an element
	 */

	public PropertyIterator(DesignElementHandle handle) {
		this.elementHandle = handle;

		List propDefns = elementHandle.getElement().getPropertyDefns();
		iter = propDefns.iterator();
	}

	// Implementation of an interface method.

	public boolean hasNext() {
		return iter.hasNext();
	}

	/**
	 * Gets the next property as a property handle. Implementation of iterator.next(
	 * )
	 * 
	 * @return the next property as a property handle.
	 * @see PropertyHandle
	 * @see UserPropertyDefnHandle
	 */

	public Object next() {
		if (!iter.hasNext())
			return null;
		ElementPropertyDefn propDefn = (ElementPropertyDefn) iter.next();
		return new PropertyHandle(elementHandle, propDefn);
	}

	/**
	 * Not supported. The application cannot remove properties.
	 */

	public void remove() {
		// Not supported.
	}

}
