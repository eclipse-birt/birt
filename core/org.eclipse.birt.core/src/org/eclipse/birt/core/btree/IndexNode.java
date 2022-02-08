/*******************************************************************************
 * Copyright (c) 2008,2010 Actuate Corporation.
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

package org.eclipse.birt.core.btree;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

/**
 * 
 * the node structure is:
 * 
 * <pre>
 * NEXT_BLOCK		INT			if the node contains extra blocks
 * NODE_TYPE		INT			node type, can be INDEX/LEAF/VALUE/EXTRA
 * 
 * NODE_SIZE		INT			node size, exclude the NODE_TYPE and NEXT_BLOCK
 * PREV_NODE_ID		INT			previous node id
 * NEXT_NODE_ID		INT			next node id
 * KEY_COUNT		INT			key count saved in this node
 * FIRST_CHILD		INT			child contains keys which are less than the first key
 * KEY_1			...			first key
 * CHILD_ID_1		INT			child node contains keys which are greater or equal than the first key 
 * KEY_2			...			second key
 * CHILD_ID_2		INT			child node contains keys which are greater than or equal to the second key
 * 
 * FIRST_CHILD &lt; KEY_1 &lt;= CHILD_ID_1 &lt; KEY_2 &lt;= CHILD_ID_2
 * 
 * </pre>
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public class IndexNode<K, V> extends BTreeNode<K, V> {

	static final int EMPTY_NODE_SIZE = 20;

	private int nodeSize;
	private int prevNodeId;
	private int nextNodeId;

	private int firstChild;
	private ArrayList<IndexEntry<K, V>> entries;

	IndexNode(BTree<K, V> btree, int nodeId) {
		super(btree, NODE_INDEX, nodeId);
		this.nodeSize = EMPTY_NODE_SIZE;
		this.prevNodeId = -1;
		this.nextNodeId = -1;
		this.firstChild = -1;
		this.entries = new ArrayList<IndexEntry<K, V>>();
	}

	public int getFirstChild() {
		return firstChild;
	}

	public void setFirstChild(int firstChild) {
		this.firstChild = firstChild;
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
		return entries.size();
	}

	public IndexEntry<K, V> getFirstEntry() {
		return entries.get(0);
	}

	public IndexEntry<K, V> getLastEntry() {
		return entries.get(entries.size() - 1);
	}

	public int getLastChild() {
		return entries.get(entries.size() - 1).getChildNodeId();
	}

	public LeafEntry<K, V> find(BTreeValue<K> key) throws IOException {
		int childNodeId = findChildNode(key);
		if (childNodeId != -1) {
			BTreeNode<K, V> node = btree.loadBTreeNode(childNodeId);
			try {
				if (node.nodeType == NODE_INDEX) {
					return ((IndexNode<K, V>) node).find(key);
				} else if (node.nodeType == NODE_LEAF) {
					return ((LeafNode<K, V>) node).find(key);
				}
				throw new IOException(CoreMessages.getFormattedString(ResourceConstants.UNEXPECTED_NODE_TYPE,
						new Object[] { node.getNodeType(), node.getNodeId() }));
			} finally {
				node.unlock();
			}
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int search(final BTreeValue<K> key) throws IOException {
		return Collections.binarySearch(entries, key, new Comparator() {

			public int compare(final Object entry, final Object key) {
				try {
					return btree.compare(((IndexEntry<K, V>) entry).getKey(), (BTreeValue<K>) key);
				} catch (final IOException ex) {
					return -1;
				}
			}
		});
	}

	/**
	 * find a entry which key is equal or less than the key.
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	private int findChildNode(BTreeValue<K> key) throws IOException {
		int index = search(key);
		if (index >= 0) {
			return entries.get(index).getChildNodeId();
		}
		index = -(index + 1);
		if (index == 0) {
			return this.firstChild;
		}
		return entries.get(index - 1).getChildNodeId();
	}

	public LeafEntry<K, V> insert(BTreeValue<K> key, BTreeValue<V>[] values) throws IOException {
		int childNodeId = findChildNode(key);
		if (childNodeId != -1) {
			BTreeNode<K, V> node = btree.loadBTreeNode(childNodeId);
			try {
				if (node.nodeType == NODE_INDEX) {
					IndexNode<K, V> indexNode = (IndexNode<K, V>) node;
					LeafEntry<K, V> insertEntry = indexNode.insert(key, values);
					if (indexNode.needSplit()) {
						IndexEntry<K, V> splitEntry = indexNode.split();
						if (splitEntry != null) {
							insertIndex(splitEntry.getKey(), splitEntry.getChildNodeId());
						}
					}
					return insertEntry;
				}
				if (node.nodeType == NODE_LEAF) {
					LeafNode<K, V> leafNode = (LeafNode<K, V>) node;
					LeafEntry<K, V> insertEntry = leafNode.insert(key, values);

					if (leafNode.needSplit()) {
						IndexEntry<K, V> splitEntry = leafNode.split();
						if (splitEntry != null) {
							insertIndex(splitEntry.getKey(), splitEntry.getChildNodeId());
						}
					}
					return insertEntry;
				}
				throw new IOException(CoreMessages.getFormattedString(ResourceConstants.UNEXPECTED_NODE_TYPE,
						new Object[] { node.getNodeType(), childNodeId }));
			} finally {
				node.unlock();
			}
		}
		return null;
	}

	protected void insertIndex(BTreeValue<K> insertKey, int childNodeId) throws IOException {
		int index = search(insertKey);
		assert index < 0;
		if (index >= 0) {
			throw new IOException("ERROR");
		}
		index = -(index + 1);
		// insert at the last entry
		IndexEntry<K, V> newEntry = new IndexEntry<K, V>(this, insertKey, childNodeId);
		entries.add(index, newEntry);
		nodeSize += getEntrySize(newEntry);

		dirty = true;
		return;
	}

	public boolean needSplit() {
		return nodeSize > MAX_NODE_SIZE && entries.size() > MIN_ENTRY_COUNT;
	}

	protected void resetNodeSize() {
		nodeSize = EMPTY_NODE_SIZE;
		for (IndexEntry<K, V> entry : entries) {
			nodeSize += getEntrySize(entry);
		}
	}

	public IndexEntry<K, V> split() throws IOException {
		// break at the node into two nodes: current and new node.
		int splitIndex = entries.size() / 2;

		// create a new node for splitEntry
		IndexNode<K, V> newNode = btree.createIndexNode();
		try {
			// for index node, the split entry will be moved to the upper level,
			// so we needn't keep it in the new node
			IndexEntry<K, V> splitEntry = entries.get(splitIndex);
			newNode.setFirstChild(splitEntry.getChildNodeId());

			List<IndexEntry<K, V>> splitEntries = entries.subList(splitIndex + 1, entries.size());
			for (IndexEntry<K, V> entry : splitEntries) {
				entry.setNode(newNode);
			}
			newNode.entries.addAll(splitEntries);
			newNode.resetNodeSize();

			// link the node together
			newNode.setPrevNodeId(nodeId);
			newNode.setNextNodeId(nextNodeId);
			if (nextNodeId != -1) {
				IndexNode<K, V> nextNode = btree.loadIndexNode(nextNodeId);
				try {
					nextNode.setPrevNodeId(newNode.getNodeId());
					nextNode.setDirty(true);
				} finally {
					nextNode.unlock();
				}
			}
			nextNodeId = newNode.getNodeId();

			ArrayList<IndexEntry<K, V>> remainEntries = new ArrayList<IndexEntry<K, V>>();
			remainEntries.addAll(entries.subList(0, splitIndex));
			entries = remainEntries;
			resetNodeSize();

			return new IndexEntry<K, V>(this, splitEntry.getKey(), newNode.getNodeId());
		} finally {
			newNode.unlock();
		}
	}

	public void read(DataInput in) throws IOException {
		nodeSize = in.readInt();
		prevNodeId = in.readInt();
		nextNodeId = in.readInt();
		int entryCount = in.readInt();
		firstChild = in.readInt();
		entries.clear();
		entries.ensureCapacity(entryCount);
		for (int i = 0; i < entryCount; i++) {
			IndexEntry<K, V> entry = readEntry(in);
			entries.add(entry);
		}
	}

	protected void write(DataOutput out) throws IOException {
		out.writeInt(nodeSize);
		out.writeInt(prevNodeId);
		out.writeInt(nextNodeId);
		out.writeInt(entries.size());
		out.writeInt(firstChild);
		for (IndexEntry<K, V> entry : entries) {
			writeEntry(out, entry);
		}
	}

	private IndexEntry<K, V> readEntry(DataInput in) throws IOException {
		BTreeValue<K> key = btree.readKey(in);
		int childNodeId = in.readInt();
		return new IndexEntry<K, V>(this, key, childNodeId);
	}

	private void writeEntry(DataOutput out, IndexEntry<K, V> entry) throws IOException {
		btree.writeKey(out, entry.getKey());
		out.writeInt(entry.getChildNodeId());
	}

	private int getEntrySize(IndexEntry<K, V> entry) {
		return 4 + btree.getKeySize(entry.getKey());
	}

	public void dumpNode() throws IOException {
		System.out.println("INDEX:" + nodeId);
		System.out.println("nodeSize:" + nodeSize);
		System.out.println("prevNodeId:" + prevNodeId);
		System.out.println("nextNodeId :" + nextNodeId);
		System.out.println("entryCount:" + entries.size());
		System.out.print(firstChild);
		for (IndexEntry<K, V> entry : entries) {
			System.out.print("<<[");
			System.out.print(btree.getKey(entry.getKey()));
			System.out.print("]<<");
			System.out.print(entry.getChildNodeId());
		}
		System.out.println();
	}

	public void dumpAll() throws IOException {
		dumpNode();
		BTreeNode<K, V> node = btree.loadBTreeNode(firstChild);
		try {
			node.dumpAll();
		} finally {
			node.unlock();
		}

		for (IndexEntry<K, V> entry : entries) {
			node = btree.loadBTreeNode(entry.getChildNodeId());
			try {
				node.dumpAll();
			} finally {
				node.unlock();
			}
		}
	}
}
