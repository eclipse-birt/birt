
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
	private List<ArrayList<Data>> columns = new ArrayList<ArrayList<Data>>( );
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
		columns.add( new ArrayList<Data>( ) );
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
		
		ArrayList<Data>[] mcol = (ArrayList<Data>[])new ArrayList[m_size];
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
					columns.add( new ArrayList<Data>( ) );
					columnId2StartRowId.put( columns.size( ) - 1, rowCount );
				}
				else
				{
					columns.add( i, new ArrayList<Data>( ) );	
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

	public void addData( int col, Data data )
	{	
		
		if ( ( getStartRowId( col ) > height ) || ( col >= getColumnCount( ) ) )
		{
			emitter.outputSheet( );
			clearCachedSheetData( );
		}
		
		List<Data> column = columns.get( col );
		column.add( data );
		if ( data instanceof Data )
		{
			BookmarkDef bookmark = ( (Data) data ).getBookmark( );
			if ( null == bookmark )
			{
				return;
			}
			int rowNo = columnId2StartRowId.get( new Integer( col ) ).intValue( )
					+ getStartRowId( col );
			bookmark.setColumnNo( col + 1 );
			bookmark.setRowNo( rowNo );
			bookmarks.add( bookmark );
		}
	}

	private void clearCachedSheetData( )
	{
		for ( int i = 0; i < getColumnCount( ); i++ )
		{
			columns.set( i, new ArrayList<Data>( ) );
		}
		Set<Entry<Integer, Integer>> entrySets = columnId2StartRowId.entrySet( );
		for ( Map.Entry<Integer, Integer> entry : entrySets )
		{
			entry.setValue( 0 );
		}
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

	public Data[] getRowData( int rownum )
	{
		List<Data> data = new ArrayList<Data>( );

		for(int i = 0 ; i < columns.size( ); i++)
		{
			Data value = getData(i, rownum);
			
			if(value != null)
			{
				data.add( value );
			}	
		}	

		Data[] row = new Data[data.size( )];
		data.toArray( row );
		return row;
	}
	
	public Data getData(int col, int row)
	{		
		if(!valid(row, col))
		{
		
			return null;
		}
		else
		{
			int start = columnId2StartRowId.get( new Integer(col) ).intValue( );
			List<Data> data = columns.get( col );
			
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