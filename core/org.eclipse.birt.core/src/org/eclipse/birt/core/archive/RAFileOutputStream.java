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

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is to be used by engine host (viewer), but not engine.  
 *
 */
public class RAFileOutputStream extends RAOutputStream
{
	private RandomAccessFile parent;
	private long startPos; 	// in parentFile, the position of the first character
	private long endPos;   	// in parentFile, the position of EOF mark (not a valid character in the file)
	private long cur;		// in current stream, the virtual file pointer (in bytes) in local file
	
	/**
	 * @param parentFile - underlying RandomAccess file
	 * @param startPos - the (global) position of the first character in parentFile 
	 * @param endPos - the (global) position of EOF mark (not a valid character in the file)
	 */
	public RAFileOutputStream( RandomAccessFile parentFile, long startPos)
	{
		this.parent = parentFile;
		this.startPos = startPos;
		this.endPos = startPos;
		
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
     * The same behavior as OutputStream.write(). <br>
     * Writes the specified byte to this output stream. The general 
     * contract for <code>write</code> is that one byte is written 
     * to the output stream. The byte to be written is the eight 
     * low-order bits of the argument <code>b</code>. The 24 
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an 
     * implementation for this method. 
     *
     * @param      b   the <code>byte</code>.
     * @exception  IOException  if an I/O error occurs. In particular, 
     *             an <code>IOException</code> may be thrown if the 
     *             output stream has been closed.
     */
	public void write( int b ) throws IOException 
	{	
		seekParent( cur );
		parent.write( b );
		
		long tmp = parent.getFilePointer();
		if ( tmp > endPos)
			endPos = tmp;
		
		cur++; // since we write a byte, the pointer (in bytes) should be increased by 1
	}

	/**
	 * Move the file pointer to the new location in the stream
	 * @param localPos - the new local postion in the stream  
	 */
	public void seek( long localPos ) throws IOException 
	{		
		seekParent( localPos );
		cur = localPos;
		
		long tmp = parent.getFilePointer();
		if (tmp > endPos)
			endPos = tmp;
	}

	/**
	 * @return the length of the stream
	 */
	public long getStreamLength()
	{
		return endPos - startPos;
	}

	/**
	 * Close the stream. If the stream is the only one in the underlying file, the file will be close too.
	 */
    public void close() throws IOException 
    {
   		parent.close();
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