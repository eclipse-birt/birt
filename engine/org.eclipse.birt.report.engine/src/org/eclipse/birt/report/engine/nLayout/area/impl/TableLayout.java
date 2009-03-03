/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.layout.pdf.cache.CursorableList;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea.TableLayoutInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo;
import org.eclipse.birt.report.engine.util.BidiAlignmentResolver;
import org.w3c.dom.css.CSSValue;

public class TableLayout
{

	protected CursorableList rows = new CursorableList( );

	/**
	 * Border conflict resolver
	 */
	protected BorderConflictResolver bcr = new BorderConflictResolver( );

	protected TableLayoutInfo layoutInfo = null;

	protected ITableContent tableContent;

	protected int startCol;

	protected int endCol;

	public TableLayout( ITableContent tableContent, TableLayoutInfo layoutInfo,
			int startCol, int endCol )
	{
		this.tableContent = tableContent;
		this.layoutInfo = layoutInfo;
		this.startCol = startCol;
		this.endCol = endCol;
		if ( tableContent != null )
		{
			bcr.setRTL( tableContent.isRTL( ) );
		}
	}

	protected int resolveBottomBorder( CellArea cell )
	{
		IStyle tableStyle = tableContent.getComputedStyle( );
		IContent cellContent = cell.getContent( );
		IStyle columnStyle = getColumnStyle( cell.getColumnID( ) );
		IStyle cellContentStyle = cellContent.getComputedStyle( );
		IStyle rowStyle = ( (IContent) cellContent.getParent( ) ).getComputedStyle( );
		if ( tableStyle != null && rowStyle != null && columnStyle != null
				&& cellContentStyle != null )
		{
			return 0;
		}
		BorderInfo border = bcr.resolveTableBottomBorder( tableStyle, rowStyle,
				columnStyle, cellContentStyle );
		if ( border != null )
		{
			cell.getBoxStyle( ).setBottomBorder( border );
			return border.getWidth( );
		}
		return 0;
	}

	protected void add( ContainerArea area, ArrayList rows )
	{
		if ( area instanceof RowArea )
		{
			rows.add( area );
		}
		else
		{
			Iterator iter = area.getChildren( );
			while ( iter.hasNext( ) )
			{
				ContainerArea container = (ContainerArea) iter.next( );
				add( container, rows );
			}
		}
	}

	public void remove( TableArea table )
	{
		ArrayList rowColloection = new ArrayList( );
		add( table, rowColloection );
		Iterator iter = rows.iterator( );
		while ( iter.hasNext( ) )
		{
			RowArea row = (RowArea) iter.next( );
			if ( rowColloection.contains( row ) )
			{
				iter.remove( );
			}
		}
		rows.resetCursor( );
	}

	protected IStyle getLeftCellContentStyle( RowArea lastRow, RowArea currentRow, int columnID )
	{
		CellArea cell = currentRow.getCell( columnID-1 );
		if ( cell == null && lastRow!=null )
		{
			cell =  lastRow.getCell( columnID - 1 );
		}
		if(cell!=null)
		{
			return cell.getContent( ).getComputedStyle( );
		}
		return null;
	}

	/**
	 * resolve cell border conflict
	 * 
	 * @param cellArea
	 */
	public void resolveBorderConflict( CellArea cellArea, boolean isFirst )
	{
		IContent cellContent = cellArea.getContent( );
		int columnID = cellArea.getColumnID( );
		int colSpan = cellArea.getColSpan( );
		IRowContent row = (IRowContent) cellContent.getParent( );
		IStyle cellContentStyle = cellContent.getComputedStyle( );
		IStyle tableStyle = tableContent.getComputedStyle( );
		IStyle rowStyle = row.getComputedStyle( );
		IStyle columnStyle = getColumnStyle( columnID );
		IStyle preRowStyle = null;
		IStyle preColumnStyle = getColumnStyle( columnID - 1 );
		IStyle leftCellContentStyle = null;
		IStyle topCellStyle = null;

		RowArea lastRow = null;

		if ( rows.size( ) > 0 )
		{
			lastRow = (RowArea) rows.getCurrent( );
		}

		if ( lastRow != null )
		{
			
			preRowStyle = lastRow.getContent( ).getComputedStyle( );
			CellArea cell = lastRow.getCell( columnID );
			if ( cell != null && cell.getContent( ) != null )
			{
				topCellStyle = cell.getContent( ).getComputedStyle( );
			}
		}
		if(columnID>0)
		{
			leftCellContentStyle = getLeftCellContentStyle( lastRow, (RowArea)cellArea.getParent( ), columnID );
		}
		// FIXME
		if ( rows.size( ) == 0 && lastRow == null )
		{
			// resolve top border
			if ( isFirst )
			{
				if ( tableStyle != null || rowStyle != null
						|| cellContentStyle != null )
				{
					BorderInfo border = bcr.resolveTableTopBorder( tableStyle,
							rowStyle, columnStyle, cellContentStyle );
					if ( border != null )
					{
						cellArea.getBoxStyle( ).setTopBorder( border );
					}
				}
			}
			else
			{
				if ( tableStyle != null )
				{
					BorderInfo border = bcr.resolveTableTopBorder( tableStyle,
							null, columnStyle, null );
					if ( border != null )
					{
						cellArea.getBoxStyle( ).setTopBorder( border );
					}
				}
			}

			// resolve left border
			if ( columnID == startCol )
			{
				if ( tableStyle != null || rowStyle != null
						|| cellContentStyle != null )
				{
					BorderInfo border = bcr.resolveTableLeftBorder( tableStyle,
							rowStyle, columnStyle, cellContentStyle );
					if ( border != null )
					{
						cellArea.getBoxStyle( ).setLeftBorder( border );
					}
				}
			}
			else
			{
				if ( leftCellContentStyle != null || cellContentStyle != null )
				{
					BorderInfo border = bcr.resolveCellLeftBorder(
							preColumnStyle, columnStyle, leftCellContentStyle,
							cellContentStyle );
					if ( border != null )
					{
						cellArea.getBoxStyle( ).setLeftBorder( border );
					}
				}
			}

			// resolve right border

			if ( columnID + colSpan - 1 == endCol )
			{
				if ( tableStyle != null || rowStyle != null
						|| cellContentStyle != null )
				{
					BorderInfo border = bcr
							.resolveTableRightBorder( tableStyle, rowStyle,
									columnStyle, cellContentStyle );
					if ( border != null )
					{
						cellArea.getBoxStyle( ).setRightBorder( border );
					}
				}
			}

		}
		else
		{
			if ( isFirst )
			{
				if ( preRowStyle != null || rowStyle != null
						|| topCellStyle != null || cellContentStyle != null )
				{
					BorderInfo border = bcr.resolveCellTopBorder( preRowStyle,
							rowStyle, topCellStyle, cellContentStyle );
					if ( border != null )
					{
						cellArea.getBoxStyle( ).setTopBorder( border );
					}
				}
			}
			else
			{
				if ( preRowStyle != null || topCellStyle != null )
				{
					BorderInfo border = bcr.resolveCellTopBorder( preRowStyle,
							null, topCellStyle, null );
					if ( border != null )
					{
						cellArea.getBoxStyle( ).setTopBorder( border );
					}
				}
			}
			// resolve left border
			if ( columnID == startCol )
			{
				// first column
				if ( tableStyle != null || rowStyle != null
						|| cellContentStyle != null )
				{
					BorderInfo border = bcr.resolveTableLeftBorder( tableStyle,
							rowStyle, columnStyle, cellContentStyle );
					if ( border != null )
					{
						cellArea.getBoxStyle( ).setLeftBorder( border );
					}
				}
			}
			else
			{
				// TODO fix row span conflict
				if ( leftCellContentStyle != null || cellContentStyle != null )
				{
					BorderInfo border = bcr.resolveCellLeftBorder(
							preColumnStyle, columnStyle, leftCellContentStyle,
							cellContentStyle );
					if ( border != null )
					{
						cellArea.getBoxStyle( ).setLeftBorder( border );
					}
				}
			}
			// resolve right border
			if ( columnID + colSpan - 1 == endCol )
			{
				if ( tableStyle != null || rowStyle != null
						|| cellContentStyle != null )
				{
					BorderInfo border = bcr
							.resolveTableRightBorder( tableStyle, rowStyle,
									columnStyle, cellContentStyle );
					if ( border != null )
					{
						cellArea.getBoxStyle( ).setRightBorder( border );
					}
				}
			}
		}
	}

	/**
	 * get column style
	 * 
	 * @param columnID
	 * @return
	 */
	private IStyle getColumnStyle( int columnID )
	{
		// current not support column style
		return null;
	}

	private int getAllocatedHeight( AbstractArea area )
	{
		if ( area instanceof ContainerArea )
		{
			return ( (ContainerArea) area ).getAllocatedHeight( );
		}
		return area.getHeight( );
	}

	private int getAllocatedWidth( AbstractArea area )
	{
		if ( area instanceof ContainerArea )
		{
			return ( (ContainerArea) area ).getAllocatedWidth( );
		}
		return area.getWidth( );
	}

	protected void verticalAlign( CellArea cell )
	{
		IContent content = cell.getContent( );
		if ( content == null )
		{
			return;
		}
		CSSValue verticalAlign = content.getComputedStyle( ).getProperty(
				IStyle.STYLE_VERTICAL_ALIGN );
		if ( IStyle.BOTTOM_VALUE.equals( verticalAlign )
				|| IStyle.MIDDLE_VALUE.equals( verticalAlign ) )
		{
			int totalHeight = 0;
			Iterator iter = cell.getChildren( );
			while ( iter.hasNext( ) )
			{
				AbstractArea child = (AbstractArea) iter.next( );
				totalHeight += getAllocatedHeight( child );
			}
			int offset = cell.getContentHeight( ) - totalHeight;
			if ( offset > 0 )
			{
				if ( IStyle.BOTTOM_VALUE.equals( verticalAlign ) )
				{
					iter = cell.getChildren( );
					while ( iter.hasNext( ) )
					{
						AbstractArea child = (AbstractArea) iter.next( );
						child.setPosition( child.getX( ), child.getY( )
								+ offset );
					}
				}
				else if ( IStyle.MIDDLE_VALUE.equals( verticalAlign ) )
				{
					iter = cell.getChildren( );
					while ( iter.hasNext( ) )
					{
						AbstractArea child = (AbstractArea) iter.next( );
						child.setPosition( child.getX( ), child.getY( )
								+ offset / 2 );
					}
				}

			}
		}

		CSSValue align = content.getComputedStyle( ).getProperty(
				IStyle.STYLE_TEXT_ALIGN );

		// bidi_hcg: handle empty or justify align in RTL direction as right
		// alignment
		boolean isRightAligned = BidiAlignmentResolver.isRightAligned( content,
				align, false );

		// single line
		if ( isRightAligned || IStyle.CENTER_VALUE.equals( align ) )
		{

			Iterator iter = cell.getChildren( );
			while ( iter.hasNext( ) )
			{
				AbstractArea area = (AbstractArea) iter.next( );
				int spacing = cell.getContentWidth( )
						- getAllocatedWidth( area );
				if ( spacing > 0 )
				{
					if ( isRightAligned )
					{
						area.setPosition( spacing + area.getX( ), area.getY( ) );
					}
					else if ( IStyle.CENTER_VALUE.equals( align ) )
					{
						area.setPosition( spacing / 2 + area.getX( ), area
								.getY( ) );
					}
				}
			}
		}
	}

	public void reset( TableArea table )
	{
		Iterator iter = rows.iterator( );
		while ( iter.hasNext( ) )
		{
			RowArea row = (RowArea) iter.next( );
			if ( table.contains( row ) )
			{
				iter.remove( );
			}
		}

		rows.resetCursor( );
	}

	/**
	 * When pagination happens, if drop cells should be finished by force, we
	 * need to end these cells and vertical align for them.
	 * 
	 */
	public int resolveAll( )
	{
		if ( rows.size( ) == 0 )
		{
			return 0;
		}
		RowArea row = (RowArea) rows.getCurrent( );
		int originalRowHeight = row.getHeight( );
		int height = originalRowHeight;

		for ( int i = startCol; i <= endCol; i++ )
		{
			CellArea cell = row.getCell( i );
			if ( null == cell )
			{
				// After padding empty cell and dummy cell, the cell should not
				// be null.
				continue;
			}
			if ( cell instanceof DummyCell )
			{
				DummyCell dummyCell = (DummyCell) cell;
				int delta = dummyCell.getDelta( );
				height = Math.max( height, dummyCell.getCell( ).getHeight( ) - delta );
			}
			else
			{
				height = Math.max( height, cell.getHeight( ) );
			}
			i = i + cell.getColSpan( ) - 1;
		}

		int dValue = height - originalRowHeight;
		if(dValue>0)
		{
			for ( int i = startCol; i <= endCol; i++ )
			{
				CellArea cell = row.getCell( i );
				if ( cell == null )
				{
					// this should NOT happen.
					continue;
				}
				if ( cell instanceof DummyCell )
				{
					int delta = ( (DummyCell) cell ).getDelta( );
					if ( cell.getRowSpan( ) == 1 )
					// this dummyCell and it reference cell height have already been
					// updated.
					{
							CellArea refCell = ( (DummyCell) cell ).getCell( );
							refCell.setHeight( delta + height );
							verticalAlign( refCell );
					}
					else
					{
						CellArea refCell = ( (DummyCell) cell ).getCell( );
						if ( delta < height )
						{
							refCell.setHeight( refCell.getHeight( ) - delta
									+ height );
						}
						verticalAlign( refCell );
					}
				}
				else
				{
					if ( dValue != 0 )
					{
						cell.setHeight( height );
						verticalAlign( cell );
					}
				}
				i = i + cell.getColSpan( ) - 1;
			}
		}
		row.setHeight( height );
		return dValue;
	}

	public int resolveBottomBorder( )
	{
		if ( rows.size( ) == 0 )
		{
			return 0;
		}
		RowArea row = (RowArea) rows.getCurrent( );
		int result = 0;
		int width = 0;
		for ( int i = startCol; i <= endCol; i++ )
		{
			CellArea cell = row.getCell( i );
			if ( cell == null )
			{
				// this should NOT happen.
				continue;
			}
			if ( cell instanceof DummyCell )
			{
				width = resolveBottomBorder( ( (DummyCell) cell ).getCell( ) );
			}
			else
			{
				width = resolveBottomBorder( cell );
			}

			if ( width > result )
				result = width;
			i = i + cell.getColSpan( ) - 1;
		}

		// update cell height
		if ( result > 0 )
		{
			row.setHeight( row.getHeight( ) + result );
			for ( int i = startCol; i <= endCol; i++ )
			{
				CellArea cell = row.getCell( i );
				if ( cell != null )
				{
					cell.setHeight( cell.getHeight( ) + result );
					i = i + cell.getColSpan( ) - 1;
				}
			}
		}
		return result;
	}

	/**
	 * Adds a list of rows to current rows.
	 */
	public void addRows( CursorableList rs )
	{
		Iterator iter = rs.iterator( );
		while ( iter.hasNext( ) )
		{
			rows.add( iter.next( ) );
		}
	}

	/**
	 * Adds the updated row wrapper to rows.
	 */
	public void addRow( RowArea rowArea )
	{
		updateRow( rowArea );
		rows.add( rowArea );
	}

	/**
	 * 1) Creates row wrapper. 2) For the null cell in the row wrapper, fills
	 * the relevant position with dummy cell or empty cell. 3) Updates the
	 * height of the row and the cells in the row.
	 * 
	 * @param rowArea
	 *            current rowArea.
	 */
	private void updateRow( RowArea rowArea )
	{
		RowArea lastRow = (RowArea) rows.getCurrent( );

		int height = rowArea.getSpecifiedHeight( );
		for ( int i = startCol; i <= endCol; i++ )
		{
			CellArea upperCell = null;
			if ( lastRow != null )
			{
				upperCell = lastRow.getCell( i );
			}
			// upperCell has row span, or is a drop cell.
			if ( upperCell != null && ( upperCell.getRowSpan( ) > 1 ) )
			{
				DummyCell dummyCell = createDummyCell( upperCell );
				rowArea.setCell( dummyCell );

				int delta = dummyCell.getDelta( );
				if ( dummyCell.getRowSpan( ) == 1 )
				{
					height = Math.max( height, dummyCell.getCell( ).getHeight( ) - delta );
				}
				i = i + upperCell.getColSpan( ) - 1;
			}
			// upperCell has NO row span, and is NOT a drop cell.
			// or upperCell is null. In this case, we need not care about
			// the upperCell.
			else
			{
				CellArea cell = rowArea.getCell( i );
				
				if (  cell != null && cell.getRowSpan( ) == 1 )
				{
					height = Math.max( height, cell.getHeight( ) );
					i = i + cell.getColSpan( ) - 1;
				}
				
			}
		}
		updateRowHeight( rowArea, height );
	}

	/**
	 * Creates dummy cell and updates its delta value.
	 * 
	 * @param upperCell
	 *            the upper cell.
	 * @return the created dummy cell.
	 */
	private DummyCell createDummyCell( CellArea upperCell )
	{
		DummyCell dummyCell = null;
		CellArea refCell = null;
		RowArea lastRow = (RowArea) rows.getCurrent( );
		int lastRowHeight = lastRow.getHeight( );
		int delta = 0;
		if ( upperCell instanceof DummyCell )
		{
			refCell = ( (DummyCell) upperCell ).getCell( );
			dummyCell = new DummyCell( refCell );
			delta = ( (DummyCell) upperCell ).getDelta( ) + lastRowHeight;
			dummyCell.setDelta( delta );
		}
		else
		{
			refCell = upperCell;
			dummyCell = new DummyCell( upperCell );
			dummyCell.setDelta( lastRowHeight );
		}
		dummyCell.setRowSpan( upperCell.getRowSpan( ) - 1 );
		dummyCell.setColSpan( upperCell.getColSpan( ) );
		return dummyCell;
	}


	/**
	 * Updates the row height and the height of the cells in the row.
	 * 
	 * @param rowArea
	 * @param height
	 */
	private void updateRowHeight( RowArea row, int height )
	{
		if ( height < 0 )
			return;
		row.setHeight( height );
		for ( int i = startCol; i <= endCol; i++ )
		{
			CellArea cell = row.getCell( i );
			if(cell!=null)
			{
				if ( cell.getRowSpan( ) == 1 )
				{
					if ( cell instanceof DummyCell )
					{
						CellArea refCell = ( (DummyCell) cell ).getCell( );
						int delta = ( (DummyCell) cell ).getDelta( );
						refCell.setHeight( delta	+ height );
						verticalAlign( refCell );
					}
					else
					{
						cell.setHeight( height );
						verticalAlign( cell );
					}
				}
				i = i + cell.getColSpan( ) - 1;
			}
		}
	}

	public int getCellWidth( int startColumn, int endColumn )
	{
		if ( layoutInfo != null )
		{
			return layoutInfo.getCellWidth( startColumn, endColumn );
		}
		return 0;
	}

	/*
	 * public RowArea getArea( ) { return row; } }
	 */
	public CursorableList getRows( )
	{
		return rows;
	}

}
