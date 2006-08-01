/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.FilterDefnUtil;
import org.eclipse.birt.data.engine.impl.document.GroupDefnUtil;
import org.eclipse.birt.data.engine.impl.document.QueryDefnUtil;
import org.eclipse.birt.data.engine.impl.document.QueryResultIDUtil;
import org.eclipse.birt.data.engine.impl.document.QueryResultInfo;
import org.eclipse.birt.data.engine.impl.document.RDLoad;
import org.eclipse.birt.data.engine.impl.document.RDUtil;
import org.eclipse.birt.data.engine.impl.document.StreamManager;

/**
 * Create concreate class of IPreparedQuery
 */
class PreparedQueryUtil
{
	/**
	 * Creates a new instance of the proper subclass based on the type of the
	 * query passed in.
	 * @param dataEngine
	 * @param queryDefn
	 * @param appContext	Application context map; could be null.
	 * @return PreparedReportQuery
	 * @throws DataException 
	 */
	static IPreparedQuery newInstance( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, Map appContext ) throws DataException
	{
		assert dataEngine != null;
		assert queryDefn != null;

		if ( queryDefn.getQueryResultsID( ) != null )
			return newIVInstance( dataEngine, queryDefn );

		IBaseDataSetDesign dset = dataEngine.getDataSetDesign( queryDefn.getDataSetName( ) );
		if ( dset == null )
		{
			// In new column binding feature, when there is no data set,
			// it is indicated that a dummy data set needs to be created
			// internally. But using the dummy one, the binding expression only
			// can refer to row object and no other object can be refered such
			// as rows.
			if ( queryDefn.getQueryResultsID( ) == null )
				return new PreparedDummyQuery( dataEngine.getContext( ),
						queryDefn,
						dataEngine.getSharedScope( ) );
		}

		IPreparedQuery preparedQuery;

		if ( dset instanceof IScriptDataSetDesign )
		{
			preparedQuery = new PreparedScriptDSQuery( dataEngine,
					queryDefn,
					dset,
					appContext );
		}
		else if ( dset instanceof IOdaDataSetDesign )
		{
			preparedQuery = new PreparedOdaDSQuery( dataEngine,
					queryDefn,
					dset,
					appContext );
		}
		else if ( dset instanceof IJointDataSetDesign )
		{
			preparedQuery = new PreparedJointDataSourceQuery( dataEngine,
					queryDefn,
					dset,
					appContext );
		}
		else
		{
			throw new DataException( ResourceConstants.UNSUPPORTED_DATASET_TYPE,
					dset.getName( ) );
		}

		return preparedQuery;
	}
	
	/**
	 * @param dataEngine
	 * @param queryDefn
	 * @return
	 * @throws DataException
	 */
	private static IPreparedQuery newIVInstance( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn ) throws DataException
	{
		if ( runQueryOnRS( dataEngine, queryDefn ) )
			return new PreparedIVQuery( dataEngine, queryDefn );
		else
			return new PreparedIVDataSourceQuery( dataEngine, queryDefn );
	}

	/**
	 * Whether query is running based on the result set of report document or
	 * the data set.
	 * 
	 * @param dataEngine
	 * @param queryDefn
	 * @return true, running on result set
	 * @throws DataException
	 */
	private static boolean runQueryOnRS( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn ) throws DataException
	{
		String queryResultID = queryDefn.getQueryResultsID( );

		String rootQueryResultID = QueryResultIDUtil.get1PartID( queryResultID );
		String parentQueryResultID = null;
		if ( rootQueryResultID != null )
			parentQueryResultID = QueryResultIDUtil.get2PartID( queryResultID );
		else
			rootQueryResultID = queryResultID;

		QueryResultInfo queryResultInfo = new QueryResultInfo( rootQueryResultID,
				parentQueryResultID,
				null,
				null,
				-1 );
		RDLoad rdLoad = RDUtil.newLoad( dataEngine.getContext( ),
				queryResultInfo );

		boolean runningOnRS = GroupDefnUtil.isEqualGroups( queryDefn.getGroups( ),
				rdLoad.loadGroupDefn( StreamManager.ROOT_STREAM,
						StreamManager.BASE_SCOPE ) );
		if ( runningOnRS == false )
			return false;
		
		runningOnRS = !hasTopBottomNInFilter( queryDefn.getFilters( ) );
		if ( runningOnRS == false)
			return false;
		
		runningOnRS = isCompatibleRSMap( rdLoad.loadQueryDefn( StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE )
				.getResultSetExpressions( ),
				queryDefn.getResultSetExpressions( ) );

		if ( runningOnRS == false )
			return false;

		runningOnRS = isCompatibleSubQuery( rdLoad.loadQueryDefn( StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE ),
				queryDefn );
		
		if ( runningOnRS == false )
			return false;
		
		IBaseQueryDefinition qd = rdLoad.loadQueryDefn( StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE );
		List filters = qd.getFilters( );
		
		if ( FilterDefnUtil.isConflictFilter( filters, queryDefn.getFilters( ) ) )
		{
			runningOnRS = false;
			filters = rdLoad.loadOriginalQueryDefn( StreamManager.ROOT_STREAM,
					StreamManager.BASE_SCOPE ).getFilters( );
			FilterDefnUtil.getRealFilterList( filters, queryDefn.getFilters( ) );
		}
		
		if ( runningOnRS == false )
			return false;
	
		if ( queryDefn.getFilters( ) != null
				&& queryDefn.getFilters( ).size( ) > 0 )
			runningOnRS = queryDefn.getResultSetExpressions( ).values( ) == null
					|| !hasAggregationOnRowObjects( queryDefn.getResultSetExpressions( )
							.values( )
							.iterator( ) );
		
		return runningOnRS;
	}

	/**
	 * @param filters
	 * @return
	 */
	private static boolean hasTopBottomNInFilter( List filters )
	{
		if ( filters == null || filters.size( ) == 0 )
			return false;
		
		for( int i = 0; i < filters.size( ); i++ )
		{
			Object o = ((IFilterDefinition)filters.get( i )).getExpression( );
			if ( o instanceof IConditionalExpression )
			{
				int type = ((IConditionalExpression)o).getOperator( );
				if( type == IConditionalExpression.OP_TOP_N 
					|| type == IConditionalExpression.OP_BOTTOM_N
					|| type == IConditionalExpression.OP_TOP_PERCENT
					|| type == IConditionalExpression.OP_BOTTOM_PERCENT)
				return true;	
			}
		}
		
		return false;
	}
	
	/**
	 * @return
	 */
	private static boolean isCompatibleRSMap( Map oldMap, Map newMap )
	{
		if ( oldMap == newMap )
			return true;
		else if ( oldMap == null )
			return newMap.size( ) == 0;
		else if ( newMap == null )
			return oldMap.size( ) == 0;

		return oldMap.size( ) >= newMap.size( );
	}
	
	/**
	 * @param oldSubQuery
	 * @param newSubQuery
	 * @return
	 */
	private static boolean isCompatibleSubQuery( IBaseQueryDefinition oldDefn,
			IBaseQueryDefinition newDefn )
	{		
		boolean isComp = QueryDefnUtil.isCompatibleSQs( oldDefn.getSubqueries( ),
				newDefn.getSubqueries( ) );

		if ( isComp == false )
			return false;

		Iterator oldIt = oldDefn.getGroups( ).iterator( );
		Iterator newIt = newDefn.getGroups( ).iterator( );
		while ( newIt.hasNext( ) )
		{
			IGroupDefinition oldGroupDefn = (IGroupDefinition) oldIt.next( );
			IGroupDefinition newGroupDefn = (IGroupDefinition) newIt.next( );
			isComp = QueryDefnUtil.isCompatibleSQs( oldGroupDefn.getSubqueries( ),
					newGroupDefn.getSubqueries( ) );
			if ( isComp == false )
				return false;
		}

		return true;
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	private static boolean hasAggregationOnRowObjects( Iterator it )
	{
		while( it.hasNext( ))
		{
			Object o = it.next( );
			if( ExpressionCompilerUtil.hasRowExprInAggregation( (IBaseExpression)o))
			{
				return true;
			}
		}
		return false;
	}
}
