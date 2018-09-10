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

import java.util.Iterator;
import java.util.LinkedList;

import junit.framework.TestCase;

/**
 * Create such a Tree, use fragment to visit such a tree. <table
 * style="text-align:center" border="all">
 * <tr>
 * <td colspan="9">0</td>
 * </tr>
 * <tr>
 * <td colspan="3">1</td>
 * <td colspan="3">5</td>
 * <td colspan="3">9</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>3</td>
 * <td>4</td>
 * <td>6</td>
 * <td>7</td>
 * <td>8</td>
 * <td>10</td>
 * <td>11</td>
 * <td>12</td>
 * </tr>
 * </table>
 * 
 */
public class TreeFragmentTest extends TestCase
{

	Tree tree;

	public void setUp( )
	{
		createTree( );
	}

	public void testFragment( )
	{
		TreeFragment treeFrag = new TreeFragment( tree );
		treeFrag.addFragment( 1, 6 );
		treeFrag.build( );
		String sFrag = treeFrag.toString( );
		assertEquals( "0,1,2,3,4,5,6,", sFrag );

		treeFrag = new TreeFragment( tree );
		treeFrag.addFragment( 1, 6 );
		treeFrag.addFragment( 8, 9 );
		treeFrag.build( );
		sFrag = treeFrag.toString( );
		assertEquals( "0,1,2,3,4,5,6,8,9,", sFrag );

		treeFrag = new TreeFragment( tree );
		treeFrag.addFragment( 1, 6 );
		treeFrag.addFragment( 8, 9 );
		treeFrag.addFragment( 11, 11 );
		treeFrag.build( );
		sFrag = treeFrag.toString( );
		assertEquals( "0,1,2,3,4,5,6,8,9,11,", sFrag );
	}

	void createTree( )
	{
		tree = new Tree( );
		tree.root = new Tree.Node( 0 );
		createTreeNodes( tree.root, 3, 1 );
	}

	// create a tree which contains 24 nodes
	long createTreeNodes( Tree.Node root, int childCount, int level )
	{
		long offset = root.offset + 1;
		for ( int i = 0; i < childCount; i++ )
		{
			Tree.Node child = new Tree.Node( offset++ );
			root.addChild( child );
			if ( level > 0 )
			{
				offset = createTreeNodes( child, childCount, level - 1 );
			}
		}
		return offset;
	}
}

class Tree
{

	static class Node
	{

		long offset;
		Node parent;
		Node child;
		Node next;

		Node( long offset )
		{
			this.offset = offset;
		}

		void addChild( Node node )
		{
			if ( child == null )
			{
				child = node;
				node.parent = this;
				return;
			}
			Node lastChild = child;
			while ( lastChild.next != null )
			{
				lastChild = lastChild.next;
			}
			lastChild.next = node;
			node.parent = this;
		}

		public String toString( )
		{
			if ( parent == null )
			{
				return String.valueOf( offset );
			}
			StringBuffer buffer = new StringBuffer( );
			toString( buffer );
			return buffer.toString( );
		}

		public void toString( StringBuffer buffer )
		{
			if ( parent == null )
			{
				buffer.append( String.valueOf( offset ) );
			}
			else
			{
				parent.toString( buffer );
				buffer.append( "." );
				buffer.append( String.valueOf( offset ) );
			}
		}
	}

	Node root;

	Long[] getEdges( Node node )
	{
		LinkedList<Node> nodes = new LinkedList<Node>( );
		while ( node != null )
		{
			nodes.addFirst( node );
			node = node.parent;
		}
		Iterator<Node> iter = nodes.iterator( );
		Long[] edges = new Long[nodes.size( )];
		int i = 0;
		while ( iter.hasNext( ) )
		{
			node = (Node) iter.next( );
			edges[i++] = node.offset;
		}
		return edges;
	}

	public String getTree( )
	{
		StringBuffer buffer = new StringBuffer( );
		visitTree( buffer, root, null );
		return buffer.toString( );
	}

	void visitTree( StringBuffer buffer, Node node, Fragment fragment )
	{
		if ( fragment == null || fragment.inFragment( node.offset ) )
		{
			buffer.append( node.toString( ) );
			buffer.append( ',' );
		}
		Node child = node.child;
		while ( child != null )
		{
			visitTree( buffer, child, fragment );
			child = child.next;
		}
	}

	Node findNode( long offset )
	{
		return findNode( root, offset );
	}

	private Node findNode( Node node, long offset )
	{
		if ( node.offset == offset )
		{
			return node;
		}
		// find it in the children
		Node child = node.child;
		while ( child != null )
		{
			Node target = findNode( child, offset );
			if ( target != null )
			{
				return target;
			}
			child = child.next;
		}
		return null;
	}
}

class TreeFragment
{

	Tree tree;
	Fragment fragment = new Fragment( new LongComparator( ) );

	TreeFragment( Tree tree )
	{
		this.tree = tree;
	}

	void addFragment( long left, long right )
	{
		Tree.Node leftNode = tree.findNode( left );
		Tree.Node rightNode = tree.findNode( right );

		Object[] leftEdges = tree.getEdges( leftNode );
		Object[] rightEdges = tree.getEdges( rightNode );
		fragment.addSection( leftEdges, rightEdges );
	}
	
	void build( )
	{
		fragment.build( );
	}

	public String toString( )
	{
		StringBuffer buffer = new StringBuffer( );
		Fragment frag = fragment.getFragment( tree.root.offset );
		visit( buffer, tree.root, frag );
		return buffer.toString( );
	}

	void visit( StringBuffer buffer, Tree.Node node, Fragment frag )
	{
		// visit the node
		buffer.append( String.valueOf( node.offset ) );
		buffer.append( "," );

		// visit the children of that node
		Tree.Node child = node.child;
		if ( child != null )
		{
			if ( frag != null )
			{
				if ( !frag.inFragment( child.offset ) )
				{
					Fragment childFrag = frag
							.getNextFragment( Segment.LEFT_MOST_EDGE );
					if ( childFrag != null )
					{
						child = tree.findNode( ( (Long) childFrag.index )
								.longValue( ) );
					}
					else
					{
						child = null;
					}
				}
			}
		}

		while ( child != null )
		{
			Fragment childFrag = null;
			if ( frag != null )
			{
				childFrag = frag.getFragment( child.offset );
			}
			visit( buffer, child, childFrag );
			child = child.next;
			if ( child != null )
			{
				if ( frag != null )
				{
					if ( !frag.inFragment( child.offset ) )
					{
						Fragment nextFrag = frag.getNextFragment( child.offset );
						if ( nextFrag != null )
						{
							child = tree.findNode( ( (Long) nextFrag.index )
									.longValue( ) );
						}
						else
						{
							child = null;
						}
					}
				}
			}
		}
	}
}
