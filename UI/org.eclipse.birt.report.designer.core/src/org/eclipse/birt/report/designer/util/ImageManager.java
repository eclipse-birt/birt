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

package org.eclipse.birt.report.designer.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.CorePlugin;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * Manages all image resources.
 */

public class ImageManager
{

	private static final String EMBEDDED_SUFFIX = ".Embedded."; //$NON-NLS-1$

	private static final ImageManager instance = new ImageManager( );

	private List invalidUrlList = new ArrayList( );

	private ImageManager( )
	{
	}

	/**
	 * Gets the instance of the image manager
	 * 
	 * @return Returns the instanceof the image manager
	 */
	public static ImageManager getInstance( )
	{
		return instance;
	}

	/**
	 * Gets the image by the given URI
	 * 
	 * @param uri
	 *            the url of the image file
	 * 
	 * @return Returns the image,or null if the url is invalid or the file
	 *         format is unsupported.
	 */
	public Image getImage( String uri, boolean refresh )
	{
		Image image;
		URL url = null;

		try
		{
			url = generateURL( uri );
			if ( url == null )
			{
				return null;
			}
			if ( !refresh && invalidUrlList.contains( url.toString( ) ) )
			{
				return null;
			}
			image = loadImage( url );
			if ( image == null )
			{
				if ( !invalidUrlList.contains( url.toString( ) ) )
				{
					invalidUrlList.add( url.toString( ) );
				}
			}
			else
			{
				invalidUrlList.remove( url.toString( ) );
			}
		}
		catch ( Exception e )
		{
			if ( url != null && !invalidUrlList.contains( url.toString( ) ) )
			{
				invalidUrlList.add( url.toString( ) );
			}
			return null;
		}
		return image;
	}

	public Image getImage( String uri )
	{
		return getImage( uri, false );
	}

	/**
	 * Gets the embedded image
	 * 
	 * @param embeddedImage
	 *            the embedded image data
	 * 
	 * @return Returns the image,or null if the embedded image doesn't exist.
	 */
	public Image getImage( ReportDesignHandle handle, String name )
	{
		String key = handle.hashCode() + EMBEDDED_SUFFIX + name;
		EmbeddedImage embeddedImage = handle.findImage( name );
		if ( embeddedImage == null )
		{
			getImageRegistry( ).remove( key );
			return null;
		}
		Image image = getImageRegistry( ).get( key );
		if ( image != null )
		{
			return image;
		}
		image = new Image( null,
				new ByteArrayInputStream( embeddedImage.getData( ) ) );
		if ( image != null )
		{
			getImageRegistry( ).put( key, image );
		}
		return image;
	}

	/**
	 * Loads the image into the image registry by the given URI
	 * 
	 * @param uri
	 *            the URI of the image to load
	 * @return Returns the image if it loaded correctly
	 * @throws IOException
	 */
	public Image loadImage( String uri ) throws IOException
	{
		URL url = generateURL( uri );
		if ( url == null )
		{
			throw new FileNotFoundException( uri );
		}
		return loadImage( url );
	}

	private Image loadImage( URL url ) throws IOException
	{
		String key = url.toString( );
		getImageRegistry( ).remove( key );
		InputStream in = null;
		Image image;
		try
		{
			in = url.openStream( );
			image = new Image( null, in );
		}
		catch ( IOException e )
		{
			throw e;
		}
		finally
		{
			if ( in != null )
			{
				in.close( );
			}
		}
		if ( image != null )
		{
			getImageRegistry( ).put( key, image );
		}
		return image;
	}

	private ImageRegistry getImageRegistry( )
	{
		return CorePlugin.getDefault( ).getImageRegistry( );
	}

	private URL generateURL( String uri ) throws MalformedURLException
	{
		try
		{			 
			return new URL( uri );
		}
		catch ( MalformedURLException e )
		{
			String path = URIUtil.getLocalPath( uri );
			if ( path != null )
			{
				String fullPath = SessionHandleAdapter.getInstance( )
						.getSessionHandle( )
						.getFileLocator( )
						.findFile( SessionHandleAdapter.getInstance( )
								.getReportDesignHandle( ),
								path );
				if ( fullPath == null )
				{
					return null;
				}
				File file = new File( fullPath );
				if ( file.exists( ) )
				{
					return file.toURL( );
				}
				return null;
			}
			return URI.create( uri ).toURL( );
		}
	}

}