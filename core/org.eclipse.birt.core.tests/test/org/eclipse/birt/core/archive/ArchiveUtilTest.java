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

import junit.framework.TestCase;

public class ArchiveUtilTest extends TestCase
{

    static final String ARCHIVE_FILE = "./utest/test.file";
    static final String ARCHIVE_FOLDER = "./utest/test.folder";

    protected void removeFile( File file )
    {
        if ( file.isDirectory( ) )
        {
            File[] files = file.listFiles( );
            for ( int i = 0; i < files.length; i++ )
            {
                removeFile( files[i] );
            }
        }
        file.delete( );
    }

    public void testArchive( ) throws IOException
    {
        removeFile( new File( ARCHIVE_FOLDER ) );
        removeFile( new File( ARCHIVE_FILE ) );
        // create a folder archive
        FolderArchiveWriter writer = new FolderArchiveWriter( ARCHIVE_FOLDER );
        writer.initialize( );
        createArchive( writer );
        writer.finish( );
        // zip it to file
        ArchiveUtil.archive( ARCHIVE_FOLDER, ARCHIVE_FILE );
        // test the file archive
        FileArchiveReader reader = new FileArchiveReader( ARCHIVE_FILE );
        reader.open( );
        checkArchive( reader );
        reader.close( );

    }

    public void testExpand( ) throws IOException
    {
        removeFile( new File( ARCHIVE_FOLDER ) );
        removeFile( new File( ARCHIVE_FILE ) );
        // create a file archive
        FileArchiveWriter writer = new FileArchiveWriter( ARCHIVE_FILE );
        writer.initialize( );
        createArchive( writer );
        writer.finish( );
        // unzip it to folder
        ArchiveUtil.expand( ARCHIVE_FILE, ARCHIVE_FOLDER );
        // test the folderarchive
        FolderArchiveReader reader = new FolderArchiveReader( ARCHIVE_FOLDER );
        reader.open( );
        checkArchive( reader );
        reader.close( );
    }

    protected void createArchive( IDocArchiveWriter writer ) throws IOException
    {
        RAOutputStream out = writer.createRandomAccessStream( "/core.txt" );
        out.writeLong( 100L );
        out.seek( 1024 * 1024 );
        out.writeLong( 100L );
        out.close( );
        out = writer.createRandomAccessStream( "/folder/core.txt" );
        out.seek( 100 );
        out.writeLong( 100 );
        out.close( );
    }

    protected void checkArchive( IDocArchiveReader reader ) throws IOException
    {
        RAInputStream in = reader.getStream( "/core.txt" );
        assertTrue( in != null );
        assertEquals( 100, in.readLong( ) );
        in.seek( 1024 * 1024 );
        assertEquals( 100, in.readLong( ) );
        in.close( );
        in = reader.getStream( "/folder/core.txt" );
        assertTrue( in != null );
        in.seek( 100 );
        assertEquals( 100, in.readLong( ) );
        in.close( );
    }
    
    public void testSplit()
    {
        assertEquals(new String[]{""}, ArchiveUtil.split("", '/'));
        assertEquals(new String[]{"",""}, ArchiveUtil.split("/", '/'));
        assertEquals(new String[]{"","",""}, ArchiveUtil.split("//", '/'));
        assertEquals(new String[]{"abc"}, ArchiveUtil.split("abc", '/'));
        assertEquals(new String[]{"abc", ""}, ArchiveUtil.split("abc/", '/'));
        assertEquals(new String[]{"","abc" }, ArchiveUtil.split("/abc", '/'));
        assertEquals(new String[]{"","abc", "" }, ArchiveUtil.split("/abc/", '/'));
    }

    public void testRelativePath( )
    {
        String[][] tests = new String[][]{new String[]{"/test", "/", "/test"},
                new String[]{"/test/", "/root/", "/root/test/"},
                new String[]{"/test/", "/root", "/root/test/"},
                new String[]{"/test", "/root/", "/root/test"},
                new String[]{"/test", "/root", "/root/test"},
                new String[]{"/test", "\\root/", "/root\\test"},
                new String[]{"/test", "\\root", "/root\\test"},
                new String[]{"/", "/root", "/root/"},
                new String[]{"/", "/root/", "/root/"},
                new String[]{"/", "/root/", "/root"},
                new String[]{"/", "/root", "/root"},};
        for ( String[] test : tests )
        {
            String relative = test[0];
            String root = test[1];
            String path = test[2];
            assertEquals( relative, ArchiveUtil.getRelativePath( root, path ) );
        }
    }

    public void testFullPath( )
    {
        String[][] tests = new String[][]{new String[]{"/root/", "/root", "/"},
                new String[]{"/root/", "/root/", "/"},
                new String[]{"/root/", "/root", "/"},
                new String[]{"/root/abc", "/root", "/abc"},
                new String[]{"/root/abc", "/root", "abc"},
                new String[]{"/root/abc", "/root/", "abc"},
                new String[]{"/root/abc", "/root/", "abc"},
                new String[]{"/root/abc/", "/root", "/abc/"},
                new String[]{"/root/abc/", "/root", "abc/"},
                new String[]{"/root/abc/", "/root/", "abc/"},
                new String[]{"/root/abc/", "/root/", "abc/"},};
        for ( String[] test : tests )
        {
            String path = test[0];
            String root = test[1];
            String relative = test[2];
            assertEquals( path, ArchiveUtil.getFullPath( root, relative ) );
        }
    }

    public void testEntryToFile( )
    {
        assertEquals( "/", ArchiveUtil.getFilePath( "/" ) );
        assertEquals( "/test", ArchiveUtil.getFilePath( "/test" ) );
        assertEquals( "/test/", ArchiveUtil.getFilePath( "/test/" ) );
        assertEquals( "/%2F/test/", ArchiveUtil.getFilePath( "//test/" ) );
        assertEquals( "/test/%2F/", ArchiveUtil.getFilePath( "/test//" ) );
        assertEquals( "/%2E", ArchiveUtil.getFilePath( "." ) );
        assertEquals( "/%2E", ArchiveUtil.getFilePath( "/." ) );
        assertEquals( "/%2E%2E", ArchiveUtil.getFilePath( ".." ) );
        assertEquals( "/%2E%2E", ArchiveUtil.getFilePath( "/.." ) );
        assertEquals( "/...", ArchiveUtil.getFilePath( "/..." ) );
        assertEquals( "/%2F/", ArchiveUtil.getFilePath( "//" ) );
        assertEquals( "/%2F/%2F/", ArchiveUtil.getFilePath( "///" ) );
    }

    public void testFileToEntry( )
    {
        assertEquals( "/", ArchiveUtil.getEntryName( "/" ) );
        assertEquals( "/.", ArchiveUtil.getEntryName( "/%2E" ) );
        assertEquals( "/..", ArchiveUtil.getEntryName( "/%2E%2E" ) );
        assertEquals( "/./", ArchiveUtil.getEntryName( "/%2E/" ) );
        assertEquals( "/../", ArchiveUtil.getEntryName( "/%2E%2E/" ) );
        assertEquals( "//", ArchiveUtil.getEntryName( "/%2F/" ) );
//      assertEquals( "/", ArchiveUtil.getEntryName( "/%2F" ) );  invalid entry name
//      assertEquals( "//", ArchiveUtil.getEntryName( "/%2F/%2F" ) ); invalid entry name
        assertEquals( "///", ArchiveUtil.getEntryName( "/%2F/%2F/" ) );
    }

    private void assertEquals( String[] v1, String[] v2 )
    {
        assertEquals( v1.length, v2.length );
        for ( int i = 0; i < v1.length; i++ )
        {
            assertEquals( v1[i], v2[i] );
        }
    }
}
