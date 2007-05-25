/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.executor.buffermgr;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.layout.LayoutUtil;
import org.eclipse.birt.report.engine.layout.html.HTMLTableLayoutEmitter.CellContent;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class TableContentLayout
{

	/**
	 * rows in the table layout
	 */
	Row[] rows;

	int rowCount;
	int colCount;
	int realColCount;

	int rowBufferSize;
	int colBufferSize;

	boolean isRowHidden;

	String format;
	ArrayList hiddenList = new ArrayList( );
	
	protected UnresolvedRowHint rowHint;
	
	
	protected boolean formalized = false;
	
	public TableContentLayout( ITableContent tableContent, String format )
	{
		this.format = format;
		this.colCount = tableContent.getColumnCount( );
		
		for ( int i = 0; i < colCount; i++ )
		{
			IColumn column = tableContent.getColumn( i );
			if ( isColumnHidden( column ) )
			{
				hiddenList.add( new Integer( i ) );
			}
		}
		this.realColCount = colCount - hiddenList.size( );
	}
	
	public void setUnresolvedRowHint(UnresolvedRowHint rowHint)
	{
		this.rowHint = rowHint;
	}
	
	
	public void endRow(IRowContent rowContent)
	{
		if(isRowHidden)
		{
			return;
		}
		
		if(rowHint!=null && !formalized && !LayoutUtil.isRepeatableRow(rowContent) )
		{
			//formalized
			Row row = rows[rowCount-1];
			Cell[] cells = row.cells;
			for ( int cellId = 0; cellId < realColCount; cellId++ )
			{
				Cell cell = cells[cellId];
				if ( cell != null )
				{
					//fill empty cell or remove dropped cell
					if ( cell.status == Cell.CELL_EMPTY
							|| ( cell.status == Cell.CELL_USED )
							&& ( rowHint.isDropColumn( cellId ) ) )
					{
						IReportContent report = rowContent.getReportContent( );
						ICellContent cellContent = report.createCellContent( );
						rowHint.initUnresolvedCell( cellContent, rowContent
								.getInstanceID( ), cellId );
						cellContent.setParent( rowContent );
						int rowSpan = cellContent.getRowSpan( );
						int colSpan = cellContent.getColSpan( );

						Cell newCell = Cell.createCell( row.rowId, cellId,
								rowSpan, colSpan, new CellContent( cellContent,
										null ) );
						row.cells[cellId] = newCell;

						for ( int i = cellId + 1; i < cellId + colSpan; i++ )
						{
							row.cells[i] = Cell.createSpanCell( row.rowId, i,
									newCell );
						}
					}
				}
			}
			formalized = true;
			rowHint = null;
		}
	}

	/**
	 * reset the table model.
	 * 
	 */
	public void reset( )
	{
		//keepUnresolvedCells( );
		fillEmptyCells( 0, 0, rowBufferSize, colBufferSize );
		rowCount = 0;
		isRowHidden = false;
	}

	public int getRowCount( )
	{
		return rowCount;
	}

	public int getColCount( )
	{
		return realColCount;
	}

	
	/**
	 * create a row in the table model
	 * 
	 * @param content
	 *            row content
	 */
	public Row createRow( Object rowContent )
	{
		if ( !isRowHidden( rowContent ) ) 
		{
			isRowHidden = false;
			ensureSize( rowCount + 1, realColCount );
			Row row = rows[rowCount];
			row.rowId = rowCount;
			row.content = rowContent;

			if ( rowCount > 0 )
			{
				Cell[] cells = row.cells;
				// update the status of last row
				Cell[] lastCells = rows[rowCount - 1].cells;;
				for ( int cellId = 0; cellId < realColCount; cellId++ )
				{
					Cell lastCell = lastCells[cellId];
					if ( lastCell.status == Cell.CELL_SPANED )
					{
						lastCell = lastCell.getCell( );
					}
					if ( lastCell.status == Cell.CELL_USED )
					{
						if ( lastCell.rowSpan < 0
								|| lastCell.rowId + lastCell.rowSpan > rowCount )
						{
							cells[cellId] = Cell.createSpanCell( rowCount,
									cellId, lastCell );
						}
					}
				}
			}
			rowCount++;
			return row;
		}
		isRowHidden = true;
		if ( rowCount > 0 )
		{
			// update the status of last row
			Cell[] lastCells = rows[rowCount - 1].cells;;
			for ( int cellId = 0; cellId < realColCount; cellId++ )
			{
				Cell lastCell = lastCells[cellId];
				if ( lastCell.status == Cell.CELL_SPANED )
				{
					lastCell = lastCell.getCell( );
				}
				if ( lastCell.status == Cell.CELL_USED )
				{
					if ( lastCell.rowId + lastCell.rowSpan >= rowCount + 1 )
					{
						lastCell.rowSpan--;
					}
				}
			}
		}
		return null;
		

	}


	/**
	 * create a cell in the current row.
	 * 
	 * if the cell content is not empty put it into the table if the cell is
	 * empty: if the cell has been used, drop the cell else, put it into the
	 * table.
	 * 
	 * @param cellId
	 *            column index of the cell.
	 * @param rowSpan
	 *            row span of the cell
	 * @param colSpan
	 *            col span of the cell
	 * @param content
	 *            cell content
	 */
	public void createCell( int cellId, int rowSpan, int colSpan,
			Cell.Content content )
	{
		if ( isRowHidden )
		{
			return;
		}
		// assert(cellId>0 && cellId<=colCount);
		// resolve real columnNumber and columnSpan
		int columnNumber = cellId;
		int columnSpan = colSpan;
		if ( hiddenList.size( ) > 0 )
		{
			for ( int i = 0; i < hiddenList.size( ); i++ )
			{
				int hCol = ( (Integer) hiddenList.get( i ) ).intValue( );
				if ( hCol < cellId )
				{
					columnNumber--;
				}
				else if ( hCol >= cellId && hCol < colSpan + cellId )
				{
					columnSpan--;
				}
			}
		}
		if ( columnSpan < 1 )
		{
			return;
		}
		assert ( columnNumber >= 0 );
		assert ( columnNumber + columnSpan <= realColCount );
		ensureSize( rowCount, columnNumber + columnSpan );
		

		Cell cell = rows[rowCount - 1].cells[columnNumber];
		int status = cell.getStatus( );

		if ( status == Cell.CELL_EMPTY )
		{
			Cell newCell = Cell.createCell( rows[rowCount - 1].rowId,
					columnNumber, rowSpan, columnSpan, content );

			Cell[] cells = rows[rowCount - 1].cells;
			rows[rowCount - 1].cells[columnNumber] = newCell;
			for ( int i = columnNumber + 1; i < columnNumber + columnSpan; i++ )
			{
				cells[i] = Cell.createSpanCell( rows[rowCount - 1].rowId, i,
						newCell );
			}
		}
		else
		{
			// FIXME resolve conflict
		}

	}
	
	

	public void resolveDropCells( boolean finished )
	{
		if ( rowCount <= 0 )
		{
			return;
		}
		if(!finished)
		{
			keepUnresolvedCells( );
		}
		Cell[] cells = rows[rowCount - 1].cells;
		for ( int cellId = 0; cellId < realColCount; cellId++ )
		{
			if(cells[cellId]!=null)
			{
				if(cells[cellId].getRowSpan( )!=1)
				{
					Cell cell = cells[cellId].getCell( );
					cell.rowSpan = rows[rowCount - 1].rowId - cell.rowId + 1;
				}
				cellId = cellId + cells[cellId].getColSpan( ) - 1;
			}
		}
		return;
	}

	public void resolveDropCells( int bandId, boolean finished )
	{
		if ( rowCount <= 0 )
		{
			return;
		}
		if(!finished)
		{
			keepUnresolvedCells( );
		}
		Cell[] cells = rows[rowCount - 1].cells;

		for ( int cellId = 0; cellId < realColCount; cellId++ )
		{
			if(cells[cellId]!=null)
			{
				Cell cell = cells[cellId].getCell( );
				if(cell.getRowSpan( )== bandId)
				{
					cell.rowSpan = rows[rowCount - 1].rowId - cell.rowId + 1;
				}
				cellId = cellId + cells[cellId].getColSpan( ) - 1;
			}
		}
	}
	
	public boolean hasUnResolvedRow()
	{
		return rowHint!=null;
	}

	public boolean hasDropCell( )
	{
		if ( rowCount <= 0 )
		{
			return false;
		}
		
		Cell[] cells = rows[rowCount - 1].cells;
		for ( int cellId = 0; cellId < realColCount; cellId++ )
		{
			Cell cell = cells[cellId];
			
			if(cell!=null )
			{
				int rowSpan = cell.getRowSpan( );
				
				if(rowSpan<0 || rowSpan>1)
				{
					return true;
				}
			}
		}
		return false;
	}

	protected void ensureSize( int newRowBufferSize, int newColBufferSize )
	{
		if ( newRowBufferSize > rowBufferSize )
		{
			Row[] newRows = new Row[newRowBufferSize];
			if ( rows != null )
			{
				System.arraycopy( rows, 0, newRows, 0, rowCount );
			}
			for ( int rowId = rowBufferSize; rowId < newRowBufferSize; rowId++ )
			{
				Row row = new Row( rowId );
				Cell[] cells = new Cell[colBufferSize];
				for ( int colId = 0; colId < colBufferSize; colId++ )
				{
					cells[colId] = Cell.EMPTY_CELL;
				}
				row.cells = cells;
				newRows[rowId] = row;
			}
			rows = newRows;
			rowBufferSize = newRowBufferSize;
		}

		if ( newColBufferSize > colBufferSize )
		{
			for ( int rowId = 0; rowId < rowBufferSize; rowId++ )
			{
				Row row = rows[rowId];
				Cell[] newCells = new Cell[newColBufferSize];
				if ( row.cells != null )
				{
					System.arraycopy( row.cells, 0, newCells, 0, colBufferSize );
				}
				for ( int colId = colBufferSize; colId < newColBufferSize; colId++ )
				{
					newCells[colId] = Cell.EMPTY_CELL;
				}
				row.cells = newCells;
			}
			colBufferSize = newColBufferSize;
		}
	}

	/**
	 * fill empty cells in the table.
	 * 
	 * @param rowId
	 *            row index
	 * @param colId
	 *            col index
	 * @param rowSize
	 *            fill area size
	 * @param colSize
	 *            fill area size
	 */
	protected void fillEmptyCells( int rowId, int colId, int rowSize,
			int colSize )
	{
		int lastRowId = rowId + rowSize;
		int lastColId = colId + colSize;
		if ( lastRowId > rowCount )
			lastRowId = rowCount;
		if ( lastColId > colCount )
			lastColId = colCount;

		for ( int i = rowId; i < lastRowId; i++ )
		{
			Cell[] cells = rows[i].cells;
			for ( int j = colId; j < lastColId; j++ )
			{

				cells[j] = Cell.EMPTY_CELL;
			}
		}
	}

	/**
	 * we never change both the row span and col span at the same time.
	 * 
	 * @param cell
	 *            the cell to be changed
	 * @param newRowSpan
	 *            new row span
	 * @param newColSpan
	 *            new col span
	 */
	protected void resizeCell( Cell cell, int newRowSpan, int newColSpan )
	{
		assert cell.status == Cell.CELL_USED;

		int rowId = cell.rowId;
		int colId = cell.colId;
		int rowSpan = cell.rowSpan;
		if ( rowSpan <= 0 )
		{
			rowSpan = rowCount - rowId;
		}

		int colSpan = cell.colSpan;

		assert rowSpan >= newRowSpan && colSpan >= newColSpan;
		fillEmptyCells( rowId, colId + newColSpan, rowSpan, colSpan
				- newColSpan );
		fillEmptyCells( rowId + newRowSpan, colId, rowSpan - newRowSpan,
				newColSpan );

		cell.colSpan = newColSpan;
		cell.rowSpan = newRowSpan;
	}

	public Cell getCell( int rowIndex, int colIndex )
	{
		return rows[rowIndex].cells[colIndex];
	}

	public Row getRow( int index )
	{
		assert ( index >= 0 && index < rowCount );
		return rows[index];
	}

	private boolean isColumnHidden( IColumn column )
	{
		String formats = column.getVisibleFormat( );
		if ( formats != null
				&& ( formats.indexOf( this.format ) >= 0 || formats
						.indexOf( BIRTConstants.BIRT_ALL_VALUE ) >= 0 ) )
		{
			return true;
		}
		return false;
	}

	
	public UnresolvedRowHint getUnresolvedRow()
	{
		return rowHint;
		
	}

	protected void keepUnresolvedCells( )
	{
		if ( rowCount <= 0 )
		{
			return;
		}
		if(rowHint==null)
		{
			Row row = rows[rowCount - 1];
			Cell[] cells = rows[rowCount - 1].cells;
			IRowContent rowContent = (IRowContent)row.getContent( );
			ITableContent table = rowContent.getTable( );
			InstanceID tableId = table.getInstanceID( );
			InstanceID rowId = rowContent.getInstanceID( );
			UnresolvedRowHint hint = new UnresolvedRowHint( tableId, rowId);
			for ( int cellId = 0; cellId < realColCount; cellId++ )
			{
				if(cells[cellId]!=null)
				{
					ICellContent cc =((CellContent)cells[cellId].getContent( )).getContent();
					String style = cc.getStyle( ).getCssText( );
					hint.addUnresolvedCell( style, cells[cellId]
							.getColId( ), cells[cellId].getColSpan( ),
							cells[cellId].getRowSpan( ) );
				}
			}
			this.rowHint = hint;
		}
		
	}

	public int getCurrentRowID( )
	{
		return rowCount - 1;
	}
	
	public boolean isRowHidden( Object rowContent)
	{
		return LayoutUtil.isRowHidden( rowContent, format );
	}

	public boolean isVisible( ICellContent cell )
	{
		IElement parent = cell.getParent( );
		if ( parent instanceof IRowContent )
		{
			if ( isRowHidden( parent ) )
			{
				return false;
			}
		}
		IColumn column = cell.getColumnInstance( );
		if ( column == null )
		{
			return false;
		}
		if ( isColumnHidden( column ) )
		{
			return false;
		}

		return true;
	}

	protected class UnresolvedRow
	{

		Row row;
		boolean invalidFlags[];

		public UnresolvedRow( Row row )
		{
			this.row = row;
			invalidFlags = new boolean[row.cells.length];
		}
		
		protected int getRowSpan(Row row, int originalRowSpan)
		{
			if(originalRowSpan >0)
			{
				if ( row.getContent( ) != this.row.getContent( ) )
				{
					return originalRowSpan -1;

				}
			}
			return originalRowSpan;
		}
 
		public Cell createCell( int colId, Row row )
		{
			Cell[] cells = this.row.cells;
			if ( colId >= 0 && colId < cells.length )
			{
				//FIXME need clear the content?
				if ( !invalidFlags[colId] )
				{
					invalidFlags[colId] = true;
					return Cell.createCell( row.rowId, colId, getRowSpan( row,
							cells[colId].getRowSpan( ) ), cells[colId]
							.getColSpan( ), cells[colId].getContent( ) );
				}
			}
			return Cell.createCell( row.rowId, colId, 1, 1,
					cells[colId].getContent( ) );
		}

	}

}
