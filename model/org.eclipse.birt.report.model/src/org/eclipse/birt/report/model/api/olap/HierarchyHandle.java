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

package org.eclipse.birt.report.model.api.olap;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;

/**
 * Represents a Hierarchy.
 *
 * @see org.eclipse.birt.report.model.elements.olap.Hierarchy
 */

public abstract class HierarchyHandle extends ReportElementHandle implements IHierarchyModel {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public HierarchyHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Gets the count of the level elements within this hierarchy.
	 *
	 * @return count of the level elements if set, otherwise 0
	 */
	public int getLevelCount() {
		return getPropertyHandle(LEVELS_PROP).getContentCount();
	}

	/**
	 * Gets the level handle by the name within this hierarchy.
	 *
	 * @param levelName name of the level to find
	 * @return the level within this hierarchy if found, otherwise null
	 */

	public LevelHandle getLevel(String levelName) {
		if (levelName == null) {
			return null;
		}

		LevelHandle found = null;
		List levels = getListProperty(LEVELS_PROP);
		for (int i = 0; i < levels.size(); i++) {
			LevelHandle tmpLevel = (LevelHandle) levels.get(i);
			if (levelName.equals(tmpLevel.getName())) {
				found = tmpLevel;
				break;
			}
		}

		return found;

	}

	/**
	 * Gets the level handle at the specified position within this hierarchy.
	 *
	 * @param index 0-based integer
	 * @return the level handle at the given index, <code>null</code> if index is
	 *         out of range
	 */
	public LevelHandle getLevel(int index) {
		return (LevelHandle) getPropertyHandle(LEVELS_PROP).getContent(index);
	}

	/**
	 * Returns an iterator for the filter list defined on this hierarchy. Each
	 * object returned is of type <code>StructureHandle</code>.
	 *
	 * @return the iterator for <code>FilterCond</code> structure list defined on
	 *         this hierarchy.
	 */

	public Iterator filtersIterator() {
		PropertyHandle propHandle = getPropertyHandle(FILTER_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Returns an iterator for the access controls. Each object returned is of type
	 * <code>AccessControlHandle</code>.
	 *
	 * @return the iterator for user accesses defined on this cube.
	 */

	public Iterator accessControlsIterator() {
		return Collections.emptyList().iterator();
	}
}
