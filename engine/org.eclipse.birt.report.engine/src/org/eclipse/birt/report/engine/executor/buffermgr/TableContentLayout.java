package org.eclipse.birt.report.engine.executor.buffermgr;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.executor.buffermgr.Cell.Content;



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
	
	int startRowID = 0;
	int currentRowID = 0;
	int hiddenRowCount = 0;
	Object currentRow;
	
	String format;
	ArrayList hiddenList = new ArrayList();
	protected Row unresolvedRow = null;
	
	public TableContentLayout(ITableContent tableContent, String format)
	{
		this.format = format;
		this.colCount = tableContent.getColumnCount( );
		
		for(int i=0; i<colCount; i++)
		{
			IColumn column = tableContent.getColumn( i );
			if(isColumnHidden( column ))
			{
				hiddenList.add( new Integer(i) );
			}
		}
		this.realColCount = colCount - hiddenList.size( );
	}

	/**
	 * reset the table model.
	 *  
	 */
	public void reset( )
	{
		fillEmptyCells( 0, 0, rowBufferSize, colBufferSize );
		rowCount = 0;
		hiddenRowCount = 0;
	}

	public int getRowCount( )
	{
		return rowCount;
	}

	public int getColCount( )
	{
		return realColCount;
	}

	boolean isRowHidden;
	/**
	 * create a row in the table model
	 * 
	 * @param content
	 *            row content
	 */
	public void createRow( Object rowContent )
	{
		if(!isSameWithLast( rowContent ))
		{
			currentRowID++;
			currentRow = rowContent;
		}
		if(!isRowHidden( rowContent ))
		{
			isRowHidden = false;
			ensureSize( rowCount + 1, realColCount );
			Row row = rows[rowCount];
			row.rowId = currentRowID;
			row.content = rowContent;
	
			if ( rowCount > 0)
			{
				Cell[] cells = row.cells;
				Cell[] lastCells = rows[rowCount - 1].cells;;
				for ( int cellId = 0; cellId < realColCount; cellId++ )
				{
					Cell cell = lastCells[cellId];
					if ( cell.status == Cell.CELL_SPANED )
					{
						cell = cell.getCell( );
					}
					if ( cell.status == Cell.CELL_USED )
					{
						if ( cell.rowSpan < 0 || cell.rowId + cell.rowSpan > currentRowID )
						{
							cells[cellId] = Cell.createSpanCell( currentRowID, cellId,
									cell );
						}
						else if(cell.rowId + cell.rowSpan == currentRowID)
						{
							cell.rowSpan = cell.rowSpan - hiddenRowCount;
						}
					}
				}
			}
			else if(unresolvedRow != null && rowCount==0)
			{
				Cell[] cells = row.cells;
				Cell[] lastCells = unresolvedRow.cells;
				for ( int cellId = 0; cellId < realColCount; cellId++ )
				{
					Cell cell = lastCells[cellId];
					if ( cell.status == Cell.CELL_SPANED )
					{
						cell = cell.getCell( );
					}
					if ( cell.status == Cell.CELL_USED && cell.rowId < currentRowID)
					{
						if ( cell.rowSpan < 0 || cell.rowId + cell.rowSpan > currentRowID )
						{
							((Content)cell.getContent( )).reset( );
							cells[cellId] = Cell.createCell( currentRowID, cell.getColId( ), cell.getRowSpan( ), cell.getColSpan( ),
									(Content)cell.getContent( ) );
						}
					}
				}
			}
			rowCount++;
		}
		else
		{
			hiddenRowCount++;
			isRowHidden = true;
		}
		
			
	}
	
	protected boolean isSameWithLast(Object rowContent)
	{
		return currentRow==rowContent;
	}

	/**
	 * create a cell in the current row.
	 * 
	 * if the cell content is not empty
	 *     put it into the table
	 * if the cell is empty:
	 *     if the cell has been used, drop the cell
	 *     else, put it into the table.
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
		if (isRowHidden)
		{
			return;
		}
		//assert(cellId>0 && cellId<=colCount);
		//resolve real columnNumber and columnSpan
		int columnNumber = cellId;
		int columnSpan = colSpan;
		if(hiddenList.size( )>0)
		{
			for(int i=0; i<hiddenList.size( ); i++)
			{
				int hCol = ((Integer)hiddenList.get(i)).intValue();
				if(hCol<cellId)
				{
					columnNumber--;
				}
				else if(hCol>=cellId && hCol<colSpan+cellId)
				{
					columnSpan--;
				}
			}
		}
		if(columnSpan<1)
		{
			return;
		}
		assert(columnNumber>=0);
		assert(columnNumber+columnSpan<=realColCount);
		ensureSize( rowCount, columnNumber + columnSpan);

		Cell cell = rows[rowCount-1].cells[columnNumber];
		int status = cell.getStatus( );
		
		if ( status == Cell.CELL_EMPTY )
		{
			Cell newCell = Cell.createCell(  rows[rowCount-1].rowId, columnNumber, rowSpan, columnSpan,
					content );

			Cell[] cells = rows[rowCount-1].cells;
			rows[rowCount-1].cells[columnNumber] = newCell;
			for ( int i = columnNumber + 1; i < columnNumber + columnSpan; i++ )
			{
				cells[i] = Cell.createSpanCell( rows[rowCount-1].rowId, i, newCell );
			}
		}
		else
		{
			//FIXME resolve conflict
		}
		
	}

	public void resolveDropCells( )
	{
		if ( rowCount <= 0 )
		{
			return;
		}
		keepUnresolvedCells();
		Cell[] cells = rows[rowCount - 1].cells;
		for ( int cellId = 0; cellId < realColCount; cellId++ )
		{
			Cell cell = cells[cellId];
			if ( cell.status == Cell.CELL_SPANED )
			{
				cell = cell.getCell( );
			}
			if ( cell.status == Cell.CELL_USED )
			{
				cell.rowSpan = rows[rowCount - 1].rowId - cell.rowId - hiddenRowCount + 1;
			}
		}
	}

	public void resolveDropCells( int bandId )
	{
		if ( rowCount <= 0 )
		{
			return;
		}
		Cell[] cells = rows[rowCount - 1].cells;

		for ( int cellId = 0; cellId < realColCount; cellId++ )
		{
			Cell cell = cells[cellId];
			if ( cell.status == Cell.CELL_SPANED )
			{
				cell = cell.getCell( );
			}
			if ( cell.status == Cell.CELL_USED )
			{
				if ( cell.rowSpan == bandId )
				{
					cell.rowSpan = rows[rowCount - 1].rowId - cell.rowId - hiddenRowCount + 1;
				}
			}
		}
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
			if ( cell.status == Cell.CELL_SPANED )
			{
				cell = cell.getCell( );
			}
			if ( cell.status == Cell.CELL_USED )
			{
				if ( cell.rowSpan < 0 || cell.rowSpan + cell.rowId > rows[rowCount - 1].rowId + 1 )
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
				Row row = new Row( rowId);
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
		if (lastRowId > rowCount) lastRowId = rowCount;
		if (lastColId > colCount) lastColId = colCount;

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
				- newColSpan);
		fillEmptyCells( rowId + newRowSpan, colId, rowSpan - newRowSpan,
				newColSpan );

		cell.colSpan = newColSpan;
		cell.rowSpan = newRowSpan;
	}
	
	public Cell getCell(int rowIndex, int colIndex)
	{
		return rows[rowIndex].cells[colIndex];
	}
	
	public Row getRow(int index)
	{
		assert(index>=0 && index<rowCount);
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
	
	protected boolean isRowHidden(Object rowContent)
	{
		if(rowContent!=null && rowContent instanceof IRowContent)
		{
			IStyle style = ((IRowContent)rowContent).getStyle();
			String formats = style.getVisibleFormat( );
			if ( formats != null
					&& ( formats.indexOf( format ) >= 0 || formats
							.indexOf( BIRTConstants.BIRT_ALL_VALUE ) >= 0 ) )
			{
				return true;
			}
		}
		return false;
	}
	
	protected void keepUnresolvedCells()
	{
		if ( rowCount <= 0 )
		{
			return;
		}
		unresolvedRow = new Row( rows[rowCount-1].rowId );
		Cell[] newcells = new Cell[realColCount];
		for ( int colId = 0; colId < realColCount; colId++ )
		{
			newcells[colId] = Cell.EMPTY_CELL;
		}
		unresolvedRow.cells = newcells;
		Cell[] cells = rows[rowCount - 1].cells;
		for ( int cellId = 0; cellId < realColCount; cellId++ )
		{
			Cell cell = cells[cellId];
			if ( cell.status == Cell.CELL_SPANED )
			{
				cell = cell.getCell( );
			}
			if(cell.status == Cell.CELL_USED)
			{
				int colSpan = cell.getColSpan( );
				Cell newCell = Cell.createCell( cell.rowId, cellId, cell.getRowSpan( ), colSpan,
						(Content)cell.getContent( ) );
				unresolvedRow.cells[cellId] = newCell;
				int maxCol = cellId + colSpan;
				for ( int i = cellId + 1; i < maxCol; i++ )
				{
					unresolvedRow.cells[i] = Cell.createSpanCell( cell.rowId, i, newCell );
				}
			}
		}
	}

}
