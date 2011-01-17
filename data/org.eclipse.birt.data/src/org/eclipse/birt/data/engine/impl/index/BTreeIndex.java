package org.eclipse.birt.data.engine.impl.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

public class BTreeIndex implements IIndexSerializer, IDataSetIndex
{
	private BTree<Object, Integer> btree = null;
	private DiskSortedStack sortedKeyRowID = null;
	private BTreeSerializer serializer = null;
	private Class keyDataType = null;
	private long memoryBufferSize = 0;
	private final int BTREE_CACHE_SIZE = 200;
	private ArchiveInputFile inputFile = null;
	
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
		this.memoryBufferSize = memoryBufferSize;
	}

	public BTreeIndex( String indexName, IDocArchiveReader reader, Class keyDataType, ClassLoader classLoader ) throws DataException
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
	}
	
	
	private static BTree<Object, Integer> createBTree( ArchiveOutputFile file, int cacheSize, BTreeSerializer serializer ) throws DataException
	{
		BTreeOption<Object, Integer> option = new BTreeOption<Object, Integer>( );
		option.setKeySerializer( serializer );
		option.setCacheSize( cacheSize );
		option.setHasValue( true );
		option.setAllowDuplicate( true );
		option.setAllowNullKey( true );
		option.setReadOnly( false );
		option.setValueSerializer( new IntegerSerializer( ) );

		option.setFile( file );
		
		try
		{
			return new BTree<Object, Integer>( option);
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
	}
	
	private static BTree<Object, Integer> createBTree( ArchiveInputFile file, int cacheSize, BTreeSerializer serializer ) throws DataException
	{
		BTreeOption<Object, Integer> option = new BTreeOption<Object, Integer>( );
		option.setKeySerializer( serializer );
		option.setCacheSize( cacheSize );
		option.setHasValue( true );
		option.setAllowDuplicate( true );
		option.setAllowNullKey( true );
		option.setReadOnly( true );
		option.setValueSerializer( new IntegerSerializer( ) );

		option.setFile( file );
		
		try
		{
			return new BTree<Object, Integer>( option);
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
			if( sortedKeyRowID != null )
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
			List<Integer> rowIDList = new ArrayList<Integer>( );
			KeyRowID keyRowID = ( KeyRowID ) sortedKeyRowID.pop( );
			boolean isFirst = true;
			Object lastKey = null;
			while( keyRowID != null )
			{
				if( isFirst )
				{
					lastKey = keyRowID.key;
					rowIDList.add( (Integer) keyRowID.rowID );
					isFirst = false;
				}
				else if( equals( lastKey, keyRowID.key ) )
				{
					rowIDList.add( (Integer) keyRowID.rowID );
				}
				else
				{
					btree.insert( lastKey, rowIDList.toArray( new Integer[0]) );
					lastKey = keyRowID.key;
					rowIDList.clear( );
					rowIDList.add( (Integer) keyRowID.rowID );
				}
				keyRowID = ( KeyRowID ) sortedKeyRowID.pop( );
			}
			if( rowIDList.size( ) > 0 )
			{
				btree.insert( lastKey, rowIDList.toArray( new Integer[0]) );
			}
			sortedKeyRowID.close( );
			sortedKeyRowID = null;
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e ); 
		}
	}
	
	public Object put(Object o1, Object o2) throws DataException
	{
		if( sortedKeyRowID == null )
		{
			int cacheSize = 10000;
			if( memoryBufferSize != 0 )
			{
				cacheSize = (int) ( memoryBufferSize / ( SizeOfUtil.sizeOf( DataType.getDataType( this.keyDataType ) ) + 16 + 4 ));
				sortedKeyRowID = new DiskSortedStack( cacheSize, false, false, KeyRowID.getCreator( ) );
			}
			else
			{
				sortedKeyRowID = new DiskSortedStack( cacheSize, false, false, KeyRowID.getCreator( ) );
				sortedKeyRowID.setUseMemoryOnly( true );
			}
			
		}
		try
		{
			sortedKeyRowID.push( new KeyRowID( o1, (Integer) o2 ) );
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e ); 
		}
		return null;
	}

	public Set<Integer> getKeyIndex( Object key, int filterType )	throws DataException
	{
		synchronized( this )
		{
			if( btree == null )
			{
				btree = createBTree( inputFile, BTREE_CACHE_SIZE, serializer );
			}			
		}
		
		if( sortedKeyRowID != null )
		{
			insertToBTree( );
			try
			{
				sortedKeyRowID.close( );
			}
			catch (IOException e)
			{
				throw new DataException( e.getLocalizedMessage( ), e );
			}
			sortedKeyRowID = null;
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
			Set<Integer> result = new HashSet<Integer>( );
			for ( Object eachKey : candidate )
			{
				result.addAll( getKeyIndex( eachKey ) );
			}
			return result;
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
			return new HashSet<Integer>( );
	}
	
	private Set<Integer> getBetween( Object key1, Object key2 ) throws DataException
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
		Set<Integer> result = new HashSet<Integer>( );
		try
		{
			if( !bCursor.first( ) )
				return result;
			if( ScriptEvalUtil.compare( bCursor.getKey( ), min ) <= 0 )
			{
				bCursor.moveTo( min );
				if( ( (Comparable)bCursor.getKey( ) ).compareTo( max ) > 0 )
					return result;
				if( ( (Comparable)bCursor.getKey( ) ).compareTo( min ) >= 0 )
					result.addAll( bCursor.getValues( ) );
			}
			else
			{
				bCursor.beforeFirst( );
			}
			while( bCursor.next( ) )
			{
				if( ( (Comparable)bCursor.getKey( ) ).compareTo( max ) > 0 )
					return result;
				result.addAll( bCursor.getValues( ) );
			}
		}
		catch (IOException e) 
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		return result;
	}
	
	private Set<Integer> getGreater( Object key, boolean includeKey ) throws DataException
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
		Set<Integer> result = new HashSet<Integer>( );
		try
		{
			if( !bCursor.first( ) )
				return result;
			if( ( (Comparable)bCursor.getKey( ) ).compareTo( key ) > 0 )
			{
				bCursor.beforeFirst( );
			}
			else
			{
				bCursor.moveTo( key );
				int cr = ( (Comparable)bCursor.getKey( ) ).compareTo( key );
				if( ( includeKey && cr == 0 ) || cr > 0 )
				{
					result.addAll( bCursor.getValues( ) );
				}
			}
			while( bCursor.next( ) )
			{
				result.addAll( bCursor.getValues( ) );
			}
		}
		catch (IOException e) 
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		return result;
	}
	
	private Set<Integer> getLess( Object key, boolean includeKey ) throws DataException
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
		Set<Integer> result = new HashSet<Integer>( );
		try
		{
			while( bCursor.next( ) )
			{
				int cr = ( (Comparable)bCursor.getKey( ) ).compareTo( key );
				if( cr < 0 ||( cr == 0 && includeKey ) )
					result.addAll( bCursor.getValues( ) );
				else
					return result;
			}
		}
		catch (IOException e) 
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		return result;
	}
	
	private Set<Integer> getKeyIndex( Object key ) throws DataException
	{
		Set<Integer> set = new HashSet<Integer>();
		Collection<Integer> rowID = null;
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
			rowID = btree.getValues( key );
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
		if( rowID != null )
		{
			set.addAll( rowID );
		}
		return set;
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
		BTreeCursor<Object, Integer> bCursor = btree.createCursor( );
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
		return key.toArray( );
	}

	public Set<Integer> getAllKeyRows() throws DataException
	{
		if( btree == null )
		{
			btree = createBTree( inputFile, BTREE_CACHE_SIZE, serializer );
		}
		BTreeCursor<Object, Integer> bCursor = btree.createCursor( );
		List<Integer> keyRow = new ArrayList<Integer>( );
		try
		{
			while( bCursor.next( ) )
			{
				keyRow.add( bCursor.getValue( ) );
			}
		}
		catch (IOException e)
		{
			throw new DataException( e.getLocalizedMessage( ), e ); 
		}
		Set<Integer> set = new HashSet<Integer>( );
		set.addAll( keyRow );
		return set;
	}

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
		input.seek( blockId * BLOCK_SIZE );
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
		input.seek( blockId * BLOCK_SIZE );
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
		output.seek( blockId * BLOCK_SIZE );
		output.write( bytes );
		output.flush( );
	}
}

class KeyRowID implements IComparableStructure
{
	Object key;
	Integer rowID;
	
	KeyRowID( Object key, Integer rowID )
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

	public int compareTo( Object o )
	{
		if( key == null )
		{
			return -1;
		}
		if( ( ( KeyRowID )o ).key == null )
			return 1;
		return ( ( Comparable)key ).compareTo( ( ( KeyRowID )o ).key );
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public static IStructureCreator getCreator( )
	{
		return new KeyRowIDCreator( );
	}
	
}

class KeyRowIDCreator implements IStructureCreator
{

	public IStructure createInstance( Object[] fields )
	{
		return new KeyRowID( fields[0], (Integer) fields[1] );
	}
}