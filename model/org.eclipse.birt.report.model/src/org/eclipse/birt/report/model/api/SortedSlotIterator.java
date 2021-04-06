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
import java.util.List;

/**
 * An sorted iterator over the elements in a slot. Each call to
 * <code>getNext( )</code> returns a handle of type {@link DesignElementHandle}.
 * The elements in the list are sorted on the display name of the element.
 * 
 */

public class SortedSlotIterator implements Iterator {

	/**
	 * Handle to the slot over which to iterate.
	 */

	protected final SlotHandle slotHandle;

	/**
	 * Internal list for sorted.
	 */

	protected List list;

	/**
	 * Current iteration position.
	 */

	protected int posn;

	/**
	 * Constructs a sorted slot iterator with the given slot handle.
	 * 
	 * @param handle handle to the slot over which to iterate
	 */

	public SortedSlotIterator(SlotHandle handle) {
		slotHandle = handle;
		posn = 0;

		sort();
	}

	/**
	 * Sorts the element in this slot according to the display label of each
	 * element.
	 * 
	 */

	private void sort() {
		list = new ArrayList();
		Iterator it = slotHandle.iterator();
		while (it.hasNext()) {
			list.add(it.next());
		}

		DesignElementHandle.doSort(list);

	}

	/**
	 * Removes the element at the current iterator position.
	 */
	// Implementation of iterator.remove( )

	public void remove() {
		// Not support
	}

	// Implementation of iterator.hasNext( )

	public boolean hasNext() {
		return posn < slotHandle.getCount();
	}

	/**
	 * Returns a handle to the next content element. The handle is one of the
	 * various element classes derived from <code>DesignElementHandle</code>.
	 * 
	 * @return a handle to the next content element.
	 */

	// Implementation of iterator.next( )

	public Object next() {
		return list.get(posn++);
	}

}
