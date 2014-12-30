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
import java.util.List;

import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.GridItem;

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

	GridColumnBandAdapter( GridHandle element )
	{
		this.element = element;
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
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getColumns()
	 */

	protected SlotHandle getColumns( )
	{
		return element.getColumns( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getCellsUnderColumn(int,
	 *      boolean)
	 */

	protected List getCellsUnderColumn( int columnNumber,
			boolean mustBeStartPosition )
	{
		return getCellsInSlot( element.getRows( ), columnNumber,
				mustBeStartPosition );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getCellPosition(org.eclipse.birt.report.model.api.CellHandle)
	 */

	protected int getCellPosition( CellHandle cell )
	{
		GridItem grid = (GridItem) element.getElement( );
		return grid.getCellPositionInColumn( getModule( ), (Cell) cell
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

	protected List getRowContainerSlots( )
	{
		List list = new ArrayList( );
		list.add( element.getRows( ) );

		return list;
	}

	@Override
	protected boolean isDroppingCell( CellContextInfo cell )
	{
		return false;
	}

}
