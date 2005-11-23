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
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class FolderArchiveWriter implements IDocumentArchiveWriter
{
	private String archiveFolderPath; 			// including the archive name
	
	public FolderArchiveWriter( String archiveFolderPath )
	{
		this.archiveFolderPath = archiveFolderPath;
	}
	
	public String getRootPath()
	{
		return archiveFolderPath;
	}

	public void initialize() 
	{
		File archiveRootFolder = new File( archiveFolderPath );
		
		if ( archiveRootFolder.exists() )
			ArchiveUtil.DeleteAllFiles( archiveRootFolder );
		
		// Create archive folder			
		archiveRootFolder.mkdirs();		
	}

	public void finish() 
	{
		// Do nothing
	}

	public File createStream( String relativePath )
	{
		String path = ArchiveUtil.generateFullPath( archiveFolderPath, relativePath );
		File fd = new File(path);

		// If the parent folder of the stream doesn't exsit, create the parent folder.
		if ( fd != null && 
			 fd.getParentFile() != null &&
			 fd.getParentFile().exists() == false ) 
		{
			fd.getParentFile().mkdirs();
		}
		
		return fd;
	}

	public RandomAccessFile createRandomAccessStream(String path) 
	{
		File file = createStream( path );
		
		if ( file != null )
		{		
			try {
				RandomAccessFile randomFile = new RandomAccessFile( file, "rw" ); //$NON-NLS-1$
				return randomFile;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}		
		return null;
	}

}