
package org.eclipse.birt.report.engine.emitter.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DataCache
{
	private ArrayList columns = new ArrayList( );
	private Hashtable colrow = new Hashtable( );// col -> start line
	private int height;
	private int width;
	
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

		// Move the orignal data to new place to make sure the map is correct
		int width = columns.size( );	
		Object[] mcol = new Object[width - col - 1];
		Map temp = new HashMap();
		
		for ( int i = col + 1, j = 0; j < width - col - 1; i++, j++ )
		{
			Integer column = new Integer( i );
			Object row = colrow.get( column );
			
			int npos = i + size;
			//Discard columns of over the max width.
			if(npos < this.width)
			{			
				temp.put( new Integer( npos ), row );
				mcol[j] = columns.get( col + 1 );
			}
			columns.remove( col + 1 );
		}
		
		colrow.putAll( temp );

		// Map col - row
		Integer collen = new Integer( getColumnSize( col ) );

		for ( int i = col + 1; i <= col + size; i++ )
		{
			columns.add( i, new ArrayList( ) );
			colrow.put( new Integer( i ), collen );
		}
		
		for(int i = 0; i < mcol.length; i++)
		{
			columns.add( mcol[i] );
		}	
	}

	public void addData( int col, Object data )
	{
		if((getColumnSize(col) < height) && (col < getColumnCount()))
		{	
			((List) columns.get( col ) ).add( data );
		}
	}

	public int getColumnSize( int column )
	{
		return ( (Integer) colrow.get( new Integer( column ) ) ).intValue( )
				+ ( (List) columns.get( column ) ).size( );
	}

	public int getRowCount( )
	{
		int max = 0;

		for ( int i = 0; i < columns.size( ); i++ )
		{
			int size = getColumnSize( i );
			max = max >= size ? max : size;
		}

		return max;
	}

	public Object[] getRowData( int row )
	{
		List data = new ArrayList( );

		for(int i = 0 ; i < columns.size( ); i++)
		{
			Object value = getData(i, row);
			
			if(value != null)
			{
				data.add( value );
			}	
		}	

		return data.toArray( new Object[0] );
	}
	
	public Object getData(int col, int row)
	{
	
		int start = ((Integer)colrow.get( new Integer(col) )).intValue( );
		
		if(row < start)
		{
		
			return null;
		}
		else
		{
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

	public int getColumnCount( )
	{
		return columns.size( );
	}	
}