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

package org.eclipse.birt.core.archive.compound.v3;

import java.io.IOException;

public class FreeBlockList extends FatBlockList
{

	FreeBlockList( Ext2FileSystem fs, Ext2Node node )
	{
		super( fs, node );
	}

	int removeLastBlock( ) throws IOException
	{
		for ( int level = Ext2Node.INDIRECT_BLOCK_COUNT - 1; level >= 0; level-- )
		{
			int blockId = node.getIndirectBlock( level );
			if ( blockId != -1 )
			{
				FatBlock fatBlock = getCachedBlock( level, blockId );
				int nextBlockId = getLastBlock( level, fatBlock );
				if ( nextBlockId > 0 )
				{
					nextBlockId = blockId;
				}
				node.blockCount--;
				return nextBlockId;
			}
		}

		for ( int i = Ext2Node.DIRECT_BLOCK_COUNT - 1; i >= 0; i-- )
		{
			int blockId = node.getDirectBlock( i );
			if ( blockId > 0 )
			{
				node.setDirectBlock( i, 0 );
				node.blockCount--;
				return blockId;
			}
		}
		assert node.getBlockCount( ) == 0;
		return -1;
	}

	protected int getLastBlock( int level, FatBlock fatBlock )
			throws IOException
	{
		for ( int index = 1023; index >= 0; index-- )
		{
			int blockId = fatBlock.getBlock( index );
			if ( blockId > 0 )
			{
				if ( level == 0 )
				{
					fatBlock.setBlock( index, 0 );
					return blockId;
				}
				FatBlock nextFatBlock = getCachedBlock( level, blockId );
				int nextBlockId = getLastBlock( level--, nextFatBlock );
				if ( nextBlockId > 0 )
				{
					return nextBlockId;
				}
				fatBlock.setBlock( index, 0 );
				return blockId;
			}
		}
		return -1;
	}

}
