/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class ArchiveFileV1 implements IArchiveFile
{

	String archiveName;

	RandomAccessFile rf;

	/**
	 * streams in the file. each entry is a stream name and start, end pos pair.
	 */
	private HashMap<String, ArchiveEntryV1> lookupMap = new HashMap<String, ArchiveEntryV1>( );

	public ArchiveFileV1( String archiveName, RandomAccessFile rf )
			throws IOException
	{
		this.archiveName = archiveName;
		this.rf = rf;
		try
		{
			if ( this.rf == null )
			{
				this.rf = new RandomAccessFile( archiveName, "r" );
			}
			readFileTable( );
		}
		catch ( IOException ex )
		{
			close( );
			throw ex;
		}
	}

	public ArchiveFileV1( String archiveName ) throws IOException
	{
		this( archiveName, null );
	}

	/**
	 * read the stream table from the archive file. the stream table is in the
	 * begining of the file, it contains: long: stream section postiton, always
	 * zero. long: entry number. followed by entries in the archive, each entry
	 * contains: utf8: stream name. long[2]: start offset, length.
	 * 
	 * @throws IOException
	 */
	protected void readFileTable( ) throws IOException
	{
		rf.seek( 0 );
		long streamSectionPos = rf.readLong( );
		long entryNumber = rf.readLong( );

		// read lookup map
		for ( long i = 0; i < entryNumber; i++ )
		{
			String name = rf.readUTF( );
			// stream position (and convert it to absolute position)
			long start = rf.readLong( ) + streamSectionPos;
			long length = rf.readLong( );
			// generate map entry
			lookupMap
					.put( name, new ArchiveEntryV1( this, name, start, length ) );
		}
	}

	public String getName( )
	{
		return archiveName;
	}

	public String getDependId( )
	{
		// Do not implement this api
		return null;
	}

	public String getSystemId( )
	{
		// Do not implement this api
		return null;
	}

	public void close( ) throws IOException
	{
		if ( rf != null )
		{
			rf.close( );
			rf = null;
		}
	}

	public void setCacheSize( int cacheSize )
	{
		// V1 doesn't support the cache size
	}

	public int getUsedCache( )
	{
		return 0;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.core.archive.compound.ArchiveFile#createEntry(java.lang
	 * .String)
	 */
	public synchronized ArchiveEntry createEntry( String name )
			throws IOException
	{
		throw new IOException( "read only archive" );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.core.archive.compound.ArchiveFile#exists(java.lang.String
	 * )
	 */
	public boolean exists( String name )
	{
		return lookupMap.containsKey( name );
	}

	public void flush( ) throws IOException
	{
		throw new IOException( "read only archive" );
	}

	public ArchiveEntry openEntry( String name ) throws IOException
	{
		ArchiveEntryV1 entry = lookupMap.get( name );
		if ( entry != null )
		{
			return entry;
		}
		throw new FileNotFoundException( name );
	}

	public List<String> listEntries( String namePattern )
	{
		ArrayList<String> list = new ArrayList<String>( );
		Iterator<String> iter = lookupMap.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String name = iter.next( );
			if ( namePattern == null || name.startsWith( namePattern ) )
			{
				list.add( name );
			}
		}
		return list;
	}

	public synchronized void refresh( ) throws IOException
	{
	}

	public synchronized boolean removeEntry( String name ) throws IOException
	{
		throw new IOException( "read only archive" );
	}

	public Object lockEntry( String stream ) throws IOException
	{
		ArchiveEntryV1 entry = lookupMap.get( stream );
		if ( entry != null )
		{
			return entry;
		}
		throw new FileNotFoundException( "not exist stream " + stream );
	}

	public void unlockEntry( Object locker ) throws IOException
	{
		if ( !( locker instanceof ArchiveEntryV1 ) )
		{
			throw new IOException( "Invalide lock type:" + locker );
		}
	}

	synchronized int read( long pos, byte[] b, int off, int len )
			throws IOException
	{
		rf.seek( pos );
		return rf.read( b, off, len );
	}

	synchronized void write( long pos, byte[] b, int off, int len )
			throws IOException
	{
		throw new IOException( "read only archive" );
	}

	public synchronized void save( ) throws IOException
	{
		throw new IOException( "read only archive" );
	}
}
