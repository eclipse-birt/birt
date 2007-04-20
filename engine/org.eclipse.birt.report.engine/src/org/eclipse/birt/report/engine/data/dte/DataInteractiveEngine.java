
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
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
import org.eclipse.birt.report.engine.ir.Report;
import org.mozilla.javascript.Scriptable;


/**
 * 
 */

public class DataInteractiveEngine extends AbstractDataEngine
{
	/**
	 * data is geting from this archive.
	 */
	private IDocArchiveReader reader;
		
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
	protected HashMap rsetRelations = new HashMap( );

	public DataInteractiveEngine( ExecutionContext context,
			IDocArchiveReader reader, IDocArchiveWriter writer )
	{
		super( context );
		try
		{	// create the DteData session.
			DataSessionContext dteSessionContext;
			if ( writer == null )
			{
				dteSessionContext = new DataSessionContext(
						DataSessionContext.MODE_PRESENTATION, null, context
								.getSharedScope( ) );
				dteSessionContext.setDocumentReader( reader );
			}
			else
			{
				dteSessionContext = new DataSessionContext(
						DataSessionContext.MODE_UPDATE, null, context
								.getSharedScope( ) );
				dteSessionContext.setDocumentReader( reader );
				dteSessionContext.setDocumentWriter( writer );
			}
			DataEngineContext dteEngineContext = dteSessionContext
					.getDataEngineContext( );
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
		this.reader = reader;
		
		try
		{
			if ( writer != null && dos == null )
			{
				dos = new DataOutputStream( writer.createRandomAccessStream( ReportDocumentConstants.DATA_SNAP_META_STREAM ) );
				//dos = new DataOutputStream( writer.createRandomAccessStream( ReportDocumentConstants.DATA_META_STREAM ) );
				IOUtil.writeString( dos, VERSION_1 );
			}
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
			e.printStackTrace( );
		}
	}
	
	
	/**
	 * save the metadata into the streams.
	 * 
	 * @param key
	 */
	private void storeDteMetaInfo( String pRsetId, String rowId, String queryId,
			String rsetId )
	{		
		
		if ( null != dos )
		{
			try
			{				
				IOUtil.writeString( dos, pRsetId );
				
				// top query in master page then set the page number as row id
				if ( pRsetId == null && context.isExecutingMasterPage( ) )
				{
					IOUtil.writeString( dos, String.valueOf( context.getPageNumber( ) ) );
				}
				else
				{
					IOUtil.writeString( dos, rowId );
				}
				IOUtil.writeString( dos, queryId );
				IOUtil.writeString( dos, rsetId );
			}
			catch ( IOException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ) );
				e.printStackTrace( );
			}
		}
	}
	
	private void loadDteMetaInfo()
	{
		loadDteMetaInfo( ReportDocumentConstants.DATA_META_STREAM );
		if ( reader.exists( ReportDocumentConstants.DATA_SNAP_META_STREAM ) )
		{
			loadDteMetaInfo( ReportDocumentConstants.DATA_SNAP_META_STREAM );
		}
	}
	
	private void loadDteMetaInfo( String metaDataStream)
	{
		DataInputStream dis = null;
		try
		{
			dis = new DataInputStream( reader.getStream( metaDataStream) );

			ArrayList result = super.loadDteMetaInfo( dis );
			if ( result != null )
			{
				StringBuffer buffer = new StringBuffer( );
				for ( int i = 0; i < result.size( ); i++ )
				{
					String[] rsetRelation = (String[])result.get( i );
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
		catch ( EOFException eofe )
		{
			// we expect that there should be an EOFexception
		}
		catch ( IOException ioe )
		{
			context.addException( new EngineException(
					"Can't load the data in report document", ioe ) );
			logger.log( Level.SEVERE, ioe.getMessage( ), ioe );
		}
		finally
		{
			if ( dis != null )
			{
				try
				{
					dis.close( );
				}
				catch ( IOException ex )
				{

				}
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

	protected void doPrepareQuery( Report report, Map appContext )
	{
		// prepare report queries
		queryIDMap.putAll( report.getQueryIDs( ) );
		loadDteMetaInfo( );
	}
	
	protected IBaseResultSet doExecuteQuery( IBaseResultSet resultSet, IDataQueryDefinition query )
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
	
	protected IBaseResultSet doExecuteQuery( IBaseResultSet parentResult,
			IQueryDefinition query )
	{
		String queryID = (String) queryIDMap.get( query );
		try
		{
			IBaseQueryResults parentQueryResults = null;
			if ( parentResult != null )
			{
				parentQueryResults = parentResult.getQueryResults( );
			}
			
			String resultSetID = loadResultSetID( parentResult, parentQueryResults, queryID );		
			if ( resultSetID == null )
			{
				logger.log( Level.SEVERE, "Can't load the report query" );
				return null;
			}

			// Interactive do not support CUBE?
			((QueryDefinition)query).setQueryResultsID( resultSetID );
			IBasePreparedQuery pQuery = dteSession.prepare( query, null );
			
			Scriptable scope = context.getSharedScope( );

			String pRsetId = null; // id of the parent query restuls
			String rowId = "-1"; // row id of the parent query results
			IBaseQueryResults dteResults; // the dteResults of this query
			QueryResultSet resultSet = null;
			
			if ( parentQueryResults == null )
			{
				// this is the root query
				dteResults = dteSession.execute( pQuery, null, scope );
				resultSet = new QueryResultSet( this, context,
							query,
							(IQueryResults) dteResults );
			}
			else
			{
				pRsetId = parentResult.getQueryResults( ).getID( );
				rowId = parentResult.getRawID( );
				
				// this is the nest query, execute the query in the
				// parent results
				dteResults = dteSession.execute( pQuery, parentQueryResults, scope );
				resultSet = new QueryResultSet( this, context, parentResult,
							(IQueryDefinition) query,
							(IQueryResults) dteResults );
				
			}
			// see DteResultSet
			resultSet.setBaseRSetID( resultSetID );
			
			storeDteMetaInfo( pRsetId, rowId, queryID, dteResults.getID( ) );
			
			return resultSet;
		}
		catch ( BirtException be )
		{
			logger.log( Level.SEVERE, be.getMessage( ) );
			context.addException( be );
			return null;
		}
	}
	
	protected IBaseResultSet doExecuteCube( IBaseResultSet parentResult,
			ICubeQueryDefinition query )
	{
		String queryID = (String) queryIDMap.get( query );
		try
		{
			IBaseQueryResults parentQueryResults = null;
			if ( parentResult != null )
			{
				parentQueryResults = parentResult.getQueryResults( );
			}
			
			String resultSetID = loadResultSetID( parentResult, parentQueryResults, queryID );		
			if ( resultSetID == null )
			{
				logger.log( Level.SEVERE, "Can't load the report query" );
				return null;
			}

			// Interactive do not support CUBE?
			((QueryDefinition)query).setQueryResultsID( resultSetID );
			IBasePreparedQuery pQuery = dteSession.prepare( query, null );
			
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
				resultSet = new CubeResultSet( this, context, resultSet, query,
						(ICubeQueryResults) dteResults );
			}
			// FIXME: 
			// resultSet.setBaseRSetID( resultSetID );
			
			storeDteMetaInfo( pRsetId, rowId, queryID, dteResults.getID( ) );
			
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
			IBaseQueryResults queryResults, String queryID )
	{
		String resultSetID = null;
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
			try
			{
				String pRsetId;
				if ( parentResult.getType( ) == IBaseResultSet.QUERY_RESULTSET )
				{
					pRsetId = ( (QueryResultSet) parentResult ).getBaseRSetID( );
				}
				else
				{
					pRsetId = queryResults.getID( );
				}
				String rowid = parentResult.getRawID( );
				resultSetID = getResultID( pRsetId, rowid, queryID );
			}
			catch ( BirtException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
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
