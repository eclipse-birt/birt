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

package org.eclipse.birt.report.model.api.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility class to handle URI.
 * 
 * 
 */
public class URIUtil
{

	/**
	 * 
	 */

	public static final String FILE_SCHEMA = "file"; //$NON-NLS-1$

	/**
	 * Checks <code>uri</code> is file path. If <code>uri</code> is an
	 * absolute uri and refers to a file, removes "file://" and returns the file
	 * path. If <code>uri</code> is relative uri and refers to a file, returns
	 * the <code>uri</code>. For other cases, returns null.
	 * <p>
	 * For examples, following uri are supported:
	 * 
	 * <ul>
	 * <li>file://C:/disk/test/data.file
	 * <li>/C:/disk/test/data.file
	 * <li>/usr/local/disk/test/data.file
	 * <li>C:\\disk\\test/data.file
	 * <li>C:/disk/test/data.file
	 * <li>./test/data.file
	 * </ul>
	 * 
	 * 
	 * @param uri
	 *            the input uri
	 * @return the file path if <code>uri</code> refers to a file. Otherwise
	 *         null.
	 */

	public static String getLocalPath( String uri )
	{
		if ( uri == null )
			return null;

		URI objURI = null;

		try
		{
			objURI = new URI( uri );
		}
		catch ( URISyntaxException e )
		{
			return getLocalFileOfFailedURI( uri );
		}

		if ( objURI.getScheme( ) == null )
		{
			if ( isFileProtocol( uri ) )
				return uri;
		}
		else if ( objURI.getScheme( ).equalsIgnoreCase( FILE_SCHEMA ) )
		{
			return objURI.getSchemeSpecificPart( );
		}
		else
		{
			// this is for files on the windows platforms.

			if ( objURI.getScheme( ).length( ) == 1 )
			{
				return uri;
			}

		}

		return null;
	}

	/**
	 * Checks whether <code>filePath</code> is a valid file on the disk.
	 * <code>filePath</code> can follow these scheme.
	 * 
	 * <ul>
	 * <li>./../hello/
	 * <li>C:\\hello\..\
	 * <li>/C:/../hello/.
	 * </ul>
	 * 
	 * @param filePath
	 *            the input filePath
	 * @return true if filePath exists on the disk. Otherwise false.
	 */

	private static boolean isFileProtocol( String filePath )
	{
		File file = new File( filePath );
		if ( file.toURI( ).getScheme( ).equalsIgnoreCase( FILE_SCHEMA ) )
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks whether <code>filePath</code> is a file protocol if it is not a
	 * invalid URI.
	 * <p>
	 * A invalid URI contains excluded US-ASCII characters:
	 * <ul>
	 * <li>contro = <US-ASCII coded characters 00-1F and 7F hexadecimal>
	 * <li>space = <US-ASCII coded character 20 hexadecimal>
	 * <li>delims="<" | ">" | "#" | "%" | <">
	 * <li>unwise="{" | "}" | "|" | "\" | "^" | "[" | "]" | "`"
	 * </ul>
	 * Details are described at the hyperlink:
	 * http://www.ietf.org/rfc/rfc2396.txt.
	 * 
	 * @param uri
	 *            the input uri
	 * @return the file path if <code>uri</code> refers to a file. Otherwise
	 *         null.
	 */

	private static String getLocalFileOfFailedURI( String uri )
	{
		URL objURI = null;
		try
		{
			objURI = new URL( uri );

			if ( !objURI.getProtocol( ).equalsIgnoreCase( FILE_SCHEMA ) )
				return null;

			return objURI.getAuthority( ) + objURI.getPath( );
		}
		catch ( MalformedURLException e )
		{
			File file = new File( uri );

			if ( uri.startsWith( FILE_SCHEMA ) )
				return file.toURI( ).getSchemeSpecificPart( );

			return uri;
		}
	}

	/**
	 * Converts a filename to a valid URL string. The filename can include
	 * directory information, either relative or absolute directory.
	 * 
	 * @param filePath
	 *            the file name
	 * @return a valid URL String
	 * 
	 */

	public static String convertFileNameToURLString( String filePath )
	{
		StringBuffer buffer = new StringBuffer( );
		String path = filePath;

		// convert non-URL style file separators

		if ( File.separatorChar != '/' )
			path = path.replace( File.separatorChar, '/' );

		// copy, converting URL special characters as we go

		for ( int i = 0; i < path.length( ); i++ )
		{
			char c = path.charAt( i );
			if ( c < 0x1F || c == 0x7f )
				buffer.append( "%" + Character.toString( c ) ); //$NON-NLS-1$
			else if ( c == '#' )
				buffer.append( "%23" ); //$NON-NLS-1$
			else if ( c == '%' )
				buffer.append( "%25" ); //$NON-NLS-1$
			else if ( c == '<' )
				buffer.append( "%3C" ); //$NON-NLS-1$
			else if ( c == '>' )
				buffer.append( "%3E" ); //$NON-NLS-1$
			else if ( c == '"' )
				buffer.append( "%22" ); //$NON-NLS-1$			
			else
				buffer.append( c );
		}

		// return URL

		return buffer.toString( );
	}

	/**
	 * Converts a filename to a valid URL. The filename can include directory
	 * information, either relative or absolute directory. And the file should
	 * be on the local disk. TODO: add samples.
	 * 
	 * @param filePath
	 *            the file name
	 * @return a valid URL
	 * 
	 */

	public static URL getDirectory( String filePath )
	{
		URL url = null;

		try
		{
			url = new URL( convertFileNameToURLString( filePath ) );
		}
		catch ( MalformedURLException e )
		{
		}

		// follows the file protocol

		if ( url == null )
			return getFileDirectory( filePath );

		if ( FILE_SCHEMA.equalsIgnoreCase( url.getProtocol( ) ) )
			return getFileDirectory( url.getPath( ) );

		// rather then the file protocol

		return getNetDirectory( url );
	}

	/**
	 * Returns the directory of a file path that is a url with network
	 * protocols. Note that <code>filePath</code> should include the file name
	 * and file extension.
	 * 
	 * @param filePath
	 *            the file url
	 * @return a url for the directory of the file
	 */

	private static URL getNetDirectory( URL filePath )
	{
		assert filePath != null;

		String path = filePath.getFile( );

		// remove the file name

		int index = path.lastIndexOf( '/' );
		if ( index != -1 && index != path.length( ) - 1 )
			path = path.substring( 0, index + 1 );

		try
		{
			return new URL( filePath.getProtocol( ), filePath.getHost( ),
					filePath.getPort( ), path );
		}
		catch ( MalformedURLException e )
		{
		}

		assert false;
		return null;
	}

	/**
	 * Returns the directory of a file path that is a filepath or a url with the
	 * file protocol.
	 * 
	 * @param filePath
	 *            the file url
	 * @return a url for the directory of the file
	 */

	private static URL getFileDirectory( String filePath )
	{
		File file = new File( filePath );
		file = file.getParentFile( );

		if ( file == null )
			return null;

		try
		{
			return file.getCanonicalFile( ).toURL( );
		}
		catch ( MalformedURLException e )
		{
			assert false;
		}
		catch ( IOException e )
		{
		}

		return null;
	}
}
