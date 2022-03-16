/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

import org.eclipse.birt.core.archive.cache.Cacheable;

/**
 * A physical block in a physical compound file, might be stream items block,
 * index block or stream data block.
 */

public class Block extends Cacheable implements ArchiveConstants {

	ArchiveFileV2 af;

	/** The physical ID -- the NO of this block */
	int id;

	final int blockSize;

	/** data of the block */
	byte[] blockData;

	private int dataSize;

	private int dirtyStart;

	private int dirtyEnd;

	/**
	 * Constructor
	 *
	 * @param fs      the compound file system it belongs to
	 * @param blockId the block ID
	 */
	Block(ArchiveFileV2 af, int blockId, int size) {
		super(af.caches, Integer.valueOf(blockId));
		this.af = af;
		blockSize = size;
		blockData = new byte[size];
		id = blockId;
		dirtyStart = 0;
		dirtyEnd = 0;
		dataSize = 0;
	}

	public void refresh() throws IOException {
		dataSize = af.read(id, 0, blockData, 0, blockSize);
		dirtyStart = 0;
		dirtyEnd = 0;
	}

	public void flush() throws IOException {

		if (dirtyEnd != dirtyStart) {
			af.write(id, dirtyStart, blockData, dirtyStart, dirtyEnd - dirtyStart);
		}
		dirtyEnd = dirtyStart = 0;
	}

	public byte[] getData() {
		return blockData;
	}

	public int write(int tgt, byte b[], int off, int len) throws IOException {
		int size = blockSize - tgt;
		if (size > len) {
			size = len;
		}
		System.arraycopy(b, off, blockData, tgt, size);
		if (dirtyStart > tgt) {
			dirtyStart = tgt;
		}
		if (dirtyEnd < tgt + size) {
			dirtyEnd = tgt + size;
		}

		if (dataSize < dirtyEnd) {
			dataSize = dirtyEnd;
		}
		return size;
	}

	public int read(int src, byte b[], int off, int len) throws IOException {
		int size = dataSize - src;
		if (size > len) {
			size = len;
		}
		System.arraycopy(blockData, src, b, off, size);
		return size;
	}
}
