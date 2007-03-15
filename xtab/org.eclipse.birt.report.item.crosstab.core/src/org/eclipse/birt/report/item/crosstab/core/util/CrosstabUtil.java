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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.IAggregationCellConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

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
	 * @param theLevelView
	 * @param dimensionName
	 * @param levelName
	 * @param isLevelInnerMost
	 * @param isAdd
	 * @throws SemanticException
	 */
	public static void adjustMeasureAggregations(
			CrosstabReportItemHandle crosstab, int axisType,
			LevelViewHandle theLevelView, String dimensionName,
			String levelName, boolean isLevelInnerMost, boolean isAdd )
			throws SemanticException
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
					|| ( isCounterAxisEmpty && ( theLevelView == null || theLevelView
							.getAggregationHeader( ) != null ) ) )
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
	 * @param measures
	 * @param function
	 * @param isAdd
	 *            true if add aggregation, otherwise false
	 * @throws SemanticException
	 */
	public static void addMeasureAggregations(
			CrosstabReportItemHandle crosstab, String leftDimension,
			String leftLevel, int axisType, String rightDimension,
			String rightLevel, List measures, String function )
			throws SemanticException
	{
		if ( crosstab == null || !isValidAxisType( axisType )
				|| measures == null )
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
		for ( int i = 0; i < measures.size( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );
			if ( measureView.getCrosstab( ) != crosstab )
				continue;
			addDataItem( crosstab, measureView, function, rowDimension,
					rowLevel, colDimension, colLevel );
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
			if ( getAllLevelCount( crosstab, axisType ) <= 1 )
			{
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				adjustMeasureAggregations( crosstab, axisType, levelView,
						dimensionName, levelName, true, isAdd );
				if ( crosstab.getGrandTotal( axisType ) == null )
					adjustMeasureAggregations( crosstab, axisType, levelView,
							null, null, false, !isAdd );

			}
			else
			{
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				adjustMeasureAggregations( crosstab, axisType, levelView,
						dimensionName, levelName, true, isAdd );
				// add one aggregation: the original innermost level
				// before this level is added and the innermost
				// level in the counter axis if the orginal
				// innermost has aggregation header
				LevelViewHandle precedingLevel = getPrecedingLevel( levelView );
				assert precedingLevel != null;
				if ( precedingLevel.getAggregationHeader( ) != null )
				{
					adjustMeasureAggregations( crosstab, dimensionName,
							levelName, axisType,
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
				adjustMeasureAggregations( crosstab, axisType, levelView,
						dimensionName, levelName, false, isAdd );

			}
		}
	}

	/**
	 * 
	 * @param levelView
	 * @param function
	 * @param measures
	 * @return
	 * @throws SemanticException
	 */
	public static CrosstabCellHandle addAggregationHeader(
			LevelViewHandle levelView, String function, List measures )
			throws SemanticException
	{
		if ( levelView == null )
			return null;

		// can not add aggregation if this level is innermost
		if ( levelView.isInnerMost( ) )
		{
			levelView
					.getLogger( )
					.log(
							Level.WARNING,
							"This level: [" + levelView.getModelHandle( ).getName( ) + "] can not add aggregation for it is innermost" ); //$NON-NLS-1$//$NON-NLS-2$
			return null;
		}
		if ( levelView.getAggregationHeader( ) != null )
		{
			levelView.getLogger( ).log( Level.INFO,
					"the aggregation header is set" ); //$NON-NLS-1$
			return levelView.getAggregationHeader( );
		}

		CommandStack stack = levelView.getCommandStack( );
		stack.startTrans( null );
		try
		{
			levelView.getAggregationHeaderProperty( ).add(
					CrosstabExtendedItemFactory.createCrosstabCell( levelView
							.getModuleHandle( ) ) );

			// adjust the measure aggregations
			CrosstabReportItemHandle crosstab = levelView.getCrosstab( );
			if ( crosstab != null && measures != null )
			{
				addMeasureAggregations( crosstab, levelView.getAxisType( ),
						levelView, ( (DimensionViewHandle) levelView
								.getContainer( ) ).getCubeDimensionName( ),
						levelView.getCubeLevelName( ), false, function,
						measures );
			}
		}
		catch ( SemanticException e )
		{
			levelView.getLogger( ).log( Level.WARNING, e.getMessage( ), e );
			stack.rollback( );
			throw e;
		}

		stack.commit( );
		return levelView.getAggregationHeader( );
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
	private static void addMeasureAggregations(
			CrosstabReportItemHandle crosstab, int axisType,
			LevelViewHandle theLevelView, String dimensionName,
			String levelName, boolean isInnerMost, String function,
			List measures ) throws SemanticException
	{
		if ( crosstab == null || !isValidAxisType( axisType )
				|| measures == null || measures.isEmpty( ) )
			return;
		int counterAxisType = CrosstabUtil.getOppositeAxisType( axisType );

		// justifies whether the counterAxis has no level and grand total
		boolean isCounterAxisEmpty = true;

		// add aggregations for all level views
		for ( int i = 0; i < measures.size( ); i++ )
		{
			MeasureViewHandle measureView = (MeasureViewHandle) measures
					.get( i );
			if ( measureView.getCrosstab( ) != crosstab )
				continue;

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
					if ( ( isInnerMost && !levelView.isInnerMost( ) && levelView
							.getAggregationHeader( ) != null )
							|| ( !isInnerMost && ( levelView.isInnerMost( ) || levelView
									.getAggregationHeader( ) != null ) ) )
					{
						addDataItem( crosstab, measureView, function,
								rowDimension, rowLevel, colDimension, colLevel );
					}
				}
			}

			// add aggregation for crosstab grand total; or there is no levels
			// and no grand total, we still need to add one aggregation
			if ( crosstab.getGrandTotal( counterAxisType ) != null
					|| ( isCounterAxisEmpty && ( theLevelView == null || theLevelView
							.getAggregationHeader( ) != null ) ) )
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
				addDataItem( crosstab, measureView, function, rowDimension,
						rowLevel, colDimension, colLevel );
			}
		}
	}

	/**
	 * 
	 * @param crosstab
	 * @param measureView
	 * @param function
	 * @param aggregationOnColumn
	 * @param aggregationOnRow
	 * @throws SemanticException
	 */
	private static void addDataItem( CrosstabReportItemHandle crosstab,
			MeasureViewHandle measureView, String function,
			String rowDimension, String rowLevel, String colDimension,
			String colLevel ) throws SemanticException
	{
		if ( crosstab == null || measureView == null )
			return;

		// create a computed column and set some properties
		String name = generateComputedColumnName( measureView, colLevel,
				rowLevel );
		ComputedColumn column = StructureFactory.newComputedColumn( crosstab
				.getModelHandle( ), name );
		column
				.setExpression( ExpressionUtil
						.createJSMeasureExpression( measureView
								.getCubeMeasureName( ) ) );
		column.setAggregateFunction( function );
		column.addAggregateOn( rowLevel );
		column.addAggregateOn( colLevel );

		// add the computed column to crosstab
		ComputedColumnHandle columnHandle = ( (ReportItemHandle) crosstab
				.getModelHandle( ) ).addColumnBinding( column, false );

		// add a data-item to the measure aggregations
		AggregationCellHandle cell = measureView.getAggregationCell(
				rowDimension, rowLevel, colDimension, colLevel );
		if ( cell == null )
		{
			cell = measureView.addAggregation( rowDimension, rowLevel,
					colDimension, colLevel );
		}
		// set the data-item result set the the name of the column handle
		DataItemHandle dataItem = crosstab.getModuleHandle( )
				.getElementFactory( ).newDataItem( null );
		dataItem.setResultSetColumn( columnHandle.getName( ) );
		cell.addContent( dataItem );
	}

	/**
	 * Generates an meaningful and unique computed column name for a measure
	 * aggregation.
	 * 
	 * @param measureView
	 * @param aggregationOnColumn
	 * @param aggregationOnRow
	 * @return
	 */
	private static String generateComputedColumnName(
			MeasureViewHandle measureView, String aggregationOnColumn,
			String aggregationOnRow )
	{
		String name = ""; //$NON-NLS-1$
		String temp = measureView.getCubeMeasureName( );
		if ( temp != null && temp.length( ) > 0 )
			name = name + temp;

		if ( aggregationOnRow != null && aggregationOnRow.length( ) > 0 )
		{
			if ( name.length( ) > 0 )
			{
				name = name + "_" + aggregationOnRow; //$NON-NLS-1$
			}
			else
			{
				name = name + aggregationOnRow;
			}
		}
		if ( aggregationOnColumn != null && aggregationOnColumn.length( ) > 0 )
		{
			if ( name.length( ) > 0 )
			{
				name = name + "_" + aggregationOnColumn; //$NON-NLS-1$
			}
			else
			{
				name = name + aggregationOnColumn;
			}
		}
		if ( name.length( ) <= 0 )
		{
			name = "measure"; //$NON-NLS-1$
		}

		return name;
	}

	/**
	 * 
	 * @param crosstab
	 * @param axisType
	 * @param function
	 * @param measures
	 * @return
	 * @throws SemanticException
	 */
	public static CrosstabCellHandle addGrandTotal(
			CrosstabReportItemHandle crosstab, int axisType, String function,
			List measures ) throws SemanticException
	{
		if ( crosstab == null || !isValidAxisType( axisType ) )
			return null;
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );
		if ( crosstabView == null )
		{
			CommandStack stack = crosstab.getCommandStack( );
			stack.startTrans( null );

			CrosstabCellHandle grandTotal;
			try
			{
				crosstabView = crosstab.addCrosstabView( axisType );
				grandTotal = addGrandTotal( crosstabView, function, measures );
			}
			catch ( SemanticException e )
			{
				stack.rollback( );
				throw e;
			}

			stack.commit( );

			return grandTotal;
		}
		return addGrandTotal( crosstabView, function, measures );
	}

	/**
	 * 
	 * @param crosstabView
	 * @param function
	 * @param measures
	 * @return
	 */
	private static CrosstabCellHandle addGrandTotal(
			CrosstabViewHandle crosstabView, String function, List measures )
			throws SemanticException
	{
		if ( crosstabView == null )
			return null;
		PropertyHandle propHandle = crosstabView.getGrandTotalProperty( );

		if ( propHandle.getContentCount( ) > 0 )
			return crosstabView.getGrandTotal( );

		CommandStack stack = crosstabView.getCommandStack( );
		try
		{
			stack.startTrans( null );

			ExtendedItemHandle grandTotal = CrosstabExtendedItemFactory
					.createCrosstabCell( crosstabView.getModuleHandle( ) );
			propHandle.add( grandTotal );

			// adjust the measure aggregations
			CrosstabReportItemHandle crosstab = crosstabView.getCrosstab( );
			if ( crosstab != null && measures != null )
			{
				addMeasureAggregations( crosstab, crosstabView.getAxisType( ),
						null, null, null, false, function, measures );
			}

			stack.commit( );
			return (CrosstabCellHandle) getReportItem( grandTotal );
		}
		catch ( SemanticException e )
		{
			crosstabView.getLogger( ).log( Level.INFO, e.getMessage( ), e );
			stack.rollback( );
			throw e;
		}
	}

	/**
	 * Gets the measure view list that define aggregations for the given level
	 * view. Each item in the list is instance of <code>MeasureViewHandle</code>.
	 * 
	 * @param levelView
	 * @return
	 */
	public static List getAggregationMeasures( LevelViewHandle levelView )
	{
		// if level view is null, or aggregation header is not set, or cube
		// level is not set, then return empty
		if ( levelView == null || levelView.getAggregationHeader( ) == null
				|| levelView.getCubeLevelName( ) == null
				|| levelView.getCubeLevelName( ).length( ) <= 0 )
			return Collections.EMPTY_LIST;
		CrosstabReportItemHandle crosstab = levelView.getCrosstab( );
		if ( crosstab == null )
			return Collections.EMPTY_LIST;

		int axisType = levelView.getAxisType( );
		String propName = null;
		if ( axisType == ICrosstabConstants.COLUMN_AXIS_TYPE )
			propName = IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP;
		else if ( axisType == ICrosstabConstants.ROW_AXIS_TYPE )
			propName = IAggregationCellConstants.AGGREGATION_ON_ROW_PROP;
		List measures = new ArrayList( );
		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );
			for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
			{
				AggregationCellHandle cell = measureView.getAggregationCell( j );
				if ( levelView.getCubeLevelName( ).equals(
						cell.getModelHandle( ).getStringProperty( propName ) ) )
					measures.add( measureView );
			}
		}
		return measures;
	}

	/**
	 * Gets the measure view list that define aggregations for the row/column
	 * grand total in the crosstab. Each item in the list is instance of
	 * <code>MeasureViewHandle</code>.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @return
	 */
	public static List getAggregationMeasures(
			CrosstabReportItemHandle crosstab, int axisType )
	{
		// if crosstab is null or has no grand total, then return empty
		if ( crosstab == null || crosstab.getGrandTotal( axisType ) == null )
			return Collections.EMPTY_LIST;

		List measures = new ArrayList( );
		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );
			for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
			{
				AggregationCellHandle cell = measureView.getAggregationCell( j );
				if ( cell.getAggregationOnColumn( ) == null
						&& cell.getAggregationOnRow( ) == null )
					measures.add( measureView );
			}
		}
		return measures;
	}

	/**
	 * Gets the aggregation function for the level view sub-total. If the level
	 * view is null or not define any sub-total, return null.
	 * 
	 * @param levelView
	 * @return
	 */
	public static String getAggregationFunction( LevelViewHandle levelView )
	{
		// if level view is null, or aggregation header is not set, or cube
		// level is not set, then return empty
		if ( levelView == null || levelView.getAggregationHeader( ) == null
				|| levelView.getCubeLevelName( ) == null
				|| levelView.getCubeLevelName( ).length( ) <= 0 )
			return null;

		// if crosstab is not found, then return null
		CrosstabReportItemHandle crosstab = levelView.getCrosstab( );
		if ( crosstab == null )
			return null;

		String levelName = levelView.getCubeLevelName( );
		int axisType = levelView.getAxisType( );
		String propName = null;
		if ( axisType == ICrosstabConstants.COLUMN_AXIS_TYPE )
			propName = IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP;
		else if ( axisType == ICrosstabConstants.ROW_AXIS_TYPE )
			propName = IAggregationCellConstants.AGGREGATION_ON_ROW_PROP;

		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );
			for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
			{
				AggregationCellHandle cell = measureView.getAggregationCell( j );
				if ( levelName.equals( cell.getModelHandle( )
						.getStringProperty( propName ) ) )
				{
					String function = getAggregationFunction( crosstab, cell );
					if ( function != null )
						return function;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the aggregation function for the row/column grand total in the
	 * crosstab.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @return
	 */
	public static String getAggregationFunction(
			CrosstabReportItemHandle crosstab, int axisType )
	{
		// if crosstab is null or not define any grand total, then return null
		if ( crosstab == null || crosstab.getGrandTotal( axisType ) == null )
			return null;

		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );
			for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
			{
				AggregationCellHandle cell = measureView.getAggregationCell( j );
				if ( cell.getAggregationOnColumn( ) == null
						&& cell.getAggregationOnRow( ) == null )
				{
					String function = getAggregationFunction( crosstab, cell );
					if ( function != null )
						return function;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the aggregation function for this cell.
	 * 
	 * @param crosstab
	 * @param cell
	 * @return
	 */
	private static String getAggregationFunction(
			CrosstabReportItemHandle crosstab, AggregationCellHandle cell )
	{
		assert crosstab != null;
		assert cell != null;
		assert cell.getCrosstab( ) == crosstab;

		ReportItemHandle crosstabModel = (ReportItemHandle) crosstab
				.getModelHandle( );
		List contents = cell.getContents( );
		for ( int index = 0; index < contents.size( ); index++ )
		{
			DesignElementHandle content = (DesignElementHandle) contents
					.get( index );
			if ( content instanceof DataItemHandle )
			{
				String columnName = ( (DataItemHandle) content )
						.getResultSetColumn( );
				ComputedColumnHandle columnHandle = crosstabModel
						.findColumnBinding( columnName );
				if ( columnHandle != null
						&& columnHandle.getAggregateFunction( ) != null )
					return columnHandle.getAggregateFunction( );
			}
		}
		return null;
	}

	/**
	 * Inserts a dimension view to given row/column axis in the specified
	 * position.
	 * 
	 * @param crosstab
	 * @param dimensionView
	 * @param axisType
	 * @param index
	 * @param measureListMap
	 * @param functionMap
	 * @throws SemanticException
	 */
	public static void insertDimension( CrosstabReportItemHandle crosstab,
			DimensionViewHandle dimensionView, int axisType, int index,
			Map measureListMap, Map functionMap ) throws SemanticException
	{
		if ( crosstab == null || dimensionView == null
				|| !isValidAxisType( axisType ) )
			return;

		CommandStack stack = crosstab.getCommandStack( );
		stack.startTrans( null );

		try
		{
			CrosstabViewHandle crosstabView = crosstab
					.getCrosstabView( axisType );

			if ( crosstabView == null )
			{
				// if the crosstab view is null, then create and add a crosstab
				// view first, and then add the dimension to it second;
				crosstabView = crosstab.addCrosstabView( axisType );
			}
			crosstabView.getViewsProperty( ).add(
					dimensionView.getModelHandle( ), index );

			String dimensionName = dimensionView.getCubeDimensionName( );
			// adjust measure aggregations
			for ( int i = 0; i < dimensionView.getLevelCount( ); i++ )
			{
				LevelViewHandle levelView = dimensionView.getLevel( i );
				String levelName = levelView.getCubeLevelName( );
				List measures = (List) ( measureListMap == null
						? null
						: measureListMap.get( levelName ) );
				String function = (String) ( functionMap == null
						? null
						: functionMap.get( levelName ) );
				addMeasureAggregations( crosstab, axisType, levelView,
						dimensionName, levelView.getCubeLevelName( ), false,
						function, measures );
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/**
	 * Inserts a level handle into a dimension view. This method will add the
	 * aggregations and data-item automatically.
	 * 
	 * @param dimensionView
	 * @param levelHandle
	 * @param index
	 * @return
	 * @throws SemanticException
	 */
	public static LevelViewHandle insertLevel(
			DimensionViewHandle dimensionView, LevelHandle levelHandle,
			int index ) throws SemanticException
	{
		ExtendedItemHandle extendedItemHandle = CrosstabExtendedItemFactory
				.createLevelView( dimensionView.getModuleHandle( ), levelHandle );
		if ( extendedItemHandle == null )
			return null;

		if ( levelHandle != null )
		{
			// if cube dimension container of this cube level element is not
			// what is referred by this dimension view, then the insertion is
			// forbidden
			if ( !levelHandle.getContainer( ).getContainer( )
					.getQualifiedName( ).equals(
							dimensionView.getCubeDimensionName( ) ) )
			{
				// TODO: throw exception
				dimensionView.getLogger( ).log( Level.WARNING, "" ); //$NON-NLS-1$
				return null;
			}

			// if this level handle has referred by an existing level view,
			// then log error and do nothing
			if ( dimensionView.getLevel( levelHandle.getQualifiedName( ) ) != null )
			{
				dimensionView.getLogger( ).log( Level.SEVERE,
						MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_LEVEL,
						levelHandle.getQualifiedName( ) );
				throw new CrosstabException( dimensionView.getModelHandle( )
						.getElement( ), new String[]{
						levelHandle.getQualifiedName( ),
						dimensionView.getModelHandle( ).getElement( )
								.getIdentifier( )},
						MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_LEVEL );
			}
		}

		CommandStack stack = dimensionView.getCommandStack( );
		stack.startTrans( null );

		LevelViewHandle levelView = null;
		try
		{
			dimensionView.getLevelsProperty( ).add( extendedItemHandle, index );

			// if level handle is specified, then adjust aggregations
			if ( levelHandle != null )
			{
				levelView = (LevelViewHandle) CrosstabUtil.getReportItem(
						extendedItemHandle, LEVEL_VIEW_EXTENSION_NAME );

				CrosstabReportItemHandle crosstab = dimensionView.getCrosstab( );
				if ( levelView != null && crosstab != null )
				{
					adjustForAddLevel( crosstab, levelView, dimensionView
							.getCubeDimensionName( ), levelHandle
							.getQualifiedName( ), dimensionView.getAxisType( ) );
				}
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}
		stack.commit( );

		return levelView;
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
	private static void adjustForAddLevel( CrosstabReportItemHandle crosstab,
			LevelViewHandle levelView, String dimensionName, String levelName,
			int axisType ) throws SemanticException
	{
		if ( crosstab == null || levelView == null
				|| !isValidAxisType( axisType ) )
			return;

		List measures = crosstab.getModelHandle( ).getContents(
				ICrosstabReportItemConstants.MEASURES_PROP );
		if ( levelView.isInnerMost( ) )
		{
			// if originally there is no levels and grand total,
			// then remove the aggregations for the axis type and
			// the counter axis level aggregations
			if ( getAllLevelCount( crosstab, axisType ) <= 1 )
			{
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				addMeasureAggregations( crosstab, axisType, levelView,
						dimensionName, levelName, true, null, measures );
				if ( crosstab.getGrandTotal( axisType ) == null )
					adjustMeasureAggregations( crosstab, axisType, levelView,
							null, null, false, false );

			}
			else
			{
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				addMeasureAggregations( crosstab, axisType, levelView,
						dimensionName, levelName, true, null, measures );
				// add one aggregation: the original innermost level
				// before this level is added and the innermost
				// level in the counter axis if the orginal
				// innermost has aggregation header
				LevelViewHandle precedingLevel = getPrecedingLevel( levelView );
				assert precedingLevel != null;
				if ( precedingLevel.getAggregationHeader( ) != null )
				{
					adjustMeasureAggregations( crosstab, dimensionName,
							levelName, axisType,
							( (DimensionViewHandle) precedingLevel
									.getContainer( ) ).getCubeDimensionName( ),
							precedingLevel.getCubeLevelName( ), true );

					// add the data-item
					addMeasureAggregations( crosstab, dimensionName, levelName,
							axisType, ( (DimensionViewHandle) precedingLevel
									.getContainer( ) ).getCubeDimensionName( ),
							precedingLevel.getCubeLevelName( ), measures, null );
				}
			}
		}
		assert levelView.getAggregationHeader( ) == null;
		// else
		// {
		// // if the added level view is not innermost and has
		// // aggregation header, then add aggregations for this
		// // level view and all counterpart axis levels and grand
		// // total
		// if ( levelView.getAggregationHeader( ) != null )
		// {
		// adjustMeasureAggregations( crosstab, axisType, levelView,
		// dimensionName, levelName, false, isAdd );
		//
		// }
		// }
	}
}
