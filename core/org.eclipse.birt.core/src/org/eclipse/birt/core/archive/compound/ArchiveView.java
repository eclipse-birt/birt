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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ArchiveView extends ArchiveFile
{

	private boolean sharedArchive = false;
	private IArchiveFile archive = null;

	public ArchiveView( String viewName, String archiveName, String viewMode )
			throws IOException
	{
		super( viewName, viewMode );
		sharedArchive = false;
		try
		{
			archive = new ArchiveFile( archiveName, "r" );
		}
		catch ( IOException ex )
		{
			super.close( );
			throw ex;
		}
	}

	public ArchiveView( String viewName, IArchiveFile archive, String viewMode )
			throws IOException
	{
		super( viewName, viewMode );
		this.archive = archive;
		this.sharedArchive = true;
	}

	public void close( ) throws IOException
	{
		try
		{
			if ( !sharedArchive )
			{
				archive.close( );
			}
		}
		finally
		{
			super.close( );
		}
	}

	synchronized public boolean exists( String name )
	{
		if ( super.exists( name ) || archive.exists( name ) )
		{
			return true;
		}
		return false;
	}

	synchronized public ArchiveEntry getEntry( String name )
	{
		ArchiveEntry result = super.getEntry( name );
		if ( result != null )
		{
			return result;
		}

		result = archive.getEntry( name );
		if ( result != null )
		{
			return new ViewEntry( this, name, result );
		}
		return null;
	}

	synchronized public List listEntries( String namePattern )
	{
		List viewList = super.listEntries( namePattern );
		List archiveList = archive.listEntries( namePattern );

		Iterator iter = archiveList.iterator( );
		while ( iter.hasNext( ) )
		{
			String entryName = (String) iter.next( );
			if ( !viewList.contains( entryName ) )
			{
				viewList.add( entryName );
			}
		}

		return viewList;
	}

	public Object lockEntry( ArchiveEntry entry ) throws IOException
	{
		return entry;
	}

	public void refresh( ) throws IOException
	{
		archive.refresh( );
		refresh( );
	}

	class ViewEntry extends ArchiveEntry
	{

		IArchiveFile view;
		boolean writable;
		ArchiveEntry entry;
		String name;

		ViewEntry( IArchiveFile view, String name, ArchiveEntry entry )
		{
			writable = false;
			this.view = view;
			this.name = name;
			this.entry = entry;
		}

		public long getLength( ) throws IOException
		{
			return entry.getLength( );
		}

		public void setLength( long length ) throws IOException
		{
			ensureWritable( );
			entry.setLength( length );
		}

		public void flush( ) throws IOException
		{
			ensureWritable( );
			entry.flush( );
		}

		public void refresh( ) throws IOException
		{
			entry.refresh( );
		}

		public int read( long pos, byte[] b, int off, int len )
				throws IOException
		{
			return entry.read( pos, b, off, len );
		}

		public void write( long pos, byte[] b, int off, int len )
				throws IOException
		{
			ensureWritable( );
			entry.write( pos, b, off, len );
		}

		void ensureWritable( ) throws IOException
		{
			if ( !writable )
			{
				ArchiveEntry viewEntry = view.createEntry( name );
				copyEntry( entry, viewEntry );
				entry = viewEntry;
				writable = true;
			}
		}

		private void copyEntry( ArchiveEntry src, ArchiveEntry tgt )
				throws IOException
		{
			byte[] b = new byte[4096];
			long length = src.getLength( );
			long pos = 0;
			while ( pos < length )
			{
				int size = src.read( pos, b, 0, 4096 );
				tgt.write( pos, b, 0, size );
				pos += size;
			}
		}
	}
}
