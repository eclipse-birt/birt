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

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Represents an object of copied objects when do copy/paste operations between
 * grids.
 */

public final class GridColumnBandAdapter extends ColumnBandAdapter
{

	/**
	 * The element where the copy/paste operation occurs.
	 */

	protected GridHandle element;

	GridColumnBandAdapter( )
	{
	}

	GridColumnBandAdapter( ColumnBandData data )
	{
		super( data );
	}

	protected ColumnBandData copyColumn( GridHandle source, int columnNumber )
			throws SemanticException
	{
		assert source != null;

		element = source;
		return super.copyColumnBand( columnNumber );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getElement()
	 */

	protected ReportItemHandle getElementHandle( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getDesign()
	 */

	protected ReportDesign getDesign( )
	{
		return element.getDesign( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getColumns()
	 */

	protected SlotHandle getColumns( )
	{
		return element.getColumns( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getCellsUnderColumn(int)
	 */

	protected List getCellsUnderColumn( int columnNumber )
	{
		return getCellsInSlot( element.getRows( ), columnNumber );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getCellPosition(org.eclipse.birt.report.model.api.CellHandle)
	 */

	protected int getCellPosition( CellHandle cell )
	{
		GridItem grid = (GridItem) element.getElement( );
		return grid.getCellPositionInColumn( getDesign( ), (Cell) cell
				.getElement( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getNumberOfRows()
	 */

	protected int getRowCount( )
	{
		// treat the table as a regular layout.

		return element.getRows( ).getCount( );
	}

	protected void pasteColumnBand( GridHandle target, int columnIndex,
			boolean inForce ) throws SemanticException
	{
		assert target != null;

		element = target;
		super.pasteColumnBand( columnIndex, inForce );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getColumnCount()
	 */

	protected int getColumnCount( )
	{
		return element.getColumnCount( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getRow(int, int,
	 *      int)
	 */

	protected RowHandle getRow( int slotId, int groupId, int rowNumber )
	{
		assert groupId == -1;
		return (RowHandle) element.getSlot( slotId ).get( rowNumber );
	}

	/**
	 * Always <code>false</code> since the "drop" property is disabled in
	 * grid.
	 * 
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#hasDroppingCell(java.util.List)
	 */

	protected boolean hasDroppingCell( List cells )
	{
		return false;
	}

	protected boolean canPaste( GridHandle grid, int columnIndex,
			boolean inForce )
	{
		assert grid != null;

		element = grid;

		return super.canPaste( columnIndex, inForce );
	}
}
