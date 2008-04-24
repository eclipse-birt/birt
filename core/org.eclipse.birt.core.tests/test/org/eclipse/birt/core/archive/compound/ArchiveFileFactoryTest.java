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
	
	static final String TEST_FOLDER = "./utest/";
	static final String ARCHIVE_FILE = TEST_FOLDER + "archiveFileName";
	static final String VIEW_FILE = TEST_FOLDER + "viewFileName";
	static final String ARCHIVE_ID = "archiveId";
	static final String VIEW_ID = "viewId";
	
	public void setUp( )
	{
		new File( TEST_FOLDER ).mkdirs( );
	}
	
	public void tearDown( )
	{
		new File( ARCHIVE_FILE ).delete( );
		new File( VIEW_FILE ).delete( );
		new File( TEST_FOLDER ).delete( );
	}
	
	public void testCreateAndOpenArchive( ) throws IOException
	{
		IArchiveFileFactory factory = new ArchiveFileFactory( );
		IArchiveFile writeArchive = factory.createArchive( ARCHIVE_ID );
		byte[] mes = new byte[TEST_COUNT * 2];
		for ( int index = 0; index < TEST_COUNT; index++ )
		{
			ArchiveEntry entry = writeArchive.createEntry( "/entry/" + index );
			entry.write( 0, mes, 0, index );
		}
		writeArchive.close( );

		IArchiveFile readArchive = factory.openArchive( ARCHIVE_ID, "r" );
		assertEquals( ARCHIVE_ID, readArchive.getSystemId( ) );
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
		IArchiveFile dependArchive = factory.createArchive( ARCHIVE_ID);
		byte[] mes = new byte[TEST_COUNT * 2];
		for ( int index = 0; index < 10; index++ )
		{
			ArchiveEntry entry = dependArchive
					.createEntry( "/entry/1." + index );
			entry.write( 0, mes, 0, index );
		}
		IArchiveFile viewArchive = factory.createView( VIEW_ID,
				dependArchive );
		for ( int index = 10; index < 20; index++ )
		{
			ArchiveEntry entry = viewArchive.createEntry( "/entry/2." + index );
			entry.write( 0, mes, 0, index );
		}
		viewArchive.flush( );
		viewArchive.close();
		
		IArchiveFile openView = factory.openView( VIEW_ID, "r",
				dependArchive );
		assertEquals( ARCHIVE_ID, dependArchive.getSystemId( ) );
		assertEquals( null, dependArchive.getDependId( ) );
		assertEquals( VIEW_ID, viewArchive.getSystemId( ) );
		assertEquals( ARCHIVE_ID, viewArchive.getDependId( ) );
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

		IArchiveFile openView2 = factory.openArchive( VIEW_ID, "r");
		assertEquals( VIEW_ID, openView2.getSystemId( ) );
		assertEquals( ARCHIVE_ID, openView2.getDependId( ) );
		
		openView2.close( );
	}
}
