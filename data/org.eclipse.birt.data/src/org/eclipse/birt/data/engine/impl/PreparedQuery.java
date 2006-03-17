/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseTransform;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ExpressionCompiler;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateRegistry;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateTable;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/** 
 * Base class for a prepared query or subquery. 
 */
final class PreparedQuery 
{
	private 	IBaseQueryDefinition 	queryDefn;
	
	private     DataEngineContext deContext;
	private     ExpressionCompiler exCompiler;
	private     Scriptable sharedScope;
	
	private  	AggregateTable	aggrTable;
	private 	Map appContext;
	
	// Map of Subquery name (String) to PreparedSubquery
	protected HashMap subQueryMap = new HashMap();
	
	protected static Logger logger = Logger.getLogger( DataEngineImpl.class.getName( ) );
	
	private IPreparedQueryService queryService;
	
	/**
	 * @param engine
	 * @param queryDefn
	 * @throws DataException
	 */
	PreparedQuery( DataEngineContext deContext, ExpressionCompiler exCompiler,
			Scriptable scope, IBaseQueryDefinition queryDefn, IPreparedQueryService queryService )
			throws DataException
	{
		logger.logp( Level.FINE,
				PreparedQuery.class.getName( ),
				"PreparedQuery",
				"PreparedQuery starts up." );
		assert queryDefn != null;

		this.exCompiler = exCompiler;
		this.deContext = deContext;
		this.sharedScope = scope;

		this.queryDefn = queryDefn;
		this.aggrTable = new AggregateTable( this.sharedScope, queryDefn );
		this.queryService = queryService;

		logger.fine( "Start to prepare a PreparedQuery." );
		prepare( );
		logger.fine( "Finished preparing the PreparedQuery." );
	}
	
	/**
	 * @param context
	 */
	protected void setAppContext( Map context )
	{
	    appContext = context;
	}
	
	/**
	 * Return the QueryResults. But the execution of query would be deferred
	 * 
	 * @param outerResults
	 *            If query is nested within another query, this is the outer
	 *            query's query result handle.
	 * @param scope
	 *            The ElementState object for the report item using the query;
	 *            this acts as the JS scope for evaluating script expressions.
	 */
	protected QueryResults doPrepare( IQueryResults outerResults,
			Scriptable scope, QueryExecutor executor,
			PreparedDataSourceQuery dataSourceQuery ) throws DataException
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
		
		// pass the prepared query's pass thru context to its executor
		executor.setAppContext( this.appContext );
		
		//here prepare the execution. After the preparation the result metadata is available by
		//calling getResultClass, and the query is ready for execution.
		logger.finer( "Start to prepare the execution." );
		executor.prepareExecution( outerResults, scope );
		logger.finer( "Finish preparing the execution." );
		
	    return new QueryResults( new QueryService( this.deContext,
				dataSourceQuery,
				queryService,
				executor,
				this.queryDefn ),
				executor.getQueryScope( ),
				executor.nestedLevel + 1 );
	}
	
	/**
	 * @throws DataException
	 */
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
	 * @param trans
	 * @param groupLevel
	 * @param cx
	 * @throws DataException
	 */
	private void prepareGroup( IBaseTransform trans, int groupLevel, Context cx )
		throws DataException
	{
		// prepare expressions appearing in this group
		prepareExpressions( trans.getAfterExpressions(), groupLevel, true, false,cx );
		prepareExpressions( trans.getBeforeExpressions(), groupLevel, false, false, cx );
		prepareExpressions( trans.getRowExpressions(),groupLevel, false, true, cx );
		
		// Prepare subqueries appearing in this group
		Collection subQueries = trans.getSubqueries( );
		Iterator subIt = subQueries.iterator( );
		while ( subIt.hasNext( ) )
		{
			ISubqueryDefinition subquery = (ISubqueryDefinition) subIt.next( );
			PreparedSubquery pq = new PreparedSubquery( this.deContext,
					this.exCompiler,
					this.sharedScope,
					subquery,
					queryService,
					groupLevel );
			subQueryMap.put( subquery.getName(), pq);
		}
	}
	
	/**
	 * Prepares all expressions in the given collection
	 * 
	 * @param expressions
	 * @param groupLevel
	 * @param afterGroup
	 * @param cx
	 */
	private void prepareExpressions( Collection expressions, int groupLevel,
			boolean afterGroup, boolean isDetailedRow, Context cx )
	{
	    if ( expressions == null )
	        return;
	    
	    AggregateRegistry reg = this.aggrTable.getAggrRegistry( groupLevel, afterGroup, isDetailedRow, cx );
	    Iterator it = expressions.iterator();
	    while ( it.hasNext() )
	    {
	        prepareExpression((IBaseExpression) it.next(), groupLevel, cx, reg);
	    }
	}
	
	/**
	 * Prepares one expression
	 * 
	 * @param expr
	 * @param groupLevel
	 * @param cx
	 * @param reg
	 */
	private void prepareExpression( IBaseExpression expr, int groupLevel,
			Context cx, AggregateRegistry reg )
	{
	    ExpressionCompiler compiler = this.exCompiler;
	    
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
	    	ce = transformConditionalExpression( ce );
			
	    	prepareExpression( ce.getExpression(), groupLevel, cx, reg );
	    	if ( ce.getOperand1() != null )
		    	prepareExpression( ce.getOperand1(), groupLevel, cx, reg );
	    	if ( ce.getOperand2() != null )
		    	prepareExpression( ce.getOperand2(), groupLevel, cx, reg );

	    	// No separate preparation is required for the conditional expression 
	    	// Set itself as the compiled handle
	    	expr.setHandle( ce );
	    	expr.setID( IDUtil.nextExprID( ) );
	    }
	    else
	    {
	    	// Should never get here
	    	assert false;
	    }
	}

	/**
	 * When a TopN/TopPercent/BottomN/BottomPercent ConditionalExpression is set, transform it to Total.TopN/
	 * Total.TopPercent/Total.BottomN/Total.BottomPercent aggregations with "isTrue" operator.
	 * 
	 * @param ce
	 * @return
	 */
	private IConditionalExpression transformConditionalExpression( IConditionalExpression ce )
	{
		String prefix = null;
		if ( ce.getOperator( ) == IConditionalExpression.OP_TOP_N )
		{
			prefix = "Total.isTopN";
		}
		if ( ce.getOperator( ) == IConditionalExpression.OP_TOP_PERCENT )
		{
			prefix = "Total.isTopPercent";
		}
		if ( ce.getOperator( ) == IConditionalExpression.OP_BOTTOM_N )
		{
			prefix = "Total.isBottomN";
		}
		if ( ce.getOperator( ) == IConditionalExpression.OP_BOTTOM_PERCENT )
		{
			prefix = "Total.isBottomPercent";
		}
		if( prefix != null )
		{
			ce = new ConditionalExpression( prefix+"("
					+ ce.getExpression( ).getText( ) + ","
					+ ce.getOperand1( ).getText( ) + ")",
					IConditionalExpression.OP_TRUE );
		}
		return ce;
	}
	
	/**
	 * Executes a subquery
	 * 
	 * @param iterator
	 * @param subQueryName
	 * @param subScope
	 * @return
	 * @throws DataException
	 */
	QueryResults execSubquery( IResultIterator iterator, String subQueryName,
			Scriptable subScope ) throws DataException
	{
		assert subQueryName != null;

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
		
		return subquery.execute( iterator, subScope );
	}
	
	/**
	 * Closes the prepared query. This instance can no longer be executed after
	 * it is closed 
	 * 
	 * TODO: expose this method in the IPreparedQuery interface
	 */
	public void close()
	{
		queryDefn = null;
		this.aggrTable = null;
		this.subQueryMap = null;
		logger.logp( Level.FINER,
				PreparedQuery.class.getName( ),
				"close",
				"Prepared query closed" );
		// TODO: close all open QueryResults obtained from this PreparedQuery
	}

	public Scriptable getSharedScope( )
	{
		return sharedScope;
	}

	protected IBaseQueryDefinition getBaseQueryDefn( )
	{
		return queryDefn;
	}

	protected AggregateTable getAggrTable( )
	{
		return aggrTable;
	}
		
}
