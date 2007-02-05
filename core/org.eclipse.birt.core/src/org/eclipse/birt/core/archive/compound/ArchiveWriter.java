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

import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.IStreamSorter;
import org.eclipse.birt.core.archive.RAOutputStream;

public class ArchiveWriter implements IDocArchiveWriter
{

	boolean shareArchive;
	ArchiveFile archive;

	public ArchiveWriter( String archiveName ) throws IOException
	{
		archive = new ArchiveFile( archiveName, "rw" );
		shareArchive = false;
	}

	public ArchiveWriter( ArchiveFile archive ) throws IOException
	{
		this.archive = archive;
		shareArchive = true;
	}

	public RAOutputStream createRandomAccessStream( String relativePath )
			throws IOException
	{
		ArchiveEntry entry = archive.createEntry( relativePath );
		return new ArchiveEntryOutputStream( entry );
	}

	public boolean dropStream( String relativePath )
	{
		try
		{
			return archive.removeEntry( relativePath );
		}
		catch ( IOException ex )
		{
			return false;
		}
	}

	public boolean exists( String relativePath )
	{
		return archive.exists( relativePath );
	}

	public void finish( ) throws IOException
	{
		archive.flush( );
		if ( !shareArchive )
		{
			archive.close( );
		}
	}

	public void flush( ) throws IOException
	{
		archive.flush( );
	}

	public String getName( )
	{
		return archive.getName( );
	}

	public void initialize( ) throws IOException
	{
	}

	public void setStreamSorter( IStreamSorter streamSorter )
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
