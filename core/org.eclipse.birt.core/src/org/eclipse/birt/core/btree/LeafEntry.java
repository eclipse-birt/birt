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

public class LeafEntry<K, V> {

	private LeafNode<K, V> node;
	private BTreeValue<K> key;
	private BTreeValues<V> values;

	private LeafEntry<K, V> prev;
	private LeafEntry<K, V> next;

	public LeafEntry(LeafNode<K, V> node, BTreeValue<K> key, BTreeValues<V> values) {
		this.node = node;
		this.key = key;
		this.values = values;
	}

	public BTreeValue<K> getKey() {
		return key;
	}

	public BTreeValues<V> getValues() {
		return values;
	}

	public LeafEntry<K, V> getNext() {
		return next;
	}

	public void setPrev(LeafEntry<K, V> prev) {
		this.prev = prev;
	}

	public void setNext(LeafEntry<K, V> next) {
		this.next = next;
	}

	public LeafEntry<K, V> getPrev() {
		return prev;
	}

	public void setValues(BTreeValues<V> values) {
		this.values = values;
	}

	public LeafNode<K, V> getNode() {
		return node;
	}

	public void setNode(LeafNode<K, V> node) {
		this.node = node;
	}
}
