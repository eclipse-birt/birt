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

import java.io.EOFException;
import java.io.IOException;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

/**
 * RAInputStream implementation based on the ArchiveEntry.
 * 
 */
public class ArchiveEntryInputStream extends RAInputStream
{

	/** the archive entry */
	private ArchiveEntry entry;

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
	}

	public void close( ) throws IOException
	{
		if ( entry != null )
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#read()
	 */
	public int read( ) throws IOException
	{
		return entry.read( );
	}

	public int available( ) throws IOException
	{
		long av = entry.getLength( ) - getOffset( );;
		if ( av > Integer.MAX_VALUE )
		{
			return Integer.MAX_VALUE;
		}
		return (int) av;
	}

	public long getOffset( ) throws IOException
	{
		return entry.getPosition( );
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
		return entry.read( b, off, len );
	}

	public int readInt( ) throws IOException
	{
		return entry.readInt( );
	}

	public long readLong( ) throws IOException
	{
		return entry.readLong( );
	}

	public void refresh( ) throws IOException
	{
		if ( entry != null )
		{
			entry.refresh( );
		}
	}

	public void seek( long localPos ) throws IOException
	{
		if ( localPos < 0 )
		{
			throw new IOException( CoreMessages.getFormattedString(
					ResourceConstants.INVALID_SEEK_OFFSET,
					new Object[]{localPos} ) );
		}

		if ( localPos >= entry.getLength( ) )
		{
			throw new EOFException(
					CoreMessages
							.getString( ResourceConstants.EXCEED_FILE_LENGTH ) );
		}
		entry.seek( localPos );
		return;
	}
}
