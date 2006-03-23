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

package org.eclipse.birt.report.model.api;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.report.model.api.util.URIUtil;

/**
 * The default file search algorithm. It searches for a given file in the 'base'
 * folder of a design. If the 'base' property of the design was not set, then
 * this class looks in folder where the design file is located.
 * <p>
 * The detail search mechanism is:
 * <ul>
 * <li>If the the file to be found is given by an absolute path, returns that
 * path.
 * <li>If it is a relative file path, search the 'base' folder of the design.
 * <li>If the 'base' property of the design is not set, then search the file in
 * the folder where the design file locates.
 * </ul>
 * 
 * @see IResourceLocator
 * @see SessionHandle
 */

public class DefaultResourceLocator implements IResourceLocator
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.IResourceLocator#findResource(org.eclipse.birt.report.model.api.ModuleHandle,
	 *      java.lang.String, int)
	 */

	public URL findResource( ModuleHandle moduleHandle, String fileName,
			int type )
	{
		assert moduleHandle != null;
		if ( fileName == null )
			return null;

		URL systemId = moduleHandle.getModule( ).getSystemId( );
		if ( systemId == null )
			return null;

		URL url = tryFileSearch( systemId, fileName );
		if ( url != null )
			return url;

		// if system id indicates a file protocol, it must have been checked
		// in the tryFileSearch(). DO NOT call tryURISearch in these cases.

		if ( systemId == null
				|| URIUtil.FILE_SCHEMA
						.equalsIgnoreCase( systemId.getProtocol( ) ) )
			return null;

		return tryURLSearch( systemId, fileName );
	}

	/**
	 * Return a url if the <code>fileName</code> can be found in the directory
	 * <code>filePath</code>.
	 * 
	 * @param systemId
	 *            the systemID to search
	 * @param fileName
	 *            the file name
	 * @return the <code>URL</code> object. <code>null</code> if the file
	 *         can not be found.
	 */

	private URL tryFileSearch( URL systemId, String fileName )
	{
		if ( !URIUtil.FILE_SCHEMA.equalsIgnoreCase( systemId.getProtocol( ) ) )
			return null;

		File f = new File( systemId.getPath( ) );
		if ( f.isDirectory( ) )
			f = new File( f.getPath( ), fileName );
		else
			f = new File( f.getParent( ), fileName );

		if ( f.isFile( ) && f.exists( ) )
		{
			try
			{
				return f.getCanonicalFile( ).toURL( );
			}
			catch ( MalformedURLException e )
			{
				return null;
			}
			catch ( IOException e )
			{
			    return null;
			}
		}

		return null;
	}

	/**
	 * Return a url with the given base <code>uri</code> and the
	 * <code>fileName</code>.
	 * 
	 * @param systemId
	 *            the URL systemID
	 * @param fileName
	 *            the file name
	 * @return the <code>URL</code> object. <code>null</code> if the file
	 *         can not be found.
	 */

	private URL tryURLSearch( URL systemId, String fileName )
	{
		assert systemId != null;

		try
		{
			URL urlObj = new URL( systemId, URIUtil
					.convertFileNameToURLString( fileName ) );
			return urlObj;
		}
		catch ( MalformedURLException e )
		{
			return null;
		}

	}
}