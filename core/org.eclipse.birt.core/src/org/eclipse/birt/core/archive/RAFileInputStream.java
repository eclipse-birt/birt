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
import java.io.IOException;
import java.io.RandomAccessFile;

public class RAFileInputStream extends RAInputStream
{
	private RandomAccessFile parent;
	private long startPos; 	// in parentFile, the position of the first character
	private long endPos;   	// in parentFile, the position of EOF mark (not a valid character in the file)
	private long cur;		// in current stream, the virtual file pointer in local file
	
	/**
	 * @param parentFile - underlying RandomAccess file
	 * @param startPos - the (global) position of the first character in parentFile 
	 * @param endPos - the (global) position of EOF mark (not a valid character in the file)
	 */
	public RAFileInputStream( RandomAccessFile parentFile, long startPos, long endPos )
	{	
		this.parent = parentFile;
		this.startPos = startPos;
		this.endPos = endPos;
		
		try 
		{
			seekParent( 0 );
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		if ( localPosToGlobalPos(cur) < endPos )
		{
			seekParent( cur );
			int ret = parent.read();
			if ( ret >= 0 )
				cur++;
			
			return ret;
		}
		else
		{
			return -1;
		}
	}
	
	public int read( byte b[], int off, int len ) throws IOException
	{
		long parentPos = localPosToGlobalPos(cur);
		long avaliableSize = endPos - parentPos;
		if ( avaliableSize >= 0)
		{
			if (len > avaliableSize)
			{
				len = (int)avaliableSize;
			}
			seekParent( cur );
			int size = parent.read( b, off, len );
			if ( size > 0 )
			{
				cur += size;
			}
			return size;
		}
		return -1;
	}

    /**
     * The same behavior as RandomAccessFile.readInt(). <br>
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
    	int ch1 = this.read();
    	int ch2 = this.read();
    	int ch3 = this.read();
    	int ch4 = this.read();
    	if ((ch1 | ch2 | ch3 | ch4) < 0)
    	    throw new EOFException();
    	
    	return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	/**
	 * @return the length of the stream
	 */
	public long getStreamLength()
	{
		return endPos - startPos;
	}

	/**
	 * Move the file pointer to the new location in the stream
	 * @param localPos - the new local postion in the stream. The localPos starts from 0.  
	 */
	public void seek( long localPos ) throws IOException 
	{
		if ( localPosToGlobalPos(localPos) >= endPos )
			throw new IOException("The seek position is out of range.");  //$NON-NLS-1$

		seekParent( localPos );
		cur = localPos;
	}

	/**
	 * Convert the local position to global position.
	 * @param localPos - the local postion which starts from 0
	 * @return
	 */
	private long localPosToGlobalPos( long localPos )
	{
		return localPos + startPos;
	}
	
	/**
	 * Convert the local position to global position and move the file pointer to there in parent file.
	 * @param localPos - the local position which starts from 0
	 * @throws IOException
	 */
	private void seekParent( long localPos ) throws IOException
	{
		parent.seek( localPosToGlobalPos(localPos) );
	}

}
