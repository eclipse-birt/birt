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

import java.io.EOFException;
import java.io.IOException;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class FatBlockList {

	/**
	 * max direct blocks in the node
	 */
	static final int MAX_DIRECT_BLOCK = Ext2Node.DIRECT_BLOCK_COUNT;
	/**
	 * indirect blocks in the node
	 */
	static final int INDIRECT_BLOCK_COUNT = 3;
	/**
	 * max indirect blocks in the node
	 */
	static final int MAX_INDIRECT_BLOCK = 1024;
	/**
	 * max double indirect blocks in the node
	 */
	static final int MAX_DOUBLE_INDIRECT_BLOCK = 1024 * 1024;
	static final int DOUBLE_INDIRECT_MASK_1 = 0xFFC00;
	static final int DOUBLE_INDIRECT_SHIFT_1 = 10;
	static final int DOUBLE_INDIRECT_MASK_2 = 0x3FF;
	static final int DOUBLE_INDIRECT_SHIFT_2 = 0;
	/**
	 * max triple indirect blocks in the node
	 */
	static final int MAX_TRIPLE_INDIRECT_BLOCK = 1024 * 1024 * 1024;
	static final int TRIPLE_INDIRECT_MASK_1 = (0x3FF << 20);
	static final int TRIPLE_INDIRECT_SHIFT_1 = 20;
	static final int TRIPLE_INDIRECT_MASK_2 = (0x3FF << 10);
	static final int TRIPLE_INDIRECT_SHIFT_2 = 10;
	static final int TRIPLE_INDIRECT_MASK_3 = 0x3FF;
	static final int TRIPLE_INDIRECT_SHIFT_3 = 0;

	protected Ext2FileSystem fs;
	protected Ext2Node node;
	protected FatBlock[] cachedFatBlocks = new FatBlock[3];

	FatBlockList(Ext2FileSystem fs, Ext2Node node) {
		this.fs = fs;
		this.node = node;
	}

	public void clear() throws IOException {
		for (int i = 0; i < 3; i++) {
			clear(i);
		}
	}

	protected void clear(int level) throws IOException {
		FatBlock cachedBlock = cachedFatBlocks[level];
		if (cachedBlock != null) {
			fs.unloadBlock(cachedBlock);
			cachedFatBlocks[level] = null;
		}
	}

	public Ext2Node getNode() {
		return node;
	}

	public int getFileBlock(int index) throws IOException {
		if (index < MAX_DIRECT_BLOCK) {
			return node.getDirectBlock(index);
		}
		index -= MAX_DIRECT_BLOCK;
		if (index < MAX_INDIRECT_BLOCK) {
			return getIndirectBlock1(index);
		}
		index -= MAX_INDIRECT_BLOCK;
		if (index < MAX_DOUBLE_INDIRECT_BLOCK) {
			return getIndirectBlock2(index);
		}
		index -= MAX_DOUBLE_INDIRECT_BLOCK;
		if (index < MAX_TRIPLE_INDIRECT_BLOCK) {
			return getIndirectBlock3(index);
		}
		throw new EOFException(CoreMessages.getString(ResourceConstants.EXCEED_FILE_LENGTH));
	}

	public void setFileBlock(int index, int fileBlockId) throws IOException {
		if (index < MAX_DIRECT_BLOCK) {
			node.setDirectBlock(index, fileBlockId);
			return;
		}
		index -= MAX_DIRECT_BLOCK;
		if (index < MAX_INDIRECT_BLOCK) {
			setIndirectBlock1(index, fileBlockId);
			return;
		}
		index -= MAX_INDIRECT_BLOCK;
		if (index < MAX_DOUBLE_INDIRECT_BLOCK) {
			setIndirectBlock2(index, fileBlockId);
			return;
		}
		index -= MAX_DOUBLE_INDIRECT_BLOCK;
		if (index < MAX_TRIPLE_INDIRECT_BLOCK) {
			setIndirectBlock3(index, fileBlockId);
			return;
		}
		throw new EOFException(CoreMessages.getString(ResourceConstants.EXCEED_FILE_LENGTH));
	}

	private void setIndirectBlock1(int index, int blockId) throws IOException {
		int fatBlockId = node.getIndirectBlock(0);
		FatBlock fatBlock = getCachedBlock(0, fatBlockId);
		if (fatBlockId <= 0) {
			fatBlockId = fatBlock.getBlockId();
			node.setBlockCount(node.getBlockCount() + 1);
			node.setIndirectBlock(0, fatBlockId);
		}
		fatBlock.setBlock(index, blockId);
	}

	private int getIndirectBlock1(int index) throws IOException {
		int fatBlockId = node.getIndirectBlock(0);
		if (fatBlockId <= 0) {
			return -1;
		}
		FatBlock fatBlock = getCachedBlock(0, fatBlockId);
		return fatBlock.getBlock(index);
	}

	private void setIndirectBlock2(int index, int blockId) throws IOException {
		int fatBlockId = node.getIndirectBlock(1);
		FatBlock fatBlock = getCachedBlock(0, fatBlockId);
		if (fatBlockId <= 0) {
			fatBlockId = fatBlock.getBlockId();
			node.setBlockCount(node.getBlockCount() + 1);
			node.setIndirectBlock(1, fatBlockId);
		}
		int index1 = (index & 0xFFC00) >> 10;
		int fatBlockId1 = fatBlock.getBlock(index1);
		FatBlock fatBlock1 = getCachedBlock(1, fatBlockId1);
		if (fatBlockId1 <= 0) {
			fatBlockId1 = fatBlock1.getBlockId();
			node.setBlockCount(node.getBlockCount() + 1);
			fatBlock.setBlock(index1, fatBlockId1);
		}
		int index2 = index & 0x3FF;
		fatBlock1.setBlock(index2, blockId);
	}

	private int getIndirectBlock2(int index) throws IOException {
		int fatBlockId = node.getIndirectBlock(1);
		if (fatBlockId > 0) {
			FatBlock fatBlock = getCachedBlock(0, fatBlockId);
			int index1 = (index & 0xFFC00) >> 10;
			int fatBlockId1 = fatBlock.getBlock(index1);
			if (fatBlockId1 > 0) {
				FatBlock fatBlock1 = getCachedBlock(1, fatBlockId1);
				int index2 = index & 0x3FF;
				return fatBlock1.getBlock(index2);
			}
		}
		return -1;
	}

	private void setIndirectBlock3(int index, int blockId) throws IOException {
		int fatBlockId = node.getIndirectBlock(2);
		FatBlock fatBlock = getCachedBlock(0, fatBlockId);
		if (fatBlockId <= 0) {
			fatBlockId = fatBlock.getBlockId();
			node.setBlockCount(node.getBlockCount() + 1);
			node.setIndirectBlock(2, fatBlockId);
		}
		int index1 = (index & 0x3FF00000) >> 20;
		int fatBlockId1 = fatBlock.getBlock(index1);
		FatBlock fatBlock1 = getCachedBlock(1, fatBlockId1);
		if (fatBlockId1 <= 0) {
			fatBlockId1 = fatBlock1.getBlockId();
			node.setBlockCount(node.getBlockCount() + 1);
			fatBlock.setBlock(index1, fatBlockId1);
		}

		int index2 = (index & 0xFFC00) >> 10;
		int fatBlockId2 = fatBlock1.getBlock(index2);
		FatBlock fatBlock2 = getCachedBlock(2, fatBlockId2);
		if (fatBlockId2 <= 0) {
			fatBlockId2 = fatBlock2.getBlockId();
			node.setBlockCount(node.getBlockCount() + 1);
			fatBlock1.setBlock(index2, fatBlockId2);
		}
		int index3 = index & 0x3FF;
		fatBlock2.setBlock(index3, blockId);
	}

	private int getIndirectBlock3(int index) throws IOException {
		int fatBlockId = node.getIndirectBlock(2);
		if (fatBlockId > 0) {
			FatBlock fatBlock = getCachedBlock(0, fatBlockId);
			int index1 = (index & 0x3FF00000) >> 20;
			int fatBlockId1 = fatBlock.getBlock(index1);
			if (fatBlockId1 > 0) {
				FatBlock fatBlock1 = getCachedBlock(1, fatBlockId1);
				int index2 = (index & 0xFFC00) >> 10;
				int fatBlockId2 = fatBlock1.getBlock(index2);
				if (fatBlockId2 > 0) {
					FatBlock fatBlock2 = getCachedBlock(2, fatBlockId2);
					int index3 = index & 0x3FF;
					return fatBlock2.getBlock(index3);
				}
			}
		}
		return -1;
	}

	FatBlock getCachedBlock(int level, int blockId) throws IOException {
		FatBlock block = cachedFatBlocks[level];
		if (block != null) {
			if (block.getBlockId() == blockId) {
				return block;
			}
			fs.unloadBlock(block);
		}
		if (blockId != -1) {
			block = fs.loadFatBlock(blockId);
		} else {
			block = fs.createFatBlock();
		}
		cachedFatBlocks[level] = block;
		return block;
	}
}
