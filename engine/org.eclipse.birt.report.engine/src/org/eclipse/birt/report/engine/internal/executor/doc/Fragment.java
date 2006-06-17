package org.eclipse.birt.report.engine.internal.executor.doc;



public class Fragment
{
	long offset;
	Segment segment = new Segment( );
	Fragment next;
	Fragment child;

	/**
	 * get the fragment which start from offset.
	 * @param offset
	 * @return
	 */
	Fragment getFragment( long offset )
	{
			Fragment frag = child;
			while (frag != null)
			{
				if ( frag.offset == offset )
				{
					return frag;
				}
				frag = frag.next;
			} 
		return null;
	}

	/**
	 * get next fragment start from offset.
	 * @param offset 
	 * @return
	 */
	public Fragment getNextFragment( long offset )
	{
		Fragment frag = child;
		while ( frag != null )
		{
			if ( frag.offset > offset )
			{
				return frag;
			}
			frag = frag.next;
		} 
		return null;
	}

	
	public Fragment()
	{
		offset = -1; 
	}
	
	Fragment(long offset)
	{
		this.offset = offset;
	}

	/**
	 * add fragment defind by the left/right edges.
	 * @param leftEdges
	 * @param rightEdges
	 */
	public void addFragment( long[] leftEdges, long[] rightEdges )
	{
		Fragment leftEdge = this;
		for ( int i = 0; i < leftEdges.length; i++ )
		{
			leftEdge.segment.startSegment( leftEdges[i] );
			// search the insert point in the edge tree
			leftEdge = addFragment( leftEdge, leftEdges[i] );
		}
		//add it into left
		leftEdge.segment.startSegment( Long.MIN_VALUE );
		
		Fragment rightEdge  = this;
		for ( int i = 0; i < rightEdges.length; i++ )
		{
			rightEdge.segment.endSegment( rightEdges[i] );
			// search the insert point in the edge tree
			rightEdge = addFragment( rightEdge, rightEdges[i] );
		}
		//add it into right
		rightEdge.segment.endSegment( Long.MIN_VALUE );
		
		segment.normalize();
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
	private Fragment addFragment( Fragment parent, long offset)
	{
		assert parent != null;
		Fragment prev = null;
		Fragment frag = parent.child;
		while ( frag != null )
		{
			if ( frag.offset == offset )
			{
				// that's it
				return frag;
			}
			if ( frag.offset > offset )
			{
				// we found it, insert it before the edge, after the prevEdge
				break;
			}
			// continue to search the insert position
			prev = frag;
			frag = frag.next;
		}

		// we have find the position, just after the prevEdge
		frag = new Fragment( offset );

		// link it with the previous
		if ( prev != null )
		{
			if (prev.next != null)
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
	 * @param offset the child offset.
	 * @return
	 */
	public boolean inFragment( long offset)
	{
		return segment.inSegment( offset );
	}
	
}
