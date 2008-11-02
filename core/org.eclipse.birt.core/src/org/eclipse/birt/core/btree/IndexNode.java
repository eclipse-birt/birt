/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.btree;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 
 * the node structure is:
 * 
 * <pre>
 * NEXT_BLOCK		INT			if the node contains extra blocks
 * NODE_TYPE		INT			node type, can be INDEX/LEAF/VALUE/EXTRA
 * 
 * NODE_SIZE		INT			node size, exclude the NODE_TYPE and NEXT_BLOCK
 * PREV_NODE_ID		INT			previous node id
 * NEXT_NODE_ID		INT			next node id
 * KEY_COUNT		INT			key count saved in this node
 * FIRST_CHILD		INT			child contains keys which are less than the first key
 * KEY_1			...			first key
 * CHILD_ID_1		INT			child node contains keys which are greater or equal than the first key 
 * KEY_2			...			second key
 * CHILD_ID_2		INT			child node contains keys which are greater than or equal to the second key
 * 
 * FIRST_CHILD &lt; KEY_1 &lt;= CHILD_ID_1 &lt; KEY_2 &lt;= CHILD_ID_2
 * 
 * </pre>
 * 
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public class IndexNode<K, V> extends BTreeNode<K, V>
{

	static final int EMPTY_NODE_SIZE = 20;

	private int nodeSize;
	private int prevNodeId;
	private int nextNodeId;
	private int entryCount;
	private int firstChild;

	private IndexEntry<K, V> firstEntry;
	private IndexEntry<K, V> lastEntry;

	IndexNode( BTree<K, V> btree, int nodeId )
	{
		super( btree, NODE_INDEX, nodeId );
		this.nodeSize = EMPTY_NODE_SIZE;
		this.prevNodeId = -1;
		this.nextNodeId = -1;
		this.entryCount = 0;
		this.firstChild = -1;
	}

	public int getFirstChild( )
	{
		return firstChild;
	}

	public void setFirstChild( int firstChild )
	{
		this.firstChild = firstChild;
	}

	public int getPrevNodeId( )
	{
		return prevNodeId;
	}

	public void setPrevNodeId( int prevNodeId )
	{
		this.prevNodeId = prevNodeId;
	}

	public int getNextNodeId( )
	{
		return nextNodeId;
	}

	public void setNextNodeId( int nextNodeId )
	{
		this.nextNodeId = nextNodeId;
	}

	public int getNodeSize( )
	{
		return nodeSize;
	}

	public int getEntryCount( )
	{
		return entryCount;
	}

	public IndexEntry<K, V> getFirstEntry( )
	{
		return firstEntry;
	}

	public IndexEntry<K, V> getLastEntry( )
	{
		return lastEntry;
	}

	public LeafEntry<K, V> find( BTreeValue<K> key ) throws IOException
	{
		int childNodeId = findChildNode( key );
		if ( childNodeId != -1 )
		{
			BTreeNode<K, V> node = btree.loadBTreeNode( childNodeId );
			try
			{
				if ( node.nodeType == NODE_INDEX )
				{
					return ( (IndexNode<K, V>) node ).find( key );
				}
				else if ( node.nodeType == NODE_LEAF )
				{
					return ( (LeafNode<K, V>) node ).find( key );
				}
				throw new IOException( "unsupport node type" + node.nodeType );
			}
			finally
			{
				node.unlock( );
			}
		}
		return null;
	}

	/**
	 * find a entry which key is equal or less than the key.
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	private int findChildNode( BTreeValue<K> key ) throws IOException
	{
		int childNodeId = firstChild;
		IndexEntry<K, V> entry = firstEntry;
		while ( entry != null )
		{
			int result = btree.compare( entry.getKey( ), key );
			if ( result == 0 )
			{
				childNodeId = entry.childNodeId;
				break;
			}
			if ( result > 0 )
			{
				break;
			}
			childNodeId = entry.childNodeId;
			entry = entry.next;
		}
		return childNodeId;
	}

	public LeafEntry<K, V> insert( BTreeValue<K> key, BTreeValue<V> value )
			throws IOException
	{
		int childNodeId = findChildNode( key );
		if ( childNodeId != -1 )
		{
			BTreeNode<K, V> node = btree.loadBTreeNode( childNodeId );
			try
			{
				if ( node.nodeType == NODE_INDEX )
				{
					IndexNode<K, V> indexNode = (IndexNode<K, V>) node;
					LeafEntry<K, V> insertEntry = indexNode.insert( key, value );
					if ( indexNode.needSplit( ) )
					{
						IndexEntry<K, V> splitEntry = indexNode.split( );
						if ( splitEntry != null )
						{
							insertIndex( splitEntry.getKey( ), splitEntry
									.getChildNodeId( ) );
						}
					}
					return insertEntry;
				}
				if ( node.nodeType == NODE_LEAF )
				{
					LeafNode<K, V> leafNode = (LeafNode<K, V>) node;
					LeafEntry<K, V> insertEntry = leafNode.insert( key, value );

					if ( leafNode.needSplit( ) )
					{
						IndexEntry<K, V> splitEntry = leafNode.split( );
						if ( splitEntry != null )
						{
							insertIndex( splitEntry.getKey( ), splitEntry
									.getChildNodeId( ) );
						}
					}
					return insertEntry;
				}
				throw new IOException( "unsupport node type" + node.nodeType
						+ " for node " + childNodeId );
			}
			finally
			{
				node.unlock( );
			}
		}
		return null;
	}

	private void insertBefore( IndexEntry<K, V> insertPoint,
			IndexEntry<K, V> entry )
	{
		entry.setNode( this );
		if ( insertPoint == null )
		{
			if ( lastEntry == null )
			{
				entry.setPrev( null );
				entry.setNext( null );
				firstEntry = entry;
				lastEntry = entry;
			}
			else
			{
				entry.setPrev( lastEntry );
				entry.setNext( null );
				lastEntry.setNext( entry );
				lastEntry = entry;
			}
		}
		else
		{
			IndexEntry<K, V> prev = insertPoint.getPrev( );
			entry.setNext( insertPoint );
			entry.setPrev( prev );
			insertPoint.setPrev( entry );
			if ( prev != null )
			{
				prev.setNext( entry );
			}
			else
			{
				firstEntry = entry;
			}
		}
		nodeSize += getEntrySize( entry );
		entryCount++;
	}

	protected void insertIndex( BTreeValue<K> insertKey, int childNodeId )
			throws IOException
	{
		IndexEntry<K, V> entry = firstEntry;
		while ( entry != null )
		{
			int result = btree.compare( entry.getKey( ), insertKey );
			if ( result == 0 )
			{
				throw new IOException( "unexpected equal keys" );
			}
			if ( result > 0 )
			{
				break;
			}
			entry = entry.next;
		}

		// insert at the last entry
		IndexEntry<K, V> newEntry = new IndexEntry<K, V>( this, insertKey,
				childNodeId );
		insertBefore( entry, newEntry );

		dirty = true;
		return;
	}

	public boolean needSplit( )
	{
		return nodeSize > MAX_NODE_SIZE && entryCount > MIN_ENTRY_COUNT;
	}

	public IndexEntry<K, V> split( ) throws IOException
	{
		entryCount = entryCount / 2;
		nodeSize = EMPTY_NODE_SIZE;
		IndexEntry<K, V> splitEntry = firstEntry;
		for ( int i = 0; i < entryCount; i++ )
		{
			nodeSize += getEntrySize( splitEntry );
			splitEntry = splitEntry.getNext( );
		}

		// break at the splitIndex into two nodes: current and new node.
		lastEntry = splitEntry.getPrev( );
		lastEntry.setNext( null );

		// create a new node for splitEntry
		IndexNode<K, V> newNode = btree.createIndexNode( );
		try
		{
			newNode.setFirstChild( splitEntry.childNodeId );
			IndexEntry<K, V> entry = splitEntry.getNext( );
			while ( entry != null )
			{
				IndexEntry<K, V> nextEntry = entry.getNext( );
				entry.setPrev( null );
				entry.setNext( null );
				newNode.insertBefore( null, entry );
				entry = nextEntry;
			}

			// link the node together
			newNode.setPrevNodeId( nodeId );
			newNode.setNextNodeId( nextNodeId );
			if ( nextNodeId != -1 )
			{
				IndexNode<K, V> nextNode = btree.loadIndexNode( nextNodeId );
				try
				{
					nextNode.setPrevNodeId( newNode.getNodeId( ) );
					nextNode.setDirty( true );
				}
				finally
				{
					nextNode.unlock( );
				}
			}
			nextNodeId = newNode.getNodeId( );

			return new IndexEntry<K, V>( this, splitEntry.getKey( ), newNode
					.getNodeId( ) );
		}
		finally
		{
			newNode.unlock( );
		}
	}

	public void read( DataInput in ) throws IOException
	{
		nodeSize = in.readInt( );
		prevNodeId = in.readInt( );
		nextNodeId = in.readInt( );
		entryCount = in.readInt( );
		firstChild = in.readInt( );
		for ( int i = 0; i < entryCount; i++ )
		{
			IndexEntry<K, V> entry = readEntry( in );
			if ( firstEntry == null )
			{
				firstEntry = entry;
				lastEntry = entry;
			}
			else
			{
				lastEntry.setNext( entry );
				entry.setPrev( lastEntry );
				lastEntry = entry;
			}
		}
		dirty = false;
	}

	protected void write( DataOutput out ) throws IOException
	{
		out.writeInt( nodeSize );
		out.writeInt( prevNodeId );
		out.writeInt( nextNodeId );
		out.writeInt( entryCount );
		out.writeInt( firstChild );
		IndexEntry<K, V> entry = firstEntry;
		while ( entry != null )
		{
			writeEntry( out, entry );
			entry = entry.getNext( );
		}
		dirty = false;
	}

	private IndexEntry<K, V> readEntry( DataInput in ) throws IOException
	{
		BTreeValue<K> key = btree.readKey( in );
		int childNodeId = in.readInt( );
		return new IndexEntry<K, V>( this, key, childNodeId );
	}

	private void writeEntry( DataOutput out, IndexEntry<K, V> entry )
			throws IOException
	{
		btree.writeKey( out, entry.getKey( ) );
		out.writeInt( entry.getChildNodeId( ) );
	}

	private int getEntrySize( IndexEntry<K, V> entry )
	{
		return 4 + btree.getKeySize( entry.getKey( ) );
	}

	public void dumpNode( ) throws IOException
	{
		System.out.println( "INDEX:" + nodeId );
		System.out.println( "nodeSize:" + nodeSize );
		System.out.println( "prevNodeId:" + prevNodeId );
		System.out.println( "nextNodeId :" + nextNodeId );
		System.out.println( "entryCount:" + entryCount );
		System.out.print( firstChild );
		IndexEntry<K, V> entry = firstEntry;
		while ( entry != null )
		{
			System.out.print( "<<[" );
			System.out.print( btree.getKey( entry.getKey( ) ) );
			System.out.print( "]<<" );
			System.out.print( entry.getChildNodeId( ) );
			entry = entry.getNext( );
		}
		System.out.println( );
	}

	public void dumpAll( ) throws IOException
	{
		dumpNode( );
		BTreeNode<K, V> node = btree.loadBTreeNode( firstChild );
		try
		{
			node.dumpAll( );
		}
		finally
		{
			node.unlock( );
		}

		IndexEntry<K, V> entry = firstEntry;
		while ( entry != null )
		{
			node = btree.loadBTreeNode( entry.getChildNodeId( ) );
			try
			{
				node.dumpAll( );
			}
			finally
			{
				node.unlock( );
			}
			entry = entry.getNext( );
		}
	}
}
