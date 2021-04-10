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

package org.eclipse.birt.data.engine.olap.data.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.olap.data.document.AbstractBufferedRandomAccessObject;

/**
 * Extension to the AbstractBufferedRandomAccessObject, use memory delegate
 * according to the setting of cacheSize if memory is available, or use the disk
 * cache instead.
 * 
 */
public class BufferedRandomAccessFile extends AbstractBufferedRandomAccessObject {
	private RandomAccessFile delegate;
	private byte[] memoryDelegate;
	private int pointer;
	private int length;
	private File file;
	private String mode;
	private boolean pureMemory;

	/**
	 * Constructor for the BufferedRandomAccessFile object
	 * 
	 * @param file       Description of Parameter
	 * @param mode       Description of Parameter
	 * @param bufferSize Description of Parameter
	 * @exception IOException Description of Exception
	 */
	public BufferedRandomAccessFile(File file, String mode, int bufferSize, int cacheSize) throws IOException {
		super(bufferSize);
		this.file = file;
		this.mode = mode;
		this.pureMemory = false;
		if (FileSecurity.fileExist(file) || cacheSize == 0) {
			createRandomAccessFile();
		} else if (cacheSize > 0) {
			memoryDelegate = new byte[cacheSize];
		} else {
			memoryDelegate = new byte[1024 * 1024];
			this.pureMemory = true;
		}
		fillBuffer();
	}

	/**
	 * 
	 * @param file
	 * @param mode
	 * @param bufferSize
	 * @throws IOException
	 */
	public BufferedRandomAccessFile(File file, String mode, int bufferSize) throws IOException {
		this(file, mode, bufferSize, 0);
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void createRandomAccessFile() throws IOException {
		File parent = file.getParentFile();
		if (!FileSecurity.fileExist(parent)) {
			FileSecurity.fileMakeDirs(parent);
		}
		delegate = FileSecurity.createRandomAccessFile(file, mode);
//		file.deleteOnExit( );
		if (memoryDelegate != null || length > 0) {
			delegate.write(memoryDelegate, 0, length);
			delegate.seek(pointer);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateClose()
	 */
	protected void delegateClose() throws IOException {
		if (delegate == null) {
			return;
		}
		delegate.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateGetFilePointer()
	 */
	protected long delegateGetFilePointer() throws IOException {
		if (delegate == null) {
			return pointer;
		}
		return delegate.getFilePointer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateLength()
	 */
	protected long delegateLength() throws IOException {
		if (delegate == null) {
			return length;
		}
		return delegate.length();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateRead(byte[], int, int)
	 */
	protected int delegateRead(byte[] b, int pos, int len) throws IOException {
		if (delegate == null) {
			int size = Math.min(length - pointer, len);
			if (size <= 0)
				return -1;
			System.arraycopy(memoryDelegate, pointer, b, pos, size);
			return size;
		}
		return delegate.read(b, pos, len);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateRead(byte[])
	 */
	protected int delegateRead(byte[] b) throws IOException {
		return delegateRead(b, 0, b.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateSeek(long)
	 */
	protected void delegateSeek(long pos) throws IOException {
		if (delegate == null) {
			if (pos > memoryDelegate.length && !pureMemory) {
				createRandomAccessFile();
			} else {
				if (pos > memoryDelegate.length) {
					restructMemoryDelegate();
				}
				pointer = (int) pos;
				return;
			}
		}
		delegate.seek(pos);
	}

	private void restructMemoryDelegate() {
		byte[] tmpBuf = null;
		if (memoryDelegate.length * 2 < 0 || memoryDelegate.length * 2l > Integer.MAX_VALUE) {
			tmpBuf = new byte[Integer.MAX_VALUE];
			this.pureMemory = false;
		} else {
			tmpBuf = new byte[memoryDelegate.length * 2];
		}
		System.arraycopy(memoryDelegate, 0, tmpBuf, 0, memoryDelegate.length);
		memoryDelegate = tmpBuf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateSetLength(long)
	 */
	protected void delegateSetLength(long newLength) throws IOException {
		if (delegate == null) {
			if (newLength > memoryDelegate.length && !pureMemory) {
				createRandomAccessFile();
			} else {
				if (newLength > memoryDelegate.length) {
					restructMemoryDelegate();
				}
				length = (int) newLength;
			}
			return;
		}
		delegate.setLength(newLength);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateWrite(byte[], int, int)
	 */
	protected void delegateWrite(byte[] b, int pos, int len) throws IOException {
		if (delegate == null) {
			if (pointer + len > memoryDelegate.length && !pureMemory) {
				createRandomAccessFile();
			} else {
				if (pointer + len > memoryDelegate.length) {
					restructMemoryDelegate();
				}
				System.arraycopy(b, pos, memoryDelegate, pointer, len);
				if (pointer + len > length) {
					length = pointer + len;
				}
				pointer += len;
				return;
			}
		}
		delegate.write(b, pos, len);
	}
}
