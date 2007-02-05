
package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.core.util.IOUtil;

public class ArchiveFileTest extends TestCase
{

	static final String ARCHIVE_FOLDER = "./utest/";
	static final String ARCHIVE_FILE = ARCHIVE_FOLDER + "archive.rptdocument";

	public void setUp( )
	{
		new File( ARCHIVE_FOLDER ).mkdirs( );
	}

	public void tearDown( )
	{
		new File( ARCHIVE_FILE ).delete( );
		new File( ARCHIVE_FOLDER ).delete( );
	}

	public void testArchiveFile( ) throws IOException
	{
		ArchiveFile archive = new ArchiveFile( ARCHIVE_FILE, "rw" );
		ArchiveEntry entry = archive.createEntry( "/abc" );

		byte[] b = new byte[4 + 4096 * 2];
		int bufferSize = 4096 * 2;
		long length = 0;
		for ( int i = 0; i < 4096; i++ )
		{
			int size = (int) Math.round( Math.random( ) * bufferSize );
			IOUtil.integerToBytes( size, b );
			size += 4;
			for ( int j = 4; j < size; j++ )
			{
				b[j] = (byte) ( j - 4 );
			}
			entry.write( length, b, 0, size );
			length = length + size;
		}

		assertEquals( length, entry.getLength( ) );
		long pos = 0;
		while ( pos < length )
		{
			entry.read( pos, b, 0, 4 );
			int size = IOUtil.bytesToInteger( b );
			pos += 4;
			entry.read( pos, b, 0, size );
			for ( int j = 0; j < size; j++ )
			{
				assertEquals( (byte) j, b[j] );
			}
			pos += size;
		}
		archive.close( );

		archive = new ArchiveFile( ARCHIVE_FILE, "r" );
		entry = archive.getEntry( "/abc" );
		assertEquals( length, entry.getLength( ) );
		pos = 0;
		while ( pos < length )
		{
			entry.read( pos, b, 0, 4 );
			int size = IOUtil.bytesToInteger( b );
			pos += 4;
			entry.read( pos, b, 0, size );
			for ( int j = 0; j < size; j++ )
			{
				assertEquals( (byte) j, b[j] );
			}
			pos += size;
		}
		archive.close( );
	}

	public void testEntries( ) throws IOException
	{
		ArchiveFile archive = new ArchiveFile( ARCHIVE_FILE, "rw" );
		int entryCount = 1024;
		byte[] b = new byte[entryCount];
		for ( int i = 0; i < entryCount; i++ )
		{
			ArchiveEntry entry = archive.createEntry( "/entry/" + i );
			entry.write( 0, b, 0, i );
		}

		for ( int i = 0; i < entryCount; i++ )
		{
			ArchiveEntry entry = archive.getEntry( "/entry/" + i );
			assertTrue( entry != null );
			assertEquals( i, entry.getLength( ) );

		}
		archive.close( );

		archive = new ArchiveFile( ARCHIVE_FILE, "r" );
		List entries = archive.listEntries( "/" );
		assertEquals( entryCount, entries.size( ) );
		for ( int i = 0; i < entryCount; i++ )
		{
			ArchiveEntry entry = archive.getEntry( "/entry/" + i );
			assertTrue( entry != null );
			assertEquals( i, entry.getLength( ) );
		}
	}
}
