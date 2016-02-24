/*******************************************************************************
 * Copyright (c) 2004, 2014 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.internal.CrosstabModelUtil;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

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
	
	private int adjustGrandColumn, adjustGrandRow;

	/**
	 * Record the information of the model structure.
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
		// there are four parts to create model
		List list = new ArrayList( );

		List columns = buildColumnArea( );
		List rows = buildRowArea( );
		List details = buildMeasures( );
		adjustDirection( columns, rows );

		buildModel( list, columns, rows, details );

		adjustColumn( columns, details );
		adjustRow( rows, details );

		List leftConner = new ArrayList();
		buildLeftConner( leftConner );
		list.addAll( leftConner );
		
		adjustGrandTotal( list, (CrosstabCellAdapter)leftConner.get( leftConner.size( ) - 1 ) );
		
		//debug("all", list);
		Collections.sort( list, new ModelComparator( ) );

		oldModelList = list;
		return list;
	}
	
	private void adjustGrandTotal(List list, CrosstabCellAdapter first)
	{
		//CrosstabCellAdapter first = (CrosstabCellAdapter)list.get( 0 );
		
		int rowCount = getRowCount( );
		int columnCount = getColumnCount( );
		for (int i=0; i<list.size( ); i++)
		{
			CrosstabCellAdapter cellAdapter = (CrosstabCellAdapter)list.get( i );
			if (adjustGrandColumn > 0)
			{
				int beforeColumnNumber = columnCount - adjustGrandColumn;
				int cellColumnNumber = cellAdapter.getColumnNumber( );
				if (cellColumnNumber > first.getColumnNumber( ) + first.getColumnSpan( ) - 1)
				{
					
					if (cellColumnNumber <= beforeColumnNumber)
					{
						cellAdapter.setColumnNumber( cellColumnNumber + adjustGrandColumn );
					}
					else
					{
						cellAdapter.setColumnNumber( cellColumnNumber - beforeColumnNumber + first.getColumnNumber( ) + first.getColumnSpan( ) - 1);
					}
				}
			}
			if (adjustGrandRow > 0)
			{
				int beforeRowNumber = rowCount - adjustGrandRow;
				int cellRowNumber = cellAdapter.getRowNumber( );
				if (cellRowNumber > first.getRowNumber( ) + first.getRowSpan( ) - 1)
				{
					if (cellRowNumber <= beforeRowNumber)
					{
						cellAdapter.setRowNumber( cellRowNumber + adjustGrandRow );
					}
					else
					{
						cellAdapter.setRowNumber( cellRowNumber - beforeRowNumber + first.getRowNumber( ) + first.getRowSpan( ) - 1 );
					}
				}
			}
		}
	}
	
	private void buildLeftConner( List list )
	{
		int rowBase = ( (Integer) map.get( COLUMNAREA_ROW ) ).intValue( );
		int columnBase = ( (Integer) map.get( ROWAREA_COLUMN ) ).intValue( );
		CrosstabReportItemHandle handle = getCrosstabReportItemHandle( );
		CrosstabCellAdapter first = null;
		if ( handle.getHeader( ) == null )
		{
			first = factory.createCrosstabCellAdapter( LEFT_CONNER,
					null,
					1,
					rowBase,
					1,
					columnBase,
					false );
		}
		else
		{
			List<LevelViewHandle> columnList = CrosstabUtil.getLevelList(handle, ICrosstabConstants.COLUMN_AXIS_TYPE);
			List<LevelViewHandle> rowList = CrosstabUtil.getLevelList(handle, ICrosstabConstants.ROW_AXIS_TYPE);
			
			if (handle.getHeaderCount( ) > 1 )
			{	
				int temp = rowBase;
				
				int rowCount = rowBase;
				int columnCount = columnBase;
				int needSpan = 0;
				if (columnList.size( ) == 0 && rowBase == 2)
				{
					temp= temp - 1;
					rowCount = 1;
					needSpan = 1;
				}
				else if (rowList.size( ) == 0 && columnBase == 2)
				{
					temp= columnBase - 1;
					columnCount = 1;
					needSpan = 2;
				}
				//if ((columnBase*temp == handle.getHeaderCount( )&& needSpan != 2) || (needSpan == 2 && rowBase * temp == handle.getHeaderCount( )))
				if (rowCount*columnCount == handle.getHeaderCount( ))
				{				
					for (int i=0; i<rowCount; i++)
					{
						for (int j=0; j<columnCount; j++)
						{
							int rowSpan = 1;
							int columnSpan = 1;
							if (needSpan == 1)
							{
								rowSpan = 2;
							}
							else if (needSpan == 2)
							{
								columnSpan = 2;
							}
							CrosstabCellAdapter cellAdapter = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CROSSTAB_HEADER,
									handle.getHeader(i*columnCount + j ),
									i + 1,
									rowSpan,
									j + 1,
									columnSpan,
									false );
							list.add( cellAdapter );
						}
					}
				}
				else
				{
					first = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CROSSTAB_HEADER,
							handle.getHeader( ),
							1,
							rowBase,
							1,
							columnBase,
							false );
				}
			}
			else
			{
				first = factory.createCrosstabCellAdapter( ICrosstabCellAdapterFactory.CROSSTAB_HEADER,
						handle.getHeader( ),
						1,
						rowBase,
						1,
						columnBase,
						false );
			}
		}
		if ( first != null )
		{
			list.add( 0, first );
		}
	}

	private void adjustDirection( List columns, List rows )
	{
		String area, otherArea;
		List workList = null;
		if ( isVertical( ) )
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

		if ( workList.size( ) != 1 )
		{
			return;
		}

		Object obj = workList.get( 0 );
		if ( !( obj instanceof VirtualCrosstabCellAdapter ) )
		{
			return;
		}
		CrosstabCellAdapter adapter = (CrosstabCellAdapter) obj;
		List measures = getMeasreViewHandleList( );
		if ( measures.size( ) > 0 )
		{
			int number;

			if ( isVertical( ) )
			{
				number = addMesureHeaderToVirtual( workList,
						1,
						1,
						2,
						1,
						measures );
				adapter.setRowSpan( number );
			}
			else
			{
				number = addMesureHeaderToVirtual( workList,
						2,
						1,
						1,
						1,
						measures );
				adapter.setColumnSpan( number );
			}
			if ( number == 0 )
			{
				number = 1;
			}
			map.put( area, Integer.valueOf( number ) );
			if (isHideHeader( ))
			{
				map.put( otherArea, Integer.valueOf( 1 ) );
			}
			else
			{
				map.put( otherArea, Integer.valueOf( 2 ) );
			}
			
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
		adjustGrandColumn = 0;
		adjustGrandRow = 0;
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
		// first adjust the rowSpan and columnSpan if there are some virtual
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

			// build the AggregationCellHandle
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
					throw new RuntimeException( "build error" ); //$NON-NLS-1$
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
					throw new RuntimeException( "build error" ); //$NON-NLS-1$
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
		if ( !isVertical( ) || isHideHeader( ))
		{
			columnNumber = 1;
		}
		// int columnNumber = Math.max( measureCount, 1 );

		// now add the measure cell
		// if ( count != 0 )
		// {
		// addMesureHeaderInColumn( retValue, 0 );
		// }
		List measureHandleList = getMeasreViewHandleList( );
		int rowNumber = 0;
		if ( count != 0 )
		{
			LevelViewHandle lastHandle = findLastLevelViewHandle( ICrosstabConstants.ROW_AXIS_TYPE );
			if (lastHandle ==  null)
			{
				throw new RuntimeException("lasthandle is null");//$NON-NLS-1$
			}
			rowNumber = addMesureHeader( retValue,
					0,
					ICrosstabConstants.ROW_AXIS_TYPE,
					measureHandleList, lastHandle );
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
				// add the levelHandle cell
				CrosstabCellAdapter cellAdapter = factory.createCrosstabCellAdapter( type,
						cellHandle,
						1,
						rowNumber,
						columnNumber,
						-1,
						false );

				retValue.add( cellAdapter );
				// put the property for build the measure area
				map.put( levelHandle, Integer.valueOf( rowNumber ) );

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
								columnNumber - ( isVertical( ) && !isHideHeader( ) ? 1 : 0 ),
								false );

						retValue.add( preCellAdapter );
						int addCount = addMesureHeader( retValue,
								rowNumber,
								ICrosstabConstants.ROW_AXIS_TYPE,
								list, preLevelHandle );
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
						columnNumber - ( isVertical( ) && !isHideHeader( ) ? 1 : 0 ),
						false );

				retValue.add( grandCellAdapter );

				int beforeRowNumber = rowNumber;
				int addCount = addMesureHeader( retValue,
						rowNumber,
						ICrosstabConstants.ROW_AXIS_TYPE,
						list, null );
				rowNumber = rowNumber + ( addCount == 0 ? 1 : addCount );
				//int adjust = rowNumber - beforeRowNumber;
				if (isGrandBefore( ICrosstabConstants.ROW_AXIS_TYPE ))
				{
					adjustGrandRow = rowNumber - beforeRowNumber;
				}
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
		map.put( ROWAREA_COLUMN, Integer.valueOf( columnNumber ) );
		map.put( ROWAREA_ROW, Integer.valueOf( rowNumber ) );
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
		
		if ( isVertical( ) || isHideHeader( ))
		{
			rowNumber = 1;
		}
		// int columnNumber = Math.max( measureCount, 1 );

		// now add the measure cell
		// if ( count != 0 )
		// {
		// addMesureHeaderInColumn( retValue, 0 );
		// }
		List measureHandleList = getMeasreViewHandleList( );
		int columnNumber = 0;
		if ( count != 0 )
		{
			LevelViewHandle lastHandle = findLastLevelViewHandle( ICrosstabConstants.COLUMN_AXIS_TYPE );
			if (lastHandle ==  null)
			{
				throw new RuntimeException("lasthandle is null");//$NON-NLS-1$
			}
			columnNumber = addMesureHeader( retValue,
					0,
					ICrosstabConstants.COLUMN_AXIS_TYPE,
					measureHandleList, lastHandle );
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
				// add the levelHandle cell
				CrosstabCellAdapter cellAdapter = factory.createCrosstabCellAdapter( type,
						cellHandle,
						rowNumber,
						1,
						1,
						columnNumber,
						false );

				retValue.add( cellAdapter );
				// put the property for build the measure area
				map.put( levelHandle, Integer.valueOf( columnNumber ) );

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
								rowNumber - ( isVertical( ) || isHideHeader( ) ? 0 : 1 ),
								columnNumber + 1,
								isVertical( ) ? 1 : preMeasureCount,
								false );

						retValue.add( preCellAdapter );
						// add subTotal measure handle
						// if ( measureCount != 0 )
						// {
						int addCount = addMesureHeader( retValue,
								columnNumber,
								ICrosstabConstants.COLUMN_AXIS_TYPE,
								list, preLevelHandle );
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
						rowNumber - ( isVertical( ) || isHideHeader( ) ? 0 : 1 ),
						columnNumber + 1,
						isVertical( ) ? 1 : size,
						false );

				retValue.add( grandCellAdapter );
				// add subTotal measure handle
				int addCount = addMesureHeader( retValue,
						columnNumber,
						ICrosstabConstants.COLUMN_AXIS_TYPE,
						list, null );
				int beforeColumnNumber = columnNumber;
				columnNumber = columnNumber + ( addCount == 0 ? 1 : addCount );
				//int adjust = columnNumber - beforeColumnNumber;
				if (isGrandBefore( ICrosstabConstants.COLUMN_AXIS_TYPE ))
				{
					adjustGrandColumn = columnNumber - beforeColumnNumber;
				}
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
		map.put( COLUMNAREA_COLUMN, Integer.valueOf( columnNumber ) );
		map.put( COLUMNAREA_ROW, Integer.valueOf( rowNumber ) );

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

	private int addMesureHeaderToVirtual( List list, int rowNumber,
			int rowSpan, int columnNumber, int columnSpan, List measures )
	{
		int measureCount = measures.size( );
		if (isHideHeader( ))
		{
			return measureCount;
		}
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
					false );
			if ( isVertical( ) )
			{
				rowNumber++;
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
			List measures, LevelViewHandle levelHandle )
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
		if (isHideHeader( ))
		{
			return measureCount;
		}
		for ( int k = 0; k < measureCount; k++ )
		{
			MeasureViewHandle preMmeasureHandle = (MeasureViewHandle) measures.get( k );
			CrosstabCellHandle preMeasureCellHandle = preMmeasureHandle.getHeader( levelHandle );

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
	 * return the previous LevelViewHandle
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
	 * Use the old model add to model list, so the no new editpart create
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
					|| CELL_FIRST_LEVEL_HANDLE.equals( type ) )
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
			else if ( CROSSTAB_HEADER.equals( type ) )
			{
				retValue = new CrosstabHeaderHandleAdapter( handle, CrosstabHeaderHandleAdapter.CROSSTAB_HEADER );
				//( (TotalCrosstabCellHandleAdapter) retValue ).setType( CrosstabHeaderHandleAdapter.CROSSTAB_HEADER );
			}
			else if ( CELL_GRAND_TOTAL.equals( type ) )
			{
				retValue = new TotalCrosstabCellHandleAdapter( handle );
				( (TotalCrosstabCellHandleAdapter) retValue ).setType( TotalCrosstabCellHandleAdapter.GRAND_TOTAL );
			}
			else if ( CELL_MEASURE_AGGREGATION.equals( type )
					|| CELL_MEASURE.equals( type ) )
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
		System.out.println( "///////////////////////////////////" ); //$NON-NLS-1$
		for ( int i = 0; i < list.size( ); i++ )
		{
			CrosstabCellAdapter adapter = (CrosstabCellAdapter) list.get( i );

			String classNmae = adapter.getClass( ).getName( );
			int index = classNmae.lastIndexOf( "." ); //$NON-NLS-1$
			classNmae = classNmae.substring( index + 1 );
			System.out.println( "cell row==" //$NON-NLS-1$
					+ adapter.getRowNumber( )
					+ " rowSpan==" //$NON-NLS-1$
					+ adapter.getRowSpan( )
					+ " column==" //$NON-NLS-1$
					+ adapter.getColumnNumber( )
					+ " columnSpan==" //$NON-NLS-1$
					+ adapter.getColumnSpan( )
					+ "           " //$NON-NLS-1$
					+ classNmae );
		}
		System.out.println( "///////////////////////////////////" ); //$NON-NLS-1$
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
			throw new RuntimeException( "model don't build" ); //$NON-NLS-1$
		}
		Integer rowRow = (Integer) map.get( ROWAREA_ROW );
		if ( columnRow == null )
		{
			throw new RuntimeException( "model don't build" ); //$NON-NLS-1$
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
			throw new RuntimeException( "model don't build" ); //$NON-NLS-1$
		}
		Integer columnColumn = (Integer) map.get( COLUMNAREA_COLUMN );
		if ( columnColumn == null )
		{
			throw new RuntimeException( "model don't build" ); //$NON-NLS-1$
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
	 * @param number
	 * @param value
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
			CrosstabCellHandle cell = CrosstabModelUtil.locateRowHeightCell(
					getCrosstabReportItemHandle( ), handle );
			MetricUtility.updateDimension( cell.getHeight( ), value );
		}
		catch ( SemanticException e )
		{
			ExceptionUtil.handle( e );
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
	 * @param number
	 * @param value
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
			CrosstabCellHandle cell = CrosstabModelUtil.locateColumnWidthCell(
					getCrosstabReportItemHandle( ), handle );
			MetricUtility.updateDimension( cell.getWidth( ), value );
			return;
		}
		catch ( SemanticException e )
		{
			//There are some issues when show as chart.So ignore the exception.
			//ExceptionUtil.handle( e );
			//do nothing now
		}
	}

	public void setWidth( double value, String units )
	{
//		try
//		{
//			DimensionValue dimensionValue = new DimensionValue( value,
//					units);
//			DimensionHandle handle = getCrosstabReportItemHandle( ).getWidth( );
//			handle.setValue( dimensionValue );
//
//			return;
//		}
//		catch ( SemanticException e )
//		{
//			ExceptionHandler.handle( e );
//		}
	}
	
	/**
	 * @param value
	 */
	public void setWidth( int value )
	{
		setWidth(value, DesignChoiceConstants.UNITS_PX);
	}

	/**
	 * Returns the defined width in model in Pixel.
	 * 
	 * @return
	 */
	public String getDefinedWidth( )
	{
//		DimensionHandle handle = ( (ExtendedItemHandle) getAdapter( DesignElementHandle.class ) ).getWidth( );
//
//		if ( handle.getUnits( ) == null || handle.getUnits( ).length( ) == 0 )
//		{
//			// TODO The default value is 100.0% to fix the bug 124051, but it is
//			// a temp solution.
//			// default value is 100.0%
//			return DEFAULT_WIDTH;
//			// return null;
//		}
//		else if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( handle.getUnits( ) ) )
//		{
//			return handle.getMeasure( )
//					+ DesignChoiceConstants.UNITS_PERCENTAGE;
//		}
//		else
//		{
//			int px = (int) DEUtil.convertoToPixel( handle );
//
//			if ( px <= 0 )
//			{
//				return null;
//			}
//
//			return String.valueOf( px );
//		}
		
		return DEFAULT_WIDTH;
	}

	/**
	 * Comparator the cell adapter row by row , column by column ModelComparator
	 */
	private static class ModelComparator implements Comparator
	{

		public int compare( Object o1, Object o2 )
		{
			CrosstabCellAdapter part1 = (CrosstabCellAdapter) o1;
			CrosstabCellAdapter part2 = (CrosstabCellAdapter) o2;
			
			int part1Row = part1.getRowNumber( );
			int part2Row = part2.getRowNumber( );
			if ( part1Row != part2Row )
			{
				return part1Row - part2Row;
			}
			return part1.getColumnNumber( ) - part2.getColumnNumber( );
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


	private boolean isGrandBefore(int type)
	{
		CrosstabReportItemHandle handle = getCrosstabReportItemHandle( );
		return ICrosstabConstants.GRAND_TOTAL_LOCATION_BEFORE.equals( handle.getCrosstabView( type ).getGrandTotalLocation());
	}
	
	private boolean isPageLayoutOver( )
	{
		return getCrosstabReportItemHandle( ).getPageLayout( )
				.equals( ICrosstabConstants.PAGE_LAYOUT_DOWN_THEN_OVER );
	}
	
	private boolean isHideHeader()
	{
		return getCrosstabReportItemHandle( ).isHideMeasureHeader( );
	}
	
	private LevelViewHandle findLastLevelViewHandle(int type)
	{
		CrosstabReportItemHandle handle = getCrosstabReportItemHandle( );
		int count = handle.getDimensionCount( type );
		if (count == 0)
		{
			return null;
		}
		
		DimensionViewHandle dimensionHandle = handle.getDimension( type,count - 1 );
		int levelCount = dimensionHandle.getLevelCount( );
		if (levelCount == 0)
		{
			return null;
		}
		LevelViewHandle levelHandle = dimensionHandle.getLevel( levelCount - 1 );
		return levelHandle;
	}
	
	public boolean layoutCheck()
	{
		int rowCount = getRowCount( );
		int columnCount = getColumnCount( );
		CrosstabCellAdapter[][] adapters = new CrosstabCellAdapter[rowCount][columnCount];
		for (int i=0;i<oldModelList.size( ); i++)
		{
			CrosstabCellAdapter adapter = (CrosstabCellAdapter)oldModelList.get( i );
			int rowNumber = adapter.getRowNumber( );
			int rowSpan = adapter.getRowSpan( );
			int columnNumber = adapter.getColumnNumber( );
			int columnSpan = adapter.getColumnSpan( );
			for (int j=0; j<rowSpan; j++)
			{
				int adapterRow = rowNumber + j;
				if (adapterRow > rowCount)
				{
					return false;
				}
				for (int k=0; k<columnSpan; k++)
				{
					int adapterColumn = columnNumber + k;
					if (adapterColumn > columnCount)
					{
						return false;
					}
					if (adapters[adapterRow-1][adapterColumn-1] == null)
					{
						adapters[adapterRow-1][adapterColumn-1] = adapter;
					}
					else if (adapters[adapterRow-1][adapterColumn-1] != adapter)
					{
						//return false;
					}
				}
			}
		}
		for (int i=0; i<adapters.length;i++)
		{
			for (int j=0; j<adapters[i].length; j++)
			{
				if (adapters[i][j] == null)
				{
					return false;
				}
			}
		}
		return true;
	}
	//support the nultiple header cells
	
}
