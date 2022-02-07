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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;

class FatBlock extends Ext2Block implements Ext2Constants {

	static final int BLOCK_PER_FAT_BLOCK = BLOCK_SIZE / 4;

	boolean dirty;
	int[] blocks;

	FatBlock(Ext2FileSystem fs, int blockId) {
		super(fs, blockId);
		this.blocks = new int[BLOCK_PER_FAT_BLOCK];
		for (int i = 0; i < BLOCK_PER_FAT_BLOCK; i++) {
			blocks[i] = -1;
		}
	}

	public int getBlockId() {
		return blockId;
	}

	public void setBlock(int index, int blockId) throws IOException {
		assert index < BLOCK_PER_FAT_BLOCK;
		dirty = true;
		blocks[index] = blockId;
	}

	public int getBlock(int index) throws IOException {
		assert index < BLOCK_PER_FAT_BLOCK;
		return blocks[index];
	}

	public void flush() throws IOException {
		if (blockId == -1) {
			throw new IllegalStateException("Must assign block id before flush");
		}
		if (dirty) {
			ByteArrayOutputStream out = new ByteArrayOutputStream(BLOCK_SIZE);
			for (int i = 0; i < BLOCK_PER_FAT_BLOCK; i++) {
				IOUtil.writeInt(out, blocks[i]);
			}
			fs.writeBlock(blockId, out.toByteArray(), 0, BLOCK_SIZE);
		}
	}

	public void refresh() throws IOException {
		if (blockId == -1) {
			throw new IllegalStateException("Must assign block id before flush");
		}
		byte[] buffer = new byte[BLOCK_SIZE];
		fs.readBlock(blockId, buffer, 0, BLOCK_SIZE);
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		for (int i = 0; i < BLOCK_PER_FAT_BLOCK; i++) {
			blocks[i] = IOUtil.readInt(in);
		}
	}
}
