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

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

class BTreeHead implements BTreeConstants {

	int version;
	boolean allowDuplicate;
	int keySize;
	boolean hasValues;
	int valueSize;
	int rootNodeId;
	int totalLevels;
	int totalKeys;
	int totalValues;

	BTreeHead() {
		version = BTREE_VERSION_0;
		totalLevels = 0;
		totalKeys = 0;
		totalValues = 0;
		allowDuplicate = false;
		keySize = 0;
		hasValues = true;
		valueSize = 0;
		rootNodeId = -1;
	}

	void read(DataInput in) throws IOException {
		long tag = in.readLong();
		if (tag != MAGIC_TAG) {
			throw new IOException(CoreMessages.getFormattedString(ResourceConstants.INVALID_MAGIC_TAG,
					new Object[] { Long.toHexString(tag) }));
		}
		version = in.readInt();
		if (version != BTREE_VERSION_0) {
			throw new IOException(
					CoreMessages.getFormattedString(ResourceConstants.UNSUPPORTED_VERSION, new Object[] { version }));
		}
		readV0(in);
	}

	void readV0(DataInput in) throws IOException {
		allowDuplicate = in.readBoolean();
		keySize = in.readShort();
		hasValues = in.readBoolean();
		valueSize = in.readShort();
		rootNodeId = in.readInt();
		totalLevels = in.readInt();
		totalKeys = in.readInt();
		totalValues = in.readInt();
	}

	void write(DataOutput out) throws IOException {
		out.writeLong(MAGIC_TAG);
		out.writeInt(BTREE_VERSION_0);
		out.writeBoolean(allowDuplicate);
		out.writeShort(keySize);
		out.writeBoolean(hasValues);
		out.writeShort(valueSize);
		out.writeInt(rootNodeId);
		out.writeInt(totalLevels);
		out.writeInt(totalKeys);
		out.writeInt(totalValues);
	}
}