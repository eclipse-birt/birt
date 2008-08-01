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
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.ir.Report;

public class DataGenerationEngine extends DteDataEngine
{
	/**
	 * output stream used to save the resultset relations
	 */
	private DataOutputStream dos;

	public DataGenerationEngine( ExecutionContext context,
			IDocArchiveWriter writer ) throws Exception
	{
		super( context, writer );

		// create the DteData session.
		DataSessionContext dteSessionContext = new DataSessionContext(
				DataSessionContext.MODE_GENERATION, null, context
						.getScriptContext( ), context
						.getApplicationClassLoader( ) );
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
		queryIDMap.putAll( report.getQueryIDs( ) );
		super.doPrepareQuery( report, appContext );
	}

	protected IBaseResultSet doExecuteQuery( IBaseResultSet parentResultSet,
			IQueryDefinition query, boolean useCache ) throws BirtException
	{
		IBaseResultSet resultSet = super.doExecuteQuery( parentResultSet,
				query, useCache );
		if ( resultSet != null )
		{
			storeMetaInfo( parentResultSet, query, resultSet );
		}

		return resultSet;
	}

	protected IBaseResultSet doExecuteCube( IBaseResultSet parentResultSet,
			ICubeQueryDefinition query, boolean useCache ) throws BirtException
	{
		IBaseResultSet resultSet = super.doExecuteCube( parentResultSet, query,
				useCache );
		if ( resultSet != null )
		{
			storeMetaInfo( parentResultSet, query, resultSet );
		}

		return resultSet;
	}

	/**
	 * save the meta information
	 * 
	 * @param parentResultSet
	 * @param query
	 * @param resultSet
	 */
	protected void storeMetaInfo( IBaseResultSet parentResultSet,
			IDataQueryDefinition query, IBaseResultSet resultSet )
			throws BirtException
	{
		String pRsetId = null; // id of the parent query restuls
		String rowId = "-1"; // row id of the parent query results
		if ( parentResultSet != null )
		{
			if ( parentResultSet instanceof QueryResultSet )
			{
				pRsetId = ( (QueryResultSet) parentResultSet )
						.getQueryResultsID( );
			}
			else
			{
				pRsetId = ( (CubeResultSet) parentResultSet )
						.getQueryResultsID( );
			}
			rowId = parentResultSet.getRawID( );
		}
		String queryID = (String) queryIDMap.get( query );
		storeDteMetaInfo( pRsetId, rowId, queryID, resultSet.getQueryResults( )
				.getID( ) );
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
					rowId = "-1";
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