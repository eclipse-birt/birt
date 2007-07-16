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
import org.eclipse.birt.report.engine.layout.TextStyle;
import org.eclipse.birt.report.engine.layout.emitter.AbstractPage;

public class PPTPage extends AbstractPage
{

	private PPTWriter writer;
	private boolean isDisposed;

	public PPTPage( int pageWidth, int pageHeight, Color backgroundColor,
			PPTWriter writer )
	{
		super( pageWidth, pageHeight );
		writer.newPage( this.pageWidth, this.pageHeight, backgroundColor );
		this.writer = writer;
		this.isDisposed = false;
	}

	public void clipRestore( )
	{
	}

	public void clipSave( )
	{
	}

	public void dispose( )
	{
		if ( !isDisposed )
		{
			writer.endPage( );
			isDisposed = true;
		}
	}

	protected void clip( float startX, float startY, float width, float height )
	{
	}
	
	protected void drawBackgroundColor( Color color, float x, float y,
			float width, float height )
	{
		writer.drawBackgroundColor( color, x, y, width, height );
	}

	protected void drawBackgroundImage( float x, float y, float width,
			float height, String repeat, String imageUrl, float absPosX,
			float absPosY ) throws IOException
	{
		writer.drawBackgroundImage( imageUrl, x, y, width, height, absPosX,
				absPosY, repeat );
	}

	protected void drawImage( byte[] imageData, String extension, float imageX,
			float imageY, float height, float width, String helpText )
			throws Exception
	{
		writer.drawImage( imageData, extension, imageX, imageY, height, width,
				helpText );
	}

	protected void drawImage( String uri, String extension, float imageX,
			float imageY, float height, float width, String helpText )
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

		drawImage( imageData, extension, imageX, imageY, height, width,
				helpText );
	}

	protected void drawLine( float startX, float startY, float endX,
			float endY, float width, Color color, String lineStyle )
	{
		writer.drawLine( startX, startY, endX, endY, width, color, lineStyle );
	}

	protected void drawText( String text, float textX, float textY, float baseline,
			float width, float height, TextStyle textStyle )
	{
		writer.drawText( text, textX, textY, width, height, textStyle
				.getFontInfo( ), textStyle.getColor( ) );
	}
}
