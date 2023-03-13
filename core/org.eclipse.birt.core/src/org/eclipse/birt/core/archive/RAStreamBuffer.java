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
package org.eclipse.birt.core.archive;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.eclipse.birt.core.util.IOUtil;

public class RAStreamBuffer {

	private RandomAccessFile randomFile;

	private byte buf[];

	// The offset in the underlying file of the first byte in the buffer.
	// It is always the multiple of IOUtil.RA_STREAM_BUFFER_LENGTH
	private long offset;

	// The number of bytes has been written in the buffer
	private int bufLen;

	// The position in the buffer where next write will occur.
	// It may be less than bufLen.
	private int bufCur;

	public RAStreamBuffer(RandomAccessFile randomFile) {
		this.randomFile = randomFile;
		this.buf = new byte[IOUtil.RA_STREAM_BUFFER_LENGTH];
	}

	/**
	 * Set the buffer to a different offset in the underlying file. Read the data
	 * already written into the file.
	 *
	 * @param offset
	 * @throws IOException
	 */
	public void resetBuffer(long offset) throws IOException {
		this.offset = offset;
		this.randomFile.seek(offset);
		this.bufLen = this.read(buf, 0, IOUtil.RA_STREAM_BUFFER_LENGTH);
		this.bufCur = 0;
	}

	public long getOffset() {
		return this.offset;
	}

	public void setBufCur(int bufCur) {
		this.bufCur = bufCur;
	}

	public int getBufCur() {
		return this.bufCur;
	}

	/**
	 * Write the data in the buffer to the underlying file
	 *
	 * @throws IOException
	 */
	public void flushBuffer() throws IOException {
		if (bufLen > 0) {
			randomFile.seek(offset);
			randomFile.write(buf, 0, bufLen);
			bufLen = 0;
		}
	}

	/**
	 * Write the data in the b[] to the interal buffer Can only write to the end of
	 * the interal buffer
	 *
	 * @param b
	 * @param off
	 * @param len
	 * @return number of bytes has been written to the interal buffer
	 */
	public int write(byte b[], int off, int len) {
		int availableSize = IOUtil.RA_STREAM_BUFFER_LENGTH - bufCur;
		if (len > availableSize) {
			len = availableSize;
		}
		System.arraycopy(b, off, buf, bufCur, len);
		bufCur += len;
		if (bufLen < bufCur) {
			bufLen = bufCur;
		}
		return len;
	}

	/**
	 * Reload the data from underlying file if there are any data from the offset of
	 * this buffer.
	 *
	 * @param b
	 * @param off
	 * @param len
	 * @return
	 * @throws IOException
	 */
	private int read(byte b[], int off, int len) throws IOException {
		int n = 0;
		do {
			int count = randomFile.read(b, off + n, len - n);
			if (count < 0) {
				return n;
			}
			n += count;
		} while (n < len);
		return n;
	}

}
