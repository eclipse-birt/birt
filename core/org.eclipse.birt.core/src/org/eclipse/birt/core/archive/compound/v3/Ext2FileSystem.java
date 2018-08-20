/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound.v3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.eclipse.birt.core.archive.cache.CacheListener;
import org.eclipse.birt.core.archive.cache.Cacheable;
import org.eclipse.birt.core.archive.cache.FileCacheManager;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

/**
 * 
 * SuperBlock structure:
 * 
 * 
 * 
 */
public class Ext2FileSystem
{

	private volatile RandomAccessFile rf;
	private long length;
	private int maxBlockId;

	private String fileName;
	private boolean readOnly;
	private boolean removeOnExit;

	/**
	 * properties saved in the file header
	 */
	private final HashMap<String, String> properties = new HashMap<String, String>( );
	private boolean propertyDirty = true;

	protected final FileCacheManager cacheManager = new FileCacheManager( );
	/**
	 * nodes define the logical stream
	 */
	private final NodeTable nodeTable = new NodeTable( this );
	/**
	 * named entries to define the logical stream
	 */
	private final EntryTable entryTable = new EntryTable( this );

	private final FreeBlockTable freeTable = new FreeBlockTable( this );

	/**
	 * opened streams
	 */
	private final HashSet<Ext2File> openedFiles = new HashSet<Ext2File>( );

	/**
	 * mode
	 * 
	 * @param filePath
	 * @param mode
	 *            defines the archive open mode: "r": read mode "rw": read write
	 *            mode, if the file exist, create a empty one. "rw+": read write
	 *            mode, if the file exist, open the exits file. "rwt": read
	 *            write cache mode, if the file exist, create a empty one. the
	 *            file is removed after the file is closed.
	 * @throws IOException
	 */
	public Ext2FileSystem( String filePath, String mode ) throws IOException
	{
		this( filePath, null, mode );
	}

	public Ext2FileSystem( String filePath, RandomAccessFile rf, String mode )
			throws IOException
	{
		fileName = new File( filePath ).getCanonicalPath( );
		this.rf = rf;

		cacheManager.setCacheListener( new Ext2FileSystemCacheListener( ) );

		if ( "rw".equals( mode ) )
		{
			readOnly = false;
			removeOnExit = false;
			createFileSystem( );
			return;
		}
		if ( "rw+".equals( mode ) )
		{
			readOnly = false;
			removeOnExit = false;
			if ( new File( fileName ).exists( ) )
			{
				openFileSystem( );
			}
			else
			{
				createFileSystem( );
			}
			return;
		}

		if ( "r".equals( mode ) )
		{
			readOnly = true;
			removeOnExit = false;
			openFileSystem( );
			return;
		}

		if ( "rwt".equals( mode ) )
		{
			readOnly = false;
			removeOnExit = true;
			createFileSystem( );
			return;
		}
		throw new IOException( CoreMessages.getFormattedString(
				ResourceConstants.UNSUPPORTED_FILE_MODE, new Object[]{mode} ) );
	}

	private void openFileSystem( ) throws IOException
	{
		if ( rf == null )
		{
			if ( readOnly )
			{
				rf = new RandomAccessFile( fileName, "r" );
			}
			else
			{
				rf = new RandomAccessFile( fileName, "rw" );
			}
		}
		length = rf.length( );
		maxBlockId = (int) ( ( length + BLOCK_SIZE - 1 ) / BLOCK_SIZE ) + 1;

		readHeader( );
		nodeTable.read( );
		entryTable.read( );
		freeTable.read( );
		readProperties( );
	}

	private void ensureParentFolderCreated( String fileName )
	{
		// try to create the parent folder
		File parentFile = new File( fileName ).getParentFile( );
		if ( parentFile != null && !parentFile.exists( ) )
		{
			parentFile.mkdirs( );
		}
	}

	private void createFileSystem( ) throws IOException
	{
		if ( !removeOnExit )
		{
			if ( rf == null )
			{
				ensureParentFolderCreated( fileName );
				rf = new RandomAccessFile( fileName, "rw" );
			}
			rf.setLength( 0 );
			writeProperties( );
			entryTable.write( );
			freeTable.write( );
			nodeTable.write( );
			writeHeader( );
		}
		length = 0;
		maxBlockId = 2;
	}

	public void setRemoveOnExit( boolean mode )
	{
		removeOnExit = mode;
	}

	synchronized public void close( ) throws IOException
	{
		try
		{
			closeFiles( );
			if ( !readOnly && !removeOnExit )
			{
				writeProperties( );
				entryTable.write( );
				nodeTable.write( );
				freeTable.write( );
				nodeTable.write( NodeTable.INODE_FREE_TABLE );
				cacheManager.touchAllCaches( );
				writeHeader( );
			}

			properties.clear( );
			entryTable.clear( );
			nodeTable.clear( );
			cacheManager.clear( );
			freeTable.clear( );
		}
		finally
		{
			if ( rf != null )
			{
				rf.close( );
				rf = null;
			}
			if ( removeOnExit )
			{
				new File( fileName ).delete( );
			}
		}
	}

	private void closeFiles( ) throws IOException
	{
		if ( openedFiles != null )
		{
			ArrayList<Ext2File> files = new ArrayList<Ext2File>( openedFiles );
			for ( Ext2File file : files )
			{
				if ( file != null )
				{
					file.close( );
				}
			}
			openedFiles.clear( );
		}
	}

	synchronized public void flush( ) throws IOException
	{
		if ( !removeOnExit )
		{
			if ( readOnly )
			{
				throw new IOException(
						CoreMessages.getString( ResourceConstants.FILE_IN_READONLY_MODE ) );
			}

			ensureFileOpened( );
			// flush all the cached data into disk
			writeProperties( );
			entryTable.write( );
			nodeTable.write( );
			freeTable.write( );
			nodeTable.write( NodeTable.INODE_FREE_TABLE );
			cacheManager.touchAllCaches( new Ext2FileSystemCacheListener( ) );
		}
	}

	public void refresh( ) throws IOException
	{
		throw new UnsupportedOperationException( "refresh" );
	}

	public boolean isReadOnly( )
	{
		return readOnly;
	}

	public boolean isRemoveOnExit( )
	{
		return removeOnExit;
	}

	synchronized void registerOpenedFile( Ext2File file )
	{
		openedFiles.add( file );
	}

	synchronized void unregisterOpenedFile( Ext2File file )
	{
		openedFiles.remove( file );
	}

	public void setCacheSize( int cacheSize )
	{
		cacheManager.setMaxCacheSize( cacheSize );
	}

	public int getUsedCacheSize( )
	{
		return cacheManager.getUsedCacheSize( );
	}

	synchronized public Ext2File createFile( String name ) throws IOException
	{
		if ( readOnly )
		{
			throw new IOException(
					CoreMessages.getString( ResourceConstants.FILE_IN_READONLY_MODE ) );
		}
		Ext2Entry entry = entryTable.getEntry( name );
		if ( entry == null )
		{
			Ext2Node node = nodeTable.allocateNode( );
			entry = new Ext2Entry( name, node.getNodeId( ) );
			entryTable.addEntry( entry );
		}
		Ext2Node node = nodeTable.getNode( entry.inode );
		Ext2File file = new Ext2File( this, entry, node );
		file.setLength( 0 );
		return file;
	}

	synchronized public Ext2File openFile( String name ) throws IOException
	{
		Ext2Entry entry = entryTable.getEntry( name );
		if ( entry != null )
		{
			Ext2Node node = nodeTable.getNode( entry.inode );
			return new Ext2File( this, entry, node );
		}

		if ( !readOnly )
		{
			return createFile( name );
		}
		throw new FileNotFoundException( name );
	}

	synchronized public boolean existFile( String name )
	{
		return entryTable.getEntry( name ) != null;
	}

	synchronized public Iterable<String> listAllFiles( )
	{
		return entryTable.listAllEntries( );
	}

	synchronized public Iterable<String> listFiles( String fromName )
	{
		return entryTable.listEntries( fromName );
	}

	synchronized public void removeFile( String name ) throws IOException
	{
		if ( readOnly )
		{
			throw new IOException(
					CoreMessages.getString( ResourceConstants.FILE_IN_READONLY_MODE ) );
		}
		// check if there are any opened stream links with the name,
		if ( !openedFiles.isEmpty( ) )
		{
			ArrayList<Ext2File> removedFiles = new ArrayList<Ext2File>( );
			for ( Ext2File file : openedFiles )
			{
				if ( name.equals( file.getName( ) ) )
				{
					removedFiles.add( file );
				}
			}
			for ( Ext2File file : removedFiles )
			{
				file.close( );
			}
		}
		Ext2Entry entry = entryTable.removeEntry( name );
		if ( entry != null )
		{
			nodeTable.releaseNode( entry.inode );
		}
	}

	public String getFileName( )
	{
		return fileName;
	}

	public String getProperty( String name )
	{
		assert name != null;
		return properties.get( name );
	}

	public void setProperty( String name, String value )
	{
		assert name != null;
		if ( value == null )
		{
			properties.remove( name );
		}
		else
		{
			properties.put( name, value );
		}
		propertyDirty = true;
	}

	static final int HEADER_SIZE = 1024;
	/** the document tag: RPTDOCV2 */
	public static final long EXT2_MAGIC_TAG = 0x525054444f435632L;
	static final int EXT2_VERSION_0 = 0;
	static final int BLOCK_SIZE = 4096;
	static final int BLOCK_SIZE_BITS = 12;
	static final int BLOCK_OFFSET_MASK = 0xFFF;

	private void readHeader( ) throws IOException
	{
		byte[] bytes = new byte[HEADER_SIZE];
		rf.seek( 0 );
		rf.readFully( bytes );

		DataInputStream in = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		long magicTag = in.readLong( );
		if ( magicTag != EXT2_MAGIC_TAG )
		{
			throw new IOException( CoreMessages.getFormattedString(
					ResourceConstants.NOT_EXT2_ARCHIVE, new Object[]{magicTag} ) );
		}
		int version = in.readInt( );
		if ( version != EXT2_VERSION_0 )
		{
			throw new IOException( CoreMessages.getFormattedString(
					ResourceConstants.UNSUPPORTED_ARCHIVE_VERSION,
					new Object[]{version} ) );
		}

		int blockSize = in.readInt( );
		if ( blockSize != BLOCK_SIZE )
		{
			throw new IOException( CoreMessages.getFormattedString(
					ResourceConstants.UNSUPPORTED_BLOCK_SIZE,
					new Object[]{blockSize} ) );
		}
	}

	private void readProperties( ) throws IOException
	{
		Ext2File file = new Ext2File( this, NodeTable.INODE_SYSTEM_HEAD, false );
		try
		{
			byte[] bytes = new byte[(int) ( file.length( ) - HEADER_SIZE )];
			file.seek( HEADER_SIZE );
			file.read( bytes, 0, bytes.length );
			DataInputStream in = new DataInputStream( new ByteArrayInputStream(
					bytes ) );
			int count = in.readInt( );
			for ( int i = 0; i < count; i++ )
			{
				String name = in.readUTF( );
				String value = in.readUTF( );
				if ( !properties.containsKey( name ) )
				{
					properties.put( name, value );
				}
			}
		}
		finally
		{
			file.close( );
		}
		propertyDirty = false;
	}

	private void writeHeader( ) throws IOException
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream( BLOCK_SIZE );
		DataOutputStream out = new DataOutputStream( bytes );
		out.writeLong( EXT2_MAGIC_TAG );
		out.writeInt( EXT2_VERSION_0 );
		out.writeInt( BLOCK_SIZE );
		rf.seek( 0 );
		rf.write( bytes.toByteArray( ) );
	}

	private void writeProperties( ) throws IOException
	{
		if ( !propertyDirty )
		{
			return;
		}
		propertyDirty = false;

		Ext2File file = new Ext2File( this, NodeTable.INODE_SYSTEM_HEAD, false );
		try
		{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream( );
			DataOutputStream out = new DataOutputStream( buffer );
			out.writeInt( properties.size( ) );
			for ( Entry<String, String> entry : properties.entrySet( ) )
			{
				String name = entry.getKey( );
				String value = entry.getValue( );
				out.writeUTF( name );
				out.writeUTF( value );
			}
			byte[] bytes = buffer.toByteArray( );
			file.seek( HEADER_SIZE );
			file.write( bytes, 0, bytes.length );
		}
		finally
		{
			file.close( );
		}
	}

	synchronized protected int allocFreeBlock( ) throws IOException
	{
		int blockId = freeTable.getFreeBlock( );
		if ( blockId > 0 )
		{
			return blockId;
		}
		return maxBlockId++;
	}

	void releaseFreeBlocks( Ext2Node node )
	{
		freeTable.addFreeBlocks( node );
	}

	synchronized protected FatBlock createFatBlock( ) throws IOException
	{
		int blockId = allocFreeBlock( );
		FatBlock block = new FatBlock( this, blockId );
		cacheManager.addCache( block );
		return block;
	}

	synchronized protected DataBlock createDataBlock( ) throws IOException
	{
		int blockId = allocFreeBlock( );
		DataBlock block = new DataBlock( this, blockId );
		cacheManager.addCache( block );
		return block;
	}

	synchronized protected void unloadBlock( Block block ) throws IOException
	{
		cacheManager.releaseCache( block );
	}

	synchronized protected FatBlock loadFatBlock( int blockId )
			throws IOException
	{
		FatBlock block = (FatBlock) cacheManager.getCache( blockId );
		if ( block == null )
		{
			block = new FatBlock( this, blockId );
			block.refresh( );
			cacheManager.addCache( block );
		}
		return block;
	}

	synchronized DataBlock loadDataBlock( int blockId ) throws IOException
	{
		Object cacheKey = Integer.valueOf( blockId );
		DataBlock block = (DataBlock) cacheManager.getCache( cacheKey );
		if ( block == null )
		{
			block = new DataBlock( this, blockId );
			block.refresh( );
			cacheManager.addCache( block );
		}
		return block;
	}

	void readBlock( int blockId, byte[] buffer, int offset, int size )
			throws IOException
	{
		readBlock( blockId, offset, buffer, offset, size );
	}

	synchronized void readBlock( int blockId, int blockOff, byte[] buffer,
			int offset, int size ) throws IOException
	{
		assert buffer != null;
		assert blockId >= 0;
		assert offset >= 0;
		assert blockOff >= 0;
		assert offset + size <= buffer.length;
		assert blockOff + size <= BLOCK_SIZE;

		long position = ( ( (long) blockId ) << BLOCK_SIZE_BITS ) + blockOff;
		if ( position < length )
		{
			long remainSize = length - position;
			rf.seek( position );
			if ( remainSize < size )
			{
				size = (int) remainSize;
			}
			rf.readFully( buffer, offset, size );
		}

	}

	void writeBlock( int blockId, byte[] buffer, int offset, int size )
			throws IOException
	{
		writeBlock( blockId, offset, buffer, offset, size );
	}

	synchronized void writeBlock( int blockId, int blockOff, byte[] buffer,
			int offset, int size ) throws IOException
	{
		assert buffer != null;
		assert blockId >= 0;
		assert offset >= 0;
		assert blockOff >= 0;
		assert offset + size <= buffer.length;
		assert blockOff + size <= BLOCK_SIZE;

		ensureFileOpened( );
		long position = ( ( (long) blockId ) << BLOCK_SIZE_BITS ) + blockOff;
		rf.seek( position );
		rf.write( buffer, offset, size );
		position += size;
		if ( position > length )
		{
			length = position;
		}
	}

	public Ext2Entry getEntry( String name )
	{
		return entryTable.getEntry( name );
	}

	public Ext2Node getNode( int nodeId )
	{
		return nodeTable.getNode( nodeId );
	}

	static class Ext2FileSystemCacheListener implements CacheListener
	{

		public void onCacheRelease( Cacheable cache )
		{
			Ext2Block block = (Ext2Block) cache;
			try
			{
			if(block.getFileSystem().isFreeDiskSpaceAvailable()) {
				block.flush( );
			} else {
				block.getFileSystem().releaseResources();
				throw new RuntimeException("Report generation was interrupted due to the end of the free disk space");
			}
			}
			catch ( IOException ex )
			{
				ex.printStackTrace( );
			}

		}
	}

	private void ensureFileOpened( ) throws IOException
	{
		if ( rf == null )
		{
			synchronized ( this )
			{
				if ( rf == null )
				{
					ensureParentFolderCreated( fileName );
					rf = new RandomAccessFile( fileName, "rw" );
					rf.setLength( 0 );
				}
			}
		}
	}

	boolean isFreeDiskSpaceAvailable() {
		try {
			return Files.getFileStore(Paths.get(fileName)).getUsableSpace() > 0;
		} catch (IOException e) {
			return true;
		}
	}
	
	void releaseResources() {
		try {
			rf.getChannel().close();
			rf.close();
			Files.deleteIfExists(Paths.get(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long length( )
	{
		// field length is only updated when archive file is written to disk
		// file, so can't use it directly.
		return maxBlockId * BLOCK_SIZE;
	}
}
