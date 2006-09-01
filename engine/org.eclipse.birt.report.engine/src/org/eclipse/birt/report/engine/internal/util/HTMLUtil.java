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

package org.eclipse.birt.report.engine.internal.util;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.TableHandle;


public class HTMLUtil
{
	/**
	 * Generates descrition text for a filter condition.
	 * 
	 * @param filterCondition
	 *            the filter condition.
	 * @return description text.
	 */
	public static String getFilterDescription( FilterConditionHandle filterCondition )
	{
		if ( filterCondition == null )
		{
			return null;
		}
		char space =  ' ';
		StringBuffer result = new StringBuffer( );
		result.append( filterCondition.getExpr( ) );
		result.append( space );
		result.append( filterCondition.getOperator( ) );
		String operator1 = filterCondition.getValue1( );
		String operator2 = filterCondition.getValue2( );
		if ( operator1 != null )
		{
			result.append( space );
			result.append( operator1 );
		}
		if ( operator2 != null )
		{
			if ( operator1 != null )
			{
				result.append( " or " ); //$NON-NLS-1$
			}
			result.append( space );
			result.append( operator2 );
		}
		return result.toString( );
	}

	/**
	 * Generates description text for the filters of a column which contains the
	 * specified cell.
	 * 
	 * @param cell
	 *            the cell.
	 * @return the description text.
	 */
	public static String getColumnFilterText( ICellContent cell )
	{
		List filterConditions = getFilterConditions( cell );
		StringBuffer conditionString = new StringBuffer( );
		for ( int i = 0; i < filterConditions.size( ); i++)
		{
			if ( i != 0 )
			{
				conditionString.append( ';' );
			}
			FilterConditionHandle condition = (FilterConditionHandle) filterConditions
					.get( i );
			conditionString.append( HTMLUtil.getFilterDescription( condition ) );
		}
		return conditionString.toString( );
	}

	/**
	 * Gets filter conditions of the column which contains the specified cell.
	 * 
	 * @param cell
	 *            the cell.
	 * @return the column filter conditions. Empty list is returned when the
	 *         column has no filter conditions.
	 */
	public static List getFilterConditions( ICellContent cell )
	{
		IRowContent row = (IRowContent) cell.getParent( );
		ITableContent table = row.getTable( );
		List filters = null;
		if ( table != null )
		{
			Object genBy = table.getGenerateBy( );
			if ( genBy instanceof TableItemDesign )
			{
				TableHandle tableHandle = (TableHandle) ( (TableItemDesign) genBy )
						.getHandle( );
				filters = tableHandle.getFilters( cell.getColumn( ) );
			}
		}
		return filters == null ? Collections.EMPTY_LIST : filters;
	}

	/**
	 * Gets group level of a cell content.
	 * 
	 * @param cellContent
	 *            the cell content.
	 * @return group level of the cell content.
	 */
	public static int getGroupLevel( ICellContent cellContent )
	{
		IRowContent row = (IRowContent) cellContent.getParent( );
		return getGroupLevel( row );
	}

	/**
	 * Gets group level of a row content.
	 * 
	 * @param rowContent
	 *            the row content
	 * @return group level of the row contnet.
	 */
	public static int getGroupLevel( IRowContent rowContent )
	{
		IGroupContent group = rowContent.getGroup( );
		IBandContent band = rowContent.getBand( );
		if ( group != null && band != null )
		{
			int bandType = band.getBandType( );
			if ( bandType == IBandContent.BAND_DETAIL )
			{
				return group.getGroupLevel( ) + 2;
			}
			else if ( bandType == IBandContent.BAND_GROUP_HEADER
					|| bandType == IBandContent.BAND_GROUP_FOOTER )
			{
				return group.getGroupLevel( ) + 1;
			}
		}
		return -1;
	}
}
