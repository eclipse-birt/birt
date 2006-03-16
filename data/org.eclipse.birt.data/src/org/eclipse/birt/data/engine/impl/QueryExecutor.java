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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.expression.ColumnReferenceExpression;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ExpressionCompiler;
import org.eclipse.birt.data.engine.expression.ExpressionProcessor;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateCalculator;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateTable;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.OnFetchScriptHelper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */
public abstract class QueryExecutor
{
	protected 	IQuery			odiQuery;
	protected 	IDataSource		odiDataSource;
	protected 	DataSourceRuntime	dataSource;
	
	/** Runtime data set used by this instance of executor */
	protected	DataSetRuntime	dataSet;
	
	/** Outer query's results; null if this query is not nested */
	protected	QueryResults	outerResults;
	
	protected 	AggregateCalculator		aggregates;
	protected 	IResultIterator	odiResult;
	
	private		Scriptable		queryScope;
	
	/** Externally provided query scope; can be null */
	private		Scriptable		parentScope;

	private 	boolean 		isPrepared = false;
	private 	boolean			isExecuted = false;
	private		Map				queryAppContext;

	/** Query nesting level, 1 - outermost query */
	protected	int				nestedLevel = 1;
	
	private static Logger logger = Logger.getLogger( DataEngineImpl.class.getName( ) );
	
	/**
	 * @return
	 */
	abstract public Scriptable getSharedScope( );
	
	/**
	 * @return
	 */	
	abstract protected ExpressionCompiler getExpressionCompiler( );
	
	/**
	 * @return
	 */
	abstract protected IBaseQueryDefinition getBaseQueryDefn( );
	
	/**
	 * @return
	 */
	abstract protected AggregateTable getAggrTable( );
	
	/**
	 * Create a new unopened odiDataSource given the data source runtime
	 * definition
	 * 
	 * @return
	 */
	abstract protected IDataSource createOdiDataSource( )
			throws DataException;

	/**
	 * Provide the actual DataSourceRuntime used for the query.
	 * 
	 * @return
	 */
	abstract protected DataSourceRuntime findDataSource( )
			throws DataException;

	/**
	 * Create a new instance of data set runtime
	 * 
	 * @return
	 */
	abstract protected DataSetRuntime newDataSetRuntime( )
			throws DataException;

	/**
	 * Create an empty instance of odi query
	 * 
	 * @return
	 */
	abstract protected IQuery createOdiQuery( ) throws DataException;
	
	/**
	 * Executes the ODI query to reproduce a ODI result set
	 * 
	 * @return
	 */
	abstract protected IResultIterator executeOdiQuery( )
			throws DataException;

	/**
	 * Prepares the ODI query
	 */
	protected void prepareOdiQuery( ) throws DataException
	{
	}
	
	/**
	 * @param context
	 */
	protected void setAppContext( Map context )
	{
	    queryAppContext = context;
	}
	
	/**
	 * @return
	 */
	public DataSetRuntime getDataSet()
	{
		return dataSet;
	} 
	
	/**
	 * Gets the Javascript scope for evaluating expressions for this query
	 * 
	 * @return
	 */
	public Scriptable getQueryScope()
	{
		if ( queryScope == null )
		{
			// Set up a query scope. All expressions are evaluated against the 
			// Data set JS object as the prototype (so that it has access to all
			// data set properties). It uses a subscope of the externally provided
			// parent scope, or the global shared scope
			queryScope = newSubScope( parentScope );
			queryScope.setPrototype( dataSet.getJSDataSetObject() );
		}
		return queryScope;
	}
	
	/**
	 * Creates a subscope within parent scope
	 * @param parentScope parent scope. If null, the shared top-level scope is used as parent
	 */
	Scriptable newSubScope( Scriptable parentScope )
	{
		if ( parentScope == null )
			parentScope = getSharedScope( );
		
		Context cx = Context.enter( );
		try
		{
			Scriptable scope = cx.newObject( parentScope );
			scope.setParentScope( parentScope );
			scope.setPrototype( parentScope );
			return scope;
		}
		finally
		{
			Context.exit( );
		}
	}
	
	/**
	 * Prepare Executor so that it is ready to execute the query
	 * 
	 * @param outerRts
	 * @param targetScope
	 * @throws DataException
	 */
	public void prepareExecution( IQueryResults outerRts, Scriptable targetScope ) throws DataException
	{
		if(isPrepared)return;
		
		this.parentScope = targetScope;
		dataSource = findDataSource( );

		if ( outerRts != null )
		{
			outerResults = ((QueryResults) outerRts );
			if ( outerResults.isClosed( ) )
			{
				// Outer result is closed; invalid
				throw new DataException( ResourceConstants.RESULT_CLOSED );
			}
			this.nestedLevel = outerResults.getNestedLevel( );
		}
		
		// Create the data set runtime
		// Since data set runtime contains the execution result, a new data set
		// runtime is needed for each execute
		dataSet = newDataSetRuntime();
		assert dataSet != null;
		
		openDataSource( );
		
		// Run beforeOpen script now so the script can modify the DataSetRuntime properties
		dataSet.beforeOpen();
					
		IExpressionProcessor exprProcessor = new ExpressionProcessor( null,
				null,
				dataSet,
				null );

		// Let subclass create a new and empty intance of the appropriate
		// odi IQuery
		odiQuery = createOdiQuery( );
		odiQuery.setExprProcessor( exprProcessor );
		populateOdiQuery( );
		prepareOdiQuery( );
		isPrepared = true;
	}
	
	/**
	 * @return
	 * @throws DataException
	 */
	public IResultMetaData getResultMetaData( ) throws DataException
	{
		assert odiQuery instanceof IPreparedDSQuery
				|| odiQuery instanceof ICandidateQuery;
		if ( odiQuery instanceof IPreparedDSQuery )
		{
			if ( ( (IPreparedDSQuery) odiQuery ).getResultClass( ) != null )
				return new ResultMetaData( ( (IPreparedDSQuery) odiQuery ).getResultClass( ) );
			else
			    return null;
		}
		else
		{
			return new ResultMetaData( ( (ICandidateQuery) odiQuery ).getResultClass( ) );
		}
	}
	
	/**
	 * @throws DataException
	 */
	public void execute() throws DataException
	{
		logger.logp( Level.FINER,
				QueryExecutor.class.getName( ),
				"execute",
				"Start to execute" );

		if(this.isExecuted)
			return;

		// Execute the query
		odiResult = executeOdiQuery( );

		// Bind the row object to the odi result set
		this.dataSet.setResultSet( odiResult, false );
			
	    // Calculate aggregate values
	    aggregates = new AggregateCalculator( getAggrTable( ), odiResult );
		    
	    // Calculate aggregate values
	    aggregates.calculate( getQueryScope() );
		
		this.isExecuted = true;
		
		logger.logp( Level.FINER,
				QueryExecutor.class.getName( ),
				"execute",
				"Finish executing" );
	}

	/**
	 * Closes the executor; release all odi resources
	 */
	public void close()
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
    		dataSet.beforeClose();
		}
	    catch (DataException e )
		{
			logger.logp( Level.FINE,
					QueryExecutor.class.getName( ),
					"close",
					e.getMessage( ),
					e );
		}
	    
	    if ( odiResult != null )
	    	odiResult.close();
	    odiQuery.close();
	    
	    try
		{
    		dataSet.close();
		}
	    catch (DataException e )
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
    		dataSet.afterClose();
		}
	    catch (DataException e )
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
		if ( dataSource != null  )
		{
			// TODO: potential bug
			if ( !dataSource.isOpen( )
					|| DataSetCacheManager.getInstance( ).doesLoadFromCache( ) == true )
			{
				// Data source is not open; create an Odi Data Source and open it
				// We should run the beforeOpen script now to give it a chance to modify
				// runtime data source properties
				dataSource.beforeOpen();
				
				// Let subclass create a new unopened odi data source
				odiDataSource = createOdiDataSource( ); 
				
				// Passes thru the prepared query executor's 
				// context to the new odi data source
			    odiDataSource.setAppContext( queryAppContext );

				// Open the odi data source
				dataSource.openOdiDataSource( odiDataSource );
				
				dataSource.afterOpen();
			}
			else
			{
				// Use existing odiDataSource created for the data source runtime
				odiDataSource = dataSource.getOdiDataSource();
				
				// Passes thru the prepared query executor's 
				// current context to existing data source
			    odiDataSource.setAppContext( queryAppContext );
			}
		}
	}
	
	/**
	 * Populates odiQuery with this query's definitions
	 */
	protected void populateOdiQuery( ) throws DataException
	{
		assert odiQuery != null;
		assert getBaseQueryDefn( ) != null;
		
		Context cx = Context.enter();
		try
		{
			List temporaryComputedColumns = new ArrayList();
			
			// Set grouping
			List groups = getBaseQueryDefn( ).getGroups();
			if ( groups != null && ! groups.isEmpty() )
			{
				IQuery.GroupSpec[] groupSpecs = new IQuery.GroupSpec[ groups.size() ];
				Iterator it = groups.iterator();
				for ( int i = 0; it.hasNext(); i++ )
				{
					IGroupDefinition src = (IGroupDefinition) it.next();
					//TODO does the index of column significant?
					IQuery.GroupSpec dest = groupDefnToSpec(cx, src,"_{$TEMP_GROUP_"+i+"$}_", -1 );
					groupSpecs[i] = dest;
					
					if( groupSpecs[i].isCompleteExpression() )
					{
						temporaryComputedColumns.add(new ComputedColumn( "_{$TEMP_GROUP_"+i+"$}_", src.getKeyExpression(), getTempComputedColumnType( groupSpecs[i].getInterval() )));
					}
				}
				odiQuery.setGrouping( Arrays.asList( groupSpecs));
			}		
			// Set sorting
			List sorts = getBaseQueryDefn( ).getSorts();
			if ( sorts != null && !sorts.isEmpty( ) )
			{
				IQuery.SortSpec[] sortSpecs = new IQuery.SortSpec[ sorts.size() ];
				Iterator it = sorts.iterator();
				for ( int i = 0; it.hasNext(); i++ )
				{
					ISortDefinition src = (ISortDefinition) it.next();
					int sortIndex = -1;
					String sortKey = src.getColumn();
					if ( sortKey == null || sortKey.length() == 0 )
					{ 
						//Firstly try to treat sort key as a column reference expression
						ColumnInfo columnInfo = getColInfoFromJSExpr( cx,
								src.getExpression( ).getText() );
													
						sortIndex = columnInfo.getColumnIndex(); 
						sortKey = columnInfo.getColumnName( );
					}
					if ( sortKey == null && sortIndex < 0 )
					{
						//If failed to treate sort key as a column reference expression
						//then treat it as a computed column expression
						temporaryComputedColumns.add(new ComputedColumn( "_{$TEMP_SORT_"+i+"$}_", src.getExpression().getText(), DataType.ANY_TYPE));
						sortIndex = -1; 
						sortKey = String.valueOf("_{$TEMP_SORT_"+i+"$}_");
					}
					
					IQuery.SortSpec dest = new IQuery.SortSpec( sortIndex,
							sortKey,
							src.getSortDirection( ) == ISortDefinition.SORT_ASC );
					sortSpecs[i] = dest;
				}
				odiQuery.setOrdering( Arrays.asList( sortSpecs));
			}

			
			List computedColumns = null;
		    // set computed column event
			computedColumns = this.dataSet.getComputedColumns( );
			if ( computedColumns != null )
			{
				computedColumns.addAll( temporaryComputedColumns );
			}
			if ( (computedColumns != null && computedColumns.size() > 0)|| temporaryComputedColumns.size( ) > 0 )
			{
				IResultObjectEvent objectEvent = new ComputedColumnHelper( this.dataSet,
						(computedColumns == null&&computedColumns.size()>0) ? temporaryComputedColumns : computedColumns );
				odiQuery.addOnFetchEvent( objectEvent );
			}
	    	if ( dataSet.getEventHandler() != null )
	    	{
	    		OnFetchScriptHelper event = new OnFetchScriptHelper( dataSet ); 
	    		odiQuery.addOnFetchEvent( event );
		    }
		    
		    // set filter event
		    List dataSetFilters = new ArrayList( );
		    List queryFilters = new ArrayList( );
		    if ( dataSet.getFilters( ) != null )
			{
				dataSetFilters = dataSet.getFilters( );
			}
		    
		    if ( getBaseQueryDefn( ).getFilters( ) != null )
			{
		    	queryFilters = getBaseQueryDefn( ).getFilters( );
			}
		   		   			    
		    if ( dataSetFilters.size( ) + queryFilters.size( ) > 0 )
		    {
		    	IResultObjectEvent objectEvent = new FilterByRow( dataSetFilters, queryFilters,
		    			dataSet );
		    	odiQuery.addOnFetchEvent( objectEvent );
		    }
		    
			// specify max rows the query should fetch
		    odiQuery.setMaxRows( getBaseQueryDefn( ).getMaxRows() );
		}
		finally
		{
			Context.exit();
		}
	}
	
	/**
	 * Convert IGroupDefn to IQuery.GroupSpec
	 * 
	 * @param cx
	 * @param src
	 * @return
	 * @throws DataException
	 */
	private IQuery.GroupSpec groupDefnToSpec( Context cx,
			IGroupDefinition src, String columnName, int index )
			throws DataException
	{
		int groupIndex = -1;
		String groupKey = src.getKeyColumn();
		boolean isComplexExpression = false;
		if ( groupKey == null || groupKey.length() == 0 )
		{
			// Group key expressed as expression; convert it to column name
			// TODO support key expression in the future by creating implicit
			// computed columns
			ColumnInfo groupKeyInfo = getColInfoFromJSExpr( cx,
				src.getKeyExpression( ) );
			//getColInfoFromJSExpr( cx,src.getKeyExpression( ) );
			groupIndex = groupKeyInfo.getColumnIndex( );
			groupKey = groupKeyInfo.getColumnName();
		}
		if ( groupKey == null && groupIndex < 0 )
		{
			ColumnInfo groupKeyInfo = new ColumnInfo(index, columnName );
			groupIndex = groupKeyInfo.getColumnIndex( );
			groupKey = groupKeyInfo.getColumnName();
			isComplexExpression = true;
		}
		
		IQuery.GroupSpec dest = new IQuery.GroupSpec( groupIndex, groupKey );
		dest.setName( src.getName() );
		dest.setInterval( src.getInterval());
		dest.setIntervalRange( src.getIntervalRange());
		dest.setIntervalStart( src.getIntervalStart());
		dest.setSortDirection( src.getSortDirection());
		dest.setFilters( src.getFilters());
		dest.setSorts( src.getSorts() );
		dest.setIsComplexExpression( isComplexExpression );
		return dest;
	}
	
	/**
	 * Common code to extract the name of a column from a JS expression which is
	 * in the form of "row.col". If expression is not in expected format,
	 * returns null
	 * 
	 * @param cx
	 * @param expr
	 * @return
	 */
	private ColumnInfo getColInfoFromJSExpr( Context cx, String expr )
	{
		int colIndex = -1;
		String colName = null;
		ExpressionCompiler compiler = getExpressionCompiler( );
		CompiledExpression ce = compiler.compile( expr, null, cx );
		if ( ce instanceof ColumnReferenceExpression )
		{
			ColumnReferenceExpression cre = ( (ColumnReferenceExpression) ce );
			colIndex = cre.getColumnindex( );
			colName = cre.getColumnName( );
		}
		return new ColumnInfo( colIndex, colName );
	}
	
	/**
	 * @param groupSpecs
	 * @param i
	 */
	private int getTempComputedColumnType( int i )
	{
		int interval = i;
		if( interval == IQuery.GroupSpec.DAY_INTERVAL 
			|| interval == IQuery.GroupSpec.HOUR_INTERVAL
			|| interval == IQuery.GroupSpec.MINUTE_INTERVAL
			|| interval == IQuery.GroupSpec.SECOND_INTERVAL
			|| interval == IQuery.GroupSpec.MONTH_INTERVAL
			|| interval == IQuery.GroupSpec.QUARTER_INTERVAL
			|| interval == IQuery.GroupSpec.YEAR_INTERVAL
			|| interval == IQuery.GroupSpec.WEEK_INTERVAL )
			interval = DataType.DATE_TYPE;
		else if ( interval == IQuery.GroupSpec.NUMERIC_INTERVAL )
			interval = DataType.DOUBLE_TYPE;
		else if ( interval == IQuery.GroupSpec.STRING_PREFIX_INTERVAL )
			interval = DataType.STRING_TYPE;
		else
			interval = DataType.ANY_TYPE;
		return interval;
	}
	
	/**
	 * Simple wrapper of colum information, including column index and column
	 * name.
	 */
	private static class ColumnInfo
	{
		private int columnIndex;
		private String columnName;

		ColumnInfo( int columnIndex, String columnName )
		{
			this.columnIndex = columnIndex;
			this.columnName = columnName;
		}

		public int getColumnIndex( )
		{
			return columnIndex;
		}

		public String getColumnName( )
		{
			return columnName;
		}
	}
	
}
