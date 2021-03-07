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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The fat block is initialized to 0. It won't cause conflicts as the 0 block is
 * a data block (header).
 *
 */
public class Ext2Node implements Ext2Constants {

	static final int STATUS_UNUSED = 0;
	static final int STATUS_USED = 1;

	static final int NODE_SIZE = 64;
	static final int DIRECT_BLOCK_COUNT = 9;
	static final int INDIRECT_BLOCK_COUNT = 3;

	private boolean dirty;
	private int nodeId;
	private int status;
	private long length;
	private int blockCount;
	private int[] directBlocks = new int[DIRECT_BLOCK_COUNT];
	private int[] indirectBlocks = new int[INDIRECT_BLOCK_COUNT];

	Ext2Node() {
		this(-1);
	}

	Ext2Node(int id) {
		this.nodeId = id;
		for (int i = 0; i < DIRECT_BLOCK_COUNT; i++) {
			directBlocks[i] = -1;
		}
		for (int i = 0; i < INDIRECT_BLOCK_COUNT; i++) {
			indirectBlocks[i] = -1;
		}
		dirty = true;
	}

	boolean isDirty() {
		return dirty;
	}

	void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public void reset() {
		this.status = STATUS_UNUSED;
		this.blockCount = 0;
		this.length = 0;
		for (int i = 0; i < DIRECT_BLOCK_COUNT; i++) {
			directBlocks[i] = -1;
		}
		for (int i = 0; i < INDIRECT_BLOCK_COUNT; i++) {
			indirectBlocks[i] = -1;
		}
		dirty = true;
	}

	public Ext2Node copyFreeNode() {
		Ext2Node freeNode = new Ext2Node();
		freeNode.blockCount = blockCount;
		for (int i = 0; i < DIRECT_BLOCK_COUNT; i++) {
			freeNode.directBlocks[i] = directBlocks[i];
		}
		for (int i = 0; i < INDIRECT_BLOCK_COUNT; i++) {
			freeNode.indirectBlocks[i] = indirectBlocks[i];
		}
		return freeNode;
	}

	public int getNodeId() {
		return nodeId;
	}

	int getStatus() {
		return status;
	}

	void setStatus(int status) {
		this.status = status;
		this.dirty = true;
	}

	public long getLength() {
		return length;
	}

	void setLength(long length) {
		this.length = length;
		this.dirty = true;
	}

	int getBlockCount() {
		return blockCount;
	}

	void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
		this.dirty = true;
	}

	void read(DataInput in) throws IOException {
		status = in.readInt();
		length = in.readLong();
		blockCount = in.readInt();
		for (int i = 0; i < DIRECT_BLOCK_COUNT; i++) {
			directBlocks[i] = in.readInt();
		}
		for (int i = 0; i < INDIRECT_BLOCK_COUNT; i++) {
			indirectBlocks[i] = in.readInt();
		}
		dirty = false;
	}

	void write(DataOutput out) throws IOException {
		out.writeInt(status);
		out.writeLong(length);
		out.writeInt(blockCount);
		for (int i = 0; i < DIRECT_BLOCK_COUNT; i++) {
			out.writeInt(directBlocks[i]);
		}
		for (int i = 0; i < INDIRECT_BLOCK_COUNT; i++) {
			out.writeInt(indirectBlocks[i]);
		}
	}

	int getDirectBlock(int index) {
		assert index < DIRECT_BLOCK_COUNT;
		return directBlocks[index];
	}

	void setDirectBlock(int index, int blockId) {
		assert index < DIRECT_BLOCK_COUNT;
		directBlocks[index] = blockId;
		this.dirty = true;
	}

	int getIndirectBlock(int index) {
		assert index < INDIRECT_BLOCK_COUNT;
		return indirectBlocks[index];
	}

	void setIndirectBlock(int index, int blockId) {
		assert index < INDIRECT_BLOCK_COUNT;
		indirectBlocks[index] = blockId;
	}
}
