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

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefn;
import org.eclipse.birt.data.engine.api.IBaseTransform;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IGroupDefn;
import org.eclipse.birt.data.engine.api.IJSExpression;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.ISortDefn;
import org.eclipse.birt.data.engine.api.ISubqueryDefn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.ExpressionCompiler.AggregateRegistry;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IFilter;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/** 
 * Common implementation of a prepared report query or a subquery.
 */
abstract class PreparedQuery 
{
	protected IBaseQueryDefn 	queryDefn;
	protected DataEngineImpl	engine;
	protected AggrExprTable		aggrTable;
	private IQuery				odiQuery;
	
	// Map of Subquery name (String) to PreparedSubquery
	protected HashMap subQueryMap = new HashMap();
	
	PreparedQuery( DataEngineImpl engine, IBaseQueryDefn queryDefn )
		throws DataException
	{
	    assert engine != null && queryDefn != null;
		this.engine = engine;
		this.queryDefn = queryDefn;
		this.aggrTable = new AggrExprTable(this);
		
		prepare();
	}
	
	protected IQuery getOdiQuery()
	{
	    return odiQuery;
	}
	
	// Gets the OdiDataSource associated with this query
	abstract protected IDataSource getOdiDataSource();
	
	// Gets the DataSetDefn associated with this query
	abstract protected DataSetDefn getDataSet();
	
	// Gets the PreparedReportQuery instance associated with this query
	abstract protected PreparedReportQuery getReportQuery();
	
	// Gets the IBaseQueryDefn instance associated with this query
	IBaseQueryDefn getQueryDefn( )
	{
		return queryDefn;
	}
	
	// Executes the ODI query to reproduce a ODI result set
	abstract protected IResultIterator executeOdiQuery(
			IQueryResults outerResults, Scriptable scope ) throws DataException;

	// Prepares the ODI query
	protected void prepareOdiQuery() throws DataException, DataException
	{
	}
	
	// Add computed column data 
	protected void setComputedColumns( IResultIterator odiResult,
			Scriptable scope ) throws DataException
	{
	}
	
	// Creates an appropriate subclass of ODI IQuery 
	abstract protected IQuery createOdiQuery( ) throws DataException;
	
	// Gets the registry of all aggregate expression
	AggrExprTable getAggrTable()
	{
		return aggrTable;
	}
	
	// Common code to extract the name of a column from a JS expression which is 
	// in the form of "row.col". If expression is not in expected format, returns null
	private String getColNameFromJSExpr( Context cx, String expr )
	{
	    String colName = null;
		ExpressionCompiler compiler = engine.getExpressionCompiler();
	    try
	    {
	        CompiledExpression ce = compiler.compile( 
	                			expr, null, cx);
	        if ( ce instanceof DirectColRefExpr )
	        {
	            colName = ((DirectColRefExpr)ce).getColumnName();
	        }
	    }
	    catch ( DataException e )
	    {
	        // Assume this is compilation error; fall through and leave colName unset
	    }
	    return colName;
	}
	
	/**
	 * Populate a ODI query with this query's transform definitions
	 */
	protected void setQueryTransforms( IQuery odiQuery, Scriptable scope ) 
			throws DataException
	{
		assert odiQuery != null && scope != null && queryDefn != null;
		Context cx = Context.enter();
		try
		{
			// Set grouping
			List groups = queryDefn.getGroups();
			if ( groups != null && ! groups.isEmpty() )
			{
				IQuery.GroupSpec[] groupSpecs = new IQuery.GroupSpec[ groups.size() ];
				Iterator it = groups.iterator();
				for ( int i = 0; it.hasNext(); i++ )
				{
					IGroupDefn src = (IGroupDefn) it.next();
					IQuery.GroupSpec dest = groupDefnToSpec(cx, src);
					groupSpecs[i] = dest;
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
					ISortDefn src = (ISortDefn) it.next();
					String sortKey = src.getColumn();
					if ( sortKey == null || sortKey.length() == 0 )
					{
						// Group key expressed as expression; convert it to column name
						// TODO support key expression in the future by creating implicit
						// computed columns
						sortKey = getColNameFromJSExpr( cx, src.getExpression() );
					}
					IQuery.SortSpec dest = 	new IQuery.SortSpec( sortKey, 
								src.getSortDirection() == ISortDefn.SORT_ASC );
					sortSpecs[i] = dest;
				}
				odiQuery.setOrdering( Arrays.asList( sortSpecs));
			}
			
			// Set filtering
			// Need to create a JS row object to help with the filtering
			JSRowObject jsRowObj = new JSRowObject( );
		    scope.put( "row", scope, jsRowObj );

		    // set filter
		    List mergedFilters = new ArrayList( );
		    if ( queryDefn.getFilters( ) != null )
			{
				mergedFilters.addAll( queryDefn.getFilters( ) );
			}
		    // If query has a data set (i.e., query is not a subquery), merge in 
		    // the data set's filters
		    DataSetDefn ds = getDataSet();
		    
		    if ( ds != null &&
		    	 ds.getDesign( ).getFilters( ) != null )
			{
				mergedFilters.addAll( ds.getDesign( ).getFilters( ) );
			}
		    
		    if ( mergedFilters.size() > 0 )
		    {
				IFilter filter = new FilterByRow( mergedFilters, scope, jsRowObj );
				odiQuery.setFiltering( filter );
		    }
		}
		catch (DataException e)
		{
			throw e;
		}
		finally
		{
			Context.exit();
		}
	}
	
	/**
	 * Executes the query, handle data transforms and aggregation calculation. Sets
	 * the odiResult, aggrCalc and odiQuery fields.
	 * @param scope
	 * @return
	 */
	protected QueryResults doExecute(  IQueryResults outerResults,Scriptable scope ) throws DataException
	{
		// Create a new sub scope if none is provided
        // We need a separate scope within which to define our "row" object
		if ( scope == null )
	    {
			scope = DataEngineImpl.createSubscope( getDataEngine().getSharedScope() );
	    }

		Context cx = Context.enter();
		try
		{
			// Create, populate and prepare Odi query if first time
			if ( odiQuery == null )
			{
				odiQuery = createOdiQuery();
				setQueryTransforms( odiQuery, scope );
				prepareOdiQuery();
			}
			IResultIterator odiResult;
			// defer executing odi query till api getResultIterator
			odiResult = executeOdiQuery( outerResults, scope );

		    // Set up the Javascript "row" object and bind it to our result set
		    JSRowObject rowObj = new JSRowObject( odiResult );
		    scope.put( "row", scope, rowObj );
			//Set the Javascript "rows" object and bind it to our result
		    
			JSRows rowsObj = new JSRows( outerResults ,rowObj);
			scope.put( "rows", scope, rowsObj );
		    
		    // Add computed column data
		    this.setComputedColumns( odiResult, scope );

		    // Calculate aggregate values
		    AggrCalc aggrCalc = new AggrCalc( aggrTable, odiResult );
		    
		    // Set up the internal JS _aggr_value object and bind it to the aggregate calc engine
		    Scriptable aggrObj = aggrCalc.getJSAggrValueObject();
		    scope.put( ExpressionCompiler.AGGR_VALUE, scope, aggrObj );
		    
		    // Calculate aggregate values
		    aggrCalc.calculate(cx, scope);
		    
		    return new QueryResults( getReportQuery(), this, odiResult, aggrCalc, scope);
		}
		catch ( DataException e )
		{
			throw e;
		}
		finally
		{
			Context.exit();
		}
		
	}
	
	private void prepare( )	throws DataException
	{
	    // TODO - validation of static queryDefn

		Context cx = Context.enter();
		
		try
		{
			// Prepare all groups; note that the report query iteself
			// is treated as a group (with group level 0 )
			List groups = queryDefn.getGroups( );
			for ( int i = 0; i <= groups.size( ); i++ )
			{
				// Group 0
				IBaseTransform groupDefn;
				if ( i == 0 )
					groupDefn = queryDefn;
				else
					groupDefn = (IGroupDefn) groups.get( i - 1);
				prepareGroup( groupDefn, i, cx );
			}			
			
		}
		finally
		{
		    Context.exit();
		}
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
			ISubqueryDefn subquery = (ISubqueryDefn) subIt.next( );
			PreparedSubquery pq = new PreparedSubquery( subquery, this, groupLevel );
			subQueryMap.put( subquery.getName(), pq);
		}
	}
	
	/* Prepares all expressions in the given collection */
	private void prepareExpressions( Collection expressions, int groupLevel, 
			boolean afterGroup, Context cx )
		throws DataException
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
		throws DataException
	{
	    ExpressionCompiler compiler = this.engine.getExpressionCompiler();
	    
	    if ( expr instanceof IJSExpression )
	    {
	    	String exprText = ((IJSExpression) expr).getText();
	    	CompiledExpression handle = compiler.compile( exprText, reg, cx);
	    	expr.setHandle( handle );
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
	 */
	protected IQuery.GroupSpec groupDefnToSpec( Context cx, IGroupDefn src )
	{
		String groupKey = src.getKeyColumn();
		if ( groupKey == null || groupKey.length() == 0 )
		{
			// Group key expressed as expression; convert it to column name
			// TODO support key expression in the future by creating implicit
			// computed columns
			groupKey = getColNameFromJSExpr( cx, src.getKeyExpresion() );
		}
		IQuery.GroupSpec dest = new IQuery.GroupSpec( groupKey );
		dest.setName( src.getName() );
		dest.setInterval( src.getInterval());
		dest.setIntervalRange( src.getIntervalRange());
		dest.setIntervalStart( src.getIntervalStart());
		dest.setSortDirection( src.getSortDirection());
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
			throw new DataException( ResourceConstants.SUBQUERY_NOT_FOUND );
		
		return subquery.execute( iterator, scope );
	}
	
	public DataEngineImpl getDataEngine()
	{
		return engine;
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
			IGroupDefn group = (IGroupDefn) groups.get(i);
			if ( groupText.equals( group.getName()) ||
				 groupText.equals( group.getKeyColumn() ) ||
				 groupText.equals( group.getKeyExpresion()) )
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
	
}
