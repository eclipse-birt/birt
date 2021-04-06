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

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

/**
 * there are two kinds of caches in the file object:
 * 
 * data cache: each file has one block data cache, contains the data at the
 * current position.
 * 
 * fat cache: each file has max to 3 blocks fat caches.
 * 
 * once the cache is retried, it is returned to the file system object. The file
 * system object decides if the cache need to be locked for other file objects
 * or return it to the cache manager.
 * 
 * there is only one Ext2File is opened as write, so we needn't synchronize the
 * FAT node list
 */
public class Ext2File {

	static final int BLOCK_SIZE = Ext2FileSystem.BLOCK_SIZE;
	static final int BLOCK_SIZE_BITS = Ext2FileSystem.BLOCK_SIZE_BITS;
	static final int BLOCK_OFFSET_MASK = Ext2FileSystem.BLOCK_OFFSET_MASK;

	/**
	 * the file system this file belongs to
	 */
	private Ext2FileSystem fs;

	private final Ext2Entry entry;

	private Ext2Node node;

	/**
	 * current position
	 */
	private long position;
	/**
	 * the cached fatBlock
	 */
	private FatBlockList blocks;

	/**
	 * if the data should be cache enabled
	 */
	private boolean enableCache;
	/**
	 * the cached data block id
	 */
	private int cachedBlockId;
	/**
	 * data block
	 */
	private DataBlock cachedBlock;

	Ext2File(Ext2FileSystem fs, int inode, boolean enableCache) throws IOException {
		this(fs, null, fs.getNode(inode), enableCache);
	}

	Ext2File(Ext2FileSystem fs, Ext2Entry entry, Ext2Node node) throws IOException {
		this(fs, entry, node, true);
	}

	Ext2File(Ext2FileSystem fs, Ext2Entry entry, Ext2Node node, boolean enableCache) throws IOException {
		this.fs = fs;
		this.entry = entry;
		this.node = node;
		this.blocks = new FatBlockList(fs, node);
		this.fs.registerOpenedFile(this);
		this.enableCache = enableCache;
		this.cachedBlockId = -1;
		this.cachedBlock = DataBlock.READ_ONLY_BLOCK;
	}

	public Ext2Entry getEntry() {
		return entry;
	}

	public String getName() {
		if (entry != null) {
			return entry.getName();
		}
		return null;
	}

	public void close() throws IOException {
		if (fs == null) {
			// the file has been closed
			return;
		}
		try {
			if (cachedBlock != DataBlock.READ_ONLY_BLOCK) {
				fs.unloadBlock(cachedBlock);
			}
			cachedBlockId = -1;
			cachedBlock = DataBlock.READ_ONLY_BLOCK;
			blocks.clear();
		} finally {
			fs.unregisterOpenedFile(this);
			fs = null;
		}
	}

	public long length() throws IOException {
		return node.getLength();
	}

	public void setLength(long length) throws IOException {
		if (fs == null) {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_HAS_BEEN_CLOSED));
		}

		if (fs.isReadOnly()) {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_IN_READONLY_MODE));
		}
		node.setLength(length);
		if (position >= length) {
			position = length;
		}
	}

	public void seek(long position) throws IOException {
		this.position = position;
	}

	public long getPointer() throws IOException {
		return this.position;
	}

	public int read(byte[] buffer, int off, int size) throws IOException {
		if (fs == null) {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_HAS_BEEN_CLOSED));
		}

		assert buffer != null;
		assert off >= 0;
		assert off + size <= buffer.length;

		if (size == 0) {
			return 0;
		}

		if (enableCache) {
			return read_with_cache(buffer, off, size);
		}
		return read_without_cache(buffer, off, size);
	}

	private int read_without_cache(byte[] buffer, int off, int size) throws IOException {
		long length = node.getLength();
		if (position + size > length) {
			size = (int) (length - position);
			if (size <= 0) {
				return -1;
			}
		}

		int blockId = (int) (position >> BLOCK_SIZE_BITS);
		int blockOff = (int) (position & BLOCK_OFFSET_MASK);

		int blockSize = BLOCK_SIZE - blockOff;
		int fileBlockId = getDataBlock(blockId);
		if (size <= blockSize) {
			fs.readBlock(fileBlockId, blockOff, buffer, off, size);
		} else {
			fs.readBlock(fileBlockId, blockOff, buffer, off, blockSize);
			off += blockSize;
			int remainSize = size - blockSize;
			int wholeBlocks = remainSize >> BLOCK_SIZE_BITS;
			for (int i = 0; i < wholeBlocks; i++) {
				blockId++;
				fileBlockId = getDataBlock(blockId);
				if (fileBlockId != -1) {
					fs.readBlock(fileBlockId, 0, buffer, off, BLOCK_SIZE);
				}
				off += BLOCK_SIZE;
			}
			remainSize = remainSize & BLOCK_OFFSET_MASK;
			if (remainSize > 0) {
				blockId++;
				fileBlockId = getDataBlock(blockId);
				if (fileBlockId != -1) {
					fs.readBlock(fileBlockId, 0, buffer, off, remainSize);
				}
			}
		}
		position += size;
		return size;
	}

	private int read_with_cache(byte[] buffer, int off, int size) throws IOException {
		long length = node.getLength();
		if (position + size > length) {
			size = (int) (length - position);
			if (size <= 0) {
				return -1;
			}
		}

		int blockId = (int) (position >> BLOCK_SIZE_BITS);
		int blockOff = (int) (position & BLOCK_OFFSET_MASK);

		int blockSize = BLOCK_SIZE - blockOff;
		DataBlock block = loadDataBlock(blockId);
		if (size <= blockSize) {
			block.read(blockOff, buffer, off, size);
		} else {
			block.read(blockOff, buffer, off, blockSize);
			off += blockSize;
			int remainSize = size - blockSize;
			int wholeBlocks = remainSize >> BLOCK_SIZE_BITS;
			for (int i = 0; i < wholeBlocks; i++) {
				blockId++;
				block = loadDataBlock(blockId);
				block.read(0, buffer, off, BLOCK_SIZE);
				off += BLOCK_SIZE;
			}
			remainSize = remainSize & BLOCK_OFFSET_MASK;
			if (remainSize > 0) {
				blockId++;
				block = loadDataBlock(blockId);
				block.read(0, buffer, off, remainSize);
			}
		}
		position += size;
		return size;
	}

	public void write(byte[] buffer, int off, int size) throws IOException {
		if (fs == null) {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_HAS_BEEN_CLOSED));
		}
		if (fs.isReadOnly()) {
			throw new IOException(CoreMessages.getString(ResourceConstants.FILE_IN_READONLY_MODE));
		}

		assert buffer != null;
		assert off >= 0;
		assert off + size <= buffer.length;

		if (size == 0) {
			return;
		}

		if (enableCache) {
			write_with_cache(buffer, off, size);
		} else {
			write_without_cache(buffer, off, size);
		}
	}

	private void write_without_cache(byte[] buffer, int off, int size) throws IOException {

		int blockId = (int) (position >> BLOCK_SIZE_BITS);
		int blockOff = (int) (position & BLOCK_OFFSET_MASK);

		int blockSize = BLOCK_SIZE - blockOff;
		int fileBlockId = getDataBlock(blockId);
		if (size <= blockSize) {
			fs.writeBlock(fileBlockId, blockOff, buffer, off, size);
		} else {
			fs.writeBlock(fileBlockId, blockOff, buffer, off, blockSize);
			off += blockSize;
			int remainSize = size - blockSize;
			int wholeBlocks = remainSize >> BLOCK_SIZE_BITS;
			for (int i = 0; i < wholeBlocks; i++) {
				blockId++;
				fileBlockId = getDataBlock(blockId);
				fs.writeBlock(fileBlockId, 0, buffer, off, BLOCK_SIZE);
				off += BLOCK_SIZE;
			}
			remainSize = remainSize & BLOCK_OFFSET_MASK;
			if (remainSize > 0) {
				blockId++;
				fileBlockId = getDataBlock(blockId);
				fs.writeBlock(fileBlockId, 0, buffer, off, remainSize);
			}
		}
		position += size;
		if (position > node.getLength()) {
			node.setLength(position);
		}
	}

	private void write_with_cache(byte[] buffer, int off, int size) throws IOException {

		int blockId = (int) (position >> BLOCK_SIZE_BITS);
		int blockOff = (int) (position & BLOCK_OFFSET_MASK);

		int blockSize = BLOCK_SIZE - blockOff;
		DataBlock block = loadDataBlock(blockId);
		if (size <= blockSize) {
			block.write(blockOff, buffer, off, size);
		} else {
			block.write(blockOff, buffer, off, blockSize);
			off += blockSize;
			int remainSize = size - blockSize;
			int wholeBlocks = remainSize >> BLOCK_SIZE_BITS;
			for (int i = 0; i < wholeBlocks; i++) {
				blockId++;
				block = loadDataBlock(blockId);
				block.write(0, buffer, off, BLOCK_SIZE);
				off += BLOCK_SIZE;
			}
			remainSize = remainSize & BLOCK_OFFSET_MASK;
			if (remainSize > 0) {
				blockId++;
				block = loadDataBlock(blockId);
				block.write(0, buffer, off, remainSize);
			}
		}
		position += size;
		if (position > node.getLength()) {
			node.setLength(position);
		}
	}

	private DataBlock loadDataBlock(int blockId) throws IOException {
		if (cachedBlockId == blockId) {
			return cachedBlock;
		}
		if (cachedBlock != DataBlock.READ_ONLY_BLOCK) {
			fs.unloadBlock(cachedBlock);
		}

		cachedBlockId = blockId;
		int fileBlockId = blocks.getFileBlock(blockId);
		if (fileBlockId != -1) {
			cachedBlock = fs.loadDataBlock(fileBlockId);
			return cachedBlock;
		}
		if (fs.isReadOnly()) {
			cachedBlock = DataBlock.READ_ONLY_BLOCK;
			return cachedBlock;
		}
		cachedBlock = fs.createDataBlock();
		node.setBlockCount(node.getBlockCount() + 1);
		blocks.setFileBlock(blockId, cachedBlock.getBlockId());
		return cachedBlock;
	}

	private int getDataBlock(int blockId) throws IOException {
		int fileBlockId = blocks.getFileBlock(blockId);
		if (fileBlockId != -1) {
			return fileBlockId;
		}
		if (fs.isReadOnly()) {
			return -1;
		}
		fileBlockId = fs.allocFreeBlock();
		node.setBlockCount(node.getBlockCount() + 1);
		blocks.setFileBlock(blockId, fileBlockId);
		return fileBlockId;
	}
}
