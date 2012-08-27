package org.eclipse.birt.data.engine.impl.index;

import it.uniroma3.mat.extendedset.intset.ConciseSet;
import it.uniroma3.mat.extendedset.intset.IntSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.btree.BTree;
import org.eclipse.birt.core.btree.BTreeCursor;
import org.eclipse.birt.core.btree.BTreeFile;
import org.eclipse.birt.core.btree.BTreeOption;
import org.eclipse.birt.core.btree.BTreeSerializer;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.SizeOfUtil;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

public class BTreeIndex implements IIndexSerializer, IDataSetIndex
{
	private BTree<Object, Object> btree = null;
	private KeyIndexHolder keyIndexHolder = null;
	private BTreeSerializer serializer = null;
	private Class keyDataType = null;
	
	private final int BTREE_CACHE_SIZE = 200;
	private ArchiveInputFile inputFile = null;
	private long memorySize;
	private int version;
	
	public BTreeIndex( long memoryBufferSize, String indexName, StreamManager manager, Class keyDataType ) throws DataException
	{
		serializer = BTreeSerializerUtil.createSerializer( keyDataType );
		try
		{
			btree = createBTree( new ArchiveOutputFile( manager.getDocWriter( ), manager.getOutStreamName( indexName ) ), BTREE_CACHE_SIZE, serializer );
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		this.keyDataType = keyDataType;
		
		this.memorySize = memoryBufferSize;
	}

	public BTreeIndex( String indexName, IDocArchiveReader reader, Class keyDataType, ClassLoader classLoader, int version ) throws DataException
	{
		serializer = BTreeSerializerUtil.createSerializer( keyDataType );
		if( serializer instanceof JavaSerializer )
		{
			( ( JavaSerializer )serializer ).setClassLoader( classLoader );
		}
		try
		{
			inputFile = new ArchiveInputFile( reader, indexName );
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		this.keyDataType = keyDataType;
		this.version = version;
	}
	
	
	private static BTree<Object, Object> createBTree( ArchiveOutputFile file, int cacheSize, BTreeSerializer serializer ) throws DataException
	{
		BTreeOption option = new BTreeOption( );
		option.setKeySerializer( serializer );
		option.setCacheSize( cacheSize );
		option.setHasValue( true );
		option.setAllowDuplicate( true );
		option.setAllowNullKey( true );
		option.setReadOnly( false );
		ConciseSerializer js = new ConciseSerializer( );
		option.setValueSerializer( js );

		option.setFile( file );
		
		try
		{
			return new BTree<Object, Object>( option);
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
	}
	
	private BTree<Object, Object> createBTree( ArchiveInputFile file, int cacheSize, BTreeSerializer serializer ) throws DataException
	{
		BTreeOption option = new BTreeOption( );
		option.setKeySerializer( serializer );
		option.setCacheSize( cacheSize );
		option.setHasValue( true );
		option.setAllowDuplicate( true );
		option.setAllowNullKey( true );
		option.setReadOnly( true );
		
		if( this.version >= VersionManager.VERSION_4_2_0 )
		{
			ConciseSerializer js = new ConciseSerializer();
			option.setValueSerializer( js );
		}
		else
		{
			option.setValueSerializer( new IntegerSerializer() );
		}
		

		option.setFile( file );
		
		try
		{
			return new BTree<Object, Object>( option);
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
	}
	
	public void close() throws DataException
	{
		try
		{
			if( this.keyIndexHolder != null )
			{
				insertToBTree( );
			}
			if( btree != null )
				btree.close( );
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
	}
	
	static private boolean equals( Object o1, Object o2 )
	{
		if( o1 == null && o2 == null )
			return true;
		if( o1 == null || o2 == null )
			return false;
		return o1.equals( o2 );
	}
	
	private void insertToBTree( ) throws DataException
	{
		try
		{
			ConciseSet rowIDList = new ConciseSet( );
			Iterator<KeyRowIDSet> it = this.keyIndexHolder.iterate( );
			boolean isFirst = true;
			Object lastKey = null;
			while( it.hasNext( ) )
			{
				KeyRowIDSet current = it.next( );
				if( isFirst )
				{
					lastKey = current.key;
					rowIDList.addAll( new ConciseSet(current.getRowID( )) );
					isFirst = false;
				}
				else if( equals( lastKey, current.key )  )
				{
					rowIDList.addAll( new ConciseSet(current.getRowID( )) );
				}
				else
				{
					btree.insert( lastKey, rowIDList );
					lastKey = current.key;
					rowIDList.clear( );
					rowIDList.addAll( new ConciseSet( current.getRowID( )) );
				}
			}
			
			if( rowIDList.size( ) > 0 )
			{
				btree.insert( lastKey, rowIDList );
			}
			this.keyIndexHolder.close( );
			this.keyIndexHolder = null;
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e ); 
		}
	}
	
	
	
	public Object put(Object o1, Object o2) throws DataException
	{
		try
		{
			if( this.keyIndexHolder == null )
				this.keyIndexHolder = new KeyIndexHolder( this.memorySize );
			this.keyIndexHolder.add( (Comparable)o1, (Integer)o2 );
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		return null;
	}

	public IntSet getKeyIndex( Object key, int filterType )	throws DataException
	{
		synchronized( this )
		{
			if( btree == null )
			{
				btree = createBTree( inputFile, BTREE_CACHE_SIZE, serializer );
			}			
		}
		
		if( keyIndexHolder != null )
		{
			insertToBTree( );
			try
			{
				this.keyIndexHolder.close( );
			}
			catch (IOException e)
			{
				throw new DataException( e.getLocalizedMessage( ), e );
			}
			this.keyIndexHolder = null;
		}
		
		if ( filterType != IConditionalExpression.OP_EQ
				&& filterType != IConditionalExpression.OP_IN
				&& filterType != IConditionalExpression.OP_GE
				&& filterType != IConditionalExpression.OP_GT
				&& filterType != IConditionalExpression.OP_LE
				&& filterType != IConditionalExpression.OP_LT
				&& filterType != IConditionalExpression.OP_BETWEEN )
		{
			throw new UnsupportedOperationException( );
		}
		if ( filterType == IConditionalExpression.OP_EQ )
			return getKeyIndex( key );
		else if ( filterType == IConditionalExpression.OP_IN )
		{
			List candidate = (List) key;
			List<IntSet> values = new ArrayList<IntSet>();
			for ( int i = 0; i < candidate.size(); i++ )
			{
				IntSet tempSet = getKeyIndex( candidate.get(i) ) ;
				if( tempSet!= null )
				{
					values.add( tempSet );
				}
			}
			
			return values.size( ) == 0?new ConciseSet():new OrIntSetImpl( values.toArray( new IntSet[0] ) );
		}
		else if ( filterType == IConditionalExpression.OP_GE )
		{
			return getGreater( key, true );
		}
		else if ( filterType == IConditionalExpression.OP_GT )
		{
			return getGreater( key, false );
		}
		else if ( filterType == IConditionalExpression.OP_LE )
		{
			return getLess( key, true );
		}
		else if ( filterType == IConditionalExpression.OP_LT )
		{
			return getLess( key, false );
		}
		else if ( filterType == IConditionalExpression.OP_BETWEEN )
		{
			List candidate = (List) key;
			return getBetween( candidate.get( 0 ), candidate.get( 1 ) );
		}
		else
			return new ConciseSet();
	}
	
	private IntSet getBetween( Object key1, Object key2 ) throws DataException
	{
		Object min, max;
		if ( ScriptEvalUtil.compare( key1, key2 ) <= 0 )
		{
			min = key1;
			max = key2;
		}
		else
		{
			min = key2;
			max = key1;
		}
		try
		{
			min = DataTypeUtil.convert( min, this.keyDataType );
			max = DataTypeUtil.convert( max, this.keyDataType );
		}
		catch ( BirtException e1 )
		{
			throw DataException.wrap( e1 );
		}
		BTreeCursor bCursor = btree.createCursor( );
		List<IntSet> temp = new ArrayList<IntSet>( );
		try
		{
			if( !bCursor.first( ) )
				return new ConciseSet();
			if( ScriptEvalUtil.compare( bCursor.getKey( ), min ) <= 0 )
			{
				bCursor.moveTo( min );
				if( ( (Comparable)bCursor.getKey( ) ).compareTo( max ) > 0 )
					return new ConciseSet();
				if( ( (Comparable)bCursor.getKey( ) ).compareTo( min ) >= 0 )
				{
					IntSet tempSet = getAll(bCursor.getValues( ));
					if( tempSet!= null )
						temp.add( tempSet );
				}
			}
			else
			{
				bCursor.beforeFirst( );
			}
			while( bCursor.next( ) )
			{
				if( ( (Comparable)bCursor.getKey( ) ).compareTo( max ) > 0 )
					return new OrIntSetImpl( temp.toArray( new IntSet[0]));
				
				IntSet tempSet = getAll(bCursor.getValues( ));
				if( tempSet!= null )
					temp.add( tempSet );
			}
		}
		catch (IOException e) 
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		finally
		{
			bCursor.close( );
		}
		return temp.size( ) == 0? new ConciseSet():new OrIntSetImpl( temp.toArray( new IntSet[0]));
	}
	
	private IntSet getGreater( Object key, boolean includeKey ) throws DataException
	{
		try
		{
			key = DataTypeUtil.convert( key, this.keyDataType );
		}
		catch ( BirtException e1 )
		{
			throw DataException.wrap( e1 );
		}
		BTreeCursor bCursor = btree.createCursor( );
		List<IntSet> temp = new ArrayList<IntSet>();
		try
		{
			if( !bCursor.first( ) )
				return new ConciseSet();
			if( bCursor.getKey( ) != null && ( (Comparable)bCursor.getKey( ) ).compareTo( key ) > 0 )
			{
				bCursor.beforeFirst( );
			}
			else
			{
				bCursor.moveTo( key );
				int cr = ( (Comparable)bCursor.getKey( ) ).compareTo( key );
				if( ( includeKey && cr == 0 ) || cr > 0 )
				{
					IntSet tempSet = getAll(bCursor.getValues( ));
					if( tempSet!= null )
						temp.add( tempSet );
				}
			}
			while( bCursor.next( ) )
			{
				IntSet tempSet = getAll(bCursor.getValues( ));
				if( tempSet!= null )
					temp.add( tempSet );
			}
		}
		catch (IOException e) 
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		finally
		{
			bCursor.close( );
		}
		return temp.size( ) == 0? new ConciseSet():new OrIntSetImpl( temp.toArray( new IntSet[0]));
	}
	
	private IntSet getAll( Collection c )
	{
		List<IntSet> temp = new ArrayList<IntSet>();

		if ( this.version >= VersionManager.VERSION_4_2_0 )
		{
			for ( Object o : c )
			{
				IntSet conciseEntry = (IntSet) o;
				temp.add( conciseEntry );
			}
		}
		else
		{
			IntSet conciseEntry = new ConciseSet();
			for( Object o : c )
			{
				conciseEntry.add( (Integer )o);
			}
			temp.add( conciseEntry );

		}
		if( temp.size() == 0 )
			return null;
		if( temp.size() == 1 )
			return temp.get(0);
		return new OrIntSetImpl( temp.toArray( new IntSet[0]));
	}
	
	private IntSet getLess( Object key, boolean includeKey ) throws DataException
	{
		try
		{
			key = DataTypeUtil.convert( key, this.keyDataType );
		}
		catch ( BirtException e1 )
		{
			throw DataException.wrap( e1 );
		}
		BTreeCursor bCursor = btree.createCursor( );
		List<IntSet> temp = new ArrayList<IntSet>( );
		try
		{
			while( bCursor.next( ) )
			{
				int cr = 0;
				if( bCursor.getKey( ) == null )
				{
					cr = -1;
				}
				else
				{
					cr = ( (Comparable)bCursor.getKey( ) ).compareTo( key );
				}
				if( cr < 0 ||( cr == 0 && includeKey ) )
				{
					IntSet tempSet = getAll(bCursor.getValues( ));
					if( tempSet!= null )
						temp.add( tempSet );
				}
				else
					return new OrIntSetImpl( temp.toArray( new IntSet[0]));
			}
		}
		catch (IOException e) 
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		finally
		{
			bCursor.close( );
		}
		return temp.size( ) == 0? new ConciseSet(): new OrIntSetImpl( temp.toArray( new IntSet[0]));
	}
	
	private IntSet getKeyIndex( Object key ) throws DataException
	{
		List<IntSet> temp = new ArrayList<IntSet>();
		try
		{
			key = DataTypeUtil.convert( key, this.keyDataType );
		}
		catch ( BirtException e1 )
		{
			throw DataException.wrap( e1 );
		}
		try
		{
			Collection<Object> values = btree.getValues( key );
			if ( values != null )
			{
				if( this.version >= VersionManager.VERSION_4_2_0 )
				{
					for ( Object o : values )
					{
						IntSet conciseEntry = (IntSet) o;
						temp.add( conciseEntry );
					}
				}
				else
				{
					IntSet conciseEntry = new ConciseSet();
					for( Object o: values )
					{
						conciseEntry.add( (Integer)o );
					}
					temp.add( conciseEntry );
				}
			}
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		return temp.size( ) > 0? new OrIntSetImpl( temp.toArray( new IntSet[0])): null;
	}

	public boolean supportFilter(int filterType) throws DataException
	{
		if ( filterType != IConditionalExpression.OP_EQ
				&& filterType != IConditionalExpression.OP_IN
				&& filterType != IConditionalExpression.OP_GE
				&& filterType != IConditionalExpression.OP_GT
				&& filterType != IConditionalExpression.OP_LE
				&& filterType != IConditionalExpression.OP_LT
				&& filterType != IConditionalExpression.OP_BETWEEN )
		{
			return false;
		}
		return true;
	}

	public Object[] getAllKeyValues() throws DataException
	{
		if( btree == null )
		{
			btree = createBTree( inputFile, BTREE_CACHE_SIZE, serializer );
		}
		BTreeCursor<Object, Object> bCursor = btree.createCursor( );
		List key = new ArrayList( );
		try
		{
			while( bCursor.next( ) )
			{
				key.add( bCursor.getKey( ) );
			}
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e ); 
		}
		finally
		{
			bCursor.close( );
		}
		return key.toArray( );
	}

	public IntSet getAllKeyRows() throws DataException
	{
		if( btree == null )
		{
			btree = createBTree( inputFile, BTREE_CACHE_SIZE, serializer );
		}
		BTreeCursor<Object, Object> bCursor = btree.createCursor( );
		IntSet conciseSet = new ConciseSet();
		List<Integer> keyRow = new ArrayList<Integer>( );
		try
		{
			while( bCursor.next( ) )
			{
				conciseSet.add( (Integer)bCursor.getValue( ) );
			}
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e ); 
		}
		finally
		{
			bCursor.close( );
		}
		
		return conciseSet;
	}
/*	TODO: In future in case of necessary use delt compression for the BTree node that not get best of Concise compression.
	private void insertToCompressedBTree( ) throws DataException
	{
		try
		{
			List<byte[]> rowIDByteList = new ArrayList<byte[]>( );
			KeyRowID keyRowID = ( KeyRowID ) sortedKeyRowID.pop( );
			boolean isFirst = true;
			Object lastKey = null;
			int lastValue = 0 ;
			int totalBytesCount = 0 ;
			while( keyRowID != null )
			{
				if( isFirst )
				{
					lastKey = keyRowID.key;
					byte[] valueBytes = BTreeUtil.getIncrementBytes( keyRowID.rowID, lastValue );
					rowIDByteList.add( valueBytes );
					lastValue = keyRowID.rowID;
					totalBytesCount += valueBytes.length;
					isFirst = false;
				}
				else if( equals( lastKey, keyRowID.key )  )
				{
					byte[] valueBytes = BTreeUtil.getIncrementBytes( keyRowID.rowID, lastValue );
					lastValue = keyRowID.rowID;
					rowIDByteList.add( valueBytes );
					totalBytesCount += valueBytes.length;
				}
				else
				{
					byte[] resultBytes = new byte[totalBytesCount];
					concatByteArrays(resultBytes,rowIDByteList);
					compressedBtree.insert( lastKey, resultBytes );
					lastKey = keyRowID.key;
					
					byte[] valueBytes = BTreeUtil.getIncrementBytes( keyRowID.rowID, 0 );		
					lastValue = keyRowID.rowID;
					rowIDByteList.clear( );
					rowIDByteList.add( valueBytes );
					totalBytesCount = valueBytes.length;
				}
				keyRowID = ( KeyRowID ) sortedKeyRowID.pop( );
			}
			if( rowIDByteList.size( ) > 0 )
			{
				byte[] resultBytes = new byte[totalBytesCount];
				concatByteArrays(resultBytes,rowIDByteList);
				compressedBtree.insert( lastKey, resultBytes );
			}
			sortedKeyRowID.close( );
			sortedKeyRowID = null;
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e ); 
		}
	}
	
	private void concatByteArrays( byte[] source, List<byte[]> addedBytes )
	{
		int pos = 0;
		for( int i = 0 ; i < addedBytes.size( ) ; i++ )
		{
			System.arraycopy( addedBytes.get( i ), 0, source, pos, addedBytes.get( i ).length );
			pos += addedBytes.get( i ).length;
		}
	}
	
	private EWAHCompressedBitmap transformToIntegers( byte[] obj )
	{
		int pos= 0;
		byte[] bytes = new byte[4];
	
		int currentValue = 0;
		int resultInt = 0;
		EWAHCompressedBitmap set = new EWAHCompressedBitmap();
		
		while(pos<obj.length)
		{
			byte b = obj[pos];
			pos++;
			
			if (b < 0) 
			{
				byte[] compressedBytes = new byte[4];
				int size=0;
				while (b < 0) 
				{
					b &= 0x7F;
					compressedBytes[size] = b;
					b = obj[pos];
					pos++;
					size++;
				}
				compressedBytes[size] = b;
							
				resultInt = computeInt(compressedBytes,size) + currentValue;
			} 
			else
			{
				bytes[3] = b;
				resultInt = BTreeUtil.bytesToInteger( bytes ) + currentValue;
			}

			set.set( resultInt );
			currentValue = resultInt;
		}
		
		return set;
	}
	
	public static int computeInt ( byte[] b,int size)
	{
		int LeftMoveCount = 9;
		int rightBits = 0;

		int result = 0;
		for ( int i = size; i >= 0; i-- )
		{
			byte newByte = b[i];
			switch ( size - i )
			{
				case 0 :
					rightBits = 0x00;
					break;
				case 1 :
					rightBits = 0x01;
					LeftMoveCount = 7;
					break;
				case 2 :
					rightBits = 0x03;
					LeftMoveCount = 6;
					break;
				case 3 :
					rightBits = 0x07;
					LeftMoveCount = 5;
					break;
			}
			newByte &= rightBits;
			if ( size == i )
				result += b[i];
			else
				result = result
						+ ( ( ( newByte << LeftMoveCount ) ) << ( size
								- i - 1 ) * 8 )
						+ ( ( ( b[i] >> ( 8 - LeftMoveCount ) ) ) << ( size - i ) * 8 );
		}

		return result;
	}
	*/

}

class ArchiveInputFile implements BTreeFile
{

	IDocArchiveReader archive;
	String name;
	RAInputStream input;

	ArchiveInputFile( IDocArchiveReader archive, String name )
			throws IOException
	{
		this.archive = archive;
		this.name = name;
		this.input = archive.getInputStream( name );
	}

	public int allocBlock( ) throws IOException
	{
		throw new IOException( "read only stream" );
	}

	public int getTotalBlock( ) throws IOException
	{
		return (int) ( ( input.length( ) + BLOCK_SIZE - 1 ) / BLOCK_SIZE );
	}

	public Object lock( ) throws IOException
	{
		return archive.lock( name );
	}

	public void readBlock( int blockId, byte[] bytes ) throws IOException
	{
		input.seek( ( long ) blockId * BLOCK_SIZE );
		input.read( bytes );
	}

	public void unlock( Object lock ) throws IOException
	{
		archive.unlock( lock );
	}

	public void writeBlock( int blockId, byte[] bytes ) throws IOException
	{
		throw new IOException( "read only stream" );
	}

	public void close( )
	{

	}
}

class ArchiveOutputFile implements BTreeFile
{

	IDocArchiveWriter archive;
	String name;
	RAOutputStream output;
	RAInputStream input;
	int totalBlock;

	ArchiveOutputFile( IDocArchiveWriter archive, String name )
			throws IOException
	{
		this.archive = archive;
		this.name = name;
		if( archive.exists( name ) )
		{
			input = archive.getInputStream( name );
			output = archive.getOutputStream( name );
		}
		else
		{
			output = archive.createOutputStream( name );
			input = archive.getInputStream( name );
		}
		totalBlock = (int) ( ( input.length( ) + BLOCK_SIZE - 1 ) / BLOCK_SIZE );
	}

	public void close( ) throws IOException
	{
		if ( output != null )
		{
			output.close( );
		}
		if ( input != null )
		{
			input.close( );
		}
	}

	public int allocBlock( ) throws IOException
	{
		return totalBlock++;
	}

	public int getTotalBlock( ) throws IOException
	{
		return totalBlock;
	}

	public Object lock( ) throws IOException
	{
		return archive.lock( name );
	}

	public void readBlock( int blockId, byte[] bytes ) throws IOException
	{
		input.refresh( );
		input.seek( ( long ) blockId * BLOCK_SIZE );
		input.read( bytes );
	}

	public void unlock( Object lock ) throws IOException
	{
		archive.unlock( lock );
	}

	public void writeBlock( int blockId, byte[] bytes ) throws IOException
	{
		if ( blockId >= totalBlock )
		{
			totalBlock = blockId + 1;
		}
		output.seek( ( long ) blockId * BLOCK_SIZE );
		output.write( bytes );
		output.flush( );
	}
	
}

class KeyIndexHolder
{
	private long memorySize;
	private long currentSize;
	private HashMap<Comparable, ConciseSet> keyValueInMemoryHolder;
	private DiskSortedStack sortedKeyRowSet;
	
	public KeyIndexHolder( long memorySize )
	{
		this.keyValueInMemoryHolder = new HashMap<Comparable, ConciseSet>();
		this.memorySize = memorySize <= 0? Integer.MAX_VALUE: memorySize;

		//TODO enhance me here
		this.sortedKeyRowSet = new DiskSortedStack( 1000, true, false, new KeyRowIDSetCreator() );
	}
	
	public void add( Comparable key, int value ) throws IOException
	{
		ConciseSet set = this.keyValueInMemoryHolder.get( key );
		if( set!= null )
		{
			int previousSize = set.getLastWordIndex( );
			set.add( value );
			//SizeDifference might be a negative number as add value to concise set might decrease its size.
			int sizeDifference = set.getLastWordIndex( ) - previousSize;
			
			this.currentSize += 4*sizeDifference;
		}
		else
		{
			set = new ConciseSet();
			set.add( value );
			this.keyValueInMemoryHolder.put( key, set );
			if( key!= null )
			{
				this.currentSize += SizeOfUtil.sizeOf( key.getClass( ), key );
			}
			this.currentSize += 4*set.getLastWordIndex( );
		}
		
		if( this.currentSize > this.memorySize )
		{
			pushToDisk( );
		}
	
	}

	private void pushToDisk( ) throws IOException
	{
		for( Object o : getSortedKeys( ) )
		{
			ConciseSet conciseSet = this.keyValueInMemoryHolder.get( o );
			this.sortedKeyRowSet.push( new KeyRowIDSet( o, BTreeSerializerUtil.intArrayToByteArray( conciseSet.getWords( )) ) );
			this.currentSize = 0;
		}
		
		this.keyValueInMemoryHolder.clear( );
	}
	
	public void close () throws IOException 
	{
		this.sortedKeyRowSet.close( );
	}
	
	public Iterator<KeyRowIDSet> iterate( ) throws IOException
	{
		if ( this.sortedKeyRowSet.size( ) > 0 )
		{
			this.pushToDisk( );

			return new Iterator<KeyRowIDSet>( ) {
				private int remainingCount = sortedKeyRowSet.size( );

				@Override
				public boolean hasNext( )
				{
					return this.remainingCount > 0;
					
				}

				@Override
				public KeyRowIDSet next( )
				{
					try
					{
						this.remainingCount--;
						return (KeyRowIDSet) sortedKeyRowSet.pop( );
					}
					catch ( IOException e )
					{
						throw new RuntimeException( );
					}
				}

				@Override
				public void remove( )
				{
					throw new UnsupportedOperationException( );

				}
			};
		}
		else
		{
			final List keys = getSortedKeys( );
			return new Iterator<KeyRowIDSet>(){
				private int currentIndex = -1;
				@Override
				public boolean hasNext( )
				{
					if( currentIndex < keys.size( )-1)
					{
						return true;
					}
					return false;
				}

				@Override
				public KeyRowIDSet next( )
				{
					this.currentIndex ++;
					Object key = keys.get( this.currentIndex );
					int[] words = keyValueInMemoryHolder.get( key ).getWords( );
					return new KeyRowIDSet( key, BTreeSerializerUtil.intArrayToByteArray( words ));
				}

				

				@Override
				public void remove( )
				{
					throw new UnsupportedOperationException( );
				}
				
				
			};
		}
	}

	private List getSortedKeys( )
	{
		List keys = new ArrayList();
		keys.addAll( this.keyValueInMemoryHolder.keySet( ) );
		if( keys.contains( null ))
		{
			keys.remove( null );
			Collections.sort( keys );
			keys.add( null );
		}
		else
		{
			Collections.sort( keys );
		}
		
		return keys;
	}
}

class KeyRowIDSetCreator implements IStructureCreator
{

	public IStructure createInstance( Object[] fields )
	{
		return new KeyRowIDSet( fields[0], (byte[]) fields[1] );
	}
}


class KeyRowIDSet implements IComparableStructure
{
	Object key;
	private byte[] rowID;
	
	KeyRowIDSet( Object key, byte[] rowID )
	{
		this.key = key;
		
		this.rowID = rowID;		
	}
	
	public Object[] getFieldValues( )
	{
		Object[] objectArrays = new Object[2];
		objectArrays[0] = key;
		objectArrays[1] = rowID;
		return objectArrays;
	}

	public int[] getRowID( )
	{
		return BTreeSerializerUtil.byteArrayToIntArray( rowID );
	}
	public int compareTo( Object o )
	{
		if( key == null )
		{
			return -1;
		}
		if( ( ( KeyRowIDSet )o ).key == null )
			return 1;
		return ( ( Comparable)key ).compareTo( ( ( KeyRowIDSet )o ).key );
	}
	
	/**
	 * 
	 * @return
	 */
	public static IStructureCreator getCreator( )
	{
		return new KeyRowIDSetCreator( );
	}
	
}

