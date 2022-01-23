/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
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

import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

public abstract class AbstractPage implements IPage {
	protected float pageWidth, pageHeight;

	public AbstractPage(int pageWidth, int pageHeight) {
		this.pageWidth = convertToPoint(pageWidth);
		this.pageHeight = convertToPoint(pageHeight);
	}

	public void startClip(int startX, int startY, int width, int height) {
		saveState();
		clip(convertToPoint(startX), convertToPoint(startY), convertToPoint(width), convertToPoint(height));
	}

	public void endClip() {
		clipEnd();
		restoreState();
	}

	public void dispose() {
	}

	public void drawBackgroundColor(Color color, int x, int y, int width, int height) {
		drawBackgroundColor(color, convertToPoint(x), convertToPoint(y), convertToPoint(width), convertToPoint(height));
	}

	public void drawBackgroundImage(int x, int y, int width, int height, int imageWidth, int imageHeight, int repeat,
			String imageUrl, byte[] imageData, int absPosX, int absPosY) throws Exception {
		if (imageData == null || imageData.length == 0) {
			return;
		}
		drawBackgroundImage(convertToPoint(x), convertToPoint(y), convertToPoint(width), convertToPoint(height),
				convertToPoint(imageWidth), convertToPoint(imageHeight), repeat, imageUrl, imageData,
				convertToPoint(absPosX), convertToPoint(absPosY));
	}

	public void drawImage(String imageId, byte[] imageData, String extension, int imageX, int imageY, int height,
			int width, String helpText, Map params) throws Exception {
		drawImage(imageId, imageData, extension, convertToPoint(imageX), convertToPoint(imageY), convertToPoint(height),
				convertToPoint(width), helpText, params);
	}

	/**
	 * @deprecated
	 */
	public void drawImage(String uri, String extension, int imageX, int imageY, int height, int width, String helpText,
			Map params) throws Exception {
		drawImage(uri, extension, convertToPoint(imageX), convertToPoint(imageY), convertToPoint(height),
				convertToPoint(width), helpText, params);
	}

	public void drawLine(int startX, int startY, int endX, int endY, int width, Color color, int lineStyle) {
		drawLine(convertToPoint(startX), convertToPoint(startY), convertToPoint(endX), convertToPoint(endY),
				convertToPoint(width), color, lineStyle);
	}

	public void drawText(String text, int textX, int textY, int textWidth, int textHeight, TextStyle textStyle) {
		float x = convertToPoint(textX);
		float y = convertToPoint(textY);
		float width = convertToPoint(textWidth);
		float height = convertToPoint(textHeight);
		FontInfo fontInfo = textStyle.getFontInfo();
		float baseline = convertToPoint(fontInfo.getBaseline());
		drawText(text, x, y, baseline, width, height, textStyle);
		float lineWidth = fontInfo.getLineWidth();
		Color color = textStyle.getColor();
		if (textStyle.isLinethrough()) {
			drawDecorationLine(x, y, width, lineWidth, convertToPoint(fontInfo.getLineThroughPosition()), color);
		}
		if (textStyle.isOverline()) {
			drawDecorationLine(x, y, width, lineWidth, convertToPoint(fontInfo.getOverlinePosition()), color);
		}
		if (textStyle.isUnderline()) {
			drawDecorationLine(x, y, width, lineWidth, convertToPoint(fontInfo.getUnderlinePosition()), color);
		}
	}

	public void showHelpText(String text, int textX, int textY, int width, int height) {
		showHelpText(text, convertToPoint(textX), convertToPoint(textY), convertToPoint(width), convertToPoint(height));
	}

	protected void drawDecorationLine(float textX, float textY, float width, float lineWidth, float verticalOffset,
			Color color) {
		textY = textY + verticalOffset;
		drawLine(textX, textY, textX + width, textY, lineWidth, color, BorderInfo.BORDER_STYLE_SOLID); // $NON-NLS-1$
	}

	protected abstract void clip(float startX, float startY, float width, float height);

	protected void clipEnd() {

	}

	protected abstract void saveState();

	protected abstract void restoreState();

	protected abstract void drawBackgroundColor(Color color, float x, float y, float width, float height);

	protected abstract void drawBackgroundImage(float x, float y, float width, float height, float imageWidth,
			float imageHeight, int repeat, String imageUrl, byte[] imageData, float absPosX, float absPosY)
			throws Exception;

	protected abstract void drawImage(String imageId, byte[] imageData, String extension, float imageX, float imageY,
			float height, float width, String helpText, Map params) throws Exception;

	protected abstract void drawImage(String uri, String extension, float imageX, float imageY, float height,
			float width, String helpText, Map params) throws Exception;

	protected abstract void drawLine(float startX, float startY, float endX, float endY, float width, Color color,
			int lineStyle);

	protected abstract void drawText(String text, float textX, float textY, float baseline, float width, float height,
			TextStyle textStyle);

	protected void showHelpText(String text, float textX, float textY, float width, float height) {

	}

	protected float convertToPoint(int value) {
		return value / PDFConstants.LAYOUT_TO_PDF_RATIO;
	}

	protected float transformY(float y) {
		return pageHeight - y;
	}

	protected float transformY(float y, float height) {
		return pageHeight - y - height;
	}

	protected float transformY(float y, float height, float containerHeight) {
		return containerHeight - y - height;
	}

}
