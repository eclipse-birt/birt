/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.odp;

import java.awt.Color;
import java.io.OutputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.layout.emitter.util.BackgroundImageLayout;
import org.eclipse.birt.report.engine.layout.emitter.util.Position;
import org.eclipse.birt.report.engine.odf.OdfUtil;
import org.eclipse.birt.report.engine.odf.style.HyperlinkInfo;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.eclipse.birt.report.engine.odf.writer.AbstractOdfWriter;

/**
 * Body writer for the OpenDocument Presentation format.
 *
 */
@SuppressWarnings("nls")
public class OdpWriter extends AbstractOdfWriter {
	protected static Logger logger = Logger.getLogger(OdpRender.class.getName());

	protected int currentPageNum = 0;

	protected float pageWidth, pageHeight;

	public OdpWriter(OutputStream out) throws Exception {
		this(out, "UTF-8");
	}

	public OdpWriter(OutputStream out, String encoding) throws Exception {
		writer = new XMLWriter();
		// no indent or newlines, because newlines inside paragraphs are
		// considered as white spaces
		writer.setIndent(false);
		writer.open(out, encoding);
	}

	/**
	 * Creates a ODP Document.
	 * 
	 */
	public void start() {
		writer.openTag("office:body");
		writer.openTag("office:presentation");
	}

	/**
	 * Closes the document.
	 * 
	 */
	public void end() {
		writer.closeTag("office:presentation");
		writer.closeTag("office:body");
		try {
			close();
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
		}
	}

	public void endPage() {
		writer.closeTag("draw:page");
	}

	/**
	 * Creates a new page.
	 * 
	 * @param page the PageArea specified from layout
	 */
	public void newPage(float pageWidth, float pageHeight, Color backgroundColor, String mpName) {
		currentPageNum++;
		if (pageWidth > this.pageWidth) {
			this.pageWidth = pageWidth;
		}
		if (pageHeight > this.pageHeight) {
			this.pageHeight = pageHeight;
		}

		writer.openTag("draw:page");
		writer.attribute("draw:name", "Slide" + currentPageNum);
		if (mpName != null) {
			writer.attribute("draw:master-page-name", mpName);
		}
		// TODO: background color
		// drawBackgroundColor( backgroundColor, 0, 0, pageWidth, pageHeight );
	}

	/**
	 * Draws a chunk of text.
	 * 
	 * @param text              the textArea to be drawn.
	 * @param textX             the X position of the textArea relative to current
	 *                          page.
	 * @param textY             the Y position of the textArea relative to current
	 *                          page.
	 * @param contentByte       the content byte to draw the text.
	 * @param contentByteHeight the height of the content byte.
	 */
	public void drawText(String text, float textX, float textY, float width, float height, StyleEntry frameStyle,
			StyleEntry style, HyperlinkInfo link) {

		writer.openTag("draw:frame");
		if (frameStyle != null) {
			writer.attribute("draw:style-name", frameStyle.getName());
		}

		width /= OdfUtil.INCH_PT;
		height /= OdfUtil.INCH_PT;
		textX /= OdfUtil.INCH_PT;
		textY /= OdfUtil.INCH_PT;

		// TODO: frame style
		writer.attribute("draw:layer", "layout");
		writer.attribute("svg:width", width + "in");
		writer.attribute("svg:height", height + "in");
		writer.attribute("svg:x", textX + "in");
		writer.attribute("svg:y", textY + "in");

		writer.openTag("draw:text-box");
		writer.openTag("text:p");

		writer.openTag("text:span");
		if (style != null) {
			writer.attribute("text:style-name", style.getName());
		}

		openHyperlink(link);
		// note: multi-line is handled by the layout engine, which will create
		// multiple text items for each line
		writeString(text);
		closeHyperlink(link);
		writer.closeTag("text:span");

		writer.closeTag("text:p");
		writer.closeTag("draw:text-box");
		writer.closeTag("draw:frame");
	}

	public void drawImage(String imageId, byte[] imageData, String imageUrl, String extension, float imageX,
			float imageY, float height, float width, String helpText, HyperlinkInfo link) {
		openHyperlink(link, "draw");

		double posX = imageX / OdfUtil.INCH_PT;
		double posY = imageY / OdfUtil.INCH_PT;
		double imageWidth = width / OdfUtil.INCH_PT;
		double imageHeight = height / OdfUtil.INCH_PT;

		drawImage(imageUrl, imageData, posX, posY, imageHeight, imageWidth, null, helpText, "layout", getImageID());

		closeHyperlink(link, "draw");
	}

	/**
	 * Draws a line from the start position to the end position with the given line
	 * width, color, and style
	 * 
	 * @param startX    the start X coordinate of the line
	 * @param startY    the start Y coordinate of the line
	 * @param endX      the end X coordinate of the line
	 * @param endY      the end Y coordinate of the line
	 * @param width     the lineWidth
	 * @param color     the color of the line
	 * @param lineStyle the given line style
	 */
	public void drawLine(double startX, double startY, double endX, double endY, StyleEntry styleEntry) {
		drawRawLine(startX, startY, endX, endY, styleEntry);
	}

	/**
	 * Draws a line with the line-style specified in advance from the start position
	 * to the end position with the given line width, color, and style. If the
	 * line-style is NOT set before invoking this method, "solid" will be used as
	 * the default line-style.
	 * 
	 * @param startX the start X coordinate of the line
	 * @param startY the start Y coordinate of the line
	 * @param endX   the end X coordinate of the line
	 * @param endY   the end Y coordinate of the line
	 * @param width  the lineWidth
	 * @param color  the color of the line
	 */
	private void drawRawLine(double startX, double startY, double endX, double endY, StyleEntry lineStyle) {
		boolean needflip = false;
		if (endX > startX && endY < startY || endX < startX && endY > startY) {
			needflip = true;
		}

		writer.openTag("draw:line");
		writer.attribute("draw:layer", "layout");
		if (lineStyle != null) {
			writer.attribute("draw:style-name", lineStyle.getName());
		}

		startX /= OdfUtil.INCH_PT;
		startY /= OdfUtil.INCH_PT;
		endX /= OdfUtil.INCH_PT;
		endY /= OdfUtil.INCH_PT;

		if (needflip) {
			writer.attribute("svg:x1", startX + "in");
			writer.attribute("svg:y1", endY + "in");
			writer.attribute("svg:x2", endX + "in");
			writer.attribute("svg:y2", startY + "in");
		} else {
			writer.attribute("svg:x1", startX + "in");
			writer.attribute("svg:y1", startY + "in");
			writer.attribute("svg:x2", endX + "in");
			writer.attribute("svg:y2", endY + "in");
		}

		writer.closeTag("draw:line");
	}

	/**
	 * Draws the background color.
	 * 
	 * @param color  the color to be drawn
	 * @param x      the start X coordinate
	 * @param y      the start Y coordinate
	 * @param width  the width of the background dimension
	 * @param height the height of the background dimension
	 */
	public void drawBackgroundColor(double x, double y, double width, double height, StyleEntry rectStyle) {
		writer.openTag("draw:rect");
		if (rectStyle != null) {
			writer.attribute("draw:style-name", rectStyle.getName());
		}

		width /= OdfUtil.INCH_PT;
		height /= OdfUtil.INCH_PT;
		x /= OdfUtil.INCH_PT;
		y /= OdfUtil.INCH_PT;

		writer.attribute("draw:layer", "layout");
		writer.attribute("svg:width", width + "in");
		writer.attribute("svg:height", height + "in");
		writer.attribute("svg:x", x + "in");
		writer.attribute("svg:y", y + "in");
		writer.closeTag("draw:rect");
	}

	/**
	 * Draws the background image at the contentByteUnder with the given offset
	 * 
	 * @param imageURI  the URI referring the image
	 * @param x         the start X coordinate where the image is positioned
	 * @param y         the start Y coordinate where the image is positioned
	 * @param width     the width of the background dimension
	 * @param height    the height of the background dimension
	 * @param positionX the offset X percentage relating to start X
	 * @param positionY the offset Y percentage relating to start Y
	 * @param repeat    the background-repeat property
	 * @param xMode     whether the horizontal position is a percentage value or not
	 * @param yMode     whether the vertical position is a percentage value or not
	 */
	public void drawBackgroundImage(String imageURI, float x, float y, float width, float height, float iWidth,
			float iHeight, float positionX, float positionY, int repeat) {
		if (imageURI == null || imageURI.length() == 0) {
			return;
		}
		float imageWidth = iWidth;
		float imageHeight = iHeight;

		Position areaPosition = new Position(x, y);
		Position areaSize = new Position(width, height);
		Position imagePosition = new Position(x + positionX, y + positionY);
		Position imageSize = new Position(imageWidth, imageHeight);
		BackgroundImageLayout layout = new BackgroundImageLayout(areaPosition, areaSize, imagePosition, imageSize);
		Collection<Position> positions = layout.getImagePositions(repeat);
		for (Position position : positions) {
			drawImage(imageURI, null, imageURI, null, position.getX(), position.getY(), imageHeight, imageWidth, null,
					null);
		}
	}

}
