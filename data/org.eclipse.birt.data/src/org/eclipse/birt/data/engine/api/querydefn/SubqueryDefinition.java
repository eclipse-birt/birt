/*
 *************************************************************************
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;

/**
 * Default implementation of the
 * {@link org.eclipse.birt.data.engine.api.ISubqueryDefinition} interface.
 */
public class SubqueryDefinition extends BaseQueryDefinition implements ISubqueryDefinition {
	private String name;
	private boolean onGroup;

	/**
	 * Constructs a SubqueryDefn. A name must be provided that uniquely identifies
	 * the subquery within the report query that contains it.
	 * 
	 * This constructor is deprecated for all the sub query definition should be
	 * assigned a parent query.
	 * 
	 * @deprecated
	 * @param name
	 */
	public SubqueryDefinition(String name) {
		super(null);
		this.name = name;
		this.onGroup = true;
	}

	/**
	 * Constructs a SubqueryDefn. A name must be provided that uniquely identifies
	 * the subquery within the report query that contains it. The outer query
	 * (parent) can be another query, or a sub query.
	 * 
	 * @param name
	 */
	public SubqueryDefinition(String name, IBaseQueryDefinition parent) {
		super(parent);
		this.name = name;
		this.onGroup = true;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.ISubqueryDefinition#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.ISubqueryDefinition#onGroup()
	 */
	public boolean applyOnGroup() {
		return this.onGroup;
	}

	/**
	 * Set the flag of whether this subquery is applied to the group or only one row
	 * of parent query.
	 * 
	 * @param onGroup
	 */
	public void setApplyOnGroupFlag(boolean onGroup) {
		this.onGroup = onGroup;
	}

	public boolean cacheQueryResults() {
		if (parentQuery != null)
			return parentQuery.cacheQueryResults();
		return super.cacheQueryResults();
	}
}
