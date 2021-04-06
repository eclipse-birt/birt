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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 
 * the structure of the node is:
 * 
 * <pre>
 * NEXT_BLOCK_ID	INT		next block id
 * NODE_TYPE		INT		the node type, can be one of the LEAF/INDEX/VALUE/EXTRA
 * </pre>
 * 
 * @param <K>
 * @param <V>
 */
abstract public class BTreeNode<K, V> implements BTreeConstants {

	protected BTree<K, V> btree;

	protected int nodeType;
	protected int nodeId;

	protected int usedBlocks[];
	protected int lockCount;
	protected boolean dirty;

	BTreeNode(BTree<K, V> tree, int type, int id) {
		this.btree = tree;

		this.nodeType = type;
		this.nodeId = id;

		this.dirty = true;
		this.lockCount = 0;
		this.usedBlocks = new int[] { id };
	}

	public int getNodeId() {
		return nodeId;
	}

	public int getNodeType() {
		return nodeType;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	abstract void read(DataInput in) throws IOException;

	abstract void write(DataOutput out) throws IOException;

	public void lock() {
		lockCount++;
	}

	public void unlock() {
		lockCount--;
	}

	public boolean isLocked() {
		return lockCount != 0;
	}

	public void setUsedBlocks(int[] usedBlocks) {
		this.usedBlocks = usedBlocks;
	}

	public int[] getUsedBlocks() {
		return this.usedBlocks;
	}

	abstract void dumpAll() throws IOException;

	abstract void dumpNode() throws IOException;

}