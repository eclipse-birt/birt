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

package org.eclipse.birt.report.engine.toc;

import java.io.EOFException;
import java.io.IOException;

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.RAInputStream;

public class ByteArrayRAInputStream extends RAInputStream {

	byte[] buffer;
	int offset;

	public ByteArrayRAInputStream(byte[] buffer) {
		this.buffer = buffer;
	}

	public int available() throws IOException {
		return buffer.length - offset;
	}

	@Override
	public long getOffset() throws IOException {
		return offset;
	}

	public long length() throws IOException {
		return buffer.length;
	}

	public void readFully(byte[] b, int off, int len) throws IOException {
		if (len + offset > buffer.length) {
			throw new EOFException();
		}
		System.arraycopy(buffer, offset, b, off, len);
		offset += len;
	}

	public int read(byte[] b, int off, int len) throws IOException {
		if (offset > buffer.length) {
			return -1;
		}

		if (len + offset > buffer.length) {
			len = buffer.length - offset;
		}
		System.arraycopy(buffer, offset, b, off, len);
		offset += len;
		return len;
	}

	@Override
	public int readInt() throws IOException {
		if (offset + 4 > buffer.length) {
			throw new EOFException();
		}
		int v = ArchiveUtil.bytesToInteger(buffer, offset);
		offset += 4;
		return v;
	}

	@Override
	public long readLong() throws IOException {
		if (offset + 8 > buffer.length) {
			throw new EOFException();
		}
		long v = ArchiveUtil.bytesToLong(buffer, offset);
		offset += 8;
		return v;
	}

	@Override
	public void refresh() throws IOException {
	}

	@Override
	public void seek(long localPos) throws IOException {
		if (localPos > buffer.length) {
			throw new EOFException();
		}
		offset = (int) localPos;
	}

	public int read() throws IOException {
		if (offset < buffer.length) {
			return buffer[offset++] & 0xFF;
		}
		return -1;
	}
}
