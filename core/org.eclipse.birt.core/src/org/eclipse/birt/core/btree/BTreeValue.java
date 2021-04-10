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

class BTreeValue<V> {

	private byte[] bytes;
	private V value;

	BTreeValue() {
	}

	BTreeValue(byte[] bytes) {
		this(null, bytes);
	}

	BTreeValue(V value, byte[] bytes) {
		this.bytes = bytes;
		this.value = value;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public String toString() {
		if (value != null) {
			return value.toString();
		}
		return null;
	}
}
