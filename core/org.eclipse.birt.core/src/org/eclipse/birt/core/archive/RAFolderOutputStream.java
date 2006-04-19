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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * RAOutputStream implementation for folder based report archive  
 */
public class RAFolderOutputStream extends RAOutputStream
{
	private RandomAccessFile randomFile;
	
	public RAFolderOutputStream( File file ) throws FileNotFoundException
	{
		this.randomFile = new RandomAccessFile( file, "rw" ); //$NON-NLS-1$
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
		randomFile.write( b );		
	}

    /**
     * Writes <code>b.length</code> bytes from the specified byte array 
     * to this output stream. The general contract for <code>write(b)</code> 
     * is that it should have exactly the same effect as the call 
     * <code>write(b, 0, b.length)</code>.
     *
     * @param      b   the data.
     * @exception  IOException  if an I/O error occurs.
     * @see        java.io.OutputStream#write(byte[], int, int)
     */
	public void write(byte b[]) throws IOException
    {
		randomFile.write( b );		
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array 
     * starting at offset <code>off</code> to this output stream. 
     * The general contract for <code>write(b, off, len)</code> is that 
     * some of the bytes in the array <code>b</code> are written to the 
     * output stream in order; element <code>b[off]</code> is the first 
     * byte written and <code>b[off+len-1]</code> is the last byte written 
     * by this operation.
     *
     * @param      b     the data.
     * @param      off   the start offset in the data.
     * @param      len   the number of bytes to write.
     * @exception  IOException  if an I/O error occurs.
     */
    public void write(byte b[], int off, int len) throws IOException 
    {
		randomFile.write( b, off, len );		
    }
    
    public void writeInt(int v) throws IOException
    {
    	randomFile.writeInt( v );
    }
    
    public void writeLong(long v) throws IOException
    {
    	randomFile.writeLong( v );
    }
    
    public long getOffset() throws IOException
    {
    	return randomFile.getFilePointer( );
    }

	/**
	 * Same behavior as the seek in RandomAccessFile. <br>
	 * Sets the file-pointer offset, measured from the beginning of this 
     * file, at which the next read or write occurs.  The offset may be 
     * set beyond the end of the file. Setting the offset beyond the end 
     * of the file does not change the file length.  The file length will 
     * change only by writing after the offset has been set beyond the end 
     * of the file. 

	 * @param localPos - the new local postion in the stream, measured in bytes from the 
     *                   beginning of the stream  
	 */
	public void seek( long localPos ) throws IOException 
	{
		randomFile.seek( localPos );
	}

	/**
	 * Close the stream. If the stream is the only one in the underlying file, the file will be close too.
	 */
    public void close() throws IOException 
    {
   		randomFile.close(); // Since the the underlying random access file is created by us, we need to close it
   		super.close();
    }

}