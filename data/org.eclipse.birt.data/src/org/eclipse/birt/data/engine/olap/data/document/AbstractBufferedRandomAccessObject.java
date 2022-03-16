
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.data.document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.UTFDataFormatException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.data.util.Bytes;

/**
 * Extension for the <code>java.io.RandomAccessFile</code>, except for the
 * constructor and <code>flush()</code> .
 * <p>
 *
 *
 * @see java.io.RandomAccessFile
 */
public abstract class AbstractBufferedRandomAccessObject implements IRandomDataAccessObject {
	private static Logger logger = Logger.getLogger(AbstractBufferedRandomAccessObject.class.getName());
	protected FileBufferStruct currBuf;

	/**
	 * Constructor for the BufferedRandomAccessFile object
	 *
	 * @param file       Description of Parameter
	 * @param mode       Description of Parameter
	 * @param bufferSize Description of Parameter
	 * @exception IOException Description of Exception
	 */
	public AbstractBufferedRandomAccessObject(int bufferSize) throws IOException {

		if (bufferSize < 1) {
			throw new Error("Buffer size must be at least 1");
		}

		currBuf = new FileBufferStruct();
		// set the buffer size as 8k for default
		currBuf.bytes = new byte[8192];

		currBuf.modified = false;
	}

	/**
	 *
	 * @return
	 */
	protected abstract long delegateGetFilePointer() throws IOException;

	/**
	 * Sets the Length attribute of the BufferedRandomAccessFile object
	 *
	 * @param newLength The new Length value
	 * @exception IOException Description of Exception
	 */
	@Override
	public void setLength(long newLength) throws IOException {
		// need to check altBuf, too.

		delegateSetLength(newLength);
		if (newLength < currBuf.filePos) {
			currBuf.filePos = newLength;
			currBuf.pos = 0;
			currBuf.dataLen = 0;
		} else if (newLength < currBuf.filePos + currBuf.dataLen) {
			currBuf.dataLen = (int) (newLength - currBuf.filePos);
			if (currBuf.dataLen > currBuf.pos) {
				currBuf.pos = currBuf.dataLen;
			}
		}
	}

	/**
	 *
	 * @param newLength
	 */
	protected abstract void delegateSetLength(long newLength) throws IOException;

	/**
	 * Gets the FilePointer attribute of the BufferedRandomAccessFile object
	 *
	 * @return The FilePointer value
	 */
	@Override
	public long getFilePointer() {
		return currBuf.filePos + currBuf.pos;
	}

	// //////////////////////////// BEGIN CUT & PASTE FROM RandomAccessFile

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public boolean readBoolean() throws IOException {
		return readByte() != 0;
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public int readUnsignedByte() throws IOException {
		int b = read();
		if (b < 0) {
			throw new EOFException();
		}
		return b & 0xff;
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public byte readByte() throws IOException {
		int b = read();
		if (b < 0) {
			throw new EOFException();
		}
		return (byte) b;
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public short readShort() throws IOException {
		int ch1 = this.read();
		int ch2 = this.read();
		if ((ch1 | ch2) < 0) {
			throw new EOFException();
		}
		return (short) ((ch1 << 8) + (ch2 << 0));
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public int readUnsignedShort() throws IOException {
		int ch1 = this.read();
		int ch2 = this.read();
		if ((ch1 | ch2) < 0) {
			throw new EOFException();
		}
		return (ch1 << 8) + (ch2 << 0);
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public char readChar() throws IOException {
		return (char) readUnsignedShort();
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public int readInt() throws IOException {
		byte[] b = new byte[4];
		readFully(b);
		return ((b[0] & 0xff) << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff));
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public long readLong() throws IOException {
		byte[] b = new byte[8];
		readFully(b);
		return ((long) (b[0] & 0xff) << 56) + ((long) (b[1] & 0xff) << 48) + ((long) (b[2] & 0xff) << 40)
				+ ((long) (b[3] & 0xff) << 32) + ((long) (b[4] & 0xff) << 24) + ((long) (b[5] & 0xff) << 16)
				+ ((long) (b[6] & 0xff) << 8) + ((long) (b[7] & 0xff));
	}

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	@Override
	public Date readDate() throws IOException {
		return new Date(readLong());
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public String readLine() throws IOException {
		StringBuilder input = new StringBuilder();
		int c = -1;
		boolean eol = false;

		while (!eol) {
			switch (c = read()) {
			case -1:
			case '\n':
				eol = true;
				break;
			case '\r':
				eol = true;
				long cur = getFilePointer();
				if ((read()) != '\n') {
					seek(cur);
				}
				break;
			default:
				input.append((char) c);
			}
		}

		if ((c == -1) && (input.length() == 0)) {
			return null;
		}
		return input.toString();
	}

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	@Override
	public BigDecimal readBigDecimal() throws IOException {
		String str = readUTF();
		return new BigDecimal(str);
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public String readUTF() throws IOException {
		// throw new Error("Not implemented yet");
		return DataInputStream.readUTF(this);
	}

	/**
	 * Description of the Method
	 *
	 * @param b Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void writeBoolean(boolean b) throws IOException {
		write(b ? 1 : 0);
	}

	/**
	 * Description of the Method
	 *
	 * @param b Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void writeByte(int b) throws IOException {
		write(b);
	}

	/**
	 * Description of the Method
	 *
	 * @param s Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void writeShort(int s) throws IOException {
		write((s >>> 8) & 0xFF);
		write((s >>> 0) & 0xFF);
	}

	/**
	 * Description of the Method
	 *
	 * @param ch Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void writeChar(int ch) throws IOException {
		writeShort(ch);
	}

	/**
	 * Description of the Method
	 *
	 * @param i Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void writeInt(int i) throws IOException {
		write((i >>> 24) & 0xFF);
		write((i >>> 16) & 0xFF);
		write((i >>> 8) & 0xFF);
		write((i >>> 0) & 0xFF);
	}

	/**
	 * Description of the Method
	 *
	 * @param l Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void writeLong(long l) throws IOException {
		write((int) (l >>> 56) & 0xFF);
		write((int) (l >>> 48) & 0xFF);
		write((int) (l >>> 40) & 0xFF);
		write((int) (l >>> 32) & 0xFF);
		write((int) (l >>> 24) & 0xFF);
		write((int) (l >>> 16) & 0xFF);
		write((int) (l >>> 8) & 0xFF);
		write((int) (l >>> 0) & 0xFF);
	}

	/**
	 *
	 * @param date
	 * @throws IOException
	 */
	@Override
	public void writeDate(Date date) throws IOException {
		writeLong(date.getTime());
	}

	/**
	 * Description of the Method
	 *
	 * @param f Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void writeFloat(float f) throws IOException {
		writeInt(Float.floatToIntBits(f));
	}

	/**
	 * Description of the Method
	 *
	 * @param f Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void writeDouble(double f) throws IOException {
		writeLong(Double.doubleToLongBits(f));
	}

	/**
	 *
	 * @param bigDecimal
	 * @throws IOException
	 */
	@Override
	public void writeBigDecimal(BigDecimal bigDecimal) throws IOException {
		writeUTF(bigDecimal.toString());
	}

	/**
	 * Description of the Method
	 *
	 * @param str Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void writeUTF(String str) throws IOException {
		int strlen = str.length();
		int utflen = 0;

		for (int i = 0; i < strlen; i++) {
			int c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				utflen++;
			} else if (c > 0x07FF) {
				utflen += 3;
			} else {
				utflen += 2;
			}
		}
		if (utflen > 65535) {
			throw new UTFDataFormatException();
		}
		write((utflen >>> 8) & 0xFF);
		write((utflen >>> 0) & 0xFF);
		for (int i = 0; i < strlen; i++) {
			int c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				write(c);
			} else if (c > 0x07FF) {
				write(0xE0 | ((c >> 12) & 0x0F));
				write(0x80 | ((c >> 6) & 0x3F));
				write(0x80 | ((c >> 0) & 0x3F));
			} else {
				write(0xC0 | ((c >> 6) & 0x1F));
				write(0x80 | ((c >> 0) & 0x3F));
			}
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param b Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void readFully(byte[] b) throws IOException {
		readFully(b, 0, b.length);
	}

	/**
	 * Description of the Method
	 *
	 * @param b   Description of Parameter
	 * @param pos Description of Parameter
	 * @param len Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void readFully(byte[] b, int pos, int len) throws IOException {
		int n = 0;
		while (n < len) {
			int count = this.read(b, pos + n, len - n);
			if (count < 0) {
				throw new EOFException();
			}
			n += count;
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param s Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void writeBytes(String s) throws IOException {
		byte[] b = s.getBytes();
		write(b, 0, b.length);
	}

	/**
	 * Description of the Method
	 *
	 * @param s Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void writeChars(String s) throws IOException {
		int clen = s.length();
		int blen = 2 * clen;
		byte[] b = new byte[blen];
		char[] c = new char[clen];
		s.getChars(0, clen, c, 0);
		for (int i = 0, j = 0; i < clen; i++) {
			b[j++] = (byte) (c[i] >>> 8);
			b[j++] = (byte) (c[i] >>> 0);
		}
		write(b, 0, blen);
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public long length() throws IOException {
		long fileLen = delegateLength();
		if (currBuf.filePos + currBuf.dataLen > fileLen) {
			return currBuf.filePos + currBuf.dataLen;
		} else {
			return fileLen;
		}
	}

	@Override
	public void writeObject(Object o) throws IOException {
		ByteArrayOutputStream buff = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(buff);
		oo.writeObject(o);
		oo.close();
		byte[] bytes = buff.toByteArray();
		writeBytes(new Bytes(bytes));
	}

	@Override
	public Object readObject() throws IOException {
		byte[] bytes = readBytes().bytesValue();
		final ClassLoader loader = org.eclipse.birt.data.engine.impl.DataEngineSession.getCurrentClassLoader();
		ObjectInputStream oo = new ObjectInputStream(new ByteArrayInputStream(bytes)) {
			@Override
			protected Class resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
				return Class.forName(desc.getName(), false, loader);
			}
		};
		Object obValue = null;

		try {
			obValue = oo.readObject();
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Failed to read object", e);
		}
		return obValue;
	}

	/**
	 *
	 * @return
	 */
	protected abstract long delegateLength() throws IOException;

	/**
	 * Description of the Method
	 *
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public int read() throws IOException {
		if (currBuf.pos < currBuf.dataLen) {
			// at least one byte is available in the buffer

			return (currBuf.bytes[currBuf.pos++] & 0xff);
		} else {
			syncBuffer(currBuf.filePos + currBuf.pos);
			if (currBuf.dataLen == 0) {
				throw new EOFException();
			}
			return read();
			// recurse: should be trivial this time.
		}
	}

	@Override
	public Bytes readBytes() throws IOException {
		byte[] b = new byte[readInt()];
		read(b, 0, b.length);
		return new Bytes(b);
	}

	/**
	 * Description of the Method
	 *
	 * @param b Description of Parameter
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	/**
	 * Description of the Method
	 *
	 * @param b   Description of Parameter
	 * @param pos Description of Parameter
	 * @param len Description of Parameter
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public int read(byte[] b, int pos, int len) throws IOException {
		if (currBuf.pos + len <= currBuf.dataLen) {
			// enough data available in buffer

			System.arraycopy(currBuf.bytes, currBuf.pos, b, pos, len);
			currBuf.pos += len;
			return len;
		} else {
			syncBuffer(currBuf.filePos + currBuf.pos);
			// currBuf.pos had better be 0 now.
			if (currBuf.dataLen < currBuf.bytes.length) {
				// we have read to EOF: couldn't fill a buffer

				int readLen = Math.min(len, currBuf.dataLen);
				if (readLen == 0) {
					return -1;
				}
				System.arraycopy(currBuf.bytes, currBuf.pos, b, pos, readLen);
				currBuf.pos += readLen;
				return readLen;
			} else if (currBuf.dataLen >= len) {
				return read(b, pos, len);
				// recurse: should be trivial this time
			} else {
				// too big for a buffer: use the delegate's read.

				delegateSeek(currBuf.filePos);
				int readLen = delegateRead(b, pos, len);
				currBuf.filePos += readLen;
				currBuf.dataLen = 0;
				currBuf.pos = 0;
				return readLen;
			}
		}
	}

	/**
	 *
	 * @param pos
	 */
	protected abstract void delegateSeek(long pos) throws IOException;

	/**
	 *
	 * @param b
	 * @param pos
	 * @param len
	 * @return
	 * @throws IOException
	 */
	protected abstract int delegateRead(byte[] b, int pos, int len) throws IOException;

	/**
	 *
	 * @param b
	 * @return
	 * @throws IOException
	 */
	protected abstract int delegateRead(byte[] b) throws IOException;

	/**
	 * Description of the Method
	 *
	 * @param pos Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void seek(long pos) throws IOException {
		long newBufPos = pos - currBuf.filePos;
		if (newBufPos >= 0 && newBufPos < currBuf.dataLen) {
			// it falls within the buffer

			currBuf.pos = (int) newBufPos;
		} else {
			syncBuffer(pos);
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param n Description of Parameter
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	@Override
	public int skipBytes(int n) throws IOException {
		return (int) skipBytes((long) n);
	}

	/**
	 * Description of the Method
	 *
	 * @param n Description of Parameter
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	public long skipBytes(long n) throws IOException {
		try {
			seek(currBuf.filePos + currBuf.pos + n);
			return n;
		} catch (EOFException ex) {
			return -1;
		}
	}

	/**
	 *
	 * @param b
	 * @throws IOException
	 */
	@Override
	public void writeBytes(Bytes b) throws IOException {
		writeInt(b.bytesValue().length);
		write(b.bytesValue(), 0, b.bytesValue().length);
	}

	/**
	 * Description of the Method
	 *
	 * @param b Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	/**
	 * Description of the Method
	 *
	 * @param b   Description of Parameter
	 * @param pos Description of Parameter
	 * @param len Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void write(byte[] b, int pos, int len) throws IOException {
		if (currBuf.pos + len <= currBuf.bytes.length) {
			System.arraycopy(b, pos, currBuf.bytes, currBuf.pos, len);
			currBuf.pos += len;
			currBuf.modified = true;
			if (currBuf.pos > currBuf.dataLen) {
				currBuf.dataLen = currBuf.pos;
			}
		} else if (len <= currBuf.bytes.length) {
			syncBuffer(currBuf.filePos + currBuf.pos);
			write(b, pos, len);
			// recurse: it should succeed trivially this time.
		} else {
			// write more than the buffer can contain: use delegate

			delegateSeek(currBuf.filePos + currBuf.pos);
			delegateWrite(b, pos, len);
			syncBuffer(currBuf.filePos + currBuf.pos + len);
		}
	}

	/**
	 *
	 * @param b
	 * @param pos
	 * @param len
	 * @throws IOException
	 */
	protected abstract void delegateWrite(byte[] b, int pos, int len) throws IOException;

	/**
	 * Description of the Method
	 *
	 * @param b Description of Parameter
	 * @exception IOException Description of Exception
	 */
	@Override
	public void write(int b) throws IOException {
		if (currBuf.pos < currBuf.bytes.length) {
			// trivial write

			currBuf.bytes[currBuf.pos++] = (byte) b;
			currBuf.modified = true;
			if (currBuf.pos > currBuf.dataLen) {
				currBuf.dataLen++;
			}
		} else {
			syncBuffer(currBuf.filePos + currBuf.pos);
			write(b);
			// recurse: should succeed trivially this time.
		}
	}

	// This will do more when dual buffers are implemented.
	//
	/**
	 * Description of the Method
	 *
	 * @exception IOException Description of Exception
	 */
	@Override
	public void flush() throws IOException {
		commitBuffer();
		/*
		 * FileBufferStruct temp = currBuf; try { currBuf = altBuf; commitBuffer(); }
		 * finally { currBuf = temp; }
		 */
	}

	/**
	 * Description of the Method
	 *
	 * @exception IOException Description of Exception
	 */
	@Override
	public void close() throws IOException {
		flush();
		delegateClose();
	}

	/**
	 *
	 */
	protected abstract void delegateClose() throws IOException;

	/**
	 * Save any changes and re-read the currBuf.bytes from the given position. Note
	 * that the read(byte[],int,int) method assumes that this method sets
	 * currBuf.pos to 0.
	 *
	 * @param new_FP Description of Parameter
	 * @return int - the number of bytes available for reading
	 * @exception IOException Description of Exception
	 */
	protected int syncBuffer(long new_FP) throws IOException {
		commitBuffer();
		delegateSeek(new_FP);
		currBuf.filePos = new_FP;
		fillBuffer();
		return currBuf.dataLen;
	}

	/**
	 * Description of the Method
	 *
	 * @exception IOException Description of Exception
	 */
	protected void fillBuffer() throws IOException {
		currBuf.filePos = delegateGetFilePointer();
		currBuf.dataLen = delegateRead(currBuf.bytes);
		currBuf.pos = 0;
		if (currBuf.dataLen < 0) {
			currBuf.dataLen = 0;
		}
	}

	/**
	 * If modified, write buffered bytes to the delegate file
	 *
	 * @exception IOException Description of Exception
	 */
	protected void commitBuffer() throws IOException {
		if (currBuf.modified) {
			delegateSeek(currBuf.filePos);

			delegateWrite(currBuf.bytes, 0, currBuf.dataLen);
			currBuf.modified = false;
		}
	}

	/*
	 * Internal structure for holding data
	 */
	protected static class FileBufferStruct {

		/**
		 * Description of the Field
		 */
		public byte[] bytes;
		/**
		 * Description of the Field
		 */
		public int pos;
		/**
		 * Description of the Field
		 */
		public int dataLen;
		/**
		 * Description of the Field
		 */
		public boolean modified;
		/**
		 * Description of the Field
		 */
		public long filePos;
	}
}
