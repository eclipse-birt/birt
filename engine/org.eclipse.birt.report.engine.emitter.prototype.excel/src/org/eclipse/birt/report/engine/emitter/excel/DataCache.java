/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.emitter.EmitterUtil;

public class DataCache
{
	/**
	 * columns is an ArrayList. Its elements are each column.
	 * Each column is also an arrayList. Its elements are the rows in the column. 
	 */
	private List<ArrayList<SheetData>> columns = new ArrayList<ArrayList<SheetData>>( );
	//FIXME: code review: remove the colrow
	private Map<Integer, Integer> columnId2StartRowId = new HashMap<Integer, Integer>( );// col -> start line
	private int width;
	private int height;
	protected static Logger logger = Logger.getLogger( EmitterUtil.class
			.getName( ) );
	private ExcelEmitter emitter;
	
	/**
	 * All the bookmarks defined in this excel file.
	 */
	private List<BookmarkDef> bookmarks = new ArrayList<BookmarkDef>();
	
	public DataCache( int width, int height, ExcelEmitter emitter )
	{
		columns.add( new ArrayList<SheetData>( ) );
		columnId2StartRowId.put( 0, 0 );
		this.width = width;
		this.height = height;
		this.emitter = emitter;
	}

	public void insertColumns( int col, int size )
	{		
		if ( size == 0 )
		{
			return;
		}

		//Get Current Width
		int columnCount = getColumnCount();
		//Get Current Position
		
		// Make sure the map is correct after moving
		int m_start = col + 1;
		int m_size = columnCount - m_start;
		m_size = Math.max( 0, m_size );
		
		ArrayList<SheetData>[] mcol = (ArrayList<SheetData>[]) new ArrayList[m_size];
		Map<Integer, Integer> temp = new HashMap<Integer, Integer>( );
		
		for ( int i = m_start, j = 0; j < m_size; i++, j++ )
		{
			Integer column = new Integer( i );
			Integer row = columnId2StartRowId.get( column );
			
			int npos = i + size;
			
			//Discard columns of over the max width.
			if(npos < width)
			{			
				temp.put( new Integer( npos ), row );
				mcol[j] = columns.get( m_start );
			}
			
			columns.remove( m_start );
		}
		
		columnId2StartRowId.putAll( temp );

		int rowCount = getStartRowId( col );		
		for ( int i = m_start; i <= col + size; i++ )
		{
			if( i < width )
			{	
				if (i > columns.size( ))
				{
					columns.add( new ArrayList<SheetData>( ) );
					columnId2StartRowId.put( columns.size( ) - 1, rowCount );
				}
				else
				{
					columns.add( i, new ArrayList<SheetData>( ) );
					columnId2StartRowId.put( i, rowCount );
				}
			}	
		}
		
		for(int i = 0; i < mcol.length; i++)
		{
			if(mcol[i] == null)
			{
				continue;
			}			
			
			columns.add( mcol[i] );				
		}	
	}

	public void addData( int col, SheetData data )
	{	
		
		if ( ( getStartRowId( col ) > height ) || ( col >= getColumnCount( ) ) )
		{
			emitter.outputSheet( );
			clearCachedSheetData( );
		}
		
		List<SheetData> column = columns.get( col );
		
		// Container info is used to check if some data is in a special row.
		// This info only useful for last data item in the column.
		int size = column.size();
		if ( size > 0 )
		{
			column.get( size - 1 ).clearContainer( );
		}
		column.add( data );
		BookmarkDef bookmark = data.getBookmark( );
		if ( bookmark == null )
		{
			return;
		}
		int rowNo = columnId2StartRowId.get( new Integer( col ) ).intValue( )
				+ getStartRowId( col );
		bookmark.setColumnNo( col + 1 );
		bookmark.setRowNo( rowNo );
		bookmarks.add( bookmark );
	}

	private void clearCachedSheetData( )
	{
		for ( int i = 0; i < getColumnCount( ); i++ )
		{
			columns.set( i, new ArrayList<SheetData>( ) );
		}
		Set<Entry<Integer, Integer>> entrySets = columnId2StartRowId.entrySet( );
		for ( Map.Entry<Integer, Integer> entry : entrySets )
		{
			entry.setValue( 0 );
		}
		bookmarks.clear( );
	}

	public int getStartRowId( int column )
	{
		if ( column < getColumnCount( ) )
		{
			return columnId2StartRowId.get( column )
					+ columns.get( column ).size( );
		}
		else
		{
			return -1;
		}
	}

	public int getMaxRow( )
	{
		int max = 0;

		for ( int i = 0; i < columns.size( ); i++ )
		{
			int size = getStartRowId( i );
			max = max >= size ? max : size;
		}

		return max;
	}

	public SheetData[] getRowData( int rownum )
	{
		List<SheetData> data = new ArrayList<SheetData>( );

		for(int i = 0 ; i < columns.size( ); i++)
		{
			SheetData value = getData(i, rownum);
			
			if(value != null)
			{
				data.add( value );
			}	
		}	

		SheetData[] row = new SheetData[data.size( )];
		data.toArray( row );
		return row;
	}
	
	public SheetData getData(int col, int row)
	{		
		if(!valid(row, col))
		{
		
			return null;
		}
		else
		{
			int start = columnId2StartRowId.get( new Integer(col) ).intValue( );
			List<SheetData> data = columns.get( col );
			
			if(data.size( ) > (row - start))
			{	
				return data.get(row - start);
			}	
			else
			{
				return null;
			}	
		}	
	}	
	
	protected boolean valid(int row, int col)
	{
		if(col >= getColumnCount() || row > getStartRowId(col)) 
		{
			return false;
		}
		
		int start = columnId2StartRowId.get( new Integer(col) ).intValue( );
		return (row >= start && 
				row < getStartRowId(col) && 
				col < getColumnCount());		
	}

	public int getColumnCount( )
	{
		return columns.size( );
	}

	
	public List<BookmarkDef> getBookmarks( )
	{
		return bookmarks;
	}	
}