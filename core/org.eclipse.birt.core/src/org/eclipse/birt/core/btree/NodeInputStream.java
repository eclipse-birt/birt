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
import java.io.InputStream;

public class NodeInputStream extends InputStream implements BTreeConstants {

	private NodeFile file;

	private int offset;
	private byte[] bytes;

	private int blockCount;
	private int[] usedBlocks;

	public NodeInputStream(NodeFile file, int blockId) throws IOException {
		this.file = file;

		this.usedBlocks = new int[4];
		this.usedBlocks[0] = blockId;
		this.blockCount = 0;

		this.bytes = new byte[BLOCK_SIZE];
		this.offset = BLOCK_SIZE;
	}

	public int read() throws IOException {
		int remainSize = available();
		if (remainSize > 0) {
			return bytes[offset++] & 0xFF;
		}
		return -1;
	}

	public int read(byte b[], int off, int len) throws IOException {
		int readSize = 0;
		while (readSize < len) {
			int copySize = len - readSize;
			int remainSize = available();
			if (remainSize == 0) {
				break;
			}
			if (copySize > remainSize) {
				copySize = remainSize;
			}
			System.arraycopy(bytes, offset, b, off, copySize);
			offset += copySize;
			off += copySize;
			readSize += copySize;
		}
		return readSize;
	}

	public int available() throws IOException {
		if (offset < BLOCK_SIZE) {
			return BLOCK_SIZE - offset;
		}

		// try to get the next block
		int blockId = usedBlocks[blockCount];
		if (blockId != -1) {
			file.readBlock(blockId, bytes);
			offset = 4;

			blockCount++;
			int nextBlockId = BTreeUtils.bytesToInteger(bytes);
			if (blockCount >= usedBlocks.length) {
				int[] blocks = new int[usedBlocks.length * 2];
				System.arraycopy(usedBlocks, 0, blocks, 0, usedBlocks.length);
				usedBlocks = blocks;
			}
			usedBlocks[blockCount] = nextBlockId;
			return BLOCK_SIZE - offset;
		}
		return 0;
	}

	public int[] getUsedBlocks() {
		int[] blocks = new int[blockCount];
		System.arraycopy(usedBlocks, 0, blocks, 0, blockCount);
		return blocks;
	}
}
