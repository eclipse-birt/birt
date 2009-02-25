/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.html.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * 
 */

public class DiagonalLineImage
{

	/**
	 * The number of the diagonal line.
	 */
	private int diagonalNumber = -1;
	/**
	 * The style of the diagonal line.
	 */
	private String diagonalStyle = null;
	/**
	 * The width of the diagonal line.
	 */
	private DimensionType diagonalWidth = null;
	/**
	 * The number of the antidiagonal line.
	 */
	private int antidiagonalNumber = -1;
	/**
	 * The style of the antidiagonal line.
	 */
	private String antidiagonalStyle = null;
	/**
	 * The width of the antidiagonal line.
	 */
	private DimensionType antidiagonalWidth = null;

	/**
	 * The line color. Default value is black;
	 */
	private Color color = null;
	/**
	 * Image width.
	 */
	DimensionType imageWidth = null;
	/**
	 * Image height.
	 */
	DimensionType imageHeight = null;
	/**
	 * Image DPI.
	 */
	protected int imageDpi = -1;

	/**
	 * Default image pixel width.
	 */
	private static int DEFAULT_IMAGE_PX_WIDTH = 200;
	/**
	 * Default image pixel height.
	 */
	private static int DEFAULT_IMAGE_PX_HEIGHT = 200;
	/**
	 * Default image DPI.
	 */
	private static int DEFAULT_IMAGE_DPI = 96;
	
	

	public void setDiagonalLine( int diagonalNumber, String diagonalStyle,
			DimensionType diagonalWidth )
	{
		this.diagonalNumber = diagonalNumber;
		this.diagonalStyle = diagonalStyle;
		this.diagonalWidth = diagonalWidth;
	}

	public void setAntidiagonalLine( int antidiagonalNumber,
			String antidiagonalStyle, DimensionType antidiagonalWidth )
	{
		this.antidiagonalNumber = antidiagonalNumber;
		this.antidiagonalStyle = antidiagonalStyle;
		this.antidiagonalWidth = antidiagonalWidth;
	}

	public void setColor( Color color )
	{
		this.color = color;
	}
	
	public void setImageDpi( int dpi )
	{
		this.imageDpi = dpi;
	}

	public void setImageSize( DimensionType imageWidth,
			DimensionType imageHeight )
	{
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}

	public byte[] drawImage( ) throws IOException
	{
		if ( diagonalNumber <= 0 && antidiagonalNumber <= 0 )
		{
			return null;
		}
		if ( imageDpi < 0 )
		{
			imageDpi = DEFAULT_IMAGE_DPI;
		}
		int imagePXWidth = HTMLEmitterUtil.getDimensionPixelValue( imageWidth, imageDpi );
		int imagePXHeight = HTMLEmitterUtil.getDimensionPixelValue( imageHeight, imageDpi );
		if ( imagePXWidth <= 0 )
		{
			imagePXWidth = DEFAULT_IMAGE_PX_WIDTH;
		}
		if ( imagePXHeight <= 0 )
		{
			imagePXHeight = DEFAULT_IMAGE_PX_HEIGHT;
		}

		// Create a buffered image in which to draw
		BufferedImage bufferedImage = new BufferedImage( imagePXWidth,
				imagePXHeight,
				BufferedImage.TYPE_INT_ARGB );
		// Create a graphics contents on the buffered image
		Graphics2D g2d = bufferedImage.createGraphics( );

		try
		{
			// set color
			if ( color != null )
			{
				g2d.setColor( color );
			}

			// FIXME continue: implement the line width and style.
			// Draw diagonal line.
			if ( diagonalNumber == 1 )
			{
				g2d.drawLine( 0, 0, imagePXWidth - 1, imagePXHeight - 1 );
			}
			else if ( diagonalNumber == 2 )
			{
				g2d.drawLine( ( imagePXWidth / 3 ) - 1,
						0,
						imagePXWidth - 1,
						imagePXHeight - 1 );
				g2d.drawLine( 0,
						( imagePXHeight / 3 ) - 1,
						imagePXWidth - 1,
						imagePXHeight - 1 );
			}
			else if ( diagonalNumber >= 3 )
			{
				g2d.drawLine( ( imagePXWidth / 2 ) - 1,
						0,
						imagePXWidth - 1,
						imagePXHeight - 1 );
				g2d.drawLine( 0, 0, imagePXWidth - 1, imagePXHeight - 1 );
				g2d.drawLine( 0,
						( imagePXHeight / 2 ) - 1,
						imagePXWidth - 1,
						imagePXHeight - 1 );
			}

			// Draw antidiagonal line.
			if ( antidiagonalNumber == 1 )
			{
				g2d.drawLine( imagePXWidth - 1, 0, 0, imagePXHeight - 1 );
			}
			else if ( antidiagonalNumber == 2 )
			{
				g2d.drawLine( ( imagePXWidth * 2 / 3 ) - 1,
						0,
						0,
						imagePXHeight - 1 );
				g2d.drawLine( imagePXWidth - 1,
						( imagePXHeight / 3 ) - 1,
						0,
						imagePXHeight - 1 );
			}
			else if ( antidiagonalNumber >= 3 )
			{
				g2d.drawLine( ( imagePXWidth / 2 ) - 1, 0, 0, imagePXHeight - 1 );
				g2d.drawLine( imagePXWidth - 1, 0, 0, imagePXHeight - 1 );
				g2d.drawLine( imagePXWidth - 1,
						( imagePXHeight / 2 ) - 1,
						0,
						imagePXHeight - 1 );
			}
		}
		finally
		{
			// Graphics context no longer needed so dispose it
			g2d.dispose( );
		}

		byte[] resultImageByteArray = null;

		ByteArrayOutputStream imageStream = new ByteArrayOutputStream( );
		// write the image data into a stream in png format.
		ImageIO.write( bufferedImage, "png", imageStream );
		imageStream.flush( );
		// convert the png image data to a byte array.
		resultImageByteArray = imageStream.toByteArray( );
		imageStream.close( );
		
		return resultImageByteArray;

		//if ( resultImageByteArray != null )
		//{
		//	image = new Image( resultImageByteArray, null, ".png" );
		//}
	}
}
