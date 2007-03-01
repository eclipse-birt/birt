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

package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;

public class ArchiveReader implements IDocArchiveReader
{

	protected ArchiveFile archive;
	protected boolean shareArchive;

	public ArchiveReader( ArchiveFile archive ) throws IOException
	{
		shareArchive = true;
		this.archive = archive;
	}

	public ArchiveReader( String archiveName ) throws IOException
	{
		if ( archiveName == null || archiveName.length( ) == 0 )
		{
			throw new IOException(
					"The file archive name is null or empty string." );
		}

		File fd = new File( archiveName );
		if ( !fd.isFile( ) )
		{
			throw new IOException(
					"The specified name is not a file name. The FileArchiveReader is expecting a valid file archive name." );
		}
		if ( !fd.exists( ) )
		{
			throw new IOException( "The specified file do not exist." );
		}

		archiveName = fd.getCanonicalPath( ); // make sure the file name is an
		
		// absolute path
		
		shareArchive = false;
		archive = new ArchiveFile( archiveName, "r" );
	}

	public void close( ) throws IOException
	{
		if ( !shareArchive )
		{
			archive.close( );
		}

	}

	public ArchiveFile getArchive( )
	{
		return archive;
	}

	public boolean exists( String relativePath )
	{
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		return archive.exists( relativePath );
	}

	public String getName( )
	{
		return archive.getName( );
	}

	public RAInputStream getStream( String relativePath ) throws IOException
	{
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		ArchiveEntry entry = archive.getEntry( relativePath );
		if ( entry != null )
		{
			return new ArchiveEntryInputStream( entry );
		}
		return null;
	}

	public List listAllStreams( ) throws IOException
	{
		ArrayList list = new ArrayList( );
		list.addAll( archive.listEntries( "/" ) );
		return list;
	}

	public List listStreams( String namePattern ) throws IOException
	{
		ArrayList list = new ArrayList( );
		Iterator iter = archive.listEntries( "/" ).iterator( );
		while ( iter.hasNext( ) )
		{
			String name = (String) iter.next( );
			if ( name.startsWith( namePattern )
					&& !name.equalsIgnoreCase( namePattern ) )
			{
				String diffString = ArchiveUtil.generateRelativePath(
						namePattern, name );
				if ( diffString.lastIndexOf( ArchiveUtil.UNIX_SEPERATOR ) == 0 )
				{
					list.add( name );
				}
			}
		}
		return list;
	}

	public void open( ) throws IOException
	{
	}

	public Object lock( String stream ) throws IOException
	{
		ArchiveEntry entry = archive.getEntry( stream );
		if ( entry != null )
		{
			return archive.lockEntry( entry );
		}
		throw new IOException( "can't find the entry " + stream );
	}

	public void unlock( Object locker )
	{
		try
		{
			archive.unlockEntry( locker );
		}
		catch ( IOException ex )
		{
		}
	}
}
