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

package org.eclipse.birt.report.engine.api.impl;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.dte.DteDataEngine;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class DataExtractionTask extends EngineTask
		implements
			IDataExtractionTask
{

	/**
	 * report document contains the data
	 */
	protected IReportDocument reportDocReader;

	/**
	 * report design in the report document.
	 */
	protected Report report;

	/**
	 * selected instance id
	 */
	protected InstanceID instanceId;
	/**
	 * selected rest set
	 */
	protected String resultSetName;

	/**
	 * selected columns
	 */
	protected String[] selectedColumns;

	/**
	 * current extaction results
	 */
	protected IExtractionResults currentResult = null;

	/**
	 * have the metadata be prepared. meta data means rsetName2IdMapping and
	 * queryId2NameMapping
	 */
	protected boolean isMetaDataPrepared = false;

	/**
	 * mapping, map the rest name to rset id.
	 */
	protected HashMap rsetName2IdMapping = new HashMap( );

	/**
	 * mapping, map the query Id to query name.
	 */
	protected HashMap queryId2NameMapping = new HashMap( );

	protected HashMap queryId2QueryMapping = new HashMap( );

	/**
	 * list contains all the resultsets each entry is a
	 */
	protected ArrayList resultMetaList = new ArrayList( );
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger( DteDataEngine.class
			.getName( ) );

	public DataExtractionTask( IReportEngine engine, IReportRunnable runnable,
			IReportDocument reader ) throws EngineException
	{
		super( engine, runnable );

		// load the report
		this.reportDocReader = reader;
		executionContext.setReportDocument( reportDocReader );
		executionContext.setFactoryMode( false );
		executionContext.setPresentationMode( true );

		ReportDesignHandle reportHandle = (ReportDesignHandle) runnable
				.getDesignHandle( );
		report = new ReportParser( executionContext ).parse( reportHandle );

		executionContext.setReport( report );

		IDataEngine dataEngine = executionContext.getDataEngine( );
		dataEngine.prepare( report, appContext );
	}

	/*
	 * prepare the meta data of DataExtractionTask.
	 */
	private void prepareMetaData( )
	{
		if ( isMetaDataPrepared == true )
			return;

		HashMap queryIds = report.getQueryIDs( );
		HashMap query2itemMapping = report.getReportItemToQueryMap( );
		Iterator iter = queryIds.entrySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iter.next( );
			IQueryDefinition query = (IQueryDefinition) entry.getKey( );
			String queryId = (String) entry.getValue( );
			ReportItemDesign item = (ReportItemDesign) query2itemMapping
					.get( query );
			String queryName = item.getName( );
			if ( queryName == null )
			{
				queryName = "ELEMENT_" + item.getID( );
			}
			queryId2NameMapping.put( queryId, queryName );
			queryId2QueryMapping.put( queryId, query );;

		}

		try
		{
			loadResultSetMetaData( );
		}
		catch ( EngineException e )
		{
			e.printStackTrace( );
		}

		isMetaDataPrepared = true;
	}

	/**
	 * get the query name through query id.
	 * 
	 * @param queryId
	 * @return query name
	 */
	private String getQueryName( String queryId )
	{
		return (String) queryId2NameMapping.get( queryId );
	}

	/**
	 * get the query defintion from the query id
	 * 
	 * @param queryId
	 * @return
	 */
	private IQueryDefinition getQuery( String queryId )
	{
		return (IQueryDefinition) queryId2QueryMapping.get( queryId );
	}

	/**
	 * load map from query id to result set id from report document.
	 */
	private void loadResultSetMetaData( ) throws EngineException
	{
		DataInputStream dis = null;
		try
		{
			HashMap query2ResultMetaData = report.getResultMetaData( );
			IDocArchiveReader reader = reportDocReader.getArchive( );
			dis = new DataInputStream( reader
					.getStream( ReportDocumentConstants.DATA_META_STREAM ) );

			HashMap queryCounts = new HashMap( );
			while ( true )
			{
				// skip the parent restset id
				IOUtil.readString( dis );
				// skip the row id
				IOUtil.readLong( dis );
				// this is the query id
				String queryId = IOUtil.readString( dis );
				// this is the rest id
				String rsetId = IOUtil.readString( dis );

				int count = -1;
				Integer countObj = (Integer) queryCounts.get( queryId );
				if ( countObj != null )
				{
					count = countObj.intValue( );
				}
				count++;
				String rsetName = getQueryName( queryId ) + "_" + count;
				queryCounts.put( queryId, new Integer( count ) );
				rsetName2IdMapping.put( rsetName, rsetId );

				if( null != query2ResultMetaData )
				{
					IQueryDefinition query = getQuery( queryId );
					ResultMetaData metaData = (ResultMetaData) query2ResultMetaData.get( query );
					IResultSetItem resultItem = new ResultSetItem( rsetName,
							metaData );
					resultMetaList.add( resultItem );
				}
			}
		}
		catch ( EOFException eofe )
		{
			// we expect that there should be an EOFexception
		}
		catch ( IOException ioe )
		{
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

	/**
	 * get the result set name used by the instance.
	 * 
	 * @param iid
	 *            instance id
	 * @return result set name.
	 */
	protected String instanceId2RsetName( InstanceID iid )
	{
		DataID dataId = iid.getDataID( );
		if ( dataId != null )
		{
			DataSetID dataSetId = dataId.getDataSetID( );
			if ( dataSetId != null )
			{
				String rsetId = dataSetId.getDataSetName( );
				if ( rsetId != null )
				{
					return rsetId2Name( rsetId );
				}
			}
		}
		return null;
	}

	/**
	 * get the rset id from the rset name.
	 * 
	 * @param id
	 * @return
	 */
	protected String rsetId2Name( String id )
	{
		// search the name/Id mapping
		Iterator iter = rsetName2IdMapping.entrySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iter.next( );
			String rsetId = (String) entry.getValue( );
			String rsetName = (String) entry.getKey( );
			if ( rsetId.equals( id ) )
			{
				return rsetName;
			}
		}
		return null;
	}

	/**
	 * get the rset name from the rset id.
	 * 
	 * @param name
	 * @return
	 */
	protected String rsetName2Id( String name )
	{
		return (String) rsetName2IdMapping.get( name );
	}

	public void setInstanceID( InstanceID iid )
	{
		assert iid != null;

		prepareMetaData( );

		instanceId = iid;
		resultSetName = null;
		selectedColumns = null;
	}

	public void selectResultSet( String displayName )
	{
		assert displayName != null;

		prepareMetaData( );

		resultSetName = displayName;
		instanceId = null;
		selectedColumns = null;
	}

	public List getMetaData( ) throws EngineException
	{
		return getResultSetList( );
	}

	public List getResultSetList( ) throws EngineException
	{
		prepareMetaData( );
		if ( instanceId != null )
		{
			ArrayList rsetList = new ArrayList( );
			String rsetName = instanceId2RsetName( instanceId );
			if ( rsetName != null )
			{
				IResultMetaData metaData = getResultMetaData( rsetName );
				if ( metaData != null )
				{
					rsetList.add( new ResultSetItem( rsetName, metaData ) );
				}
			}
			return rsetList;

		}
		return resultMetaList;
	}

	/**
	 * get the metadata of a result set.
	 * 
	 * @param rsetName
	 * @return
	 */
	protected IResultMetaData getResultMetaData( String rsetName )
	{
		Iterator iter = resultMetaList.iterator( );
		while ( iter.hasNext( ) )
		{
			IResultSetItem rsetItem = (IResultSetItem) iter.next( );
			if ( rsetItem.getResultSetName( ).equals( rsetName ) )
			{
				return rsetItem.getResultMetaData( );
			}
		}
		return null;
	}

	public void selectColumns( String[] columnNames )
	{
		selectedColumns = columnNames;
	}

	public IExtractionResults extract( ) throws EngineException
	{
		String rsetName = resultSetName;
		if ( rsetName == null )
		{
			if ( instanceId != null )
			{
				rsetName = instanceId2RsetName( instanceId );
			}
		}
		if ( rsetName != null )
		{
			return extractByResultSetName( rsetName );
		}
		return null;
	}

	/*
	 * export result directly from result set name
	 */
	private IExtractionResults extractByResultSetName( String rsetName )
			throws EngineException
	{
		assert rsetName != null;
		assert executionContext.getDataEngine( ) != null;

		prepareMetaData( );

		DataEngine dataEngine = executionContext.getDataEngine( )
				.getDataEngine( );
		try
		{
			String rsetId = rsetName2Id( rsetName );
			if ( rsetId != null )
			{
				IQueryResults results = dataEngine.getQueryResults( rsetId );
				IResultMetaData metaData = getResultMetaData( rsetName );
				return new ExtractionResults( results, metaData,
						this.selectedColumns );
			}
		}
		catch ( BirtException e )
		{
			e.printStackTrace( );
		}
		return null;
	}

}
