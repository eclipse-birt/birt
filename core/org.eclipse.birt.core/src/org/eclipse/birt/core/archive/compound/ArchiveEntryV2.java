/*******************************************************************************
 * Copyright (c) 2004,2011 Actuate Corporation.
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

package org.eclipse.birt.core.archive.compound;

import java.io.IOException;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

class ArchiveEntryV2 extends ArchiveEntry implements ArchiveConstants {

	protected final int BLOCK_SIZE;
	protected int cachId;
	protected ArchiveFileV2 af;
	protected NameEntry entry;
	protected AllocEntry index;
	private int cachedBlockId;
	private Block cachedBlock;

	ArchiveEntryV2(ArchiveFileV2 af, NameEntry entry) throws IOException {
		super(entry.getName());
		this.af = af;
		this.BLOCK_SIZE = af.BLOCK_SIZE;
		this.entry = entry;
		this.cachId = entry.getBlock();
		if (cachId != -1) {
			index = af.allocTbl.loadEntry(cachId);
		}
		cachedBlockId = -1;
		cachedBlock = null;
	}

	public void close() throws IOException {
		if (cachedBlock != null) {
			af.unloadBlock(cachedBlock);
		}
		cachedBlockId = -1;
		cachedBlock = null;
	}

	public synchronized long getLength() throws IOException {
		return entry.getLength();
	}

	public synchronized void setLength(long length) throws IOException {
		ensureSize(length);
		entry.setLength(length);
	}

	public synchronized void flush() throws IOException {
		super.flush();
	}

	public synchronized void refresh() throws IOException {
		// TODO: support refresh in future.
	}

	public synchronized int read(long pos, byte[] b, int off, int len) throws IOException {
		long length = entry.getLength();

		if (pos >= length) {
			return -1;
		}

		if (pos + len > length) {
			len = (int) (length - pos);
		}

		if (len == 0) {
			return 0;
		}

		// read first block
		int blockId = (int) (pos / BLOCK_SIZE);
		int blockOff = (int) (pos % BLOCK_SIZE);
		int readSize = BLOCK_SIZE - blockOff;
		if (len < readSize) {
			readSize = len;
		}
		Block block = loadBlock(blockId);
		block.read(blockOff, b, off, readSize);
		int remainSize = len - readSize;

		// read blocks
		while (remainSize >= BLOCK_SIZE) {
			blockId++;
			block = loadBlock(blockId);
			block.read(0, b, off + readSize, BLOCK_SIZE);
			readSize += BLOCK_SIZE;
			remainSize -= BLOCK_SIZE;
		}

		// read remain blocks
		if (remainSize > 0) {
			blockId++;
			block = loadBlock(blockId);
			block.read(0, b, off + readSize, remainSize);
			readSize += remainSize;
		}

		return readSize;
	}

	public synchronized void write(long pos, byte[] b, int off, int len) throws IOException {
		ensureSize(pos + len);

		if (len == 0) {
			return;
		}
		int blockId = (int) (pos / BLOCK_SIZE);
		int blockOff = (int) (pos % BLOCK_SIZE);
		int writeSize = BLOCK_SIZE - blockOff;
		if (len < writeSize) {
			writeSize = len;
		}
		Block block = loadBlock(blockId);
		block.write(blockOff, b, off, writeSize);
		int remainSize = len - writeSize;

		// write blocks
		while (remainSize >= BLOCK_SIZE) {
			blockId++;
			block = loadBlock(blockId);
			block.write(0, b, off + writeSize, BLOCK_SIZE);
			writeSize += BLOCK_SIZE;
			remainSize -= BLOCK_SIZE;
		}

		// write remain blocks
		if (remainSize > 0) {
			blockId++;
			block = loadBlock(blockId);
			block.write(0, b, off + writeSize, remainSize);
		}

		long length = entry.getLength();
		long offset = pos + len;
		if (length < offset) {
			setLength(offset);
		}
	}

	protected void ensureSize(long newLength) throws IOException {
		if (index == null) {
			index = af.allocTbl.createEntry();
			entry.setBlock(index.getFirstBlock());
		}
		int blockCount = (int) ((newLength + BLOCK_SIZE - 1) / BLOCK_SIZE);
		int totalBlock = index.getTotalBlocks();
		if (blockCount > totalBlock) {
			while (totalBlock < blockCount) {
				int freeBlock = af.allocTbl.getFreeBlock();
				index.appendBlock(freeBlock);
				totalBlock++;
			}
		}
	}

	private Block loadBlock(int blockId) throws IOException {
		if (cachedBlockId == blockId) {
			return cachedBlock;
		}

		if (cachedBlock != null) {
			af.unloadBlock(cachedBlock);
			cachedBlock = null;
		}

		cachedBlockId = blockId;
		int fileBlockId = index.getBlock(blockId);
		if (fileBlockId != -1) {
			cachedBlock = af.loadBlock(fileBlockId);
			return cachedBlock;
		}
		throw new IOException(
				CoreMessages.getFormattedString(ResourceConstants.INVALID_INDEX, new Object[] { blockId }));
	}

}
