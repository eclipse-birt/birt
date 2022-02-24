
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

import org.eclipse.birt.data.engine.olap.data.util.Bytes;

/**
 * 
 */

public interface IDocumentObject {
	/**
	 * 
	 * @param newLength
	 * @throws IOException
	 */
	public void setLength(long newLength) throws IOException;

	/**
	 * 
	 * @param pos
	 * @throws IOException
	 */
	public void seek(long pos) throws IOException;

	/**
	 * 
	 * @param n
	 * @return
	 * @throws IOException
	 */
	public int skipBytes(int n) throws IOException;

	/**
	 * 
	 * @return
	 */
	public long getFilePointer();

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public long length() throws IOException;

	/**
	 * 
	 * @param b
	 * @param pos
	 * @param len
	 * @throws IOException
	 */
	public void write(byte[] b, int pos, int len) throws IOException;

	/**
	 * 
	 * @param b
	 * @param pos
	 * @param len
	 * @return
	 * @throws IOException
	 */
	public int read(byte[] b, int pos, int len) throws IOException;

	/**
	 * 
	 * @param b
	 * @throws IOException
	 */
	public void writeBytes(Bytes b) throws IOException;

	/**
	 * 
	 * @param b
	 * @return
	 * @throws IOException
	 */
	public Bytes readBytes() throws IOException;

	/**
	 * 
	 * @param b
	 * @throws IOException
	 */
	public void writeByte(int b) throws IOException;

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public byte readByte() throws IOException;

	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void writeBoolean(boolean value) throws IOException;

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean readBoolean() throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @param bytes
	 * @return
	 */
	public void writeInt(int value) throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @param bytes
	 * @return
	 */
	public int readInt() throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @param bytes
	 * @return
	 */
	public void writeShort(int value) throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @param bytes
	 * @return
	 */
	public int readShort() throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @param value
	 */
	public void writeDouble(double value) throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @return
	 */
	public double readDouble() throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @param value
	 */
	public void writeBigDecimal(BigDecimal value) throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @return
	 */
	public BigDecimal readBigDecimal() throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @param value
	 */
	public void writeDate(Date value) throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @return
	 */
	public Date readDate() throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @param value
	 */
	public void writeString(String value) throws IOException;

	/**
	 * 
	 * @param diskObjectName
	 * @param offset
	 * @return
	 */
	public String readString() throws IOException;

	public Object readObject() throws IOException;

	public void writeObject(Object o) throws IOException;

	/**
	 * 
	 *
	 */
	public void close() throws IOException;

	/**
	 * 
	 * @throws IOException
	 */
	public void flush() throws IOException;
}
