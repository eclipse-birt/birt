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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.elements.structures.EmbeddedImage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;

/**
 * Manages all image resources.
 */

public class ImageManager
{

	private static final String EMBEDDED_SUFFIX = ".Embedded."; //$NON-NLS-1$

	/**
	 * Gets the image by the given file path
	 * 
	 * @param filePath
	 *            the path of the image file
	 * 
	 * @return Returns the image,or null if the path is invalid or the file
	 *         format is unsupported.
	 */
	public static Image getImage( String filePath )
	{
		try
		{
			File file = new File( filePath );
			if ( !file.exists( ) )
			{
				return null;
			}
			return getImage( file.toURL( ) );
		}
		catch ( MalformedURLException e )
		{
			return null;
		}
	}

	/**
	 * Gets the image by the given URL
	 * 
	 * @param url
	 *            the url of the image file
	 * 
	 * @return Returns the image,or null if the url is invalid or the file
	 *         format is unsupported.
	 */
	public static Image getImage( URL url )
	{
		String key = url.toString( );
		Image image = JFaceResources.getImage( key );
		if ( image == null )
		{
			image = ImageDescriptor.createFromURL( url ).createImage( false );
			if ( image != null )
			{
				JFaceResources.getImageRegistry( ).put( key, image );
			}
		}
		return image;
	}

	/**
	 * Gets the embedded image
	 * 
	 * @param embeddedImage
	 *            the embedded image data
	 * 
	 * @return Returns the image,or null if the embedded image doesn't exist.
	 */
	public static Image getImage( EmbeddedImage embeddedImage )
	{
		String fileName = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getFileName( );
		String key = embeddedImage.getName( ) + EMBEDDED_SUFFIX + fileName;
		Image image = JFaceResources.getImage( key );
		if ( image == null )
		{
			InputStream is = new ByteArrayInputStream( embeddedImage.getData( ) );
			image = new Image( null, is );
			if ( image != null )
			{
				JFaceResources.getImageRegistry( ).put( key, image );
			}
		}
		return image;
	}
}