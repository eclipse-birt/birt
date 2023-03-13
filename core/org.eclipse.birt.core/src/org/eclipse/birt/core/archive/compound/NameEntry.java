/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.archive.ArchiveUtil;

/**
 * name entry in the entry table. Each entry represents a stream in the file.
 *
 */
class NameEntry implements ArchiveConstants {

	/**
	 * the slot id of the entry, it is used as the stream id.
	 */
	private int slotId;
	/**
	 * the slots used by this entry. For stream with long name, it may used mutiple
	 * slots.
	 */
	private int usedSlots;
	/**
	 * the length of the stream.
	 */
	private long length;
	/**
	 * the first block of this stream.
	 */
	private int firstBlock;
	/**
	 * the name of the stream.
	 */
	private String name;

	NameEntry(int slotId) {
		this.slotId = slotId;
		this.name = null;
		this.usedSlots = 0;
		this.firstBlock = -1;
		this.length = 0;
	}

	NameEntry(int slotId, String name) {
		this.slotId = slotId;
		this.name = name;
		this.usedSlots = caculateUsedSlots(name);
		this.firstBlock = -1;
		this.length = 0;
	}

	private int caculateUsedSlots(String name) {
		ByteArrayOutputStream buff = new ByteArrayOutputStream(name.length() * 2);
		try {
			DataOutputStream os = new DataOutputStream(buff);
			os.writeUTF(name);
			os.flush();
		} catch (Exception ex) {

		}
		int totalSize = buff.size() + 4 + 8 + 4;
		return (totalSize + ENTRY_ITEM_SIZE - 1) / ENTRY_ITEM_SIZE;
	}

	void read(NameTable tbl) throws IOException {
		byte[] b = new byte[ENTRY_ITEM_SIZE];
		tbl.readSlot(slotId, b, 0);
		usedSlots = ArchiveUtil.bytesToInteger(b);
		if (usedSlots <= 0) {
			return;
		}
		if (usedSlots > 1) {
			byte[] nb = new byte[ENTRY_ITEM_SIZE * usedSlots];
			System.arraycopy(b, 0, nb, 0, ENTRY_ITEM_SIZE);
			for (int i = 1; i < usedSlots; i++) {
				tbl.readSlot(slotId + i, nb, i * ENTRY_ITEM_SIZE);
			}
			b = nb;
		}
		DataInputStream is = new DataInputStream(new ByteArrayInputStream(b));
		is.skipBytes(4); // skip the used slots
		length = is.readLong();
		firstBlock = is.readInt();
		name = is.readUTF();
	}

	void write(NameTable tbl) throws IOException {
		if (usedSlots > 0) {
			ByteArrayOutputStream buff = new ByteArrayOutputStream(ENTRY_ITEM_SIZE * usedSlots);
			DataOutputStream os = new DataOutputStream(buff);
			os.writeInt(usedSlots);
			os.writeLong(length);
			os.writeInt(firstBlock);
			os.writeUTF(name);
			byte[] b = buff.toByteArray();
			for (int i = 0; i < usedSlots; i++) {
				tbl.writeSlot(slotId + i, b, i * ENTRY_ITEM_SIZE);
			}
		} else {
			ByteArrayOutputStream buff = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(buff);
			os.writeInt(usedSlots);
			os.writeLong(length);
			os.writeInt(firstBlock);
			os.writeUTF("");
			byte[] b = buff.toByteArray();
			tbl.writeSlot(slotId, b, 0);
		}
	}

	int getSlotID() {
		return slotId;
	}

	int getUsedSlots() {
		return usedSlots;
	}

	void setUsedSlots(int slots) {
		usedSlots = slots;
	}

	String getName() {
		return name;
	}

	long getLength() {
		return length;
	}

	void setLength(long length) {
		this.length = length;
	}

	int getBlock() {
		return firstBlock;
	}

	void setBlock(int blockId) {
		this.firstBlock = blockId;
	}
}
