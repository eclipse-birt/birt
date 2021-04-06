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

public class ValueEntry<V> {

	private BTreeValue<V> value;
	private ValueEntry<V> prev;
	private ValueEntry<V> next;

	ValueEntry(BTreeValue<V> value) {
		this.value = value;
	}

	BTreeValue<V> getValue() {
		return value;
	}

	ValueEntry<V> getNext() {
		return next;
	}

	ValueEntry<V> getPrev() {
		return prev;
	}

	public void setValue(BTreeValue<V> value) {
		this.value = value;
	}

	public void setPrev(ValueEntry<V> prev) {
		this.prev = prev;
	}

	public void setNext(ValueEntry<V> next) {
		this.next = next;
	}

}
