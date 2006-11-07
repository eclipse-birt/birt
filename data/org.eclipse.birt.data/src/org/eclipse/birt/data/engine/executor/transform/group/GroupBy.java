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

package org.eclipse.birt.data.engine.executor.transform.group;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;

/**
 * To every column which has group property, a GroupBy instance will be
 * generated to do group judgment which supports regarding distinction as
 * standard or not within an interval range as another standard.
 */

public abstract class GroupBy
{

	private int columnIndex;
	private String columnName;
	private GroupSpec groupSpec;

	/**
	 * Static method to create and instance of subclass of GroupBy, based on the
	 * group definition
	 * 
	 * @param groupDefn
	 * @param columnIndex
	 * @param columnType
	 * @return GroupBy
	 * @throws DataException
	 */
	public static GroupBy newInstance( GroupSpec groupDefn, int columnIndex, String columnName,
			Class columnType ) throws DataException
	{
		assert groupDefn != null;

		GroupBy groupBy = null;
		
		switch ( groupDefn.getInterval( ) )
		{
			case GroupSpec.NO_INTERVAL :
				groupBy = new GroupByDistinctValue( );
				break;
			case GroupSpec.STRING_PREFIX_INTERVAL :
				if ( isString( columnType ) )
					groupBy = new GroupByStringRange( groupDefn );
				else
					throw new DataException( ResourceConstants.BAD_GROUP_INTERVAL_TYPE,
							new Object[]{
									"string prefix", columnType.getName( )
							} );
				break;
			default :
			{
				if ( groupDefn.getIntervalRange( ) == 0 )
					groupBy = new GroupByDistinctValue( );
				else
					groupBy = new GroupByNumberRange( groupDefn );
				break;
			}
		}

		groupBy.groupSpec = groupDefn;
		groupBy.columnIndex = columnIndex;
		groupBy.columnName = columnName;

		return groupBy;
	}

	/**
	 * Static method to create and instance of subclass of GroupBy, based on the
	 * group definition
	 * 
	 * @param groupDefn
	 * @return GroupBy
	 * @throws DataException
	 */
	public static GroupBy newInstanceForRowID( GroupSpec groupDefn )
			throws DataException
	{
		assert groupDefn != null;

		GroupBy groupBy = null;
		switch ( groupDefn.getInterval( ) )
		{
			case GroupSpec.NO_INTERVAL :
				groupBy = new GroupByDistinctValue( );
				break;
			case GroupSpec.NUMERIC_INTERVAL :
				groupBy = new GroupByPositionRange( groupDefn );
				break;
			default :
				throw new DataException( ResourceConstants.BAD_GROUP_INTERVAL_TYPE_ROWID );
		}

		groupBy.groupSpec = groupDefn;
		groupBy.columnIndex = -1;

		return groupBy;
	}

	/**
	 * Determines if the current group key is in the same group as the key value
	 * provided in the last call. This method can be overrided in special case,
	 * for example, group on row position.
	 * 
	 * @param currentGroupKey
	 * @param previousGroupKey
	 * @param groupStartValue
	 * @param currRowPos
	 * @return boolean value
	 * @throws DataException
	 */
	public boolean isInSameGroup( Object currentGroupKey, Object previousGroupKey,
			Object groupStartValue, int currRowPos ) throws DataException
	{
		return isInSameGroup( currentGroupKey, previousGroupKey );
	}

	/**
	 * Determines if the current group key is in the same group as the key value
	 * provided in the last call
	 * 
	 * @param currentGroupKey
	 * @param previousGroupKey
	 * @return boolean
	 * @throws DataException
	 */
	public boolean isInSameGroup( Object currentGroupKey, Object previousGroupKey )
			throws DataException
	{
		if ( previousGroupKey == currentGroupKey )
			return true;

		if ( previousGroupKey == null || currentGroupKey == null )
			return false;

		return isSameGroup( currentGroupKey, previousGroupKey );
	}

	/**
	 * If the currentGroupKey and previousGroupKey is not null, compare these
	 * two key values
	 * 
	 * @param currentGroupKey
	 * @param previousGroupKey
	 * @return boolean
	 * @throws DataException
	 */
	abstract boolean isSameGroup( Object currentGroupKey,
			Object previousGroupKey ) throws DataException;

	/**
	 * Returun whether start group value is needed
	 * 
	 * @return yes, start group value is needed
	 */
	boolean needsGroupStartValue( )
	{
		return false;
	}

	/**
	 * Gets the index of the column to group by
	 */
	int getColumnIndex( )
	{
		return columnIndex;
	}
	
	String getColumnName( )
	{
		return columnName;
	}

	/**
	 * Gets the GroupSpec associated with this group by
	 */
	GroupSpec getGroupSpec( )
	{
		return groupSpec;
	}

/*	private static boolean isNumber( Class columnType )
	{
		return Number.class.isAssignableFrom( columnType );
	}

	private static boolean isDate( Class columnType )
	{
		return Date.class.isAssignableFrom( columnType );
	}
*/	
	private static boolean isString( Class columnType )
	{
		return String.class.isAssignableFrom( columnType );
	}

	

}