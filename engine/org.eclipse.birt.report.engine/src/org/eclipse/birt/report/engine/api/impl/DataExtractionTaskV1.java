/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryLocator;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.DataExtractionOption;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IDataExtractionOption;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IEngineConfig;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.dte.DteDataEngine;
import org.eclipse.birt.report.engine.data.dte.DteMetaInfoIOUtil;
import org.eclipse.birt.report.engine.data.dte.QueryResultSet;
import org.eclipse.birt.report.engine.executor.EngineExtensionManager;
import org.eclipse.birt.report.engine.extension.IDataExtractionExtension;
import org.eclipse.birt.report.engine.extension.engine.IDataExtension;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.ReportItemHandle;
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
	 * simple sort expression
	 */
	protected ISortDefinition[] sortExpressions = null;

	/**
	 * maximum rows
	 */
	protected int maxRows = -1;

	/**
	 * Start row.
	 */
	protected int startRow = 0;
	
	/**
	 * whether get distinct values
	 */
	protected boolean distinct;
	
	/**
	 * group mode. Default is true.
	 * group mode isn't used if startRow or distint is set.
	 */
	protected boolean groupMode = true;

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

	protected HashMap query2QueryIdMapping = new HashMap( );

	protected HashMap rssetIdMapping = new HashMap( );

	/**
	 * list contains all the resultsets each entry is a
	 */
	protected ArrayList resultMetaList = new ArrayList( );
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger( DteDataEngine.class
			.getName( ) );

	public DataExtractionTaskV1( ReportEngine engine, IReportDocument reader )
			throws EngineException
	{
		super( engine, IEngineTask.TASK_DATAEXTRACTION );
		IReportRunnable runnable = getOnPreparedRunnable( reader );
		setReportRunnable( runnable );
		IInternalReportDocument internalDoc = (IInternalReportDocument) reader;
		Report reportIR = internalDoc
				.getReportIR( executionContext.getDesign( ) );
		executionContext.setReport( reportIR );
		this.report = executionContext.getReport( );
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
				// FIXME: as an report element may have 2 or more queries, the queryName
				// shoulde be different for each query
				if ( queryName == null )
				{
					queryName = "ELEMENT_" + item.getID( );
				}
				queryId2NameMapping.put( queryId, queryName );
				queryId2QueryMapping.put( queryId, query );
				query2QueryIdMapping.put( query, queryId );
			}
		}

		try
		{
			loadResultSetMetaData( );
		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
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
	private void loadResultSetMetaData( ) // throws EngineException
	{
		try
		{
			HashMap query2ResultMetaData = report.getResultMetaData( );
			IDocArchiveReader reader = reportDocReader.getArchive( );

			HashMap queryCounts = new HashMap( );

			ArrayList result = DteMetaInfoIOUtil.loadDteMetaInfo( reader );

			if ( result != null )
			{
				Set dteMetaInfoSet = new HashSet( );
				for ( int i = 0; i < result.size( ); i++ )
				{
					String[] rsetRelation = (String[]) result.get( i );

					rssetIdMapping.put( this
							.getDteMetaInfoString( rsetRelation ),
							rsetRelation[3] );
					// if the rset has been loaded, skip it.
					String dteMetaInfoString = getDteMetaInfoString( rsetRelation );
					if ( dteMetaInfoSet.contains( dteMetaInfoString ) )
					{
						continue;
					}
					dteMetaInfoSet.add( dteMetaInfoString );

					// this is the query id
					String queryId = rsetRelation[2];
					// this is the rest id
					String rsetId = rsetRelation[3];

					IQueryDefinition query = getQuery( queryId );

					//rsetId2queryIdMapping.put( rsetId, queryId );

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
					//rsetName2IdMapping.put( rsetName, rsetId );

					if ( null != query2ResultMetaData )
					{
						ResultMetaData metaData = (ResultMetaData) query2ResultMetaData
								.get( query );
						if ( metaData != null && metaData.getColumnCount( ) > 0 )
						{
							ReportItemDesign design = (ReportItemDesign) report
									.getReportItemToQueryMap( ).get( query );
							ReportItemHandle handle = (ReportItemHandle) design
									.getHandle( );
							if ( !handle.allowExport( ) )
							{
								continue;
							}
							IResultSetItem resultItem = new ResultSetItem(
									rsetName, metaData, handle,
									executionContext.getLocale( ) );
							
							resultMetaList.add( resultItem );
							rsetName2IdMapping.put( rsetName, rsetId );
							rsetId2queryIdMapping.put( rsetId, queryId );
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

	/**
	 * Transfer the rset relation array to a string.
	 * 
	 * @param rsetRelation
	 * @return
	 */
	private String getDteMetaInfoString( String[] rsetRelation )
	{
		StringBuffer buffer = new StringBuffer( );

		String pRsetId = rsetRelation[0];
		String rowId = rsetRelation[1];
		String queryId = rsetRelation[2];
		buffer.setLength( 0 );
		if ( pRsetId == null )
		{
			buffer.append( "null" );
		}
		else
		{
			buffer.append( pRsetId );
		}
		buffer.append( "." );
		buffer.append( rowId );
		buffer.append( "." );
		buffer.append( queryId );
		return buffer.toString( );
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
			prepareMetaData( );
			if ( resultSetName != null )
			{
				return extractByResultSetName( resultSetName );
			}
			if ( instanceId != null )
			{
				return extractByInstanceID( instanceId );
			}
			return null;
		}
		catch ( BirtException ex )
		{
			throw new EngineException( ex );
		}
	}
	
	class QueryTask
	{

		IBaseQueryDefinition query;
		DataSetID parent;
		int rowId;
		InstanceID iid;

		QueryTask( IBaseQueryDefinition query, DataSetID parent, int rowId, InstanceID iid )
		{
			this.query = query;
			this.parent = parent;
			this.rowId = rowId;
			this.iid = iid;
		}
	}
	
	/*
	 * A plan is a list of QueryTask and these tasks are arranged in son-parent order,
	 * that is, the nth QueryTask is based on the (n+1)th QueryTask
	 */
	class Plan extends ArrayList<QueryTask>
	{
	}
	
	/*
	 * create a plan.
	 */
	private Plan createPlan( InstanceID instanceId ) throws EngineException
	{
		InstanceID iid = instanceId;
		IBaseQueryDefinition query = null;
		while ( iid != null )
		{
			long id = iid.getComponentID( );
			ReportItemDesign design = (ReportItemDesign) report
					.getReportItemByID( id );
			IDataQueryDefinition dataQuery = design.getQuery( );

			if ( dataQuery != null )
			{
				ReportItemHandle handle = (ReportItemHandle) design.getHandle( );
				if ( !handle.allowExport( ) )
				{
					throw new EngineException(
							MessageConstants.RESULTSET_EXTRACT_ERROR );
				}
				if ( !( dataQuery instanceof IBaseQueryDefinition ) )
				{
					// it is a cube query, as we don't support it now, exit.
					return null;
				}
				query = (IBaseQueryDefinition) dataQuery;
				break;
			}
			iid = iid.getParentID( );
		}
		// At this point, query refers to the last query in the query chain.
		ArrayList<DataSetID> dsIDs = new ArrayList<DataSetID>( );
		Plan plan = new Plan( );
		while ( query != null )
		{
			while ( iid != null )
			{
				if ( iid.getDataID( ) != null )
				{
					DataID dataId = iid.getDataID( );
					DataSetID dsId = dataId.getDataSetID( );
					boolean found = false;
					Iterator<DataSetID> itr = dsIDs.iterator( );
					while ( itr.hasNext( ) )
					{
						if ( itr.next( ).equals( dsId ) )
						{
							found = true;
							break;
						}
					}
					if ( !found )
					{
						dsIDs.add( dsId );
						QueryTask task = new QueryTask( query, dsId,
								(int) dataId.getRowID( ), iid );
						plan.add( task );
						break;
					}
				}
				iid = iid.getParentID( );
			}
			if ( iid == null )
			{
				break;
			}
			query = query.getParentQuery( );
		}
		// At this point, query refers to the top most query
		QueryTask task = new QueryTask( query, null, -1, null );
		plan.add( task );
		return plan;
	}
	
	/*
	 * update a plan by replacing the item at [index] with the new query
	 */
	private void updatePlan( Plan plan, int index, IBaseQueryDefinition query )
	{
		if ( index < 0 || plan == null || plan.size( ) < index + 1 )
		{
			return;
		}
		QueryTask task = plan.get( index );
		task.query = query;
	}
	
	/*
	 * update a plan's queries using the specified query and all its parent
	 */
	private void updatePlanFully( Plan plan, IBaseQueryDefinition query )
	{
		int index = 0;
		while ( query != null && index <= plan.size( ) )
		{
			updatePlan( plan, index, query );
			index++;
			query = query.getParentQuery( );
		}
	}
	
	/*
	 * execute those tasks from [index] to the top most
	 */
	private QueryResultSet executePlan( ArrayList<QueryTask> plan, int index )
			throws BirtException
	{
		if ( plan == null || plan.size( ) == 0 || index < 0
				|| index + 1 > plan.size( ) )
		{
			return null;
		}

		QueryResultSet parent = null;
		for ( int current = plan.size( ) - 1; current >= index; current-- )
		{
			QueryTask task = plan.get( current );
			IBaseQueryDefinition query = task.query;
			if ( task.parent == null )
			{
				// this is a top query
				String queryId = getQueryId( query );
				String rsID = getResultsetID( null, -1, queryId );
				assert rsID != null;

				IQueryResults qryRS = executeQuery( rsID,
						(QueryDefinition) query );
				parent = new QueryResultSet( executionContext
						.getDataEngine( ), executionContext,
						(IQueryDefinition) query, qryRS );
			}
			else
			{
				if ( parent == null )
				{
					throw new EngineException(
							MessageConstants.RESULTSET_EXTRACT_ERROR );
				}
				IResultIterator parentItr = parent.getResultIterator( );
				parentItr.moveTo( task.rowId );
				if ( query instanceof IQueryDefinition )
				{
					// this is a nested query
					String queryId = getQueryId( query );
					int rowId = parentItr.getRowId( );
					String rsID = getResultsetID( parent.getQueryResultsID( ),
							rowId, queryId );
					assert rsID != null;
					IQueryResults qryRS = executeQuery( rsID,
							(QueryDefinition) query );
					parent = new QueryResultSet( executionContext
							.getDataEngine( ), executionContext, parent,
							(IQueryDefinition) query, qryRS );
				}
				else if ( query instanceof ISubqueryDefinition )
				{
					// this is a sub query
					String queryName = query.getName( );
					Scriptable scope = executionContext.getSharedScope( );
					IResultIterator itr2 = parentItr.getSecondaryIterator(
							queryName, scope );
					parent = new QueryResultSet( parent,
							(ISubqueryDefinition) query, itr2 );
				}
				else
				{
					// should not enter here
					return null;
				}
			}
		}
		return parent;
	}
	
	// when cloning queries, map the cloned query to its old query id
	private HashMap tmpQuery2QueryIdMapping = new HashMap( );
	
	private String getQueryId( IBaseQueryDefinition query )
	{
		String id = (String) tmpQuery2QueryIdMapping.get( query );
		if ( id == null )
		{
			return (String) query2QueryIdMapping.get( query );
		}
		return id;
	}
	
	private IQueryResults executeQuery( String rset, QueryDefinition query )
			throws BirtException
	{
		( (QueryDefinition) query ).setQueryResultsID( rset );

		DataRequestSession dataSession = executionContext.getDataEngine( )
				.getDTESession( );
		Scriptable scope = executionContext.getSharedScope( );
		Map appContext = executionContext.getAppContext( );
		// prepare the query
		processQueryExtensions( query );

		IPreparedQuery pQuery = dataSession.prepare( query, appContext );
		return pQuery.execute( scope );
	}

	/*
	 * export result directly from result set name
	 */
	private IExtractionResults extractByResultSetName( String rsetName )
			throws BirtException
	{
		if ( !rsetName2IdMapping.containsKey( rsetName ) )
		{
			throw new EngineException( MessageConstants.RESULTSET_EXTRACT_ERROR );
		}

		DataRequestSession dataSession = executionContext.getDataEngine( )
				.getDTESession( );
		String rsetId = rsetName2Id( rsetName );
		if ( rsetId != null )
		{
			IQueryResults results = null;
			String queryId = (String) rsetId2queryIdMapping.get( rsetId );
			QueryDefinition query = (QueryDefinition) getQuery( queryId );
			if ( null == query )
			{
				return null;
			}
			// set up a new query
			QueryDefinition newQuery = null;
			if ( groupMode )
			{
				newQuery = cloneQuery( query );
				setupQueryWithFilterAndSort( newQuery );
				newQuery.setQueryResultsID( rsetId );
			}
			else
			{
				QueryDefinition cloned = cloneQuery( query );
				cloned.setQueryResultsID( rsetId );
				newQuery = new QueryDefinition( );
				newQuery.setSourceQuery( cloned );
				setupQueryWithFilterAndSort( newQuery );
				setupDistinct( newQuery );
			}
			// execute query
			Scriptable scope = executionContext.getSharedScope( );
			processQueryExtensions( newQuery );
			IPreparedQuery preparedQuery = dataSession.prepare( newQuery );
			results = preparedQuery.execute( scope );
			if ( null != results )
			{
				IResultMetaData metaData = getResultMetaData( rsetName );
				if ( metaData != null )
				{
					return new ExtractionResults( results, metaData,
							this.selectedColumns, startRow, maxRows );
				}
			}
		}
		return null;
	}

	private IExtractionResults extractByInstanceID( InstanceID instanceId )
			throws BirtException
	{
		assert instanceId != null;
		Plan plan = createPlan( instanceId );
		if ( plan.size( ) == 0 )
		{
			return null;
		}
		tmpQuery2QueryIdMapping.clear( );
		IQueryResults queryResults = null;

		QueryTask task = plan.get( 0 );
		IBaseQueryDefinition query = task.query;
		if ( groupMode )
		{
			IBaseQueryDefinition newQuery = null;
			newQuery = cloneQuery( query );
			setupQueryWithFilterAndSort( newQuery );

			updatePlanFully( plan, newQuery );
			QueryResultSet rset = executePlan( plan, 0 );
			if ( rset == null )
			{
				return null;
			}
			queryResults = (IQueryResults) rset.getQueryResults( );
		}
		else
		{
			QueryDefinition newQuery = new QueryDefinition( );
			if ( query instanceof IQueryDefinition )
			{
				QueryDefinition cloned = cloneQuery( (QueryDefinition) query );
				updatePlanFully( plan, cloned );
				QueryResultSet rset = executePlan( plan, 0 );
				( (QueryDefinition) cloned ).setQueryResultsID( rset
						.getQueryResultsID( ) );
				newQuery.setSourceQuery( cloned );
				setupQueryWithFilterAndSort( newQuery );
			}
			else
			{
				ISubqueryDefinition clonedSubquery = cloneQuery2(
						(SubqueryDefinition) query, task.iid );
				updatePlanFully( plan, clonedSubquery );
				int index = 1;
				ISubqueryDefinition topSubquery = clonedSubquery;
				while ( topSubquery != null )
				{
					if ( topSubquery.getParentQuery( ) instanceof IQueryDefinition )
					{
						break;
					}
					topSubquery = (ISubqueryDefinition) topSubquery
							.getParentQuery( );
					index++;
				}
				QueryResultSet rset = executePlan( plan, index );
				( (QueryDefinition) topSubquery.getParentQuery( ) )
						.setQueryResultsID( rset.getQueryResultsID( ) );
				newQuery.setSourceQuery( clonedSubquery );
				setupDistinct( newQuery );
				setupQueryWithFilterAndSort( newQuery );
			}

			DataRequestSession dataSession = executionContext.getDataEngine( )
					.getDTESession( );
			Scriptable scope = executionContext.getSharedScope( );
			processQueryExtensions( newQuery );
			IPreparedQuery preparedQuery = dataSession.prepare( newQuery );
			queryResults = preparedQuery.execute( scope );
		}
		if ( queryResults != null )
		{
			IResultMetaData metaData = getMetaDateByInstanceID( instanceId );
			if ( metaData != null )
			{
				return new ExtractionResults( queryResults, metaData,
						this.selectedColumns, startRow, maxRows );
			}
		}
		return null;
	}

	private void setupQueryWithFilterAndSort( IBaseQueryDefinition query )
	{
		// add filter
		if ( filterExpressions != null )
		{
			for ( int iNum = 0; iNum < filterExpressions.length; iNum++ )
			{
				query.getFilters( ).add( filterExpressions[iNum] );
			}
			filterExpressions = null;
		}

		// add sort
		if ( sortExpressions != null )
		{
			for ( int iNum = 0; iNum < sortExpressions.length; iNum++ )
			{
				query.getSorts( ).add( sortExpressions[iNum] );
			}
			sortExpressions = null;
		}
	}
	
	private void setupDistinct( IBaseQueryDefinition query )
	{
		( (BaseQueryDefinition) query ).setDistinctValue( this.distinct );
	}

	private String getResultsetID( String prset, long rowId, String queryId )
	{
		String parentRSet = ( prset == null ) ? "null" : prset;
		String rsmeta = parentRSet + "." + rowId + "." + queryId;
		return (String) rssetIdMapping.get( rsmeta );
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
				ReportItemHandle handle = (ReportItemHandle) design.getHandle( );
				if ( !handle.allowExport( ) )
				{
					return null;
				}
				HashMap query2ResultMetaData = report.getResultMetaData( );
				if ( null != query2ResultMetaData )
				{
					//return (ResultMetaData) query2ResultMetaData.get( query );
					return hackMetaData( query2ResultMetaData, query );
				}
				return null;
			}
			iid = iid.getParentID( );
		}
		return null;
	}
	
	private IResultMetaData hackMetaData( HashMap metas,
			IDataQueryDefinition query )
	{
		IResultMetaData meta = (ResultMetaData) metas.get( query );
		if ( meta.getColumnCount( ) > 0 )
			return meta;

		if ( query instanceof SubqueryDefinition )
		{
			IBaseQueryDefinition parent = ( (SubqueryDefinition) query )
					.getParentQuery( );
			meta = (ResultMetaData) metas.get( parent );
			while ( parent instanceof SubqueryDefinition )
			{
				if ( meta != null && meta.getColumnCount( ) > 0 )
				{
					return meta;
				}
				parent = ( (SubqueryDefinition) parent ).getParentQuery( );
				meta = (ResultMetaData) metas.get( parent );
			}
		}

		return meta;
	}
/*
	private IResultIterator executeQuery( String rset, QueryDefinition query )
			throws BirtException
	{
		( (QueryDefinition) query ).setQueryResultsID( rset );

		DataRequestSession dataSession = executionContext.getDataEngine( )
				.getDTESession( );
		Scriptable scope = executionContext.getSharedScope( );
		Map appContext = executionContext.getAppContext( );
		// prepare the query
		processQueryExtensions( query );

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
	
	private IResultIterator executeQuery( DataSetID dataSet, long rowId,
			IQueryDefinition query, String queryId ) throws BirtException
	{
		IResultIterator rsetIter = null;
		String rset = dataSet.getDataSetName( );
		if ( rset == null )
		{
			ISubqueryDefinition parentQuery = (ISubqueryDefinition) query
					.getParentQuery( );
			rsetIter = executeSubQuery( dataSet.getParentID( ), dataSet
					.getRowID( ), parentQuery );
			rsetIter.moveTo( (int) rowId );
			int rawId = rsetIter.getRowId( );
			return executeQuery( dataSet.getParentID( ).toString( ), rawId,
					queryId, query );
		}
		else
		{
			QueryDefinition parentQuery = (QueryDefinition) query
					.getParentQuery( );
			String parentQueryId = (String) queryId2QueryMapping
					.get( parentQuery );
			if ( rset != null )
			{
				rsetIter = executeQuery( rset, parentQuery );
			}
			else
			{
				rsetIter = executeQuery( dataSet.getParentID( ), dataSet
						.getRowID( ), parentQuery, parentQueryId );
			}
			rsetIter.moveTo( (int) rowId );
			int rawId = rsetIter.getRowId( );
			return executeQuery( rset, rawId, queryId, query );
		}
	}
	
	private IResultIterator executeQuery( String prset, long rowId,
			String queryId, IQueryDefinition query ) throws BirtException
	{
		String rsId = getResultsetID( prset, rowId, queryId );
		if ( rsId != null )
		{
			return executeQuery( rsId, (QueryDefinition) query );
		}
		return null;
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
		if ( design != null )
		{
			IDataQueryDefinition query = design.getQuery( );
			if ( query != null )
			{
				return executionContext.getDataEngine( ).execute( prset, query,
						false );
			}
		}
		return prset;
	}

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
*/
	private BaseQueryDefinition cloneQuery2( IBaseQueryDefinition query,
			InstanceID instanceID )
	{
		if ( query instanceof SubqueryDefinition )
		{
			return cloneQuery2( (SubqueryDefinition) query, instanceID );
		}
		else if ( query instanceof QueryDefinition )
		{
			return cloneQuery( (QueryDefinition) query );
		}
		return null;
	}

	private SubqueryDefinition cloneQuery2( SubqueryDefinition query,
			InstanceID instanceID )
	{
		while ( instanceID.getDataID( ) == null )
		{
			instanceID = instanceID.getParentID( );
		}
		InstanceID currentID = instanceID;
		
		BaseQueryDefinition parent = cloneQuery2( query.getParentQuery( ),
				instanceID.getParentID( ) );

		SubqueryLocator locator = new SubqueryLocator( (int) currentID
				.getDataID( ).getRowID( ), query.getName( ), parent );

		locator.getBindings( ).putAll( query.getBindings( ) );
		locator.getSorts( ).addAll( query.getSorts( ) );
		locator.getFilters( ).addAll( query.getFilters( ) );
		locator.getSubqueries( ).addAll( query.getSubqueries( ) );

		locator.getGroups( ).addAll( query.getGroups( ) );
		locator.setUsesDetails( query.usesDetails( ) );
		
		return locator;
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
		newQuery.setApplyOnGroupFlag( query.applyOnGroup( ) );
		parent.getSubqueries( ).add( newQuery );

		return newQuery;
	}

	private QueryDefinition cloneQuery( QueryDefinition query )
	{
		QueryDefinition newQuery = new QueryDefinition(
				(BaseQueryDefinition) query.getParentQuery( ) );
		newQuery.getBindings( ).putAll( query.getBindings( ) );
		newQuery.getSorts( ).addAll( query.getSorts( ) );
		newQuery.getFilters( ).addAll( query.getFilters( ) );

		newQuery.getGroups( ).addAll( query.getGroups( ) );
		newQuery.setUsesDetails( query.usesDetails( ) );
		newQuery.setMaxRows( query.getMaxRows( ) );

		newQuery.setDataSetName( query.getDataSetName( ) );
		newQuery.setAutoBinding( query.needAutoBinding( ) );
		newQuery.setColumnProjection( query.getColumnProjection( ) );
		
		newQuery.setName( query.getName( ) );
		
		String queryID = (String)query2QueryIdMapping.get( query );
		tmpQuery2QueryIdMapping.put( newQuery, queryID );
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

	/**
	 * @param simpleSortExpression
	 */
	public void setSorts( ISortDefinition[] simpleSortExpression )
	{
		sortExpressions = simpleSortExpression;
	}

	/**
	 * @param maxRows
	 */
	public void setMaxRows( int maxRows )
	{
		this.maxRows = maxRows;
	}

	public void extract( IDataExtractionOption option ) throws BirtException
	{
		IDataExtractionOption extractOption = setupExtractOption( option );
		IDataExtractionExtension dataExtraction = getDataExtractionExtension( extractOption );
		try
		{
			dataExtraction.initilize( executionContext.getReportContext( ),
					extractOption );
			dataExtraction.output( extract( ) );
		}
		finally
		{
			dataExtraction.release( );
		}
	}

	private IDataExtractionExtension getDataExtractionExtension(
			IDataExtractionOption option ) throws EngineException
	{
		IDataExtractionExtension dataExtraction = null;
		String extension = option.getExtension( );
		ExtensionManager extensionManager = ExtensionManager.getInstance( );
		if ( extension != null )
		{
			dataExtraction = extensionManager
					.createDataExtractionExtensionById( extension );
			if ( dataExtraction == null )
			{
				logger.log( Level.WARNING, "Extension with id " + extension
						+ " doesn't exist." );
			}
		}

		String format = null;
		if ( dataExtraction == null )
		{
			format = option.getOutputFormat( );
			if ( format != null )
			{
				dataExtraction = extensionManager
						.createDataExtractionExtensionByFormat( format );
				if ( dataExtraction == null )
				{
					logger.log( Level.WARNING, "Extension of format " + format
							+ " doesn't exist." );
				}
			}
		}
		if ( dataExtraction == null )
		{
			throw new EngineException(
					MessageConstants.INVALID_EXTENSION_ERROR, new Object[]{
							extension, format} );
		}
		return dataExtraction;
	}

	public void setStartRow( int startRow )
	{
		this.startRow = startRow;
		groupMode = false;
	}
	
	public void setDistinctValuesOnly( boolean distinct )
	{
		this.distinct = distinct;
		groupMode = false;
	}

	protected void processQueryExtensions( IDataQueryDefinition query )
			throws EngineException
	{
		String[] extensions = executionContext.getEngineExtensions( );
		if ( extensions != null )
		{
			EngineExtensionManager manager = executionContext
					.getEngineExtensionManager( );
			for ( String extensionName : extensions )
			{
				IDataExtension extension = manager
						.getDataExtension( extensionName );
				if ( extension != null )
				{
					extension.prepareQuery( query );
				}
			}
		}
	}

	private IDataExtractionOption setupExtractOption(
			IDataExtractionOption options )
	{
		// setup the data extraction options from:
		HashMap allOptions = new HashMap( );

		// try to get the default render option from the engine config.
		HashMap configs = engine.getConfig( ).getEmitterConfigs( );
		// get the default format of the emitters, the default format key is
		// IRenderOption.OUTPUT_FORMAT;
		IRenderOption defaultOptions = (IRenderOption) configs
				.get( IEngineConfig.DEFAULT_RENDER_OPTION );
		if ( defaultOptions != null )
		{
			allOptions.putAll( defaultOptions.getOptions( ) );
		}

		// try to get the render options by the format
		IRenderOption defaultHtmlOptions = (IRenderOption) configs
				.get( IRenderOption.OUTPUT_FORMAT_HTML );
		if ( defaultHtmlOptions != null )
		{
			allOptions.putAll( defaultHtmlOptions.getOptions( ) );
		}

		// merge the user's setting
		allOptions.putAll( options.getOptions( ) );

		// copy the new setting to old APIs
		Map appContext = executionContext.getAppContext( );
		Object renderContext = appContext
				.get( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT );
		if ( renderContext == null )
		{
			HTMLRenderContext htmlContext = new HTMLRenderContext( );
			HTMLRenderOption htmlOptions = new HTMLRenderOption( allOptions );
			htmlContext.setBaseImageURL( htmlOptions.getBaseImageURL( ) );
			htmlContext.setBaseURL( htmlOptions.getBaseURL( ) );
			htmlContext.setImageDirectory( htmlOptions.getImageDirectory( ) );
			htmlContext.setSupportedImageFormats( htmlOptions
					.getSupportedImageFormats( ) );
			htmlContext.SetRenderOption( htmlOptions );
			appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					htmlContext );
		}

		// setup the instance id which is comes from the task.setInstanceId
		IDataExtractionOption extractOption = new DataExtractionOption(
				allOptions );
		if ( extractOption.getInstanceID( ) == null )
		{
			if ( instanceId != null )
			{
				extractOption.setInstanceID( instanceId );
			}
		}

		return extractOption;
	}
}
