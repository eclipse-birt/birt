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

package org.eclipse.birt.report.engine.emitter.html;

import java.util.HashMap;
import java.util.Stack;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumnContent;
import org.eclipse.birt.report.engine.content.IReportItemContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.ITableEmitter;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.StyleDesign;

/**
 * <code>HTMLTableEmitter</code> is a concrete subclass of
 * <code>HTMLBaseEmitter</code> that outputs a table to HTML file.
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/23 06:50:04 $
 */
public class HTMLTableEmitter extends HTMLBaseEmitter implements ITableEmitter
{

	/**
	 * <code>PersistData</code> is a concrete class that stores necessary data
	 * so that <code>HTMLTableEmitter</code> can fill the missing cells, get
	 * the colAlign attribute for a cell, etc.
	 * 
	 * @version $Revision: 1.3 $ $Date: 2005/02/23 06:50:04 $
	 */
	private class PersistData
	{

		/**
		 * The constructor.
		 */
		public PersistData( )
		{
			columns = 0;
			lastCol = 1;
			colAlignMap = new HashMap( );
		}

		/**
		 * Saves the column alignment information and updates column number.
		 * This method is called by startColumn.
		 * 
		 * @param align
		 *            The column(s)' alignment.
		 * @param repeat
		 *            The column's repeat property.
		 */
		public void saveColInfo( String align, int repeat )
		{
			for ( int i = 1; i <= repeat; i++ )
			{
				colAlignMap.put( new Integer( columns + i ), align );
			}
			columns += repeat;
		}

		/**
		 * When we get the column numbers, we need to create the rowSpans array
		 * and initialize it.
		 */
		public void createCols( )
		{
			rowSpans = new int[columns];
			for ( int n = 0; n < columns; n++ )
			{
				rowSpans[n] = 0;
			}
		}

		/**
		 * Adjusts the row spans of each column when a row ends. If lastCol is
		 * not bigger than column number, we may also need to fill the empty
		 * cells before ending the row.
		 */
		public void adjustCols( )
		{
			for ( ; lastCol <= columns; lastCol++ )
			{
				if ( rowSpans[lastCol - 1] == 0 )
				{
					startCell( null );
					endCell( );
				}
			}

			lastCol = 1;
			for ( int n = 0; n < columns; n++ )
			{
				if ( rowSpans[n] > 0 )
				{
					rowSpans[n]--;
				}
			}
		}

		/**
		 * Fills the empty cells if needed. When a new cell's columnID is larger
		 * than lastCol and there are some empty cells (with rowspan=0) between
		 * them, we need to insert them before adding the new cell.
		 * 
		 * @param columnID
		 *            The column ID of the new cell.
		 * @param rowSpan
		 *            The row span of the new cell.
		 * @param colSpan
		 *            The column span of the new cell.
		 */
		public void fillCells( int columnID, int rowSpan, int colSpan )
		{
			if ( columnID > 0 )
			{
				for ( ; lastCol < columnID; lastCol++ )
				{
					if ( rowSpans[lastCol - 1] == 0 )
					{
						startCell( null );
						endCell( );
					}
				}
			}
			else
			{
				while ( rowSpans[lastCol - 1] > 0 )
				{
					lastCol++;
				}
			}

			curColumnID = lastCol;

			for ( int n = 0; n < colSpan; n++, lastCol++ )
			{
				rowSpans[lastCol - 1] = rowSpan;
			}
		}

		/**
		 * Get the column align for the current cell.
		 * 
		 * @return column align String.
		 */
		public String getCurColAlign( )
		{
			return (String) colAlignMap.get( new Integer( curColumnID ) );
		}

		/**
		 * Specifies the total column number.
		 */
		private int columns;

		/**
		 * The column ID of current cell.
		 */
		private int curColumnID;

		/**
		 * An integer array to store the row span of each column.
		 */
		private int rowSpans[];

		/**
		 * The Column ID of last cell.
		 */
		private int lastCol;

		/**
		 * The <code>colAlignMap</code> that stores alignment attribute of
		 * each column.
		 */
		private HashMap colAlignMap;
	};

	/**
	 * The <code>Stack</code> object that stores <code>PersistData</code>
	 * objects in case that tables are nested.
	 */
	private Stack statusStack = new Stack( );

	/**
	 * The current <code>PersistData</code> object.
	 */
	private PersistData currentData;

	/**
	 * The constructor.
	 * 
	 * @param report
	 *            The <code>HTMLReportEmitter</code> object that creates this
	 *            Emitter.
	 * @param htmlContext
	 *            The <code>HTMLContext</code> object.
	 */
	public HTMLTableEmitter( HTMLReportEmitter report )
	{
		super( report );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#startTable(org.eclipse.birt.report.engine.content.TableObject)
	 */
	public void start( IReportItemContent item )
	{
		ITableContent tableObj = (ITableContent) item;
		assert tableObj != null;

		reportEmitter.push( tableObj );
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}
		if ( log.isTraceEnabled( ) )
		{
			log.trace( "[HTMLTableEmitter] Start table" ); //$NON-NLS-1$
		}

		int type;
		DimensionType x = tableObj.getX( );
		DimensionType y = tableObj.getY( );
		StringBuffer styleBuffer = new StringBuffer( );
		StyleDesign mergedStyle = tableObj.getMergedStyle( );

		type = checkElementType( x, y, mergedStyle, styleBuffer );

		writer.openTag( "table" ); //$NON-NLS-1$

		// style string
		setStyleName( tableObj.getStyle( ) );
		if ( type == ELEMENT_INLINE )
		{
			styleBuffer.append( "display: inline;" ); //$NON-NLS-1$
		}
		styleBuffer.append( "border-collapse:collapse;empty-cells: show;" ); //$NON-NLS-1$
		handleShrink( ELEMENT_BLOCK, mergedStyle, tableObj.getHeight( ),
				tableObj.getWidth( ), styleBuffer );
		AttributeBuilder.buildStyle( styleBuffer,
				tableObj.getHighlightStyle( ), reportEmitter );
		writer.attribute( "style", styleBuffer.toString( ) ); //$NON-NLS-1$

		// bookmark
		setBookmark( null, tableObj.getBookmarkValue( ) );

		// table caption
		String caption = tableObj.getCaption( );
		if ( caption != null && caption.length( ) > 0 )
		{
			writer.openTag( "caption" ); //$NON-NLS-1$
			writer.text( caption );
			writer.closeTag( "caption" ); //$NON-NLS-1$
		}

		currentData = new PersistData( );
		statusStack.push( currentData );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#endTable()
	 */
	public void end( )
	{
		if ( reportEmitter.pop( ) )
		{
			return;
		}

		writer.closeTag( "table" ); //$NON-NLS-1$

		statusStack.pop( );
		if ( statusStack.size( ) > 0 )
		{
			currentData = (PersistData) statusStack.peek( );
		}
		else
		{
			currentData = null;
		}

		if ( log.isTraceEnabled( ) )
		{
			log.trace( "[HTMLTableEmitter] End table" ); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#startColumns()
	 */
	public void startColumns( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#endColumns()
	 */
	public void endColumns( )
	{
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}

		assert ( currentData != null );
		currentData.createCols( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#startColumn(org.eclipse.birt.report.engine.content.ColumnObject)
	 */
	public void startColumn( IColumnContent columnObj )
	{
		assert columnObj != null;

		if ( reportEmitter.isHidden( ) )
		{
			return;
		}

		int repeat;

		repeat = columnObj.getRepeat( );

		assert repeat > 0 && currentData != null;

		StyleDesign mergedStyle = columnObj.getMergedStyle( );

		currentData.saveColInfo( mergedStyle == null ? null : mergedStyle
				.getTextAlign( ), repeat );

		writer.openTag( "col" ); //$NON-NLS-1$

		setStyleName( columnObj.getStyle( ) );

		// width
		StringBuffer styleBuffer = new StringBuffer( );
		AttributeBuilder.buildSize( styleBuffer, "width", //$NON-NLS-1$
				columnObj.getWidth( ) );
		AttributeBuilder.buildStyle( styleBuffer,
				columnObj.getHighlightStyle( ), reportEmitter );
		writer.attribute( "style", styleBuffer.toString( ) ); //$NON-NLS-1$

		// span
		if ( repeat > 1 )
		{
			writer.attribute( "span", repeat ); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#endColumn()
	 */
	public void endColumn( )
	{
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}
		writer.closeNoEndTag( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#startRow(org.eclipse.birt.report.engine.content.RowObject)
	 */
	public void startRow( IRowContent rowObj )
	{
		assert rowObj != null;
		reportEmitter.push( rowObj );
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}
		writer.openTag( "tr" ); //$NON-NLS-1$

		setStyleName( rowObj.getStyle( ) );

		// bookmark
		setBookmark( null, rowObj.getBookmarkValue( ) );

		StringBuffer styleBuffer = new StringBuffer( );

		AttributeBuilder.buildSize( styleBuffer, "height", rowObj.getHeight( ) ); //$NON-NLS-1$
		AttributeBuilder.buildStyle( styleBuffer, rowObj.getHighlightStyle( ),
				reportEmitter );
		writer.attribute( "style", styleBuffer.toString( ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#endRow()
	 */
	public void endRow( )
	{
		if ( reportEmitter.pop( ) )
		{
			return;
		}
		assert currentData != null;

		currentData.adjustCols( );
		writer.closeTag( "tr" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#startCell(org.eclipse.birt.report.engine.content.CellObject)
	 */
	public void startCell( ICellContent cellObj )
	{
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}

		int span;
		int columnID;

		if ( log.isTraceEnabled( ) )
		{
			log.trace( "[HTMLTableEmitter] Start cell." ); //$NON-NLS-1$
		}

		if ( cellObj != null )
		{
			assert currentData != null;

			// fill empty cell if needed
			currentData.fillCells( cellObj.getColumn( ), cellObj.getRowSpan( ),
					cellObj.getColSpan( ) );

			// output 'td' tag
			writer.openTag( "td" ); //$NON-NLS-1$

			// set the 'name' property
			setStyleName( cellObj.getStyle( ) );

			// colspan
			if ( ( span = cellObj.getColSpan( ) ) > 1 )
			{
				writer.attribute( "colspan", cellObj.getColSpan( ) ); //$NON-NLS-1$
			}

			// rowspan
			if ( ( span = cellObj.getRowSpan( ) ) > 1 )
			{
				writer.attribute( "rowspan", cellObj.getRowSpan( ) ); //$NON-NLS-1$
			}

			// 'col' align
			if ( cellObj.getMergedStyle( ) == null
					|| cellObj.getMergedStyle( ).getTextAlign( ) == null )
			{
				writer.attribute( "align", currentData.getCurColAlign( ) ); //$NON-NLS-1$
			}

			StringBuffer styleBuffer = new StringBuffer( );
			AttributeBuilder.buildStyle( styleBuffer, cellObj
					.getHighlightStyle( ), reportEmitter );
			writer.attribute( "style", styleBuffer.toString( ) ); //$NON-NLS-1$

		}
		else
		{
			writer.openTag( "td" ); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#endCell()
	 */
	public void endCell( )
	{
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}
		if ( log.isTraceEnabled( ) )
		{
			log.trace( "[HTMLTableEmitter] End cell." ); //$NON-NLS-1$
		}

		writer.closeTag( "td" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#startHeader()
	 */
	public void startHeader( )
	{
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}
		writer.openTag( "thead" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#endHeader()
	 */
	public void endHeader( )
	{
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}
		writer.closeTag( "thead" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#startFooter()
	 */
	public void startFooter( )
	{
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}
		writer.openTag( "tfoot" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#endFooter()
	 */
	public void endFooter( )
	{
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}
		writer.closeTag( "tfoot" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#startBody()
	 */
	public void startBody( )
	{
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}
		writer.openTag( "tbody" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.ITableEmitter#endBody()
	 */
	public void endBody( )
	{
		if ( reportEmitter.isHidden( ) )
		{
			return;
		}
		writer.closeTag( "tbody" ); //$NON-NLS-1$
	}
}