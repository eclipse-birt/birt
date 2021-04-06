/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public void setTargetHierarchy(IHierarchyDefinition hierarchy);

	/**
	 * Return the hierarchy on which to apply this drill filter
	 * 
	 */
	public IHierarchyDefinition getTargetHierarchy();

	/**
	 * The level of the Hierarchy to drill up or down to on the branch that contains
	 * drill member
	 * 
	 * @return
	 */
	public void setTargetLevelName(String targetLevelName);

	/**
	 * The level of the Hierarchy to drill up or down to on the branch that contains
	 * drill member
	 * 
	 * @return
	 */
	public String getTargetLevelName();

	/**
	 * Add filter
	 * 
	 * @param sort
	 */
	public void addLevelFilter(IFilterDefinition filter);

	/**
	 * Get filter definition
	 * 
	 * @return
	 */
	public List<IFilterDefinition> getLevelFilter();

	/**
	 * Add sort definition for target level.
	 * 
	 * @param sort
	 * 
	 */
	public void addLevelSort(ISortDefinition sort);

	/**
	 * Return sorts definition for the target level.
	 * 
	 * @return
	 */
	public List<ISortDefinition> getLevelSort();

	/**
	 * The collection represents the value of edge
	 * 
	 * @param level
	 * @param value
	 */
	public void setTuple(Collection<Object[]> tuple);

	/**
	 * Get the value associate with certain level.
	 * 
	 * @param level
	 * @return
	 */
	public Collection<Object[]> getTuple();
}
