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
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.Iterator;
import java.util.LinkedList;

public class FileArchiveWriter implements IDocArchiveWriter
{
	private String fileName;
	private String tempFolderName;
	private String countFileName;
	private FolderArchiveWriter folderWriter; 			
	private LinkedList openStreams = new LinkedList( );

	/**
	 * @param absolute fileName the archive file name
	 */
	public FileArchiveWriter( String fileName ) throws IOException
	{
		if ( fileName == null ||
			 fileName.length() == 0 )
			throw new IOException("The file name is null or empty string.");
		
		File fd = new File( fileName );
		fileName = fd.getCanonicalPath();   // make sure the file name is an absolute path
		this.fileName = fileName;
		this.tempFolderName = fileName + ".tmpfolder";
		this.countFileName = ArchiveUtil.generateFullPath( tempFolderName,
				FolderArchiveWriter.READER_COUNT_FILE_NAME );
		
		// try to create an empty file, if failed that means
		// some the file has been opened, throw out an exception.
		RandomAccessFile rf = new RandomAccessFile( fileName, "rw" );
		// try to lock the file
		try
		{
			FileLock lock = rf.getChannel( ).lock( );
			// syncrhonize the lock among mutiple thread
			try
			{
				synchronized ( fileName.intern( ) )
				{
					rf.setLength( 0 );
					// try to remove the temp folder
					File archiveRootFolder = new File( tempFolderName );
					if ( archiveRootFolder.exists( ) )
					{
						ArchiveUtil.DeleteAllFiles( archiveRootFolder );
					}
					if ( archiveRootFolder.exists( ) )
					{
						throw new IOException(
								"archive root folder can't be removed" );
					}
					// Create archive folder
					archiveRootFolder.mkdirs( );
					// create an ref count in the archive root folder
					RandomAccessFile cf = new RandomAccessFile( countFileName,
							"rw" );
					try
					{
						cf.writeInt( 1 );
					}
					finally
					{
						cf.close( );
					}
					folderWriter = new FolderArchiveWriter( tempFolderName );
				}
			}
			finally
			{
				lock.release( );
			}
		}
		finally
		{
			rf.close( );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#initialize()
	 */
	public void initialize() 
	{
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#createRandomAccessStream(java.lang.String)
	 */
	public RAOutputStream createRandomAccessStream( String relativePath ) throws IOException
	{
		RAOutputStream raOutputStream = folderWriter
				.createRandomAccessStream( relativePath );
		synchronized ( openStreams )
		{
			openStreams.add( raOutputStream );
		}
		return raOutputStream;
	}
	
	/**
	 * Delete a stream from the archive. NOTE: FileArchiveWriter doesn't support this function and always returns false; 
	 * @param relativePath - the relative path of the stream
	 * @return whether the operation was successful
	 * @throws IOException
	 */
	public boolean dropStream( String relativePath )
	{
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#getName()
	 */
	public String getName() 
	{
		return fileName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#exists()
	 */
	public boolean exists( String relativePath ) 
	{
		return folderWriter.exists( relativePath );
	}	

	public void setStreamSorter( IStreamSorter streamSorter )
	{
		folderWriter.setStreamSorter( streamSorter );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#finish()
	 */
	public void finish() throws IOException
	{
		if ( folderWriter != null )
		{
			// try to remove the temp folders
			RandomAccessFile rf = new RandomAccessFile( fileName, "rw" );
			try
			{
				FileLock locker = rf.getChannel( ).lock( );
				try
				{
					synchronized ( fileName.intern( ) )
					{
						closeAllStream( );
						folderWriter.finish( );
						folderWriter.toFileArchive( rf );
						folderWriter = null;
						
						RandomAccessFile readerCountFile = new RandomAccessFile(
								countFileName, "rw" );;
						int readerCount = readerCountFile.readInt( );
						readerCount--;
						readerCountFile.seek( 0 );
						readerCountFile.writeInt( readerCount );
						readerCountFile.close( );
						if ( readerCount == 0 )
						{
							ArchiveUtil.DeleteAllFiles( new File(
									tempFolderName ) );
						}
					}
				}
				finally
				{
					locker.release( );
				}
			}
			finally
			{
				rf.close( );
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
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
	
	protected void closeAllStream()
	{
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
}