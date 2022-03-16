/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl.index;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;

public class DataSetInMemoryStringIndex extends HashMap implements IDataSetIndex {

	private static final long serialVersionUID = 1L;

	public DataSetInMemoryStringIndex(RAInputStream indexStream, RAInputStream valueStream) throws IOException {
		super();
		DataInputStream dis = new DataInputStream(indexStream);
		int size = IOUtil.readInt(indexStream);
		for (int i = 0; i < size; i++) {
			long offset = IOUtil.readLong(dis);
			if (SerializableBirtHash.NULL_VALUE_OFFSET == offset) {
				super.put(null, new WrapperedValue(null, IOUtil.readIntList(dis)));
			} else if (SerializableBirtHash.NOT_HASH_VALUE_OFFSET == offset) {
				String keyValue = IOUtil.readString(dis);
				super.put(keyValue, new WrapperedValue(keyValue, IOUtil.readIntList(dis)));
			} else {
				Integer keyValue = IOUtil.readInt(dis);
				super.put(keyValue, new WrapperedValue(valueStream, IOUtil.readIntList(dis), offset));
			}
		}
	}

	@Override
	public IOrderedIntSet getKeyIndex(Object key, int searchType) throws DataException {
		ArrayList fastSet = new ArrayList();
		for (int i : this.getKeyIndex1(key, searchType)) {
			fastSet.add(i);
		}
		Collections.sort(fastSet);

		return new OrderedIntSet(fastSet);
	}

	public Set<Integer> getKeyIndex1(Object key, int searchType) throws DataException {
		if (searchType != IConditionalExpression.OP_EQ && searchType != IConditionalExpression.OP_IN) {
			throw new UnsupportedOperationException();
		}
		if (searchType == IConditionalExpression.OP_EQ) {
			return getKeyIndex(key);
		} else {
			List candidate = (List) key;
			Set<Integer> result = new HashSet<>();
			for (Object eachKey : candidate) {
				result.addAll(getKeyIndex(eachKey));
			}
			return result;
		}
	}

	private Set<Integer> getKeyIndex(Object key) throws DataException {
		Object result = getWrappedKey(key);
		if (result == null) {
			return new HashSet();
		} else {
			return ((WrapperedValue) result).getIndex();
		}
	}

	public String getKeyValue(Object key) {
		try {
			Object result = getWrappedKey(key);
			if (result == null) {
				return null;
			} else {
				return ((WrapperedValue) result).getKeyValue();
			}
		} catch (DataException e) {
			return null;
		}
	}

	private Object getWrappedKey(Object key) throws DataException {
		Object result = null;
		if (key == null) {
			result = this.get(null);
		} else if (key instanceof String) {
			result = this.get(key);
			if (result == null) {
				result = this.get(key.hashCode());
				if (result instanceof WrapperedValue) {
					// Detect hash conflicting
					if (key.equals(((WrapperedValue) result).getKeyValue())) {
						return result;
					} else {
						result = null;
					}
				}
			}
		}
		if (result == null) {
			result = this.get(key);
		}
		return result;
	}

	private static class WrapperedValue {

		private long keyOffset;
		private RAInputStream keyStream;
		private Set index = new HashSet();
		private Object keyValue;

		WrapperedValue(RAInputStream keyStream, List index, long keyOffset) {
			this.keyOffset = keyOffset;
			this.keyStream = keyStream;
			this.index.addAll(index);
		}

		WrapperedValue(String keyValue, List index) {
			this.keyValue = keyValue;
			this.index.addAll(index);
		}

		public Set getIndex() {
			return this.index;
		}

		public String getKeyValue() throws DataException {
			try {
				if (keyValue != null) {
					if (keyValue instanceof String) {
						return (String) this.keyValue;
					}
					if (keyValue instanceof SoftReference) {
						String result = ((SoftReference<String>) keyValue).get();
						if (result != null) {
							return result;
						}
					}
				}
				if (keyStream == null) {
					return null;
				}
				synchronized (this.keyStream) {
					if (keyValue != null) {
						if (keyValue instanceof String) {
							return (String) this.keyValue;
						}
						if (keyValue instanceof SoftReference) {
							String result = ((SoftReference<String>) keyValue).get();
							if (result != null) {
								return result;
							}
						}
					}
					this.keyStream.seek(this.keyOffset);
					this.keyValue = new SoftReference<>(IOUtil.readString(new DataInputStream(this.keyStream)));
				}

				return ((SoftReference<String>) this.keyValue).get();
			} catch (IOException e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.impl.index.IDataSetIndex#supportFilter(int)
	 */
	@Override
	public boolean supportFilter(int filterType) throws DataException {
		if (filterType != IConditionalExpression.OP_EQ && filterType != IConditionalExpression.OP_IN) {
			return false;
		}
		return true;
	}

	@Override
	public Object[] getAllKeyValues() throws DataException {
		Object[] values = this.values().toArray();
		Object[] keys = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			keys[i] = ((WrapperedValue) values[i]).getKeyValue();
		}
		return keys;
	}

	@Override
	public IOrderedIntSet getAllKeyRows() throws DataException {
		List arrayList = new ArrayList();
		Object[] values = this.values().toArray();
		for (int i = 0; i < values.length; i++) {
			Iterator iterator = ((WrapperedValue) values[i]).getIndex().iterator();
			arrayList.add((Integer) iterator.next());
		}
		Collections.sort(arrayList);
		return new OrderedIntSet(arrayList);
	}

	private class OrderedIntSet implements IOrderedIntSet {
		private List values;

		public OrderedIntSet(List values) {
			this.values = values;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.impl.index.IOrderedIntSet#iterator()
		 */
		@Override
		public IOrderedIntSetIterator iterator() {
			return new IOrderedIntSetIterator() {

				int i = 0;

				@Override
				public boolean hasNext() {
					return values.size() <= i;
				}

				@Override
				public int next() {
					int result = (Integer) values.get(i);
					i++;
					return result;
				}

			};
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.impl.index.IOrderedIntSet#isEmpty()
		 */
		@Override
		public boolean isEmpty() {
			return this.values.isEmpty();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.impl.index.IOrderedIntSet#size()
		 */
		@Override
		public int size() {
			return this.values.size();
		}

	}
}
