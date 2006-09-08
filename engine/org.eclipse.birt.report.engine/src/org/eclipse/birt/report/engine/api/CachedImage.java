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

package org.eclipse.birt.report.engine.api;

public class CachedImage
{

	/**
	 * id of the image.
	 */
	String id;
	/**
	 * url of the image. 
	 * It is the file path of the cached image, it is used as if it 
	 * is defined in the report design directly. 
	 */
	String url;
	/**
	 * mime type of the image
	 */
	String mimeType;
	/**
	 * image map of the image.
	 */
	String imageMap;

	public CachedImage( )
	{

	}

	public CachedImage( String id, String url )
	{
		this.id = id;
		this.url = url;
	}

	public void setID( String id )
	{
		this.id = id;
	}

	public String getID( )
	{
		return id;
	}

	public String getURL( )
	{
		return url;
	}

	public void setURL( String url )
	{
		this.url = url;
	}

	public String getImageMap( )
	{
		return imageMap;
	}

	public void setImageMap( String imageMap )
	{
		this.imageMap = imageMap;
	}

	public String getMIMEType( )
	{
		return imageMap;
	}

	public void setMIMEType( String mimeType )
	{
		this.mimeType = mimeType;
	}
}
