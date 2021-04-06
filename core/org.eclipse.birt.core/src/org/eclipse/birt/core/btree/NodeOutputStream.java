/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.btree;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class NodeOutputStream extends OutputStream implements BTreeConstants {

	private NodeFile file;

	private int blockId;
	private int offset;
	private byte[] bytes;

	private int blockCount;
	private int[] usedBlocks;

	public NodeOutputStream(NodeFile file) throws IOException {
		this(file, new int[] { file.allocBlock() });
	}

	public NodeOutputStream(NodeFile file, int[] usedBlocks) {
		this.file = file;

		this.bytes = new byte[BLOCK_SIZE];
		this.offset = BLOCK_SIZE;

		this.usedBlocks = usedBlocks;
		this.blockId = -1;
		this.blockCount = 0;
	}

	public void write(int b) throws IOException {
		ensureCapacity();
		bytes[offset] = (byte) (b & 0xFF);
		offset++;
	}

	public void write(byte b[], int off, int len) throws IOException {
		while (len > 0) {
			ensureCapacity();
			int copySize = BLOCK_SIZE - offset;
			if (copySize > len) {
				copySize = len;
			}
			System.arraycopy(b, off, bytes, offset, copySize);
			off += copySize;
			offset += copySize;
			len -= copySize;
		}
	}

	public void close() throws IOException {
		if (blockId != -1) {
			BTreeUtils.integerToBytes(-1, bytes);
			file.writeBlock(blockId, bytes);
		}

		for (int i = blockCount; i < usedBlocks.length; i++) {
			int freeBlock = usedBlocks[i];
			if (freeBlock != -1) {
				file.freeBlock(freeBlock);
			}
		}
	}

	private void ensureCapacity() throws IOException {
		if (offset >= BLOCK_SIZE) {
			// get the next block
			if (blockCount >= usedBlocks.length) {
				int[] blocks = new int[blockCount * 2];
				System.arraycopy(usedBlocks, 0, blocks, 0, usedBlocks.length);
				Arrays.fill(blocks, usedBlocks.length, blocks.length, -1);
				usedBlocks = blocks;
			}

			int nextBlockId = usedBlocks[blockCount];
			if (nextBlockId == -1) {
				nextBlockId = file.allocBlock();
				usedBlocks[blockCount] = nextBlockId;
			}

			// flush the current block into the disk
			if (blockId != -1) {
				BTreeUtils.integerToBytes(nextBlockId, bytes);
				file.writeBlock(blockId, bytes);
			}

			blockId = nextBlockId;
			blockCount++;

			Arrays.fill(bytes, (byte) 0);

			offset = 4;
		}
	}

	public int[] getUsedBlocks() {
		int[] blocks = new int[blockCount];
		System.arraycopy(usedBlocks, 0, blocks, 0, blockCount);
		return blocks;
	}
}
