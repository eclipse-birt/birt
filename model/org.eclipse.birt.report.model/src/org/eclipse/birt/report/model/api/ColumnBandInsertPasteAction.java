/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.TableColumn;

/**
 * Provides the insert and paste operation to the column band in the grid/table.
 * 
 */

class ColumnBandInsertPasteAction extends ColumnBandCopyAction
{

	/**
	 * @param adapter
	 */

	public ColumnBandInsertPasteAction( ColumnBandAdapter adapter )
	{
		super( adapter );
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

	protected boolean canInsertAndPaste( int columnIndex, ColumnBandData data )
	{
		// if table has parent, its layout can't be changed. so can't do insert
		// operation.

		if ( adapter.hasParent( ) )
			return false;

		int columnCount = adapter.getColumnCount( );
		int targetColumnIndex = columnIndex + 1;

		List originalCells = null;

		if ( targetColumnIndex > columnCount )
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
			if ( !isValidInsertAndPasteArea( originalCells ) )
				return false;
		}

		List cells = data.getCells( );
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

	protected List insertAndPasteColumnBand( int columnIndex,
			ColumnBandData data ) throws SemanticException
	{
		boolean canDone = canInsertAndPaste( columnIndex, data );

		if ( !canDone )
			throw new SemanticError( adapter.getElementHandle( ).getElement( ),
					new String[]{adapter.getElementHandle( ).getName( )},
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN );

		TableColumn column = data.getColumn( );
		List cells = data.getCells( );
		List originalCells = getCellsContextInfo( adapter
				.getCellsUnderColumn( columnIndex ) );

		try
		{
			if ( adapter instanceof TableColumnBandAdapter )
				adapter.getModule( ).getActivityStack( ).startSilentTrans( );
			else
				adapter.getModule( ).getActivityStack( ).startTrans( );

			pasteColumn( column, columnIndex, true );
			pasteCells( cells, originalCells, columnIndex, true );
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
