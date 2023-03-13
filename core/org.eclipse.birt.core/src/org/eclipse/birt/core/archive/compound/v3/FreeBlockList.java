/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound.v3;

import java.io.IOException;

public class FreeBlockList extends FatBlockList {

	FreeBlockList(Ext2FileSystem fs, Ext2Node node) {
		super(fs, node);
	}

	int removeLastBlock() throws IOException {
		for (int level = Ext2Node.INDIRECT_BLOCK_COUNT - 1; level >= 0; level--) {
			int blockId = node.getIndirectBlock(level);
			if (blockId > 0) {
				FatBlock fatBlock = getCachedBlock(level, blockId);
				int nextBlockId = getLastBlock(level, fatBlock);
				if (nextBlockId < 0) {
					nextBlockId = blockId;
					node.setIndirectBlock(level, -1);
					clear(level);
				}
				int blockCount = node.getBlockCount();
				node.setBlockCount(blockCount - 1);
				return nextBlockId;
			}
		}

		for (int i = Ext2Node.DIRECT_BLOCK_COUNT - 1; i >= 0; i--) {
			int blockId = node.getDirectBlock(i);
			if (blockId > 0) {
				node.setDirectBlock(i, -1);
				int blockCount = node.getBlockCount();
				node.setBlockCount(blockCount - 1);
				return blockId;
			}
		}
		assert node.getBlockCount() == 0;
		return -1;
	}

	protected int getLastBlock(int level, FatBlock fatBlock) throws IOException {
		for (int index = 1023; index >= 0; index--) {
			int blockId = fatBlock.getBlock(index);
			if (blockId > 0) {
				if (level == 0) {
					fatBlock.setBlock(index, -1);
					return blockId;
				}
				FatBlock nextFatBlock = getCachedBlock(level - 1, blockId);
				int nextBlockId = getLastBlock(level - 1, nextFatBlock);
				if (nextBlockId > 0) {
					return nextBlockId;
				}
				// return the block used by the nextFatBlock
				fatBlock.setBlock(index, -1);
				clear(level - 1);
				return blockId;
			}
		}
		return -1;
	}

}
