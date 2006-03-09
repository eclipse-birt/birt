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

public class FileArchiveWriter implements IDocArchiveWriter
{
	private String fileName;
	private FolderArchiveWriter folderWriter; 			

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
		
		String tempFolder = ArchiveUtil.generateUniqueFileFolderName( fileName );		
		File archiveRootFolder = new File( tempFolder );	
		assert ( !archiveRootFolder.exists() );		
		
		// Create archive folder			
		archiveRootFolder.mkdirs();	
		
		folderWriter = new FolderArchiveWriter( tempFolder );
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
		return folderWriter.createRandomAccessStream( relativePath );
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
		folderWriter.toFileArchive( fileName );
	}

}