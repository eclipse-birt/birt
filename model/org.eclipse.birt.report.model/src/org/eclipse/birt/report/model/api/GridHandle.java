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

import org.eclipse.birt.report.model.core.DesignElement;
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

}