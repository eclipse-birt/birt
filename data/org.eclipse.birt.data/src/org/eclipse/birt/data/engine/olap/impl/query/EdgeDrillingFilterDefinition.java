/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.impl.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;

/**
 * 
 *
 */
public class EdgeDrillingFilterDefinition implements IEdgeDrillFilter {
	private Collection<Object[]> tuple;
	private List<IFilterDefinition> filterList;
	private List<ISortDefinition> sortList;
	private String name;
	private IHierarchyDefinition targetHierarchyDefinition;
	private String targetLevel;

	public EdgeDrillingFilterDefinition(String name) {
		this.name = name;
		this.filterList = new ArrayList<IFilterDefinition>();
		this.sortList = new ArrayList<ISortDefinition>();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillingDownDefinition#
	 * addTargetLevelSort(org.eclipse.birt.data.engine.api.ISortDefinition)
	 */
	public void addLevelSort(ISortDefinition sort) {
		sortList.add(sort);
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillingDownDefinition#
	 * getTargetLevelSort()
	 */
	public List<ISortDefinition> getLevelSort() {
		return this.sortList;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillingDownDefinition#
	 * addTargetLevelSort(org.eclipse.birt.data.engine.api.ISortDefinition)
	 */
	public void addLevelFilter(IFilterDefinition filter) {
		filterList.add(filter);
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillingDownDefinition#
	 * getTargetLevelSort()
	 */
	public List<IFilterDefinition> getLevelFilter() {
		return this.filterList;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.INamedObject#getName()
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.query.INamedObject#setName(java.lang.
	 * String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter#setHierarchy(org
	 * .eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition)
	 */
	public void setTargetHierarchy(IHierarchyDefinition hierarchy) {
		this.targetHierarchyDefinition = hierarchy;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter#getHierarchy()
	 */
	public IHierarchyDefinition getTargetHierarchy() {
		return this.targetHierarchyDefinition;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter#
	 * getTargetLevelName()
	 */
	public String getTargetLevelName() {
		return this.targetLevel;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter#getTuple()
	 */
	public Collection<Object[]> getTuple() {
		return this.tuple;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter#setTuple(java.
	 * util.Collection)
	 */
	public void setTuple(Collection<Object[]> tuple) {
		this.tuple = tuple;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter#
	 * setTargetLevelName(java.lang.String)
	 */
	public void setTargetLevelName(String levelName) {
		this.targetLevel = levelName;
	}
}
