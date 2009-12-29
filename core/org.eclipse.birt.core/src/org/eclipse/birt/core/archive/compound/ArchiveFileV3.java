/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.archive.compound.v3.Ext2Entry;
import org.eclipse.birt.core.archive.compound.v3.Ext2File;
import org.eclipse.birt.core.archive.compound.v3.Ext2FileSystem;
import org.eclipse.birt.core.archive.compound.v3.Ext2Node;

public class ArchiveFileV3 implements IArchiveFile
{

	public static final String PROPERTY_SYSTEM_ID = "archive.system-id";
	public static final String PROPERTY_DEPEND_ID = "archive.depened-id";

	protected Ext2FileSystem fs;

	public ArchiveFileV3( String fileName, String mode ) throws IOException

	{
		this( fileName, null, mode );
	}

	public ArchiveFileV3( String fileName, RandomAccessFile rf, String mode )
			throws IOException

	{
		fs = new Ext2FileSystem( fileName, rf, mode );
		fs.setCacheManager( ArchiveFile.systemCacheManager );
	}

	public void close( ) throws IOException
	{
		if ( fs != null )
		{
			fs.close( );
			fs = null;
		}
	}

	public void setSystemId( String id )
	{
		fs.setProperty( PROPERTY_SYSTEM_ID, id );
	}

	public void setDependId( String id )
	{
		fs.setProperty( PROPERTY_DEPEND_ID, id );
	}

	@Override
	public ArchiveEntry createEntry( String name ) throws IOException
	{
		Ext2File file = fs.createFile( name );
		return new ArchiveEntryV3( file );
	}

	@Override
	public boolean exists( String name )
	{
		return fs.existFile( name );
	}

	@Override
	public void flush( ) throws IOException
	{
	}

	@Override
	public String getDependId( )
	{
		return fs.getProperty( PROPERTY_DEPEND_ID );
	}

	public ArchiveEntry openEntry( String name ) throws IOException
	{
		if ( fs.existFile( name ) )
		{
			Ext2File file = fs.openFile( name );
			return new ArchiveEntryV3( file );
		}
		throw new FileNotFoundException( name );
	}

	@Override
	public String getName( )
	{
		return fs.getFileName( );
	}

	@Override
	public String getSystemId( )
	{
		return fs.getProperty( PROPERTY_SYSTEM_ID );
	}

	@Override
	public int getUsedCache( )
	{
		return fs.getUsedCacheSize( ) * 4096;
	}

	@Override
	public List listEntries( String namePattern )
	{
		ArrayList<String> files = new ArrayList<String>( );
		for ( String file : fs.listFiles( ) )
		{
			if ( file.startsWith( namePattern ) )
			{
				files.add( file );
			}
		}
		return files;
	}

	@Override
	public Object lockEntry( String name ) throws IOException
	{
		Ext2Entry entry = fs.getEntry( name );
		if ( entry != null )
		{
			return entry;
		}
		throw new FileNotFoundException( name );
	}

	@Override
	public void refresh( ) throws IOException
	{
	}

	@Override
	public boolean removeEntry( String name ) throws IOException
	{
		fs.removeFile( name );
		return true;
	}

	@Override
	public void save( ) throws IOException
	{
		fs.flush( );
		fs.setRemoveOnExit( false );
	}

	@Override
	public void setCacheSize( int cacheSize )
	{
		fs.setCacheSize( cacheSize );
	}

	@Override
	public void unlockEntry( Object locker ) throws IOException
	{
		assert ( locker instanceof Ext2Node );
	}

	private static class ArchiveEntryV3 extends ArchiveEntry
	{

		Ext2File file;

		ArchiveEntryV3( Ext2File file )
		{
			this.file = file;
		}

		public String getName( )
		{
			return file.getName( );
		}

		public long getLength( ) throws IOException
		{
			return file.length( );
		}

		public void close( ) throws IOException
		{
			file.close( );
		}

		@Override
		public void flush( ) throws IOException
		{
		}

		@Override
		public int read( long pos, byte[] b, int off, int len )
				throws IOException
		{
			file.seek( pos );
			return file.read( b, off, len );
		}

		@Override
		public void refresh( ) throws IOException
		{
		}

		@Override
		public void setLength( long length ) throws IOException
		{
			file.setLength( length );
		}

		@Override
		public void write( long pos, byte[] b, int off, int len )
				throws IOException
		{
			file.seek( pos );
			file.write( b, off, len );
		}
	}
}
