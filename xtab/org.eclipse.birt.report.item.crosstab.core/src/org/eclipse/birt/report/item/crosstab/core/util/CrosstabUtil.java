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

public class CrosstabUtil implements ICrosstabConstants
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
			case COLUMN_AXIS_TYPE :
				return ROW_AXIS_TYPE;
			case ROW_AXIS_TYPE :
				return COLUMN_AXIS_TYPE;
			default :
				return NO_AXIS_TYPE;
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
				|| ( axisType != ROW_AXIS_TYPE && axisType != COLUMN_AXIS_TYPE ) )
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
					if ( counterAxisType == ROW_AXIS_TYPE )
					{
						rowDimension = dimensionView.getCubeDimensionName( );
						rowLevel = levelView.getCubeLevelName( );
						colDimension = dimensionName;
						colLevel = levelName;
					}
					else if ( counterAxisType == COLUMN_AXIS_TYPE )
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
				if ( counterAxisType == ROW_AXIS_TYPE )
				{
					colDimension = dimensionName;
					colLevel = levelName;
				}
				else if ( counterAxisType == COLUMN_AXIS_TYPE )
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
	 * Justifies whether the given axis type is valid.
	 * 
	 * @param axisType
	 * @return true if axis type is valid, otherwise false
	 */
	public static boolean isValidAxisType( int axisType )
	{
		if ( axisType == COLUMN_AXIS_TYPE || axisType == ROW_AXIS_TYPE )
			return true;
		return false;
	}

	/**
	 * Adjust measure aggregations for the given two level views.
	 * 
	 * @param crosstab
	 *            the crosstab where the leve views reside
	 * @param leftDimension
	 *            the first dimension name
	 * @param leftLevel
	 *            the first level name
	 * @param axisType
	 *            the row/column axis type for the first level view
	 * @param rightDimension
	 *            the second dimension name
	 * @param rightLevel
	 *            the second level name
	 * @param isAdd
	 *            true if add aggregation, otherwise false
	 * @throws SemanticException
	 */
	public static void adjustMeasureAggregations(
			CrosstabReportItemHandle crosstab, String leftDimension,
			String leftLevel, int axisType, String rightDimension,
			String rightLevel, boolean isAdd ) throws SemanticException
	{
		if ( crosstab == null || !isValidAxisType( axisType ) )
			return;

		String rowDimension = null;
		String rowLevel = null;
		String colDimension = null;
		String colLevel = null;

		if ( axisType == ROW_AXIS_TYPE )
		{
			rowDimension = leftDimension;
			rowLevel = leftLevel;

			colDimension = rightDimension;
			colLevel = rightLevel;
		}
		else if ( axisType == COLUMN_AXIS_TYPE )
		{
			rowDimension = rightDimension;
			rowLevel = rightLevel;

			colDimension = leftDimension;
			colLevel = leftLevel;
		}
		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );

			if ( isAdd )
				measureView.addAggregation( rowDimension, rowLevel,
						colDimension, colLevel );
			else
				measureView.removeAggregation( rowDimension, rowLevel,
						colDimension, colLevel );
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

	/**
	 * Gets the innermost level view in the crosstab.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @return
	 */
	public static LevelViewHandle getInnerMostLevel(
			CrosstabReportItemHandle crosstab, int axisType )
	{
		if ( crosstab == null )
			return null;

		for ( int dimensionIndex = crosstab.getDimensionCount( axisType ) - 1; dimensionIndex >= 0; dimensionIndex-- )
		{
			DimensionViewHandle dimensionView = crosstab.getDimension(
					axisType, dimensionIndex );
			for ( int levelIndex = dimensionView.getLevelCount( ) - 1; levelIndex >= 0; levelIndex-- )
			{
				return dimensionView.getLevel( levelIndex );
			}
		}

		return null;
	}

	/**
	 * 
	 * @param crosstab
	 * @param levelView
	 * @param dimensionName
	 * @param levelName
	 * @param axisType
	 * @param isAdd
	 * @throws SemanticException
	 */
	public static void adjustForLevelView( CrosstabReportItemHandle crosstab,
			LevelViewHandle levelView, String dimensionName, String levelName,
			int axisType, boolean isAdd ) throws SemanticException
	{
		if ( crosstab == null || levelView == null
				|| !isValidAxisType( axisType ) )
			return;

		// int axisType = getAxisType( );
		if ( levelView.isInnerMost( ) )
		{
			// if originally there is no levels and grand total,
			// then remove the aggregations for the axis type and
			// the counter axis level aggregations
			if ( CrosstabUtil.getAllLevelCount( crosstab, axisType ) <= 1 )
			{
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				CrosstabUtil.adjustMeasureAggregations( crosstab, axisType,
						dimensionName, levelName, true, isAdd );
				if ( crosstab.getGrandTotal( axisType ) == null )
					CrosstabUtil.adjustMeasureAggregations( crosstab, axisType,
							null, null, false, !isAdd );

			}
			else
			{
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				CrosstabUtil.adjustMeasureAggregations( crosstab, axisType,
						dimensionName, levelName, true, isAdd );
				// add one aggregation: the original innermost level
				// before this level is added and the innermost
				// level in the counter axis if the orginal
				// innermost has aggregation header
				LevelViewHandle precedingLevel = CrosstabUtil
						.getPrecedingLevel( levelView );
				assert precedingLevel != null;
				if ( precedingLevel.getAggregationHeader( ) != null )
				{
					CrosstabUtil.adjustMeasureAggregations( crosstab,
							dimensionName, levelName, axisType,
							( (DimensionViewHandle) precedingLevel
									.getContainer( ) ).getCubeDimensionName( ),
							precedingLevel.getCubeLevelName( ), isAdd );
				}
			}
		}
		else
		{
			// if the added level view is not innermost and has
			// aggregation header, then add aggregations for this
			// level view and all counterpart axis levels and grand
			// total
			if ( levelView.getAggregationHeader( ) != null )
			{
				CrosstabUtil.adjustMeasureAggregations( crosstab, axisType,
						dimensionName, levelName, false, isAdd );

			}
		}
	}
}
