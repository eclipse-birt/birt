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

package org.eclipse.birt.report.designer.internal.ui.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.schematic.ColumnHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.TableBorderHelper;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.IReportElementFigure;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.FixTableLayoutCalculator;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

/**
 * The layout manager for Table report element
 */
public class TableLayout extends XYLayout
{

	/**
	 * An insets singleton.
	 */
	private static final Insets INSETS_SINGLETON = new Insets( );

	/** The layout constraints */
	protected Map constraints = new HashMap( );
	WorkingData data = null;
	private ITableLayoutOwner owner;

	boolean needlayout = true;

	TableBorderHelper helper;

	Map<IFigure, FigureInfomation> figureInfo = new HashMap<IFigure, FigureInfomation>( );

	private boolean isCalculating = false;
	private boolean isNeedRelayout = true;

	static class FigureInfomation
	{
		public int rowNumber, columnNumber, rowSpan, columnSpan;
	}

	/**
	 * Default constructor
	 */
	public TableLayout( )
	{
		super( );
	}

	/**
	 * The constructor.
	 * 
	 * @param rowCount
	 * @param columnCount
	 */
	public TableLayout( ITableLayoutOwner part )
	{
		super( );
		this.owner = part;
	}

	/**
	 * Layout given container.
	 * 
	 * @param container
	 * @param bool
	 */
	public void layout( IFigure container, boolean bool )
	{
		boolean temp = needlayout;
		layout( container );
		if ( bool )
		{
			needlayout = temp;
		}
	}

	/**
	 * Mark dirty flag to trigger re-layout.
	 */
	public void markDirty( )
	{
		needlayout = true;
	}

	/**
	 * Returns the helper for current tableLayout.
	 */
	public TableBorderHelper getBorderHelper( )
	{
		return helper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout( IFigure container )
	{
		if ( !isDistroy( ) )
		{
			return;
		}

		//if ( !isCalculating )
		{
			helper = new TableBorderHelper( owner );

			helper.updateCellBorderInsets( );
		}

		figureInfo.clear( );

		data = new WorkingData( );
		data.columnWidths = new TableLayoutData.ColumnData[getColumnCount( )];
		data.rowHeights = new TableLayoutData.RowData[getRowCount( )];

		// initialize the default value of each cell from DE model
		init( data.columnWidths, data.rowHeights );

		// get the figure list of all cell
		List children = container.getChildren( );

		// calculate the minimum width of each cell
		initMinSize( children );

		// be not implemented yet
		initMergeMinsize( children );

		// adjust the cell data with calculated width and height
		caleLayoutData( container );

		// first pass, layout the children.
		if ( !isCalculating )
		{
			layoutTable( container );
		}

		// reset the row minimum height data.
		resetRowMinSize( data.rowHeights );

		initRowMinSize( children );
		initRowMergeMinsize( children );
		caleRowData( );

		// second pass, layout the container itself.
		if ( !isCalculating )
		{
			layoutTable( container );
		}

		setConstraint( container, data );
		needlayout = false;

		if ( isCalculating )
		{
			// return;
		}

		int containerWidth = getOwner( ).getFigure( )
				.getParent( )
				.getClientArea( )
				.getSize( ).width;

		if ( containerWidth < 0 )
		{
			Display.getCurrent( ).asyncExec( new Runnable( ) {

				public void run( )
				{
					if ( isNeedRelayout )
					{
						getOwner( ).reLayout( );
						isNeedRelayout = false;
					}
				}
			} );

			return;
		}

		reselect( );
	}

	void reselect( )
	{
		final List list = new ArrayList( ( (StructuredSelection) getOwner( ).getViewer( )
				.getSelection( ) ).toList( ) );

		boolean hasCell = false;
		for ( int i = 0; i < list.size( ); i++ )
		{
			if ( list.get( i ) instanceof ITableLayoutCell
					|| list.get( i ) instanceof ITableLayoutOwner )
			{
				hasCell = true;
				break;
			}
		}
		if ( hasCell )
		{
			Platform.run( new SafeRunnable( ) {

				public void run( )
				{
					UIUtil.resetViewSelection( getOwner( ).getViewer( ), false );
				}
			} );
		}
	}

	void layoutTable( IFigure container )
	{
		List children = container.getChildren( );
		int size = children.size( );
		//Map map = getOwner( ).getViewer( ).getVisualPartMap( );

		for ( int i = 0; i < size; i++ )
		{
			IFigure figure = (IFigure) children.get( i );
			FigureInfomation info = figureInfo.get( figure );

			int rowNumber = info.rowNumber;
			int columnNumber = info.columnNumber;

			int rowSpan = info.rowSpan;
			int columnSpan = info.columnSpan;

			int x = getColumnWidth( 1, columnNumber );
			int y = getRowHeight( 1, rowNumber );
			int width = getColumnWidth( columnNumber, columnNumber + columnSpan );
			int height = getRowHeight( rowNumber, rowNumber + rowSpan );

			// cellPart.markDirty( true, false );

			setBoundsOfChild( container, figure, new Rectangle( x,
					y,
					width,
					height ) );
		}
	}

	private int getRowHeight( int start, int end )
	{
		int retValue = 0;
		for ( int i = start; i < end; i++ )
		{
			retValue = retValue + data.rowHeights[i - 1].height;
		}
		return retValue;
	}

	private int getColumnWidth( int start, int end )
	{
		int retValue = 0;
		for ( int i = start; i < end; i++ )
		{
			retValue = retValue + data.columnWidths[i - 1].width;
		}
		return retValue;
	}

	protected void setBoundsOfChild( IFigure parent, IFigure child,
			Rectangle bounds )
	{
		parent.getClientArea( Rectangle.SINGLETON );
		bounds.translate( Rectangle.SINGLETON.x, Rectangle.SINGLETON.y );

		// comment out to force invalidation.
		// if ( !bounds.equals( child.getBounds( ) ) )
		{
			child.setBounds( bounds );
			if ( child.getLayoutManager( ) != null )
				child.getLayoutManager( ).invalidate( );
			child.revalidate( );
		}
	}

	private void resetRowMinSize( TableLayoutData.RowData[] rowHeights )
	{
		int size = rowHeights.length;

		for ( int i = 1; i < size + 1; i++ )
		{
			// rowHeights[i - 1] = new TableLayoutData.RowData( );
			// rowHeights[i - 1].rowNumber = i;
			// Object obj = getOwner( ).getRow( i );
			// RowHandleAdapter adapt = HandleAdapterFactory.getInstance( )
			// .getRowHandleAdapter( obj );
			// rowHeights[i - 1].height = adapt.getHeight( );
			// rowHeights[i - 1].isForce = adapt.isCustomHeight( );
			//
			// //add to handle percentage case.
			// DimensionHandle dim = ( (RowHandle) adapt.getHandle( ) )
			// .getHeight( );
			rowHeights[i - 1] = new TableLayoutData.RowData( );
			rowHeights[i - 1].rowNumber = i;

			rowHeights[i - 1].height = getOwner( ).getRowHeightValue( i );

			// add to handle percentage case.
			ITableLayoutOwner.DimensionInfomation dim = getOwner( ).getRowHeight( i );

			rowHeights[i - 1].isForce = dim.getMeasure( ) > 0;
			if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( dim.getUnits( ) )
					&& dim.getMeasure( ) > 0 )
			{
				rowHeights[i - 1].isPercentage = true;
				rowHeights[i - 1].percentageHeight = dim.getMeasure( );
			}

			// add to handle auto case;
			if ( dim.getUnits( ) == null || dim.getUnits( ).length( ) == 0 )
			{
				rowHeights[i - 1].isAuto = true;
			}

			// add by gao 2004.11.22
			rowHeights[i - 1].trueMinRowHeight = ( rowHeights[i - 1].isForce && !rowHeights[i - 1].isPercentage ) ? rowHeights[i - 1].height
					: rowHeights[i - 1].minRowHeight;
					
			if (rowHeights[i - 1].trueMinRowHeight < RowHandleAdapter.DEFAULT_MINHEIGHT)
			{
				rowHeights[i - 1].trueMinRowHeight = RowHandleAdapter.DEFAULT_MINHEIGHT;
			}
		}
	}

	private void initRowMinSize( List children )
	{
		int size = children.size( );
		//Map map = getOwner( ).getViewer( ).getVisualPartMap( );

		for ( int i = 0; i < size; i++ )
		{
			IFigure figure = (IFigure) children.get( i );
			FigureInfomation info = figureInfo.get( figure );

			int rowNumber = info.rowNumber;
			int columnNumber = info.columnNumber;

			int rowSpan = info.rowSpan;
			int columnSpan = info.columnSpan;

			TableLayoutData.RowData rowData = data.findRowData( rowNumber );
			TableLayoutData.ColumnData columnData = data.findColumnData( columnNumber );

			int colWidth = columnData.width;

			if ( columnSpan > 1 )
			{
				for ( int k = 1; k < columnSpan; k++ )
				{
					TableLayoutData.ColumnData cData = data.findColumnData( columnNumber
							+ k );

					if ( cData != null )
					{
						colWidth += cData.width;
					}
				}
			}

			Dimension dim = figure.getMinimumSize( colWidth, -1 );

			if ( dim.height > rowData.minRowHeight && rowSpan == 1 )
			{
				rowData.minRowHeight = dim.height;
			}

			if ( dim.height > rowData.trueMinRowHeight && rowSpan == 1 )
			{
				rowData.trueMinRowHeight = dim.height;
				rowData.isSetting = true;
			}
		}
	}

	private void initRowMergeMinsize( List children )
	{
		int size = children.size( );
		//Map map = getOwner( ).getViewer( ).getVisualPartMap( );
		List list = new ArrayList( );
		List adjustRow = new ArrayList( );

		for ( int i = 0; i < size; i++ )
		{
			IFigure figure = (IFigure) children.get( i );
			FigureInfomation info = figureInfo.get( figure );

			int rowNumber = info.rowNumber;
			int rowSpan = info.rowSpan;

			if ( rowSpan == 1 )
			{
				continue;
			}

			list.add( figure );

			if ( rowSpan > 1 )
			{
				for ( int j = rowNumber; j < rowNumber + rowSpan; j++ )
				{
					adjustRow.add( Integer.valueOf( j ) );
				}
			}
		}

		caleRowMergeMinHeight( list, adjustRow, new ArrayList( ) );

	}

	private void caleRowMergeMinHeight( List figures, List adjust,
			List hasAdjust )
	{
		if ( adjust.isEmpty( ) )
		{
			return;
		}
		int size = figures.size( );
		//Map map = getOwner( ).getViewer( ).getVisualPartMap( );
		int adjustMax = 0;
		int trueAdjustMax = 0;
		int adjustMaxNumber = 0;

		for ( int i = 0; i < size; i++ )
		{
			IFigure figure = (IFigure) figures.get( i );
			FigureInfomation info = figureInfo.get( figure );

			int rowNumber = info.rowNumber;
			int columnNumber = info.columnNumber;

			int rowSpan = info.rowSpan;
			// int columnSpan = info.columnSpan;

			Dimension minSize = figure.getMinimumSize( data.findColumnData( columnNumber ).width,
					-1 );

			int samMin = 0;
			int trueSamMin = 0;

			int[] adjustNumber = new int[0];
			for ( int j = rowNumber; j < rowNumber + rowSpan; j++ )
			{
				TableLayoutData.RowData rowData = data.findRowData( j );
				if ( !hasAdjust.contains( Integer.valueOf( j ) ) )
				{
					int len = adjustNumber.length;
					int temp[] = new int[len + 1];
					System.arraycopy( adjustNumber, 0, temp, 0, len );
					temp[len] = j;
					adjustNumber = temp;
				}
				else
				{
					samMin = samMin + rowData.trueMinRowHeight;
					trueSamMin = trueSamMin + rowData.trueMinRowHeight;
				}
			}
			int adjustCount = adjustNumber.length;
			if ( adjustCount == 0 )
			{
				continue;
			}
			int value = minSize.height - samMin;
			int trueValue = minSize.height - trueSamMin;
			for ( int j = 0; j < adjustCount; j++ )
			{
				int temp = 0;
				int trueTemp = 0;
				if ( j == adjustCount - 1 )
				{
					temp = value / adjustCount + value % adjustCount;
					trueTemp = trueValue
							/ adjustCount
							+ trueValue
							% adjustCount;
				}
				else
				{
					temp = value / adjustCount;
					trueTemp = trueValue / adjustCount;
				}
				TableLayoutData.RowData rowData = data.findRowData( adjustNumber[j] );
				temp = Math.max( temp, rowData.minRowHeight );
				trueTemp = Math.max( trueTemp, rowData.trueMinRowHeight );

				if ( trueTemp > trueAdjustMax )
				{
					adjustMax = temp;
					trueAdjustMax = trueTemp;
					adjustMaxNumber = adjustNumber[j];
				}
			}
		}

		if ( adjustMaxNumber > 0 )
		{
			TableLayoutData.RowData rowData = data.findRowData( adjustMaxNumber );
			rowData.minRowHeight = adjustMax;
			rowData.trueMinRowHeight = trueAdjustMax;
			adjust.remove( Integer.valueOf( adjustMaxNumber ) );
			hasAdjust.add( Integer.valueOf( adjustMaxNumber ) );
			caleMergeMinHeight( figures, adjust, hasAdjust );
		}
	}

	private void caleRowData( )
	{

		if ( data == null )
		{
			return;
		}

		int size = data.rowHeights.length;
		int dxRows[] = new int[size];
		int dxTotal = 0;
		for ( int i = 0; i < size; i++ )
		{
			dxRows[i] = data.rowHeights[i].height
					- data.rowHeights[i].trueMinRowHeight;
			dxTotal = dxTotal + dxRows[i];
		}

		for ( int i = 0; i < size; i++ )
		{
			if ( dxRows[i] < 0 )
			{
				data.rowHeights[i].height = data.rowHeights[i].trueMinRowHeight;
			}
		}

	}

	private void caleLayoutData( IFigure container )
	{
		if ( data == null )
		{
			return;
		}

		/**
		 * layout row, the row/column layout sequence can be changed.
		 */
		int size = data.rowHeights.length;
		int dxRows[] = new int[size];
		int dxTotal = 0;

		for ( int i = 0; i < size; i++ )
		{
			dxRows[i] = data.rowHeights[i].height
					- data.rowHeights[i].trueMinRowHeight;
			dxTotal = dxTotal + dxRows[i];
		}

		for ( int i = 0; i < size; i++ )
		{
			if ( dxRows[i] < 0 )
			{
				data.rowHeights[i].height = data.rowHeights[i].trueMinRowHeight;
			}
		}

		/**
		 * layout column
		 */
		size = data.columnWidths.length;

		int containerWidth = getLayoutWidth( );

		containerWidth = Math.max( 0, containerWidth );

		String[] definedWidth = new String[size];
		for ( int i = 1; i < size + 1; i++ )
		{
			definedWidth[i - 1] = getOwner( ).getRawWidth( i );
		}

		FixTableLayoutCalculator calculator = new FixTableLayoutCalculator( );
		calculator.setTableWidth( containerWidth );
		calculator.setColMinSize( ColumnHandleAdapter.DEFAULT_MINWIDTH );
		calculator.setDefinedColWidth( definedWidth );

		TableLayoutHelper.calculateColumnWidth( data.columnWidths,
				containerWidth,
				calculator );
	}

	Insets getFigureMargin( IFigure f )
	{
		if ( f instanceof IReportElementFigure )
		{
			return ( (IReportElementFigure) f ).getMargin( );
		}

		return INSETS_SINGLETON;
	}

	private int getDefinedWidth( String dw, int cw )
	{
		if ( dw == null || dw.length( ) == 0 )
		{
			return 0;
		}

		try
		{
			if ( dw.endsWith( "%" ) ) //$NON-NLS-1$
			{
				return (int) ( Double.parseDouble( dw.substring( 0,
						dw.length( ) - 1 ) )
						* cw / 100 );
			}

			return (int) Double.parseDouble( dw );
		}
		catch ( NumberFormatException e )
		{
			// ignore.
		}

		return 0;
	}

	private void initMinSize( List children )
	{
		int size = children.size( );
		Map map = getOwner( ).getViewer( ).getVisualPartMap( );

		for ( int i = 0; i < size; i++ )
		{
			IFigure figure = (IFigure) children.get( i );
			ITableLayoutCell cellPart = (ITableLayoutCell) map.get( figure );

			int rowNumber = cellPart.getRowNumber( );
			int columnNumber = cellPart.getColumnNumber( );
			int rowSpan = cellPart.getRowSpan( );
			int columnSpan = cellPart.getColSpan( );

			FigureInfomation info = new FigureInfomation( );
			info.rowNumber = rowNumber;
			info.columnNumber = columnNumber;
			info.rowSpan = rowSpan;
			info.columnSpan = columnSpan;

			figureInfo.put( figure, info );

			// may be implement a interface
			// get minimum size of cell figure
			Dimension dim = figure.getMinimumSize( );

			TableLayoutData.RowData rowData = data.findRowData( rowNumber );
			TableLayoutData.ColumnData columnData = data.findColumnData( columnNumber );

			if ( dim.height > rowData.minRowHeight && rowSpan == 1 )
			{
				rowData.minRowHeight = dim.height;
			}

			if ( dim.height > rowData.trueMinRowHeight && rowSpan == 1 )
			{
				rowData.trueMinRowHeight = dim.height;
				rowData.isSetting = true;
			}

			// max(defaultValue,MCW)
			if ( dim.width > columnData.minColumnWidth && columnSpan == 1 )
			{
				columnData.minColumnWidth = dim.width;
			}

			if ( dim.width > columnData.trueMinColumnWidth && columnSpan == 1 )
			{
				columnData.trueMinColumnWidth = dim.width;
				columnData.isSetting = true;
			}

		}
	}

	private void initMergeMinsize( List children )
	{
		int size = children.size( );
		//Map map = getOwner( ).getViewer( ).getVisualPartMap( );
		List list = new ArrayList( );
		List adjustRow = new ArrayList( );
		List adjustColumn = new ArrayList( );

		for ( int i = 0; i < size; i++ )
		{
			IFigure figure = (IFigure) children.get( i );
			// ITableLayoutCell cellPart = (ITableLayoutCell) map.get( figure );
			FigureInfomation info = figureInfo.get( figure );

			int rowNumber = info.rowNumber;
			int columnNumber = info.columnNumber;

			int rowSpan = info.rowSpan;
			int columnSpan = info.columnSpan;

			if ( rowSpan == 1 && columnSpan == 1 )
			{
				continue;
			}

			list.add( figure );

			if ( rowSpan > 1 )
			{
				for ( int j = rowNumber; j < rowNumber + rowSpan; j++ )
				{
					adjustRow.add( Integer.valueOf( j ) );
				}
			}

			if ( columnSpan > 1 )
			{
				for ( int j = columnNumber; j < columnNumber + columnSpan; j++ )
				{
					adjustColumn.add( Integer.valueOf( j ) );
				}
			}
		}

		caleMergeMinHeight( list, adjustRow, new ArrayList( ) );
		caleMergeMinWidth( list, adjustColumn, new ArrayList( ) );
	}

	private void caleMergeMinHeight( List figures, List adjust, List hasAdjust )
	{
		if ( adjust.isEmpty( ) )
		{
			return;
		}

		int size = figures.size( );
		//Map map = getOwner( ).getViewer( ).getVisualPartMap( );
		int adjustMax = 0;
		int trueAdjustMax = 0;
		int adjustMaxNumber = 0;

		for ( int i = 0; i < size; i++ )
		{
			IFigure figure = (IFigure) figures.get( i );
			FigureInfomation info = figureInfo.get( figure );

			int rowNumber = info.rowNumber;

			int rowSpan = info.rowSpan;

			Dimension minSize = figure.getMinimumSize( data.findColumnData( info.columnNumber ).width,
					-1 );
			int samMin = 0;
			int trueSamMin = 0;

			int[] adjustNumber = new int[0];
			for ( int j = rowNumber; j < rowNumber + rowSpan; j++ )
			{
				TableLayoutData.RowData rowData = data.findRowData( j );
				if ( !hasAdjust.contains( Integer.valueOf( j ) ) )
				{
					int len = adjustNumber.length;
					int temp[] = new int[len + 1];
					System.arraycopy( adjustNumber, 0, temp, 0, len );
					temp[len] = j;
					adjustNumber = temp;
				}
				else
				{
					samMin = samMin + rowData.trueMinRowHeight;
					trueSamMin = trueSamMin + rowData.trueMinRowHeight;
				}
			}

			int adjustCount = adjustNumber.length;
			if ( adjustCount == 0 )
			{
				continue;
			}

			int value = minSize.height - samMin;
			int trueValue = minSize.height - trueSamMin;
			// int trueAvage = minSize.h/adjustCount;
			for ( int j = 0; j < adjustCount; j++ )
			{
				int temp = 0;
				int trueTemp = 0;
				if ( j == adjustCount - 1 )
				{
					temp = value / adjustCount + value % adjustCount;
					trueTemp = trueValue
							/ adjustCount
							+ trueValue
							% adjustCount;
				}
				else
				{
					temp = value / adjustCount;
					trueTemp = trueValue / adjustCount;
				}
				TableLayoutData.RowData rowData = data.findRowData( adjustNumber[j] );
				temp = Math.max( temp, rowData.minRowHeight );
				trueTemp = Math.max( trueTemp, rowData.trueMinRowHeight );

				if ( trueTemp > trueAdjustMax )
				{
					adjustMax = temp;
					trueAdjustMax = trueTemp;
					adjustMaxNumber = adjustNumber[j];
				}
			}
		}

		if ( adjustMaxNumber > 0 )
		{
			TableLayoutData.RowData rowData = data.findRowData( adjustMaxNumber );
			rowData.minRowHeight = adjustMax;
			rowData.trueMinRowHeight = trueAdjustMax;
			adjust.remove( Integer.valueOf( adjustMaxNumber ) );
			hasAdjust.add( Integer.valueOf( adjustMaxNumber ) );
			caleMergeMinHeight( figures, adjust, hasAdjust );
		}
	}

	private void caleMergeMinWidth( List figures, List adjust, List hasAdjust )
	{
		if ( adjust.isEmpty( ) )
		{
			return;
		}

		int size = figures.size( );
		//Map map = getOwner( ).getViewer( ).getVisualPartMap( );
		int adjustMax = 0;
		int trueAdjustMax = 0;
		int adjustMaxNumber = 0;

		for ( int i = 0; i < size; i++ )
		{
			IFigure figure = (IFigure) figures.get( i );
			// ITableLayoutCell cellPart = (ITableLayoutCell) map.get( figure );
			FigureInfomation info = figureInfo.get( figure );

			int columnNumber = info.columnNumber;
			int columnSpan = info.columnSpan;

			Dimension minSize = figure.getMinimumSize( );
			int samMin = 0;
			int trueSamMin = 0;

			int[] adjustNumber = new int[0];
			for ( int j = columnNumber; j < columnNumber + columnSpan; j++ )
			{
				TableLayoutData.ColumnData columnData = data.findColumnData( j );

				if ( !hasAdjust.contains( Integer.valueOf( j ) ) )
				{
					int len = adjustNumber.length;
					int temp[] = new int[len + 1];
					System.arraycopy( adjustNumber, 0, temp, 0, len );
					temp[len] = j;
					adjustNumber = temp;
				}
				else
				{
					samMin = samMin + columnData.trueMinColumnWidth;
					trueSamMin = trueSamMin + columnData.trueMinColumnWidth;
				}
			}

			int adjustCount = adjustNumber.length;
			if ( adjustCount == 0 )
			{
				continue;
			}

			int value = minSize.width - samMin;
			int trueValue = minSize.width - trueSamMin;

			for ( int j = 0; j < adjustCount; j++ )
			{
				int temp = 0;
				int trueTemp = 0;
				if ( j == adjustCount - 1 )
				{
					temp = value / adjustCount + value % adjustCount;
					trueTemp = trueValue
							/ adjustCount
							+ trueValue
							% adjustCount;
				}
				else
				{
					temp = value / adjustCount;
					trueTemp = trueValue / adjustCount;
				}

				TableLayoutData.ColumnData columnData = data.findColumnData( adjustNumber[j] );
				temp = Math.max( temp, columnData.minColumnWidth );
				trueTemp = Math.max( trueTemp, columnData.trueMinColumnWidth );

				if ( trueTemp > trueAdjustMax )
				{
					adjustMax = temp;
					trueAdjustMax = trueTemp;
					adjustMaxNumber = adjustNumber[j];
				}
			}
		}

		if ( adjustMaxNumber > 0 )
		{
			TableLayoutData.ColumnData columnData = data.findColumnData( adjustMaxNumber );
			columnData.minColumnWidth = adjustMax;
			columnData.trueMinColumnWidth = trueAdjustMax;
			adjust.remove( Integer.valueOf( adjustMaxNumber ) );
			hasAdjust.add( Integer.valueOf( adjustMaxNumber ) );
			caleMergeMinWidth( figures, adjust, hasAdjust );
		}

	}

	private void init( TableLayoutData.ColumnData[] columnWidths,
			TableLayoutData.RowData[] rowHeights )
	{
		int size = rowHeights.length;
		for ( int i = 1; i < size + 1; i++ )
		{
			rowHeights[i - 1] = new TableLayoutData.RowData( );
			rowHeights[i - 1].rowNumber = i;

			rowHeights[i - 1].height = getOwner( ).getRowHeightValue( i );

			// add to handle percentage case.
			ITableLayoutOwner.DimensionInfomation dim = getOwner( ).getRowHeight( i );

			rowHeights[i - 1].isForce = dim.getMeasure( ) > 0;

			if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( dim.getUnits( ) )
					&& dim.getMeasure( ) > 0 )
			{
				rowHeights[i - 1].isPercentage = true;
				rowHeights[i - 1].percentageHeight = dim.getMeasure( );
			}

			// add to handle auto case;
			if ( dim.getUnits( ) == null || dim.getUnits( ).length( ) == 0 )
			{
				rowHeights[i - 1].isAuto = true;
			}

			// add by gao 2004.11.22
			rowHeights[i - 1].trueMinRowHeight = ( rowHeights[i - 1].isForce && !rowHeights[i - 1].isPercentage ) ? rowHeights[i - 1].height
					: rowHeights[i - 1].minRowHeight;
					
			if (rowHeights[i - 1].trueMinRowHeight < RowHandleAdapter.DEFAULT_MINHEIGHT)
			{
				rowHeights[i - 1].trueMinRowHeight = RowHandleAdapter.DEFAULT_MINHEIGHT;
			}

		}

		size = columnWidths.length;
		for ( int i = 1; i < size + 1; i++ )
		{
			columnWidths[i - 1] = new TableLayoutData.ColumnData( );
			columnWidths[i - 1].columnNumber = i;

			columnWidths[i - 1].width = getOwner( ).getColumnWidthValue( i );

			// add to handle percentage case.
			ITableLayoutOwner.DimensionInfomation dim = getOwner( ).getColumnWidth( i );

			columnWidths[i - 1].isForce = dim.getMeasure( ) > 0;
			if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( dim.getUnits( ) )
					&& dim.getMeasure( ) > 0 )
			{
				columnWidths[i - 1].isPercentage = true;
				columnWidths[i - 1].percentageWidth = dim.getMeasure( );
			}

			// add to handle auto case;
			if ( dim.getUnits( ) == null || dim.getUnits( ).length( ) == 0 )
			{
				columnWidths[i - 1].isAuto = true;
			}

			// added by gao 2004.11.22
			columnWidths[i - 1].trueMinColumnWidth = ( columnWidths[i - 1].isForce && !columnWidths[i - 1].isPercentage ) ? columnWidths[i - 1].width
					: columnWidths[i - 1].minColumnWidth;
		}

	}

	/**
	 * @see LayoutManager#getConstraint(IFigure)
	 */
	public Object getConstraint( IFigure figure )
	{
		return constraints.get( figure );
	}

	/**
	 * @see LayoutManager#remove(IFigure)
	 */
	public void remove( IFigure figure )
	{
		super.remove( figure );
		constraints.remove( figure );
	}

	/**
	 * Sets the layout constraint of the given figure. The constraints can only
	 * be of type {@link Rectangle}.
	 * 
	 * @see LayoutManager#setConstraint(IFigure, Object)
	 */
	public void setConstraint( IFigure figure, Object newConstraint )
	{
		super.setConstraint( figure, newConstraint );
		if ( newConstraint != null )
			constraints.put( figure, newConstraint );
	}

	/**
	 * @return column count
	 */
	public int getColumnCount( )
	{
		return getOwner( ).getColumnCount( );
	}

	/**
	 * Gets row count of Row
	 * 
	 * @return
	 */
	public int getRowCount( )
	{
		return getOwner( ).getRowCount( );
	}

	/**
	 * Keeps table layout information includes columns width, rows height
	 * 
	 */
	public static class WorkingData
	{

		public TableLayoutData.ColumnData columnWidths[];
		public TableLayoutData.RowData rowHeights[];

		public TableLayoutData.RowData findRowData( int number )
		{
			return rowHeights[number - 1];
		}

		public TableLayoutData.ColumnData findColumnData( int number )
		{
			return columnWidths[number - 1];
		}
	}

	/**
	 * Gets the table edit part of, which owned this layout manager
	 * 
	 * @return
	 */
	public ITableLayoutOwner getOwner( )
	{
		return owner;
	}

	protected Dimension calculateMinimumSize( IFigure figure, int wHint,
			int hHint )
	{
		isCalculating = true;
		layout( figure, true );
		isCalculating = false;
		IFigure table = figure.getParent( ).getParent( ).getParent( );
		int widthExpand = table.getInsets( ).getWidth( );

		int width = 0;
		int size = data.columnWidths.length;
		for ( int i = 0; i < size; i++ )
		{
			width = width + data.columnWidths[i].trueMinColumnWidth;
		}

		String ww = getOwner( ).getDefinedWidth( );

		if ( ww != null
				&& ww.length( ) > 0
				&& !ww.endsWith( DesignChoiceConstants.UNITS_PERCENTAGE ) )
		{
			try
			{
				int dwidth = Integer.parseInt( ww );

				if ( dwidth > width + widthExpand )
				{
					width = dwidth - widthExpand;
				}
			}
			catch ( Exception e )
			{
				// ignore;
			}
		}

		int height = 0;
		size = data.rowHeights.length;
		for ( int i = 0; i < size; i++ )
		{
			height = height + data.rowHeights[i].height;
		}
		Dimension dim = new Dimension( width, height );

		return dim.expand( table.getInsets( ).getWidth( ), table.getInsets( )
				.getHeight( ) );
	}

	/**
	 * @see org.eclipse.draw2d.LayoutManager#getMinimumSize(org.eclipse.draw2d.IFigure,
	 *      int, int)
	 */
	public Dimension getMinimumSize( IFigure container, int wHint, int hHint )
	{
		return calculateMinimumSize( container, wHint, hHint );
	}
	
	protected boolean isDistroy()
	{
		// TODO this is a temp fix to check the validity of the layout owner, may
		// need refactor ITableLayoutOwner interface to provide this info.
		if ( owner instanceof ReportElementEditPart
				&& ( (ReportElementEditPart) owner ).isDelete( ) )
		{
			return false;
		}
		
		return !(data != null && data.columnWidths != null
			&& data.columnWidths.length == getColumnCount( )
			&& data.rowHeights != null
			&& data.rowHeights.length == getRowCount( ) && !needlayout
			|| !owner.isActive( ));
	}
	
	protected int getLayoutWidth()
	{
		int containerWidth = getOwner( ).getFigure( ).getParent( )
		.getClientArea( ).getSize( ).width;

		containerWidth -= getFigureMargin( getOwner( ).getFigure( ) )
			.getWidth( );


		String ww = getOwner( ).getDefinedWidth( );

		containerWidth = getDefinedWidth( ww, containerWidth );


		int padding = getOwner( ).getFigure( ).getBorder( ).getInsets(
				getOwner( ).getFigure( ) ).getWidth( );

		containerWidth -= padding;
		
		return containerWidth;
	}
}