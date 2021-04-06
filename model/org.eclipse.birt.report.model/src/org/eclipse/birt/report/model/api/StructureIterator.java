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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.Structure;

/**
 * Iterates over the structures within a property or member defined as a list of
 * structures. Each object returned by <code>getNext( )</code> is of type
 * <code>StructureHandle</code>.
 * 
 */

class StructureIterator implements Iterator {

	/**
	 * Handle to the property or member that contains the list.
	 */

	protected final SimpleValueHandle valueHandle;

	/**
	 * Cached copy of the property list.
	 */

	protected final ArrayList<Structure> list;

	/**
	 * The count over the list positions.
	 */

	protected int index;

	/**
	 * Constructs an structure iterator for the property or member that has the list
	 * of structures over which to iterate.
	 * 
	 * @param handle handle to the property or member that has the list of
	 *               structures over which to iterate
	 */

	public StructureIterator(SimpleValueHandle handle) {
		valueHandle = handle;
		list = valueHandle.getListValue();
		index = -1;
	}

	/**
	 * Removes the structure at the current position.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */

	public void remove() {
		if (index < 0 || index >= list.size())
			return;
		try {
			valueHandle.removeItem(index);
			list.remove(index--);
		} catch (PropertyValueException e) {
			// Ignore any errors.
		}
	}

	// Implementation of iterator.hasNext( )

	public boolean hasNext() {
		return list != null && index + 1 < list.size();
	}

	/**
	 * Returns a handle to the next structure in the list. The handle is of type
	 * <code>StructureHandle</code>
	 * 
	 * @return a handle to the next structure in the list
	 * @see StructureHandle
	 */
	// Implementation of iterator.next( )
	public Object next() {
		if (!hasNext())
			return null;

		Structure struct = list.get(++index);
		return struct.getHandle(valueHandle, index);
	}

}
