/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.style;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.engine.util.SvgFile;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.Image;

public class BackgroundImageInfo extends AreaConstants
{
	protected int xOffset;
	protected int yOffset;
	protected int repeatedMode;
	protected int width;
	protected int height;
	protected String url;
	protected byte[] imageData;
	
	private Image image;

	public BackgroundImageInfo( String url, int repeatedMode, int xOffset,
			int yOffset, int height, int width)
	{
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.repeatedMode = repeatedMode;
		this.width = width;
		this.height = height;
		this.url = url;
		prepareImageByteArray( );
	}

	public BackgroundImageInfo( BackgroundImageInfo bgi )
	{
		this.xOffset = bgi.xOffset;
		this.yOffset = bgi.yOffset;
		this.repeatedMode = bgi.repeatedMode;
		this.width = bgi.width;
		this.height = bgi.height;
		this.url = bgi.url;
		this.imageData = bgi.imageData;
		this.image = bgi.image;
	}

	public BackgroundImageInfo( String url, CSSValue mode, int xOffset,
			int yOffset, int height, int width)
	{
		this( url, repeatMap.get( mode ), xOffset, yOffset, height, width );
	}

	public BackgroundImageInfo( String url, int height, int width )
	{
		this( url, 0, 0, 0, height, width );
	}
	
	private void prepareImageByteArray( )
	{
		InputStream in = null;
		try
		{
			in = new URL( url ).openStream( );
			ByteArrayOutputStream out = new ByteArrayOutputStream( );
			int data = in.read( );
			while ( data != -1 )
			{
				out.write( data );
				data = in.read( );
			}
			imageData = out.toByteArray( );
			out.close( );
		}
		catch ( IOException ioe )
		{
			imageData = null;
			image = null;
			return;
		}
		finally
		{
			if ( in != null )
			{
				try
				{
					in.close( );
				}
				catch ( IOException e )
				{
				}
			}
		}
		try
		{
			image = Image.getInstance( imageData );
		}
		catch ( Exception e )
		{
			try
			{
				imageData = SvgFile.transSvgToArray( new ByteArrayInputStream(
						imageData ) );
				image = Image.getInstance( imageData );
			}
			catch ( Exception te )
			{
				imageData = null;
				image = null;
			}
		}

	}
	
	public Image getImageInstance( )
	{
		return image;
	}

	public int getXOffset( )
	{
		return xOffset;
	}

	public void setYOffset( int y )
	{
		this.yOffset = y;
	}
	

	public void setXOffset( int x )
	{
		this.xOffset = x;
	}

	public int getYOffset( )
	{
		return yOffset;
	}

	
	public int getHeight( )
	{
		return height;
	}

	
	public void setHeight( int height )
	{
		this.height = height;
	}

	
	public int getWidth( )
	{
		return width;
	}

	
	public void setWidth( int width )
	{
		this.width = width;
	}

	public int getRepeatedMode( )
	{
		return repeatedMode;
	}
	
	public String getUrl( )
	{
		return url;
	}

	public byte[] getImageData( )
	{
		return imageData;
	}

}
