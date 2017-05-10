/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.report.data.adapter.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.impl.CubeCreationQueryDefinition;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.MemoryUsageSetting;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDerivedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.CubeElementFactory;
import org.eclipse.birt.data.engine.olap.data.api.cube.CubeMaterializer;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IColumnValueIterator;
import org.eclipse.birt.report.data.adapter.api.ICubeInterceptor;
import org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil;
import org.eclipse.birt.report.data.adapter.api.IDataSetInterceptor;
import org.eclipse.birt.report.data.adapter.api.IDataSetInterceptorContext;
import org.eclipse.birt.report.data.adapter.api.IFilterUtil;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter.ExpressionLocation;
import org.eclipse.birt.report.data.adapter.api.IQueryDefinitionUtil;
import org.eclipse.birt.report.data.adapter.api.IRequestInfo;
import org.eclipse.birt.report.data.adapter.group.GroupCalculatorFactory;
import org.eclipse.birt.report.data.adapter.i18n.AdapterResourceHandle;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.data.adapter.impl.DataSetIterator.ColumnMeta;
import org.eclipse.birt.report.data.adapter.impl.DataSetIterator.IDataProcessor;
import org.eclipse.birt.report.data.adapter.impl.QueryExecutionHelper.DataSetHandleProcessContext;
import org.eclipse.birt.report.data.adapter.internal.adapter.GroupAdapter;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularCubeModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularLevelModel;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;
import org.mozilla.javascript.Scriptable;

/**
 * Implementation of DataRequestSession
 */
public class DataRequestSessionImpl extends DataRequestSession
{
	private static Logger logger = Logger.getLogger( DataRequestSessionImpl.class.getName( ) );
	//
	private DataEngineImpl dataEngine;
	private IModelAdapter modelAdaptor;
	private DataSessionContext sessionContext;
	private Map cubeHandleMap, cubeMetaDataHandleMap;

	private Map<ReportElementHandle, QueryDefinition> cubeQueryMap = new HashMap<ReportElementHandle, QueryDefinition>();
	private Map<ReportElementHandle, List<ColumnMeta>> cubeMetaMap = new HashMap<ReportElementHandle, List<ColumnMeta>>();
	//Used to avoid creating same dimension repeatedly when a dimension is shared by multiple cubes
	private Map<String, IDimension> createdDimensions;

	private CubeMaterializer cubeMaterializer;
	private IDataQueryDefinition[] registeredQueries;
	private IDataSetInterceptorContext interceptorContext;

	private CubeMaterializer getCubeMaterializer( int cacheSize ) throws BirtException
	{
		//Make sure only one instance, do not created until really needed
		if ( cubeMaterializer == null )
		{
			try
			{
				cubeMaterializer = new CubeMaterializer( this.dataEngine,
						String.valueOf( dataEngine.hashCode( )), cacheSize );
			}
			catch ( IOException e )
			{
				throw new DataException( e.getLocalizedMessage( ), e );
			}
		}
		return cubeMaterializer;
	}

	/**
	 * Constructs the data request session with the provided session context
	 * information.
	 *
	 * @param context
	 * @throws BirtException
	 */
	public DataRequestSessionImpl( DataSessionContext context )
			throws BirtException
	{
		if ( context == null )
			throw new AdapterException( ResourceConstants.CONEXT_NULL_ERROR );

		dataEngine = (DataEngineImpl)DataEngine.newDataEngine( context.getDataEngineContext( ) );
		modelAdaptor = new DataModelAdapter( context );

		sessionContext = context;
		cubeHandleMap = new HashMap( );
		cubeMetaDataHandleMap = new HashMap( );
		createdDimensions = new HashMap<String, IDimension>( );
		interceptorContext = new DataSetInterceptorContext( );
		if( sessionContext!= null )
		{
			this.setModuleHandleToAppContext();
		}
		DataSessionConfig.startup( this );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#defineDataSource(org.eclipse.birt.data.engine.api.IBaseDataSourceDesign)
	 */
	public void defineDataSource( IBaseDataSourceDesign design )
			throws BirtException
	{
		dataEngine.defineDataSource( design );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#defineDataSet(org.eclipse.birt.data.engine.api.IBaseDataSetDesign)
	 */
	public void defineDataSet( IBaseDataSetDesign design ) throws BirtException
	{
		dataEngine.defineDataSet( design );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.api.DataRequestSession#getDataSetMetaData(java.lang.String,
	 *      boolean)
	 */
	public IResultMetaData getDataSetMetaData( String dataSetName,
			boolean useCache ) throws BirtException
	{
		return getDataSetMetaData( this.sessionContext.getModuleHandle( )
				.findDataSet( dataSetName ), useCache );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.api.DataRequestSession#getDataSetMetaData(org.eclipse.birt.report.model.api.DataSetHandle,
	 *      boolean)
	 */
	public IResultMetaData getDataSetMetaData( DataSetHandle dataSetHandle,
			boolean useCache ) throws BirtException
	{
		return new DataSetMetaDataHelper( this.dataEngine,
				this.modelAdaptor,
				this.sessionContext,
				this ).getDataSetMetaData( dataSetHandle, useCache );
	}

	/*
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getColumnValueSet(org.eclipse.birt.report.model.api.DataSetHandle,
	 *      java.util.Iterator, java.util.Iterator, java.lang.String)
	 */
	public Collection getColumnValueSet( DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			String boundColumnName ) throws BirtException
	{
		return getColumnValueSet( dataSet,
				inputParamBindings,
				columnBindings,
				boundColumnName,
				null );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getColumnValueIterator(org.eclipse.birt.report.model.api.DataSetHandle, java.util.Iterator, java.util.Iterator, java.lang.String)
	 */
	public IColumnValueIterator getColumnValueIterator( DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			String boundColumnName ) throws BirtException
	{
		return this.getColumnValueIterator( dataSet,
				inputParamBindings,
				columnBindings,
				null,
				boundColumnName,
				true,
				null );
	}

	/**
	 *
	 * @param dataSet
	 * @param inputParamBindings
	 * @param columnBindings
	 * @param groupDefn
	 * @param boundColumnName
	 * @param requestInfo
	 * @return
	 * @throws BirtException
	 */
	private IColumnValueIterator getColumnValueIterator( DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			Iterator groupDefn, String boundColumnName, boolean useDataSetFilter, IRequestInfo requestInfo )
			throws BirtException
	{
		IQueryResults queryResults = getQueryResults( dataSet,
				inputParamBindings,
				columnBindings,
				groupDefn,
				boundColumnName, useDataSetFilter  );
		return new ColumnValueIterator( queryResults,
				boundColumnName,
				requestInfo );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getColumnValueSet(org.eclipse.birt.report.model.api.DataSetHandle, java.util.Iterator, java.util.Iterator, java.lang.String, int, int)
	 */
	public Collection getColumnValueSet( DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			String boundColumnName, IRequestInfo requestInfo )
			throws BirtException
	{
		IColumnValueIterator columnValueIterator = getColumnValueIterator( dataSet,
				inputParamBindings,
				columnBindings,
				null,
				boundColumnName,
				true,
				requestInfo );

		ArrayList values = new ArrayList( );

		do
		{
//			if ( columnValueIterator.getValue()!=null )
				values.add( columnValueIterator.getValue( ) );
		}while(  columnValueIterator.next( )  );

		columnValueIterator.close( );

		return values;
	}

	/*
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getColumnValueSet(org.eclipse.birt.report.model.api.DataSetHandle, java.util.Iterator, java.util.Iterator, java.util.Iterator, java.lang.String, org.eclipse.birt.report.data.adapter.api.IRequestInfo)
	 */
	public Collection getColumnValueSet( DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			Iterator groupDefns, String boundColumnName,
			IRequestInfo requestInfo ) throws BirtException
	{
		IColumnValueIterator columnValueIterator = getColumnValueIterator( dataSet,
				inputParamBindings,
				columnBindings,
				groupDefns,
				boundColumnName,
				true,
				requestInfo );

		ArrayList values = new ArrayList( );

		do
		{
//			if ( columnValueIterator.getValue( ) != null )
				values.add( columnValueIterator.getValue( ) );
		} while ( columnValueIterator.next( ) );

		columnValueIterator.close( );
		return values;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getColumnValueSet(org.eclipse.birt.report.model.api.DataSetHandle, java.util.Iterator, java.util.Iterator, java.util.Iterator, java.lang.String, boolean, org.eclipse.birt.report.data.adapter.api.IRequestInfo)
	 */
	public Collection getColumnValueSet( DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			Iterator groupDefns, String boundColumnName,
			boolean useDataSetFilter, IRequestInfo requestInfo )
			throws BirtException
	{
		IColumnValueIterator columnValueIterator = getColumnValueIterator( dataSet,
				inputParamBindings,
				columnBindings,
				groupDefns,
				boundColumnName, useDataSetFilter,
				requestInfo );

		ArrayList values = new ArrayList( );

		do
		{
//			if ( columnValueIterator.getValue( ) != null )
				values.add( columnValueIterator.getValue( ) );
		} while ( columnValueIterator.next( ) );

		columnValueIterator.close( );
		return values;
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.api.DataRequestSession#refreshMetaData(org.eclipse.birt.report.model.api.DataSetHandle)
	 */
	public IResultMetaData refreshMetaData( DataSetHandle dataSetHandle )
			throws BirtException
	{
		return new DataSetMetaDataHelper( this.dataEngine,
				this.modelAdaptor,
				this.sessionContext,
				this ).refreshMetaData( dataSetHandle );
	}

	/*
	 *
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#refreshMetaData(org.eclipse.birt.report.model.api.DataSetHandle,
	 *      boolean)
	 */
	public IResultMetaData refreshMetaData( DataSetHandle dataSetHandle,
			boolean holdEvent ) throws BirtException
	{
		return new DataSetMetaDataHelper( this.dataEngine,
				this.modelAdaptor,
				this.sessionContext,
				this ).refreshMetaData( dataSetHandle, holdEvent );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.api.DataRequestSession#executeQuery(org.eclipse.birt.data.engine.api.IQueryDefinition,
	 *      java.util.Iterator, java.util.Iterator, java.util.Iterator)
	 */
	public IQueryResults executeQuery( IQueryDefinition queryDefn,
			Iterator paramBindingIt, Iterator filterIt, Iterator bindingIt )
			throws BirtException
	{
		return new QueryExecutionHelper( this.dataEngine,
				this.modelAdaptor,
				this.sessionContext,
				this ).executeQuery( queryDefn,
				paramBindingIt,
				filterIt,
				bindingIt,
				this.sessionContext.getTopScope( ),
				this.interceptorContext );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#getQueryResults(java.lang.String)
	 */
	public IQueryResults getQueryResults( String queryResultID )
			throws BirtException
	{
		return dataEngine.getQueryResults( queryResultID );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#clearCache(org.eclipse.birt.data.engine.api.IBaseDataSourceDesign,
	 *      org.eclipse.birt.data.engine.api.IBaseDataSetDesign)
	 */
	public void clearCache( IBaseDataSourceDesign dataSource,
			IBaseDataSetDesign dataSet ) throws BirtException
	{
		dataEngine.clearCache( dataSource, dataSet );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#clearCache(java.lang.String)
	 */
	public void clearCache( String cacheID ) throws BirtException
	{
		dataEngine.clearCache( cacheID );
	}
	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#prepare(org.eclipse.birt.data.engine.api.IQueryDefinition,
	 *      java.util.Map)
	 */
	public IPreparedQuery prepare( IQueryDefinition query, Map appContext )
			throws BirtException
	{
		QueryAdapter.adaptQuery( query );
		defineDataSourceDataSet( query );
		if ( appContext == null )
			// Use session app context
			appContext = sessionContext.getAppContext( );
		setModuleHandleToAppContext( appContext );

		return dataEngine.prepare( query, appContext );
	}

	/*
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#prepare(org.eclipse.birt.data.engine.api.IQueryDefinition)
	 */
	public IPreparedQuery prepare( IQueryDefinition query )
			throws BirtException
	{
		// Use session app context
		return prepare( query, null );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#closeDataSource(java.lang.String)
	 */
	public void closeDataSource( String dataSourceName ) throws BirtException
	{
		dataEngine.closeDataSource( dataSourceName );
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#getModelAdaptor()
	 */
	public IModelAdapter getModelAdaptor( )
	{
		return modelAdaptor;
	}

	/*
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#shutdown()
	 */
	public void shutdown( )
	{
		if ( cubeMaterializer != null )
		{
			try
			{
				cubeMaterializer.close( );
			}
			catch ( IOException e )
			{
				logger.log( Level.WARNING, e.getLocalizedMessage( ), e );
			}
		}
		if( interceptorContext != null )
		{
			interceptorContext.close( );
		}
		DataSessionConfig.finalize( this );
		if ( dataEngine != null )
		{
			dataEngine.shutdown( );
			dataEngine = null;
		}
	}

	/**
	 * get the distinct value of query
	 * @param dataSet
	 * @param inputParamBindings
	 * @param columnBindings
	 * @param boundColumnName
	 * @return
	 * @throws BirtException
	 */
	private IQueryResults getQueryResults( DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			Iterator groupDefns, String boundColumnName, boolean useDataSetFilter ) throws BirtException
	{
		assert dataSet != null;
		// TODO: this is the inefficient implementation
		// Need to enhance the implementation to verify that the column is bound
		// to a data set column

		// Run a query with the provided binding information. Group by bound
		// column so we can
		// retrieve distinct values using the grouping feature
		QueryDefinition query = null;
		
		if ( columnBindings == null || !columnBindings.hasNext( ) )
		{
			query = new QueryDefinition( true );
			useDataSetFilter = false;
		}
		else
		{
			query = new QueryDefinition( );
		}
		query.setDataSetName( dataSet.getQualifiedName( ) );

		if ( groupDefns != null )
		{
			while ( groupDefns.hasNext( ) )
			{
				GroupHandle groupHandle = (GroupHandle) groupDefns.next( );
				query.addGroup( this.modelAdaptor.adaptGroup( groupHandle ) );
			}
		}

//		ModuleHandle moduleHandle = sessionContext.getModuleHandle( );
//		if ( moduleHandle == null )
//			moduleHandle = dataSet.getModuleHandle( );
 
		DefineDataSourceSetUtil.defineDataSourceAndDataSet( dataSet,
				this.dataEngine,
				this.modelAdaptor,
				new DataSetHandleProcessContext( dataSet,
						true,
						useDataSetFilter,
						false ) );
		
		
		QueryExecutionHelper execHelper = new QueryExecutionHelper( this.dataEngine,
				this.modelAdaptor,
				this.sessionContext,
				this );
		IQueryResults results = execHelper.executeQuery( query,
				inputParamBindings,
				null,
				columnBindings,
				useDataSetFilter,
				false,
				this.sessionContext.getTopScope(),
				this.interceptorContext );
		return results;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#execute(org.eclipse.birt.data.engine.api.IBasePreparedQuery, org.eclipse.birt.data.engine.api.IBaseQueryResults, org.mozilla.javascript.Scriptable)
	 */
	public IBaseQueryResults execute( IBasePreparedQuery query,
			IBaseQueryResults outerResults, Scriptable scope )
			throws AdapterException
	{
		try
		{
			if ( query instanceof IPreparedQuery )
			{
				return ( (IPreparedQuery) query ).execute( outerResults,
						scope );
			}
			else if ( query instanceof IPreparedCubeQuery )
			{
				String queryName = ( (IPreparedCubeQuery) query ).getCubeQueryDefinition( )
						.getName( );
				if ( this.cubeHandleMap.get( queryName ) != null )
				{
					this.materializeCube( (CubeHandle) this.cubeHandleMap.get( queryName ),
							this.sessionContext.getAppContext( ) );
					this.cubeHandleMap.remove( queryName );
				}

				return ( (IPreparedCubeQuery) query ).execute( outerResults, scope );
			}
			return null;
		}
		catch ( BirtException e )
		{
			throw new AdapterException( ResourceConstants.EXCEPTION_ERROR, e );
		}
	}

	public IBaseQueryResults execute( IBasePreparedQuery query,
			IBaseQueryResults outerResults, ScriptContext context )
			throws AdapterException
	{
		try
		{
			IDataScriptEngine engine = (IDataScriptEngine) context.getScriptEngine( IDataScriptEngine.ENGINE_NAME );
			Scriptable scope = engine.getJSScope( context );
			if ( query instanceof IPreparedQuery )
			{
				return ( (IPreparedQuery) query ).execute( outerResults,
						scope );
			}
			else if ( query instanceof IPreparedCubeQuery )
			{
				String queryName = ( (IPreparedCubeQuery) query ).getCubeQueryDefinition( )
						.getName( );
				if ( this.cubeHandleMap.get( queryName ) != null )
				{
					this.materializeCube( (CubeHandle) this.cubeHandleMap.get( queryName ),
							this.sessionContext.getAppContext( ) );
					this.cubeHandleMap.remove( queryName );
				}

				return ( (IPreparedCubeQuery) query ).execute( outerResults, scope );
			}
			return null;
		}
		catch ( BirtException e )
		{
			throw new AdapterException( ResourceConstants.EXCEPTION_ERROR, e );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#prepare(org.eclipse.birt.data.engine.api.IDataQueryDefinition)
	 */
	public IBasePreparedQuery prepare( IDataQueryDefinition query,
			Map appContext ) throws AdapterException
	{
		try
		{
			setModuleHandleToAppContext( appContext );

			if ( query instanceof IQueryDefinition )
				return prepare( (IQueryDefinition) query, appContext == null
						? this.sessionContext.getAppContext( ) : appContext );
			else if ( query instanceof ICubeQueryDefinition )
				return prepare( (ICubeQueryDefinition) query,
						appContext == null
								? this.sessionContext.getAppContext( )
								: appContext );
			else if ( query instanceof ISubCubeQueryDefinition )
				return prepare( (ISubCubeQueryDefinition) query,
						appContext == null
								? this.sessionContext.getAppContext( )
								: appContext );
			else
				return null;
		}
		catch ( BirtException e )
		{
			throw new AdapterException( ResourceConstants.EXCEPTION_ERROR, e );
		}
	}

	/*
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#defineCube(org.eclipse.birt.report.model.api.olap.CubeHandle)
	 */
	public void defineCube( CubeHandle cubeHandle ) throws BirtException
	{
		CubeHandleUtil.defineCube( dataEngine, cubeHandle, this.sessionContext.getAppContext( ) );
		this.cubeMetaDataHandleMap.put( cubeHandle.getQualifiedName( ), cubeHandle );

		ICubeInterceptor cubeInterceptor = CubeInterceptorFinder.find( cubeHandle );
		if ( cubeInterceptor != null )
		{
			cubeInterceptor.preDefineCube( this.sessionContext,
					cubeHandle );
		}

		if ( cubeInterceptor == null || cubeInterceptor.needDefineCube( ))
		{
			if( ! (cubeHandle instanceof TabularCubeHandle) )
				return;
			Set involvedDataSets = getInvolvedDataSets((TabularCubeHandle)cubeHandle);
			Iterator itr = involvedDataSets.iterator( );
			while (itr.hasNext( ))
			{
				DataSetHandle dsHandle = (DataSetHandle) itr.next( );
				BaseDataSourceDesign baseDataSource = this.modelAdaptor.adaptDataSource( dsHandle.getDataSource( ) );
				BaseDataSetDesign baseDataSet = this.modelAdaptor.adaptDataSet( dsHandle );

				//When the data set is joint data set, the data source does not exist.
				if ( baseDataSource!= null && this.dataEngine.getDataSourceRuntime( baseDataSource.getName( ) ) == null )
					this.defineDataSource( baseDataSource );

				//If the data set has not been defined previously, define it.
				if( this.dataEngine.getDataSetDesign(  baseDataSet.getName( ) ) == null )
				{
					DefineDataSourceSetUtil.defineDataSourceAndDataSet( dsHandle, this );
				}
			}

			if ( !cubeHandleMap.containsKey( cubeHandle.getQualifiedName( ) ) )
			{
				this.cubeHandleMap.put( cubeHandle.getQualifiedName( ), cubeHandle );
			}

			prepareForCubeGeneration( (TabularCubeHandle)cubeHandle );
		}
	}

	/**
	 *
	 * @param cubeHandle
	 * @param appContext
	 * @param stopSign
	 * @throws BirtException
	 */
	void materializeCube( CubeHandle cubeHandle, Map appContext ) throws BirtException
	{
		CubeMeasureUtil.validateDerivedMeasures( cubeHandle );
		
		int mode = this.sessionContext.getDataEngineContext( ).getMode( );
		try
		{
			if ( appContext == null )
				appContext = sessionContext.getAppContext( );

			String memoryUsage =  (String)( appContext.get( DataEngine.MEMORY_USAGE ) );
			MemoryUsageSetting.setMemoryUsage( memoryUsage );

			if ( mode == DataEngineContext.DIRECT_PRESENTATION )
			{
				int size = 0;
				if ( appContext != null )
				{
					Integer value = DataTypeUtil.toInteger( appContext.get( DataEngine.IN_MEMORY_CUBE_SIZE ) );
					if ( value != null && value.intValue( ) > 0)
					{
						size = value.intValue( );
					}
				}
				CubeMaterializer cm = getCubeMaterializer( size );
				createCube( (TabularCubeHandle) cubeHandle,
						cm,
						appContext );
			}
			else if ( mode == DataEngineContext.MODE_GENERATION )
			{
				CubeMaterializer cm = getCubeMaterializer( 0 );
				createCube(  (TabularCubeHandle)cubeHandle, cm, appContext );
				cm.saveCubeToReportDocument( cubeHandle.getQualifiedName( ),
						this.sessionContext.getDocumentWriter( ),
						this.dataEngine.getSession( ).getStopSign( ) );
			}
		}
		catch ( Exception e )
		{
			throw new DataException( ResourceConstants.EXCEPTION_ERROR, e);
		}
	}

	// Appcontext entries that may be temporarily modified during createCube call
	private static final String[] APPCONTEXT_BACKUP_KEYS = {
				DataEngine.MEMORY_DATA_SET_CACHE,
				DataEngine.DATA_SET_CACHE_ROW_LIMIT };
	
	/**
	 * Create a backup of app context values that may be changed during cube creation
	 */
	private Map<Object,Object> backupAppContextForCube(Map<Object,Object> originalAppContext) 
	{
		Map<Object, Object> backup = new HashMap<Object, Object>();
		for ( String key : APPCONTEXT_BACKUP_KEYS ) 
		{
			if ( originalAppContext.containsKey( key ))
				backup.put( key, originalAppContext.get( key ) );
		}
		return backup;
	}
	
	/**
	 * Restore appcontext value based on backup taken with backupAppContextForCube
	 */
	private void restoreAppContext(Map<Object,Object> appContext, Map<Object,Object> backup)
	{
		for ( String key : APPCONTEXT_BACKUP_KEYS ) 
		{
			appContext.remove( key );
		}
		appContext.putAll( backup );
	}
	
	/**
	 *
	 * @param cubeHandle
	 * @param cubeMaterializer
	 * @param stopSign
	 * @throws BirtException
	 * @throws IOException
	 * @throws BirtException
	 * @throws DataException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createCube( TabularCubeHandle cubeHandle,
			CubeMaterializer cubeMaterializer, Map appContext ) throws BirtException
	{
		SecurityListener sl = new SecurityListener( this );
		sl.start( cubeHandle );

		//Please note that we should always use original application context during query execution,
		//rather than create a new one with same properties.Application Context is sometimes used as cross-query
		//information carrier.
		// Also make sure that the backup/restore steps don't alter appcontext values updated/added by 
		// downstream components
		if( appContext == null )
			appContext = new HashMap();
		Map<Object,Object> backupAppContext = backupAppContextForCube(appContext);

		List measureNames = new ArrayList( );
		Map calculatedMeasure = new HashMap( );
		List measureGroups = cubeHandle.getContents( CubeHandle.MEASURE_GROUPS_PROP );
		for ( int i = 0; i < measureGroups.size( ); i++ )
		{
			MeasureGroupHandle mgh = (MeasureGroupHandle) measureGroups.get( i );
			List measures = mgh.getContents( MeasureGroupHandle.MEASURES_PROP );
			for ( int j = 0; j < measures.size( ); j++ )
			{
				MeasureHandle measure = (MeasureHandle) measures.get( j );
				if( !measure.isCalculated( ) )
				{
					measureNames.add( measure.getName( ) );				
				}
				else
				{
					calculatedMeasure.put( measure.getName( ), DataAdapterUtil.adaptModelDataType( measure.getDataType( ) ) );
				}
			}
		}

		IDimension[] dimensions = populateDimensions( cubeMaterializer,
				cubeHandle,
				appContext,
				sl );
		String[][] factTableKey = new String[dimensions.length][];
		String[][] dimensionKey = new String[dimensions.length][];
		boolean fromJoin = false;
		for ( int i = 0; i < dimensions.length; i++ )
		{
			DimensionHandle dim = (DimensionHandle) cubeHandle.getDimension( dimensions[i].getName( ) );
			TabularHierarchyHandle hier = (TabularHierarchyHandle) dim.getDefaultHierarchy( );
			DimensionJoinConditionHandle condition = getFacttableJointKey(cubeHandle,
					hier);
			if ( cubeHandle.getDataSet( ).equals( hier.getDataSet( ) ) || hier.getDataSet( ) == null
					|| ( isDateTimeDimension( hier ) && existColumnName( hier, condition.getHierarchyKey( ) ) ) )
			{

				String[] keyNames = dimensions[i].getHierarchy().getLevels()[dimensions[i]
						.getHierarchy().getLevels().length - 1].getKeyNames();
				factTableKey[i] = new String[keyNames.length];
				dimensionKey[i] = new String[keyNames.length];
				for ( int j = 0; j<keyNames.length; j++)
				{
					factTableKey[i][j] =  dim.getName() + "/" + keyNames[j];
					dimensionKey[i][j] = keyNames[j];
				}
			}
			else
			{
				fromJoin = true;
				Iterator it = cubeHandle.joinConditionsIterator( );
				if ( !it.hasNext() )
					throw new AdapterException( ResourceConstants.MISSING_JOIN_CONDITION,
							new String[]{cubeHandle.getDataSet( ).getName( ), dim.getName(), cubeHandle.getName( )} );
				boolean foundJoinCondition = false;
				while ( it.hasNext( ) )
				{
					DimensionConditionHandle dimCondHandle = (DimensionConditionHandle) it.next( );

					if ( dimCondHandle.getHierarchy( ).getName( ).equals( hier.getName( ) ) )
					{
						Iterator conditionIt = dimCondHandle.getJoinConditions( )
								.iterator( );
						List dimensionKeys = new ArrayList( );
						List factTableKeys = new ArrayList( );
						while ( conditionIt.hasNext( ) )
						{
							foundJoinCondition = true;
							DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionIt.next( );
							String levelName = joinCondition.getLevelName( );
							if ( levelName != null
									&& isAttribute( dimensions[i],
											levelName,
											joinCondition.getHierarchyKey( ) ) )
							{
								dimensionKeys.add( OlapExpressionUtil.getAttributeColumnName( getLevelName( dimensions[i],
										levelName ),
										joinCondition.getHierarchyKey( ) ) );
							}
							else
							{
								String existLevelName = getLevelName( hier, joinCondition.getHierarchyKey( ));
								if ( existLevelName != null )
								{
									//joint hierarchy key is the key column name of one level
									dimensionKeys.add( existLevelName );
								}
								else
								{
									dimensionKeys.add( getDummyLevelNameForJointHierarchyKey(joinCondition.getHierarchyKey( )) );
								}
							}
							factTableKeys.add( OlapExpressionUtil.getQualifiedLevelName( dimensions[i].getName( ),
									joinCondition.getCubeKey( ) ) );
						}
						factTableKey[i] = new String[factTableKeys.size( )];
						dimensionKey[i] = new String[dimensionKeys.size( )];
						for( int j = 0; j < dimensionKeys.size( ); j++ )
						{
							factTableKey[i][j] = factTableKeys.get( j ).toString( );
							dimensionKey[i][j] = dimensionKeys.get( j ).toString( );
						}
					}
				}

				if( !foundJoinCondition )
					throw new AdapterException( ResourceConstants.MISSING_JOIN_CONDITION, dim.getName() );
			}
		}
		DataSetIterator dataForCube = null;
		if ( cubeHandle.autoPrimaryKey( ) )
		{
			QueryDefinition qd = cubeQueryMap.get( cubeHandle );
			//does not need aggregation to define measures in this case, clear all aggregations on measures
			for ( Object measureName : measureNames )
			{
				IBinding b = (IBinding)qd.getBindings( ).get( measureName );
				if ( b != null )
				{
					b.setAggrFunction( null );
					if ( b.getAggregatOns( ) != null )
					{
						b.getAggregatOns( ).clear( );
					}
				}
			}
			if ( !fromJoin )
			{
				List<ColumnMeta> metas = cubeMetaMap.get( cubeHandle );
				//append binding in fact table query for temp PK
				IBinding tempPKBinding = new Binding( DataSetIterator.createLevelName(
						getCubeTempPKDimensionName( cubeHandle ),
						getCubeTempPKFieldName( cubeHandle )),
					new ScriptExpression( "row.__rownum" ) ); //take rownum as the the primary key
				qd.addBinding( tempPKBinding );
				DataSetIterator.ColumnMeta cm = new DataSetIterator.ColumnMeta( tempPKBinding.getBindingName( ), null, DataSetIterator.ColumnMeta.LEVEL_KEY_TYPE );
				cm.setDataType( DataType.INTEGER_TYPE );
				metas.add( cm );

				//append temp PK dimension
				String countBindingName = "COUNT"; //$NON-NLS-1$
				IBinding b = new Binding( countBindingName );
				b.setAggrFunction( IBuildInAggregation.TOTAL_COUNT_FUNC );
				QueryDefinition q = cubeQueryMap.get( cubeHandle );
				q.addBinding( b );
				dataForCube = new DataSetIterator( this,
						cubeQueryMap.get( cubeHandle ),
						cubeMetaMap.get( cubeHandle ),
						appContext );
				int rowCount = 0;
				try
				{
					rowCount = dataForCube.getSummaryInt( countBindingName );
				}
				catch( BirtException e )
				{
					rowCount = 1;
				}
				dimensions = appendArray( dimensions, populateTempPKDimension( cubeMaterializer, cubeHandle,
						new DataSetIteratorForTempPK( rowCount ),
						appContext ));

				//append fact table key for temp PK dimension
				factTableKey = appendArray( factTableKey, new String[]{
						DataSetIterator.createLevelName(
								getCubeTempPKDimensionName( cubeHandle ),
								getCubeTempPKFieldName( cubeHandle ))} );

				//append dimension key for temp PK dimension
				dimensionKey = appendArray( dimensionKey, new String[]{
						getCubeTempPKFieldName( cubeHandle )});
			}
			//If it is from join, just generate normal data iterator.
			if( dataForCube == null )
			{
				dataForCube = new DataSetIterator( this,
						cubeQueryMap.get( cubeHandle ),
						cubeMetaMap.get( cubeHandle ),
						appContext );
			}
		}



		try
		{
			List<String> measureAggrFunctions = new ArrayList<String>();

			if( dataForCube == null )
			{
				QueryDefinition query = cubeQueryMap.get( cubeHandle );
				for ( Object measureName : measureNames )
				{
					IBinding b = (IBinding)query.getBindings( ).get( measureName );
					assert b!= null && b.getAggrFunction( )!= null;
					measureAggrFunctions.add( b.getAggrFunction( ) );
					if ( IBuildInAggregation.TOTAL_COUNT_FUNC.equalsIgnoreCase( b.getAggrFunction( ) )
							|| IBuildInAggregation.TOTAL_COUNTDISTINCT_FUNC.equalsIgnoreCase( b.getAggrFunction( ) ) )
					{
						if ( b.getExpression( ) == null )
						{
							// We set a dummy expression here for
							// count/countdistinct aggregations.Because in data
							// engine's table query execution we do not allow
							// empty expression.
							// This is a tricky.
							b.setExpression( new ScriptExpression( "1" ) );
						}
					}
					
					if( b.getExpression( ) == null || ((ScriptExpression)b.getExpression( )).getText( ) == null )
					{
						throw new AdapterException( ResourceConstants.INVALID_MEASURE_EXPRESSION, measureName );
					}

					b.setAggrFunction( null );
					if ( b.getAggregatOns( ) != null )
					{
						b.getAggregatOns( ).clear( );
					}
				}
				query.getGroups( ).clear( );
				dataForCube = new DataSetIterator( this,
						query,
						cubeMetaMap.get( cubeHandle ),
						appContext );
			}
			cubeMaterializer.createCube( cubeHandle.getQualifiedName( ),
					factTableKey,
					dimensionKey,
					dimensions,
					dataForCube,
					this.toStringArray( measureNames ),
					calculatedMeasure,
					this.toStringArray( measureAggrFunctions ),
					computeMemoryBufferSize( appContext ),
					dataEngine.getSession( ).getStopSign( ) );
		}
		catch ( Exception e )
		{
			throw new AdapterException( ResourceConstants.CUBE_MEASURE_CREATION_ERROR,
					e );
		}
		finally
		{
			if( dataForCube!= null )
				dataForCube.close( );
		}

		sl.end( );

		restoreAppContext( appContext, backupAppContext );
	}

	public static long computeMemoryBufferSize( Map appContext )
	{
		//here a simple assumption, that 1M memory can accommodate 2000 rows
		if ( appContext == null )
			return 0;

		//The unit is 1M.
		return populateMemBufferSize( appContext.get( DataEngine.MEMORY_BUFFER_SIZE )) * 1024 * 1024;
	}

	/**
	 *
	 * @param propValue
	 * @return
	 */
	private static long populateMemBufferSize( Object propValue )
	{
		String targetBufferSize =  propValue == null
				? "0" : propValue
						.toString( );

		long memoryCacheSize = 0;

		if ( targetBufferSize != null )
			memoryCacheSize = Long.parseLong( targetBufferSize );

		return memoryCacheSize;
	}

	private String getLevelName( TabularHierarchyHandle hierhandle, String columnName )
	{
		List levels = hierhandle.getContents( TabularHierarchyHandle.LEVELS_PROP );
		for ( int k = 0; k < levels.size( ); k++ )
		{
			TabularLevelHandle level = (TabularLevelHandle) levels.get( k );
			if ( columnName.equals( level.getColumnName( ) ))
			{
				return level.getName( );
			}
		}
		return null;
	}

	/**
	 *
	 * @param cubeHandle
	 * @throws BirtException
	 */
	private void prepareForCubeGeneration( CubeHandle cHandle )
			throws BirtException
	{
		TabularCubeHandle cubeHandle = null;
		if( cHandle instanceof TabularCubeHandle )
		{
			cubeHandle = (TabularCubeHandle)cHandle;
		}
		List<IQueryDefinition> queryDefns = new ArrayList<IQueryDefinition>();

		List<ColumnMeta> metaList = new ArrayList<ColumnMeta>();
		QueryDefinition query =  createQuery( this, cubeHandle, metaList );
		if ( cubeHandle.autoPrimaryKey( ) )
		{
			//need no groups in query definition for generating fact table
			//Instead we sort fact tables directly
			query.setUsesDetails( true );
			query.getGroups( ).clear( );
		}
		queryDefns.add( query );
		cubeQueryMap.put( cubeHandle, query );
		cubeMetaMap.put( cubeHandle, metaList );

		List<DimensionHandle> dimHandles = cubeHandle.getContents( CubeHandle.DIMENSIONS_PROP );
		for ( DimensionHandle dim:dimHandles )
		{
			List<TabularHierarchyHandle> hiers = dim.getContents( DimensionHandle.HIERARCHIES_PROP );
			for ( TabularHierarchyHandle hier: hiers )
			{
				String columnForDeepestLevel = null;
				List levels = hier.getContents( TabularHierarchyHandle.LEVELS_PROP );
				if ( levels.size( ) >= 1 )
				{
					TabularLevelHandle level = (TabularLevelHandle) levels.get( levels.size( ) -1 );
					columnForDeepestLevel = level.getColumnName( );
				}
				metaList = new ArrayList<ColumnMeta>();
				query = createDimensionQuery( this, dim,
						hier,
						metaList,
						String.valueOf( cubeHandle.getElement( ).getID( ) ) );
				String[] jointHierarchyKeys = getJointHierarchyKeys( cubeHandle, hier );
				if ( cubeHandle.autoPrimaryKey( ) && jointHierarchyKeys.length > 0 )
				{
					if ( !Arrays.deepEquals( jointHierarchyKeys, new String[]{ columnForDeepestLevel} ) )
					{
						for ( String key : jointHierarchyKeys )
						{
							//add bindings for dummy level based on joint hierarchy keys
							String exprString = ExpressionUtil.createJSDataSetRowExpression( key );
							query.addBinding( new Binding( getDummyLevelNameForJointHierarchyKey( key ), new ScriptExpression(exprString) ) );
							DataSetIterator.ColumnMeta temp = new DataSetIterator.ColumnMeta( getDummyLevelNameForJointHierarchyKey( key ),
									null,
									DataSetIterator.ColumnMeta.LEVEL_KEY_TYPE );
							temp.setDataType( getColumnDataType( hier.getDataSet( ), key ) );
							metaList.add( temp );
						}
					}

					//need no groups in query definition for generating dimension table

					query.setUsesDetails( true );
					query.getGroups( ).clear( );
				}
				queryDefns.add( query );
				cubeQueryMap.put( hier, query );
				cubeMetaMap.put( hier, metaList );
			}
		}

		this.dataEngine.registerQueries( queryDefns.toArray( new IDataQueryDefinition[0] ) );
	}

	/**
	 * Apply the filter to dimension query when there is start/end time setting.
	 * @param dim
	 * @param hier
	 * @return
	 * @throws BirtException
	 */
	private FilterDefinition buildFilterForTimeDimension( DimensionHandle dim,
			TabularHierarchyHandle hier ) throws BirtException
	{
		Date startTime = CubeHandleUtil.getStartTime( dim );
		Date endTime = CubeHandleUtil.getEndTime( dim );

		if ( startTime == null && endTime == null )
		{
			return null;
		}
		List levels = hier.getContents( TabularHierarchyHandle.LEVELS_PROP );
		TabularLevelHandle level = (TabularLevelHandle) levels.get( levels.size( ) - 1 );
		FilterDefinition filter = null;
		String exprString = ExpressionUtil.createJSDataSetRowExpression( level.getColumnName( ) );
		if ( startTime != null && endTime != null )
		{
			ConditionalExpression expr = new ConditionalExpression( exprString,
					IConditionalExpression.OP_BETWEEN,
					JavascriptEvalUtil.transformToJsExpression( DataTypeUtil.toLocaleNeutralString( startTime ) ),
					JavascriptEvalUtil.transformToJsExpression( DataTypeUtil.toLocaleNeutralString( endTime ) ) );
			filter = new FilterDefinition( expr );
		}
		else if ( startTime != null )
		{
			ConditionalExpression expr = new ConditionalExpression( exprString,
					IConditionalExpression.OP_GE,
					JavascriptEvalUtil.transformToJsExpression( DataTypeUtil.toLocaleNeutralString( startTime ) ) );
			filter = new FilterDefinition( expr );
		}
		else if ( endTime != null )
		{
			ConditionalExpression expr = new ConditionalExpression( exprString,
					IConditionalExpression.OP_LE,
					JavascriptEvalUtil.transformToJsExpression( DataTypeUtil.toLocaleNeutralString( endTime ) ) );
			filter = new FilterDefinition( expr );
		}

		return filter;
	}

	/**
	 *
	 * @param cubeHandle
	 * @return
	 */
	private List getDataSetsToCache( TabularCubeHandle cubeHandle )
	{
		List list = new ArrayList( );
		if( cubeHandle.getDataSet( ) == null )
			return list;
		list.add( cubeHandle.getDataSet( ) );
		List dimHandles = cubeHandle.getContents( CubeHandle.DIMENSIONS_PROP );
		for( int i = 0; i < dimHandles.size( ); i++ )
		{
			DimensionHandle dimHandle = (DimensionHandle)dimHandles.get( i );
			List hiers = dimHandle.getContents( DimensionHandle.HIERARCHIES_PROP );
			TabularHierarchyHandle hierHandle = (TabularHierarchyHandle)hiers.get( 0 );
			if( hierHandle.getDataSet( )!= null )
				list.add( hierHandle.getDataSet( ) );
			else
				list.add( cubeHandle.getDataSet( ) );
		}
		return list;
	}

	private Set getInvolvedDataSets(TabularCubeHandle cubeHandle)
	{
		return new HashSet(getDataSetsToCache(cubeHandle));
	}

	/**
	 * whether this key name is attribute or not
	 * @param dimensions
	 * @param colName
	 * @return
	 */
	private boolean isAttribute( IDimension dimension, String levelName,
			String colName )
	{
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		for ( int j = 0; j < levels.length; j++ )
		{
			if ( !levelName.equals( OlapExpressionUtil.getQualifiedLevelName( dimension.getName( ),
					levels[j].getName( ) ) ) )
				continue;
			String[] attributes = levels[j].getAttributeNames( );
			if ( attributes == null )
				continue;
			for ( int k = 0; k < attributes.length; k++ )
			{
				if ( attributes[k].equals( OlapExpressionUtil.getAttributeColumnName( levels[j].getName( ),
						colName ) ) )
					return true;
			}
		}
		return false;
	}

	/**
	 * Because the targeName's pattern is dimensionName/levelName, provide this
	 * method to get the real levelName
	 *
	 * @param dimension
	 * @param targetName
	 * @return
	 */
	private String getLevelName( IDimension dimension, String targetName )
	{
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		for ( int j = 0; j < levels.length; j++ )
		{
			if ( targetName.equals( OlapExpressionUtil.getQualifiedLevelName( dimension.getName( ),
					levels[j].getName( ) ) ) )
			{
				return levels[j].getName( );
			}
		}
		return targetName;
	}

	/**
	 * Populate all dimensions.
	 * @param cubeMaterializer
	 * @param dimHandles
	 * @param stopSign
	 * @return
	 * @throws AdapterException
	 * @throws IOException
	 * @throws BirtException
	 * @throws DataException
	 */
	private IDimension[] populateDimensions( CubeMaterializer cubeMaterializer,
			TabularCubeHandle cubeHandle, Map appContext,
			SecurityListener sl ) throws AdapterException
	{
		List dimHandles = cubeHandle.getContents( CubeHandle.DIMENSIONS_PROP );
		List result = new ArrayList( );
		for ( int i = 0; i < dimHandles.size( ); i++ )
		{
			DimensionHandle dh = (DimensionHandle) dimHandles.get( i );
			IDimension dim = createdDimensions.get( dh.getName( ) );
			if ( dim == null )
			{
				dim = populateDimension( cubeMaterializer,
						dh,
						cubeHandle,
						appContext, sl );
				createdDimensions.put( dh.getName( ), dim );
			}
			result.add( dim);
		}

		IDimension[] dimArray = new IDimension[dimHandles.size( )];
		for ( int i = 0; i < result.size( ); i++ )
		{
			dimArray[i] = (IDimension) result.get( i );
		}
		return dimArray;
	}

	/**
	 * Populate the dimension.
	 *
	 * @param cubeMaterializer
	 * @param dim
	 * @param stopSign
	 * @return
	 * @throws AdapterException
	 * @throws IOException
	 * @throws BirtException
	 * @throws DataException
	 */
	private IDimension populateDimension( CubeMaterializer cubeMaterializer,
			DimensionHandle dim, TabularCubeHandle cubeHandle, Map appContext,SecurityListener sl )
			throws AdapterException
	{
		List hiers = dim.getContents( DimensionHandle.HIERARCHIES_PROP );
		List iHiers = new ArrayList( );
		for ( int j = 0; j < hiers.size( ); j++ )
		{
			TabularHierarchyHandle hierhandle = (TabularHierarchyHandle) hiers.get( 0 );
			List levels = hierhandle.getContents( TabularHierarchyHandle.LEVELS_PROP );

			List<ILevelDefn> levelInHier = new ArrayList<ILevelDefn>( );
			List<String> leafLevelKeyColumn = new ArrayList<String>( );
			Set<String> columnNamesForLevels = new HashSet<String>( );
			for ( int k = 0; k < levels.size( ); k++ )
			{
				TabularLevelHandle level = (TabularLevelHandle) levels.get( k );
				columnNamesForLevels.add(  level.getColumnName( ) );
				List levelAttrs = new ArrayList( );
				Iterator it = level.attributesIterator( );
				while ( it.hasNext( ) )
				{
					LevelAttributeHandle levelAttr = (LevelAttributeHandle) it.next( );
					levelAttrs.add( OlapExpressionUtil.getAttributeColumnName( level.getName( ),
							levelAttr.getName( ) ) );
				}
				if ( DesignChoiceConstants.LEVEL_TYPE_DYNAMIC.equals( level.getLevelType( ) )
						&& level.getDisplayColumnName( ) != null )
				{
					levelAttrs.add( OlapExpressionUtil.getDisplayColumnName( level.getName( ) ) );
				}
				leafLevelKeyColumn.add(level.getName( ));

				levelInHier.add(CubeElementFactory.createLevelDefinition( level.getName( ),
						new String[]{
							level.getName( )
						},
						this.toStringArray( levelAttrs ) ));
			}
			String[] jointHierarchyKeys = getJointHierarchyKeys( cubeHandle, hierhandle );
			if ( !cubeHandle.autoPrimaryKey( ) )
			{
				for ( String jointKey : jointHierarchyKeys )
				{
					if ( !columnNamesForLevels.contains( jointKey ))
					{
						throw new AdapterException( ResourceConstants.CUBE_JOINT_COLUMN_NOT_IN_LEVELS,
								new String[]{jointKey, dim.getName( )});
					}
				}
			}
			//create leaf level
			if ( levelInHier.size( ) >= 1 )
			{
				if ( cubeHandle.autoPrimaryKey( ) && jointHierarchyKeys.length > 0 )
				{
					if ( !Arrays.deepEquals( jointHierarchyKeys, new String[]{
							((TabularLevelHandle)levels.get( levels.size( ) - 1 )).getColumnName( )
						}))
					{
						//need to append joint keys as leaf level
						levelInHier.add( CubeElementFactory.createLevelDefinition( "_${INTERNAL_INDEX}$_",
								getDummyLevelNamesForJointHierarchyKeys( jointHierarchyKeys ),
							    new String[0] ));
					}
					else if ( levelInHier.size( ) > 1 && isDateTimeDimension(hierhandle) )
					{
						levelInHier.add( CubeElementFactory.createLevelDefinition( "_${INTERNAL_INDEX}$_",
								leafLevelKeyColumn.toArray( new String[0] ),
							    new String[0] ));
					}
				}
				else if ( levelInHier.size( ) > 1 )
				{
					levelInHier.add( CubeElementFactory.createLevelDefinition( "_${INTERNAL_INDEX}$_",
							leafLevelKeyColumn.toArray( new String[0] ),
						    new String[0] ));
				}
			}
			Object originalMemCache = null;
			Object originalRowLimit = null;
			IDatasetIterator valueIt = null;
			try
			{
				sl.process( dim );
				if ( !( cubeHandle.getDataSet( )
								.equals( hierhandle.getDataSet( ) ) || hierhandle.getDataSet( ) == null ))
				{
					//remove cache limit for dimension data set
					originalMemCache = appContext.remove( DataEngine.MEMORY_DATA_SET_CACHE );
					originalRowLimit = appContext.remove( DataEngine.DATA_SET_CACHE_ROW_LIMIT );
				}
				
				String[] timeType = getTimeLevelType( hierhandle );
				for( int i = 0; i < timeType.length; i++ )
				{
					levelInHier.get( i ).setTimeType( timeType[i] );
				}
				valueIt = new DataSetIterator( this,
						cubeQueryMap.get( hierhandle ),
						cubeMetaMap.get( hierhandle ),
						appContext );
				( (DataSetIterator) valueIt ).initSecurityListenerAndDimension( dim.getName( ),
						sl );

				iHiers.add( cubeMaterializer.createHierarchy( dim.getName( ),
						hierhandle.getName( ),
						valueIt,
						levelInHier.toArray( new ILevelDefn[0] ),
						dataEngine.getSession( ).getStopSign( ) ) );
			}
			catch ( Exception e )
			{
				throw new AdapterException( ResourceConstants.CUBE_HIERARCHY_CREATION_ERROR,
						e,
						dim.getName( ) + "." + hierhandle.getName( ) );
			}
			finally
			{
				if( valueIt!= null )
				{
					try
					{
						valueIt.close( );
					}
					catch ( BirtException e )
					{
					}
				}
				if ( originalMemCache != null )
				{
					appContext.put( DataEngine.MEMORY_DATA_SET_CACHE, originalMemCache );
				}
				if( originalRowLimit!= null )
				{
					appContext.put( DataEngine.DATA_SET_CACHE_ROW_LIMIT, originalRowLimit );
				}
			}
		}

		try
		{
			return cubeMaterializer.createDimension( dim.getName( ),
					(IHierarchy) iHiers.get( 0 ) );
		}
		catch ( Exception e )
		{
			throw new AdapterException( ResourceConstants.CUBE_DIMENSION_CREATION_ERROR,
					e,
					dim.getName( ) );
		}
	}

	private String[] getFieldName( TabularHierarchyHandle timeHierhandle )
	{
		List levels = timeHierhandle.getContents( TabularHierarchyHandle.LEVELS_PROP );
		String[] fieldName = new String[levels.size( )];
		for ( int i = 0; i < levels.size( ); i++ )
		{
			TabularLevelHandle level = (TabularLevelHandle) levels.get( i );
			fieldName[i] = level.getName( );
		}
		return fieldName;
	}

	private String[] getTimeLevelType( TabularHierarchyHandle timeHierhandle )
	{
		List levels = timeHierhandle.getContents( TabularHierarchyHandle.LEVELS_PROP );
		String[] timeType = new String[levels.size( )];
		for ( int i = 0; i < levels.size( ); i++ )
		{
			TabularLevelHandle level = (TabularLevelHandle) levels.get( i );
			timeType[i] = level.getDateTimeLevelType( );
		}
		return timeType;
	}


	private String[] getJointHierarchyKeys( TabularCubeHandle cubeHandle, TabularHierarchyHandle hier )
	{
		List<String> hierarchyKeys = new ArrayList( );
		if ( hier.getDataSet( ) != null && !hier.getDataSet( ).equals(cubeHandle.getDataSet( )))
		{
			Iterator it = cubeHandle.joinConditionsIterator( );
			while ( it.hasNext( ) )
			{
				DimensionConditionHandle dimCondHandle = (DimensionConditionHandle) it.next( );
				if ( dimCondHandle.getHierarchy( ).getName( ).equals( hier.getName( ) ) )
				{
					Iterator conditionIt = dimCondHandle.getJoinConditions( )
							.iterator( );
					while ( conditionIt.hasNext( ) )
					{
						DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionIt.next( );
						String key = joinCondition.getHierarchyKey( );
						hierarchyKeys.add( key );
					}
				}
			}
		}
		return hierarchyKeys.toArray( new String[0] );
	}

	/**
	 * Populate the dimension.
	 *
	 * @param cubeMaterializer
	 * @param dim
	 * @param stopSign
	 * @return
	 * @throws AdapterException
	 * @throws IOException
	 * @throws BirtException
	 * @throws DataException
	 */
	private IDimension populateTempPKDimension( CubeMaterializer cubeMaterializer,
			TabularCubeHandle cubeHandle,DataSetIteratorForTempPK dataForTempPK, Map appContext )
			throws AdapterException
	{
		QueryDefinition q = null;
		ILevelDefn[] tempLevels = new ILevelDefn[]{ CubeElementFactory.createLevelDefinition( getCubeTempPKFieldName( cubeHandle ),
				new String[]{ getCubeTempPKFieldName( cubeHandle )},
				new String[]{}) };
		IHierarchy h = null ;
		try
		{
			h = cubeMaterializer.createHierarchy( getCubeTempPKDimensionName( cubeHandle ),
					getCubeTempPKHierarchyName( cubeHandle ),
						dataForTempPK,
						tempLevels,
					dataEngine.getSession( ).getStopSign( ) ) ;
		}
		catch ( Exception e )
		{
			throw new AdapterException( ResourceConstants.CUBE_HIERARCHY_CREATION_ERROR,
					e,
					getCubeTempPKDimensionName( cubeHandle ) + "." + getCubeTempPKHierarchyName( cubeHandle ) );
		}
		try
		{
			return cubeMaterializer.createDimension( getCubeTempPKDimensionName( cubeHandle ),
					h );
		}
		catch ( Exception e )
		{
			throw new AdapterException( ResourceConstants.CUBE_DIMENSION_CREATION_ERROR,
					e,
					getCubeTempPKDimensionName( cubeHandle ));
		}
	}



	/**
	 *
	 * @param object
	 * @return
	 */
	private String[] toStringArray( List object )
	{
		if( object == null )
			return null;
		String[] result = new String[object.size( )];
		for( int i = 0; i < object.size( ); i ++ )
		{
			result[i] = object.get( i ).toString();
		}
		return result;
	}

	/*
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#prepare(org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition)
	 */
	public IPreparedCubeQuery prepare( ICubeQueryDefinition query ) throws BirtException
	{
		return this.prepare( query, sessionContext.getAppContext( ) );
	}

	/*
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#prepare(org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition)
	 */
	public IPreparedCubeQuery prepare( ICubeQueryDefinition query,
			Map appContext ) throws BirtException
	{
		IBaseDataSetDesign design = dataEngine.getDataSetDesign( query.getName( ) );
		if ( design != null )
		{
			final IDataSetInterceptor dataSetInterceptor = DataSetInterceptorFinder.find( design );
			if ( dataSetInterceptor != null )
			{
				dataSetInterceptor.preDefineDataSet( dataEngine.getDataSourceDesign( design.getDataSourceName( ) ),
						design,
						query,
						this.registeredQueries,
						sessionContext,
						dataEngine.getSession( ).getTempDir( ), this.interceptorContext );
			}
		}
        else
        {
            // Null data set indicates that the query uses cube. Measure[]
            // expression must have aggregateOn/aggregateFunc defined.
            refactorCubeQueryDefinition( query );
        }
		populateMeasureDefinitionForCalculateMeasures( query );
		setMeasureDataTypeForCubeQuery ( query );
		QueryAdapter.adaptQuery( query );
		
		CubeHandle cubeHandle = null;
		if ( this.cubeMetaDataHandleMap != null
				&& this.cubeMetaDataHandleMap.containsKey( query.getName( ) ) )
		{
			cubeHandle = (CubeHandle) this.cubeMetaDataHandleMap.get( query.getName( ) );
		}
		QueryValidator.validateTimeFunctionInCubeQuery( query, cubeHandle );
		
		dataEngine.getSession( ).getStopSign( ).start( );
		setModuleHandleToAppContext( appContext );

		return this.dataEngine.prepare( query, appContext );
	}
	
	private void populateMeasureDefinitionForCalculateMeasures ( ICubeQueryDefinition query ) throws BirtException
	{	
		List calculatedMeasures = query.getDerivedMeasures( );
		List measures = query.getMeasures( );
		HashSet<String> measureNameList = new HashSet<String>( );
		for ( int i = 0; i < measures.size( ); i++ )
		{
			measureNameList.add( ( (IMeasureDefinition) measures.get( i ) ).getName( ));
		}
		HashSet<String> derivedMeasureNameList = new HashSet<String>();
		for ( int i = 0 ; i < calculatedMeasures.size( );i++)
		{
			derivedMeasureNameList.add( ( (IDerivedMeasureDefinition) calculatedMeasures.get( i ) ).getName( ) );
		}
		
		//populate all calculated measure in cube query.
		if ( this.cubeMetaDataHandleMap != null
				&& this.cubeMetaDataHandleMap.containsKey( query.getName( ) ) )
		{
			CubeHandle cubeHandle = (CubeHandle) this.cubeMetaDataHandleMap.get( query.getName( ) );
			CubeMeasureUtil.validateDerivedMeasures( cubeHandle );
			List measureGroups = cubeHandle.getContents( CubeHandle.MEASURE_GROUPS_PROP );
			for ( int i = 0; i < measureGroups.size( ); i++ )
			{
				MeasureGroupHandle mgh = (MeasureGroupHandle) measureGroups.get( i );
				List measureGroup = mgh.getContents( MeasureGroupHandle.MEASURES_PROP );
				for ( int j = 0; j < measureGroup.size( ); j++ )
				{
					MeasureHandle measure = (MeasureHandle) measureGroup.get( j );
					if( measure.isCalculated( ) && !derivedMeasureNameList.contains( measure.getName( ) ) )
					{
						derivedMeasureNameList.add( measure.getName( ) );
						query.createDerivedMeasure( measure.getName( ),
								DataAdapterUtil.adaptModelDataType( measure.getDataType( ) ),
								modelAdaptor.adaptExpression( (Expression) measure.getExpressionProperty( IMeasureModel.MEASURE_EXPRESSION_PROP )
										.getValue( ),
										ExpressionLocation.CUBE ));
					}
				}
			}
		}
		
		validateBindings( query.getBindings( ), derivedMeasureNameList );
		calculatedMeasures = query.getDerivedMeasures( );
		if ( calculatedMeasures == null || calculatedMeasures.size( ) == 0)
			return;
		
		for ( int i = 0; i < calculatedMeasures.size( ); i++ )
		{
			IDerivedMeasureDefinition dmd = (IDerivedMeasureDefinition) calculatedMeasures.get( i );
			List measureNames = ExpressionCompilerUtil.extractColumnExpression( dmd.getExpression( ),
					ExpressionUtil.MEASURE_INDICATOR );
			for ( int j = 0; j < measureNames.size( ); j++ )
			{
				if ( !measureNameList.contains( measureNames.get( j ).toString( ) )
						&& !derivedMeasureNameList.contains( measureNames.get( j )
								.toString( ) ) )
				{
					IMeasureDefinition md = query.createMeasure( measureNames.get( j ).toString( ) );
					measureNameList.add( md.getName( ) );
					if ( this.cubeMetaDataHandleMap != null
							&& this.cubeMetaDataHandleMap.containsKey( query.getName( ) ) )
					{
						CubeHandle cubeHandle = (CubeHandle) this.cubeMetaDataHandleMap.get( query.getName( ) );
						MeasureHandle measureHandle = cubeHandle.getMeasure( measureNames.get( j )
								.toString( ) );
						if ( measureHandle == null )
							throw new DataException( AdapterResourceHandle.getInstance( )
									.getMessage( ResourceConstants.CUBE_DERIVED_MEASURE_INVALID_REF,
											new Object[]{
													dmd.getName( ),
													measureNames.get( j )
											} ) );
						md.setAggrFunction( DataAdapterUtil.adaptModelAggregationType( measureHandle.getFunction( ) ) );
					}
				}
			}
		}
	}
	
	private void validateBindings( List<IBinding> bindings,
			Collection calculatedMeasures ) throws AdapterException
	{
		// Not support aggregation filter reference calculated measures.
		try
		{
			for ( IBinding b : bindings )
			{
				if ( b.getAggrFunction( ) == null || b.getFilter( ) == null )
					continue;
				
				List referencedMeasures = ExpressionCompilerUtil.extractColumnExpression( b.getFilter( ),
						ExpressionUtil.MEASURE_INDICATOR );
				for ( Object r : referencedMeasures )
				{
					if ( calculatedMeasures.contains( r.toString( ) ) )
					{
						throw new AdapterException( AdapterResourceHandle.getInstance( )
								.getMessage( ResourceConstants.CUBE_AGGRFILTER_REFER_CALCULATEDMEASURE,
										new Object[]{
												b.getBindingName( ),
												r.toString( )
										} ) );
					}
				}
			}
		}
		catch ( DataException ex )
		{
			throw new AdapterException( ex.getMessage( ), ex );
		}
	}
	
	private void setMeasureDataTypeForCubeQuery( ICubeQueryDefinition query )
	{
		List measures = query.getMeasures( );

		if ( this.cubeMetaDataHandleMap != null
				&& this.cubeMetaDataHandleMap.containsKey( query.getName( ) ) )
		{
			CubeHandle cubeHandle = (CubeHandle) this.cubeMetaDataHandleMap.get( query.getName( ) );

			for ( int i = 0; i < measures.size( ); i++ )
			{
				IMeasureDefinition measureDef = ( IMeasureDefinition ) measures.get( i );
				MeasureHandle measureHandle = cubeHandle.getMeasure( measureDef.getName( ) );
				if ( measureHandle != null )
					measureDef.setDataType( DataAdapterUtil.adaptModelDataType( measureHandle.getDataType( ) ) );
				//if cube is auto primary key, measure definition should ignore the aggregation function.
				if( cubeHandle.getBooleanProperty( ITabularCubeModel.AUTO_KEY_PROP ) )
				{
					measureDef.setAggrFunction( null );
				}
			}
		}
	}

    /**
     * Add aggFunc/aggOn for all measure[] expression.
     *
     * measure without aggFunc/aggOn is meaningless, so adding default
     * aggFunc/aggOn to fix those kinds of bindings.
     * 
     * @param query
     * @throws DataException
     * @throws AdapterException
     */
    private void refactorCubeQueryDefinition( ICubeQueryDefinition query )
			throws DataException, AdapterException
	{
		List bindings = query.getBindings( );
		List levelExprList = getAllAggrOns( query );
		for ( int i = 0; i < bindings.size( ); i++ )
		{
			IBinding binding = (IBinding) bindings.get( i );

			String measureName = OlapExpressionUtil.getMeasure( binding.getExpression( ) );
			if ( measureName != null )
			{
				if ( binding.getAggrFunction( ) == null )
				{
					String aggrFunc = getAggrFunction( query, measureName );
					binding.setAggrFunction( aggrFunc );
					if ( binding.getAggregatOns( ).size( ) == 0 )
					{
						for ( Iterator itr = levelExprList.iterator( ); itr.hasNext( ); )
						{
							binding.addAggregateOn( itr.next( ).toString( ) );
						}
					}
				}
			}
		}
	}


	/**
	 *
	 * @param query
	 * @param measureName
	 * @return
	 */
	private String getAggrFunction( ICubeQueryDefinition query,
			String measureName )
	{
		for ( Iterator itr = query.getMeasures( ).iterator( ); itr.hasNext( ); )
		{
			IMeasureDefinition measure = (IMeasureDefinition) itr.next( );
			if ( measure.getName( ).equals( measureName ) )
			{
				if ( measure.getAggrFunction( ) != null )
					return measure.getAggrFunction( );
				break;
			}
		}
		if ( this.cubeHandleMap != null
				&& this.cubeHandleMap.containsKey( query.getName( ) ) )
		{
			CubeHandle cubeHandle = (CubeHandle) this.cubeHandleMap.get( query.getName( ) );
			MeasureHandle measureHandle = cubeHandle.getMeasure( measureName );
			try
			{
				return DataAdapterUtil.adaptModelAggregationType( measureHandle.getFunction( ) );
			}
			catch ( AdapterException e )
			{
			}
		}
		return "SUM";
	}

	private List getAllAggrOns( ICubeQueryDefinition query )
	{
		List levels = CubeQueryDefinitionUtil.populateMeasureAggrOns( query );
		List levelExprs = new ArrayList();
		for ( Iterator itr = levels.iterator( ); itr.hasNext( ); )
		{
			DimLevel level = (DimLevel) itr.next( );
			levelExprs.add( ExpressionUtil.createJSDimensionExpression( level.getDimensionName( ),
					level.getLevelName( ) ) );
		}
		return levelExprs;
	}

	/**
	 * This method create a ResourceIdentifiers instance which is in turn being passed to appContext.
	 *
	 * The consumer of appContext, especially those Oda drivers, can then use it for acquire Resource info.
	 *
	 * @param handle
	 * @return
	 */
	private static ResourceIdentifiers createResourceIdentifiers(
			final ModuleHandle handle )
	{
		if ( handle == null )
			return null;
		try
		{
			ResourceIdentifiers identifiers = new ResourceIdentifiers( );
			if ( handle.getSystemId( ) != null )
			{
				identifiers.setDesignResourceBaseURI( handle.getSystemId( ).toURI( ) );
			}
			if( handle.getResourceFolder( ) != null )
			{
				URI uri = AccessController.doPrivileged( new PrivilegedAction<URI>()
				{
				  public URI run()
				  {
				    return new File( handle.getModule( ).getSession( ).getResourceFolder( ) ).toURI( );
				  }
				});

				identifiers.setApplResourceBaseURI( uri );
			}
			return identifiers;
		}
		catch ( URISyntaxException e )
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getCachedDataSetMetaData(org.eclipse.birt.data.engine.api.IBaseDataSourceDesign, org.eclipse.birt.data.engine.api.IBaseDataSetDesign)
	 */
	public IResultMetaData getCachedDataSetMetaData(
			IBaseDataSourceDesign dataSource, IBaseDataSetDesign dataSet )
			throws BirtException
	{
		return this.dataEngine.getCachedDataSetMetaData( dataSource, dataSet );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getCubeQueryUtil()
	 */
	public ICubeQueryUtil getCubeQueryUtil( )
	{
		return new CubeQueryUtil( this );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getAggregationFactory()
	 */
	public AggregationManager getAggregationManager ( ) throws DataException
	{
		return AggregationManager.getInstance();
	}

	public Scriptable getScope( ) throws AdapterException
	{
		try
		{
			return this.sessionContext.getDataEngineContext( ).getJavaScriptScope( );
		}
		catch ( BirtException e )
		{
			throw new AdapterException( ResourceConstants.EXCEPTION_ERROR, e );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#cancel()
	 */
	public void cancel( )
	{
		this.dataEngine.cancel( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#restart()
	 */
	public void restart( )
	{
		this.dataEngine.restart();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getQueryDefinitionCopyUtil()
	 */
	public IQueryDefinitionUtil getQueryDefinitionUtil( )
	{
		return new QueryDefinitionUtil( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#prepareQueries(java.util.List)
	 */
	public void registerQueries(IDataQueryDefinition[] queryDefns ) throws AdapterException
	{
		try
		{
			this.dataEngine.registerQueries( queryDefns );
			this.registeredQueries = queryDefns;
		}
		catch ( DataException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ), e );
		}
	}

	/*
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#prepare(org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition, java.util.Map)
	 */
	public IPreparedCubeQuery prepare( ISubCubeQueryDefinition query,
			Map appContext ) throws BirtException
	{
		QueryAdapter.adaptQuery( query );
		setModuleHandleToAppContext( appContext );
		return this.dataEngine.prepare( query, appContext );
	}

	/**
	 * Set the module handle instance to appContext
	 *
	 */
	private void setModuleHandleToAppContext( )
	{
		if ( this.sessionContext.getAppContext( ) == null )
		{
			this.sessionContext.setAppContext( new HashMap( ) );
		}
		setModuleHandleToAppContext( this.sessionContext.getAppContext( ) );
	}

	private void setModuleHandleToAppContext( Map appContext )
	{
		if ( appContext == null )
		{
			appContext = new HashMap( );
		}
		String resouceIDs = ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS;
		if ( !appContext.containsKey( resouceIDs )
				|| appContext.get( resouceIDs ) == null )
		{
			if ( this.sessionContext.getModuleHandle( ) != null )
			{
				appContext.put( resouceIDs,
						createResourceIdentifiers( this.sessionContext.getModuleHandle( ) ) );
			}
			else if ( this.sessionContext.getAppContext( ) != null )
			{
				appContext.put( resouceIDs, this.sessionContext.getAppContext( )
						.get( resouceIDs ) );
			}
		}
	}

	private void defineDataSourceDataSet( IQueryDefinition queryDefn ) throws BirtException
	{
		String dataSetName = queryDefn.getDataSetName( );

		ModuleHandle module = sessionContext.getModuleHandle();
		if ( module != null )
		{
			List l = module.getAllDataSets( );
			DataSetHandle handle = null;
			for ( int i = 0; i < l.size( ); i++ )
			{
				if ( ( (DataSetHandle) l.get( i ) ).getQualifiedName( ) != null
						&& ( (DataSetHandle) l.get( i ) ).getQualifiedName( )
								.equals( dataSetName ) )
				{
					handle = (DataSetHandle) l.get( i );
					break;
				}
			}
			DefineDataSourceSetUtil.defineDataSourceAndDataSet( handle, dataEngine, this.modelAdaptor, null );
			
			DefineDataSourceSetUtil.prepareForTransientQuery( sessionContext,
					dataEngine,
					handle,
					queryDefn,
					this.registeredQueries,
					this.interceptorContext );
		}
	}

	/**
	 *
	 * @param query
	 * @param resultMetaList
	 * @param levelNameColumnNamePair
	 * @param hierHandle
	 * @throws BirtException
	 */
	private void prepareLevels( QueryDefinition query,
			TabularHierarchyHandle hierHandle, List metaList, String dimName, String levelColumnName, boolean addGroup )
			throws BirtException
	{
		try
		{
			// Use same data set as cube fact table
			List levels = hierHandle.getContents( TabularHierarchyHandle.LEVELS_PROP );

			for ( int j = 0; j < levels.size( ); j++ )
			{

				TabularLevelHandle level = (TabularLevelHandle) levels.get( j );

				DataSetIterator.ColumnMeta temp = null;
				String exprString = ExpressionUtil.createJSDataSetRowExpression( levelColumnName == null ? level.getColumnName( ):levelColumnName );

				int type = DataAdapterUtil.adaptModelDataType( level.getDataType( ) );
				if ( type == DataType.UNKNOWN_TYPE || type == DataType.ANY_TYPE )
					type = DataType.STRING_TYPE;
				if ( level.getDateTimeLevelType( ) != null )
				{
					temp = new DataSetIterator.ColumnMeta( DataSetIterator.createLevelName( dimName, level.getName( )),
							new DataSetIterator.DataProcessorWrapper( GroupCalculatorFactory.getGroupCalculator( IGroupDefinition.NUMERIC_INTERVAL,
									DataType.INTEGER_TYPE,
									String.valueOf( DataSetIterator.getDefaultStartValue( level.getDateTimeLevelType( ),
											level.getIntervalBase( ) ) ),
									level.getIntervalRange( ), sessionContext.getDataEngineContext( ).getLocale( ),
									sessionContext.getDataEngineContext( ).getTimeZone( )) ),
							DataSetIterator.ColumnMeta.LEVEL_KEY_TYPE );
					temp.setDataType( DataType.INTEGER_TYPE );
					exprString = DataSetIterator.createDateTransformerExpr( level.getDateTimeLevelType( ), exprString );
				}
				else
				{
					IDataProcessor processor = null;
					if ( DesignChoiceConstants.LEVEL_TYPE_DYNAMIC.equals( level.getLevelType( ) ) )
					{
						int interval = GroupAdapter.intervalFromModel( level.getInterval( ) );
						if ( interval != IGroupDefinition.NO_INTERVAL )
							processor = new DataSetIterator.DataProcessorWrapper( GroupCalculatorFactory.getGroupCalculator( interval,
									type,
									level.getIntervalBase( ),
									level.getIntervalRange( ),
									sessionContext.getDataEngineContext( ).getLocale( ),
									sessionContext.getDataEngineContext( ).getTimeZone( )) );
					}
					else if ( DesignChoiceConstants.LEVEL_TYPE_MIRRORED.equals( level.getLevelType( ) ) )
					{
						Iterator it = level.staticValuesIterator( );
						List dispExpr = new ArrayList( );
						List filterExpr = new ArrayList( );
						while ( it.hasNext( ) )
						{
							RuleHandle o = (RuleHandle) it.next( );
							dispExpr.add( o.getDisplayExpression( ) );
							filterExpr.add( o.getRuleExpression( ) );

						}

						StringBuffer buf = new StringBuffer();

						if( level.getDefaultValue() != null )
						{
							buf.append( "\"" );
							buf.append( JavascriptEvalUtil.transformToJsConstants( level.getDefaultValue() ) );
							buf.append( "\";" );
						}
						for ( int i = 0; i < dispExpr.size( ); i++ )
						{
							String disp = "\""
									+ JavascriptEvalUtil.transformToJsConstants( String.valueOf( dispExpr.get( i ) ) )
									+ "\"";
							String filter = String.valueOf( filterExpr.get( i ) );
							buf.append( "if(" );
							buf.append( filter );
							buf.append( ")" );
							buf.append( disp );
							buf.append( ";" );
						}
						exprString = buf.toString( );

					}
					temp = new DataSetIterator.ColumnMeta( DataSetIterator.createLevelName( dimName, level.getName( )),
							processor,
							DataSetIterator.ColumnMeta.LEVEL_KEY_TYPE );
					temp.setDataType( type );
				}

				metaList.add( temp );
				Iterator it = level.attributesIterator( );
				while ( it.hasNext( ) )
				{
					LevelAttributeHandle levelAttr = (LevelAttributeHandle) it.next( );

					IDataProcessor processor = null;
					String bindingExpr = null;
					if( level.getDateTimeLevelType( ) != null && DataSetIterator.DATE_TIME_ATTR_NAME.equals( levelAttr.getName()))
					{
						processor = new DataSetIterator.DateTimeAttributeProcessor( level.getDateTimeLevelType( ),
								this.sessionContext.getDataEngineContext( )
										.getLocale( ), sessionContext.getDataEngineContext( ).getTimeZone( ) );
						bindingExpr = ExpressionUtil.createJSDataSetRowExpression( levelColumnName == null ? level.getColumnName( ):levelColumnName ) ;
					}else
					{
						bindingExpr = ExpressionUtil.createJSDataSetRowExpression( levelAttr.getName() ) ;
					}
					DataSetIterator.ColumnMeta meta = new DataSetIterator.ColumnMeta( DataSetIterator.createLevelName( dimName, OlapExpressionUtil.getAttributeColumnName( level.getName( ),
							levelAttr.getName( ) )),
							processor,
							DataSetIterator.ColumnMeta.UNKNOWN_TYPE );

					meta.setDataType( DataAdapterUtil.adaptModelDataType( levelAttr.getDataType( ) ) );
					metaList.add( meta );

					query.addBinding( new Binding( meta.getName( ),
							new ScriptExpression( bindingExpr ) ));
				}

				//Only dynamical level can use display name.
				if ( DesignChoiceConstants.LEVEL_TYPE_DYNAMIC.equals( level.getLevelType( ) )
						&& level.getDisplayColumnName( ) != null )
				{
					DataSetIterator.ColumnMeta meta = new DataSetIterator.ColumnMeta( DataSetIterator.createLevelName( dimName,
							OlapExpressionUtil.getDisplayColumnName( level.getName( ) ) ),
							null,
							DataSetIterator.ColumnMeta.UNKNOWN_TYPE );
					meta.setDataType( DataType.STRING_TYPE );
					metaList.add( meta );
					ExpressionHandle displayExprHandle = level.getExpressionProperty( ITabularLevelModel.DISPLAY_COLUMN_NAME_PROP );
					if( displayExprHandle != null )
					{
						query.addBinding( new Binding( meta.getName( ),
							modelAdaptor.adaptJSExpression( displayExprHandle.getStringExpression( ), displayExprHandle.getType( ) ) ) );
					}
				}

				//Populate Security column
				if( level.getMemberACLExpression( )!= null && level.getMemberACLExpression( ).getExpression( )!= null )
				{
					String aclExprName = DataSetIterator.createLevelACLName (level.getName( ));
					IScriptExpression expr = modelAdaptor.adaptExpression( (Expression) level.getMemberACLExpression( ).getValue( ));
					query.addBinding( new Binding( aclExprName, expr ) );
					DataSetIterator.ColumnMeta meta = new DataSetIterator.ColumnMeta( aclExprName, null, DataSetIterator.ColumnMeta.UNKNOWN_TYPE );
					metaList.add( meta );
				}

				String levelName = DataSetIterator.createLevelName( dimName, level.getName( ));
				query.addBinding( new Binding( levelName ,
						new ScriptExpression( exprString, type )));
				GroupDefinition gd = new GroupDefinition( String.valueOf( query.getGroups( ).size( )));

//				if ( ExpressionUtil.getColumnName( exprString ) != null )
//				{
//					//if exprString is just dataSetRow["xxx"]
//					//dataSetRow["xxx"] evaluation faster than row["xxx"] since its value are just from data set result set rather than Rhino
//					gd.setKeyExpression( exprString );
//				}
//				else
				{
					gd.setKeyExpression( ExpressionUtil.createJSRowExpression( levelName ) );
				}

				if ( level.getLevelType( ) != null && level.getDateTimeLevelType( ) == null )
				{
					gd.setIntervalRange( level.getIntervalRange( ) );
					gd.setIntervalStart( level.getIntervalBase( ) );
					gd.setInterval( GroupAdapter.intervalFromModel( level.getInterval( ) ) );
				}
				if ( level.getDateTimeLevelType( ) != null )
				{
					gd.setIntervalRange( level.getIntervalRange( ) == 0 ? 1
							: level.getIntervalRange( ) );
					gd.setIntervalStart( String.valueOf( DataSetIterator.getDefaultStartValue( level.getDateTimeLevelType( ),level.getIntervalBase( ))) );
					gd.setInterval( IGroupDefinition.NUMERIC_INTERVAL  );
				}
				if( addGroup )
					query.addGroup( gd );
			}
		}
		catch ( DataException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ), e );
		}
	}

	/**
	 *
	 * @param cubeHandle
	 * @param query
	 * @param resultMetaList
	 * @throws DataException
	 * @throws AdapterException
	 */
	private void prepareMeasure( TabularCubeHandle cubeHandle,
			QueryDefinition query, List metaList ) throws AdapterException
	{
		try
		{
			List measureGroups = cubeHandle.getContents( CubeHandle.MEASURE_GROUPS_PROP );
			for ( int i = 0; i < measureGroups.size( ); i++ )
			{
				MeasureGroupHandle mgh = (MeasureGroupHandle) measureGroups.get( i );
				List measures = mgh.getContents( MeasureGroupHandle.MEASURES_PROP );
				for ( int j = 0; j < measures.size( ); j++ )
				{
					MeasureHandle measure = (MeasureHandle) measures.get( j );
					if( measure.isCalculated( ) )
					{
						continue;
					}
					String function = measure.getFunction( );
					String exprText = measure.getMeasureExpression( );
					ExpressionHandle measureExprHandle = measure.getExpressionProperty( IMeasureModel.MEASURE_EXPRESSION_PROP );
					IScriptExpression expr = null;
					if( exprText != null && measureExprHandle != null )
					{
						expr = modelAdaptor.adaptJSExpression( measureExprHandle.getStringExpression( ),
									measureExprHandle.getType( ) );
					}

					Binding binding = new Binding( measure.getName( ), expr );
					binding.setAggrFunction( DataAdapterUtil.adaptModelAggregationType( function ) );

					query.addBinding( binding );
					DataSetIterator.ColumnMeta meta = new DataSetIterator.ColumnMeta( measure.getName( ),
							null,
							DataSetIterator.ColumnMeta.MEASURE_TYPE );
					meta.setDataType( DataAdapterUtil.adaptModelDataType( measure.getDataType( ) ) );
					metaList.add( meta );
				}
			}
		}
		catch ( DataException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ), e );
		}
	}

	/**
	 *
	 * @param session
	 * @param filterIterator
	 * @param query
	 * @throws AdapterException
	 */
	private static void popualteFilter( DataRequestSession session,
			Iterator filterIterator, QueryDefinition query ) throws AdapterException
	{
		while( filterIterator.hasNext( ) )
		{
			FilterConditionHandle filter = (FilterConditionHandle) filterIterator.next( );
			query.addFilter( session.getModelAdaptor( ).adaptFilter( filter ) );
		}
	}

	 private boolean isDateTimeDimension( TabularHierarchyHandle hierHandle )
	 {
		 List levels = hierHandle.getContents( TabularHierarchyHandle.LEVELS_PROP );

		for ( int j = 0; j < levels.size( ); j++ )
		{
			TabularLevelHandle level = (TabularLevelHandle) levels.get( j );
			if ( level.getDateTimeLevelType( ) == null )
				return false;
		}
		 return true;
	 }
	/**
	 *
	 * @param session
	 * @param cubeHandle
	 * @param metaList
	 * @return
	 * @throws BirtException
	 */
	QueryDefinition createQuery(
			DataRequestSessionImpl session, TabularCubeHandle cubeHandle,
			List metaList ) throws BirtException
	{
		if( metaList == null )
			metaList = new ArrayList();
		QueryDefinition query = new CubeCreationQueryDefinition( );
		//Ensure the query execution result would not be save to report document.
		query.setAsTempQuery( );

		query.setName( String.valueOf( cubeHandle.getElement( ).getID( )) );
		if( cubeHandle.getDataSet( ) == null )
			throw new AdapterException(AdapterResourceHandle.getInstance( )
					.getMessage( ResourceConstants.CUBE_MISS_DATASET_ERROR ));
		query.setDataSetName( cubeHandle.getDataSet( ).getQualifiedName( ) );

		List dimensions = cubeHandle.getContents( CubeHandle.DIMENSIONS_PROP );

		if ( dimensions != null )
		{
			for ( int i = 0; i < dimensions.size( ); i++ )
			{
				DimensionHandle dimension = (DimensionHandle) dimensions.get( i );
				List hiers = dimension.getContents( DimensionHandle.HIERARCHIES_PROP );

				//By now we only support one hierarchy per;

				TabularHierarchyHandle hierHandle = (TabularHierarchyHandle) hiers.get( 0 );

				if ( hierHandle.getDataSet( ) == null
						|| hierHandle.getDataSet( )
								.getQualifiedName( )
								.equals( cubeHandle.getDataSet( ).getQualifiedName( ) ) )
				{
					prepareLevels( query,
							hierHandle,
							metaList,
							dimension.getName(), null, true );
					continue;
				}
				DimensionJoinConditionHandle condition = getFacttableJointKey(cubeHandle,
						hierHandle);
				if( isDateTimeDimension( hierHandle ) && existColumnName( hierHandle, condition.getHierarchyKey( ) ) )
				{
					prepareLevels( query,
							hierHandle,
							metaList,
							dimension.getName(), condition.getCubeKey( ), true );
					continue;
				}

				Iterator it = cubeHandle.joinConditionsIterator( );
				while ( it.hasNext( ) )
				{
					DimensionConditionHandle dimCondHandle = (DimensionConditionHandle) it.next( );
						if ( dimCondHandle.getHierarchy( ).getName( ).equals( hierHandle.getName( ) ) )
					{
						Iterator conditionIt = dimCondHandle.getJoinConditions( )
								.iterator( );
						while ( conditionIt.hasNext( ) )
						{
							DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionIt.next( );
							String cubeKey = joinCondition.getCubeKey( );
							String cubeKeyWithDimIdentifier = OlapExpressionUtil.getQualifiedLevelName( dimension.getName( ),
									cubeKey );
							DataSetIterator.ColumnMeta temp = new DataSetIterator.ColumnMeta( cubeKeyWithDimIdentifier,
									null,
									DataSetIterator.ColumnMeta.LEVEL_KEY_TYPE );
							temp.setDataType( getColumnDataType( cubeHandle.getDataSet( ), cubeKey ) );
							metaList.add( temp );
							query.addBinding( new Binding( cubeKeyWithDimIdentifier,
									new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( cubeKey ) ) ) );
							/*GroupDefinition gd = new GroupDefinition( String.valueOf( query.getGroups( )
									.size( ) ) );
								//dataSetRow["xxx"] evaluation faster than row["xxx"] since its value are just from data set result set rather than Rhino
								gd.setKeyExpression( ExpressionUtil.createJSDataSetRowExpression( cubeKey ) );
								query.addGroup( gd );*/
							}
						}
					}
			}
		}

		session.prepareMeasure( cubeHandle, query, metaList );
		DataRequestSessionImpl.popualteFilter( session, cubeHandle.filtersIterator( ), query );
		return query;
	}

	private boolean existColumnName( TabularHierarchyHandle hierHandle, String name )
	{
		List levels = hierHandle.getContents( TabularHierarchyHandle.LEVELS_PROP );

		for ( int j = 0; j < levels.size( ); j++ )
		{
			TabularLevelHandle level = (TabularLevelHandle) levels.get( j );
			if ( level.getColumnName( ).equals( name ) )
				return true;
		}
		return false;
	}

	private DimensionJoinConditionHandle getFacttableJointKey(TabularCubeHandle cubeHandle,
			TabularHierarchyHandle hierHandle)
	{
		Iterator it = cubeHandle.joinConditionsIterator( );
		while ( it.hasNext( ) )
		{
			DimensionConditionHandle dimCondHandle = (DimensionConditionHandle) it.next( );

			if ( dimCondHandle.getHierarchy( ).getName( ).equals( hierHandle.getName( ) ) )
			{
				Iterator conditionIt = dimCondHandle.getJoinConditions( ).iterator( );
				while ( conditionIt.hasNext( ) )
				{
					DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionIt.next( );
					return joinCondition;
				}
			}
		}
		return null;
	}

	/**
	 *
	 * @param session
	 * @param cubeHandle
	 * @param metaList
	 * @return
	 * @throws BirtException
	 */
	private QueryDefinition createQueryForTempPKDimension(
		TabularCubeHandle cubeHandle ) throws BirtException
	{
		QueryDefinition query = new QueryDefinition( );
		//Ensure the query execution result would not be save to report document.
		query.setAsTempQuery( );

		query.setUsesDetails( false );
		query.setDataSetName( cubeHandle.getDataSet( ).getQualifiedName( ) );
		query.setIsSummaryQuery( true );
		query.setName( String.valueOf( cubeHandle.getElement( ).getID( )));
		DataRequestSessionImpl.popualteFilter( this, cubeHandle.filtersIterator( ), query );
		return query;
	}

	/**
	 *
	 * @param handle
	 * @return
	 */
	private static String getDataSet( TabularHierarchyHandle handle )
	{
		if ( handle.getDataSet( ) != null )
			return handle.getDataSet( ).getQualifiedName( );
		else if ( handle.getProperty( ITabularCubeModel.DATA_SET_PROP ) != null )
		{
			return handle.getProperty( ITabularCubeModel.DATA_SET_PROP )
					.toString( );
		}
		else
		{
			CubeHandle cubeHandle = acquireContainerCube( handle );
			if ( cubeHandle != null )
			{
				return cubeHandle.getElementProperty( ITabularCubeModel.DATA_SET_PROP )
						.getQualifiedName( );
			}
		}
		return null;
	}

	/**
	 *
	 * @param hierHandle
	 * @return
	 */
	private static CubeHandle acquireContainerCube(
			TabularHierarchyHandle hierHandle )
	{
		DesignElementHandle handle = hierHandle.getContainer( ).getContainer( );
		if ( handle == null || !( handle instanceof CubeHandle ) )
			return null;
		return (CubeHandle) handle;
	}

	/**
	 *
	 * @param handle
	 * @return
	 */
	private static Iterator getFilterIterator( TabularHierarchyHandle handle )
	{
		if ( handle.getDataSet( )!= null )
			return handle.filtersIterator( );
		else
		{
			CubeHandle cubeHandle = DataRequestSessionImpl.acquireContainerCube( handle );
			if( cubeHandle!= null )
				return cubeHandle.filtersIterator( );
		}
		return new ArrayList().iterator( );
	}

	/**
	 * Create a query definition for an Hierarchy.
	 *
	 * @param session
	 * @param hierHandle
	 * @param metaList
	 * @return
	 * @throws BirtException
	 */
	QueryDefinition createQuery(
			DataRequestSessionImpl session, TabularHierarchyHandle hierHandle,
			List metaList, String cubeName ) throws BirtException
	{
		assert metaList!= null;
		QueryDefinition query = new CubeCreationQueryDefinition( );
		//Ensure the query execution result would not be save to report document.
		query.setAsTempQuery( );
		query.setUsesDetails( false );

		query.setDataSetName( DataRequestSessionImpl.getDataSet ( hierHandle ) );
		query.setName( cubeName );

		prepareLevels( query,
				hierHandle, metaList, null, null, true );

		DataRequestSessionImpl.popualteFilter( session, DataRequestSessionImpl.getFilterIterator( hierHandle ), query );
		return query;
	}

	/**
	 * 
	 * @param session
	 * @param dim
	 * @param hierHandle
	 * @param metaList
	 * @param cubeName
	 * @return
	 * @throws BirtException
	 */
	QueryDefinition createDimensionQuery(
			DataRequestSessionImpl session, DimensionHandle dim, TabularHierarchyHandle hierHandle,
			List metaList, String cubeName ) throws BirtException
	{
		return this.createDimensionQuery( session, dim, hierHandle, metaList, cubeName, null );
	}
	
	/**
	 * Create a query definition for an Hierarchy.
	 *
	 * @param session
	 * @param hierHandle
	 * @param metaList
	 * @return
	 * @throws BirtException
	 */
	QueryDefinition createDimensionQuery(
			DataRequestSessionImpl session, DimensionHandle dim, TabularHierarchyHandle hierHandle,
			List metaList, String cubeName, String levelColumnName ) throws BirtException
	{
		assert metaList!= null;
		QueryDefinition query = new CubeCreationQueryDefinition( );
		//Ensure the query execution result would not be save to report document.
		query.setAsTempQuery( );
		query.setDataSetName( DataRequestSessionImpl.getDataSet ( hierHandle ) );
		query.setName( cubeName );

		prepareLevels( query,
				hierHandle, metaList, null, levelColumnName, false );

		DataRequestSessionImpl.popualteFilter( session, DataRequestSessionImpl.getFilterIterator( hierHandle ), query );
		
		//if it is a time dimension
		if ( CubeHandleUtil.isTimeDimension( dim ) )
		{
			FilterDefinition filter = null;
			if( ( dim instanceof TabularDimensionHandle ) &&((TabularDimensionHandle) dim ).getSharedDimension( )!= null )
			{
				filter = buildFilterForTimeDimension( ((TabularDimensionHandle) dim ).getSharedDimension( ),
						hierHandle );
			}
			else
			{
				filter = buildFilterForTimeDimension( dim, hierHandle );
			}
			if ( filter != null )
				query.addFilter( filter );
		}
		return query;
	}
	@Override
	public DataSessionContext getDataSessionContext( )
	{
		return this.sessionContext;
	}

	private static String getCubeTempPKDimensionName( TabularCubeHandle tch )
	{
		return "TEMP_PK_DIMENSION_" + tch.hashCode( );
	}

	private static String getCubeTempPKHierarchyName( TabularCubeHandle tch )
	{
		return "TEMP_PK_HIERARCHY_" + tch.hashCode( );
	}

	private static String getCubeTempPKFieldName( TabularCubeHandle tch )
	{
		return "TEMP_PK_" + tch.hashCode( );
	}

	private static <T> T[] appendArray( T[] src, T v )
	{
		T[] result = (T[])java.lang.reflect.Array.
			newInstance(src.getClass().getComponentType(), src.length + 1);
		System.arraycopy( src, 0, result, 0, src.length );
		result[src.length] = v;
		return result;
	}

	private static int getColumnDataType( DataSetHandle dsh, String jointHierarchyKey )
	{
		CachedMetaDataHandle cmdh = dsh.getCachedMetaDataHandle( );
		Iterator itr = cmdh.getResultSet( ).iterator( );
		while ( itr.hasNext( ) )
		{
			ResultSetColumnHandle rsch = (ResultSetColumnHandle)itr.next( );
			if ( rsch.getColumnName( ).equals( jointHierarchyKey ))
			{
				return org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType( rsch.getDataType( ) );
			}
		}
		return DataType.STRING_TYPE;
	}

	private String getDummyLevelNameForJointHierarchyKey( String hierarchyKey )
	{
		return hierarchyKey + "_Dummy" + this.hashCode( );
	}

	private String[] getDummyLevelNamesForJointHierarchyKeys( String[] hierarchyKeys )
	{
		String[] result = new String[hierarchyKeys.length];
		int i = 0;
		for ( String key : hierarchyKeys )
		{
			result[i] = getDummyLevelNameForJointHierarchyKey( key );
			i++;
		}
		return result;
	}

	public DataEngine getDataEngine( )
	{
		return this.dataEngine;
	}

	@Override
	public IBasePreparedQuery prepare(IDataQueryDefinition query)
			throws AdapterException
	{
		return prepare( query, null );
	}

	@Override
	public IPreparedCubeQuery prepare(ISubCubeQueryDefinition query)
			throws BirtException
	{
		return prepare( query, null );
	}

	@Override
	public IFilterUtil getFilterUtil( ) throws BirtException
	{
		return new FilterUtil( );
	}
}
