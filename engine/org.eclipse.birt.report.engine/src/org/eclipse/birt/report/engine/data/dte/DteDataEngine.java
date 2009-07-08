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

package org.eclipse.birt.report.engine.data.dte;

import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.i18n.MessageConstants;

/**
 * implments IDataEngine interface, using birt's data transformation engine
 * (DtE)
 * 
 */
public class DteDataEngine extends AbstractDataEngine
{
	
	private boolean needCache;
	
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
	public DteDataEngine( ExecutionContext context, boolean needCache ) throws BirtException
	{
		super( context );
		this.needCache = needCache;
		try
		{
			// create the DteData session.
			DataSessionContext dteSessionContext = new DataSessionContext(
					DataSessionContext.MODE_DIRECT_PRESENTATION, context
							.getDesign( ), context.getScriptContext( ), context
							.getApplicationClassLoader( ) );
			DataEngineContext dteEngineContext = dteSessionContext.getDataEngineContext( );
			dteEngineContext.setLocale( context.getLocale( ) );
			dteEngineContext.setTimeZone( context.getTimeZone( ) );
			String tempDir = getTempDir( context );
			if ( tempDir != null )
			{
				dteEngineContext.setTmpdir( tempDir );
			}

			dteSession = DataRequestSession.newSession( dteSessionContext );
		}
		catch ( Exception ex )
		{
			//FIXME: code review. throw engine exception. 
			logger.log( Level.SEVERE, "can't create the DTE data engine", ex );
		}
	}
	
	/**
	 * this constructor is used 
	 * @param context
	 * @param obj
	 * @throws BirtException 
	 */
	protected DteDataEngine( ExecutionContext context, Object obj ) throws BirtException
	{
		super( context );
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
		boolean needExecute = queryCache.needExecute( query, queryOwner, needCache || useCache );
		if ( !needExecute )
		{
			dteResults = getCachedQueryResult( query, parentResultSet );
		}
		if ( dteResults == null )
		{
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
			putCachedQueryResult( query, dteResults.getID( ) );
		}		
		if ( parentResultSet == null )
		{
			// this is the root query						
			return new QueryResultSet( this,
					context,
					query,
					(IQueryResults) dteResults );
		}
		else
		{
			// this is the nest query		
			return new QueryResultSet( this,
					context,
					parentResultSet,
					query,
					(IQueryResults) dteResults );
		}
	}

	protected IBaseResultSet doExecuteCube( IBaseResultSet parentResultSet,
			ICubeQueryDefinition query, Object queryOwner, boolean useCache ) throws BirtException
	{
		if ( needCache || useCache )
		{
			Object obj = cachedQueryToResults.get( query );
			String rsetId = obj == null ? null : String.valueOf( obj );
			query.setQueryResultsID( rsetId );
		}
		else
		{
			query.setQueryResultsID( null );
		}
		
		// the cube query must be re-prepared before executing.
		IBasePreparedQuery pQuery = (IBasePreparedQuery) queryMap.get( query );
		if ( pQuery == null )
		{
			throw new EngineException( MessageConstants.PREPARED_QUERY_NOT_FOUND_ERROR , query );
		}

		ScriptContext scriptContext = context.getScriptContext( );
		IBaseResultSet resultSet;

		ICubeQueryResults dteResults; // the dteResults of this query
		if ( parentResultSet == null )
		{
			// this is the root query
			dteResults = (ICubeQueryResults) dteSession.execute( pQuery,
					null, scriptContext );
			resultSet = new CubeResultSet( this,
					context,
					query,
					dteResults );
		}
		else
		{
			// this is the nest query, execute the query in the
			// parent results
			dteResults = (ICubeQueryResults) dteSession.execute( pQuery,
					parentResultSet.getQueryResults( ), scriptContext );
			resultSet = new CubeResultSet( this, context, parentResultSet, query,
					(ICubeQueryResults) dteResults );
		}

		// persist the queryResults witch need cached. 
		if ( query.cacheQueryResults( ) )
		{
			cachedQueryToResults.put( query, dteResults.getID( ) );
		}

		return resultSet;
	}
}