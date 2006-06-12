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

package org.eclipse.birt.data.engine.impl;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.IParameterDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;
import org.eclipse.birt.data.engine.executor.JointDataSetQuery;
import org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.expression.ExpressionProcessor;
import org.eclipse.birt.data.engine.expression.FilterExpressionParser;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateCalculator;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateTable;
import org.eclipse.birt.data.engine.odaconsumer.ParameterHint;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.OnFetchScriptHelper;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */
public abstract class QueryExecutor implements IQueryExecutor
{

	private IBaseQueryDefinition baseQueryDefn;
	private AggregateTable aggrTable;
	private AggregateCalculator aggregates;

	// from PreparedQuery->PreparedDataSourceQuery->DataEngineImpl
	private Scriptable sharedScope;
	/** Externally provided query scope; can be null */
	// from PreparedQuery->PreparedDataSourceQuery
	private Scriptable parentScope;

	// for query execution
	private Scriptable queryScope;

	private boolean isPrepared = false;
	private boolean isExecuted = false;
	private Map queryAppContext;

	/** Query nesting level, 1 - outermost query */
	private int nestedLevel = 1;

	/** Runtime data source and data set used by this instance of executor */
	protected DataSourceRuntime dataSource;
	protected DataSetRuntime dataSet;

	protected IDataSource odiDataSource;
	protected IQuery odiQuery;

	/** Outer query's results; null if this query is not nested */
	protected IQueryService outerResults;
	private IResultIterator odiResult;
	private IExecutorHelper parentHelper;

	private List temporaryComputedColumns = new ArrayList( );
	private static Logger logger = Logger.getLogger( DataEngineImpl.class.getName( ) );

	/**
	 * @param sharedScope
	 * @param baseQueryDefn
	 * @param aggrTable
	 */
	QueryExecutor( Scriptable sharedScope, IBaseQueryDefinition baseQueryDefn,
			AggregateTable aggrTable )
	{
		this.sharedScope = sharedScope;
		this.baseQueryDefn = baseQueryDefn;
		this.aggrTable = aggrTable;
	}

	/**
	 * Provide the actual DataSourceRuntime used for the query.
	 * 
	 * @return
	 */
	abstract protected DataSourceRuntime findDataSource( ) throws DataException;

	/**
	 * Create a new instance of data set runtime
	 * 
	 * @return
	 */
	abstract protected DataSetRuntime newDataSetRuntime( ) throws DataException;

	/**
	 * Create a new unopened odiDataSource given the data source runtime
	 * definition
	 * 
	 * @return
	 */
	abstract protected IDataSource createOdiDataSource( ) throws DataException;

	/**
	 * Create an empty instance of odi query
	 * 
	 * @return
	 */
	abstract protected IQuery createOdiQuery( ) throws DataException;

	/**
	 * Prepares the ODI query
	 */
	protected void prepareOdiQuery( ) throws DataException
	{
	}

	/**
	 * Executes the ODI query to reproduce a ODI result set
	 * @param eventHandler 
	 * 
	 * @return
	 */
	abstract protected IResultIterator executeOdiQuery(
			IEventHandler eventHandler ) throws DataException;

	/**
	 * @param context
	 */
	void setAppContext( Map context )
	{
		queryAppContext = context;
	}

	/**
	 * Prepare Executor so that it is ready to execute the query
	 * 
	 * @param outerRts
	 * @param targetScope
	 * @throws DataException
	 */
	void prepareExecution( IQueryResults outerRts, Scriptable targetScope )
			throws DataException
	{
		if ( isPrepared )
			return;

		this.parentScope = targetScope;
		dataSource = findDataSource( );

		if ( outerRts != null )
		{
			outerResults = ( (IQueryService) outerRts );
			if ( outerResults.isClosed( ) )
			{
				// Outer result is closed; invalid
				throw new DataException( ResourceConstants.RESULT_CLOSED );
			}
			this.nestedLevel = outerResults.getNestedLevel( );
			this.setParentExecutorHelper( outerResults.getExecutorHelper( ) );
		}

		// Create the data set runtime
		// Since data set runtime contains the execution result, a new data set
		// runtime is needed for each execute
		dataSet = newDataSetRuntime( );
		assert dataSet != null;

		openDataSource( );

		// Run beforeOpen script now so the script can modify the DataSetRuntime properties
		dataSet.beforeOpen( );

		IExpressionProcessor exprProcessor = new ExpressionProcessor( null,
				null,
				dataSet,
				null );

		// Let subclass create a new and empty intance of the appropriate
		// odi IQuery
		odiQuery = createOdiQuery( );
		odiQuery.setDistinctValueFlag( dataSet.needDistinctValue( ) );
		odiQuery.setExprProcessor( exprProcessor );
		populateOdiQuery( );
		prepareOdiQuery( );
		isPrepared = true;
	}

	/**
	 * Open the required DataSource. This method should be called after
	 * "dataSource" is initialized by findDataSource() method.
	 * 
	 * @throws DataException
	 */
	protected void openDataSource( ) throws DataException
	{
		assert odiDataSource == null;

		// Open the underlying data source
		// dataSource = findDataSource( );
		if ( dataSource != null )
		{
			// TODO: potential bug
			if ( !dataSource.isOpen( )
					|| DataSetCacheManager.getInstance( ).doesLoadFromCache( ) == true )
			{
				// Data source is not open; create an Odi Data Source and open it
				// We should run the beforeOpen script now to give it a chance to modify
				// runtime data source properties
				dataSource.beforeOpen( );

				// Let subclass create a new unopened odi data source
				odiDataSource = createOdiDataSource( );

				// Passes thru the prepared query executor's 
				// context to the new odi data source
				odiDataSource.setAppContext( queryAppContext );

				// Open the odi data source
				dataSource.openOdiDataSource( odiDataSource );

				dataSource.afterOpen( );
			}
			else
			{
				// Use existing odiDataSource created for the data source runtime
				odiDataSource = dataSource.getOdiDataSource( );

				// Passes thru the prepared query executor's 
				// current context to existing data source
				odiDataSource.setAppContext( queryAppContext );
			}
		}
	}

	/**
	 * Populates odiQuery with this query's definitions
	 * 
	 * @throws DataException
	 */
	protected void populateOdiQuery( ) throws DataException
	{
		assert odiQuery != null;
		assert this.baseQueryDefn != null;

		Context cx = Context.enter( );
		try
		{
			// Set grouping
			populateGrouping( cx );
			// Set sorting
			List sorts = this.baseQueryDefn.getSorts( );
			if ( sorts != null && !sorts.isEmpty( ) )
			{
				IQuery.SortSpec[] sortSpecs = new IQuery.SortSpec[sorts.size( )];
				Iterator it = sorts.iterator( );
				for ( int i = 0; it.hasNext( ); i++ )
				{
					ISortDefinition src = (ISortDefinition) it.next( );
					int sortIndex = -1;
					String sortKey = src.getColumn( );
					if ( sortKey == null )
						sortKey = src.getExpression( ).getText( );
					else
					{
						sortKey = getColumnRefExpression( sortKey );
					}

					temporaryComputedColumns.add( new ComputedColumn( "_{$TEMP_SORT_"
							+ i + "$}_",
							sortKey,
							DataType.ANY_TYPE ) );
					sortIndex = -1;
					sortKey = String.valueOf( "_{$TEMP_SORT_" + i + "$}_" );

					IQuery.SortSpec dest = new IQuery.SortSpec( sortIndex,
							sortKey,
							src.getSortDirection( ) == ISortDefinition.SORT_ASC );
					sortSpecs[i] = dest;
				}
				odiQuery.setOrdering( Arrays.asList( sortSpecs ) );
			}

			// set filter event
			List dataSetFilters = new ArrayList( );
			List queryFilters = new ArrayList( );
			if ( dataSet.getFilters( ) != null )
			{
				dataSetFilters = dataSet.getFilters( );
			}

			if ( this.baseQueryDefn.getFilters( ) != null )
			{
				for ( int i = 0; i < this.baseQueryDefn.getFilters( ).size( ); i++ )
				{
					queryFilters.add( this.baseQueryDefn.getFilters( ).get( i ) );
				}
			}

			//When prepare filters, the temporaryComputedColumns would also be effect.
			List multipassFilters = prepareFilters( cx,
					dataSetFilters,
					queryFilters,
					temporaryComputedColumns );

			//******************populate the onFetchEvent below**********************/		    
			List computedColumns = null;
			// set computed column event
			computedColumns = this.dataSet.getComputedColumns( );
			if ( computedColumns == null )
				computedColumns = new ArrayList( );
			if ( computedColumns.size( ) > 0
					|| temporaryComputedColumns.size( ) > 0 )
			{
				IResultObjectEvent objectEvent = new ComputedColumnHelper( this.dataSet,
						computedColumns,
						temporaryComputedColumns );
				odiQuery.addOnFetchEvent( objectEvent );
				this.dataSet.getComputedColumns( )
						.addAll( temporaryComputedColumns );
			}
			if ( dataSet.getEventHandler( ) != null )
			{
				OnFetchScriptHelper event = new OnFetchScriptHelper( dataSet );
				odiQuery.addOnFetchEvent( event );
			}

			if ( dataSetFilters.size( )
					+ queryFilters.size( ) + multipassFilters.size( ) > 0 )
			{
				IResultObjectEvent objectEvent = new FilterByRow( dataSetFilters,
						queryFilters,
						multipassFilters,
						dataSet );
				odiQuery.addOnFetchEvent( objectEvent );
			}

			// specify max rows the query should fetch
			odiQuery.setMaxRows( this.baseQueryDefn.getMaxRows( ) );
			
			prepareCacheQuery( );
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * TODO: enhance me, this is only a temp logic
	 * Set temporary computed columns to DataSourceQuery where cache is used
	 */
	private void prepareCacheQuery( )
	{
		if ( odiQuery instanceof org.eclipse.birt.data.engine.executor.dscache.DataSourceQuery
				&& temporaryComputedColumns != null
				&& temporaryComputedColumns.size( ) > 0 )
		{
			( (org.eclipse.birt.data.engine.executor.dscache.DataSourceQuery) odiQuery ).setTempComputedColumn( this.temporaryComputedColumns );
		}
	}
	
	/**
	 * 
	 * @param cx
	 * @throws DataException
	 */
	private void populateGrouping( Context cx ) throws DataException
	{
		List groups = this.baseQueryDefn.getGroups( );
		if ( groups != null && !groups.isEmpty( ) )
		{
			IQuery.GroupSpec[] groupSpecs = new IQuery.GroupSpec[groups.size( )];
			Iterator it = groups.iterator( );
			for ( int i = 0; it.hasNext( ); i++ )
			{
				IGroupDefinition src = (IGroupDefinition) it.next( );

				if ( ( src.getKeyColumn( ) == null || src.getKeyColumn( )
						.trim( )
						.length( ) == 0 )
						&& ( src.getKeyExpression( ) == null || src.getKeyExpression( )
								.trim( )
								.length( ) == 0 ) )
					throw new DataException( ResourceConstants.BAD_GROUP_EXPRESSION );
					
				String expr = getGroupKeyExpression( src );
				String groupName = "";
				if ( expr.trim( ).equalsIgnoreCase( "row[0]" )
						|| expr.trim( )
								.equalsIgnoreCase( "row._rowPosition" )
						|| expr.trim( ).equalsIgnoreCase( "dataSetRow[0]" )
						|| expr.trim( )
								.equalsIgnoreCase( "dataSetRow._rowPosition" ) )
				{
					groupName = "_{$TEMP_GROUP_" + i + "ROWID$}_";
				}
				else
				{
					groupName = "_{$TEMP_GROUP_" + i + "$}_";
				}
				
				if ( ( src.getInterval( ) != IGroupDefinition.NO_INTERVAL )
						&& ( src.getIntervalRange( ) != 0 ) )
				{
					try
					{						
						expr = ExpressionUtil.createGroupByExpression( src.getInterval( ),
								src.getIntervalStart( ),
								getGroupKeyExpression( src ),
								src.getIntervalRange( ),
								getColumnDataType(cx, expr));
						
					}
					catch(BirtException be)
					{
						throw DataException.wrap( be );
					}
				}
				IQuery.GroupSpec dest = QueryExecutorUtil.groupDefnToSpec( cx,
						src,
						expr,
						groupName,
						-1 );
				groupSpecs[i] = dest;

				if ( ( dest.getInterval( ) != IGroupDefinition.NO_INTERVAL )
						&& ( dest.getIntervalRange( ) != 0 ) )
				{
					if (dest.getInterval( ) == IGroupDefinition.STRING_PREFIX_INTERVAL)
					{
						temporaryComputedColumns.add( new ComputedColumn( groupName,
								expr,
								DataType.STRING_TYPE ) );
					}
					else
					{
						temporaryComputedColumns.add( new ComputedColumn( groupName,
								expr,
								DataType.INTEGER_TYPE ) );
					}
					
				}
				else
				{
					temporaryComputedColumns.add( new ComputedColumn( groupName,
							expr,
							QueryExecutorUtil.getTempComputedColumnType( groupSpecs[i].getInterval( ) ) ) );
				}

			}
			odiQuery.setGrouping( Arrays.asList( groupSpecs ) );
		}
	}

	/**
	 * get the data type of a expression
	 * @param cx
	 * @param expr
	 * @return
	 */
	private int getColumnDataType( Context cx, String expr )
	{
		String columnName = QueryExecutorUtil.getColInfoFromJSExpr( cx, expr )
				.getColumnName( );
		if ( columnName == null )
		{
			return DataType.UNKNOWN_TYPE;
		}
		if ( columnName.equals( "__rownum" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		Object baseExpr = ( this.baseQueryDefn.getResultSetExpressions( ).get( columnName ) );

		if ( baseExpr == null )
		{
			return DataType.UNKNOWN_TYPE;
		}

		return ( (IBaseExpression) baseExpr ).getDataType( );
	}
	
	/**
	 * @param src
	 * @return
	 */
	private String getGroupKeyExpression( IGroupDefinition src )
	{
		String expr = src.getKeyColumn( );
		if ( expr == null )
		{
			expr = src.getKeyExpression( );
		}
		else
		{
			expr = getColumnRefExpression( expr );
		}
		return expr;
	}

	/**
	 * 
	 * @param expr
	 * @return
	 */
	private String getColumnRefExpression( String expr )
	{
		return "row[\""
				+ JavascriptEvalUtil.transformToJsConstants( expr ) + "\"]";
	}

	void setParentExecutorHelper( IExecutorHelper helper )
	{
		this.parentHelper = helper;
	}

	/**
	 * 
	 * @param cx
	 * @param dataSetFilters
	 * @param queryFilters
	 * @param temporaryComputedColumns
	 * @return
	 */
	private List prepareFilters( Context cx, List dataSetFilters,
			List queryFilters, List temporaryComputedColumns )
	{
		List result = new ArrayList( );
		prepareFilter( cx, dataSetFilters, temporaryComputedColumns, result );
		prepareFilter( cx, queryFilters, temporaryComputedColumns, result );
		return result;
	}

	/**
	 * 
	 * @param cx
	 * @param dataSetFilters
	 * @param temporaryComputedColumns
	 * @param result
	 */
	private void prepareFilter( Context cx, List dataSetFilters,
			List temporaryComputedColumns, List result )
	{
		if ( dataSetFilters != null && !dataSetFilters.isEmpty( ) )
		{
			Iterator it = dataSetFilters.iterator( );
			for ( int i = 0; it.hasNext( ); i++ )
			{
				IFilterDefinition src = (IFilterDefinition) it.next( );
				IBaseExpression expr = src.getExpression( );

				if ( isGroupFilter( src ) )
				{
					ConditionalExpression ce = ( (ConditionalExpression) expr );
					String exprText = ce.getExpression( ).getText( );
					ColumnInfo columnInfo = QueryExecutorUtil.getColInfoFromJSExpr( cx,
							exprText );

					int index = columnInfo.getColumnIndex( );
					String name = columnInfo.getColumnName( );

					if ( name == null && index < 0 )
					{
						// If failed to treate filter key as a column reference
						// expression
						// then treat it as a computed column expression
						temporaryComputedColumns.add( new ComputedColumn( "_{$TEMP_FILTER_"
								+ i + "$}_",
								exprText,
								DataType.ANY_TYPE ) );
						it.remove( );
						result.add( new FilterDefinition( new ConditionalExpression( new ScriptExpression( String.valueOf( "dataSetRow[\"_{$TEMP_FILTER_"
								+ i + "$}_\"]" ) ),
								ce.getOperator( ),
								ce.getOperand1( ),
								ce.getOperand2( ) ) ) );
					}
				}

			}
		}
	}

	/**
	 * 
	 * @param filter
	 * @return
	 */
	private boolean isGroupFilter( IFilterDefinition filter )
	{
		IBaseExpression expr = filter.getExpression( );

		if ( expr instanceof IConditionalExpression )
		{
			String expr4Exception = ( (ConditionalExpression) expr ).getExpression( )
					.getText( );
			try
			{
				new FilterExpressionParser( null, null ).compileFilterExpression( expr4Exception );
			}
			catch ( DataException e )
			{
				return true;
			}
		}
		return false;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryExecutor#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData( ) throws DataException
	{
		assert odiQuery instanceof IPreparedDSQuery
				|| odiQuery instanceof ICandidateQuery
				|| odiQuery instanceof JointDataSetQuery;

		if ( odiQuery instanceof IPreparedDSQuery )
		{
			if ( ( (IPreparedDSQuery) odiQuery ).getResultClass( ) != null )
				return new ResultMetaData( ( (IPreparedDSQuery) odiQuery ).getResultClass( ) );
			else
				return null;
		}
		else if ( odiQuery instanceof JointDataSetQuery )
		{
			return new ResultMetaData( ( (JointDataSetQuery) odiQuery ).getResultClass( ) );
		}
		else
		{
			return new ResultMetaData( ( (ICandidateQuery) odiQuery ).getResultClass( ) );
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryExecutor#execute()
	 */
	public void execute( IEventHandler eventHandler ) throws DataException
	{
		logger.logp( Level.FINER,
				QueryExecutor.class.getName( ),
				"execute",
				"Start to execute" );

		if ( this.isExecuted )
			return;

		ExecutorHelper helper = new ExecutorHelper( this.getQueryScope() );
		helper.setParent( this.parentHelper );
		eventHandler.setExecutorHelper( helper );

		// Execute the query
		odiResult = executeOdiQuery( eventHandler );
		//helper.setResultIterator( odiResult );

		if (odiQuery instanceof BaseQuery) 
		{
			IExpressionProcessor ep = ((BaseQuery) odiQuery).getExprProcessor();

			Map results = baseQueryDefn.getResultSetExpressions();
			Object[] o = results.values().toArray();
			Object[] names = results.keySet().toArray();
			DummyICCState iccState = new DummyICCState(o, names);

			ep.setResultIterator(odiResult);
			while (!iccState.isFinish()) {
				ep.compileExpression(iccState);
				ep.calculate();
			}

		}
		helper.setJSRowObject( this.dataSet.getJSResultRowObject( ) );

		resetComputedColumns( );
		// Bind the row object to the odi result set
		this.dataSet.setResultSet( odiResult, false );

		// Calculate aggregate values
		aggregates = new AggregateCalculator( this.aggrTable, odiResult );

		// Calculate aggregate values
		aggregates.calculate( getQueryScope( ) );

		this.isExecuted = true;

		logger.logp( Level.FINER,
				QueryExecutor.class.getName( ),
				"execute",
				"Finish executing" );
	}

	/**
	 * reset computed columns
	 */
	private void resetComputedColumns( )
	{
		List l = this.getDataSet( ).getComputedColumns( );
		if ( l != null )
			l.removeAll( this.temporaryComputedColumns );
	}

	/*
	 * Closes the executor; release all odi resources
	 * 
	 * @see org.eclipse.birt.data.engine.impl.IQueryExecutor#close()
	 */
	public void close( )
	{
		if ( odiQuery == null )
		{
			// already closed
			logger.logp( Level.FINER,
					QueryExecutor.class.getName( ),
					"close",
					"executor closed " );
			return;
		}

		// Close the data set and associated odi query
		try
		{
			dataSet.beforeClose( );
		}
		catch ( DataException e )
		{
			logger.logp( Level.FINE,
					QueryExecutor.class.getName( ),
					"close",
					e.getMessage( ),
					e );
		}

		if ( odiResult != null )
			odiResult.close( );
		odiQuery.close( );

		try
		{
			dataSet.close( );
		}
		catch ( DataException e )
		{
			logger.logp( Level.FINE,
					QueryExecutor.class.getName( ),
					"close",
					e.getMessage( ),
					e );
		}

		odiQuery = null;
		odiDataSource = null;
		aggregates = null;
		odiResult = null;
		queryScope = null;
		isPrepared = false;
		isExecuted = false;

		// Note: reset dataSet and dataSource only after afterClose() is executed, since
		// the script may access these two objects
		try
		{
			dataSet.afterClose( );
		}
		catch ( DataException e )
		{
			logger.logp( Level.FINE,
					QueryExecutor.class.getName( ),
					"close",
					e.getMessage( ),
					e );
		}
		dataSet = null;
		dataSource = null;

		logger.logp( Level.FINER,
				QueryExecutor.class.getName( ),
				"close",
				"executor closed " );
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryExecutor#getDataSet()
	 */
	public DataSetRuntime getDataSet( )
	{
		return dataSet;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryExecutor#getSharedScope()
	 */
	public Scriptable getSharedScope( )
	{
		return this.sharedScope;
	}

	/**
	 * Gets the Javascript scope for evaluating expressions for this query
	 * 
	 * @return
	 */
	public Scriptable getQueryScope( )
	{
		if ( queryScope == null )
		{
			// Set up a query scope. All expressions are evaluated against the 
			// Data set JS object as the prototype (so that it has access to all
			// data set properties). It uses a subscope of the externally provided
			// parent scope, or the global shared scope
			queryScope = newSubScope( parentScope );
			queryScope.setPrototype( dataSet.getJSDataSetObject( ) );
		}
		return queryScope;
	}

	/**
	 * Creates a subscope within parent scope
	 * @param parentAndProtoScope parent scope. If null, the shared top-level scope is used as parent
	 */
	private Scriptable newSubScope( Scriptable parentAndProtoScope )
	{
		if ( parentAndProtoScope == null )
			parentAndProtoScope = sharedScope;

		Context cx = Context.enter( );
		try
		{
			Scriptable scope = cx.newObject( parentAndProtoScope );
			scope.setParentScope( parentAndProtoScope );
			scope.setPrototype( parentAndProtoScope );
			return scope;
		}
		finally
		{
			Context.exit( );
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryExecutor#getNestedLevel()
	 */
	public int getNestedLevel( )
	{
		return this.nestedLevel;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryExecutor#getDataSourceInstanceHandle()
	 */
	public IDataSourceInstanceHandle getDataSourceInstanceHandle( )
	{
		return this.dataSource;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryExecutor#getJSAggrValueObject()
	 */
	public Scriptable getJSAggrValueObject( )
	{
		if ( aggregates != null )
			return aggregates.getJSAggrValueObject( );
		else
			return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryExecutor#getNestedDataSets(int)
	 */
	public DataSetRuntime[] getNestedDataSets( int nestedCount )
	{
		return outerResults.getDataSetRuntime( nestedCount );
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryExecutor#getOdiResultSet()
	 */
	public IResultIterator getOdiResultSet( )
	{
		return this.odiResult;
	}

	/**
	 * Resolve parameter bindings and return a Collection of ParameterHints, which merged information obtained
	 * from query parameter binding and the data set parameter definition
	 * 
	 */
	protected Collection resolveDataSetParameters( boolean evaluateValue ) throws DataException
	{
		// This method can only be called by Oda and Script query executors
		assert this instanceof PreparedOdaDSQuery.OdaDSQueryExecutor ||
		       this instanceof PreparedScriptDSQuery.ScriptDSQueryExecutor;
		
		List paramDefns = getDataSet().getParameters();
		int nParams = paramDefns == null? 0 : paramDefns.size();
		
		// array of parameter hints
		ParameterHint[] paramHints = new ParameterHint[nParams];
		// whether corresponding item in paramHints has been bound
		boolean[] bindingResolved = new boolean[nParams];
		
		// First create param hints for all data set params 
		for ( int i = 0; i < nParams; i++ )
		{
			IParameterDefinition paramDefn = (IParameterDefinition) paramDefns.get(i);
			paramHints[i] = createParameterHint( 
					paramDefn , paramDefn.getDefaultInputValue() );
			bindingResolved[i] = false;
			
			// Can the data set RT provide an input parameter value? (this has the highest
			// priority, over bindings) 
			if ( paramDefn.isInputMode() && paramDefn.getName() != null )
			{
				Object paramValue = DataSetRuntime.UNSET_VALUE;
				try
				{
					paramValue =  getDataSet().getInputParameterValue( paramDefn.getName() );
				}
				catch (BirtException e)
				{
					// This is unexpected; the parameter must be in the list
					assert false;
					throw DataException.wrap(e);
				}
				
				if ( paramValue != DataSetRuntime.UNSET_VALUE )
				{
					String paramValueStr = this.getParameterValueString(
								paramHints[i].getDataType(), paramValue);
					paramHints[i].setDefaultInputValue( paramValueStr );
					bindingResolved[i] = true;
				}
			}
		}

		Context cx = null;
		if ( evaluateValue )
			cx = Context.enter( );
		try
		{
			// Resolve parameter bindings

			// Parameter values are determined in the following order of priority
			// (1) Input param values set by scripts (already resolved above)
			// (2) Query parameter bindings
			// (3) Data set parameter bindings
			
			resolveParameterBindings(
				( (IQueryDefinition) this.baseQueryDefn ).getInputParamBindings( ),
				paramHints, bindingResolved, cx );
			       
			resolveParameterBindings(
					getDataSet().getInputParamBindings(),
					paramHints, bindingResolved, cx );
		}
		finally
		{
			if ( cx != null )
				Context.exit( );
		}
		
		return Arrays.asList( paramHints );
	}

	/**
	 * Resolve a list of parameter bindings and update the hints
	 * @param cx JS context to evaluate binding. If null, binding does not need to be evaluated
	 */
	private void resolveParameterBindings( Collection bindings, 
			ParameterHint[] paramHints, boolean[] bindingResolved, Context cx )
		throws DataException
	{
		if ( bindings == null )
			return;
		
		Iterator it = bindings.iterator();
		while ( it.hasNext() )
		{
			resolveParameterBinding( (IInputParameterBinding) it.next(),
					paramHints, bindingResolved, cx );
		}
	}
		
	/**
	 * Resolve a parameter binding and update the hints
	 * @param cx JS context to evaluate binding. If null, binding does not need to be evaluated
	 */
	private void resolveParameterBinding( IInputParameterBinding binding, 
			ParameterHint[] paramHints, boolean[] bindingResolved, Context cx )
		throws DataException
	{
		// Find the hint which matches the binding
		int i = findParameterHint( paramHints, binding.getPosition(), binding.getName());
		
		if ( i < 0 )
		{
			// A binding exists but the data set has no definition for the
			// bound parameter, log an error and ignore the param
			logger.warning( "Ignored binding defined for non-exising data set parameter: " +
						"name=" + binding.getName() + ", position=" + binding.getPosition());
		}
		
		// Do not set binding value if the parameter has already been resolved
		// (e.g., query binding has already been evaluated for this param, and
		// we are now checking data set binding for the same param )
		else if ( ! bindingResolved[i] )
		{
			Object value = 
				(cx != null) ? evaluateInputParameterValue( getQueryScope(), cx, binding )
						: binding.getExpr();
			String valueStr = getParameterValueString( paramHints[i].getDataType(), value);
			paramHints[i].setDefaultInputValue( valueStr );
			bindingResolved[i] = true;
			
			// Also give the value to data set RT for script access
			if ( cx != null && paramHints[i].isInputMode() &&
					paramHints[i].getName() != null )
			{
				try
				{
					getDataSet().setInputParameterValue( paramHints[i].getName(),
							value);
				}
				catch (BirtException e)
				{
					// Unexpected 
					assert false;
					throw DataException.wrap(e);
				}
			}
		}
	}
	
	/**
	 * Find index of matching parameter hint in paramHints array, based on
	 * param name or position.
	 * Returns index of param hint found in array, or -1 if no match
	 */
	private int findParameterHint( ParameterHint[] hints, int position, String name )
	{
		for (int i = 0; i < hints.length; i++)
		{
            ParameterHint paramHint = hints[i];
            if( position <= 0)
            {
                if( paramHint.getName().equalsIgnoreCase(name) )
                    return i;
            }
            else
            {
                if( paramHint.getPosition() == position )
                    return i;
            }
        }
        return -1;
	}
	
	/**
	 * @param scope
	 * @param cx
	 * @param iParamBind
	 * @return
	 * @throws DataException
	 */
	private Object evaluateInputParameterValue( Scriptable scope, Context cx, IInputParameterBinding iParamBind ) throws DataException
	{
		// Evaluate Expression:
		// If the expression has been prepared, 
		// use its handle to getValue() from outerResultIterator
		// else use Rhino to evaluate in corresponding scope
		Object evaluateResult = null;
		Scriptable evaluateScope = scope;
		
		if ( outerResults != null )
		{
			try
			{
				evaluateScope = outerResults.getQueryScope( );
				evaluateResult = outerResults.getExecutorHelper( )
						.evaluate( iParamBind.getExpr( ) );
			}
			catch (BirtException e)
			{
				//do not expect a exception here.
				DataException dataEx= new DataException( ResourceConstants.UNEXPECTED_ERROR, e );
				logger.logp( Level.FINE,
						PreparedOdaDSQuery.class.getName( ),
						"getMergedParameters",
						"Error occurs in IQueryResults.getResultIterator()",
						e );
				throw dataEx;
			}
		}
		
		if (evaluateResult == null)
			evaluateResult = ScriptEvalUtil.evalExpr( iParamBind.getExpr( ),
					cx,
					evaluateScope,
					"ParamBinding(" + iParamBind.getName( ) + ")",
					0 );
					
		if ( evaluateResult == null)
		    throw new DataException(ResourceConstants.DEFAULT_INPUT_PARAMETER_VALUE_CANNOT_BE_NULL);
		return evaluateResult;
	}
	
	/**
	 * Converts a parameter value to a String expected by ParameterHint
	 */
	private String getParameterValueString( Class paramType, Object paramValue )
		throws DataException
	{
		if ( paramValue instanceof String )
			return (String) paramValue;
		
	    // Type conversion note: An integer constant like "1" will be
	    // interpreted as a floating number by Rhino, which will then be converted
	    // to "1.0", which some drivers don't like.
	    // So we will first convert the value to the type required by the input
	    // parameter, then convert it to String. This guarantees that an input
	    // value "1" will be pass on as "1", and not "1.0"
		try
		{
			paramValue = DataTypeUtil.convert( paramValue, paramType );
			return DataTypeUtil.toString( paramValue );
		}
		catch ( BirtException e )
		{
			throw new DataException( ResourceConstants.DATATYPEUTIL_ERROR,
					e );
		}
	}
	/**
	 * Create a parameter hint based on Parameter definition and value
	 * @param paramDefn
	 * @param evaValue
	 */
	private ParameterHint createParameterHint( IParameterDefinition paramDefn,
			Object paramValue ) throws DataException
	{
	    assert paramValue != null;
        
		ParameterHint parameterHint = new ParameterHint( paramDefn.getName(), 
				paramDefn.isInputMode(),
				paramDefn.isOutputMode() );
		if( paramDefn.getPosition() > 0)
			parameterHint.setPosition( paramDefn.getPosition( ) );

		// following data types is not supported by odaconsumer currently
		Class dataTypeClass = DataType.getClass( paramDefn.getType( ) );
		if ( dataTypeClass == DataType.AnyType.class
				|| dataTypeClass == Boolean.class || dataTypeClass == Blob.class )
		{
			dataTypeClass = String.class;
		}
		parameterHint.setDataType( dataTypeClass );
		parameterHint.setIsInputOptional( paramDefn.isInputOptional( ) );
		parameterHint.setDefaultInputValue( 
				getParameterValueString( dataTypeClass, paramValue) );
		parameterHint.setIsNullable( paramDefn.isNullable() );
		return parameterHint;
	}
}

/**
 * Class DummyICCState is used by ExpressionProcessor to calculate multipass 
 * aggregations.
 *
 */
class DummyICCState implements IComputedColumnsState
{
	Object[] exprs;
	Object[] names;
	boolean[] isValueAvailable;
	
	/**
	 * 
	 * @param exprs
	 * @param names
	 */
	DummyICCState( Object[] exprs, Object[] names )
	{
		this.exprs = exprs;
		this.names = names;
		this.isValueAvailable= new boolean[exprs.length];
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#isValueAvailable(int)
	 */
	public boolean isValueAvailable( int index )
	{
		return this.isValueAvailable[index];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#getName(int)
	 */
	public String getName( int index )
	{
		return this.names[index].toString( );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#getExpression(int)
	 */
	public IBaseExpression getExpression( int index )
	{
		return (IBaseExpression) exprs[index];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#setValueAvailable(int)
	 */
	public void setValueAvailable( int index )
	{
		this.isValueAvailable[index] = true;		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#getCount()
	 */
	public int getCount( )
	{
		return this.isValueAvailable.length;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#getComputedColumn(int)
	 */
	public IComputedColumn getComputedColumn( int index )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#setModel(int)
	 */
	public void setModel( int model )
	{
			
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFinish()
	{
		for( int i = 0; i < isValueAvailable.length; i++ )
		{
			if( !isValueAvailable[i] )
				return false;
		}
		return true;
	}
}