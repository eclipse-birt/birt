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

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.core.util.IOUtil;

/**
 * The AllocationTable defines the FAT table in the archive file.
 * 
 * The FAT table a integer array, each element represent a block in the stream.
 * The block id is the array index. The value of the element is the id of the
 * next block followed by the block index. It constructs a link list which saves
 * the blocks in a stream.
 * <p>
 * There are multiple links in the FAT table.
 * <li>The first link stream is the free blocks which starts from block 0.</li>
 * <li>The second link stream is the FAT table itself which starts from block
 * 1.</li>
 * <li>The third link stream is the entry table which starts from block 2.</li>
 * <li>The other streams id are saved in the entry table.</li>
 */
class AllocTable implements ArchiveConstants
{

	/**
	 * the increase step of the size of the link list.
	 */
	private final static int BLOCK_COUNT_INCREASE = 32;
	/**
	 * the last block indicator.
	 */
	private final static int LAST_BLOCK = -1;

	/**
	 * the archive file which use this allocation table.
	 */
	protected ArchiveFile af;

	/**
	 * the list saves the blocks of the allocat table.
	 */
	protected int[] allocBlocks;
	/**
	 * total blocks in the allocat table.
	 */
	protected int totalAllocBlocks;

	/**
	 * free blocks in the table
	 */
	protected int[] freeBlocks;

	protected int totalFreeBlocks;

	protected HashMap entries = new HashMap( );

	AllocTable( ArchiveFile af )
	{
		this.af = af;
		allocBlocks = new int[BLOCK_COUNT_INCREASE];
		allocBlocks[0] = ALLOC_TABLE_BLOCK;
		totalAllocBlocks = 1;

		freeBlocks = new int[BLOCK_COUNT_INCREASE];
		totalFreeBlocks = 0;

		entries.put( new Integer( ENTRY_TABLE_BLOCK ), new AllocEntry(
				ENTRY_TABLE_BLOCK ) );
	}

	static AllocTable createTable( ArchiveFile af ) throws IOException
	{
		AllocTable table = new AllocTable( af );
		table.flush( );
		return table;
	}

	static AllocTable loadTable( ArchiveFile af ) throws IOException
	{
		AllocTable table = new AllocTable( af );
		table.refresh( );
		return table;
	}

	/**
	 * write the whole FAT table into the file.
	 * 
	 * @throws IOException
	 */
	void flush( ) throws IOException
	{
		// flush all entries in the table
		Iterator iter = entries.values( ).iterator( );
		while ( iter.hasNext( ) )
		{
			AllocEntry entry = (AllocEntry) iter.next( );
			entry.flush( this );
		}

		// flush the free block list
		if ( totalFreeBlocks != 0 )
		{
			for ( int i = 1; i < totalFreeBlocks; i++ )
			{
				writeFATInt( freeBlocks[i - 1] * 4, freeBlocks[i] );
			}
			writeFATInt( freeBlocks[totalFreeBlocks - 1] * 4, LAST_BLOCK );
		}
		else
		{
			writeFATInt( 0, LAST_BLOCK );
		}

		// flush the total blocks
		for ( int i = 1; i < totalAllocBlocks; i++ )
		{
			writeFATInt( allocBlocks[i - 1] * 4, allocBlocks[i] );
		}
		writeFATInt( allocBlocks[totalAllocBlocks - 1] * 4, LAST_BLOCK );
	}

	/**
	 * reload the FAT table from the file.
	 * 
	 * @throws IOException
	 */
	void refresh( ) throws IOException
	{
		// reload the fat tables
		int lastBlockId = allocBlocks[totalAllocBlocks - 1];
		int blockId = readFATInt( lastBlockId * 4 );
		while ( blockId != -1 )
		{
			ensureFATBlocks( totalAllocBlocks + 1 );
			allocBlocks[totalAllocBlocks] = blockId;
			totalAllocBlocks++;
			blockId = readFATInt( blockId * 4 );
		}

		// the free blocks is only used by the writer, so we needn't refresh it.

		// refresh all entries in the table
		Iterator iter = entries.values( ).iterator( );
		while ( iter.hasNext( ) )
		{
			AllocEntry entry = (AllocEntry) iter.next( );
			entry.refresh( this );
		}
	}

	/**
	 * allocate a block from the free list. If the free list is empty, create a
	 * new block at the end of file.
	 * 
	 * @return the block id.
	 * @throws IOException
	 */
	int getFreeBlock( ) throws IOException
	{
		// get the free block
		if ( totalFreeBlocks > 0 )
		{
			int freeBlockId = freeBlocks[totalFreeBlocks - 1];
			totalFreeBlocks--;
			return freeBlockId;
		}
		else
		{
			int freeBlockId = af.allocateBlock( );
			if ( freeBlockId % ( BLOCK_SIZE / 4 ) == 0 )
			{
				freeBlockId = af.allocateBlock( );
			}
			return freeBlockId;
		}
	}

	AllocEntry createEntry( ) throws IOException
	{
		int blockId = getFreeBlock( );
		AllocEntry entry = new AllocEntry( blockId );
		entries.put( new Integer( blockId ), entry );
		return entry;
	}

	AllocEntry loadEntry( int blockId ) throws IOException
	{
		AllocEntry entry = (AllocEntry) entries.get( new Integer( blockId ) );
		if ( entry == null )
		{
			entry = new AllocEntry( blockId );
			entry.refresh( this );
			entries.put( new Integer( blockId ), entry );
		}
		return entry;
	}

	/**
	 * remove the entry from the FAT table.
	 * 
	 * @param entry
	 *            the entry to be removed.
	 * @throws IOException
	 */
	void removeEntry( AllocEntry entry ) throws IOException
	{
		int totalBlocks = entry.getTotalBlocks( );
		ensureFreeBlocks( totalFreeBlocks + totalBlocks );
		for ( int i = 0; i < totalBlocks; i++ )
		{
			int freeBlock = entry.getBlock( i );
			freeBlocks[totalFreeBlocks] = freeBlock;
			totalFreeBlocks++;
		}
		entries.remove( new Integer( entry.getFirstBlock( ) ) );
	}

	int readFATInt( long offset ) throws IOException
	{
		if ( offset > (long) totalAllocBlocks * BLOCK_SIZE )
		{
			throw new EOFException( );
		}
		int blockId = (int) ( offset / BLOCK_SIZE );
		int off = (int) ( offset % BLOCK_SIZE );
		int phyBlockId = getFATBlock( blockId );
		byte[] b = new byte[4];
		af.read( phyBlockId, off, b, 0, 4 );
		return IOUtil.bytesToInteger( b );
	}

	void writeFATInt( long offset, int block ) throws IOException
	{
		int blockId = (int) ( offset / BLOCK_SIZE );
		int off = (int) ( offset % BLOCK_SIZE );
		if ( blockId >= totalAllocBlocks )
		{
			int newTotalBlocks = blockId + 1;
			ensureFATBlocks( newTotalBlocks );
			for ( int i = totalAllocBlocks; i < newTotalBlocks; i++ )
			{
				allocBlocks[totalAllocBlocks] = 1024 * totalAllocBlocks;
				totalAllocBlocks++;
			}
		}
		int phyBlockId = getFATBlock( blockId );
		byte[] b = new byte[4];
		IOUtil.integerToBytes( block, b );
		af.write( phyBlockId, off, b, 0, 4 );
	}

	private void ensureFATBlocks( int size )
	{
		// ensure the buffer is larger enough.
		if ( allocBlocks.length < size )
		{
			int length = ( size / BLOCK_COUNT_INCREASE + 1 )
					* BLOCK_COUNT_INCREASE;
			int[] blocks = new int[length];
			System.arraycopy( allocBlocks, 0, blocks, 0, totalAllocBlocks );
			allocBlocks = blocks;
		}
	}

	private void ensureFreeBlocks( int size )
	{
		// ensure the buffer is larger enough.
		if ( freeBlocks.length < size )
		{
			int length = ( size / BLOCK_COUNT_INCREASE + 1 )
					* BLOCK_COUNT_INCREASE;
			int[] blocks = new int[length];
			System.arraycopy( freeBlocks, 0, blocks, 0, totalFreeBlocks );
			freeBlocks = blocks;
		}
	}

	private int getFATBlock( int blockId ) throws IOException
	{
		return allocBlocks[blockId];
	}

	void debug_dump( )
	{
		System.out.println( "ALLOC:" );
		for ( int i = 0; i < totalAllocBlocks; i++ )
		{
			System.out.print( allocBlocks[i] + "," );
		}
		System.out.println( "FREE:" );
		for ( int i = 0; i < totalFreeBlocks; i++ )
		{
			System.out.print( freeBlocks[i] + "," );
		}
		System.out.println( );
		Iterator iter = entries.values( ).iterator( );
		while ( iter.hasNext( ) )
		{
			AllocEntry entry = (AllocEntry) iter.next( );
			for ( int i = 0; i < entry.getTotalBlocks( ); i++ )
			{
				System.out.print( entry.getBlock( i ) + "," );
			}
			System.out.println( );
		}

	}
}
