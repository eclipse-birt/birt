
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
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
	
	private IDocArchiveWriter writer;
	
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
		{
			// create the DteData engine.
			DataEngineContext dteContext;
			if ( writer == null)
			{
				dteContext = DataEngineContext.newInstance(
					DataEngineContext.MODE_PRESENTATION, context.getSharedScope( ),
					reader, null );
			}
			else
			{
				dteContext = DataEngineContext.newInstance(
						DataEngineContext.MODE_UPDATE, context.getSharedScope( ),
						reader, writer );
			}
		
			dteEngine = DataEngine.newDataEngine( dteContext );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "can't create the DTE data engine", ex );
			ex.printStackTrace( );
		}
		this.reader = reader;
		this.writer = writer;
	}
	
	
	/**
	 * save the metadata into the streams.
	 * 
	 * @param key
	 */
	private void storeDteMetaInfo( String pRsetId, long rowId, String queryId,
			String rsetId )
	{
		if ( writer == null ){
			return;
		}
		try
		{
			if ( dos == null )
			{
				dos = new DataOutputStream( writer.createRandomAccessStream( ReportDocumentConstants.DATA_SNAP_META_STREAM ) );
				//dos = new DataOutputStream( writer.createRandomAccessStream( ReportDocumentConstants.DATA_META_STREAM ) );
			}
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
			e.printStackTrace( );
		}
		
		if ( null != dos )
		{
			try
			{
				IOUtil.writeString( dos, pRsetId );
				IOUtil.writeLong( dos, rowId );
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

			StringBuffer buffer = new StringBuffer( );
			while ( true )
			{
				String pRsetId = IOUtil.readString( dis );
				long rowId = IOUtil.readLong( dis );
				String queryId = IOUtil.readString( dis );
				String rsetId = IOUtil.readString( dis );

				buffer.setLength( 0 );
				buffer.append( pRsetId );
				buffer.append( "." );
				buffer.append( rowId );
				buffer.append( "." );
				buffer.append( queryId );

				rsetRelations.put( buffer.toString( ), rsetId );
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

	protected String getResultID( String pRsetId, long rowId, String queryId )
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
	
	protected IResultSet doExecuteQuery( DteResultSet parentResult,
			IQueryDefinition query )
	{
		String queryID = (String) queryIDMap.get( query );
		try
		{
			IQueryResults parentQueryResults = null;
			if ( parentResult != null )
			{
				parentQueryResults = parentResult.getQueryResults( );
			}

			String resultSetID = null;
			if ( parentQueryResults == null )
			{
				resultSetID = getResultID( null, -1, queryID );
			}
			else
			{
				String pRsetId = parentResult.getBaseRSetID( );
				long rowid = parentResult.getRawID( );

				resultSetID = getResultID( pRsetId, rowid, queryID );
			}
			
			if ( resultSetID == null )
			{
				logger.log( Level.SEVERE, "Can't load the report query" );
				return null;
			}

			//((QueryDefinition)query).setDataSetName( null );
			((QueryDefinition)query).setQueryResultsID( resultSetID );
			IPreparedQuery pQuery = dteEngine.prepare( query );
			
			Scriptable scope = context.getSharedScope( );

			String pRsetId = null; // id of the parent query restuls
			long rowId = -1; // row id of the parent query results
			IQueryResults dteResults; // the dteResults of this query
			DteResultSet resultSet = null;
			
			if ( parentQueryResults == null )
			{
				// this is the root query
				dteResults = pQuery.execute( scope );
				resultSet = new DteResultSet( this, context, query, dteResults );
			}
			else
			{
				pRsetId = parentResult.getQueryResults( ).getID( );
				rowId = parentResult.getRawID( );
				
				// this is the nest query, execute the query in the
				// parent results
				dteResults = pQuery.execute( parentQueryResults, scope );
				resultSet = new DteResultSet( parentResult, query, dteResults );
			}
			resultSet.setBaseRSetID( resultSetID );
			
//			 save the
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
		dteEngine.shutdown( );
	}
}
