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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Iterates over the items within a property or member defined as a list of
 * simple value. Each object returned by <code>getNext( )</code> is of type
 * <code>Object</code>.
 *
 */

class SimpleIterator implements Iterator {

	/**
	 * Handle to the property or member that contains the list.
	 */

	protected final SimpleValueHandle valueHandle;

	/**
	 * Cached copy of the property list.
	 */

	protected final ArrayList list;

	/**
	 * The count over the list positions.
	 */

	protected int index;

	/**
	 * Constructs a simple iterator for the property or member that has the list of
	 * items over which to iterate.
	 *
	 * @param handle handle to the property or member that has the list of items
	 *               over which to iterate
	 */

	public SimpleIterator(SimpleValueHandle handle) {
		valueHandle = handle;
		list = valueHandle.getListValue();
		index = 0;
	}

	/**
	 * Removes the item at the current position.
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Iterator#remove()
	 */

	@Override
	public void remove() {
		if (!hasNext()) {
			return;
		}
		try {
			valueHandle.removeItem(index);
		} catch (PropertyValueException e) {
			// Ignore any errors.
		}
	}

	// Implementation of iterator.hasNext( )

	@Override
	public boolean hasNext() {
		return list != null && index < list.size();
	}

	/**
	 * Returns a handle to the next item in the list. The handle is of type
	 * <code>Object</code>
	 *
	 * @return a handle to the next item in the list
	 */
	// Implementation of iterator.next( )
	@Override
	public Object next() {
		if (!hasNext()) {
			return null;
		}

		Object value = list.get(index++);
		if (value instanceof ElementRefValue) {
			ElementRefValue elementRef = (ElementRefValue) value;
			if (elementRef.isResolved()) {
				return elementRef.getElement().getHandle(elementRef.getElement().getRoot());
			}
			return elementRef.getQualifiedReference();
		}
		return value;
	}

}
