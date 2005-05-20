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

package org.eclipse.birt.report.engine.executor.buffermgr;


/**
 * CELL in table layout
 * 
 * @version $Revision:$ $Date:$
 */
public class Cell
{

	final static Cell EMPTY_CELL = new Cell(Cell.CELL_EMPTY);
	/**
	 * CELL is empty
	 */
	public static final int CELL_EMPTY = 0;
	/**
	 * CELL is used, it contains a CELL
	 */
	public static final int CELL_USED = 1;
	/**
	 * CELL is used, it is spaned by another CELL.
	 */
	public static final int CELL_SPANED = 2;

	int status;
	int rowId;
	int colId;
	int rowSpan;
	int colSpan;
	Object content;
	Cell cell;

	static Cell createCell( int rowId, int colId, int rowSpan, int colSpan,
			IContent content )
	{
		Cell cell = new Cell( CELL_USED );
		cell.rowId = rowId;
		cell.colId = colId;
		cell.rowSpan = rowSpan;
		cell.colSpan = colSpan;
		cell.content = content;
		return cell;
	}

	static Cell createSpanCell( int rowId, int colId, Cell cell )
	{
		assert cell.status == CELL_USED;
		Cell span = new Cell( CELL_SPANED );
		span.rowId = rowId;
		span.colId = colId;
		span.content = cell;
		return span;
	}

	private Cell( int status )
	{
		this.status = status;
	}

	Cell getCell( )
	{
		assert status == CELL_SPANED;
		return (Cell) content;
	}

	public int getStatus( )
	{
		return this.status;
	}

	public IContent getContent( )
	{
		assert status == CELL_USED;
		return (IContent) content;
	}
	
	public int getRowId()
	{
		return rowId;
	}
	public int getColId()
	{
		return colId;
	}
	public int getRowSpan()
	{
		return rowSpan;
	}
	public int getColSpan()
	{
		return colSpan;
	}
}