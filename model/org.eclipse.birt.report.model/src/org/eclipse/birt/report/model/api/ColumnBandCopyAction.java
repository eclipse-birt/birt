
package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ColumnHelper;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableRow;

abstract class ColumnBandCopyAction extends ColumnBandAction
{

	/**
	 * @param adapter
	 */

	public ColumnBandCopyAction( ColumnBandAdapter adapter )
	{
		super( adapter );
	}

	/**
	 * Copies the column object and cells under it to the adapter.
	 * 
	 * @param columnNumber
	 *            the column number
	 * @return the copied column band that includes the copied column and cells
	 * @throws SemanticException
	 *             if the copy operation on the column <code>columnNumber</code>
	 *             is forbidden.
	 */

	protected ColumnBandData copyColumnBand( int columnNumber )
			throws SemanticException
	{
		ColumnBandData data = new ColumnBandData( );

		if ( columnNumber <= 0 )
			return null;

		TableColumn clonedColumn = copyColumn( adapter.getColumns( ),
				columnNumber );
		List cells = cloneCells( adapter.getCellsUnderColumn( columnNumber ),
				columnNumber );

		data.setColumn( clonedColumn );
		data.setCells( cells );

		if ( !isRectangleArea( cells, 1 ) )
			throw new SemanticError( adapter.getElementHandle( ).getElement( ),
					new String[]{Integer.toString( columnNumber ),
							adapter.getElementHandle( ).getName( )},
					SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN );

		if ( adapter.hasDroppingCell( cells ) )
			throw new SemanticError( adapter.getElementHandle( ).getElement( ),
					new String[]{Integer.toString( columnNumber ),
							adapter.getElementHandle( ).getName( )},
					SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN );

		return data;
	}

	/**
	 * Makes new copies of a list of cell handles with the given column number.
	 * 
	 * @param cells
	 *            a list of cells.
	 * @param columnNumber
	 *            the column number
	 * @return a list containing new cloned cells
	 */

	private List cloneCells( List cells, int columnNumber )
	{
		List list = new ArrayList( );

		for ( int i = 0; i < cells.size( ); i++ )
		{
			CellHandle originalCell = (CellHandle) cells.get( i );
			Cell clonedCell = (Cell) originalCell.copy( );

			// clears the column property in the cell is not useful here.

			list.add( getCellContextInfo( clonedCell, (RowHandle) originalCell
					.getContainer( ) ) );
		}
		return list;
	}

	/**
	 * Returns the context information of a cell. The cell must reside in a
	 * valid row container.
	 * 
	 * @param cell
	 *            the cell handle
	 * @param row
	 *            the row that contains the context information
	 * @return a new <code>CellContextInfo</code> object
	 */

	protected CellContextInfo getCellContextInfo( Cell cell, RowHandle row )
	{
		DesignElementHandle rowContainer = row.getContainer( );
		int slotId = rowContainer.findContentSlot( row );
		int groupId = -1;
		SlotHandle slot = rowContainer.getSlot( slotId );

		if ( rowContainer instanceof TableGroupHandle )
		{
			TableHandle rowGrandPa = (TableHandle) rowContainer.getContainer( );
			groupId = rowGrandPa.getGroups( ).findPosn( rowContainer );
		}

		CellContextInfo cellInfo = new CellContextInfo( cell, cell
				.getRowSpan( adapter.getModule( ) ), cell.getColSpan( adapter
				.getModule( ) ), cell.getStringProperty( adapter.getModule( ),
				Cell.DROP_PROP ) );

		int rowNumber = slot.findPosn( row );
		cellInfo.setContainerDefnName( rowContainer.getDefn( ).getName( ) );
		cellInfo.setSlotId( slotId );
		cellInfo.setGroupId( groupId );
		cellInfo.setRowNumber( rowNumber );

		return cellInfo;
	}

	/**
	 * Returns insert positions of <code>copiedCells</code>. Each element in
	 * the return value is an integer, which can be
	 * 
	 * <ul>
	 * <li>0 -- insert to the beginning of row
	 * <li>an integer between 0 and the maximal position
	 * <li>-1 -- insert to the end of the row
	 * </ul>
	 * 
	 * @param copiedCells
	 *            a list containing cells that is to be inserted.
	 * @param originalCells
	 *            a list containing cells that is to be deleted.
	 * @param columnIndex
	 *            the column index where copied cells are pasted
	 * @param isInsert
	 *            <code>true</code> if this is an insert and paste action.
	 *            Otherwise <code>false</code>.
	 * 
	 * @return an array containing insert positions
	 */

	private int[] getInsertPosition( List copiedCells, List originalCells,
			int columnIndex, boolean isInsert )
	{
		// insert column index that is from 1.

		int[] insertPosition = null;

		int columnCount = adapter.getColumnCount( );
		if ( isInsert && ( columnIndex == 0 || columnIndex == columnCount - 1 ) )
		{
			insertPosition = new int[copiedCells.size( )];

			if ( columnIndex == 0 )
				Arrays.fill( insertPosition, 0 );
			else
				Arrays.fill( insertPosition, -1 );
		}
		else
		{
			int[] copiedRowSpans = new int[copiedCells.size( )];
			int[] originalPositions = new int[originalCells.size( )];
			int[] originalRowSpans = new int[originalCells.size( )];

			// remove cells first.

			for ( int i = 0; i < originalCells.size( ); i++ )
			{
				CellContextInfo contextInfo = (CellContextInfo) originalCells
						.get( i );
				CellHandle cell = contextInfo.getCell( ).handle(
						adapter.getModule( ) );

				originalPositions[i] = ColumnBandAdapter
						.findCellPosition( cell );
				originalRowSpans[i] = contextInfo.getRowSpan( );
			}

			for ( int i = 0; i < copiedCells.size( ); i++ )
			{
				CellContextInfo contextInfo = (CellContextInfo) copiedCells
						.get( i );
				copiedRowSpans[i] = contextInfo.getRowSpan( );
			}

			insertPosition = getIndexToAdd( originalPositions,
					originalRowSpans, copiedRowSpans );
		}

		return insertPosition;
	}

	/**
	 * Performs insert and paste or paste operations. Removes cells in
	 * <code>originalCells</code> if <code>isInsert</code> is
	 * <code>true</code>. Then inserts cells in <code>copiedCells</code> to
	 * the element.
	 * 
	 * @param copiedCells
	 *            a list containing cells that is to be inserted.
	 * @param originalCells
	 *            a list containing cells that is to be deleted.
	 * @param columnIndex
	 *            the column index where copied cells are pasted
	 * @param isInsert
	 *            <code>true</code> if this is an insert and paste action.
	 *            Otherwise <code>false</code>.
	 * @throws SemanticException
	 *             if any error occurs during pasting cells.
	 */

	protected void pasteCells( List copiedCells, List originalCells,
			int columnIndex, boolean isInsert ) throws SemanticException
	{

		// insert column index that is from 1. this must happen before removing
		// operation.

		int[] insertPosition = getInsertPosition( copiedCells, originalCells,
				columnIndex, isInsert );

		// remove cells first.

		for ( int i = 0; !isInsert && i < originalCells.size( ); i++ )
		{
			CellContextInfo contextInfo = (CellContextInfo) originalCells
					.get( i );
			CellHandle cell = contextInfo.getCell( ).handle(
					adapter.getModule( ) );
			if ( !isInsert )
				cell.getContainerSlotHandle( ).drop( cell );
		}

		// adds the copied cells to the destination.

		for ( int i = 0; i < copiedCells.size( ); i++ )
		{

			CellContextInfo contextInfo = (CellContextInfo) copiedCells.get( i );

			// groupId is equal to -1, means this is a top slot in the table

			RowHandle row = adapter.getRow( contextInfo.getSlotId( ),
					contextInfo.getGroupId( ), contextInfo.getRowNumber( ) );

			assert row != null;

			int pos = insertPosition[i];
			CellHandle cell = contextInfo.getCell( ).handle(
					adapter.getModule( ) );

			// if this is only paste operation, then paste it to the old
			// position. Otherwise, append it to the next available position.

			if ( !isInsert )
				pos--;

			if ( pos != -1 )
				row.addElement( cell, TableRow.CONTENT_SLOT, pos );
			else
				row.addElement( cell, TableRow.CONTENT_SLOT );
		}
	}

	/**
	 * Calculates and returns a list containing insert positions of copied
	 * cells.
	 * 
	 * @param originalPositions
	 *            positions of to be replaced cells
	 * @param originalRowSpans
	 *            row spans of to be replaced cells
	 * @param copiedRowSpans
	 *            row spans of copied cells
	 * 
	 * @return a list containing insert positions of copied cells.
	 */

	private static int[] getIndexToAdd( int[] originalPositions,
			int[] originalRowSpans, int[] copiedRowSpans )
	{
		int[] retValue = new int[copiedRowSpans.length];

		int copiedIndex = 0;
		int originalIndex = 0;

		while ( copiedIndex < copiedRowSpans.length )
		{
			int copiedRowSpan = copiedRowSpans[copiedIndex];
			assert originalIndex < originalRowSpans.length;

			retValue[copiedIndex] = originalPositions[originalIndex];

			int originalRowSpan = originalRowSpans[originalIndex];
			while ( copiedRowSpan < originalRowSpan )
			{
				copiedIndex++;
				copiedRowSpan += copiedRowSpans[copiedIndex];
				originalIndex++;
			}
			copiedIndex++;
		}

		return retValue;
	}

	/**
	 * Copies a column with the given column slot and the column number.
	 * 
	 * @param columns
	 *            the column slot
	 * @param columnIndex
	 *            the column number
	 * @return a new column instance
	 */

	protected TableColumn copyColumn( SlotHandle columns, int columnIndex )
	{
		TableColumn column = ColumnHelper.findColumn( adapter.getModule( ),
				columns.getSlot( ), columnIndex );

		if ( column == null )
			return null;

		TableColumn clonedColumn = null;

		try
		{
			clonedColumn = (TableColumn) column.clone( );
			clonedColumn
					.setProperty( TableColumn.REPEAT_PROP, new Integer( 1 ) );
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
		}

		return clonedColumn;
	}

}
