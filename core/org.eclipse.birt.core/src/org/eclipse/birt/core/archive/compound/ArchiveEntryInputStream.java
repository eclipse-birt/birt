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

import org.eclipse.birt.core.archive.RAInputStream;

/**
 * An InputStream wraper for RandomAccessStreamImpl.
 * 
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/05 09:22:20 $
 */

public class ArchiveEntryInputStream extends RAInputStream
{

	/** the stream item */

	protected ArchiveEntry entry;

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
	public ArchiveEntryInputStream( ArchiveEntry entry )
	{
		this.entry = entry;
		this.offset = 0;
	}

	byte[] buffer = new byte[8];

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#read()
	 */
	public int read( ) throws IOException
	{
		entry.read( offset, buffer, 0, 1 );
		offset += 1;
		return buffer[0];
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
		entry.read( offset, b, off, len );
		offset += len;
	}

	public int readInt( ) throws IOException
	{
		entry.read( offset, buffer, 0, 4 );
		offset += 4;
		return ( (int) buffer[0] ) << 12 + ( (int) buffer[1] ) << 8 + ( (int) buffer[2] ) << 4 + buffer[3];
	}

	public long readLong( ) throws IOException
	{
		entry.read( offset, buffer, 0, 8 );
		offset += 4;
		return ( (long) buffer[0] ) << 28 + ( (long) buffer[1] ) << 24 + ( (long) buffer[2] ) << 20 + ( (long) buffer[3] ) << 16 + ( (long) buffer[0] ) << 12 + ( (long) buffer[1] ) << 8 + ( (long) buffer[2] ) << 4 + buffer[3];
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
