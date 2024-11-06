/*******************************************************************************
 * Copyright (c) 2008, 2009, 2024 Actuate Corporation and others
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

package org.eclipse.birt.report.engine.emitter.wpml.writer;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.emitter.wpml.AbstractEmitterImpl.TextFlag;
import org.eclipse.birt.report.engine.emitter.wpml.DiagonalLineInfo;
import org.eclipse.birt.report.engine.emitter.wpml.DiagonalLineInfo.Line;
import org.eclipse.birt.report.engine.emitter.wpml.HyperlinkInfo;
import org.eclipse.birt.report.engine.emitter.wpml.SpanInfo;
import org.eclipse.birt.report.engine.emitter.wpml.WordUtil;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;

/**
 * This is used for writing WordML by the DocxEmitter and by the old Word 2003
 * emitter.
 */
public abstract class AbstractWordXmlWriter {

	protected XMLWriter writer;

	protected final String RIGHT = "right";

	protected final String LEFT = "left";

	protected final String TOP = "top";

	protected final String BOTTOM = "bottom";

	/** constant property: space */
	public static final char SPACE = ' ';

	/** constant property: empty string */
	public static final String EMPTY_STRING = "";

	/** constant property: index not found */
	public static final int INDEX_NOTFOUND = -1;

	/**
	 * <p>
	 * The soft hyphen Unicode symbol is intended to be visible only when a line
	 * break occurs there.
	 * </p>
	 * <p>
	 * This hiding logic of the SHY symbol needs special attention in many emitters.
	 * </p>
	 * <p>
	 * SOFT HYPHEN is often abbreviated as SHY, which also is very descriptive,
	 * because this symbol is hiding inside the surrounding words most of the time.
	 * </p>
	 * <p>
	 * In most fonts, its width is defined as zero, which of cause is correct only
	 * if it is hidden. If it is rendered, it looks similar to the minus sign.
	 * </p>
	 * <p>
	 * The Unicode standard also defines a HYPHEN symbol, which should look the same
	 * as the SHY symbol, but doesn't have the hiding logic. However, the HYPHEN
	 * symbol is rarely defined in TTF fonts.
	 * </p>
	 */
	public static final char SOFT_HYPHEN = '\u00ad';

	protected int imageId = 75;

	protected int bookmarkId = 0;

	private int lineId = 0;

	// Holds the global layout orientation.
	protected boolean rtl = false;

	protected abstract void writeTableLayout();

	protected abstract void writeFontSize(IStyle style);

	protected abstract void writeFont(String fontFamily);

	protected abstract void writeFontStyle(IStyle style);

	protected abstract void writeFontWeight(IStyle style);

	protected abstract void openHyperlink(HyperlinkInfo info);

	protected abstract void closeHyperlink(HyperlinkInfo info);

	protected abstract void writeVmerge(SpanInfo spanInfo);

	protected abstract void writeIndent(int indent);

	protected abstract void writeIndent(int leftMargin, int rightMargin, int textIndent);

	/**
	 * Start section in paragraph
	 */
	public void startSectionInParagraph() {
		writer.openTag("w:p");
		writer.openTag("w:pPr");
		startSection();
	}

	/**
	 * End section in paragraph
	 */
	public void endSectionInParagraph() {
		endSection();
		writer.closeTag("w:pPr");
		writer.closeTag("w:p");
	}

	/**
	 * Start section
	 */
	public void startSection() {
		writer.openTag("w:sectPr");
	}

	/**
	 * End section
	 */
	public void endSection() {
		writer.closeTag("w:sectPr");
	}

	protected void drawImageShapeType(int imageId) {
		writer.openTag("v:shapetype");
		writer.attribute("id", "_x0000_t" + imageId);
		writer.attribute("coordsize", "21600,21600");
		writer.attribute("o:spt", "75");
		writer.attribute("o:preferrelative", "t");
		writer.attribute("path", "m@4@5l@4@11@9@11@9@5xe");
		writer.attribute("filled", "f");
		writer.attribute("stroked", "f");
		writer.openTag("v:stroke");
		writer.attribute("imagealignshape", "false");
		writer.attribute("joinstyle", "miter");
		writer.closeTag("v:stroke");
		writer.openTag("v:formulas");
		writer.openTag("v:f");
		writer.attribute("eqn", "if lineDrawn pixelLineWidth 0");
		writer.closeTag("v:f");
		writer.openTag("v:f");
		writer.attribute("eqn", "sum @0 1 0");
		writer.closeTag("v:f");
		writer.openTag("v:f");
		writer.attribute("eqn", "sum 0 0 @1");
		writer.closeTag("v:f");
		writer.openTag("v:f");
		writer.attribute("eqn", "prod @2 1 2");
		writer.closeTag("v:f");
		writer.openTag("v:f");
		writer.attribute("eqn", "prod @3 21600 pixelWidth");
		writer.closeTag("v:f");
		writer.openTag("v:f");
		writer.attribute("eqn", "prod @3 21600 pixelHeight");
		writer.closeTag("v:f");
		writer.openTag("v:f");
		writer.attribute("eqn", "sum @0 0 1");
		writer.closeTag("v:f");
		writer.openTag("v:f");
		writer.attribute("eqn", "prod @6 1 2");
		writer.closeTag("v:f");
		writer.openTag("v:f");
		writer.attribute("eqn", "prod @7 21600 pixelWidth");
		writer.closeTag("v:f");
		writer.openTag("v:f");
		writer.attribute("eqn", "sum @8 21600 0 ");
		writer.closeTag("v:f");
		writer.openTag("v:f");
		writer.attribute("eqn", "prod @7 21600 pixelHeight");
		writer.closeTag("v:f");
		writer.openTag("v:f");
		writer.attribute("eqn", "sum @10 21600 0");
		writer.closeTag("v:f");
		writer.closeTag("v:formulas");
		writer.openTag("v:path");
		writer.attribute("o:extrusionok", "f");
		writer.attribute("gradientshapeok", "t");
		writer.attribute("o:connecttype", "rect");
		writer.closeTag("v:path");
		writer.openTag("o:lock");
		writer.attribute("v:ext", "edit");
		writer.attribute("aspectratio", "t");
		writer.closeTag("o:lock");
		writer.closeTag("v:shapetype");
	}

	protected void drawImageBordersStyle(IStyle style) {
		drawImageBorderStyle(BOTTOM, style.getBorderBottomStyle(),
				style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
		drawImageBorderStyle(TOP, style.getBorderTopStyle(), style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH));
		drawImageBorderStyle(LEFT, style.getBorderLeftStyle(),
				style.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH));
		drawImageBorderStyle(RIGHT, style.getBorderRightStyle(),
				style.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH));
	}

	private void drawImageBorderStyle(String pos, String style, CSSValue width) {
		if (PropertyUtil.getDimensionValue(width) != 0) {
			String direct = "w10:border" + pos;
			writer.openTag(direct);
			writer.attribute("type", WordUtil.parseImageBorderStyle(style));
			writer.attribute("width", WordUtil.parseBorderSize(PropertyUtil.getDimensionValue(width)));
			writer.closeTag(direct);
		}
	}

	protected void drawImageBordersColor(IStyle style) {
		drawImageBorderColor(BOTTOM, style.getBorderBottomColor());
		drawImageBorderColor(TOP, style.getBorderTopColor());
		drawImageBorderColor(LEFT, style.getBorderLeftColor());
		drawImageBorderColor(RIGHT, style.getBorderRightColor());
	}

	private void drawImageBorderColor(String pos, String color) {
		String borderColor = "#" + WordUtil.parseColor(color);
		String direct = "o:border" + pos + "color";
		writer.attribute(direct, borderColor);
	}

	/**
	 * Write page properties
	 *
	 * @param pageHeight   page height
	 * @param pageWidth    page width
	 * @param headerHeight header height
	 * @param footerHeight footer height
	 * @param topMargin    top margin
	 * @param bottomMargin bottom margin
	 * @param leftMargin   left margin
	 * @param rightMargin  right margin
	 * @param orient       page orientation
	 */
	public void writePageProperties(int pageHeight, int pageWidth, int headerHeight, int footerHeight, int topMargin,
			int bottomMargin, int leftMargin, int rightMargin, String orient) {
		writer.openTag("w:pgSz");
		writer.attribute("w:w", pageWidth);
		writer.attribute("w:h", pageHeight);
		writer.attribute("w:orient", orient);
		writer.closeTag("w:pgSz");

		writer.openTag("w:pgMar");
		writer.attribute("w:top", topMargin);
		writer.attribute("w:bottom", bottomMargin);
		writer.attribute("w:left", leftMargin);
		writer.attribute("w:right", rightMargin);
		writer.attribute("w:header", topMargin);
		writer.attribute("w:footer", bottomMargin);
		writer.closeTag("w:pgMar");
	}

	/**
	 * Start table and write the table properties to the output stream
	 *
	 * @param style      table style
	 * @param tablewidth table width
	 */
	public void startTable(IStyle style, int tablewidth) {
		startTable(style, tablewidth, false);
	}

	/**
	 * Start table and write the table properties to the output stream
	 *
	 * @param style      table style
	 * @param tablewidth table width
	 * @param inForeign  in foreign
	 */
	public void startTable(IStyle style, int tablewidth, boolean inForeign) {
		writer.openTag("w:tbl");
		writer.openTag("w:tblPr");
		writeTableIndent(style);
		writeAttrTag("w:tblStyle", "TableGrid");
		writeAttrTag("w:tblOverlap", "Never");
		writeBidiTable();
		writeTableWidth(style, tablewidth);
		writeAttrTag("w:tblLook", "01E0");
		writeTableLayout();
		writeTableBorders(style);
		writeBackgroundColor(style.getBackgroundColor());

		// "justify" is not an option for table alignment in word
		if ("justify".equalsIgnoreCase(style.getTextAlign())) {
			writeAlign("left", style.getDirection());
		} else {
			writeAlign(style.getTextAlign(), style.getDirection());
		}
		if (inForeign) {
			writeTableCellMarginZero();
		}
		writer.closeTag("w:tblPr");
	}

	private void writeTableCellMarginZero() {
		writer.openTag("w:tblCellMar");
		writer.openTag("w:top");
		writer.attribute("w:w", 0);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:top");
		writer.openTag("w:left");
		writer.attribute("w:w", 0);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:left");
		writer.openTag("w:bottom");
		writer.attribute("w:w", 0);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:bottom");
		writer.openTag("w:right");
		writer.attribute("w:w", 0);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:right");
		writer.closeTag("w:tblCellMar");
	}

	private void writeTableBorders(IStyle style) {
		writer.openTag("w:tblBorders");
		writeBorders(style, 0, 0, 0, 0);
		writer.closeTag("w:tblBorders");
	}

	/**
	 * End table
	 */
	public void endTable() {
		writer.closeTag("w:tbl");
	}

	private void writeTableWidth(int tablewidth) {
		writer.openTag("w:tblW");
		writer.attribute("w:w", tablewidth);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:tblW");
	}

	private void writeTableIndent(IStyle style) {
		writer.openTag("w:tblInd");
		writer.attribute("w:w", WordUtil
				.milliPt2Twips(PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_LEFT))));
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:tblInd");
	}

	private void writeTableWidth(IStyle style, int tablewidth) {
		int leftSpace = WordUtil
				.milliPt2Twips(PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_LEFT)));
		int rightSpace = WordUtil
				.milliPt2Twips(PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_RIGHT)));
		writeTableWidth(tablewidth - leftSpace - rightSpace);
	}

	protected void writeBorders(IStyle style, int bottomMargin, int topMargin, int leftMargin, int rightMargin) {
		String borderStyle = style.getBorderBottomStyle();
		if (hasBorder(borderStyle)) {
			writeSingleBorder(BOTTOM, borderStyle, style.getBorderBottomColor(),
					style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH), bottomMargin);
		}

		borderStyle = style.getBorderTopStyle();
		if (hasBorder(borderStyle)) {
			writeSingleBorder(TOP, borderStyle, style.getBorderTopColor(),
					style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH), topMargin);
		}

		borderStyle = style.getBorderLeftStyle();
		if (hasBorder(borderStyle)) {
			writeSingleBorder(LEFT, borderStyle, style.getBorderLeftColor(),
					style.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH), leftMargin);
		}

		borderStyle = style.getBorderRightStyle();
		if (hasBorder(borderStyle)) {
			writeSingleBorder(RIGHT, borderStyle, style.getBorderRightColor(),
					style.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH), rightMargin);
		}

	}

	/**
	 * Writer of the diagonal attribute
	 *
	 * @param borderStyle style of the diagonal
	 * @param color       color of the diagonal
	 * @param width       width of the diagonal
	 */
	private void writeDiagonalBorder(String borderStyle, String color, double width) {
		writer.openTag("w:tl2br");
		int borderSize = (int) width * 9;
		writeBorderProperty(borderStyle, color, borderSize, 0);
		writer.closeTag("w:tl2br");
	}

	/**
	 * Writer of the antidiagonal attribute
	 *
	 * @param borderStyle style of the diagonal
	 * @param color       color of the diagonal
	 * @param width       width of the diagonal
	 */
	private void writeAntidiagonalBorder(String borderStyle, String color, double width) {
		writer.openTag("w:tr2bl");
		int borderSize = (int) width * 9;
		writeBorderProperty(borderStyle, color, borderSize, 0);
		writer.closeTag("w:tr2bl");
	}

	private void writeSingleBorder(String type, String borderStyle, String color, CSSValue width, int margin) {
		writer.openTag("w:" + type);
		int borderSize = WordUtil.parseBorderSize(PropertyUtil.getDimensionValue(width));
		writeBorderProperty(borderStyle, color, borderSize, margin);
		writer.closeTag("w:" + type);
	}

	private void writeBorderProperty(String style, String color, int width, int margin) {
		writer.attribute("w:val", WordUtil.parseBorderStyle(style));
		writer.attribute("w:sz", "double".equals(style) ? width / 3 : width);
		writer.attribute("w:space", validateBorderSpace(margin));
		writer.attribute("w:color", WordUtil.parseColor(color));
	}

	private int validateBorderSpace(int margin) {
		// word only accept 0-31 pt
		int space = (int) WordUtil.twipToPt(margin);
		if (space > 31) {
			space = 31;
		}
		return space;
	}

	protected void writeAlign(String align, String direction) {
		if (null == align) {
			return;
		}
		String textAlign = align;
		if ("justify".equalsIgnoreCase(align)) {
			textAlign = "both";
		}

		// Need to swap 'left' and 'right' when orientation is RTL.
		if (CSSConstants.CSS_RTL_VALUE.equalsIgnoreCase(direction)) {
			if (CSSConstants.CSS_RIGHT_VALUE.equals(textAlign)) {
				writeAttrTag("w:jc", CSSConstants.CSS_LEFT_VALUE);
			} else if (CSSConstants.CSS_LEFT_VALUE.equals(textAlign)) {
				writeAttrTag("w:jc", CSSConstants.CSS_RIGHT_VALUE);
			} else {
				writeAttrTag("w:jc", textAlign);
			}
		} else {
			writeAttrTag("w:jc", textAlign);
		}
	}

	protected void writeBackgroundColor(String color) {
		String cssColor = WordUtil.parseColor(color);
		if (cssColor == null) {
			return;
		}
		writer.openTag("w:shd");
		writer.attribute("w:val", "clear");
		writer.attribute("w:color", "auto");
		writer.attribute("w:fill", cssColor);
		writer.closeTag("w:shd");
	}

	/**
	 * @param direction
	 *
	 * @author bidi_hcg
	 */
	private void writeBidiTable() {
		if (this.rtl) {
			writer.openTag("w:bidiVisual");
			writer.closeTag("w:bidiVisual");
		}
	}

	protected void writeRunBorders(IStyle style) {
		String borderStyle = style.getBorderTopStyle();
		if (hasBorder(borderStyle)) {
			writeRunBorder(borderStyle, style.getBorderTopColor(),
					style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH));
			return;
		}

		borderStyle = style.getBorderBottomStyle();
		if (hasBorder(borderStyle)) {
			writeRunBorder(borderStyle, style.getBorderBottomColor(),
					style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
			return;
		}

		borderStyle = style.getBorderLeftStyle();
		if (hasBorder(borderStyle)) {
			writeRunBorder(borderStyle, style.getBorderLeftColor(),
					style.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH));
			return;
		}

		borderStyle = style.getBorderRightStyle();
		if (hasBorder(borderStyle)) {
			writeRunBorder(borderStyle, style.getBorderRightColor(),
					style.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH));
		}
	}

	protected boolean hasBorder(String borderStyle) {
		return !(borderStyle == null || "none".equalsIgnoreCase(borderStyle));
	}

	private void writeRunBorder(String borderStyle, String color, CSSValue borderWidth) {
		writer.openTag("w:bdr");
		int borderSize = WordUtil.parseBorderSize(PropertyUtil.getDimensionValue(borderWidth));
		writeBorderProperty(borderStyle, color, borderSize, 0);
		writer.closeTag("w:bdr");
	}

	private boolean needNewParagraph(String txt) {
		return ("\n".equals(txt) || "\r".equalsIgnoreCase(txt) || "\r\n".equals(txt));
	}

	/**
	 * Start paragraph
	 *
	 * @param style          style of paragraph
	 * @param isInline       is inline paragraph
	 * @param paragraphWidth paragraph width
	 */
	public void startParagraph(IStyle style, boolean isInline, int paragraphWidth) {
		startParagraph(style, isInline, paragraphWidth, null);
	}

	/**
	 * Used only in inline text. The text align style of inline text is ignored, but
	 * its parent text align should be applied.
	 *
	 * @param style
	 * @param isInline
	 * @param paragraphWidth
	 * @param textAlign      parent text align of inline text
	 */
	public void startParagraph(IStyle style, boolean isInline, int paragraphWidth, String textAlign) {
		writer.openTag("w:p");
		writer.openTag("w:pPr");
		writeSpacing(null, style.getProperty(StyleConstants.STYLE_MARGIN_TOP),
				style.getProperty(StyleConstants.STYLE_MARGIN_BOTTOM),
				style.getProperty(StyleConstants.STYLE_PADDING_TOP),
				style.getProperty(StyleConstants.STYLE_PADDING_BOTTOM));
		writeAlign(textAlign, style.getDirection());
		int indent = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_TEXT_INDENT), paragraphWidth)
				/ 1000 * 20;

		int leftMargin = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_LEFT),
				paragraphWidth) / 1000 * 20;

		int rightMargin = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_RIGHT),
				paragraphWidth) / 1000 * 20;

		int leftPadding = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_LEFT),
				paragraphWidth) / 1000 * 20;

		int rightPadding = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_RIGHT),
				paragraphWidth) / 1000 * 20;

		leftMargin += leftPadding;
		rightMargin += rightPadding;

		writeIndent(leftMargin, rightMargin, indent);

		if (!isInline) {
			writeBackgroundColor(style.getBackgroundColor());
			writeParagraphBorders(style);
		}
		writeBidi(CSSConstants.CSS_RTL_VALUE.equals(style.getDirection()));
		writer.closeTag("w:pPr");
	}

	private void writeSpacing(CSSValue height, CSSValue topMargin, CSSValue bottomMargin, CSSValue topPadding,
			CSSValue bottomPadding) {
		writer.openTag("w:spacing");
		// unit: twentieths of a point(twips)
		if (height != null) {
			float spacingValue = PropertyUtil.getDimensionValue(height);
			int spacing = WordUtil.milliPt2Twips(spacingValue);
			writer.attribute("w:lineRule", "exact");
			writer.attribute("w:line", spacing);
		}

		int beforeTop = 0;
		if (topMargin != null)
			beforeTop += WordUtil.milliPt2Twips(PropertyUtil.getDimensionValue(topMargin));
		if (topPadding != null)
			beforeTop += WordUtil.milliPt2Twips(PropertyUtil.getDimensionValue(topPadding));
		if (beforeTop != 0)
			writer.attribute("w:before", beforeTop);

		int afterBottom = 0;
		if (bottomMargin != null)
			afterBottom += WordUtil.milliPt2Twips(PropertyUtil.getDimensionValue(bottomMargin));
		if (bottomPadding != null)
			afterBottom += WordUtil.milliPt2Twips(PropertyUtil.getDimensionValue(bottomPadding));
		if (afterBottom != 0)
			writer.attribute("w:after", afterBottom);

		writer.closeTag("w:spacing");
	}

	protected void writeAutoText(int type) {
		writer.openTag("w:instrText");
		if (type == IAutoTextContent.PAGE_NUMBER) {
			writer.text("PAGE");
		} else if (type == IAutoTextContent.TOTAL_PAGE) {
			writer.text("NUMPAGES");
		}
		writer.closeTag("w:instrText");
	}

	private void writeString(String txt, IStyle style) {
		if (txt == null) {
			return;
		}
		if (style != null) {
			String textTransform = style.getTextTransform();
			if (CSSConstants.CSS_CAPITALIZE_VALUE.equalsIgnoreCase(textTransform)) {
				txt = WordUtil.capitalize(txt);
			} else if (CSSConstants.CSS_UPPERCASE_VALUE.equalsIgnoreCase(textTransform)) {
				txt = txt.toUpperCase();
			} else if (CSSConstants.CSS_LOWERCASE_VALUE.equalsIgnoreCase(textTransform)) {
				txt = txt.toLowerCase();
			}
		}

		writer.openTag("w:t");
		writer.attribute("xml:space", "preserve");
		int length = txt.length();
		int start = 0;
		int end = 0;
		while (end < length) {
			char ch = txt.charAt(end);
			if (ch == '\r' || ch == '\n') {
				// output previous text
				writeText(txt.substring(start, end));
				writer.cdata("<w:br/>");
				start = end + 1;
				if (ch == '\r' && start < length && txt.charAt(start) == '\n') {
					start++;
				}
				end = start + 1;
			} else if (ch == SOFT_HYPHEN) {
				// Output a special WordML tag for the SHY symbol.
				writeText(txt.substring(start, end));
				writer.closeTag("w:t"); //$NON-NLS-1$
				writer.cdata("<w:softHyphen/>"); // $NON-LS-1$
				writer.openTag("w:t"); //$NON-NLS-1$
				start = end + 1;
				end++;
			} else {
				end++;
			}
		}
		writeText(txt.substring(start));

		writer.closeTag("w:t");
	}

	/**
	 * Word have extra limitation on text in run: a. it must following xml format.
	 * b. no ]]> so , we need replace all &, <,> in the text
	 *
	 * @param text
	 */
	private void writeText(String text) {
		int length = text.length();
		StringBuilder sb = new StringBuilder(length * 2);
		for (int i = 0; i < length; i++) {
			char ch = text.charAt(i);
			switch (ch) {
			case '&':
				sb.append("&amp;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			default:
				sb.append(ch);
			}
		}
		writer.cdata(sb.toString());
	}

	private void writeLetterSpacing(IStyle style) {
		int letterSpacing = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_LETTER_SPACING));
		writeAttrTag("w:spacing", WordUtil.milliPt2Twips(letterSpacing));
	}

	private void writeHyperlinkStyle(HyperlinkInfo info, IStyle style) {
		// deal with hyperlink
		if (info != null) {
			String color = info.getColor();
			if (color != null) {
				writeAttrTag("w:color", color);
			}
			if (info.isHasHyperlinkDecoration()) {
				writeAttrTag("w:rStyle", "Hyperlink");
			}
		} else {
			writeTextUnderline(style);
			writeTextColor(style);
		}
	}

	protected void writeTocText(String tocText, int level) {
		writer.openTag("w:r");
		writer.openTag("w:instrText");
		writer.text(" TC \"" + tocText + "\"" + " \\f C \\l \"" + String.valueOf(level) + "\"");
		writer.closeTag("w:instrText");
		writer.closeTag("w:r");
	}

	/**
	 * @param rtl text direction
	 *
	 * @author bidi_hcg
	 */
	protected void writeBidi(boolean rtl) {
		writeAttrTag("w:bidi", rtl ? "" : "off");
	}

	protected void writeField(boolean isStart) {
		String fldCharType = isStart ? "begin" : "end";
		writer.openTag("w:r");
		writer.openTag("w:fldChar");
		writer.attribute("w:fldCharType", fldCharType);
		writer.closeTag("w:fldChar");
		writer.closeTag("w:r");
	}

	protected void writeField(boolean isStart, IStyle style, String fontName) {
		String fldCharType = isStart ? "begin" : "end";
		writer.openTag("w:r");
		writeFieldRunProperties(style, fontName);
		writer.openTag("w:fldChar");
		writer.attribute("w:fldCharType", fldCharType);
		writer.closeTag("w:fldChar");
		writer.closeTag("w:r");
	}

	/**
	 * Write the columns
	 *
	 * @param cols column count array
	 */
	public void writeColumn(int[] cols) {
		// unit: twips
		writer.openTag("w:tblGrid");

		for (int i = 0; i < cols.length; i++) {
			writeAttrTag("w:gridCol", cols[i]);
		}
		writer.closeTag("w:tblGrid");
	}


	/**
	 * Start the table row creation
	 *
	 * @param height       row height
	 * @param isHeader     is header row
	 * @param repeatHeader is repeat header
	 * @param fixedLayout  fixed layout
	 */
	public void startTableRow(double height, boolean isHeader, boolean repeatHeader, boolean fixedLayout) {
		writer.openTag("w:tr");

		// write the row height, unit: twips
		writer.openTag("w:trPr");

		if (height != -1) {
			writer.openTag("w:trHeight");
			if (fixedLayout) {
				writer.attribute("w:h-rule", "exact");
			}
			writer.attribute("w:val", height);
			writer.closeTag("w:trHeight");
		}

		// if value is "off",the header will be not repeated
		if (isHeader) {
			String headerOnOff = repeatHeader ? "on" : "off";
			writeAttrTag("w:tblHeader", headerOnOff);
		}
		writer.closeTag("w:trPr");
	}

	/**
	 * Write the table row end
	 */
	public void endTableRow() {
		writer.closeTag("w:tr");
	}

	/**
	 * Create the table tag of cell element
	 *
	 * @param width            of the cell element
	 * @param style            of the cell element
	 * @param spanInfo         of the cell element
	 * @param diagonalLineInfo of the cell element
	 */
	public void startTableCell(int width, IStyle style, SpanInfo spanInfo, DiagonalLineInfo diagonalLineInfo) {
		writer.openTag("w:tc");
		writer.openTag("w:tcPr");
		writeCellWidth(width);
		if (spanInfo != null) {
			writeGridSpan(spanInfo);
			writeVmerge(spanInfo);
		}
		writeCellProperties(style, diagonalLineInfo);
		writer.closeTag("w:tcPr");

		String align = style.getTextAlign();
		if (align == null) {
			return;
		}
		String direction = style.getDirection(); // bidi_hcg
		if (CSSConstants.CSS_LEFT_VALUE.equals(align)) {
			if (!CSSConstants.CSS_RTL_VALUE.equals(direction)) {
				return;
			}
		}
		writer.openTag("w:pPr");
		writeAlign(align, direction);
		writer.closeTag("w:pPr");
	}

	private void writeCellWidth(int width) {
		writer.openTag("w:tcW");
		writer.attribute("w:w", width);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:tcW");
	}

	private void writeGridSpan(SpanInfo spanInfo) {
		int columnSpan = spanInfo.getColumnSpan();
		if (columnSpan > 1) {
			writeAttrTag("w:gridSpan", columnSpan);
		}
	}

	/**
	 * Write span cell
	 *
	 * @param info span info object
	 */
	public void writeSpanCell(SpanInfo info) {
		writer.openTag("w:tc");
		writer.openTag("w:tcPr");
		writeCellWidth(info.getCellWidth());
		writeGridSpan(info);
		writeVmerge(info);
		writeCellProperties(info.getStyle(), null);
		writer.closeTag("w:tcPr");
		insertEmptyParagraph();
		writer.closeTag("w:tc");
	}

	/**
	 * Write table cell end
	 *
	 * @param empty is empty
	 */
	public void endTableCell(boolean empty) {
		endTableCell(empty, false);
	}

	/**
	 * Write table cell end
	 *
	 * @param empty     is empty
	 * @param inForeign is foreign
	 */
	public void endTableCell(boolean empty, boolean inForeign) {

		if (empty) {
			if (inForeign) {
				insertEmptyParagraphInForeign();
			} else {
				insertEmptyParagraph();
			}
		}
		writer.closeTag("w:tc");
	}

	/**
	 * Write empty table cell end
	 */
	public void writeEmptyCell() {
		writer.openTag("w:tc");
		writer.openTag("w:tcPr");
		writer.openTag("w:tcW");
		writer.attribute("w:w", 0);
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:tcW");
		writer.closeTag("w:tcPr");
		insertEmptyParagraph();
		writer.closeTag("w:tc");
	}

	/**
	 * Write empty paragraph
	 */
	public void insertEmptyParagraph() {
		writer.openTag("w:p");
		writer.openTag("w:pPr");
		writer.openTag("w:spacing");
		writer.attribute("w:line", "1");
		writer.attribute("w:lineRule", "auto");
		writer.closeTag("w:spacing");
		writer.closeTag("w:pPr");
		writer.closeTag("w:p");
	}

	/**
	 * Write empty paragraph in foreign
	 */
	public void insertEmptyParagraphInForeign() {
		writer.openTag("w:p");
		writer.closeTag("w:p");
	}

	/**
	 * Write hidden paragraph
	 */
	public void insertHiddenParagraph() {
		writer.openTag("w:p");
		writeHiddenProperty();
		writer.closeTag("w:p");
	}

	/**
	 * Write hidden property
	 */
	public void writeHiddenProperty() {
		writer.openTag("w:rPr");
		writeAttrTag("w:vanish", "on");
		writer.closeTag("w:rPr");
	}

	/**
	 * Write end paragraph
	 */
	public void endParagraph() {
		writer.closeTag("w:p");
	}

	/**
	 * Write caption
	 *
	 * @param txt caption text
	 */
	public void writeCaption(String txt) {
		writer.openTag("w:p");
		writer.openTag("w:pPr");
		writeAlign("center", null);
		writer.closeTag("w:pPr");
		writer.openTag("w:r");
		writer.openTag("w:rPr");
		writeString(txt, null);
		writer.closeTag("w:rPr");
		writer.closeTag("w:r");
		writer.closeTag("w:p");
	}

	/**
	 * If the cell properties is not set, then check the row properties and write
	 * those properties.
	 *
	 * @param style this cell style
	 */
	private void writeCellProperties(IStyle style, DiagonalLineInfo diagonalLineInfo) {
		// A cell background color may inherit from row background,
		// so we should get the row background color here,
		// if the cell background is transparent
		if (style == null) {
			return;
		}
		writeBackgroundColor(style.getBackgroundColor());
		writeCellBorders(style, diagonalLineInfo);
		writeCellPadding(style);
		String verticalAlign = style.getVerticalAlign();
		if (verticalAlign != null) {
			writeAttrTag("w:vAlign", WordUtil.parseVerticalAlign(verticalAlign));
		}
		String noWrap = CSSConstants.CSS_NOWRAP_VALUE.equalsIgnoreCase(style.getWhiteSpace()) ? "on" : "off";
		writeAttrTag("w:noWrap", noWrap);
	}

	private void writeCellBorders(IStyle style, DiagonalLineInfo diagonalLineInfo) {
		writer.openTag("w:tcBorders");
		writeBorders(style, 0, 0, 0, 0);

		if (diagonalLineInfo != null) {
			if (diagonalLineInfo.getDiagonalNumber() > 0
					&& !"none".equals(diagonalLineInfo.getDiagonalStyle())) {
				writeDiagonalBorder(diagonalLineInfo.getDiagonalStyle(), diagonalLineInfo.getDiagonalColor(),
						diagonalLineInfo.getDiagonalLineWidth());
			}
			if (diagonalLineInfo.getAntidiagonalNumber() > 0
					&& !"none".equals(diagonalLineInfo.getAntidiagonalStyle())) {
				writeAntidiagonalBorder(diagonalLineInfo.getAntidiagonalStyle(),
						diagonalLineInfo.getAntidiagonalColor(), diagonalLineInfo.getAntidiagonalLineWidth());
			}
		}
		writer.closeTag("w:tcBorders");
	}

	private void writeCellPadding(IStyle style) {
		int bottomPadding = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_BOTTOM));
		int leftPadding = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_LEFT));
		int topPadding = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_TOP));
		int rightPadding = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_RIGHT));

		// the cell padding in DOC is tcMar
		writer.openTag("w:tcMar");
		writeCellPadding(bottomPadding, BOTTOM);
		writeCellPadding(leftPadding, LEFT);
		writeCellPadding(topPadding, TOP);
		writeCellPadding(rightPadding, RIGHT);
		writer.closeTag("w:tcMar");
	}

	/**
	 *
	 * @param padding  milliPoint
	 * @param position top/right/bottom/left
	 */
	private void writeCellPadding(int padding, String position) {
		writer.openTag("w:" + position);
		writer.attribute("w:w", WordUtil.milliPt2Twips(padding));
		writer.attribute("w:type", "dxa");
		writer.closeTag("w:" + position);
	}

	protected void writeAttrTag(String name, String val) {
		writer.openTag(name);
		writer.attribute("w:val", val);
		writer.closeTag(name);
	}

	protected void writeAttrTag(String name, int val) {
		writer.openTag(name);
		writer.attribute("w:val", val);
		writer.closeTag(name);
	}

	protected void writeAttrTag(String name, double val) {
		writer.openTag(name);
		writer.attribute("w:val", val);
		writer.closeTag(name);
	}

	protected int getImageID() {
		return imageId++;
	}

	private void writeTextInParagraph(int type, String txt, IStyle style, String fontFamily, HyperlinkInfo info,
			int paragraphWidth, boolean runIsRtl) {
		writer.openTag("w:p");
		writer.openTag("w:pPr");

		CSSValue lineHeight = style.getProperty(StyleConstants.STYLE_LINE_HEIGHT);
		if (!"normal".equalsIgnoreCase(lineHeight.getCssText())) {
			writeSpacing(lineHeight, style.getProperty(StyleConstants.STYLE_MARGIN_TOP),
					style.getProperty(StyleConstants.STYLE_MARGIN_BOTTOM),
					style.getProperty(StyleConstants.STYLE_PADDING_TOP),
					style.getProperty(StyleConstants.STYLE_PADDING_BOTTOM));
		} else {
			writeSpacing(null, style.getProperty(StyleConstants.STYLE_MARGIN_TOP),
					style.getProperty(StyleConstants.STYLE_MARGIN_BOTTOM),
					style.getProperty(StyleConstants.STYLE_PADDING_TOP),
					style.getProperty(StyleConstants.STYLE_PADDING_BOTTOM));
		}

		writeAlign(style.getTextAlign(), style.getDirection());
		writeBackgroundColor(style.getBackgroundColor());
		writeParagraphBorders(style);

		int indent = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_TEXT_INDENT), paragraphWidth)
				/ 1000 * 20;

		int leftMargin = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_LEFT),
				paragraphWidth) / 1000 * 20;

		int rightMargin = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_RIGHT),
				paragraphWidth) / 1000 * 20;

		int leftPadding = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_LEFT),
				paragraphWidth) / 1000 * 20;

		int rightPadding = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_RIGHT),
				paragraphWidth) / 1000 * 20;

		leftMargin += leftPadding;
		rightMargin += rightPadding;
		writeIndent(leftMargin, rightMargin, indent);

		writeBidi(CSSConstants.CSS_RTL_VALUE.equals(style.getDirection())); // bidi_hcg
		// We need to apply the text font style to the paragraph. It is useful
		// if the end user want to paste some text into this paragraph and
		// changes the text to the paragraph's font style.
		writer.openTag("w:rPr");
		writeRunProperties(style, fontFamily, info);
		writer.closeTag("w:rPr");
		writer.closeTag("w:pPr");
		writeTextInRun(type, txt, style, fontFamily, info, false, paragraphWidth, runIsRtl);
	}

	private void writeParagraphBorders(IStyle style) {
		writer.openTag("w:pBdr");
		writeBorders(style, 0, 0, 0, 0);
		writer.closeTag("w:pBdr");
	}

	/**
	 * Write the text
	 *
	 * @param type           text type
	 * @param txt            text
	 * @param style          text style
	 * @param fontFamily     font family
	 * @param info           hyperlink info object
	 * @param flag           text flag
	 * @param paragraphWidth paragraph width
	 * @param runIsRtl       run is rtl flag
	 */
	public void writeText(int type, String txt, IStyle style, String fontFamily, HyperlinkInfo info, TextFlag flag,
			int paragraphWidth, boolean runIsRtl) {
		if (flag == TextFlag.START) {
			writeTextInParagraph(type, txt, style, fontFamily, info, paragraphWidth, runIsRtl);
		} else if (flag == TextFlag.END) {
			writer.closeTag("w:p");
		} else if (flag == TextFlag.MIDDLE) {
			writeTextInRun(type, txt, style, fontFamily, info, false, paragraphWidth, runIsRtl);
		} else {
			writeTextInParagraph(type, txt, style, fontFamily, info, paragraphWidth, runIsRtl);
			writer.closeTag("w:p");
		}
	}

	/**
	 * function emulate the overflow hidden behavior on table cell
	 *
	 * @param text       String to check
	 * @param style      style of the text
	 * @param fontFamily fond of the text
	 * @param cellWidth  the width of the container in points
	 * @return String with truncated words that surpasses the cell width
	 */
	public String cropOverflowString(String text, IStyle style, String fontFamily, int cellWidth) {
		// TODO: retrieve font type and replace plain with corresponding
		Font font = new Font(fontFamily, Font.PLAIN, WordUtil
				.parseFontSize(PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_FONT_SIZE))));
		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(font);
		// conversion from point to advancement point from sample linear
		// regression:
		int cellWidthInPointAdv = (cellWidth * (int) WordUtil.PT_TWIPS - 27) / 11;
		StringBuilder sb = new StringBuilder(text.length() + 1);
		int wordEnd = INDEX_NOTFOUND;
		do {
			wordEnd = text.indexOf(SPACE);
			if (wordEnd != INDEX_NOTFOUND) // space found
			{
				String word = text.substring(0, wordEnd);
				word = cropOverflowWord(word, fm, cellWidthInPointAdv);
				sb.append(word);
				sb.append(SPACE);
				text = text.substring(wordEnd + 1);
			}
		} while (wordEnd != INDEX_NOTFOUND && !EMPTY_STRING.equals(text));
		sb.append(cropOverflowWord(text, fm, cellWidthInPointAdv));
		return sb.toString();
	}

	/**
	 * crop words according to the given container point advance
	 *
	 * @param text                   it is a given word
	 * @param fm                     the Font metrics
	 * @param containerPointAdvWidth
	 * @return the word is cropped if longer than container width
	 */
	private String cropOverflowWord(String word, FontMetrics fm, int containerPointAdvWidth) {
		int wordlength = fm.stringWidth(word);
		if (wordlength > containerPointAdvWidth) {
			int cropEnd = (containerPointAdvWidth * word.length()) / wordlength;
			if (cropEnd == 0) {
				return "";
			}
			return word.substring(0, cropEnd);
		}
		return word;
	}

	/**
	 * Write the text inline
	 *
	 * @param type           text type
	 * @param txt            text
	 * @param style          text style
	 * @param fontFamily     font family
	 * @param info           hyperlink info object
	 * @param isInline       is inline text
	 * @param paragraphWidth paragraph width
	 * @param runIsRtl       run is rtl flag
	 */
	public void writeTextInRun(int type, String txt, IStyle style, String fontFamily, HyperlinkInfo info,
			boolean isInline, int paragraphWidth, boolean runIsRtl) {
		writeTextInRun(type, txt, style, fontFamily, info, isInline, paragraphWidth, runIsRtl, null);
	}

	/**
	 * Write the text inline
	 *
	 * @param type           text type
	 * @param txt            text
	 * @param style          text style
	 * @param fontFamily     font family
	 * @param info           hyperlink info object
	 * @param isInline       is inline text
	 * @param paragraphWidth paragraph width
	 * @param runIsRtl       run is rtl flag
	 * @param textAlign      text alignment
	 */
	public void writeTextInRun(int type, String txt, IStyle style, String fontFamily, HyperlinkInfo info,
			boolean isInline, int paragraphWidth, boolean runIsRtl, String textAlign) {
		if ("".equals(txt)) {
			return;
		}
		if (needNewParagraph(txt)) {
			writer.closeTag("w:p");
			startParagraph(style, isInline, paragraphWidth, textAlign);
			return;
		}
		if (textAlign == null)
			textAlign = style.getTextAlign();

		openHyperlink(info);
		boolean isField = WordUtil.isField(type);
		String direction = style.getDirection();

		if (isField) {
			writeField(true, style, fontFamily);
		}
		writer.openTag("w:r");
		writer.openTag("w:rPr");
		writeRunProperties(style, fontFamily, info);
		if (isInline) {
			writeAlign(textAlign, direction);
			writeBackgroundColor(style.getBackgroundColor());
			writePosition(style.getVerticalAlign(), style.getProperty(StyleConstants.STYLE_FONT_SIZE));
			writeRunBorders(style);
		}
		if (!isField && runIsRtl) {
			writer.openTag("w:rtl");
			writer.closeTag("w:rtl");
		}
		writer.closeTag("w:rPr");

		if (isField) {
			writeAutoText(type);
		} else {
			// get text attribute overflow hidden
			// and run the function to emulate if true
			if (CSSConstants.CSS_OVERFLOW_HIDDEN_VALUE.equals(style.getOverflow()) && txt != null) {
				txt = cropOverflowString(txt, style, fontFamily, paragraphWidth);
			}
			writeString(txt, style);
		}
		writer.closeTag("w:r");
		if (isField) {
			writeField(false, style, fontFamily);
		}
		closeHyperlink(info);
	}

	private void writePosition(String verticalAlign, CSSValue fontSize) {
		int size = WordUtil.parseFontSize(PropertyUtil.getDimensionValue(fontSize));
		if ("top".equalsIgnoreCase(verticalAlign)) {
			writeAttrTag("w:position", size * 1 / 3);
		} else if ("bottom".equalsIgnoreCase(verticalAlign)) {
			writeAttrTag("w:position", -size * 1 / 3);
		}
	}

	protected void writeRunProperties(IStyle style, String fontFamily, HyperlinkInfo info) {
		writeHyperlinkStyle(info, style);
		writeFont(fontFamily);
		writeFontSize(style);
		writeLetterSpacing(style);
		writeTextLineThrough(style);
		writeFontStyle(style);
		writeFontWeight(style);
	}

	protected void writeFieldRunProperties(IStyle style, String fontFamily) {
		writeFont(fontFamily);
		writeFontSize(style);
		writeLetterSpacing(style);
		writeTextLineThrough(style);
		writeFontStyle(style);
		writeFontWeight(style);
	}

	private void writeTextColor(IStyle style) {
		String val = WordUtil.parseColor(style.getColor());
		if (val != null) {
			writeAttrTag("w:color", val);
		}
	}

	private void writeTextUnderline(IStyle style) {
		String val = WordUtil.removeQuote(style.getTextUnderline());
		if (!"none".equalsIgnoreCase(val)) {
			writeAttrTag("w:u", "single");
		}
	}

	private void writeTextLineThrough(IStyle style) {
		String val = WordUtil.removeQuote(style.getTextLineThrough());
		if (!"none".equalsIgnoreCase(val)) {
			writeAttrTag("w:strike", "on");
		}
	}

	protected void startHeaderFooterContainer(int headerHeight, int headerWidth) {
		startHeaderFooterContainer(headerHeight, headerWidth, false);
	}

	protected void startHeaderFooterContainer(int headerHeight, int headerWidth, boolean writeColumns) {
		// the tableGrid in DOC has a 0.19cm cell margin by default on left and right.
		// so the header or footer width should be 2*0.19cm larger. that is 215.433
		// twips.
		headerWidth += 215;
		writer.openTag("w:tbl");
		writer.openTag("w:tblPr");
		writeTableWidth(headerWidth);
		writeAttrTag("w:tblLook", "01E0");
		writeTableCellMarginZero();
		writeTableLayout();
		writer.closeTag("w:tblPr");
		if (writeColumns) {
			writeColumn(new int[] { headerWidth });
		}
		writer.openTag("w:tr");
		// write the row height, unit: twips
		writer.openTag("w:trPr");
		writeAttrTag("w:trHeight", headerHeight);
		writer.closeTag("w:trPr");
		writer.openTag("w:tc");
		writer.openTag("w:tcPr");
		writeCellWidth(headerWidth);
		writer.closeTag("w:tcPr");
	}

	protected void endHeaderFooterContainer() {
		insertEmptyParagraph();
		writer.closeTag("w:tc");
		writer.closeTag("w:tr");
		writer.closeTag("w:tbl");
	}

	/**
	 * Draw the diagonal line
	 *
	 * @param diagonalLineInfo information object of the diagonal
	 */
	public void drawDiagonalLine(DiagonalLineInfo diagonalLineInfo) {
		if (diagonalLineInfo.getDiagonalNumber() <= 0 && diagonalLineInfo.getAntidiagonalNumber() <= 0
				|| "NONE".equals(diagonalLineInfo.getDiagonalStyle())
						&& "NONE".equals(diagonalLineInfo.getAntidiagonalStyle())) {
			return;
		}
		writer.openTag("w:p");
		writer.openTag("w:r");
		writer.openTag("w:pict");
		String lineColor = diagonalLineInfo.getColor();

		if (diagonalLineInfo.getDiagonalNumber() > 0 && !"NONE".equals(diagonalLineInfo.getDiagonalStyle())) {
			double diagonalLineWidth = diagonalLineInfo.getDiagonalLineWidth();
			String diagonalLineStyle = diagonalLineInfo.getDiagonalStyle();

			for (Line line : diagonalLineInfo.getDiagonalLine()) {
				drawLine(diagonalLineWidth, diagonalLineStyle, lineColor, line);
			}
		}
		if (diagonalLineInfo.getAntidiagonalNumber() > 0 && !"NONE".equals(diagonalLineInfo.getAntidiagonalStyle())) {
			double antidiagonalLineWidth = diagonalLineInfo.getAntidiagonalLineWidth();
			String antidiagonalLineStyle = diagonalLineInfo.getAntidiagonalStyle();
			for (Line antiLine : diagonalLineInfo.getAntidiagonalLine()) {
				drawLine(antidiagonalLineWidth, antidiagonalLineStyle, lineColor, antiLine);
			}
		}
		writer.closeTag("w:pict");
		writer.closeTag("w:r");
		writer.closeTag("w:p");
	}

	private void drawLine(double width, String style, String color, Line line) {
		writer.openTag("v:line");
		writer.attribute("id", "Line" + getLineId());
		writer.attribute("style", "position:absolute;left:0;text-align:left;z-index:1");
		writer.attribute("from", line.getXCoordinateFrom() + "pt," + line.getYCoordinateFrom() + "pt");
		writer.attribute("to", line.getXCoordinateTo() + "pt," + line.getYCoordinateTo() + "pt");
		writer.attribute("strokeweight", width + "pt");
		writer.attribute("strokecolor", "#" + color);
		writer.openTag("v:stroke");
		writer.attribute("dashstyle", WordUtil.parseLineStyle(style));
		writer.closeTag("v:stroke");
		writer.closeTag("v:line");
	}

	private int getLineId() {
		return lineId++;
	}
}
