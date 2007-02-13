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

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;

/**
 * An OutputStream wraper for RandomAccessStreamImpl.
 * 
 */

public class ArchiveEntryOutputStream extends RAOutputStream
{

	/** the stream item */

	protected ArchiveEntry entry;

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
	ArchiveEntryOutputStream( ArchiveEntry entry )
	{
		this.entry = entry;
		this.offset = 0;
	}

	public long getOffset( ) throws IOException
	{
		return offset;
	}

	public void seek( long localPos ) throws IOException
	{
		entry.ensureSize( localPos );
		offset = localPos;
	}

	private byte[] b = new byte[8];

	public void write( int b ) throws IOException
	{
		this.b[0] = (byte) b;
		entry.write( offset, this.b, 0, 1 );
		offset++;
	}

	public void writeInt( int value ) throws IOException
	{
		IOUtil.integerToBytes( value, b );
		entry.write( offset, b, 0, 4 );
		offset += 4;
	}

	public void writeLong( long value ) throws IOException
	{
		IOUtil.longToBytes( value, b );
		entry.write( offset, b, 0, 8 );
		offset += 8;
	}

	public void write( byte b[], int off, int len ) throws IOException
	{
		entry.write( offset, b, off, len );
		offset += len;
	}
}
