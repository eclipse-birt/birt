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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
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
			IDocArchiveReader reader )
	{
		super( context );
		try
		{
			// create the DteData engine.
			DataEngineContext dteContext = DataEngineContext.newInstance( DataEngineContext.MODE_PRESENTATION,
					context.getSharedScope( ),
					reader,
					null );
			dteContext.setLocale( context.getLocale( ) );
			
			dteEngine = DataEngine.newDataEngine( dteContext );

		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "can't create the DTE data engine", ex );
			ex.printStackTrace( );
		}
		this.reader = reader;
	}

	protected void doPrepareQuery( Report report, Map appContext )
	{
		// prepare report queries
		queryIDMap.putAll( report.getQueryIDs( ) );
		loadDteMetaInfo( );
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

	protected IResultSet doExecuteQuery( DteResultSet parentResult, IQueryDefinition query )
	{
		String queryID = (String) queryIDMap.get( query );

		try
		{
			IQueryResults queryResults = null;
			if ( parentResult != null )
			{
				queryResults = parentResult.getQueryResults( );
			}

			String resultSetID = null;

			if ( queryResults == null )
			{
				// if the query is used in master page, the row id is set as page number
				if ( context.isExecutingMasterPage( ) )
				{
					long pageNumber = context.getPageNumber( );
					resultSetID = getResultID( null, pageNumber, queryID );
					if ( resultSetID == null )
					{
						resultSetID = getResultID( null, -1, queryID );
					}
				}
				else
				{
					resultSetID = getResultID( null, -1, queryID );
				}
			}
			else
			{
				String pRsetId = queryResults.getID( );
				long rowid = parentResult.getRawID( );

				resultSetID = getResultID( pRsetId, rowid, queryID );
			}
			if ( resultSetID == null )
			{
				logger.log( Level.SEVERE, "Can't load the report query" );
				return null;
			}

			queryResults = dteEngine.getQueryResults( resultSetID );

			DteResultSet resultSet = null;
			if ( parentResult == null )
			{
				// this is the root query
				resultSet = new DteResultSet( this, context, query, queryResults );
			}
			else
			{
				// this is the nest query
				resultSet = new DteResultSet( parentResult, query, queryResults );
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
}