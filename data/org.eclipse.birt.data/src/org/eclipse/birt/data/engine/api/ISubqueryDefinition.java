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
package org.eclipse.birt.data.engine.api;

/**
 * Provides definition of subquery: a supplemental use of rows returned by a
 * data set or a group. A subquery does not have its own data set, but rather it
 * provides an alternate view of data of an existing group or query by applying
 * additional transforms on top of such data.
 */
public interface ISubqueryDefinition extends IBaseQueryDefinition {
	/**
	 * Gets the name of the subquery. Each Subquery must have a name that uniquely
	 * identifies it within the main query that contains it.
	 *
	 * @return Name of the subquery
	 */
	@Override
	String getName();

	/**
	 * Subquery can apply to the group in which the sub query is added, or to the
	 * each row of current query definition. If it is the previous case, all rows of
	 * current group will be the data source of sub query, but in latter case, only
	 * the current row of parent query will be the data source. A note is the false
	 * value will be valid when it is added into the query definition, and it will
	 * have no any effect if it is on group.
	 *
	 * @return true, sub query is applied on group, false, applied on current row of
	 *         parent query
	 */
	boolean applyOnGroup();

}
