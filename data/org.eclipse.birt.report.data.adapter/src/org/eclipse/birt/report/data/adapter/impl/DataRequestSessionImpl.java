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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
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
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.CubeElementFactory;
import org.eclipse.birt.data.engine.olap.data.api.cube.CubeMaterializer;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IColumnValueIterator;
import org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IQueryDefinitionUtil;
import org.eclipse.birt.report.data.adapter.api.IRequestInfo;
import org.eclipse.birt.report.data.adapter.group.GroupCalculatorFactory;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.data.adapter.impl.DataSetIterator.ColumnMeta;
import org.eclipse.birt.report.data.adapter.impl.DataSetIterator.IDataProcessor;
import org.eclipse.birt.report.data.adapter.internal.adapter.GroupAdapter;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
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
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;
import org.mozilla.javascript.Scriptable;

/**
 * Implementation of DataRequestSession
 */
public class DataRequestSessionImpl extends DataRequestSession
{

	//
	private DataEngineImpl dataEngine;
	private IModelAdapter modelAdaptor;
	private DataSessionContext sessionContext;
	private Map cubeHandleMap;
	private StopSign stopSign;

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
		modelAdaptor = new ModelAdapter( context );
		sessionContext = context;
		cubeHandleMap = new HashMap( );
		stopSign = new StopSign( );
		if( sessionContext!= null )
		{
			this.setModuleHandleToAppContext();
		}
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
				this.sessionContext ).getDataSetMetaData( dataSetHandle,
				useCache );
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
	public IColumnValueIterator getColumnValueIterator (  DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			String boundColumnName ) throws BirtException
	{
		return this.getColumnValueIterator(dataSet, inputParamBindings, columnBindings, boundColumnName, null );
	}
	
	/**
	 * 
	 * @param dataSet
	 * @param inputParamBindings
	 * @param columnBindings
	 * @param boundColumnName
	 * @param requestInfo
	 * @return
	 * @throws BirtException
	 */
	private IColumnValueIterator getColumnValueIterator( DataSetHandle dataSet,
			Iterator inputParamBindings, Iterator columnBindings,
			String boundColumnName, IRequestInfo requestInfo ) throws BirtException
	{
		ArrayList temp = new ArrayList( );
		
		while ( columnBindings != null && columnBindings.hasNext( ) )
		{
			temp.add( columnBindings.next( ) );
		}
//		if ( referToAggregation( temp, boundColumnName ) )
//			return new ColumnValueIterator( null, null, null );
		
		IQueryResults queryResults = getQueryResults( dataSet,
				inputParamBindings,
				temp.iterator( ),
				boundColumnName );
		return new ColumnValueIterator( queryResults, boundColumnName, requestInfo );
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
				boundColumnName,
				requestInfo );
		
		ArrayList values = new ArrayList( );

		do
		{
			if ( columnValueIterator.getValue()!=null )
				values.add( columnValueIterator.getValue( ) );
		}while(  columnValueIterator.next( )  );
		
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
				this.sessionContext ).refreshMetaData( dataSetHandle );
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
				this.sessionContext ).refreshMetaData( dataSetHandle, holdEvent );
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
				this.sessionContext ).executeQuery( queryDefn,
				paramBindingIt,
				filterIt,
				bindingIt, this.sessionContext.getTopScope() );
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
	 * @see org.eclipse.birt.report.data.adaptor.impl.IDataRequestSession#prepare(org.eclipse.birt.data.engine.api.IQueryDefinition,
	 *      java.util.Map)
	 */
	public IPreparedQuery prepare( IQueryDefinition query, Map appContext )
			throws BirtException
	{
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
		dataEngine.shutdown( );
		dataEngine = null;
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
			String boundColumnName ) throws BirtException
	{
		assert dataSet != null;
		// TODO: this is the inefficient implementation
		// Need to enhance the implementation to verify that the column is bound
		// to a data set column

		// Run a query with the provided binding information. Group by bound
		// column so we can
		// retrieve distinct values using the grouping feature
		QueryDefinition query = new QueryDefinition( );
		query.setDataSetName( dataSet.getQualifiedName( ) );
		boolean useDataSetFilter = true;
		if( columnBindings == null || !columnBindings.hasNext() )
		{
			query.setAutoBinding(true);
			useDataSetFilter = false;
		}

		ModuleHandle moduleHandle = sessionContext.getModuleHandle( );
		if ( moduleHandle == null )
			moduleHandle = dataSet.getModuleHandle( );

		QueryExecutionHelper execHelper = new QueryExecutionHelper( this.dataEngine,
				this.modelAdaptor,
				this.sessionContext );
		IQueryResults results = execHelper.executeQuery( query,
				inputParamBindings,
				null,
				columnBindings, 
				useDataSetFilter, 
				false,
				this.sessionContext.getTopScope());
		return results;
	}

	/**
	 * This method is used to validate the column binding to see if it contains aggregations.
	 * If so then return true, else return false;
	 * 
	 * @param columnBindings
	 * @param boundColumnName
	 * @throws BirtException
	 */
	private boolean referToAggregation( List bindings,
			String boundColumnName ) throws BirtException
	{
		if ( boundColumnName == null )
			return true;
		Iterator columnBindings = bindings.iterator( ); 
		while ( columnBindings != null && columnBindings.hasNext( ) )
		{
			IComputedColumn column = this.modelAdaptor.adaptComputedColumn( (ComputedColumnHandle) columnBindings.next( ) );
			if ( column.getName( ).equals( boundColumnName ) )
			{
				ScriptExpression sxp = (ScriptExpression) column.getExpression( );
				if ( column.getAggregateFunction( )!= null || ExpressionUtil.hasAggregation( sxp.getText( ) ) )
				{
					return true;
				}
				else
				{
					Iterator columnBindingNameIt = ExpressionUtil.extractColumnExpressions( sxp.getText( ) )
							.iterator( );
					while ( columnBindingNameIt.hasNext( ) )
					{
						IColumnBinding columnBinding = (IColumnBinding)columnBindingNameIt.next( );
						
						if ( referToAggregation( bindings,
								columnBinding.getResultSetColumnName( ) ) )
							return true;
					}
				}
			}
		}
		return false;
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
				this.defineDataSet( baseDataSet );
		}
		
		if ( !cubeHandleMap.containsKey( cubeHandle.getQualifiedName( ) ) )
		{
			this.cubeHandleMap.put( cubeHandle.getQualifiedName( ), cubeHandle );
		}
	}
	
	/**
	 * 
	 * @param cubeHandle
	 * @param appContext
	 * @param stopSign
	 * @throws BirtException
	 */
	private void materializeCube( CubeHandle cubeHandle, Map appContext ) throws BirtException
	{
		int mode = this.sessionContext.getDataEngineContext( ).getMode( );
		try
		{
			CubeMaterializer cubeMaterializer = null;
			if ( appContext == null )
				appContext = sessionContext.getAppContext( );

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
				cubeMaterializer = createCubeMaterializer( cubeHandle, size );
				createCube( (TabularCubeHandle) cubeHandle,
						cubeMaterializer,
						appContext );
				cubeMaterializer.close( );
			}
			else if ( mode == DataEngineContext.MODE_GENERATION )
			{
				cubeMaterializer = createCubeMaterializer( cubeHandle, 0 );
				createCube(  (TabularCubeHandle)cubeHandle, cubeMaterializer, appContext );
				cubeMaterializer.saveCubeToReportDocument( cubeHandle.getQualifiedName( ),
						this.sessionContext.getDocumentWriter( ),
						null );
				cubeMaterializer.close( );
			}
		}
		catch ( Exception e )
		{
			throw new DataException( ResourceConstants.EXCEPTION_ERROR, e);
		}
	}

	/**
	 * Create a cube materializer.
	 * @param cubeHandle
	 * @return
	 * @throws DataException
	 * @throws IOException
	 * @throws BirtException
	 */
	private CubeMaterializer createCubeMaterializer( CubeHandle cubeHandle, int size )
			throws DataException, IOException, BirtException
	{
		CubeMaterializer cubeMaterializer = new CubeMaterializer( this.dataEngine,
				cubeHandle.getQualifiedName( ), size );
		return cubeMaterializer;
	}

	/**
	 * 
	 * @param cubeHandle
	 * @param cubeMaterializer
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 * @throws DataException
	 */
	private void createCube( TabularCubeHandle cubeHandle,
			CubeMaterializer cubeMaterializer, Map appContext )
			throws IOException, BirtException, DataException
	{
		Map<?,?> backupAppContext = new HashMap();
		if( appContext == null )
			appContext = new HashMap();
		//Please note that we should always use original application context during query execution,
		//rather than create a new one with same properties.Application Context is sometimes used as cross-query
		//information carrier.
		
		backupAppContext.putAll( appContext );

		Map<ReportElementHandle, IQueryDefinition> queryMap = new HashMap<ReportElementHandle, IQueryDefinition>();
		Map<ReportElementHandle, List<ColumnMeta>> metaMap = new HashMap<ReportElementHandle, List<ColumnMeta>>();
		
		prepareForCubeGeneration( cubeHandle, queryMap, metaMap );
		
		List measureNames = new ArrayList( );
		List measureGroups = cubeHandle.getContents( CubeHandle.MEASURE_GROUPS_PROP );
		for ( int i = 0; i < measureGroups.size( ); i++ )
		{
			MeasureGroupHandle mgh = (MeasureGroupHandle) measureGroups.get( i );
			List measures = mgh.getContents( MeasureGroupHandle.MEASURES_PROP );
			for ( int j = 0; j < measures.size( ); j++ )
			{
				MeasureHandle measure = (MeasureHandle) measures.get( j );
				measureNames.add( measure.getName( ) );
			}
		}

		IDimension[] dimensions = populateDimensions( cubeMaterializer,
				cubeHandle,
				appContext, 
				queryMap, metaMap);
		String[][] factTableKey = new String[dimensions.length][];
		String[][] dimensionKey = new String[dimensions.length][];

		for ( int i = 0; i < dimensions.length; i++ )
		{
			TabularDimensionHandle dim = (TabularDimensionHandle) cubeHandle.getDimension( dimensions[i].getName( ) );
			TabularHierarchyHandle hier = (TabularHierarchyHandle) dim.getDefaultHierarchy( );
			if ( cubeHandle.getDataSet( ).equals( hier.getDataSet( ) ) || hier.getDataSet( ) == null )
			{

				String[] keyNames = dimensions[i].getHierarchy().getLevels()[dimensions[i]
						.getHierarchy().getLevels().length - 1].getKeyNames();
				for( int j = 0; j < keyNames.length; j++)                                       						
				{
					keyNames[j] = dimensions[i].getName() + "/" + keyNames[j];
				}
				factTableKey[i] = keyNames;
				dimensionKey[i] = factTableKey[i];
			}
			else
			{
				Iterator it = cubeHandle.joinConditionsIterator( );
				if ( !it.hasNext() )
					throw new AdapterException( ResourceConstants.MISSING_JOIN_CONDITION, dim.getName() );
			
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
								dimensionKeys.add( joinCondition.getHierarchyKey( ) );
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
		cubeMaterializer.createCube( cubeHandle.getQualifiedName( ),
				factTableKey,
				dimensionKey,
				dimensions,
				new DataSetIterator( this,
						queryMap.get( cubeHandle ),
						metaMap.get( cubeHandle ),
						appContext ),
				this.toStringArray( measureNames ),
				stopSign );
		appContext.clear( );
		appContext.putAll( backupAppContext );
		
	}

	/**
	 * 
	 * @param cubeHandle
	 * @throws AdapterException
	 * @throws DataException
	 */
	private void prepareForCubeGeneration( TabularCubeHandle cubeHandle,
			Map<ReportElementHandle, IQueryDefinition> queryMap,
			Map<ReportElementHandle, List<ColumnMeta>> metaMap )
			throws AdapterException, DataException
	{
		List<IQueryDefinition> queryDefns = new ArrayList<IQueryDefinition>();
		
		List<ColumnMeta> metaList = new ArrayList<ColumnMeta>();
		IQueryDefinition query =  DataRequestSessionImpl.createQuery( this, cubeHandle, metaList );
		queryDefns.add( query );
		queryMap.put( cubeHandle, query );
		metaMap.put( cubeHandle, metaList );
		
		List<DimensionHandle> dimHandles = cubeHandle.getContents( CubeHandle.DIMENSIONS_PROP );
		for ( DimensionHandle dim:dimHandles )
		{
			List<TabularHierarchyHandle> hiers = dim.getContents( DimensionHandle.HIERARCHIES_PROP );
			for ( TabularHierarchyHandle hier: hiers )
			{
				metaList = new ArrayList<ColumnMeta>();
				query =  DataRequestSessionImpl.createQuery( this,  hier, metaList );
				queryDefns.add( query );
				queryMap.put( hier, query );
				metaMap.put( hier, metaList );
			}
		}
		
		this.dataEngine.registerQueries( queryDefns.toArray( new IDataQueryDefinition[0] ) );
	} 

	/**
	 * Clear the data set caches that are used in cube creation.
	 * 
	 * @param dataSetHandles
	 * @throws BirtException
	 */
	private void clearCache( Set dataSetHandles )
	{
		Iterator it = dataSetHandles.iterator( );
		while ( it.hasNext( ) )
		{
			try
			{
				DataSetHandle dsHandle = (DataSetHandle) it.next( );
				BaseDataSourceDesign baseDataSource = this.modelAdaptor.adaptDataSource( dsHandle.getDataSource( ) );
				BaseDataSetDesign baseDataSet = this.modelAdaptor.adaptDataSet( dsHandle );
				this.dataEngine.clearCache( baseDataSource, baseDataSet );

				if ( dsHandle instanceof JointDataSetHandle )
				{
					Set parentSet = new HashSet( );
					Iterator parentIt = ( (JointDataSetHandle) dsHandle ).dataSetsIterator( );
					while ( parentIt != null && parentIt.hasNext( ) )
					{
						parentSet.add( parentIt.next( ) );
					}
					clearCache( parentSet );
				}
			}
			catch ( Exception e )
			{
				//Do nothing
			}
		}
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
	 * @throws IOException
	 * @throws BirtException
	 * @throws DataException
	 */
	private IDimension[] populateDimensions( CubeMaterializer cubeMaterializer,
			TabularCubeHandle cubeHandle, Map appContext,
			Map<ReportElementHandle, IQueryDefinition> queryMap,
			Map<ReportElementHandle, List<ColumnMeta>> metaMap )
			throws IOException, BirtException, DataException
	{
		List dimHandles = cubeHandle.getContents( CubeHandle.DIMENSIONS_PROP );
		List result = new ArrayList( );
		for ( int i = 0; i < dimHandles.size( ); i++ )
		{
			result.add( populateDimension( cubeMaterializer,
					(DimensionHandle) dimHandles.get( i ),
					cubeHandle,
					appContext,
					queryMap,
					metaMap ) );
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
	 * @throws IOException
	 * @throws BirtException
	 * @throws DataException
	 */
	private IDimension populateDimension( CubeMaterializer cubeMaterializer,
			DimensionHandle dim, TabularCubeHandle cubeHandle, Map appContext,
			Map<ReportElementHandle, IQueryDefinition> queryMap,
			Map<ReportElementHandle, List<ColumnMeta>> metaMap )
			throws IOException,
			BirtException, DataException
	{
		List hiers = dim.getContents( DimensionHandle.HIERARCHIES_PROP );
		List iHiers = new ArrayList();
		for ( int j = 0; j < hiers.size( ); j++ )
		{
			TabularHierarchyHandle hierhandle = (TabularHierarchyHandle) hiers.get( 0 );
			List levels = hierhandle.getContents( TabularHierarchyHandle.LEVELS_PROP );

			ILevelDefn[] levelInHier = null;
			if( hierhandle.getLevelCount( ) == 1 )
				levelInHier = new ILevelDefn[1];
			else
				levelInHier = new ILevelDefn[hierhandle.getLevelCount( )+1];

			String[] leafLevelKeyColumn = new String[levels.size( )];
			for ( int k = 0; k < levels.size( ); k++ )
			{
				TabularLevelHandle level = (TabularLevelHandle) levels.get( k );
				List levelKeys = new ArrayList();
				Iterator it = level.attributesIterator( );
				while( it.hasNext( ) )
				{
					LevelAttributeHandle levelAttr = (LevelAttributeHandle)it.next( );
					levelKeys.add( OlapExpressionUtil.getAttributeColumnName( level.getName( ),
							levelAttr.getName( ) ) );
				}
				if ( DesignChoiceConstants.LEVEL_TYPE_DYNAMIC.equals( level.getLevelType( ) )
						&& level.getDisplayColumnName( ) != null )
				{
					levelKeys.add( OlapExpressionUtil.getDisplayColumnName( level.getName( ) ) );
				}
				leafLevelKeyColumn[k] = level.getName( );
				
				levelInHier[k] = CubeElementFactory.createLevelDefinition( level.getName( ),
						new String[]{
							level.getName( )
						},
						this.toStringArray( levelKeys ) );
			}
			
			createLeafLevel( levels, levelInHier, leafLevelKeyColumn );
		
			iHiers.add( cubeMaterializer.createHierarchy( dim.getName( ), hierhandle.getName( ),
					new DataSetIterator( this,
							queryMap.get( hierhandle ),
							metaMap.get( hierhandle ),
							appContext ),
					levelInHier, stopSign ) );
		}
		return cubeMaterializer.createDimension( dim.getName( ),
				(IHierarchy) iHiers.get( 0 ) ) ;
	}

	/**
	 * 
	 * @param levels
	 * @param levelInHier
	 * @param leafLevelKeyColumn
	 */
	private void createLeafLevel( List levels, ILevelDefn[] levelInHier,
			String[] leafLevelKeyColumn )
	{
		if( levelInHier.length > levels.size() )
		{
			levelInHier[levelInHier.length-1] = CubeElementFactory.createLevelDefinition( "_${INTERNAL_INDEX}$_",
				leafLevelKeyColumn,
			    new String[0] );
		}
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	private String[] toStringArray( List object )
	{
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
		return this.prepare( query, null );
	}
	
	/*
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#prepare(org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition)
	 */
	public IPreparedCubeQuery prepare( ICubeQueryDefinition query,
			Map appContext ) throws BirtException
	{
		stopSign.start( );
		setModuleHandleToAppContext( appContext );

		if ( this.cubeHandleMap.get( query.getName( ) ) != null )
		{
			this.materializeCube( (CubeHandle) this.cubeHandleMap.get( query.getName( ) ),
					appContext );
			this.cubeHandleMap.remove( query.getName( ) );
		}

		return this.dataEngine.prepare( query, appContext );
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
				URI piTmp0 = null;
				piTmp0 = (URI)AccessController.doPrivileged( new PrivilegedAction<Object>()
				{
				  public Object run()
				  {
				    return new File(handle.getResourceFolder()).toURI();
				  }
				});
				
				identifiers.setApplResourceBaseURI( piTmp0 );
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
		stopSign.stop( );
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
		if ( module != null  )
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
			defineDataSourceDataSet( handle );
		}
	}
	
	private void defineDataSourceDataSet( DataSetHandle handle ) throws BirtException
	{

		if ( handle == null )
			return;
		
		DataSourceHandle dataSourceHandle = handle.getDataSource( );
		if ( dataSourceHandle != null
				&& ( (DataEngineImpl) dataEngine ).getDataSourceRuntime( dataSourceHandle.getName( ) ) == null )
		{
			IBaseDataSourceDesign dsourceDesign = this.modelAdaptor.adaptDataSource( dataSourceHandle );
			dataEngine.defineDataSource( dsourceDesign );
		}
		if ( handle instanceof JointDataSetHandle )
		{
			defineDataSourceDataSet( (JointDataSetHandle) handle );
		}

		if ( ( (DataEngineImpl) dataEngine ).getDataSetDesign( handle.getName( ) ) == null )
		{
			BaseDataSetDesign baseDS = this.modelAdaptor.adaptDataSet( handle );
			dataEngine.defineDataSet( baseDS );
		}
	}
	
	/**
	 * @param dataSet
	 * @param dataSetDesign
	 * @throws BirtException
	 */
	private void defineDataSourceDataSet( JointDataSetHandle jointDataSetHandle )
			throws BirtException
	{
		Iterator iter = ( (JointDataSetHandle) jointDataSetHandle ).dataSetsIterator();
		while (iter.hasNext( ))
		{
			DataSetHandle dsHandle = (DataSetHandle) iter.next( ); 
			if ( dsHandle != null )
			{
				defineDataSourceDataSet( dsHandle );
			}
		}
	}

	/**
	 * 
	 * @param query
	 * @param resultMetaList
	 * @param levelNameColumnNamePair
	 * @param hierHandle
	 * @throws AdapterException
	 */
	private static void prepareLevels( QueryDefinition query,
			TabularHierarchyHandle hierHandle, List metaList, String dimName )
			throws AdapterException
	{
		try
		{
			// Use same data set as cube fact table
			List levels = hierHandle.getContents( TabularHierarchyHandle.LEVELS_PROP );
	
			for ( int j = 0; j < levels.size( ); j++ )
			{
	
				TabularLevelHandle level = (TabularLevelHandle) levels.get( j );
	
				DataSetIterator.ColumnMeta temp = null;
	
				String exprString = ExpressionUtil.createJSDataSetRowExpression( level.getColumnName( ) );
	
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
									level.getIntervalRange( ) ) ),
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
									level.getIntervalRange( ) ) );
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
						exprString = "";
						if( level.getDefaultValue() != null )
						{
							exprString += "\"" + JavascriptEvalUtil.transformToJsConstants( level.getDefaultValue() ) + "\";" ;
						}
						for ( int i = 0; i < dispExpr.size( ); i++ )
						{
							String disp = "\""
									+ JavascriptEvalUtil.transformToJsConstants( String.valueOf( dispExpr.get( i ) ) )
									+ "\"";
							String filter = String.valueOf( filterExpr.get( i ) );
							exprString += "if(" + filter + ")" + disp + ";";
						}
						
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
						processor = new DataSetIterator.DateTimeAttributeProcessor( level.getDateTimeLevelType());
						bindingExpr = ExpressionUtil.createJSDataSetRowExpression( level.getColumnName() ) ;
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
					query.addBinding( new Binding( meta.getName( ),
							new ScriptExpression( level.getDisplayColumnName( ) ) ) );
				}
				
				String levelName = DataSetIterator.createLevelName( dimName, level.getName( ));
				query.addBinding( new Binding( levelName ,
						new ScriptExpression( exprString, type )));
	
				GroupDefinition gd = new GroupDefinition( String.valueOf( query.getGroups( ).size( )));
				gd.setKeyExpression( ExpressionUtil.createJSRowExpression( levelName ) );
	
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
	private static void prepareMeasure( TabularCubeHandle cubeHandle,
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
					String function = measure.getFunction( );
					String exprText = measure.getMeasureExpression( );
					IScriptExpression expr = exprText != null
							? new ScriptExpression( exprText ) : null;
					if ( query.getGroups( ).size( ) > 0 )
					{
						Binding binding = new Binding( measure.getName( ), expr );
						binding.setAggrFunction( DataAdapterUtil.adaptModelAggregationType( function ) );
						IGroupDefinition group = (IGroupDefinition) query.getGroups( )
								.get( query.getGroups( ).size( ) - 1 );
						binding.addAggregateOn( group.getName( ) );
	
						query.addBinding( binding );
					}
					else
					{
						query.addBinding( new Binding( measure.getName( ), expr ) );
					}
	
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
	 */
	private static void popualteFilter( DataRequestSession session,
			Iterator filterIterator, QueryDefinition query )
	{
		while( filterIterator.hasNext( ) )
		{
			FilterConditionHandle filter = (FilterConditionHandle) filterIterator.next( );
			query.addFilter( session.getModelAdaptor( ).adaptFilter( filter ) );
		}
	}

	/**
	 * 
	 * @param session
	 * @param cubeHandle
	 * @param metaList
	 * @return
	 * @throws AdapterException
	 * @throws DataException
	 */
	static QueryDefinition createQuery(
			DataRequestSessionImpl session, TabularCubeHandle cubeHandle,
			List metaList ) throws AdapterException, DataException
	{
		if( metaList == null )
			metaList = new ArrayList();
		QueryDefinition query = new QueryDefinition( );
	
		query.setUsesDetails( false );
		query.setDataSetName( cubeHandle.getDataSet( ).getQualifiedName( ) );
	
		List dimensions = cubeHandle.getContents( CubeHandle.DIMENSIONS_PROP );
	
		
		if ( dimensions != null )
		{
			for ( int i = 0; i < dimensions.size( ); i++ )
			{
				TabularDimensionHandle dimension = (TabularDimensionHandle) dimensions.get( i );
				List hiers = dimension.getContents( DimensionHandle.HIERARCHIES_PROP );
	
				//By now we only support one hierarchy per dimension.
				assert hiers.size( ) == 1;
	
				TabularHierarchyHandle hierHandle = (TabularHierarchyHandle) hiers.get( 0 );
	
				if ( hierHandle.getDataSet( ) == null
						|| hierHandle.getDataSet( )
								.getQualifiedName( )
								.equals( cubeHandle.getDataSet( ).getQualifiedName( ) ) )
				{
					DataRequestSessionImpl.prepareLevels( query,
							hierHandle,
							metaList,
							dimension.getName());
				}
				else
				{
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
								metaList.add( new DataSetIterator.ColumnMeta( cubeKeyWithDimIdentifier,
										null,
										DataSetIterator.ColumnMeta.LEVEL_KEY_TYPE ) );
								query.addBinding( new Binding( cubeKeyWithDimIdentifier,
										new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( cubeKey ) ) ) );
	
								GroupDefinition gd = new GroupDefinition( String.valueOf( query.getGroups( )
										.size( ) ) );
								gd.setKeyExpression( ExpressionUtil.createJSRowExpression( cubeKeyWithDimIdentifier ) );
								query.addGroup( gd );
							}
						}
					}
				}
			}
		}
	
		DataRequestSessionImpl.prepareMeasure( cubeHandle, query, metaList );
		DataRequestSessionImpl.popualteFilter( session, cubeHandle.filtersIterator( ), query );
		return query;
	}

	/**
	 * 
	 * @param handle
	 * @return
	 */
	private static String getDataSet( TabularHierarchyHandle handle )
	{
		if ( handle.getDataSet( )!= null )
			return handle.getDataSet( ).getQualifiedName( );
		else
		{
			TabularCubeHandle cubeHandle = acquireContainerCube( handle );
			if( cubeHandle!= null )
				return cubeHandle.getDataSet( ).getQualifiedName( ); 
		}
		return null;
	}

	/**
	 * 
	 * @param hierHandle
	 * @return
	 */
	private static TabularCubeHandle acquireContainerCube( TabularHierarchyHandle hierHandle )
	{
		DesignElementHandle handle = hierHandle.getContainer( ).getContainer( );
		if( handle == null || !(handle instanceof TabularCubeHandle))
			return null;
		return (TabularCubeHandle)handle;
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
			TabularCubeHandle cubeHandle = DataRequestSessionImpl.acquireContainerCube( handle );
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
	 * @throws AdapterException
	 */
	static QueryDefinition createQuery(
			DataRequestSessionImpl session, TabularHierarchyHandle hierHandle,
			List metaList ) throws AdapterException
	{
		assert metaList!= null;
		QueryDefinition query = new QueryDefinition( );
		query.setUsesDetails( false );
		
		query.setDataSetName( DataRequestSessionImpl.getDataSet ( hierHandle ) );
	
		
		DataRequestSessionImpl.prepareLevels( query,
				hierHandle, metaList, null );
		
		DataRequestSessionImpl.popualteFilter( session, DataRequestSessionImpl.getFilterIterator( hierHandle ), query );
		return query;
	}

	@Override
	public DataSessionContext getDataSessionContext( )
	{
		return this.sessionContext;
	}

}
