/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseTransform;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ExpressionProcessorManager;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.JSOutputParams;
import org.eclipse.birt.data.engine.script.JSRowObject;
import org.eclipse.birt.data.engine.script.JSRows;
import org.eclipse.birt.data.engine.script.OnFetchScriptHelper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;


/** 
 * Base class for a prepared query or subquery. 
 */
abstract class PreparedQuery 
{
	private 	IBaseQueryDefinition 	queryDefn;
	private 	DataEngineImpl	engine;
	private  	AggregateTable	aggrTable;
	private 	Object appContext;
	
	// Map of Subquery name (String) to PreparedSubquery
	protected HashMap subQueryMap = new HashMap();
	
	protected static Logger logger = Logger.getLogger( DataEngineImpl.class.getName( ) );

	PreparedQuery( DataEngineImpl engine, IBaseQueryDefinition queryDefn )
		throws DataException
	{
		logger.logp( Level.FINE,
				PreparedQuery.class.getName( ),
				"PreparedQuery",
				"PreparedQuery starts up." );
	    assert engine != null && queryDefn != null;
		this.engine = engine;
		this.queryDefn = queryDefn;
		this.aggrTable = new AggregateTable(this);
		
		logger.fine( "Start to prepare a PreparedQuery." );
		prepare();
		logger.fine( "Finished preparing the PreparedQuery." );
	}
	
	/** Gets the IBaseQueryDefn instance which defines this query */
	protected IBaseQueryDefinition getQueryDefn( )
	{
		return queryDefn;
	}
	
	/** Gets the registry of all aggregate expression */
	AggregateTable getAggrTable()
	{
		return aggrTable;
	}

	protected Object getAppContext()
	{
	    return appContext;	    
	}
	
	protected void setAppContext( Object context )
	{
	    appContext = context;
	}
	
	/** Gets the appropriate subclass of the Executor */
	protected abstract Executor newExecutor();
	
	/**
	 * Gets the main data source query. For a SubQuery, this returns the top-level
	 * data source query that contains the SubQuery. For other queries, "this"
	 * is returned
	 */
	abstract protected PreparedDataSourceQuery getDataSourceQuery();
	
	private void prepare( )	throws DataException
	{
	    // TODO - validation of static queryDefn

		Context cx = Context.enter();
		
		try
		{
			// Prepare all groups; note that the report query iteself
			// is treated as a group (with group level 0 )
			List groups = queryDefn.getGroups( );
			IGroupDefinition group;
			//If there are group definitions that of invalid or duplicate group name ,then
			//throw exceptions.
			for ( int i = 0; i < groups.size( ); i++ )
			{
				group = (IGroupDefinition) groups.get( i );
				if ( group.getName( ) == null
						|| group.getName( ).trim( ).length( ) == 0 )
					continue;
				for ( int j = 0; j < groups.size( ); j++ )
				{
					if ( group.getName( )
							.equals( ( (IGroupDefinition) groups.get( j ) ).getName( ) == null
									? ""
									: ( (IGroupDefinition) groups.get( j ) ).getName( ) )
							&& j != i )
						throw new DataException( ResourceConstants.DUPLICATE_GROUP_NAME );
				}
			}
			
			for ( int i = 0; i <= groups.size( ); i++ )
			{
				// Group 0
				IBaseTransform groupDefn;
				if ( i == 0 )
					groupDefn = queryDefn;
				else
				{
					groupDefn = (IGroupDefinition) groups.get( i - 1 );
					// Filter on group is not supported now, throw exception
					// TODO support filter on group in the future
					//if ( groupDefn.getFilters( ).size( ) > 0 )
					//	throw new DataException( ResourceConstants.UNSUPPORTED_FILTER_ON_GROUP );
				}
				prepareGroup( groupDefn, i, cx );
			}			
			
		}
		finally
		{
		    Context.exit();
		}
	} 

	/**
	 * Return the QueryResults. But the execution of query would be deferred 
	 * @param outerResults If query is nested within another query, this is the outer query's query 
	 *        result handle.
	 * @param scope The ElementState object for the report item using the query; this acts as the 
	 *    JS scope for evaluating script expressions.
	 */
	protected QueryResults doPrepare( IQueryResults outerResults, Scriptable scope ) throws DataException
	{
		if ( this.queryDefn == null )
		{
			// we are closed
			DataException e = new DataException(ResourceConstants.PREPARED_QUERY_CLOSED);
			logger.logp( Level.WARNING,
					PreparedQuery.class.getName( ),
					"doPrepare",
					"PreparedQuery instance is closed.",
					e );
			throw e;
		}
		
		Executor executor = newExecutor();
		

		
		// pass the prepared query's pass thru context to its executor
		executor.setAppContext( this.getAppContext() );
		

		//here prepare the execution. After the preparation the result metadata is available by
		//calling getResultClass, and the query is ready for execution.
		logger.finer( "Start to prepare the execution." );
		executor.prepareExecution( outerResults, scope );
		logger.finer( "Finish preparing the execution." );
	    return new QueryResults( engine.getContext( ),
				getDataSourceQuery( ),
				this,
				executor );
	}
	
	// Common code to extract the name of a column from a JS expression which is 
	// in the form of "row.col". If expression is not in expected format, returns null
	private ColumnInfo getColInfoFromJSExpr( Context cx, String expr )
	{
		int colIndex = -1;
		String colName = null;
		ExpressionCompiler compiler = engine.getExpressionCompiler( );
		CompiledExpression ce = compiler.compile( expr, null, cx );
		if ( ce instanceof ColumnReferenceExpression )
		{
			ColumnReferenceExpression cre = ( (ColumnReferenceExpression) ce );
			colIndex = cre.getColumnindex( );
			colName = cre.getColumnName( );
		}
		return new ColumnInfo( colIndex, colName );
	}
	
	private void prepareGroup( IBaseTransform trans, int groupLevel, Context cx )
		throws DataException
	{
		// prepare expressions appearing in this group
		prepareExpressions( trans.getAfterExpressions(), groupLevel, true, cx );
		prepareExpressions( trans.getBeforeExpressions(), groupLevel, false, cx );
		prepareExpressions( trans.getRowExpressions(), groupLevel, false, cx );
		
		// Prepare subqueries appearing in this group
		Collection subQueries = trans.getSubqueries( );
		Iterator subIt = subQueries.iterator( );
		while ( subIt.hasNext( ) )
		{
			ISubqueryDefinition subquery = (ISubqueryDefinition) subIt.next( );
			PreparedSubquery pq = new PreparedSubquery( subquery, this, groupLevel );
			subQueryMap.put( subquery.getName(), pq);
		}
	}
	
	/* Prepares all expressions in the given collection */
	private void prepareExpressions( Collection expressions, int groupLevel, 
			boolean afterGroup, Context cx )
	{
	    if ( expressions == null )
	        return;
	    
	    AggregateRegistry reg = this.aggrTable.getAggrRegistry( groupLevel, afterGroup, cx );
	    Iterator it = expressions.iterator();
	    while ( it.hasNext() )
	    {
	        prepareExpression((IBaseExpression) it.next(), groupLevel, cx, reg);
	    }
	}
	
	// Prepares one expression 
	private void prepareExpression( IBaseExpression expr, int groupLevel, Context cx, 
			AggregateRegistry reg )
	{
	    ExpressionCompiler compiler = this.engine.getExpressionCompiler();
	    
	    if ( expr instanceof IScriptExpression )
	    {
	    	String exprText = ((IScriptExpression) expr).getText();
	    	CompiledExpression handle = compiler.compile( exprText, reg, cx);
	    	expr.setHandle( handle );
	    	expr.setID( IDUtil.nextExprID( ) );
	    }
	    else if ( expr instanceof IConditionalExpression )
	    {
	    	// 3 sub expressions of the conditional expression should be prepared
	    	// individually
	    	IConditionalExpression ce = (IConditionalExpression) expr;
	    	prepareExpression( ce.getExpression(), groupLevel, cx, reg );
	    	if ( ce.getOperand1() != null )
		    	prepareExpression( ce.getOperand1(), groupLevel, cx, reg );
	    	if ( ce.getOperand2() != null )
		    	prepareExpression( ce.getOperand2(), groupLevel, cx, reg );

	    	// No separate preparation is required for the conditional expression 
	    	// Set itself as the compiled handle
	    	expr.setHandle( expr );
	    	expr.setID( IDUtil.nextExprID( ) );
	    }
	    else
	    {
	    	// Should never get here
	    	assert false;
	    }
	}
	
	/**
	 * Convert IGroupDefn to IQuery.GroupSpec
	 * @param cx
	 * @param src
	 * @return
	 * @throws DataException
	 */
	protected IQuery.GroupSpec groupDefnToSpec( Context cx, IGroupDefinition src, String columnName, int index ) throws DataException
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
	 * Executes a subquery
	 */
	QueryResults execSubquery( IResultIterator iterator,
			String subQueryName, Scriptable scope ) throws DataException
	{
		assert subQueryName != null;
		assert scope != null;

		PreparedSubquery subquery = (PreparedSubquery) subQueryMap.get( subQueryName );
		if ( subquery == null )
		{
			DataException e = new DataException( ResourceConstants.SUBQUERY_NOT_FOUND,
					subQueryName );
			logger.logp( Level.FINE,
					PreparedQuery.class.getName( ),
					"execSubquery",
					"Subquery name not found",
					e );
			throw e;
		}
		
		return subquery.execute( iterator, scope );
	}
	
	public DataEngineImpl getDataEngine()
	{
		return engine;
	}
	
	/**
	 * Closes the prepared query. This instance can no longer be executed after it is closed
	 * TODO: expose this method in the IPreparedQuery interface
	 */
	public void close()
	{
		queryDefn = null;
		this.aggrTable = null;
		this.engine = null;
		this.subQueryMap = null;
		logger.logp( Level.FINER,
				PreparedQuery.class.getName( ),
				"close",
				"Prepared query closed" );
		// TODO: close all open QueryResults obtained from this PreparedQuery
	}
	
	/**
	 * Finds a group given a text identifier of a group. Returns index of group found (1 = outermost
	 * group, 2 = second level group etc.). The text identifier can be the group name, the group key
	 * column name, or the group key expression text. Returns -1 if no matching group is found
	 */
	int getGroupIndex( String groupText )
	{
		assert groupText != null;
		assert queryDefn != null; 
		
		List groups = queryDefn.getGroups();
		for ( int i = 0; i < groups.size(); i++)
		{
			IGroupDefinition group = (IGroupDefinition) groups.get(i);
			if ( groupText.equals( group.getName()) ||
				 groupText.equals( group.getKeyColumn() ) ||
				 groupText.equals( group.getKeyExpression()) )
			 {
				return i + 1;			// Note that group index is 1-based
			 }
		}
		return -1;
	}
	
	/**
	 * Gets the group count defined in report query
	 */
	int getGroupCount()
	{
		assert queryDefn != null;
		return queryDefn.getGroups().size();
	}
	
	/**
	 * PreparedQuery.Executor: executes a prepared query and maintains execute-time data and result
	 * set associated with an execution.
	 * 
	 * A PreparedQuery can be executed multiple times. Each execute is performed by one instance of 
	 * the Executor.
	 * 
	 * Each subclass of PreparedQuery is expected to have its own subclass of the Executor.
	 */
	abstract class Executor
	{
		protected 	IQuery			odiQuery;
		protected 	IDataSource		odiDataSource;
		protected 	DataSourceRuntime	dataSource;
		protected	DataSetRuntime	dataSet;
		protected 	AggregateCalculator		aggregates;
		protected 	IResultIterator	odiResult;
		protected 	JSRowObject		rowObject;
		protected 	JSRows			rowsObject;
		protected   JSOutputParams 	outputParamsJSObject;
		protected	Scriptable		scope;
		protected 	IQueryResults outerResults;
		private 	boolean 		isPrepared = false;
		private 	boolean			isExecuted = false;
		private		Object			queryAppContext;
		
		/**
		 * Overridden by subclass to create a new unopened odiDataSource given the data 
		 * source runtime definition
		 */
		abstract protected IDataSource createOdiDataSource( ) throws DataException;
		
		/**
		 * Overridden by subclass to provide the actual DataSourceRuntime used for the query.
		 */
		abstract protected DataSourceRuntime findDataSource( ) throws DataException;
		
		/**
		 * Overridden by subclass to create a new instance of data set runtime
		 */
		abstract protected DataSetRuntime newDataSetRuntime( ) throws DataException;

		/**
		 * Overridden by sub class to create an emty instance of odi query
		 */
		abstract protected IQuery createOdiQuery( ) throws DataException;
		
		/**
		 * Executes the ODI query to reproduce a ODI result set
		 */
		abstract protected IResultIterator executeOdiQuery( ) throws DataException;

		/**
		 * Prepares the ODI query
		 */
		protected void prepareOdiQuery(  ) throws DataException
		{
		}
		
		/**
		 * Constructor
		 */
		public Executor( )
		{
		}

		protected Object getAppContext()
		{
		    return queryAppContext;	    
		}
		
		protected void setAppContext( Object context )
		{
		    queryAppContext = context;
		}
		
		/*
		 * Prepare Executor so that it is ready to execute the query
		 * 
		 */
		private void prepareExecution( IQueryResults outerRts, Scriptable targetScope ) throws DataException
		{
			if(isPrepared)return;
			
			dataSource = findDataSource( );
	
			if ( targetScope == null )
			{
				if ( this.dataSource != null )
				{
					dataSource.setScope( DataEngineImpl.createSubscope(engine.getSharedScope( )) );
					this.scope = DataEngineImpl.createSubscope( this.dataSource.getScriptable( ) );
				}
				else
					this.scope = DataEngineImpl.createSubscope( engine.getSharedScope( ) );
			}
			else
			{
				if ( this.dataSource != null )
				{
					dataSource.setScope( createSubscope (targetScope) );
				    this.scope = createSubscope( this.dataSource.getScriptable( ) );
				}
				else
					this.scope = targetScope;
			}

			
			openDataSource( );
			
			//this.scope = DataEngineImpl.createSubscope( this.dataSource.getScriptable());
			this.outerResults = outerRts;
			// Create the data set runtime
			// Since data set runtime contains the execution result, a new data set
			// runtime is needed for each execute
			dataSet = newDataSetRuntime();
				
		    // Set up the Javascript "row" object; this is needed before executeOdiQuery
			// since filtering may need the object
		    rowObject = new JSRowObject( dataSet );
		    this.scope.put( "row", this.scope,  rowObject );
				
			
			
			if ( dataSet != null  )
			{
				// Run beforeOpen script now so the script can modify the DataSetRuntime properties
				dataSet.beforeOpen();
			}
			

			ExpressionProcessorManager.registerInstance(new ExpressionProcessor( null, null, scope, null ));
			
			// Let subclass create a new and empty intance of the appropriate odi IQuery
			odiQuery = createOdiQuery( );
			populateOdiQuery( );
			prepareOdiQuery( );
			isPrepared = true;
		}
		
		/**
		 * Creates a new child scope using given parent
		 */
		private Scriptable createSubscope( Scriptable parent )
		{
			Context cx = Context.enter( );
			try
			{
				Scriptable scope = cx.newObject( parent );
				//scope.setPrototype( prototype );
				scope.setParentScope( parent );
				return scope;
			}
			catch ( JavaScriptException e )
			{
				// Not expected; use provided scrope instead
				logger.logp( Level.WARNING,
						DataEngineImpl.class.getName( ),
						"createSubscope",
						"Failed to create sub scope",
						e );
				return parent;
			}
			finally
			{
				Context.exit( );
			}

		}
		
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
		
		public void execute() throws DataException
		{
			logger.logp( Level.FINER,
					PreparedQuery.Executor.class.getName( ),
					"execute",
					"Start to execute" );

			if(this.isExecuted)
				return;

			// Set the Javascript "rows" object and bind it to our result
			rowsObject = new JSRows( this.outerResults, rowObject );
			scope.put( "rows", scope, rowsObject );
			

			outputParamsJSObject = new JSOutputParams( );
			scope.put( "outputParams", scope, outputParamsJSObject );
			

			// Execute the query
			odiResult = executeOdiQuery( );

			// Bind the row object to the odi result set
			rowObject.setResultSet( odiResult, false );
				
		    // Calculate aggregate values
		    aggregates = new AggregateCalculator( aggrTable, odiResult );
			    
		    // Set up the internal JS _aggr_value object and bind it to the aggregate calc engine
		    Scriptable aggrObj = aggregates.getJSAggrValueObject();
		    scope.put( ExpressionCompiler.AGGR_VALUE, scope, aggrObj );
			    
			Context cx = Context.enter();
			try
			{
			    // Calculate aggregate values
			    aggregates.calculate(cx, scope);
			    
			}
			finally
			{
				Context.exit();
			}
			
			this.isExecuted = true;
			
			logger.logp( Level.FINER,
					PreparedQuery.Executor.class.getName( ),
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
						PreparedQuery.Executor.class.getName( ),
						"close",
						"executor closed " );
				return;
			}
			
		    // Close the data set and associated odi query
		    try
			{
		    	if ( dataSet != null )
		    		dataSet.beforeClose();
			}
		    catch (DataException e )
			{
				logger.logp( Level.FINE,
						PreparedQuery.Executor.class.getName( ),
						"close",
						e.getMessage( ),
						e );
			}
		    
		    if ( odiResult != null )
		    	odiResult.close();
		    odiQuery.close();
		    
		    try
			{
		    	if ( dataSet != null )
		    	{
		    		dataSet.close();
		    	}
			}
		    catch (DataException e )
			{
				logger.logp( Level.FINE,
						PreparedQuery.Executor.class.getName( ),
						"close",
						e.getMessage( ),
						e );
			}
		    
			odiQuery = null;
			odiDataSource = null;
			aggregates = null;
			odiResult = null;
			rowObject = null;
			rowsObject = null;
			scope = null;
			isPrepared = false;
			isExecuted = false;
			
			// Note: reset dataSet and dataSource only after afterClose() is executed, since
			// the script may access these two objects
	    	if ( dataSet != null )
	    	{
	    		try
				{
		    		dataSet.afterClose();
				}
			    catch (DataException e )
				{
					logger.logp( Level.FINE,
								PreparedQuery.Executor.class.getName( ),
								"close",
								e.getMessage( ),
								e );
				}
			    dataSet = null;
	    	}
			dataSource = null;
		    
			logger.logp( Level.FINER,
					PreparedQuery.Executor.class.getName( ),
					"close",
					"executor closed " );
		}
		
		/**
		 * Open the required DataSource. This method should be called after "dataSource"
		 * is initialized by findDataSource() method.
		 * @throws DataException
		 */ 
		 
		protected void openDataSource( ) throws DataException
		{
			assert odiDataSource == null;
			
			// Open the underlying data source
		    // dataSource = findDataSource( );
			if ( dataSource != null  )
			{
				if ( ! dataSource.isOpen() )
				{
					// Data source is not open; create an Odi Data Source and open it
					// We should run the beforeOpen script now to give it a chance to modify
					// runtime data source properties
					dataSource.beforeOpen();
					
					// Let subclass create a new unopened odi data source
					odiDataSource = createOdiDataSource( ); 
					
					// Passes thru the prepared query executor's 
					// context to the new odi data source
				    odiDataSource.setAppContext( getAppContext() );

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
				    odiDataSource.setAppContext( getAppContext() );
				}
			}
		}
		
		/**
		 * Populates odiQuery with this query's definitions
		 */
		protected void populateOdiQuery( ) throws DataException
		{
			assert odiQuery != null;
			assert scope != null;
			assert queryDefn != null;
			
			Context cx = Context.enter();
			try
			{
				List temporaryComputedColumns = new ArrayList();
				
				// Set grouping
				List groups = queryDefn.getGroups();
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
							temporaryComputedColumns.add(new ComputedColumn( "_{$TEMP_GROUP_"+i+"$}_", src.getKeyExpression(), DataType.ANY_TYPE));
					}
					odiQuery.setGrouping( Arrays.asList( groupSpecs));
				}		
				// Set sorting
				List sorts = queryDefn.getSorts();
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
			    if ( dataSet != null )
				{
					computedColumns = this.dataSet.getComputedColumns( );
					if ( computedColumns != null )
					{
						computedColumns.addAll( temporaryComputedColumns );
						
					}
				}
				if ( (computedColumns != null && computedColumns.size() > 0)|| temporaryComputedColumns.size( ) > 0 )
				{
					IResultObjectEvent objectEvent = new ComputedColumnHelper( ExpressionProcessorManager.getInstance( )
							.getScope( ),
							this.rowObject,
							(computedColumns == null&&computedColumns.size()>0) ? temporaryComputedColumns : computedColumns );
					odiQuery.addOnFetchEvent( objectEvent );
				}
			    // set Onfetch event - this should be added after computed column and before filtering
			    if ( dataSet != null )
			    {
			    	String onFetchScript = dataSet.getOnFetchScript();
			    	if ( onFetchScript != null && onFetchScript.length() > 0 )
			    	{
			    		OnFetchScriptHelper event = new OnFetchScriptHelper( dataSet ); 
			    		odiQuery.addOnFetchEvent( event );
			    	}
			    }
			    
				// Set filtering
		    	assert rowObject != null;
		    	assert scope != null;

			    // set filter event
			    List mergedFilters = new ArrayList( );
			    if ( queryDefn.getFilters( ) != null )
				{
					mergedFilters.addAll( queryDefn.getFilters( ) );
				}
			    
			    if ( dataSet != null &&
		    		 dataSet.getFilters( ) != null )
				{
					mergedFilters.addAll( dataSet.getFilters( ) );
				}
			    
			    if ( mergedFilters.size() > 0 )
			    {
			    	IResultObjectEvent objectEvent = new FilterByRow( mergedFilters, 
			    			ExpressionProcessorManager.getInstance().getScope(),  rowObject );
			    	odiQuery.addOnFetchEvent( objectEvent );
			    }
			    
				// specify max rows the query should fetch
			    odiQuery.setMaxRows( queryDefn.getMaxRows() );
			}
			finally
			{
				Context.exit();
			}
		}
	}
	
	/**
	 * Simple wrapper of colum information, including
	 * column index and column name.
	 */
	private static class ColumnInfo
	{
		private int columnIndex;
		private String columnName;
		
		ColumnInfo(int columnIndex, String columnName)
		{
			this.columnIndex = columnIndex;
			this.columnName = columnName;
		}
		
		public int getColumnIndex()
		{
			return columnIndex;
		}
		
		public String getColumnName()
		{
			return columnName;
		}
	}
	
}
