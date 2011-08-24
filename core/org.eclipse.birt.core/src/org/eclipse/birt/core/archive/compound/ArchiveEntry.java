/*******************************************************************************
 * Copyright (c) 2004,2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.EOFException;
import java.io.IOException;

import org.eclipse.birt.core.archive.ArchiveUtil;

/**
 * the user must close the archive
 */
abstract public class ArchiveEntry
{

	abstract public String getName( ) throws IOException;

	abstract protected long _getLength( ) throws IOException;

	abstract protected void _setLength( long length ) throws IOException;

	abstract protected void _flush( ) throws IOException;

	abstract protected void _refresh( ) throws IOException;

	abstract public int read( long offset, byte[] b, int off, int size )
			throws IOException;

	abstract public void write( long offset, byte[] b, int off, int size )
			throws IOException;

	abstract public void close( ) throws IOException;

	/**
	 * the offset of the first byte of buffer in the file
	 */
	protected long offset = 0;
	final private static int MAX_BUFFER_SIZE = 4096;
	/**
	 * the read writer buffer
	 */
	protected byte[] buffer = new byte[MAX_BUFFER_SIZE];
	/**
	 * if the buffer is write buffer
	 */
	protected boolean buffer_dirty;
	/**
	 * the current point in the buffer
	 */
	protected int buffer_offset;
	/**
	 * the data size in the buffer
	 */
	protected int buffer_size;

	/**
	 * get the length of the entry.
	 *
	 * @return
	 * @throws IOException
	 */
	synchronized final public long getLength( ) throws IOException
	{
		long length = _getLength( );
		// in case there are any buffered data not flushed to the disk
		if ( length < offset + buffer_size )
		{
			return offset + buffer_size;
		}
		return length;
	}

	/**
	 * set the length to the entry
	 *
	 * @param length
	 * @throws IOException
	 */
	synchronized final public void setLength( long length ) throws IOException
	{
		// file end before the buffer, ignore the buffer then set the offset
		// to file end
		if ( length <= offset )
		{
			offset = length;
			buffer_size = 0;
			buffer_offset = 0;
			buffer_dirty = false;
		}
		else if ( length <= offset + buffer_size )
		{
			// file ended in the buffer, ignore the remain buffer
			buffer_size = (int) ( length - offset );
			if ( buffer_size < buffer_offset )
			{
				buffer_offset = buffer_size;
			}
		}
		_setLength( length );
	}

	/**
	 * flush the data into the disk
	 *
	 * @throws IOException
	 */
	synchronized final public void flush( ) throws IOException
	{
		saveBuffer( );
		_flush( );
	}

	/**
	 * reload the data from the disk.
	 *
	 * It fails if there are any dirty data. Otherwise, it simply ignore the
	 * buffer
	 *
	 * @throws IOException
	 */
	synchronized final public void refresh( ) throws IOException
	{
		if ( !buffer_dirty )
		{
			offset += buffer_offset;
			buffer_offset = 0;
			buffer_size = 0;
		}
		_refresh( );
	}

	/**
	 * set the file position to offset
	 *
	 * if the file position exceed the file length, the EOF exception is throw
	 * out in next read. It is OK for write.
	 *
	 * @param off
	 *            the file position
	 * @throws IOException
	 */
	synchronized final public void seek( long off ) throws IOException
	{
		if ( off > offset )
		{
			if ( off < offset + buffer_size )
			{
				buffer_offset = (int) ( off - offset );
				return;
			}
		}
		flushBuffer( );
		offset = off;
		buffer_size = 0;
		buffer_offset = 0;
	}

	/**
	 * return the file position.
	 *
	 * as describe in the seek, the file position may be larger than the file
	 * length
	 *
	 * @return file position
	 * @throws IOException
	 */
	synchronized final public long getPosition( ) throws IOException
	{
		return offset + buffer_offset;
	}

	/**
	 * write the data at the current position.
	 *
	 * the current position is move to next. If the file is opened in read only
	 * mode, the exception is throw out in next flush()
	 *
	 * @param b
	 * @throws IOException
	 */
	synchronized final public void write( int b ) throws IOException
	{
		if ( buffer_offset >= MAX_BUFFER_SIZE )
		{
			flushBuffer( );
		}
		buffer[buffer_offset++] = (byte) b;
		if ( buffer_size < buffer_offset )
		{
			buffer_size = buffer_offset;
		}
		buffer_dirty = true;
	}

	/**
	 * write a integer at the current position.
	 *
	 * the current position is move by 4 bytes. The IO exception is throw out
	 * for next flush() if it is read only.
	 *
	 * @param v
	 * @throws IOException
	 */
	synchronized final public void writeInt( int v ) throws IOException
	{
		if ( buffer_offset + 4 > MAX_BUFFER_SIZE )
		{
			flushBuffer( );
		}
		ArchiveUtil.integerToBytes( v, buffer, buffer_offset );
		buffer_offset += 4;
		if ( buffer_size < buffer_offset )
		{
			buffer_size = buffer_offset;
		}
		buffer_dirty = true;
	}

	/**
	 * write a long at the current position.
	 *
	 * the current position is move by 8 bytes. The IO exception is thrown out
	 * in next flush() for read only file
	 *
	 * @param v
	 * @throws IOException
	 */
	synchronized final public void writeLong( long v ) throws IOException
	{
		if ( buffer_offset + 8 >= MAX_BUFFER_SIZE )
		{
			flushBuffer( );
		}
		ArchiveUtil.longToBytes( v, buffer, buffer_offset );
		buffer_offset += 8;
		if ( buffer_size < buffer_offset )
		{
			buffer_size = buffer_offset;
		}
		buffer_dirty = true;
	}

	/**
	 * write byte[] at the current position.
	 *
	 * the position is moved by len.
	 *
	 * @param b
	 * @param off
	 * @param len
	 * @throws IOException
	 */
	synchronized final public void write( byte[] b, int off, int len )
			throws IOException
	{
		if ( buffer_offset + len <= MAX_BUFFER_SIZE )
		{
			System.arraycopy( b, off, buffer, buffer_offset, len );
			buffer_offset += len;
			if ( buffer_size < buffer_offset )
			{
				buffer_size = buffer_offset;
			}
			buffer_dirty = true;
			return;
		}
		flushBuffer( );
		if ( len < MAX_BUFFER_SIZE )
		{
			System.arraycopy( b, off, buffer, 0, len );
			buffer_offset = len;
			if ( buffer_size < buffer_offset )
			{
				buffer_size = buffer_offset;
			}
		}
		else
		{
			write( offset, b, off, len );
			offset += len;
		}
		buffer_dirty = true;
	}

	synchronized final public int read( ) throws IOException
	{
		if ( buffer_offset >= buffer_size )
		{
			refreshBuffer( 1 );
			if ( buffer_size < 1 )
			{
				return -1;
			}
		}
		return buffer[buffer_offset++] & 0xFF;
	}

	synchronized final public int readInt( ) throws IOException
	{
		if ( buffer_offset + 4 > buffer_size )
		{
			refreshBuffer( 4 );
			if ( buffer_size < 4 )
			{
				throw new EOFException( );
			}
		}
		int v = ArchiveUtil.bytesToInteger( buffer, buffer_offset );
		buffer_offset += 4;
		return v;
	}

	synchronized final public long readLong( ) throws IOException
	{
		if ( buffer_offset + 8 > buffer_size )
		{
			refreshBuffer( 8 );
			if ( buffer_size < 8 )
			{
				throw new EOFException( );
			}
		}
		long v = ArchiveUtil.bytesToLong( buffer, buffer_offset );
		buffer_offset += 8;
		return v;
	}

	synchronized final public int read( byte[] b, int off, int len )
			throws IOException
	{

		// we can load from the buffer
		if ( buffer_offset < buffer_size )
		{
			int size = Math.min( len, buffer_size - buffer_offset );
			System.arraycopy( buffer, buffer_offset, b, off, size );
			buffer_offset += size;
			return size;
		}

		// test if there are more data to be read
		long remainSize = _getLength( ) - offset - buffer_offset;
		if ( remainSize <= 0 )
		{
			return -1;
		}

		// the data is smaller than the buffer size, so first load it into the
		// buffer and read from the buffer
		if ( len < MAX_BUFFER_SIZE )
		{
			// fist try to read from the buffer
			refreshBuffer( 1 );
			int size = Math.min( len, buffer_size - buffer_offset );
			System.arraycopy( buffer, buffer_offset, b, off, size );
			buffer_offset += size;
			return size;
		}

		// the read size is larger than the buffer size, read from the file
		// directly.
		// as the buffer is useless, we need fist save the data and ignore the
		// buffer
		saveBuffer( );
		offset = offset + buffer_offset;
		buffer_size = 0;
		buffer_offset = 0;

		// calculate the max read size
		int readSize = len;
		if ( remainSize < readSize )
		{
			readSize = (int) remainSize;
		}
		// read data from the file directly and ignore the buffer
		int size = read( offset + buffer_offset, b, off, readSize );
		if ( size > 0 )
		{
			offset += size;
		}
		return size;
	}

	/**
	 * ignore the buffer by reset the buffer offset and buffer size
	 *
	 * @throws IOException
	 */
	protected void discardBuffer( ) throws IOException
	{
		offset += buffer_offset;
		buffer_offset = 0;
		buffer_size = 0;
	}

	/**
	 * save the changed buffer into disk
	 *
	 * @throws IOException
	 */
	protected void saveBuffer( ) throws IOException
	{
		if ( buffer_dirty )
		{
			write( offset, buffer, 0, buffer_size );
			buffer_dirty = false;
		}
	}

	/**
	 * flush the data into the buffer and move the buffer to next position.
	 *
	 * @throws IOException
	 */
	protected void flushBuffer( ) throws IOException
	{
		if ( buffer_dirty )
		{
			write( offset, buffer, 0, buffer_size );
			buffer_dirty = false;
		}
		offset += buffer_offset;
		buffer_offset = 0;
		buffer_size = 0;
	}

	protected void refreshBuffer( int minSize ) throws IOException
	{
		if ( buffer_dirty )
		{
			write( offset, buffer, 0, buffer_size );
			buffer_dirty = false;
		}
		offset += buffer_offset;
		buffer_size -= buffer_offset;
		if ( buffer_size > 0 )
		{
			System.arraycopy( buffer, buffer_offset, buffer, 0, buffer_size );
		}
		buffer_offset = 0;

		int readSize = MAX_BUFFER_SIZE - buffer_size;
		long remainSize = _getLength( ) - offset;
		if ( remainSize < readSize )
		{
			readSize = (int) remainSize;
		}
		while ( readSize > 0 )
		{
			int size = read( offset + buffer_size, buffer, buffer_size,
					readSize );
			if ( size < 0 )
			{
				break;
			}
			buffer_size += size;
			readSize -= size;
			if ( buffer_size >= minSize )
			{
				break;
			}
		}
	}
}