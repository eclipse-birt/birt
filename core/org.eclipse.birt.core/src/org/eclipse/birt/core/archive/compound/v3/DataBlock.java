/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound.v3;

import java.io.IOException;

public class DataBlock extends Ext2Block
{

	private byte[] buffer;
	private boolean dirty;

	DataBlock( Ext2FileSystem fs )
	{
		this( fs, -1 );
	}

	DataBlock( Ext2FileSystem fs, int blockId )
	{
		super( fs, blockId );
		this.buffer = new byte[BLOCK_SIZE];
		this.dirty = false;
	}

	public synchronized int write( int tgt, byte b[], int off, int len )
			throws IOException
	{
		assert ( b != null );
		assert ( tgt + len < buffer.length );
		assert ( off + len < b.length );

		if ( len > 0 )
		{
			System.arraycopy( b, off, buffer, tgt, len );
			dirty = true;
		}
		return len;
	}

	public synchronized int read( int src, byte b[], int off, int len )
			throws IOException
	{
		assert ( b != null );
		assert ( off + len < b.length );
		assert ( src + len < buffer.length );
		System.arraycopy( buffer, src, b, off, len );
		return len;
	}

	public void refresh( ) throws IOException
	{
		assert blockId != -1;
		fs.readBlock( blockId, buffer, 0, BLOCK_SIZE );
		dirty = false;
	}

	public void flush( ) throws IOException
	{
		if ( blockId == -1 )
		{
			throw new IllegalStateException(
					"Must assign the block id before flush" );
		}
		if ( dirty )
		{
			fs.writeBlock( blockId, buffer, 0, BLOCK_SIZE );
			dirty = false;
		}
	}

	static final DataBlock READ_ONLY_BLOCK = new ReadOnlyBlock( );

	private static class ReadOnlyBlock extends DataBlock
	{

		ReadOnlyBlock( )
		{
			super( null, -1 );
		}

		public int write( int tgt, byte b[], int off, int len )
				throws IOException
		{
			throw new IOException( "Can't change the read only buffer" );
		}

		public int read( int src, byte b[], int off, int len )
				throws IOException
		{
			assert ( b != null );
			assert ( off + len < b.length );
			for ( int i = 0; i < len; i++ )
			{
				b[off + i] = 0;
			}
			return len;
		}

		public void refresh( ) throws IOException
		{
		}

		public void flush( ) throws IOException
		{
		}
	}
}
