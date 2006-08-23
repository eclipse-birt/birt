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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
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
		// Write the temp archive content to the compound file	
		createFileFromFolder( fileArchiveName ); 
	}
	
	public void toFileArchive( RandomAccessFile compoundFile ) throws IOException
	{
		// Write the temp archive content to the compound file	
		createFileFromFolder( compoundFile ); 
	}


	/**
	 * files used to record the reader count reference.
	 */
	static final String READER_COUNT_FILE_NAME = "/.reader.count";
	/**
	 * files which should not be copy into the archives
	 */
	static final String[] SKIP_FILES = new String[]{READER_COUNT_FILE_NAME};

	static boolean needSkip( String file )
	{
		for ( int i = 0; i < SKIP_FILES.length; i++ )
		{
			if ( SKIP_FILES[i].equals( file ) )
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Compound File Format: <br>
	 * 1long(stream section position) + 1long(entry number in lookup map) + lookup map section + stream data section <br>
	 * The Lookup map is a hash map. The key is the relative path of the stram. The entry contains two long number.
	 * The first long is the start postion. The second long is the length of the stream. <br>
	 * @param tempFolder
	 * @param fileArchiveName - the file archive name
	 * @return Whether the compound file was created successfully.
	 */
	private void createFileFromFolder( String fileArchiveName ) throws IOException
	{
		// Create the file
		File targetFile = new File( fileArchiveName );
		ArchiveUtil.DeleteAllFiles( targetFile );	// Delete existing file or folder that has the same name of the file archive.
		RandomAccessFile compoundFile = null;
		compoundFile = new RandomAccessFile( targetFile, "rw" );  //$NON-NLS-1$
		createFileFromFolder(compoundFile);
		compoundFile.close( );
	}
	private void createFileFromFolder( RandomAccessFile compoundFile ) throws IOException
	{
		compoundFile.setLength( 0 );
		compoundFile.seek( 0 );

		compoundFile.writeLong(0); 	// reserve a spot for writing the start position of the stream data section in the file
		compoundFile.writeLong(0);	// reserve a sopt for writing the entry number of the lookup map.

		ArrayList fileList = new ArrayList();
		getAllFiles( new File( folderName ), fileList );	
		
		if ( streamSorter != null )
		{
			ArrayList streamNameList = new ArrayList();
			for ( int i=0; i<fileList.size(); i++ )
			{
				File file = (File)fileList.get(i);
				streamNameList.add( ArchiveUtil.generateRelativePath(folderName, file.getAbsolutePath()) );
			}
					
			ArrayList sortedNameList = streamSorter.sortStream( streamNameList ); // Sort the streams by using the stream sorter (if any).
			
			if ( sortedNameList != null )
			{
				fileList.clear();			
				for ( int i=0; i<sortedNameList.size(); i++ )
				{
					String fileName = ArchiveUtil.generateFullPath( folderName, (String)sortedNameList.get(i) );
					fileList.add( new File(fileName) );
				}
			}			
		}

		// Generate the in-memory lookup map and serialize it to the compound file.
		long streamRelativePosition = 0;
		long entryNum = 0;
		for ( int i=0; i<fileList.size(); i++ )
		{
			File file = (File)fileList.get(i);				
			String relativePath = ArchiveUtil.generateRelativePath( folderName, file.getAbsolutePath() );
			if ( !needSkip( relativePath ) )
			{
				compoundFile.writeUTF( relativePath );
				compoundFile.writeLong( streamRelativePosition );
				compoundFile.writeLong( file.length() );
				
				streamRelativePosition += file.length();
				entryNum++;
			}
		}

		// Write the all of the streams to the stream data section in the compound file
		long streamSectionPos = compoundFile.getFilePointer();
		for ( int i=0; i<fileList.size(); i++ )
		{
			File file = (File)fileList.get(i);
			String relativePath = ArchiveUtil.generateRelativePath( folderName, file.getAbsolutePath() );
			if ( !needSkip( relativePath ) )
			{
				copyFileIntoTheArchive( file, compoundFile );
			}
		}			
		
		// go back and write the start position of the stream data section and the entry number of the lookup map 
		compoundFile.seek( 0 );						 
		compoundFile.writeLong( streamSectionPos );	
		compoundFile.writeLong( entryNum );
	}

	/**
	 * Get all the files under the specified folder (including all the files under sub-folders)
	 * @param dir - the folder to look into
	 * @param fileList - the fileList to be returned
	 */
	private void getAllFiles( File dir, ArrayList fileList )
	{
		if ( dir.exists() &&
			 dir.isDirectory() )
		{
			File[] files = dir.listFiles();
			if ( files == null )
				return;

			for ( int i=0; i<files.length; i++ )
			{
				File file = files[i];
				if ( file.isFile() )
				{
					fileList.add( file );
				}
				else if ( file.isDirectory() )
				{
					getAllFiles( file, fileList );
				}
			}
		}
	}
	
	/**
	 * Copy files from in to out
	 * @param in - input file
	 * @param out - output compound file. Since the input is only part of the file, the compound file output should be be closed by caller. 
	 * @throws Exception
	 */
	private long copyFileIntoTheArchive( File in, RandomAccessFile out ) 
		throws IOException 
	{	
		long totalBytesWritten = 0;
	    FileInputStream fis  = new FileInputStream(in);
	    byte[] buf = new byte[1024 * 5];
	    
	    int i = 0;
	    while( (i=fis.read(buf))!=-1 ) 
	    {
	      out.write(buf, 0, i);
	      totalBytesWritten += i;
	    }
	    fis.close();
	    
	    return totalBytesWritten;
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
		return DocArchiveLockManager.getInstance( ).lock( path );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#unlock(java.lang.Object)
	 */
	public void unlock( Object lock )
	{
		DocArchiveLockManager.getInstance( ).unlock( lock );
	}
}