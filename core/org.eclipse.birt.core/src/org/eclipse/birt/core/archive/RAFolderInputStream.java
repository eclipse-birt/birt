/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * RAInputStream implementation for folder based report archive  
 * The implementation is based on Java random acess file.
 */
public class RAFolderInputStream extends RAInputStream
{
	private RandomAccessFile randomFile;
	
	/**
	 * @param file - a regular file (i.e. stream) in the folder
	 * @throws FileNotFoundException 
	 */
	public RAFolderInputStream( File file ) throws FileNotFoundException
	{	
		this.randomFile = new RandomAccessFile( file, "r" ); //$NON-NLS-1$
	}

    /**
     * The same behavior as InputStream.read().<br>
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     *
     * <p> A subclass must provide an implementation of this method.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
	public int read() throws IOException 
	{
		return randomFile.read();
	}
	
	public int read( byte b[], int off, int len ) throws IOException
	{
		return randomFile.read( b, off, len );
	}

    /**
     * Instead of calling randomAccessFile.readInt, we implement it in a better way (much better performace.) 
     * The external behavior is the same as RandomAccessFile.readInt(). <br>
     * Reads a signed 32-bit integer from this file. This method reads 4 
     * bytes from the file, starting at the current file pointer. 
     * If the bytes read, in order, are <code>b1</code>,
     * <code>b2</code>, <code>b3</code>, and <code>b4</code>, where 
     * <code>0&nbsp;&lt;=&nbsp;b1, b2, b3, b4&nbsp;&lt;=&nbsp;255</code>, 
     * then the result is equal to:
     * <blockquote><pre>
     *     (b1 &lt;&lt; 24) | (b2 &lt;&lt; 16) + (b3 &lt;&lt; 8) + b4
     * </pre></blockquote>
     * <p>
     * This method blocks until the four bytes are read, the end of the 
     * stream is detected, or an exception is thrown. 
     *
     * @return     the next four bytes of this stream, interpreted as an
     *             <code>int</code>.
     * @exception  EOFException  if this stream reaches the end before reading
     *               four bytes.
     * @exception  IOException   if an I/O error occurs.
     */
	public int readInt() throws IOException 
	{
		byte ch[] = new byte[4];
		randomFile.readFully(ch, 0, 4);
		
		int ret = 0;
		for ( int i = 0; i < ch.length; i++ )
		    ret = ((ret << 8) & 0xFFFFFF00) | (ch[i] & 0x000000FF);
		return ret;		
	}
	
	public long readLong() throws IOException
	{
		return randomFile.readLong( );
	}
	
    /**
     * The same behavior as RandomAccessFile.readFully(byte b[], int off, int len)
     * Reads exactly <code>len</code> bytes from this file into the byte 
     * array, starting at the current file pointer. This method reads 
     * repeatedly from the file until the requested number of bytes are 
     * read. This method blocks until the requested number of bytes are 
     * read, the end of the stream is detected, or an exception is thrown. 
     *
     * @param      b     the buffer into which the data is read.
     * @param      off   the start offset of the data.
     * @param      len   the number of bytes to read.
     * @exception  EOFException  if this file reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
	public final void readFully(byte b[], int off, int len) throws IOException
	{
		randomFile.readFully( b, off, len );
	}

	/**
	 * @return the length of the stream
	 * @throws IOException 
	 */
	public long getStreamLength() throws IOException
	{
		return randomFile.length();
	}
	
	/**
	 * Move the file pointer to the new location in the stream
	 * @param localPos - the new local postion in the stream. The localPos starts from 0.  
	 */
	public void seek( long localPos ) throws IOException 
	{
		randomFile.seek( localPos );
	}
	
	public long getOffset() throws IOException
	{
		return randomFile.getFilePointer( );
	}
	
	/**
	 * Close the stream
	 */
    public void close() throws IOException
    {
    	randomFile.close();	// Since the the underlying random access file is created by us, we need to close it
    	super.close();
    }
    
}
