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
import java.util.ArrayList;

import org.eclipse.birt.core.archive.ArchiveUtil;

class AllocTableLoader implements ArchiveConstants
{

	ArrayList entries = new ArrayList( );
	AllocEntry lastEntry;

	AllocTableLoader( )
	{
	}

	ArrayList getEntryies( )
	{
		return entries;
	}

	private AllocEntry getEntry( int blockId )
	{
		if ( blockId == lastEntry.getLastBlock( ) )
		{
			return lastEntry;
		}
		for ( int i = entries.size( ) - 1; i >= 0; i-- )
		{
			AllocEntry entry = (AllocEntry) entries.get( i );
			if ( entry.getLastBlock( ) == blockId )
			{
				lastEntry = entry;
				return entry;
			}
		}
		AllocEntry entry = new AllocEntry( blockId );
		entries.add( entry );
		lastEntry = entry;
		return entry;
	}

	void load( ArchiveFileV2 af ) throws IOException
	{
		int BLOCK_SIZE = af.BLOCK_SIZE;
		int INDEX_PER_BLOCK = BLOCK_SIZE / 4;
		// initialize the FAT entry
		AllocEntry fatEntry = new AllocEntry( ALLOC_TABLE_BLOCK );
		entries.add( fatEntry );
		lastEntry = fatEntry;

		// load the FAT block one by one
		byte buffer[] = new byte[BLOCK_SIZE];
		int readBlocks = 0;
		int blockIndex = 0;
		while ( readBlocks < fatEntry.getTotalBlocks( ) )
		{
			int fatBlockId = fatEntry.getBlock( readBlocks );
			af.read( fatBlockId, 0, buffer, 0, BLOCK_SIZE );
			for ( int i = 0; i < INDEX_PER_BLOCK; i++ )
			{
				int blockId = ArchiveUtil.bytesToInteger( buffer, i * 4 );
				if ( blockId > 0 )
				{
					AllocEntry entry = getEntry( blockIndex );
					entry.appendBlock( blockId );
				}
				blockIndex++;
			}
			readBlocks++;
		}
	}
}
