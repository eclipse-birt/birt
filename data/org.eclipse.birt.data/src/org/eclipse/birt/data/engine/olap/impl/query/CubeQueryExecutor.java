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

package org.eclipse.birt.data.engine.olap.impl.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.data.api.IComputedMeasureHelper;
import org.eclipse.birt.data.engine.olap.util.ComputedMeasureHelper;
import org.eclipse.birt.data.engine.olap.util.filter.AggrMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.BaseDimensionFilterEvalHelper;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class CubeQueryExecutor
{

	private ICubeQueryDefinition defn;
	private Scriptable scope;
	private DataEngineSession session;
	private DataEngineContext context;
	private String queryResultsId;
	private IBaseQueryResults outResults;
	
	/**
	 * 
	 * @param outResults
	 * @param defn
	 * @param session
	 * @param scope
	 * @param context
	 */
	public CubeQueryExecutor( IBaseQueryResults outResults, ICubeQueryDefinition defn, DataEngineSession session, Scriptable scope,
			DataEngineContext context )
	{
		this.defn = defn;
		this.scope = scope;
		this.context = context;
		this.session = session;
		this.outResults = outResults;
	}
	
	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public List getDimensionFilterEvalHelpers( ) throws DataException
	{
		List filters = defn.getFilters( );
		List results = new ArrayList( );
		for ( int i = 0; i < filters.size( ); i++ )
		{
			IFilterDefinition filter = (IFilterDefinition) filters.get( i );
			if ( !isMeasureFilter( filter ) )
			{
				results.add( BaseDimensionFilterEvalHelper.createFilterHelper( this.outResults, this.scope,
						defn,
						filter,
						this.session.getEngineContext( ).getScriptContext( )) );
			}
		}
		return results;
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public List getMeasureFilterEvalHelpers() throws DataException
	{
		List filters = defn.getFilters( );
		List results = new ArrayList( );
		for ( int i = 0; i < filters.size( ); i++ )
		{
			IFilterDefinition filter = (IFilterDefinition) filters.get( i );
			if ( isMeasureFilter( filter ) )
			{
				AggrMeasureFilterEvalHelper filterHelper = new AggrMeasureFilterEvalHelper( this.outResults, scope, 
						defn,
						filter,
						session.getEngineContext( ).getScriptContext( ));
				results.add( filterHelper );
			}
		}
		return results;
	}

	/**
	 * 
	 * @param filter
	 * @return
	 */
	private boolean isMeasureFilter( IFilterDefinition filter )
	{
		if ( filter instanceof ICubeFilterDefinition )
		{
			ICubeFilterDefinition cubeFilter = (ICubeFilterDefinition) filter;
			return cubeFilter.getTargetLevel( ) == null;
		}
		return false;
	}


	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public IComputedMeasureHelper getComputedMeasureHelper( )
			throws DataException
	{
		if ( this.defn.getComputedMeasures( ) != null
				&& this.defn.getComputedMeasures( ).size( ) > 0 )
			return new ComputedMeasureHelper( this.scope,
					session.getEngineContext( ).getScriptContext( ),
					this.defn.getComputedMeasures( ));
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public ICubeQueryDefinition getCubeQueryDefinition( )
	{
		return this.defn;
	}

	/**
	 * 
	 * @return
	 */
	public DataEngineSession getSession( )
	{
		return this.session;
	}
	
	/**
	 * 
	 * @return
	 */
	public DataEngineContext getContext( )
	{
		return this.context;
	}

	/**
	 * 
	 * @return
	 */
	public List getColumnEdgeSort( )
	{
		return getEdgeSort( ICubeQueryDefinition.COLUMN_EDGE );
	}

	/**
	 * 
	 * @return
	 */
	public List getRowEdgeSort( )
	{
		return getEdgeSort( ICubeQueryDefinition.ROW_EDGE );
	}

	/**
	 * 
	 * @return
	 */
	public List getPageEdgeSort( )
	{
		return getEdgeSort( ICubeQueryDefinition.PAGE_EDGE );
	}
	
	/**
	 * 
	 * @return
	 */
	public String getQueryResultsId()
	{
		return this.queryResultsId;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setQueryResultsId( String id )
	{
		this.queryResultsId = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public IBaseQueryResults getOuterResults( )
	{
		return this.outResults;
	}
	
	public Scriptable getScope( )
	{
		return scope;
	}
	
	/**
	 * 
	 * @param edgeType
	 * @return
	 */
	private List getEdgeSort( int edgeType )
	{
		List l = this.defn.getSorts( );
		List result = new ArrayList( );
		for ( int i = 0; i < l.size( ); i++ )
		{
			ICubeSortDefinition sort = (ICubeSortDefinition) l.get( i );
			if ( this.defn.getEdge( edgeType ) != null &&
					this.defn.getEdge( edgeType )
							.getDimensions( )
							.contains( sort.getTargetLevel( )
									.getHierarchy( )
									.getDimension( ) ) )
			{
				result.add( sort );
			}
		}

		Collections.sort( result, new Comparator( ) {

			public int compare( Object arg0, Object arg1 )
			{
				int level1 = ( (ICubeSortDefinition) arg0 ).getTargetLevel( )
						.getHierarchy( )
						.getLevels( )
						.indexOf( ( (ICubeSortDefinition) arg0 ).getTargetLevel( ) );
				int level2 = ( (ICubeSortDefinition) arg1 ).getTargetLevel( )
						.getHierarchy( )
						.getLevels( )
						.indexOf( ( (ICubeSortDefinition) arg1 ).getTargetLevel( ) );

				if ( level1 == level2 )
					return 0;
				else if ( level1 < level2 )
					return -1;
				else
					return 1;

			}
		} );
		return result;
	}
}
