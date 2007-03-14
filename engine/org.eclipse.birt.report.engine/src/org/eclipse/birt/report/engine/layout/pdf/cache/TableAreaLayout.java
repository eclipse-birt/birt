/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.pdf.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;
import org.eclipse.birt.report.engine.layout.pdf.BorderConflictResolver;
import org.eclipse.birt.report.engine.layout.pdf.PDFTableLM.TableLayoutInfo;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;


//FIXME add DropCell extends CellArea

public class TableAreaLayout
{
	
	protected CursorableList rows = new CursorableList();
	
	/**
	 * Border conflict resolver
	 */
	protected BorderConflictResolver bcr = new BorderConflictResolver( );

	/**
	 * number of table column
	 */
	protected int columnNumber;

	protected TableLayoutInfo layoutInfo = null;
	
	protected ITableContent tableContent;
	
	protected ICellContent lastCellContent;
		
	protected int start = 0;
	
	protected int end = 0;
	
	protected boolean hasDropCell = true;
	
	protected UnresolvedRow unfinishedRow = null;
	
	protected boolean firstRow = true;

	public TableAreaLayout(ITableContent tableContent, TableLayoutInfo layoutInfo, int start, int columnNubmer)
	{
		this.tableContent = tableContent;
		this.layoutInfo = layoutInfo;
		this.start = start;
		this.end = start + columnNumber;
		this.columnNumber = columnNubmer;
	}
	
	
	protected int resolveBottomBorder(CellArea cell)
	{
		IStyle tableStyle = tableContent.getComputedStyle( );
		IContent cellContent = cell.getContent( );
		IStyle rowStyle = ((IContent)cellContent.getParent()).getComputedStyle( ) ;
		IStyle columnStyle = getColumnStyle( cell.getColumnID( ) );
		IStyle cellContentStyle = cellContent.getComputedStyle( );
		IStyle cellAreaStyle = cell.getStyle( );
		bcr.resolveTableBottomBorder( tableStyle, rowStyle,
				columnStyle, cellContentStyle, cellAreaStyle );
		return PropertyUtil.getDimensionValue( cellAreaStyle
						.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
	}
	
	protected void add(ContainerArea area, ArrayList rows)
	{
		if(area instanceof RowArea)
		{
			rows.add( area );
		}
		else
		{
			Iterator iter = area.getChildren( );
			while(iter.hasNext( ))
			{
				ContainerArea container = (ContainerArea)iter.next();
				add(container, rows);
			}
		}
	}
	
	public void remove(TableArea table)
	{
		ArrayList rowColloection = new ArrayList();
		add(table, rowColloection);
		Iterator iter = rows.iterator( );
		while(iter.hasNext( ))
		{
			Row row = (Row)iter.next( );
			if(rowColloection.contains( row.getArea( ) ))
			{
				iter.remove( );
			}
		}
		
		rows.resetCursor( );
	}
	/**
	 * resolve cell border conflict
	 * 
	 * @param cellArea
	 */
	public void resolveBorderConflict( CellArea cellArea )
	{
		IContent cellContent = cellArea.getContent( );
		int columnID = cellArea.getColumnID( );
		int rowID = cellArea.getRowID( );
		int colSpan = cellArea.getColSpan( );
		IRowContent row = (IRowContent) cellContent.getParent( );
		IStyle cellContentStyle = cellContent.getComputedStyle( );
		IStyle cellAreaStyle = cellArea.getStyle( );
		IStyle tableStyle = tableContent.getComputedStyle( );
		IStyle rowStyle = row.getComputedStyle( );
		IStyle columnStyle = getColumnStyle( columnID );
		IStyle preRowStyle = null;
		IStyle preColumnStyle = getColumnStyle( columnID - 1 );
		IStyle leftCellContentStyle = null;
		IStyle topCellStyle = null;

		if ( lastCellContent!=null && lastCellContent.getColumn( )<columnID)
		{
			leftCellContentStyle = lastCellContent.getComputedStyle( );
		}
		if(rows.size( )>0 )
		{
			Row lastRow = (Row)rows.getCurrent( );
			if(lastRow!=null)
			{
				preRowStyle = lastRow.getContent( ).getComputedStyle( );
				CellArea cell = lastRow.getCell( columnID );
				if(cell!=null && cell.getContent( )!=null)
				{
					topCellStyle = cell.getContent( ).getComputedStyle( );
				}
			}
		}
		
					
		if ( rowID == 0 )
		{
			// resolve top border
			bcr.resolveTableTopBorder( tableStyle, rowStyle, columnStyle,
					cellContentStyle, cellAreaStyle );

			// resolve left border
			if ( columnID == 0 )
			{
				bcr.resolveTableLeftBorder( tableStyle, rowStyle, columnStyle,
						cellContentStyle, cellAreaStyle );
			}
			else
			{
				bcr.resolveCellLeftBorder( preColumnStyle, columnStyle,
						leftCellContentStyle, cellContentStyle, cellAreaStyle );
			}

			// resovle right border

			if ( columnID + colSpan == columnNumber )
			{
				bcr.resolveTableRightBorder( tableStyle, rowStyle, columnStyle,
						cellContentStyle, cellAreaStyle );
			}

		}
		else
		{
			bcr.resolveCellTopBorder( preRowStyle, rowStyle, topCellStyle,
						cellContentStyle, cellAreaStyle );
			// resolve left border
			if ( columnID == 0 )
			{
				// first column
				bcr.resolveTableLeftBorder( tableStyle, rowStyle, columnStyle,
						cellContentStyle, cellAreaStyle );
			}
			else
			{
				// TODO fix row span conflict
				bcr.resolveCellLeftBorder( preColumnStyle, columnStyle,
						leftCellContentStyle, cellContentStyle, cellAreaStyle );
			}
			// resolve right border
			if ( columnID + colSpan == columnNumber )
			{
				bcr.resolveTableRightBorder( tableStyle, rowStyle, columnStyle,
						cellContentStyle, cellAreaStyle );
			}
		}
		
		lastCellContent = (ICellContent)cellContent;

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
	
	
	
	
	
	protected void verticalAlign( CellArea c )
	{
		CellArea cell;
		if(c instanceof DummyCell)
		{
			cell = ((DummyCell)c).getCell( );
		}
		else
		{
			cell = c;
		}
		IContent content = cell.getContent( );
		if ( content == null )
		{
			return;
		}
		CSSValue verticalAlign = content.getComputedStyle( ).getProperty( IStyle.STYLE_VERTICAL_ALIGN );
		if ( IStyle.BOTTOM_VALUE.equals( verticalAlign )
				|| IStyle.MIDDLE_VALUE.equals( verticalAlign ) )
		{
			int totalHeight = 0;
			Iterator iter = cell.getChildren( );
			while ( iter.hasNext( ) )
			{
				AbstractArea child = (AbstractArea) iter.next( );
				totalHeight += child.getAllocatedHeight( );
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
						child.setAllocatedPosition( child.getAllocatedX( ),
								child.getAllocatedY( ) + offset );
					}
				}
				else if ( IStyle.MIDDLE_VALUE.equals( verticalAlign ) )
				{
					iter = cell.getChildren( );
					while ( iter.hasNext( ) )
					{
						AbstractArea child = (AbstractArea) iter.next( );
						child.setAllocatedPosition( child.getAllocatedX( ),
								child.getAllocatedY( ) + offset / 2 );
					}
				}

			}
		}
		
		CSSValue align = content.getComputedStyle( ).getProperty( IStyle.STYLE_TEXT_ALIGN );
		// single line
		if ( ( IStyle.RIGHT_VALUE.equals(  align ) || IStyle.CENTER_VALUE
				.equals( align ) ) )
		{
			
			Iterator iter = cell.getChildren( );
			while ( iter.hasNext( ) )
			{
				AbstractArea area = (AbstractArea) iter.next( );
				int spacing = cell.getContentWidth( ) - area.getAllocatedWidth( ) ;
				if(spacing>0)
				{
					if ( IStyle.RIGHT_VALUE.equals( align ) )
					{
						area.setAllocatedPosition( spacing + area.getAllocatedX( ),
								area.getAllocatedY( ) );
					}
					else if ( IStyle.CENTER_VALUE
							.equals( align ) )
					{
						area.setAllocatedPosition( spacing / 2
								+ area.getAllocatedX( ), area.getAllocatedY( ) );
					}
				}
			}

		}
	}
	
	public void reset(TableArea table)
	{
		Iterator iter = rows.iterator( );
		while(iter.hasNext( ))
		{
			Row row = (Row)iter.next( );
			if(table.contains( row.getArea( ) ))
			{
				iter.remove( );
			}
		}
		
		rows.resetCursor( );
	}
	
	
	public void resolveDropCells(int dropValue)
	{
		/*
		 * 1. scan current row and calculate row height
		 * 2. update the height of drop cells
		 * 3. update the height of row
		 */
		assert(dropValue<0);
		
		if(rows.size( )==0 || !hasDropCell)
		{
			return;
		}
		Row row = (Row)rows.getCurrent( );
		assert(row!=null);
		int rowHeight = row.getArea( ).getHeight( );
		int height = rowHeight;
		boolean needResolve = false;
		//scan for Max height for drop cells
		for(int i=0; i<columnNumber; i++)
		{
			CellArea cell = row.getCell( start + i );
			if(cell!=null && cell.getRowSpan( )==dropValue)
			{
				height = Math.max( height, cell.getHeight( ) );
				needResolve = true;
			}
		}
		
		if(needResolve)
		{
			HashSet dropCells = new HashSet();
			int delta = height - rowHeight;
			for(int i=0; i<columnNumber; i++)
			{
				CellArea cell = row.getCell( start + i );
				if(cell==null)
				{
					continue;
				}
				if(cell.getRowSpan( )==dropValue)
				{
					if(cell instanceof DummyCell)
					{
						CellArea ref = ((DummyCell)cell).getCell( );
						int cellHeight = cell.getHeight( );
						int refHeight = ref.getHeight( );
						if(!dropCells.contains( ref ))
						{
							ref.setHeight( refHeight - cellHeight + delta );
							verticalAlign( ref );
							dropCells.add( ref );
						}
					}
					else
					{
						cell.setHeight( height );
						verticalAlign( cell );							
					}
					cell.setRowSpan(1);
				}
				else if(cell.getRowSpan( )==1)
				{
					if(cell instanceof DummyCell)
					{
						CellArea ref = ((DummyCell)cell).getCell( );
						if(!dropCells.contains( ref ))
						{
							ref.setHeight( ref.getHeight( ) + delta );
							if(delta>0)
							{
								verticalAlign( ref );
							}
							dropCells.add( ref );
						}
					}
					else
					{
						cell.setHeight( height );
						verticalAlign( cell );							
					}
				}
					
			}
			
		}
		
		
	}
	
	protected void keepUnresolvedCell(Row lastRow)
	{
		unfinishedRow = new UnresolvedRow();
		for(int i=0; i<columnNumber; i++)
		{
			CellArea cell = lastRow.getCell( start + i );
			if(cell!=null)
			{
				int rowSpan = cell.getRowSpan( );
				if(rowSpan< 0 || rowSpan>1)
				{
					//FIXME resolve conflict?
					unfinishedRow.addUnresolvedCell( (ICellContent) cell
							.getContent( ), getLeftRowSpan(
									lastRow.finished, rowSpan ) );
				}
				else if(rowSpan==1)
				{
					if(!lastRow.finished)
					{
						unfinishedRow.addUnresolvedCell( (ICellContent) cell
								.getContent( ), 1 );
					}
				}
			}
				
		}

	}
	
	public void resolveAll()
	{
		/*
		 * 1. scan current row and calculate row height
		 * 2. update the height of drop cells
		 * 3. update the height of row
		 */
		
		if(rows.size( )==0 || !hasDropCell)
		{
			return;
		}
		Row row = (Row)rows.getCurrent( );
		int rowHeight = row.getArea( ).getHeight( );
		int height = rowHeight;
		boolean hasDropCell = false;
		//scan for Max height for drop cells
		for(int i=0; i<columnNumber; i++)
		{
			CellArea cell = row.getCell( start + i );
			if(cell!=null)
			{
				if(cell.getRowSpan( )< 0 || cell.getRowSpan( )>1 )
				{
					height = Math.max( height, cell.getHeight( ) );
					hasDropCell = true;
				}
			}
		}
		
		if(hasDropCell)
		{
			unfinishedRow = new UnresolvedRow();
			HashSet dropCells = new HashSet();
			int delta = height - rowHeight;
			for(int i=0; i<columnNumber; i++)
			{
				CellArea cell = row.getCell( start + i );
				if(cell==null)
				{
					continue;
				}
				int rowSpan = cell.getRowSpan( );
				if(rowSpan< 0 || rowSpan>1)
				{
					if(cell instanceof DummyCell)
					{
						CellArea ref = ((DummyCell)cell).getCell( );
						int cellHeight = cell.getHeight( );
						int refHeight = ref.getHeight( );
						if(!dropCells.contains( ref ))
						{
							ref.setHeight( refHeight - cellHeight + delta );
							verticalAlign( ref );
							dropCells.add( ref );
						}
					}
					else
					{
						cell.setHeight( height );
						verticalAlign( cell );							
					}
					//FIXME resolve conflict?
					unfinishedRow.addUnresolvedCell( (ICellContent) cell
							.getContent( ), getLeftRowSpan(
							row.finished, rowSpan ) );
				}
				else if(rowSpan==1)
				{
					if(cell instanceof DummyCell)
					{
						CellArea ref = ((DummyCell)cell).getCell( );
						if(!dropCells.contains( ref ))
						{
							ref.setHeight( ref.getHeight( ) + delta );
							if(delta>0)
							{
								verticalAlign( ref );
							}
							dropCells.add( ref );
						}
					}
					else
					{
						cell.setHeight( height );
						verticalAlign( cell );							
					}
					if(row!=null && !row.finished)
					{
						unfinishedRow.addUnresolvedCell( (ICellContent) cell
								.getContent( ), 1 );
					}
				}
					
			}
			
		}
		
		if(hasDropCell || (row!=null && !row.finished))
		{
			this.keepUnresolvedCell( row );
		}
	}
	
	private int getLeftRowSpan(boolean finished, int rowSpan)
	{
		if(rowSpan<0)
		{
			return rowSpan;
		}
		else
		{
			if(finished)
			{
				return rowSpan - 1;
			}
			else
			{
				return rowSpan;
			}
		}
	}
	
	public int resolveBottomBorder()
	{
		if(rows.size()==0)
		{
			return 0;
		}
		Row row = (Row)rows.getCurrent( );
		HashSet cells = new HashSet();
		int result = 0;
		for(int i=0; i<columnNumber; i++)
		{
			CellArea cell = row.getCell( start + i );
			if(cell!=null)
			{
				if(cell instanceof DummyCell)
				{
					CellArea ref = ((DummyCell)cell).getCell( );
					if(!cells.contains( ref ))
					{
						int width = resolveBottomBorder( ref );
						if ( width > result )
							result = width;
						if(width>0)
						{
							ref.setHeight( ref.getHeight( ) + width );
						}
						cells.add( ref );
					}
				}
				else
				{
					if(!cells.contains( cell ))
					{
						int width = resolveBottomBorder( cell );
						if ( width > result )
							result = width;
						if(width>0)
						{
							cell.setHeight( cell.getHeight( ) + width );
						}
						cells.add( cell );
					}
				}
			}
		}
		return result;
	}
	
	protected void mergeDropCell(RowArea row)
	{
		if(unfinishedRow==null || unfinishedRow.isEmpty( ))
		{
			return;
		}
		CellArea[] cells = new CellArea[columnNumber];
		Iterator iter = row.getChildren( );
		while(iter.hasNext( ))
		{
			CellArea cell = (CellArea)iter.next( );
			int colStart = cell.getColumnID( );
			int colEnd = colStart + cell.getColSpan( );
			for(int i=colStart; i<colEnd; i++)
			{
				int index = colStart - start;
				if(index>=0 && index<columnNumber)
				{
					cells[colStart-start] = cell;
				}
			}
		}
		
		for(int i=0; i<columnNumber; i++)
		{
			if(cells[i]==null)
			{
				DropCellContent dropCellContent = unfinishedRow.getDropCellContent( start + i );
				if(dropCellContent!=null)
				{
					//FIXME resolve column span conflict
					//FIXME resolve content hierarchy
					ICellContent cellContent = dropCellContent.cell;
					int startColumn = cellContent.getColumn( );
					int endColumn = cellContent.getColSpan( ) + startColumn;
					CellArea emptyCell = AreaFactory
							.createCellArea( cellContent );
					emptyCell.setRowSpan( dropCellContent.rowSpan );
					resolveBorderConflict( emptyCell );
					emptyCell.setWidth( getCellWidth( startColumn, endColumn ) );
					emptyCell.setPosition( layoutInfo.getXPosition( i ), 0 );
					row.addChild( emptyCell );
				}
			}
		}
	}
	
	public void addRow(RowArea rowArea, boolean finished)
	{
		/*
		 * 1. create row wrapper, and add it to rows
		 */
		Row lastRow = (Row)rows.getCurrent( );
		Row row = new Row(rowArea, start, columnNumber, finished );
		for(int i=0; i<columnNumber; i++)
		{
			CellArea lastCell = null;
			if(lastRow!=null)
			{
				lastCell = lastRow.getCell( start + i );
			}
			CellArea cell = row.getCell( start + i );
			if(lastCell!=null &&(lastCell.getRowSpan( )>1 || lastCell.getRowSpan( )<0))
			{
				if(cell==null)
				{
					DummyCell dummyCell = null;
					if(lastCell instanceof DummyCell)
					{
						DummyCell refDummy = ((DummyCell)lastCell);
						dummyCell = new DummyCell(refDummy.getCell( ));
						if(lastCell.getRowSpan()>0)
						{
							dummyCell.setRowSpan( lastCell.getRowSpan( )-1 );
						}
						else
						{
							dummyCell.setRowSpan( lastCell.getRowSpan( ) );
						}
						dummyCell.setHeight( refDummy.getHeight( ) );
						
					}
					else
					{
						dummyCell = new DummyCell(lastCell);
						if(lastCell.getRowSpan()>0)
						{
							dummyCell.setRowSpan( lastCell.getRowSpan( )-1 );
						}
						else
						{
							dummyCell.setRowSpan( lastCell.getRowSpan( ) );
						}
						dummyCell.setHeight( lastCell.getHeight( ) - lastRow.getArea( ).getHeight( ));
					}
					
					row.addArea( dummyCell );
					i = i + dummyCell.getColSpan( ) -1;
				}
			}
		}
		
		rows.add( row );

	}
	
	public void skipRow(RowArea area)
	{
		
	}
	
	public void updateRow(RowArea rowArea, int specifiedHeight, boolean finished)
	{
		/*
		 * 1. create row wrapper, and add it to rows
		 * 2. resolve drop conflict, formailize current row.
		 * 3. resolve row height
		 * 
		 */
		hasDropCell = !finished;
		Row lastRow = (Row)rows.getCurrent( );
		if(lastRow==null)
		{
			mergeDropCell( rowArea );
		}
		Row row = new Row(rowArea, start, columnNumber, finished );
		int height = specifiedHeight;
		ArrayList dropCells = new ArrayList();
		
		for(int i=0; i<columnNumber; i++)
		{
			CellArea lastCell = null;
			if(lastRow!=null)
			{
				lastCell = lastRow.getCell( start + i );
			}
			CellArea cell = row.getCell( start + i );
			if(lastCell!=null &&(lastCell.getRowSpan( )>1 || lastCell.getRowSpan( )<0))
			{
				//should remove conflict area. 
				if(cell!=null)
				{
					row.remove( start + i );
				}
				DummyCell dummyCell = null;
				if(lastCell instanceof DummyCell)
				{
					DummyCell refDummy = ((DummyCell)lastCell);
					dummyCell = new DummyCell(refDummy.getCell( ));
					if(lastCell.getRowSpan()>0)
					{
						dummyCell.setRowSpan( lastCell.getRowSpan( )-1 );
					}
					else
					{
						dummyCell.setRowSpan( lastCell.getRowSpan( ) );
					}
					dummyCell.setHeight( refDummy.getHeight( ) );
					
				}
				else
				{
					dummyCell = new DummyCell(lastCell);
					if(lastCell.getRowSpan()>0)
					{
						dummyCell.setRowSpan( lastCell.getRowSpan( )-1 );
					}
					else
					{
						dummyCell.setRowSpan( lastCell.getRowSpan( ) );
					}
					dummyCell.setHeight( lastCell.getHeight( ) - lastRow.getArea( ).getHeight( ));
				}
				
				row.addArea( dummyCell );
				i = i + dummyCell.getColSpan( ) -1;
				if(dummyCell.getRowSpan( )==1)
				{
					height = Math.max( height, dummyCell.getHeight( ) );
					dropCells.add( dummyCell );
				}
			}
			else
			{
				if(cell!=null)
				{
					if( cell.getRowSpan( )==1)
					{
						height = Math.max( height, cell.getHeight( ) );
					}
				}
				if(cell==null)
				{
					//create a empty cell
					ICellContent cellContent = tableContent.getReportContent( )
								.createCellContent( );
					cellContent.setColumn( i );
					cellContent.setColSpan( 1 );
					cellContent.setRowSpan( 1 );
					cellContent.setParent( row.getArea( ).getContent( ) );
					
					int startColumn = cellContent.getColumn( );
					int endColumn = cellContent.getColSpan( ) + startColumn;
					CellArea emptyCell = AreaFactory
							.createCellArea( cellContent );

					resolveBorderConflict( emptyCell );
					IStyle areaStyle = emptyCell.getStyle( );
					/*areaStyle.setProperty( IStyle.STYLE_BORDER_TOP_WIDTH,
							IStyle.NUMBER_0 );*/
					areaStyle.setProperty( IStyle.STYLE_PADDING_TOP, IStyle.NUMBER_0 );
					areaStyle.setProperty( IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0 );
					emptyCell.setWidth( getCellWidth( startColumn, endColumn ) );
					emptyCell.setPosition( layoutInfo.getXPosition( i ), 0 );
					row.addArea( emptyCell );
				}
				
			}
		}
		
		//update row height
		if(height>0)
		{
			HashSet updated = new HashSet();
			for(int i=0; i<columnNumber; i++)
			{
				CellArea cell = row.getCell( start + i );
				if(cell==null || updated.contains(cell))
				{
					continue;
				}
				updated.add(cell);
				if(cell.getRowSpan()==1)
				{
					if(cell instanceof DummyCell)
					{
						cell.setHeight( cell.getHeight( ) - height );
					}
					else
					{
						cell.setHeight( height );
						verticalAlign( cell );
					}
					
				}
				else
				{
					hasDropCell = true;
					if(cell instanceof DummyCell)
					{
						cell.setHeight( cell.getHeight( ) - height );
					}
				}
			}
			
			//update the height of drop cells
			if(dropCells.size( )>0)
			{
				HashSet updatedList = new HashSet();
				Iterator iter = dropCells.iterator( );
				while(iter.hasNext( ))
				{
					DummyCell cell =(DummyCell) iter.next( );
					CellArea ref = cell.getCell( );
					if(cell.getHeight( )<0 && !updatedList.contains( ref ))
					{
						ref.setHeight( ref.getHeight( ) - cell.getHeight( ) );
						verticalAlign(ref);
						updatedList.add( ref );
					}
				}
			}
			row.getArea( ).setHeight( height );
		}
		rows.add( row );
	}
	
	protected CellArea getReference()
	{
		return null;
	}
	
	public int getCellWidth( int startColumn, int endColumn )
	{
		if ( layoutInfo != null )
		{
			return layoutInfo.getCellWidth( startColumn, endColumn );
		}
		return 0;
	}
	
	private static class UnresolvedRow
	{
		protected HashMap map = new HashMap();
		
		public void addUnresolvedCell(ICellContent cell, int rowSpan)
		{
			int start = cell.getColumn( );
			int end = start + cell.getColSpan( );
			for(int i=start; i<end; i++)
			{
				map.put( new Integer(start), new DropCellContent(cell, rowSpan) );
			}
		}
		
		public boolean isEmpty()
		{
			return map.isEmpty( );
		}
		
		public DropCellContent getDropCellContent(int colId)
		{
			return (DropCellContent)map.get( new Integer(colId) );
		}
		
	}

	private static class DropCellContent
	{
		public DropCellContent(ICellContent cell, int rowSpan)
		{
			this.cell = cell;
			this.rowSpan = rowSpan;
		}
		
		ICellContent cell;
		int rowSpan;
	}
	
	private static class Row
	{
		protected int start;
		protected int length;
		protected int end;
		protected RowArea row;
		protected CellArea[] cells;
		protected boolean finished = true;

		Row(RowArea row, int start, int length, boolean finished)
		{
			this(row, start, length);
			this.finished = finished;
		}
		
		
		Row(RowArea row, int start, int length)
		{
			this.row = row;
			this.start = start;
			this.end = start + length;
			this.length = length;
			cells = new CellArea[length];
			for(int i=0; i<length; i++)
			{
				Iterator iter = row.getChildren( );
				while(iter.hasNext( ))
				{
					CellArea cell = (CellArea)iter.next( );
					int colId = cell.getColumnID( );
					int colSpan = cell.getColSpan( );
					if((colId>=start) && (colId+colSpan<=end))
					{
						for(int j=0; j<colSpan; j++)
						{
							cells[colId - start + j] = cell;
						}
					}
				}
			}
		}
		
		public IContent getContent()
		{
			return row.getContent( );
		}
		
		public void remove(int colId)
		{
			//FIXME
			row.removeChild( getCell(colId));
		}
		
		public void remove(IArea area)
		{
			if(area!=null)
			{
				if(!(area instanceof DummyCell))
				{
					row.removeChild( area );
				}
				CellArea cell = (CellArea)area;
				int colId = cell.getColumnID( );
				int colSpan = cell.getColSpan( );
				if((colId>=start) && (colId+colSpan<=end))
				{
					for(int j=0; j<colSpan; j++)
					{
						cells[colId - start + j] = null;
					}
				}
			}
		}
		
		public CellArea getCell(int colId)
		{
			if(colId<start || colId>=end)
			{
				assert(false);
				return null;
			}
			return cells[colId-start];
		}
		
		public void addArea(IArea area)
		{
			if(!(area instanceof DummyCell))
			{
				row.addChild( area );
			}
			CellArea cell = (CellArea)area;
			int colId = cell.getColumnID( );
			int colSpan = cell.getColSpan( );
			
			if((colId>=start) && (colId+colSpan<=end))
			{
				for(int j=0; j<colSpan; j++)
				{
					cells[colId - start + j] = cell;
				}
			}
			
		}

		/**
		 * row content
		 */
		public RowArea getArea()
		{
			return row;
		}
		
		
	}
	
	

	
}






