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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementDefn;

/**
 * Iterate content elements within the given level.
 */

public class LevelContentIterator implements Iterator<DesignElement> {

	/**
	 * The maximal level.
	 */

	protected static final int MAX_LEVEL = Integer.MAX_VALUE;

	/**
	 * List of content elements.
	 */

	List<DesignElement> elementContents = null;

	/**
	 * Current iteration position.
	 */

	protected int posn = 0;

	/**
	 * Constructs a iterator that will visit all the content element within the
	 * given <code>element</code>
	 * 
	 * @param module
	 * 
	 * @param element the element to visit.
	 * @param level   the depth of elements to iterate
	 */

	public LevelContentIterator(Module module, DesignElement element, int level) {
		assert element != null;

		elementContents = new ArrayList<DesignElement>();
		buildContentsList(module, element, level);
	}

	/**
	 * Constructs a iterator that will visit all the content element within the
	 * given slot id of the given <code>element</code>
	 * 
	 * @param module
	 * 
	 * @param containerInfor the container information to visit.
	 * @param level          the depth of elements to iterate.
	 */

	public LevelContentIterator(Module module, ContainerContext containerInfor, int level) {
		assert containerInfor != null;

		elementContents = new ArrayList<DesignElement>();

		buildContentsList(module, containerInfor, level);
	}

	/**
	 * Adds the content elements in the given container element into
	 * <code>elementContents</code>
	 * 
	 * @param element the next element to build.
	 */

	private void buildContentsList(Module module, DesignElement element, int level) {
		if (level < 0 || !element.isContainer())
			return;

		ElementDefn defn = (ElementDefn) element.getDefn();

		// slots
		Iterator<ISlotDefn> slots = defn.slotsIterator();
		while (slots.hasNext()) {
			ISlotDefn iSlotDefn = slots.next();

			buildContentsList(module, new ContainerContext(element, iSlotDefn.getSlotID()), level);
		}

		// build properties
		List<IElementPropertyDefn> properties = element.getContents();
		for (int i = 0; i < properties.size(); i++) {
			buildContentsList(module, new ContainerContext(element, properties.get(i).getName()), level);
		}
	}

	/**
	 * Adds the content elements of the given slot in the given container element
	 * into <code>elementContents</code>
	 * 
	 * @param element the next element to build.
	 * @param slotId  the slot id.
	 */

	private void buildContentsList(Module module, ContainerContext containerInfor, int level) {
		if (level <= 0)
			return;

		List<DesignElement> contents = containerInfor.getContents(module);

		for (Iterator<DesignElement> iter = contents.iterator(); iter.hasNext();) {
			DesignElement e = iter.next();
			elementContents.add(e);

			buildContentsList(module, e, level - 1);
		}
	}

	/**
	 * Not allowed.
	 */

	public void remove() {
		assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */

	public boolean hasNext() {
		return posn < elementContents.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */

	public DesignElement next() {
		return elementContents.get(posn++);
	}

}
