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
import java.util.logging.Level;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumnContent;
import org.eclipse.birt.report.engine.content.IReportItemContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.ITableEmitter;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * <code>HTMLTableEmitter</code> is a concrete subclass of
 * <code>HTMLBaseEmitter</code> that outputs a table to HTML file.
 * 
 * @version $Revision: 1.8 $ $Date: 2005/03/15 07:22:32 $
 */
public class HTMLTableEmitter extends HTMLBaseEmitter implements ITableEmitter
{

	/**
	 * <code>PersistData</code> is a concrete class that stores necessary data
	 * so that <code>HTMLTableEmitter</code> can fill the missing cells, get
	 * the colAlign attribute for a cell, etc.
	 * 
	 * @version $Revision: 1.8 $ $Date: 2005/03/15 07:22:32 $
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
		this( report, false );
	}

	public HTMLTableEmitter( HTMLReportEmitter report, boolean isEmbedded )
	{
		super( report, isEmbedded );
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
		logger.log( Level.FINE, "[HTMLTableEmitter] Start table" ); //$NON-NLS-1$
		int type;
		DimensionType x = tableObj.getX( );
		DimensionType y = tableObj.getY( );
		StringBuffer styleBuffer = new StringBuffer( );

		IStyle mergedStyle = tableObj.getMergedStyle( );
		addDefaultTableStyles( mergedStyle, styleBuffer );

		type = checkElementType( x, y, mergedStyle, styleBuffer );

		writer.openTag( HTMLTags.TAG_TABLE );

		// style string
		setStyleName( tableObj.getStyle( ) );
		if ( type == ELEMENT_INLINE )
		{
			styleBuffer.append( "display: inline;" ); //$NON-NLS-1$
		}

		handleShrink( ELEMENT_BLOCK, mergedStyle, tableObj.getHeight( ),
				tableObj.getWidth( ), styleBuffer );
		handleStyle( tableObj, styleBuffer );

		// bookmark
		setBookmark( null, tableObj.getBookmarkValue( ) );

		// table caption
		String caption = tableObj.getCaption( );
		if ( caption != null && caption.length( ) > 0 )
		{
			writer.openTag( HTMLTags.TAG_CAPTION );
			writer.text( caption );
			writer.closeTag( HTMLTags.TAG_CAPTION );
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

		writer.closeTag( HTMLTags.TAG_TABLE );
		statusStack.pop( );
		if ( statusStack.size( ) > 0 )
		{
			currentData = (PersistData) statusStack.peek( );
		}
		else
		{
			currentData = null;
		}

		logger.log( Level.FINE, "[HTMLTableEmitter] End table" ); //$NON-NLS-1$
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

		IStyle mergedStyle = columnObj.getMergedStyle( );

		currentData.saveColInfo( mergedStyle == null ? null : mergedStyle
				.getTextAlign( ), repeat );

		writer.openTag( HTMLTags.TAG_COL );

		setStyleName( columnObj.getStyle( ) );

		// width
		StringBuffer styleBuffer = new StringBuffer( );
		AttributeBuilder.buildSize( styleBuffer, HTMLTags.ATTR_WIDTH, columnObj
				.getWidth( ) );

		handleStyle( columnObj, styleBuffer );

		// span
		if ( repeat > 1 )
		{
			writer.attribute( HTMLTags.ATTR_SPAN, repeat );
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
		writer.openTag( HTMLTags.TAG_TR );

		setStyleName( rowObj.getStyle( ) );

		// bookmark
		setBookmark( null, rowObj.getBookmarkValue( ) );

		StringBuffer styleBuffer = new StringBuffer( );

		AttributeBuilder.buildSize( styleBuffer, HTMLTags.ATTR_HEIGHT, rowObj
				.getHeight( ) ); //$NON-NLS-1$
		handleStyle( rowObj, styleBuffer );
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
		writer.closeTag( HTMLTags.TAG_TR );
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

		//		int span;
		//		int columnID;

		logger.log( Level.FINE, "[HTMLTableEmitter] Start cell." ); //$NON-NLS-1$

		if ( cellObj != null )
		{
			assert currentData != null;

			// fill empty cell if needed
			currentData.fillCells( cellObj.getColumn( ), cellObj.getRowSpan( ),
					cellObj.getColSpan( ) );

			// output 'td' tag
			writer.openTag( HTMLTags.TAG_TD ); //$NON-NLS-1$

			// set the 'name' property
			setStyleName( cellObj.getStyle( ) );

			// colspan
			if ( ( cellObj.getColSpan( ) ) > 1 )
			{
				writer.attribute( HTMLTags.ATTR_COLSPAN, cellObj.getColSpan( ) );
			}

			// rowspan
			if ( ( cellObj.getRowSpan( ) ) > 1 )
			{
				writer.attribute( HTMLTags.ATTR_ROWSPAN, cellObj.getRowSpan( ) );
			}

			// 'col' align
			if ( cellObj.getMergedStyle( ) == null
					|| cellObj.getMergedStyle( ).getTextAlign( ) == null )
			{
				writer.attribute( HTMLTags.ATTR_ALIGN, currentData
						.getCurColAlign( ) );
			}

			StringBuffer styleBuffer = new StringBuffer( );
			handleStyle( cellObj, styleBuffer );
		}
		else
		{
			writer.openTag( HTMLTags.TAG_TD );
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
		logger.log( Level.FINE, "[HTMLTableEmitter] End cell." ); //$NON-NLS-1$

		writer.closeTag( HTMLTags.TAG_TD );
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
		writer.openTag( HTMLTags.TAG_THEAD );
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
		writer.closeTag( HTMLTags.TAG_THEAD );
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
		writer.openTag( HTMLTags.TAG_TFOOT );
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
		writer.closeTag( HTMLTags.TAG_TFOOT );
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
		writer.openTag( HTMLTags.TAG_TBODY );
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
		writer.closeTag( HTMLTags.TAG_TBODY );
	}

	protected void addDefaultTableStyles( IStyle style,
			StringBuffer styleBuffer )
	{
		if ( isEmbedded )
		{
			styleBuffer
					.append( "border-collapse: collapse; empty-cells: show;" ); //$NON-NLS-1$
		}
	}

}