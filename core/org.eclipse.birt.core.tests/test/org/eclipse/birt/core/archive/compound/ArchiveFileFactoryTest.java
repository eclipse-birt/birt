/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;


public class ArchiveFileFactoryTest extends TestCase
{
	static final int TEST_COUNT = 50;

	public void testCreateAndOpenArchive( ) throws IOException
	{
		IArchiveFileFactory factory = new ArchiveFileFactory( );
		IArchiveFile writeArchive = factory.createArchive( "archiveId",
				"fileName" );
		byte[] mes = new byte[TEST_COUNT * 2];
		for ( int index = 0; index < TEST_COUNT; index++ )
		{
			ArchiveEntry entry = writeArchive.createEntry( "/entry/" + index );
			entry.write( 0, mes, 0, index );
		}
		writeArchive.close( );

		IArchiveFile readArchive = factory.openArchive( "fileName", "r" );
		assertEquals( "archiveId", readArchive.getSystemId( ) );
		assertEquals( null, readArchive.getDependId( ) );
		for ( int index = 0; index < TEST_COUNT; index++ )
		{
			ArchiveEntry entry = readArchive.getEntry( "/entry/" + index );
			assertTrue( entry != null );
			assertTrue( entry.getLength( ) == index );
		}
		readArchive.close( );
	}
	
	public void testCreateAndOpenView( ) throws IOException
	{
		IArchiveFileFactory factory = new ArchiveFileFactory( );
		IArchiveFile dependArchive = factory.createArchive( "archiveId",
				"archiveId" );
		byte[] mes = new byte[TEST_COUNT * 2];
		for ( int index = 0; index < 10; index++ )
		{
			ArchiveEntry entry = dependArchive
					.createEntry( "/entry/1." + index );
			entry.write( 0, mes, 0, index );
		}
		IArchiveFile viewArchive = factory.createView( "viewFileName",
				"viewFileName", dependArchive );
		for ( int index = 10; index < 20; index++ )
		{
			ArchiveEntry entry = viewArchive.createEntry( "/entry/2." + index );
			entry.write( 0, mes, 0, index );
		}
		viewArchive.flush( );
		viewArchive.close();
		
		IArchiveFile openView = factory.openView( "viewFileName", "r",
				dependArchive );
		assertEquals( "archiveId", dependArchive.getSystemId( ) );
		assertEquals( null, dependArchive.getDependId( ) );
		assertEquals( "viewFileName", viewArchive.getSystemId( ) );
		assertEquals( "archiveId", viewArchive.getDependId( ) );
		for ( int index = 0; index < 10; index++ )
		{
			ArchiveEntry entry = openView.getEntry( "/entry/1." + index );
			assertTrue( entry != null );
			assertTrue( entry.getLength( ) == index );
		}
		for ( int index = 10; index < 20; index++ )
		{
			ArchiveEntry entry = openView.getEntry( "/entry/2." + index );
			assertTrue( entry != null );
			assertTrue( entry.getLength( ) == index );
		}
		openView.close( );
		dependArchive.close( );

		IArchiveFile openView2 = factory.openArchive( "viewFileName", "r");
		assertEquals( "viewFileName", openView2.getSystemId( ) );
		assertEquals( "archiveId", openView2.getDependId( ) );
		
		openView2.close( );
	}
}
