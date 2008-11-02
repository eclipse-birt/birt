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
 * 
 * NEXT_BLOCK		INT			if the node contains extra blocks
 * NODE_TYPE		INT			node type, must be LEAF
 * 
 * NODE_SIZE		INT			node size, exclude the NODE_TYPE and NEXT_BLOCK
 * PREV_NODE_ID		INT			previous node id
 * NEXT_NODE_ID		INT			next node id
 * KEY_COUNT		INT			key count saved in this node
 * KEY_1			...			key
 * VALUES_TYPE  	INT			can be INLINE/EXTERNAL
 * VALUES			...			values
 * 
 * </pre>
 * 
 * 
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */

class LeafNode<K, V> extends BTreeNode<K, V>
{

	static final int EMPTY_NODE_SIZE = 16;

	private int prevNodeId = -1;
	private int nextNodeId = -1;
	private int entryCount;
	private int nodeSize;

	private LeafEntry<K, V> firstEntry;
	private LeafEntry<K, V> lastEntry;

	public LeafNode( BTree<K, V> btree, int nodeId )
	{
		super( btree, NODE_LEAF, nodeId );
		this.nodeSize = EMPTY_NODE_SIZE;
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

	public int getEntryCount( )
	{
		return entryCount;
	}

	public int getNodeSize( )
	{
		return nodeSize;
	}

	public LeafEntry<K, V> getFirstEntry( )
	{
		return firstEntry;
	}

	public LeafEntry<K, V> getLastEntry( )
	{
		return lastEntry;
	}

	/**
	 * return the first entry which key is great than or equal to the given key.
	 * 
	 * @param key
	 *            the search key value.
	 * @return the first entry which key is great than or equal to the given
	 *         key.
	 * @throws IOException
	 */
	public LeafEntry<K, V> find( BTreeValue<K> key ) throws IOException
	{
		LeafEntry<K, V> entry = firstEntry;
		while ( entry != null )
		{
			int result = btree.compare( entry.getKey( ), key );
			if ( result == 0 )
			{
				return entry;
			}
			if ( result > 0 )
			{
				return null;
			}
			entry = entry.getNext( );
		}
		return null;
	}

	public LeafEntry<K, V> insert( BTreeValue<K> key, BTreeValue<V> value )
			throws IOException
	{
		dirty = true;
		LeafEntry<K, V> insertPoint = firstEntry;
		while ( insertPoint != null )
		{
			int result = btree.compare( insertPoint.getKey( ), key );
			if ( result == 0 )
			{
				if ( !btree.hasValue( ) )
				{
					return insertPoint;
				}
				// append or replace the value
				if ( !btree.allowDuplicate( ) )
				{
					// replace the current value
					BTreeValues<V> values = insertPoint.getValues( );
					int valueSize1 = values.getValueSize( );
					SingleValueList<K, V> sv = new SingleValueList<K, V>(
							btree, value );
					int valueSize2 = sv.getValueSize( );
					insertPoint.setValues( sv );
					nodeSize = nodeSize + valueSize2 - valueSize1;
					return insertPoint;
				}

				// append it to the current values
				BTreeValues<V> values = insertPoint.getValues( );
				int valueSize1 = values.getValueSize( );
				values.append( value );
				int valueSize2 = values.getValueSize( );
				if ( valueSize2 > MAX_NODE_SIZE / 2 )
				{
					values = btree.createExternalValueList( values );
					valueSize2 = values.getValueSize( );
					insertPoint.setValues( values );
				}
				nodeSize = nodeSize - valueSize1 + valueSize2;

				btree.increaseTotalValues( );
				return insertPoint;
			}

			if ( result > 0 )
			{
				// insert just before the insertPoint
				break;
			}
			insertPoint = insertPoint.getNext( );
		}

		// now we should insert the entry before the insert point
		BTreeValues<V> values = null;
		if ( btree.hasValue( ) )
		{
			if ( btree.allowDuplicate( ) )
			{
				values = new InlineValueList<K, V>( btree );
				values.append( value );
				if ( values.getValueSize( ) > MAX_NODE_SIZE / 2 )
				{
					values = btree.createExternalValueList( values );
				}
			}
			else
			{
				values = new SingleValueList<K, V>( btree, value );
			}
		}
		LeafEntry<K, V> entry = new LeafEntry<K, V>( this, key, values );
		insertBefore( insertPoint, entry );

		// if the node size is larger than the block size, split into two nodes.
		if ( btree.hasValue( ) )
		{
			btree.increaseTotalValues( );
		}
		btree.increaseTotalKeys( );
		return entry;
	}

	private void insertBefore( LeafEntry<K, V> insertPoint,
			LeafEntry<K, V> entry ) throws IOException
	{
		entry.setNode( this );
		// now we should insert the entry before the insert point
		if ( insertPoint == null )
		{
			// insert it as the last entry
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
			LeafEntry<K, V> prev = insertPoint.getPrev( );
			entry.setPrev( prev );
			entry.setNext( insertPoint );
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

	public boolean needSplit( )
	{
		return nodeSize > MAX_NODE_SIZE && entryCount > MIN_ENTRY_COUNT;
	}

	public IndexEntry<K, V> split( ) throws IOException
	{
		entryCount = entryCount / 2;
		nodeSize = EMPTY_NODE_SIZE;
		LeafEntry<K, V> splitEntry = firstEntry;
		for ( int i = 0; i < entryCount; i++ )
		{
			nodeSize += getEntrySize( splitEntry );
			splitEntry = splitEntry.getNext( );
		}

		// break at the splitIndex into two nodes: current and new node.
		lastEntry = splitEntry.getPrev( );
		lastEntry.setNext( null );

		// create a new node for values which after (include) splitEntry
		LeafNode<K, V> newNode = btree.createLeafNode( );
		try
		{
			LeafEntry<K, V> entry = splitEntry;
			while ( entry != null )
			{
				LeafEntry<K, V> nextEntry = entry.getNext( );
				// remove it out from the original list
				entry.setPrev( null );
				entry.setNext( null );
				// add it to the new node
				newNode.insertBefore( null, entry );
				entry = nextEntry;
			}

			// link the node together
			newNode.setNextNodeId( nextNodeId );
			newNode.setPrevNodeId( nodeId );
			if ( nextNodeId != -1 )
			{
				LeafNode<K, V> nextNode = btree.loadLeafNode( nextNodeId );
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

			// return the split entry
			return new IndexEntry<K, V>( null, splitEntry.getKey( ), newNode
					.getNodeId( ) );
		}
		finally
		{
			newNode.unlock( );
		}
	}

	void read( DataInput in ) throws IOException
	{
		nodeSize = in.readInt( );
		prevNodeId = in.readInt( );
		nextNodeId = in.readInt( );
		entryCount = in.readInt( );
		for ( int i = 0; i < entryCount; i++ )
		{
			LeafEntry<K, V> entry = readEntry( in );
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
	}

	protected void write( DataOutput out ) throws IOException
	{
		out.writeInt( nodeSize );
		out.writeInt( prevNodeId );
		out.writeInt( nextNodeId );
		out.writeInt( entryCount );
		LeafEntry<K, V> entry = firstEntry;
		while ( entry != null )
		{
			writeEntry( out, entry );
			entry = entry.getNext( );
		}
	}

	private int getEntrySize( LeafEntry<K, V> entry )
	{
		int keySize = btree.getKeySize( entry.getKey( ) );
		if ( btree.hasValue( ) )
		{
			BTreeValues<V> values = entry.getValues( );
			if ( btree.allowDuplicate( ) )
			{
				return keySize + 4 + values.getValueSize( );
			}
			return keySize + values.getValueSize( );
		}
		return keySize;
	}

	protected LeafEntry<K, V> readEntry( DataInput in ) throws IOException
	{
		BTreeValue<K> key = btree.readKey( in );
		BTreeValues<V> values = readValues( in );
		return new LeafEntry<K, V>( this, key, values );
	}

	private BTreeValues<V> readValues( DataInput in ) throws IOException
	{
		if ( btree.hasValue( ) )
		{
			if ( btree.allowDuplicate( ) )
			{
				int type = in.readInt( );
				if ( type == BTreeValues.INLINE_VALUES )
				{
					InlineValueList<K, V> inlineValues = new InlineValueList<K, V>(
							btree );
					inlineValues.read( in );
					return inlineValues;
				}
				if ( type == BTreeValues.EXTERNAL_VALUES )
				{
					ExternalValueList<K, V> externalValues = new ExternalValueList<K, V>(
							btree );
					externalValues.read( in );
					return externalValues;
				}
				throw new IOException( "unknown values type :" + type );
			}
			SingleValueList<K, V> singleValues = new SingleValueList<K, V>(
					btree );
			singleValues.read( in );
			return singleValues;
		}
		return null;
	}

	private void writeEntry( DataOutput out, LeafEntry<K, V> entry )
			throws IOException
	{
		btree.writeKey( out, entry.getKey( ) );
		if ( btree.hasValue( ) )
		{
			BTreeValues<V> values = entry.getValues( );
			if ( btree.allowDuplicate( ) )
			{
				out.writeInt( values.getType( ) );
			}
			values.write( out );
		}
	}

	public void dumpNode( ) throws IOException
	{
		System.out.println( "LeafNode:" + nodeId );
		System.out.println( "nodeSize:" + nodeSize );
		System.out.println( "prevNodeId:" + prevNodeId );
		System.out.println( "nextNodeId :" + nextNodeId );
		System.out.println( "entryCount:" + entryCount );
		LeafEntry<K, V> entry = firstEntry;
		int id = 0;
		while ( entry != null )
		{
			System.out.print( id + ":\"" + btree.getKey( entry.getKey( ) )
					+ "\"" );
			if ( btree.hasValue( ) )
			{
				System.out.print( " valueCount:"
						+ entry.getValues( ).getValueCount( ) );
				System.out.print( " valueSize:"
						+ entry.getValues( ).getValueSize( ) );
			}
			System.out.println( );
			id++;
			entry = entry.getNext( );
		}
	}

	public void dumpAll( ) throws IOException
	{
		dumpNode( );

		LeafEntry<K, V> entry = firstEntry;
		while ( entry != null )
		{
			BTreeValues<V> values = entry.getValues( );
			if ( values != null )
			{
				if ( values.getType( ) == BTreeValues.EXTERNAL_VALUES )
				{
					ExternalValueList<K, V> extValues = (ExternalValueList<K, V>) values;
					int nodeId = extValues.getFirstNodeId( );
					while ( nodeId != -1 )
					{
						ValueNode<K, V> valueNode = btree
								.loadValueNode( nodeId );
						try
						{
							valueNode.dumpAll( );
							nodeId = valueNode.getNextNodeId( );
						}
						finally
						{
							valueNode.unlock( );
						}
					}
				}
			}
			entry = entry.getNext( );
		}
	}

}