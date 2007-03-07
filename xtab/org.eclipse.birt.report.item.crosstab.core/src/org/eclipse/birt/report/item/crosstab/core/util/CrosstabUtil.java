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

package org.eclipse.birt.report.item.crosstab.core.util;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

/**
 * Utility clas for crosstab.
 */

public class CrosstabUtil
{

	/**
	 * 
	 * @param element
	 * @return report item if found, otherwise null
	 */
	public static IReportItem getReportItem( DesignElementHandle element )
	{
		if ( !( element instanceof ExtendedItemHandle ) )
			return null;
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) element;
		try
		{
			return extendedItem.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			return null;
		}
	}

	/**
	 * 
	 * @param element
	 * @param extensionName
	 * @return report item if found, otherwise null
	 */
	public static IReportItem getReportItem( DesignElementHandle element,
			String extensionName )
	{
		if ( !( element instanceof ExtendedItemHandle ) )
			return null;
		if ( extensionName == null )
			throw new IllegalArgumentException(
					"extension name can not be null" ); //$NON-NLS-1$
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) element;
		if ( extensionName.equals( extendedItem.getExtensionName( ) ) )
		{
			try
			{
				return extendedItem.getReportItem( );
			}
			catch ( ExtendedElementException e )
			{
				return null;
			}
		}
		return null;
	}

	/**
	 * Gets the opposite axis type for the given axis. If axis type is column,
	 * then return row; if axis type is row, then return column; otherwise
	 * return <code>ICrosstabConstants.NO_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 * @return
	 */
	public static int getOppositeAxisType( int axisType )
	{
		switch ( axisType )
		{
			case ICrosstabConstants.COLUMN_AXIS_TYPE :
				return ICrosstabConstants.ROW_AXIS_TYPE;
			case ICrosstabConstants.ROW_AXIS_TYPE :
				return ICrosstabConstants.COLUMN_AXIS_TYPE;
			default :
				return ICrosstabConstants.NO_AXIS_TYPE;
		}
	}

	/**
	 * Adjusts the measure aggregations when row/column dimension or level is
	 * changed.
	 * 
	 * @param crosstab
	 * @param axisType
	 *            the axis type where the dimension or level is changed or the
	 *            grand total is changed
	 * @param dimensionName
	 * @param levelName
	 * @param isLevelInnerMost
	 * @param isAdd
	 * @throws SemanticException
	 */
	public static void adjustMeasureAggregations(
			CrosstabReportItemHandle crosstab, int axisType,
			String dimensionName, String levelName, boolean isLevelInnerMost,
			boolean isAdd ) throws SemanticException
	{
		if ( crosstab == null
				|| ( axisType != ICrosstabConstants.ROW_AXIS_TYPE && axisType != ICrosstabConstants.COLUMN_AXIS_TYPE ) )
			return;
		int counterAxisType = CrosstabUtil.getOppositeAxisType( axisType );

		// justifies whether the counterAxis has no level and grand total
		boolean isCounterAxisEmpty = true;

		// add aggregations for all level views
		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );

			for ( int dimension = 0; dimension < crosstab
					.getDimensionCount( counterAxisType ); dimension++ )
			{
				DimensionViewHandle dimensionView = crosstab.getDimension(
						counterAxisType, dimension );
				for ( int level = 0; level < dimensionView.getLevelCount( ); level++ )
				{
					// one level exists in this crosstab, then set
					// isCounterAxisEmpty to false
					isCounterAxisEmpty = false;

					LevelViewHandle levelView = dimensionView.getLevel( level );
					String rowDimension = null;
					String rowLevel = null;
					String colDimension = null;
					String colLevel = null;
					if ( counterAxisType == ICrosstabConstants.ROW_AXIS_TYPE )
					{
						rowDimension = dimensionView.getCubeDimensionName( );
						rowLevel = levelView.getCubeLevelName( );
						colDimension = dimensionName;
						colLevel = levelName;
					}
					else if ( counterAxisType == ICrosstabConstants.COLUMN_AXIS_TYPE )
					{
						rowDimension = dimensionName;
						rowLevel = levelName;
						colDimension = dimensionView.getCubeDimensionName( );
						colLevel = levelView.getCubeLevelName( );
					}

					// if 'isLevelInnerMost' is true, then add aggregation for
					// those not innermost and has aggregation levels in counter
					// axis; otherwise 'isLevelInnerMost' is false, then add
					// aggregation for those is innermost or has aggregation
					// levels in counter axis
					if ( ( isLevelInnerMost && !levelView.isInnerMost( ) && levelView
							.getAggregationHeader( ) != null )
							|| ( !isLevelInnerMost && ( levelView.isInnerMost( ) || levelView
									.getAggregationHeader( ) != null ) ) )
					{
						if ( isAdd )
							measureView.addAggregation( rowDimension, rowLevel,
									colDimension, colLevel );
						else
							measureView.removeAggregation( rowDimension,
									rowLevel, colDimension, colLevel );
					}
				}
			}

			// add aggregation for crosstab grand total; or there is no levels
			// and no grand total, we still need to add one aggregation
			if ( crosstab.getGrandTotal( counterAxisType ) != null
					|| isCounterAxisEmpty )
			{
				String rowDimension = null;
				String rowLevel = null;
				String colDimension = null;
				String colLevel = null;
				if ( counterAxisType == ICrosstabConstants.ROW_AXIS_TYPE )
				{
					colDimension = dimensionName;
					colLevel = levelName;
				}
				else if ( counterAxisType == ICrosstabConstants.COLUMN_AXIS_TYPE )
				{
					rowDimension = dimensionName;
					rowLevel = levelName;
				}
				if ( isAdd )
					measureView.addAggregation( rowDimension, rowLevel,
							colDimension, colLevel );
				else
					measureView.removeAggregation( rowDimension, rowLevel,
							colDimension, colLevel );
			}
		}
	}

	/**
	 * Gets the count of all the levels in all the dimension views at the given
	 * axis type.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @return
	 */
	public static int getAllLevelCount( CrosstabReportItemHandle crosstab,
			int axisType )
	{
		if ( crosstab == null )
			return 0;
		int count = 0;
		for ( int i = 0; i < crosstab.getDimensionCount( axisType ); i++ )
		{
			DimensionViewHandle dimensionView = crosstab.getDimension(
					axisType, i );
			count += dimensionView.getLevelCount( );
		}

		return count;
	}

	/**
	 * Gets the preceding level in the crosstab.
	 * 
	 * @param levelView
	 *            the level view to search the preceding one
	 * @return the preceding leve for the given if found, otherwise null
	 */
	public static LevelViewHandle getPrecedingLevel( LevelViewHandle levelView )
	{
		if ( levelView == null )
			return null;

		// such the preceding one in the same dimension
		DimensionViewHandle dimensionView = (DimensionViewHandle) levelView
				.getContainer( );
		if ( dimensionView == null )
			return null;
		int index = levelView.getIndex( );
		if ( index - 1 >= 0 )
			return dimensionView.getLevel( index - 1 );

		// such the last one in the preceding dimension
		CrosstabViewHandle crosstabView = (CrosstabViewHandle) dimensionView
				.getContainer( );
		if ( crosstabView == null )
			return null;
		index = dimensionView.getIndex( );
		for ( int i = index - 1; i >= 0; i-- )
		{
			dimensionView = crosstabView.getDimension( i );
			int levelCount = dimensionView.getLevelCount( );
			if ( levelCount > 0 )
				return dimensionView.getLevel( levelCount - 1 );
		}

		return null;

	}
}
