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
import org.mozilla.javascript.Scriptable;

/**
 * implments IDataEngine interface, using birt's data transformation engine
 * (DtE)
 * 
 */
public class DteDataEngine extends AbstractDataEngine
{
	
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
	 */
	public DteDataEngine( ExecutionContext context )
	{
		super( context );

		try
		{
			// create the DteData session.
			DataSessionContext dteSessionContext = new DataSessionContext(
					DataSessionContext.MODE_DIRECT_PRESENTATION, context.getDesign( ), context
							.getSharedScope( ) );
			DataEngineContext dteEngineContext = dteSessionContext.getDataEngineContext( );
			dteEngineContext.setLocale( context.getLocale( ) );
			dteEngineContext.setClassLoader( context.getApplicationClassLoader( ) );

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
	 */
	protected DteDataEngine( ExecutionContext context, Object obj )
	{
		super( context );
	}

	protected IBaseResultSet doExecuteQuery( IBaseResultSet parentResultSet,
			IQueryDefinition query, boolean useCache ) throws BirtException
	{
		IPreparedQuery pQuery = (IPreparedQuery) queryMap.get( query );
		if ( pQuery == null )
		{
			return null;
		}

		Scriptable scope = context.getSharedScope( );

		IBaseQueryResults dteResults = null; // the dteResults of this query
		if ( useCache )
		{
			dteResults = getCachedQueryResult( query, parentResultSet );
		}
		if ( dteResults == null )
		{
			if ( parentResultSet == null )
			{
				// this is the root query
				dteResults = dteSession.execute( pQuery, null, scope );
			}
			else
			{
				// this is the nest query, execute the query in the
				// parent results
				dteResults = dteSession.execute( pQuery, parentResultSet
						.getQueryResults( ), scope );
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
			ICubeQueryDefinition query, boolean useCache ) throws BirtException
	{
		if ( useCache )
		{
			String rsetId = String.valueOf( cachedQueryToResults.get( query ) );
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

		Scriptable scope = context.getSharedScope( );
		IBaseResultSet resultSet;

		ICubeQueryResults dteResults; // the dteResults of this query
		if ( parentResultSet == null )
		{
			// this is the root query
			dteResults = (ICubeQueryResults) dteSession.execute( pQuery,
					null, scope );
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
					parentResultSet.getQueryResults( ), scope );
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