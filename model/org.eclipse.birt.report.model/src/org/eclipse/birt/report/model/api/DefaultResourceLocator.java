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
import java.net.URLConnection;

import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

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

		try
		{
			// try absolute path search

			File f = new File( fileName );
			if ( f.isAbsolute( ) )
				return f.exists( ) && f.isFile( ) ? f.getCanonicalFile( )
						.toURL( ) : null;

			// try url search
			try
			{
				URL objURI = new URL( fileName );
				String protocol = objURI.getProtocol( );

				if ( URIUtil.FILE_SCHEMA.equalsIgnoreCase( protocol ) )
				{
					f = new File( objURI.getPath( ) );
					if ( f.isAbsolute( ) )
						return f.exists( ) && f.isFile( ) ? f
								.getCanonicalFile( ).toURL( ) : null;
				}
				else if ( URIUtil.JAR_SCHEMA.equalsIgnoreCase( protocol ) )
				{
					URLConnection jarConnection = objURI.openConnection( );
					try
					{
						jarConnection.connect( );
						return objURI;
					}
					catch ( IOException e1 )
					{
						return null;
					}
				}
				else
					return objURI;
			}
			catch ( MalformedURLException e )
			{
			}

			// try fragment search

			URL url = tryFragmentSearch( moduleHandle, fileName );
			if ( url != null )
				return url;

			// try file search based on resource path set on this session

			String resourcePath = moduleHandle.getModule( ).getSession( )
					.getBirtResourcePath( );
			if ( resourcePath != null )
			{
				f = new File( resourcePath, fileName );
				if ( f.exists( ) && f.isFile( ) )
					return f.getCanonicalFile( ).toURL( );
			}

			// try file search based on path of the input module

			if ( moduleHandle == null )
				return null;

			URL systemId = moduleHandle.getModule( ).getSystemId( );
			if ( systemId == null )
				return null;

			if ( URIUtil.FILE_SCHEMA.equalsIgnoreCase( systemId.getProtocol( ) ) )
				return tryFileSearch( systemId, fileName );
			else if ( URIUtil.JAR_SCHEMA.equalsIgnoreCase( systemId
					.getProtocol( ) )
					&& systemId.getPath( ).startsWith( URIUtil.FILE_SCHEMA ) )
				return tryJarSearch( systemId, fileName );
			else if ( URIUtil.BUNDLE_RESOURCE_SCHEMA.equalsIgnoreCase( systemId
					.getProtocol( ) ) )
				return tryFragmentSearch( moduleHandle, new URL( systemId,
						URIUtil.convertFileNameToURLString( fileName ) )
						.getPath( ) );

			return new URL( systemId, URIUtil
					.convertFileNameToURLString( fileName ) );
		}
		catch ( IOException e )
		{
		}

		return null;
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
	 * @throws IOException
	 * @throws MalformedURLException
	 */

	private URL tryFileSearch( URL systemId, String fileName )
			throws MalformedURLException, IOException
	{
		File f = new File( systemId.getPath( ) );
		if ( f.isDirectory( ) )
			f = new File( f.getPath( ), fileName );
		else
			f = new File( f.getParent( ), fileName );

		if ( f.isFile( ) && f.exists( ) )
			return f.getCanonicalFile( ).toURL( );

		return null;
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
	 * @throws IOException
	 * @throws MalformedURLException
	 */

	private URL tryJarSearch( URL base, String fileName )
	{
		URL url = null;
		try
		{
			url = new URL( base, fileName );
			url.openConnection( ).connect( );
		}
		catch ( MalformedURLException e )
		{
		}
		catch ( IOException e1 )
		{
			return null;
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

	private URL tryFragmentSearch( ModuleHandle moduleHandle, String fileName )
	{
		if ( moduleHandle == null )
			return null;

		String symbolicName = moduleHandle.getSymbolicName( );
		if ( symbolicName == null )
			return null;

		Bundle bundle = Platform.getBundle( symbolicName );
		if ( bundle != null )
			return bundle.getResource( URIUtil
					.convertFileNameToURLString( fileName ) );
		else
			return null;

	}
}