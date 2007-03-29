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

package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.List;

public class ArchiveFile implements IArchiveFile
{

	/**
	 * the archive file name.
	 */
	protected String archiveName;

	protected IArchiveFile af;

	public ArchiveFile( String fileName, String mode ) throws IOException
	{
		if ( fileName == null || fileName.length( ) == 0 )
			throw new IOException( "The file name is null or empty string." );

		File fd = new File( fileName );
		// make sure the file name is an absolute path
		fileName = fd.getCanonicalPath( );
		this.archiveName = fileName;

		if ( "r".equals( mode ) )
		{
			openArchiveForReading( );
		}
		else if ( "rw+".equals( mode ) )
		{
			openArchiveForAppending( );
		}
		else
		{
			//rwt, rw mode
			af = new ArchiveFileV2( fileName, mode );
		}
	}

	protected void openArchiveForReading( ) throws IOException
	{
		// test if we need upgrade the document
		RandomAccessFile rf = new RandomAccessFile( archiveName, "r" );
		try
		{
			long magicTag = rf.readLong( );
			if ( magicTag != ArchiveFileV2.DOCUMENT_TAG )
			{
				af = new ArchiveFileV1( archiveName, rf );
			}
			else
			{
				af = new ArchiveFileV2( archiveName, rf, "r" );
			}
		}
		catch ( IOException ex )
		{
			rf.close( );
			throw ex;
		}
	}

	protected void openArchiveForAppending( ) throws IOException
	{
		// we need upgrade the document
		RandomAccessFile rf = new RandomAccessFile( archiveName, "rw" );
		if ( rf.length( ) == 0 )
		{
			// this is a empty file
			af = new ArchiveFileV2( archiveName, rf, "rw" );
		}
		else
		{
			try
			{
				long magicTag = rf.readLong( );
				if ( magicTag == ArchiveFileV2.DOCUMENT_TAG )
				{
					af = new ArchiveFileV2( archiveName, rf, "rw+" );
					return;
				}
				rf.close( );
				upgradeArchiveV1( );
				af = new ArchiveFileV2( archiveName, "rw+" );
				return;
			}
			catch ( IOException ex )
			{
				rf.close( );
				throw ex;
			}
		}
	}

	/**
	 * get the archive name.
	 * 
	 * the archive name is the file name used to create the archive instance.
	 * 
	 * @return archive name.
	 */
	public String getName( )
	{
		return archiveName;
	}

	/**
	 * close the archive.
	 * 
	 * all changed data will be flushed into disk if the file is opened for
	 * write.
	 * 
	 * the file will be removed if it is opend as transient.
	 * 
	 * after close, the instance can't be used any more.
	 * 
	 * @throws IOException
	 */
	public void close( ) throws IOException
	{
		if ( af != null )
		{
			af.close( );
			af = null;
		}
	}

	public void setCacheSize( int cacheSize )
	{
		af.setCacheSize( cacheSize );
	}

	public int getUsedCache( )
	{
		if ( af == null )
		{
			return 0;
		}
		return af.getUsedCache( );
	}

	static public int getTotalUsedCache( )
	{
		return BlockManager.totalPoolSize * BlockManager.BLOCK_SIZE;
	}
	/**
	 * save the
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void saveAs( String fileName ) throws IOException
	{
		ArchiveFileV2 file = new ArchiveFileV2( fileName, "rw" );
		try
		{
			List entries = listEntries( "/" );
			Iterator iter = entries.listIterator( );
			while ( iter.hasNext( ) )
			{
				String name = (String) iter.next( );
				ArchiveEntry tgt = file.createEntry( name );
				ArchiveEntry src = getEntry( name );
				copyEntry( src, tgt );
			}
		}
		finally
		{
			file.close( );
		}
	}

	/**
	 * save the file. If the file is transient file, after saving, it will be
	 * converts to normal file.
	 * 
	 * @throws IOException
	 */
	public void save( ) throws IOException
	{
		if ( af != null )
		{
			if ( af instanceof ArchiveFileV2 )
			{
				( (ArchiveFileV2) af ).save( );
			}
			else
			{
				af.flush( );
			}
		}
	}

	private void copyEntry( ArchiveEntry src, ArchiveEntry tgt )
			throws IOException
	{
		byte[] b = new byte[4096];
		long length = src.getLength( );
		long pos = 0;
		while ( pos < length )
		{
			int size = src.read( pos, b, 0, 4096 );
			tgt.write( pos, b, 0, size );
			pos += size;
		}
	}

	synchronized public void flush( ) throws IOException
	{
		af.flush( );
	}

	synchronized public void refresh( ) throws IOException
	{
		af.refresh( );
	}

	synchronized public boolean exists( String name )
	{
		return af.exists( name );
	}

	synchronized public ArchiveEntry getEntry( String name )
	{
		return af.getEntry( name );
	}

	synchronized public List listEntries( String namePattern )
	{
		return af.listEntries( namePattern );
	}

	synchronized public ArchiveEntry createEntry( String name )
			throws IOException
	{
		return af.createEntry( name );
	}

	synchronized public boolean removeEntry( String name ) throws IOException
	{
		return af.removeEntry( name );
	}

	public Object lockEntry( ArchiveEntry entry ) throws IOException
	{
		return af.lockEntry( entry );
	}

	public void unlockEntry( Object locker ) throws IOException
	{
		af.unlockEntry( locker );
	}

	/**
	 * upgrade the archive file to the latest version
	 * 
	 * @throws IOException
	 */
	private void upgradeArchiveV1( ) throws IOException
	{
		ArchiveFileV1 reader = new ArchiveFileV1( archiveName );
		try
		{
			File tempFile = File.createTempFile( "temp_", ".archive" );
			tempFile.deleteOnExit( );
			ArchiveFile writer = new ArchiveFile( tempFile.getAbsolutePath( ),
					"rwt" );
			List streams = reader.listEntries( "" );
			Iterator iter = streams.iterator( );
			while ( iter.hasNext( ) )
			{
				String name = (String) iter.next( );
				ArchiveEntry src = reader.getEntry( name );
				ArchiveEntry tgt = writer.createEntry( name );
				copyEntry( src, tgt );
			}
			writer.saveAs( archiveName );
			writer.close( );
		}
		finally
		{
			reader.close( );
		}
	}
}
