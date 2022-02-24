
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

import org.eclipse.birt.core.archive.RAInputStream;

/**
 *
 */

public class RAReader implements IRandomAccessObject {
	private RAInputStream inputStream;

	/**
	 *
	 * @param raInputStream
	 * @param raOutputStream
	 */
	RAReader(RAInputStream inputStream) {
		this.inputStream = inputStream;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#close()
	 */
	@Override
	public void close() throws IOException {
		if (inputStream != null) {
			inputStream.close();
			inputStream = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#
	 * getFilePointer()
	 */
	@Override
	public long getFilePointer() throws IOException {
		return inputStream.getOffset();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#length()
	 */
	@Override
	public long length() throws IOException {
		return inputStream.length();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#read(byte
	 * [], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return inputStream.read(b, off, len);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#read(byte
	 * [])
	 */
	@Override
	public int read(byte[] b) throws IOException {
		return inputStream.read(b);
	}

	/**
	 *
	 */
	@Override
	public void seek(long pos) throws IOException {
		inputStream.seek(pos);
	}

	/**
	 *
	 */
	@Override
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
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		throw new UnsupportedOperationException("This is a read only object!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#flush()
	 */
	@Override
	public void flush() throws IOException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#read()
	 */
	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

}
