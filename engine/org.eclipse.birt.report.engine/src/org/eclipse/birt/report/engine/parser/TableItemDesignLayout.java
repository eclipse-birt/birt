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

package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.executor.buffermgr.Cell;
import org.eclipse.birt.report.engine.executor.buffermgr.Row;
import org.eclipse.birt.report.engine.executor.buffermgr.Table;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

/**
 * calculate the cell id explictly.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/05/24 09:22:41 $
 */
public class TableItemDesignLayout
{

	Table layout = new Table( );

	public void layout( GridItemDesign grid )
	{
		layout = new Table(0, grid.getColumnCount());
		layout.reset( );
		for ( int i = 0; i < grid.getRowCount( ); i++ )
		{
			layoutRow( grid.getRow( i ) );
		}
		layout.resolveDropCells( );

		// Fill the column designs.
		for ( int i = grid.getColumnCount( ); i < layout.getColCount( ); i++ )
		{
			grid.addColumn( new ColumnDesign( ) );
		}

		// update the row design, create the empty cell.
		normalize( );
		for ( int i = grid.getColumnCount( ); i < layout.getColCount( ); i++ )
		{
			grid.addColumn( new ColumnDesign( ) );
		}
	}

	protected void normalize( )
	{
		for ( int i = 0; i < layout.getRowCount( ); i++ )
		{
			Row row = layout.getRow( i );
			RowDesign design = (RowDesign) row.getContent( );
			design.removeCells( );
			for ( int j = 0; j < layout.getColCount( ); j++ )
			{
				Cell cell = row.getCell( j );
				if ( cell.getStatus( ) == Cell.CELL_EMPTY )
				{
					CellDesign cellDesign = new CellDesign( );
					cellDesign.setRowSpan( 1 );
					cellDesign.setColSpan( 1 );
					cellDesign.setColumn( j );
					design.addCell( cellDesign );
				}
				if ( cell.getStatus( ) == Cell.CELL_USED )
				{
					CellDesign cellDesign = ( (CellContent) cell.getContent( ) ).cell;
					cellDesign.setColSpan( cell.getColSpan( ) );
					cellDesign.setRowSpan( cell.getRowSpan( ) );
					cellDesign.setColumn( j );
					design.addCell( cellDesign );
				}
			}
		}

	}

	public void layout( TableItemDesign table )
	{
		layout = new Table(0, table.getColumnCount());
		layoutBand( table.getHeader( ) );
		for ( int i = 0; i < table.getGroupCount( ); i++ )
		{
			layoutBand( table.getGroup( i ).getHeader( ) );
		}
		layoutBand( table.getDetail( ) );
		for ( int i = table.getGroupCount( ) - 1; i >= 0; i-- )
		{
			layoutBand( table.getGroup( i ).getFooter( ) );
		}
		layoutBand( table.getFooter( ) );
		normalize( );
		for ( int i = table.getColumnCount( ); i < layout.getColCount( ); i++ )
		{
			table.addColumn( new ColumnDesign( ) );
		}
	}

	void layoutBand( TableBandDesign band )
	{
		if ( band != null )
		{
			for ( int i = 0; i < band.getRowCount( ); i++ )
			{
				layoutRow( band.getRow( i ) );
			}
			layout.resolveDropCells( );
		}
	}

	void layoutRow( RowDesign row )
	{
		layout.createRow( row );

		for ( int i = 0; i < row.getCellCount( ); i++ )
		{
			CellDesign cell = row.getCell( i );
			int columnNo = cell.getColumn( );
			int rowSpan = cell.getRowSpan( );
			int colSpan = cell.getColSpan( );
			layout.createCell( columnNo, rowSpan, colSpan, new CellContent(
					cell ) );
		}
	}

	private class CellContent implements Cell.Content
	{

		CellDesign cell;

		CellContent( CellDesign cell )
		{
			this.cell = cell;
		}

		public boolean isEmpty( )
		{
			return cell != null;
		}
	}
}
