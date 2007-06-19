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

//FIXME: 2.1.3
package org.eclipse.birt.report.engine.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.dte.DteDataEngine;
import org.eclipse.birt.report.engine.data.dte.DteMetaInfoIOUtil;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.mozilla.javascript.Scriptable;

public class DataExtractionTaskV1 extends EngineTask
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
	 * simple filter expression
	 */
	protected IFilterDefinition[] filterExpressions = null;

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
	 * mapping, map the rest name to rset id.
	 */
	protected HashMap rsetId2queryIdMapping = new HashMap( );

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

	public DataExtractionTaskV1( IReportEngine engine,
			IReportRunnable runnable, IReportDocument reader )
			throws EngineException
	{
		super( engine, runnable );

		this.report = ( (ReportRunnable) runnable ).getReportIR( );

		// load the report
		this.reportDocReader = reader;
		executionContext.setReportDocument( reportDocReader );
		executionContext.setFactoryMode( false );
		executionContext.setPresentationMode( true );

		
		// load the informationf rom the report document
		setParameterValues( reportDocReader.getParameterValues( ) );
		setParameterDisplayTexts( reportDocReader.getParameterDisplayTexts( ) );
		usingParameterValues( );
		executionContext.registerGlobalBeans( reportDocReader
				.getGlobalVariables( null ) );
		
		Map appContext = executionContext.getAppContext( );
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
			IDataQueryDefinition baseQuery = (IDataQueryDefinition) entry
					.getKey( );
			if ( baseQuery instanceof IQueryDefinition )
			{
				IQueryDefinition query = (IQueryDefinition) baseQuery;
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
		try
		{
			HashMap query2ResultMetaData = report.getResultMetaData( );
			IDocArchiveReader reader = reportDocReader.getArchive( );

			HashMap queryCounts = new HashMap( );

			ArrayList result = DteMetaInfoIOUtil.loadDteMetaInfo( reader );

			if ( result != null )
			{

				for ( int i = 0; i < result.size( ); i++ )
				{

					String[] rsetRelation = (String[]) result.get( i );
					// this is the query id
					String queryId = rsetRelation[2];
					// this is the rest id
					String rsetId = rsetRelation[3];

					IQueryDefinition query = getQuery( queryId );

					rsetId2queryIdMapping.put( rsetId, queryId );

					int count = -1;
					Integer countObj = (Integer) queryCounts.get( queryId );
					if ( countObj != null )
					{
						count = countObj.intValue( );
					}
					count++;
					String rsetName = getQueryName( queryId );
					if ( count > 0 )
					{
						rsetName = rsetName + "_" + count;
					}
					queryCounts.put( queryId, new Integer( count ) );
					rsetName2IdMapping.put( rsetName, rsetId );

					if ( null != query2ResultMetaData )
					{
						ResultMetaData metaData = (ResultMetaData) query2ResultMetaData
								.get( query );
						if ( metaData != null && metaData.getColumnCount( ) > 0 )
						{
							IResultSetItem resultItem = new ResultSetItem(
									rsetName, metaData );
							resultMetaList.add( resultItem );
						}
					}
				}
			}
		}
		catch ( IOException ioe )
		{
			logger.log( Level.SEVERE, ioe.getMessage( ), ioe );
		}
	}

	InstanceID[] getAncestors( InstanceID id )
	{
		LinkedList iids = new LinkedList( );
		while ( id != null )
		{
			iids.addFirst( id );
			id = id.getParentID( );
		}
		return (InstanceID[]) iids.toArray( new InstanceID[]{} );
	}

	IBaseResultSet executeQuery( IBaseResultSet prset, InstanceID iid )
			throws BirtException
	{
		DataID dataId = iid.getDataID( );
		if ( dataId != null && prset != null )
		{
			if ( prset instanceof IQueryResultSet )
			{
				( (IQueryResultSet) prset ).skipTo( dataId.getRowID( ) );
			}
			else if ( prset instanceof ICubeResultSet )
			{
				( (ICubeResultSet) prset ).skipTo( dataId.getCellID( ) );
			}
		}
		long id = iid.getComponentID( );
		ReportItemDesign design = (ReportItemDesign) report
				.getReportItemByID( id );
		IDataQueryDefinition query = design.getQuery( );
		if ( query != null )
		{
			return executionContext.getDataEngine( ).execute( prset, query,
					false );
		}
		return prset;
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
		InstanceID[] iids = getAncestors( iid );
		ArrayList rsets = new ArrayList( );
		IBaseResultSet prset = null;
		IBaseResultSet rset = null;
		String rsetName = null;
		try
		{
			for ( int i = 0; i < iids.length; i++ )
			{
				rset = executeQuery( prset, iids[i] );
				if ( rset != null && rset != prset )
				{
					rsets.add( rset );
				}
				prset = rset;
			}
			if ( rset != null )
			{
				rsetName = rset.getID( ).getDataSetName( );
			}
		}
		catch ( BirtException ex )
		{
			logger.log( Level.SEVERE, ex.getLocalizedMessage( ), ex );

		}
		for ( int i = 0; i < rsets.size( ); i++ )
		{
			rset = (IBaseResultSet) rsets.get( i );
			rset.close( );
		}

		if ( rsetName != null )
		{
			return rsetId2Name( rsetName );
		}
		return rsetName;
	}

	/**
	 * get the resultset id from the query id.
	 * 
	 * @param id
	 * @return
	 */
	protected String queryId2rsetId( String id )
	{
		// search the name/Id mapping
		Iterator iter = rsetId2queryIdMapping.entrySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iter.next( );
			String queryId = (String) entry.getValue( );
			String rsetId = (String) entry.getKey( );
			if ( queryId.equals( id ) )
			{
				return rsetId;
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

		if ( displayName.startsWith( "InstanceId:" ) )
		{
			resultSetName = null;
			instanceId = InstanceID.parse( displayName.substring( 11 ) );
		}
		else
		{
			resultSetName = displayName;
			instanceId = null;
		}
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
			IResultMetaData metaData = getMetaDateByInstanceID( instanceId );
			if ( metaData != null )
			{
				rsetList.add( new ResultSetItem( "InstanceId:"
						+ instanceId.toUniqueString( ), metaData ) );
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
		try
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
			if ( instanceId != null )
			{
				return extractByInstanceID( instanceId );
			}
			return null;
		}
		catch ( BirtException ex )
		{
			throw new EngineException( ex.getLocalizedMessage( ), ex );
		}
	}

	/*
	 * export result directly from result set name
	 */
	private IExtractionResults extractByResultSetName( String rsetName )
			throws BirtException
	{
		assert rsetName != null;
		assert executionContext.getDataEngine( ) != null;

		prepareMetaData( );

		DataRequestSession dataSession = executionContext.getDataEngine( ).getDTESession( );
		String rsetId = rsetName2Id( rsetName );
		if ( rsetId != null )
		{
			IQueryResults results = null;
			if ( null == filterExpressions )
			{
				results = dataSession.getQueryResults( rsetId );
			}
			else
			{
				// creat new query
				String queryId = (String) rsetId2queryIdMapping.get( rsetId );
				QueryDefinition query = (QueryDefinition) getQuery( queryId );
				QueryDefinition newQuery = cloneQuery( query );
				if ( null == newQuery )
				{
					return null;
				}

				// add filter
				for ( int iNum = 0; iNum < filterExpressions.length; iNum++ )
				{
					newQuery.getFilters( ).add( filterExpressions[iNum] );
				}
				filterExpressions = null;

				// get new result
				newQuery.setQueryResultsID( rsetId );
				Scriptable scope = executionContext.getSharedScope( );
				IPreparedQuery preparedQuery = dataSession.prepare( newQuery );
				results = preparedQuery.execute( scope );
			}

			if ( null != results )
			{
				IResultMetaData metaData = getResultMetaData( rsetName );
				if ( metaData != null )
				{
					return new ExtractionResults( results, metaData,
							this.selectedColumns );
				}
			}
		}
		return null;
	}

	private IExtractionResults extractByInstanceID( InstanceID instanceId )
			throws BirtException
	{
		InstanceID iid = instanceId;
		while ( iid != null )
		{
			long id = iid.getComponentID( );
			ReportItemDesign design = (ReportItemDesign) report
					.getReportItemByID( id );
			IDataQueryDefinition dataQuery = design.getQuery( );
			if ( dataQuery != null  )
			{
				if ( !( dataQuery instanceof IBaseQueryDefinition ) )
				{
					// it is a cube query, as we don't support it now, exit.
					return null;
				}
				IBaseQueryDefinition query = (IBaseQueryDefinition) dataQuery;
				if ( filterExpressions != null )
				{
					query = cloneQuery( query );
					// add filter
					for ( int i = 0; i < filterExpressions.length; i++ )
					{
						query.getFilters( ).add( filterExpressions[i] );
					}
				}

				while ( iid != null )
				{
					DataID dataId = iid.getDataID( );
					if ( dataId != null )
					{
						DataSetID dataSetId = dataId.getDataSetID( );
						long rowId = dataId.getRowID( );
						IResultIterator dataIter = executeSubQuery( dataSetId,
								rowId, (ISubqueryDefinition) query );
						IResultMetaData metaData = getMetaDateByInstanceID( instanceId );
						if ( dataIter != null && metaData != null )
						{
							return new ExtractionResults( dataIter, metaData,
									this.selectedColumns );
						}
						return null;
					}
					iid = iid.getParentID( );
				}
				return null;
			}
			iid = iid.getParentID( );
		}
		return null;
	}

	private IResultMetaData getMetaDateByInstanceID( InstanceID iid )
	{
		while ( iid != null )
		{
			long id = iid.getComponentID( );
			ReportItemDesign design = (ReportItemDesign) report
					.getReportItemByID( id );
			IDataQueryDefinition query = design.getQuery( );
			if ( query != null )
			{
				HashMap query2ResultMetaData = report.getResultMetaData( );
				if ( null != query2ResultMetaData )
				{
					return (ResultMetaData) query2ResultMetaData.get( query );
				}
				return null;
			}
			iid = iid.getParentID( );
		}
		return null;
	}

	private IResultIterator executeQuery( String rset, QueryDefinition query )
			throws BirtException
	{
		( (QueryDefinition) query ).setQueryResultsID( rset );

		DataRequestSession dataSession = executionContext.getDataEngine( )
				.getDTESession( );
		Scriptable scope = executionContext.getSharedScope( );
		Map appContext = executionContext.getAppContext( );
		IPreparedQuery pQuery = dataSession.prepare( query, appContext );
		IQueryResults results = pQuery.execute( scope );
		return results.getResultIterator( );
	}

	private IResultIterator executeSubQuery( DataSetID dataSet, long rowId,
			ISubqueryDefinition query ) throws BirtException
	{
		IResultIterator rsetIter = null;
		String rset = dataSet.getDataSetName( );
		if ( rset != null )
		{
			rsetIter = executeQuery( rset, (QueryDefinition) query
					.getParentQuery( ) );
		}
		else
		{
			rsetIter = executeSubQuery( dataSet.getParentID( ), dataSet
					.getRowID( ), (ISubqueryDefinition) query.getParentQuery( ) );
		}
		rsetIter.moveTo( (int) rowId );
		String queryName = query.getName( );
		Scriptable scope = executionContext.getSharedScope( );
		return rsetIter.getSecondaryIterator( queryName, scope );
	}

	/**
	 * copy a query.
	 * 
	 * @param query
	 * @return
	 */
	private BaseQueryDefinition cloneQuery( IBaseQueryDefinition query )
	{
		if ( query instanceof SubqueryDefinition )
		{
			return cloneQuery( (SubqueryDefinition) query );
		}
		else if ( query instanceof QueryDefinition )
		{
			return cloneQuery( (QueryDefinition) query );
		}
		return null;
	}

	private SubqueryDefinition cloneQuery( SubqueryDefinition query )
	{
		BaseQueryDefinition parent = cloneQuery( query.getParentQuery( ) );

		SubqueryDefinition newQuery = new SubqueryDefinition( query.getName( ),
				parent );
		newQuery.getBindings( ).putAll( query.getBindings( ) );
		newQuery.getSorts( ).addAll( query.getSorts( ) );
		newQuery.getFilters( ).addAll( query.getFilters( ) );
		newQuery.getSubqueries( ).addAll( query.getSubqueries( ) );

		newQuery.getGroups( ).addAll( query.getGroups( ) );
		newQuery.setUsesDetails( query.usesDetails( ) );

		parent.getSubqueries( ).add( newQuery );
		
		return newQuery;
	}

	private QueryDefinition cloneQuery( QueryDefinition query )
	{
		QueryDefinition newQuery = new QueryDefinition( );
		newQuery.getBindings( ).putAll( query.getBindings( ) );
		newQuery.getSorts( ).addAll( query.getSorts( ) );
		newQuery.getFilters( ).addAll( query.getFilters( ) );

		newQuery.getGroups( ).addAll( query.getGroups( ) );
		newQuery.setUsesDetails( query.usesDetails( ) );
		newQuery.setMaxRows( query.getMaxRows( ) );

		newQuery.setDataSetName( query.getDataSetName( ) );
		newQuery.setAutoBinding( query.needAutoBinding( ) );
		newQuery.setColumnProjection( query.getColumnProjection( ) );

		return newQuery;
	}

	/**
	 * @param simpleFilterExpression
	 *            add one filter condition to the extraction. Only simple filter
	 *            expressions are supported for now, i.e., LHS must be a column
	 *            name, only <, >, = and startWith is supported.
	 */
	public void setFilters( IFilterDefinition[] simpleFilterExpression )
	{
		filterExpressions = simpleFilterExpression;
	}

}
