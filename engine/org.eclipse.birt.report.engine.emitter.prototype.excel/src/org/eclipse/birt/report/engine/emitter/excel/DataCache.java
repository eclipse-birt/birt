
package org.eclipse.birt.report.engine.emitter.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DataCache
{
	/**
	 * columns is an ArrayList. Its elements are each column.
	 * Each column is also an arrayList. Its elements are the rows in the column. 
	 */
	private ArrayList columns = new ArrayList( );
	//FIXME: code review: remove the colrow
	private Hashtable colrow = new Hashtable( );// col -> start line
	private int height;
	private int width;
	/**
	 * All the bookmarks defined in this excel file.
	 */
	private ArrayList bookmarks = new ArrayList();
	
	public DataCache( int height, int width)
	{
		Integer start = new Integer( 0 );		
		columns.add( new ArrayList( ) );
		colrow.put( start, start );	
		this.height = height;
		this.width = width;
	}

	public void insertColumns( int col, int size )
	{		
		if ( size == 0 )
		{
			return;
		}

		//Get Current Width
		int c_width = getColumnCount();
		//Get Current Position
		Integer collen = new Integer( getColumnSize( col ) );		
		
		// Make sure the map is correct after moving
		int m_start = col + 1;
		int m_size = c_width - m_start;
		m_size = Math.max( 0, m_size );
		
		Object[] mcol = new Object[m_size];
		Map temp = new HashMap();
		
		for ( int i = m_start, j = 0; j < m_size; i++, j++ )
		{
			Integer column = new Integer( i );
			Object row = colrow.get( column );
			
			int npos = i + size;
			
			//Discard columns of over the max width.
			if(npos < width)
			{			
				temp.put( new Integer( npos ), row );
				mcol[j] = columns.get( m_start );
			}
			
			columns.remove( m_start );
		}
		
		colrow.putAll( temp );

		for ( int i = m_start; i <= col + size; i++ )
		{
			if( i < width )
			{	
				columns.add( i, new ArrayList( ) );
				colrow.put( new Integer( i ), collen );
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

	public void addData( int col, Object data )
	{	
		if((getColumnSize(col) < height) && (col < getColumnCount()))
		{	
			((List) columns.get( col ) ).add( data );
		}
		if (data instanceof Data)
		{			
			BookmarkDef bookmark = ((Data)data).getBookmark( );
			if ( null == bookmark )
			{
				return;
			}
			int rowNo = ( (Integer) colrow.get( new Integer( col ) ) )
					.intValue( )
					+ getColumnSize( col );
			bookmark.setColumnNo( col+1 );
			bookmark.setRowNo( rowNo );
			bookmarks.add( bookmark );
		}
	}

	public int getColumnSize( int column )
	{
		if ( column < getColumnCount( ) )
		{
			return ( (Integer) colrow.get( new Integer( column ) ) ).intValue( )
					+ ( (List) columns.get( column ) ).size( );
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
			int size = getColumnSize( i );
			max = max >= size ? max : size;
		}

		return max;
	}

	public Object[] getRowData( int rownum )
	{
		List data = new ArrayList( );

		for(int i = 0 ; i < columns.size( ); i++)
		{
			Object value = getData(i, rownum);
			
			if(value != null)
			{
				data.add( value );
			}	
		}	

		Object[] row = new Object[data.size( )];
		data.toArray( row );
		return row;
	}
	
	public Object getData(int col, int row)
	{		
		if(!valid(row, col))
		{
		
			return null;
		}
		else
		{
			int start = ((Integer)colrow.get( new Integer(col) )).intValue( );
			List data = (List) columns.get( col );
			
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
		if(col >= getColumnCount() || row > getColumnSize(col)) 
		{
			return false;
		}
		
		int start = ((Integer)colrow.get( new Integer(col) )).intValue( );
		return (row >= start && 
				row < getColumnSize(col) && 
				col < getColumnCount());		
	}

	public int getColumnCount( )
	{
		return columns.size( );
	}

	
	public ArrayList getBookmarks( )
	{
		return bookmarks;
	}	
}