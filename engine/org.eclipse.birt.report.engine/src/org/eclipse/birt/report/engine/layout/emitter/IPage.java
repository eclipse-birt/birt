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
import java.util.Map;

import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

public interface IPage {

	void dispose();

	/**
	 * Saves last graphic state, and clips a rectangle area.
	 * 
	 * @param startX x coordinate of left upper corner.
	 * @param startY y coordinate of left upper corner.
	 * @param width  width of the area.
	 * @param height height of the area.
	 */
	void startClip(int startX, int startY, int width, int height);

	/**
	 * restores last graphic state.
	 */
	void endClip();

	/**
	 * Draws text at specified position with specified styles.
	 * 
	 * @param text
	 * @param textX
	 * @param textY
	 * @param width
	 * @param height
	 * @param textStyle
	 */
	void drawText(String text, int textX, int textY, int width, int height, TextStyle textStyle);

	void drawImage(String imageId, byte[] imageData, String extension, int imageX, int imageY, int height, int width,
			String helpText, Map params) throws Exception;

	void drawImage(String uri, String extension, int imageX, int imageY, int height, int width, String helpText,
			Map params) throws Exception;

	/**
	 * Draws a line from the start position to the end position with the given line
	 * width, color, and style.
	 * 
	 * @param startX    the start X coordinate of the line
	 * @param startY    the start Y coordinate of the line
	 * @param endX      the end X coordinate of the line
	 * @param endY      the end Y coordinate of the line
	 * @param width     the lineWidth
	 * @param color     the color of the line
	 * @param lineStyle the given line style
	 */
	void drawLine(int startX, int startY, int endX, int endY, int width, Color color, int lineStyle);

	/**
	 * Draws the background color at the contentByteUnder of the pdf
	 * 
	 * @param color  the color to be drawn
	 * @param x      the start X coordinate
	 * @param y      the start Y coordinate
	 * @param width  the width of the background dimension
	 * @param height the height of the background dimension
	 */
	void drawBackgroundColor(Color color, int x, int y, int width, int height);

	void drawBackgroundImage(int x, int y, int width, int height, int imageWidth, int imageHeight, int repeat,
			String imageUrl, byte[] imageData, int absPosX, int absPosY) throws Exception;

	void showHelpText(String text, int x, int y, int width, int height);
}