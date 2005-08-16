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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.Module;

/**
 * Provides methods for finding a cell. Currently, only TableItem and GridItem
 * have cells.
 */

public class CellHelper
{

	/**
	 * Gets the content slot handle of the cell at the position where the given
	 * row and column intersect.
	 * 
	 * @param module
	 *            the module
	 * @param grid
	 *            the grid item to find the cell
	 * @param rowNum
	 *            the row position indexing from 1
	 * @param colNum
	 *            the column position indexing from 1
	 * @return the the cell if found, otherwise <code>null</code>
	 */

	public static Cell findCell( Module module, GridItem grid,
			int rowNum, int colNum )
	{
		if ( grid == null )
			return null;
		if ( colNum > grid.findMaxCols( module ) )
			return null;
		ContainerSlot rowSlot = grid.getSlot( GridItem.ROW_SLOT );
		for ( int i = 0; i < rowSlot.getCount( ); i++ )
		{
			TableRow row = (TableRow)rowSlot.getContent( i );
			ContainerSlot cellSlot = row.getSlot( TableRow.CONTENT_SLOT );
			for ( int j = 0; j < cellSlot.getCount( ); j++ )
			{
				int rowIndex = i;
				Cell cell = (Cell)cellSlot.getContent( j );
				int rowSpan = cell.getIntProperty( module, Cell.ROW_SPAN_PROP );
				rowSpan = ( rowSpan < 1 ) ? 1 : rowSpan;
				
				// compute the logic row position				
				rowIndex += rowSpan;
				
				// the row position is not smaller than the rowNum
				// the the cell maybe the one we try to find
				
				if ( rowIndex >= rowNum )
				{
					int colIndex = 0;
					int column = grid.getCellPositionInColumn( module, cell );
					assert column > 0;					
					int colSpan = cell.getIntProperty( module, Cell.COL_SPAN_PROP );
					colSpan = ( colSpan < 1 ) ? 1 : colSpan;
					colIndex = column + colSpan - 1;
					if ( colIndex >= colNum )
						return cell;
				}
			}
		}

		return null;
	}
}