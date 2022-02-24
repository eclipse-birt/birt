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

import java.util.Collection;
import java.util.Set;

/**
 * Defines a data engine query: a set of data transforms that provides data for
 * a list-like element in the report. The data engine query encapsulates three
 * types of information:<br>
 * 
 * 1. A data set, including computed columns together with the parameter
 * bindings. <br>
 * 2. Data transforms that are defined on report items, i.e., sorting,
 * filtering, grouping, aggregation functions, and so on. <br>
 * 3. Subqueries that are contained in the current report query.<br>
 *
 */
public interface IQueryDefinition extends IBaseQueryDefinition {
	/**
	 * Gets the name of the data set used by this query
	 */
	public String getDataSetName();

	/**
	 * When this value is not null, the data set name will not be used, since it
	 * indicates query is running on the data of report document or local caching of
	 * QueryResults.
	 * 
	 * @return associated queryResultID in query on report document/local caching
	 */
	public String getQueryResultsID();

	/**
	 * When user knows which columns are in data set and user likes to get the
	 * column value without explicitly binding a name to a data set row expression,
	 * this flag can be set as true to indicate it.
	 * 
	 * For example, there is one column, COUNTRY. When this flag is false, if user
	 * wants to get the value of COUNTRY, user first needs to add a binding like
	 * <COUNTRY, dataSetRow.COUNTRY>, and then user can get the value by the name of
	 * COUNTRY. But if this flag is set, user does not need to add the binding, and
	 * then user can get the value directly.
	 * 
	 * Currently only when there is data set defined, this flag will have effect.
	 * 
	 * @return true, auto binding needs to be supported. false, auto binding is not
	 *         supported, this is default behavior.
	 */
	public boolean needAutoBinding();

	/**
	 * Returns the set of input parameter bindings as an unordered collection of
	 * {@link org.eclipse.birt.data.engine.api.IInputParameterBinding} objects.
	 * 
	 * @return the input parameter bindings. If no binding is defined, null is
	 *         returned.
	 */
	public Collection getInputParamBindings();

	/**
	 * Provides a column projection hint to the data engine. The caller informs the
	 * data engine that only a selected list of columns defined by the data set are
	 * used by this report query. The names of those columns (the "projected
	 * columns") are passed in as an array of string. <br>
	 * If a column projection is set, runtime error may occur if the report query
	 * uses columns that are not defined in the projected column list.
	 */
	public String[] getColumnProjection();

	/**
	 * Return the source query of current query. If source query is provided, the
	 * execution result of it will be treated as the "data source" of current query
	 * definition. That is, the current query will be executed against the query
	 * results, rather then "data source/data set" settings.
	 *
	 * @return
	 */

	public IBaseQueryDefinition getSourceQuery();

	/**
	 * Return whether this query definition is a summary query definition. A summary
	 * query definition contains only one row each group, and only allow
	 * aggregations in inner most group.
	 * 
	 * @return
	 */
	public boolean isSummaryQuery();

	/**
	 * Get all links between datasets.
	 * 
	 * @return
	 */
	public Set<IBaseLinkDefinition> getLinks();

	/**
	 * The links open an interface for calculating joins between datasets. Currently
	 * not used.
	 * 
	 * @return
	 */
	public void addLink(IBaseLinkDefinition link);

	/**
	 * Clone itself.
	 * 
	 * @return
	 */
	public IQueryDefinition clone();
}
