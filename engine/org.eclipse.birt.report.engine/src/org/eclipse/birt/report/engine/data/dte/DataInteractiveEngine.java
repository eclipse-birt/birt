
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
package org.eclipse.birt.report.engine.data.dte;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.mozilla.javascript.Scriptable;


/**
 * 
 */

public class DataInteractiveEngine extends AbstractDataEngine
{
	/**
	 * output stream used to save the resultset relations
	 */
	private DataOutputStream dos;
	
	/**
	 * store relations of various query ResultSet. Such as relations between
	 * parent ResultSet and nested query ResultSet.
	 * 
	 * The user use
	 * 
	 * ParentResultId.rowId.queryName to access the result set id.
	 */
	protected HashMap<String, Map<String, List<ResultSetID>>> rsetRelations = new HashMap<String, Map<String, List<ResultSetID>>>( );

	private static class ResultSetID{
		int rowId;
		String resultSetId;
		public ResultSetID(int rowId, String resultSetId )
		{
			this.rowId = rowId;
			this.resultSetId = resultSetId;
		}
	}
	
	public DataInteractiveEngine( ExecutionContext context,
			IDocArchiveReader reader, IDocArchiveWriter writer )
			throws Exception
	{
		super( context );
		// create the DteData session.
		DataSessionContext dteSessionContext = new DataSessionContext(
				DataSessionContext.MODE_UPDATE, null, context.getSharedScope( ) );
		dteSessionContext.setDocumentReader( reader );
		dteSessionContext.setDocumentWriter( writer );
		DataEngineContext dteEngineContext = dteSessionContext
				.getDataEngineContext( );
		dteEngineContext.setLocale( context.getLocale( ) );
		dteEngineContext.setClassLoader( context.getApplicationClassLoader( ) );

		String tempDir = getTempDir( context );
		if ( tempDir != null )
		{
			dteEngineContext.setTmpdir( tempDir );
		}

		dteSession = DataRequestSession.newSession( dteSessionContext );

		loadDteMetaInfo( reader );

		if ( writer != null && dos == null )
		{
			dos = new DataOutputStream(
					writer
							.createRandomAccessStream( ReportDocumentConstants.DATA_SNAP_META_STREAM ) );
			// dos = new DataOutputStream( writer.createRandomAccessStream(
			// ReportDocumentConstants.DATA_META_STREAM ) );
			DteMetaInfoIOUtil.startMetaInfo( dos );
		}
	}
	
	
	/**
	 * save the metadata into the streams.
	 * 
	 * @param key
	 */
	private void storeDteMetaInfo( String pRsetId, String rowId,
			String queryId, String rsetId )
	{
		if ( dos != null )
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
	
	private void loadDteMetaInfo( IDocArchiveReader reader ) throws IOException
	{
		ArrayList result = DteMetaInfoIOUtil.loadAllDteMetaInfo( reader);
		if ( result != null )
		{
			for ( int i = 0; i < result.size( ); i++ )
			{
				String[] rsetRelation = (String[]) result.get( i );
				String pRsetId = rsetRelation[0];
				String rowId = rsetRelation[1];
				String queryId = rsetRelation[2];
				String rsetId = rsetRelation[3];
				addResultSetRelation( pRsetId, rowId, queryId, rsetId );
			}
		}
	}

	private void addResultSetRelation( String pRsetId, String rowId,
			String queryId, String rsetId )
	{
		int intRowId = Integer.parseInt( rowId );
		Map<String, List<ResultSetID>> queryMap = rsetRelations.get( pRsetId );
		ResultSetID resultsetId = new ResultSetID(intRowId, rsetId);
		if ( queryMap == null )
		{
			queryMap = new HashMap<String, List<ResultSetID>>( );
			rsetRelations.put( pRsetId, queryMap );
		}
		List<ResultSetID> rowIds = queryMap.get( queryId );
		if ( rowIds == null )
		{
			rowIds = new ArrayList<ResultSetID>( );
			queryMap.put( queryId, rowIds );
			rowIds.add( resultsetId);
			return;
		}
		int index = search(rowIds, intRowId);
		rowIds.add( index + 1, resultsetId);
	}

	/**
	 * Searches index of the result set id which has the max row id less than or equals
	 * to <code>rowId</code> in the list. If <code>rowId</code> is less than
	 * the row IDs of all the result set id in the list, <code>-1</code> is
	 * returned.
	 * 
	 * @param resultSetIds
	 *            list of result set ids
	 * @param rowId
	 *            row id.
	 * @return index of result set id which has the max row id less than or equals to
	 *         <code>rowId</code> or <code>-1</code> if <code>rowId</code>the
	 *         row IDs of all the result set id in the list
	 */
	private int search( List<ResultSetID> resultSetIds, int rowId )
	{
		int size = resultSetIds.size( );
		if ( size == 0 )
		{
			return -1;
		}
		int last = size - 1;
		ResultSetID startId = resultSetIds.get( 0 );
		ResultSetID endId = resultSetIds.get( last );
		if( startId.rowId == rowId )
		{
			return 0;
		}
		if ( rowId < startId.rowId)
		{
			return -1;
		}
		if ( rowId >= endId.rowId )
		{
			return last;
		}
		return search( resultSetIds, rowId, 0, last);
	}

	private int search( List<ResultSetID> rowIds, int rowId,
			int startIndex, int endIndex )
	{
		int count = endIndex - startIndex;
		if ( count == 0)
		{
			return startIndex;
		}
		int smallSeparator = startIndex + count / 2;
		int bigSeparator = smallSeparator + 1;
		ResultSetID smallId = rowIds.get( smallSeparator );
		ResultSetID bigId = rowIds.get( bigSeparator );
		if ( rowId < smallId.rowId )
		{
			return search(rowIds, rowId, startIndex, smallSeparator );
		}
		if( rowId > bigId.rowId )
		{
			return search(rowIds, rowId, bigSeparator, endIndex );
		}
		if( rowId == bigId.rowId )
		{
			return bigSeparator;
		}
		return smallSeparator;
	}

	protected String getResultID( String pRsetId, String rowId, String queryId )
	{
		int intRowId = Integer.parseInt( rowId );
		String resultSetId = findResultSetId( pRsetId, intRowId, queryId );
		if ( resultSetId == null )
		{
			if ( pRsetId != null )
			{
				int charAt = pRsetId.indexOf( "_" );
				if ( charAt != -1 )
				{
					String rootId = pRsetId.substring( 0, charAt );
					resultSetId = findResultSetId( rootId, intRowId, queryId );
				}
			}
		}
		return resultSetId;
	}

	private String findResultSetId( String pRsetId, int rowId, String queryId )
	{
		Map<String, List<ResultSetID>> queryMap = rsetRelations.get( pRsetId );
		if ( queryMap == null )
		{
			return null;
		}
		List<ResultSetID> rowIds = queryMap.get( queryId );
		if ( rowIds == null )
		{
			return null;
		}
		return searchResultSetId( rowId, rowIds );
	}

	private String searchResultSetId( int rowId, List<ResultSetID> rowIds )
	{
		int index = search( rowIds, rowId);
		if ( index < 0 )
		{
			return null;
		}
		return rowIds.get( index ).resultSetId;
	}

	protected void doPrepareQuery( Report report, Map appContext )
	{
		this.appContext = appContext;
		// prepare report queries
		queryIDMap.putAll( report.getQueryIDs( ) );
	}
	
	protected IBaseResultSet doExecuteQuery( IBaseResultSet parentResult,
			IQueryDefinition query, boolean useCache ) throws BirtException
	{
		String queryID = (String) queryIDMap.get( query );

		IBaseQueryResults parentQueryResults = null;
		if ( parentResult != null )
		{
			parentQueryResults = parentResult.getQueryResults( );
		}

		String resultSetID = loadResultSetID( parentResult, queryID );		
		if ( resultSetID == null )
		{
			throw new EngineException(MessageConstants.REPORT_QUERY_LOADING_ERROR , query.getClass( ).getName( ) );
		}

		// Interactive do not support CUBE?
		((QueryDefinition)query).setQueryResultsID( resultSetID );
		IBasePreparedQuery pQuery = dteSession.prepare( query, null );

		Scriptable scope = context.getSharedScope( );

		String pRsetId = null; // id of the parent query restuls
		String rowId = "-1"; // row id of the parent query results
		IBaseQueryResults dteResults = null; // the dteResults of this query
		QueryResultSet resultSet = null;

		if ( parentQueryResults == null )
		{
			// this is the root query
			if ( useCache )
			{
				dteResults = getCachedQueryResult( query, parentResult );
			}
			if ( dteResults == null )
			{
				dteResults = dteSession.execute( pQuery, null, scope );
				putCachedQueryResult( query, dteResults.getID( ) );
			}
			resultSet = new QueryResultSet( this, context,
					query,
					(IQueryResults) dteResults );
		}
		else
		{
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
			rowId = parentResult.getRawID( );

			// this is the nest query, execute the query in the
			// parent results
			if ( useCache )
			{
				dteResults = getCachedQueryResult( query, parentResult );
			}
			if ( dteResults == null )
			{
				dteResults = dteSession.execute( pQuery, parentQueryResults, scope );
				putCachedQueryResult( query, dteResults.getID( ) );
			}
			resultSet = new QueryResultSet( this, context, parentResult,
					(IQueryDefinition) query,
					(IQueryResults) dteResults );

		}
		// see DteResultSet
		resultSet.setBaseRSetID( resultSetID );

		storeDteMetaInfo( pRsetId, rowId, queryID, dteResults.getID( ) );

		return resultSet;
	}
	
	protected IBaseResultSet doExecuteCube( IBaseResultSet parentResult,
			ICubeQueryDefinition query, boolean useCache ) throws BirtException
	{
		String queryID = (String) queryIDMap.get( query );

		IBaseQueryResults parentQueryResults = null;
		if ( parentResult != null )
		{
			parentQueryResults = parentResult.getQueryResults( );
		}

		String resultSetID = loadResultSetID( parentResult, queryID );
		if ( resultSetID == null )
		{
			throw new EngineException(MessageConstants.REPORT_QUERY_LOADING_ERROR , queryID);
		}

		if ( useCache )
		{
			String rsetId = String.valueOf( cachedQueryToResults.get( query ) );
			query.setQueryResultsID( rsetId );
		}
		else
		{
			query.setQueryResultsID( null );
		}

		// Interactive do not support CUBE?
		query.setQueryResultsID( resultSetID );
		IBasePreparedQuery pQuery = dteSession.prepare( query, appContext );

		Scriptable scope = context.getSharedScope( );

		String pRsetId = null; // id of the parent query restuls
		String rowId = "-1"; // row id of the parent query results
		IBaseQueryResults dteResults; // the dteResults of this query
		CubeResultSet resultSet = null;

		if ( parentQueryResults == null )
		{
			// this is the root query
			dteResults = dteSession.execute( pQuery, null, scope );
			resultSet = new CubeResultSet( this, context, query,
					(ICubeQueryResults) dteResults );
		}
		else
		{
			pRsetId = parentResult.getQueryResults( ).getID( );
			rowId = parentResult.getRawID( );

			// this is the nest query, execute the query in the
			// parent results
			dteResults = dteSession.execute( pQuery, parentQueryResults,
					scope );
			resultSet = new CubeResultSet( this, context, parentResult, query,
					(ICubeQueryResults) dteResults );
		}
		// FIXME:
		// resultSet.setBaseRSetID( resultSetID );

		storeDteMetaInfo( pRsetId, rowId, queryID, dteResults.getID( ) );

		// persist the queryResults witch need cached.
		if ( query.cacheQueryResults( ) )
		{
			cachedQueryToResults.put( query, dteResults.getID( ) );
		}

		return resultSet;
	}
	
	private String loadResultSetID( IBaseResultSet parentResult, String queryID )
			throws BirtException
	{
		String resultSetID = null;
		if ( parentResult == null )
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
					resultSetID = getResultID( null, "-1", queryID );
				}
			}
			else
			{
				resultSetID = getResultID( null, "-1", queryID );
			}
		}
		else
		{
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
}
