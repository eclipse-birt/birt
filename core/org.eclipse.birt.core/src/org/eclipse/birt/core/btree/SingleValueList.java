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

public class SingleValueList<K, V> implements BTreeValues<V> {

	private BTree<K, V> btree;
	private SingleValue value;

	SingleValueList(BTree<K, V> btree) {
		this.btree = btree;
	}

	SingleValueList(BTree<K, V> btree, BTreeValue<V> v) {
		this.btree = btree;
		this.value = new SingleValue(v);
	}

	public int getType() {
		return SINGLE_VALUES;
	}

	public void read(DataInput in) throws IOException {
		BTreeValue<V> v = btree.readValue(in);
		value = new SingleValue(v);
	}

	public void write(DataOutput out) throws IOException {
		btree.writeValue(out, value.getValue());
	}

	public int getValueCount() {
		return 1;
	}

	public int getValueSize() {
		return btree.getValueSize(value.getValue());
	}

	public Value<V> getFirstValue() {
		return value;
	}

	public Value<V> getLastValue() {
		return value;
	}

	public Value<V> append(BTreeValue<V> value) {
		throw new java.lang.UnsupportedOperationException("append");
	}

	private class SingleValue implements Value<V> {

		BTreeValue<V> value;

		SingleValue(BTreeValue<V> value) {
			this.value = value;
		}

		public Value<V> getNext() throws IOException {
			return null;
		}

		public Value<V> getPrev() throws IOException {
			return null;
		}

		public BTreeValue<V> getValue() {
			return value;
		}

	}
}
