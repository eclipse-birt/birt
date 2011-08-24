/*******************************************************************************
 * Copyright (c) 2004,2011 Actuate Corporation.
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
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

/**
 * An OutputStream wraper for RandomAccessStreamImpl.
 * 
 */

public class ArchiveEntryOutputStream extends RAOutputStream
{

	protected ArchiveWriter writer;

	/** the stream item */

	protected ArchiveEntry entry;

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
	}

	public long getOffset( ) throws IOException
	{
		return entry.getPosition( );
	}

	public void seek( long localPos ) throws IOException
	{
		if ( localPos < 0 )
		{
			throw new IOException( CoreMessages.getFormattedString(
					ResourceConstants.INVALID_SEEK_OFFSET,
					new Object[]{localPos} ) );
		}
		if ( localPos > entry.getLength( ) )
		{
			entry.setLength( localPos );
		}
		entry.seek( localPos );
	}

	public void write( int b ) throws IOException
	{
		entry.write( b );
	}

	public void writeInt( int value ) throws IOException
	{
		entry.writeInt( value );
	}

	public void writeLong( long value ) throws IOException
	{
		entry.writeLong( value );
	}

	public void write( byte b[], int off, int len ) throws IOException
	{
		entry.write( b, off, len );
	}

	public void flush( ) throws IOException
	{
		if ( entry != null )
		{
			entry.flush( );
		}
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

	public long length( ) throws IOException
	{
		return entry.getLength( );
	}
}
