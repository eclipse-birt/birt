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

import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * ICubeQueryDefinition is the entry point of a cube query. It defines the
 * edges, bindings, etc. for the cube query. 
 */

public interface ICubeQueryDefinition extends IBaseCubeQueryDefinition
{

	// The row edge type
	public static final int ROW_EDGE = 1;

	// The column edge type
	public static final int COLUMN_EDGE = 2;

	//The page edge type
	public static final int PAGE_EDGE = 3;
	
	/**
	 * When this value is not null, the data set name will not be used, since it
	 * indicates query is running on the data of report document or local caching of QueryResults.
	 * 
	 * @return associated queryResultID in query on report document/local caching
	 */
	public String getQueryResultsID( );
	
	/**
	 * Set the query results id to cube query.
	 * @param id
	 */
	public void setQueryResultsID( String id );
	
	/**
	 * Indicates if the query need access the fact table. 
	 * @return
	 */
	public boolean needAccessFactTable( );
	
	/**
	 * Indicates if the query need access the fact table. 
	 * @param accessFactTable
	 */
	public void setNeedAccessFactTable( boolean needAccessFactTable );
	
	/**
	 * Indicates if the query need cache the QueryResults . The query result
	 * can be reload form the cache if the cache is used.
	 * 
	 * @return true if cache is needed.
	 */
	public boolean cacheQueryResults( );
	
	/**
	 * Indicates if the query need cache the QueryResults.
	 * 
	 * @return true if cache is needed.
	 */
	public void setCacheQueryResults( boolean b );
	
	/**
	 * Create an edge of the cube. At initial stage we only support two types of
	 * edges: row edge and column edge. The edge created will automatically be
	 * linked to the cube.
	 * 
	 * @param type
	 * @return
	 */
	public IEdgeDefinition createEdge( int type );

	/**
	 * Create a measure which is used in the query. A measure is a specific
	 * dimension. The measure created by this method will be automatically
	 * linked to the cube.
	 * 
	 * @param measureName
	 * @return
	 */
	public IMeasureDefinition createMeasure( String measureName );

	/**
	 * Create a computed measure which is dynamically created during the population
	 * of CubeCursor.
	 * 
	 * @param measureName
	 * @param expr
	 * @return
	 * @throws DataException 
	 */
	public IComputedMeasureDefinition createComputedMeasure( String measureName, int type,
			IBaseExpression expr ) throws DataException;
	
	/**
	 * Create a calculated measure which is dynamically created during the population
	 * of CubeCursor.
	 * 
	 * @param measureName
	 * @param expr
	 * @return
	 * @throws DataException 
	 */
	public IDerivedMeasureDefinition createDerivedMeasure( String measureName, int type,
			IBaseExpression expr ) throws DataException;
	
	/**
	 * Return the list of measures defined.
	 * 
	 * @return
	 */
	public List getMeasures( );

	/**
	 * Return the list of computed measure defined.
	 * @return
	 */
	public List getComputedMeasures( );
	
	/**
	 * Return the list of calculated measure defined.
	 * @return
	 */
	public List getDerivedMeasures( );
	
	/**
	 * Get the specific EdgeDefn, for each type of Edge there is only one Edge
	 * instance.
	 * 
	 * @param type
	 * @return
	 */
	public IEdgeDefinition getEdge( int type );

	/**
	 * Add bindings to the query definition.
	 * 
	 * @param binding
	 */
	public void addBinding( IBinding binding );

	/**
	 * Return the list of bindings defined in the cube query.
	 * 
	 * @return
	 */
	public List getBindings( );

	/**
	 * Add the sort. Currently we only support sorts which are based on one
	 * single level.
	 * 
	 * @param sort
	 */
	public void addSort( ISortDefinition sort );

	/**
	 * Add the filter. Currently we only support filters which are based on one
	 * single level. The multiple filters defined in cube will have an 锟紸ND锟�
	 * relationship.
	 * 
	 * @param filter
	 */
	public void addFilter( IFilterDefinition filter );

	/**
	 * Return the sorts defined
	 * 
	 * @return
	 */
	public List getSorts( );

	/**
	 * Return the filters defined
	 * 
	 * @return
	 */
	public List getFilters( );
	
	/**
	 * The filters are applied under breakHierarychy or non-breakHierarchy.
	 * 
	 * @return   zero indicate breakHierarchy
	 *           none zero indicate non-breakHierarchy.
	 */
	public int getFilterOption( );

	/**
	 * Set the filter option to indicate whether is breakHierarchy or
	 * non-breakHierarchy situation.
	 * 
	 * @param breakHierarchyOption
	 *           zero indicate breakHierarchy
	 *           none zero indicate non-breakHierarchy.
	 */
	public void setFilterOption( int breakHierarchyOption );
	
	
	
	/**
	 * add a cube operations
	 * After a query common execution, all added cube operations are executed one by one based on query execution result 
	 * @param cubeOperation, NullPointerException is thrown when cubeOperation is null
	 */
	public void addCubeOperation( ICubeOperation cubeOperation );
	
	/**
	 * @return all added cube operations, An empty array returned if no cube operation is added
	 */
	public ICubeOperation[] getCubeOperations( );
	
	/**
	 * @return the ID of the report Item
	 */
	public String getID( );
	
	/**
	 * @param the ID of the report Item
	 */
	public void setID(String ID);

    /**
     * Clone itself.
     */
    public ICubeQueryDefinition clone( );
}
