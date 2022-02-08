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
