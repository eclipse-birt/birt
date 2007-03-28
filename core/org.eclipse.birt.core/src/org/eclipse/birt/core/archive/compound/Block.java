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

public class Block implements ArchiveConstants
{

	/** The physical ID -- the NO of this block */
	int id;

	Block prev;

	Block next;

	/** data of the block */
	byte[] blockData = new byte[BLOCK_SIZE];

	private int blockSize;

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
		id = -1;
		dirtyStart = 0;
		dirtyEnd = 0;
		blockSize = 0;
		prev = null;
		next = null;
	}

	void reset( )
	{
		id = -1;
		dirtyStart = 0;
		dirtyEnd = 0;
		blockSize = 0;
		prev = null;
		next = null;
	}

	public void refresh( RandomAccessFile rf ) throws IOException
	{
		blockSize = 0;
		if (id < 0)
		{
			assert false;
		}
		rf.seek( ( (long) id ) * BLOCK_SIZE );
		do
		{
			int size = rf.read( blockData, blockSize, BLOCK_SIZE - blockSize );
			if ( size < 0 )
			{
				break;
			}
			blockSize += size;
		} while ( blockSize < BLOCK_SIZE );
		dirtyStart = 0;
		dirtyEnd = 0;
	}

	public void flush( RandomAccessFile file ) throws IOException
	{
		if ( dirtyEnd != dirtyStart )
		{
 			file.seek( ( (long) id * BLOCK_SIZE ) + dirtyStart );
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
		int size = BLOCK_SIZE - tgt;
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

		if ( blockSize < dirtyEnd )
		{
			blockSize = dirtyEnd;
		}
		return size;
	}

	public int read( int src, byte b[], int off, int len ) throws IOException
	{
		int size = blockSize - src;
		if ( size > len )
		{
			size = len;
		}
		System.arraycopy( blockData, src, b, off, size );
		return size;
	}
}
