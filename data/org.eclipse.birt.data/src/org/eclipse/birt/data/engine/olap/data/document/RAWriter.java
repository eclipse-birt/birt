
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

import org.eclipse.birt.core.archive.RAOutputStream;

/**
 * 
 */

public class RAWriter implements IRandomAccessObject {
	private RAOutputStream outputStream;
	private long length;

	/**
	 * 
	 * @param outputStream
	 */
	RAWriter(RAOutputStream outputStream) {
		this.outputStream = outputStream;
		this.length = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#close()
	 */
	public void close() throws IOException {
		outputStream.flush();
		outputStream.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#
	 * getFilePointer()
	 */
	public long getFilePointer() throws IOException {
		return outputStream.getOffset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#length()
	 */
	public long length() throws IOException {
		return length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#read(byte
	 * [], int, int)
	 */
	public int read(byte[] b, int off, int len) throws IOException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#read(byte
	 * [])
	 */
	public int read(byte[] b) throws IOException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#seek(
	 * long)
	 */
	public void seek(long pos) throws IOException {
		outputStream.seek(pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#setLength
	 * (long)
	 */
	public void setLength(long newLength) throws IOException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#write(
	 * byte[], int, int)
	 */
	public void write(byte[] b, int off, int len) throws IOException {
		long start = getFilePointer();

		outputStream.write(b, off, len);
		if (start + len > length)
			length = start + len;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#flush()
	 */
	public void flush() throws IOException {
		outputStream.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#read()
	 */
	public int read() throws IOException {
		throw new UnsupportedOperationException();
	}

}
