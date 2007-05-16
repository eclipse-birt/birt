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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveReader;
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
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.ir.Report;

public class DataPresentationEngine extends AbstractDataEngine
{

	private IDocArchiveReader reader;

	/*
	 * store relations of various query ResultSet. Such as relations between
	 * parent ResultSet and nested query ResultSet.
	 * 
	 * The user use
	 * 
	 * ParentResultId.rowId.queryName to access the result set id.
	 */
	protected HashMap rsetRelations = new HashMap( );

	public DataPresentationEngine( ExecutionContext context,
			IDocArchiveReader reader ) throws Exception
	{
		super( context );
		// create the DteData session.
		DataSessionContext dteSessionContext = new DataSessionContext(
				DataSessionContext.MODE_PRESENTATION, null, context
						.getSharedScope( ) );
		dteSessionContext.setDocumentReader( reader );
		DataEngineContext dteEngineContext = dteSessionContext
				.getDataEngineContext( );
		dteEngineContext.setLocale( context.getLocale( ) );

		String tempDir = getTempDir( context );
		if ( tempDir != null )
		{
			dteEngineContext.setTmpdir( tempDir );
		}

		dteSession = DataRequestSession.newSession( dteSessionContext );

		this.reader = reader;
		
		// try to load the meta data informations
		loadDteMetaInfo( );
	}

	/**
	 * prepare the queries defined in the report.
	 */
	public void prepare( Report report, Map appContext )
	{
		// build report queries
		new ReportQueryBuilder( report, context ).build( );

		doPrepareQuery( report, appContext );
	}
	
	protected void doPrepareQuery( Report report, Map appContext )
	{
		// prepare report queries
		queryIDMap.putAll( report.getQueryIDs( ) );
	}

	private void loadDteMetaInfo( ) throws IOException
	{
		ArrayList result = DteMetaInfoIOUtil.loadDteMetaInfo( reader );
		if ( result != null )
		{
			StringBuffer buffer = new StringBuffer( );
			for ( int i = 0; i < result.size( ); i++ )
			{
				String[] rsetRelation = (String[]) result.get( i );
				String pRsetId = rsetRelation[0];
				String rowId = rsetRelation[1];
				String queryId = rsetRelation[2];
				String rsetId = rsetRelation[3];
				buffer.setLength( 0 );
				buffer.append( pRsetId );
				buffer.append( "." );
				buffer.append( rowId );
				buffer.append( "." );
				buffer.append( queryId );
				rsetRelations.put( buffer.toString( ), rsetId );
			}
		}
	}

	private StringBuffer keyBuffer = new StringBuffer( );

	protected String getResultID( String pRsetId, String rowId, String queryId )
	{
		keyBuffer.setLength( 0 );
		keyBuffer.append( pRsetId );
		keyBuffer.append( "." );
		keyBuffer.append( rowId );
		keyBuffer.append( "." );
		keyBuffer.append( queryId );
		// try to search the ret
		String rsetId = (String) rsetRelations.get( keyBuffer.toString( ) );
		if ( rsetId == null )
		{
			if ( pRsetId != null )
			{
				int charAt = pRsetId.indexOf( "_" );
				if ( charAt != -1 )
				{
					String rootId = pRsetId.substring( 0, charAt );
					keyBuffer.setLength( 0 );
					keyBuffer.append( rootId );
					keyBuffer.append( "." );
					keyBuffer.append( rowId );
					keyBuffer.append( "." );
					keyBuffer.append( queryId );
					rsetId = (String) rsetRelations.get( keyBuffer.toString( ) );
				}
			}
		}
		return rsetId;
	}
	
	protected IBaseResultSet doExecuteQuery( IBaseResultSet resultSet,
			IDataQueryDefinition query, boolean useCache )
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

	protected IBaseResultSet doExecuteQuery( IBaseResultSet parentResult, IQueryDefinition query )
	{
		String queryID = (String) queryIDMap.get( query );

		try
		{
			String resultSetID = loadResultSetID( parentResult, queryID );
			if ( resultSetID == null )
			{
				logger.log( Level.SEVERE, "Can't load the report query" );
				return null;
			}

			IBaseQueryResults queryResults = dteSession.getQueryResults( resultSetID );

			QueryResultSet resultSet = null;
			if ( parentResult == null )
			{
				// this is the root query
				resultSet = new QueryResultSet( this, context,
							query,
							(IQueryResults) queryResults );
			}
			else
			{
				// this is the nest query
				resultSet = new QueryResultSet( this, context, parentResult,
							query,
							(IQueryResults) queryResults );
			}
						
			return resultSet;
		}
		catch ( BirtException be )
		{
			logger.log( Level.SEVERE, be.getMessage( ) );
			context.addException( be );
			return null;
		}
	}
	
	protected IBaseResultSet doExecuteCube( IBaseResultSet parentResult, ICubeQueryDefinition query )
	{
		String queryID = (String) queryIDMap.get( query );

		try
		{
			String resultSetID = loadResultSetID( parentResult, queryID );
			IBaseQueryResults queryResults = null;
			query.setQueryResultsID( resultSetID );
			IBasePreparedQuery pQuery = dteSession.prepare( query,
					context.getAppContext( ) );
			if ( parentResult != null )
			{
				queryResults = dteSession.execute( pQuery,
						parentResult.getQueryResults( ),
						context.getSharedScope( ) );
			}
			else
			{
				queryResults = dteSession.execute( pQuery,
						null,
						context.getSharedScope( ) );
			}

			
			CubeResultSet resultSet = null;
			if ( parentResult == null )
			{
				// this is the root query
				resultSet = new CubeResultSet( this, context, query,
						(ICubeQueryResults) queryResults );
			}
			else
			{
				// this is the nest query
				resultSet = new CubeResultSet( this, context, resultSet, query,
						(ICubeQueryResults) queryResults );
			}
						
			return resultSet;
		}
		catch ( BirtException be )
		{
			logger.log( Level.SEVERE, be.getMessage( ) );
			context.addException( be );
			return null;
		}
	}
	
	private String loadResultSetID( IBaseResultSet parentResult,
			String queryID )
	{
		String resultSetID = null;
		IBaseQueryResults queryResults = null;
		if ( parentResult != null )
		{
			queryResults = parentResult.getQueryResults( );
		}
		if ( queryResults == null )
		{
			// if the query is used in master page, the row id is set as page
			// number
			if ( context.isExecutingMasterPage( ) )
			{
				long pageNumber = context.getPageNumber( );
				resultSetID = getResultID( null, String.valueOf( pageNumber ),
						queryID );
				if ( resultSetID == null )
				{
					// try to find the query defined in page 1
					resultSetID = getResultID( null, "1", queryID );
				}
			}
			else
			{
				resultSetID = getResultID( null, "-1", queryID );
			}
		}
		else
		{
			String pRsetId = queryResults.getID( );
			String rowid;
			try
			{
				rowid = parentResult.getRawID( );
				resultSetID = getResultID( pRsetId, rowid, queryID );
			}
			catch ( BirtException e )
			{
				context.addException( e );
			}

		}
		return resultSetID;
	}
}