
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

import junit.framework.TestCase;

public class InputStreamRefreshTest extends TestCase
{
	static final String ARCHIVE_NAME = "./utest/test.archive";
	static final String STREAM_NAME = "/teststream";

	public void setUp( )
	{
		ArchiveUtil.DeleteAllFiles( new File( ARCHIVE_NAME ) );
	}

	public void tearDown( )
	{	ArchiveUtil.DeleteAllFiles( new File( ARCHIVE_NAME ) );
	}

	public void testReaderDuringWriter( ) throws Exception
	{
		FileArchiveWriter writer = new FileArchiveWriter( ARCHIVE_NAME );
		writer.initialize( );
		FileArchiveReader reader = new FileArchiveReader( ARCHIVE_NAME );
		reader.open( );
		RAOutputStream ws = writer.createRandomAccessStream( STREAM_NAME );
		ws.writeInt( 1 );
		ws.flush( );
		RAInputStream rs = reader.getStream( STREAM_NAME );
		assertEquals( 1, rs.readInt( ) );
		ws.seek( 0 );
		ws.writeLong( 2L );
		ws.flush( );
		// test refresh
		rs.refresh( );
		rs.seek( 0 );
		assertEquals( 2L, rs.readLong( ) );
		
		rs.close( );
		ws.close( );
		reader.close( );
		writer.finish( );
	}
}
