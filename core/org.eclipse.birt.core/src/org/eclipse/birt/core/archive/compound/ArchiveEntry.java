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

import java.io.IOException;

public class ArchiveEntry
{

	protected int cachId;
	protected ArchiveFile af;
	protected EntryTable.Entry entry;
	protected AllocationTable.Entry index;

	ArchiveEntry( ArchiveFile af, EntryTable.Entry entry ) throws IOException
	{
		this.af = af;
		this.entry = entry;
		this.cachId = entry.getBlock( );
		if ( cachId != -1 )
		{
			index = af.allocTbl.loadEntry( cachId );
		}
	}

	public long getLength( ) throws IOException
	{
		return entry.getLength( );
	}

	public void setLength( long length ) throws IOException
	{
		entry.setLength( length );
	}

	public void flush( ) throws IOException
	{
		// TODO: must support flush
	}

	public void refresh( ) throws IOException
	{
		// TODO: support refresh later.
	}

	public Object lock( ) throws IOException
	{
		return af.lockEntry( this );
	}

	public void unlock( Object lock ) throws IOException
	{
		af.unlockEntry( lock );
	}

	public int read( long pos, byte[] b, int off, int len ) throws IOException
	{
		long length = entry.getLength( );

		if ( pos >= length )
		{
			return -1;
		}

		if ( pos + len > length )
		{
			len = (int) ( length - pos );
		}

		// read first block
		int blockId = (int) ( pos / Block.BLOCK_SIZE );
		int blockOff = (int) ( pos % Block.BLOCK_SIZE );
		int readSize = Block.BLOCK_SIZE - blockOff;
		if ( len < readSize )
		{
			readSize = len;
		}
		int phyBlockId = index.getBlock( blockId );
		af.read( phyBlockId, blockOff, b, off, readSize );
		int remainSize = len - readSize;

		// read blocks
		while ( remainSize >= Block.BLOCK_SIZE )
		{
			blockId++;
			phyBlockId = index.getBlock( blockId );
			af.read( phyBlockId, 0, b, off + readSize, Block.BLOCK_SIZE );
			readSize += Block.BLOCK_SIZE;
			remainSize -= Block.BLOCK_SIZE;
		}

		// read remain blocks
		if ( remainSize > 0 )
		{
			blockId++;
			phyBlockId = index.getBlock( blockId );
			af.read( phyBlockId, 0, b, off + readSize, remainSize );
			readSize += remainSize;
		}

		return readSize;
	}

	public void write( long pos, byte[] b, int off, int len )
			throws IOException
	{
		ensureSize( pos + len );

		int blockId = (int) ( pos / Block.BLOCK_SIZE );
		int phyBlockId = index.getBlock( blockId );
		int blockOff = (int) ( pos % Block.BLOCK_SIZE );
		int writeSize = Block.BLOCK_SIZE - blockOff;
		if ( len < writeSize )
		{
			writeSize = len;
		}
		af.write( phyBlockId, blockOff, b, off, writeSize );
		int remainSize = len - writeSize;

		// write blocks
		while ( remainSize >= Block.BLOCK_SIZE )
		{
			blockId++;
			phyBlockId = index.getBlock( blockId );
			af.write( phyBlockId, 0, b, off + writeSize, Block.BLOCK_SIZE );
			writeSize += Block.BLOCK_SIZE;
			remainSize -= Block.BLOCK_SIZE;
		}

		// write remain blocks
		if ( remainSize > 0 )
		{
			blockId++;
			phyBlockId = index.getBlock( blockId );
			af.write( phyBlockId, 0, b, off + writeSize, remainSize );
		}

		long length = entry.getLength( );
		long offset = pos + len;
		if ( length < offset )
		{
			setLength( offset );
		}
	}

	protected void ensureSize( long newLength ) throws IOException
	{
		if ( index == null )
		{
			index = af.allocTbl.createEntry( );
			entry.setBlock( index.getFirstBlock( ) );
		}
		int blockCount = (int) ( ( newLength + Block.BLOCK_SIZE - 1 ) / Block.BLOCK_SIZE );
		int totalBlock = index.getTotalBlocks( );
		if ( blockCount > totalBlock )
		{
			index.allocBlocks( blockCount - totalBlock );
		}
	}
}
