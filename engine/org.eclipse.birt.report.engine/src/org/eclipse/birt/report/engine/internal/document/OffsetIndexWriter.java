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

package org.eclipse.birt.report.engine.internal.document;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class OffsetIndexWriter {

	protected String indexFile;
	protected RandomAccessFile index;
	protected long maxOffset;

	public OffsetIndexWriter(String indexFile) {
		this.indexFile = indexFile;
	}

	public void open() throws IOException {
		File file = new File(indexFile);
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		index = new RandomAccessFile(file, "rw");
		maxOffset = -1;
	}

	public void close() {
		if (index != null) {
			try {
				index.close();
			} catch (Exception ex) {
			}
		}
	}

	public void write(long offset, long value) throws IOException {
		assert (offset > maxOffset);
		index.writeLong(offset);
		index.writeLong(value);
		maxOffset = offset;
	}
}
