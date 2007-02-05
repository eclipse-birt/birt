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

package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * the archive file contains following mode:
 * <li> "r" open the file for read only.
 * <li> "rw" create the file for read/write
 * <li> "rw+" open file is open for read/write
 * <li> "rwt" create the trainsnt file, it will be removed after closing.
 */
public class ArchiveFile
{

	/** the physical file correspond to this compound file system */
	protected RandomAccessFile rf;

	protected boolean isWritable;

	protected boolean isTransient;

	protected boolean isAppend;

	/**
	 * the archive file name.
	 */
	protected String archiveName;

	protected ArchiveHeader head;
	protected AllocationTable allocTbl;
	protected EntryTable entryTbl;
	protected HashMap entries;

	protected BlockManager caches;
	protected int totalBlocks;

	private boolean enableCache = true;

	private void setupArchiveMode( String mode ) throws IOException
	{
		if ( "r".equals( mode ) )
		{
			isWritable = false;
			isTransient = false;
			isAppend = false;
		}
		else if ( "rw".equals( mode ) )
		{
			isWritable = true;
			isTransient = false;
			isAppend = false;
		}
		else if ( "rw+".equals( mode ) )
		{
			isWritable = true;
			isTransient = false;
			isAppend = true;
		}
		else if ( "rwt".equals( mode ) )
		{
			isWritable = true;
			isTransient = true;
			isAppend = true;
		}
		else
		{
			throw new IllegalArgumentException( );
		}
	}

	public ArchiveFile( String fileName, String mode ) throws IOException
	{
		this.archiveName = fileName;

		setupArchiveMode( mode );

		caches = new BlockManager( new CacheEventAdapter( ) );

		if ( isWritable && !isAppend )
		{
			createDocument( );
		}
		else
		{
			openDocument( );
		}
	}

	public void setCacheSize( int cacheSize )
	{
		int blockCount = ( cacheSize + Block.BLOCK_SIZE - 1 )
				/ Block.BLOCK_SIZE;
		caches.setCacheSize( blockCount );
	}

	/**
	 * open the archive file for read or rw.
	 * 
	 * @throws IOException
	 */
	private void openDocument( ) throws IOException
	{
		try
		{
			if ( !isWritable && !useNativeLock )
			{
				rf = new RandomAccessFile( archiveName, "r" );
			}
			else
			{
				rf = new RandomAccessFile( archiveName, "rw" );
			}
			totalBlocks = (int) ( ( rf.length( ) + Block.BLOCK_SIZE - 1 ) / Block.BLOCK_SIZE );
			head = ArchiveHeader.loadHeader( this );
			allocTbl = AllocationTable.loadTable( this );
			entryTbl = EntryTable.loadTable( this );
			entries = new HashMap( );
			Iterator iter = entryTbl.listEntries( ).iterator( );
			while ( iter.hasNext( ) )
			{
				EntryTable.Entry nameEnt = (EntryTable.Entry) iter.next( );
				entries.put( nameEnt.getName( ), new ArchiveEntry( this,
						nameEnt ) );
			}
		}
		catch ( IOException ex )
		{
			rf.close( );
			throw ex;
		}
	}

	/**
	 * create the document
	 * 
	 * @throws IOException
	 */
	private void createDocument( ) throws IOException
	{
		if ( !isTransient )
		{
			rf = new RandomAccessFile( archiveName, "rw" );
			rf.setLength( 3 * Block.BLOCK_SIZE );
		}
		totalBlocks = 3;
		head = ArchiveHeader.createHeader( this );
		allocTbl = AllocationTable.createTable( this );
		entryTbl = EntryTable.createTable( this );
		entries = new HashMap( );
	}

	public String getName( )
	{
		return archiveName;
	}

	public void close( ) throws IOException
	{
		if ( isWritable )
		{
			if ( !isTransient )
			{
				flush( );
			}
		}
		if ( rf != null )
		{
			rf.close( );
		}
		if ( isTransient )
		{
			new File( archiveName ).delete( );
		}
	}

	public void saveAs( String fileName ) throws IOException
	{
		ArchiveFile file = new ArchiveFile( fileName, "rw" );
		try
		{
			List entries = listEntries( "/" );
			Iterator iter = entries.listIterator( );
			while ( iter.hasNext( ) )
			{
				String name = (String) iter.next( );
				ArchiveEntry tgt = file.createEntry( name );
				ArchiveEntry src = getEntry( name );
				copyEntry( src, tgt );
			}
		}
		finally
		{
			file.close( );
		}
	}

	private void copyEntry( ArchiveEntry src, ArchiveEntry tgt )
			throws IOException
	{
		byte[] b = new byte[4096];
		long length = src.getLength( );
		long pos = 0;
		while ( pos < length )
		{
			int size = src.read( pos, b, 0, 4096 );
			tgt.write( pos, b, 0, size );
			pos += size;
		}
	}

	public synchronized void flush( ) throws IOException
	{
		checkWritable( );
		if ( !isTransient )
		{
			caches.flush( );
		}
	}

	public synchronized void refresh( ) throws IOException
	{
		// TODO: support refresh operations
	}

	public synchronized boolean exists( String name )
	{
		return entries.containsKey( name );
	}

	public synchronized ArchiveEntry getEntry( String name ) throws IOException
	{
		return (ArchiveEntry) entries.get( name );
	}

	public synchronized List listEntries( String namePattern )
			throws IOException
	{
		ArrayList list = new ArrayList( );
		Iterator iter = entries.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String name = (String) iter.next( );
			if ( name.startsWith( namePattern ) )
			{
				list.add( name );
			}
		}
		return list;
	}

	public synchronized ArchiveEntry createEntry( String name )
			throws IOException
	{
		checkWritable( );
		ArchiveEntry entry = (ArchiveEntry) entries.get( name );
		if ( entry != null )
		{
			entry.setLength( 0L );
			return entry;
		}
		EntryTable.Entry nameEnt = entryTbl.createEntry( name );
		entry = new ArchiveEntry( this, nameEnt );
		entries.put( name, entry );
		return entry;
	}

	public synchronized boolean removeEntry( String name ) throws IOException
	{
		checkWritable( );
		ArchiveEntry entry = (ArchiveEntry) entries.get( name );
		if ( entry != null )
		{
			entries.remove( name );
			EntryTable.Entry nameEnt = entryTbl.findEntry( name );
			entryTbl.removeEntry( nameEnt );
			int blockId = nameEnt.getBlock( );
			if ( blockId != -1 )
			{
				AllocationTable.Entry allEnt = allocTbl.loadEntry( blockId );
				allocTbl.removeEntry( allEnt );
			}
			return true;
		}
		return false;
	}

	/**
	 * should use the native file lock to synchronize the reader/writer
	 */
	private boolean useNativeLock = false;

	Object lockEntry( ArchiveEntry entry ) throws IOException
	{
		if ( useNativeLock )
		{
			if ( !isTransient )
			{
				entry.ensureSize( 1 );
				int blockId = entry.index.getBlock( 0 );
				return rf.getChannel( ).lock( blockId * Block.BLOCK_SIZE, 1,
						false );
			}
		}
		return entry;
	}

	void unlockEntry( Object locker ) throws IOException
	{
		if ( locker instanceof FileLock )
		{
			FileLock flck = (FileLock) locker;
			flck.release( );
		}
		if ( !( locker instanceof ArchiveEntry ) )
		{
			throw new IOException( "Invalide lock type:" + locker );
		}
	}

	/**
	 * return the total blocks of the archive file.
	 * 
	 * @return
	 * @throws IOException
	 */
	int getTotalBlocks( ) throws IOException
	{
		return totalBlocks;
	}

	int allocateBlock( ) throws IOException
	{
		checkWritable( );
		int blockId = totalBlocks++;
		if ( !isTransient )
		{
			rf.setLength( totalBlocks * Block.BLOCK_SIZE );
		}
		return blockId;
	}

	private void checkWritable( ) throws IOException
	{
		if ( !isWritable )
		{
			throw new IOException( "Archive must be opend for write" );
		}
	}

	/**
	 * read the data from cache.
	 * 
	 * This API read <code>len</code> bytes from <code>blockOff</code> in
	 * block <code>blockId</code>, store the data into <code>b</code> from
	 * <code>off</code>. The read cache is identified by <code>slotId</code>
	 * 
	 * @param blockId
	 *            the block id
	 * @param blockOff
	 *            the block offset
	 * @param b
	 *            read buffer
	 * @param off
	 *            buffer offset
	 * @param len
	 *            read length
	 * @throws IOException
	 */
	synchronized void read( int blockId, int blockOff, byte[] b, int off,
			int len ) throws IOException
	{
		if ( enableCache )
		{
			Block block = caches.getBlock( blockId );
			block.read( blockOff, b, off, len );
		}
		else
		{
			long pos = blockId * Block.BLOCK_SIZE + blockOff;
			rf.seek( pos );
			rf.readFully( b, off, len );
		}
	}

	/**
	 * write the data into cache.
	 * 
	 * The API saves <code>len</code> bytes in <code>b</code> from
	 * <code>off</code> to block <code>blockId</code> from
	 * <code>blockOff</code>
	 * 
	 * @param blockId
	 *            block id.
	 * @param blockOff
	 *            offset in the block.
	 * @param b
	 *            data to be saved
	 * @param off
	 *            offset.
	 * @param len
	 *            write size.
	 * @throws IOException
	 */
	synchronized void write( int blockId, int blockOff, byte[] b, int off,
			int len ) throws IOException
	{
		checkWritable( );
		if ( enableCache )
		{
			Block block = caches.getBlock( blockId );
			block.write( blockOff, b, off, len );
		}
		else
		{
			long pos = blockId * Block.BLOCK_SIZE + blockOff;
			rf.seek( pos );
			rf.write( b, off, len );
		}
	}

	class CacheEventAdapter extends BlockManagerEventAdapter
	{
		public void flush( Block block ) throws IOException
		{
			if ( isWritable )
			{
				if ( rf == null )
				{
					rf = new RandomAccessFile( archiveName, "rw" );
					rf.setLength( 0 );
				}
				block.flush( rf );
			}
		}

		public void refresh( Block block ) throws IOException
		{
			if ( block.id < totalBlocks )
			{
				block.refresh( rf );
			}
		}
	}
}
