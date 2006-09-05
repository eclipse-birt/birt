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
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
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

	public DataGenerationEngine( ExecutionContext context, IDocArchiveWriter writer )
	{
		super( context );

		try
		{
			// create the DteData engine.
			DataEngineContext dteContext = DataEngineContext.newInstance( DataEngineContext.MODE_GENERATION,
					context.getSharedScope( ),
					null,
					writer );
			dteContext.setLocale( context.getLocale( ) );

			dteEngine = DataEngine.newDataEngine( dteContext );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "can't create the DTE data engine", ex );
			ex.printStackTrace( );
		}

		try
		{
			dos = new DataOutputStream( writer
					.createRandomAccessStream( ReportDocumentConstants.DATA_META_STREAM ) );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
			e.printStackTrace( );
		}
	}

	protected void doPrepareQuery( Report report, Map appContext )
	{
		// prepare report queries
		queryIDMap.putAll( report.getQueryIDs( ) );
		for ( int i = 0; i < report.getQueries( ).size( ); i++ )
		{
			IQueryDefinition queryDef = (IQueryDefinition) report.getQueries( )
					.get( i );
			try
			{
				IPreparedQuery preparedQuery = dteEngine.prepare( queryDef,
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

	protected IResultSet doExecuteQuery( DteResultSet resultSet, IQueryDefinition query )
	{
		assert query instanceof IQueryDefinition;

		IPreparedQuery pQuery = (IPreparedQuery) queryMap.get( query );
		if ( pQuery == null )
		{
			return null;
		}

		try
		{
			String queryID = (String) queryIDMap.get( query );
			Scriptable scope = context.getSharedScope( );

			String pRsetId = null; // id of the parent query restuls
			long rowId = -1; // row id of the parent query results
			IQueryResults dteResults; // the dteResults of this query
			if ( resultSet == null )
			{
				// this is the root query
				dteResults = pQuery.execute( scope );
				resultSet = new DteResultSet( this, context, query, dteResults );
			}
			else
			{
				pRsetId = resultSet.getQueryResults( ).getID( );
				rowId = resultSet.getRawID( );

				// this is the nest query, execute the query in the
				// parent results
				dteResults = pQuery.execute( resultSet.getQueryResults( ),
						scope );
				resultSet = new DteResultSet( resultSet, query, dteResults );
			}

			// save the
			storeDteMetaInfo( pRsetId, rowId, queryID, dteResults.getID( ) );

			return resultSet;
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
		dteEngine.shutdown( );
	}

	/**
	 * save the metadata into the streams.
	 * 
	 * @param key
	 */
	private void storeDteMetaInfo( String pRsetId, long rowId, String queryId,
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
					IOUtil.writeLong( dos, context.getPageNumber( ) );
				}
				else
				{
					IOUtil.writeLong( dos, rowId );
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
}