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
import org.eclipse.birt.core.util.IOUtil;

/**
 * RAOutputStream implementation for folder based report archive  
 */
public class RAFolderOutputStream extends RAOutputStream
{
	FolderArchiveWriter archive;
	
	private RandomAccessFile randomFile;
	
    /**
     * The internal buffer where data is stored. 
     */
    protected byte buf[];

    /**
     * The number of valid bytes in the buffer. This value is always 
     * in the range <tt>0</tt> through <tt>buf.length</tt>; elements 
     * <tt>buf[0]</tt> through <tt>buf[count-1]</tt> contain valid 
     * byte data.
     */
    protected int count;
	
	public RAFolderOutputStream( FolderArchiveWriter archive, File file ) throws FileNotFoundException
	{
		this.archive = archive;
		this.randomFile = new RandomAccessFile( file, "rw" ); //$NON-NLS-1$
		this.buf = new byte[IOUtil.RA_STREAM_BUFFER_LENGTH];
		this.count = 0;
	}
	
    /** Flush the internal buffer */
    private void flushBuffer() throws IOException {
        if (count > 0) {
	    randomFile.write(buf, 0, count);
	    count = 0;
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
		if (count >= buf.length) {
		    flushBuffer();
		}
		buf[count++] = (byte)b;
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
		this.write(b, 0, b.length);		
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
    	if (len >= buf.length) {
    	    /* If the request length exceeds the size of the output buffer,
        	       flush the output buffer and then write the data directly.
        	       In this way buffered streams will cascade harmlessly. */
    	    flushBuffer();
    	    randomFile.write(b, off, len);
    	    return;
    	}
    	if (len > buf.length - count) {
    	    flushBuffer();
    	}
    	System.arraycopy(b, off, buf, count, len);
    	count += len;
    }
    
    /**
     * Same behavior as DataOutputStream.writeInt();
     */
    public void writeInt(int v) throws IOException
    {
		this.write( ( v >>> 24 ) & 0xFF );
		this.write( ( v >>> 16 ) & 0xFF );
		this.write( ( v >>> 8 ) & 0xFF );
		this.write( ( v >>> 0 ) & 0xFF );
    }
    
    /**
     * Same behavior as DataOutputStream.writeLong();
     */
    public void writeLong(long v) throws IOException
    {
    	byte writeBuffer[] = new byte[8];
        writeBuffer[0] = (byte)(v >>> 56);
        writeBuffer[1] = (byte)(v >>> 48);
        writeBuffer[2] = (byte)(v >>> 40);
        writeBuffer[3] = (byte)(v >>> 32);
        writeBuffer[4] = (byte)(v >>> 24);
        writeBuffer[5] = (byte)(v >>> 16);
        writeBuffer[6] = (byte)(v >>>  8);
        writeBuffer[7] = (byte)(v >>>  0);
        this.write(writeBuffer, 0, 8);
    }
    
    public long getOffset() throws IOException
    {
    	return randomFile.getFilePointer( ) + count;
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
		if ( localPos != this.getOffset() )
		{
			flushBuffer();
			randomFile.seek( localPos );
		}
	}
	
	/**
	 * Flush the stream.
	 */
	public void flush() throws IOException
	{
		flushBuffer();
		super.flush();
	}
	
	/**
	 * Close the stream. If the stream is the only one in the underlying file, the file will be close too.
	 */
    public void close() throws IOException 
    {
    	flushBuffer();
   		randomFile.close(); // Since the the underlying random access file is created by us, we need to close it
   		super.close();
   		archive.removeStream( this );
    }

}