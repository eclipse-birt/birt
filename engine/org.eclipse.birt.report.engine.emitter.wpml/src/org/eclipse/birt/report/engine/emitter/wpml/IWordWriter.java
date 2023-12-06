/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.wpml;

import java.io.IOException;

import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.wpml.AbstractEmitterImpl.InlineFlag;
import org.eclipse.birt.report.engine.emitter.wpml.AbstractEmitterImpl.TextFlag;

/**
 * Interface word writer
 *
 * @since 3.3
 *
 */
public interface IWordWriter {

	/**
	 * Start of document
	 *
	 * @param rtl         rtl direction
	 * @param creator     creator name
	 * @param title       title
	 * @param description description
	 * @param subject     subject
	 * @throws IOException
	 */
	void start(boolean rtl, String creator, String title, String description, String subject) throws IOException;

	/**
	 * Draw document background
	 *
	 * @param backgroundColor    background color
	 * @param backgroundImageUrl background image URL
	 * @param backgrounHeight    background height
	 * @param backgroundWidth    background width
	 * @throws IOException
	 */
	void drawDocumentBackground(String backgroundColor, String backgroundImageUrl, String backgrounHeight,
			String backgroundWidth) throws IOException;

	/**
	 * Draw document background image
	 *
	 * @param backgroundImageUrl background image URL
	 * @param backgroundHeight   background height
	 * @param backgroundWidth    background width
	 * @param topMargin          margin top
	 * @param leftMargin         margin left
	 * @param pageHeight         page height
	 * @param pageWidth          page width
	 * @throws IOException
	 */
	void drawDocumentBackgroundImage(String backgroundImageUrl, String backgroundHeight, String backgroundWidth,
			double topMargin, double leftMargin, double pageHeight, double pageWidth) throws IOException;

	/**
	 * End of document
	 *
	 * @throws IOException
	 */
	void end() throws IOException;

	/**
	 * start section in paragraph
	 */
	void startSectionInParagraph();

	/**
	 * end section in paragraph
	 */
	void endSectionInParagraph();

	/**
	 * start section
	 */
	void startSection();

	/**
	 * end section
	 */
	void endSection();

	/**
	 *
	 * Write page property
	 *
	 * @param pageHeight   page height
	 * @param pageWidth    page width
	 * @param headerHeight header height
	 * @param footerHeight footer height
	 * @param topMargin    margin top
	 * @param bottomMargin margin bottom
	 * @param leftMargin   margin left
	 * @param rightMargin  margin right
	 * @param orient       orientation
	 */
	void writePageProperties(int pageHeight, int pageWidth, int headerHeight, int footerHeight, int topMargin,
			int bottomMargin, int leftMargin, int rightMargin, String orient);

	/**
	 * Write page border
	 *
	 * @param style        style
	 * @param topMargin    margin top
	 * @param bottomMargin margin bottom
	 * @param leftMargin   margin left
	 * @param rightMargin  margin right
	 */
	void writePageBorders(IStyle style, int topMargin, int bottomMargin, int leftMargin, int rightMargin);

	/**
	 * Start table
	 *
	 * @param style      style
	 * @param tableWidth table width
	 */
	void startTable(IStyle style, int tableWidth);

	/**
	 * Start table
	 *
	 * @param style      style
	 * @param tableWidth table width
	 * @param inForeign  in foreign container
	 */
	void startTable(IStyle style, int tableWidth, boolean inForeign);

	/**
	 * End table
	 */
	void endTable();

	/**
	 * Write columns
	 *
	 * @param cols columns array
	 */
	void writeColumn(int[] cols);

	/**
	 * Start table row
	 *
	 * @param height       row height
	 * @param isHeader     row is header
	 * @param repeatHeader repeat header
	 * @param fixedLayout  fixed layout
	 */
	void startTableRow(double height, boolean isHeader, boolean repeatHeader, boolean fixedLayout);

	/**
	 * Start table row
	 *
	 * @param height row height
	 */
	void startTableRow(double height);

	/**
	 * End of row
	 */
	void endTableRow();

	// void startTableCell(int width, IStyle style, SpanInfo info);

	/**
	 * Create the table start tag of cell element
	 *
	 * @param width
	 * @param style
	 * @param info
	 * @param diagonalLineInfo
	 */
	void startTableCell(int width, IStyle style, SpanInfo info, DiagonalLineInfo diagonalLineInfo);

	/**
	 * End of table cell
	 *
	 * @param needEmptyP need empty paragraph
	 */
	void endTableCell(boolean needEmptyP);

	/**
	 * End of table cell
	 *
	 * @param needEmptyP need empty paragraph
	 * @param inForeign  is foreign
	 */
	void endTableCell(boolean needEmptyP, boolean inForeign);

	/**
	 * Write span cell
	 *
	 * @param info span info
	 */
	void writeSpanCell(SpanInfo info);

	/**
	 * Write empty cell
	 */
	void writeEmptyCell();

	/**
	 * Write TOC
	 *
	 * @param toc      toc entry
	 * @param tocLevel toc level
	 */
	void writeTOC(String toc, int tocLevel);

	/**
	 * Write TOC
	 *
	 * @param toc          toc entry
	 * @param color        entry color
	 * @param tocLevel     toc level
	 * @param middleInline middle inline
	 */
	void writeTOC(String toc, String color, int tocLevel, boolean middleInline);

	/**
	 * Insert hidden paragraph
	 */
	void insertHiddenParagraph();

	/**
	 * Insert empty paragraph
	 */
	void insertEmptyParagraph();

	/**
	 * End paragraph
	 */
	void endParagraph();

	/**
	 * Write caption
	 *
	 * @param txt caption text
	 */
	void writeCaption(String txt);

	/**
	 * Write bookmark
	 *
	 * @param bm bookmark text
	 */
	void writeBookmark(String bm);

	/**
	 * Draw image
	 *
	 * @param data       image data
	 * @param height     image height
	 * @param width      image width
	 * @param hyper      image hyper link
	 * @param style      image style
	 * @param inlineFlag inline flag
	 * @param altText    alt text
	 * @param uri        image URI
	 */
	void drawImage(byte[] data, double height, double width, HyperlinkInfo hyper, IStyle style, InlineFlag inlineFlag,
			String altText, String uri);

	/**
	 * Draw diagonal line
	 *
	 * @param diagonalLineInfo diagonal line info
	 */
	void drawDiagonalLine(DiagonalLineInfo diagonalLineInfo);

	/**
	 * Start header
	 *
	 * @param showHeaderOnFirst show header on first
	 * @param headerHeight      header height
	 * @param headerWidth       header width
	 * @throws IOException
	 */
	void startHeader(boolean showHeaderOnFirst, int headerHeight, int headerWidth) throws IOException;

	/**
	 * End header
	 */
	void endHeader();

	/**
	 * Start footer
	 *
	 * @param footerHeight footer height
	 * @param footerWidth  footer width
	 * @throws IOException
	 */
	void startFooter(int footerHeight, int footerWidth) throws IOException;

	/**
	 * End footer
	 */
	void endFooter();

	/**
	 * Write foreign
	 *
	 * @param foreignContent foreign content
	 */
	void writeForeign(IForeignContent foreignContent);

	/**
	 * Write content
	 *
	 * @param type           content type
	 * @param txt            content text
	 * @param style          content style
	 * @param inlineStyle    inline style
	 * @param fontFamily     font family
	 * @param info           hyperlink info
	 * @param inlineFlag     inline flag
	 * @param flag           text flag
	 * @param paragraphWidth paragraph width
	 * @param runIsRtl       run as rtl
	 * @param textAlign      text alignment
	 */
	void writeContent(int type, String txt, IStyle style, IStyle inlineStyle, String fontFamily, HyperlinkInfo info,
			InlineFlag inlineFlag, TextFlag flag, int paragraphWidth, boolean runIsRtl, String textAlign);

	/**
	 * Start page
	 */
	void startPage();

	/**
	 * End page
	 */
	void endPage();
}
