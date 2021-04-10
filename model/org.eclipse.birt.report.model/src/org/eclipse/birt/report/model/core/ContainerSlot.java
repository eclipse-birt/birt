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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;

/**
 * Represents a slot within an element. A slot is the ability for one element to
 * contain other elements. Many elements have one slot, some have multiple
 * slots. The slot itself can contain either a single element (see the
 * {@link SingleElementSlot}class), or multiple elements (see the
 * {@link MultiElementSlot}class.) See the
 * {@link org.eclipse.birt.report.model.api.metadata.ISlotDefn}class for more
 * information about slots.
 * 
 */

public abstract class ContainerSlot implements Cloneable {

	/**
	 * Finds the position of an element in the slot.
	 * 
	 * @param content content element to find
	 * @return Tzero-based index of the element. Returns -1 if the element is not
	 *         found
	 */

	public abstract int findPosn(DesignElement content);

	/**
	 * Inserts an element into the slot at the given position.
	 * 
	 * @param content element to insert
	 * @param posn    insert position. 0 is the first element, n is the last
	 *                position, where n is the current content count
	 */

	public abstract void insert(DesignElement content, int posn);

	/**
	 * Removes an element from the slot.
	 * 
	 * @param content element to remove
	 */

	public abstract void remove(DesignElement content);

	/**
	 * Removes an element at the given position.
	 * 
	 * @param posn position of the element that is to be removed.
	 * @return the element that was removed from the list.
	 * @throws IndexOutOfBoundsException if index out of range <tt>(posn
	 * 		  &lt; 0 || posn &gt;= getCount())</tt>.
	 */

	public abstract Object remove(int posn);

	/**
	 * Checks if the element can be dropped. One case where this will return false
	 * is if the user asks to delete a BIRT-defined style. The element must be a
	 * content of the slot.
	 * 
	 * @param content element to check
	 * @return true if the element can be deleted, false if not
	 */

	public abstract boolean canDrop(DesignElement content);

	/**
	 * Returns the slot contents. Items are in order by position.
	 * 
	 * @return the slot contents
	 */

	public abstract List<DesignElement> getContents();

	/**
	 * Returns an iterator over the contents of the slot.
	 * 
	 * @return an iterator over the contents. The iterator returns objects of type
	 *         DesignElement.
	 */

	public Iterator<DesignElement> iterator() {
		return getContents().iterator();
	}

	/**
	 * Returns the number of elements in the slot.
	 * 
	 * @return the content count
	 */

	public abstract int getCount();

	/**
	 * Moves an element within the slot.
	 * 
	 * @param from current position
	 * @param to   new position. The new position is relative to the current list
	 *             contents.
	 */

	public abstract void moveContent(int from, int to);

	/**
	 * Determines if the slot contains a given element.
	 * 
	 * @param content the element to check
	 * @return true if the slot contains the element, false if not.
	 */

	public abstract boolean contains(DesignElement content);

	/**
	 * Returns the content at the given position.
	 * 
	 * @param posn content position.
	 * @return the element at the given position, or null if the position is outside
	 *         the range of valid positions.
	 */

	public abstract DesignElement getContent(int posn);

	/**
	 * Removes all the contents of this container slot.
	 * 
	 */

	public abstract void clear();

	/**
	 * Determines if the slot is empty.
	 * 
	 * @return true if the slot is empty, false if not empty
	 */

	public boolean isEmpty() {
		return getCount() == 0;
	}

	/**
	 * Adds a new element to the end of the list.
	 * 
	 * @param element element to add
	 */

	public void add(DesignElement element) {
		insert(element, getCount());
	}

	/**
	 * Returns the cloned slot with new container. The new container is what
	 * contains this cloned slot, so the container of all contents in the cloned
	 * slot is the new container.
	 * 
	 * @param newContainer the new container which contains the cloned slot.
	 * @param slotID       the slot id
	 * @param policy       the copy policy
	 * @return the clone slot.
	 * 
	 */

	public ContainerSlot copy(DesignElement newContainer, int slotID, CopyPolicy policy) {
		ContainerSlot newSlot = null;

		try {
			newSlot = (ContainerSlot) doClone(policy);
			for (int i = 0; i < newSlot.getCount(); i++)
				newSlot.getContent(i).setContainer(newContainer, slotID);
		} catch (CloneNotSupportedException e) {
			assert false;
		}

		return newSlot;
	}

	/**
	 * Returns the cloned slot according the copy policy. The container of the
	 * contents is not changed.
	 * 
	 * @param policy the copy policy
	 * @return the cloned slot, the container of the contents is not changed.
	 * @throws CloneNotSupportedException
	 */

	abstract protected Object doClone(CopyPolicy policy) throws CloneNotSupportedException;

}