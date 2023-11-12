/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.emitter;

import java.awt.Color;
import java.util.Map;

import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

/**
 * Interface to define a page
 *
 * @since 3.3
 *
 */
public interface IPage {

	/**
	 * disposer of the page
	 */
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

	/**
	 * Draw the image
	 *
	 * @param imageId   image id
	 * @param imageData image data
	 * @param extension image type
	 * @param imageX    image x position
	 * @param imageY    image y position
	 * @param height    image height
	 * @param width     image width
	 * @param helpText  help text
	 * @param params    map of parameters
	 * @throws Exception handling exception
	 */
	void drawImage(String imageId, byte[] imageData, String extension, int imageX, int imageY, int height, int width,
			String helpText, Map params) throws Exception;

	/**
	 * Draw the image
	 *
	 * @param uri       image uri
	 * @param extension image type
	 * @param imageX    image x position
	 * @param imageY    image y position
	 * @param height    image height
	 * @param width     image width
	 * @param helpText  help text
	 * @param params    map of parameters
	 * @throws Exception handling exception
	 */
	void drawImage(String uri, String extension, int imageX, int imageY, int height, int width, String helpText,
			Map params)
			throws Exception;

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

	/**
	 * Draw the background image
	 *
	 * @param x           image x position
	 * @param y           image y position
	 * @param width       with
	 * @param height      height
	 * @param imageWidth  image width
	 * @param imageHeight image height
	 * @param repeat      repeat the image on background
	 * @param imageUrl    image URL
	 * @param imageData   image data
	 * @param absPosX     absolute x position
	 * @param absPosY     absolute y position
	 * @throws Exception handling exception
	 */
	void drawBackgroundImage(int x, int y, int width, int height, int imageWidth, int imageHeight, int repeat,
			String imageUrl, byte[] imageData, int absPosX, int absPosY) throws Exception;

	/**
	 * Show the help text
	 *
	 * @param text   help text
	 * @param x      x position
	 * @param y      y position
	 * @param width  width
	 * @param height height
	 */
	void showHelpText(String text, int x, int y, int width, int height);
}
