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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;

public class DataPresentationEngine extends AbstractDataEngine
{
	protected HashMap rsetRelations2 = new HashMap( );

	private boolean needAccessFactTable = false;

	public DataPresentationEngine( DataEngineFactory factory,
	        ExecutionContext context, IDocArchiveReader reader,
	        boolean needAccessFactTable )
			throws Exception
	{
		super( factory, context );
		// create the DteData session.
		DataSessionContext dteSessionContext = new DataSessionContext(
				DataSessionContext.MODE_PRESENTATION, context.getDesign( ), context
						.getScriptContext( ), context
						.getApplicationClassLoader( ) );
		dteSessionContext.setDocumentReader( reader );
		dteSessionContext.setAppContext( context.getAppContext( ) );
		DataEngineContext dteEngineContext = dteSessionContext
				.getDataEngineContext( );
		dteEngineContext.setLocale( context.getLocale( ) );
		dteEngineContext.setTimeZone( context.getTimeZone( ) );
		String tempDir = getTempDir( context );
		if ( tempDir != null )
		{
			dteEngineContext.setTmpdir( tempDir );
		}

		dteSession = context.newSession( dteSessionContext );
		
		// try to load the meta data informations
		loadDteMetaInfo( reader );
		this.needAccessFactTable = needAccessFactTable;
	}

	protected void doPrepareQuery( Report report, Map appContext )
	{
		// prepare report queries
		queryIDMap.putAll( report.getQueryIDs( ) );
	}

	private void loadDteMetaInfo( IDocArchiveReader reader ) throws IOException
	{
		ArrayList result = DteMetaInfoIOUtil.loadDteMetaInfo( reader );
		if ( result != null )
		{
			StringBuffer buffer = new StringBuffer( );
			for ( int i = 0; i < result.size( ); i++ )
			{
				String[] rsetRelation = (String[]) result.get( i );
				String pRsetId = rsetRelation[0];
				String rawId = rsetRelation[1];
				String queryId = rsetRelation[2];
				String rsetId = rsetRelation[3];
				String rowId = rsetRelation[4];
				addResultSetRelation( pRsetId, rawId, queryId, rsetId );
				buffer.setLength( 0 );
				buffer.append( pRsetId );
				buffer.append( "." );
				buffer.append( rowId );
				buffer.append( "." );
				buffer.append( queryId );
				rsetRelations2.put( buffer.toString( ), rsetId );
			}
		}
	}

	private StringBuffer keyBuffer = new StringBuffer( );

	public String getResultIDByRowID( String pRsetId, String rowId,
			String queryId )
	{
		keyBuffer.setLength( 0 );
		keyBuffer.append( pRsetId );
		keyBuffer.append( "." );
		keyBuffer.append( rowId );
		keyBuffer.append( "." );
		keyBuffer.append( queryId );
		// try to search the rset id
		String rsetId = (String) rsetRelations2.get( keyBuffer.toString( ) );		
		return rsetId;
	}

	protected IBaseResultSet doExecuteQuery( IBaseResultSet parentResult,
			IQueryDefinition query, Object queryOwner, boolean useCache ) throws BirtException
	{
		String queryID = (String) queryIDMap.get( query );

		String resultSetID = loadResultSetID( parentResult, queryID );
		if ( resultSetID == null )
		{
			throw new EngineException(
					MessageConstants.REPORT_QUERY_LOADING_ERROR );
			/*if ( queryOwner instanceof DesignElementHandle )
			{
				throw new EngineException( MessageConstants.REPORT_QUERY_LOADING_ERROR2,
						new Object[]{
								queryID,
								( (DesignElementHandle) queryOwner ).getID( )
						} );
			}
			else
			{
				throw new EngineException( MessageConstants.REPORT_QUERY_LOADING_ERROR,
						queryID );
			}*/
		}

		((QueryDefinition)query).setQueryResultsID( resultSetID );
		IPreparedQuery pq = dteSession.prepare( query );
		IBaseQueryResults queryResults = dteSession.execute( pq,
				parentResult == null ? null : parentResult.getQueryResults( ),
				context.getScriptContext( ) );
		//IBaseQueryResults queryResults = dteSession.getQueryResults( resultSetID );

		//FIXME: hchu, return the result set directly.
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
	
	protected IBaseResultSet doExecuteCube( IBaseResultSet parentResult,
			ICubeQueryDefinition query, Object queryOwner, boolean useCache ) throws BirtException
	{
		String queryID = (String) queryIDMap.get( query );

		String resultSetID = loadResultSetID( parentResult, queryID );
		IBaseQueryResults queryResults = null;
		query.setQueryResultsID( resultSetID );
		IBasePreparedQuery pQuery = dteSession.prepare( query,
				context.getAppContext( ) );

		query.setNeedAccessFactTable( needAccessFactTable );
		ScriptContext scriptContext = context.getScriptContext( );
		if ( parentResult != null )
		{
			queryResults = dteSession.execute( pQuery, parentResult
					.getQueryResults( ), scriptContext );
		}
		else
		{
			queryResults = dteSession.execute( pQuery, null, scriptContext );
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
			resultSet = new CubeResultSet( this, context, parentResult, query,
					(ICubeQueryResults) queryResults );
		}

		return resultSet;
	}
	
	private String loadResultSetID( IBaseResultSet parentResult,
			String queryID ) throws BirtException
	{
		String resultSetID = null;
		if ( parentResult == null )
		{
			// if the query is used in master page, the row id is set as page
			// number
			if ( context.isExecutingMasterPage( ) )
			{
				resultSetID = getResultID( null, "-1", queryID );
				if ( resultSetID == null )
				{
					long pageNumber = context.getPageNumber( );
					resultSetID = getResultID( null, String
							.valueOf( pageNumber ), queryID );
					if ( resultSetID == null )
					{
						// try to find the query defined in page 1
						resultSetID = getResultID( null, "1", queryID );
					}
				}
			}
			else
			{
				resultSetID = getResultID( null, "-1", queryID );
			}
		}
		else
		{
			//FIXME: test nest query of sub-query to see if we need get the base query result id.
			String pRsetId;
			if ( parentResult instanceof QueryResultSet )
			{
				pRsetId = ( (QueryResultSet) parentResult )
						.getQueryResultsID( );
			}
			else
			{
				pRsetId = ( (CubeResultSet) parentResult )
						.getQueryResultsID( );
			}
			String rowid = parentResult.getRawID( );
			resultSetID = getResultID( pRsetId, rowid, queryID );
		}
		return resultSetID;
	}
}