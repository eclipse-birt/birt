
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.data.util.Bytes;

/**
 * An implementation of the <tt>IDocumentObject</tt> interface.
 */

public class DocumentObject implements IDocumentObject {
	IRandomDataAccessObject delegate = null;
	private static Logger logger = Logger.getLogger(DocumentObject.class.getName());

	DocumentObject(IRandomDataAccessObject randomAccessObject) throws IOException {
		this.delegate = randomAccessObject;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IDocumentObject#getFilePointer()
	 */
	@Override
	public long getFilePointer() {
		try {
			return delegate.getFilePointer();
		} catch (IOException e) {
			logger.log(Level.FINE, e.getMessage(), e);
			return -1;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#length()
	 */
	@Override
	public long length() throws IOException {
		return delegate.length();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#readBoolean()
	 */
	@Override
	public boolean readBoolean() throws IOException {
		return delegate.readBoolean();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IDocumentObject#readBigDecimal()
	 */
	@Override
	public BigDecimal readBigDecimal() throws IOException {
		if (delegate.readByte() == 0) {
			return null;
		}
		return delegate.readBigDecimal();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#read(byte[],
	 * int, int)
	 */
	@Override
	public int read(byte[] b, int pos, int len) throws IOException {
		return delegate.read(b, pos, len);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#readDate()
	 */
	@Override
	public Date readDate() throws IOException {
		if (delegate.readByte() == 0) {
			return null;
		}
		return delegate.readDate();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#readDouble()
	 */
	@Override
	public double readDouble() throws IOException {
		return delegate.readDouble();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#readInt()
	 */
	@Override
	public int readInt() throws IOException {
		return delegate.readInt();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#readShort()
	 */
	@Override
	public int readShort() throws IOException {
		return delegate.readShort();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#readString()
	 */
	@Override
	public String readString() throws IOException {
		if (delegate.readByte() == 0) {
			return null;
		}
		return delegate.readUTF();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#seek(long)
	 */
	@Override
	public void seek(long pos) throws IOException {
		delegate.seek(pos);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#setLength(long)
	 */
	@Override
	public void setLength(long newLength) throws IOException {
		delegate.setLength(newLength);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#skipBytes(int)
	 */
	@Override
	public int skipBytes(int n) throws IOException {
		return delegate.skipBytes(n);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#writeBoolean(
	 * boolean)
	 */
	@Override
	public void writeBoolean(boolean b) throws IOException {
		delegate.writeBoolean(b);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IDocumentObject#writeBigDecimal(java
	 * .math.BigDecimal)
	 */
	@Override
	public void writeBigDecimal(BigDecimal value) throws IOException {
		if (value == null) {
			delegate.writeByte(0);
			return;
		} else {
			delegate.writeByte(1);
		}
		delegate.writeBigDecimal(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#write(byte[],
	 * int, int)
	 */
	@Override
	public void write(byte[] b, int pos, int len) throws IOException {
		delegate.write(b, pos, len);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IDocumentObject#writeDate(java.util.
	 * Date)
	 */
	@Override
	public void writeDate(Date value) throws IOException {
		if (value == null) {
			delegate.writeByte(0);
			return;
		} else {
			delegate.writeByte(1);
		}
		delegate.writeDate(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IDocumentObject#writeDouble(double)
	 */
	@Override
	public void writeDouble(double value) throws IOException {
		delegate.writeDouble(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#writeShort(int)
	 */
	@Override
	public void writeShort(int value) throws IOException {
		delegate.writeShort(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#writeInt(int)
	 */
	@Override
	public void writeInt(int value) throws IOException {
		delegate.writeInt(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IDocumentObject#writeString(java.
	 * lang.String)
	 */
	@Override
	public void writeString(String value) throws IOException {
		if (value == null) {
			delegate.writeByte(0);
			return;
		} else {
			delegate.writeByte(1);
		}
		delegate.writeUTF(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#close()
	 */
	@Override
	public void close() throws IOException {
		delegate.close();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#readByte()
	 */
	@Override
	public byte readByte() throws IOException {
		return delegate.readByte();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#writeByte(int)
	 */
	@Override
	public void writeByte(int b) throws IOException {
		delegate.writeByte(b);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#flush()
	 */
	@Override
	public void flush() throws IOException {
		delegate.flush();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#readBytes()
	 */
	@Override
	public Bytes readBytes() throws IOException {
		if (delegate.readByte() == 0) {
			return null;
		}
		return delegate.readBytes();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentObject#writeBytes(org.
	 * eclipse.birt.data.olap.data.util.Bytes)
	 */
	@Override
	public void writeBytes(Bytes b) throws IOException {
		if (b == null) {
			delegate.writeByte(0);
			return;
		} else {
			delegate.writeByte(1);
		}
		delegate.writeBytes(b);
	}

	@Override
	public Object readObject() throws IOException {
		if (delegate.readByte() == 0) {
			return null;
		}
		return delegate.readObject();
	}

	@Override
	public void writeObject(Object o) throws IOException {
		if (o == null) {
			delegate.writeByte(0);
			return;
		}
		delegate.writeByte(1);
		delegate.writeObject(o);
	}

}
