/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
import java.util.ArrayList;
import java.util.Collection;

/**
 * A table contains all the stream item blocks of a compound file system.
 */
class NameTable implements ArchiveConstants {

	protected ArrayList<NameEntry> slots;
	protected AllocEntry index;
	protected ArchiveFileV2 af;
	protected final int BLOCK_SIZE;

	private NameTable(ArchiveFileV2 af) throws IOException {
		this.af = af;
		BLOCK_SIZE = af.BLOCK_SIZE;
		slots = new ArrayList<NameEntry>();
		index = af.allocTbl.loadEntry(ENTRY_TABLE_BLOCK);
	}

	static NameTable loadTable(ArchiveFileV2 af) throws IOException {
		NameTable table = new NameTable(af);
		table.refresh();
		return table;
	}

	static NameTable createTable(ArchiveFileV2 af) throws IOException {
		NameTable table = new NameTable(af);
		return table;
	}

	synchronized void refresh() throws IOException {
		// refresh the index
		index.refresh(af.allocTbl);

		// refresh the slots
		int maxSlots = index.getTotalBlocks() * BLOCK_SIZE / ENTRY_ITEM_SIZE;
		int lastSlot = slots.size();
		while (lastSlot < maxSlots) {
			NameEntry entry = new NameEntry(lastSlot);
			entry.read(this);
			int usedSlots = entry.getUsedSlots();
			// this is the last entry
			if (usedSlots == -1) {
				break;
			}
			// the entry has been removed
			if (usedSlots == 0) {
				usedSlots = 1;
			}
			lastSlot += usedSlots;
			// the entry is an valid entry
			slots.add(entry);
			for (int i = 1; i < usedSlots; i++) {
				slots.add(null);
			}
		}
		assert slots.size() == lastSlot;
	}

	synchronized void flush() throws IOException {
		ensureSlots(slots.size() + 1);
		for (NameEntry entry : slots) {
			if (entry != null) {
				entry.write(this);
			}
		}
		// write the last slots
		NameEntry lastEntry = new NameEntry(slots.size());
		lastEntry.setUsedSlots(-1);
		lastEntry.write(this);
	}

	synchronized void removeEntry(NameEntry entry) throws IOException {
		int slotId = entry.getSlotID();
		int usedSlots = entry.getUsedSlots();
		for (int i = slotId; i < usedSlots; i++) {
			slots.set(i, new NameEntry(i));
		}
	}

	/**
	 * create a stream item
	 * 
	 * @param name stream name
	 * @return the stream item created
	 * @throws IOException
	 */

	synchronized NameEntry createEntry(String name) throws IOException {
		int lastSlot = slots.size();
		NameEntry entry = new NameEntry(lastSlot, name);
		int usedSlots = entry.getUsedSlots();
		slots.add(entry);
		for (int i = 1; i < usedSlots; i++) {
			slots.add(null);
		}
		return entry;
	}

	synchronized Collection<NameEntry> listEntries() {
		ArrayList<NameEntry> entries = new ArrayList<NameEntry>();
		for (NameEntry entry : slots) {
			if (entry != null) {
				if (entry.getUsedSlots() != 0) {
					entries.add(entry);
				}
			}
		}
		return entries;
	}

	private void ensureSlots(int slotSize) throws IOException {
		// the last slot is used to indicate the EOF table
		long offset = (long) (slotSize + 1) * ENTRY_ITEM_SIZE;
		int blockCount = (int) ((offset + BLOCK_SIZE - 1) / BLOCK_SIZE);
		int totalBlock = index.getTotalBlocks();
		while (blockCount > totalBlock) {
			int freeBlock = af.allocTbl.getFreeBlock();
			index.appendBlock(freeBlock);
			totalBlock++;
		}
	}

	void readSlot(int slotId, byte[] b, int off) throws IOException {
		long offset = (long) slotId * ENTRY_ITEM_SIZE;
		int blkId = (int) (offset / BLOCK_SIZE);
		int blkOff = (int) (offset % BLOCK_SIZE);
		int phyBlk = index.getBlock(blkId);
		af.read(phyBlk, blkOff, b, off, ENTRY_ITEM_SIZE);
	}

	void writeSlot(int slotId, byte[] b, int off) throws IOException {
		long offset = (long) slotId * ENTRY_ITEM_SIZE;
		int blkId = (int) (offset / BLOCK_SIZE);
		int blkOff = (int) (offset % BLOCK_SIZE);
		int phyBlk = index.getBlock(blkId);
		int size = b.length - off;
		if (size > ENTRY_ITEM_SIZE) {
			size = ENTRY_ITEM_SIZE;
		}
		af.write(phyBlk, blkOff, b, off, size);
	}

	void debug_dump() {
		System.out.println("NAME TABLE:");
		for (int i = 0; i < index.getTotalBlocks(); i++) {
			System.out.print(index.getBlock(i) + ",");
		}
		System.out.println();
		for (int i = 0; i < slots.size(); i++) {
			NameEntry entry = (NameEntry) slots.get(i);
			if (entry != null) {
				System.out.print(entry.getSlotID() + ",");
				System.out.print(entry.getUsedSlots() + ",");
				if (entry.getUsedSlots() > 0) {
					System.out.print(entry.getLength() + ",");
					System.out.print(entry.getName() + ",");
					System.out.print(entry.getBlock());
				}
				System.out.println();
			}
		}
	}
}
