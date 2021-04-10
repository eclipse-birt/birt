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
 * the node structure is:
 * 
 * <pre>
 * 
 * NEXT_BLOCK		INT			if the node contains extra blocks
 * NODE_TYPE		INT			node type, must be VALUE
 * 
 * NODE_SIZE		INT			node size, exclude the NODE_TYPE and NEXT_BLOCK
 * PREV_NODE_ID		INT			previous node id
 * NEXT_NODE_ID		INT			next node id
 * VALUE_COUNT		INT			value count saved in this node
 * VALUE_1			...			value 1
 * VALUE_2  		...			value 2
 * 
 * </pre>
 * 
 * 
 * @param <K> the key type
 * @param <V> the value type
 */

public class ValueNode<K, V> extends BTreeNode<K, V> {

	static final int EMPTY_NODE_SIZE = 16;
	private int nodeSize;
	private int prevNodeId;
	private int nextNodeId;
	private int entryCount;
	private ValueEntry<V> firstEntry;
	private ValueEntry<V> lastEntry;

	ValueNode(BTree<K, V> tree, int id) {
		super(tree, NODE_VALUE, id);
		this.nodeSize = EMPTY_NODE_SIZE;
		this.prevNodeId = -1;
		this.nextNodeId = -1;
		this.entryCount = 0;
		this.firstEntry = null;
		this.lastEntry = null;
	}

	public void read(DataInput in) throws IOException {
		nodeSize = in.readInt();
		prevNodeId = in.readInt();
		nextNodeId = in.readInt();
		entryCount = in.readInt();
		for (int i = 0; i < entryCount; i++) {
			BTreeValue<V> value = btree.readValue(in);
			ValueEntry<V> entry = new ValueEntry<V>(value);
			if (firstEntry == null) {
				firstEntry = entry;
				lastEntry = entry;
			} else {
				lastEntry.setNext(entry);
				entry.setPrev(lastEntry);
				lastEntry = entry;
			}
		}
	}

	public void write(DataOutput out) throws IOException {
		out.writeInt(nodeSize);
		out.writeInt(prevNodeId);
		out.writeInt(nextNodeId);
		out.writeInt(entryCount);
		ValueEntry<V> entry = firstEntry;
		while (entry != null) {
			btree.writeValue(out, entry.getValue());
			entry = entry.getNext();
		}
	}

	public ValueEntry<V> getFirstEntry() {
		return firstEntry;
	}

	protected ValueEntry<V> getLastEntry() {
		return lastEntry;
	}

	public ValueEntry<V> append(BTreeValue<V> value) throws IOException {
		ValueEntry<V> entry = new ValueEntry<V>(value);
		// insert it as the last entry
		if (lastEntry == null) {
			firstEntry = entry;
			lastEntry = entry;
		} else {
			lastEntry.setNext(entry);
			entry.setPrev(lastEntry);
			lastEntry = entry;
		}
		nodeSize += btree.getValueSize(value);
		entryCount++;
		dirty = true;

		return entry;
	}

	public int getPrevNodeId() {
		return prevNodeId;
	}

	public void setPrevNodeId(int prevNodeId) {
		this.prevNodeId = prevNodeId;
	}

	public int getNextNodeId() {
		return nextNodeId;
	}

	public void setNextNodeId(int nextNodeId) {
		this.nextNodeId = nextNodeId;
	}

	public int getNodeSize() {
		return nodeSize;
	}

	public int getEntryCount() {
		return entryCount;
	}

	void dumpNode() throws IOException {
		System.out.println("VALUE:" + nodeId);
		System.out.println("nodeSize:" + nodeSize);
		System.out.println("prevNodeId:" + prevNodeId);
		System.out.println("nextNodeId:" + nextNodeId);
		System.out.println("entryCount:" + entryCount);
	}

	void dumpAll() throws IOException {
		dumpNode();
		ValueEntry<V> entry = firstEntry;
		while (entry != null) {
			System.out.println(btree.getValue(entry.getValue()));
			entry = entry.getNext();
		}
	}

}
