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

/**
 * Extension fo the RandomAccessFile to use currBuf.bytesfered I/O as much as
 * possible. Usable with the <code>com.objectwave.persist.FileBroker</code> .
 * Publically identical to <code>java.io.RandomAccessFile</code> , except for
 * the constuctor and <code>flush()</code> .
 * <p>
 * 
 * <b>Note:</b> This class is not threadsafe.
 * 
 * @see java.io.RandomAccessFile
 */
public class BufferedRandomDataAccessObject extends AbstractBufferedRandomAccessObject {
	IRandomAccessObject delegate;

	/**
	 * Constructor for the BufferedRandomAccessFile object
	 * 
	 * @param file       Description of Parameter
	 * @param mode       Description of Parameter
	 * @param bufferSize Description of Parameter
	 * @exception IOException Description of Exception
	 */
	public BufferedRandomDataAccessObject(IRandomAccessObject randomAccessObject, int bufferSize) throws IOException {
		super(bufferSize);
		delegate = randomAccessObject;
		fillBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateClose()
	 */
	protected void delegateClose() throws IOException {
		delegate.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateGetFilePointer()
	 */
	protected long delegateGetFilePointer() throws IOException {
		return delegate.getFilePointer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateLength()
	 */
	protected long delegateLength() throws IOException {
		return delegate.length();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateRead(byte[], int, int)
	 */
	protected int delegateRead(byte[] b, int pos, int len) throws IOException {
		return delegate.read(b, pos, len);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateRead(byte[])
	 */
	protected int delegateRead(byte[] b) throws IOException {
		return delegate.read(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateSeek(long)
	 */
	protected void delegateSeek(long pos) throws IOException {
		delegate.seek(pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateSetLength(long)
	 */
	protected void delegateSetLength(long newLength) throws IOException {
		delegate.setLength(newLength);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.document.
	 * AbstractBufferedRandomAccessObject#delegateWrite(byte[], int, int)
	 */
	protected void delegateWrite(byte[] b, int pos, int len) throws IOException {
		delegate.write(b, pos, len);
	}

}
