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

import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.IReferencableElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Iterates over the clients of an element. A client is an element that
 * references another specified element. For example, if element B extends
 * element A, then element B is a client of element A. Each call to
 * <code>getNext( )</code> returns a handle of type {@link DesignElementHandle}.
 * 
 * @see org.eclipse.birt.report.model.core.ReferenceableElement
 */

class ClientIterator implements Iterator {

	/**
	 * The cached iterator.
	 */

	protected Iterator iter;

	/**
	 * Module.
	 */

	protected Module module;

	/**
	 * Constructs a iterator to return the clients of the given element.
	 * 
	 * @param elementHandle handle to the element for which clients are wanted. Must
	 *                      not be <code>null</code>.
	 */

	public ClientIterator(DesignElementHandle elementHandle) {
		assert elementHandle != null;

		this.module = elementHandle.getModule();
		assert module != null;

		if (elementHandle.getElement() instanceof IReferencableElement) {
			IReferencableElement element = (IReferencableElement) elementHandle.getElement();
			iter = element.getClientList().iterator();
		} else {
			iter = null;
		}
	}

	/**
	 * Inherited method that is disabled in this iterator; the caller cannot remove
	 * clients using this class.
	 * 
	 * @see java.util.Iterator#remove()
	 */

	public void remove() {
		// This iterator can not be used to remove anything.

		throw new IllegalOperationException();
	}

	/**
	 * Returns true if there is another client to retrieve.
	 * 
	 * @return true if there is another client to retrieve, false otherwise
	 * @see java.util.Iterator#hasNext()
	 */

	public boolean hasNext() {
		if (iter != null) {
			return iter.hasNext();
		}
		return false;
	}

	/**
	 * Returns a handle of the client element.
	 * 
	 * @return the handle of the client element
	 * 
	 * @see java.util.Iterator#next()
	 * @see DesignElementHandle
	 */

	public Object next() {
		if (iter != null) {
			BackRef client = (BackRef) iter.next();
			return client.getElement().getHandle(module);
		}
		return null;
	}
}
