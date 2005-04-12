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
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utility class to handle URI.
 * 
 *  
 */
public class URIUtil
{

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
			if ( isFileProtocol( uri ) )
				return uri;
		}

		if ( objURI.getScheme( ) == null )
		{
			if ( isFileProtocol( uri ) )
				return uri;
		}
		else if ( objURI.getScheme( ).equalsIgnoreCase( "file" ) ) //$NON-NLS-1$
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
		if ( file.toURI( ).getScheme( ).equalsIgnoreCase( "file" ) ) //$NON-NLS-1$
		{
			return true;
		}
		return false;
	}
}
