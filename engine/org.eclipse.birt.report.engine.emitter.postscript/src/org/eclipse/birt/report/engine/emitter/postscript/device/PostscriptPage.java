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

package org.eclipse.birt.report.engine.emitter.postscript.device;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.engine.emitter.postscript.PostscriptWriter;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.TextStyle;
import org.eclipse.birt.report.engine.layout.emitter.IPage;

public class PostscriptPage implements IPage
{

	private PostscriptWriter writer;
	private boolean isDisposed;

	public PostscriptPage( PostscriptWriter writer )
	{
		this.writer = writer;
		this.isDisposed = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#setPageSize(float,
	 *      float)
	 */
	void setPageSize( int pageWidth, int pageHeight )
	{
		writer.startPage( convertToPoint( pageWidth ),
				convertToPoint( pageHeight ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#dispose()
	 */
	public void dispose( )
	{
		if ( !isDisposed )
		{
			writer.endPage( );
			isDisposed = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawText(java.lang.String,
	 *      float, float,
	 *      org.eclipse.birt.report.engine.layout.pdf.font.FontInfo, float,
	 *      float, java.awt.Color, boolean, boolean, boolean)
	 */
	public void drawText( String text, int textX, int textY, int width,
			int height, TextStyle fontStyle )
	{
		writer.drawString( text, convertToPoint( textX ),
				convertToPoint( textY ), fontStyle.getFontInfo( ),
				convertToPoint( fontStyle.getLetterSpacing( ) ),
				convertToPoint( fontStyle.getWordSpacing( ) ), fontStyle
						.getColor( ), fontStyle.isLinethrough( ), fontStyle
						.isOverline( ), fontStyle.isUnderline( ), fontStyle
						.getAlign( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawImage(java.io.InputStream,
	 *      float, float, float, float)
	 */
	public void drawImage( InputStream input, int imageX, int imageY,
			int height, int width, String helpText ) throws Exception
	{
		writer.drawImage( input, convertToPoint( imageX ),
				convertToPoint( imageY ), convertToPoint( width ),
				convertToPoint( height ) );
	}

	public void drawImage( byte[] imageData, String extension, int imageX, int imageY,
			int height, int width, String helpText ) throws Exception
	{
		InputStream input = new ByteArrayInputStream( imageData );
		drawImage( input, imageX, imageY, height, width, helpText );
	}

	public void drawImage( String uri, String extension, int imageX,
			int imageY, int height, int width, String helpText )
			throws Exception
	{
		if ( uri == null )
		{
			return;
		}
		drawImage( new URL( uri ).openStream( ), imageX, imageY, height, width,
				helpText );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawLine(float,
	 *      float, float, float, float, java.awt.Color, java.lang.String)
	 */
	public void drawLine( int startX, int startY, int endX, int endY,
			int width, Color color, String lineStyle )
	{
		writer.drawLine( convertToPoint( startX ), convertToPoint( startY ),
				convertToPoint( endX ), convertToPoint( endY ),
				convertToPoint( width ), color, lineStyle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawBackgroundColor(java.awt.Color,
	 *      float, float, float, float)
	 */
	public void drawBackgroundColor( Color color, int x, int y, int width,
			int height )
	{
		if ( color != null )
		{
			writer.fillRect( convertToPoint( x ), convertToPoint( y ),
					convertToPoint( width ), convertToPoint( height ), color );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawPageBackgroundColor(java.awt.Color)
	 */
	public void drawPageBackgroundColor( Color color )
	{
		writer.fillPage( color );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawBackgroundImage(float,
	 *      float, float, float, java.lang.String, java.lang.String, float,
	 *      float)
	 */
	public void drawBackgroundImage( int x, int y, int width, int height,
			String repeat, String imageUrl, int absPosX, int absPosY )
			throws IOException
	{
		writer.drawBackgroundImage( imageUrl, convertToPoint( x ),
				convertToPoint( y ), convertToPoint( width ),
				convertToPoint( height ), convertToPoint( absPosX ),
				convertToPoint( absPosY ), repeat );
	}

	public void clip( int startX, int startY, int width, int height )
	{
		writer.clipRect( convertToPoint( startX ), convertToPoint( startY ),
				convertToPoint( width ), convertToPoint( height ) );
	}

	public void clipSave( )
	{
		writer.clipSave( );
	}

	public void clipRestore( )
	{
		writer.clipRestore( );
	}

	private float convertToPoint( int value )
	{
		return value / PDFConstants.LAYOUT_TO_PDF_RATIO;
	}
}
