/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.report.engine.api.script.instance.IImageInstance;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class representing the runtime state of an image
 */
public class ImageInstance extends ReportItemInstance implements IImageInstance
{

	public ImageInstance( IImageContent image, ExecutionContext context )
	{
		super( image, context );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getAltText()
	 */
	public String getAltText( )
	{
		return ( ( IImageContent ) content ).getAltText( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setAltText(java.lang.String)
	 */
	public void setAltText( String altText )
	{
		( ( IImageContent ) content ).setAltText( altText );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getAltTextKey()
	 */
	public String getAltTextKey( )
	{
		return ( ( IImageContent ) content ).getAltTextKey( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setAltTextKey(java.lang.String)
	 */
	public void setAltTextKey( String altTextKey )
	{
		( ( IImageContent ) content ).setAltTextKey( altTextKey );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getURI()
	 */
	public String getURI( )
	{
		return ( ( IImageContent ) content ).getURI( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setURI(java.lang.String)
	 */
	public void setURI( String uri )
	{
		( ( IImageContent ) content ).setURI( uri );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getImageSource()
	 */
	public int getImageSource( )
	{
		return ( ( IImageContent ) content ).getImageSource( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setImageSource(int)
	 */
	public void setImageSource( int source )
	{
		( ( IImageContent ) content ).setImageSource( source );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getImageName()
	 */
	public String getImageName( )
	{
		return ( ( IImageContent ) content ).getURI( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setImageName(java.lang.String)
	 */
	public void setImageName( String imageName )
	{
		( ( IImageContent ) content ).setImageSource( IImageContent.IMAGE_NAME );
		( ( IImageContent ) content ).setURI( imageName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getData( )
	 */
	public byte[] getData( )
	{
		return ( ( IImageContent ) content ).getData( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setData(
	 *      byte[])
	 */
	public void setData( byte[] data )
	{
		( ( IImageContent ) content ).setData( data );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getMimeType()
	 */
	public String getMimeType( )
	{
		return ( ( IImageContent ) content ).getMIMEType( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setMimeType(java.lang.String)
	 */
	public void setMimeType( String type )
	{
		( ( IImageContent ) content ).setMIMEType( type );
	}

}
