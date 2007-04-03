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
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.report.engine.emitter.postscript.PostScriptWriter;
import org.eclipse.birt.report.engine.layout.IPage;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

public class PostscriptPage implements IPage
{

	private PostScriptWriter writer;
	private boolean isDisposed;

	public PostscriptPage( PostScriptWriter writer )
	{
		this.writer = writer;
		this.isDisposed = false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#setPageSize(float, float)
	 */
	void setPageSize( float pageWidth, float pageHeight )
	{
		writer.startPage( pageWidth, pageHeight );
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawText(java.lang.String, float, float, org.eclipse.birt.report.engine.layout.pdf.font.FontInfo, float, float, java.awt.Color, boolean, boolean, boolean)
	 */
	public void drawText( String text, float textX, float textY,
			FontInfo fontInfo, float characterSpacing, float wordSpacing,
			Color color, boolean linethrough, boolean overline,
			boolean underline )
	{
		writer.drawString( text, textX, textY, fontInfo, characterSpacing,
				wordSpacing, color, linethrough, overline, underline );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawImage(java.io.InputStream, float, float, float, float)
	 */
	public void drawImage( InputStream input, float imageX, float imageY,
			float height, float width ) throws Exception
	{
		writer.drawImage( input, imageX, imageY, width, height );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawLine(float, float, float, float, float, java.awt.Color, java.lang.String)
	 */
	public void drawLine( float startX, float startY, float endX, float endY,
			float width, Color color, String lineStyle )
	{
		writer.drawLine( startX, startY, endX, endY, width, color, lineStyle );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawBackgroundColor(java.awt.Color, float, float, float, float)
	 */
	public void drawBackgroundColor( Color color, float x, float y,
			float width, float height )
	{
		if ( color != null )
		{
			writer.fillRect( x, y, width, height, color );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawPageBackgroundColor(java.awt.Color)
	 */
	public void drawPageBackgroundColor( Color color )
	{
		writer.fillPage( color );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPageDevice#drawBackgroundImage(float, float, float, float, java.lang.String, java.lang.String, float, float)
	 */
	public void drawBackgroundImage( float x, float y, float width,
			float height, String repeat, String imageUrl, float absPosX,
			float absPosY ) throws IOException
	{
		writer.drawBackgroundImage( imageUrl, x, y, width, height, absPosX,
				absPosY, repeat );
	}

	public void clip( float startX, float startY, float width, float height )
	{
		writer.clipRect( startX, startY, width, height );
	}

	public void clipSave( )
	{
		writer.clipSave( );
	}

	public void clipRestore( )
	{
		writer.clipRestore( );
	}
}
