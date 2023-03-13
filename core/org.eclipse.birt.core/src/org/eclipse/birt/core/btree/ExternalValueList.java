/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

/**
 *
 * the structure of ExternalValueList is:
 *
 * <pre>
 * VALUE_COUNT		INT		total values in the list
 * LIST_NODE_ID		INT		the first VALUE_NODE used to save the list
 * LAST_NODE_ID		INT		the last VALUE_NODE used to save the list
 * </pre>
 *
 * @param <K>
 * @param <V>
 */
public class ExternalValueList<K, V> implements BTreeValues<V> {

	private BTree<K, V> btree;
	private int valueCount;
	private int firstNodeId;
	private int lastNodeId;

	public ExternalValueList(BTree<K, V> btree) {
		this.btree = btree;
		this.valueCount = 0;
		this.firstNodeId = -1;
		this.lastNodeId = -1;
	}

	@Override
	public int getType() {
		return EXTERNAL_VALUES;
	}

	@Override
	public int getValueSize() {
		return 12;
	}

	public int getFirstNodeId() {
		return firstNodeId;
	}

	public int getLastNodeId() {
		return lastNodeId;
	}

	@Override
	public void read(DataInput input) throws IOException {
		valueCount = input.readInt();
		firstNodeId = input.readInt();
		lastNodeId = input.readInt();
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeInt(valueCount);
		output.writeInt(firstNodeId);
		output.writeInt(lastNodeId);
	}

	@Override
	public Value<V> getFirstValue() throws IOException {
		if (firstNodeId != -1) {
			ValueNode<K, V> valueNode = btree.loadValueNode(firstNodeId);
			try {
				ValueEntry<V> entry = valueNode.getFirstEntry();
				return new ExternalValue(valueNode, entry);
			} finally {
				valueNode.unlock();
			}
		}
		return null;
	}

	@Override
	public Value<V> getLastValue() throws IOException {
		if (lastNodeId != -1) {
			ValueNode<K, V> valueNode = btree.loadValueNode(lastNodeId);
			try {
				ValueEntry<V> entry = valueNode.getLastEntry();
				return new ExternalValue(valueNode, entry);
			} finally {
				valueNode.unlock();
			}
		}
		return null;
	}

	@Override
	public int getValueCount() {
		return valueCount;
	}

	@Override
	public Value<V> append(BTreeValue<V> value) throws IOException {
		if (lastNodeId == -1) {
			ValueNode<K, V> valueNode = btree.createValueNode();
			try {
				firstNodeId = valueNode.getNodeId();
				lastNodeId = valueNode.getNodeId();
				ValueEntry<V> entry = valueNode.append(value);
				valueCount++;
				return new ExternalValue(valueNode, entry);
			} finally {
				valueNode.unlock();
			}
		}

		ValueNode<K, V> lastNode = btree.loadValueNode(lastNodeId);
		try {
			int valueSize = btree.getValueSize(value);
			int nodeSize = lastNode.getNodeSize();
			if (nodeSize + valueSize <= BTreeConstants.MAX_NODE_SIZE) {
				// append it to the new node
				ValueEntry<V> entry = lastNode.append(value);
				valueCount++;
				return new ExternalValue(lastNode, entry);
			}

			// append the value into the new node
			ValueNode<K, V> valueNode = btree.createValueNode();
			try {
				ValueEntry<V> entry = valueNode.append(value);
				valueNode.setPrevNodeId(lastNode.getNodeId());
				lastNode.setNextNodeId(valueNode.getNodeId());
				lastNode.setDirty(true);
				lastNodeId = valueNode.getNodeId();
				valueCount++;
				return new ExternalValue(valueNode, entry);
			} finally {
				valueNode.unlock();
			}
		} finally {
			lastNode.unlock();
		}
	}

	class ExternalValue implements Value<V> {

		ValueNode<K, V> node;
		ValueEntry<V> entry;

		ExternalValue(ValueNode<K, V> node, ValueEntry<V> entry) {
			this.node = node;
			this.entry = entry;
		}

		@Override
		public Value<V> getPrev() throws IOException {
			ValueEntry<V> prev = entry.getPrev();
			if (prev != null) {
				return new ExternalValue(node, prev);
			}

			int prevNodeId = node.getPrevNodeId();
			if (prevNodeId != -1) {
				ValueNode<K, V> prevNode = btree.loadValueNode(prevNodeId);
				try

				{
					prev = prevNode.getLastEntry();
					if (prev != null) {
						return new ExternalValue(prevNode, prev);
					}
				} finally {
					prevNode.unlock();
				}
			}
			return null;
		}

		@Override
		public Value<V> getNext() throws IOException {
			ValueEntry<V> next = entry.getNext();
			if (next != null) {
				return new ExternalValue(node, next);
			}
			int nextNodeId = node.getNextNodeId();
			if (nextNodeId != -1) {
				ValueNode<K, V> nextNode = btree.loadValueNode(nextNodeId);
				try {
					next = nextNode.getFirstEntry();
					if (next != null) {
						return new ExternalValue(nextNode, next);
					}
				} finally {
					nextNode.unlock();
				}
			}
			return null;
		}

		@Override
		public BTreeValue<V> getValue() {
			return entry.getValue();
		}
	}
}
