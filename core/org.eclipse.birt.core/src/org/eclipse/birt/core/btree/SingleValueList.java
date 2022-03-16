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

	@Override
	public int getType() {
		return SINGLE_VALUES;
	}

	@Override
	public void read(DataInput in) throws IOException {
		BTreeValue<V> v = btree.readValue(in);
		value = new SingleValue(v);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		btree.writeValue(out, value.getValue());
	}

	@Override
	public int getValueCount() {
		return 1;
	}

	@Override
	public int getValueSize() {
		return btree.getValueSize(value.getValue());
	}

	@Override
	public Value<V> getFirstValue() {
		return value;
	}

	@Override
	public Value<V> getLastValue() {
		return value;
	}

	@Override
	public Value<V> append(BTreeValue<V> value) {
		throw new java.lang.UnsupportedOperationException("append");
	}

	private class SingleValue implements Value<V> {

		BTreeValue<V> value;

		SingleValue(BTreeValue<V> value) {
			this.value = value;
		}

		@Override
		public Value<V> getNext() throws IOException {
			return null;
		}

		@Override
		public Value<V> getPrev() throws IOException {
			return null;
		}

		@Override
		public BTreeValue<V> getValue() {
			return value;
		}

	}
}
