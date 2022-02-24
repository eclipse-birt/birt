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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is to be used by engine host (viewer), but not engine.
 *
 */
public class RAFileOutputStream extends RAOutputStream {
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger(RAFileOutputStream.class.getName());

	private RandomAccessFile parent;
	private long startPos; // in parentFile, the position of the first character
	private long endPos; // in parentFile, the position of EOF mark (not a valid character in the file)
	private long cur; // in current stream, the virtual file pointer (in bytes) in local file

	/**
	 * @param parentFile - underlying RandomAccess file
	 * @param startPos   - the (global) position of the first character in
	 *                   parentFile
	 * @param endPos     - the (global) position of EOF mark (not a valid character
	 *                   in the file)
	 */
	public RAFileOutputStream(RandomAccessFile parentFile, long startPos) {
		this.parent = parentFile;
		this.startPos = startPos;
		this.endPos = startPos;

		try {
			seekParent(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * The same behavior as OutputStream.write(). <br>
	 * Writes the specified byte to this output stream. The general contract for
	 * <code>write</code> is that one byte is written to the output stream. The byte
	 * to be written is the eight low-order bits of the argument <code>b</code>. The
	 * 24 high-order bits of <code>b</code> are ignored.
	 * <p>
	 * Subclasses of <code>OutputStream</code> must provide an implementation for
	 * this method.
	 *
	 * @param b the <code>byte</code>.
	 * @exception IOException if an I/O error occurs. In particular, an
	 *                        <code>IOException</code> may be thrown if the output
	 *                        stream has been closed.
	 */
	public void write(int b) throws IOException {
		seekParent(cur);
		parent.write(b);

		long tmp = parent.getFilePointer();
		if (tmp > endPos)
			endPos = tmp;

		cur++; // since we write a byte, the pointer (in bytes) should be increased by 1
	}

	/**
	 * Writes <code>b.length</code> bytes from the specified byte array to this
	 * output stream. The general contract for <code>write(b)</code> is that it
	 * should have exactly the same effect as the call
	 * <code>write(b, 0, b.length)</code>.
	 *
	 * @param b the data.
	 * @exception IOException if an I/O error occurs.
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public void write(byte b[]) throws IOException {
		seekParent(cur);
		parent.write(b);

		long tmp = parent.getFilePointer();
		if (tmp > endPos)
			endPos = tmp;

		cur += b.length; // since we write a byte, the pointer (in bytes) should be increased by 1
	}

	/**
	 * Writes <code>len</code> bytes from the specified byte array starting at
	 * offset <code>off</code> to this output stream. The general contract for
	 * <code>write(b, off, len)</code> is that some of the bytes in the array
	 * <code>b</code> are written to the output stream in order; element
	 * <code>b[off]</code> is the first byte written and <code>b[off+len-1]</code>
	 * is the last byte written by this operation.
	 *
	 * @param b   the data.
	 * @param off the start offset in the data.
	 * @param len the number of bytes to write.
	 * @exception IOException if an I/O error occurs.
	 */
	public void write(byte b[], int off, int len) throws IOException {
		seekParent(cur);
		parent.write(b, off, len);

		long tmp = parent.getFilePointer();
		if (tmp > endPos)
			endPos = tmp;

		cur += len; // since we write a byte, the pointer (in bytes) should be increased by 1
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

	/**
	 * Same behavior as the seek in RandomAccessFile. <br>
	 * Sets the file-pointer offset, measured from the beginning of this file, at
	 * which the next read or write occurs. The offset may be set beyond the end of
	 * the file. Setting the offset beyond the end of the file does not change the
	 * file length. The file length will change only by writing after the offset has
	 * been set beyond the end of the file.
	 * 
	 * @param localPos - the new local postion in the stream, measured in bytes from
	 *                 the beginning of the stream
	 */
	public void seek(long localPos) throws IOException {
		seekParent(localPos);
		cur = localPos;
	}

	public long getOffset() throws IOException {
		return cur;
	}

	/**
	 * @return the length of the stream
	 */
	public long getStreamLength() {
		return endPos - startPos;
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

	/**
	 * Convert the local position to global position and move the file pointer to
	 * there in parent file.
	 * 
	 * @param localPos - the local position which starts from 0
	 * @throws IOException
	 */
	private void seekParent(long localPos) throws IOException {
		parent.seek(localPosToGlobalPos(localPos));
	}

	public long length() throws IOException {
		return getStreamLength();
	}
}
