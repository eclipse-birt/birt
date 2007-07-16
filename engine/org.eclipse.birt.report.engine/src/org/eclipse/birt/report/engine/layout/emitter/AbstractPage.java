/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.emitter;

import java.awt.Color;
import java.io.IOException;

import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.TextStyle;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;


public abstract class AbstractPage implements IPage
{
	protected float pageWidth, pageHeight;
	
	public AbstractPage( int pageWidth, int pageHeight )
	{
		this.pageWidth = convertToPoint( pageWidth );
		this.pageHeight = convertToPoint( pageHeight );
	}
	 
	public void clip( int startX, int startY, int width, int height )
	{
		clip( convertToPoint( startX ), convertToPoint( startY ),
				convertToPoint( width ), convertToPoint( height ) );
	}

	public void clipRestore( )
	{
	}

	public void clipSave( )
	{
	}

	public void dispose( )
	{
	}

	public void drawBackgroundColor( Color color, int x, int y, int width,
			int height )
	{
		drawBackgroundColor( color, convertToPoint( x ), convertToPoint( y ),
				convertToPoint( width ), convertToPoint( height ) );
	}

	public void drawBackgroundImage( int x, int y, int width, int height,
			String repeat, String imageUrl, int absPosX, int absPosY )
			throws IOException
	{
		drawBackgroundImage( convertToPoint( x ), convertToPoint( y ),
				convertToPoint( width ), convertToPoint( height ), repeat,
				imageUrl, convertToPoint( absPosX ), convertToPoint( absPosY ) );
	}

	public void drawImage( byte[] imageData, String extension, int imageX,
			int imageY, int height, int width, String helpText )
			throws Exception
	{
		drawImage( imageData, extension, convertToPoint( imageX ),
				convertToPoint( imageY ), convertToPoint( height ),
				convertToPoint( width ), helpText );
	}

	public void drawImage( String uri, String extension, int imageX,
			int imageY, int height, int width, String helpText )
			throws Exception
	{
		drawImage( uri, extension, convertToPoint( imageX ),
				convertToPoint( imageY ), convertToPoint( height ),
				convertToPoint( width ), helpText );
	}

	public void drawLine( int startX, int startY, int endX, int endY,
			int width, Color color, String lineStyle )
	{
		drawLine( convertToPoint( startX ), convertToPoint( startY ),
				convertToPoint( endX ), convertToPoint( endY ),
				convertToPoint( width ), color, lineStyle );
	}

	public void drawText( String text, int textX, int textY, int textWidth,
			int textHeight, TextStyle textStyle )
	{
		float x = convertToPoint( textX );
		float y = convertToPoint( textY );
		float width = convertToPoint( textWidth );
		float height = convertToPoint( textHeight );
		FontInfo fontInfo = textStyle.getFontInfo( );
		float baseline = convertToPoint( fontInfo.getBaseline( ) );
		drawText( text, x, y, baseline , width, height, textStyle );
		float lineWidth = fontInfo.getLineWidth( );
		Color color = textStyle.getColor( );
		if ( textStyle.isLinethrough( ) )
		{
			drawDecorationLine( x, y, width, lineWidth,
					convertToPoint( fontInfo.getLineThroughPosition( ) ), color );
		}
		if ( textStyle.isOverline( ) )
		{
			drawDecorationLine( x, y, width, lineWidth,
					convertToPoint( fontInfo.getOverlinePosition( ) ), color );
		}
		if ( textStyle.isUnderline( ) )
		{
			drawDecorationLine( x, y, width, lineWidth,
					convertToPoint( fontInfo.getUnderlinePosition( ) ), color );
		}
	}
	
	private void drawDecorationLine( float textX, float textY, float width,
			float lineWidth, float verticalOffset, Color color )
	{
		textY = textY + verticalOffset;
		drawLine( textX, textY, textX + width, textY, lineWidth, color,
				"solid" ); //$NON-NLS-1$
	}

	protected abstract void clip( float startX, float startY, float width, float height );

	protected abstract void drawBackgroundColor( Color color, float x, float y, float width,
			float height );

	protected abstract void drawBackgroundImage( float x, float y, float width, float height,
			String repeat, String imageUrl, float absPosX, float absPosY )
			throws IOException;

	protected abstract void drawImage( byte[] imageData, String extension, float imageX,
			float imageY, float height, float width, String helpText )
			throws Exception;

	protected abstract void drawImage( String uri, String extension, float imageX,
			float imageY, float height, float width, String helpText )
			throws Exception;

	protected abstract void drawLine( float startX, float startY, float endX, float endY,
			float width, Color color, String lineStyle );

	protected abstract void drawText( String text, float textX, float textY, float baseline, float width,
			float height, TextStyle textStyle );

	protected float convertToPoint( int value )
	{
		return value / PDFConstants.LAYOUT_TO_PDF_RATIO;
	}

	protected float transformY( float y )
	{
		return pageHeight - y;
	}

	protected float transformY( float y, float height )
	{
		return pageHeight - y - height;
	}

	protected float transformY( float y, float height, float containerHeight )
	{
		return containerHeight - y - height;
	}

}
