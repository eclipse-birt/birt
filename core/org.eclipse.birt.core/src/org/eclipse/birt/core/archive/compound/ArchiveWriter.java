/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.IStreamSorter;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;

public class ArchiveWriter implements IDocArchiveWriter
{

	boolean shareArchive;
	IArchiveFile archive;
	HashSet streams;

	public ArchiveWriter( String archiveName ) throws IOException
	{
		archive = new ArchiveFile( archiveName, "rw" );
		shareArchive = false;
		streams = new HashSet( );
	}

	public ArchiveWriter( IArchiveFile archive ) throws IOException
	{
		this.archive = archive;
		shareArchive = true;
		streams = new HashSet( );
	}

	public IArchiveFile getArchive( )
	{
		return archive;
	}

	public RAOutputStream createRandomAccessStream( String relativePath )
			throws IOException
	{
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		ArchiveEntry entry = archive.createEntry( relativePath );
		RAOutputStream stream = new ArchiveEntryOutputStream( this, entry );
		streams.add( stream );
		return stream;
	}

	public RAOutputStream openRandomAccessStream( String relativePath )
			throws IOException
	{
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		ArchiveEntry entry;
		if ( archive.exists( relativePath ) )
		{
			entry = archive.openEntry( relativePath );
		}
		else
		{
			entry = archive.createEntry( relativePath );
		}
		RAOutputStream stream = new ArchiveEntryOutputStream( this, entry );
		streams.add( stream );
		return stream;
	}

	public RAOutputStream createOutputStream( String relativePath )
			throws IOException
	{
		return createRandomAccessStream( relativePath );
	}

	public RAOutputStream getOutputStream( String relativePath )
			throws IOException
	{
		return openRandomAccessStream( relativePath );
	}

	public RAInputStream getInputStream( String relativePath )
			throws IOException
	{
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		ArchiveEntry entry = archive.openEntry( relativePath );
		return new ArchiveEntryInputStream( entry );
	}

	public boolean dropStream( String relativePath )
	{
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
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
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		return archive.exists( relativePath );
	}

	public void finish( ) throws IOException
	{
		try
		{
			// close all the streams opend in this archive writer
			ArrayList unclosedStreams = new ArrayList( );
			unclosedStreams.addAll( streams );
			Iterator iter = unclosedStreams.iterator( );
			while ( iter.hasNext( ) )
			{
				RAOutputStream stream = (RAOutputStream) iter.next( );
				stream.close( );
			}
			// flush the archvies
			archive.flush( );
		}
		finally
		{
			if ( !shareArchive )
			{
				archive.close( );
			}
		}
	}

	public void flush( ) throws IOException
	{
		ArrayList unclosedStreams = new ArrayList( );
		unclosedStreams.addAll( streams );
		Iterator iter = unclosedStreams.iterator( );
		while ( iter.hasNext( ) )
		{
			RAOutputStream stream = (RAOutputStream) iter.next( );
			stream.flush( );
		}
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

	public Object lock( String relativePath ) throws IOException
	{
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;
		return archive.lockEntry( relativePath );
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

	void registerStream( ArchiveEntryOutputStream stream )
	{
		streams.add( stream );
	}

	void unregisterStream( ArchiveEntryOutputStream stream )
	{
		streams.remove( stream );
	}

}
