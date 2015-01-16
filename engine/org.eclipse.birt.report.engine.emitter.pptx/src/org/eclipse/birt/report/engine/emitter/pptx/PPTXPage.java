/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pptx;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;

import org.eclipse.birt.report.engine.emitter.ppt.util.PPTUtil.HyperlinkDef;
import org.eclipse.birt.report.engine.emitter.pptx.util.PPTXUtil;
import org.eclipse.birt.report.engine.emitter.pptx.writer.Slide;
import org.eclipse.birt.report.engine.layout.emitter.IPage;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

/**
 * accept draw command, output to PPTXCanvas.
 * 
 * The graphic operations do following processing: 
 * <ol>
 * <li>dimension transformation, convert point to enums. 
 * <li>call canvas to export PPTX operations.
 * </ol>
 */
public class PPTXPage implements IPage
{

	private Slide slide;
	private PPTXCanvas canvas;
	private HyperlinkDef link = null;

	public PPTXPage( Slide slide )
	{
		this.slide = slide;
		this.canvas = slide.getCanvas( );
	}

	public PPTXPage( PPTXCanvas canvas )
	{
		this.canvas = canvas;
		this.slide = null;
	}

	public void drawBackgroundColor( Color color, int x, int y, int width,
			int height )
	{
		x = PPTXUtil.convertToEnums( x );
		y = PPTXUtil.convertToEnums( y );
		width = PPTXUtil.convertToEnums( width );
		height = PPTXUtil.convertToEnums( height );
		canvas.drawBackgroundColor( color, x, y, width, height );
	}

	public void drawBackgroundImage( int x, int y, int width, int height,
			int imageWidth, int imageHeight, int repeat, String imageUrl,
			byte[] imageData, int absPosX, int absPosY ) throws IOException
	{
		x = PPTXUtil.convertToPointer( x );
		y = PPTXUtil.convertToPointer( y );
		width = PPTXUtil.convertToPointer( width );
		height = PPTXUtil.convertToPointer( height );
		absPosX = PPTXUtil.convertToPointer( absPosX );
		absPosY = PPTXUtil.convertToPointer( absPosY );
		imageWidth = PPTXUtil.convertToPointer( imageWidth );
		imageHeight = PPTXUtil.convertToPointer( imageHeight );
		canvas.drawBackgroundImage( x, y, width, height, imageWidth,
				imageHeight, repeat, imageUrl, imageData, absPosX, absPosY );
	}

	public void drawImage( String imageId, byte[] imageData, String extension,
			int imageX, int imageY, int height, int width, String helpText,
			Map parameters ) throws Exception
	{
		imageX = PPTXUtil.convertToEnums( imageX );
		imageY = PPTXUtil.convertToEnums( imageY );
		width = PPTXUtil.convertToEnums( width );
		height = PPTXUtil.convertToEnums( height );
		canvas.drawImage( imageId, imageData, extension, imageX, imageY,
				height, width, helpText, link );
	}

	public void drawImage( String uri, String extension, int imageX,
			int imageY, int height, int width, String helpText, Map parameters )
			throws Exception
	{
		imageX = PPTXUtil.convertToEnums( imageX );
		imageY = PPTXUtil.convertToEnums( imageY );
		width = PPTXUtil.convertToEnums( width );
		height = PPTXUtil.convertToEnums( height );
		canvas.drawImage( uri, extension, imageX, imageY, height, width,
				helpText, link );
	}

	public void drawLine( int startX, int startY, int endX, int endY,
			int width, Color color, int lineStyle )
	{
		startX = PPTXUtil.convertToEnums( startX );
		startY = PPTXUtil.convertToEnums( startY );
		endX = PPTXUtil.convertToEnums( endX );
		endY = PPTXUtil.convertToEnums( endY );
		width = PPTXUtil.convertToEnums( width );
		canvas.drawLine( startX, startY, endX, endY, width, color, lineStyle );
	}

	public void drawText( String text, int textX, int textY, int width,
			int height, TextStyle textStyle )
	{
		textX = PPTXUtil.convertToEnums( textX );
		textY = PPTXUtil.convertToEnums( textY );
		width = PPTXUtil.convertToEnums( width );
		height = PPTXUtil.convertToEnums( height );
		canvas.drawText( text, textX, textY, width + 1, height, textStyle, link );
	}

	public void startClip( int startX, int startY, int width, int height )
	{
		startX = PPTXUtil.convertToEnums( startX );
		startY = PPTXUtil.convertToEnums( startY );
		width = PPTXUtil.convertToEnums( width );
		height = PPTXUtil.convertToEnums( height );
		canvas.startClip( startX, startY, width, height );
	}

	public void endClip( )
	{
		canvas.endClip( );
	}

	public void dispose( )
	{
		if ( slide != null )
		{
			slide.dispose( );
		}
		slide = null;
		canvas = null;
	}

	public void setLink( HyperlinkDef link )
	{
		this.link = link;
	}

	public void showHelpText( String text, int x, int y, int width, int height )
	{
		// PPTX currently does not support popup tooltips.
	}

	public PPTXCanvas getCanvas( )
	{
		return canvas;
	}
}
