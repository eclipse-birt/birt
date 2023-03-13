
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
	void setLength(long newLength) throws IOException;

	/**
	 *
	 * @param pos
	 * @throws IOException
	 */
	void seek(long pos) throws IOException;

	/**
	 *
	 * @param n
	 * @return
	 * @throws IOException
	 */
	int skipBytes(int n) throws IOException;

	/**
	 *
	 * @return
	 */
	long getFilePointer();

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	long length() throws IOException;

	/**
	 *
	 * @param b
	 * @param pos
	 * @param len
	 * @throws IOException
	 */
	void write(byte[] b, int pos, int len) throws IOException;

	/**
	 *
	 * @param b
	 * @param pos
	 * @param len
	 * @return
	 * @throws IOException
	 */
	int read(byte[] b, int pos, int len) throws IOException;

	/**
	 *
	 * @param b
	 * @throws IOException
	 */
	void writeBytes(Bytes b) throws IOException;

	/**
	 *
	 * @param b
	 * @return
	 * @throws IOException
	 */
	Bytes readBytes() throws IOException;

	/**
	 *
	 * @param b
	 * @throws IOException
	 */
	void writeByte(int b) throws IOException;

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	byte readByte() throws IOException;

	/**
	 *
	 * @param value
	 * @throws IOException
	 */
	void writeBoolean(boolean value) throws IOException;

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	boolean readBoolean() throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @param bytes
	 * @return
	 */
	void writeInt(int value) throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @param bytes
	 * @return
	 */
	int readInt() throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @param bytes
	 * @return
	 */
	void writeShort(int value) throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @param bytes
	 * @return
	 */
	int readShort() throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @param value
	 */
	void writeDouble(double value) throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @return
	 */
	double readDouble() throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @param value
	 */
	void writeBigDecimal(BigDecimal value) throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @return
	 */
	BigDecimal readBigDecimal() throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @param value
	 */
	void writeDate(Date value) throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @return
	 */
	Date readDate() throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @param value
	 */
	void writeString(String value) throws IOException;

	/**
	 *
	 * @param diskObjectName
	 * @param offset
	 * @return
	 */
	String readString() throws IOException;

	Object readObject() throws IOException;

	void writeObject(Object o) throws IOException;

	/**
	 *
	 *
	 */
	void close() throws IOException;

	/**
	 *
	 * @throws IOException
	 */
	void flush() throws IOException;
}
