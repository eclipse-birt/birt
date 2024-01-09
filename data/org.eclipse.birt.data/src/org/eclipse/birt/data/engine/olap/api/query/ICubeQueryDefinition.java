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

import java.util.List;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseLinkDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * ICubeQueryDefinition is the entry point of a cube query. It defines the
 * edges, bindings, etc. for the cube query.
 */

public interface ICubeQueryDefinition extends IBaseCubeQueryDefinition {

	// The row edge type
	int ROW_EDGE = 1;

	// The column edge type
	int COLUMN_EDGE = 2;

	// The page edge type
	int PAGE_EDGE = 3;

	/**
	 * When this value is not null, the data set name will not be used, since it
	 * indicates query is running on the data of report document or local caching of
	 * QueryResults.
	 *
	 * @return associated queryResultID in query on report document/local caching
	 */
	String getQueryResultsID();

	/**
	 * Set the query results id to cube query.
	 *
	 * @param id
	 */
	void setQueryResultsID(String id);

	/**
	 * Indicates if the query need access the fact table.
	 *
	 * @return
	 */
	boolean needAccessFactTable();

	/**
	 * Indicates if the query need access the fact table.
	 *
	 * @param accessFactTable
	 */
	void setNeedAccessFactTable(boolean needAccessFactTable);

	/**
	 * Indicates if the query need cache the QueryResults . The query result can be
	 * reload form the cache if the cache is used.
	 *
	 * @return true if cache is needed.
	 */
	boolean cacheQueryResults();

	/**
	 * Indicates if the query need cache the QueryResults.
	 *
	 * @return true if cache is needed.
	 */
	void setCacheQueryResults(boolean b);

	/**
	 * Create an edge of the cube. At initial stage we only support two types of
	 * edges: row edge and column edge. The edge created will automatically be
	 * linked to the cube.
	 *
	 * @param type
	 * @return
	 */
	IEdgeDefinition createEdge(int type);

	/**
	 * Create a measure which is used in the query. A measure is a specific
	 * dimension. The measure created by this method will be automatically linked to
	 * the cube.
	 *
	 * @param measureName
	 * @return
	 */
	IMeasureDefinition createMeasure(String measureName);

	/**
	 * Create a computed measure which is dynamically created during the population
	 * of CubeCursor.
	 *
	 * @param measureName
	 * @param expr
	 * @return
	 * @throws DataException
	 */
	IComputedMeasureDefinition createComputedMeasure(String measureName, int type, IBaseExpression expr)
			throws DataException;

	/**
	 * Create a calculated measure which is dynamically created during the
	 * population of CubeCursor.
	 *
	 * @param measureName
	 * @param expr
	 * @return
	 * @throws DataException
	 */
	IDerivedMeasureDefinition createDerivedMeasure(String measureName, int type, IBaseExpression expr)
			throws DataException;

	/**
	 * Return the list of measures defined.
	 *
	 * @return
	 */
	List getMeasures();

	/**
	 * Return the list of computed measure defined.
	 *
	 * @return
	 */
	List getComputedMeasures();

	/**
	 * Return the list of calculated measure defined.
	 *
	 * @return
	 */
	List getDerivedMeasures();

	/**
	 * Get the specific EdgeDefn, for each type of Edge there is only one Edge
	 * instance.
	 *
	 * @param type
	 * @return
	 */
	IEdgeDefinition getEdge(int type);

	/**
	 * Add bindings to the query definition.
	 *
	 * @param binding
	 */
	void addBinding(IBinding binding);

	/**
	 * Return the list of bindings defined in the cube query.
	 *
	 * @return
	 */
	List<IBinding> getBindings();

	/**
	 * Add the sort. Currently we only support sorts which are based on one single
	 * level.
	 *
	 * @param sort
	 */
	void addSort(ISortDefinition sort);

	/**
	 * Add the filter. Currently we only support filters which are based on one
	 * single level. The multiple filters defined in cube will have an 'AND'
	 * relationship.
	 *
	 * @param filter
	 */
	void addFilter(IFilterDefinition filter);

	/**
	 * Return the sorts defined
	 *
	 * @return
	 */
	List getSorts();

	/**
	 * Return the filters defined
	 *
	 * @return
	 */
	List getFilters();

	/**
	 * The filters are applied under breakHierarychy or non-breakHierarchy.
	 *
	 * @return zero indicate breakHierarchy none zero indicate non-breakHierarchy.
	 */
	int getFilterOption();

	/**
	 * Set the filter option to indicate whether is breakHierarchy or
	 * non-breakHierarchy situation.
	 *
	 * @param breakHierarchyOption zero indicate breakHierarchy none zero indicate
	 *                             non-breakHierarchy.
	 */
	void setFilterOption(int breakHierarchyOption);

	/**
	 * add a cube operations After a query common execution, all added cube
	 * operations are executed one by one based on query execution result
	 *
	 * @param cubeOperation, NullPointerException is thrown when cubeOperation is
	 *                       null
	 */
	void addCubeOperation(ICubeOperation cubeOperation);

	/**
	 * @return all added cube operations, An empty array returned if no cube
	 *         operation is added
	 */
	ICubeOperation[] getCubeOperations();

	/**
	 * @return the ID of the report Item
	 */
	String getID();

	/**
	 * @param the ID of the report Item
	 */
	void setID(String ID);

	/**
	 * Clone itself.
	 */
	ICubeQueryDefinition clone();

	/**
	 * Get all links between datasets.
	 *
	 * @return
	 */
	Set<IBaseLinkDefinition> getLinks();

	/**
	 * The links open an interface for calculating joins between datasets. Currently
	 * not used.
	 *
	 * @return
	 */
	void addLink(IBaseLinkDefinition link);
}
