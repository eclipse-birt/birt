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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileArchiveWriter implements IDocArchiveWriter
{
	private String fileName;
	private String tempFolder; 			

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
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss"); //$NON-NLS-1$
		String dateTimeString = df.format( new Date() );
		tempFolder = fileName + "_" + dateTimeString; //$NON-NLS-1$

		File archiveRootFolder = new File( tempFolder );
		
		if ( archiveRootFolder.exists() )
			ArchiveUtil.DeleteAllFiles( archiveRootFolder );
		
		// Create archive folder			
		archiveRootFolder.mkdirs();		
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
		String path = ArchiveUtil.generateFullPath( tempFolder, relativePath );
		File fd = new File(path);

		// If the parent folder of the stream doesn't exsit, create the parent folder.
		if ( fd != null && 
			 fd.getParentFile() != null &&
			 fd.getParentFile().exists() == false ) 
		{
			fd.getParentFile().mkdirs();
		}
		
		RandomAccessFile file = null;
		file = new RandomAccessFile( fd, "rw" ); //$NON-NLS-1$
		
		RAFileOutputStream out = new RAFileOutputStream( file, 0 ); 
		return out;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#getName()
	 */
	public String getName() {
		return fileName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#exists()
	 */
	public boolean exists( String relativePath ) 
	{
		String path = ArchiveUtil.generateFullPath( tempFolder, relativePath );
		File fd = new File(path);
		return fd.exists();
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#finish()
	 */
	public void finish() throws IOException
	{
		// Write the temp archive content to the compound file	
		createFileFromFolder( ); 
		
		// Delete the temp archive folder
		File tempArchiveFolder = new File( tempFolder );
		ArchiveUtil.DeleteAllFiles( tempArchiveFolder );		
	}

	/**
	 * Compound File Format: <br>
	 * 1long(stream section position) + 1long(entry number in lookup map) + lookup map section + stream data section <br>
	 * The Lookup map is a hash map. The key is the relative path of the stram. The entry contains two long number.
	 * The first long is the start postion. The second long is the length of the stream. <br>
	 * @param tempFolder
	 * @param fileName
	 * @return Whether the compound file was created successfully.
	 */
	private void createFileFromFolder( ) throws IOException
	{
		// Create the file
		File targetFile = new File( fileName );
		ArchiveUtil.DeleteAllFiles( targetFile );	
		RandomAccessFile compoundFile = null;
		compoundFile = new RandomAccessFile( targetFile, "rw" );  //$NON-NLS-1$

		compoundFile.writeLong(0); 	// reserve a spot for writing the start position of the stream data section in the file
		compoundFile.writeLong(0);	// reserve a sopt for writing the entry number of the lookup map.

		ArrayList fileList = new ArrayList();
		getAllFiles( new File( tempFolder ), fileList );

		// Generate the in-memory lookup map and serialize it to the compound file.
		long streamRelativePosition = 0;
		long entryNum = 0;
		for ( int i=0; i<fileList.size(); i++ )
		{
			File file = (File)fileList.get(i);				
			String relativePath = ArchiveUtil.generateRelativePath( tempFolder, file.getAbsolutePath() );
			
			compoundFile.writeUTF( relativePath );
			compoundFile.writeLong( streamRelativePosition );
			compoundFile.writeLong( file.length() );
			
			streamRelativePosition += file.length();
			entryNum++;
		}

		// Write the all of the streams to the stream data section in the compound file
		long streamSectionPos = compoundFile.getFilePointer();
		for ( int i=0; i<fileList.size(); i++ )
		{
			File file = (File)fileList.get(i);
			copyFileIntoTheArchive( file, compoundFile );
		}			
		
		// go back and write the start position of the stream data section and the entry number of the lookup map 
		compoundFile.seek( 0 );						 
		compoundFile.writeLong( streamSectionPos );	
		compoundFile.writeLong( entryNum );

		// close the file
		compoundFile.close();		
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
	 * @param out - output file
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

}