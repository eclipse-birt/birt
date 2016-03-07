/*******************************************************************************
 * Copyright (c) 2008,2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.btree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

/**
 * 
 * 
 * @param <K>
 * @param <V>
 */
public class BTree<K, V> implements BTreeConstants
{

	protected static Logger logger = Logger.getLogger( BTree.class.getName( ) );

	protected NodeFile file;
	protected boolean shareFile;

	private int version;
	private boolean allowDuplicate;
	private boolean allowNullKey;
	private int keySize;
	private boolean hasValue;
	private int valueSize;
	private int headNodeId;
	private int rootNodeId;
	private int freeNodeId;
	private int totalBlocks;
	private int totalLevels;
	private int totalKeys;
	private int totalValues;
	private int cacheSize;

	protected boolean readOnly;
	protected BTreeSerializer<K> keySerializer;
	protected BTreeSerializer<V> valueSerializer;
	protected Comparator<K> comparator;

	public BTree( ) throws IOException
	{
		this( new BTreeOption<K, V>( ) );
	}

	public BTree( BTreeOption<K, V> option ) throws IOException
	{
		if ( option.file != null )
		{
			this.shareFile = option.shareFile;
			if ( option.file instanceof NodeFile )
			{
				this.file = (NodeFile) option.file;
			}
			else
			{
				this.file = new ReusableBTreeFile( option.file );
			}
		}

		this.comparator = option.comparator;
		this.keySerializer = option.keySerializer;
		this.valueSerializer = option.valueSerializer;
		this.readOnly = option.readOnly;

		this.version = BTREE_VERSION_0;
		this.rootNodeId = -1;
		this.freeNodeId = -1;
		this.totalLevels = 0;
		this.totalKeys = 0;
		this.totalValues = 0;
		this.allowDuplicate = option.allowDuplicate;
		this.allowNullKey = option.allowNullKey;
		this.keySize = option.keySize;
		this.hasValue = option.hasValue;
		this.valueSize = option.valueSize;
		this.headNodeId = option.headNodeId;
		this.cacheSize = option.cacheSize;

		if ( file != null )
		{
			if ( file.getTotalBlock( ) > headNodeId )
			{
				byte[] bytes = new byte[BLOCK_SIZE];
				file.readBlock( headNodeId, bytes );
				DataInput input = new DataInputStream(
						new ByteArrayInputStream( bytes ) );
				readTreeHead( input );
			}
			else
			{
				ByteArrayOutputStream buffer = new ByteArrayOutputStream(
						BLOCK_SIZE );
				DataOutput output = new DataOutputStream( buffer );
				writeTreeHead( output );
				file.writeBlock( headNodeId, buffer.toByteArray( ) );
			}
			totalBlocks = file.getTotalBlock( );
		}
	}

	public void close( ) throws IOException
	{
		if ( file == null )
		{
			//has been closed
			return;
		}

		try
		{
			if ( !readOnly )
			{
				// write the header
				ByteArrayOutputStream buffer = new ByteArrayOutputStream(
						BLOCK_SIZE );
				DataOutput output = new DataOutputStream( buffer );
				writeTreeHead( output );
				file.writeBlock( headNodeId, buffer.toByteArray( ) );

				// flush the nodes
				for ( BTreeNode<K, V> node : nodeCaches.values( ) )
				{
					if ( node.isDirty( ) )
					{
						writeNode( node );
					}
				}
			}
			if ( !shareFile )
			{
				file.close( );
			}
		}
		finally
		{
			file = null;
		}
	}

	LeafEntry<K, V> getFirstEntry( ) throws IOException
	{
		int nodeId = rootNodeId;
		while ( nodeId != -1 )
		{
			BTreeNode<K, V> node = loadBTreeNode( nodeId );
			try
			{
				if ( node.getNodeType( ) == NODE_LEAF )
				{
					return ( (LeafNode<K, V>) node ).getFirstEntry( );
				}
				else
				{
					nodeId = ( (IndexNode<K, V>) node ).getFirstChild( );
				}
			}
			finally
			{
				node.unlock( );
			}
		}
		return null;
	}

	LeafEntry<K, V> getLastEntry( ) throws IOException
	{
		int nodeId = rootNodeId;
		while ( nodeId != -1 )
		{
			BTreeNode<K, V> node = loadBTreeNode( nodeId );
			try
			{
				if ( node.getNodeType( ) == NODE_LEAF )
				{
					return ( (LeafNode<K, V>) node ).getLastEntry( );
				}
				else
				{
					nodeId = ( (IndexNode<K, V>) node ).getLastChild( );
				}
				
			}
			finally
			{
				node.unlock( );
			}
		}
		return null;
	}

	protected LeafEntry<K, V> findEntry( K k ) throws IOException
	{
		if ( k == null && !allowNullKey )
		{
			throw new NullPointerException( "k can not be null" );
		}
		if ( rootNodeId != -1 )
		{
			BTreeValue<K> key = createKey( k );
			BTreeNode<K, V> root = loadBTreeNode( rootNodeId );
			try
			{
				int nodeType = root.getNodeType( );
				if ( nodeType == NODE_INDEX )
				{
					return ( (IndexNode<K, V>) root ).find( key );
				}
				else if ( nodeType == NODE_LEAF )
				{
					return ( (LeafNode<K, V>) root ).find( key );
				}
			}
			finally
			{
				root.unlock( );
			}
		}
		return null;
	}

	void removeEntry( LeafEntry<K, V> entry ) throws IOException
	{
		throw new UnsupportedOperationException( "setEntryValue" );
	}

	protected LeafEntry<K, V> insertEntry( K k, V v ) throws IOException
	{
		BTreeValue<K> key = createKey( k );
		@SuppressWarnings("unchecked")
		BTreeValue<V>[] values = (BTreeValue<V>[]) new BTreeValue[1];
		if ( hasValue( ) )
		{
			values[0] = createValue( v );
		}
		return insertEntry( key, values );
	}

	LeafEntry<K, V> insertEntry( K k, V[] vs ) throws IOException
	{
		if ( !allowNullKey && k == null )
		{
			throw new NullPointerException( "key can not be null" );
		}
		assert vs != null && vs.length > 0;
		BTreeValue<K> key = createKey( k );
		if ( !hasValue( ) || vs == null || vs.length == 0 )
		{
			@SuppressWarnings("unchecked")
			BTreeValue<V>[] values = (BTreeValue<V>[]) new BTreeValue[1];
			return insertEntry( key, values );
		}
		@SuppressWarnings("unchecked")
		BTreeValue<V>[] values = (BTreeValue<V>[]) new BTreeValue[vs.length];
		for ( int i = 0; i < values.length; i++ )
		{
			values[i] = createValue( vs[i] );
		}
		return insertEntry( key, values );
	}

	private LeafEntry<K, V> insertEntry( BTreeValue<K> key,
			BTreeValue<V>[] values ) throws IOException
	{
		if ( rootNodeId == -1 )
		{
			LeafNode<K, V> root = createLeafNode( );
			try
			{
				root.setPrevNodeId( -1 );
				root.setNextNodeId( -1 );
				rootNodeId = root.getNodeId( );
				totalLevels++;
				return root.insert( key, values );
			}
			finally
			{
				root.unlock( );
			}
		}
		else
		{
			BTreeNode<K, V> root = loadBTreeNode( rootNodeId );
			try
			{
				int nodeType = root.getNodeType( );
				if ( nodeType == NODE_INDEX )
				{
					IndexNode<K, V> indexNode = (IndexNode<K, V>) root;
					LeafEntry<K, V> insertEntry = indexNode
							.insert( key, values );
					if ( indexNode.needSplit( ) )
					{
						IndexEntry<K, V> splitEntry = indexNode.split( );
						if ( splitEntry != null )
						{
							insertIndex( splitEntry.getKey( ),
									splitEntry.getChildNodeId( ) );
						}
					}
					return insertEntry;
				}
				if ( nodeType == NODE_LEAF )
				{
					LeafNode<K, V> leafNode = (LeafNode<K, V>) root;
					LeafEntry<K, V> insertEntry = leafNode.insert( key, values );

					if ( leafNode.needSplit( ) )
					{
						IndexEntry<K, V> splitEntry = leafNode.split( );
						if ( splitEntry != null )
						{
							insertIndex( splitEntry.getKey( ),
									splitEntry.getChildNodeId( ) );
						}
					}
					return insertEntry;
				}

				throw new IOException( CoreMessages.getFormattedString(
						ResourceConstants.UNEXPECTED_NODE_TYPE,
						new Object[]{nodeType} ) );
			}
			finally
			{
				root.unlock( );
			}
		}
	}

	protected void insertIndex( BTreeValue<K> key, int childNodeId )
			throws IOException
	{
		IndexNode<K, V> newRoot = createIndexNode( );
		try
		{
			newRoot.setPrevNodeId( -1 );
			newRoot.setNextNodeId( -1 );
			newRoot.setFirstChild( rootNodeId );
			newRoot.insertIndex( key, childNodeId );
			rootNodeId = newRoot.getNodeId( );
			totalLevels++;
		}
		finally
		{
			newRoot.unlock( );
		}
	}

	public int getTotalKeys( )
	{
		return totalKeys;
	}

	public int getTotalValues( )
	{
		return totalValues;
	}

	public V getValue( K key ) throws IOException
	{
		if ( !hasValue( ) )
		{
			return null;
		}
		LeafEntry<K, V> entry = findEntry( key );
		if ( entry != null )
		{
			K entryKey = getKey( entry.getKey( ) );
			if ( comparator.compare( key, entryKey ) == 0 )
			{
				BTreeValues<V> values = entry.getValues( );
				BTreeValues.Value<V> value = values.getFirstValue( );
				return getValue( value.getValue( ) );
			}
		}
		return null;
	}

	public Collection<V> getValues( K key ) throws IOException
	{
		if ( !hasValue( ) )
		{
			return null;
		}
		LeafEntry<K, V> entry = findEntry( key );
		if ( entry != null )
		{
			K entryKey = getKey( entry.getKey( ) );
			if ( comparator.compare( key, entryKey ) == 0 )
			{
				BTreeValues<V> values = entry.getValues( );
				ArrayList<V> list = new ArrayList<V>( values.getValueCount( ) );
				BTreeValues.Value<V> value = values.getFirstValue( );
				while ( value != null )
				{
					list.add( getValue( value.getValue( ) ) );
					value = value.getNext( );
				}
				return list;
			}
		}
		return null;
	}

	public boolean exist( K key ) throws IOException
	{
		LeafEntry<K, V> entry = findEntry( key );
		if ( entry != null )
		{
			K entryKey = getKey( entry.getKey( ) );
			if ( comparator.compare( key, entryKey ) == 0 )
			{
				return true;
			}
		}
		return false;
	}

	public void insert( K k, V v ) throws IOException
	{
		if ( readOnly )
		{
			throw new IOException(
					CoreMessages.getString( ResourceConstants.READ_ONLY_TREE ) );
		}
		insertEntry( k, v );
	}

	public void insert( K k, V[] vs ) throws IOException
	{
		if ( readOnly )
		{
			throw new IOException(
					CoreMessages.getString( ResourceConstants.READ_ONLY_TREE ) );
		}
		insertEntry( k, vs );
	}

	public void remove( K key ) throws IOException
	{
		LeafEntry<K, V> entry = findEntry( key );
		if ( entry != null )
		{
			K entryKey = getKey( entry.getKey( ) );
			if ( comparator.compare( key, entryKey ) == 0 )
			{
				removeEntry( entry );
			}
		}
	}

	public BTreeCursor<K, V> createCursor( )
	{
		return new BTreeCursor<K, V>( this );
	}

	int compare( BTreeValue<K> k1, BTreeValue<K> k2 ) throws IOException
	{
		K key1 = getKey( k1 );
		K key2 = getKey( k2 );
		if ( key1 == key2 )
		{
			return 0;
		}
		if ( key1 == null )
		{
			return -1;
		}
		if ( key2 == null )
		{
			return 1;
		}
		return comparator.compare( key1, key2 );
	}

	// cache used by the btree
	private LinkedHashMap<Integer, BTreeNode<K, V>> nodeCaches = new LinkedHashMap<Integer, BTreeNode<K, V>>(
			8, 0.75f, true ) {

		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(
				Map.Entry<Integer, BTreeNode<K, V>> arg )
		{
			if ( file == null )
			{
				// we never remove the cache out if there is no file.
				return false;
			}
			if ( size( ) >= cacheSize )
			{
				BTreeNode<K, V> node = arg.getValue( );
				if ( node.isLocked( ) )
				{
					Iterator<Map.Entry<Integer, BTreeNode<K, V>>> iter = this
							.entrySet( ).iterator( );
					while ( iter.hasNext( ) )
					{
						Map.Entry<Integer, BTreeNode<K, V>> entry = iter.next( );
						BTreeNode<K, V> value = entry.getValue( );
						if ( !value.isLocked( ) )
						{
							// remove this node
							if ( value.isDirty( ) )
							{
								try
								{
									writeNode( value );
								}
								catch ( IOException ex )
								{
									logger.log(
											Level.WARNING,
											"failed to write node "
													+ value.getNodeId( )
													+ " type "
													+ value.getNodeType( ), ex );
									return false;
								}
							}
							remove( entry.getKey( ) );
							break;
						}
					}
					return false;
				}
				if ( node.isDirty( ) )
				{
					try
					{
						writeNode( node );
					}
					catch ( IOException ex )
					{
						logger.log( Level.WARNING,
								"failed to write node " + node.getNodeId( )
										+ " type " + node.getNodeType( ), ex );
						return false;
					}
				}
				return true;
			}
			return false;
		}
	};

	private void writeNode( BTreeNode<K, V> node ) throws IOException
	{
		NodeOutputStream out = new NodeOutputStream( file, node.getUsedBlocks( ) );
		try
		{
			DataOutput output = new DataOutputStream( out );
			output.writeInt( node.getNodeType( ) );
			node.write( output );
			node.setDirty( false );
		}
		finally
		{
			out.close( );
		}
	}

	synchronized BTreeNode<K, V> loadBTreeNode( int nodeId ) throws IOException
	{
		BTreeNode<K, V> node = nodeCaches.get( nodeId );
		if ( node != null )
		{
			node.lock( );
			return node;
		}

		if ( file == null )
		{
			throw new IOException( CoreMessages.getFormattedString(
					ResourceConstants.CANNOT_LOAD_NODE, new Object[]{nodeId} ) );
		}

		NodeInputStream in = new NodeInputStream( file, nodeId );
		try
		{
			DataInput input = new DataInputStream( in );
			int nodeType = input.readInt( );
			switch ( nodeType )
			{
				case NODE_INDEX :
					node = new IndexNode<K, V>( this, nodeId );
					break;
				case NODE_LEAF :
					node = new LeafNode<K, V>( this, nodeId );
					break;
				case NODE_VALUE :
					node = new ValueNode<K, V>( this, nodeId );
					break;
				default :
					throw new IOException( CoreMessages.getFormattedString(
							ResourceConstants.UNEXPECTED_NODE_TYPE,
							new Object[]{nodeType, nodeId} ) );
			}
			node.read( input );
			node.setUsedBlocks( in.getUsedBlocks( ) );
			node.setDirty( false );
			node.lock( );
			nodeCaches.put( Integer.valueOf( nodeId ), node );
			return node;
		}
		finally
		{
			in.close( );
		}
	}

	IndexNode<K, V> loadIndexNode( int nodeId ) throws IOException
	{
		BTreeNode<K, V> node = loadBTreeNode( nodeId );
		if ( node instanceof IndexNode )
		{
			IndexNode<K, V> indexNode = (IndexNode<K, V>) node;
			return indexNode;
		}
		node.unlock( );
		throw new IOException( CoreMessages.getFormattedString(
				ResourceConstants.UNEXPECTED_NODE_TYPE,
				new Object[]{node.getNodeType( ), node.getNodeId( )} ) );
	}

	LeafNode<K, V> loadLeafNode( int nodeId ) throws IOException
	{
		BTreeNode<K, V> node = loadBTreeNode( nodeId );
		if ( node instanceof LeafNode )
		{
			LeafNode<K, V> leafNode = (LeafNode<K, V>) node;
			return leafNode;
		}
		node.unlock( );
		throw new IOException( CoreMessages.getFormattedString(
				ResourceConstants.UNEXPECTED_NODE_TYPE,
				new Object[]{node.getNodeType( ), node.getNodeId( )} ) );
	}

	ValueNode<K, V> loadValueNode( int nodeId ) throws IOException
	{
		BTreeNode<K, V> node = loadBTreeNode( nodeId );
		if ( node instanceof ValueNode )
		{
			return (ValueNode<K, V>) node;
		}
		node.unlock( );
		throw new IOException( CoreMessages.getFormattedString(
				ResourceConstants.UNEXPECTED_NODE_TYPE,
				new Object[]{node.getNodeType( ), node.getNodeId( )} ) );
	}

	protected int allocBlock( ) throws IOException
	{
		totalBlocks++;
		if ( file != null )
		{
			return file.allocBlock( );
		}
		return totalBlocks;
	}

	protected void releaseBlock( int blockId ) throws IOException
	{
		if ( file != null )
		{
			file.freeBlock( blockId );
		}
	}

	public LeafNode<K, V> createLeafNode( ) throws IOException
	{
		int nodeId = allocBlock( );
		LeafNode<K, V> valueNode = new LeafNode<K, V>( this, nodeId );
		this.nodeCaches.put( nodeId, valueNode );
		valueNode.lock( );
		return valueNode;
	}

	public IndexNode<K, V> createIndexNode( ) throws IOException
	{
		int nodeId = allocBlock( );
		IndexNode<K, V> indexNode = new IndexNode<K, V>( this, nodeId );
		this.nodeCaches.put( nodeId, indexNode );
		indexNode.lock( );
		return indexNode;
	}

	public ValueNode<K, V> createValueNode( ) throws IOException
	{
		int nodeId = allocBlock( );
		ValueNode<K, V> valueNode = new ValueNode<K, V>( this, nodeId );
		this.nodeCaches.put( nodeId, valueNode );
		valueNode.lock( );
		return valueNode;
	}

	ExternalValueList<K, V> createExternalValueList( BTreeValues<V> values )
			throws IOException
	{
		ExternalValueList<K, V> list = new ExternalValueList<K, V>( this );
		BTreeValues.Value<V> value = values.getFirstValue( );
		while ( value != null )
		{
			list.append( value.getValue( ) );
			value = value.getNext( );
		}
		return list;
	}

	private final BTreeValue<K> NULL_KEY = new BTreeValue<K>( );

	BTreeValue<K> createKey( K key ) throws IOException
	{
		if ( key == null )
		{
			assert allowNullKey == true;
			return NULL_KEY;
		}
		byte[] keyBytes = keySerializer.getBytes( key );
		int keySize = getKeySize( );
		if ( keySize != 0 && keySize != keyBytes.length )
		{
			throw new IOException( CoreMessages.getFormattedString(
					ResourceConstants.KEY_SIZE_ERROR, new Object[]{
							keyBytes.length, keySize} ) );
		}
		return new BTreeValue<K>( key, keyBytes );
	}

	protected K getKey( BTreeValue<K> key ) throws IOException
	{
		if ( key == NULL_KEY )
		{
			return null;
		}
		K k = key.getValue( );
		if ( k != null )
		{
			return k;
		}
		byte[] keyBytes = key.getBytes( );
		if ( keyBytes != null )
		{
			try
			{
				k = keySerializer.getObject( keyBytes );
				key.setValue( k );
			}
			catch ( ClassNotFoundException ce )
			{
				throw new IOException( ce.getMessage( ) );
			}
		}
		return k;
	}

	int writeKey( DataOutput out, BTreeValue<K> key ) throws IOException
	{
		int size = 0;
		if ( allowNullKey )
		{
			if ( key == NULL_KEY )
			{
				out.writeBoolean( true );
				return 1;
			}
			out.writeBoolean( false );
			size = 1;
		}
		byte[] bytes = key.getBytes( );
		int keySize = getKeySize( );
		if ( keySize != 0 && keySize != bytes.length )
		{
			throw new IOException(
					CoreMessages
							.getString( ResourceConstants.MISMATCH_KEY_LENGTH ) );
		}
		if ( keySize == 0 )
		{
			out.writeInt( bytes.length );
			out.write( bytes );
			return size + 4 + bytes.length;
		}
		out.write( bytes );
		return size + bytes.length;
	}

	BTreeValue<K> readKey( DataInput in ) throws IOException
	{
		if ( allowNullKey )
		{
			boolean isNull = in.readBoolean( );
			if ( isNull )
			{
				return NULL_KEY;
			}
		}
		int keySize = getKeySize( );
		if ( keySize == 0 )
		{
			keySize = in.readInt( );
		}
		byte[] keyBytes = new byte[keySize];
		in.readFully( keyBytes );
		return new BTreeValue<K>( keyBytes );
	}

	protected V getValue( BTreeValue<V> value ) throws IOException
	{
		V v = value.getValue( );
		if ( v != null )
		{
			return v;
		}
		byte[] valueBytes = value.getBytes( );
		if ( valueBytes != null )
		{
			try
			{
				v = valueSerializer.getObject( valueBytes );
				value.setValue( v );
			}
			catch ( ClassNotFoundException ex )
			{
				throw new IOException( ex.getMessage( ) );
			}
		}
		return v;
	}

	private BTreeValue<V> createValue( V value ) throws IOException
	{
		byte[] valueBytes = valueSerializer.getBytes( value );
		int valueSize = getValueSize( );
		if ( valueSize != 0 && valueSize != valueBytes.length )
		{
			throw new IOException( CoreMessages.getFormattedString(
					ResourceConstants.Value_SIZE_ERROR, new Object[]{
							valueBytes.length, valueSize} ) );
		}
		return new BTreeValue<V>( value, valueBytes );

	}

	int writeValue( DataOutput out, BTreeValue<V> value ) throws IOException
	{
		byte[] bytes = value.getBytes( );
		if ( valueSize != 0 && valueSize != bytes.length )
		{
			throw new IOException(
					CoreMessages
							.getString( ResourceConstants.MISMATCH_VALUE_LENGTH ) );
		}
		if ( valueSize == 0 )
		{
			out.writeInt( bytes.length );
			out.write( bytes );
			return bytes.length + 4;
		}

		out.write( bytes );
		return valueSize;
	}

	BTreeValue<V> readValue( DataInput in ) throws IOException
	{
		int size = getValueSize( );
		if ( size == 0 )
		{
			size = in.readInt( );
		}
		byte[] bytes = new byte[size];
		in.readFully( bytes );
		return new BTreeValue<V>( bytes );
	}

	// opened cursor and entries, nodes, once a node is locked, we should never
	// remove it out from the key
	void lockEntry( LeafEntry<K, V> entry )
	{
		entry.getNode( ).lock( );
	}

	void unlockEntry( LeafEntry<K, V> entry )
	{
		entry.getNode( ).unlock( );
	}

	int getKeySize( )
	{
		return keySize;
	}

	int getValueSize( )
	{
		return valueSize;
	}

	int getKeySize( BTreeValue<K> key )
	{
		if ( allowNullKey )
		{
			if ( key == NULL_KEY )
			{
				return 1;
			}
			if ( keySize == 0 )
			{
				return 5 + key.getBytes( ).length;
			}
			return keySize + 1;
		}
		if ( keySize == 0 )
		{
			return 4 + key.getBytes( ).length;
		}
		return keySize;
	}

	int getValueSize( BTreeValue<V> value )
	{
		if ( valueSize == 0 )
		{
			return 4 + value.getBytes( ).length;
		}
		return valueSize;
	}

	boolean hasValue( )
	{
		return hasValue;
	}

	boolean allowDuplicate( )
	{
		return allowDuplicate;
	}

	int getRootNodeId( )
	{
		return rootNodeId;
	}

	protected class ReusableBTreeFile implements NodeFile
	{

		BTreeFile file;

		ReusableBTreeFile( BTreeFile file )
		{
			this.file = file;
			freeNodeId = -1;
		}

		public int getTotalBlock( ) throws IOException
		{
			return file.getTotalBlock( );
		}

		public int allocBlock( ) throws IOException
		{
			if ( freeNodeId != -1 )
			{
				int blockId = freeNodeId;
				byte[] bytes = new byte[4];
				file.readBlock( freeNodeId, bytes );
				freeNodeId = BTreeUtils.bytesToInteger( bytes );
				return blockId;
			}
			return file.allocBlock( );
		}

		public void freeBlock( int blockId ) throws IOException
		{
			byte[] bytes = new byte[8];
			BTreeUtils.integerToBytes( freeNodeId, bytes );
			file.writeBlock( blockId, bytes );
			freeNodeId = blockId;
		}

		public Object lock( ) throws IOException
		{
			return file.lock( );
		}

		public void readBlock( int blockId, byte[] bytes ) throws IOException
		{
			file.readBlock( blockId, bytes );
		}

		public void unlock( Object lock ) throws IOException
		{
			file.unlock( lock );
		}

		public void writeBlock( int blockId, byte[] bytes ) throws IOException
		{
			file.writeBlock( blockId, bytes );
		}

		public void close( ) throws IOException
		{
			file.close( );
		}
	}

	protected void readTreeHead( DataInput in ) throws IOException
	{
		long tag = in.readLong( );
		if ( tag != MAGIC_TAG )
		{
			throw new IOException( CoreMessages.getFormattedString(
					ResourceConstants.INVALID_MAGIC_TAG,
					new Object[]{Long.toHexString( tag )} ) );
		}
		version = in.readInt( );
		if ( version != BTREE_VERSION_0 )
		{
			throw new IOException( CoreMessages.getFormattedString(
					ResourceConstants.UNSUPPORTED_VERSION,
					new Object[]{version} ) );
		}
		readV0( in );
	}

	private void readV0( DataInput in ) throws IOException
	{
		allowDuplicate = in.readBoolean( );
		keySize = in.readInt( );
		hasValue = in.readBoolean( );
		valueSize = in.readInt( );
		rootNodeId = in.readInt( );
		freeNodeId = in.readInt( );
		totalLevels = in.readInt( );
		totalKeys = in.readInt( );
		totalValues = in.readInt( );
		allowNullKey = in.readBoolean( );
	}

	protected void writeTreeHead( DataOutput out ) throws IOException
	{
		out.writeLong( MAGIC_TAG );
		out.writeInt( BTREE_VERSION_0 );
		out.writeBoolean( allowDuplicate );
		out.writeInt( keySize );
		out.writeBoolean( hasValue );
		out.writeInt( valueSize );
		out.writeInt( rootNodeId );
		out.writeInt( freeNodeId );
		out.writeInt( totalLevels );
		out.writeInt( totalKeys );
		out.writeInt( totalValues );
		out.writeBoolean( allowNullKey );
	}

	void increaseTotalKeys( )
	{
		totalKeys++;
	}

	void increaseTotalValues( int count )
	{
		totalValues += count;
	}

	public void dump( ) throws IOException
	{
		System.out.println( "BTREE:" + rootNodeId );
		System.out.println( "keySize:" + keySize );
		System.out.println( "hasValue:" + hasValue );
		System.out.println( "allowDuplicate:" + allowDuplicate );
		System.out.println( "valueSize:" + valueSize );
		System.out.println( "rootNodeId" );
		System.out.println( "freeNodeId" );
		System.out.println( "totalLevles:" + totalLevels );
		System.out.println( "totalKeys:" + totalKeys );
		System.out.println( "totalValues:" + totalValues );
	}

	public void dumpAll( ) throws IOException
	{
		dump( );
		if ( rootNodeId != -1 )
		{
			BTreeNode<K, V> rootNode = loadBTreeNode( rootNodeId );
			try
			{
				rootNode.dumpAll( );
			}
			finally
			{
				rootNode.unlock( );
			}
		}
	}
}