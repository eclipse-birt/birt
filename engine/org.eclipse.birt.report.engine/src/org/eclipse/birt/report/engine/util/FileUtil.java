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

package org.eclipse.birt.report.engine.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Collection of file utility.
 * 
 * @version $Revision: #1 $ $Date: 2005/01/21 $
 */
public class FileUtil
{

	/**
	 * The default prefix for creating temporarily file.
	 */
	private static String DEFAULT_PREFIX = "Res";

	public static final int SEPARATOR_PATH = 0;
	public static final int SEPARATOR_URI = 1;
	/**
	 * The File/URI separator characters.
	 */
	public static final char separators[] = {File.separatorChar, '/'};

	/**
	 * The Log object that <code>FileUtil</code> uses to log the error, debug,
	 * information messages.
	 */
	protected static Log log = LogFactory.getLog( FileUtil.class );

	/**
	 * The <code>HashMap</code> object that stores image type/file extension
	 * mapping.
	 */
	private static HashMap fileExtension = new HashMap( );

	static
	{
		// initialize fileExtension
		fileExtension.put( "image/bmp", ".bmp" );
		fileExtension.put( "image/gif", ".gif" );
		fileExtension.put( "image/jpg", ".jpg" );
		fileExtension.put( "image/pcx", ".pcx" );
		fileExtension.put( "image/png", ".png" );
		fileExtension.put( "image/tif", ".tif" );
	}

	/**
	 * Creates a new empty file in the specified directory, using the given
	 * prefix and suffix strings to generate its name.
	 * 
	 * @param prefix
	 *            The prefix string to be used in generating the file's name; if
	 *            it's less than three characters, we will use DEFAULT_PREFIX
	 *            instead
	 * @param suffix
	 *            The suffix string to be used in generating the file's name;
	 *            may be null, in which case the suffix ".tmp" will be used
	 * @param path
	 *            The directory in which the file is to be created
	 * @return the corresponding File object
	 */
	public static File createTempFile( String prefix, String suffix, String path )
	{
		assert path != null;

		if ( prefix == null || prefix.length( ) < 3 )
			prefix = DEFAULT_PREFIX;

		File dir = new File( path );
		if ( !dir.exists( ) )
		{
			if ( dir.mkdirs( ) == false )
			{
				log.error( "[FileUtil] Cannot create directory." );
				return null;
			}
		}

		try
		{
			File newFile = File.createTempFile( prefix, suffix, dir );
			return newFile;
		}
		catch ( IOException e )
		{
			log.error( "[FileUtil] " + e.getMessage( ) );
			e.printStackTrace( );
			return null;
		}
	}

	/**
	 * Copies the content from a resource specified by URI string to a target
	 * file.
	 * 
	 * @param srcUri
	 *            The source URI string.
	 * @param tgtFile
	 *            The target File object.
	 * @return A <code>boolean</code> value indicating if copyFile succeeded
	 *         or not.
	 */
	public static boolean copyFile( String srcUri, File tgtFile )
	{
		assert srcUri != null && tgtFile != null;

		try
		{
			FileOutputStream fos = new FileOutputStream( tgtFile );

			URL srcUrl = new URL( srcUri );
			InputStream is = srcUrl.openStream( );

			byte[] buffer = new byte[1024];
			while ( is.read( buffer ) > 0 )
			{
				fos.write( buffer );
			}

			is.close( );
			fos.close( );
			return true;
		}
		catch ( Exception e )
		{
			log.error( e.getMessage( ) );
			return false;
		}
	}

	/**
	 * Extract the file extension string from the given file name.
	 * 
	 * @param fileName
	 *            The file name.
	 * @param separatorFlag
	 *            Specifies whether the file name uses File or URI separator
	 *            character.
	 * @return The file extension string containing the '.' character.
	 */
	public static String getExtFromFileName( String fileName, int separatorFlag )
	{
		assert fileName != null;

		int lastindex = fileName.lastIndexOf( '.' );
		if ( lastindex > fileName.lastIndexOf( separators[separatorFlag] ) )
		{
			return fileName.substring( lastindex, fileName.length( ) );
		}

		return null;
	}

	/**
	 * Checks if a given URI string refers to a local resource. If a URI string
	 * starts with "http", we assume it's a global resource.
	 * 
	 * @param uri
	 *            The URI string value to check.
	 * @return A <code>boolean</code> value indicating if it's a local or
	 *         global resource.
	 */
	public static boolean isLocalResource( String uri )
	{
		return uri != null && uri.length( ) > 0
				&& uri.toLowerCase( ).startsWith( "http" ) == false;
	}

	/**
	 * Generates an absolute file name from the give path and file name.
	 * 
	 * @param path
	 *            The path name.
	 * @param fileName
	 *            The file name.
	 * @return The absolute file path.
	 */
	public static String getAbsolutePath( String path, String fileName )
	{
		if ( fileName == null || fileName.length( ) == 0 )
		{
			return null;
		}

		if ( path == null || path.length( ) == 0 || fileName.indexOf( ':' ) > 0 )
		{
			return fileName;
		}

		StringBuffer fullPath = new StringBuffer( );
		fullPath.append( path );
		if ( path.charAt( path.length( ) - 1 ) != File.separatorChar )
		{
			fullPath.append( File.separatorChar );
		}
		fullPath.append( fileName );
		return fullPath.toString( );
	}

	/**
	 * Verifies whether a file exists and then return its corresponding URI
	 * string.
	 * 
	 * @param fileName
	 *            The file name to verify.
	 * @return The file's URI string, returns <code>null</code> if the file
	 *         does not exist.
	 */
	public static String getURI( String fileName )
	{
		if ( fileName == null || fileName.length( ) == 0 )
		{
			return null;
		}

		File file = new File( fileName );
		if ( file.exists( ) && file.length( ) > 0l )
		{
			return file.toURI( ).toString( );
		}

		return null;
	}

	/**
	 * Saves a byte array to a specified file.
	 * 
	 * @param targetFile
	 *            The target File object.
	 * @param data
	 *            The output byte array.
	 * @return A <code>boolean</code> value indicating if the function
	 *         succeeded or not.
	 */
	public static boolean saveFile( File targetFile, byte[] data )
	{
		if ( targetFile == null )
		{
			return false;
		}

		try
		{
			FileOutputStream out;
			out = new FileOutputStream( targetFile );
			out.write( data );
			out.close( );
			return true;
		}
		catch ( IOException e )
		{
			log.error( "[FileUtil] " + e.getMessage( ) );
			return false;
		}
	}

	/**
	 * Gets the file extension according to the given Image file type.
	 * 
	 * @param fileType
	 *            The image file type say, "image/jpg".
	 * @return File extension string say, ".jpg".
	 */
	public static String getExtFromType( String fileType )
	{
		return (String) fileExtension.get( fileType );
	}

	/**
	 * Checks if a given file name contains relative path.
	 * 
	 * @param fileName
	 *            The file name.
	 * @return A <code>boolean</code> value indicating if the file name
	 *         contains relative path or not.
	 */
	public static boolean isRelativePath( String fileName )
	{
		return fileName != null && fileName.indexOf( ':' ) <= 0;
	}

	/**
	 * Gets the file content in the format of byte array
	 * 
	 * @param fileName
	 *            the file to be read
	 * @return the file content if no error happens, otherwise reutrn
	 *         <tt>null</tt>
	 */
	public static byte[] getFileContent( String fileName )
	{
		try
		{
			//Uses the buffered stream to improve the performance.
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream( fileName ) );
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			byte[] buf=new byte[1024];
			int len;
			while((len=in.read(buf))!=-1)
			{
				out.write(buf,0,len);
			}
			return out.toByteArray();
		}
		catch ( Exception e )
		{
			log.error( "Cannot get the content of the file " + fileName, e );
		}
		return null;
	}
}