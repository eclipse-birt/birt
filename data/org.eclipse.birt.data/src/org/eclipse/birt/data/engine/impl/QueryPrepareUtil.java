
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.QueryResultIDUtil;
import org.eclipse.birt.data.engine.impl.document.QueryResultInfo;
import org.eclipse.birt.data.engine.impl.document.RDLoad;
import org.eclipse.birt.data.engine.impl.document.RDUtil;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.PreparedCubeQuery;

/**
 * 
 */

public class QueryPrepareUtil
{
	public static IPreparedQuery prepareQuery( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, IBaseDataSetDesign dataSetDesign,
			Map appContext, IQueryContextVisitor contextVisitor) throws DataException
	{
		return null;
		
	}
	
	static public IPreparedQuery preparePresentationQuery(
			DataEngineImpl dataEngine, IQueryDefinition queryDefn,
			IBaseDataSetDesign dataSetDesign, Map appContext,
			IQueryContextVisitor contextVisitor ) throws DataException
	{
		return null;
	}
	
	public static IPreparedQuery prepareIVGenerationQuery(
			DataEngineImpl dataEngine, IQueryDefinition queryDefn,
			IBaseDataSetDesign dataSetDesign, Map appContext,
			IQueryContextVisitor contextVisitor ) throws DataException
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
		RDLoad rdLoad = RDUtil.newLoad( dataEngine.getSession( ).getTempDir( ), dataEngine.getContext( ),
				queryResultInfo );

		//Please Note We should use parent scope here, for the new query should be compared to the query being executed 
		//immediately behind it rather than the root query.
		IBaseQueryDefinition previousQueryDefn = rdLoad.loadQueryDefn( StreamManager.ROOT_STREAM,
						StreamManager.PARENT_SCOPE );
	
		if( QueryCompUtil.isIVQueryDefnEqual( dataEngine.getContext( ).getMode( ), previousQueryDefn, queryDefn ))
		{
			return new DummyPreparedQuery( queryDefn,
					dataEngine.getSession( ),
					dataEngine.getContext( ),
					PLSUtil.isPLSEnabled( queryDefn )? queryDefn.getQueryExecutionHints( )
									.getTargetGroupInstances( ) : null );
		}
		else if ( NoRecalculateQueryUtil.isOptimizableIVQuery( previousQueryDefn, queryDefn, queryResultID ))
		{
			return NoRecalculateQueryUtil.getPreparedIVQuery( dataEngine, previousQueryDefn, queryDefn, queryResultID, appContext );
		}
		else
		{
			if ( queryDefn.isSummaryQuery( ) )
			{
				IResultClass rsMeta = rdLoad.loadResultClass( );
				PreparedQueryUtil.populateSummaryBinding( queryDefn, rsMeta );
			}
			return new PreparedIVDataSourceQuery( dataEngine, queryDefn, QueryContextVisitorUtil.createQueryContextVisitor( queryDefn,
					appContext ) );
		}	
	}
	
	public static IPreparedCubeQuery prepareQuery( Map<String, String> cubeDataSourceMap, Map<String, String> cubeDataObjectMap, DataEngineSession session, DataEngineContext context, ICubeQueryDefinition cubeQuery, Map appContext ) throws DataException
	{
		return new PreparedCubeQuery( cubeQuery,
				session,
				context,
				appContext );
	}
	
	public static void clear( DataEngineSession session )
	{		
	}
}
