
package org.eclipse.birt.report.engine.internal.executor.doc;

import java.util.Comparator;

public class Fragment
{

	Object index;
	Segment segment;
	Fragment next;
	Fragment child;
	Comparator comparator;

	public Fragment( Comparator comparator )
	{
		this(comparator, null);
	}

	public Fragment( Comparator comparator, Object offset )
	{
		if ( comparator instanceof FragmentComparator )
		{
			this.comparator = comparator;
		}
		else
		{
			this.comparator = new FragmentComparator( comparator );
		}
		this.index = offset;
		this.segment = new Segment( this.comparator );
	}

	/**
	 * get the fragment which start from offset.
	 * 
	 * @param offset
	 * @return
	 */
	public Fragment getFragment( Object offset )
	{
		Fragment frag = child;
		while ( frag != null )
		{
			if ( comparator.compare( frag.index, offset ) == 0 )
			{
				return frag;
			}
			frag = frag.next;
		}
		return null;
	}

	/**
	 * get next fragment start from offset.
	 * 
	 * @param offset
	 * @return
	 */
	public Fragment getNextFragment( Object offset )
	{
		if ( offset == Segment.LEFT_MOST_EDGE )
		{
			return child;
		}
		if ( offset == Segment.RIGHT_MOST_EDGE )
		{
			return null;
		}
		Fragment frag = child;
		while ( frag != null )
		{
			if ( comparator.compare( frag.index, offset ) > 0 )
			{
				return frag;
			}
			frag = frag.next;
		}
		return null;
	}

	public Fragment getFirstFragment( )
	{
		return child;
	}

	/**
	 * add fragment defind by the left/right edges.
	 * 
	 * @param leftEdges
	 * @param rightEdges
	 */
	public void addFragment( Object[] leftEdges, Object[] rightEdges )
	{
		Fragment leftEdge = this;
		for ( int i = 0; i < leftEdges.length; i++ )
		{
			leftEdge.segment.startSegment( leftEdges[i] );
			// search the insert point in the edge tree
			leftEdge = addFragment( leftEdge, leftEdges[i] );
		}
		// add it into left
		leftEdge.segment.startSegment( Segment.LEFT_MOST_EDGE );
		

		Fragment rightEdge = this;
		for ( int i = 0; i < rightEdges.length; i++ )
		{
			rightEdge.segment.endSegment( rightEdges[i] );
			// search the insert point in the edge tree
			rightEdge = addFragment( rightEdge, rightEdges[i] );
		}

		// add it into right
		rightEdge.segment.endSegment( Segment.LEFT_MOST_EDGE );

		segment.normalize( );
	}

	/**
	 * search in the edge list to find a position to update the node. If there
	 * is no proper edge node exits, it will create a edge node and insert it
	 * into the list.
	 * 
	 * @param parent
	 *            the insered or finded edge node.
	 * @param node
	 *            node to be insert.
	 * @return edge node which contains the node.
	 */
	private Fragment addFragment( Fragment parent, Object offset )
	{
		assert parent != null;
		Fragment prev = null;
		Fragment frag = parent.child;
		while ( frag != null )
		{
			int result = comparator.compare( frag.index, offset );
			if ( result == 0 )
			{
				// that's it
				return frag;
			}
			if ( result == 1 )
			{
				// we found it, insert it before the edge, after the prevEdge
				break;
			}
			// continue to search the insert position
			prev = frag;
			frag = frag.next;
		}

		// we have find the position, just after the prevEdge
		frag = new Fragment( comparator, offset );

		// link it with the previous
		if ( prev != null )
		{
			if ( prev.next != null )
			{
				frag.next = prev.next.next;
			}
			prev.next = frag;
		}
		else
		{
			if ( parent != null )
			{
				frag.next = parent.child;
				parent.child = frag;
			}
		}

		return frag;
	}

	/**
	 * does the offset is in the fragmetn.
	 * 
	 * @param offset
	 *            the child offset.
	 * @return
	 */
	public boolean inFragment( Object offset )
	{
		return segment.inSegment( offset );
	}

	public Object getOffset( )
	{
		return index;
	}

	public Object[][] getSections( )
	{
		if ( segment != null )
		{
			segment.normalize( );
			return segment.sections;
		}
		return null;
	}
}
