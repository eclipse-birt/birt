/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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

/**
 * the entry defined in the FAT stream. It defines the blocks in a stream.
 *
 */
class AllocEntry {

	static final int BLOCK_COUNT_INCREASE = 32;
	private int totalBlocks;
	private int[] blockIds;

	AllocEntry(int blockId) {
		blockIds = new int[BLOCK_COUNT_INCREASE];
		blockIds[0] = blockId;
		totalBlocks = 1;
	}

	void refresh(AllocTable tbl) throws IOException {
		int lastBlockId = getLastBlock();
		int blockId = tbl.readFATInt(lastBlockId * 4);
		while (blockId != -1) {
			ensureBlocks(totalBlocks + 1);
			blockIds[totalBlocks] = blockId;
			totalBlocks++;
			blockId = tbl.readFATInt(blockId * 4);
		}
	}

	void flush(AllocTable tbl) throws IOException {
		for (int i = 1; i < totalBlocks; i++) {
			tbl.writeFATInt(blockIds[i - 1] * 4, blockIds[i]);
		}
		tbl.writeFATInt(blockIds[totalBlocks - 1] * 4, -1);
	}

	private void ensureBlocks(int size) {
		// ensure the buffer is larger enough.
		if (blockIds == null || blockIds.length < size) {
			int base = size / BLOCK_COUNT_INCREASE * BLOCK_COUNT_INCREASE;
			int increase = base / 4;
			if (increase < BLOCK_COUNT_INCREASE) {
				increase = BLOCK_COUNT_INCREASE;
			}
			size = base + increase;
			int[] blocks = new int[size];
			if (blockIds != null) {
				System.arraycopy(blockIds, 0, blocks, 0, totalBlocks);
			}
			blockIds = blocks;
		}
	}

	int getTotalBlocks() {
		return totalBlocks;
	}

	int getFirstBlock() {
		return blockIds[0];
	}

	int getLastBlock() {
		return blockIds[totalBlocks - 1];
	}

	int getBlock(int index) {
		if (index < totalBlocks) {
			return blockIds[index];
		}
		return -1;
	}

	void appendBlock(int blockId) {
		ensureBlocks(totalBlocks + 1);
		blockIds[totalBlocks] = blockId;
		totalBlocks++;
	}

	int removeLastBlock() {
		if (totalBlocks > 0) {
			totalBlocks--;
			return blockIds[totalBlocks];
		}
		return -1;
	}
}
