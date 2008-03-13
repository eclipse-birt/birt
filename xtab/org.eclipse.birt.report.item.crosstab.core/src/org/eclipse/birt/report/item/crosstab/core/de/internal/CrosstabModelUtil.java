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

package org.eclipse.birt.report.item.crosstab.core.de.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.item.crosstab.core.IAggregationCellConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

/**
 * Provide all util methods for Model part of x-tab.
 */
public class CrosstabModelUtil implements ICrosstabConstants
{

	/**
	 * 
	 * @param elements
	 * @return
	 */
	public static List getReportItems( List elements )
	{
		if ( elements == null || elements.isEmpty( ) )
			return Collections.EMPTY_LIST;

		List values = new ArrayList( );
		for ( int i = 0; i < elements.size( ); i++ )
		{
			if ( elements.get( i ) instanceof DesignElementHandle )
				values.add( CrosstabUtil.getReportItem( (DesignElementHandle) elements.get( i ) ) );
		}
		return values;
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
	 * TODO this method should provide by DTE?
	 */
	public static String getRollUpAggregationFunction( String functionName )
	{
		if ( DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE.equals( functionName )
				|| DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT.equals( functionName )
				|| DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT.equals( functionName ) )
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_SUM;
		}
		else
		{
			return functionName;
		}
	}

	/**
	 * Gets the index where this level resides in the whole crosstab.
	 * 
	 * @param levelView
	 * @return
	 */
	static int getTotalIndex( LevelViewHandle levelView )
	{
		if ( levelView == null )
			return -1;
		CrosstabReportItemHandle crosstab = levelView.getCrosstab( );
		if ( crosstab == null )
			return -1;

		int axisType = levelView.getAxisType( );
		int levelIndex = levelView.getIndex( );
		int dimensionIndex = ( (DimensionViewHandle) levelView.getContainer( ) ).getIndex( );

		int result = levelIndex;
		for ( int i = 0; i < dimensionIndex; i++ )
		{
			DimensionViewHandle dimension = crosstab.getDimension( axisType, i );
			result += dimension.getLevelCount( );
		}
		return result;

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
			DimensionViewHandle dimensionView = crosstab.getDimension( axisType,
					i );
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
		DimensionViewHandle dimensionView = (DimensionViewHandle) levelView.getContainer( );
		if ( dimensionView == null )
			return null;
		int index = levelView.getIndex( );
		if ( index - 1 >= 0 )
			return dimensionView.getLevel( index - 1 );

		// such the last one in the preceding dimension
		CrosstabViewHandle crosstabView = (CrosstabViewHandle) dimensionView.getContainer( );
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
			DimensionViewHandle dimensionView = crosstab.getDimension( axisType,
					dimensionIndex );
			for ( int levelIndex = dimensionView.getLevelCount( ) - 1; levelIndex >= 0; levelIndex-- )
			{
				return dimensionView.getLevel( levelIndex );
			}
		}

		return null;
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
	 * @param functions
	 * @param isAdd
	 *            true if add aggregation, otherwise false
	 * @throws SemanticException
	 */
	public static void addMeasureAggregations(
			CrosstabReportItemHandle crosstab, String leftDimension,
			String leftLevel, int axisType, String rightDimension,
			String rightLevel, List measures, List functions )
			throws SemanticException
	{
		if ( crosstab == null
				|| !isValidAxisType( axisType )
				|| measures == null )
			return;
		if ( functions == null || functions.size( ) != measures.size( ) )
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
			addDataItem( crosstab,
					measureView,
					(String) functions.get( i ),
					rowDimension,
					rowLevel,
					colDimension,
					colLevel );
		}
	}

	public static void addDataItem( CrosstabReportItemHandle crosstab,
			MeasureViewHandle measureView, String function,
			String rowDimension, String rowLevel, String colDimension,
			String colLevel ) throws SemanticException
	{
		addDataItem( crosstab,
				null,
				measureView,
				function,
				rowDimension,
				rowLevel,
				colDimension,
				colLevel,
				true );
	}

	/**
	 * @param crosstab
	 * @param measureView
	 * @param function
	 * @param rowDimension
	 * @param rowLevel
	 * @param colDimension
	 * @param colLevel
	 * @throws SemanticException
	 * 
	 * 
	 */
	public static void addDataItem( CrosstabReportItemHandle crosstab,
			AggregationCellHandle cell, MeasureViewHandle measureView,
			String function, String rowDimension, String rowLevel,
			String colDimension, String colLevel, boolean forceAdd )
			throws SemanticException
	{
		if ( crosstab == null || measureView == null )
			return;

		if ( cell == null )
		{
			// add a data-item to the measure aggregations
			cell = measureView.getAggregationCell( rowDimension,
					rowLevel,
					colDimension,
					colLevel );
		}

		if ( cell == null && forceAdd )
		{
			cell = measureView.addAggregation( rowDimension,
					rowLevel,
					colDimension,
					colLevel );
		}

		if ( cell != null
				&& !( measureView instanceof ComputedMeasureViewHandle ) )
		{
			// create a computed column and set some properties
			String name = CrosstabModelUtil.generateComputedColumnName( measureView,
					colLevel,
					rowLevel );
			ComputedColumn column = StructureFactory.newComputedColumn( crosstab.getModelHandle( ),
					name );
			String dataType = measureView.getDataType( );
			column.setDataType( dataType );
			column.setExpression( ExpressionUtil.createJSMeasureExpression( measureView.getCubeMeasureName( ) ) );
			column.setAggregateFunction( function != null ? function
					: getDefaultMeasureAggregationFunction( measureView ) );
			if ( rowLevel != null )
			{
				column.addAggregateOn( rowLevel );
			}
			if ( colLevel != null )
			{
				column.addAggregateOn( colLevel );
			}

			// add the computed column to crosstab
			ComputedColumnHandle columnHandle = ( (ReportItemHandle) crosstab.getModelHandle( ) ).addColumnBinding( column,
					false );

			if ( cell.getContents( ).size( ) == 0 )
			{
				// set the data-item result set the the name of the column
				// handle
				DataItemHandle dataItem = crosstab.getModuleHandle( )
						.getElementFactory( )
						.newDataItem( null );
				dataItem.setResultSetColumn( columnHandle.getName( ) );
				cell.addContent( dataItem );
			}
			else if ( cell.getContents( ).size( ) == 1
					&& cell.getContents( ).get( 0 ) instanceof DataItemHandle )
			{
				DataItemHandle dataItem = (DataItemHandle) cell.getContents( )
						.get( 0 );
				dataItem.setResultSetColumn( columnHandle.getName( ) );
			}
		}
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
	public static String generateComputedColumnName(
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
	 * Returns the default aggregation function for specific measure view
	 */
	public static String getDefaultMeasureAggregationFunction(
			MeasureViewHandle mv )
	{
		if ( mv != null && mv.getCubeMeasure( ) != null )
		{
			String func = mv.getCubeMeasure( ).getFunction( );

			if ( func != null )
			{
				return CrosstabModelUtil.getRollUpAggregationFunction( func );
			}
		}

		return DEFAULT_MEASURE_FUNCTION;
	}

	/**
	 * Gets the aggregation function for this cell.
	 * 
	 * @param crosstab
	 * @param cell
	 * @return
	 */
	public static String getAggregationFunction(
			CrosstabReportItemHandle crosstab, AggregationCellHandle cell )
	{
		assert crosstab != null;
		assert cell != null;
		assert cell.getCrosstab( ) == crosstab;

		ReportItemHandle crosstabModel = (ReportItemHandle) crosstab.getModelHandle( );
		List contents = cell.getContents( );
		for ( int index = 0; index < contents.size( ); index++ )
		{
			DesignElementHandle content = (DesignElementHandle) contents.get( index );
			if ( content instanceof DataItemHandle )
			{
				String columnName = ( (DataItemHandle) content ).getResultSetColumn( );
				ComputedColumnHandle columnHandle = crosstabModel.findColumnBinding( columnName );
				if ( columnHandle != null
						&& columnHandle.getAggregateFunction( ) != null )
					return columnHandle.getAggregateFunction( );
			}
		}
		return null;
	}

	/**
	 * Gets the aggregation function for this cell.
	 * 
	 * @param crosstab
	 * @param cell
	 * @param function
	 * @return
	 * @throws SemanticException
	 */
	public static void setAggregationFunction(
			CrosstabReportItemHandle crosstab, AggregationCellHandle cell,
			String function ) throws SemanticException
	{
		assert crosstab != null;
		assert cell != null;
		assert cell.getCrosstab( ) == crosstab;

		ReportItemHandle crosstabModel = (ReportItemHandle) crosstab.getModelHandle( );
		List contents = cell.getContents( );
		for ( int index = 0; index < contents.size( ); index++ )
		{
			DesignElementHandle content = (DesignElementHandle) contents.get( index );
			if ( content instanceof DataItemHandle )
			{
				String columnName = ( (DataItemHandle) content ).getResultSetColumn( );
				ComputedColumnHandle columnHandle = crosstabModel.findColumnBinding( columnName );
				if ( columnHandle != null )
					columnHandle.setAggregateFunction( function );
			}
		}
	}

	/**
	 * Locates the cell which controls the column with for given cell
	 * 
	 * @param crosstabItem
	 * 
	 * @param cell
	 * @return
	 */
	public static CrosstabCellHandle locateColumnWidthCell(
			CrosstabReportItemHandle crosstabItem, CrosstabCellHandle cell )
	{
		if ( crosstabItem != null
				&& cell != null
				&& cell.getCrosstab( ) == crosstabItem )
		{
			// TODO valid source cell, rowSpan/columnSpan must be 1.

			boolean isMeasureHorizontal = MEASURE_DIRECTION_HORIZONTAL.equals( crosstabItem.getMeasureDirection( ) );

			if ( cell instanceof AggregationCellHandle )
			{
				AggregationCellHandle aggCell = (AggregationCellHandle) cell;

				MeasureViewHandle mv = null;

				if ( IMeasureViewConstants.DETAIL_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					// for measure detail cell

					if ( isMeasureHorizontal )
					{
						// use detail cell from current measure
						return ( (MeasureViewHandle) cell.getContainer( ) ).getCell( );
					}
					else
					{
						// use first meausre detail cell
						return crosstabItem.getMeasure( 0 ).getCell( );
					}
				}

				if ( isMeasureHorizontal )
				{
					// for horizontal measure, use container measure
					mv = (MeasureViewHandle) aggCell.getContainer( );
				}
				else
				{
					// else use first measure
					mv = crosstabItem.getMeasure( 0 );
				}

				String colDimension = aggCell.getDimensionName( COLUMN_AXIS_TYPE );
				String colLevel = aggCell.getLevelName( COLUMN_AXIS_TYPE );

				LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
						COLUMN_AXIS_TYPE );
				DimensionViewHandle colDimHandle = (DimensionViewHandle) colLevelHandle.getContainer( );

				if ( colLevelHandle.getCubeLevelName( ).equals( colLevel )
						&& colDimHandle.getCubeDimensionName( )
								.equals( colDimension ) )
				{
					// aggregation on innerest column level
					return mv.getCell( );
				}

				String rowDimension = null;
				String rowLevel = null;

				LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
						ROW_AXIS_TYPE );
				if ( rowLevelHandle != null )
				{
					rowDimension = ( (DimensionViewHandle) rowLevelHandle.getContainer( ) ).getCubeDimensionName( );
					rowLevel = rowLevelHandle.getCubeLevelName( );
				}

				// return selected aggregation cell on measure
				return mv.getAggregationCell( rowDimension,
						rowLevel,
						colDimension,
						colLevel );
			}
			else if ( cell.getContainer( ) instanceof MeasureViewHandle )
			{
				if ( isMeasureHorizontal )
				{
					// use detail cell from current measure
					return ( (MeasureViewHandle) cell.getContainer( ) ).getCell( );
				}
				else if ( IMeasureViewConstants.HEADER_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					// use first available measrue header cell
					for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
					{
						MeasureViewHandle mv = crosstabItem.getMeasure( i );
						if ( mv.getHeader( ) != null )
						{
							return mv.getHeader( );
						}
					}
				}
				else
				{
					// use first meausre detail cell
					return crosstabItem.getMeasure( 0 ).getCell( );
				}
			}
			else if ( cell.getContainer( ) instanceof LevelViewHandle )
			{
				LevelViewHandle lv = (LevelViewHandle) cell.getContainer( );

				boolean isRowLevl = ICrosstabReportItemConstants.ROWS_PROP.equals( lv.getContainer( )
						.getContainer( )
						.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) );

				if ( isRowLevl )
				{
					if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
							.getContainerPropertyHandle( )
							.getPropertyDefn( )
							.getName( ) ) )
					{
						// use innerest row level cell
						return getInnerMostLevel( crosstabItem, ROW_AXIS_TYPE ).getCell( );
					}
					// use current level cell
					return cell;
				}

				// not row level
				if ( crosstabItem.getMeasureCount( ) == 0 )
				{
					if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
							.getContainerPropertyHandle( )
							.getPropertyDefn( )
							.getName( ) ) )
					{
						// for subtotal cell, return itself
						return cell;
					}
					// if no measure, for level detail and subtotal, use
					// inneerest level cell
					return getInnerMostLevel( crosstabItem, COLUMN_AXIS_TYPE ).getCell( );
				}
				if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					// user selected aggregation cell on first measure
					String rowDimension = null;
					String rowLevel = null;

					String colDimension = ( (DimensionViewHandle) lv.getContainer( ) ).getCubeDimensionName( );
					String colLevel = lv.getCubeLevelName( );

					LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
							ROW_AXIS_TYPE );
					if ( rowLevelHandle != null )
					{
						rowDimension = ( (DimensionViewHandle) rowLevelHandle.getContainer( ) ).getCubeDimensionName( );
						rowLevel = rowLevelHandle.getCubeLevelName( );
					}

					if ( isMeasureHorizontal )
					{
						// user first available aggregation cell
						for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
						{
							CrosstabCellHandle aggCell = crosstabItem.getMeasure( i )
									.getAggregationCell( rowDimension,
											rowLevel,
											colDimension,
											colLevel );

							if ( aggCell != null )
							{
								return aggCell;
							}
						}
					}
					else
					{
						// use selected aggregation cell on first measure
						return crosstabItem.getMeasure( 0 )
								.getAggregationCell( rowDimension,
										rowLevel,
										colDimension,
										colLevel );
					}
				}
				// user first measure detail cell
				return crosstabItem.getMeasure( 0 ).getCell( );
			}
			else if ( cell.getContainer( ) instanceof CrosstabViewHandle )
			{

				boolean isRowGrandTotal = ICrosstabReportItemConstants.ROWS_PROP.equals( cell.getContainer( )
						.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) );

				if ( isRowGrandTotal )
				{
					LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
							ROW_AXIS_TYPE );
					if ( rowLevelHandle != null )
					{
						// use innerest row level cell
						return rowLevelHandle.getCell( );
					}
					// user itself
					return cell;
				}

				if ( crosstabItem.getMeasureCount( ) == 0 )
				{
					// if no measure, for grand total cell, use itself
					return cell;
				}
				// user selected aggregation cell on first measure
				String rowDimension = null;
				String rowLevel = null;

				LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
						ROW_AXIS_TYPE );
				if ( rowLevelHandle != null )
				{
					rowDimension = ( (DimensionViewHandle) rowLevelHandle.getContainer( ) ).getCubeDimensionName( );
					rowLevel = rowLevelHandle.getCubeLevelName( );
				}

				if ( isMeasureHorizontal )
				{
					// user first available aggregation cell
					for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
					{
						CrosstabCellHandle aggCell = crosstabItem.getMeasure( i )
								.getAggregationCell( rowDimension,
										rowLevel,
										null,
										null );

						if ( aggCell != null )
						{
							return aggCell;
						}
					}
				}
				else
				{
					// use selected aggregation cell on first measure
					return crosstabItem.getMeasure( 0 )
							.getAggregationCell( rowDimension,
									rowLevel,
									null,
									null );
				}
			}
			else if ( cell.getContainer( ) instanceof CrosstabReportItemHandle )
			{
				// crosstab header cell

				LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
						ROW_AXIS_TYPE );
				if ( rowLevelHandle != null )
				{
					// use innerest row level cell
					return rowLevelHandle.getCell( );
				}

				if ( !isMeasureHorizontal )
				{
					// use first available measrue header cell
					for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
					{
						MeasureViewHandle mv = crosstabItem.getMeasure( i );
						if ( mv.getHeader( ) != null )
						{
							return mv.getHeader( );
						}
					}
				}

				// user itself
				return cell;
			}
		}

		return null;
	}

	/**
	 * Locates the cell which controls the row height for given cell
	 * 
	 * @param crosstabItem
	 * 
	 * @param cell
	 * @return
	 */
	public static CrosstabCellHandle locateRowHeightCell(
			CrosstabReportItemHandle crosstabItem, CrosstabCellHandle cell )
	{
		if ( crosstabItem != null
				&& cell != null
				&& cell.getCrosstab( ) == crosstabItem )
		{
			// TODO valid source cell, rowSpan/columnSpan must be 1.

			boolean isMeasureHorizontal = MEASURE_DIRECTION_HORIZONTAL.equals( crosstabItem.getMeasureDirection( ) );

			if ( cell instanceof AggregationCellHandle )
			{
				AggregationCellHandle aggCell = (AggregationCellHandle) cell;

				MeasureViewHandle mv = null;

				if ( IMeasureViewConstants.DETAIL_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					if ( !isMeasureHorizontal )
					{
						// use detail cell from current measure
						return ( (MeasureViewHandle) cell.getContainer( ) ).getCell( );
					}
					else
					{
						// use first meausre detail cell
						return crosstabItem.getMeasure( 0 ).getCell( );
					}
				}

				if ( !isMeasureHorizontal )
				{
					// for horizontal measure, use container measure
					mv = (MeasureViewHandle) aggCell.getContainer( );
				}
				else
				{
					// else use first measure
					mv = crosstabItem.getMeasure( 0 );
				}

				String rowDimension = aggCell.getDimensionName( ROW_AXIS_TYPE );
				String rowLevel = aggCell.getLevelName( ROW_AXIS_TYPE );

				LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
						ROW_AXIS_TYPE );
				DimensionViewHandle rowDimHandle = (DimensionViewHandle) rowLevelHandle.getContainer( );

				if ( rowLevelHandle.getCubeLevelName( ).equals( rowLevel )
						&& rowDimHandle.getCubeDimensionName( )
								.equals( rowDimension ) )
				{
					// aggregation on innerest column level
					return mv.getCell( );
				}

				String colDimension = null;
				String colLevel = null;

				LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
						COLUMN_AXIS_TYPE );
				if ( colLevelHandle != null )
				{
					colDimension = ( (DimensionViewHandle) colLevelHandle.getContainer( ) ).getCubeDimensionName( );
					colLevel = colLevelHandle.getCubeLevelName( );
				}

				// return selected aggregation cell on measure
				return mv.getAggregationCell( rowDimension,
						rowLevel,
						colDimension,
						colLevel );
			}
			else if ( cell.getContainer( ) instanceof MeasureViewHandle )
			{
				if ( !isMeasureHorizontal )
				{
					// use detail cell from current measure
					return ( (MeasureViewHandle) cell.getContainer( ) ).getCell( );
				}
				else if ( IMeasureViewConstants.HEADER_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					// use first available measrue header cell
					for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
					{
						MeasureViewHandle mv = crosstabItem.getMeasure( i );
						if ( mv.getHeader( ) != null )
						{
							return mv.getHeader( );
						}
					}
				}
				else
				{
					// use first meausre detail cell
					return crosstabItem.getMeasure( 0 ).getCell( );
				}
			}
			else if ( cell.getContainer( ) instanceof LevelViewHandle )
			{
				LevelViewHandle lv = (LevelViewHandle) cell.getContainer( );

				boolean isRowLevl = ICrosstabReportItemConstants.ROWS_PROP.equals( lv.getContainer( )
						.getContainer( )
						.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) );

				if ( !isRowLevl )
				{
					if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
							.getContainerPropertyHandle( )
							.getPropertyDefn( )
							.getName( ) ) )
					{
						// use innerest column level cell
						return getInnerMostLevel( crosstabItem,
								COLUMN_AXIS_TYPE ).getCell( );
					}
					// use current level cell
					return cell;
				}
				if ( crosstabItem.getMeasureCount( ) == 0 )
				{
					if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
							.getContainerPropertyHandle( )
							.getPropertyDefn( )
							.getName( ) ) )
					{
						// for subtotal cell, return itself
						return cell;
					}
					// if no measure, for level detail and subtotal, use
					// inneerest level cell
					return getInnerMostLevel( crosstabItem, ROW_AXIS_TYPE ).getCell( );
				}
				if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					// user selected aggregation cell on first measure
					String colDimension = null;
					String colLevel = null;

					String rowDimension = ( (DimensionViewHandle) lv.getContainer( ) ).getCubeDimensionName( );
					String rowLevel = lv.getCubeLevelName( );

					LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
							COLUMN_AXIS_TYPE );
					if ( colLevelHandle != null )
					{
						colDimension = ( (DimensionViewHandle) colLevelHandle.getContainer( ) ).getCubeDimensionName( );
						colLevel = colLevelHandle.getCubeLevelName( );
					}

					if ( isMeasureHorizontal )
					{
						// use selected aggregation cell on first measure
						return crosstabItem.getMeasure( 0 )
								.getAggregationCell( rowDimension,
										rowLevel,
										colDimension,
										colLevel );
					}
					else
					{
						// user first available aggregation cell
						for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
						{
							CrosstabCellHandle aggCell = crosstabItem.getMeasure( i )
									.getAggregationCell( rowDimension,
											rowLevel,
											colDimension,
											colLevel );

							if ( aggCell != null )
							{
								return aggCell;
							}
						}
					}
				}
				// user first measure detail cell
				return crosstabItem.getMeasure( 0 ).getCell( );
			}
			else if ( cell.getContainer( ) instanceof CrosstabViewHandle )
			{

				boolean isRowGrandTotal = ICrosstabReportItemConstants.ROWS_PROP.equals( cell.getContainer( )
						.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) );

				if ( !isRowGrandTotal )
				{
					LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
							COLUMN_AXIS_TYPE );
					if ( colLevelHandle != null )
					{
						// use innerest column level cell
						return colLevelHandle.getCell( );
					}
					// user itself
					return cell;
				}
				if ( crosstabItem.getMeasureCount( ) == 0 )
				{
					// if no measure, for grand total cell, use itself
					return cell;
				}
				// user selected aggregation cell on first measure
				String colDimension = null;
				String colLevel = null;

				LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
						COLUMN_AXIS_TYPE );
				if ( colLevelHandle != null )
				{
					colDimension = ( (DimensionViewHandle) colLevelHandle.getContainer( ) ).getCubeDimensionName( );
					colLevel = colLevelHandle.getCubeLevelName( );
				}

				if ( isMeasureHorizontal )
				{
					// use selected aggregation cell on first measure
					return crosstabItem.getMeasure( 0 )
							.getAggregationCell( null,
									null,
									colDimension,
									colLevel );
				}
				else
				{
					// user first available aggregation cell
					for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
					{
						CrosstabCellHandle aggCell = crosstabItem.getMeasure( i )
								.getAggregationCell( null,
										null,
										colDimension,
										colLevel );

						if ( aggCell != null )
						{
							return aggCell;
						}
					}
				}
			}
			else if ( cell.getContainer( ) instanceof CrosstabReportItemHandle )
			{
				// crosstab header cell

				LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
						COLUMN_AXIS_TYPE );
				if ( colLevelHandle != null )
				{
					// use innerest column level cell
					return colLevelHandle.getCell( );
				}

				if ( isMeasureHorizontal )
				{
					// use first available measrue header cell
					for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
					{
						MeasureViewHandle mv = crosstabItem.getMeasure( i );
						if ( mv.getHeader( ) != null )
						{
							return mv.getHeader( );
						}
					}
				}

				// use itself
				return cell;
			}

		}

		return null;
	}

	/**
	 * Gets the list of the level views that effects the aggregation cells.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @return
	 */
	public static List getAllAggregationLevels(
			CrosstabReportItemHandle crosstab, int axisType )
	{
		List result = new ArrayList( );
		for ( int i = 0; i < crosstab.getDimensionCount( axisType ); i++ )
		{
			DimensionViewHandle dimensionView = crosstab.getDimension( axisType,
					i );
			for ( int j = 0; j < dimensionView.getLevelCount( ); j++ )
			{
				LevelViewHandle levelView = dimensionView.getLevel( j );

				// if the level view not specify the cube level, it is
				// meaningless to do anything
				if ( levelView.getCubeLevelName( ) == null )
					continue;

				if ( levelView.isInnerMost( )
						|| levelView.getAggregationHeader( ) != null )
				{
					result.add( levelView );
				}
			}
		}

		return result;
	}

	/**
	 * Gets the property name of the aggregation on property in AggregationCell
	 * by the axis type.
	 * 
	 * @param axisType
	 * @return
	 */
	public static String getAggregationOnPropName( int axisType )
	{
		if ( axisType == COLUMN_AXIS_TYPE )
			return IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP;
		if ( axisType == ROW_AXIS_TYPE )
			return IAggregationCellConstants.AGGREGATION_ON_ROW_PROP;
		return null;
	}

	/**
	 * Justifies whether the given measure is aggregated on the level view.
	 * 
	 * @param measureView
	 * @param levelName
	 * @param axisType
	 * @return
	 */
	public static boolean isAggregationOn( MeasureViewHandle measureView,
			String levelName, int axisType )
	{
		if ( measureView == null || !isValidAxisType( axisType ) )
			return false;

		String propName = getAggregationOnPropName( axisType );
		for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
		{
			AggregationCellHandle cell = measureView.getAggregationCell( j );
			String aggregationOn = cell.getModelHandle( )
					.getStringProperty( propName );
			if ( ( levelName == null && aggregationOn == null )
					|| ( levelName != null && levelName.equals( aggregationOn ) ) )
				return true;
		}
		return false;
	}

}
