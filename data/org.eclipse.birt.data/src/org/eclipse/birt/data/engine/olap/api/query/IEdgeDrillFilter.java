/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.api.query;

import java.util.Collection;
import java.util.List;

import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;

/**
 * This is the the interface to define the edge drilling down/up operation.
 */

public interface IEdgeDrillFilter extends INamedObject {
	/**
	 * The hierarchy on which to apply this drill filter
	 *
	 * @param hierarchy
	 */
	void setTargetHierarchy(IHierarchyDefinition hierarchy);

	/**
	 * Return the hierarchy on which to apply this drill filter
	 *
	 */
	IHierarchyDefinition getTargetHierarchy();

	/**
	 * The level of the Hierarchy to drill up or down to on the branch that contains
	 * drill member
	 *
	 * @return
	 */
	void setTargetLevelName(String targetLevelName);

	/**
	 * The level of the Hierarchy to drill up or down to on the branch that contains
	 * drill member
	 *
	 * @return
	 */
	String getTargetLevelName();

	/**
	 * Add filter
	 *
	 * @param sort
	 */
	void addLevelFilter(IFilterDefinition filter);

	/**
	 * Get filter definition
	 *
	 * @return
	 */
	List<IFilterDefinition> getLevelFilter();

	/**
	 * Add sort definition for target level.
	 *
	 * @param sort
	 *
	 */
	void addLevelSort(ISortDefinition sort);

	/**
	 * Return sorts definition for the target level.
	 *
	 * @return
	 */
	List<ISortDefinition> getLevelSort();

	/**
	 * The collection represents the value of edge
	 *
	 * @param level
	 * @param value
	 */
	void setTuple(Collection<Object[]> tuple);

	/**
	 * Get the value associate with certain level.
	 *
	 * @param level
	 * @return
	 */
	Collection<Object[]> getTuple();
}
