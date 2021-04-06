/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.btree;

import java.util.Comparator;

public class BTreeOption<K, V> {

	boolean readOnly;
	int keySize;
	int valueSize;
	boolean hasValue;
	boolean allowNullKey;
	boolean allowDuplicate;
	Comparator<K> comparator;
	BTreeSerializer<K> keySerializer;
	BTreeSerializer<V> valueSerializer;
	BTreeFile file;
	boolean shareFile;

	int headNodeId;
	int cacheSize;

	public BTreeOption() {
		readOnly = false;
		keySize = 0;
		valueSize = 0;
		hasValue = true;
		allowNullKey = false;
		allowDuplicate = false;

		comparator = new JavaComparator<K>();
		keySerializer = new JavaSerializer<K>();
		valueSerializer = new JavaSerializer<V>();

		headNodeId = BTreeConstants.HEAD_BLOCK_ID;
		cacheSize = 13;

		file = null;
	}

	public void setAllowDuplicate(boolean allowDuplicate) {
		this.allowDuplicate = allowDuplicate;
	}

	public void setAllowNullKey(boolean allowNullKey) {
		this.allowNullKey = allowNullKey;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void setHasValue(boolean hasValue) {
		this.hasValue = hasValue;
	}

	public boolean hasValue() {
		return hasValue;
	}

	public void setComparator(Comparator<K> comparator) {
		this.comparator = comparator;
	}

	public void setKeySerializer(BTreeSerializer<K> serializer) {
		this.keySerializer = serializer;
	}

	public void setValueSerializer(BTreeSerializer<V> serializer) {
		this.valueSerializer = serializer;
	}

	public void setFile(BTreeFile file) {
		this.file = file;
	}

	public void setFile(BTreeFile file, boolean shareFile) {
		this.file = file;
		this.shareFile = shareFile;
	}

	public void setKeySize(int size) {
		keySize = size;
	}

	public int getKeySize() {
		return keySize;
	}

	public void setValueSize(int size) {
		valueSize = size;
	}

	public int getValueSize() {
		return valueSize;
	}

	public void setHeadNodeId(int headNodeId) {
		this.headNodeId = headNodeId;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}
}
