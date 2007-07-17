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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.ir.Report;
import org.mozilla.javascript.Scriptable;

public class DataGenerationEngine extends AbstractDataEngine
{

	/*
	 * need not be stored in report document.
	 */
	protected HashMap queryMap = new HashMap( );

	/**
	 * output stream used to save the resultset relations
	 */
	private DataOutputStream dos;

	public DataGenerationEngine( ExecutionContext context,
			IDocArchiveWriter writer ) throws Exception
	{
		super( context );

		// create the DteData session.
		DataSessionContext dteSessionContext = new DataSessionContext(
				DataSessionContext.MODE_GENERATION, null, context
						.getSharedScope( ) );
		dteSessionContext.setDocumentWriter( writer );
		DataEngineContext dteEngineContext = dteSessionContext
				.getDataEngineContext( );
		dteEngineContext.setLocale( context.getLocale( ) );

		String tempDir = getTempDir( context );
		if ( tempDir != null )
		{
			dteEngineContext.setTmpdir( tempDir );
		}

		dteSession = DataRequestSession.newSession( dteSessionContext );

		dos = new DataOutputStream(
				writer
						.createRandomAccessStream( ReportDocumentConstants.DATA_META_STREAM ) );

		DteMetaInfoIOUtil.startMetaInfo( dos );

	}

	protected void doPrepareQuery( Report report, Map appContext )
	{
		this.appContext = appContext;
		// prepare report queries
		queryIDMap.putAll( report.getQueryIDs( ) );
		for ( int i = 0; i < report.getQueries( ).size( ); i++ )
		{
			IDataQueryDefinition queryDef = (IDataQueryDefinition) report.getQueries( )
					.get( i );
			try
			{
				IBasePreparedQuery preparedQuery = dteSession.prepare( queryDef,
						appContext );
				queryMap.put( queryDef, preparedQuery );
			}
			catch ( BirtException be )
			{
				logger.log( Level.SEVERE, be.getMessage( ) );
				context.addException( be );
			}
		}
	}	
	
	protected IBaseResultSet doExecuteQuery( IBaseResultSet resultSet,
			IDataQueryDefinition query, boolean useCache )
	{
		if ( query instanceof IQueryDefinition )
		{
			return doExecuteQuery( resultSet, (IQueryDefinition) query,
					useCache );
		}
		else if ( query instanceof ICubeQueryDefinition )
		{
			return doExecuteCube( resultSet, (ICubeQueryDefinition) query,
					useCache );
		}
		return null;
	}

	protected IQueryResultSet doExecuteQuery( IBaseResultSet resultSet,
			IQueryDefinition query, boolean useCache )
	{		
		IBasePreparedQuery pQuery = (IBasePreparedQuery) queryMap.get( query );
		if ( pQuery == null )
		{
			return null;
		}

		try
		{
			String queryID = (String) queryIDMap.get( query );
			Scriptable scope = context.getSharedScope( );

			String pRsetId = null; // id of the parent query restuls
			String rowId = "-1"; // row id of the parent query results
			IBaseQueryResults dteResults = null; // the dteResults of this query
			IQueryResultSet curRSet = null;
			if ( resultSet == null )
			{
				// this is the root query
				if ( useCache )
				{
					dteResults = getCachedQueryResult( query );
				}
				if ( dteResults == null )
				{
					dteResults = dteSession.execute( pQuery, null, scope );
					if ( query.cacheQueryResults( ) )
					{
						cachedQueryIdMap.put( query, dteResults.getID( ) );
					}
				}
				curRSet = new QueryResultSet( this, context, query,
						(IQueryResults) dteResults );
			}
			else
			{
				pRsetId = resultSet.getQueryResults( ).getID( );
				rowId = resultSet.getRawID( );

				if ( useCache )
				{
					dteResults = getCachedQueryResult( query );
				}
				if ( dteResults == null )
				{
					// this is the nest query, execute the query in the
					// parent results
					dteResults = dteSession.execute( pQuery, resultSet
							.getQueryResults( ), scope );
					if ( query.cacheQueryResults( ) )
					{
						cachedQueryIdMap.put( query, dteResults.getID( ) );
					}
				}
				curRSet = new QueryResultSet( this, context, resultSet, query,
						(IQueryResults) dteResults );
			}

			// save the meta infomation
			storeDteMetaInfo( pRsetId, rowId, queryID, dteResults.getID( ) );

			return curRSet;
		}
		catch ( BirtException be )
		{
			logger.log( Level.SEVERE, be.getMessage( ) );
			context.addException( be );
		}

		return null;
	}
	
	protected ICubeResultSet doExecuteCube( IBaseResultSet resultSet,
			ICubeQueryDefinition query, boolean useCache )
	{			
		if ( useCache )
		{
			String rsetId = String.valueOf( cachedQueryIdMap.get( query ) );
			query.setQueryResultsID( rsetId );
		}
		else
		{
			query.setQueryResultsID( null );
		}
		
		// the cube query must be re-prepared before executing.
		IBasePreparedQuery pQuery = null;
		try
		{
			pQuery = dteSession.prepare( query, appContext );
		}
		catch ( BirtException be )
		{
			logger.log( Level.SEVERE, be.getMessage( ) );
			context.addException( be );
		}		
		if ( pQuery == null )
		{
			return null;
		}
		
		try
		{
			String queryID = (String) queryIDMap.get( query );
			Scriptable scope = context.getSharedScope( );

			String pRsetId = null; // id of the parent query restuls
			String rowId = "-1"; // row id of the parent query results
			IBaseQueryResults dteResults; // the dteResults of this query
			ICubeResultSet curRSet = null;
			if ( resultSet == null )
			{
				// this is the root query
				dteResults = dteSession.execute( pQuery, null, scope );
				curRSet = new CubeResultSet( this, context, query,
						(ICubeQueryResults) dteResults );
			}
			else
			{
				pRsetId = resultSet.getQueryResults( ).getID( );
				rowId = resultSet.getRawID( );

				// this is the nest query, execute the query in the
				// parent results
				dteResults = dteSession.execute( pQuery, resultSet.getQueryResults( ), scope );
				curRSet = new CubeResultSet( this, context, resultSet, query,
						(ICubeQueryResults) dteResults );
			}

			// save the meta infomation
			storeDteMetaInfo( pRsetId, rowId, queryID, dteResults.getID( ) );
			
			// persist the queryResults witch need cached. 
			if ( query.cacheQueryResults( ) )
			{
				cachedQueryIdMap.put( query, dteResults.getID( ) );
			}
			
			return curRSet;
		}
		catch ( BirtException be )
		{
			logger.log( Level.SEVERE, be.getMessage( ) );
			context.addException( be );
		}

		return null;
	}

	public void shutdown( )
	{
		if ( null != dos )
		{
			try
			{
				dos.close( );
			}
			catch ( IOException e )
			{
			}
			dos = null;
		}
		dteSession.shutdown( );
	}

	/**
	 * save the metadata into the streams.
	 * 
	 * @param key
	 */
	private void storeDteMetaInfo( String pRsetId, String rowId,
			String queryId, String rsetId )
	{
		try
		{

			// save the meta infomation
			if ( context.isExecutingMasterPage( ) )
			{
				if ( pRsetId == null )
				{
					rowId = String.valueOf( context.getPageNumber( ) );
				}
			}
			DteMetaInfoIOUtil.storeMetaInfo( dos, pRsetId, rowId, queryId,
					rsetId );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
		}
	}
	
	

}