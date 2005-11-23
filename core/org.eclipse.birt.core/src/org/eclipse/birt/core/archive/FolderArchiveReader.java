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
import java.util.ArrayList;
import java.util.List;

public class FolderArchiveReader implements IDocumentArchiveReader
{
	private String archiveFolderPath; 			// including the archive name 

	public FolderArchiveReader( String archiveFolderPath )
	{
		this.archiveFolderPath = archiveFolderPath;
	}
	
	public String getRootPath()
	{
		return archiveFolderPath;
	}

	public void open() {
		// Do nothing
	}

	public void close() {
		// Do nothing		
	}

	public File getStream( String relativePath )
	{
		String path = ArchiveUtil.generateFullPath( archiveFolderPath, relativePath );
		File file = new File(path);
		
		if ( file.exists() )
			return file;
		else
			return null;
	}

	public RandomAccessFile getRandomAccessStream( String relativePath ) 
	{
		File file = getStream( relativePath );
		
		if ( file != null )
		{		
			try 
			{
				RandomAccessFile randomFile = new RandomAccessFile( file, "r" ); //$NON-NLS-1$
				return randomFile;
			} 
			catch (FileNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}

	public boolean exists( String relativePath ) 
	{
		File file = new File( ArchiveUtil.generateFullPath(archiveFolderPath, relativePath) );
		return file.exists();
	}

	/**
	 * reurun a list of strings which are the relative path of streams
	 */
	public List listStreams( String relativeStoragePath )
	{		
		String storagePath = ArchiveUtil.generateFullPath( archiveFolderPath, relativeStoragePath );
		File dir = new File( storagePath );
		
		if ( dir.exists() &&
			 dir.isDirectory() )
		{
			File[] files = dir.listFiles();
			if ( files == null )
				return null;

			ArrayList streamList = new ArrayList();
			for ( int i=0; i<files.length; i++ )
			{
				File file = files[i];
				if ( file.isFile() )
				{
					String relativePath = ArchiveUtil.generateRelativePath( archiveFolderPath, file.getPath() );
					streamList.add( relativePath );
				}
			}

			return streamList;
		}
		
		return null;
	}

}