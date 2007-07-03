/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.ppt.device;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.engine.emitter.ppt.PPTWriter;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.TextStyle;
import org.eclipse.birt.report.engine.layout.emitter.IPage;

public class PPTPage implements IPage
{

	private PPTWriter writer;
	private boolean isDisposed;

	public PPTPage( PPTWriter writer )
	{
		this.writer = writer;
		this.isDisposed = false;
	}

	void setPageSize( int pageWidth, int pageHeight, Color backgroundColor )
	{
		writer.newPage( convertToPoint( pageWidth ),
				convertToPoint( pageHeight ),
				backgroundColor );
	}

	// clip handling needs to implement in future
	public void clip( int startX, int startY, int width, int height )
	{
		// TODO Auto-generated method stub
	}

	public void clipRestore( )
	{
		// TODO Auto-generated method stub
	}

	public void clipSave( )
	{
		// TODO Auto-generated method stub
	}

	public void dispose( )
	{
		if ( !isDisposed )
		{
			writer.endPage( );
			isDisposed = true;
		}
	}

	public void drawBackgroundColor( Color color, int x, int y, int width,
			int height )
	{
		writer.drawBackgroundColor( color,
				convertToPoint( x ),
				convertToPoint( y ),
				convertToPoint( width ),
				convertToPoint( height ) );
	}

	public void drawBackgroundImage( int x, int y, int width, int height,
			String repeat, String imageUrl, int absPosX, int absPosY )
			throws IOException
	{
		writer.drawBackgroundImage( imageUrl,
				convertToPoint( x ),
				convertToPoint( y ),
				convertToPoint( width ),
				convertToPoint( height ),
				convertToPoint( absPosX ),
				convertToPoint( absPosY ),
				repeat );
	}

	public void drawImage( byte[] imageData, String extension, int imageX,
			int imageY, int height, int width, String helpText )
			throws Exception
	{
		writer.drawImage( imageData,
				extension,
				convertToPoint( imageX ),
				convertToPoint( imageY ),
				convertToPoint( height ),
				convertToPoint( width ),
				helpText );
	}

	public void drawImage( String uri, String extension, int imageX,
			int imageY, int height, int width, String helpText )
			throws Exception
	{
		if ( uri == null )
		{
			return;
		}
		byte[] imageData = null;
		InputStream imageStream = new URL( uri ).openStream( );
		imageData = new byte[imageStream.available( )];
		imageStream.read( imageData );

		drawImage( imageData,
				extension,
				imageX,
				imageY,
				height,
				width,
				helpText );
	}

	public void drawLine( int startX, int startY, int endX, int endY,
			int width, Color color, String lineStyle )
	{
		writer.drawLine( convertToPoint( startX ),
				convertToPoint( startY ),
				convertToPoint( endX ),
				convertToPoint( endY ),
				convertToPoint( width ),
				color,
				lineStyle );
	}

	public void drawText( String text, int textX, int textY, int width,
			int height, TextStyle textStyle )
	{
		writer.drawText( text,
				convertToPoint( textX ),
				convertToPoint( textY ),
				convertToPoint( width ),
				convertToPoint( height ),
				textStyle.getFontInfo( ),
				textStyle.getColor( ),
				textStyle.isLinethrough( ),
				textStyle.isOverline( ),
				textStyle.isUnderline( ) );
	}

	private float convertToPoint( int value )
	{
		return value / PDFConstants.LAYOUT_TO_PDF_RATIO;
	}
}
