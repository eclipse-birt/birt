/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound.v3;

import java.io.IOException;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class DataBlock extends Ext2Block {

	final private byte[] buffer;
	private int dirtyStart;
	private int dirtyEnd;

	DataBlock(Ext2FileSystem fs) {
		this(fs, -1);
	}

	DataBlock(Ext2FileSystem fs, int blockId) {
		super(fs, blockId);
		this.buffer = new byte[BLOCK_SIZE];
		this.dirtyStart = -1;
		this.dirtyEnd = -1;
	}

	public synchronized int write(int tgt, byte b[], int off, int len) throws IOException {
		assert b != null;
		assert tgt + len <= buffer.length;
		assert off + len <= b.length;

		if (len > 0) {
			System.arraycopy(b, off, buffer, tgt, len);
			if (dirtyStart == -1) {
				dirtyStart = tgt;
				dirtyEnd = tgt + len;
			} else {
				if (dirtyStart > tgt) {
					dirtyStart = tgt;
				}
				if (dirtyEnd < tgt + len) {
					dirtyEnd = tgt + len;
				}
			}
		}
		return len;
	}

	public synchronized int read(int src, byte b[], int off, int len) throws IOException {
		assert b != null;
		assert off + len <= b.length;
		assert src + len <= buffer.length;
		System.arraycopy(buffer, src, b, off, len);
		return len;
	}

	@Override
	public void refresh() throws IOException {
		assert blockId != -1;
		fs.readBlock(blockId, buffer, 0, BLOCK_SIZE);
		dirtyStart = dirtyEnd = -1;
	}

	@Override
	public void flush() throws IOException {
		if (blockId == -1) {
			throw new IllegalStateException("Must assign the block id before flush");
		}
		if (dirtyStart != dirtyEnd) {
			fs.writeBlock(blockId, buffer, dirtyStart, dirtyEnd - dirtyStart);
			dirtyStart = dirtyEnd = -1;
		}
	}

	static final DataBlock READ_ONLY_BLOCK = new ReadOnlyBlock();

	private static class ReadOnlyBlock extends DataBlock {

		ReadOnlyBlock() {
			super(null, -1);
		}

		@Override
		public int write(int tgt, byte b[], int off, int len) throws IOException {
			throw new IOException(CoreMessages.getString(ResourceConstants.CANNOT_CHANGE_READONLY_BUFFER));
		}

		@Override
		public int read(int src, byte b[], int off, int len) throws IOException {
			assert b != null;
			assert off + len <= b.length;
			for (int i = 0; i < len; i++) {
				b[off + i] = 0;
			}
			return len;
		}

		@Override
		public void refresh() throws IOException {
		}

		@Override
		public void flush() throws IOException {
		}
	}
}
