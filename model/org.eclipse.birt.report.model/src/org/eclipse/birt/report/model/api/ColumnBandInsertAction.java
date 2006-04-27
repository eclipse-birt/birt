/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableColumn;

/**
 * Provides the insert and paste operation to the column band in the grid/table.
 * 
 */

class ColumnBandInsertAction extends ColumnBandCopyAction
{

	/**
	 * The target position is one ahead specified column.
	 */

	private static final int INSERT_AFTER = 1;

	/**
	 * The target position is one after specified column.
	 */

	private static final int INSERT_BEFORE = -1;

	/**
	 * 0-based column index.
	 */

	private int targetColumnIndex;

	private ColumnBandData bandData = null;

	List originalCells = null;

	public ColumnBandInsertAction( ColumnBandAdapter adapter )
	{
		super( adapter );
	}

	private ColumnBandData prepareColumnBandData( )
	{
		ColumnBandData band = new ColumnBandData( );
		band.setColumn( new TableColumn( ) );

		List cells = new ArrayList( );
		List slots = adapter.getRowContainerSlots( );
		for ( int i = 0; i < slots.size( ); i++ )
		{
			SlotHandle slot = (SlotHandle) slots.get( i );

			for ( int j = 0; j < slot.getCount( ); j++ )
			{
				RowHandle row = (RowHandle) slot.get( j );
				cells.add( getCellContextInfo( new Cell( ), row ) );
			}
		}

		band.setCells( cells );

		return band;
	}

	/**
	 * Checks whether the paste operation can be done with the given copied
	 * column band data, the column index and the operation flag.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @param data
	 *            the copied column band data
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	protected boolean canInsert( int columnIndex, int insertFlag )
	{
		int columnCount = adapter.getColumnCount( );

		targetColumnIndex = columnIndex;
		if ( insertFlag == INSERT_BEFORE )
			targetColumnIndex = targetColumnIndex - 1;

		// must be >=, since if the columnIndex == columnCount. It means that
		// the column band is supposed to be appended at the far right-end of
		// table.

		if ( targetColumnIndex >= columnCount || targetColumnIndex < 1 )
		{
			// for this case, we only focus on the slot layout information, no
			// sense to check the row number

			originalCells = getCellsContextInfo( adapter
					.getCellsUnderColumn( 1 ) );
		}
		else
		{
			originalCells = getCellsContextInfo( adapter
					.getCellsUnderColumn( targetColumnIndex ) );

			if ( !isRectangleArea( originalCells, 1 ) )
				return false;

			if ( !isValidInsertAndPasteArea( originalCells ) )
				return false;
		}

		bandData = prepareColumnBandData( );
		List cells = bandData.getCells( );
		try
		{
			isSameLayout( cells, originalCells );
		}
		catch ( SemanticException e )
		{
			return false;
		}

		return true;
	}

	/**
	 * Inserts a copied column to the given column index.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @param data
	 *            the copied column band data
	 * @return a list containing post-parsing errors. Each element in the list
	 *         is <code>ErrorDetail</code>.
	 * @throws SemanticException
	 *             if layouts of slots are different.
	 */

	protected List insertColumnBand( int columnIndex, int insertFlag )
			throws SemanticException
	{
		boolean canDone = canInsert( columnIndex, insertFlag );

		if ( !canDone )
			throw new SemanticError( adapter.getElementHandle( ).getElement( ),
					new String[]{adapter.getElementHandle( ).getName( )},
					SemanticError.DESIGN_EXCEPTION_COLUMN_INSERT_FORBIDDEN );

		TableColumn column = bandData.getColumn( );
		List cells = bandData.getCells( );

		try
		{
			if ( adapter instanceof TableColumnBandAdapter )
				adapter.getModule( ).getActivityStack( ).startSilentTrans( );
			else
				adapter.getModule( ).getActivityStack( ).startTrans( );

			pasteColumn( column, targetColumnIndex, true );
			pasteCells( cells, originalCells, targetColumnIndex, true );
		}
		catch ( SemanticException e )
		{
			adapter.getModule( ).getActivityStack( ).rollback( );
			throw e;
		}
		adapter.getModule( ).getActivityStack( ).commit( );

		return doPostPasteCheck( column, cells );
	}

	/**
	 * Checks whether copied cells can be inserted and pasted.
	 * 
	 * @param cells
	 *            cloned cells
	 * @return <code>true</code> if the row count matches the count of
	 *         "rowSpans" in <code>cells</code>, otherwise <code>false</code>.
	 * 
	 */

	private boolean isValidInsertAndPasteArea( List cells )
	{
		int numOfRows = adapter.getRowCount( );
		int rowCount = 0;

		for ( int i = 0; i < cells.size( ); i++ )
		{
			CellContextInfo contextInfo = (CellContextInfo) cells.get( i );
			rowCount += contextInfo.getRowSpan( );

			// TODO dropping effects
		}

		assert rowCount <= numOfRows;
		if ( rowCount < numOfRows )
			return false;

		return true;
	}

}
