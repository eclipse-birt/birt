/*******************************************************************************
 * Copyright (c) 2004, 2007, 2024, 2025 Actuate Corporation and others
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

package org.eclipse.birt.report.engine.emitter.pdf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.print.PrintTranscoder;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.layout.emitter.AbstractPage;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.AreaConstants;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.util.SvgFile;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.LayoutProcessor;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfRectangle;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfTextArray;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Definition of the PDF page
 *
 * @since 3.3
 *
 */
public class PDFPage extends AbstractPage {

	/**
	 * The PDF Writer
	 */
	protected PdfWriter writer = null;

	/**
	 * ContentByte layer for PDF
	 */
	protected PdfContentByte contentByte = null;

	protected static Logger logger = Logger.getLogger(PDFPage.class.getName());

	protected float containerHeight;

	protected PDFPageDevice pageDevice;

	protected short artifactDepth = 0;

	/**
	 * font size must greater than minimum font . if not,illegalArgumentException
	 * will be thrown.
	 */
	private static float MIN_FONT_SIZE = 1.0E-4f;

	private static Pattern PAGE_LINK_PATTERN = Pattern
			.compile("^((([a-zA-Z]:))(/(\\w[\\w ]*.*))+\\.(pdf|PDF))+#page=(\\d+)$");

	private final boolean isTagged;
	/**
	 * Constructor of the PDF page
	 *
	 * @param pageWidth
	 * @param pageHeight
	 * @param document
	 * @param writer
	 * @param pageDevice
	 */
	public PDFPage(int pageWidth, int pageHeight, Document document, PdfWriter writer, PDFPageDevice pageDevice) {
		super(pageWidth, pageHeight);
		this.writer = writer;
		this.pageDevice = pageDevice;
		this.containerHeight = this.pageHeight;
		Rectangle pageSize = new Rectangle(this.pageWidth, this.pageHeight);
		document.setPageSize(pageSize);
		isTagged = writer.isTagged();
		if (!document.isOpen()) {
			document.open();
			if (isTagged) {
				pageDevice.initStructure();
			}
		} else {
			document.newPage();
		}

		this.contentByte = writer.getDirectContent();
	}

	@Override
	protected void clip(float startX, float startY, float width, float height) {
		startY = transformY(startY, height);
		contentByte.rectangle(startX, startY, width, height);
		contentByte.clip();
		contentByte.newPath();
	}

	@Override
	protected void restoreState() {
		contentByte.restoreState();
	}

	@Override
	protected void saveState() {
		contentByte.saveState();
	}

	@Override
	public void dispose() {
	}

	@Override
	protected void drawBackgroundColor(Color color, float x, float y, float width, float height) {
		if (null == color) {
			return;
		}
		y = transformY(y, height);
		PdfDictionary properties = new PdfDictionary();
		properties.put(PdfNames.TYPE, PdfNames.BACKGROUND);
		beginArtifact(properties);
		contentByte.saveState();
		contentByte.setColorFill(color);
		contentByte.concatCTM(1, 0, 0, 1, x, y);
		contentByte.rectangle(0, 0, width, height);
		contentByte.fill();
		contentByte.restoreState();
		endArtifact();
	}


	@Override
	protected void drawBackgroundImage(float x, float y, float width, float height, float imageWidth, float imageHeight,
			int repeat, String imageUrl, byte[] imageData, float offsetX, float offsetY) throws Exception {
		PdfDictionary properties = new PdfDictionary();
		properties.put(PdfNames.TYPE, PdfNames.BACKGROUND);
		beginArtifact(properties);
		contentByte.saveState();
		clip(x, y, width, height);

		PdfTemplate image = null;
		if (imageUrl != null) {
			if (pageDevice.getImageCache().containsKey(imageUrl)) {
				image = pageDevice.getImageCache().get(imageUrl);
			}
		}
		if (image == null) {

			// SVG images
			try {
				if (this.pageDevice.useBackgroundImageSvg() && SvgFile.isSvg(null, null, imageUrl)
						&& (new File((new URL(imageUrl)).toURI().getPath())).exists()) {
					image = transSVG(imageUrl, null, imageHeight, imageWidth);
					if (image != null) {
						drawImage(image, x, y, imageHeight, imageWidth, null);
					}
				}
			} catch (Exception e) {
				// default handling, use raster image
				logger.log(Level.WARNING,
						"SVG file not usable, the raster image will be used instead. " + e.getMessage());
			}

			// raster images & fallback of SVG images
			if (image == null) {
				Image img = Image.getInstance(imageData);
				if (imageHeight == 0 || imageWidth == 0) {
					int resolutionX = img.getDpiX();
					int resolutionY = img.getDpiY();
					if (0 == resolutionX || 0 == resolutionY) {
						resolutionX = 96;
						resolutionY = 96;
					}
					imageWidth = img.getPlainWidth() / resolutionX * 72;
					imageHeight = img.getPlainHeight() / resolutionY * 72;
				}

				image = contentByte.createTemplate(imageWidth, imageHeight);
				image.addImage(img, imageWidth, 0, 0, imageHeight, 0, 0);
			}

			if (imageUrl != null && image != null) {
				pageDevice.getImageCache().put(imageUrl, image);
			}
		}

		boolean xExtended = (repeat & AreaConstants.REPEAT_X) == AreaConstants.REPEAT_X;
		boolean yExtended = (repeat & AreaConstants.REPEAT_Y) == AreaConstants.REPEAT_Y;

		float originalX = offsetX;
		float originalY = offsetY;
		if (xExtended) {
			while (originalX > 0) {
				originalX -= imageWidth;
			}
		}
		if (yExtended) {
			while (originalY > 0) {
				originalY -= imageHeight;
			}
		}

		float startY = originalY;
		do {
			float startX = originalX;
			do {
				drawImage(image, x + startX, y + startY, imageWidth, imageHeight);
				startX += imageWidth;
			} while (startX < width && xExtended);
			startY += imageHeight;
		} while (startY < height && yExtended);
		contentByte.restoreState();
		endArtifact();
	}

	@Override
	protected void drawImage(String imageId, byte[] imageData, String extension, float imageX, float imageY,
			float height, float width, String helpText, Map params) throws Exception {

		if (isTagged && artifactDepth == 0) {
			pageDevice.structureCurrentNode.put(PdfNames.ALT, new PdfString(helpText));

			PdfDictionary attributes = pageDevice.structureCurrentNode.getAsDict(PdfName.A);
			if (attributes == null) {
				attributes = new PdfDictionary();
				pageDevice.structureCurrentNode.put(PdfName.A, attributes);
			}
			attributes.put(PdfName.BBOX, new PdfRectangle(imageX, imageY, width, height));

			contentByte.beginMarkedContentSequence(pageDevice.structureCurrentNode);
		}

		// Cached Image
		PdfTemplate template = null;
		if (imageId != null) {
			if (pageDevice.getImageCache().containsKey(imageId)) {
				template = pageDevice.getImageCache().get(imageId);
			}
			if (template != null) {
				drawImage(template, imageX, imageY, height, width, helpText);
				if (isTagged && artifactDepth == 0) {
					contentByte.endMarkedContentSequence();
				}
				return;
			}
		}

		// Not cached yet
		if (SvgFile.isSvg(null, null, extension)) {
			template = generateTemplateFromSVG(imageData, height, width);
		} else {
			// PNG/JPG/BMP... images:
			Image image = Image.getInstance(imageData);
			image.setAlt(helpText);

			// Transparent images are not allowed in PDF/A-1,
			// so remove transparency if necessary
			if (image.isSmask() && pageDevice.isPdfAFormat()) {
				if (pageDevice.getPdfConformance().startsWith("PDF.A1")) {
					logger.severe("PDF/A-1 format specified, transparency is not allowed for image " + imageId);
					image.setSmask(false);
				}
			}

			if (imageId == null) {
				// image without imageId, not able to cache.
				drawImage(image, imageX, imageY, height, width, helpText);
				if (isTagged && artifactDepth == 0) {
					contentByte.endMarkedContentSequence();
				}
				return;
			}
			template = contentByte.createTemplate(width, height);
			template.addImage(image, width, 0, 0, height, 0, 0);
		}
		// Cache the image
		if (imageId != null && template != null) {
			pageDevice.getImageCache().put(imageId, template);
		}
		if (template != null) {
			drawImage(template, imageX, imageY, height, width, helpText);
		}

		if (isTagged && artifactDepth == 0) {
			contentByte.endMarkedContentSequence();
		}

	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	protected void drawImage(String uri, String extension, float imageX, float imageY, float height, float width,
			String helpText, Map params) throws Exception {
	}

	/**
	 * Draws a line with the line-style specified in advance from the start position
	 * to the end position with the given line width, color, and style at the given
	 * PDF layer. If the line-style is NOT set before invoking this method, "solid"
	 * will be used as the default line-style.
	 *
	 * @param startX    the start X coordinate of the line.
	 * @param startY    the start Y coordinate of the line.
	 * @param endX      the end X coordinate of the line.
	 * @param endY      the end Y coordinate of the line.
	 * @param width     the lineWidth
	 * @param color     the color of the line.
	 * @param lineStyle the style of the line.
	 */
	@Override
	protected void drawLine(float startX, float startY, float endX, float endY, float width, Color color,
			int lineStyle) {
		// if the border does NOT have color or the line width of the border is
		// zero or the lineStyle is "none", just return.
		if (null == color || 0f == width || AreaConstants.BORDER_STYLE_NONE == lineStyle) // $NON-NLS-1$
		{
			return;
		}
		contentByte.saveState();
		if (AreaConstants.BORDER_STYLE_SOLID == lineStyle) // $NON-NLS-1$
		{
			drawRawLine(startX, startY, endX, endY, width, color, contentByte);
		} else if (AreaConstants.BORDER_STYLE_DASHED == lineStyle) // $NON-NLS-1$
		{
			contentByte.setLineDash(3 * width, 2 * width, 0f);
			drawRawLine(startX, startY, endX, endY, width, color, contentByte);
		} else if (AreaConstants.BORDER_STYLE_DOTTED == lineStyle) // $NON-NLS-1$
		{
			contentByte.setLineDash(width, width, 0f);
			drawRawLine(startX, startY, endX, endY, width, color, contentByte);
		} else if (AreaConstants.BORDER_STYLE_DOUBLE == lineStyle) // $NON-NLS-1$
		{
			return;
		}
		// the other line styles, e.g. 'ridge', 'outset', 'groove', 'inset' is
		// NOT supported now.
		// We look it as the default line style -- 'solid'
		else {
			drawRawLine(startX, startY, endX, endY, width, color, contentByte);
		}
		contentByte.restoreState();
	}

	protected void drawDecorationLine(float textX, float textY, float width, float lineWidth, float verticalOffset,
			Color color, boolean artifact) {
		if (artifact) {
			// FIXME: Can we really treat a strike-through line as an artifact?
			// Probably one should mark the text itself as "deleted" instead, but how can we
			// do this?
			PdfDictionary properties = new PdfDictionary();
			properties.put(PdfNames.TYPE, PdfNames.LAYOUT);
			beginArtifact(properties);
			super.drawDecorationLine(textX, textY, width, lineWidth, verticalOffset, color);
			endArtifact();
		} else {
			super.drawDecorationLine(textX, textY, width, lineWidth, verticalOffset, color);
		}
	}

	@Override
	protected void drawDecorationLine(float textX, float textY, float width, float lineWidth, float verticalOffset,
			Color color) {
		drawDecorationLine(textX, textY, width, lineWidth, verticalOffset, color, true);
	}

	@Override
	protected void drawText(String text, float textX, float textY, float baseline, float width, float height,
			TextStyle textStyle) {
		drawText1(text, textX, textY + baseline, textStyle.getFontInfo(),
				convertToPoint(textStyle.getLetterSpacing()), convertToPoint(textStyle.getWordSpacing()),
				textStyle.getColor(), textStyle.getAlign());
		if (textStyle.isHasHyperlink() && textStyle.isHasHyperlinkDecoration()) {
			// FIXME ATM, the underline is marked as Artifact (see drawDecorationLine).
			// I think this is not quite correct and not necessary.
			// LibreOffice does not do it this way.
			// The underline decoration should be included in the /Link element.
			// For this, we probably have to handle the beginMarkedContentSequence and
			// endMarkedContentSequence stuff right here instead of in drawDecorationLine
			// and the overloaded drawText methods.
			FontInfo fontInfo = textStyle.getFontInfo();
			float lineWidth = fontInfo.getLineWidth();
			Color color = textStyle.getColor();
			drawDecorationLine(textX, textY, width, lineWidth, convertToPoint(fontInfo.getUnderlinePosition()), color);
		}
	}

	private void drawText1(String text, float textX, float textY, FontInfo fontInfo,
			float characterSpacing, float wordSpacing, Color color, CSSValue align) {
		drawText(text, textX, textY, fontInfo, characterSpacing, wordSpacing, color, align);
	}

	/**
	 * Draw the total page
	 *
	 * @param text
	 * @param textX
	 * @param textY
	 * @param width
	 * @param height
	 * @param textInfo
	 * @param scale
	 */
	public void drawTotalPage(String text, int textX, int textY, int width, int height, TextStyle textInfo,
			float scale) {
		PdfTemplate template = pageDevice.getPDFTemplate(scale);
		if (template != null) {
			PdfContentByte tempCB = this.contentByte;
			this.containerHeight = template.getHeight();
			this.contentByte = template;
			int artifactDepthOld = artifactDepth;
			if (artifactDepthOld == 0) {
				PdfDictionary properties = new PdfDictionary();
				properties.put(PdfNames.TYPE, PdfNames.PAGINATION);
				beginArtifact();
			}
			drawText(text, textX, textY, width, height, textInfo);
			if (artifactDepthOld == 0) {
				endArtifact();
			}
			this.contentByte = tempCB;
			this.containerHeight = pageHeight;
		}
	}

	/**
	 * Create the bookmarks
	 *
	 * @param bookmark
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void createBookmark(String bookmark, int x, int y, int width, int height) {
		createBookmark(bookmark, convertToPoint(y));
	}

	private void createBookmark(String bookmark, float y) {
		contentByte.localDestination(bookmark, new PdfDestination(PdfDestination.XYZ, -1, transformY(y), 0));
	}

	/**
	 * Create a hyperlink.
	 *
	 * @param hyperlink
	 * @param bookmark
	 * @param targetWindow
	 * @param type
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 *
	 * @return a new PdfAnnotation describing the hyperlink.
	 */
	public PdfAnnotation createHyperlink(String hyperlink, String bookmark, String targetWindow, int type, int x, int y,
			int width, int height) {
		return createHyperlink(hyperlink, bookmark, targetWindow, type, convertToPoint(x), convertToPoint(y),
				convertToPoint(width), convertToPoint(height));
	}

	private PdfAnnotation createHyperlink(String hyperlink, String bookmark, String targetWindow, int type, float x,
			float y,
			float width, float height) {
		y = transformY(y, height);
		PdfAnnotation annotation = new PdfAnnotation(writer, x, y, x + width, y + height,
				createPdfAction(hyperlink, bookmark, targetWindow, type));
		writer.addAnnotation(annotation);
		return annotation;
	}

	/**
	 * Create the total page template
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param scale
	 */
	public void createTotalPageTemplate(int x, int y, int width, int height, float scale) {
		createTotalPageTemplate(convertToPoint(x), convertToPoint(y), convertToPoint(width), convertToPoint(height),
				scale);
	}

	private void createTotalPageTemplate(float x, float y, float width, float height, float scale) {
		PdfTemplate template = null;
		if (pageDevice.hasTemplate(scale)) {
			template = pageDevice.getPDFTemplate(scale);
		} else {
			template = contentByte.createTemplate(width, height);
			pageDevice.setPDFTemplate(scale, template);
		}
		y = transformY(y, height);
		contentByte.saveState();
		contentByte.addTemplate(template, x, y);
		contentByte.restoreState();
	}

	/**
	 * Draws a line with the line-style specified in advance from the start position
	 * to the end position with the given linewidth, color, and style at the given
	 * pdf layer. If the line-style is NOT set before invoking this method, "solid"
	 * will be used as the default line-style.
	 *
	 * @param startX      the start X coordinate of the line
	 * @param startY      the start Y coordinate of the line
	 * @param endX        the end X coordinate of the line
	 * @param endY        the end Y coordinate of the line
	 * @param width       the lineWidth
	 * @param color       the color of the line
	 * @param contentByte the given pdf layer
	 */
	private void drawRawLine(float startX, float startY, float endX, float endY, float width, Color color,
			PdfContentByte contentByte) {
		startY = transformY(startY);
		endY = transformY(endY);
		contentByte.concatCTM(1, 0, 0, 1, startX, startY);
		if (null != color && !Color.BLACK.equals(color)) {
			contentByte.setColorStroke(color);
		}

		contentByte.moveTo(0, 0);
		contentByte.lineTo(endX - startX, endY - startY);

		contentByte.setLineWidth(width);
		contentByte.stroke();
	}

	private void drawText(String text, float textX, float textY, FontInfo fontInfo, float characterSpacing,
			float wordSpacing, Color color, CSSValue align) {
		contentByte.saveState();

		// This is not allowed inbetween beginText/endText, thus it must come first.
		contentByte.concatCTM(1, 0, 0, 1, textX, transformY(textY, 0, containerHeight));

		// start drawing the text content
		contentByte.beginText();

		if (isTagged && artifactDepth == 0) {
			contentByte.beginMarkedContentSequence(pageDevice.structureCurrentNode);
		}

		if (null != color && !Color.BLACK.equals(color)) {
			contentByte.setColorFill(color);
			contentByte.setColorStroke(color);
		}

		BaseFont font = getBaseFont(fontInfo);
		font.setIncludeCidSet(this.pageDevice.isIncludeCidSet());

		float fontSize = fontInfo.getFontSize();
		try {
			// PDF/A: if font not embeddable then use the configured PDF/A fallback font
			if (this.pageDevice.isPdfAFormat() && fontInfo.getBaseFont() != null
					&& !fontInfo.getBaseFont().isEmbedded()) {
				try {
					// PDF/A fallback font must be entered fully qualified with path and file name
					String defaultFontPdfA = this.pageDevice.getDefaultFontPdfA();
					if (defaultFontPdfA != null) {
						if (!LayoutProcessor.isEnabled()) {
							LayoutProcessor.enableKernLiga();
						}
						font = BaseFont.createFont(defaultFontPdfA, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
						font.setIncludeCidSet(this.pageDevice.isIncludeCidSet());
					}
					logger.log(Level.WARNING,
							"PDF/A: " + fontInfo.getFontName() + " not embeddable, fallback font used.");
				} catch (Exception e) {
					logger.log(Level.WARNING,
							"PDF/A: " + fontInfo.getFontName() + " not embeddable." + e.getMessage());
				}
			}
			contentByte.setFontAndSize(font, fontSize);
		} catch (IllegalArgumentException iae) {
			logger.log(Level.WARNING, iae.getMessage());
			// close to zero , increase by one MIN_FONT_SIZE step
			contentByte.setFontAndSize(font, MIN_FONT_SIZE * 2);
		}
		if (characterSpacing != 0) {
			contentByte.setCharacterSpacing(characterSpacing);
		}
		if (wordSpacing != 0) {
			contentByte.setWordSpacing(wordSpacing);
		}
		setTextMatrix(contentByte, fontInfo);
		if ((font.getFontType() == BaseFont.FONT_TYPE_TTUNI) && CSSValueConstants.JUSTIFY_VALUE.equals(align)
				&& wordSpacing > 0) {
			int idx = text.indexOf(' ');
			if (idx >= 0) {
				float spaceCorrection = -wordSpacing * 1000 / fontSize;
				PdfTextArray textArray = new PdfTextArray(text.substring(0, idx));
				int lastIdx = idx;
				while ((idx = text.indexOf(' ', lastIdx + 1)) >= 0) {
					textArray.add(spaceCorrection);
					textArray.add(text.substring(lastIdx, idx));
					lastIdx = idx;
				}
				textArray.add(spaceCorrection);
				textArray.add(text.substring(lastIdx));
				contentByte.showText(textArray);
			} else {
				contentByte.showText(text);
			}
		} else {
			contentByte.showText(text);
		}
		if (isTagged && artifactDepth == 0) {
			contentByte.endMarkedContentSequence();
		}
		contentByte.endText();
		contentByte.restoreState();
	}

	protected BaseFont getBaseFont(FontInfo fontInfo) {
		return fontInfo.getBaseFont();
	}

	/**
	 * Creates a PdfAction.
	 *
	 * @param hyperlink the hyperlink.
	 * @param bookmark  the bookmark.
	 * @param target    if target equals "_blank", the target will be opened in a
	 *                  new window, else the target will be opened in the current
	 *                  window.
	 * @return the created PdfAction.
	 */
	private PdfAction createPdfAction(String hyperlink, String bookmark, String target, int type) {
		// patch from Ales Novy
		if ("_top".equalsIgnoreCase(target) || "_parent".equalsIgnoreCase(target) || "_blank".equalsIgnoreCase(target)
				|| "_self".equalsIgnoreCase(target))
		// Opens the target in a new window.
		{
			if (hyperlink == null) {
				hyperlink = "";
			}
			boolean isUrl = hyperlink.startsWith("http");
			if (!isUrl) {
				Matcher matcher = PAGE_LINK_PATTERN.matcher(hyperlink);
				if (matcher.find()) {
					String fileName = matcher.group(1);
					String pageNumber = matcher.group(matcher.groupCount());
					return new PdfAction(fileName, Integer.parseInt(pageNumber));
				}
			}
			return new PdfAction(hyperlink);
		} else if (type == IHyperlinkAction.ACTION_BOOKMARK) {
			return PdfAction.gotoLocalPage(bookmark, false);
		} else {
			return PdfAction.gotoRemotePage(hyperlink, bookmark, false, false);
		}
	}

	private void setTextMatrix(PdfContentByte cb, FontInfo fi) {

		if (!fi.getSimulation()) {
			cb.setTextMatrix(0, 0);
			return;
		}
		switch (fi.getFontStyle()) {
		case Font.ITALIC: {
			simulateItalic(cb);
			break;
		}
		case Font.BOLD: {
			simulateBold(cb, fi.getFontWeight());
			break;
		}
		case Font.BOLDITALIC: {
			simulateBold(cb, fi.getFontWeight());
			simulateItalic(cb);
			break;
		}
		}
	}

	static HashMap<Integer, Float> fontWeightLineWidthMap = new HashMap<>();
	static {
		fontWeightLineWidthMap.put(500, 0.1f);
		fontWeightLineWidthMap.put(600, 0.185f);
		fontWeightLineWidthMap.put(700, 0.225f);
		fontWeightLineWidthMap.put(800, 0.3f);
		fontWeightLineWidthMap.put(900, 0.5f);
	}

	private void simulateBold(PdfContentByte cb, int fontWeight) {
		cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
		if (fontWeightLineWidthMap.containsKey(fontWeight)) {
			cb.setLineWidth(fontWeightLineWidthMap.get(fontWeight));
		} else {
			cb.setLineWidth(0.225f);
		}
		cb.setTextMatrix(0, 0);
	}

	private void simulateItalic(PdfContentByte cb) {
		float beta = EmitterUtil.ITALIC_HORIZONTAL_COEFFICIENT;
		cb.setTextMatrix(1, 0, beta, 1, 0, 0);
	}

	@Override
	public void showHelpText(String helpText, float x, float y, float width, float height) {
		showHelpText(x, transformY(y, height), width, height, helpText);
	}

	protected void showHelpText(float x, float y, float width, float height, String helpText) {
		Rectangle rectangle = new Rectangle(x, y, x + width, y + height);
		PdfAnnotation annotation = PdfAnnotation.createSquareCircle(writer, rectangle, helpText, true);
		PdfBorderDictionary borderStyle = new PdfBorderDictionary(0, PdfBorderDictionary.STYLE_SOLID, null);
		annotation.setBorderStyle(borderStyle);
		annotation.setFlags(288);
		writer.addAnnotation(annotation);
	}

	protected void drawImage(PdfTemplate image, float imageX, float imageY, float height, float width, String helpText)
			throws DocumentException {
		imageY = transformY(imageY, height);
		contentByte.saveState();
		contentByte.concatCTM(1, 0, 0, 1, imageX, imageY);
		float w = image.getWidth();
		float h = image.getHeight();
		contentByte.addTemplate(image, width / w, 0f / w, 0f / h, height / h, 0f, 0f);
		if (helpText != null) {
			showHelpText(imageX, imageY, width, height, helpText);
		}
		contentByte.restoreState();
	}

	private void drawImage(PdfTemplate image, float imageX, float imageY, float width, float height)
			throws DocumentException {
		drawImage(image, imageX, imageY, height, width, null);
	}

	protected void drawImage(Image image, float imageX, float imageY, float height, float width, String helpText)
			throws DocumentException {
		imageY = transformY(imageY, height);
		contentByte.saveState();
		contentByte.concatCTM(1, 0, 0, 1, imageX, imageY);
		contentByte.addImage(image, width, 0f, 0f, height, 0f, 0f);
		if (helpText != null) {
			showHelpText(imageX, imageY, width, height, helpText);
		}
		contentByte.restoreState();
	}

	protected PdfTemplate generateTemplateFromSVG(byte[] svgData, float height, float width)
			throws Exception {
		return transSVG(null, svgData, height, width);
	}

	protected PdfTemplate transSVG(String svgPath, byte[] svgData, float height, float width)
			throws DocumentException {
		PdfTemplate template = contentByte.createTemplate(width, height);
		Graphics2D g2D = template.createGraphics(width, height);

		PrintTranscoder transcoder = new PrintTranscoder();
		if (null != svgData && svgData.length > 0) {
			transcoder.transcode(new TranscoderInput(new ByteArrayInputStream(svgData)), null);
		} else if (null != svgPath) {
			transcoder.transcode(new TranscoderInput(svgPath), null);
		}
		transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_ALLOW_EXTERNAL_RESOURCES, Boolean.TRUE);
		PageFormat pg = new PageFormat();
		Paper p = new Paper();
		p.setSize(width, height);
		p.setImageableArea(0, 0, width, height);
		pg.setPaper(p);
		transcoder.print(g2D, pg, 0);
		g2D.dispose();
		return template;
	}

	/**
	 * Mark the beginning of an artifact. Artifact content is whatever is not
	 * essential for the reader, such as page headers and footers, or repeated table
	 * headers, or graphical elements like lines or boxes which do not really have a
	 * meaning.
	 */
	public void beginArtifact() {
		if (!isTagged) {
			return;
		}
		if (artifactDepth == 0) {
			contentByte.beginMarkedContentSequence(PdfNames.ARTIFACT);
		}
		artifactDepth++;
	}

	/**
	 * Mark the beginning of an artifact. Artifact content is whatever is not
	 * essential for the reader, such as page headers and footers, or repeated table
	 * headers, or graphical elements like lines or boxes which do not really have a
	 * meaning.
	 *
	 * @param properties additional properties which are used in the call to
	 *                   beginMarkedContentSequence.
	 */
	public void beginArtifact(PdfDictionary properties) {
		if (!isTagged) {
			return;
		}
		if (artifactDepth == 0) {
			contentByte.beginMarkedContentSequence(PdfNames.ARTIFACT, properties, true);
		}
		artifactDepth++;
	}

	/**
	 * Mark the end of and artifact.
	 */
	public void endArtifact() {
		if (!isTagged) {
			return;
		}
		artifactDepth--;
		if (artifactDepth == 0) {
			contentByte.endMarkedContentSequence();
		}
	}

	/**
	 * @return if we are currently in an artifact or not.
	 */
	public boolean isInArtifact() {
		return artifactDepth > 0;
	}
}
