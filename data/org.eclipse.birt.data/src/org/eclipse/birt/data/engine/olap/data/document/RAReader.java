
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
	public long getFilePointer() throws IOException {
		return inputStream.getOffset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#length()
	 */
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
	public int read(byte[] b) throws IOException {
		return inputStream.read(b);
	}

	/**
	 * 
	 */
	public void seek(long pos) throws IOException {
		inputStream.seek(pos);
	}

	/**
	 * 
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
		throw new UnsupportedOperationException("This is a read only object!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#flush()
	 */
	public void flush() throws IOException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#read()
	 */
	public int read() throws IOException {
		return inputStream.read();
	}

}
