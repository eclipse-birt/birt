/*******************************************************************************
 * Copyright (c) 2004,2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.data.dte;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;

/**
 * implments IDataEngine interface, using birt's data transformation engine
 * (DtE)
 * 
 */
public class DteDataEngine extends AbstractDataEngine
{
	
	private boolean needCache;

	/**
	 * cache the query - result set mapping. 
	 *
     * <li>the key is: [parent rset]"." [raw id] "." [query id] </li>
     * <li>the value is: result set name </li>
	 *
     * It is only valid if the  needCache set to true
	 */
	protected HashMap<String, String> rsetRelations = new HashMap<String, String>( );

	/**
	 * similar to rsetRelations.
	 * Difference is that rsetRelations2 is using row id instead of raw id.
	 * The mapping is:
	 * <li>the key is: [parent rset]"."[row id]"."[query id] </li>
	 * <li>the value is: result set name </li>
	 */
	protected HashMap<String, String> rsetRelations2 = new HashMap<String, String>( );

	//FIXME: code review. throw out all exceptions in data engines. And throw exception not return null.	

	/**
	 * creates data engine, by first look into the directory specified by
	 * configuration variable odadriver, for oda configuration file. The oda
	 * configuration file is at $odadriver/drivers/driverType/odaconfig.xml.
	 * <p>
	 * 
	 * If the config variable is not set, search configuration file at
	 * ./drivers/driverType/odaconfig.xml.
	 * 
	 * @param context
	 * @throws BirtException 
	 */
	public DteDataEngine( DataEngineFactory factory, ExecutionContext context,
			boolean needCache ) throws BirtException
	{
		super( factory, context );
		this.needCache = needCache;
		try
		{
			// create the DteData session.
			DataSessionContext dteSessionContext = new DataSessionContext(
					DataSessionContext.MODE_DIRECT_PRESENTATION, context
							.getDesign( ), context.getScriptContext( ), context
							.getApplicationClassLoader( ) );
			dteSessionContext.setAppContext( context.getAppContext( ) );
			DataEngineContext dteEngineContext = dteSessionContext.getDataEngineContext( );
			dteEngineContext.setLocale( context.getLocale( ) );
			dteEngineContext.setTimeZone( context.getTimeZone( ) );
			String tempDir = getTempDir( context );
			if ( tempDir != null )
			{
				dteEngineContext.setTmpdir( tempDir );
			}

			dteSession = context.newSession( dteSessionContext );
		}
		catch ( Exception ex )
		{
			//FIXME: code review. throw engine exception. 
			logger.log( Level.SEVERE, "can not create the DTE data engine", ex );
		}
	}
	
	/**
	 * this constructor is used 
	 * @param context
	 * @param obj
	 * @throws BirtException 
	 */
	protected DteDataEngine( DataEngineFactory factory,
			ExecutionContext context, Object obj ) throws BirtException
	{
		super( factory, context );
	}

	protected IBaseResultSet doExecuteQuery( IBaseResultSet parentResultSet,
			IQueryDefinition query, Object queryOwner, boolean useCache ) throws BirtException
	{
		IPreparedQuery pQuery = (IPreparedQuery) queryMap.get( query );
		if ( pQuery == null )
		{
			return null;
		}

		ScriptContext scriptContext = context.getScriptContext( );

		IBaseQueryResults dteResults = null; // the dteResults of this query
		boolean needExecute = queryCache.needExecute( query, queryOwner,
				useCache );
		if ( !needExecute )
		{
			dteResults = getCachedQueryResult( query, parentResultSet );
		}
		if ( dteResults == null )
		{
			if ( needCache )
			{
				( (BaseQueryDefinition) query ).setCacheQueryResults( true );
			}
			if ( parentResultSet == null )
			{
				// this is the root query
				dteResults = dteSession.execute( pQuery, null, scriptContext );
			}
			else
			{
				// this is the nest query, execute the query in the
				// parent results
				dteResults = dteSession.execute( pQuery, parentResultSet
						.getQueryResults( ), scriptContext );
			}
			queryCache.putCachedQuery( query, dteResults.getID( ) );
		}

		IBaseResultSet resultSet;
		if ( parentResultSet == null )
		{
			// this is the root query						
			resultSet = new QueryResultSet( this,
					context,
					query,
					(IQueryResults) dteResults );
		}
		else
		{
			// this is the nest query		
			resultSet = new QueryResultSet( this,
					context,
					parentResultSet,
					query,
					(IQueryResults) dteResults );
		}
		if ( needCache )
		{
			cacheResultID( parentResultSet, query, resultSet );
		}
		return resultSet;
	}

	protected IBaseResultSet doExecuteCube( IBaseResultSet parentResultSet,
			ICubeQueryDefinition query, Object queryOwner, boolean useCache ) throws BirtException
	{
		if ( useCache )
		{
			Object obj = cachedQueryToResults.get( query );
			String rsetId = obj == null ? null : String.valueOf( obj );
			query.setQueryResultsID( rsetId );
		}
		else
		{
			query.setQueryResultsID( null );
		}
		
		if ( needCache )
		{
			ICubeQueryDefinition cubeQuery = (ICubeQueryDefinition) query;
			cubeQuery.setCacheQueryResults( true );
			cubeQuery.setNeedAccessFactTable( true );
		}

		// the cube query must be re-prepared before executing.
		IBasePreparedQuery pQuery = (IBasePreparedQuery) queryMap.get( query );
		if ( pQuery == null )
		{
			throw new EngineException( MessageConstants.PREPARED_QUERY_NOT_FOUND_ERROR , query );
		}

		ScriptContext scriptContext = context.getScriptContext( );
		

		ICubeQueryResults dteResults = null; // the dteResults of this query
		boolean needExecute = cubeCache.needExecute( query, queryOwner,
				useCache );
		if ( !needExecute )
		{
			dteResults = (ICubeQueryResults) getCachedCubeResult(
					(ICubeQueryDefinition) query, parentResultSet );
		}
		if ( dteResults == null )
		{
			if ( parentResultSet == null )
			{
				// this is the root query
				dteResults = (ICubeQueryResults) dteSession.execute( pQuery,
						null, scriptContext );
			}
			else
			{
				// this is the nest query, execute the query in the
				// parent results
				dteResults = (ICubeQueryResults) dteSession.execute( pQuery,
						parentResultSet.getQueryResults( ), scriptContext );
			}
		}
		
		IBaseResultSet resultSet = null;
		{
			if ( parentResultSet == null )
			{
				resultSet = new CubeResultSet( this, context, query, dteResults );
			}
			else
			{
				resultSet = new CubeResultSet( this, context, parentResultSet,
						query,  dteResults );
			}
		}

		// persist the queryResults witch need cached. 
		cachedQueryToResults.put( query, dteResults.getID( ) );

		if ( needCache )
		{
			cacheResultID( parentResultSet, query, resultSet );
		}
		return resultSet;
	}

	protected void doPrepareQuery( Report report, Map appContext )
	{
		// prepare report queries
		queryIDMap.putAll( report.getQueryIDs( ) );
		super.doPrepareQuery( report, appContext );
	}

	protected void cacheResultID( IBaseResultSet parentResultSet,
			IDataQueryDefinition query, IBaseResultSet resultSet )
			throws BirtException
	{
		String pRsetId = null; // id of the parent query restuls
		String rawId = "-1"; // row id of the parent query results
		String rowId = "-1";
		if ( parentResultSet != null )
		{
			if ( parentResultSet instanceof QueryResultSet )
			{
				QueryResultSet qrs = (QueryResultSet) parentResultSet;
				pRsetId = qrs.getQueryResultsID( );
				rowId = String.valueOf( qrs.getRowIndex( ) );
			}
			else
			{
				CubeResultSet crs = (CubeResultSet) parentResultSet;
				pRsetId = crs.getQueryResultsID( );
				rowId = crs.getCellIndex( );
			}
			rawId = parentResultSet.getRawID( );
		}
		String queryID = (String) queryIDMap.get( query );
		addResultID( pRsetId, rawId, queryID, resultSet.getQueryResults( )
				.getID( ), rowId );
	}

	protected void addResultID( String pRsetId, String rawId, String queryId,
			String rsetId, String rowId )
	{
		keyBuffer.setLength( 0 );
		keyBuffer.append( pRsetId );
		keyBuffer.append( "." );
		keyBuffer.append( rawId );
		keyBuffer.append( "." );
		keyBuffer.append( queryId );
		rsetRelations.put( keyBuffer.toString( ), rsetId );
		keyBuffer.setLength( 0 );
		keyBuffer.append( pRsetId );
		keyBuffer.append( "." );
		keyBuffer.append( rowId );
		keyBuffer.append( "." );
		keyBuffer.append( queryId );
		rsetRelations2.put( keyBuffer.toString( ), rsetId );
		
	}

	private StringBuffer keyBuffer = new StringBuffer( );

	public String getResultID( String parent, String rawId, String queryId )
	{
		keyBuffer.setLength( 0 );
		keyBuffer.append( parent );
		keyBuffer.append( "." );
		keyBuffer.append( rawId );
		keyBuffer.append( "." );
		keyBuffer.append( queryId );
		// try to search the rset id
		String rsetId = (String) rsetRelations.get( keyBuffer.toString( ) );
		return rsetId;
	}

	public String getResultIDByRowID( String parent, String rowId,
			String queryId )
	{
		keyBuffer.setLength( 0 );
		keyBuffer.append( parent );
		keyBuffer.append( "." );
		keyBuffer.append( rowId );
		keyBuffer.append( "." );
		keyBuffer.append( queryId );
		// try to search the rset id
		String rsetId = (String) rsetRelations2.get( keyBuffer.toString( ) );
		return rsetId;
	}
}