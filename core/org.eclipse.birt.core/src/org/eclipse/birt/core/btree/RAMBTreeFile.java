/*******************************************************************************
 * Copyright (c) 2008,2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.btree;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class RAMBTreeFile implements NodeFile {

	private ArrayList<byte[]> blocks = new ArrayList<byte[]>();

	public RAMBTreeFile() {
	}

	public void close() {
	}

	public int getTotalBlock() {
		return blocks.size();
	}

	public int allocBlock() throws IOException {
		byte[] block = new byte[BLOCK_SIZE];
		BTreeUtils.integerToBytes(-1, block);
		blocks.add(block);
		return blocks.size() - 1;
	}

	public void freeBlock(int blockId) throws IOException {

	}

	public void readBlock(int blockId, byte[] bytes) throws IOException {
		if (bytes == null) {
			throw new NullPointerException();
		}

		if (blockId >= blocks.size()) {
			throw new EOFException(CoreMessages.getFormattedString(ResourceConstants.EXCEED_MAX_BLOCK,
					new Object[] { blockId, blocks.size() }));
		}

		byte[] block = blocks.get(blockId);
		int length = bytes.length > BLOCK_SIZE ? BLOCK_SIZE : bytes.length;
		System.arraycopy(block, 0, bytes, 0, length);
	}

	public void writeBlock(int blockId, byte[] bytes) throws IOException {
		if (bytes == null) {
			throw new NullPointerException();
		}

		if (blockId >= blocks.size()) {
			for (int i = 0; i <= blockId; i++) {
				byte[] block = new byte[BLOCK_SIZE];
				BTreeUtils.integerToBytes(-1, block);
				blocks.add(block);
			}
		}

		byte[] block = blocks.get(blockId);
		int length = bytes.length > BLOCK_SIZE ? BLOCK_SIZE : bytes.length;
		System.arraycopy(bytes, 0, block, 0, length);
	}

	public void read(String file) throws IOException {
		blocks.clear();
		RandomAccessFile rf = new RandomAccessFile(file, "r");
		try {
			int blockCount = (int) (rf.length() / BLOCK_SIZE);
			byte[] block = new byte[BLOCK_SIZE];
			for (int i = 0; i < blockCount; i++) {
				rf.readFully(block);
				blocks.add(block);
			}
		} finally {
			rf.close();
		}
	}

	public void read(InputStream in) throws IOException {
		blocks.clear();
		DataInputStream data = new DataInputStream(in);
		try {
			while (true) {
				byte[] block = new byte[BLOCK_SIZE];
				data.readFully(block);
				blocks.add(block);
			}
		} catch (EOFException ex) {
		}
	}

	public void write(String file) throws IOException {
		RandomAccessFile rf = new RandomAccessFile(file, "w");
		try {
			int blockCount = blocks.size();
			for (int i = 0; i < blockCount; i++) {
				byte[] block = (byte[]) blocks.get(i);
				rf.write(block);
			}
		} finally {
			rf.close();
		}
	}

	public Object lock() throws IOException {
		return this;
	}

	public void unlock(Object lock) throws IOException {
	}
}
