/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.ppt.device;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.engine.emitter.ppt.PPTWriter;
import org.eclipse.birt.report.engine.emitter.ppt.util.PPTUtil.HyperlinkDef;
import org.eclipse.birt.report.engine.layout.emitter.AbstractPage;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

public class PPTPage extends AbstractPage {

	private PPTWriter writer;
	private boolean isDisposed;
	private HyperlinkDef link;

	public PPTPage(int pageWidth, int pageHeight, Color backgroundColor, PPTWriter writer) {
		super(pageWidth, pageHeight);
		writer.newPage(this.pageWidth, this.pageHeight, backgroundColor);
		this.writer = writer;
		this.isDisposed = false;
	}

	@Override
	public void restoreState() {
	}

	@Override
	public void saveState() {
	}

	@Override
	public void dispose() {
		if (!isDisposed) {
			writer.endPage();
			isDisposed = true;
		}
	}

	@Override
	protected void clip(float startX, float startY, float width, float height) {
		writer.clip(startX, startY, width, height);
	}

	@Override
	protected void clipEnd() {
		writer.clipEnd();
	}

	@Override
	protected void drawBackgroundColor(Color color, float x, float y, float width, float height) {
		writer.drawBackgroundColor(color, x, y, width, height);
	}

	@Override
	protected void drawBackgroundImage(float x, float y, float width, float height, float imageWidth, float imageHeight,
			int repeat, String imageUrl, byte[] imageData, float absPosX, float absPosY) throws IOException {
		writer.drawBackgroundImage(imageUrl, imageData, x, y, width, height, imageWidth, imageHeight, absPosX, absPosY,
				repeat);
	}

	@Override
	protected void drawImage(String imageId, byte[] imageData, String extension, float imageX, float imageY,
			float height, float width, String helpText) throws Exception {
		writer.drawImage(imageId, imageData, extension, imageX, imageY, height, width, helpText, link);
	}

	@Override
	protected void drawImage(String uri, String extension, float imageX, float imageY, float height, float width,
			String helpText) throws Exception {
		if (uri == null) {
			return;
		}
		InputStream imageStream = new URL(uri).openStream();
		int data;
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		while ((data = imageStream.read()) != -1) {
			byteArrayOut.write(data);
		}
		drawImage(uri, byteArrayOut.toByteArray(), extension, imageX, imageY, height, width, helpText);
	}

	@Override
	protected void drawLine(float startX, float startY, float endX, float endY, float width, Color color,
			int lineStyle) {
		writer.drawLine(startX, startY, endX, endY, width, color, lineStyle);
	}

	@Override
	protected void drawText(String text, float textX, float textY, float baseline, float width, float height,
			TextStyle textStyle) {
		// width of text is enlarged by 1 point because in ppt the text will be
		// automatically wrapped if the width of textbox equals to the width of
		// text exactly.
		writer.drawText(text, textX, textY, width, height, textStyle, link);
	}

	@Override
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
	}

	public void setLink(HyperlinkDef link) {
		this.link = link;
	}
}
