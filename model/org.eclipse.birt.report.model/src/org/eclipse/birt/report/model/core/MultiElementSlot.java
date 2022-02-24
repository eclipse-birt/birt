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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;

/**
 * A slot that contains an ordered list of elements.
 * 
 */

public class MultiElementSlot extends ContainerSlot {

	/**
	 * The ordered list of contents.
	 */

	public ArrayList<DesignElement> contents = new ArrayList<DesignElement>();

	/**
	 * Makes a clone for this slot. The cloned slot contains all of the cloned
	 * contents in the original slot. The relationship between content and container
	 * is not kept.
	 * <p>
	 * If the content-container relationship needs to be kept, call
	 * {@link ContainerSlot#copy(DesignElement, int)}.
	 * 
	 * @return Object the cloned slot.
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		MultiElementSlot slot = (MultiElementSlot) super.clone();
		slot.contents = new ArrayList<DesignElement>();
		for (int i = 0; i < contents.size(); i++) {
			DesignElement e = contents.get(i);
			slot.contents.add((DesignElement) e.doClone(policy));
		}
		return slot;
	}

	/**
	 * Finds the position of the given design element in the slot.
	 * 
	 * @param content the design element whose position needs to be returned.
	 * @return the position of the given design element in the slot
	 */

	public int findPosn(DesignElement content) {
		return contents.indexOf(content);
	}

	/**
	 * Inserts a design element into this slot with a given position number. The
	 * caller must have validated that the element has not existed in this slot.
	 * 
	 * @param element design element which need to be inserted into slot.
	 * @param posn    the zero-based integer number defines the inserted position in
	 *                the slot.
	 */

	public void insert(DesignElement element, int posn) {
		assert !contents.contains(element);
		assert posn >= 0 && posn <= contents.size();
		contents.add(posn, element);
	}

	/**
	 * Removes the design element in this slot. The removed element must existed in
	 * this slot.
	 * 
	 * @param element design element to be removed.
	 * 
	 */

	public void remove(DesignElement element) {
		assert contents.contains(element);
		contents.remove(element);

		// Flushing the containment stack is not required on remove for
		// two reasons. First, the elements are no longer accessible and
		// their properties will no longer be accessed. Second, if we
		// do add the element back into the containment hierarchy, we'll
		// flush the cache then.
	}

	/**
	 * Removes an element at the given position.
	 * 
	 * @param posn position of the element that is to be removed.
	 * @return the element that was removed from the list.
	 */

	public Object remove(int posn) {
		assert posn >= 0 && posn < getCount();
		return contents.remove(posn);
	}

	/**
	 * Determines if the design element can be removed.
	 * 
	 * @param element design element
	 * 
	 * @return true if the element existed in this slot.
	 * 
	 */

	public boolean canDrop(DesignElement element) {
		return contents.contains(element);
	}

	/**
	 * Returns the contents list which were stored in the slot.
	 * 
	 * @return the contents list which were stored in the slot.
	 */

	public List<DesignElement> getContents() {
		return contents;
	}

	/**
	 * Returns the current size of the slot.
	 * 
	 * @return current size of the slot.
	 */

	public int getCount() {
		return contents.size();
	}

	/**
	 * 
	 * Moves the design element from position <code>from</code> to <code>to</code>
	 * in this slot.
	 * 
	 * @param from the old position of the design element
	 * @param to   the new position of the design element
	 */

	public void moveContent(int from, int to) {
		assert from >= 0 && from < contents.size();
		assert to >= 0 && to < contents.size();

		if (from == to)
			return;

		DesignElement obj = contents.remove(from);
		contents.add(to, obj);
	}

	/**
	 * Returns true if the design element existed in the slot.
	 * 
	 * @param element design element
	 * @return true if the design element exists in this slot.
	 */

	public boolean contains(DesignElement element) {
		return contents.contains(element);
	}

	/**
	 * Returns the design element stored in this slot at the given position
	 * <code>posn</code>.
	 * 
	 * @param posn the integer number which defines the position in slot.
	 * @return the design element stored in this slot at the given position number.
	 */

	public DesignElement getContent(int posn) {
		assert posn >= 0 && posn < contents.size();
		return contents.get(posn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ContainerSlot#clear()
	 */

	public void clear() {
		this.contents.clear();

	}

}
