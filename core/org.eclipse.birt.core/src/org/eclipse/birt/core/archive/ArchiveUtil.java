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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ArchiveUtil
{
	// We need this because the report document should be platform neutual. Here we define the neutual is the unix seperator.
	public static String UNIX_SEPERATOR = "/";
	
	/**
	 * @param rootPath - the absolute path of the root folder. The path is seperated by system's File seperator. 
	 * @param relativePath - the relative path. The path is either seperated by system's File seperator or seperated by Unix seperator "/".
	 * @return the absolute path which concats rootPath and relativePath. The full path is seperated by system's File seperator. The returned absolute path can be used directly to locate the file.  
	 */
	public static String generateFullPath( String rootPath, String relativePath )
	{
		relativePath = convertToSystemString( relativePath );
		
		if ( rootPath != null )
		{
			if ( !rootPath.endsWith(File.separator) )
				rootPath += File.separator;
			
			if ( relativePath.startsWith(File.separator) )
				relativePath = relativePath.substring( 1 );
			
			return rootPath + relativePath;
		}
		
		return relativePath;
	}

	/**
	 * @param rootPath - the absolute path of the root folder. The path is seperated by system's File seperator.
	 * @param fullString - the absolute path of the stream. The path is seperated by system's File seperator.
	 * @return the relative path string. The path is based on Unix syntax and starts with "/". 
	 */
	public static String generateRelativePath( String rootPath, String fullPath )
	{
		String relativePath = null;
		
		if ( (rootPath != null) &&
			 fullPath.startsWith(rootPath) )
		{
			relativePath = fullPath.substring( rootPath.length() );
		}
		else
			relativePath = fullPath;
				
		relativePath = convertToUnixString( relativePath );
		
		if ( !relativePath.startsWith(UNIX_SEPERATOR) )
			relativePath = UNIX_SEPERATOR + relativePath;

		
		return relativePath;
	}

	/**
	 * @param path - the path that could be in system format (seperated by File.seperator) or Unix format (seperated by "/").
	 * @return the path that is in Unix format.
	 */
	private static String convertToUnixString( String path )
	{
		if ( path == null )
			return null;
		
		return path.replace( File.separator.charAt(0), UNIX_SEPERATOR.charAt(0) );
	}

	/**
	 * @param path - the path that could be in system format (seperated by File.seperator) or Unix format (seperated by "/").
	 * @return the path that is in the system format.
	 */
	private static String convertToSystemString( String path )
	{
		if ( path == null )
			return null;
		
		return path.replace( UNIX_SEPERATOR.charAt(0), File.separator.charAt(0) );
	}


	public static void DeleteAllFiles( File dirOrFile )
	{	
		if ( !dirOrFile.exists() )
			return;
		
		if ( dirOrFile.isFile() )
		{
			dirOrFile.delete();
		}
		else // dirOrFile is directory
		{
			if ( (dirOrFile.listFiles() != null) &&
				 (dirOrFile.listFiles().length > 0) )
			{
				File[] fileList = dirOrFile.listFiles();
				for ( int i=0; i<fileList.length; i++ )
					DeleteAllFiles( fileList[i] );
			}
	
			// Directory can only be deleted when it is empty.
			dirOrFile.delete();
		}
	}
	
	public static void zipFolderToStream( String tempFolderPath, OutputStream ostream ) 
	{
		ZipOutputStream zipOutput = new ZipOutputStream( ostream );
		File rootDir = new File( tempFolderPath );
		File[] files = rootDir.listFiles();
		
		try {
			zipFiles( zipOutput, files, tempFolderPath );
			zipOutput.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Utility funtion to write files/directories to a ZipOutputStream. For directories, all the files and subfolders are written recursively.
	 */
	private static void zipFiles( ZipOutputStream zipOut, File[] files, String tempFolderPath )
		throws FileNotFoundException, IOException
	{
		if ( files == null )
			return;
		
		for ( int i = 0; i < files.length; i++ ) 
		{
			File file = files[i];			
			if ( file.isDirectory() ) 
			{	// if file is a directory, get child files and recursively call this method
				File[] dirFiles = file.listFiles();
				zipFiles( zipOut, dirFiles, tempFolderPath );
			}
			else 
			{ 	// if file is a file, create a new ZipEntry and write out the file.	
				BufferedInputStream in = new BufferedInputStream( new FileInputStream(file) );
				String relativePath = generateRelativePath( tempFolderPath, file.getPath() );
				ZipEntry entry = new ZipEntry( relativePath );
				entry.setTime( file.lastModified() );				
				zipOut.putNextEntry( entry ); // Create a new zipEntry

				int len;
				byte[] buf = new byte[1024 * 5];
				while ( (len = in.read(buf)) > 0 ) 
				{
					zipOut.write( buf, 0, len );
				}
				
				in.close();
				zipOut.closeEntry();
			}
		} // end of for ( int i = 0; i < files.length; i++ )
	}
	
	public static void unzipArchive( File zipArchive, String tempFolderPath )
	{
		try 
		{
			ZipFile zipFile = new ZipFile( zipArchive );
			
		    Enumeration entries = zipFile.entries();
		    while(entries.hasMoreElements()) 
		    {
				ZipEntry entry = (ZipEntry)entries.nextElement();
				if( entry.isDirectory() ) 
				{ 	// Assume directories are stored parents first then children.
					String dirName = generateFullPath( tempFolderPath, entry.getName() );
					// TODO: handle the error case where the folder can not be created!
					File dir = new File(dirName);
					dir.mkdirs();
				}
				else
				{
					InputStream in = zipFile.getInputStream(entry);
					File file = new File( generateFullPath(tempFolderPath, entry.getName()) );
					
					File dir = new File( file.getParent() );
					if (dir.exists( )) 
					{
						assert (dir.isDirectory( ));
					} else 
					{
						dir.mkdirs( );
					}
						
					BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream(file) );					

				    int len;
					byte[] buf = new byte[1024 * 5];
					while( (len = in.read(buf)) > 0 )
					{
						out.write(buf, 0, len);
					}
					in.close();
					out.close();
					
				}
		    }
		    zipFile.close();

		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}