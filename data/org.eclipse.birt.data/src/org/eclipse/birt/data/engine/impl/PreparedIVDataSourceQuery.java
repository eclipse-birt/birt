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

package org.eclipse.birt.data.engine.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.cache.CacheRequest;
import org.eclipse.birt.data.engine.executor.cache.OdiAdapter;
import org.eclipse.birt.data.engine.executor.cache.SmartCache;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateTable;
import org.eclipse.birt.data.engine.impl.document.PLSEnabledDataSetPopulator;
import org.eclipse.birt.data.engine.impl.document.QueryResultIDManager;
import org.eclipse.birt.data.engine.impl.document.QueryResultIDUtil;
import org.eclipse.birt.data.engine.impl.document.QueryResultInfo;
import org.eclipse.birt.data.engine.impl.document.RDLoad;
import org.eclipse.birt.data.engine.impl.document.RDUtil;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.viewing.DataSetResultSet;
import org.eclipse.birt.data.engine.impl.document.viewing.NewInstanceHelper;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * When query is applied with a different group from the original group, this
 * instance will be used to regenerate the query result.
 */
class PreparedIVDataSourceQuery extends PreparedDataSourceQuery
{

	private DataEngineImpl engine;
	private IQueryDefinition queryDefn;

	private String realBasedQueryID;

	/**
	 * @param dataEngine
	 * @param queryDefn
	 * @throws DataException
	 */
	PreparedIVDataSourceQuery( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, IQueryContextVisitor visitor ) throws DataException
	{
		super( dataEngine, PLSUtil.isPLSEnabled( queryDefn )
				? PLSUtil.populateBindings( queryDefn ) : queryDefn, null, null, visitor );
		Object[] params = {
				dataEngine, queryDefn
		};
		logger.entering( PreparedIVDataSourceQuery.class.getName( ),
				"PreparedIVDataSourceQuery",
				params );

		this.queryDefn = queryDefn;
		this.engine = dataEngine;

		if(!PLSUtil.isPLSEnabled( queryDefn ))
			cleanUpOldRD();
		logger.exiting( PreparedIVDataSourceQuery.class.getName( ),
				"PreparedIVDataSourceQuery" );
	}

	/**
	 * Since this query is running based on the data set, the old things stored
	 * in report document is no more use, and it will be safter if they are all
	 * removed.
	 * 
	 * @throws DataException
	 */
	private void cleanUpOldRD( ) throws DataException
	{

		String basedID = this.queryDefn.getQueryResultsID( );
		String _1partID = QueryResultIDUtil.get1PartID( basedID );
		if ( _1partID != null )
			basedID = _1partID;

		// remove EXPR_VALUE_STREAM, EXPR_META_STREAM, EXPR_ROWLEN_STREAM
		StreamManager streamManager = new StreamManager( engine.getContext( ),
				new QueryResultInfo( queryDefn.getQueryResultsID( ),
						null,
						0 ) );

		streamManager.dropStream1( DataEngineContext.EXPR_VALUE_STREAM );
		streamManager.dropStream1( DataEngineContext.EXPR_ROWLEN_STREAM );
		// remove QUERYID_INFO_STREAM
		QueryResultIDManager.cleanChildOfRoot( streamManager );
		streamManager.dropStream1( DataEngineContext.META_STREAM );
		streamManager.dropStream1( DataEngineContext.META_INDEX_STREAM );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.PreparedDataSourceQuery#newExecutor()
	 */
	protected QueryExecutor newExecutor( )
	{
		return new IVDataSourceExecutor( engine.getSession( ).getSharedScope( ),
				queryDefn,
				this.preparedQuery.getAggrTable( ) );
	}

	/**
	 * Dummy implementation.
	 */
	public Collection getParameterMetaData( ) throws BirtException
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.PreparedDataSourceQuery#initializeExecution
	 * (org.eclipse.birt.data.engine.api.IBaseQueryResults,
	 * org.mozilla.javascript.Scriptable)
	 */
	protected void initializeExecution( IBaseQueryResults outerResults,
			Scriptable scope ) throws DataException
	{
		String basedID = queryDefn.getQueryResultsID( );

		String _1partID = QueryResultIDUtil.get1PartID( basedID );
		if ( _1partID == null )
			realBasedQueryID = basedID;
		else
			realBasedQueryID = _1partID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.PreparedDataSourceQuery#produceQueryResults
	 * (org.eclipse.birt.data.engine.api.IBaseQueryResults,
	 * org.mozilla.javascript.Scriptable)
	 */
	protected IQueryResults produceQueryResults(
			IBaseQueryResults outerResults, Scriptable scope )
			throws DataException
	{
		QueryResults queryResults = preparedQuery.doPrepare( outerResults,
				scope,
				newExecutor( ),
				this );
		queryResults.setID( realBasedQueryID );
		return queryResults;
	}

	/**
	 * 
	 *
	 */
	private class IVDataSourceExecutor extends QueryExecutor
	{

		private Scriptable queryScope;
		private BaseQuery query;
		private DataSetRuntime dsRuntime;

		/**
		 * @param sharedScope
		 * @param baseQueryDefn
		 * @param aggrTable
		 */
		IVDataSourceExecutor( Scriptable sharedScope,
				IBaseQueryDefinition baseQueryDefn, AggregateTable aggrTable )
		{
			super( sharedScope,
					baseQueryDefn,
					aggrTable,
					engine.getSession( ),
					PreparedIVDataSourceQuery.this.contextVisitor );
		}

		/*
		 * @seeorg.eclipse.birt.data.engine.impl.PreparedQuery.Executor#
		 * createOdiDataSource()
		 */
		protected IDataSource createOdiDataSource( ) throws DataException
		{
			return NewInstanceHelper.newDataSource( );
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#findDataSource
		 * ()
		 */
		protected DataSourceRuntime findDataSource( ) throws DataException
		{
			return NewInstanceHelper.newDataSourceRuntime( queryScope );
		}

		/*
		 * @seeorg.eclipse.birt.data.engine.impl.PreparedQuery.Executor#
		 * newDataSetRuntime()
		 */
		protected DataSetRuntime newDataSetRuntime( ) throws DataException
		{
			dsRuntime = new DataSetRuntime( NewInstanceHelper.newIVDataSetDesign( ),
					this, this.getSession( ) );

			return dsRuntime;
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.QueryExecutor#getResultMetaData()
		 */
		public IResultMetaData getResultMetaData( ) throws DataException
		{
			RDLoad rdLoad = RDUtil.newLoad( engine.getSession( ).getTempDir( ),
					engine.getContext( ),
					new QueryResultInfo( realBasedQueryID, null, -1 ) );
			// TODO: enhanceme
			return rdLoad.loadResultMetaData( );
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiQuery
		 * ()
		 */
		protected IQuery createOdiQuery( ) throws DataException
		{
			query = NewInstanceHelper.newBaseQuery( );
			return query;
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#executeOdiQuery
		 * ()
		 */
		protected IResultIterator executeOdiQuery( IEventHandler eventHandler ) throws DataException
		{
			try
			{
				RDLoad rdLoad = RDUtil.newLoad( engine.getSession( )
						.getTempDir( ),
						engine.getContext( ),
						new QueryResultInfo( realBasedQueryID, null, -1 ) );
				DataSetResultSet dataSetResult = rdLoad.loadDataSetData( );
				StreamManager manager = new StreamManager( engine.getContext( ),
						new QueryResultInfo( queryDefn.getQueryResultsID( ),
								null,
								0 ) );
				if ( PLSUtil.isPLSEnabled( queryDefn ) )
				{
					if ( PLSUtil.needUpdateDataSet( queryDefn, manager ) )
					{
						if ( engine.getContext( ).getDocWriter( ) != null )
						{
							// When we can update the data set data.
							populatePLSDataSetData( eventHandler,
									manager );

							dataSetResult.close( );

							rdLoad = RDUtil.newLoad( engine.getSession( )
									.getTempDir( ),
									engine.getContext( ),
									new QueryResultInfo( realBasedQueryID,
											null,
											-1 ) );

							dataSetResult = rdLoad.loadDataSetData( );
						}
						else
						{
							// Indicate that we need not update the report
							// document.
							org.eclipse.birt.data.engine.impl.document.ResultIterator docIt = new org.eclipse.birt.data.engine.impl.document.ResultIterator( engine.getSession( )
									.getTempDir( ),
									engine.getContext( ),
									null,
									queryDefn.getQueryResultsID( ) );
							PLSEnabledDataSetPopulator populator = new PLSEnabledDataSetPopulator( queryDefn,
									queryDefn.getQueryExecutionHints( )
											.getTargetGroupInstances( ),
									docIt );
							IResultIterator resultIterator = new CachedResultSet( query,
									populateResultClass( populator.getResultClass( ) ),
									populator,
									eventHandler,
									engine.getSession( ) );
							dataSetResult.close( );
							cleanUpOldRD( );
							return resultIterator;
						}
					}
					else
					{
						cleanUpOldRD( );
					}
				}
				
				IResultClass meta = dataSetResult.getResultClass( );
				IResultIterator resultIterator = new CachedResultSet( query,
						populateResultClass( meta ),
						dataSetResult,
						eventHandler,
						engine.getSession( ) );
				dataSetResult.close( );

				return resultIterator;
			}
			catch ( IOException e )
			{
				throw new DataException( e.getLocalizedMessage( ) );
			}
		}

		/**
		 * 
		 * @param eventHandler
		 * @param stopSign
		 * @param manager
		 * @throws DataException
		 * @throws IOException
		 */
		private void populatePLSDataSetData( IEventHandler eventHandler, StreamManager manager )
				throws DataException, IOException
		{
			org.eclipse.birt.data.engine.impl.document.ResultIterator docIt = new org.eclipse.birt.data.engine.impl.document.ResultIterator( engine.getSession( )
					.getTempDir( ),
					engine.getContext( ),
					null,
					queryDefn.getQueryResultsID( ) );

			PLSEnabledDataSetPopulator populator = new PLSEnabledDataSetPopulator( queryDefn,
					queryDefn.getQueryExecutionHints( )
							.getTargetGroupInstances( ),
					docIt );

			ResultClass processedRC = (ResultClass) populateResultClass( populator.getResultClass( ) );
			
			SmartCache cache = new SmartCache( new CacheRequest( 0,
					new ArrayList( ),
					null,
					eventHandler ),
					new OdiAdapter( populator ),
					processedRC,
					engine.getSession( ) );
			
			manager.dropStream1( DataEngineContext.DATASET_DATA_STREAM );
			manager.dropStream1( DataEngineContext.DATASET_DATA_LEN_STREAM );
			cleanUpOldRD();
			OutputStream resultClassStream = manager.getOutStream( DataEngineContext.DATASET_META_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE );
			processedRC.doSave( resultClassStream,
					new ArrayList( queryDefn.getBindings( ).values( ) ) );

			resultClassStream.close( );


			DataOutputStream dataSetDataStream = new DataOutputStream( manager.getOutStream( DataEngineContext.DATASET_DATA_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE ) );
			DataOutputStream rowLensStream = new DataOutputStream( manager.getOutStream( DataEngineContext.DATASET_DATA_LEN_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE ) );

			cache.doSave( dataSetDataStream,
					rowLensStream,
					eventHandler.getAllColumnBindings( ) );
			cache.close( );

			DataOutputStream plsGroupLevelStream = new DataOutputStream( manager.getOutStream( DataEngineContext.PLS_GROUPLEVEL_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE ) );

			IOUtil.writeInt( plsGroupLevelStream,
					PLSUtil.getOutmostPlsGroupLevel( queryDefn ) );
			plsGroupLevelStream.close( );
		}

		/**
		 * 
		 * @param meta
		 * @return
		 * @throws DataException
		 */
		private IResultClass populateResultClass( IResultClass meta )
				throws DataException
		{
			List projectedColumns = new ArrayList( );
			addOriginalMetadata( meta, projectedColumns );
			addComputedColumn( projectedColumns );
			if ( dataSet.getResultSetHints( ) != null )
			{
				List hintList = dataSet.getResultSetHints( );
				for ( int i = 0; i < hintList.size( ); i++ )
				{
					IColumnDefinition columnDefinition = (IColumnDefinition) hintList.get( i );
					for ( int j = 0; j < projectedColumns.size( ); j++ )
					{
						ResultFieldMetadata resultFieldMetadata = (ResultFieldMetadata) projectedColumns.get( j );
						if ( columnDefinition.getColumnName( )
								.equals( resultFieldMetadata.getName( ) ) )
						{
							resultFieldMetadata.setAlias( columnDefinition.getAlias( ) );
							break;
						}
					}
				}
			}
			return new ResultClass( projectedColumns );
		}

		/**
		 * @param projectedColumns
		 */
		private void addComputedColumn( List projectedColumns )
		{
			if ( dataSet.getComputedColumns( ) != null )
			{
				for ( int i = 0; i < dataSet.getComputedColumns( ).size( ); i++ )
				{
					IComputedColumn cc = (IComputedColumn) dataSet.getComputedColumns( )
							.get( i );
					projectedColumns.add( new ResultFieldMetadata( i,
							cc.getName( ),
							cc.getName( ),
							DataType.getClass( cc.getDataType( ) ),
							null,
							true ) );
				}
			}
		}

		/**
		 * @param meta
		 * @param projectedColumns
		 * @throws DataException
		 */
		private void addOriginalMetadata( IResultClass meta,
				List projectedColumns ) throws DataException
		{
			for ( int i = 1; i <= meta.getFieldCount( ); i++ )
			{
				ResultFieldMetadata rfm = new ResultFieldMetadata( i,
						meta.getFieldName( i ),
						meta.getFieldName( i ),
						meta.getFieldValueClass( i ),
						meta.getFieldNativeTypeName( i ),
						false );
				rfm.setAlias( meta.getFieldAlias( i ) );
				projectedColumns.add( rfm );
			}
		}
	}

}
