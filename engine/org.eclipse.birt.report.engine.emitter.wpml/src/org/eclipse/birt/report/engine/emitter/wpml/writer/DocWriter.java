/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.emitter.wpml.AbstractEmitterImpl.InlineFlag;
import org.eclipse.birt.report.engine.emitter.wpml.AbstractEmitterImpl.TextFlag;
import org.eclipse.birt.report.engine.emitter.wpml.HyperlinkInfo;
import org.eclipse.birt.report.engine.emitter.wpml.IWordWriter;
import org.eclipse.birt.report.engine.emitter.wpml.SpanInfo;
import org.eclipse.birt.report.engine.emitter.wpml.WordUtil;
import org.eclipse.birt.report.engine.layout.emitter.Image;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;

public class DocWriter extends AbstractWordXmlWriter implements IWordWriter {

	protected static Logger logger = Logger.getLogger(DocWriter.class.getName());

	public DocWriter(OutputStream out) {
		this(out, "UTF-8");
	}

	public DocWriter(OutputStream out, String encoding) {
		writer = new XMLWriter();
		writer.open(out, encoding);
	}

	public void start(boolean rtl, String creator, String title, String description, String subject) {
		this.rtl = rtl;
		writer.startWriter();
		writer.literal("\n");
		writer.literal("<?mso-application progid=\"Word.Document\"?>");
		writer.literal("\n");
		writer.openTag("w:wordDocument");
		writer.attribute("xmlns:w", "http://schemas.microsoft.com/office/word/2003/wordml");
		writer.attribute("xmlns:v", "urn:schemas-microsoft-com:vml");
		writer.attribute("xmlns:w10", "urn:schemas-microsoft-com:office:word");
		writer.attribute("xmlns:o", "urn:schemas-microsoft-com:office:office");
		writer.attribute("xmlns:dt", "uuid:C2F41010-65B3-11d1-A29F-00AA00C14882");
		writer.attribute("xmlns:wx", "http://schemas.microsoft.com/office/word/2003/auxHint");
		writer.attribute("xmlns:aml", "http://schemas.microsoft.com/aml/2001/core");
		writer.attribute("xml:space", "preserve");
		writeCoreProperties(creator, title, description, subject);

		// style for outline
		writer.openTag("w:styles");
		writer.openTag("w:style");
		writer.attribute("w:type", "paragraph");
		writer.attribute(" w:styleId", 4);
		writer.openTag("w:name");
		writer.attribute("w:val", "toc 4");
		writer.closeTag("w:name");
		writer.openTag("wx:uiName");
		writer.attribute("wx:val", "catalog 4");
		writer.closeTag("wx:uiName");
		writer.openTag("w:autoRedefine");
		writer.closeTag("w:autoRedefine");
		writer.openTag("w:semiHidden");
		writer.closeTag("w:semiHidden");
		writer.openTag("w:rsid");
		writer.attribute("w:val", "009B3C8F");
		writer.closeTag("w:rsid");
		writer.openTag("w:pPr");
		writer.openTag("w:pStyle");
		writer.attribute("w:val", 4);
		writer.closeTag("w:pStyle");
		writeBidi(rtl); // bidi_hcg
		writer.closeTag("w:pPr");
		writer.openTag("w:rPr");
		writer.openTag("wx:font");
		writer.attribute("wx:val", "Times New Roman");
		writer.closeTag("wx:font");
		writer.closeTag("w:rPr");
		writer.closeTag("w:style");

		writer.openTag("w:style");
		writer.attribute("w:type", "character");
		writer.attribute("w:styleId", "Hyperlink");
		writeAttrTag("w:name", "Hyperlink");
		writer.openTag("w:rPr");
		writeAttrTag("w:u", "single");
		writeAttrTag("w:color", "0000ff");
		writer.closeTag("w:rPr");
		writer.closeTag("w:style");

		writer.openTag("w:style");
		writer.attribute("w:type", "table");
		writer.attribute("w:default", "on");
		writer.attribute("styleId", "TableNormal");
		writeAttrTag("w:name", "Normal Table");
		writer.openTag("wx:uiName");
		writer.attribute("wx:val", "Table Normal");
		writer.closeTag("wx:uiName");
		writer.openTag("w:rPr");
		writer.openTag("wx:font");
		writer.attribute("wx:val", "Calibri");
		writer.closeTag("wx:font");
		writer.openTag("w:lang");
		writer.attribute("w:val", "EN-US");
		writer.attribute("w:fareast", "ZH-CN");
		writer.attribute("w:bidi", "AR-SA");
		writer.closeTag("w:lang");
		writer.closeTag("w:rPr");
		writer.openTag("w:tblPr");
		writer.openTag("w:tblInd");
		writer.attribute("w:w", 0);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:tblInd");
		writer.openTag("w:tblCellMar");
		writer.openTag("w:top");
		writer.attribute("w:w", 0);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:top");
		writer.openTag("w:left");
		writer.attribute("w:w", 108);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:left");
		writer.openTag("w:bottom");
		writer.attribute("w:w", 0);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:bottom");
		writer.openTag("w:right");
		writer.attribute("w:w", 108);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:right");
		writer.closeTag("w:tblCellMar");
		writer.closeTag("w:tblPr");
		writer.closeTag("w:style");

		writer.closeTag("w:styles");

		// For show background
		writer.openTag("w:displayBackgroundShape");
		writer.closeTag("w:displayBackgroundShape");

		writer.openTag("w:docPr");
		writer.openTag("w:view");
		writer.attribute("w:val", "print");
		writer.closeTag("w:view");
		writer.openTag("w:zoom");
		writer.attribute("w:percent", "100");
		writer.closeTag("w:zoom");
		writer.closeTag("w:docPr");
		writer.openTag("w:body");
	}

	private void writeCoreProperties(String creator, String title, String description, String subject) {
		writer.openTag("o:DocumentProperties");
		writer.openTag("o:Author");
		writer.text(creator);
		writer.closeTag("o:Author");
		writer.openTag("o:Title");
		writer.text(title);
		writer.closeTag("o:Title");
		writer.openTag("o:Description");
		writer.text(description);
		writer.closeTag("o:Description");
		writer.openTag("o:Subject");
		writer.text(subject);
		writer.closeTag("o:Subject");
		writer.closeTag("o:DocumentProperties");
	}

	/**
	 * 
	 * @param data   image data
	 * @param height image height, unit = pt
	 * @param width  image width, unit = pt
	 */
	public void drawImage(byte[] data, double height, double width, HyperlinkInfo hyper, IStyle style,
			InlineFlag inlineFlag, String altText, String imageUrl) {
		if (inlineFlag == InlineFlag.BLOCK || inlineFlag == InlineFlag.FIRST_INLINE) {
			writer.openTag("w:p");
		}
		int imageId = getImageID();
		openHyperlink(hyper);
		writer.openTag("w:r");
		writer.openTag("w:pict");
		drawImageShapeType(imageId);
		drawImageData(data, imageId);
		drawImageShape(height, width, style, altText, imageId);
		writer.closeTag("w:pict");
		writer.closeTag("w:r");
		closeHyperlink(hyper);
		if (inlineFlag == InlineFlag.BLOCK) {
			writer.closeTag("w:p");
		}
	}

	private void drawImageData(byte[] data, int imageId) {
		String pic2Text = null;
		if (data != null && data.length != 0) {
			pic2Text = new String(Base64.encodeBase64(data, false));
		}
		if (pic2Text != null) {
			writer.openTag("w:binData");
			writer.attribute("w:name", "wordml://" + imageId + ".png");
			writer.text(pic2Text);
			writer.closeTag("w:binData");
		}
	}

	private void drawImageShape(double height, double width, IStyle style, String altText, int imageId) {
		writer.openTag("v:shape");
		writer.attribute("id", "_x0000_i10" + imageId);
		writer.attribute("type", "#_x0000_t" + imageId);
		writer.attribute("alt", altText);
		writer.attribute("style", "width:" + width + "pt;height:" + height + "pt");
		drawImageBordersColor(style);
		writer.openTag("v:imagedata");
		writer.attribute("src", "wordml://" + imageId + ".png");
		writer.attribute("otitle", "");
		writer.closeTag("v:imagedata");
		drawImageBordersStyle(style);
		writer.closeTag("v:shape");
	}

	public void writeContent(int type, String txt, IStyle style, IStyle inlineStyle, String fontFamily,
			HyperlinkInfo info, InlineFlag inlineFlag, TextFlag flag, int paragraphWidth, boolean runIsRtl,
			String textAlign) {
		if (inlineFlag == InlineFlag.BLOCK) {
			writeText(type, txt, style, fontFamily, info, flag, paragraphWidth, runIsRtl);
		} else {
			boolean isInline = true;
			if (inlineFlag == InlineFlag.FIRST_INLINE && flag == TextFlag.START) {
				startParagraph(style, isInline, paragraphWidth, textAlign);
			}
			if (inlineStyle != null)
				writeTextInRun(type, txt, inlineStyle, fontFamily, info, isInline, paragraphWidth, runIsRtl, textAlign);
			else
				writeTextInRun(type, txt, style, fontFamily, info, isInline, paragraphWidth, runIsRtl, textAlign);
		}
	}

	protected void openHyperlink(HyperlinkInfo info) {
		if (info == null) {
			return;
		}
		writer.openTag("w:hlink");
		if (HyperlinkInfo.BOOKMARK == info.getType()) {
			writer.attribute("w:bookmark", info.getUrl());
		} else if (HyperlinkInfo.HYPERLINK == info.getType()) {
			writer.attribute("w:dest", info.getUrl());
			if (info.getBookmark() != null) {
				writer.attribute("w:bookmark", info.getBookmark());
			}
		}
		if (info.getTooltip() != null) {
			writer.attribute("w:screenTip", info.getTooltip());
		}
	}

	protected void closeHyperlink(HyperlinkInfo info) {
		if ((info == null) || (info.getType() == HyperlinkInfo.DRILL)) {
			return;
		}
		writer.closeTag("w:hlink");
	}

	public void writeBookmark(String bm) {
		bm = WordUtil.validBookmarkName(bm);

		writer.openTag("aml:annotation");
		writer.attribute("aml:id", bookmarkId);
		writer.attribute("w:type", "Word.Bookmark.Start");
		writer.attribute("w:name", bm);
		writer.closeTag("aml:annotation");

		writer.openTag("aml:annotation");
		writer.attribute("aml:id", bookmarkId);
		writer.attribute("w:type", "Word.Bookmark.End");
		writer.closeTag("aml:annotation");

		bookmarkId++;
	}

	public void close() {
		writer.close();
	}

	protected void writeTableLayout() {
		writer.openTag("w:tblLayout");
		writer.attribute("w:type", "Fixed");
		writer.closeTag("w:tblLayout");
	}

	protected void writeFontSize(IStyle style) {
		CSSValue fontSize = style.getProperty(StyleConstants.STYLE_FONT_SIZE);
		int size = WordUtil.parseFontSize(PropertyUtil.getDimensionValue(fontSize));
		writeAttrTag("w:sz", size);
		writeAttrTag("w:sz-cs", size);
	}

	protected void writeFont(String fontFamily) {
		writer.openTag("w:rFonts");
		writer.attribute("w:ascii", fontFamily);
		writer.attribute("w:fareast", fontFamily);
		writer.attribute("w:h-ansi", fontFamily);
		writer.attribute("w:cs", fontFamily);
		writer.closeTag("w:rFonts");
	}

	protected void writeFontStyle(IStyle style) {
		String val = WordUtil.removeQuote(style.getFontStyle());
		if (!"normal".equalsIgnoreCase(val)) {
			writeAttrTag("w:i", "on");
			writeAttrTag("w:i-cs", "on");
		}
	}

	protected void writeFontWeight(IStyle style) {
		String val = WordUtil.removeQuote(style.getFontWeight());
		if (!"normal".equalsIgnoreCase(val)) {
			writeAttrTag("w:b", "on");
			writeAttrTag("w:b-cs", "on");
		}
	}

	public void drawDocumentBackground(String bgcolor, String backgroundImageUrl, String backgroundHeight,
			String backgroundWidth) {
		// Image priority is higher than color.
		writer.openTag("w:bgPict");
		if (backgroundImageUrl != null && backgroundHeight == null && backgroundWidth == null) {
			try {
				byte[] backgroundImageData = EmitterUtil.getImageData(backgroundImageUrl);
				drawDocumentBackgroundImage(backgroundImageData);
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage());
			}
		} else
			drawDocumentBackgroundColor(bgcolor);
		writer.closeTag("w:bgPict");
	}

	public void drawDocumentBackgroundImage(String backgroundImageUrl, String height, String width, double topMargin,
			double leftMargin, double pageHeight, double pageWidth) {
		if (backgroundImageUrl != null) {
			try {
				Image imageInfo = EmitterUtil.parseImage(null, IImageContent.IMAGE_URL, backgroundImageUrl, null, null);
				int imageWidth = imageInfo.getWidth();
				int imageHeight = imageInfo.getHeight();
				String[] realSize = WordUtil.parseBackgroundSize(height, width, imageWidth, imageHeight, pageWidth,
						pageHeight);
				byte[] backgroundImageData = EmitterUtil.getImageData(backgroundImageUrl);
				int imageId = getImageID();
				writer.openTag("w:p");
				writeHiddenProperty();
				writer.openTag("w:r");
				writer.openTag("w:pict");
				drawImageShapeType(imageId);
				drawImageData(backgroundImageData, imageId);
				drawBackgroundImageShape(realSize, topMargin, leftMargin, imageId);
				writer.closeTag("w:pict");
				writer.closeTag("w:r");
				writer.closeTag("w:p");
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage());
			}
		}
	}

	private void drawBackgroundImageShape(String[] size, double topMargin, double leftMargin, int imageId) {
		writer.openTag("v:shape");
		writer.attribute("id", "_x0000_i10" + imageId);
		writer.attribute("type", "#_x0000_t" + imageId);
		writer.attribute("style", "position:absolute;left:0;text-align:left;margin-left:-" + leftMargin + "pt"
				+ ";margin-top:-" + topMargin + "pt" + ";width:" + size[1] + ";height:" + size[0] + ";z-index:-1");
		writer.openTag("v:imagedata");
		writer.attribute("src", "wordml://" + imageId + ".png");
		writer.attribute("otitle", "");
		writer.closeTag("v:imagedata");
		writer.closeTag("v:shape");
	}

	private void drawDocumentBackgroundImage(byte[] data) {
		int imgId = getImageID();
		drawImageData(data, imgId);
		writer.openTag("w:background");
		writer.attribute("w:bgcolor", "white");
		writer.attribute("w:background", "wordml://" + imgId + ".png");
		writer.closeTag("w:background");
	}

	private void drawDocumentBackgroundColor(String data) {
		String color = WordUtil.parseColor(data);
		if (color != null) {
			writer.openTag("w:background");
			writer.attribute("w:bgcolor", color);
			writer.closeTag("w:background");
		}
	}

	public void startTableRow(double height) {
		startTableRow(height, false, false, false);
	}

	public void startPage() {
		writer.openTag("wx:sect");
	}

	public void endPage() {
		writer.closeTag("wx:sect");
	}

	public void end() {
		writer.closeTag("w:body");
		writer.closeTag("w:wordDocument");
		writer.close();
	}

	public void startHeader(boolean showHeaderOnFirst, int headerHeight, int headerWidth) {
		writer.openTag("w:hdr");
		if (showHeaderOnFirst) {
			writer.attribute("w:type", "first");
			writer.openTag("w:p");
			writer.openTag("w:r");
			writer.closeTag("w:r");
			writer.closeTag("w:p");
		} else
			writer.attribute("w:type", "odd");
		startHeaderFooterContainer(headerHeight, headerWidth);
	}

	public void endHeader() {
		endHeaderFooterContainer();
		writer.closeTag("w:hdr");
	}

	public void startFooter(int footerHeight, int footerWidth) {
		writer.openTag("w:ftr");
		writer.attribute("w:type", "odd");
		startHeaderFooterContainer(footerHeight, footerWidth);
	}

	public void endFooter() {
		endHeaderFooterContainer();
		writer.closeTag("w:ftr");
	}

	public void writeTOC(String tocText, int level) {
		writeTOC(tocText, null, level, false);
	}

	public void writeTOC(String tocText, String color, int level, boolean middleInline) {
		if (!middleInline) {
			writer.openTag("w:p");
		}

		if (color != null) {
			writer.openTag("w:pPr");
			writer.openTag("w:shd");
			writer.attribute("w:val", "clear");
			writer.attribute("w:color", "auto");
			writer.attribute("w:fill", color);
			writer.closeTag("w:shd");
			writer.openTag("w:rPr");
			writer.openTag("w:vanish");
			writer.closeTag("w:vanish");
			writer.closeTag("w:rPr");
			writer.closeTag("w:pPr");
		} else {
			writer.openTag("w:pPr");
			writer.openTag("w:rPr");
			writer.openTag("w:vanish");
			writer.closeTag("w:vanish");
			writer.closeTag("w:rPr");
			writer.closeTag("w:pPr");
		}

		writer.openTag("aml:annotation");
		writer.attribute("aml:id", bookmarkId);
		writer.attribute("w:type", "Word.Bookmark.Start");
		writer.attribute("w:name", "_Toc" + tocText);
		writer.closeTag("aml:annotation");

		writer.openTag("aml:annotation");
		writer.attribute("aml:id", bookmarkId++);
		writer.attribute("w:type", "Word.Bookmark.End");
		writer.closeTag("aml:annotation");

		writeField(true);
		writeTocText(tocText, level);
		writeField(false);
		if (!middleInline) {
			writer.closeTag("w:p");
		}
	}

	protected void writeVmerge(SpanInfo spanInfo) {
		if (spanInfo.isStart()) {
			writeAttrTag("w:vmerge", "restart");
		} else {
			writer.openTag("w:vmerge");
			writer.closeTag("w:vmerge");
		}
	}

	public void writeForeign(IForeignContent foreignContent) {
	}

	public void writePageBorders(IStyle style, int topMargin, int bottomMargin, int leftMargin, int rightMargin) {
		// TODO Auto-generated method stub
		writer.openTag("w:pgBorders");
		writer.attribute("w:offset-from", "page");
		writeBorders(style, topMargin, bottomMargin, leftMargin, rightMargin);
		writer.closeTag("w:pgBorders");

	}

	protected void writeIndent(int textIndent) {
		writer.openTag("w:ind");
		writer.attribute("w:first-line", textIndent);
		writer.closeTag("w:ind");
	}

	protected void writeIndent(int leftMargin, int rightMargin, int textIndent) {
		if (leftMargin == 0 && rightMargin == 0 && textIndent == 0) {
			return;
		}
		writer.openTag("w:ind");
		if (leftMargin != 0) {
			writer.attribute("w:left", leftMargin);
		}

		if (rightMargin != 0) {
			writer.attribute("w:right", rightMargin);
		}

		if (textIndent != 0) {
			writer.attribute("w:first-line", textIndent);
		}
		writer.closeTag("w:ind");
	}
}
