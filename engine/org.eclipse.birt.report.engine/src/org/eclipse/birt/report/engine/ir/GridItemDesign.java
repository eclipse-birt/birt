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

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;

/**
 * Grid Item.
 * 
 * Grid Item is static table, which contains a
 * 
 * column define, serveral rows. and each row contains several cells(maximum to
 * column count defined in column define).
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
public class GridItemDesign extends ReportItemDesign
{

	/**
	 * column defines. the item type is Column.
	 */
	protected ArrayList columns = new ArrayList( );
	/**
	 * rows. the item type is Row.
	 * 
	 * @see RowDesign
	 */
	protected ArrayList rows = new ArrayList( );

	/**
	 * add column into the column define.
	 * 
	 * @param column
	 *            column to be added.
	 */
	public void addColumn( ColumnDesign column )
	{
		assert ( column != null );
		this.columns.add( column );
	}

	/**
	 * get column count.
	 * 
	 * @return count of the column.
	 */
	public int getColumnCount( )
	{
		return this.columns.size( );
	}

	/**
	 * get column defines. the index is not the order of addColumn. It is the
	 * actual column defines(repeated by colum.repeat).
	 * 
	 * @param index
	 *            index of the column.
	 * @return column define.
	 */
	public ColumnDesign getColumn( int index )
	{
		assert ( index >= 0 && index < this.columns.size( ) );
		return (ColumnDesign) this.columns.get( index );
	}

	/**
	 * add a row into the grid.
	 * 
	 * @param row
	 */
	public void addRow( RowDesign row )
	{
		assert ( row != null );
		this.rows.add( row );
	}

	/**
	 * get the row number.
	 * 
	 * @return row number
	 */
	public int getRowCount( )
	{
		return this.rows.size( );
	}

	/**
	 * get the row.
	 * 
	 * @param index
	 *            index of the row.
	 * @return row.
	 */
	public RowDesign getRow( int index )
	{
		assert ( index >= 0 && index < rows.size( ) );
		return (RowDesign) this.rows.get( index );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.ir.ReportItem#accept(org.eclipse.birt.report.engine.ir.ReportItemVisitor)
	 */
	public void accept( IReportItemVisitor visitor )
	{
		visitor.visitGridItem( this );
	}

}
