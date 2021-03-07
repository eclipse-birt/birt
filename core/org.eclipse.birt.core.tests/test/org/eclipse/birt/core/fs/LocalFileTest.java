/*******************************************************************************
 * Copyright (c) 2018 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import junit.framework.TestCase;

/**
 * 
 */

public class LocalFileTest extends TestCase
{

	private static final String TEST_FOLDER = "./utest/fs/testfiles/";

	@Override
	protected void setUp( ) throws Exception
	{
		File folder = new File( TEST_FOLDER );
		if ( !folder.exists( ) )
		{
			folder.mkdirs( );
		}
		new File( TEST_FOLDER + "abc.txt" ).createNewFile( );
	}

	@Override
	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}

	public void testGetName( )
	{
		assertEquals( "abc.txt",
				new LocalFile( new File( TEST_FOLDER + "abc.txt" ) )
						.getName( ) );
	}

	public void testGetPath( )
	{
		String expectedPath = String.join(File.separator, ".", "utest", "fs", "testfiles", "abc.txt");
		assertEquals(expectedPath,
				new LocalFile( new File( TEST_FOLDER + "abc.txt" ) )
						.getPath( ) );
	}

	public void testIsDirectory( )
	{
		assertEquals( true,
				new LocalFile( new File( TEST_FOLDER ) ).isDirectory( ) );
		assertEquals( false,
				new LocalFile( new File( TEST_FOLDER + "/abc.txt" ) )
						.isDirectory( ) );
	}

	public void testExists( )
	{
		assertEquals( true,
				new LocalFile( new File( TEST_FOLDER + "/abc.txt" ) )
						.exists( ) );
		assertEquals( false,
				new LocalFile( new File( TEST_FOLDER + "/abcd.txt" ) )
						.exists( ) );
	}

	public void testListFiles( )
	{
		IFile[] files = new LocalFile( new File( TEST_FOLDER ) ).listFiles( );
		assertEquals( 1, files.length );
		assertEquals( "abc.txt", files[0].getName( ) );
	}

	public void testIsAbsolute( )
	{
		IFile file = new LocalFile( new File( TEST_FOLDER + "/abc.txt" ) );
		assertEquals( false, file.isAbsolute( ) );
		try
		{
			assertEquals( true,
					new LocalFile( new File( file.toURL( ).toURI( ) ) )
							.isAbsolute( ) );
		}
		catch ( URISyntaxException e )
		{
			e.printStackTrace( );
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}
	}

	public void testGetParent( )
	{
		assertEquals( "testfiles",
				new LocalFile( new File( TEST_FOLDER + "/abc.txt" ) )
						.getParent( ).getName( ) );
	}

	public void testToURL( )
	{
		IFile file = new LocalFile( new File( TEST_FOLDER + "/abc.txt" ) );
		try
		{
			assertEquals( true,
					new LocalFile( new File( file.toURL( ).toURI( ) ) )
							.isAbsolute( ) );
		}
		catch ( URISyntaxException e )
		{
			e.printStackTrace( );
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}
	}

	public void testDelete( )
	{
		try
		{
			// delete file
			File newFile = new File( TEST_FOLDER + "ccc.txt" );
			newFile.createNewFile( );
			IFile newIFile = new LocalFile( newFile );
			assertEquals( true, newIFile.exists( ) );
			newIFile.delete( );
			assertEquals( false, newIFile.exists( ) );

			// delete folder
			newIFile = new LocalFile( new File( TEST_FOLDER + "folder" ) );
			newIFile.mkdirs( );
			new File( TEST_FOLDER + "folder/abc.txt" ).createNewFile( );
			new File( TEST_FOLDER + "folder/abcd.txt" ).createNewFile( );
			assertEquals( true, newIFile.exists( ) );
			newIFile.delete( );
			assertEquals( false, newIFile.exists( ) );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
	}

	public void testMkdirs( )
	{
		IFile newIFile = new LocalFile( new File( TEST_FOLDER + "folder" ) );
		try
		{
			assertEquals( true, newIFile.mkdirs( ) );
			assertEquals( true, newIFile.exists( ) );
			newIFile.delete( );
			assertEquals( false, newIFile.exists( ) );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
	}

	public void testCreateInputAndOutputStream( )
	{

		String testText = "This is a test!";
		IFile file = new LocalFile( new File( TEST_FOLDER + "/abc.txt" ) );
		try
		{
			// Test output stream
			OutputStream os = file.createOutputStream( );
			os.write( testText.getBytes( ) );
			os.flush( );
			os.close( );

			// Test input stream
			InputStream is = file.createInputStream( );
			byte[] content = new byte[256];
			int len = is.read( content );
			is.close( );
			assertEquals( testText, new String( content, 0, len ) );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
	}
}
