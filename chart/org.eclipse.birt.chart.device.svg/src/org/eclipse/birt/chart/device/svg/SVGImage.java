/***********************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.svg;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

/**
 * This class repesents an SVG image.
 */
public class SVGImage extends Image
{

	protected Image image;
	protected URL url;
	protected byte[] data;
	public static final String BASE64 = "data:;base64,";

	public byte[] getData( )
	{
		return data;
	}

	/**
	 * @param image
	 */
	public SVGImage( Image image, URL url )
	{
		super( );
		this.image = image;
		this.url = url;
	}

	public SVGImage( Image image, URL url, byte[] data )
	{
		this( image, url );
		this.data = data;
	}

	/**
	 * 
	 */
	public void flush( )
	{
		image.flush( );
	}

	/**
	 * @return
	 */
	public Graphics getGraphics( )
	{
		return image.getGraphics( );
	}

	/**
	 * @param arg0
	 * @return
	 */
	public int getHeight( ImageObserver arg0 )
	{
		return image.getHeight( arg0 );
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public Object getProperty( String arg0, ImageObserver arg1 )
	{
		return image.getProperty( arg0, arg1 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Image#getScaledInstance(int, int, int)
	 */
	public Image getScaledInstance( int arg0, int arg1, int arg2 )
	{
		return image.getScaledInstance( arg0, arg1, arg2 );
	}

	/**
	 * @return
	 */
	public ImageProducer getSource( )
	{
		return image.getSource( );
	}

	/**
	 * @param arg0
	 * @return
	 */
	public int getWidth( ImageObserver arg0 )
	{
		return image.getWidth( arg0 );
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl( )
	{
		if ( url == null )
		{
			if ( data != null )
			{
				return BASE64 + new String( Base64.encodeBase64( data ) );

			}
			else
			{
				return ""; //$NON-NLS-1$
			}
		}
		return url.toExternalForm( );
	}

	/**
	 * @param url
	 *            The url to set.
	 */
	public void setUrl( URL url )
	{
		this.url = url;
	}
}
