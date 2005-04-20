/**
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ColumnHelper;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableRow;

/**
 * Represents an object for copy/paste in Gird/Table. The copy/paste between
 * Grid/Table must follow the follwing rules:
 * 
 * <ul>
 * <li>Copy/paste operations must occur among the same type of elements, like
 * among grid elements. A copy/paste operation between Grid/Table is not
 * allowed.
 * <li>Current copy/paste operations do not support cells with "drop"
 * properties.
 * <li>Each time, only one column can be copied/pasted.
 * <li>Slot layouts between the source grid/table and the target grid/table
 * must be same.
 * </ul>
 * 
 */

abstract class ColumnBandAdapter
{

	/**
	 * The copied objects for the copy operation. May includes one column and
	 * cells.
	 */

	protected ColumnBandData data = null;

	/**
	 * Returns the element where the copy/paste operation occurs.
	 * 
	 * @return the element
	 */

	protected abstract ReportItemHandle getElementHandle( );

	/**
	 * Returns the report design where the element belongs to.
	 * 
	 * @return the report design
	 */

	protected abstract ReportDesign getDesign( );

	/**
	 * Returns the column slot.
	 * 
	 * @return the column slot
	 */

	protected abstract SlotHandle getColumns( );

	/**
	 * Returns the number of columns in the element.
	 * 
	 * @return the number of columns in the element
	 */

	protected abstract int getColumnCount( );

	ColumnBandAdapter( )
	{
		data = new ColumnBandData( );
	}

	ColumnBandAdapter( ColumnBandData data )
	{
		this.data = data;
	}

	/**
	 * Checks whether the paste operation can be done with the given copied
	 * column band data, the column index and the operation flag.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @param inForce
	 *            <code>true</code> indicates to paste the column regardless
	 *            of the different layout of cells. <code>false</code>
	 *            indicates not.
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	protected boolean canPaste( int columnIndex, boolean inForce )
	{
		List cells = data.getCells( );

		List originalCells = getCellsContextInfo( getCellsUnderColumn( columnIndex ) );

		if ( !isRectangleArea( originalCells, 1 ) )
			return false;

		boolean isSameLayout = false;

		try
		{
			isSameLayout = isSameLayout( cells, originalCells );
		}
		catch ( SemanticException e )
		{
			return false;
		}

		if ( !inForce && !isSameLayout )
			return false;

		return true;
	}

	/**
	 * Pastes a column to the given <code>target</code>.
	 * 
	 * @param columnIndex
	 *            the column number
	 * @param inForce
	 *            <code>true</code> if paste regardless of the differece of
	 *            cell layouts, otherwise <code>false</code>.
	 * @return a list containing post-parsing errors. Each element in the list
	 *         is <code>ErrorDetail</code>.
	 * @throws SemanticException
	 *             if layouts of slots are different. Or, <code>inForce</code>
	 *             is <code>false</code> and the layout of cells are
	 *             different.
	 */

	protected List pasteColumnBand( int columnIndex, boolean inForce )
			throws SemanticException
	{
		boolean canDone = canPaste( columnIndex, inForce );

		if ( inForce && !canDone )
			throw new SemanticError( getElementHandle( ).getElement( ),
					new String[]{getElementHandle( ).getName( )},
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN );

		if ( !inForce && !canDone )
			throw new SemanticError(
					getElementHandle( ).getElement( ),
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_DIFFERENT_LAYOUT );

		TableColumn column = data.getColumn( );
		List cells = data.getCells( );
		List originalCells = getCellsContextInfo( getCellsUnderColumn( columnIndex ) );

		try
		{
			getDesign( ).getActivityStack( ).startTrans( );
			pasteColumn( column, columnIndex );
			pasteCells( cells, originalCells, columnIndex );
		}
		catch ( SemanticException e )
		{
			getDesign( ).getActivityStack( ).rollback( );
			throw e;
		}
		getDesign( ).getActivityStack( ).commit( );

		return doPostPasteCheck( column, cells );
	}

	/**
	 * Checks element references after the paste operation.
	 * 
	 * @param column
	 *            the column to check
	 * @param cells
	 *            cells to check
	 * 
	 * @return a list containing post-parsing errors. Each element in the list
	 *         is <code>ErrorDetail</code>.
	 */

	private List doPostPasteCheck( TableColumn column, List cells )
	{
		List list = Collections.EMPTY_LIST;

		if ( column != null )
			list = checkElementPostPaste( column.getHandle( getDesign( ) ) );

		for ( int i = 0; i < cells.size( ); i++ )
		{
			CellContextInfo contextInfo = (CellContextInfo) cells.get( i );
			CellHandle cell = contextInfo.getCell( ).handle( getDesign( ) );
			list.addAll( checkElementPostPaste( cell ) );
		}
		return list;
	}

	/**
	 * Checks the element after the paste action.
	 * 
	 * @param content
	 *            the pasted element
	 * 
	 * @return a list containing parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 */

	private List checkElementPostPaste( DesignElementHandle content )
	{
		if ( content == null )
			return Collections.EMPTY_LIST;

		List errors = content.getElement( ).validateWithContents( getDesign( ) );
		Iterator iter = errors.iterator( );

		ArrayList detailList = new ArrayList( );
		while ( iter.hasNext( ) )
		{
			ErrorDetail error = new ErrorDetail( (Exception) iter.next( ) );
			detailList.add( error );
		}

		return detailList;
	}

	/**
	 * Checks whether layouts in source element and destination element are the
	 * same. It is considered as the same if there are same numbers of rows in
	 * source and destination elements.
	 * 
	 * @param copiedCells
	 *            the copied cells.
	 * @param targetCells
	 *            the target cells to be replaced.
	 * @return <code>true</code> if layouts are exactly same.
	 *         <code>false</code> if two elements have the same number of rows
	 *         in slot but cells have different rowSpan values.
	 * @throws SemanticException
	 *             if number of rows in slots of the source and destination are
	 *             different.
	 */

	private boolean isSameLayout( List copiedCells, List targetCells )
			throws SemanticException
	{
		String oldContainerDefnName = null;
		int oldSlotId = DesignElement.NO_SLOT;
		int oldGroupId = -1;

		for ( int i = 0; i < copiedCells.size( ); i++ )
		{
			CellContextInfo contextInfo = (CellContextInfo) copiedCells.get( i );

			String containerDefnName = contextInfo.getContainerDefnName( );
			int slotId = contextInfo.getSlotId( );
			int groupId = contextInfo.getGroupId( );

			if ( !containerDefnName.equals( oldContainerDefnName )
					|| slotId != oldSlotId || groupId != oldGroupId )
			{
				SlotLayoutInfo info1 = getLayoutOfSlot( copiedCells,
						containerDefnName, slotId, groupId );
				SlotLayoutInfo info2 = getLayoutOfSlot( targetCells,
						containerDefnName, slotId, groupId );

				if ( !info1.isSameNumOfRows( info2 ) )
					throw new SemanticError(
							getElementHandle( ).getElement( ),
							new String[]{getElementHandle( ).getName( )},
							SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN );

				if ( !info1.isSameLayoutOfRows( info2 ) )
					return false;
			}
			oldContainerDefnName = containerDefnName;
			oldSlotId = slotId;
			oldGroupId = groupId;
		}

		return true;
	}

	/**
	 * Creates a <code>SlotLayoutInfo</code> with the given cell, container
	 * name, slot id and group id.
	 * 
	 * @param cells
	 *            a list containing cell handles
	 * @param containerDefnName
	 *            the definition name of the container
	 * @param slotId
	 *            the slot id
	 * @param groupId
	 *            the group id
	 * @return a <code>SlotLayoutInfo</code> object
	 */

	private SlotLayoutInfo getLayoutOfSlot( List cells,
			String containerDefnName, int slotId, int groupId )
	{
		SlotLayoutInfo layoutInfo = new SlotLayoutInfo( containerDefnName,
				slotId, groupId );
		for ( int i = 0; i < cells.size( ); i++ )
		{
			Object obj = cells.get( i );

			String tmpDefnName = null;
			int tmpSlotId = DesignElement.NO_SLOT;
			int tmpGroupId = -1;

			CellContextInfo contextInfo = (CellContextInfo) obj;
			tmpDefnName = contextInfo.getContainerDefnName( );
			tmpSlotId = contextInfo.getSlotId( );
			tmpGroupId = contextInfo.getGroupId( );

			if ( containerDefnName.equals( tmpDefnName ) && tmpSlotId == slotId
					&& tmpGroupId == groupId )
				layoutInfo.addCell( contextInfo.getCell( ), contextInfo
						.getRowSpan( ) );
		}

		return layoutInfo;
	}

	/**
	 * Pastes the copied column <code>column</code> to the given
	 * <code>columnNumber</code> in the target element.
	 * 
	 * @param column
	 *            the copied column
	 * @param columnNumber
	 *            the column number
	 * @throws SemanticException
	 *             if any error occurs during pasting a column header
	 */

	protected void pasteColumn( TableColumn column, int columnNumber )
			throws SemanticException
	{
		TableColumn targetColumn = null;
		SlotHandle columns = getColumns( );

		if ( columns.getCount( ) == 0 && column == null )
			return;

		if ( columns.getCount( ) == 0 && column != null )
		{
			addColumnHeader( column, columnNumber );
			return;
		}

		targetColumn = ColumnHelper.findColumn( getDesign( ),
				columns.getSlot( ), columnNumber );

		replaceColumn( column, targetColumn.handle( getDesign( ) ),
				columnNumber );

	}

	/**
	 * Replaces the <code>target</code> column with the given
	 * <code>source</code> column at the given column number.
	 * 
	 * @param source
	 *            the column to replace
	 * @param target
	 *            the column to be replaced
	 * @param columnNumber
	 *            the column number
	 */

	private void replaceColumn( TableColumn source, ColumnHandle target,
			int columnNumber ) throws SemanticException
	{
		SlotHandle columns = target.getContainerSlotHandle( );
		assert target != null;

		int colStartPos = 1;
		int colPosInSlot = columns.findPosn( target.getElement( ) );
		for ( int i = 0; i < colPosInSlot; i++ )
		{
			ColumnHandle col = (ColumnHandle) columns.get( i );
			colStartPos += col.getRepeatCount( );

			if ( colStartPos == columnNumber )
				break;
		}

		ColumnHandle toAdd = null;
		if ( source == null )
			toAdd = target.getElementFactory( ).newTableColumn( );
		else
			toAdd = (ColumnHandle) source.getHandle( getDesign( ) );

		// removes the column required.

		if ( target.getRepeatCount( ) == 1 )
		{
			int oldPos = columns.findPosn( target.getElement( ) );
			columns.drop( target );
			columns.add( toAdd, oldPos );
			return;
		}

		assert target.getRepeatCount( ) > 1;

		// the new column is replaced at the beginning or end the target column

		if ( columnNumber == colStartPos
				|| columnNumber == colStartPos + target.getRepeatCount( ) - 1 )
		{
			target.setRepeatCount( target.getRepeatCount( ) - 1 );

			int pos = columns.findPosn( target.getElement( ) );
			if ( columnNumber != colStartPos )
				pos++;

			columns.add( toAdd, pos );

			return;
		}

		// the new column is replaced at the center of the target column (not
		// beginning or the end).

		if ( columnNumber > colStartPos
				&& columnNumber < colStartPos + target.getRepeatCount( ) - 1 )
		{
			int repeat1 = columnNumber - colStartPos;
			int repeat2 = target.getRepeatCount( ) - repeat1 - 1;

			ColumnHandle newColumn = ( (TableColumn) target.copy( ) )
					.handle( getDesign( ) );
			target.setRepeatCount( repeat1 );
			newColumn.setRepeatCount( repeat2 );
			int pos = columns.findPosn( target.getElement( ) );
			columns.add( toAdd, pos + 1 );
			columns.add( newColumn, pos + 2 );
		}
	}

	/**
	 * Adds all column headers for an element that has no column information.
	 * 
	 * @param column
	 *            the column from the copy operation
	 * @param columnNumber
	 *            the column number of <code>column</code>
	 */

	protected void addColumnHeader( TableColumn column, int columnNumber )
	{
		SlotHandle columns = getColumns( );
		assert columns.getCount( ) == 0;

		// the number of columns must be cached since this number changes during
		// the execution of table.getColumnCount()

		int columnCount = getColumnCount( );

		for ( int i = 0; i < columnCount; i++ )
		{
			ColumnHandle toAdd = null;

			if ( i != columnNumber - 1 )
				toAdd = getElementHandle( ).getElementFactory( )
						.newTableColumn( );
			else
				toAdd = column.handle( getDesign( ) );

			try
			{
				columns.add( toAdd );
			}
			catch ( SemanticException e )
			{
				assert false;
			}
		}
	}

	/**
	 * Returns the row with the given slot id, group id and the row number.
	 * 
	 * @param slotId
	 *            the slot id
	 * @param groupId
	 *            the group id
	 * @param rowNumber
	 *            the row number
	 * @return the row that matches the input parameters
	 */

	abstract protected RowHandle getRow( int slotId, int groupId, int rowNumber );

	/**
	 * Returns the position where the cell resides in the row.
	 * 
	 * @param cell
	 *            the cell handle
	 * @return the position indexing from 0
	 */

	private static int findCellPosition( CellHandle cell )
	{
		RowHandle row = (RowHandle) cell.getContainer( );
		assert row != null;
		return row.getCells( ).findPosn( cell.getElement( ) ) + 1;
	}

	/**
	 * Removes cells in <code>originalCells</code> and inserts cells in
	 * <code>copiedCells</code> to the element.
	 * 
	 * @param copiedCells
	 *            a list containing cells that is to be inserted.
	 * @param originalCells
	 *            a list containing cells that is to be deleted.
	 * @param columnIndex
	 *            the column index where copied cells are pasted
	 * @throws SemanticException
	 *             if any error occurs during pasting cells.
	 */

	protected void pasteCells( List copiedCells, List originalCells,
			int columnIndex ) throws SemanticException
	{
		int[] originalPositions = new int[originalCells.size( )];
		int[] originalRowSpans = new int[originalCells.size( )];

		// remove cells first.

		for ( int i = 0; i < originalCells.size( ); i++ )
		{
			CellContextInfo contextInfo = (CellContextInfo) originalCells
					.get( i );
			CellHandle cell = contextInfo.getCell( ).handle( getDesign( ) );

			originalPositions[i] = findCellPosition( cell );
			originalRowSpans[i] = contextInfo.getRowSpan( );

			cell.getContainerSlotHandle( ).drop( cell );
		}

		int[] copiedRowSpans = new int[copiedCells.size( )];
		for ( int i = 0; i < copiedCells.size( ); i++ )
		{
			CellContextInfo contextInfo = (CellContextInfo) originalCells
					.get( i );
			copiedRowSpans[i] = contextInfo.getRowSpan( );
		}

		// insert column index that is from 1.

		int[] insertPosition = getIndexToAdd( originalPositions,
				originalRowSpans, copiedRowSpans );

		// adds the copied cells to the destination.

		for ( int i = 0; i < copiedCells.size( ); i++ )
		{

			CellContextInfo contextInfo = (CellContextInfo) copiedCells.get( i );

			// groupId is equal to -1, means this is a top slot in the table

			RowHandle row = getRow( contextInfo.getSlotId( ), contextInfo
					.getGroupId( ), contextInfo.getRowNumber( ) );

			assert row != null;

			int pos = insertPosition[i];
			CellHandle cell = contextInfo.getCell( ).handle( getDesign( ) );
			cell.setColumn( columnIndex );

			row.addElement( cell, TableRow.CONTENT_SLOT, pos - 1 );
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

	protected static int[] getIndexToAdd( int[] originalPositions,
			int[] originalRowSpans, int[] copiedRowSpans )
	{
		int[] retValue = new int[copiedRowSpans.length];

		int copiedRowCount = 0;
		int originalRowCount = 0;

		for ( int i = 0, j = 0; i < copiedRowSpans.length; i++ )
		{
			int copiedRowSpan = copiedRowSpans[i];
			copiedRowCount += copiedRowSpan;

			boolean isJChanged = false;

			while ( originalRowCount < copiedRowCount )
			{
				originalRowCount += originalRowSpans[j];
				j++;
				isJChanged = true;
			}
			retValue[i] = originalPositions[isJChanged ? j - 1 : j];
		}

		return retValue;
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
		if ( columnNumber <= 0 )
			return null;

		TableColumn clonedColumn = copyColumn( getColumns( ), columnNumber );
		List cells = cloneCells( getCellsUnderColumn( columnNumber ),
				columnNumber );

		data.setColumn( clonedColumn );
		data.setCells( cells );

		if ( !isRectangleArea( cells, 1 ) )
			throw new SemanticError( getElementHandle( ).getElement( ),
					new String[]{Integer.toString( columnNumber ),
							getElementHandle( ).getName( )},
					SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN );

		if ( hasDroppingCell( cells ) )
			throw new SemanticError( getElementHandle( ).getElement( ),
					new String[]{Integer.toString( columnNumber ),
							getElementHandle( ).getName( )},
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
	 * Returns the context information for a list of cells. Cells must reside in
	 * a valid row container.
	 * 
	 * @param cells
	 *            a list of cell handles
	 * @return a list containing new <code>CellContextInfo</code> objects.
	 */

	private List getCellsContextInfo( List cells )
	{
		List list = new ArrayList( );

		for ( int i = 0; i < cells.size( ); i++ )
		{
			CellHandle cell = (CellHandle) cells.get( i );
			list.add( getCellContextInfo( (Cell) cell.getElement( ),
					(RowHandle) cell.getContainer( ) ) );
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

	private CellContextInfo getCellContextInfo( Cell cell, RowHandle row )
	{
		DesignElementHandle rowContainer = row.getContainer( );
		int slotId = rowContainer.findContentSlot( row );
		int groupId = -1;
		SlotHandle slot = rowContainer.getSlot( slotId );

		if ( rowContainer instanceof TableGroupHandle )
		{
			TableHandle rowGrandPa = (TableHandle) rowContainer.getContainer( );
			groupId = rowGrandPa.getGroups( ).findPosn(
					rowContainer.getElement( ) );
		}

		CellContextInfo cellInfo = new CellContextInfo( cell, cell
				.getRowSpan( getDesign( ) ), cell.getColSpan( getDesign( ) ),
				cell.getStringProperty( getDesign( ), Cell.DROP_PROP ) );

		int rowNumber = slot.findPosn( row.getElement( ) );
		cellInfo.setContainerDefnName( rowContainer.getDefn( ).getName( ) );
		cellInfo.setSlotId( slotId );
		cellInfo.setGroupId( groupId );
		cellInfo.setRowNumber( rowNumber );

		return cellInfo;
	}

	/**
	 * Returns the number of rows in the element.
	 * 
	 * @return the number or rows in the element.
	 */

	abstract protected int getRowCount( );

	/**
	 * Checks whether any cell in <code>cells</code> has a value of
	 * <code>DesignChoiceConstants#DROP_TYPE_DETAIL</code> or
	 * <code>DesignChoiceConstants#DROP_TYPE_ALL</code> for the "drop"
	 * property.
	 * 
	 * @param cells
	 *            a list containing cell handles
	 * @return <code>true</code> if any cell has the "drop" property,
	 *         otherwise <code>false</code>.
	 */

	abstract protected boolean hasDroppingCell( List cells );

	/**
	 * Checks whether copied cells can be integrated into a rectangle.
	 * 
	 * @param cells
	 *            cloned cells
	 * @param rectWidth
	 *            the column width
	 * @return <code>true</code> if the shape of integrated cells is a
	 *         rectangle, otherwise <code>false</code>.
	 */

	private boolean isRectangleArea( List cells, int rectWidth )
	{
		int numOfRows = getRowCount( );
		int rowCount = 0;

		for ( int i = 0; i < cells.size( ); i++ )
		{
			CellContextInfo contextInfo = (CellContextInfo) cells.get( i );

			int colSpan = contextInfo.getColumnSpan( );
			if ( colSpan > rectWidth )
				return false;

			rowCount += contextInfo.getRowSpan( );

			// TODO dropping effects
		}

		assert rowCount <= numOfRows;

		if ( rowCount < numOfRows )
			return false;

		return true;
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

	private TableColumn copyColumn( SlotHandle columns, int columnIndex )
	{
		TableColumn column = ColumnHelper.findColumn( getDesign( ), columns
				.getSlot( ), columnIndex );

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

	/**
	 * Returns copied cells with the column number.
	 * 
	 * @param columnNumber
	 *            the column number
	 * @return new cell instances
	 */

	abstract protected List getCellsUnderColumn( int columnNumber );

	/**
	 * Returns copied cells with the given slot and column number.
	 * 
	 * @param handle
	 *            the slot
	 * @param columnIndex
	 *            the column number
	 * @return new cell instances
	 */

	protected List getCellsInSlot( SlotHandle handle, int columnIndex )
	{
		List retValue = new ArrayList( );

		for ( int i = 0; i < handle.getCount( ); i++ )
		{
			RowHandle row = (RowHandle) handle.get( i );
			CellHandle cell = getCellsInRow( row, columnIndex );
			if ( cell != null )
				retValue.add( cell );
		}
		return retValue;
	}

	/**
	 * Returns the column number with a given cell.
	 * 
	 * @param cell
	 *            the cell to find.
	 * @return the column number of the given cell.
	 */

	abstract protected int getCellPosition( CellHandle cell );

	/**
	 * Returns a copied cell with the given row and column number.
	 * 
	 * @param row
	 *            the row
	 * @param columnIndex
	 *            the column number
	 * @return a new cell instance
	 */

	protected CellHandle getCellsInRow( RowHandle row, int columnIndex )
	{
		SlotHandle cells = row.getCells( );

		for ( int i = 0; i < cells.getCount( ); i++ )
		{
			CellHandle cell = (CellHandle) cells.get( i );
			int cellColumnIndex = getCellPosition( cell );

			if ( cellColumnIndex == columnIndex )
				return cell;
		}

		return null;
	}

	/**
	 * Represents the layout of a slot. The information includes the container
	 * of the slot, the slot id, the group id and rows in the slot.
	 */

	private static class SlotLayoutInfo
	{

		/**
		 * Rows in the slot.
		 */

		private List details = new ArrayList( );

		/**
		 * The definition name of the container.
		 */

		private String containerDefnName;

		/**
		 * The slot Id.
		 */

		private int slotId;

		/**
		 * The group id. If the slot is not in the group, this value is -1.
		 */

		private int groupId;

		protected SlotLayoutInfo( String containerDefnName, int slotId,
				int groupId )
		{
			this.containerDefnName = containerDefnName;
			this.slotId = slotId;
			this.groupId = groupId;
		}

		/**
		 * Adds a cell to the slot layout information.
		 * 
		 * @param cell
		 *            the cell handle
		 * @param rowSpan
		 *            the row span
		 */

		protected void addCell( Cell cell, int rowSpan )
		{
			details.add( new Integer( rowSpan ) );
		}

		protected int getNumOfCells( )
		{
			return details.size( );
		}

		/**
		 * Checks whether numbers of rows in two <code>SlotLayoutInfo</code>
		 * are same.
		 * 
		 * @param info
		 *            the slot information
		 * @return <code>true</code> if two numbers are same. Otherwise
		 *         <code>false</code>.
		 */

		public boolean isSameNumOfRows( SlotLayoutInfo info )
		{
			if ( !containerDefnName.equals( info.containerDefnName ) )
				return false;

			if ( slotId != info.slotId || groupId != groupId )
				return false;

			int myNumOfRows = getNumOfRows( );
			int targetNumOfRows = info.getNumOfRows( );

			return ( myNumOfRows == targetNumOfRows );
		}

		/**
		 * Checks whether layout information in two <code>SlotLayoutInfo</code>
		 * are same.
		 * 
		 * @param info
		 *            the slot information
		 * @return <code>true</code> if layout information is same. Otherwise
		 *         <code>false</code>.
		 */

		public boolean isSameLayoutOfRows( SlotLayoutInfo info )
		{
			if ( details.size( ) != info.details.size( ) )
				return false;

			for ( int i = 0; i < details.size( ); i++ )
			{
				Integer myRowSpan = (Integer) details.get( i );

				Object targetRowSpan = info.details.get( i );
				if ( !myRowSpan.equals( targetRowSpan ) )
					return false;
			}

			return true;
		}

		private int getNumOfRows( )
		{
			int numOfRows = 0;
			for ( int i = 0; i < details.size( ); i++ )
			{
				Integer rowSpan = (Integer) details.get( i );
				numOfRows += rowSpan.intValue( );
			}

			return numOfRows;
		}

	}
}
