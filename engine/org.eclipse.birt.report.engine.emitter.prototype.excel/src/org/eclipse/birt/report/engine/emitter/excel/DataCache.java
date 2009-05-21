
package org.eclipse.birt.report.engine.emitter.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.emitter.EmitterUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.emitter.EmitterUtil;

public class DataCache
{
	/**
	 * columns is an ArrayList. Its elements are each column.
	 * Each column is also an arrayList. Its elements are the rows in the column. 
	 */
	private List<ArrayList<Data>> columns = new ArrayList<ArrayList<Data>>( );
	private int width;
	protected static Logger logger = Logger.getLogger( EmitterUtil.class
			.getName( ) );
	
	/**
	 * All the bookmarks defined in this excel file.
	 */
	private List<BookmarkDef> bookmarks = new ArrayList<BookmarkDef>();
	private int maxRowIndex = 0;

	public DataCache( int width, int height )
	{
		columns.add( new ArrayList<Data>( ) );
		this.width = width;
	}

	public void insertColumns( int startColumn, int columnCount )
	{
		if ( columnCount == 0 )
		{
			return;
		}

		int startPosition = startColumn + 1;

		for ( int i = startPosition; i <= startColumn + columnCount; i++ )
		{
			if ( i < width )
			{
				columns.add( i, new ArrayList<Data>( ) );
			}
		}
	}

	public void addData( int col, Data data )
	{	
		
		int rowIndex = data.getRowIndex( );
		columns.get( col ).add( data );
		maxRowIndex = maxRowIndex > rowIndex ? maxRowIndex : rowIndex;
		BookmarkDef bookmark = data.getBookmark( );
		if ( bookmark == null )
		{
			return;
		}
		bookmark.setColumnNo( col + 1 );
		bookmark.setRowNo( rowIndex );
		bookmarks.add( bookmark );
	}

	public void clearCachedSheetData( )
	{
		for ( int i = 0; i < getColumnCount( ); i++ )
		{
			columns.set( i, new ArrayList<Data>( ) );
		}
		bookmarks.clear( );
		maxRowIndex = 1;
	}

	public int getMaxRow( )
	{
		return maxRowIndex;
	}
	
	protected boolean valid(int row, int col)
	{
		if ( col >= getColumnCount( ) )
		{
			return false;
		}
		return true;
	}

	public int getColumnCount( )
	{
		return columns.size( );
	}

	
	public List<BookmarkDef> getBookmarks( )
	{
		return bookmarks;
	}	

	/**
	 * @param column
	 * @return
	 */
	public int getMaxRowIndex( int column )
	{
		Data lastData = getColumnLastData( column );
		if ( lastData != null )
			return lastData.getRowIndex( );
		return 0;
	}

	/**
	 * @param index
	 * @return
	 */
	public Data getColumnLastData( int index )
	{
		ArrayList<Data> columnDatas = columns.get( index );
		if ( !columnDatas.isEmpty( ) )
			return columnDatas.get( columnDatas.size( ) - 1 );
		return null;
	}

	public DataCahceIterator getRowIterator( )
	{
		return new DataCahceIterator( );
	}

	private class DataCahceIterator implements Iterator<Data[]>
	{

		private int[] columnIndexes;
		private int rowIndex = 1;

		public DataCahceIterator( )
		{
			columnIndexes = new int[columns.size( )];
		}

		public boolean hasNext( )
		{
			return rowIndex <= maxRowIndex;
		}

		public Data[] next( )
		{
			if ( !hasNext( ) )
			{
				throw new NoSuchElementException( );
			}
			Data[] rowDatas = new Data[columnIndexes.length];
			for ( int i = 0; i < columnIndexes.length; i++ )
			{
				ArrayList<Data> columnData = columns.get( i );
				int cursor = columnIndexes[i];
				int size = columnData.size( );
				for ( int j = cursor; j < size; j++ )
				{
					Data data = columnData.get( j );
					int dataRowIndex = data.getRowIndex( );
					if ( dataRowIndex == rowIndex )
					{
						rowDatas[i] = data;
						columnIndexes[i] = j + 1;
						break;
					}
					else if ( dataRowIndex > rowIndex )
					{
						columnIndexes[i] = j;
						break;
					}
				}
			}
			rowIndex++;
			return rowDatas;
		}

		public void remove( )
		{
			throw new UnsupportedOperationException( );
		}
		
	}
}