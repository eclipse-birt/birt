
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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.eclipse.birt.data.engine.core.security.FileSecurity;

/**
 *
 */

public class SimpleRandomAccessObject implements IRandomAccessObject {
	RandomAccessFile randomAccessFile = null;

	public SimpleRandomAccessObject(File file, String mode) throws FileNotFoundException {
		this.randomAccessFile = FileSecurity.createRandomAccessFile(file, mode);
	}

	@Override
	public void close() throws IOException {
		randomAccessFile.close();
	}

	public FileDescriptor getFD() throws IOException {
		return randomAccessFile.getFD();
	}

	@Override
	public long getFilePointer() throws IOException {
		return randomAccessFile.getFilePointer();
	}

	@Override
	public long length() throws IOException {
		return randomAccessFile.length();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return randomAccessFile.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return randomAccessFile.read(b);
	}

	@Override
	public void seek(long pos) throws IOException {
		randomAccessFile.seek(pos);
	}

	@Override
	public void setLength(long newLength) throws IOException {
		randomAccessFile.setLength(newLength);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		randomAccessFile.write(b, off, len);
	}

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
		return randomAccessFile.read();
	}

}
