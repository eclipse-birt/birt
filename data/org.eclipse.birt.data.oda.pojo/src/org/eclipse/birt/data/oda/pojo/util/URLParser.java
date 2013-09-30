/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;


/**
 * Parse a class path string , where each path is separated with
 * <code>Constants.CLASS_PATH_SEPERATOR</code>, to generate an array containing
 * <code>java.net.URL<code>
 */
public class URLParser
{
	private static Logger logger = Logger.getLogger( URLParser.class.getName( ) );
	
	private Object resourceIdentifiers;
	
	@SuppressWarnings("rawtypes")
	public URLParser( Map appContext )
	{
		if ( appContext != null )
		{
			resourceIdentifiers = appContext.get( ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS );
		}
		if ( resourceIdentifiers == null )
		{
			logger.log( Level.WARNING, "No ResourceIdentifiers are provided in appContext!" );  //$NON-NLS-1$
		}
	}

	/**
	 * parse <code>classPath</code> into an array containing
	 * <code>java.net.URL<code>
	 * 
	 * @param classPath
	 *            : each path is separated with
	 *            <code>Constants.CLASS_PATH_SEPERATOR</code>
	 * @return
	 * @throws OdaException
	 *             if error/exception occur during parsing
	 */
	public URL[] parse( String classPath ) throws OdaException
	{
		if ( classPath == null )
		{
			return new URL[0];
		}
		String[] paths = classPath.split( String.valueOf( Constants.CLASS_PATH_SEPERATOR ) );
		List<URL> urls = new ArrayList<URL>( );
		for ( String path : paths )
		{
			path = path.trim( );
			if ( path.equals( "" )) //$NON-NLS-1$
			{
				//just ignore
				continue;
			}
			URI uri = null;
			if ( (new File( path )).isAbsolute( ) )
			{
				//an absolute path
				uri = new File( path ).toURI( );
				try
				{
					urls.add( uri.toURL( ) );					
				}
				catch ( MalformedURLException e )
				{
					throw new OdaException( e );
				}
			}
			else
			{
				//a relative path
				try
				{
					uri = new URI( resolveURI( path ));
				}
				catch ( URISyntaxException e )
				{
					throw new OdaException( e );
				}
				if ( resourceIdentifiers != null )
				{
					URI resovledUri = ResourceIdentifiers.resolveApplResource( resourceIdentifiers, uri );
					if ( resovledUri == null )
					{
						logger.log( Level.WARNING, "Failed to resolve path:" + uri //$NON-NLS-1$
								+ " from app resource folder(" + ResourceIdentifiers.getApplResourceBaseURI( resourceIdentifiers ) + ')');  //$NON-NLS-1$
					
					//then, try to resolve it from design resource
						resovledUri = ResourceIdentifiers.resolveDesignResource( resourceIdentifiers, uri );
					}
					if ( resovledUri == null )
					{
						logger.log( Level.WARNING, "Failed to resolve path:" + uri ); //$NON-NLS-1$
					}
					else
					{
						try
						{
							try 
							{
								String urlpath = enableURI( URLDecoder.decode( resovledUri.toString(), "UTF-8" ) );
								urls.add( new URL( urlpath ) );
							}
							catch ( UnsupportedEncodingException e ) 
							{
								urls.add( resovledUri.toURL( ) );
							}
						}
						catch ( MalformedURLException e )
						{
							throw new OdaException( e );
						}
					}
				}
				else
				{
					try
					{
						urls.add( uri.toURL( ) );
					}
					catch ( MalformedURLException e )
					{
						throw new OdaException( e );
					}
				}
			}
		}
		return urls.toArray( new URL[0] );
	}
	

	private String resolveURI( String location )
	{
		String result = enableURI( location );
		if ( !result.endsWith( ".jar" ) //$NON-NLS-1$
				&& !result.endsWith( ".zip" )) //$NON-NLS-1$
		{
			//consider it as a directory
			//but a URL not ends with "/" is treated as file
			if ( result.charAt( result.length( ) -1 ) != '/')
			{
				 result += "/"; //$NON-NLS-1$
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	private String enableURI( String location )
	{
		String result = location;
		try
		{
			if ( File.separatorChar != '/' )
				location = location.replace( File.separatorChar, '/' );
			if( location.startsWith( "/" ) )
			{
				result = new File( location ).toURI( )
						.toASCIIString( )
						.replace( new File( "/" ).toURI( ).toASCIIString( ), "/" );				
			}
			else
				result = new File( location ).toURI( )
					.toASCIIString( )
					.replace( new File( "" ).toURI( ).toASCIIString( ), "" );
		}
		catch ( Exception e )
		{
			return location;
		}
		return result;
	}
}
