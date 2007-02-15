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
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class FolderArchiveWriter implements IDocArchiveWriter
{
	private String folderName;
	private IStreamSorter streamSorter = null;
	private LinkedList openStreams = new LinkedList( );
	

	/**
	 * @param absolute fileName the archive file name
	 */
	public FolderArchiveWriter( String folderName ) throws IOException
	{
		if ( folderName == null ||
				folderName.length() == 0 )
			throw new IOException("The folder name is null or empty string.");
		
		File fd = new File( folderName );
		folderName = fd.getCanonicalPath();   // make sure the file name is an absolute path
		this.folderName = folderName;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#initialize()
	 */
	public void initialize() 
	{
		new File(folderName).mkdirs( );
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#createRandomAccessStream(java.lang.String)
	 */
	public RAOutputStream createRandomAccessStream( String relativePath ) throws IOException
	{
		String path = ArchiveUtil.generateFullPath(folderName, relativePath);
		File fd = new File(path);

		ArchiveUtil.createParentFolder(fd);

		RAFolderOutputStream out = new RAFolderOutputStream(this, fd);
		synchronized (openStreams) 
		{
			openStreams.add(out);
		}
		return out;
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

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#getName()
	 */
	public String getName() 
	{
		return folderName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#exists()
	 */
	public boolean exists( String relativePath ) 
	{
		String path = ArchiveUtil.generateFullPath( folderName, relativePath );
		File fd = new File(path);
		return fd.exists();
	}	

	public void setStreamSorter( IStreamSorter streamSorter )
	{
		this.streamSorter = streamSorter;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#finish()
	 */
	public void finish() throws IOException
	{
		closeAllStream( );
	}
	
	/**
	 * Convert the current folder archive to file archive. 
	 * The original folder archive will NOT be removed.
	 * @param fileArchiveName
	 * @throws IOException
	 */
	public void toFileArchive( String fileArchiveName ) throws IOException
	{
		ArchiveUtil.archive( folderName, streamSorter, fileArchiveName );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#flush()
	 */
	public void flush( ) throws IOException
	{
		IOException ioex = null;
		synchronized ( openStreams )
		{
			Iterator iter = openStreams.iterator( );
			while ( iter.hasNext( ) )
			{
				RAFolderOutputStream stream = (RAFolderOutputStream) iter
						.next( );
				if ( stream != null )
				{
					try
					{
						stream.flush( );
					}
					catch ( IOException ex )
					{
						ioex = ex;
					}
				}
			}
		}
		if ( ioex != null )
		{
			throw ioex;
		}
	}
	
	void removeStream(RAFolderOutputStream stream)
	{
		synchronized (openStreams)
		{
			openStreams.remove(stream);
		}
		//remove the stream out from the ouptutStreams.
	}

	protected void closeAllStream()
	{
		synchronized ( openStreams )
		{
			LinkedList streams = new LinkedList(openStreams);
			Iterator iter = streams.iterator( );
			while ( iter.hasNext( ) )
			{
				RAFolderOutputStream stream = (RAFolderOutputStream) iter
						.next( );
				if ( stream != null )
				{
					try
					{
						stream.close( );
					}
					catch ( IOException ex )
					{
					}
				}
			}
			openStreams.clear( );
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