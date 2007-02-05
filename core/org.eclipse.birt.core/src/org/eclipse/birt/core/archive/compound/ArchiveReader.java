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
import java.util.List;

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
		archive = new ArchiveFile( archiveName, "r" );
		shareArchive = false;
	}

	public void close( ) throws IOException
	{
		if ( !shareArchive )
		{
			archive.close( );
		}

	}

	public boolean exists( String relativePath )
	{
		return archive.exists( relativePath );
	}

	public String getName( )
	{
		return archive.getName( );
	}

	public RAInputStream getStream( String relativePath ) throws IOException
	{
		ArchiveEntry entry = archive.getEntry( relativePath );
		if ( entry != null )
		{
			return new ArchiveEntryInputStream( entry );
		}
		return null;
	}

	public List listStreams( String relativeStoragePath ) throws IOException
	{
		return archive.listEntries( relativeStoragePath );
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
