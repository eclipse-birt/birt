/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api;

import java.util.List;
import java.util.Map;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * /** Represents attributes common to a data engine query and a subquery.
 *
 */
public interface IBaseQueryDefinition extends IBaseTransform, IDataQueryDefinition {
	/**
	 * Returns the group definitions as an ordered collection of
	 * {@link org.eclipse.birt.data.engine.api.IGroupDefinition} objects. Groups are
	 * organizations within the data that support aggregation, filtering and
	 * sorting. Reports use groups to trigger level breaks.
	 * 
	 * @return the list of groups. If no group is defined, null is returned.
	 */

	public List getGroups();

	/**
	 * Indicates if the report will use the detail rows. Allows the data engine to
	 * optimize the query if the details are not used.
	 * 
	 * @return true if the detail rows are used, false if not used
	 */
	public boolean usesDetails();

	/**
	 * Indicates if the query need cache the result rows . The query result can be
	 * reload form the cache if the cache is used.
	 * 
	 * @return true if cache is needed.
	 */
	public boolean cacheQueryResults();

	/**
	 * Returns the parent query. The parent query is the outer query which encloses
	 * this query.
	 */
	public IBaseQueryDefinition getParentQuery();

	/**
	 * Gets the maximum number of detail rows that can be retrieved by this query
	 * 
	 * @return Maximum number of rows. If 0, there is no limit on how many rows this
	 *         query can retrieve.
	 */
	public int getMaxRows();

	/**
	 * set the maximum number of detail rows that can be retrieved by this query
	 */
	public void setMaxRows(int maxRows);

	/**
	 * Gets the expressions that needs to be available at the group/list, as an Map
	 * of bound colum name to
	 * {@link org.eclipse.birt.data.engine.api.IBaseExpression} objects.
	 * 
	 * @return
	 * @deprecated
	 */
	public Map getResultSetExpressions();

	/**
	 * Add a column binding instance to query definition.
	 * 
	 * @param name
	 * @param binding
	 * @throws DataException
	 * @throws DataException
	 */
	public void addBinding(IBinding binding) throws DataException;

	/**
	 * Get all column binding instance from query definition.
	 * 
	 * @return
	 * @throws DataException
	 */
	public Map getBindings();

	/**
	 * Return the Query Execution Hints information. The Query Execution Hints
	 * information defines hints info for Data engine to execution the query.
	 * 
	 * @return
	 */
	public IQueryExecutionHints getQueryExecutionHints();

	/**
	 * Gets the starting row that will be retrieved by this query
	 * 
	 * @return
	 */
	public int getStartingRow();

	/**
	 * If the flag is true this query will return the rows with distinct or unique
	 * column values. Currently this flag is valid only for the query which use
	 * another query as data source.
	 * 
	 * @return
	 */
	public boolean getDistinctValue();

}
