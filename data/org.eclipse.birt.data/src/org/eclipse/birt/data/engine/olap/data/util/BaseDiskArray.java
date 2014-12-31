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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.DataEngineThreadLocal;
import org.eclipse.birt.data.engine.core.security.FileSecurity;

/**
 * 
 */

abstract class BaseDiskArray implements IDiskArray
{

	private static final String fileNamePrefix = "BasicDiskList";
	private static final int FILE_BUFFER_SIZE = 1024;
	private static final int bufferSize = 1;
	
	protected static final short NORMAL_VALUE = 0;
	protected static final short NULL_VALUE = -1;
	
	private static Logger logger = Logger.getLogger( BaseDiskArray.class.getName( ) );

	private int currentCacheStartIndex;
	private int size;

	private File diskFile = null;
	protected BufferedRandomAccessFile randomAccessFile = null;

	private Object[] buffer = null;

	private List<Long> segmentOffsets = null;

	/**
	 * @throws IOException
	 * 
	 * 
	 */
	public BaseDiskArray( ) throws IOException
	{
		this.currentCacheStartIndex = 0;
		this.size = 0;

		this.buffer = new Object[bufferSize];
		this.segmentOffsets = new ArrayList<Long>( );
		this.segmentOffsets.add( Long.valueOf( 0L ) );
//		createRandomAccessFile( );
		DataEngineThreadLocal.getInstance( ).getCloseListener( ).add( this );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IDiskArray#add(java.lang.Object)
	 */
	public boolean add( Object o ) throws IOException
	{
		if ( size >= currentCacheStartIndex
				&& size < ( currentCacheStartIndex + bufferSize ) )
		{
			buffer[size-currentCacheStartIndex] = o;
		}
		writeObject( o );
		size++;
		if ( size % bufferSize == 0 )
		{
			segmentOffsets.add( getOffset( ) );
		}
		return true;
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void createRandomAccessFile( ) throws IOException
	{
		createCacheFile( );
		randomAccessFile = new BufferedRandomAccessFile( diskFile,
				"rw",
				FILE_BUFFER_SIZE );
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private long getOffset( ) throws IOException
	{
		return getRandomAccessFile( ).getFilePointer( );
	}

	/**
	 * Write a object to disk
	 * 
	 * @param oos
	 * @param object
	 * @throws IOException
	 */
	protected abstract void writeObject( Object object ) throws IOException;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IDiskArray#get(int)
	 */
	public Object get( int index ) throws IOException
	{
		RangeCheck( index );
		if ( index < currentCacheStartIndex
				|| index > ( currentCacheStartIndex + bufferSize - 1 ) )
		{
			int readSize = bufferSize;
			if ( ( index / bufferSize ) == segmentOffsets.size( ) - 1 )
			{
				readSize = size % bufferSize;
			}
			currentCacheStartIndex = ( index / bufferSize ) * bufferSize;
			readObjects( getSegmentOffset( index ), readSize );
		}
		return buffer[index % bufferSize];

	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	private long getSegmentOffset( int index )
	{
		return this.segmentOffsets.get( index / bufferSize ).longValue( );
	}

	/**
	 * Reads up to <code>readSize</code> objects from disk.
	 * 
	 * @param dis
	 * @param list
	 * @return
	 * @throws IOException
	 */
	private void readObjects( long offset, int readSize )
			throws IOException
	{
		getRandomAccessFile( ).seek( offset );
		for ( int i = 0; i < readSize; i++ )
		{
			this.buffer[i] = readObject( );
		}
	}

	/**
	 * Read one object from disk.
	 * 
	 * @param oos
	 * @param object
	 * @throws IOException
	 */
	protected abstract Object readObject( ) throws IOException;

	/**
	 * Create a file for caching objects.
	 * 
	 * @param cacheIndex
	 * @return
	 * @throws IOException
	 */
	private void createCacheFile( ) throws IOException
	{
		String tempFileStr = DataEngineThreadLocal.getInstance( )
				.getPathManager( )
				.getTempFileName( fileNamePrefix, this.hashCode( ), "" );
		if ( diskFile == null )
		{
			diskFile = new File( tempFileStr );
			FileSecurity.createNewFile( diskFile );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IDiskArray#size()
	 */
	public int size( )
	{
		return this.size;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.IDiskArray#close()
	 */
	public void close( ) throws IOException
	{
		if ( randomAccessFile != null )
		{
			randomAccessFile.close( );
			randomAccessFile = null;
		}
		clearDiskFile( );
		this.size = 0;
		this.buffer = null;
	}

	/**
	 * Check if the given index is in range. If not, throw an
	 * ArrayIndexOutOfBoundsException.
	 */
	private void RangeCheck( int index )
	{
		if ( index >= size )
			throw new IndexOutOfBoundsException( "Index: "
					+ index + ", Size: " + size );
	}

	/**
	 * Delete the used disk file;
	 * 
	 * @throws IOException
	 * 
	 */
	private void clearDiskFile( ) throws IOException
	{
		if ( randomAccessFile != null )
		{
			randomAccessFile.close( );
			randomAccessFile = null;
		}
		if ( diskFile != null )
		{
			FileSecurity.fileDelete( diskFile );
			diskFile = null;
		}
	}

//	public void finalize( )
//	{
//		try
//		{
//			clearDiskFile( );
//		}
//		catch ( IOException e )
//		{
//			logger.log( Level.SEVERE, e.toString( ), e );
//		}
//	}

	public void clear( ) throws IOException
	{
		this.currentCacheStartIndex = -1;
		this.size = 0;
		clearDiskFile( );
		this.segmentOffsets.clear( );
		this.segmentOffsets.add( Long.valueOf( 0L ) );
		createRandomAccessFile( );
	}

	
	public BufferedRandomAccessFile getRandomAccessFile( ) throws IOException
	{
		if(randomAccessFile == null)
		{
			createRandomAccessFile( );
		}
		return randomAccessFile;
	}
	
	
}
