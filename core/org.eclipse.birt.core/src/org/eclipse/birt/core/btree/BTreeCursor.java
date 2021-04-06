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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class BTreeCursor<K, V> {

	protected BTree<K, V> btree;
	protected LeafEntry<K, V> entry;
	protected boolean beforeFirst;

	BTreeCursor(BTree<K, V> btree) {
		this.btree = btree;
		// before the first entry...
		this.entry = null;
		beforeFirst = true;
	}

	/**
	 * return the entry count in the cursor.
	 * 
	 * @return
	 */
	public int getTotalKeys() {
		return btree.getTotalKeys();
	}

	public int getTotalValues() {
		return btree.getTotalValues();
	}

	/**
	 * move the cursor before the first.
	 */
	public void beforeFirst() throws IOException {
		if (entry != null) {
			btree.unlockEntry(entry);
		}
		entry = null;
		beforeFirst = true;
	}

	/**
	 * move the cursor after the last
	 */
	public void afterLast() throws IOException {
		if (entry != null) {
			btree.unlockEntry(entry);
		}
		entry = null;
		beforeFirst = false;
	}

	/**
	 * test if the cursor before first
	 */
	public boolean isBeforeFirst() throws IOException {
		if (entry == null) {
			return beforeFirst;
		}
		return false;
	}

	/**
	 * test if the cursor after the last
	 */
	public boolean isAfterLast() throws IOException {
		if (entry == null) {
			return !beforeFirst;
		}
		return false;
	}

	/**
	 * move the cursor to the first.
	 */
	public boolean first() throws IOException {
		LeafEntry<K, V> tgtEntry = btree.getFirstEntry();
		if (tgtEntry != null) {
			btree.lockEntry(tgtEntry);
			if (entry != null) {
				btree.unlockEntry(entry);
			}
			entry = tgtEntry;
			return true;
		}
		// no first entry means the tree is empty, move to the before first
		entry = null;
		beforeFirst = true;
		return false;
	}

	/**
	 * move the cursor to the last
	 */
	public boolean last() throws IOException {
		LeafEntry<K, V> tgtEntry = btree.getLastEntry();
		if (tgtEntry != null) {
			btree.lockEntry(tgtEntry);
			if (entry != null) {
				btree.unlockEntry(entry);
			} else {
				beforeFirst = false;
			}
			entry = tgtEntry;
			return true;
		}
		// no last entry means the tree is empty, move to the after last
		entry = null;
		beforeFirst = false;
		return false;
	}

	/**
	 * move to the first entry which value equals to the key. If there is no equals
	 * keys, return the position which just before the insert key. It may move the
	 * cursor to before the first.
	 * 
	 * @param key key value
	 * @return true if the current key equals to the key.
	 * @throws IOException
	 */
	public boolean moveTo(K key) throws IOException {
		LeafEntry<K, V> tgtEntry = btree.findEntry(key);
		if (tgtEntry != null) {
			btree.lockEntry(tgtEntry);
			if (entry != null) {
				btree.unlockEntry(entry);
			}
			entry = tgtEntry;
			K tgtKey = btree.getKey(tgtEntry.getKey());
			if (key.equals(tgtKey)) {
				return true;
			}
			return false;
		}
		if (entry != null) {
			btree.unlockEntry(entry);
			entry = null;
		}
		beforeFirst = true;
		return false;
	}

	private LeafEntry<K, V> getPrevEntry(LeafEntry<K, V> entry) throws IOException {
		LeafEntry<K, V> prevEntry = entry.getPrev();
		if (prevEntry != null) {
			return prevEntry;
		}
		int prevNodeId = entry.getNode().getPrevNodeId();
		if (prevNodeId != -1) {
			LeafNode<K, V> prevNode = (LeafNode<K, V>) btree.loadBTreeNode(prevNodeId);
			try {
				return prevNode.getLastEntry();
			} finally {
				prevNode.unlock();
			}
		}
		return null;
	}

	private LeafEntry<K, V> getNextEntry(LeafEntry<K, V> entry) throws IOException {
		LeafEntry<K, V> nextEntry = entry.getNext();
		if (nextEntry != null) {
			return nextEntry;
		}
		int nextNodeId = entry.getNode().getNextNodeId();
		if (nextNodeId != -1) {
			LeafNode<K, V> nextNode = (LeafNode<K, V>) btree.loadBTreeNode(nextNodeId);
			try {
				return nextNode.getFirstEntry();
			} finally {
				nextNode.unlock();
			}
		}
		return null;
	}

	public boolean previous() throws IOException {
		if (entry == null) {
			// we already after last, so return the last
			if (!beforeFirst) {
				if (last()) {
					return true;
				}
				beforeFirst = true;
			}
			// we are before the first, return false
			return false;
		}
		LeafEntry<K, V> tgtEntry = getPrevEntry(entry);
		if (tgtEntry != null) {
			btree.lockEntry(tgtEntry);
			btree.unlockEntry(entry);
			entry = tgtEntry;
			return true;
		}
		// there is no more previous
		btree.unlockEntry(entry);
		entry = null;
		beforeFirst = true;
		return false;
	}

	public boolean next() throws IOException {
		if (entry == null) {
			// test if we are already before first, return first
			if (beforeFirst) {
				if (first()) {
					return true;
				}
				beforeFirst = false;
			}
			// after last, return false
			return false;
		}

		LeafEntry<K, V> tgtEntry = getNextEntry(entry);
		if (tgtEntry != null) {
			btree.lockEntry(tgtEntry);
			btree.unlockEntry(entry);
			entry = tgtEntry;
			return true;
		}
		// there is no more next, we are after the last
		btree.unlockEntry(entry);
		entry = null;
		beforeFirst = false;
		return false;
	}

	public K getKey() throws IOException {
		if (entry == null) {
			throw new IOException(CoreMessages.getString(ResourceConstants.INVALID_CURSOR));
		}
		return btree.getKey(entry.getKey());
	}

	public V getValue() throws IOException {
		if (entry == null) {
			throw new IOException(CoreMessages.getString(ResourceConstants.INVALID_CURSOR));
		}
		BTreeValues<V> values = entry.getValues();
		BTreeValues.Value<V> value = values.getFirstValue();
		BTreeValue<V> v = value.getValue();
		return btree.getValue(v);
	}

	public Collection<V> getValues() throws IOException {
		if (entry == null) {
			throw new IOException(CoreMessages.getString(ResourceConstants.INVALID_CURSOR));
		}
		BTreeValues<V> values = entry.getValues();
		ArrayList<V> list = new ArrayList<V>(values.getValueCount());
		BTreeValues.Value<V> value = values.getFirstValue();
		while (value != null) {
			BTreeValue<V> bv = value.getValue();
			V v = btree.getValue(bv);
			list.add(v);
			value = value.getNext();
		}
		return list;
	}

	/**
	 * insert the key/value pair to the btree, move the current position to the
	 * insert point
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void insert(K key, V value) throws IOException {
		LeafEntry<K, V> tgtEntry = btree.insertEntry(key, value);
		btree.lockEntry(tgtEntry);
		if (entry != null) {
			btree.unlockEntry(entry);
		}
		entry = tgtEntry;
	}

	/**
	 * insert the key/values pair to the btree, move the current position to the
	 * insert point
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void insert(K key, V[] values) throws IOException {
		LeafEntry<K, V> tgtEntry = btree.insertEntry(key, values);
		btree.lockEntry(tgtEntry);
		if (entry != null) {
			btree.unlockEntry(entry);
		}
		entry = tgtEntry;
	}

	/**
	 * remove the current entry. the cursor is moved to the next entry.
	 * 
	 * @throws IOException
	 */
	public void remove() throws IOException {
		throw new UnsupportedOperationException("remove");
	}

	/**
	 * change the current entry's value.
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void setValue(V value) throws IOException {
		throw new UnsupportedOperationException("setValue(V)");
	}

	/**
	 * release the cursor.
	 */
	public void close() {
		if (entry != null) {
			btree.unlockEntry(entry);
			entry = null;
		}
	}
}
