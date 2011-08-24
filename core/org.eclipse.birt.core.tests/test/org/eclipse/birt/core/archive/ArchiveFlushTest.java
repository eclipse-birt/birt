/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

import org.eclipse.birt.core.archive.compound.ArchiveEntry;
import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;

public class ArchiveFlushTest extends TestCase
{

	/**
	 * It is used to test flush.
	 *
	 * Once the archive file is flushed, the stream should be flushed
	 * automactially.
	 *
	 * test steps:
	 * <ol>
	 * <li>create a archive file</li>
	 * <li>. create a reader and writer.</li>
	 * <li>create a output stream using the writer and input stream for reader.</li>
	 * <li>write some data into the output stream</li>
	 * <li>read the data from the input stream, different.</li>
	 * <li>flush the archive file</li>
	 * <li>read the data from the input stream, same.</li>
	 * </ol>
	 */
	public void testFlush( ) throws IOException
	{

		ArchiveFile af = new ArchiveFile( "test.dat", "rw+" );
		try
		{
			ArchiveReader reader = new ArchiveReader( af );
			ArchiveWriter writer = new ArchiveWriter( af );
			OutputStream out = writer.createOutputStream( "test" );
			InputStream in = reader.getInputStream( "test" );

			out.write( 12 );
			assertEquals( 0, in.available( ) );
			af.flush( );
			assertEquals( 1, in.available( ) );
			int v = in.read( );
			assertEquals( 12, v );
			v = in.read( );
			assertEquals( -1, v );

			in.close( );
			out.close( );
			reader.close( );
			writer.finish( );
		}
		finally
		{
			af.close( );
			new File( "test.dat" ).delete( );
		}
	}

	public void testReadWrite( ) throws IOException
	{
		ArchiveFile af = new ArchiveFile( "test.dat", "rw+" );
		try
		{
			ArchiveEntry entry = af.createEntry( "test" );
			entry.write( 100 );
			assertEquals( 1, entry.getLength( ) );
			entry.refresh( );

			entry.close( );
		}
		finally
		{
			af.close( );
			new File( "test.dat" ).delete( );
		}

	}
}
