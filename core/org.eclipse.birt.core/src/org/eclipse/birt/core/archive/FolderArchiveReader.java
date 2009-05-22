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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FolderArchiveReader implements IDocArchiveReader
{

	static Logger logger = Logger.getLogger( FolderArchiveReader.class
			.getName( ) );
	private String folderName;
	private HashSet<RAFolderInputStream> inputStreams = new HashSet<RAFolderInputStream>( );;

	/**
	 * @param folderName -
	 *            the absolute name of the folder archive
	 */
	public FolderArchiveReader( String folderName ) throws IOException
	{
		if ( folderName == null || folderName.length( ) == 0 )
		{
			throw new IOException(
					"The folder archive name is null or empty string." );
		}

		File fd = new File( folderName );
		if ( !fd.isDirectory( ) )
		{
			throw new IOException(
					"The specified name is not a folder name. The FolderArchiveReader is expecting a valid folder archive name." );
		}
		// make sure the folder name is an absolute path
		this.folderName = fd.getCanonicalPath( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#getName()
	 */
	/**
	 * return the folder name as the report archive name
	 */
	public String getName( )
	{
		return folderName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#open()
	 */
	public void open( )
	{
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#close()
	 */
	public void close( ) throws IOException
	{
		IOException exception = null;
		synchronized ( inputStreams )
		{
			ArrayList<RAFolderInputStream> inputs = new ArrayList<RAFolderInputStream>(
					inputStreams );
			for ( RAFolderInputStream in : inputs )
			{
				try
				{
					in.close( );
				}
				catch ( IOException ex )
				{
					if ( exception != null )
					{
						exception = ex;
					}
					logger.log( Level.SEVERE, ex.getMessage( ), ex );
				}
			}
			if ( exception != null )
			{
				throw exception;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#getStream(java.lang.String)
	 */
	public RAInputStream getStream( String relativePath ) throws IOException
	{
		String path = ArchiveUtil.generateFullPath( folderName, relativePath );

		File file = new File( path );
		if ( file.exists( ) )
		{
			return new RAFolderInputStream( inputStreams, file );
		}
		throw new IOException( relativePath + " doesn't exit" );
	}
	
	public RAInputStream getInputStream( String relativePath )
			throws IOException
	{
		return getStream( relativePath );
	}


	public boolean exists( String relativePath )
	{
		String fullPath = ArchiveUtil.generateFullPath( folderName,
				relativePath );
		File fd = new File( fullPath );
		return fd.exists( );
	}

	/**
	 * return a list of strings which are the relative path of streams
	 */
	public List listStreams( String relativeStoragePath ) throws IOException
	{
		ArrayList streamList = new ArrayList( );
		String storagePath = ArchiveUtil.generateFullPath( folderName,
				relativeStoragePath );
		File dir = new File( storagePath );

		if ( dir.exists( ) && dir.isDirectory( ) )
		{
			File[] files = dir.listFiles( );
			if ( files != null )
			{
				for ( int i = 0; i < files.length; i++ )
				{
					File file = files[i];
					if ( file.isFile( ) )
					{
						String relativePath = ArchiveUtil.generateRelativePath(
								folderName, file.getPath( ) );
						if ( !ArchiveUtil.needSkip( relativePath ) )
						{
							streamList.add( relativePath );
						}
					}
				}
			}
		}

		return streamList;
	}

	public List listAllStreams( ) throws IOException
	{
		ArrayList list = new ArrayList( );
		ArchiveUtil.listAllFiles( new File( folderName ), list );

		ArrayList streams = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			File file = (File) list.get( i );
			String relativePath = ArchiveUtil.generateRelativePath( folderName,
					file.getPath( ) );
			if ( !ArchiveUtil.needSkip( relativePath ) )
			{
				streams.add( relativePath );
			}
		}
		return streams;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#lock(java.lang.String)
	 */
	public Object lock( String stream ) throws IOException
	{
		String path = ArchiveUtil.generateFullPath( folderName, stream + ".lck" );
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