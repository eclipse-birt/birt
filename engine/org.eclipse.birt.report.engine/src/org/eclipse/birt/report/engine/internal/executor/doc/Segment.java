
package org.eclipse.birt.report.engine.internal.executor.doc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

class Segment
{

	class SegmentEdge
	{

		SegmentEdge( long offset )
		{
			this.offset = offset;
		}
		long offset;
		boolean leftEdge;
	}

	LinkedList edges = new LinkedList( );
	long[][] sections;

	void startSegment( long left )
	{
		addEdge( left, true );
	}

	void endSegment( long right )
	{
		addEdge( right, false );
	}

	boolean inSegment( long offset )
	{
		if ( sections == null )
		{
			normalize( );
		}
		for ( int i = 0; i < sections.length; i++ )
		{
			long[] sect = sections[i];
			if ( sect[0] <= offset && sect[1] >= offset )
			{
				return true;
			}
		}
		return false;
	}

	private void addEdge( long offset, boolean left )
	{
		// drop the normalize result
		sections = null;
		// try to find the first segment will less that left
		SegmentEdge edge = null;
		ListIterator iter = edges.listIterator( edges.size( ) );
		while ( iter.hasPrevious( ) )
		{
			SegmentEdge next = (SegmentEdge) iter.previous( );
			if ( next.offset <= offset )
			{
				// insert it after the next
				edge = new SegmentEdge( offset );
				iter.next( );
				iter.add( edge );
				break;
			}
		}
		if ( edge == null )
		{
			// insert it at the end of the list
			edge = new SegmentEdge( offset );
			edges.addFirst( edge );
		}

		edge.leftEdge = left;
	}

	private static final long UNCLOSE_EDGE = -1;
	private static final long LEFT_MOST_EDGE = Long.MIN_VALUE;
	private static final long RIGHT_MOST_EDGE = Long.MAX_VALUE;
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
	 * <td> if the left is equals to the right, set the right to UNBOUND, change to START.
	 * Otherwise, save the section, create a new section [left, unbound], change to
	 * START</td>
	 * <td>update the section as [left, right]</td>
	 * <td>save the section</td>
	 * </tr>
	 * </table>
	 * 
	 * 
	 */
	private void normalize( )
	{
		ArrayList sects = new ArrayList( );
		long leftEdge = UNCLOSE_EDGE;
		long rightEdge = UNCLOSE_EDGE;
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
						if (edge.offset == rightEdge)
						{
							rightEdge = UNCLOSE_EDGE;
						}
						else
						{
							sects.add( new long[]{leftEdge, rightEdge} );
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
				sects.add( new long[]{leftEdge, RIGHT_MOST_EDGE} );
				break;
			case STATUS_CLOSE :
				sects.add( new long[]{leftEdge, rightEdge} );
				break;
		}

		sections = new long[sects.size( )][];
		for ( int i = 0; i < sects.size( ); i++ )
		{
			sections[i] = (long[]) sects.get( i );
		}
	}

	public String toString( )
	{
		StringBuffer buffer = new StringBuffer( );

		if (sections == null)
		{
			normalize( );
		}
		if (sections.length == 0)
		{
			return "[NONE]";
		}
		for ( int i = 0; i < sections.length; i++ )
		{
			long[] seg = sections[i];
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
