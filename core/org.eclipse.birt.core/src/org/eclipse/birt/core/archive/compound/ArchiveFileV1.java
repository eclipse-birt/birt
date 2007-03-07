
package org.eclipse.birt.core.archive.compound;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class ArchiveFileV1 implements IArchiveFile
{

	String archiveName;

	RandomAccessFile rf;

	/**
	 * streams in the file. each entry is a stream name and start, end pos pair.
	 */
	private HashMap lookupMap = new HashMap( );

	ArchiveFileV1( String archiveName, RandomAccessFile rf ) throws IOException
	{
		this.archiveName = archiveName;
		this.rf = rf;
		try
		{
			if ( rf == null )
			{
				rf = new RandomAccessFile( archiveName, "r" );
			}
			readFileTable( );
		}
		finally
		{
			close( );
		}
	}

	ArchiveFileV1( String archiveName ) throws IOException
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.compound.ArchiveFile#createEntry(java.lang.String)
	 */
	public synchronized ArchiveEntry createEntry( String name )
			throws IOException
	{
		throw new IOException( "read only archive" );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.compound.ArchiveFile#exists(java.lang.String)
	 */
	public boolean exists( String name )
	{
		return lookupMap.containsKey( name );
	}

	public void flush( ) throws IOException
	{
		throw new IOException( "read only archive" );
	}

	public ArchiveEntry getEntry( String name )
	{
		return (ArchiveEntryV1) lookupMap.get( name );
	}

	public List listEntries( String namePattern )
	{
		ArrayList list = new ArrayList( );
		Iterator iter = lookupMap.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String name = (String) iter.next( );
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

	public Object lockEntry( ArchiveEntry entry ) throws IOException
	{
		return entry;
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
}
