
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.document;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.data.util.Bytes;

/**
 * 
 */

public class RandomDataAccessObject implements IRandomDataAccessObject {

	IRandomAccessObject delegate;
	private static Logger logger = Logger.getLogger(RandomDataAccessObject.class.getName());

	/**
	 * Constructor for the BufferedRandomAccessFile object
	 * 
	 * @param file       Description of Parameter
	 * @param mode       Description of Parameter
	 * @param bufferSize Description of Parameter
	 * @exception IOException Description of Exception
	 */
	public RandomDataAccessObject(IRandomAccessObject randomAccessObject) throws IOException {
		delegate = randomAccessObject;
	}

	/**
	 * Sets the Length attribute of the BufferedRandomAccessFile object
	 * 
	 * @param newLength The new Length value
	 * @exception IOException Description of Exception
	 */
	public void setLength(long newLength) throws IOException {
		delegate.setLength(newLength);
	}

	/**
	 * Gets the FilePointer attribute of the BufferedRandomAccessFile object
	 * 
	 * @return The FilePointer value
	 */
	public long getFilePointer() {
		try {
			return delegate.getFilePointer();
		} catch (IOException e) {
			logger.log(Level.FINE, e.getMessage(), e);
			return -1;
		}
	}

	// //////////////////////////// BEGIN CUT & PASTE FROM RandomAccessFile

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	public boolean readBoolean() throws IOException {
		return readByte() != 0;
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
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
	public char readChar() throws IOException {
		return (char) readUnsignedShort();
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	public int readInt() throws IOException {
		int ch1 = this.read();
		int ch2 = this.read();
		int ch3 = this.read();
		int ch4 = this.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0) {
			throw new EOFException();
		}
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	public long readLong() throws IOException {
		return ((long) (readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Date readDate() throws IOException {
		return new Date(readLong());
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	public String readLine() throws IOException {
		StringBuffer input = new StringBuffer();
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
	public void writeBoolean(boolean b) throws IOException {
		write(b ? 1 : 0);
	}

	/**
	 * Description of the Method
	 * 
	 * @param b Description of Parameter
	 * @exception IOException Description of Exception
	 */
	public void writeByte(int b) throws IOException {
		write(b);
	}

	/**
	 * Description of the Method
	 * 
	 * @param s Description of Parameter
	 * @exception IOException Description of Exception
	 */
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
	public void writeChar(int ch) throws IOException {
		writeShort(ch);
	}

	/**
	 * Description of the Method
	 * 
	 * @param i Description of Parameter
	 * @exception IOException Description of Exception
	 */
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
	public void writeDate(Date date) throws IOException {
		writeLong(date.getTime());
	}

	/**
	 * Description of the Method
	 * 
	 * @param f Description of Parameter
	 * @exception IOException Description of Exception
	 */
	public void writeFloat(float f) throws IOException {
		writeInt(Float.floatToIntBits(f));
	}

	/**
	 * Description of the Method
	 * 
	 * @param f Description of Parameter
	 * @exception IOException Description of Exception
	 */
	public void writeDouble(double f) throws IOException {
		writeLong(Double.doubleToLongBits(f));
	}

	/**
	 * 
	 * @param bigDecimal
	 * @throws IOException
	 */
	public void writeBigDecimal(BigDecimal bigDecimal) throws IOException {
		writeUTF(bigDecimal.toString());
	}

	/**
	 * Description of the Method
	 * 
	 * @param str Description of Parameter
	 * @exception IOException Description of Exception
	 */
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
	public long length() throws IOException {
		return delegate.length();
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
	public int read() throws IOException {
		return delegate.read();
	}

	/**
	 * 
	 */
	public Bytes readBytes() throws IOException {
		int size = readInt();
		byte[] b = new byte[size];
		int totalReadSize = 0;
		int readSize = read(b, 0, b.length);
		totalReadSize = readSize;
		while (readSize != -1 && totalReadSize < size) {
			readSize = read(b, totalReadSize, size - totalReadSize);
			if (readSize != -1) {
				totalReadSize += readSize;
			}
		}
		return new Bytes(b);
	}

	/**
	 * Description of the Method
	 * 
	 * @param b Description of Parameter
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
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
	public int read(byte[] b, int pos, int len) throws IOException {
		return delegate.read(b, pos, len);
	}

	/**
	 * Description of the Method
	 * 
	 * @param pos Description of Parameter
	 * @exception IOException Description of Exception
	 */
	public void seek(long pos) throws IOException {
		delegate.seek(pos);
	}

	/**
	 * Description of the Method
	 * 
	 * @param n Description of Parameter
	 * @return Description of the Returned Value
	 * @exception IOException Description of Exception
	 */
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
			delegate.seek(getFilePointer() + n);
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
	public void write(byte[] b, int pos, int len) throws IOException {
		this.delegate.write(b, pos, len);
	}

	/**
	 * Description of the Method
	 * 
	 * @param b Description of Parameter
	 * @exception IOException Description of Exception
	 */
	public void write(int b) throws IOException {
		byte[] bA = new byte[1];
		bA[0] = (byte) b;
		delegate.write(bA, 0, 1);
	}

	// This will do more when dual buffers are implemented.
	//
	/**
	 * Description of the Method
	 * 
	 * @exception IOException Description of Exception
	 */
	public void flush() throws IOException {
		delegate.flush();
	}

	/**
	 * Description of the Method
	 * 
	 * @exception IOException Description of Exception
	 */
	public void close() throws IOException {
		flush();
		delegate.close();
	}

	public Object readObject() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void writeObject(Object o) throws IOException {
		// TODO Auto-generated method stub

	}

}
