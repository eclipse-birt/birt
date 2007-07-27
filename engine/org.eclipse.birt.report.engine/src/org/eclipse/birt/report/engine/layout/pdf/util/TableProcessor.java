/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.executor.buffermgr.Cell;
import org.eclipse.birt.report.engine.executor.buffermgr.Row;
import org.eclipse.birt.report.engine.executor.buffermgr.Table;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class TableProcessor
{
	private static final String ATTRIBUTE_COLSPAN = "colspan";
	
	private static final String ATTRIBUTE_ROWSPAN = "rowspan";

	//FIXME code review: extract two method so that the logic will be more clear.
	public static void processTable( Element ele, Map cssStyles,
			IContent content, IContent inlineParent, ActionContent action )
	{
		// FIXME code review: this block is used to parse table content. extract
		// a method parseTable().
		TableState tableState = new TableState( ele, cssStyles,
				content, inlineParent, action );
		tableState.processNodes( );
		
		// FIXME code review: this block is used to layout the table. extract to
		// method layoutTable();
		Table table = new Table( tableState.getRowCount( ), tableState
				.getColumnCount( ) );
		TableContent tableContent = (TableContent)tableState.getContent( );
		Iterator rows = tableContent.getChildren( ).iterator( );
		while( rows.hasNext( ) )
		{
			RowContent row = (RowContent)rows.next( );
			table.createRow( row );
			Iterator cells = row.getChildren( ).iterator( );
			while( cells.hasNext( ) )
			{
				CellContent cell = (CellContent)cells.next( );
				int rowSpan = cell.getRowSpan( );
				int colSpan = cell.getColSpan( );
				// Notice that the cell id is -1, that means the cell id will be
				// dynamic adjusted by <code>Table</code>.
				table.createCell( -1, rowSpan, colSpan,
						new InternalCellContent( cell ) );
			}
		}
		normalize( table, tableContent, tableState );
	}

	protected static void normalize( Table table, TableContent tableContent,
			TableState tableState )
	{
		ReportContent report = (ReportContent)tableContent.getReportContent( );
		for ( int i = 0; i < table.getRowCount( ); i++ )
		{
			Row row = table.getRow( i );
			RowContent rowContent = (RowContent) row.getContent( );
			Collection children = rowContent.getChildren( );
			children.clear( );
			for ( int j = 0; j < table.getColCount( ); j++ )
			{
				Cell cell = row.getCell( j );
				CellContent cellContent = null;
				int status = cell.getStatus( );
				if ( status == Cell.CELL_EMPTY || status == Cell.CELL_SPANED )
				{
					cellContent = new CellContent( report );
					cellContent.setRowSpan( 1 );
					cellContent.setColSpan( 1 );
					cellContent.setColumn( j );
				}
				else if ( status == Cell.CELL_USED )
				{
					cellContent = ( (InternalCellContent) cell.getContent( ) ).cell;
					cellContent.setColSpan( cell.getColSpan( ) );
					cellContent.setRowSpan( cell.getRowSpan( ) );
					cellContent.setColumn( j );
				}
				children.add( cellContent );
				cellContent.setParent( rowContent );
			}
		}
		for ( int i = 0; i < table.getColCount( ); i++ )
		{
			Column column = new Column( report );
			column.setWidth( tableState.getColumnWidth( i ) );
			tableContent.addColumn( column );
		}
	}
	
	private static class State
	{
		protected Element element;
		protected Map cssStyles;
		protected IContent parent, inlineParent, content;
		protected ActionContent action;

		public State( Element element, Map cssStyles,
				IContent parent, IContent inlineParent, ActionContent action )
		{
			this.element = element;
			this.cssStyles = cssStyles;
			this.parent = parent;
			this.inlineParent = inlineParent;
			this.action = action;
		}

		public IContent getContent( )
		{
			return content;
		}

	}

	public static class TableState extends State
	{
		private int columnCount;
		private int rowCount;
		private List columnWidth;

		public TableState( Element element, Map cssStyles,
				IContent parent, IContent inlineParent, ActionContent action )
		{
			super( element, cssStyles, parent, inlineParent, action );
			content = new TableContent( (ReportContent) parent
					.getReportContent( ) );
			content.setWidth( PropertyUtil.getDimensionAttribute( element, "width" ) );
			HTML2Content.handleStyle( element, cssStyles, content );
			columnWidth = new ArrayList( );
		}
		
		public void processNodes( )
		{
			Element ele = element;
			processNodes( ele );
			parent.getChildren( ).add( content );
			content.setParent( parent );
		}

		private void processNodes( Element ele )
		{
			for ( Node node = ele.getFirstChild( ); node != null; node = node
					.getNextSibling( ) )
			{
				assert ( node.getNodeType( ) == Node.ELEMENT_NODE );
				Element element = (Element) node;
				String tagName = element.getTagName( );
				if ( "tr".equals( tagName ) )
				{
					RowState rowState = new RowState( element, cssStyles, content, inlineParent, action );
					rowState.processNodes( );
					columnCount = Math.max( columnCount, rowState
							.getColumnCount( ) );
					++rowCount;
				}
				else if ( "col".equals( tagName ) )
				{
					columnWidth.add( PropertyUtil.getDimensionAttribute( element, "width" ) );
				}
				else if ( "tbody".equals( tagName ) || "thead".equals( tagName )
						|| "tfoot".equals( tagName ) )
				{
					processNodes( element );
				}
			}
		}
		 
		public int getColumnCount( )
		{
			return columnCount;
		}
		
		public int getRowCount( )
		{
			return rowCount;
		}
		
		public DimensionType getColumnWidth( int column )
		{
			if ( column >= columnWidth.size( ) )
			{
				return null;
			}
			return (DimensionType)columnWidth.get( column );
		}
	}

	private static class RowState extends State
	{
		private int columnCount;

		public RowState( Element element, Map cssStyles,
				IContent parent, IContent inlineParent, ActionContent action )
		{
			super( element, cssStyles, parent, inlineParent, action );
			content = new RowContent( (ReportContent) parent.getReportContent( ) );
			HTML2Content.handleStyle( element, cssStyles, content );
			content.setHeight( PropertyUtil.getDimensionAttribute( element, "height" ) );
		}
		
		public void processNodes( )
		{
			for ( Node node = element.getFirstChild( ); node != null; node = node
					.getNextSibling( ) )
			{
				assert ( node.getNodeType( ) == Node.ELEMENT_NODE );
				Element element = (Element) node;
				String tagName = element.getTagName( );
				assert ( "td".equals( tagName ) );
				CellState cellState = new CellState( element, cssStyles, content, inlineParent, action );
				cellState.processNodes( );
				columnCount += cellState.getColSpan( );
			}
			parent.getChildren( ).add( content );
			content.setParent( parent );
		}
		
		public int getColumnCount( )
		{
			return columnCount;
		}
	}

	private static class CellState extends State
	{
		private CellContent cell;
		
		public CellState( Element element, Map cssStyles,
				IContent parent, IContent inlineParent, ActionContent action )
		{
			super( element, cssStyles, parent, inlineParent, action );
			cell = new CellContent( (ReportContent) parent.getReportContent( ) );
			content = cell;
			HTML2Content.handleStyle( element, cssStyles, content );
			cell.setRowSpan( PropertyUtil.getIntAttribute( element,
					ATTRIBUTE_ROWSPAN ) );
			cell.setColSpan( PropertyUtil.getIntAttribute( element,
					ATTRIBUTE_COLSPAN ) );
		}

		public void processNodes( )
		{
			HTML2Content.processNodes( element, cssStyles, content,
					inlineParent, action );
			parent.getChildren( ).add( content );
			content.setParent( parent );
		}
		
		public int getColSpan( )
		{
			return cell.getColSpan( );
		}
	}

	private static class InternalCellContent implements Cell.Content
	{

		CellContent cell;

		InternalCellContent( CellContent cell )
		{
			this.cell = cell;
		}

		public boolean isEmpty( )
		{
			return cell != null;
		}

		public void reset( )
		{
		}
	}
}
