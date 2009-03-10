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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class BTreeCursor<K, V>
{

	protected BTree<K, V> btree;
	protected LeafEntry<K, V> entry;

	BTreeCursor( BTree<K, V> btree )
	{
		this.btree = btree;
		this.entry = null;
	}

	/**
	 * return the entry count in the cursor.
	 * 
	 * @return
	 */
	public int getTotalKeys( )
	{
		return btree.getTotalKeys( );
	}

	public int getTotalValues( )
	{
		return btree.getTotalValues( );
	}

	/**
	 * set the cursor's position to the first entry.
	 * 
	 * @return
	 */
	public void reset( ) throws IOException
	{
		entry = btree.getFirstEntry( );
	}

	/**
	 * move to the first entry which value equals to the key.
	 * 
	 * @param key
	 *            key value
	 * @return true if the current key equals to the key.
	 * @throws IOException
	 */
	public boolean moveTo( K key ) throws IOException
	{
		LeafEntry<K, V> tgtEntry = btree.findEntry( key );
		if ( tgtEntry != null )
		{
			btree.lockEntry( tgtEntry );
			if ( entry != null )
			{
				btree.unlockEntry( entry );
			}
			entry = tgtEntry;
			K tgtKey = btree.getKey( tgtEntry.getKey( ) );
			if ( key.equals( tgtKey ) )
			{
				return true;
			}
		}
		return false;
	}
	
	private LeafEntry<K, V> getPrevEntry( LeafEntry<K, V> entry )
			throws IOException
	{
		LeafEntry<K, V> prevEntry = entry.getPrev( );
		if ( prevEntry != null )
		{
			return prevEntry;
		}
		int prevNodeId = entry.getNode( ).getPrevNodeId( );
		if ( prevNodeId != -1 )
		{
			LeafNode<K, V> prevNode = (LeafNode<K, V>) btree
					.loadBTreeNode( prevNodeId );
			try
			{
				return prevNode.getLastEntry( );
			}
			finally
			{
				prevNode.unlock( );
			}
		}
		return null;
	}

	private LeafEntry<K, V> getNextEntry( LeafEntry<K, V> entry )
			throws IOException
	{
		if ( entry == null )
			return null;
		LeafEntry<K, V> nextEntry = entry.getNext( );
		if ( nextEntry != null )
		{
			return nextEntry;
		}
		int nextNodeId = entry.getNode( ).getNextNodeId( );
		if ( nextNodeId != -1 )
		{
			LeafNode<K, V> nextNode = (LeafNode<K, V>) btree
					.loadBTreeNode( nextNodeId );
			try
			{
				return nextNode.getFirstEntry( );
			}
			finally
			{
				nextNode.unlock( );
			}
		}
		return null;
	}

	public boolean previous( ) throws IOException
	{
		LeafEntry<K, V> tgtEntry = getPrevEntry( entry );
		if ( tgtEntry != null )
		{
			btree.lockEntry( tgtEntry );
			if ( entry != null )
			{
				btree.unlockEntry( entry );
			}
			entry = tgtEntry;
			return true;
		}
		return false;
	}

	public boolean next( ) throws IOException
	{
		if( entry == null )
			return false;
		LeafEntry<K, V> tgtEntry = getNextEntry( entry );
		if ( tgtEntry != null )
		{
			btree.lockEntry( tgtEntry );
			if ( entry != null )
			{
				btree.unlockEntry( entry );
			}
			entry = tgtEntry;
			return true;
		}
		return false;
	}

	public boolean hasPrevious( ) throws IOException
	{
		LeafEntry<K, V> prev = getPrevEntry( entry );
		return prev != null;
	}

	public boolean hasNext( ) throws IOException
	{
		LeafEntry<K, V> next = getNextEntry( entry );
		return next != null;
	}

	public K getKey( ) throws IOException
	{
		if ( entry == null )
		{
			throw new IOException( "Invalid cursor position" );
		}
		return btree.getKey( entry.getKey( ) );
	}

	public V getValue( ) throws IOException
	{
		if ( !btree.hasValue( ) )
		{
			return null;
		}
		BTreeValues<V> values = entry.getValues( );
		BTreeValues.Value<V> value = values.getFirstValue( );
		BTreeValue<V> v = value.getValue( );
		return btree.getValue( v );
	}

	public Collection<V> getValues( ) throws IOException
	{
		if ( entry != null )
		{
			BTreeValues<V> values = entry.getValues( );
			ArrayList<V> list = new ArrayList<V>( values.getValueCount( ) );
			BTreeValues.Value<V> value = values.getFirstValue( );
			while ( value != null )
			{
				BTreeValue<V> bv = value.getValue( );
				V v = btree.getValue( bv );
				list.add( v );
			}
			return list;
		}
		throw new IOException( "must initialize the cursor first" );
	}

	/**
	 * insert the key/value pair to the btree.
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void insert( K key, V value ) throws IOException
	{
		LeafEntry<K, V> tgtEntry = btree.insertEntry( key, value );
		btree.lockEntry( tgtEntry );
		if ( entry != null )
		{
			btree.unlockEntry( entry );
		}
		entry = tgtEntry;
	}

	/**
	 * remove the current entry. the cursor is moved to the next entry.
	 * 
	 * @throws IOException
	 */
	public void remove( ) throws IOException
	{
		throw new UnsupportedOperationException( "remove" );
	}

	/**
	 * change the current entry's value.
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void setValue( V value ) throws IOException
	{
		throw new UnsupportedOperationException( "setValue(V)" );
	}

	/**
	 * release the cursor.
	 */
	public void close( )
	{
		if ( entry != null )
		{
			btree.unlockEntry( entry );
			entry = null;
		}
	}
}
