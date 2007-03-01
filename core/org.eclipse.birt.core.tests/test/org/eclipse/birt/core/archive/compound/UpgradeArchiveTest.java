
package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

import org.eclipse.birt.core.archive.ArchiveUtil;

public class UpgradeArchiveTest extends TestCase
{

	public void setUp( )
	{
		new File( "./utest/" ).mkdir( );
	}

	public void tearDown( )
	{
		new File( "./utest/" ).delete( );
	}

	public void testUpgrade( ) throws IOException
	{
		InputStream in = getClass( ).getClassLoader( ).getResourceAsStream(
				"org/eclipse/birt/core/archive/compound/V2_1_1.rptarchive" );
		OutputStream out = new FileOutputStream( "./utest/test.rptarchive" );
		byte[] buffer = new byte[4096];
		int size = in.read( buffer );
		do
		{
			out.write( buffer, 0, size );
			size = in.read( buffer );
		} while ( size != -1 );

		out.close( );
		in.close( );

		ArchiveFile af = new ArchiveFile( "./utest/test.rptarchive", "rw+" );

		for ( int i = 0; i < 128; i++ )
		{
			ArchiveEntry entry = af.getEntry( "/" + i );
			assertEquals( i * 4, entry.getLength( ) );
			for ( int j = 0; j < i; j++ )
			{
				entry.read( j * 4, buffer, 0, 4 );
				int v = ArchiveUtil.bytesToInteger( buffer );
				assertEquals( j, v );
			}
		}

		af.close( );

		new File( "./utest/test.rptarchive" ).delete( );
	}
}
