
package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ArchiveRemoveTest extends TestCase
{

	static final String ARCHIVE_FOLDER = "./utest/";
	static final String ARCHIVE_FILE = ARCHIVE_FOLDER + "archive.rptdocument";
	@Before
    public void setUp()
	{
		new File( ARCHIVE_FOLDER ).mkdirs( );
	}
	@After
    public void tearDown()
	{
		new File( ARCHIVE_FILE ).delete( );
		new File( ARCHIVE_FOLDER ).delete( );
	}
	@Test
    public void testArchiveFileRemove( ) throws IOException
	{
		ArchiveFile archive = new ArchiveFile( ARCHIVE_FILE, "rw" );
		archive.setCacheSize( 64 * 1024 );

		try
		{
			createEntry( archive, "ENTRY", 9 );
			archive.flush( );
			assertEquals( 12, getFileSize( archive ) );

			archive.removeEntry( "ENTRY" );
			createEntry( archive, "ENTRY", 10 );
			assertEquals( 14, getFileSize( archive ) );

			archive.removeEntry( "ENTRY" );
			createEntry( archive, "ENTRY", 1033 );
			assertEquals( 1037, getFileSize( archive ) );

			archive.removeEntry( "ENTRY" );
			createEntry( archive, "ENTRY", 1034 );
			assertEquals( 1040, getFileSize( archive ) );

			archive.removeEntry( "ENTRY" );
			createEntry( archive, "ENTRY", 1035 );
			assertEquals( 1041, getFileSize( archive ) );

		}
		finally
		{
			archive.close( );
		}
	}
	@Test
    public void testArchiveFileRandomRemove( ) throws IOException
	{
		ArchiveFile archive = new ArchiveFile( ARCHIVE_FILE, "rw" );
		archive.setCacheSize( 64 * 1024 );

		try
		{
			for ( int i = 0; i < 32; i++ )
			{
				createEntry( archive, "ENTRY", 9 + i * 2);
				archive.removeEntry( "ENTRY" );
				if ( i % 10 == 0 )
				{
					System.out.println( i + ":"
							+ ( archive.getUsedCache( ) / 4096 ) + "/"
							+ ( ArchiveFile.getTotalUsedCache( ) / 4096 ) );
				}
			}
		}
		finally
		{
			archive.close( );
		}
	}

	protected void createEntry( IArchiveFile af, String name, int block )
			throws IOException
	{
		ArchiveEntry entry = af.createEntry( name );
		try
		{
			long position = 0;
			byte[] buffer = new byte[4096];
			for ( int j = 0; j < block; j++ )
			{
				entry.write( position, buffer, 0, buffer.length );
				position += buffer.length;
			}
		}
		finally
		{
			entry.close( );
		}
	}

	int getFileSize( IArchiveFile af ) throws IOException
	{
		af.flush( );
		String filename = af.getName( );
		return (int) ( ( new File( filename ).length( ) + 4095 ) / 4096 );
	}
}
