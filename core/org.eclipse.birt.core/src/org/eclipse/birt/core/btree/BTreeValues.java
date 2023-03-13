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

public interface BTreeValues<V> {

	int INLINE_VALUES = 0;
	int EXTERNAL_VALUES = 1;
	int SINGLE_VALUES = 2;

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
