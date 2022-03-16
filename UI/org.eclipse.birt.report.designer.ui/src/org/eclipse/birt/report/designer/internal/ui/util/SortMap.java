/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.designer.internal.ui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SortMap implements Map {

	private static class Entry implements Map.Entry {
		Object key;
		Object value;

		@Override
		public Object getKey() {
			return key;
		}

		@Override
		public Object getValue() {
			return value;
		}

		@Override
		public Object setValue(Object newValue) {
			Object oldValue = value;
			value = newValue;
			return oldValue;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			Map.Entry e = (Map.Entry) o;
			Object k1 = getKey();
			Object k2 = e.getKey();
			if (k1 == k2 || (k1 != null && k1.equals(k2))) {
				Object v1 = getValue();
				Object v2 = e.getValue();
				if (v1 == v2 || (v1 != null && v1.equals(v2))) {
					return true;
				}
			}
			return false;
		}

		@Override
		public int hashCode() {
			return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
		}

		@Override
		public String toString() {
			return getKey() + "=" + getValue();
		}
	}

	private List keyList = new ArrayList();
	private List entryList = new ArrayList();

	@Override
	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}
		for (int i = 0; i < keyList.size(); i++) {
			if (keyList.get(i).equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		for (int i = 0; i < entryList.size(); i++) {
			if ((((Entry) entryList.get(i)).value).equals(value)) {
				return true;
			}
		}
		return false;
	}

	public int getIndexOf(Object key) {
		if (key == null) {
			return -1;
		}
		for (int i = 0; i < keyList.size(); i++) {
			if (keyList.get(i).equals(key)) {
				return i;
			}
		}
		return -1;
	}

	public Object putAt(Object key, Object value, int index) {
		if (key == null || value == null || index < 0 || index > keyList.size() + 1) {
			return null;
		}

		Object result = null;
		if (containsKey(key)) {
			result = get(key);
			remove(key);
		}

		Entry entry = new Entry();
		entry.key = key;
		entry.value = value;
		entryList.add(entry);
		keyList.add(index, key);

		return result;
	}

	@Override
	public Object put(Object key, Object value) {
		if (key == null || value == null) {
			return null;
		}
		if (containsKey(key)) {
			Object result = get(key);
			for (int i = 0; i < entryList.size(); i++) {
				if (((Entry) entryList.get(i)).key.equals(key)) {
					((Entry) entryList.get(i)).value = value;
					break;
				}
			}
			return result;
		} else {
			Entry entry = new Entry();
			entry.key = key;
			entry.value = value;
			entryList.add(entry);
			keyList.add(key);
			return null;
		}
	}

	@Override
	public Object remove(Object key) {
		if ((key == null) || !containsKey(key)) {
			return null;
		}

		Object result = null;
		for (int i = 0; i < entryList.size(); i++) {
			if (((Entry) entryList.get(i)).key.equals(key)) {
				result = entryList.get(i);
				entryList.remove(i);
				break;
			}
		}
		keyList.remove(key);
		return result;
	}

	public void remove(int index) {
		if (index < 0 || index >= keyList.size()) {
			return;
		}
		Object key = keyList.get(index);
		for (int i = 0; i < entryList.size(); i++) {
			if (((Entry) entryList.get(i)).key.equals(key)) {
				entryList.remove(i);
				break;
			}
		}
		keyList.remove(key);

	}

	public List getKeyList() {
		return keyList;
	}

	public List getValueList() {
		List valueList = new LinkedList();
		for (int i = 0; i < keyList.size(); i++) {
			valueList.add(get(keyList.get(i)));
		}
		return valueList;
	}

	@Override
	public Object get(Object key) {
		if ((key == null) || !containsKey(key)) {
			return null;
		}
		for (int i = 0; i < entryList.size(); i++) {
			if (((Entry) entryList.get(i)).key.equals(key)) {
				return ((Entry) entryList.get(i)).value;
			}
		}
		return null;
	}

	public Object get(int index) {
		if (index < 0 || index >= keyList.size()) {
			return null;
		}
		Object key = keyList.get(index);
		for (int i = 0; i < entryList.size(); i++) {
			if (((Entry) entryList.get(i)).key.equals(key)) {
				return ((Entry) entryList.get(i)).value;
			}
		}
		return null;
	}

	@Override
	public void clear() {
		keyList.clear();
		entryList.clear();
	}

	@Override
	public int size() {
		return keyList.size();
	}

	@Override
	public Set entrySet() {
		LinkedHashSet set = new LinkedHashSet(entryList);
		return set;
	}

	@Override
	public boolean isEmpty() {
		return keyList.isEmpty();
	}

	@Override
	public Set keySet() {
		LinkedHashSet set = new LinkedHashSet(keyList);
		return set;
	}

	@Override
	public void putAll(Map map) {
		Object[] keys = map.keySet().toArray();
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				put(keys[i], map.get(keys[i]));
			}
		}
	}

	@Override
	public Collection values() {
		return getValueList();
	}

}
