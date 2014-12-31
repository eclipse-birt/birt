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

/**
 * 
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryLocator;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.impl.document.viewing.NewInstanceHelper;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.Scriptable;
/**
 * When query is applied with a different group from the original group, this
 * instance will be used to regenerate the query result.
 */
abstract class PreparedIVQuerySourceQuery extends PreparedDataSourceQuery
{

	protected DataEngineImpl engine;
	protected DataEngineImpl preDataEngine;
	protected IQueryResults queryResults;
	
	protected boolean hasBinding; 

	/**
	 * @param dataEngine
	 * @param queryDefn
	 * @throws DataException
	 */
	PreparedIVQuerySourceQuery( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, Map appContext, IQueryContextVisitor visitor ) throws DataException
	{
		super( dataEngine, queryDefn, null, appContext, visitor );
		Object[] params = {
				dataEngine, queryDefn
		};
		logger.entering( PreparedIVDataSourceQuery.class.getName( ),
				"PreparedIVNestedQuery",
				params );

		this.queryDefn = queryDefn;
		
		/* If the current query contains bindings, this variable hasBinding will be set as true. 
		Currently this variable is only used for getting distinct column values from a existed result set.*/ 
		if( this.queryDefn.getBindings( ).size( ) > 0 )
			hasBinding = true;
		else
			hasBinding = false;
		
		this.engine = dataEngine;
		prepareQuery( );
		preparedQuery = new PreparedQuery( dataEngine.getSession( ),
				dataEngine.getContext( ),
				this.queryDefn,
				this,
				appContext );
		logger.exiting( PreparedIVDataSourceQuery.class.getName( ),
				"PreparedIVNestedQuery" );
	}

	/**
	 * Prepare the column bindings.
	 * 
	 * @param queryDefn
	 * @throws DataException
	 */
	protected abstract void prepareQuery( )
			throws DataException;

	/**
	 * @throws BirtException 
	 * 
	 */
	protected void newPreDataEnige( ) throws BirtException
	{
		DataEngineContext parentContext = engine.getContext( );
		DataEngineContext newContext = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				parentContext.getScriptContext( ),
				parentContext.getDocReader( ),
				parentContext.getDocWriter( ),
				parentContext.getClassLoader( ) );
		String datasetName = ((IQueryDefinition) queryDefn.getSourceQuery( )).getDataSetName( );
		preDataEngine = (DataEngineImpl) DataEngine.newDataEngine( newContext );
		IBaseDataSetDesign dataSetDesign = engine.getDataSetDesign( datasetName );
		if( dataSetDesign != null )
		{
			IBaseDataSourceDesign datasourceDesign = engine.getDataSourceDesign( dataSetDesign.getDataSourceName( ) );
			if( datasourceDesign != null )
			{
				preDataEngine.defineDataSource( datasourceDesign );
			}
			preDataEngine.defineDataSet( engine.getDataSetDesign( datasetName ) );
		}
		
	}
	
	/**
	 * 
	 * @param queryDefinition
	 * @param subQueryName
	 * @return
	 * @throws DataException 
	 */
	protected static void getSubQueryBindings(
			IBaseQueryDefinition queryDefinition, String subQueryName, List<IBinding> resultBindingList ) throws DataException
	{
		List groups = queryDefinition.getGroups( );
		if ( groups != null )
		{
			for ( int i = 0; i < groups.size( ); i++ )
			{
				GroupDefinition groupDefinition = (GroupDefinition) groups.get( i );
				if ( groupDefinition.getSubqueries( ) != null )
				{
					SubqueryDefinition[] subqueryDefinitions = (SubqueryDefinition[]) groupDefinition.getSubqueries( )
							.toArray( new SubqueryDefinition[0] );
					getSubQueryBindings( subqueryDefinitions,
							subQueryName, resultBindingList );
					if ( resultBindingList.size( ) > 0 )
					{
						return;
					}
				}
			}
		}
		if( queryDefinition.getSubqueries( ) != null )
		{
			SubqueryDefinition[] subqueryDefinitions = (SubqueryDefinition[]) queryDefinition.getSubqueries( )
					.toArray( new SubqueryDefinition[0] );
			getSubQueryBindings( subqueryDefinitions, subQueryName, resultBindingList );
			if ( resultBindingList.size( ) > 0 )
			{
				return;
			}
		}
		return;
	}
	
	/**
	 * 
	 * @param resultBindingList
	 * @param bindingCollection
	 * @throws DataException 
	 */
	protected static void addQueryBindings( List<IBinding> resultBindingList,
			Map bindings ) throws DataException
	{
		Map<String, Boolean> aggrInfo = QueryDefinitionUtil.parseAggregations( bindings );
		Iterator it = bindings.entrySet( ).iterator( );
		while ( it.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) it.next( );
			String name = (String) entry.getKey( );

			if ( !aggrInfo.get( name ) )
			{
				{
					IBinding binding = (IBinding) entry.getValue( );
					boolean exist = false;
					for ( int i = 0; i < resultBindingList.size( ); i++ )
					{
						if ( resultBindingList.get( i ) != null
								&& resultBindingList.get( i ).getBindingName( ).equals( binding.getBindingName( ) ) )
						{
							exist = true;
						}
					}
					if ( !exist )
						resultBindingList.add( binding );
				}
			}
		}
	}
	
	/**
	 * 
	 * @param subqueryDefinitions
	 * @param subQueryName
	 * @return
	 * @throws DataException 
	 */
	private static void getSubQueryBindings(
			SubqueryDefinition[] subqueryDefinitions, String subQueryName, List<IBinding> resultBindingList ) throws DataException
	{
		for ( int j = 0; j < subqueryDefinitions.length; j++ )
		{
			if ( subqueryDefinitions[j].getName( ) != null
					&& subqueryDefinitions[j].getName( )
							.equals( subQueryName ) )
			{
				addQueryBindings( resultBindingList, subqueryDefinitions[j].getBindings( ) );
				return;
			}
			getSubQueryBindings( subqueryDefinitions[j], subQueryName, resultBindingList );
			if ( resultBindingList.size( ) > 0 )
			{
				addQueryBindings( resultBindingList, subqueryDefinitions[j].getBindings( ) );
				return;
			}
		}
		return;
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.PreparedDataSourceQuery#newExecutor()
	 */
	protected QueryExecutor newExecutor( )
	{
		return new IVQuerySourceExecutor( engine.getSession( ).getSharedScope( ) );
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
		return queryResults;
	}

	/**
	 * 
	 *
	 */
	protected class IVQuerySourceExecutor extends QueryExecutor
	{

		private Scriptable queryScope;
		protected BaseQuery query;
		private DataSetRuntime dsRuntime;
		protected IResultClass resultClass;

		/**
		 * @param sharedScope
		 * @param baseQueryDefn
		 * @param aggrTable
		 */
		IVQuerySourceExecutor( Scriptable sharedScope )
		{
			super( preparedQuery.getSharedScope( ),
					preparedQuery.getBaseQueryDefn( ),
					preparedQuery.getAggrTable( ),
					dataEngine.getSession( ), PreparedIVQuerySourceQuery.this.contextVisitor );
			ignoreDataSetFilter = true;
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
		 * @see org.eclipse.birt.data.engine.impl.QueryExecutor#findDataSource()
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
		
		protected String getDataSetName( )
		{
			return queryDefn.getDataSetName( );
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.QueryExecutor#getResultMetaData()
		 */
		public IResultMetaData getResultMetaData( ) throws DataException
		{
			return new ResultMetaData( getResultClass( ) );
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiQuery
		 * ()
		 */
		protected IQuery createOdiQuery( ) throws DataException
		{
			query = new IVQuerySourceQuery( getResultClass( ) );
			return query;
		}

		/**
		 * 
		 * @return
		 * @throws DataException
		 */
		protected IResultClass getResultClass( ) throws DataException
		{
			IBinding[] bindings = null;
			IQueryDefinition queryDefinition = queryResults.getPreparedQuery( )
					.getReportQueryDefn( );

			if ( queryDefn.getSourceQuery( ) instanceof SubqueryLocator )
			{
				ArrayList<IBinding> bindingList = new ArrayList<IBinding>( );
				getSubQueryBindings( queryDefinition,
						( (SubqueryLocator) queryDefn.getSourceQuery( ) ).getName( ), bindingList );
				addQueryBindings( bindingList, queryDefinition.getBindings( ) );
				bindings = bindingList.toArray( new IBinding[0] );
			}
			else
			{
				bindings = (IBinding[]) ( queryDefinition.getBindings( )
						.values( ).toArray( new IBinding[0] ) );
			}
			if( hasBinding )
			{
				if( queryDefinition.needAutoBinding( ) )
				{
					try
					{
						IResultMetaData metaData = queryResults.getResultMetaData( );
						bindings = new IBinding[ metaData.getColumnCount( ) ];
						for ( int i = 1; i <= metaData.getColumnCount( ); i++ )
						{
							String colName = metaData.getColumnName( i );
							if ( ServiceForQueryResults.isTempColumn( colName ))
							{
								continue;
							}
							ScriptExpression baseExpr = new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( colName ),
									metaData.getColumnType( i ) );
							bindings[i-1] = new Binding( colName, baseExpr );
						}
					}
					catch ( BirtException e1 )
					{
						throw DataException.wrap( e1 );
					}
					resultClass = createResultClass( bindings, temporaryComputedColumns );
				}
				else
					resultClass = createResultClass( bindings, temporaryComputedColumns );
			}
			else
				resultClass = createResultClass( bindings, temporaryComputedColumns );

			return resultClass;
		}
		

		/**
		 * 
		 * @param exprMetaInfo
		 * @param temporaryComputedColumns
		 * @return
		 * @throws DataException
		 */
		private IResultClass createResultClass( IBinding[] bindings,
				List temporaryComputedColumns ) throws DataException
		{
			ResultFieldMetadata rfm = null;
			ArrayList<ResultFieldMetadata> projectedColumns = new ArrayList<ResultFieldMetadata>( );
			if ( bindings != null )
			{
				for ( int i = 0; i < bindings.length; i++ )
				{
					Class result = null;
					result = DataType.getClass( bindings[i].getDataType( ) );
					if ( result == null )
					{
						result = String.class;
					}
					rfm = new ResultFieldMetadata( i,
							bindings[i].getBindingName( ),
							"",
							result,
							null,
							false, -1 );
					projectedColumns.add( rfm );
				}
			}
			for ( int i = 0; i < temporaryComputedColumns.size( ); i++ )
			{
				IComputedColumn computedColumn = (IComputedColumn) temporaryComputedColumns.get( i );
				Class result = DataType.getClass( computedColumn.getDataType( ) );
				if ( result == null )
				{
					result = String.class;
				}
				rfm = new ResultFieldMetadata( i,
						computedColumn.getName( ),
						"",
						result,
						null,
						true, -1 );
				projectedColumns.add( rfm );
			}
			return new ResultClass( projectedColumns );
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#executeOdiQuery
		 * ()
		 */
		protected IResultIterator executeOdiQuery( IEventHandler eventHandler ) throws DataException
		{

			IResultIterator resultIterator;
			try
			{
				org.eclipse.birt.data.engine.api.IResultIterator sourceResultIterator = queryResults.getResultIterator( );
				if ( queryDefn.getSourceQuery( ) instanceof SubqueryLocator )
				{
					sourceResultIterator = getSubQueryIterator( (SubqueryLocator) queryDefn.getSourceQuery( ),
							sourceResultIterator );
				}
				IDataSetPopulator querySourcePopulator = new IVQuerySourcePopulator( sourceResultIterator,
						getResultClass( ),
						query,
						queryDefn.getStartingRow( ) );
				resultIterator = new CachedResultSet( query,
						resultClass,
						querySourcePopulator,
						eventHandler,
						engine.getSession( ) );
				return resultIterator;
			}
			catch ( BirtException e )
			{
				throw DataException.wrap( e );
			}

		}
		
		/**
		 * 
		 * @param subqueryLocator
		 * @param sourceResultIterator
		 * @return
		 * @throws BirtException 
		 */
		private org.eclipse.birt.data.engine.api.IResultIterator getSubQueryIterator(
				SubqueryLocator subqueryLocator,
				org.eclipse.birt.data.engine.api.IResultIterator sourceResultIterator )
				throws BirtException
		{
			org.eclipse.birt.data.engine.api.IResultIterator resultIterator = sourceResultIterator;
			if ( subqueryLocator.getParentQuery( ) != null && subqueryLocator.getParentQuery( ) instanceof SubqueryLocator )
			{
				resultIterator = getSubQueryIterator( (SubqueryLocator) subqueryLocator.getParentQuery( ),
						sourceResultIterator );
			}
			if ( subqueryLocator.getRowId( ) <= -1 && resultIterator.isEmpty( ) )
			{
				return resultIterator.getSecondaryIterator( subqueryLocator.getName( ), queryScope );
			}
			else
				resultIterator.moveTo( subqueryLocator.getRowId( ) );
			return resultIterator.getSecondaryIterator( subqueryLocator.getName( ), queryScope );
		}

	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	static class IVQuerySourcePopulator implements IDataSetPopulator
	{

		private org.eclipse.birt.data.engine.api.IResultIterator apiResultIterator = null;
		private IResultClass resultClass = null;
		private String[] fieldNames = null;

		IVQuerySourcePopulator(
				org.eclipse.birt.data.engine.api.IResultIterator apiResultIterator,
				IResultClass resultClass, BaseQuery query, int startingRow )
				throws DataException
		{
			this.apiResultIterator = apiResultIterator;
			this.resultClass = resultClass;
			fieldNames = resultClass.getFieldNames( );
			if( startingRow > 0 )
			{
				try
				{
					this.apiResultIterator.moveTo( startingRow - 1 );
				}
				catch ( BirtException e )
				{
					throw DataException.wrap( e );
				}
			}
		}

		/**
		 * 
		 */
		public IResultObject next( ) throws DataException
		{
			try
			{
				if ( !apiResultIterator.next( ) )
				{
					return null;
				}
			}
			catch ( BirtException e )
			{
				throw DataException.wrap( e );
			}
			Object[] fields = new Object[fieldNames.length];
			for ( int i = 0; i < fields.length; i++ )
			{
				try
				{
					if ( !resultClass.isCustomField( i + 1 ) )
					{
						fields[i] = apiResultIterator.getValue( fieldNames[i] );
					}
				}
				catch ( BirtException e )
				{
					throw DataException.wrap( e );
				}
			}
			IResultObject result = new ResultObject( resultClass, fields );

			return result;
		}

	}

	/**
	 * 
	 */
	public class IVQuerySourceQuery extends BaseQuery
	{

		//
		private IResultClass meta;

		/**
		 * Constructor
		 * 
		 * @param resultClass
		 */
		public IVQuerySourceQuery( IResultClass resultClass )
		{
			meta = resultClass;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.odi.IQuery#close()
		 */
		public void close( )
		{
			if( preDataEngine != null )
				preDataEngine.shutdown( );
		}

		/**
		 * Return the result class of this joint data set.
		 * 
		 * @return
		 */
		public IResultClass getResultClass( )
		{
			return meta;
		}

	}

}
