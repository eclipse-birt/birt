/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html.buffer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.internal.content.wrap.TableContentWrapper;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;
import org.w3c.dom.css.CSSValue;

public class TableBreakBuffer implements IPageBuffer
{

	IPageBuffer currentBuffer = null;
	IPageBuffer[] buffers = null;
	HTMLLayoutContext context;
	int nestCount = 0;
	int currentTableIndex = -1;
	int[] pageBreakIndexs;
	int currentIndex = 0;

	public TableBreakBuffer( IPageBuffer parentBuffer, HTMLLayoutContext context )
	{
		if ( parentBuffer != null )
		{
			currentBuffer = parentBuffer;
		}
		else
		{
			currentBuffer = context.getBufferFactory( ).createBuffer( );
		}
		this.context = context;
	}

	public void startContainer( IContent content, boolean isFirst,
			IContentEmitter emitter, boolean visible )
	{
		switch ( content.getContentType( ) )
		{
			case IContent.TABLE_CONTENT :
				nestCount++;
				ITableContent table = (ITableContent) content;
				boolean hasPageBreak = hasPageBreak( table );
				if ( hasPageBreak )
				{
					if ( currentTableIndex < 0 )
					{
						IContent[] contentList = currentBuffer
								.getContentStack( );
						pageBreakIndexs = getPageBreakIndex( table );

						currentBuffer
								.startContainer( createTable( table,
										pageBreakIndexs, 0 ), isFirst, emitter,
										visible );
						currentTableIndex = nestCount;

						buffers = new IPageBuffer[pageBreakIndexs.length];
						buffers[0] = currentBuffer;
						String tableId = table.getInstanceID( )
								.toUniqueString( );
						currentBuffer.addTableColumnHint( new TableColumnHint(
								tableId, 0, pageBreakIndexs[0] + 1 ) );
						for ( int i = 1; i < pageBreakIndexs.length; i++ )
						{
							buffers[i] = new TableBreakBuffer( null, context );
							IContent[] list = new IContent[contentList.length + 1];
							list[0] = createTable( table, pageBreakIndexs, i );
							for ( int j = 0; j < contentList.length; j++ )
							{
								list[j + 1] = contentList[j];
							}

							buffers[i].openPage( list, emitter );
							buffers[i]
									.addTableColumnHint( new TableColumnHint(
											tableId,
											pageBreakIndexs[i - 1] + 1,
											pageBreakIndexs[i]
													- pageBreakIndexs[i - 1] ) );
						}
					}
					else
					{
						currentBuffer.startContainer( content, isFirst,
								emitter, visible );
					}
				}
				else
				{
					currentBuffer.startContainer( content, isFirst, emitter,
							visible );
				}
				break;
			case IContent.TABLE_GROUP_CONTENT :
			case IContent.TABLE_BAND_CONTENT :
			case IContent.ROW_CONTENT :
				if ( currentTableIndex == nestCount && currentTableIndex > 0 )
				{
					currentIndex = 0;
					currentBuffer = buffers[0];
					startContainerInPages( content, isFirst, emitter, visible );
				}
				else
				{
					currentBuffer.startContainer( content, isFirst, emitter,
							visible );
				}
				break;
			default :
				currentBuffer.startContainer( content, isFirst, emitter,
						visible );
				break;

		}

	}

	protected void startContainerInPages( IContent content, boolean isFirst,
			IContentEmitter emitter, boolean visible )
	{
		buffers[0].startContainer( content, isFirst, emitter, visible );
		for ( int i = 1; i < buffers.length; i++ )
		{
			buffers[i].startContainer( content, false, emitter, visible );
		}
	}

	public void startContent( IContent content, IContentEmitter emitter,
			boolean visible )
	{
		currentBuffer.startContent( content, emitter, visible );
	}

	public void endContainer( IContent content, boolean finished,
			IContentEmitter emitter, boolean visible )
	{
		switch ( content.getContentType( ) )
		{
			case IContent.TABLE_CONTENT :
				// FIXME wrap the table content
				IContent[] contentList = currentBuffer.getContentStack( );

				nestCount--;
				if ( currentTableIndex == nestCount + 1
						&& currentTableIndex > 0 )
				{
					assert ( buffers != null );
					for ( int i = 0; i < buffers.length - 1; i++ )
					{
						buffers[i].closePage( contentList, emitter );
					}
					buffers[buffers.length - 1].endContainer( content,
							finished, emitter, visible );
					context.getBufferFactory( ).refresh( );
					currentBuffer = buffers[buffers.length - 1];

					buffers = null;
				}
				else
				{
					currentBuffer.endContainer( content, finished, emitter,
							visible );
				}
				break;
			case IContent.TABLE_GROUP_CONTENT :
			case IContent.TABLE_BAND_CONTENT :
			case IContent.ROW_CONTENT :
				if ( currentTableIndex == nestCount )
				{
					endContainerInPages( content, finished, emitter, visible );
				}
				else
				{
					currentBuffer.endContainer( content, finished, emitter,
							visible );
				}
				break;
			case IContent.CELL_CONTENT :
				if ( currentTableIndex == nestCount && currentTableIndex > 0 )
				{
					int pageIndex = needPageBreak( (ICellContent) content );
					if ( pageIndex >= 0 )
					{
						currentBuffer.endContainer( content, false, emitter,
								visible );
						currentBuffer = buffers[pageIndex];
					}
					else
					{
						currentBuffer.endContainer( content, finished, emitter,
								visible );
					}
				}
				else
				{
					currentBuffer.endContainer( content, finished, emitter,
							visible );
				}
				break;
			case IContent.PAGE_CONTENT :
				currentBuffer
						.endContainer( content, finished, emitter, visible );
				context.getBufferFactory( ).refresh( );
				break;
			default :
				currentBuffer
						.endContainer( content, finished, emitter, visible );
				break;
		}

	}

	protected void endContainerInPages( IContent content, boolean finished,
			IContentEmitter emitter, boolean visible )
	{
		if ( currentTableIndex == nestCount && currentTableIndex > 0 )
		{
			for ( int i = 0; i < buffers.length - 1; i++ )
			{
				buffers[i].endContainer( content, false, emitter, visible );
			}
			buffers[buffers.length - 1].endContainer( content, finished,
					emitter, visible );
		}
		else
		{
			currentBuffer.endContainer( content, finished, emitter, visible );
		}
	}

	public void flush( )
	{
		for ( int i = 0; i < buffers.length; i++ )
		{
			buffers[i].flush( );
		}
	}

	public boolean isRepeated( )
	{
		if ( currentBuffer != null )
		{
			return currentBuffer.isRepeated( );
		}
		if ( buffers != null )
		{
			return buffers[0].isRepeated( );
		}
		return false;
	}

	public void setRepeated( boolean isRepeated )
	{
		if ( currentBuffer != null )
		{
			currentBuffer.setRepeated( isRepeated );
		}
		if ( buffers != null )
		{
			for ( int i = 0; i < buffers.length; i++ )
			{
				buffers[i].setRepeated( isRepeated );
			}
		}
	}

	protected boolean hasPageBreak( ITableContent table )
	{
		int count = table.getColumnCount( );
		for ( int i = 0; i < count; i++ )
		{
			IColumn column = table.getColumn( i );
			IStyle style = column.getStyle( );
			CSSValue pageBreak = style
					.getProperty( IStyle.STYLE_PAGE_BREAK_BEFORE );
			if ( i > 0 && IStyle.ALWAYS_VALUE == pageBreak )
			{
				return true;
			}
			pageBreak = style.getProperty( IStyle.STYLE_PAGE_BREAK_AFTER );
			if ( i < count - 1 && IStyle.ALWAYS_VALUE == pageBreak )
			{
				return true;
			}
		}
		return false;
	}

	protected int[] getPageBreakIndex( ITableContent table )
	{
		List<Integer> indexs = new ArrayList<Integer>( );
		int count = table.getColumnCount( );
		for ( int i = 0; i < count; i++ )
		{
			IColumn column = table.getColumn( i );
			IStyle style = column.getStyle( );
			CSSValue pageBreak = style
					.getProperty( IStyle.STYLE_PAGE_BREAK_BEFORE );
			if ( i > 0 && IStyle.ALWAYS_VALUE == pageBreak )
			{
				if ( !indexs.contains( i - 1 ) )
				{
					indexs.add( i - 1 );
				}
			}
			pageBreak = style.getProperty( IStyle.STYLE_PAGE_BREAK_AFTER );
			if ( i < count - 1 && IStyle.ALWAYS_VALUE == pageBreak )
			{
				if ( !indexs.contains( i ) )
				{
					indexs.add( i );
				}
			}
		}
		if ( !indexs.contains( count - 1 ) )
		{
			indexs.add( count - 1 );
		}
		int[] values = new int[indexs.size( )];
		for ( int i = 0; i < indexs.size( ); i++ )
		{
			values[i] = indexs.get( i ).intValue( );
		}
		return values;
	}

	public int needPageBreak( ICellContent cell )
	{

		int end = cell.getColumn( ) + cell.getColSpan( );
		if ( end > pageBreakIndexs[currentIndex] )
		{
			while ( pageBreakIndexs[currentIndex] < end )
			{
				currentIndex++;
				if ( currentIndex == pageBreakIndexs.length )
				{
					currentIndex = 0;
					break;
				}
			}
			return currentIndex;
		}
		return -1;

	}

	public boolean finished( )
	{
		return currentBuffer.finished( );
	}

	public void closePage( IContent[] contentList, IContentEmitter emitter )
	{
		currentBuffer.closePage( contentList, emitter );
	}

	public void openPage( IContent[] contentList, IContentEmitter emitter )
	{
		currentBuffer.openPage( contentList, emitter );
	}

	public IContent[] getContentStack( )
	{
		return currentBuffer.getContentStack( );
	}

	protected ITableContent createTable( ITableContent table,
			int[] pageBreakIndex, int index )
	{
		List columns = new ArrayList( );
		int start = 0;
		int end = pageBreakIndex[index];
		if ( index == 0 )
		{
			start = 0;
		}
		else
		{
			start = pageBreakIndex[index - 1];
		}

		for ( int i = start; i < end; i++ )
		{
			IColumn column = table.getColumn( i );
			columns.add( column );
		}
		return new TableContentWrapper( table, columns );
	}

	public void addTableColumnHint( TableColumnHint hint )
	{
		if ( currentBuffer != null )
		{
			currentBuffer.addTableColumnHint( hint );
		}

	}

}
