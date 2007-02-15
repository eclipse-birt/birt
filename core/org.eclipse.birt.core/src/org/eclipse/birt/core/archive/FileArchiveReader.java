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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.compound.ArchiveReader;

/**
 * file based archive reader.
 * It reads multiple streams from a single physical file. the file
 * is created by FileArchiveWriter.
 */
public class FileArchiveReader implements IDocArchiveReader
{
	/**
	 * file name of the archive.
	 */
	private String fileName;
	/**
	 * the file archive reader
	 */
	private ArchiveReader reader;
	/**
	 * random file used to access the file
	 */
	private RandomAccessFile file = null;
	/**
	 * streams in the file. each entry is a stream name and start, end pos pair.
	 */
	private HashMap lookupMap = new HashMap( );

	/** 
	 * @param fileName - the absolute name of the file archive
	 */
	public FileArchiveReader( String fileName ) throws IOException
	{
		if ( fileName == null || fileName.length( ) == 0 )
		{
			throw new IOException(
					"The file archive name is null or empty string." );
		}

		File fd = new File( fileName );
		if ( !fd.isFile( ) )
		{
			throw new IOException(
					"The specified name is not a file name. The FileArchiveReader is expecting a valid file archive name." );
		}
		if ( !fd.exists( ) )
		{
			throw new IOException( "The specified file do not exist." );
		}

		this.fileName = fd.getCanonicalPath( ); // make sure the file name is an absolute path
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
		if ( file != null || reader != null)
		{
			// has been opend
			return;
		}
		// try to open the file as archive file
		try
		{
			reader = new ArchiveReader( fileName );
		}
		catch ( IOException ioex )
		{
			// try to open the file as old format
			file = new RandomAccessFile( new File( fileName ), "r" ); //$NON-NLS-1$
			// restore the in-memory lookup map
			try
			{
				readFileTable( );
			}
			catch ( IOException ex )
			{
				file.close( );
				file = null;
				throw ex;
			}
		}
	}
	

	/**
	 * read the stream table from the archive file.
	 * the stream table is in the begining of the file, it contains:
	 * long: stream section postiton, always zero.
	 * long: entry number.
	 * followed by entries in the archive, each entry contains:
	 * utf8: stream name.
	 * long[2]: start offset, length. 
	 * @throws IOException
	 */
	protected void readFileTable( ) throws IOException
	{
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
		if ( reader != null )
		{
			reader.close( );
			reader = null;
		}
		if ( file != null )
		{
			file.close( );
			file = null;
		}
	}

	public RAInputStream getStream( String relativePath ) throws IOException
	{
		if ( reader != null )
		{
			return reader.getStream( relativePath );
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
		if ( reader != null )
		{
			return reader.exists( relativePath );
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
		if ( reader != null )
		{
			return reader.listStreams( relativeStoragePath );
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
		File folder = new File( folderArchiveName );
		folderArchiveName = folder.getCanonicalPath( );
		
		ArchiveUtil.DeleteAllFiles( folder ); // Clean up the folder if it exists.					
		folder.mkdirs(); 					  // Create archive folder
				
		List streamList = getAllStreams( );
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
		if ( reader != null )
		{
			return reader.getArchive( ).listEntries( "/" );
		}
		
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
	
	public Object lock( String stream ) throws IOException
	{
		if ( reader != null )
		{
			return reader.lock( stream );
		}
		return stream.toString( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#unlock(java.lang.Object)
	 */
	public void unlock( Object lock )
	{
		if ( reader != null )
		{
			reader.unlock( lock );
		}
	}

}