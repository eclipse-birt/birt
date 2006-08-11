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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileArchiveReader implements IDocArchiveReader
{

	private String fileName;
	private String tempFolderName;
	private String readerCountFileName;
	private RandomAccessFile file = null;
	private FolderArchiveReader folderReader = null;
	private HashMap lookupMap = new HashMap( );

	/** 
	 * @param fileName - the absolute name of the file archive
	 */
	public FileArchiveReader( String fileName ) throws IOException
	{
		if ( fileName == null || fileName.length( ) == 0 )
			throw new IOException(
					"The file archive name is null or empty string." );

		File fd = new File( fileName );
		if ( !fd.isFile( ) )
			throw new IOException(
					"The specified name is not a file name. The FileArchiveReader is expecting a valid file archive name." );

		this.fileName = fd.getCanonicalPath( ); // make sure the file name is an absolute path
		this.tempFolderName = fileName + ".tmpfolder";
		this.readerCountFileName = ArchiveUtil.generateFullPath( tempFolderName,
				FolderArchiveWriter.READER_COUNT_FILE_NAME );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#getName()
	 */
	public String getName( )
	{
		return fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#open()
	 */
	public void open( ) throws IOException
	{
		if ( file != null || folderReader != null )
		{
			// has been opend
			return;
		}
		File fd = new File( fileName );
		if ( !fd.isFile( ) )
			throw new IOException(
					"The specified name is not a file name. The FileArchiveReader is expecting a valid file archive name." );
		if ( !fd.exists( ) )
		{
			throw new IOException( "The specified file do not exist." );
		}

		RandomAccessFile lf = new RandomAccessFile( fd, "rw" );
		try
		{
			FileLock lock = lf.getChannel( ).lock( );
			try
			{
				synchronized ( fileName.intern( ) )
				{
					if ( lf.length( ) == 0 )
					{
						//it is the folder archive now
						RandomAccessFile rf = new RandomAccessFile(
						// open the refernce count, increase 1
								readerCountFileName, "rw" );
						try
						{
							int refCount = rf.readInt( );
							refCount++;
							rf.seek( 0 );
							rf.writeInt( refCount );
						}
						finally
						{
							rf.close( );
						}
						// read it as a folder
						folderReader = new FolderArchiveReader( tempFolderName );
						folderReader.open( );
						return;
					}
				}
			}
			finally
			{
				lock.release( );
			}
		}
		finally
		{
			lf.close( );
		}
		
		// restore the in-memory lookup map
		file = new RandomAccessFile( new File( fileName ), "r" ); //$NON-NLS-1$

		long streamSectionPos = file.readLong( );
		long entryNumber = file.readLong( );

		// read lookup map
		for ( long i = 0; i < entryNumber; i++ )
		{
			String relativeFilePath = file.readUTF( );
			long[] positionAndLength = new long[2];
			// stream position (and convert it to absolute position)
			positionAndLength[0] = file.readLong( ) + streamSectionPos; 
			 // stream length
			positionAndLength[1] = file.readLong( );
			// generate map entry
			lookupMap.put( relativeFilePath, positionAndLength ); 
		}
	}

	public void close( ) throws IOException
	{
		if ( folderReader != null )
		{
			folderReader.close( );
			folderReader = null;

			RandomAccessFile lf = new RandomAccessFile( new File( fileName ),
					"rw" );
			try
			{
				FileLock lock = lf.getChannel( ).lock( );
				try
				{
					synchronized ( fileName.intern( ) )
					{
						// open the refernce count, increase 1
						RandomAccessFile rf = new RandomAccessFile(
								readerCountFileName, "rw" );
						int refCount = rf.readInt( );
						refCount--;
						rf.seek( 0 );
						rf.writeInt( refCount );
						rf.close( );
						if ( refCount == 0 )
						{
							ArchiveUtil.DeleteAllFiles( new File(
									tempFolderName ) );
						}
					}
				}
				finally
				{
					lock.release( );
				}
			}
			finally
			{
				lf.close( );
			}
		}
		if ( file != null )
		{
			file.close( );
			file = null;
		}
	}

	public RAInputStream getStream( String relativePath ) throws IOException
	{
		if ( folderReader != null )
		{
			return folderReader.getStream( relativePath );
		}
		
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;

		Object entryValue = lookupMap.get( relativePath );
		if ( entryValue == null )
			return null; // File doesn't exist

		long[] positionAndLength = (long[]) entryValue;

		RAFileInputStream inputStream = new RAFileInputStream( file,
				positionAndLength[0], positionAndLength[0]
						+ positionAndLength[1] );
		return inputStream;
	}

	public boolean exists( String relativePath )
	{
		if ( folderReader != null )
		{
			return folderReader.exists( relativePath );
		}
		
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;

		return ( lookupMap.get( relativePath ) != null );
	}

	/**
	 * reurun a list of strings which are the relative path of streams
	 */
	public List listStreams( String relativeStoragePath ) throws IOException
	{
		if ( folderReader != null )
		{
			return folderReader.listStreams( relativeStoragePath );
		}
		ArrayList streamList = new ArrayList( );
		if ( !relativeStoragePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativeStoragePath = ArchiveUtil.UNIX_SEPERATOR
					+ relativeStoragePath;

		// loop through the lookup map
		Iterator entryIter = lookupMap.entrySet( ).iterator( );
		while ( entryIter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) entryIter.next( );
			String relativePath = (String) entry.getKey( );
			if ( relativePath.startsWith( relativeStoragePath )
					&& !relativePath.equalsIgnoreCase( relativeStoragePath ) )
			{
				String diffString = ArchiveUtil.generateRelativePath(
						relativeStoragePath, relativePath );
				if ( diffString.lastIndexOf( ArchiveUtil.UNIX_SEPERATOR ) == 0 )
				{
					streamList.add( relativePath );
				}
			}
		}

		return streamList;
	}

	/**
	 * Explode the existing compound file archive to a folder that contains corresponding files in it.
	 * NOTE: The original file archive will NOT be deleted. However, if the specified folder archive exists already, its old content will be totally erased first. 
	 * @param folderArchiveName - the name of the folder archive.
	 * @throws IOException
	 */
	public void expandFileArchive( String folderArchiveName ) throws IOException
	{
		assert folderReader == null;
		
		File folder = new File( folderArchiveName );
		folderArchiveName = folder.getCanonicalPath( );
		
		ArchiveUtil.DeleteAllFiles( folder ); // Clean up the folder if it exists.					
		folder.mkdirs(); 					  // Create archive folder
				
		List streamList = getAllStreams();
		for ( int i=0; i<streamList.size(); i++ )
		{
			String streamPath = (String)streamList.get( i );
			RAInputStream in = getStream( streamPath );
			
			String newFileName = ArchiveUtil.generateFullPath( folderArchiveName, streamPath );
			File fd = new File( newFileName );
			ArchiveUtil.createParentFolder( fd );
			FileOutputStream out = new FileOutputStream( fd );
			
			copyFileFromTheArchive( in, out );
		}
	}

	/**
	 * @return the list of all the streams in the file archive. The returnd list contains the stream paths within the file archive.
	 */
	private List getAllStreams()
	{
		ArrayList streamList = new ArrayList( );
		
		// loop through the lookup map
		Iterator entryIter = lookupMap.entrySet( ).iterator( );
		while ( entryIter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) entryIter.next( );
			String relativePath = (String) entry.getKey( );
			streamList.add( relativePath );
		}
		
		return streamList;
	}

	/**
	 * Copy a file from compound FileArchive (RAInputStream) to a regular file (FileOutputStream) 
	 * @param in - RAInputStream in the file archive
	 * @param out - FileOutputStream of the output file
	 * @throws IOException
	 */
	private void copyFileFromTheArchive( RAInputStream in, FileOutputStream out ) throws IOException
	{	
	    byte[] buf = new byte[1024 * 5]; // 5K buffer
	    
	    int i = 0;
	    while( (i=in.read(buf))!=-1 ) 
	    {
	      out.write( buf, 0, i );
	    }
	    
	    in.close();
	    out.close();
	}

}