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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * An iterator over the children of an element. A child is an element that
 * extends another specified element. Each call to <code>getNext( )</code>
 * returns a handle of type {@link DesignElementHandle}.
 * <p>
 * This iterator returns only direct descendents, but not indirect descendents
 * (indirect descendents are elements derived from elements that derive from
 * this element.)
 */

class DerivedElementIterator implements Iterator {

	/**
	 * The cached iterator.
	 */

	protected Iterator iter;

	/**
	 * The module.
	 */

	protected Module module;

	/**
	 * Constructs a iterator with the given design and the design element handle.
	 * 
	 * @param module        module
	 * @param elementHandle handle to the element over which to iterate its derived
	 *                      elements
	 */

	public DerivedElementIterator(Module module, DesignElementHandle elementHandle) {
		assert module != null;
		assert elementHandle != null;

		this.module = module;

		iter = elementHandle.getElement().getDerived().iterator();
	}

	/**
	 * Inherited method that is disabled in this iterator; the caller cannot remove
	 * descendents using this class.
	 * 
	 * @see java.util.Iterator#remove()
	 */

	public void remove() {
		// This iterator can not be used to remove anything.

		throw new IllegalOperationException();
	}

	/**
	 * Returns true if there is another descendent to retrieve.
	 * 
	 * @return true if there is another descendent to retrieve, false otherwise
	 * @see java.util.Iterator#hasNext()
	 */

	public boolean hasNext() {
		return iter.hasNext();
	}

	/**
	 * Returns a handle of a derived element.
	 * 
	 * @return the handle of a derived element
	 * 
	 * @see java.util.Iterator#next()
	 * @see DesignElementHandle
	 */

	public Object next() {
		DesignElement derived = (DesignElement) iter.next();

		return derived.getHandle(module);
	}

}
