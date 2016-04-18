/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
import java.io.RandomAccessFile;

import org.eclipse.birt.core.archive.compound.ArchiveEntry;
import org.eclipse.birt.core.archive.compound.ArchiveFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class ArchiveFileSaveTest
{
	@Test
    public void testSave( ) throws IOException
	{
		ArchiveFile file = new ArchiveFile( "test.archive", "rwt" );
		ArchiveEntry entry = file.createEntry( "/name" );
		byte[] bytes = new byte[255];
		for ( int i = 0; i < 255; i++ )
		{
			bytes[i] = (byte) i;
		}
		entry.write( 0, bytes, 0, 255 );
		file.save( );
		file.close( );
		copyFile( "test.archive", "new.archive" );
		assertTrue( new File( "test.archive" ).exists( ) );

		file = new ArchiveFile( "new.archive", "r" );
		entry = file.openEntry( "/name" );
		assertTrue( entry != null );
		entry.read( 0, bytes, 0, 255 );
		for ( int i = 0; i < 255; i++ )
		{
			assertEquals( bytes[i], (byte) i );
		}
		entry.close( );
		file.close( );

		new File( "test.archive" ).delete( );
		new File( "new.archive" ).delete( );
	}

	void copyFile( String src, String tgt ) throws IOException
	{
		RandomAccessFile srcFile = new RandomAccessFile( src, "r" );
		RandomAccessFile tgtFile = new RandomAccessFile( tgt, "rw" );
		byte[] bytes = new byte[(int) srcFile.length( )];
		srcFile.read( bytes );
		tgtFile.setLength( 0 );
		tgtFile.write( bytes );
		srcFile.close( );
		tgtFile.close( );
	}
}
