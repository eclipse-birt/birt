
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

/**
 * 
 */

public interface IRandomAccessObject {
	/**
	 * Returns the current offset in this object.
	 *
	 * @return the offset from the beginning of the object, in bytes, at which the
	 *         next read or write occurs.
	 * @exception IOException if an I/O error occurs.
	 */
	public long getFilePointer() throws IOException;

	/**
	 * Sets the length of this object.
	 *
	 * @param newLength The desired length of the object
	 * @exception IOException If an I/O error occurs
	 */
	public void setLength(long newLength) throws IOException;

	/**
	 * Returns the length of this object.
	 *
	 * @return the length of this object, measured in bytes.
	 * @exception IOException if an I/O error occurs.
	 */
	public long length() throws IOException;

	/**
	 * Sets the object-pointer offset, measured from the beginning of this object,
	 * at which the next read or write occurs.
	 *
	 * @param pos the offset position, measured in bytes from the beginning of the
	 *            object, at which to set the object pointer.
	 * @exception IOException if <code>pos</code> is less than <code>0</code> or if
	 *                        an I/O error occurs.
	 */
	public void seek(long pos) throws IOException;

	/**
	 * Reads up to <code>len</code> bytes of data from this object into an array of
	 * bytes. This method blocks until at least one byte of input is available.
	 * 
	 * @param b   the buffer into which the data is read.
	 * @param off the start offset of the data.
	 * @param len the maximum number of bytes read.
	 * @return the total number of bytes read into the buffer, or <code>-1</code> if
	 *         there is no more data because the end of the object has been reached.
	 * @exception IOException if an I/O error occurs.
	 */
	public int read(byte b[], int off, int len) throws IOException;

	/**
	 * Writes <code>len</code> bytes from the specified byte array starting at
	 * offset <code>off</code> to this object.
	 *
	 * @param b   the data.
	 * @param off the start offset in the data.
	 * @param len the number of bytes to write.
	 * @exception IOException if an I/O error occurs.
	 */
	public void write(byte b[], int off, int len) throws IOException;

	/**
	 * Closes this random access object and releases any system resources associated
	 * with the object.
	 * 
	 * @exception IOException if an I/O error occurs.
	 *
	 */
	public void close() throws IOException;

	/**
	 * Reads up to <code>b.length</code> bytes of data from this object into an
	 * array of bytes. This method blocks until at least one byte of input is
	 * available.
	 * 
	 * @param b the buffer into which the data is read.
	 * @return the total number of bytes read into the buffer, or <code>-1</code> if
	 *         there is no more data because the end of this object has been
	 *         reached.
	 * @exception IOException if an I/O error occurs.
	 */
	public int read(byte b[]) throws IOException;

	/*
	 * 
	 */
	public int read() throws IOException;

	/**
	 * 
	 *
	 */
	public void flush() throws IOException;
}
