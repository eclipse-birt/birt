/***********************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.LayoutUtil;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;
import org.eclipse.birt.report.engine.layout.pdf.BorderConflictResolver;
import org.eclipse.birt.report.engine.layout.pdf.PDFTableLM.TableLayoutInfo;
import org.eclipse.birt.report.engine.layout.pdf.cache.ClonedCellContent;
import org.eclipse.birt.report.engine.layout.pdf.cache.CursorableList;
import org.eclipse.birt.report.engine.layout.pdf.cache.DummyCell;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;
import org.w3c.dom.css.CSSValue;


public class TableAreaLayout
{
	
	protected CursorableList rows = new CursorableList();
	
	/**
	 * Border conflict resolver
	 */
	protected BorderConflictResolver bcr = new BorderConflictResolver( );

	protected TableLayoutInfo layoutInfo = null;
	
	protected ITableContent tableContent;
	
	protected ICellContent lastCellContent;
	
	protected int startCol;
	
	protected int endCol;
	
	protected boolean hasDropCell;
	
	public TableAreaLayout(ITableContent tableContent, TableLayoutInfo layoutInfo, int startCol, int endCol)
	{
		this.tableContent = tableContent;
		this.layoutInfo = layoutInfo;
		this.startCol = startCol;
		this.endCol = endCol;
	}
	
	public void initTableLayout(UnresolvedRowHint hint)
	{
	}
	
	
	protected int resolveBottomBorder(CellArea cell)
	{
		IStyle tableStyle = tableContent.getComputedStyle( );
		IContent cellContent = cell.getContent( );
		IStyle columnStyle = getColumnStyle( cell.getColumnID( ) );
		IStyle cellAreaStyle = cell.getStyle( );
		IStyle cellContentStyle = cellContent.getComputedStyle( );
		IStyle rowStyle = ((IContent)cellContent.getParent()).getComputedStyle( ) ;
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
	
	protected IStyle getLeftCellContentStyle(Row lastRow, int columnID)
	{
		if(lastCellContent!=null && lastCellContent.getColumn( ) + lastCellContent.getColSpan( )<= columnID)
		{
			if(lastRow!=null && columnID>0)
			{
				CellArea cell = lastRow.getCell( columnID-1 );
				if(cell!=null && cell.getRowSpan( )<0 && cell.getContent( )!=null)
				{
					return cell.getContent( ).getComputedStyle( );
				}
				
			}
			return lastCellContent.getComputedStyle( );
			
		}
		if(lastRow!=null && columnID>0)
		{
			CellArea cell = lastRow.getCell( columnID-1 );
			if(cell!=null && cell.getRowSpan( )>1 && cell.getContent( )!=null)
			{
				return cell.getContent( ).getComputedStyle( );
			}
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
		IStyle cellAreaStyle = cellArea.getStyle( );
		IStyle tableStyle = tableContent.getComputedStyle( );
		IStyle rowStyle = row.getComputedStyle( );
		IStyle columnStyle = getColumnStyle( columnID );
		IStyle preRowStyle = null;
		IStyle preColumnStyle = getColumnStyle( columnID - 1 );
		IStyle leftCellContentStyle = null;
		IStyle topCellStyle = null;

		Row lastRow = null;
		if(rows.size( )>0 )
		{
			lastRow = (Row)rows.getCurrent( );
		}
		
		leftCellContentStyle = getLeftCellContentStyle(lastRow, columnID);
		
		if(lastRow!=null)
		{
			preRowStyle = lastRow.getContent( ).getComputedStyle( );
			CellArea cell = lastRow.getCell( columnID );
			if(cell!=null && cell.getContent( )!=null)
			{
				topCellStyle = cell.getContent( ).getComputedStyle( );
			}
		}
		//FIXME
		if ( rows.size( ) == 0  && lastRow==null)
		{
			// resolve top border
			if(isFirst)
			{
				bcr.resolveTableTopBorder( tableStyle, rowStyle, columnStyle,
					cellContentStyle, cellAreaStyle );
			}
			else
			{
				bcr.resolveTableTopBorder( tableStyle, null, columnStyle,
						null, cellAreaStyle );
			}

			// resolve left border
			if ( columnID == startCol )
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

			if ( columnID + colSpan - 1 == endCol )
			{
				bcr.resolveTableRightBorder( tableStyle, rowStyle, columnStyle,
						cellContentStyle, cellAreaStyle );
			}

		}
		else
		{
			if(isFirst)
			{
				bcr.resolveCellTopBorder( preRowStyle, rowStyle, topCellStyle,
							cellContentStyle, cellAreaStyle );
			}
			else
			{
				bcr.resolveCellTopBorder( preRowStyle, null, topCellStyle,
						null, cellAreaStyle );
			}
			// resolve left border
			if ( columnID == startCol )
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
			if ( columnID + colSpan-1 == endCol )
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
	
	
	public int resolveAll()
	{
		
		/*
		 * 1. scan current row and calculate row height
		 * 2. update the height of drop cells
		 * 3. update the height of row
		 */
		
		if(rows.size( )==0 )
		{
			return 0;
		}

		Row row = (Row)rows.getCurrent( );
		int rowHeight = row.getArea( ).getHeight( );
		int height = rowHeight;
		boolean hasDropCell = false;
		//scan for Max height for drop cells
		for(int i=startCol; i<=endCol; i++)
		{
			CellArea cell = row.getCell( i );
			if(cell!=null)
			{
				if( cell.getRowSpan( )>1 )
				{
					if ( cell instanceof DummyCell )
					{
						height = Math.max( height, cell.getHeight() + rowHeight );
					}
					else
					{
						height = Math.max( height, cell.getHeight( ) );	
					}
					hasDropCell = true;
				}
			}
		}
		
		int delta = height - rowHeight;
		if(hasDropCell)
		{
			HashSet dropCells = new HashSet();
			
			if(delta>0)
			{
				row.getArea( ).setHeight( height );
			}
			for(int i=startCol; i<=endCol; i++)
			{
				CellArea cell = row.getCell( i );
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
					/*unfinishedRow.addUnresolvedCell( (ICellContent) cell
							.getContent( ), getLeftRowSpan(
							row.finished, rowSpan ) );*/
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
					/*if(row!=null && !row.finished)
					{
						unfinishedRow.addUnresolvedCell( (ICellContent) cell
								.getContent( ), 1 );
					}*/
				}
					
			}
			
		}		
		return delta;
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
		for(int i=startCol; i<=endCol; i++)
		{
			CellArea cell = row.getCell( i );
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
						
						cells.add( cell );
					}
				}
			}
		}
		//update cell height
		if(result>0)
		{
			if(cells.size( )>0)
			{
				Iterator iter = cells.iterator( );
				while (iter.hasNext( ))
				{
					CellArea cell = (CellArea)iter.next( );
					cell.setHeight( cell.getHeight( ) + result );
				}
				row.getArea( ).setHeight( row.getArea( ).getHeight( ) + result );
			}
		}
		return result;
	}
	
	public void addRow(RowArea rowArea)
	{
		/*
		 * 1. create row wrapper, and add it to rows
		 */
		Row lastRow = (Row)rows.getCurrent( );
		
		Row row = new Row(rowArea, startCol, endCol );

		int rowHeight = rowArea.getHeight( );
		
		HashSet dropCells = new HashSet();
		for(int i=startCol; i<=endCol; i++)
		{
			CellArea lastCell = null;
			if(lastRow!=null)
			{
				lastCell = lastRow.getCell( i );
			}
			CellArea cell = row.getCell( i );
			if(cell!=null && (cell.getRowSpan( )>1 ))
			{
				hasDropCell = true;
			}
			if(lastCell!=null &&(lastCell.getRowSpan( )>1 ))
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
						dummyCell.setHeight( refDummy.getHeight( ) - rowHeight );
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
						dummyCell.setHeight( lastCell.getHeight( ) - lastRow.getArea( ).getHeight( ) - rowHeight);
					}
					row.addArea( dummyCell );
					//update drop cell height and vertial alignment
					if(dummyCell.getRowSpan( )==1)
					{
						if(dummyCell.getHeight( )<0)
						{
							CellArea cArea = dummyCell.getCell( );
							if(!dropCells.contains( cArea ))
							{
								cArea.setHeight( cArea.getHeight( ) - dummyCell.getHeight( ) );
								verticalAlign( cArea );
								dropCells.add( cArea );
							}
						}
					}
					else
					{
						hasDropCell = true;
					}
					i = i + dummyCell.getColSpan( ) -1;
				}
			}
		}
		rows.add( row );
	}
	
	public void skipRow(RowArea area)
	{
		
	}
	
	
	
	public void updateRow(RowArea rowArea, int specifiedHeight)
	{
		Row lastRow = getPreviousRow();
		Row row = new Row(rowArea, startCol, endCol);
		int height = specifiedHeight;
		
		for(int i=startCol; i<=endCol; i++)
		{
			CellArea lastCell = null;
			if(lastRow!=null)
			{
				lastCell = lastRow.getCell( i );
			}
			CellArea cell = row.getCell( i );
			if ( lastCell != null
					&& ( lastCell.getRowSpan( ) > 1  )  )
			{
				
				if(lastCell.getRowSpan( )==2)
				{
					if(lastCell instanceof DummyCell)
					{
						height = Math.max( height, lastCell.getHeight( ) );
					}
					else
					{
						height = Math.max( height, lastCell.getHeight( ) - lastRow.getArea( ).getHeight( ) );
					}
			
				}
				i = i + lastCell.getColSpan( ) -1;
			}
			else
			{
				if(cell!=null)
				{
					if( cell.getRowSpan( )==1 )
					{
						height = Math.max( height, cell.getHeight( ) );
					}
				}
				if(cell==null)
				{
					ICellContent cellContent = null;
					
						//create a empty cell
					cellContent = tableContent.getReportContent( )
								.createCellContent( );
					cellContent.setColumn( i );
						cellContent.setColSpan( 1 );
					cellContent.setRowSpan( 1 );
					cellContent.setParent( rowArea.getContent( ) );
					int startColumn = cellContent.getColumn( );
					int endColumn = cellContent.getColSpan( ) + startColumn;
					CellArea emptyCell = AreaFactory
							.createCellArea( cellContent );

					resolveBorderConflict( emptyCell, false );
					IStyle areaStyle = emptyCell.getStyle( );
					/*areaStyle.setProperty( IStyle.STYLE_BORDER_TOP_WIDTH,
							IStyle.NUMBER_0 );*/
					areaStyle.setProperty( IStyle.STYLE_PADDING_TOP, IStyle.NUMBER_0 );
					areaStyle.setProperty( IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0 );
					emptyCell.setWidth( getCellWidth( startColumn, endColumn ) );
					emptyCell.setPosition( layoutInfo.getXPosition( i ), 0 );
					rowArea.addChild( emptyCell );
					i = i + emptyCell.getColSpan( ) -1;
				}
			}
		}

		
		//update row height
		if(height >= 0)
		{
			Iterator iter = rowArea.getChildren( );
			while(iter.hasNext( ))
			{
				CellArea cell = (CellArea)iter.next( );
				if(cell.getRowSpan( )==1)
				{
					cell.setHeight( height );
					verticalAlign( cell );
				}
			}
			rowArea.setHeight( height );
		}
		
		
	}

	
	protected int getRowSpan(IRowContent row, CellArea cell, RowArea rowArea)
	{
		int rowSpan = cell.getRowSpan();
		IContent rowContent = rowArea.getContent();
		InstanceID id = row.getInstanceID( );
		InstanceID contentId = rowContent.getInstanceID( );
		
		if(id!=null && contentId!=null)
		{
			if ( rowSpan > 1
					&& ( !id.toUniqueString( ).equals(
							contentId.toUniqueString( ) ) ) )
			{
				return rowSpan - 1;
			}
			return rowSpan;
		}
		else //FIX 203576
		{
			if(row!=rowContent && rowSpan > 1)
			{
				return rowSpan-1;
			}
			else
			{
				return rowSpan;
			}
		}
		
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
	
	
	
	public Row getLastRow()
	{
		Row row = (Row)rows.getCurrent( );
		return row;
	}
	protected Row getPreviousRow()
	{
		//FIXME use current cursor
		int size = rows.size( );
		for(int i=size-1; i>=0; i--)
		{
			Row row = (Row)rows.get( i );
			return row;
		}
		return null;
	}

	public static class Row
	{
		protected int start;
		protected int length;
		protected int end;
		protected RowArea row;
		protected CellArea[] cells;
	
		
		
		Row(RowArea row, int start, int end)
		{
			this.row = row;
			this.start = start;
			this.end = end;
			this.length = end - start + 1;
			cells = new CellArea[length];

			Iterator iter = row.getChildren( );
			while(iter.hasNext( ))
			{
				CellArea cell = (CellArea)iter.next( );
				int colId = cell.getColumnID( );
				int colSpan = cell.getColSpan( );
				if((colId>=start) && (colId+colSpan-1<=end))
				{
					int loopEnd = Math.min(colSpan, end-colId+1);
					for(int j=0; j<loopEnd; j++)
					{
						cells[colId - start + j] = cell;
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
				if((colId>=start) && (colId+colSpan-1<=end))
				{
					int loopEnd = Math.min(colSpan, end-colId+1);
					for(int j=0; j<loopEnd; j++)
					{
						cells[colId - start + j] = null;
					}
				}
			}
		}
		
		public CellArea getCell(int colId)
		{
			if(colId<start || colId>end)
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
			
			if((colId>=start) && (colId+colSpan-1<=end))
			{
				int loopEnd = Math.min(colSpan, end-colId+1);
				for(int j=0; j<loopEnd; j++)
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






