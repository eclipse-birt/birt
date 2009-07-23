/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ArchiveFile implements IArchiveFile, ArchiveConstants
{

	/**
	 * the archive file name.
	 */
	protected String archiveName;
	
	protected String systemId;
	
	protected IArchiveFile af;

	public ArchiveFile( String fileName, String mode ) throws IOException
	{
		// set blank string as the default system id of the archive file.
		this( null, fileName, mode );
	}
	
	public ArchiveFile( String systemId, String fileName, String mode ) throws IOException
	{
		if ( fileName == null || fileName.length( ) == 0 )
			throw new IOException( "The file name is null or empty string." );

		File fd = new File( fileName );
		// make sure the file name is an absolute path
		fileName = fd.getCanonicalPath( );
		this.archiveName = fileName;
		this.systemId = systemId;
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
			af = new ArchiveFileV2( systemId, fileName, mode );
		}
	}

	protected void openArchiveForReading( ) throws IOException
	{
		// test if we need upgrade the document
		RandomAccessFile rf = new RandomAccessFile( archiveName, "r" );
		try
		{
			long magicTag = rf.readLong( );
			if ( magicTag != DOCUMENT_TAG )
			{
				af = new ArchiveFileV1( archiveName, rf );
			}
			else
			{
				ArchiveFileV2 v2 = new ArchiveFileV2( archiveName, rf, "r" );
				upgradeSystemId( v2 );
				af = v2;
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
				if ( magicTag == DOCUMENT_TAG )
				{
					af = new ArchiveFileV2( archiveName, rf, "rw+" );
				}
				else
				{
					rf.close( );
					upgradeArchiveV1( );
					af = new ArchiveFileV2( archiveName, "rw+" );
				}
				upgradeSystemId( af );
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
	
	public String getDependId( )
	{
		return af.getDependId( );
	}

	public String getSystemId( )
	{
		return systemId;
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
		if ( isArchiveFileAvailable( af ) )
		{
			af.close( );
			af = null;
		}
	}

	public void setCacheSize( int cacheSize )
	{
		if ( isArchiveFileAvailable( af ) )
		{
			af.setCacheSize( cacheSize );
		}
	}

	public int getUsedCache( )
	{
		if ( isArchiveFileAvailable( af ) )
		{
			return af.getUsedCache( );
		}
		return 0;
	}

	static public int getTotalUsedCache( )
	{
		return BlockManager.totalCacheSize;
	}
	/**
	 * save the
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void saveAs( String fileName ) throws IOException
	{
		ArchiveFileV2 file = new ArchiveFileV2( this.systemId, fileName, "rw" );
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
		if ( isArchiveFileAvailable( af ) )
		{
			af.save( );
			/*
			if ( af instanceof ArchiveFileV2 )
			{
				( (ArchiveFileV2) af ).save( );
			}
			else
			{
				af.flush( );
			}
			*/
		}
		else
		{
			throw new IOException( "The archive file has been closed. System ID: " + systemId );
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
		if ( isArchiveFileAvailable( af ) )
		{
			af.flush( );
		}
		else
		{
			throw new IOException( "The archive file has been closed. System ID: " + systemId );
		}
	}

	synchronized public void refresh( ) throws IOException
	{
		if ( isArchiveFileAvailable( af ) )
		{
			af.refresh( );
		}
		else
		{
			throw new IOException( "The archive file has been closed. System ID: " + systemId );
		}
	}

	synchronized public boolean exists( String name )
	{
		if ( isArchiveFileAvailable( af ) )
		{
			return af.exists( name );
		}
		return false;
	}

	synchronized public ArchiveEntry getEntry( String name ) throws IOException
	{
		if ( isArchiveFileAvailable( af ) )
		{
			return af.getEntry( name );
		}
		else
		{
			throw new IOException( "Can not get entry named " + name
					+ " because the archive file has been closed. System ID: "
					+ systemId );
		}
	}

	synchronized public List listEntries( String namePattern )
	{
		if ( isArchiveFileAvailable( af ) )
		{
			return af.listEntries( namePattern );
		}
		else
		{
			return Collections.EMPTY_LIST;
		}
	}

	synchronized public ArchiveEntry createEntry( String name )
			throws IOException
	{
		if ( isArchiveFileAvailable( af ) )
		{
			return af.createEntry( name );
		}
		else
		{
			throw new IOException( "Can not create entry named " + name
					+ "because the archive file has been closed. System ID: " + systemId );
		}
	}

	synchronized public boolean removeEntry( String name ) throws IOException
	{
		if ( isArchiveFileAvailable( af ) )
		{
			return af.removeEntry( name );
		}
		else
		{
			throw new IOException( "Can not remove entry named " + name
					+ " because the archive file has been closed. System ID: " + systemId );
		}
	}

	public Object lockEntry( ArchiveEntry entry ) throws IOException
	{
		return af.lockEntry( entry );
	}

	public void unlockEntry( Object locker ) throws IOException
	{
		if ( isArchiveFileAvailable( af ) )
		{
			af.unlockEntry( locker );
		}
		else
		{
			throw new IOException( "The archive file has been closed. System ID: " + systemId );
		}
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
	
	/**
	 * upgrade systemId when open/append the current file
	 * 
	 * @param file
	 */
	private void upgradeSystemId( IArchiveFile file )
	{
		if ( systemId == null )
		{
			systemId = file.getSystemId( );
		}
	}
	
	/**
	 * @param af ArchiveFile
	 * @return whether the ArchiveFile instance is available
	 */
	private boolean isArchiveFileAvailable( IArchiveFile af )
	{
		return af != null;
	}
}
