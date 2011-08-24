/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

public class ArchiveEntryAdapter extends ArchiveEntry
{

	protected ArchiveEntry entry;
	protected String entryName;

	public ArchiveEntryAdapter( String name, ArchiveEntry entry )
	{
		this.entryName = name;
		this.entry = entry;
	}

	@Override
	public String getName( ) throws IOException
	{
		if ( entryName == null )
		{
			return entry.getName( );
		}
		return entryName;
	}

	@Override
	protected long _getLength( ) throws IOException
	{
		return entry._getLength( );
	}

	@Override
	protected void _setLength( long length ) throws IOException
	{
		entry._setLength( length );
	}

	@Override
	protected void _flush( ) throws IOException
	{
		entry._flush( );
	}

	@Override
	protected void _refresh( ) throws IOException
	{
		entry._refresh( );
	}

	@Override
	public int read( long offset, byte[] b, int off, int size )
			throws IOException
	{
		return entry.read( offset, b, off, size );
	}

	@Override
	public void write( long offset, byte[] b, int off, int size )
			throws IOException
	{
		entry.write( offset, b, off, size );
	}

	@Override
	public void close( ) throws IOException
	{
		entry.close( );
	}
}
