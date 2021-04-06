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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface BTreeValues<V> {

	static final int INLINE_VALUES = 0;
	static final int EXTERNAL_VALUES = 1;
	static final int SINGLE_VALUES = 2;

	int getType();

	int getValueCount();

	int getValueSize();

	Value<V> getFirstValue() throws IOException;

	Value<V> getLastValue() throws IOException;

	Value<V> append(BTreeValue<V> value) throws IOException;

	void read(DataInput in) throws IOException;

	void write(DataOutput out) throws IOException;

	interface Value<V> {

		BTreeValue<V> getValue() throws IOException;

		Value<V> getNext() throws IOException;

		Value<V> getPrev() throws IOException;
	}
}
