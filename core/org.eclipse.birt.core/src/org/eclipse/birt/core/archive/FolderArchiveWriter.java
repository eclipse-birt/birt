/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FolderArchiveWriter implements IDocArchiveWriter
{

	private static Logger logger = Logger.getLogger( FolderArchiveWriter.class
			.getName( ) );
	private String folderName;
	private IStreamSorter streamSorter = null;
	private HashSet<RAFolderInputStream> inputStreams = new HashSet<RAFolderInputStream>( );
	private HashSet<RAFolderOutputStream> outputStreams = new HashSet<RAFolderOutputStream>( );

	/**
	 * @param absolute
	 *            fileName the archive file name
	 */
	public FolderArchiveWriter( String folderName ) throws IOException
	{
		if ( folderName == null || folderName.length( ) == 0 )
			throw new IOException( "The folder name is null or empty string." );

		File fd = new File( folderName );
		if ( !fd.exists( ) )
		{
			fd.mkdirs( );
		}
		this.folderName = fd.getCanonicalPath( ); // make sure the file name is an
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#initialize()
	 */
	public void initialize( )
	{
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#createRandomAccessStream(java.lang.String)
	 */
	public RAOutputStream createRandomAccessStream( String relativePath )
			throws IOException
	{
		String path = ArchiveUtil.generateFullPath( folderName, relativePath );
		File fd = new File( path );

		ArchiveUtil.createParentFolder( fd );

		RAFolderOutputStream out = new RAFolderOutputStream( outputStreams, fd );
		return out;
	}

	public RAOutputStream openRandomAccessStream( String relativePath )
			throws IOException
	{
		String path = ArchiveUtil.generateFullPath( folderName, relativePath );
		File fd = new File( path );

		ArchiveUtil.createParentFolder( fd );
		RAFolderOutputStream out = new RAFolderOutputStream( outputStreams, fd,
				true );
		return out;
	}

	public RAOutputStream createOutputStream( String relativePath )
			throws IOException
	{
		return createRandomAccessStream( relativePath );
	}

	public RAOutputStream getOutputStream( String relativePath )
			throws IOException
	{
		return openRandomAccessStream( relativePath );
	}

	public RAInputStream getInputStream( String relativePath )
			throws IOException
	{
		String path = ArchiveUtil.generateFullPath( folderName, relativePath );

		File file = new File( path );
		if ( file.exists( ) )
		{
			RAFolderInputStream in = new RAFolderInputStream( inputStreams,
					file );
			return in;
		}
		throw new IOException( relativePath + " doesn't exit" );
	}

	/**
	 * Delete a stream from the archive and make sure the stream has been
	 * closed.
	 * 
	 * @param relativePath -
	 *            the relative path of the stream
	 * @return whether the delete operation was successful
	 * @throws IOException
	 */
	public boolean dropStream( String relativePath )
	{
		String path = ArchiveUtil.generateFullPath( folderName, relativePath );
		File fd = new File( path );
		return removeFileAndFolder( fd );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#getName()
	 */
	public String getName( )
	{
		return folderName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#exists()
	 */
	public boolean exists( String relativePath )
	{
		String path = ArchiveUtil.generateFullPath( folderName, relativePath );
		File fd = new File( path );
		return fd.exists( );
	}

	public void setStreamSorter( IStreamSorter streamSorter )
	{
		this.streamSorter = streamSorter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#finish()
	 */
	public void finish( ) throws IOException
	{
		close( );
	}

	public void close( ) throws IOException
	{
		IOException exception = null;
		synchronized ( outputStreams )
		{
			ArrayList<RAFolderOutputStream> outputs = new ArrayList<RAFolderOutputStream>(
					outputStreams );
			for ( RAFolderOutputStream output : outputs )
			{
				try
				{
					output.close( );
				}
				catch ( IOException ex )
				{
					logger.log(Level.SEVERE, ex.getMessage( ), ex);
					if ( exception != null )
					{
						exception = ex;
					}
				}
			}
			outputStreams.clear( );
		}
		synchronized ( inputStreams )
		{
			ArrayList<RAFolderInputStream> inputs = new ArrayList<RAFolderInputStream>(
					inputStreams );
			for ( RAFolderInputStream input : inputs )
			{
				try
				{
					input.close( );
				}
				catch ( IOException ex )
				{
					logger.log( Level.SEVERE, ex.getMessage( ), ex );
					if ( exception != null )
					{
						exception = ex;
					}
				}
			}
			inputStreams.clear( );
		}
		if ( exception != null )
		{
			throw exception;
		}
	}

	/**
	 * Convert the current folder archive to file archive. The original folder
	 * archive will NOT be removed.
	 * 
	 * @param fileArchiveName
	 * @throws IOException
	 */
	public void toFileArchive( String fileArchiveName ) throws IOException
	{
		ArchiveUtil.archive( folderName, streamSorter, fileArchiveName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#flush()
	 */
	public void flush( ) throws IOException
	{
		IOException ioex = null;
		synchronized ( inputStreams )
		{
			for ( RAOutputStream output : outputStreams )
			{
				output.flush( );
			}
		}
		if ( ioex != null )
		{
			throw ioex;
		}
	}

	/**
	 * delete file or folder with its sub-folders and sub-files
	 * 
	 * @param file
	 *            file/folder which need to be deleted
	 * @return if files/folders can not be deleted, return false, or true
	 */
	private boolean removeFileAndFolder( File file )
	{
		assert ( file != null );
		if ( file.isDirectory( ) )
		{
			File[] children = file.listFiles( );
			if ( children != null )
			{
				for ( int i = 0; i < children.length; i++ )
				{
					removeFileAndFolder( children[i] );
				}
			}
		}
		if ( file.exists( ) )
		{
			return file.delete( );
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#lock(java.lang.String)
	 */
	public Object lock( String stream ) throws IOException
	{
		String path = ArchiveUtil
				.generateFullPath( folderName, stream + ".lck" );
		IArchiveLockManager lockManager = ArchiveLockManager.getInstance( );
		return lockManager.lock( path );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#unlock(java.lang.Object)
	 */
	public void unlock( Object lock )
	{
		IArchiveLockManager lockManager = ArchiveLockManager.getInstance( );
		lockManager.unlock( lock );
	}
}