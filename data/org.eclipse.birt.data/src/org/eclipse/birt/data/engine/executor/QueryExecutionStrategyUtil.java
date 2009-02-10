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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.FilterUtil;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;

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
		Simple, Complex
	}

	/**
	 * 
	 * @param query
	 * @param dataSet
	 * @return
	 * @throws DataException
	 */
	public static Strategy getQueryExecutionStrategy( IQueryDefinition query,
			IBaseDataSetDesign dataSet ) throws DataException
	{
		if ( query.getGroups( ) != null && query.getGroups( ).size( ) > 0 )
			return Strategy.Complex;

		if ( query.getFilters( ) != null && query.getFilters( ).size( ) > 0 )
		{
			//if ( FilterUtil.hasMutipassFilters( query.getFilters( ) ) )
				return Strategy.Complex;
		}

		if ( query.getSorts( ) != null && query.getSorts( ).size( ) > 0 )
			return Strategy.Complex;

		if ( query.getSubqueries( ) != null
				&& query.getSubqueries( ).size( ) > 0 )
			return Strategy.Complex;
		
		if( !query.usesDetails( ) )
		{
			return Strategy.Complex;
		}

		if ( query.getBindings( ) != null )
		{
			Iterator bindingIt = query.getBindings( ).values( ).iterator( );

			while ( bindingIt.hasNext( ) )
			{
				IBinding binding = (IBinding) bindingIt.next( );
				if ( binding.getAggrFunction( ) != null )
					return Strategy.Complex;

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

		return Strategy.Simple;
	}
}
