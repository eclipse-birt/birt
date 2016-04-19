
package org.eclipse.birt.core.archive.compound;

import java.io.IOException;

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class ArchiveEntryInputStreamTest
{

	long STREAM_SIZE = 40960;
	int TEST_COUNT = 1000;
	@Test
    public void testInputStream( ) throws IOException
	{
		ArchiveFile af = new ArchiveFile( "temp", "rwt" );
		ArchiveWriter writer = new ArchiveWriter( af );
		writer.initialize( );
		RAOutputStream out = writer.createRandomAccessStream( "/test" );
		for ( int i = 0; i < STREAM_SIZE; i++ )
		{
			out.write( (int) ( i % 256 ) );
		}
		out.close( );
		writer.finish( );

		ArchiveReader reader = new ArchiveReader( af );
		reader.open( );
		RAInputStream in = reader.getStream( "/test" );

		// test read
		for ( int i = 0; i < TEST_COUNT; i++ )
		{
			long pos = (long) ( Math.random( ) * STREAM_SIZE );
			in.seek( pos );
			assertEquals( STREAM_SIZE - pos, in.available( ) );
			int v = in.read( );
			assertEquals( goldenByte( pos ), v );
		}

		// test read int
		for ( int i = 0; i < TEST_COUNT; i++ )
		{
			long pos = (long) ( Math.random( ) * ( STREAM_SIZE - 4 ) );
			in.seek( pos );
			int v = in.readInt( );
			assertEquals( goldenInt( pos ), v );
		}

		// test read long
		for ( int i = 0; i < TEST_COUNT; i++ )
		{
			long pos = (long) ( Math.random( ) * ( STREAM_SIZE - 8 ) );
			in.seek( pos );
			long v = in.readLong( );
			assertEquals( goldenLong( pos ), v );
		}

		// test read
		for ( int i = 0; i < TEST_COUNT; i++ )
		{
			byte[] buffer = new byte[1023];
			long pos = (long) ( Math.random( ) * ( STREAM_SIZE - 1 ) );
			in.seek( pos );
			int readSize = in.read( buffer );
			checkBytes( buffer, readSize, pos );
		}

		// test read fully
		// test read
		for ( int i = 0; i < TEST_COUNT; i++ )
		{
			byte[] buffer = new byte[1023];
			long pos = (long) ( Math.random( ) * ( STREAM_SIZE - 1023 ) );
			in.seek( pos );
			in.readFully( buffer, 0, 1023 );
			checkBytes( buffer, 1023, pos );
		}

		reader.close( );

		af.close( );

	}

	private int goldenByte( long pos )
	{
		return (int) ( pos % 256 );
	}

	private int goldenInt( long pos )
	{
		byte[] b = new byte[4];
		for ( int i = 0; i < 4; i++ )
		{
			b[i] = (byte) ( ( pos + i ) % 256 );
		}
		return ArchiveUtil.bytesToInteger( b );
	}

	private long goldenLong( long pos )
	{
		byte[] b = new byte[8];
		for ( int i = 0; i < 8; i++ )
		{
			b[i] = (byte) ( ( pos + i ) % 256 );
		}
		return ArchiveUtil.bytesToLong( b );
	}

	private void checkBytes( byte[] bytes, int size, long pos )
	{
		for ( int i = 0; i < size; i++ )
		{
			if ( ( (byte) ( ( pos + i ) % 256 ) ) != bytes[i] )
			{
				System.out.println( i );
			}
			assertEquals( (byte) ( ( pos + i ) % 256 ), bytes[i] );
		}
	}

}
