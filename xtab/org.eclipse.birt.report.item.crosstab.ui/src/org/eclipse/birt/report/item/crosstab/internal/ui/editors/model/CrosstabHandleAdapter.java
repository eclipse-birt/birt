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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;

/**
 * The adapter for the crosstab.
 */

public class CrosstabHandleAdapter extends BaseCrosstabAdapter
{

	private static final String COLUMNAREA_COLUMN = "columnarea_column";//$NON-NLS-1$
	private static final String COLUMNAREA_ROW = "columnarea_row";//$NON-NLS-1$
	private static final String ROWAREA_COLUMN = "rowarea_column";//$NON-NLS-1$
	private static final String ROWAREA_ROW = "rowarea_row";//$NON-NLS-1$
	public static final String DEFAULT_WIDTH = "100.0"//$NON-NLS-1$
			+ DesignChoiceConstants.UNITS_PERCENTAGE;
	public static final String LEFT_CONNER = "left_conner";//$NON-NLS-1$
	private ICrosstabCellAdapterFactory factory = createCrosstabCellAdapterFactory( );
	private int columnAndMeasureColumnNumber = -1;

	private List oldModelList = new ArrayList( );

	/**
	 * Record the infomation of the model structure.
	 */
	private HashMap map = new HashMap( );

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public CrosstabHandleAdapter( CrosstabReportItemHandle handle )
	{
		super( handle );
	}

	/**
	 * Creates the adapter factory for create the children.
	 * 
	 * @return
	 */
	protected ICrosstabCellAdapterFactory createCrosstabCellAdapterFactory( )
	{
		return new DefaultCrocsstabCellAdapterFactory( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.model.schematic.crosstab.BaseCrosstabAdapter#getModelList()
	 */
	public List getModelList( )
	{
		init( );
		// there are four part to ctraet model
		List list = new ArrayList( );

		List columns = buildColumnArea( );
		List rows = buildRowArea( );
		List details = buildMeasures( );
		adjustDirection(columns, rows);
		
		buildModel( list, columns, rows, details );

		adjustColumn( columns, details );
		adjustRow( rows, details );
		
		int rowBase = ( (Integer) map.get( COLUMNAREA_ROW ) ).intValue( );
		int columnBase = ( (Integer) map.get( ROWAREA_COLUMN ) ).intValue( );
		CrosstabCellAdapter first = factory.createCrosstabCellAdapter( LEFT_CONNER,
				null,
				1,
				rowBase,
				1,
				columnBase,
				false );
		list.add( 0, first );
		// debug("all", list);

		Collections.sort( list, new ModelComparator( ) );

		oldModelList = list;
		return list;
	}

	private void adjustDirection( List columns, List rows )
	{
		String area, otherArea;
		List workList = null;
		if (isVertical( ))
		{
			workList = rows;
			area = ROWAREA_ROW;
			otherArea = ROWAREA_COLUMN;
		}
		else
		{
			workList = columns;
			area = COLUMNAREA_COLUMN;
			otherArea = COLUMNAREA_ROW;
		}
		
		if (workList.size( ) != 1)
		{
			return;
		}
		
		Object obj = workList.get( 0 );
		if (!(obj instanceof VirtualCrosstabCellAdapter))
		{
			return;
		}
		CrosstabCellAdapter adapter = (CrosstabCellAdapter)obj;
		List measures = getMeasreViewHandleList( );
		if (measures.size( ) > 0)
		{
			int number ;
			
			if  (isVertical( ))
			{
				number = addMesureHeaderToVirtual( workList, 1, 1, 2, 1, measures );
				adapter.setRowSpan( number  );
			}
			else
			{
				number = addMesureHeaderToVirtual( workList, 2, 1, 1, 1, measures );
				adapter.setColumnSpan( number );
			}
			if (number == 0)
			{
				number = 1;
			}
			map.put( area, new Integer( number ) );	
			map.put( otherArea, new Integer(2) );
		}
		
	}

	private void adjustColumn( List columns, List details )
	{
		for ( int i = 0; i < columns.size( ); i++ )
		{
			CrosstabCellAdapter adapter = (CrosstabCellAdapter) columns.get( i );
			int row = adapter.getRowNumber( );
			// int rowSpan = adapter.getRowSpan( );
			int column = adapter.getColumnNumber( );
			int columnSpan = adapter.getColumnSpan( );

			if ( !adapter.getPositionType( )
					.equals( ICrosstabCellAdapterFactory.CELL_SUB_TOTAL ) )
			{
				continue;
			}
			if ( isBefore( adapter.getCrosstabCellHandle( ) ) )
			{
				CrosstabCellAdapter beforeAdapter = getCell( row,
						column - 1,
						columns );
				int beforeColumn = beforeAdapter.getColumnNumber( );
				int beforeColumnSpan = beforeAdapter.getColumnSpan( );

				for ( int j = 0; j < columns.size( ); j++ )
				{
					CrosstabCellAdapter tempAdapter = (CrosstabCellAdapter) columns.get( j );

					if ( tempAdapter.getRowNumber( ) < row )
					{
						continue;
					}
					int tempColumnNumber = tempAdapter.getColumnNumber( );
					if ( tempColumnNumber >= beforeColumn
							&& tempColumnNumber < beforeColumn
									+ beforeColumnSpan )
					{
						tempAdapter.setColumnNumber( tempColumnNumber
								+ columnSpan );
					}
					else if ( tempColumnNumber >= column
							&& tempColumnNumber < column + columnSpan )
					{
						tempAdapter.setColumnNumber( tempColumnNumber
								- beforeColumnSpan );
					}
				}

				for ( int j = 0; j < details.size( ); j++ )
				{
					CrosstabCellAdapter tempAdapter = (CrosstabCellAdapter) details.get( j );

					int tempColumnNumber = tempAdapter.getColumnNumber( );
					if ( tempColumnNumber >= beforeColumn
							&& tempColumnNumber < beforeColumn
									+ beforeColumnSpan )
					{
						tempAdapter.setColumnNumber( tempColumnNumber
								+ columnSpan );
					}
					else if ( tempColumnNumber >= column
							&& tempColumnNumber < column + columnSpan )
					{
						tempAdapter.setColumnNumber( tempColumnNumber
								- beforeColumnSpan );
					}
				}
			}
		}
	}

	private void adjustRow( List rows, List details )
	{
		for ( int i = 0; i < rows.size( ); i++ )
		{
			CrosstabCellAdapter adapter = (CrosstabCellAdapter) rows.get( i );
			int row = adapter.getRowNumber( );
			int rowSpan = adapter.getRowSpan( );
			int column = adapter.getColumnNumber( );
			// int columnSpan = adapter.getColumnSpan( );

			if ( !adapter.getPositionType( )
					.equals( ICrosstabCellAdapterFactory.CELL_SUB_TOTAL ) )
			{
				continue;
			}
			if ( isBefore( adapter.getCrosstabCellHandle( ) ) )
			{
				CrosstabCellAdapter beforeAdapter = getCell( row - 1,
						column,
						rows );
				int beforeRow = beforeAdapter.getRowNumber( );
				int beforeRowSpan = beforeAdapter.getRowSpan( );

				for ( int j = 0; j < rows.size( ); j++ )
				{
					CrosstabCellAdapter tempAdapter = (CrosstabCellAdapter) rows.get( j );

					if ( tempAdapter.getColumnNumber( ) < column )
					{
						continue;
					}
					int tempRowNumber = tempAdapter.getRowNumber( );
					if ( tempRowNumber >= beforeRow
							&& tempRowNumber < beforeRow + beforeRowSpan )
					{
						tempAdapter.setRowNumber( tempRowNumber + rowSpan );
					}
					else if ( tempRowNumber >= row
							&& tempRowNumber < row + rowSpan )
					{
						tempAdapter.setRowNumber( tempRowNumber - beforeRowSpan );
					}
				}

				for ( int j = 0; j < details.size( ); j++ )
				{
					CrosstabCellAdapter tempAdapter = (CrosstabCellAdapter) details.get( j );

					int tempRowNumber = tempAdapter.getRowNumber( );
					if ( tempRowNumber >= beforeRow
							&& tempRowNumber < beforeRow + beforeRowSpan )
					{
						tempAdapter.setRowNumber( tempRowNumber + rowSpan );
					}
					else if ( tempRowNumber >= row
							&& tempRowNumber < row + rowSpan )
					{
						tempAdapter.setRowNumber( tempRowNumber - beforeRowSpan );
					}
				}
			}
		}
	}

	private void init( )
	{
		columnAndMeasureColumnNumber = -1;
		map.clear( );
	}

	private void buildModel( List ori, List columns, List rows, List details )
	{
		int rowBase = ( (Integer) map.get( COLUMNAREA_ROW ) ).intValue( );
		int columnBase = ( (Integer) map.get( ROWAREA_COLUMN ) ).intValue( );

		adjustSpan( columns, rows, details );

		addToModel( ori, columns, 0, columnBase );
		addToModel( ori, rows, rowBase, 0 );
		addToModel( ori, details, rowBase, columnBase );

		// debug("all", ori);
	}

	private void adjustSpan( List columns, List rows, List details )
	{
		int columnSpanBase = ( (Integer) map.get( COLUMNAREA_COLUMN ) ).intValue( );
		int measureBase = getCrosstabReportItemHandle( ).getMeasureCount( );
		int rowBase = ( (Integer) map.get( ROWAREA_ROW ) ).intValue( );
		// first adjust the rowspna and columnspan if there are some virtual
		// editpart in the crosstab
		// only need adjust column area and measure
		if ( columnSpanBase > 1 && details.size( ) == 1 )
		{
			Object obj = details.get( 0 );
			if ( obj instanceof VirtualCrosstabCellAdapter )
			{
				( (VirtualCrosstabCellAdapter) obj ).setColumnSpan( columnSpanBase );
			}
		}
		if ( !isVertical( ) && measureBase > 1 && columns.size( ) == 1 )
		{
			Object obj = columns.get( 0 );
			if ( obj instanceof VirtualCrosstabCellAdapter )
			{
				( (VirtualCrosstabCellAdapter) obj ).setColumnSpan( measureBase );
				columnAndMeasureColumnNumber = measureBase;
			}
			else
			{
				columnAndMeasureColumnNumber = -1;
			}
		}
		if ( isVertical( ) && measureBase > 1 && rows.size( ) == 1 )
		{
			Object obj = rows.get( 0 );
			if ( obj instanceof VirtualCrosstabCellAdapter )
			{
				( (VirtualCrosstabCellAdapter) obj ).setRowSpan( measureBase );
				columnAndMeasureColumnNumber = measureBase;
			}
			else
			{
				columnAndMeasureColumnNumber = -1;
			}
		}
		if ( rowBase > 1 && details.size( ) == 1 )
		{
			Object obj = details.get( 0 );
			if ( obj instanceof VirtualCrosstabCellAdapter )
			{
				( (VirtualCrosstabCellAdapter) obj ).setRowSpan( rowBase );
			}
		}
	}

	private void addToModel( List ori, List add, int rowBase, int columnBase )
	{
		int size = add.size( );
		for ( int i = 0; i < size; i++ )
		{
			CrosstabCellAdapter adapter = (CrosstabCellAdapter) add.get( i );
			adapter.setRowNumber( rowBase + adapter.getRowNumber( ) );
			adapter.setColumnNumber( columnBase + adapter.getColumnNumber( ) );
			addToModel( ori, adapter );
		}
	}

	private List buildMeasures( )
	{
		List retValue = new ArrayList( );

		CrosstabReportItemHandle crosstab = getCrosstabReportItemHandle( );
		int count = crosstab.getMeasureCount( );
		// loop the measure
		for ( int i = 0; i < count; i++ )
		{
			MeasureViewHandle measureHandle = crosstab.getMeasure( i );

			// build the cell
			CrosstabCellHandle handle = measureHandle.getCell( );
			CrosstabCellAdapter adapter = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CELL_MEASURE,
					handle,
					1,
					-1,
					i + 1,
					-1,
					isVertical( ) );

			retValue.add( adapter );

			// bulid the AggregationCellHandle
			int addregationCellCount = measureHandle.getAggregationCount( );

			for ( int j = 0; j < addregationCellCount; j++ )
			{
				AggregationCellHandle cell = measureHandle.getAggregationCell( j );
				LevelViewHandle levelViewHandle = cell.getLevelView( getWorkArea( ICrosstabConstants.COLUMN_AXIS_TYPE ) );

				int measureCount = count;

				Integer temp;
				List measuresHandles;
				int position;
				if ( levelViewHandle == null )// grand cell
				{
					temp = (Integer) map.get( getWorkArea( COLUMNAREA_COLUMN ) );
					measuresHandles = crosstab.getAggregationMeasures( getWorkArea( ICrosstabConstants.COLUMN_AXIS_TYPE ) );
				}
				else
				{
					temp = (Integer) map.get( levelViewHandle );
					measuresHandles = levelViewHandle.getAggregationMeasures( );
				}
				if ( temp == null )
				{
					throw new RuntimeException( "build error" );
				}
				measureCount = measuresHandles.size( );
				position = measuresHandles.indexOf( measureHandle );

				if ( temp.intValue( ) <= count )
				{
					measureCount = count;
					position = i;
				}
				int tempPosition = temp.intValue( ) >= measureCount ? temp.intValue( )
						: measureCount;
				int column = tempPosition - ( measureCount - position ) + 1;
				// int column = temp.intValue( ) - ( measureCount - position ) +
				// 1;

				levelViewHandle = cell.getLevelView( getWorkArea( ICrosstabConstants.ROW_AXIS_TYPE ) );
				if ( levelViewHandle == null )// grand cell
				{
					temp = (Integer) map.get( getWorkArea( ROWAREA_ROW ) );
				}
				else
				{
					temp = (Integer) map.get( levelViewHandle );

				}
				if ( temp == null )
				{
					throw new RuntimeException( "build error" );
				}

				int row = temp.intValue( );
				// add the adapter to the list
				CrosstabCellAdapter aggregationCell = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CELL_MEASURE_AGGREGATION,
						cell,
						row,
						1,
						column,
						1,
						isVertical( ) );

				retValue.add( aggregationCell );
			}

		}

		if ( retValue.isEmpty( ) )
		{
			CrosstabCellAdapter first = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CELL_MEASURE_VIRTUAL,
					null,
					1,
					-1,
					1,
					-1,
					false );
			retValue.add( first );
		}
		// debug("measure", retValue);
		return retValue;
	}

	private List buildRowArea( )
	{
		List retValue = new ArrayList( );
		CrosstabReportItemHandle handle = getCrosstabReportItemHandle( );
		int count = handle.getDimensionCount( ICrosstabConstants.ROW_AXIS_TYPE );

		// int measureCount = handle.getMeasureCount( );
		int measureCount = handle.getMeasureCount( );
		// int rowNumber = 1;
		// int columnNumber = 1;

		int columnNumber = measureCount == 0 ? 1 : 2;
		if ( !isVertical( ) )
		{
			columnNumber = 1;
		}
		// int columnNumber = Math.max( measureCount, 1 );

		// now add the mearsure cell
		// if ( count != 0 )
		// {
		// addMesureHeaderInColumn( retValue, 0 );
		// }
		List measureHandleList = getMeasreViewHandleList( );
		int rowNumber = 0;
		if ( count != 0 )
		{
			rowNumber = addMesureHeader( retValue,
					0,
					ICrosstabConstants.ROW_AXIS_TYPE,
					measureHandleList );
		}

		if ( rowNumber == 0 )
		{
			rowNumber = 1;
		}

		for ( int i = count - 1; i >= 0; i-- )
		{
			DimensionViewHandle dimensionHandle = handle.getDimension( ICrosstabConstants.ROW_AXIS_TYPE,
					i );

			int levelCount = dimensionHandle.getLevelCount( );
			for ( int j = levelCount - 1; j >= 0; j-- )
			{
				LevelViewHandle levelHandle = dimensionHandle.getLevel( j );
				CrosstabCellHandle cellHandle = levelHandle.getCell( );
				String type = j == 0 ? ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE
						: ICrosstabCellAdapterFactory.CELL_LEVEL_HANDLE;
				// add the levelhandle cell
				CrosstabCellAdapter cellAdapter = factory.createCrosstabCellAdapter( type,
						cellHandle,
						1,
						rowNumber,
						columnNumber,
						-1,
						false );

				retValue.add( cellAdapter );
				// put the property for build the measure area
				map.put( levelHandle, new Integer( rowNumber ) );

				LevelViewHandle preLevelHandle = getPreviousLevelViewHandle( dimensionHandle,
						levelHandle );

				// add the sub total
				if ( preLevelHandle != null
						&& preLevelHandle.getAggregationHeader( ) != null )
				{
					List list = preLevelHandle.getAggregationMeasures( );
					if ( list.size( ) != 0 )
					{
						int preMeasureCount = list.size( );
						CrosstabCellHandle preCellHandle = preLevelHandle.getAggregationHeader( );
						// rowNumber = rowNumber + 1;
						CrosstabCellAdapter preCellAdapter = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CELL_SUB_TOTAL,
								preCellHandle,
								rowNumber + 1,
								// rowNumber,
								isVertical( ) ? preMeasureCount : 1,
								columnNumber,
								columnNumber - ( isVertical( ) ? 1 : 0 ),
								false );

						retValue.add( preCellAdapter );
						int addCount = addMesureHeader( retValue,
								rowNumber,
								ICrosstabConstants.ROW_AXIS_TYPE,
								list );
						rowNumber = rowNumber + ( addCount == 0 ? 1 : addCount );
					}

				}
				if ( j != 0 || i != 0 )
				{
					columnNumber = columnNumber + 1;
				}
			}
		}

		// add the grand cell

		CrosstabCellHandle grandCell = handle.getGrandTotal( ICrosstabConstants.ROW_AXIS_TYPE );
		if ( grandCell != null && !retValue.isEmpty( ) )
		{
			List list = handle.getAggregationMeasures( ICrosstabConstants.ROW_AXIS_TYPE );
			int size = list.size( );
			if ( size > 0 )
			{
				CrosstabCellAdapter grandCellAdapter = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CELL_GRAND_TOTAL,
						grandCell,
						rowNumber + 1,
						isVertical( ) ? size : 1,
						columnNumber,
						columnNumber - ( isVertical( ) ? 1 : 0 ),
						false );

				retValue.add( grandCellAdapter );

				int addCount = addMesureHeader( retValue,
						rowNumber,
						ICrosstabConstants.ROW_AXIS_TYPE,
						list );
				rowNumber = rowNumber + ( addCount == 0 ? 1 : addCount );
				// rowNumber = rowNumber + 1;
			}

		}
		// put the row number to the map

		if ( retValue.isEmpty( ) )
		{
			CrosstabCellAdapter columnVirtual = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CELL_ROW_VIRTUAL,
					null,
					1,
					-1,
					1,
					-1,
					false );

			retValue.add( columnVirtual );

			columnNumber = 1;
			rowNumber = 1;
		}
		map.put( ROWAREA_COLUMN, new Integer( columnNumber ) );
		map.put( ROWAREA_ROW, new Integer( rowNumber ) );
		// covert the row number
		covertColumnNumber( columnNumber, retValue );
		// debug( "row", retValue );
		return retValue;
	}

	private List buildColumnArea( )
	{
		List retValue = new ArrayList( );
		CrosstabReportItemHandle handle = getCrosstabReportItemHandle( );
		int count = handle.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE );

		int measureCount = handle.getMeasureCount( );

		int rowNumber = measureCount == 0 ? 1 : 2;
		if ( isVertical( ) )
		{
			rowNumber = 1;
		}
		// int columnNumber = Math.max( measureCount, 1 );

		// now add the mearsure cell
		// if ( count != 0 )
		// {
		// addMesureHeaderInColumn( retValue, 0 );
		// }
		List measureHandleList = getMeasreViewHandleList( );
		int columnNumber = 0;
		if ( count != 0 )
		{
			columnNumber = addMesureHeader( retValue,
					0,
					ICrosstabConstants.COLUMN_AXIS_TYPE,
					measureHandleList );
		}
		if ( columnNumber == 0 )
		{
			columnNumber = 1;
		}

		for ( int i = count - 1; i >= 0; i-- )
		{
			DimensionViewHandle dimensionHandle = handle.getDimension( ICrosstabConstants.COLUMN_AXIS_TYPE,
					i );

			int levelCount = dimensionHandle.getLevelCount( );
			for ( int j = levelCount - 1; j >= 0; j-- )
			{
				LevelViewHandle levelHandle = dimensionHandle.getLevel( j );
				CrosstabCellHandle cellHandle = levelHandle.getCell( );
				String type = j == 0 ? ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE
						: ICrosstabCellAdapterFactory.CELL_LEVEL_HANDLE;
				// add the levelhandle cell
				CrosstabCellAdapter cellAdapter = factory.createCrosstabCellAdapter( type,
						cellHandle,
						rowNumber,
						1,
						1,
						columnNumber,
						false );

				retValue.add( cellAdapter );
				// put the property for build the measure area
				map.put( levelHandle, new Integer( columnNumber ) );

				LevelViewHandle preLevelHandle = getPreviousLevelViewHandle( dimensionHandle,
						levelHandle );

				// add the sub total
				if ( preLevelHandle != null
						&& preLevelHandle.getAggregationHeader( ) != null )
				{
					List list = preLevelHandle.getAggregationMeasures( );
					if ( list.size( ) != 0 )
					{
						int preMeasureCount = list.size( );
						CrosstabCellHandle preCellHandle = preLevelHandle.getAggregationHeader( );

						CrosstabCellAdapter preCellAdapter = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CELL_SUB_TOTAL,
								preCellHandle,
								rowNumber,
								rowNumber - ( isVertical( ) ? 0 : 1 ),
								columnNumber + 1,
								isVertical( ) ? 1 : preMeasureCount,
								false );

						retValue.add( preCellAdapter );
						// add subtotal measure handle
						// if ( measureCount != 0 )
						// {
						int addCount = addMesureHeader( retValue,
								columnNumber,
								ICrosstabConstants.COLUMN_AXIS_TYPE,
								list );
						columnNumber = columnNumber
								+ ( addCount == 0 ? 1 : addCount );
						// }
						// else
						// {
						// columnNumber = columnNumber + 1;
						// }
					}

				}
				if ( j != 0 || i != 0 )
				{
					rowNumber = rowNumber + 1;
				}
			}
		}

		// add the grand cell
		CrosstabCellHandle grandCell = handle.getGrandTotal( ICrosstabConstants.COLUMN_AXIS_TYPE );
		if ( grandCell != null && !retValue.isEmpty( ) )
		{
			List list = handle.getAggregationMeasures( ICrosstabConstants.COLUMN_AXIS_TYPE );
			int size = list.size( );
			if ( size > 0 )
			{
				CrosstabCellAdapter grandCellAdapter = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CELL_GRAND_TOTAL,
						grandCell,
						rowNumber,
						rowNumber - ( isVertical( ) ? 0 : 1 ),
						columnNumber + 1,
						isVertical( ) ? 1 : size,
						false );

				retValue.add( grandCellAdapter );
				// add subtotal measure handle
				int addCount = addMesureHeader( retValue,
						columnNumber,
						ICrosstabConstants.COLUMN_AXIS_TYPE,
						list );
				columnNumber = columnNumber + ( addCount == 0 ? 1 : addCount );
			}
		}
		// put the row number to the map

		if ( retValue.isEmpty( ) )
		{
			CrosstabCellAdapter columnVirtual = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CELL_COLUMN_VIRTUAL,
					null,
					1,
					-1,
					1,
					-1,
					false );

			retValue.add( columnVirtual );
			// map.put( COLUMNAREA_COLUMN, new Integer( 1 ) );
			// map.put( COLUMNAREA_ROW, new Integer( 1 ) );
			columnNumber = 1;
			rowNumber = 1;
		}
		map.put( COLUMNAREA_COLUMN, new Integer( columnNumber ) );
		map.put( COLUMNAREA_ROW, new Integer( rowNumber ) );

		// covert the row number
		covertRowNumber( rowNumber, retValue );
		// debug( "column", retValue );
		return retValue;
	}

	private void covertRowNumber( int rowNumber, List cells )
	{
		int size = cells.size( );
		for ( int i = 0; i < size; i++ )
		{
			CrosstabCellAdapter adapter = (CrosstabCellAdapter) cells.get( i );
			adapter.setRowNumber( rowNumber - adapter.getRowNumber( ) + 1 );
		}

		if ( isPageLayoutOver( ) )
		{
			for ( int i = 0; i < size; i++ )
			{
				CrosstabCellAdapter adapter = (CrosstabCellAdapter) cells.get( i );
				int row = adapter.getRowNumber( );
				int rowSpan = adapter.getRowSpan( );
				int column = adapter.getColumnNumber( );
				int columnSpan = adapter.getColumnSpan( );

				if ( adapter.getPositionType( )
						.equals( ICrosstabCellAdapterFactory.CELL_SUB_TOTAL ) )
				{
					adapter.setRowNumber( row - 1 );
					adapter.setRowSpan( rowSpan + 1 );

					CrosstabCellAdapter levelAdapter = getCell( row - 1,
							column - 1,
							cells );
					levelAdapter.setColumnSpan( levelAdapter.getColumnSpan( )
							- columnSpan );
				}

			}
		}
	}

	private CrosstabCellAdapter getCell( int rowNumber, int columnNumber,
			List list )
	{
		for ( int i = 0; i < list.size( ); i++ )
		{
			CrosstabCellAdapter adapter = (CrosstabCellAdapter) list.get( i );
			if ( rowNumber >= adapter.getRowNumber( )
					&& rowNumber < adapter.getRowNumber( )
							+ adapter.getRowSpan( )
					&& columnNumber >= adapter.getColumnNumber( )
					&& columnNumber < adapter.getColumnNumber( )
							+ adapter.getColumnSpan( ) )
			{
				return adapter;
			}
		}
		return null;
	}

	private void covertColumnNumber( int columnNumber, List cells )
	{
		int size = cells.size( );
		for ( int i = 0; i < size; i++ )
		{
			CrosstabCellAdapter adapter = (CrosstabCellAdapter) cells.get( i );
			adapter.setColumnNumber( columnNumber
					- adapter.getColumnNumber( )
					+ 1 );
		}

		if ( isPageLayoutOver( ) )
		{
			for ( int i = 0; i < size; i++ )
			{
				CrosstabCellAdapter adapter = (CrosstabCellAdapter) cells.get( i );
				int row = adapter.getRowNumber( );
				int rowSpan = adapter.getRowSpan( );
				int column = adapter.getColumnNumber( );
				int columnSpan = adapter.getColumnSpan( );

				if ( adapter.getPositionType( )
						.equals( ICrosstabCellAdapterFactory.CELL_SUB_TOTAL ) )
				{
					adapter.setColumnNumber( column - 1 );
					adapter.setColumnSpan( columnSpan + 1 );

					CrosstabCellAdapter levelAdapter = getCell( row - 1,
							column - 1,
							cells );
					levelAdapter.setRowSpan( levelAdapter.getRowSpan( )
							- rowSpan );
				}
			}
		}
	}
	
	private int addMesureHeaderToVirtual( List list, int rowNumber, int rowSpan, int columnNumber, int columnSpan,
			List measures )
	{
		int measureCount = measures.size( );
		for ( int k = 0; k < measureCount; k++ )
		{
			MeasureViewHandle preMmeasureHandle = (MeasureViewHandle) measures.get( k );
			CrosstabCellHandle preMeasureCellHandle = preMmeasureHandle.getHeader( );

			CrosstabCellAdapter measureCellAdapt = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CELL_MEASURE_HEADER,
					preMeasureCellHandle,
					rowNumber,
					rowSpan,
					columnNumber,
					columnSpan,
					false);
			if (isVertical( ))
			{
				rowNumber ++;
			}
			else
			{
				columnNumber++;
			}
			list.add( measureCellAdapt );
		}
		return measureCount;
	}
	
	private int addMesureHeader( List list, int baseColumn, int area,
			List measures )
	{
		if ( isVertical( ) && area == ICrosstabConstants.COLUMN_AXIS_TYPE )
		{
			return 0;
		}
		else if ( !isVertical( ) && area == ICrosstabConstants.ROW_AXIS_TYPE )
		{
			return 0;
		}
		int measureCount = measures.size( );
		for ( int k = 0; k < measureCount; k++ )
		{
			MeasureViewHandle preMmeasureHandle = (MeasureViewHandle) measures.get( k );
			CrosstabCellHandle preMeasureCellHandle = preMmeasureHandle.getHeader( );

			CrosstabCellAdapter measureCellAdapt = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CELL_MEASURE_HEADER,
					preMeasureCellHandle,
					1,
					-1,
					baseColumn + k + 1,
					-1,
					isVertical( ) );

			list.add( measureCellAdapt );
		}
		return measureCount;
	}

	// private void addMesureHeaderInColumn( List list, int baseColumn,
	// List measures )
	// {
	// // CrosstabReportItemHandle handle = getCrosstabReportItemHandle( );
	// int measureCount = measures.size( );
	// for ( int k = 0; k < measureCount; k++ )
	// {
	// MeasureViewHandle preMmeasureHandle = (MeasureViewHandle) measures.get( k
	// );
	// CrosstabCellHandle preMeasureCellHandle = preMmeasureHandle.getHeader( );
	//
	// CrosstabCellAdapter measureCellAdapt = factory.createCrosstabCellAdapter(
	// ICrosstabCellAdapterFactory.CELL_MEASURE_HEADER,
	// preMeasureCellHandle,
	// 1,
	// -1,
	// baseColumn + k + 1,
	// -1 ,
	// isVertical( ));
	//
	// list.add( measureCellAdapt );
	// }
	//
	// }

	/**
	 * return the previous levelviewhandle
	 * 
	 * @return
	 */

	// TODO AbstractCrosstabItemHandle has the getContain method so refactor the
	// code
	private LevelViewHandle getPreviousLevelViewHandle(
			DimensionViewHandle dimensionHandle, LevelViewHandle levelHandle )
	{
		int index = levelHandle.getIndex( );
		if ( index > 0 )
		{
			return dimensionHandle.getLevel( index - 1 );
		}
		else
		{
			int dimIndex = dimensionHandle.getIndex( );
			if ( dimIndex <= 0 )
			{
				return null;
			}
			DimensionViewHandle preDimsionViewHandle = getCrosstabReportItemHandle( ).getDimension( dimensionHandle.getAxisType( ),
					dimIndex - 1 );
			return preDimsionViewHandle.getLevel( preDimsionViewHandle.getLevelCount( ) - 1 );
		}
	}

	/**
	 * Use the old model add to model list, so the no new editpart creat
	 * 
	 * @param list
	 * @param adapter
	 */
	private void addToModel( List list, BaseCrosstabAdapter adapter )
	{
		if ( list.contains( adapter ) )
		{
			return;
		}
		else if ( oldModelList.contains( adapter ) )
		{
			int index = oldModelList.indexOf( adapter );
			BaseCrosstabAdapter copy = (BaseCrosstabAdapter) oldModelList.get( index );
			list.add( adapter.copyToTarget( copy ) );
		}
		else
		{
			list.add( adapter );
		}
	}

	private CrosstabReportItemHandle getCrosstabReportItemHandle( )
	{
		return (CrosstabReportItemHandle) getCrosstabItemHandle( );
	}

	/**
	 * DefaultCrocsstabCellAdapterFactory
	 */
	protected class DefaultCrocsstabCellAdapterFactory implements
			ICrosstabCellAdapterFactory
	{

		public CrosstabCellAdapter createCrosstabCellAdapter( String type,
				CrosstabCellHandle handle, int rowNumber, int rowSpan,
				int columnNumber, int columnSpan, boolean isConvert )
		{
			CrosstabCellAdapter retValue = null;
			if ( CELL_LEVEL_HANDLE.equals( type )
					|| CELL_FIRST_LEVEL_HANDLE.equals( type )
					|| CELL_MEASURE.equals( type ) )
			{
				retValue = new NormalCrosstabCellAdapter( handle );
			}
			else if ( CELL_MEASURE_HEADER.equals( type ) )
			{
				retValue = new HeaderCrosstabCellHandleAdapter( handle );
			}
			else if ( CELL_ROW_VIRTUAL.equals( type ) )
			{
				retValue = new VirtualCrosstabCellAdapter( handle );
				( (VirtualCrosstabCellAdapter) retValue ).setCrosstabReportItemHandle( getCrosstabReportItemHandle( ) );
				( (VirtualCrosstabCellAdapter) retValue ).setType( VirtualCrosstabCellAdapter.ROW_TYPE );
			}
			else if ( CELL_COLUMN_VIRTUAL.equals( type ) )
			{
				retValue = new VirtualCrosstabCellAdapter( handle );
				( (VirtualCrosstabCellAdapter) retValue ).setCrosstabReportItemHandle( getCrosstabReportItemHandle( ) );
				( (VirtualCrosstabCellAdapter) retValue ).setType( VirtualCrosstabCellAdapter.COLUMN_TYPE );
			}
			else if ( CELL_MEASURE_VIRTUAL.equals( type ) )
			{
				retValue = new VirtualCrosstabCellAdapter( handle );
				( (VirtualCrosstabCellAdapter) retValue ).setCrosstabReportItemHandle( getCrosstabReportItemHandle( ) );
				( (VirtualCrosstabCellAdapter) retValue ).setType( VirtualCrosstabCellAdapter.MEASURE_TYPE );
			}
			else if ( LEFT_CONNER.equals( type ) )
			{
				retValue = new VirtualCrosstabCellAdapter( handle );
				( (VirtualCrosstabCellAdapter) retValue ).setCrosstabReportItemHandle( getCrosstabReportItemHandle( ) );
				( (VirtualCrosstabCellAdapter) retValue ).setType( VirtualCrosstabCellAdapter.IMMACULATE_TYPE );
			}
			else if ( CELL_SUB_TOTAL.equals( type ) )
			{
				retValue = new TotalCrosstabCellHandleAdapter( handle );
				( (TotalCrosstabCellHandleAdapter) retValue ).setType( TotalCrosstabCellHandleAdapter.SUB_TOTAL );
			}
			else if ( CELL_GRAND_TOTAL.equals( type ) )
			{
				retValue = new TotalCrosstabCellHandleAdapter( handle );
				( (TotalCrosstabCellHandleAdapter) retValue ).setType( TotalCrosstabCellHandleAdapter.GRAND_TOTAL );
			}
			else if ( CELL_MEASURE_AGGREGATION.equals( type ) )
			{
				retValue = new AggregationCrosstabCellAdapter( (AggregationCellHandle) handle );
			}

			if ( retValue == null )
			{
				return retValue;
			}

			if ( isConvert )
			{
				int temp = rowNumber;
				rowNumber = columnNumber;
				columnNumber = temp;

				temp = rowSpan;
				rowSpan = columnSpan;
				columnSpan = temp;
			}
			if ( rowNumber >= 1 )
			{
				retValue.setRowNumber( rowNumber );
			}
			if ( rowSpan >= 1 )
			{
				retValue.setRowSpan( rowSpan );
			}
			if ( columnNumber >= 1 )
			{
				retValue.setColumnNumber( columnNumber );
			}
			if ( columnSpan >= 1 )
			{
				retValue.setColumnSpan( columnSpan );
			}

			retValue.setPositionType( type );
			return retValue;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString( )
	{
		return super.toString( );
	}

	private void debug( String area, List list )
	{
		System.out.println( "///////////////////////////////////" );
		for ( int i = 0; i < list.size( ); i++ )
		{
			CrosstabCellAdapter adapter = (CrosstabCellAdapter) list.get( i );

			String classNmae = adapter.getClass( ).getName( );
			int index = classNmae.lastIndexOf( "." );
			classNmae = classNmae.substring( index + 1 );
			System.out.println( "cell row=="
					+ adapter.getRowNumber( )
					+ " rowSpan=="
					+ adapter.getRowSpan( )
					+ " column=="
					+ adapter.getColumnNumber( )
					+ " columnSpan=="
					+ adapter.getColumnSpan( )
					+ "           "
					+ classNmae );
		}
		System.out.println( "///////////////////////////////////" );
	}

	/**
	 * Gets the row count
	 * 
	 * @return
	 */
	public int getRowCount( )
	{
		Integer columnRow = (Integer) map.get( COLUMNAREA_ROW );
		if ( columnRow == null )
		{
			throw new RuntimeException( "model don't build" );
		}
		Integer rowRow = (Integer) map.get( ROWAREA_ROW );
		if ( columnRow == null )
		{
			throw new RuntimeException( "model don't build" );
		}
		int value = rowRow.intValue( );
		if ( getAdjustNumber( ICrosstabConstants.ROW_AXIS_TYPE ) > value )
		{
			value = getAdjustNumber( ICrosstabConstants.ROW_AXIS_TYPE );
		}
		return columnRow.intValue( ) + value;
	}

	/**
	 * Gets the column count
	 * 
	 * @return
	 */
	public int getColumnCount( )
	{
		Integer rowColumn = (Integer) map.get( ROWAREA_COLUMN );
		if ( rowColumn == null )
		{
			throw new RuntimeException( "model don't build" );
		}
		Integer columnColumn = (Integer) map.get( COLUMNAREA_COLUMN );
		if ( columnColumn == null )
		{
			throw new RuntimeException( "model don't build" );
		}

		int value = columnColumn.intValue( );
		if ( getAdjustNumber( ICrosstabConstants.COLUMN_AXIS_TYPE ) > value )
		{
			value = getAdjustNumber( ICrosstabConstants.COLUMN_AXIS_TYPE );
		}
		return rowColumn.intValue( ) + value;
	}

	private int getAdjustNumber( int type )
	{
		if ( !isVertical( ) && type == ICrosstabConstants.COLUMN_AXIS_TYPE )
		{
			return columnAndMeasureColumnNumber;
		}
		if ( isVertical( ) && type == ICrosstabConstants.ROW_AXIS_TYPE )
		{
			return columnAndMeasureColumnNumber;
		}
		return -1;
	}

	/**
	 * Gets the row height from the model.
	 * 
	 * @param number
	 * @return
	 */
	public DimensionHandle getRowHeight( int number )
	{
		// because the crosstab has no the row and column so there must pass a
		// cell
		CrosstabCellHandle handle = getRowOprationCell( number );
		if ( handle == null )
		{
			return null;
		}
		try
		{
			return getCrosstabReportItemHandle( ).getRowHeight( handle );
		}
		catch ( CrosstabException e )
		{
			return null;
		}
	}

	/**
	 * Gets the row height from the model.
	 * 
	 * @param number
	 * @return
	 */
	public void setRowHeight( int number, int value )
	{
		// because the crosstab has no the row and column so there must pass a
		// cell
		CrosstabCellHandle handle = getRowOprationCell( number );
		if ( handle == null )
		{
			return;
		}
		try
		{
			DimensionValue dimensionValue = new DimensionValue( value,
					DesignChoiceConstants.UNITS_PX );
			getCrosstabReportItemHandle( ).setRowHeight( handle, dimensionValue );
		}
		catch ( CrosstabException e )
		{
			ExceptionHandler.handle( e );
		}

	}

	/**
	 * @param rowNumber
	 * @return
	 */
	public CrosstabCellHandle getRowOprationCell( int rowNumber )
	{
		int size = oldModelList.size( );
		for ( int i = 0; i < size; i++ )
		{
			CrosstabCellAdapter cellAdapter = (CrosstabCellAdapter) oldModelList.get( i );
			if ( cellAdapter.getRowNumber( ) == rowNumber
					&& cellAdapter.getRowSpan( ) == 1
					&& cellAdapter.getCrosstabCellHandle( ) != null
					&& ( !isVertical( ) || !cellAdapter.getPositionType( )
							.equals( ICrosstabCellAdapterFactory.CELL_MEASURE_HEADER ) ) )
			{

				return cellAdapter.getCrosstabCellHandle( );

			}
		}

		return null;
	}

	/**
	 * @param columnNumber
	 * @return
	 */
	public CrosstabCellHandle getColumnOprationCell( int columnNumber )
	{
		int size = oldModelList.size( );
		for ( int i = 0; i < size; i++ )
		{
			CrosstabCellAdapter cellAdapter = (CrosstabCellAdapter) oldModelList.get( i );
			if ( cellAdapter.getColumnNumber( ) == columnNumber
					&& cellAdapter.getColumnSpan( ) == 1
					&& cellAdapter.getCrosstabCellHandle( ) != null
					&& ( isVertical( ) || !cellAdapter.getPositionType( )
							.equals( ICrosstabCellAdapterFactory.CELL_MEASURE_HEADER ) ) )
			{
				return cellAdapter.getCrosstabCellHandle( );
			}
		}
		return null;
	}

	/**
	 * Gets the column width from the model.
	 * 
	 * @param number
	 * @return
	 */
	public DimensionHandle getColumnWidth( int number )
	{
		// because the crosstab has no the row and column so there must pass a
		// cell
		CrosstabCellHandle handle = getColumnOprationCell( number );
		if ( handle == null )
		{
			return null;
		}
		try
		{
			return getCrosstabReportItemHandle( ).getColumnWidth( handle );
		}
		catch ( CrosstabException e )
		{
			return null;
		}
	}

	/**
	 * Gets the column width from the model.
	 * 
	 * @param number
	 * @return
	 */
	public void setColumnWidth( int number, int value )
	{
		// because the crosstab has no the row and column so there must pass a
		// cell
		CrosstabCellHandle handle = getColumnOprationCell( number );
		if ( handle == null )
		{
			return;
		}
		try
		{
			DimensionValue dimensionValue = new DimensionValue( value,
					DesignChoiceConstants.UNITS_PX );
			getCrosstabReportItemHandle( ).setColumnWidth( handle,
					dimensionValue );

			return;
		}
		catch ( CrosstabException e )
		{
			ExceptionHandler.handle( e );
		}

	}

	public void setWidth( int value )
	{
		try
		{
			DimensionValue dimensionValue = new DimensionValue( value,
					DesignChoiceConstants.UNITS_PX );
			DimensionHandle handle = getCrosstabReportItemHandle( ).getWidth( );
			handle.setValue( dimensionValue );

			return;
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Returns the defined width in model in Pixel.
	 * 
	 * @return
	 */
	public String getDefinedWidth( )
	{
		DimensionHandle handle = ( (ExtendedItemHandle) getAdapter( DesignElementHandle.class ) ).getWidth( );

		if ( handle.getUnits( ) == null || handle.getUnits( ).length( ) == 0 )
		{
			// TODO The default value is 100.0% to fix the bug 124051, but it is
			// a temp solution.
			// default value is 100.0%
			return DEFAULT_WIDTH;
			// return null;
		}
		else if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( handle.getUnits( ) ) )
		{
			return handle.getMeasure( )
					+ DesignChoiceConstants.UNITS_PERCENTAGE;
		}
		else
		{
			int px = (int) DEUtil.convertoToPixel( handle );

			if ( px <= 0 )
			{
				return null;
			}

			return String.valueOf( px );
		}
	}

	/**
	 * Comparator the cell adapter row by row , column by column ModelComparator
	 */
	private class ModelComparator implements Comparator
	{

		public int compare( Object o1, Object o2 )
		{
			CrosstabCellAdapter part1 = (CrosstabCellAdapter) o1;
			CrosstabCellAdapter part2 = (CrosstabCellAdapter) o2;
			if ( part1.getRowNumber( ) > part2.getRowNumber( ) )
			{
				return 1;
			}
			else if ( part1.getRowNumber( ) < part2.getRowNumber( ) )
			{
				return -1;
			}
			else if ( part1.getColumnNumber( ) > part2.getColumnNumber( ) )
			{
				return 1;
			}
			else if ( part1.getColumnNumber( ) > part2.getColumnNumber( ) )
			{
				return -1;
			}
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode( )
	{
		return getCrosstabItemHandle( ).hashCode( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj )
	{
		if ( obj == getCrosstabItemHandle( ) )
		{
			return true;
		}
		if ( obj instanceof CrosstabHandleAdapter )
		{
			return getCrosstabItemHandle( ) == ( (CrosstabHandleAdapter) obj ).getCrosstabItemHandle( );
		}
		if ( obj instanceof ExtendedItemHandle )
		{
			try
			{
				return getCrosstabItemHandle( ) == ( (ExtendedItemHandle) obj ).getReportItem( );
			}
			catch ( ExtendedElementException e )
			{
				return false;
			}
		}
		return super.equals( obj );
	}

	private String getWorkArea( String type )
	{
		if ( isVertical( ) )
		{
			if ( type.equals( COLUMNAREA_COLUMN ) )
			{
				return ROWAREA_ROW;
			}
			else if ( type.equals( ROWAREA_ROW ) )
			{
				return COLUMNAREA_COLUMN;
			}
		}
		return type;
	}

	private int getWorkArea( int type )
	{
		if ( isVertical( ) )
		{
			if ( type == ICrosstabConstants.COLUMN_AXIS_TYPE )
			{
				return ICrosstabConstants.ROW_AXIS_TYPE;
			}
			else if ( type == ICrosstabConstants.ROW_AXIS_TYPE )
			{
				return ICrosstabConstants.COLUMN_AXIS_TYPE;
			}
		}
		return type;
	}

	private boolean isVertical( )
	{
		return ICrosstabConstants.MEASURE_DIRECTION_VERTICAL.equals( getCrosstabReportItemHandle( ).getMeasureDirection( ) );
	}

	private List getMeasreViewHandleList( )
	{
		List retValue = new ArrayList( );
		CrosstabReportItemHandle handle = getCrosstabReportItemHandle( );
		int measureCount = handle.getMeasureCount( );
		for ( int k = 0; k < measureCount; k++ )
		{
			MeasureViewHandle preMmeasureHandle = handle.getMeasure( k );

			retValue.add( preMmeasureHandle );
		}
		return retValue;
	}

	private boolean isBefore( CrosstabCellHandle cellHandle )
	{
		AbstractCrosstabItemHandle parent = cellHandle;
		while ( parent != null )
		{
			if ( parent instanceof LevelViewHandle )
			{
				return ICrosstabConstants.AGGREGATION_HEADER_LOCATION_BEFORE.equals( ( (LevelViewHandle) parent ).getAggregationHeaderLocation( ) );
			}
			parent = parent.getContainer( );
		}
		return false;
	}

	private boolean isPageLayoutOver( )
	{
		return getCrosstabReportItemHandle( ).getPageLayout( )
				.equals( ICrosstabConstants.PAGE_LAYOUT_DOWN_THEN_OVER );
	}
}
