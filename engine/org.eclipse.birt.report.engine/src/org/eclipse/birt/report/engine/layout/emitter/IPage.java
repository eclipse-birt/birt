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

package org.eclipse.birt.report.engine.layout.emitter;

import java.awt.Color;
import java.io.IOException;

import org.eclipse.birt.report.engine.layout.TextStyle;

public interface IPage
{

	void dispose( );

	/**
	 * Clips a rectangle area.
	 * 
	 * @param startX
	 *            x coordinate of left upper corner.
	 * @param startY
	 *            y coordinate of left upper corner.
	 * @param width
	 *            width of the area.
	 * @param height
	 *            height of the area.
	 */
	void clip( float startX, float startY, float width, float height );
	
	/**
	 * Saves current clip state.
	 */
	void clipSave();
	
	/**
	 * Restores last saved clip state.
	 */
	void clipRestore();
	
	/**
	 * Draws text at specified position with specified styles.
	 * @param text the text.
	 * @param textX p
	 * @param textY
	 * @param fontInfo
	 * @param characterSpacing
	 * @param wordSpacing
	 * @param color
	 * @param linethrough
	 * @param overline
	 * @param underline
	 * @param align align property of the text.
	 */
	void drawText( String text, float textX, float textY, float width,
			float height, TextStyle textStyle );

	void drawImage( byte[] imageData, float imageX, float imageY, float height,
			float width, String helpText ) throws Exception;

	void drawImage( String uri, float imageX, float imageY, float height,
			float width, String helpText ) throws Exception;

	/**
	 * Draws a line from the start position to the end position with the given
	 * linewidth, color, and style at the given pdf layer.
	 * 
	 * @param startX
	 *            the start X coordinate of the line
	 * @param startY
	 *            the start Y coordinate of the line
	 * @param endX
	 *            the end X coordinate of the line
	 * @param endY
	 *            the end Y coordinate of the line
	 * @param width
	 *            the lineWidth
	 * @param color
	 *            the color of the line
	 * @param lineStyle
	 *            the given line style
	 * @param contentByte
	 *            the given pdf layer
	 */
	void drawLine( float startX, float startY, float endX, float endY,
			float width, Color color, String lineStyle );

	/**
	 * Draws the background color at the contentByteUnder of the pdf
	 * 
	 * @param color
	 *            the color to be drawed
	 * @param x
	 *            the start X coordinate
	 * @param y
	 *            the start Y coordinate
	 * @param width
	 *            the width of the background dimension
	 * @param height
	 *            the height of the background dimension
	 */
	void drawBackgroundColor( Color color, float x, float y, float width,
			float height );

	void drawBackgroundImage( float x, float y, float width, float height,
			String repeat, String imageUrl, float absPosX, float absPosY )
			throws IOException;
}