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

public class IndexEntry<K, V> {

	protected IndexNode<K, V> node;
	protected BTreeValue<K> key;
	protected int childNodeId;

	IndexEntry(IndexNode<K, V> node, BTreeValue<K> key, int childNodeId) {
		this.node = node;
		this.key = key;
		this.childNodeId = childNodeId;
	}

	BTreeValue<K> getKey() {
		return key;
	}

	public int getChildNodeId() {
		return childNodeId;
	}

	public void setNode(IndexNode<K, V> node) {
		this.node = node;
	}

	public IndexNode<K, V> getNode() {
		return node;
	}
}
