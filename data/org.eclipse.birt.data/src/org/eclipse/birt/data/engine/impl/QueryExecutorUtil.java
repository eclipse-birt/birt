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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ColumnReferenceExpression;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.mozilla.javascript.Context;

/**
 * 
 */
public final class QueryExecutorUtil
{
	/**
	 * NO instance
	 */
	private QueryExecutorUtil()
	{		
	}
	
	/**
	 * Convert IGroupDefn to IQuery.GroupSpec
	 * 
	 * @param cx
	 * @param src
	 * @return
	 * @throws DataException
	 */
	static IQuery.GroupSpec groupDefnToSpec( Context cx,
			IGroupDefinition src, String expr, String columnName, int index )
			throws DataException
	{
/*		int groupIndex = -1;
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
		{*/
			ColumnInfo groupKeyInfo = new ColumnInfo(index, columnName );
			int groupIndex = groupKeyInfo.getColumnIndex( );
			String groupKey = groupKeyInfo.getColumnName();
			boolean isComplexExpression = true;
		//}
		
		IQuery.GroupSpec dest = new IQuery.GroupSpec( groupIndex, groupKey );
		dest.setName( src.getName() );
		dest.setInterval( src.getInterval());
		dest.setIntervalRange( src.getIntervalRange());
		dest.setIntervalStart( src.getIntervalStart());
		dest.setSortDirection( src.getSortDirection( ) != ISortDefinition.SORT_DESC? 
								 ISortDefinition.SORT_ASC:ISortDefinition.SORT_DESC );
		//	dest.setSortDirection( src.getSortDirection());
		dest.setFilters( src.getFilters());
		if( src.getSorts( ).size( ) != 0)
		{
			dest.setSorts( src.getSorts() );
		}
		dest.setIsComplexExpression( isComplexExpression );
		return dest;
	}
	
	/**
	 * Convert IGroupDefn to IQuery.GroupSpec
	 * 
	 * @param cx
	 * @param src
	 * @return
	 * @throws DataException
	 * @deprecated
	 */
	static IQuery.GroupSpec subQueryGroupDefnToSpec( Context cx,
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
	 * @param groupSpecs
	 * @param i
	 */
	static int getTempComputedColumnType( int i )
	{
		int interval = i;
		if( interval == IGroupDefinition.DAY_INTERVAL 
			|| interval == IGroupDefinition.HOUR_INTERVAL
			|| interval == IGroupDefinition.MINUTE_INTERVAL
			|| interval == IGroupDefinition.SECOND_INTERVAL
			|| interval == IGroupDefinition.MONTH_INTERVAL
			|| interval == IGroupDefinition.QUARTER_INTERVAL
			|| interval == IGroupDefinition.YEAR_INTERVAL
			|| interval == IGroupDefinition.WEEK_INTERVAL
			|| interval == IGroupDefinition.NUMERIC_INTERVAL )
			interval = DataType.DOUBLE_TYPE;
		else if ( interval == IGroupDefinition.STRING_PREFIX_INTERVAL )
			interval = DataType.STRING_TYPE;
		else
			interval = DataType.ANY_TYPE;
		return interval;
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
	public static ColumnInfo getColInfoFromJSExpr( Context cx, String expr )
	{
		int colIndex = -1;
		String colName = null;
		CompiledExpression ce = ExpressionCompilerUtil.compile( expr, cx );
		if ( ce instanceof ColumnReferenceExpression )
		{
			ColumnReferenceExpression cre = ( (ColumnReferenceExpression) ce );
			colIndex = cre.getColumnindex( );
			colName = cre.getColumnName( );
		}
		return new ColumnInfo( colIndex, colName );
	}
	
}
