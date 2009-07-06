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

public interface IEdgeDrillFilter
{
	public static final int DRILL_TO_PARENT = 0;

	public static final int DRILL_TO_ANCESTORS = 1;

	public static final int DRILL_TO_DESCENDANTS = 2;

	public static final int DRILL_TO_CHILDREN = 3;

	public static final int DRILL_TO_ROOTS = 4;

	public static final int DRILL_TO_LEAVES = 5;


	/**
	 * Get Drill operation for this drill filter, it include Children,
	 * Descendants, Parent, and Ancestors, Root and Leaves.
	 */
	public int getDrillOperation( );

	/**
	 * The hierarchy on which to apply this drill filter
	 * 
	 * @param hierarchy
	 */
	public void setHierarchy( IHierarchyDefinition hierarchy );
	
	/**
	 * Return the hierarchy on which to apply this drill filter
	 * 
	 */
	public IHierarchyDefinition getHierarchy( );

	/**
	 * The level of the Hierarchy to drill up or down to on the branch that
	 * contains drill member
	 * 
	 * @return
	 */
	public void setTargetLevelName( String targetLevelName );
	
	
	/**
	 * The level of the Hierarchy to drill up or down to on the branch that
	 * contains drill member
	 * 
	 * @return
	 */
	public String getTargetLevelName( );
	
	/**
	 * Add a filter to target level. If no filter is added then by default all
	 * the values of that level will be populated.
	 * 
	 * @param filter
	 */
	public void addTargetLevelFilter( IFilterDefinition filter );

	/**
	 * Return all filters defined for target level.
	 * 
	 * @return
	 */
	public List<IFilterDefinition> getTargetLevelFilter( );

	/**
	 * Add sort definition for target level.
	 * 
	 * @param sort
	 */
	public void addTargetLevelSort( ISortDefinition sort );

	/**
	 * Return sorts definition for the target level.
	 * 
	 * @return
	 */
	public List<ISortDefinition> getTargetLevelSort( );

	/**
	 * The collection represents the value of edge
	 * 
	 * @param level
	 * @param value
	 */
	public void setTuple( Collection<Object[]> tuple );

	/**
	 * Get the value associate with certain level.
	 * 
	 * @param level
	 * @return
	 */
	public Collection<Object[]> getTuple( );
}
