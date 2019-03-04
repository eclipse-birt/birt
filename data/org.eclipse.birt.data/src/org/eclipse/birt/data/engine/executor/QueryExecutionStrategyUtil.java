/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.FilterUtil;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.PreparedQueryUtil;
import org.eclipse.birt.data.engine.impl.SortingOptimizer;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

/**
 * 
 * @author Work
 *
 */
public final class QueryExecutionStrategyUtil
{
	/**
	 * 
	 * @author Work
	 *
	 */
	public static enum Strategy {
		SimpleLookingFoward, SimpleNoLookingFoward, Complex
	}

	/**
	 * 
	 * @param query
	 * @param dataSet
	 * @return
	 * @throws DataException
	 */
	public static Strategy getQueryExecutionStrategy( DataEngineSession session, IQueryDefinition query,
			IBaseDataSetDesign dataSet ) throws DataException
	{
		/*if ( session.getEngineContext( ).getDataEngineOption( ) > 4 )
			return Strategy.Complex;*/
		
		SortingOptimizer opt = new SortingOptimizer( dataSet, query );

		if ( session.getEngineContext( ).getMode( ) == DataEngineContext.MODE_UPDATE )
			return Strategy.Complex;
		if ( query.getGroups( ) != null && query.getGroups( ).size( ) > 0 )
		{
			for( IGroupDefinition group : (List<IGroupDefinition>) query.getGroups( ) )
			{
				if ( group.getSubqueries( ) != null
						&& group.getSubqueries( ).size( ) > 0 )
					return Strategy.Complex;
				if ( !isDirectColumnRefGroupKey( group, query ) )
					return Strategy.Complex;
				if( group.getFilters( ).isEmpty( ) && group.getSorts( ).isEmpty( ) && !query.getQueryExecutionHints( ).doSortBeforeGrouping( ))
					continue;
				if( opt.acceptGroupSorting( ) )
				{
					continue;
				}
				return Strategy.Complex;
			}
		}

		if ( query.getFilters( ) != null && query.getFilters( ).size( ) > 0 )
		{
			if ( FilterUtil.hasMutipassFilters( query.getFilters( ) ) )
				return Strategy.Complex;
			
			Set<String> bindings = new HashSet<String>();			
			for( Object filter : query.getFilters())
			{
				IBaseExpression baseExpr = ((IFilterDefinition)filter).getExpression();
				if( ExpressionCompilerUtil.hasAggregationInExpr( baseExpr ))
					return Strategy.Complex;
				bindings.addAll(ExpressionCompilerUtil.extractColumnExpression( baseExpr, ExpressionUtil.ROW_INDICATOR ));
				
				//TODO: support progressive viewing on viewing time filter
				if( ((IFilterDefinition)filter).updateAggregation() == false )
					return Strategy.Complex;
			}
			
			if (PreparedQueryUtil.existAggregationBinding(bindings,
					query.getBindings())) 
			{
				return Strategy.Complex;
			}
		}

		if ( query.getSorts( ) != null && query.getSorts( ).size( ) > 0 )
		{
			if( !opt.acceptQuerySorting( ) )
				return Strategy.Complex;
		}

		if ( query.getSubqueries( ) != null
				&& query.getSubqueries( ).size( ) > 0 )
			return Strategy.Complex;

		if( !query.usesDetails( ) )
		{
			return Strategy.Complex;
		}

		boolean hasAggregation = false;
		
		if ( query.getBindings( ) != null )
		{
			Iterator bindingIt = query.getBindings( ).values( ).iterator( );

			while ( bindingIt.hasNext( ) )
			{
				IBinding binding = (IBinding) bindingIt.next( );
				if ( binding.getAggrFunction( ) != null )
				{
					hasAggregation = true;
					IAggrFunction aggr = AggregationManager.getInstance().getAggregation(binding.getAggrFunction());
					if( aggr!= null && aggr.getNumberOfPasses() > 1 )
					{
						return Strategy.Complex;
					}
					
					//TODO:Enhance me
					List exprs = new ArrayList();
					exprs.addAll(binding.getArguments());
					if( binding.getExpression()!= null )
						exprs.add(binding.getExpression());
					for( int i = 0; i < exprs.size(); i++ )
					{
						Object expr = exprs.get(i);
						if( !(expr instanceof IScriptExpression) )
						{
							return Strategy.Complex;
						}
						
						IScriptExpression scriptExpr = (IScriptExpression)expr;
						try
						{
							List<IColumnBinding> columnExprs = ExpressionUtil.extractColumnExpressions( scriptExpr.getText() );
							for( IColumnBinding temp : columnExprs )
							{
								Object obj = query.getBindings().get( temp.getResultSetColumnName());
								if( obj instanceof IBinding )
								{
									IBinding bindingObj = (IBinding)obj;
									if( bindingObj.getAggrFunction()!= null )
										return Strategy.Complex;
									
									IBaseExpression baseExpr = ((IBinding) obj).getExpression();
									if( baseExpr instanceof IScriptExpression )
									{
										String cb = ExpressionUtil.getColumnName(((IScriptExpression)baseExpr).getText());
										if( ScriptEvalUtil.compare(bindingObj.getBindingName(), cb)!= 0)
											return Strategy.Complex;
									}
								}
							}
						}
						catch( BirtException e )
						{
							return Strategy.Complex;
						}
					}
				}

				if ( ExpressionCompilerUtil.hasAggregationInExpr( binding.getExpression( ) ))
				{
					return Strategy.Complex;
				}
			}
		}
		if ( dataSet != null )
		{
			if ( dataSet.getFilters( ) != null )
			{
				if ( FilterUtil.hasMutipassFilters( dataSet.getFilters( ) ) )
				{
					return Strategy.Complex;
				}
				
				for( Object filter : dataSet.getFilters())
				{
					IBaseExpression baseExpr = ((IFilterDefinition)filter).getExpression();
					if( ExpressionCompilerUtil.hasAggregationInExpr( baseExpr ))
						return Strategy.Complex;
					
					if( ((IFilterDefinition)filter).updateAggregation() == false )
						return Strategy.Complex;
				}
			}

			if ( dataSet.needDistinctValue( ) )
				return Strategy.Complex;

			if ( dataSet.getComputedColumns( ) != null )
			{
				List computedColumns = dataSet.getComputedColumns( );
				for ( int i = 0; i < computedColumns.size( ); i++ )
				{
					IComputedColumn computedColumn = (IComputedColumn) computedColumns.get( i );
					if ( computedColumn.getAggregateFunction( ) != null )
						return Strategy.Complex;
					if ( computedColumn.getExpression( ) instanceof IScriptExpression )
					{
						if ( ExpressionUtil.hasAggregation( ( (IScriptExpression) computedColumn.getExpression( ) ).getText( ) ) )
						{
							return Strategy.Complex;
						}
					}
				}
			}
		}

		return hasAggregation?Strategy.SimpleLookingFoward:Strategy.SimpleNoLookingFoward;
	}
	
	private static boolean isDirectColumnRefGroupKey(IGroupDefinition group,IQueryDefinition query )
	{
		String expr = getGroupKeyExpression(group);
		String dataSetExpr;
		try
		{
			dataSetExpr = getDataSetExpr( expr,query );
		}
		catch (DataException e)
		{
			dataSetExpr = null;
		}
		try
		{
			if( dataSetExpr != null && ExpressionUtil.getColumnName( dataSetExpr ) == null && ExpressionUtil.getColumnBindingName(dataSetExpr) == null)
			{
				return false;
			}
		}
		catch (BirtException e)
		{
			return false;
		}
		return true;
	}
	
	
	private static String getGroupKeyExpression(IGroupDefinition src) 
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

	private static String getColumnRefExpression( String expr )
	{
		return ExpressionUtil.createJSRowExpression( expr );
	}
	
	private static String getDataSetExpr( String rowExpr,IQueryDefinition query ) throws DataException
	{
		String dataSetExpr = null ;
		try
		{
			String bindingName = ExpressionUtil.getColumnBindingName( rowExpr );
			Object binding = query.getBindings( ).get( bindingName );
			if( binding != null )
			{
				IBaseExpression expr = ( (IBinding) binding ).getExpression( );
				if( expr != null && expr instanceof IScriptExpression )
				{
					dataSetExpr = ( ( IScriptExpression )expr ).getText( );
				}
			}
			return dataSetExpr;
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
	}
}
