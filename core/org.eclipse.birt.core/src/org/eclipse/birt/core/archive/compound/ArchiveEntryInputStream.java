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

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.RAInputStream;

/**
 * RAInputStream implementation based on the ArchiveEntry.
 * 
 */
public class ArchiveEntryInputStream extends RAInputStream
{

	/** the archive entry */
	private ArchiveEntry entry;

	/** the current input position */
	private long offset;

	/**
	 * Constructor
	 * 
	 * @param fs
	 *            the compound file system.
	 * @param stream
	 *            the stream item.
	 */
	ArchiveEntryInputStream( ArchiveEntry entry )
	{
		this.entry = entry;
		this.offset = 0;
	}

	/**
	 * buffer used to read the int/long
	 */
	private byte[] buffer = new byte[8];

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#read()
	 */
	public int read( ) throws IOException
	{
		int size = entry.read( offset, buffer, 0, 1 );
		if ( size == -1 )
		{
			return -1;
		}
		offset++;
		return buffer[0] & 0xff;
	}

	public int available( ) throws IOException
	{
		long av = entry.getLength( ) - offset;
		if ( av > Integer.MAX_VALUE )
		{
			return Integer.MAX_VALUE;
		}
		return (int) av;
	}

	public long getOffset( ) throws IOException
	{
		return offset;
	}

	public long length( ) throws IOException
	{
		return entry.getLength( );
	}

	public void readFully( byte[] b, int off, int len ) throws IOException
	{
		int n = 0;
		do
		{
			int count = read( b, off + n, len - n );
			if ( count < 0 )
				throw new EOFException( );
			n += count;
		} while ( n < len );
	}

	public int read( byte b[], int off, int len ) throws IOException
	{
		int size = entry.read( offset, b, off, len );
		if ( size != -1 )
		{
			offset += size;
		}
		return size;
	}

	public int readInt( ) throws IOException
	{
		int size = entry.read( offset, buffer, 0, 4 );
		if ( size != 4 )
		{
			throw new EOFException( );
		}
		offset += 4;
		return ArchiveUtil.bytesToInteger( buffer );
	}

	public long readLong( ) throws IOException
	{
		int size = entry.read( offset, buffer, 0, 8 );
		if ( size != 8 )
		{
			throw new EOFException( );
		}
		offset += 8;
		return ArchiveUtil.bytesToLong( buffer );
	}

	public void refresh( ) throws IOException
	{
		entry.refresh( );
	}

	public void seek( long localPos ) throws IOException
	{
		if ( localPos >= entry.getLength( ) )
		{
			throw new EOFException( "exceed the file length" );
		}
		offset = localPos;
	}

}
