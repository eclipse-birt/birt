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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.CellHelper;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Represents a grid item in the design. A grid item contains a set of report
 * items arranged into a grid. Grids contains rows and columns. The grid
 * contains cells. Each cell can span one or more columns, or one or more rows.
 * Each cell can contain one or more items.
 * <p>
 * Grid layout is familiar to anyone who has used HTML tables, Word tables or
 * Excel: data is divided into a series of rows and columns.
 * 
 * @see org.eclipse.birt.report.model.elements.GridItem
 */

public class GridHandle extends ReportItemHandle
{

	/**
	 * Constructs a grid handle with the given design and the design element.
	 * The application generally does not create handles directly. Instead, it
	 * uses one of the navigation methods available on other element handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public GridHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns a slot handle for the columns in the grid.
	 * 
	 * @return a handle to the column slot
	 * @see SlotHandle
	 */

	public SlotHandle getColumns( )
	{
		return getSlot( GridItem.COLUMN_SLOT );
	}

	/**
	 * Returns a slot handle for the rows in the grid.
	 * 
	 * @return a handle to the row slot
	 * @see SlotHandle
	 */

	public SlotHandle getRows( )
	{
		return getSlot( GridItem.ROW_SLOT );
	}

	/**
	 * Returns the number of columns in the Grid. The number is defined as the
	 * sum of columns described in the "column" slot.
	 * 
	 * @return the number of columns in the grid.
	 */

	public int getColumnCount( )
	{
		return ( (GridItem) getElement( ) ).getColumnCount( design );
	}

	/**
	 * Gets the cell at the position where the given row and column intersect.
	 * 
	 * @param row
	 *            the row position indexing from 1
	 * @param column
	 *            the column position indexing from 1
	 * @return the cell handle at the position if the cell exists, otherwise
	 *         <code>null</code>
	 */

	public CellHandle getCell( int row, int column )
	{
		Cell cell = CellHelper.findCell( getDesign( ),
				(GridItem) getElement( ), row, column );

		if ( cell == null )
			return null;
		return cell.handle( getDesign( ) );
	}

	/**
	 * Gets the content slot handle of the cell at the position where the given
	 * row and column intersect.
	 * 
	 * @param row
	 *            the row position indexing from 1
	 * @param column
	 *            the column position indexing from 1
	 * @return the content slot handle of the cell at the position if the cell
	 *         exists, otherwise <code>null</code>
	 */

	public SlotHandle getCellContent( int row, int column )
	{
		CellHandle cell = getCell( row, column );
		if ( cell == null )
			return null;
		return cell.getContent( );
	}

	/**
	 * Copies a column and cells under it with the given column number.
	 * 
	 * @param columnIndex
	 *            the column position indexing from 1.
	 * @return <code>true</code> if this column band can be copied. Otherwise
	 *         <code>false</code>.
	 */

	public boolean canCopyColumn( int columnIndex )
	{
		GridColumnBandAdapter adapter = new GridColumnBandAdapter( );

		try
		{
			adapter.copyColumn( this, columnIndex );
		}
		catch ( SemanticException e )
		{
			return false;
		}

		return true;
	}

	/**
	 * Copies a column and cells under it with the given column number.
	 * 
	 * @param columnIndex
	 *            the column number
	 * @return a new <code>GridColumnBandAdapter</code> instance
	 * @throws SemanticException
	 *             if the cell layout of the column is invalid.
	 */

	public ColumnBandData copyColumn( int columnIndex )
			throws SemanticException
	{
		GridColumnBandAdapter adapter = new GridColumnBandAdapter( );
		return adapter.copyColumn( this, columnIndex );
	}

	/**
	 * Pastes a column with its cells to the given column number.
	 * 
	 * @param data
	 *            the data of a column band to paste
	 * @param columnNumber
	 *            the column number
	 * @param inForce
	 *            <code>true</code> if pastes the column regardless of the
	 *            warning. Otherwise <code>false</code>.
	 * @throws SemanticException
	 */

	public void pasteColumn( ColumnBandData data, int columnNumber,
			boolean inForce ) throws SemanticException
	{
		if ( data == null )
			throw new IllegalArgumentException( "empty column to paste." ); //$NON-NLS-1$

		GridColumnBandAdapter adapter = new GridColumnBandAdapter( data );
		adapter.pasteColumnBand( this, columnNumber, inForce );
	}

	/**
	 * Checks whether the paste operation can be done with the given copied
	 * column band data, the column index and the operation flag.
	 * 
	 * @param data
	 *            the column band data to paste
	 * @param columnIndex
	 *            the column index
	 * @param inForce
	 *            <code>true</code> indicates to paste the column regardless
	 *            of the different layout of cells. <code>false</code>
	 *            indicates not.
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canPasteColumn( ColumnBandData data, int columnIndex,
			boolean inForce )
	{
		if ( data == null )
			throw new IllegalArgumentException( "empty column to check." ); //$NON-NLS-1$

		GridColumnBandAdapter adapter = new GridColumnBandAdapter( data );
		return adapter.canPaste( this, columnIndex, inForce );
	}
	
	/**
	 * Inserts and pastes a column with its cells to the given column number.
	 * 
	 * @param data
	 *            the data of a column band to paste
	 * @param columnNumber
	 *            the column number
	 * @throws SemanticException
	 */

	public void insertAndPasteColumn( ColumnBandData data, int columnNumber )
			throws SemanticException
	{
		if ( data == null )
			throw new IllegalArgumentException( "empty column to paste." ); //$NON-NLS-1$

		GridColumnBandAdapter adapter = new GridColumnBandAdapter( data );
		adapter.insertAndPasteColumnBand( this, columnNumber );
	}

	/**
	 * Checks whether the insert and paste operation can be done with the given
	 * copied column band data, the column index and the operation flag. This is
	 * different from <code>canPasteColumn</code> since this action creates an
	 * extra column for the table.
	 * 
	 * @param data
	 *            the column band data to paste
	 * @param columnIndex
	 *            the column index
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canInsertAndPasteColumn( ColumnBandData data, int columnIndex )
	{
		if ( data == null )
			throw new IllegalArgumentException( "empty column to check." ); //$NON-NLS-1$

		GridColumnBandAdapter adapter = new GridColumnBandAdapter( data );
		return adapter.canInsertAndPaste( this, columnIndex );
	}
}