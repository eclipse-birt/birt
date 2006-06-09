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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateTable;
import org.eclipse.birt.data.engine.impl.document.QueryResultInfo;
import org.eclipse.birt.data.engine.impl.document.RDLoad;
import org.eclipse.birt.data.engine.impl.document.RDUtil;
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
	
	/**
	 * @param dataEngine
	 * @param queryDefn
	 * @throws DataException
	 */
	PreparedIVDataSourceQuery( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn ) throws DataException
	{
		super( dataEngine, queryDefn, null, null );
		this.queryDefn = queryDefn;
		this.engine = dataEngine;
	}	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.PreparedDataSourceQuery#newExecutor()
	 */
	protected QueryExecutor newExecutor() 
	{
		return new IVDataSourceExecutor(engine.getSharedScope(), queryDefn,
				this.preparedQuery.getAggrTable( ));
	}

	/**
	 * Dummy implementation.
	 */
	public Collection getParameterMetaData() throws BirtException 
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.PreparedDataSourceQuery#execute(org.eclipse.birt.data.engine.api.IQueryResults,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public IQueryResults execute( IQueryResults outerResults, Scriptable scope )
			throws DataException
	{
		QueryResults queryResults = (QueryResults) super.execute( outerResults,
				scope );
		queryResults.setID( queryDefn.getQueryResultsID( ) );
		return queryResults;
	}
	
	/**
	 * 
	 *
	 */
	private class IVDataSourceExecutor extends QueryExecutor
	{
		Scriptable queryScope;
		BaseQuery query;
		DataSetRuntime dSruntime;
		
		IVDataSourceExecutor(Scriptable sharedScope,
				IBaseQueryDefinition baseQueryDefn, AggregateTable aggrTable) 
		{
			super(sharedScope, baseQueryDefn, aggrTable);
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiDataSource()
		 */
		protected IDataSource createOdiDataSource( ) throws DataException
		{
			return NewInstanceHelper.newDataSource( );
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#findDataSource()
		 */
		protected DataSourceRuntime findDataSource( ) throws DataException
		{			
			return NewInstanceHelper.newDataSourceRuntime( queryScope );
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#newDataSetRuntime()
		 */
		protected DataSetRuntime newDataSetRuntime( ) throws DataException
		{
			 dSruntime = new DataSetRuntime( NewInstanceHelper.newIVDataSetDesign( ),
					this );
			
			return dSruntime;
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.QueryExecutor#getResultMetaData()
		 */
		public IResultMetaData getResultMetaData( ) throws DataException
		{
			RDLoad rdLoad = RDUtil.newLoad( engine.getContext( ),
					new QueryResultInfo( queryDefn.getQueryResultsID( ),
							null,
							-1 ) );
			// TODO: enhanceme
			return rdLoad.loadResultMetaData( );
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiQuery()
		 */
		protected IQuery createOdiQuery( ) throws DataException
		{
			query = new IVQuery();
			return query;
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#executeOdiQuery()
		 */
		protected IResultIterator executeOdiQuery( IEventHandler eventHandler )
				throws DataException
		{
			RDLoad rdLoad = RDUtil.newLoad( engine.getContext( ),
					new QueryResultInfo( queryDefn.getQueryResultsID( ),
							null,
							-1 ) );
			DataSetResultSet exprDataRS = rdLoad.loadDataSetData();
			//IExprDataResultSet exprDataRS = rdLoad.loadExprDataResultSet();
			IResultClass meta = exprDataRS.getResultClass();
			
			IResultIterator resultIterator = new CachedResultSet(query,
					populateResultClass(meta), exprDataRS, eventHandler);
			exprDataRS.close();
			return resultIterator;
		}
		
		/**
		 * 
		 * @param meta
		 * @return
		 * @throws DataException
		 */
		private IResultClass populateResultClass(IResultClass meta) throws DataException 
		{
			List projectedColumns = new ArrayList();
			populateOriginalResultMetaToList(meta, projectedColumns);
			populateComputedColumnMetaToData(projectedColumns);
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

		private void populateComputedColumnMetaToData(List projectedColumns) {
			if( dataSet.getComputedColumns( )!= null)
			{
				for( int i = 0; i <dataSet.getComputedColumns( ).size( ); i++)
				{
					IComputedColumn cc = (IComputedColumn)dataSet.getComputedColumns( ).get(i);
					projectedColumns.add( new ResultFieldMetadata( i,
							cc.getName( ),
							cc.getName( ),
							DataType.getClass( cc.getDataType( ) ),
							null,
							true ) );
				}
			}
		}

		private void populateOriginalResultMetaToList(IResultClass meta, List projectedColumns) throws DataException {
			for ( int i = 1; i <= meta.getFieldCount(); i++ )
			{
				projectedColumns.add( new ResultFieldMetadata( i,
						meta.getFieldName( i ),
						meta.getFieldName( i ),
						meta.getFieldValueClass( i ),
						meta.getFieldNativeTypeName( i ),
						false ) );
			}
		}
	}
	
	/**
	 * 
	 *
	 */
	private class IVQuery extends BaseQuery
	{

		public void close() {
						
		}
		
	}
	
}



