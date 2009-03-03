
package org.eclipse.birt.report.engine.layout.emitter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TableBorder
{
	/**
	 *  the upper left x
	 */
	int tableX = 0;
	int tableY = 0;
	
	/**
	 * the lower right x
	 */
	int tableLRX = 0;
	
	/**
	 * the lower right y
	 */
	int tableLRY = 0;
	

	class Border
	{
		Border( int position )
		{
			this.position = position;
		}

		int position;
		int width;

		ArrayList breakPoints = new ArrayList( );
		ArrayList segments = new ArrayList( );
	}

	class BorderSegment
	{
		BorderSegment( int start, int end, int style, int width, Color color )
		{
			this.start = start;
			this.end = end;
			this.style = style;
			this.width = width;
			this.color = color;
		}

		int start;
		int end;
		int style;
		int width;
		Color color;
	}

	HashMap columnBorders = new HashMap( );
	HashMap rowBorders = new HashMap( );

	TableBorder( int x, int y )
	{
		tableX = x;
		tableY = y;
		addColumn( x );
		addRow( y );
	}

	public void addColumn( int position )
	{
		if(!columnBorders.containsKey( new Integer(position) ))
		{
			columnBorders.put( new Integer(position), new Border( position ) );
		}
		tableLRX = Math.max( position, tableLRX );
	}

	public void addRow( int position )
	{
		if(!rowBorders.containsKey( new Integer(position) ))
		{
			rowBorders.put( new Integer(position), new Border( position ) );
		}
		tableLRY = Math.max( position, tableLRY );
	}

	public void setColumnBorder( int position, int start, int end, int style, int width, Color color )
	{
		addBorderSegment( (Border) columnBorders.get( new Integer(position) ), start, end, style,
				width, color );
	}

	public void setRowBorder( int position, int start, int end, int style, int width, Color color )
	{
		addBorderSegment( (Border) rowBorders.get( new Integer(position) ), start, end, style,
				width, color );
	}

	protected void addBorderSegment( Border border, int start, int end,
			int style, int width, Color color )
	{
		if ( style == org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo.BORDER_STYLE_NONE || color == null || width == 0 || border == null)
		{
			return;
		}
		ArrayList segments = border.segments;
		BorderSegment last = null;
		if ( !segments.isEmpty( ) )
		{
			last = (BorderSegment) segments.get( segments.size( ) - 1 );
			if ( last.width == width && last.color.equals( color ) && last.style== style  )
			{
				if ( last.end == start )
				{
					last.end = end;
					return;
				}
				// bidi_hcg: In RTL X coordinates are progressing from right to left
				if ( last.start == end )
				{
					last.start = start;
					return;
				}
			}
		}

		segments.add( new BorderSegment( start, end, style, width, color ) );

		if ( border.width < width )
		{
			border.width = width;
		}
	}

	public void findBreakPoints( )
	{
		for (Iterator i = rowBorders.keySet( ).iterator( ); i.hasNext(); )
		{
			findBreakPoints((Border)rowBorders.get( i.next( ) ));	
		}
		for (Iterator i = columnBorders.keySet( ).iterator( ); i.hasNext(); )
		{
			findBreakPoints((Border)columnBorders.get( i.next() ));	
		}
		
	}

	private void findBreakPoints( Border border )
	{
		int segCount = border.segments.size( );
		BorderSegment last = null;
		for ( int j = 0; j < segCount; j++ )
		{
			BorderSegment current = (BorderSegment) border.segments.get( j );
			if ( last == null )
			{
				border.breakPoints.add( new Integer( current.start ) );
			}
			else if ( current.start != last.end )
			{
				border.breakPoints.add( new Integer( last.end ) );
				border.breakPoints.add( new Integer( current.start ) );
			}
			last = current;
		}
		if ( null != last )
		{
			border.breakPoints.add( new Integer( last.end ) );
		}
	}

}
