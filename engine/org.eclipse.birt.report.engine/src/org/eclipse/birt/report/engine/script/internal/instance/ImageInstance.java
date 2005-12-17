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
import org.eclipse.birt.report.engine.content.impl.ImageContent;

/**
 * A class representing the runtime state of an image
 */
public class ImageInstance extends ReportItemInstance implements IImageInstance
{

	public ImageInstance( ImageContent image )
	{
		super( image );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getAltText()
	 */
	public String getAltText( )
	{
		return ( ( ImageContent ) content ).getAltText( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setAltText(java.lang.String)
	 */
	public void setAltText( String altText )
	{
		( ( ImageContent ) content ).setAltText( altText );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getAltTextKey()
	 */
	public String getAltTextKey( )
	{
		return ( ( ImageContent ) content ).getAltTextKey( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setAltTextKey(java.lang.String)
	 */
	public void setAltTextKey( String altTextKey )
	{
		( ( ImageContent ) content ).setAltTextKey( altTextKey );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getExtension()
	 */
	public String getExtension( )
	{
		return ( ( ImageContent ) content ).getExtension( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getURI()
	 */
	public String getURI( )
	{
		return ( ( ImageContent ) content ).getURI( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setURI(java.lang.String)
	 */
	public void setURI( String uri )
	{
		( ( ImageContent ) content ).setURI( uri );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getImageSource()
	 */
	public int getImageSource( )
	{
		return ( ( ImageContent ) content ).getImageSource( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setImageSource(int)
	 */
	public void setImageSource( int source )
	{
		( ( ImageContent ) content ).setImageSource( source );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getImageName()
	 */
	public String getImageName( )
	{
		return ( ( ImageContent ) content ).getImageName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setImageName(java.lang.String)
	 */
	public void setImageName( String imageName )
	{
		( ( ImageContent ) content ).setImageName( imageName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getData( )
	 */
	public byte[] getData( )
	{
		return ( ( ImageContent ) content ).getData( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setData( byte[])
	 */
	public void setData( byte[] data )
	{
		( ( ImageContent ) content ).setData( data );
	}

}
