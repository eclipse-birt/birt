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

public class InlineValueList<K, V> implements BTreeValues<V> {

	private BTree<K, V> btree;
	private int valueCount;
	private InlineValue firstValue;
	private InlineValue lastValue;
	private int valueSize;

	InlineValueList(BTree<K, V> btree) {
		this.btree = btree;
		this.valueSize = 8;
		this.valueCount = 0;
		this.firstValue = null;
		this.lastValue = null;
	}

	@Override
	public int getType() {
		return INLINE_VALUES;
	}

	@Override
	public Value<V> append(BTreeValue<V> v) throws IOException {
		InlineValue value = new InlineValue(v);
		if (lastValue == null) {
			value.setPrev(null);
			value.setNext(null);
			firstValue = value;
			lastValue = value;
		} else {
			value.setPrev(lastValue);
			value.setNext(null);
			lastValue.setNext(value);
			lastValue = value;
		}
		valueCount++;
		valueSize += btree.getValueSize(v);
		return value;
	}

	@Override
	public void read(DataInput in) throws IOException {
		valueSize = in.readInt();
		valueCount = in.readInt();
		for (int i = 0; i < valueCount; i++) {
			BTreeValue<V> v = btree.readValue(in);
			InlineValue value = new InlineValue(v);
			if (lastValue == null) {
				firstValue = value;
				lastValue = value;
			} else {
				lastValue.setNext(value);
				value.setPrev(lastValue);
				lastValue = value;
			}
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(valueSize);
		out.writeInt(valueCount);
		Value<V> value = firstValue;
		while (value != null) {
			btree.writeValue(out, value.getValue());
			value = value.getNext();
		}
	}

	@Override
	public Value<V> getFirstValue() {
		return firstValue;
	}

	@Override
	public Value<V> getLastValue() {
		return lastValue;
	}

	@Override
	public int getValueCount() {
		return valueCount;
	}

	@Override
	public int getValueSize() {
		return valueSize;
	}

	private class InlineValue implements Value<V> {

		InlineValue next;
		InlineValue prev;
		BTreeValue<V> value;

		InlineValue(BTreeValue<V> value) {
			this.value = value;
		}

		@Override
		public Value<V> getNext() throws IOException {
			return next;
		}

		@Override
		public Value<V> getPrev() throws IOException {
			return prev;
		}

		@Override
		public BTreeValue<V> getValue() throws IOException {
			return value;
		}

		public void setNext(InlineValue next) {
			this.next = next;
		}

		public void setPrev(InlineValue prev) {
			this.prev = prev;
		}

	}
}
