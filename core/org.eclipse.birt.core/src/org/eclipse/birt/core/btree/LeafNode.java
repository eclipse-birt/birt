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
 *
 * NEXT_BLOCK		INT			if the node contains extra blocks
 * NODE_TYPE		INT			node type, must be LEAF
 *
 * NODE_SIZE		INT			node size, exclude the NODE_TYPE and NEXT_BLOCK
 * PREV_NODE_ID		INT			previous node id
 * NEXT_NODE_ID		INT			next node id
 * KEY_COUNT		INT			key count saved in this node
 * KEY_1			...			key
 * VALUES_TYPE  	INT			can be INLINE/EXTERNAL
 * VALUES			...			values
 *
 * </pre>
 *
 *
 * @param <K> the key type
 * @param <V> the value type
 */

class LeafNode<K, V> extends BTreeNode<K, V> {

	static final int EMPTY_NODE_SIZE = 16;

	private int prevNodeId = -1;
	private int nextNodeId = -1;
	private int nodeSize;

	private ArrayList<LeafEntry<K, V>> entries = new ArrayList<>();

	public LeafNode(BTree<K, V> btree, int nodeId) {
		super(btree, NODE_LEAF, nodeId);
		this.nodeSize = EMPTY_NODE_SIZE;
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

	public int getEntryCount() {
		return entries.size();
	}

	public int getNodeSize() {
		return nodeSize;
	}

	public LeafEntry<K, V> getFirstEntry() {
		if (entries.isEmpty()) {
			return null;
		}
		return entries.get(0);
	}

	public LeafEntry<K, V> getLastEntry() {
		if (entries.isEmpty()) {
			return null;
		}
		return entries.get(entries.size() - 1);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int search(final BTreeValue<K> key) throws IOException {
		return Collections.binarySearch(entries, key, new Comparator() {

			@Override
			public int compare(final Object entry, final Object key) {
				try {
					return btree.compare(((LeafEntry<K, V>) entry).getKey(), (BTreeValue<K>) key);
				} catch (final IOException ex) {
					return -1;
				}
			}
		});
	}

	/**
	 * return the first entry which key is less than or equal to the given key.
	 *
	 * @param key the search key value.
	 * @return the first entry which key is smaller than or equal to the given key.
	 * @throws IOException
	 */
	public LeafEntry<K, V> find(BTreeValue<K> key) throws IOException {
		int index = search(key);
		if (index >= 0) {
			return entries.get(index);
		}
		index = -(index + 1);
		if (index > 0) {
			return entries.get(index - 1);
		}
		// it can only happens for the first element of the first leaf
		return null;
	}

	public LeafEntry<K, V> insert(BTreeValue<K> key, BTreeValue<V>[] vs) throws IOException {
		assert vs != null;
		assert vs.length > 0;
		dirty = true;

		int index = search(key);
		if (index >= 0) {
			LeafEntry<K, V> insertPoint = entries.get(index);
			if (!btree.hasValue()) {
				return insertPoint;
			}
			// append or replace the value
			if (!btree.allowDuplicate()) {
				// replace the current value
				BTreeValues<V> values = insertPoint.getValues();
				int valueSize1 = values.getValueSize();
				SingleValueList<K, V> sv = new SingleValueList<>(btree, vs[0]);
				int valueSize2 = sv.getValueSize();
				insertPoint.setValues(sv);
				nodeSize = nodeSize + valueSize2 - valueSize1;
				return insertPoint;
			}

			// append it to the current values
			BTreeValues<V> values = insertPoint.getValues();
			int valueSize1 = values.getValueSize();
			for (BTreeValue<V> v : vs) {
				values.append(v);
			}
			int valueSize2 = values.getValueSize();
			if (valueSize2 > MAX_NODE_SIZE / 2) {
				values = btree.createExternalValueList(values);
				valueSize2 = values.getValueSize();
				insertPoint.setValues(values);
			}
			nodeSize = nodeSize - valueSize1 + valueSize2;

			btree.increaseTotalValues(vs.length);
			return insertPoint;
		}

		index = -(index + 1);
		// now we should insert the entry before the insert point
		BTreeValues<V> values = null;
		if (btree.hasValue()) {
			if (btree.allowDuplicate()) {
				values = new InlineValueList<>(btree);
				for (BTreeValue<V> v : vs) {
					values.append(v);
				}
				if (values.getValueSize() > MAX_NODE_SIZE / 2) {
					values = btree.createExternalValueList(values);
				}
			} else {
				values = new SingleValueList<>(btree, vs[0]);
			}
		}
		LeafEntry<K, V> entry = new LeafEntry<>(this, key, values);
		insert(index, entry);

		// if the node size is larger than the block size, split into two nodes.
		if (btree.hasValue()) {
			btree.increaseTotalValues(vs.length);
		}
		btree.increaseTotalKeys();
		return entry;
	}

	private void insert(int index, LeafEntry<K, V> entry) throws IOException {
		LeafEntry<K, V> prev = null;
		LeafEntry<K, V> next = null;
		if (index > 0) {
			prev = entries.get(index - 1);
		}
		if (index < entries.size()) {
			next = entries.get(index);
		}
		entries.add(index, entry);

		entry.setNode(this);

		// now we should insert the entry before the insert point
		entry.setPrev(prev);
		entry.setNext(next);
		if (prev != null) {
			prev.setNext(entry);
		}
		if (next != null) {
			next.setPrev(entry);
		}

		nodeSize += getEntrySize(entry);
	}

	public boolean needSplit() {
		return nodeSize > MAX_NODE_SIZE && entries.size() > MIN_ENTRY_COUNT;
	}

	private void resetNodeSize() throws IOException {
		nodeSize = EMPTY_NODE_SIZE;
		for (LeafEntry<K, V> entry : entries) {
			nodeSize += getEntrySize(entry);
		}
	}

	public IndexEntry<K, V> split() throws IOException {
		int splitIndex = entries.size() / 2;

		// create a new node for values which after (include) splitEntry
		LeafNode<K, V> newNode = btree.createLeafNode();
		try {
			// break the node list, the split entry should be kept in the new
			// node otherwise we can't find it
			LeafEntry<K, V> splitEntry = entries.get(splitIndex);
			LeafEntry<K, V> prev = splitEntry.getPrev();
			splitEntry.setPrev(null);
			if (prev != null) {
				prev.setNext(null);
			}
			// the following entries should be add to new node
			List<LeafEntry<K, V>> splitEntries = entries.subList(splitIndex, entries.size());
			for (LeafEntry<K, V> entry : splitEntries) {
				entry.setNode(newNode);
			}
			newNode.entries.addAll(splitEntries);
			newNode.resetNodeSize();

			// link the node together
			newNode.setNextNodeId(nextNodeId);
			newNode.setPrevNodeId(nodeId);
			if (nextNodeId != -1) {
				LeafNode<K, V> nextNode = btree.loadLeafNode(nextNodeId);
				try {
					nextNode.setPrevNodeId(newNode.getNodeId());
					nextNode.setDirty(true);
				} finally {
					nextNode.unlock();
				}
			}
			nextNodeId = newNode.getNodeId();

			// reset the new nodes
			ArrayList<LeafEntry<K, V>> remainEntries = new ArrayList<>(entries.subList(0, splitIndex));
			entries = remainEntries;
			resetNodeSize();

			// return the split entry
			return new IndexEntry<>(null, splitEntry.getKey(), newNode.getNodeId());
		} finally {
			newNode.unlock();
		}
	}

	@Override
	void read(DataInput in) throws IOException {
		nodeSize = in.readInt();
		prevNodeId = in.readInt();
		nextNodeId = in.readInt();
		int entryCount = in.readInt();
		LeafEntry<K, V> prev = null;
		for (int i = 0; i < entryCount; i++) {
			LeafEntry<K, V> entry = readEntry(in);
			entry.setPrev(prev);
			if (prev != null) {
				prev.setNext(entry);
			}
			entries.add(entry);
			prev = entry;
		}
	}

	@Override
	protected void write(DataOutput out) throws IOException {
		out.writeInt(nodeSize);
		out.writeInt(prevNodeId);
		out.writeInt(nextNodeId);
		out.writeInt(entries.size());
		for (LeafEntry<K, V> entry : entries) {
			writeEntry(out, entry);
		}
	}

	private int getEntrySize(LeafEntry<K, V> entry) {
		int keySize = btree.getKeySize(entry.getKey());
		if (btree.hasValue()) {
			BTreeValues<V> values = entry.getValues();
			if (btree.allowDuplicate()) {
				return keySize + 4 + values.getValueSize();
			}
			return keySize + values.getValueSize();
		}
		return keySize;
	}

	protected LeafEntry<K, V> readEntry(DataInput in) throws IOException {
		BTreeValue<K> key = btree.readKey(in);
		BTreeValues<V> values = readValues(in);
		return new LeafEntry<>(this, key, values);
	}

	private BTreeValues<V> readValues(DataInput in) throws IOException {
		if (btree.hasValue()) {
			if (btree.allowDuplicate()) {
				int type = in.readInt();
				if (type == BTreeValues.INLINE_VALUES) {
					InlineValueList<K, V> inlineValues = new InlineValueList<>(btree);
					inlineValues.read(in);
					return inlineValues;
				}
				if (type == BTreeValues.EXTERNAL_VALUES) {
					ExternalValueList<K, V> externalValues = new ExternalValueList<>(btree);
					externalValues.read(in);
					return externalValues;
				}
				throw new IOException(
						CoreMessages.getFormattedString(ResourceConstants.UNKNOWN_VALUE_TYPE, new Object[] { type }));
			}
			SingleValueList<K, V> singleValues = new SingleValueList<>(btree);
			singleValues.read(in);
			return singleValues;
		}
		return null;
	}

	private void writeEntry(DataOutput out, LeafEntry<K, V> entry) throws IOException {
		btree.writeKey(out, entry.getKey());
		if (btree.hasValue()) {
			BTreeValues<V> values = entry.getValues();
			if (btree.allowDuplicate()) {
				out.writeInt(values.getType());
			}
			values.write(out);
		}
	}

	@Override
	public void dumpNode() throws IOException {
		System.out.println("LeafNode:" + nodeId);
		System.out.println("nodeSize:" + nodeSize);
		System.out.println("prevNodeId:" + prevNodeId);
		System.out.println("nextNodeId :" + nextNodeId);
		System.out.println("entryCount:" + entries.size());
		for (LeafEntry<K, V> entry : entries) {
			System.out.print(btree.getKey(entry.getKey()) + "\"");
			if (btree.hasValue()) {
				System.out.print(" valueCount:" + entry.getValues().getValueCount());
				System.out.print(" valueSize:" + entry.getValues().getValueSize());
			}
			System.out.println();
		}
	}

	@Override
	public void dumpAll() throws IOException {
		dumpNode();

		for (LeafEntry<K, V> entry : entries) {
			BTreeValues<V> values = entry.getValues();
			if (values != null) {
				if (values.getType() == BTreeValues.EXTERNAL_VALUES) {
					@SuppressWarnings("unchecked")
					ExternalValueList<K, V> extValues = (ExternalValueList<K, V>) values;
					int nodeId = extValues.getFirstNodeId();
					while (nodeId != -1) {
						ValueNode<K, V> valueNode = btree.loadValueNode(nodeId);
						try {
							valueNode.dumpAll();
							nodeId = valueNode.getNextNodeId();
						} finally {
							valueNode.unlock();
						}
					}
				}
			}
		}
	}

}
