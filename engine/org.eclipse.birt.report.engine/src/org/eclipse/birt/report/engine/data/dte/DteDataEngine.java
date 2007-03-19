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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.ir.Report;
import org.mozilla.javascript.Scriptable;

/**
 * implments IDataEngine interface, using birt's data transformation engine
 * (DtE)
 * 
 */
public class DteDataEngine extends AbstractDataEngine
{

	/*
	 * need not be stored in report document.
	 */
	protected HashMap queryMap = new HashMap( );

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
			DataSessionContext dteSessionContext = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION );
			DataEngineContext dteEngineContext = dteSessionContext.getDataEngineContext( );
			dteEngineContext.setLocale( context.getLocale( ) );

			String tempDir = getTempDir( context );
			if ( tempDir != null )
			{
				dteEngineContext.setTmpdir( tempDir );
			}

			dteSession = DataRequestSession.newSession( dteSessionContext );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "can't create the DTE data engine", ex );
			ex.printStackTrace( );
		}
	}

	protected void doPrepareQuery( Report report, Map appContext )
	{
		// prepare report queries
		List queries = report.getQueries( );
		for ( int i = 0; i < queries.size( ); i++ )
		{
			IDataQueryDefinition query = (IDataQueryDefinition) queries.get( i );
			try
			{
				IBasePreparedQuery preparedQuery = dteSession.prepare( query,
						appContext );
				queryMap.put( query, preparedQuery );
			}
			catch ( BirtException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
				context.addException( e );
			}
		} // end of prepare
	}

	protected IBaseResultSet doExecuteQuery( IBaseResultSet resultSet,
			IDataQueryDefinition query )
	{
		if ( query instanceof IQueryDefinition )
		{
			return doExecuteQuery( resultSet, (IQueryDefinition) query );
		}
		else if ( query instanceof ICubeQueryDefinition )
		{
			return doExecuteCube( resultSet, (ICubeQueryDefinition) query );
		}
		return null;
	}

	protected IBaseResultSet doExecuteQuery( IBaseResultSet resultSet,
			IQueryDefinition query )
	{
		IPreparedQuery pQuery = (IPreparedQuery) queryMap.get( query );
		if ( pQuery == null )
		{
			return null;
		}

		try
		{
			Scriptable scope = context.getSharedScope( );

			IBaseQueryResults dteResults; // the dteResults of this query
			if ( resultSet == null )
			{
				// this is the root query
				dteResults = dteSession.execute( pQuery, null, scope );
				resultSet = new QueryResultSet( this,
						context,
						query,
						(IQueryResults) dteResults );
			}
			else
			{
				// this is the nest query, execute the query in the
				// parent results
				dteResults = dteSession.execute( pQuery,
						resultSet.getQueryResults( ),
						scope );
				resultSet = new QueryResultSet( this,
						context,
						resultSet,
						query,
						(IQueryResults) dteResults );
			}

			return resultSet;
		}
		catch ( BirtException be )
		{
			logger.log( Level.SEVERE, be.getMessage( ) );
			context.addException( be );
		}

		return null;
	}

	protected IBaseResultSet doExecuteCube( IBaseResultSet resultSet,
			ICubeQueryDefinition query )
	{
		IPreparedCubeQuery pQuery = (IPreparedCubeQuery) queryMap.get( query );
		if ( pQuery == null )
		{
			return null;
		}

		try
		{
			Scriptable scope = context.getSharedScope( );

			ICubeQueryResults dteResults; // the dteResults of this query
			if ( resultSet == null )
			{
				// this is the root query
				dteResults = (ICubeQueryResults) dteSession.execute( pQuery,
						null,
						scope );				
				resultSet = new CubeResultSet( this,
						context,
						pQuery.getCubeQueryDefinition( ),
						dteResults );
			}
			else
			{
				// this is the nest query, execute the query in the
				// parent results
				dteResults = (ICubeQueryResults) dteSession.execute( pQuery,
						resultSet.getQueryResults( ),
						scope );
				resultSet = new CubeResultSet( this, context, resultSet, query,
						(ICubeQueryResults) dteResults );
			}

			return resultSet;
		}
		catch ( BirtException be )
		{
			logger.log( Level.SEVERE, be.getMessage( ) );
			context.addException( be );
		}

		return null;
	}
}