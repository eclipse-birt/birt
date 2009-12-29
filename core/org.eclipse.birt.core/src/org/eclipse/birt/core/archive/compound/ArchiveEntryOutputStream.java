/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.RAOutputStream;

/**
 * An OutputStream wrapper for RandomAccessStreamImpl.
 * 
 */

public class ArchiveEntryOutputStream extends RAOutputStream
{

	protected ArchiveWriter writer;

	/** the stream item */

	protected ArchiveEntry entry;

	/** the current output position */

	private long offset;

	private byte bytes[] = new byte[16];

	/**
	 * Constructor
	 * 
	 * @param fs
	 *            the compound file system.
	 * @param stream
	 *            the stream item.
	 */
	ArchiveEntryOutputStream( ArchiveWriter writer, ArchiveEntry entry )
	{
		this.writer = writer;
		this.entry = entry;
		this.offset = 0;
	}

	public long getOffset( ) throws IOException
	{
		return offset;
	}

	public void seek( long localPos ) throws IOException
	{
		if ( localPos < 0 )
		{
			throw new IOException( "Invalid seek offset " + localPos );
		}
		offset = localPos;
	}

	public void write( int b ) throws IOException
	{
		bytes[0] = (byte) b;
		entry.write( offset, bytes, 0, 1 );
		offset++;

	}

	public void writeInt( int value ) throws IOException
	{
		ArchiveUtil.integerToBytes( value, bytes );
		entry.write( offset, bytes, 0, 4 );
		offset += 4;
	}

	public void writeLong( long value ) throws IOException
	{
		ArchiveUtil.longToBytes( value, bytes );
		entry.write( offset, bytes, 0, 8 );
		offset += 8;
	}

	public void write( byte b[], int off, int len ) throws IOException
	{
		entry.write( offset, b, off, len );
		offset += len;
	}

	public void flush( ) throws IOException
	{
		entry.flush( );
	}

	public void close( ) throws IOException
	{
		try
		{
			entry.close( );
		}
		finally
		{
			// remove it from the writer
			writer.unregisterStream( this );
		}
	}

	public long length( ) throws IOException
	{
		long length = entry.getLength( );
		long offset = getOffset( );
		if ( offset > length )
		{
			return offset;
		}
		return length;
	}
}
