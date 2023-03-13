/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.presentation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.InstanceID;

public class PageSection {
	public static final int TYPE_AUTO_LAYOUT_PAGE_SECTION = 0;
	public static final int TYPE_FIXED_LAYOUT_PAGE_SECTION = 1;

	public long startOffset;
	public long endOffset;

	public InstanceIndex[] starts;
	public InstanceIndex[] ends;

	public void write(DataOutputStream out) throws IOException {
		IOUtil.writeInt(out, TYPE_AUTO_LAYOUT_PAGE_SECTION);
		writeInstanceIndex(out, starts);
		writeInstanceIndex(out, ends);
	}

	public void read(DataInputStream in) throws IOException {
		starts = readInstanceIndex(in);
		ends = readInstanceIndex(in);
		startOffset = starts[starts.length - 1].getOffset();
		endOffset = ends[ends.length - 1].getOffset();
	}

	protected void writeInstanceIndex(DataOutputStream out, InstanceIndex[] indexes) throws IOException {
		if (indexes == null) {
			IOUtil.writeInt(out, 0);
			return;
		}
		IOUtil.writeInt(out, indexes.length);
		for (int i = 0; i < indexes.length; i++) {
			IOUtil.writeString(out, indexes[i].getInstanceID().toUniqueString());
			IOUtil.writeLong(out, indexes[i].getOffset());
		}
	}

	protected InstanceIndex[] readInstanceIndex(DataInputStream in) throws IOException {
		int length = IOUtil.readInt(in);
		InstanceIndex[] indexes = new InstanceIndex[length];
		for (int i = 0; i < length; i++) {
			String id = IOUtil.readString(in);
			long offset = IOUtil.readLong(in);
			indexes[i] = new InstanceIndex(InstanceID.parse(id), offset);
		}
		return indexes;
	}

}
