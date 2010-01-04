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

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.RAOutputStream;

/**
 * An OutputStream wraper for RandomAccessStreamImpl.
 * 
 */

public class ArchiveEntryOutputStream extends RAOutputStream
{

	protected ArchiveWriter writer;

	/** the stream item */

	protected ArchiveEntry entry;

	protected byte[] buffer;
	protected int buffer_offset;
	protected int buffer_size;

	/** the current output position */

	private long offset;

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
		this.buffer_offset = 0;
		this.buffer_size = 4096;
		this.buffer = new byte[4096];
	}

	public long getOffset( ) throws IOException
	{
		return offset + buffer_offset;
	}

	public void seek( long localPos ) throws IOException
	{
		if ( localPos < 0 )
		{
			throw new IOException( "Invalid seek offset " + localPos );
		}
		if ( localPos > entry.getLength( ) )
		{
			entry.setLength( localPos );
		}
		//entry.ensureSize( localPos );

		if ( offset + buffer_offset != localPos )
		{
			flushBuffer( );
			offset = localPos;
		}
	}

	public void write( int b ) throws IOException
	{
		if ( buffer_offset >= buffer_size )
		{
			flushBuffer( );
		}
		buffer[buffer_offset] = (byte) b;
		buffer_offset++;
	}

	public void writeInt( int value ) throws IOException
	{
		if ( buffer_offset + 4 >= buffer_size )
		{
			flushBuffer( );
		}
		ArchiveUtil.integerToBytes( value, buffer, buffer_offset );
		buffer_offset += 4;
	}

	public void writeLong( long value ) throws IOException
	{
		if ( buffer_offset + 8 >= buffer_size )
		{
			flushBuffer( );
		}
		ArchiveUtil.longToBytes( value, buffer, buffer_offset );
		buffer_offset += 8;
	}

	public void write( byte b[], int off, int len ) throws IOException
	{
		if ( buffer_offset + len <= buffer_size )
		{
			System.arraycopy( b, off, buffer, buffer_offset, len );
			buffer_offset += len;
			return;
		}
		flushBuffer( );
		entry.write( offset, b, off, len );
		offset += len;
	}

	public void flush( ) throws IOException
	{
		flushBuffer( );
		entry.flush( );
		super.flush( );
	}

	public void close( ) throws IOException
	{
		if ( entry != null )
		{
			// remove it from the writer
			writer.unregisterStream( this );
			try
			{
				// flush the data into the stream
				flush( );
			}
			finally
			{
				try
				{
					entry.close( );
				}
				finally
				{
					entry = null;
				}
			}
		}
	}

	private void flushBuffer( ) throws IOException
	{
		if ( buffer_offset != 0 )
		{
			entry.write( offset, buffer, 0, buffer_offset );
			offset += buffer_offset;
			buffer_offset = 0;
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
