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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;

/**
 * Represents a slot that can contain one element, such as the content slot of
 * the section element.
 * 
 */

public class SingleElementSlot extends ContainerSlot {

	private DesignElement content = null;

	/**
	 * Makes a clone of this single element slot. The cloned slot contains the copy
	 * of the content which was stored in the original slot. The relationship
	 * between content and container is not kept.
	 * <p>
	 * If the content-container relationship needs to be kept, call
	 * {@link ContainerSlot#copy(DesignElement, int)}.
	 * 
	 * @return Object the cloned slot.
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		SingleElementSlot slot = (SingleElementSlot) super.clone();
		if (content != null) {
			slot.content = (DesignElement) content.doClone(policy);
		}

		return slot;
	}

	/**
	 * Gets the position of the element stored in this slot.
	 * 
	 * @param element design element
	 * 
	 * @return 0 returned if the element is null or is the current content of this
	 *         slot, otherwise -1 returned.
	 */

	public int findPosn(DesignElement element) {
		if (element != null && element == content)
			return 0;
		return -1;
	}

	/**
	 * Inserts an element into the slot by the given position <code>posn</code>. The
	 * caller must have validated that the element should not be null and this slot
	 * is not full.
	 * 
	 * @param element design element
	 * @param posn    integer number.
	 */

	public void insert(DesignElement element, int posn) {
		assert content == null;
		assert element != null;
		assert posn == 0;
		content = element;
	}

	/**
	 * Removes the element from this slot.
	 * 
	 * @param element the design element to remove
	 * 
	 */

	public void remove(DesignElement element) {
		assert content != null && content == element;
		content = null;
	}

	/**
	 * Removes an element at the given position.
	 * 
	 * @param posn position of the element that is to be removed.
	 * @return the element that was removed from the list.
	 */

	public Object remove(int posn) {
		assert posn == 0;
		return content = null;
	}

	/**
	 * Checks whether the element can be dropped from this slot.
	 * 
	 * @param element design element
	 * 
	 * @return true is the element is the content in this slot, otherwise return
	 *         false.
	 */

	public boolean canDrop(DesignElement element) {
		assert content != null && content == element;
		return true;
	}

	/**
	 * Gets the content list in this slot. The return list will just hold one
	 * element if the slot is not empty.
	 * 
	 * @return the content list.
	 * 
	 */

	public List<DesignElement> getContents() {
		ArrayList<DesignElement> list = new ArrayList<DesignElement>();
		if (content != null)
			list.add(content);
		return list;
	}

	/**
	 * Gets the number of contents in this slot.
	 * 
	 * @return 1 if the slot is not empty, otherwise 0 returned.
	 * 
	 */

	public int getCount() {
		return content == null ? 0 : 1;
	}

	/**
	 * Can't move content within a single-item slot.
	 * 
	 * @param from the old position
	 * @param to   the new position
	 */

	public void moveContent(int from, int to) {
		// Can't move a single-item slot.

		assert false;
	}

	/**
	 * Checks whether the element is the content of this slot.
	 * 
	 * @param element the content element
	 * @return true is the element is the content of this slot, otherwise return
	 *         false.
	 */

	public boolean contains(DesignElement element) {
		return element != null && content == element;
	}

	/**
	 * Gets the content if this slot.
	 * 
	 * @param pos the position of the content in container element
	 * @return a design element returned if the slot is not empty.
	 */

	public DesignElement getContent(int pos) {
		assert pos == 0 && content != null;
		return content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ContainerSlot#clear()
	 */

	public void clear() {
		this.content = null;
	}

}