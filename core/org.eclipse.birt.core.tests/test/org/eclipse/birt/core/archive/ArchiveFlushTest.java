/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

import org.eclipse.birt.core.archive.compound.ArchiveEntry;
import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.junit.Test;

import junit.framework.TestCase;

public class ArchiveFlushTest extends TestCase
{

    /**
     * It is used to test flush.
     *
     * Once the archive file is flushed, the stream should be flushed
     * automatically.
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
	@Test
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

    /**
     * Demonstrate how to use the archive entry and input/output stream
     */
	@Test
    public void testReadWrite( ) throws IOException
    {
        ArchiveFile af = new ArchiveFile( "test.dat", "rw+" );
        try
        {
            ArchiveWriter writer = new ArchiveWriter( af );
            ArchiveReader reader = new ArchiveReader( af );
            // using the entry to write data
            ArchiveEntry entry1 = af.createEntry( "/test1" );
            entry1.write( 0, new byte[]{0, 0, 0, 1}, 0, 4 );
            assertEquals( 4, entry1.getLength( ) );
            // entry1 has no buffer, so we can read the data directly
            // using the stream to read the data
            RAInputStream in = reader.getInputStream( "/test1" );
            assertEquals( 1, in.readInt( ) );
            in.close( );
            entry1.close( );

            // using the stream to write the data
            RAOutputStream out = writer.createOutputStream( "/test2" );
            out.writeInt( 2 );
            // not flushed, so the length is still ZERO

            in = reader.getInputStream( "/test2" );
            assertEquals( 0, in.available( ) );

            // after flush, we can read it out
            af.flush( );
            assertEquals( 2, in.readInt( ) );
            out.writeInt( 3 );
            out.close( );
            assertEquals( 3, in.readInt( ) );
        }
        finally
        {
            af.close( );
            new File( "test.dat" ).delete( );
        }
    }
}
