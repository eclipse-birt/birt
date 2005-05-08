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

	private static final String FILE_SCHEMA = "file"; //$NON-NLS-1$

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
}
