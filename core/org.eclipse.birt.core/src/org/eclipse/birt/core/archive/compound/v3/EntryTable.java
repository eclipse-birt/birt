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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Collections;
import java.util.TreeMap;

/**
 * The entry table maintains a {@link TreeMap} ordered by entry name key
 * {@link String} with {@link Ext2Entry} values that represent the underlying
 * {@link Ext2FileSystem}.
 * <p>
 * This implementation provides constant time pre-sorted iteration order for
 * {@link #listAllEntries()} and {@link #listEntries(String)}, and log(n) time
 * cost for {@link #addEntry(Ext2Entry)}, {@link #getEntry(String)}, and
 * {@link #removeEntry(String)}.
 */
public class EntryTable {

	private final Ext2FileSystem fs;
	private final TreeMap<String, Ext2Entry> entries;
	private boolean dirty;

	EntryTable(Ext2FileSystem fs) {
		this.fs = fs;
		this.entries = new TreeMap<String, Ext2Entry>();
		this.dirty = true;
	}

	void read() throws IOException {
		Ext2File file = new Ext2File(fs, NodeTable.INODE_ENTRY_TABLE, false);
		try {
			byte[] bytes = new byte[(int) file.length()];
			file.read(bytes, 0, bytes.length);
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
			try {
				while (true) {
					String name = in.readUTF();
					int inode = in.readInt();
					entries.put(name, new Ext2Entry(name, inode));
				}
			} catch (EOFException ex) {
				// expect the EOF exception
			} finally {
				in.close();
			}

		} finally {
			file.close();
		}
		this.dirty = false;
	}

	void write() throws IOException {
		if (!dirty) {
			return;
		}
		dirty = false;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buffer);
		for (Ext2Entry entry : entries.values()) {
			out.writeUTF(entry.name);
			out.writeInt(entry.inode);
		}

		Ext2File file = new Ext2File(fs, NodeTable.INODE_ENTRY_TABLE, false);
		try {
			byte[] bytes = buffer.toByteArray();
			file.write(bytes, 0, bytes.length);
			file.setLength(bytes.length);
		} finally {
			file.close();
		}
	}

	Ext2Entry getEntry(String name) {
		return entries.get(name);
	}

	Ext2Entry removeEntry(String name) {
		Ext2Entry entry = entries.remove(name);
		if (entry != null) {
			dirty = true;
		}
		return entry;
	}

	void addEntry(Ext2Entry entry) {
		dirty = true;
		entries.put(entry.name, entry);
	}

	/**
	 * @return sorted view of all entry names
	 */
	Iterable<String> listAllEntries() {
		return Collections.unmodifiableSet(entries.keySet());
	}

	/**
	 * @param fromName entry name low end point
	 * @return sorted set view of entry names, beginning from the specified low end
	 *         point to the end of the entries.
	 */
	Iterable<String> listEntries(String fromName) {
		return PrefixedIterable.filteredByPrefix(entries, fromName);
	}

	void clear() {
		dirty = true;
		entries.clear();
	}

}
