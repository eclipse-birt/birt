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

import java.io.IOException;

import org.eclipse.birt.core.archive.RAOutputStream;

public class ByteArrayRAOutputStream extends RAOutputStream {

	byte[] buffer;
	int length;
	int offset;

	public ByteArrayRAOutputStream() {
		this(1024);
	}

	public ByteArrayRAOutputStream(int size) {
		this.buffer = new byte[size];
		this.length = 0;
		this.offset = 0;
	}

	public long getOffset() throws IOException {
		return offset;
	}

	public long length() throws IOException {
		return length;
	}

	private byte writeBuffer[] = new byte[8];

	public void writeInt(int v) throws IOException {
		writeBuffer[0] = (byte) (v >>> 24);
		writeBuffer[1] = (byte) (v >>> 16);
		writeBuffer[2] = (byte) (v >>> 8);
		writeBuffer[3] = (byte) (v >>> 0);
		write(writeBuffer, 0, 4);
	}

	public void writeLong(long v) throws IOException {
		writeBuffer[0] = (byte) (v >>> 56);
		writeBuffer[1] = (byte) (v >>> 48);
		writeBuffer[2] = (byte) (v >>> 40);
		writeBuffer[3] = (byte) (v >>> 32);
		writeBuffer[4] = (byte) (v >>> 24);
		writeBuffer[5] = (byte) (v >>> 16);
		writeBuffer[6] = (byte) (v >>> 8);
		writeBuffer[7] = (byte) (v >>> 0);
		write(writeBuffer, 0, 8);
	}

	@Override
	public void seek(long localPos) throws IOException {
		offset = (int) localPos;
		if (offset > buffer.length) {
			increase_buffer(offset);
		}
		if (offset > length) {
			length = offset;
		}
	}

	public void write(int b) throws IOException {
		increase_buffer(offset + 1);
		buffer[offset] = (byte) (b & 0xFF);
		offset += 1;
		if (length < offset) {
			length = offset;
		}
	}

	public void write(byte[] buff, int off, int len) throws IOException {
		increase_buffer(offset + len);
		System.arraycopy(buff, off, buffer, offset, len);
		offset += len;
		if (length < offset) {
			length = offset;
		}
	}

	private void increase_buffer(int size) {
		if (size > buffer.length) {
			int new_size = (size / 1024 + 1) * 1024;
			byte[] new_buffer = new byte[new_size];
			System.arraycopy(buffer, 0, new_buffer, 0, length);
			buffer = new_buffer;
		}
	}

	public byte[] toByteArray() {
		byte[] buf = new byte[length];
		System.arraycopy(buffer, 0, buf, 0, length);
		return buf;
	}
}
