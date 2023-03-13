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

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.util.IOUtil;

public class RAFileInputStream extends RAInputStream {
	private RandomAccessFile parent;
	private long startPos; // in parentFile, the position of the first character
	private long endPos; // in parentFile, the position of EOF mark (not a valid character in the file)
	private long cur; // in current stream, the virtual file pointer in local file

	private byte buf[];
	private int bufLen; // total bytes in the buffer
	private int bufCur; // the pointer in the buffer

	/**
	 * @param parentFile - underlying RandomAccess file
	 * @param startPos   - the (global) position of the first character in
	 *                   parentFile
	 * @param endPos     - the (global) position of EOF mark (not a valid character
	 *                   in the file)
	 */
	public RAFileInputStream(RandomAccessFile parentFile, long startPos, long endPos) {
		this.parent = parentFile;
		this.startPos = startPos;
		this.endPos = endPos;
		this.buf = new byte[IOUtil.RA_STREAM_BUFFER_LENGTH];
		this.cur = 0;
		this.bufLen = 0;
		this.bufCur = 0;
	}

	@Override
	public void refresh() throws IOException {
		bufLen = 0;
		bufCur = 0;
	}

	private void readToBuffer() throws IOException {
		bufLen = 0;
		bufCur = 0;

		long parentPos = startPos + cur;
		long availableSize = endPos - parentPos;
		if (availableSize <= 0) {
			return;
		}
		int len = buf.length;
		if (len > availableSize) {
			len = (int) availableSize;
		}
		synchronized (parent) {
			parent.seek(parentPos);
			bufLen = parent.read(buf, 0, len);
		}
		cur += bufLen;
	}

	/**
	 * The same behavior as InputStream.read().<br>
	 * Reads the next byte of data from the input stream. The value byte is returned
	 * as an <code>int</code> in the range <code>0</code> to <code>255</code>. If no
	 * byte is available because the end of the stream has been reached, the value
	 * <code>-1</code> is returned. This method blocks until input data is
	 * available, the end of the stream is detected, or an exception is thrown.
	 *
	 * <p>
	 * A subclass must provide an implementation of this method.
	 *
	 * @return the next byte of data, or <code>-1</code> if the end of the stream is
	 *         reached.
	 * @exception IOException if an I/O error occurs.
	 */
	@Override
	public int read() throws IOException {
		if (bufLen <= 0 || bufLen == bufCur) {
			readToBuffer();
		}
		if (bufLen <= 0) {
			return -1;
		}
		return buf[bufCur++] & 0xff;
	}

	/**
	 * The same behavior as InputStream.read(byte b[], int off, int len ).<br>
	 * Reads up to len bytes of data from the input stream into an array of bytes.
	 * If no byte is available because the end of the stream has been reached, the
	 * value <code>-1</code> is returned. This method blocks until input data is
	 * available, the end of the stream is detected, or an exception is thrown.
	 *
	 * <p>
	 * A subclass must provide an implementation of this method.
	 *
	 * @return the total number of bytes read into the buffer, or <code>-1</code> if
	 *         there is no more data because the end of the stream has been reached
	 * @exception IOException if an I/O error occurs.
	 */
	@Override
	public int read(byte b[], int off, int len) throws IOException {
		int n = 0;
		do {
			int count = this.read1(b, off + n, len - n);
			if (count < 0) {
				break;
			}
			n += count;
		} while (n < len);
		return (n > 0) ? n : -1;
	}

	/**
	 * Read the data in the buffer up to len to an array of bytes.
	 *
	 * @param b
	 * @param off
	 * @param len
	 * @return
	 * @throws IOException
	 */
	private int read1(byte b[], int off, int len) throws IOException {
		if (bufLen <= 0 || bufLen == bufCur) {
			readToBuffer();
		}
		if (bufLen <= 0) {
			return -1;
		}
		int availableSize = bufLen - bufCur;
		if (len > availableSize) {
			len = availableSize;
		}
		System.arraycopy(buf, bufCur, b, off, len);
		bufCur += len;
		return len;
	}

	/**
	 * The same behavior as RandomAccessFile.readInt(). <br>
	 * Reads a signed 32-bit integer from this file. This method reads 4 bytes from
	 * the file, starting at the current file pointer. If the bytes read, in order,
	 * are <code>b1</code>, <code>b2</code>, <code>b3</code>, and <code>b4</code>,
	 * where <code>0&nbsp;&lt;=&nbsp;b1, b2, b3, b4&nbsp;&lt;=&nbsp;255</code>, then
	 * the result is equal to: <blockquote>
	 *
	 * <pre>
	 * (b1 &lt;&lt; 24) | (b2 &lt;&lt; 16) + (b3 &lt;&lt; 8) + b4
	 * </pre>
	 *
	 * </blockquote>
	 * <p>
	 * This method blocks until the four bytes are read, the end of the stream is
	 * detected, or an exception is thrown.
	 *
	 * @return the next four bytes of this stream, interpreted as an
	 *         <code>int</code>.
	 * @exception EOFException if this stream reaches the end before reading four
	 *                         bytes.
	 * @exception IOException  if an I/O error occurs.
	 */
	@Override
	public int readInt() throws IOException {
		byte ch[] = new byte[4];
		this.readFully(ch, 0, 4);

		int ret = 0;
		for (int i = 0; i < ch.length; i++) {
			ret = ((ret << 8) & 0xFFFFFF00) | (ch[i] & 0x000000FF);
		}
		return ret;
	}

	/**
	 * Reads a signed 64-bit integer from this file. This method reads eight bytes
	 * from the file, starting at the current file pointer. If the bytes read, in
	 * order, are <code>b1</code>, <code>b2</code>, <code>b3</code>,
	 * <code>b4</code>, <code>b5</code>, <code>b6</code>, <code>b7</code>, and
	 * <code>b8,</code> where: <blockquote>
	 *
	 * <pre>
	 *     0 &lt;= b1, b2, b3, b4, b5, b6, b7, b8 &lt;=255,
	 * </pre>
	 *
	 * </blockquote>
	 * <p>
	 * then the result is equal to:
	 * <p>
	 * <blockquote>
	 *
	 * <pre>
	 * ((long) b1 &lt;&lt; 56) + ((long) b2 &lt;&lt; 48) + ((long) b3 &lt;&lt; 40) + ((long) b4 &lt;&lt; 32) + ((long) b5 &lt;&lt; 24)
	 * 		+ ((long) b6 &lt;&lt; 16) + ((long) b7 &lt;&lt; 8) + b8
	 * </pre>
	 *
	 * </blockquote>
	 * <p>
	 * This method blocks until the eight bytes are read, the end of the stream is
	 * detected, or an exception is thrown.
	 *
	 * @return the next eight bytes of this file, interpreted as a
	 *         <code>long</code>.
	 * @exception EOFException if this file reaches the end before reading eight
	 *                         bytes.
	 * @exception IOException  if an I/O error occurs.
	 */
	@Override
	public final long readLong() throws IOException {
		return ((long) (readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
	}

	/**
	 * The same behavior as RandomAccessFile.readFully(byte b[], int off, int len)
	 * Reads exactly <code>len</code> bytes from this file into the byte array,
	 * starting at the current file pointer. This method reads repeatedly from the
	 * file until the requested number of bytes are read. This method blocks until
	 * the requested number of bytes are read, the end of the stream is detected, or
	 * an exception is thrown.
	 *
	 * @param b   the buffer into which the data is read.
	 * @param off the start offset of the data.
	 * @param len the number of bytes to read.
	 * @exception EOFException if this file reaches the end before reading all the
	 *                         bytes.
	 * @exception IOException  if an I/O error occurs.
	 */
	@Override
	public final void readFully(byte b[], int off, int len) throws IOException {
		int n = 0;
		do {
			int count = this.read(b, off + n, len - n);
			if (count < 0) {
				throw new EOFException();
			}
			n += count;
		} while (n < len);
	}

	/**
	 * @return the length of the stream
	 */
	public long getStreamLength() {
		return endPos - startPos;
	}

	/**
	 * Move the file pointer to the new location in the stream
	 *
	 * @param localPos - the new local postion in the stream. The localPos starts
	 *                 from 0.
	 */
	@Override
	public void seek(long localPos) throws IOException {
		if (localPosToGlobalPos(localPos) >= endPos) {
			throw new IOException(CoreMessages.getString(ResourceConstants.OUT_OF_RANGE)); // $NON-NLS-1$
		}

		if (localPos < cur - bufLen || localPos > cur) {
			cur = localPos;
			bufCur = 0;
			bufLen = 0;
		} else {
			bufCur = bufLen - (int) (cur - localPos);
		}

	}

	@Override
	public long getOffset() throws IOException {
		return cur - bufLen + bufCur;
	}

	@Override
	public long length() throws IOException {
		return getStreamLength();
	}

	@Override
	public int available() throws IOException {
		long availableSize = endPos - localPosToGlobalPos(cur);
		if (availableSize > Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		} else if (availableSize <= 0) {
			return -1;
		} else {
			return (int) availableSize;
		}
	}

	/**
	 * Convert the local position to global position.
	 *
	 * @param localPos - the local postion which starts from 0
	 * @return
	 */
	private long localPosToGlobalPos(long localPos) {
		return localPos + startPos;
	}
}
