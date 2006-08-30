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
import java.io.InputStream;
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
		if ( fileName == null )
			return null;

		// try absolute path search

		try
		{
			File f = new File( fileName );
			if ( f.isAbsolute( ) )
				return f.exists( ) && f.isFile( ) ? f.getCanonicalFile( )
						.toURL( ) : null;
		}
		catch ( IOException e )
		{
			return null;
		}

		// try url search

		try
		{
			URL objURI = new URL( fileName );
			if ( isGlobalResource( objURI ) )
				return objURI;

			return tryLocalResourceSearch( objURI );
		}
		catch ( MalformedURLException e )
		{
			// ignore the error
		}

		// if module is null, then can not search the resource path or systemId
		if ( moduleHandle == null )
			return tryFragmentSearch( fileName );

		// try file search based on resource path, value set on this module
		// takes the higher priority than that in the session

		String resourcePath = moduleHandle.getResourceFolder( );
		if ( resourcePath == null )
			resourcePath = moduleHandle.getModule( ).getSession( )
					.getResourceFolder( );
		if ( resourcePath != null )
		{
			File f = new File( resourcePath, fileName );

			try
			{
				if ( f.exists( ) && f.isFile( ) )
					return f.getCanonicalFile( ).toURL( );
			}
			catch ( IOException e )
			{
				// ignore the error
			}
		}

		// try fragment search

		URL url = tryFragmentSearch( fileName );
		if ( url != null )
			return url;

		// try file search based on path of the input module

		URL systemId = moduleHandle.getModule( ).getSystemId( );
		if ( systemId == null )
			return null;

		try
		{
			url = new URL( systemId, URIUtil
					.convertFileNameToURLString( fileName ) );

			if ( isGlobalResource( url ) )
				return url;

			return tryLocalResourceSearch( url );
		}
		catch ( MalformedURLException e )
		{
			// ignore the error
		}

		return null;
	}

	/**
	 * Tests if the url indicates a global resource.
	 * 
	 * @param url
	 *            the url to test
	 * @return true if the url indicates to a global resource, false otherwise.
	 */

	private boolean isGlobalResource( URL url )
	{
		if ( URIUtil.FTP_SCHEMA.equalsIgnoreCase( url.getProtocol( ) )
				|| URIUtil.HTTP_SCHEMA.equalsIgnoreCase( url.getProtocol( ) ) )
			return true;

		if ( url.getFile( ).toLowerCase( ).startsWith( URIUtil.FTP_SCHEMA )
				|| url.getFile( ).toLowerCase( ).startsWith(
						URIUtil.HTTP_SCHEMA ) )
			return true;

		return false;
	}

	/**
	 * Search local resources.
	 * 
	 * @param url
	 *            url of the resources.
	 * @return url of the resource if found, null otherwise.
	 */

	private URL tryLocalResourceSearch( URL url )
	{
		InputStream in = null;
		try
		{
			in = url.openStream( );
		}
		catch ( IOException e1 )
		{
			return null;
		}
		finally
		{
			if ( in != null )
				try
				{
					in.close( );
				}
				catch ( IOException e )
				{
				}
		}

		return url;
	}

	/**
	 * Returns the url of resource which is in corresponding bundle.
	 * 
	 * @param moduleHandle
	 *            module in which the bundle symbolic name is cached
	 * @param fileName
	 *            the relative file name
	 * @return the url of resource if found
	 */

	private URL tryFragmentSearch( String fileName )
	{
		return BundleFactory.getBundleFactory( ).getBundleResource(
				FRAGMENT_RESOURCE_HOST, fileName );
	}
}