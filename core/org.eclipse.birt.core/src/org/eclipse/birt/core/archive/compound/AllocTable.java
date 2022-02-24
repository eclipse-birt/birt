/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.birt.core.archive.ArchiveUtil;

/**
 * The AllocationTable defines the FAT table in the archive file.
 *
 * The FAT table a integer array, each element represent a block in the stream.
 * The block id is the array index. The value of the element is the id of the
 * next block followed by the block index. It constructs a link list which saves
 * the blocks in a stream.
 * <p>
 * There are multiple links in the FAT table.
 * <li>The first link stream is the free blocks which starts from block 0.</li>
 * <li>The second link stream is the FAT table itself which starts from block
 * 1.</li>
 * <li>The third link stream is the entry table which starts from block 2.</li>
 * <li>The other streams id are saved in the entry table.</li>
 */
class AllocTable implements ArchiveConstants {

	/**
	 * the archive file which use this allocation table.
	 */
	protected ArchiveFileV2 af;

	final int BLOCK_SIZE;

	final int INDEX_PER_BLOCK;

	protected AllocEntry fatEntry;

	protected AllocEntry freeEntry;

	protected HashMap<Integer, AllocEntry> entries = new HashMap<>();

	AllocTable(ArchiveFileV2 af) {
		this.af = af;
		BLOCK_SIZE = af.BLOCK_SIZE;
		INDEX_PER_BLOCK = BLOCK_SIZE / 4;
		fatEntry = new AllocEntry(ALLOC_TABLE_BLOCK);
		freeEntry = new AllocEntry(0);
		entries.put(Integer.valueOf(ENTRY_TABLE_BLOCK), new AllocEntry(ENTRY_TABLE_BLOCK));
	}

	static AllocTable createTable(ArchiveFileV2 af) throws IOException {
		AllocTable table = new AllocTable(af);
		return table;
	}

	static AllocTable loadTable(ArchiveFileV2 af) throws IOException {
		AllocTable table = new AllocTable(af);
		table.load();
		return table;
	}

	synchronized void load() throws IOException {
		AllocTableLoader loader = new AllocTableLoader();
		loader.load(af);
		ArrayList<AllocEntry> loadedEntries = loader.getEntryies();
		for (int i = 0; i < loadedEntries.size(); i++) {
			AllocEntry entry = loadedEntries.get(i);
			int blockId = entry.getFirstBlock();
			if (blockId == ALLOC_TABLE_BLOCK) {
				fatEntry = entry;
			} else if (blockId == 0) {
				freeEntry = entry;
			} else {
				entries.put(Integer.valueOf(entry.getFirstBlock()), entry);
			}
		}
	}

	/**
	 * write the whole FAT table into the file.
	 *
	 * @throws IOException
	 */
	synchronized void flush() throws IOException {
		// flush all entries in the table
		for (AllocEntry entry : entries.values()) {
			entry.flush(this);
		}

		// flush the total blocks
		fatEntry.flush(this);

		// flush the free block list
		freeEntry.flush(this);
	}

	/**
	 * reload the FAT table from the file.
	 *
	 * @throws IOException
	 */
	synchronized void refresh() throws IOException {
		// reload the fat tables
		fatEntry.refresh(this);

		// the free blocks is only used by the writer, so we needn't refresh it.

		// refresh all entries in the table
		for (AllocEntry entry : entries.values()) {
			entry.refresh(this);
		}
	}

	/**
	 * allocate a block from the free list. If the free list is empty, create a new
	 * block at the end of file.
	 *
	 * @return the block id.
	 * @throws IOException
	 */
	synchronized int getFreeBlock() throws IOException {
		// get the free block
		if (freeEntry.getTotalBlocks() > 1) {
			int freeBlockId = freeEntry.removeLastBlock();
			return freeBlockId;
		} else {
			int freeBlockId = af.allocateBlock();
			if (freeBlockId % INDEX_PER_BLOCK == 0) {
				freeBlockId = af.allocateBlock();
			}
			return freeBlockId;
		}
	}

	synchronized AllocEntry createEntry() throws IOException {
		int blockId = getFreeBlock();
		AllocEntry entry = new AllocEntry(blockId);
		entries.put(Integer.valueOf(blockId), entry);
		return entry;
	}

	synchronized AllocEntry loadEntry(int blockId) throws IOException {
		AllocEntry entry = (AllocEntry) entries.get(Integer.valueOf(blockId));
		if (entry == null) {
			entry = new AllocEntry(blockId);
			entry.refresh(this);
			entries.put(Integer.valueOf(blockId), entry);
		}
		return entry;
	}

	/**
	 * remove the entry from the FAT table.
	 *
	 * @param entry the entry to be removed.
	 * @throws IOException
	 */
	synchronized void removeEntry(AllocEntry entry) throws IOException {
		int totalBlocks = entry.getTotalBlocks();
		for (int i = 0; i < totalBlocks; i++) {
			int freeBlock = entry.getBlock(i);
			freeEntry.appendBlock(freeBlock);
		}
		entries.remove(Integer.valueOf(entry.getFirstBlock()));
	}

	int readFATInt(long offset) throws IOException {
		int totalBlocks = fatEntry.getTotalBlocks();
		if (offset > (long) totalBlocks * BLOCK_SIZE) {
			throw new EOFException();
		}
		int blockId = (int) (offset / BLOCK_SIZE);
		int off = (int) (offset % BLOCK_SIZE);
		int phyBlockId = fatEntry.getBlock(blockId);
		byte[] b = new byte[4];
		af.read(phyBlockId, off, b, 0, 4);
		return ArchiveUtil.bytesToInteger(b);
	}

	void writeFATInt(long offset, int block) throws IOException {
		int totalBlocks = fatEntry.getTotalBlocks();
		int blockId = (int) (offset / BLOCK_SIZE);
		int off = (int) (offset % BLOCK_SIZE);
		if (blockId >= totalBlocks) {
			int newTotalBlocks = blockId + 1;
			for (int i = totalBlocks; i < newTotalBlocks; i++) {
				fatEntry.appendBlock(INDEX_PER_BLOCK * i);
			}
		}
		int phyBlockId = fatEntry.getBlock(blockId);
		byte[] b = new byte[4];
		ArchiveUtil.integerToBytes(block, b);
		af.write(phyBlockId, off, b, 0, 4);
	}

	void debug_dump() {
		System.out.println("ALLOC:");
		for (int i = 0; i < fatEntry.getTotalBlocks(); i++) {
			System.out.print(fatEntry.getBlock(i) + ",");
		}
		System.out.println();
		System.out.println("FREE:");
		for (int i = 0; i < freeEntry.getTotalBlocks(); i++) {
			System.out.print(freeEntry.getBlock(i) + ",");
		}
		System.out.println();
		for (AllocEntry entry : entries.values()) {
			for (int i = 0; i < entry.getTotalBlocks(); i++) {
				System.out.print(entry.getBlock(i) + ",");
			}
			System.out.println();
		}

	}
}
