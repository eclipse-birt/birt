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

package org.eclipse.birt.report.engine.internal.executor.doc;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Segment
{

	static class SegmentEdge
	{

		SegmentEdge( Object offset, boolean leftEdge )
		{
			this.offset = offset;
			this.leftEdge = leftEdge;
		}
		Object offset;
		boolean leftEdge;
	}

	LinkedList<SegmentEdge> edges = new LinkedList<SegmentEdge>( );
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
		// add the new edge at the end of the list.
		SegmentEdge edge = new SegmentEdge( offset, left );
		edges.add( edge );
	}

	/**
	 * @deprecated
	 * the parameter left and right should be equal, or the LEFT_MOST_EDGE and the RIGHT_MOST_EDGE 
	 * @param left
	 * @param right
	 */
	void insertSection( Object left, Object right )
	{
		// drop the normalize result
		sections = null;
		SegmentEdge edge = null;

		if ( left == Segment.LEFT_MOST_EDGE && right == Segment.RIGHT_MOST_EDGE )
		{
			while ( edges.size( ) > 0 )
			{
				edges.remove( );
			}
			edge = new SegmentEdge( left, true );
			edges.add( edge );
			edge = new SegmentEdge( right, false );
			edges.add( edge );
			return;
		}

		if ( left != right )
		{
			return;
		}
		// try to find the first segment will less that left
		ListIterator iter = edges.listIterator( edges.size( ) );
		while ( iter.hasPrevious( ) )
		{
			SegmentEdge next = (SegmentEdge) iter.previous( );
			if ( comparator.compare( next.offset, left ) <= 0 )
			{
				// insert it after the next
				if ( !next.leftEdge )
				{
					iter.next( );
					edge = new SegmentEdge( left, true );
					iter.add( edge );
					edge = new SegmentEdge( right, false );
					iter.add( edge );
				}
				return;
			}
		}
		if ( edge == null )
		{
			// insert it at the end of the list
			edge = new SegmentEdge( right, false );
			edges.addFirst( edge );
			edge = new SegmentEdge( left, true );
			edges.addFirst( edge );
		}
	}

	public static final Object LEFT_MOST_EDGE = "LEFT";
	public static final Object RIGHT_MOST_EDGE = "RIGHT";

	public void normalize( )
	{
		LinkedList<Object[]> sects = new LinkedList<Object[]>( );
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
			assert ( edges.size( ) % 2 == 0 );

			ListIterator<SegmentEdge> edgesIter = edges.listIterator( );

			while ( edgesIter.hasNext( ) )
			{
				SegmentEdge leftEdge = edgesIter.next( );
				SegmentEdge rightEdge = edgesIter.next( );
				if ( sects.size( ) > 0 )
				{
					Object[] prevSect = sects.getLast( );
					if ( leftEdge.offset.equals( prevSect[1] ) )
					{
						prevSect[1] = rightEdge.offset;
						continue;
					}
				}
				if ( !isSectEmpty( leftEdge.offset, rightEdge.offset ) )
				{
					sects.add( new Object[]{leftEdge.offset,
									rightEdge.offset} );
				}
			}
		}

		sections = sects.toArray( new Object[sects.size( )][] );
	}

	private boolean isSectEmpty( Object leftEdge, Object rightEdge )
	{
		if ( rightEdge == LEFT_MOST_EDGE || leftEdge == RIGHT_MOST_EDGE )
		{
			return true;
		}
		return false;
	}
	
//	private boolean isSameEdge( Object edge1, Object edge2 )
//	{
//		if ( edge1 == edge2 )
//		{
//			return true;
//		}
//		if ( edge1 instanceof InstanceIndex && edge2 instanceof InstanceIndex )
//		{
//			InstanceID a = ( (InstanceIndex) edge1 ).getInstanceID( );
//			InstanceID b = ( (InstanceIndex) edge2 ).getInstanceID( );
//			if ( a == null || b == null )
//			{
//				return false;
//			}
//			long uid_a = a.getUniqueID( );
//			long uid_b = b.getUniqueID( );
//			if ( uid_a == uid_b )
//			{
//				return true;
//			}
//		}
//		return false;
//	}

	public String toString( )
	{
		StringBuffer buffer = new StringBuffer( );

		normalize( );
		
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
