/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.emitter.odp.device;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;

import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.emitter.odp.OdpContext;
import org.eclipse.birt.report.engine.emitter.odp.OdpWriter;
import org.eclipse.birt.report.engine.emitter.odp.util.OdpUtil;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.emitter.AbstractPage;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.AreaConstants;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.odf.OdfUtil;
import org.eclipse.birt.report.engine.odf.pkg.ImageEntry;
import org.eclipse.birt.report.engine.odf.pkg.ImageManager;
import org.eclipse.birt.report.engine.odf.style.HyperlinkInfo;
import org.eclipse.birt.report.engine.odf.style.StyleBuilder;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.w3c.dom.css.CSSPrimitiveValue;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

/**
 * @since 3.3
 *
 */
public class OdpPage extends AbstractPage {

	private OdpWriter writer;
	private boolean isDisposed;
	private HyperlinkInfo link;
	private OdpContext context;

	private StyleEntry textFrameStyle;

	/**
	 * @param pageWidth
	 * @param pageHeight
	 * @param backgroundColor
	 * @param writer
	 * @param context
	 */
	public OdpPage(int pageWidth, int pageHeight, Color backgroundColor, OdpWriter writer, OdpContext context) {
		super(pageWidth, pageHeight);
		this.context = context;

		writer.newPage(this.pageWidth, this.pageHeight, backgroundColor,
				context.getMasterPageManager().getCurrentMasterPage());
		this.writer = writer;
		this.isDisposed = false;

		textFrameStyle = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_DRAW);
		textFrameStyle.setProperty(StyleConstant.GRAPHIC_FILL, "none"); //$NON-NLS-1$
		textFrameStyle.setProperty(StyleConstant.GRAPHIC_STROKE, "none"); //$NON-NLS-1$
		context.addStyle(textFrameStyle);
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
	}

	@Override
	protected void drawBackgroundColor(Color color, float x, float y, float width, float height) {
		if (color == null) {
			return;
		}

		StyleEntry rectStyle = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_DRAW);
		rectStyle.setProperty(StyleConstant.GRAPHIC_FILL_COLOR, OdpUtil.getColorString(color));
		rectStyle.setProperty(StyleConstant.GRAPHIC_FILL, "solid"); //$NON-NLS-1$
		rectStyle.setProperty(StyleConstant.GRAPHIC_STROKE, "none"); //$NON-NLS-1$
		context.addStyle(rectStyle);
		writer.drawBackgroundColor(x, y, width, height, rectStyle);
	}

	protected void drawBackgroundImage(float x, float y, float width, float height, float imageWidth, float imageHeight,
			int repeat, String imageUrl, float absPosX, float absPosY) throws IOException {
		if (imageUrl == null) {
			return;
		}

		ImageEntry entry = context.getImageManager().addImage(imageUrl, null, null);

		org.eclipse.birt.report.engine.layout.emitter.Image image = entry.getImage();
		if (imageWidth == 0) {
			imageWidth = image.getWidth();
		}

		if (imageHeight == 0) {
			imageHeight = image.getHeight();
		}

		writer.drawBackgroundImage(entry.getUri(), x, y, width, height, imageWidth, imageHeight, absPosX, absPosY,
				repeat);
	}

	@Override
	protected void drawImage(String imageId, byte[] imageData, String extension, float imageX, float imageY,
			float height, float width, String helpText, Map params) throws Exception {

		if (extension == null && imageId != null) {
			extension = "." + ImageManager.getImageExtension(imageId); //$NON-NLS-1$
		}

		ImageEntry entry = context.getImageManager().addImage(imageData, extension);
		writer.drawImage(imageId, null, entry.getUri(), extension, imageX, imageY, height, width, helpText, link);

	}

	@Override
	protected void drawImage(String imageId, String extension, float imageX, float imageY, float height, float width,
			String helpText, Map params) throws Exception {
		if (imageId == null) {
			return;
		}
		ImageEntry entry = context.getImageManager().addImage(imageId, null, extension);

		writer.drawImage(imageId, null, entry.getUri(), extension, imageX, imageY, height, width, helpText, link);
	}

	@Override
	protected void drawLine(float startX, float startY, float endX, float endY, float width, Color color,
			int lineStyle) {
		if (null == color || 0f == width || lineStyle == AreaConstants.BORDER_STYLE_NONE) {
			return;
		}

		StyleEntry entry = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_DRAW);
		entry.setProperty(StyleConstant.COLOR_PROP, OdpUtil.getColorString(color));
		entry.setProperty(StyleConstant.GRAPHIC_STROKE_WIDTH, width / OdfUtil.INCH_PT);

		if (lineStyle == AreaConstants.BORDER_STYLE_DASHED || lineStyle == AreaConstants.BORDER_STYLE_DOTTED) {
			entry.setProperty(StyleConstant.GRAPHIC_STROKE, "dash"); //$NON-NLS-1$
			// TODO: dash style, which is quite complex to implement
		} else {
			entry.setProperty(StyleConstant.GRAPHIC_STROKE, "solid"); //$NON-NLS-1$
		}

		context.addStyle(entry);

		writer.drawLine(startX, startY, endX, endY, entry);
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
	}

	@Override
	protected void drawText(String text, float textX, float textY, float baseline, float width, float height,
			TextStyle textStyle) {
		// width of text is enlarged by 1 point because the text will be
		// automatically wrapped if the width of textbox equals to the width of
		// text exactly.
		FontInfo fontInfo = textStyle.getFontInfo();
		float descend = fontInfo.getBaseFont().getFontDescriptor(BaseFont.DESCENT, fontInfo.getFontSize());

		StyleEntry style = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_TEXT);
		style.setProperty(StyleConstant.DIRECTION_PROP, textStyle.getDirection());
		style.setProperty(StyleConstant.COLOR_PROP, OdpUtil.getColorString(textStyle.getColor()));
		style.setProperty(StyleConstant.LETTER_SPACING, new FloatValue(CSSPrimitiveValue.CSS_PT,
				textStyle.getLetterSpacing() / PDFConstants.LAYOUT_TO_PDF_RATIO));

		if (fontInfo != null) {
			BaseFont baseFont = fontInfo.getBaseFont();
			String fontName = OdpUtil.getFontName(baseFont);

			style.setProperty(StyleConstant.FONT_FAMILY_PROP, fontName);
			style.setProperty(StyleConstant.FONT_SIZE_PROP, Double.valueOf(fontInfo.getFontSize()));

			if ((fontInfo.getFontStyle() & Font.BOLD) != 0) {
				style.setProperty(StyleConstant.FONT_WEIGHT_PROP, "bold"); //$NON-NLS-1$
			}

			if ((fontInfo.getFontStyle() & Font.ITALIC) != 0) {
				style.setProperty(StyleConstant.FONT_STYLE_PROP, "italic"); //$NON-NLS-1$
			}

			if (textStyle.isLinethrough()) {
				style.setProperty(StyleConstant.TEXT_LINE_THROUGH_PROP, true);
			}

			if (textStyle.isOverline()) {
				style.setProperty(StyleConstant.TEXT_OVERLINE_PROP, true);
			}

			if (textStyle.isUnderline()) {
				style.setProperty(StyleConstant.TEXT_UNDERLINE_PROP, true);
			}
		}

		// TODO: hyperlink

		context.addStyle(style);
		writer.drawText(text, textX, textY, width, height + descend * 0.6f, textFrameStyle, style, link);
	}

	/**
	 * @param link
	 */
	public void setLink(HyperlinkInfo link) {
		this.link = link;
	}

	@Override
	protected void drawBackgroundImage(float x, float y, float width, float height, float imageWidth, float imageHeight,
			int repeat, String imageUrl, byte[] imageData, float absPosX, float absPosY) throws IOException {
		drawBackgroundImage(x, y, width, height, imageWidth, imageHeight, repeat, imageUrl, absPosX, absPosY);
	}

}
