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
import java.io.RandomAccessFile;

/**
 * A physical block in a physical compound file, might be stream items block,
 * index block or stream data block.
 */

class Block implements ArchiveConstants
{

	/** The physical ID -- the NO of this block */
	int id;

	Block prev;

	Block next;

	final int blockSize;

	/** data of the block */
	byte[] blockData;


	private int dataSize;

	private int dirtyStart;

	private int dirtyEnd;

	/**
	 * Constructor
	 * 
	 * @param fs
	 *            the compound file system it belongs to
	 * @param blockId
	 *            the block ID
	 */
	Block( )
	{
		this( DEFAULT_BLOCK_SIZE );
	}

	Block( int size )
	{
		blockSize = size;
		blockData = new byte[size];
		id = -1;
		dirtyStart = 0;
		dirtyEnd = 0;
		dataSize = 0;
		prev = null;
		next = null;
	}

	void reset( )
	{
		id = -1;
		dirtyStart = 0;
		dirtyEnd = 0;
		dataSize = 0;
		prev = null;
		next = null;
	}

	void refresh( RandomAccessFile rf ) throws IOException
	{
		dataSize = 0;
		if ( id < 0 )
		{
			assert false;
		}
		rf.seek( ( (long) id ) * blockSize );
		do
		{
			int size = rf.read( blockData, dataSize, blockSize - dataSize );
			if ( size < 0 )
			{
				break;
			}
			dataSize += size;
		} while ( dataSize < blockSize );
		dirtyStart = 0;
		dirtyEnd = 0;
	}

	public void flush( RandomAccessFile file ) throws IOException
	{
		if ( dirtyEnd != dirtyStart )
		{
			file.seek( ( (long) id * blockSize ) + dirtyStart );
			file.write( blockData, dirtyStart, dirtyEnd - dirtyStart );
		}
		dirtyEnd = dirtyStart = 0;
	}

	public byte[] getData( )
	{
		return blockData;
	}

	public int write( int tgt, byte b[], int off, int len ) throws IOException
	{
		int size = blockSize - tgt;
		if ( size > len )
			size = len;
		System.arraycopy( b, off, blockData, tgt, size );
		if ( dirtyStart > tgt )
		{
			dirtyStart = tgt;
		}
		if ( dirtyEnd < tgt + size )
		{
			dirtyEnd = tgt + size;
		}

		if ( dataSize < dirtyEnd )
		{
			dataSize = dirtyEnd;
		}
		return size;
	}

	public int read( int src, byte b[], int off, int len ) throws IOException
	{
		int size = dataSize - src;
		if ( size > len )
		{
			size = len;
		}
		System.arraycopy( blockData, src, b, off, size );
		return size;
	}
}
