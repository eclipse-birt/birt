
package org.eclipse.birt.report.engine.internal.executor.doc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Segment
{

	class SegmentEdge
	{

		SegmentEdge( Object offset, boolean leftEdge )
		{
			this.offset = offset;
			this.leftEdge = leftEdge;
		}
		Object offset;
		boolean leftEdge;
	}

	LinkedList edges = new LinkedList( );
	Object[][] sections;
	Comparator comparator;

	Segment( Comparator comparator )
	{
		if ( !( comparator instanceof FragmentComparator ) )
		{
			comparator = new FragmentComparator( comparator );
		}
		this.comparator = comparator;
	}

	void startSegment( Object left )
	{
		addEdge( left, true );
	}

	void endSegment( Object right )
	{
		addEdge( right, false );
	}

	boolean inSegment( Object offset )
	{
		if ( sections == null )
		{
			normalize( );
		}
		for ( int i = 0; i < sections.length; i++ )
		{
			Object[] sect = sections[i];
			if ( comparator.compare( sect[0], offset ) <= 0 )
			{
				if ( comparator.compare( sect[1], offset ) >= 0 )
				{
					return true;
				}
			}
		}
		return false;
	}

	private void addEdge( Object offset, boolean left )
	{
		// drop the normalize result
		sections = null;
		// try to find the first segment will less that left
		SegmentEdge edge = null;
		ListIterator iter = edges.listIterator( edges.size( ) );
		while ( iter.hasPrevious( ) )
		{
			SegmentEdge next = (SegmentEdge) iter.previous( );
			if ( comparator.compare( next.offset, offset ) <= 0 )
			{
				// insert it after the next
				edge = new SegmentEdge( offset, left );
				iter.next( );
				iter.add( edge );
				return;
			}
		}
		if ( edge == null )
		{
			// insert it at the end of the list
			edge = new SegmentEdge( offset, left );
			edges.addFirst( edge );
		}
	}

	static final Object UNCLOSE_EDGE = new String( "UNCLOSE" );
	public static final Object LEFT_MOST_EDGE = new String( "LEFT" );
	public static final Object RIGHT_MOST_EDGE = new String( "RIGHT" );
	private final static int STATUS_INIT = 0;
	private final static int STATUS_START = 1;
	private final static int STATUS_CLOSE = 2;

	/**
	 * <table> <col/> <col width="30%"/> <col width="30%"/> <col width="30%"/>
	 * <tr>
	 * <th> STATUS </th>
	 * <th> LEFT EDGE</th>
	 * <th>RIGHT EDGE</th>
	 * <th>TERMINATE</th>
	 * </tr>
	 * <tr>
	 * <th>INIT</th>
	 * <td> create a section[left, UNBOUND], change to START. </td>
	 * <td> create a section[-1, right], change status to CLOSE</td>
	 * <td> set the section to NONE </td>
	 * </tr>
	 * <tr>
	 * <th>START</th>
	 * <td> skip </td>
	 * <td> set the section to [left, right], change to END status </td>
	 * <td> set the section to [left, MAX]</td>
	 * </tr>
	 * <tr>
	 * <th>CLOSE</th>
	 * <td> if the left is equals to the right, set the right to UNBOUND, change
	 * to START. Otherwise, save the section, create a new section [left,
	 * unbound], change to START</td>
	 * <td>update the section as [left, right]</td>
	 * <td>save the section</td>
	 * </tr>
	 * </table>
	 * 
	 * 
	 */
	public void normalize( )
	{
		// insert the first open in that segment if there is no open.
		if ( !edges.isEmpty( ) )
		{
			SegmentEdge start = (SegmentEdge) edges.getFirst( );
			if ( !start.leftEdge )
			{
				// assume it start from left most edge
				start = new SegmentEdge( LEFT_MOST_EDGE, true );
				edges.addFirst( start );
			}
			SegmentEdge end = (SegmentEdge) edges.getLast( );
			if ( end.leftEdge )
			{
				// assume we need end to right most edge
				end = new SegmentEdge( RIGHT_MOST_EDGE, false );
				edges.addLast( end );
			}
		}

		ArrayList sects = new ArrayList( );
		Object leftEdge = UNCLOSE_EDGE;
		Object rightEdge = UNCLOSE_EDGE;

		// insert the last close edge in that segment if there is no close.
		int status = STATUS_INIT;
		for ( int i = 0; i < edges.size( ); i++ )
		{
			SegmentEdge edge = (SegmentEdge) edges.get( i );
			switch ( status )
			{
				case STATUS_INIT :
					if ( edge.leftEdge )
					{
						leftEdge = edge.offset;
						rightEdge = UNCLOSE_EDGE;
						status = STATUS_START;
					}
					else
					{
						leftEdge = LEFT_MOST_EDGE;
						rightEdge = edge.offset;
						status = STATUS_CLOSE;
					}
					break;
				case STATUS_START :
					if ( !edge.leftEdge )
					{
						rightEdge = edge.offset;
						status = STATUS_CLOSE;
					}
					break;
				case STATUS_CLOSE :
					if ( edge.leftEdge )
					{
						if ( comparator.compare( edge.offset, rightEdge ) == 0 )
						{
							rightEdge = UNCLOSE_EDGE;
						}
						else
						{
							if ( !isSectEmpty( leftEdge, rightEdge ) )
							{
								sects.add( new Object[]{leftEdge, rightEdge} );
							}
							leftEdge = edge.offset;
							rightEdge = UNCLOSE_EDGE;
						}
						status = STATUS_START;
					}
					else
					{
						rightEdge = edge.offset;
					}
					break;
			}
		}
		// TERMINATE
		switch ( status )
		{
			case STATUS_START :
				if ( !isSectEmpty( leftEdge, RIGHT_MOST_EDGE ) )
				{
					sects.add( new Object[]{leftEdge, RIGHT_MOST_EDGE} );
				}
				break;
			case STATUS_CLOSE :
				if ( !isSectEmpty( leftEdge, rightEdge ) )
				{
					sects.add( new Object[]{leftEdge, rightEdge} );
				}
				break;
		}

		sections = new Object[sects.size( )][];
		for ( int i = 0; i < sects.size( ); i++ )
		{
			Object[] sect = (Object[]) sects.get( i );;
			sections[i] = sect;
		}
	}

	private boolean isSectEmpty( Object leftEdge, Object rightEdge )
	{
		if ( rightEdge == LEFT_MOST_EDGE || leftEdge == RIGHT_MOST_EDGE )
		{
			return true;
		}
		return false;
	}

	public String toString( )
	{
		StringBuffer buffer = new StringBuffer( );

		if ( sections == null )
		{
			normalize( );
		}
		if ( sections.length == 0 )
		{
			return "[NONE]";
		}
		for ( int i = 0; i < sections.length; i++ )
		{
			Object[] seg = sections[i];
			buffer.append( "[" );
			if ( seg[0] == LEFT_MOST_EDGE && seg[1] == RIGHT_MOST_EDGE )
			{
				buffer.append( "ALL" );
			}
			else if ( seg[0] == LEFT_MOST_EDGE && seg[1] == LEFT_MOST_EDGE )
			{
				buffer.append( "NONE" );
			}
			else
			{

				if ( seg[0] != LEFT_MOST_EDGE )
				{
					buffer.append( seg[0] );
				}
				buffer.append( "-" );
				if ( seg[1] != RIGHT_MOST_EDGE )
				{
					buffer.append( seg[1] );
				}
			}
			buffer.append( "]" );
		}
		return buffer.toString( );
	}
}
