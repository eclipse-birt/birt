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

public interface IWordWriter {

	void start(boolean rtl, String creator, String title, String description, String subject) throws IOException;

	void drawDocumentBackground(String backgroundColor, String backgroundImageUrl, String backgrounHeight,
			String backgroundWidth) throws IOException;

	void drawDocumentBackgroundImage(String backgroundImageUrl, String backgroundHeight, String backgroundWidth,
			double topMargin, double leftMargin, double pageHeight, double pageWidth) throws IOException;

	void end() throws IOException;

	void startSectionInParagraph();

	void endSectionInParagraph();

	void startSection();

	void endSection();

	void writePageProperties(int pageHeight, int pageWidth, int headerHeight, int footerHeight, int topMargin,
			int bottomMargin, int leftMargin, int rightMargin, String orient);

	void writePageBorders(IStyle style, int topMargin, int bottomMargin, int leftMargin, int rightMargin);

	void startTable(IStyle style, int tableWidth);

	void startTable(IStyle style, int tableWidth, boolean inForeign);

	void endTable();

	void writeColumn(int[] cols);

	void startTableRow(double height, boolean isHeader, boolean repeatHeader, boolean fixedLayout);

	void startTableRow(double height);

	void endTableRow();

	void startTableCell(int width, IStyle style, SpanInfo info);

	void endTableCell(boolean needEmptyP);

	void endTableCell(boolean needEmptyP, boolean inForeign);

	void writeSpanCell(SpanInfo info);

	void writeEmptyCell();

	void writeTOC(String toc, int tocLevel);

	void writeTOC(String toc, String color, int tocLevel, boolean middleInline);

	void insertHiddenParagraph();

	void insertEmptyParagraph();

	void endParagraph();

	void writeCaption(String txt);

	void writeBookmark(String bm);

	void drawImage(byte[] data, double height, double width, HyperlinkInfo hyper, IStyle style, InlineFlag inlineFlag,
			String altText, String uri);

	void drawDiagonalLine(DiagonalLineInfo diagonalLineInfo);

	void startHeader(boolean showHeaderOnFirst, int headerHeight, int headerWidth) throws IOException;

	void endHeader();

	void startFooter(int footerHeight, int footerWidth) throws IOException;

	void endFooter();

	void writeForeign(IForeignContent foreignContent);

	void writeContent(int type, String txt, IStyle style, IStyle inlineStyle, String fontFamily, HyperlinkInfo info,
			InlineFlag inlineFlag, TextFlag flag, int paragraphWidth, boolean runIsRtl, String textAlign);

	void startPage();

	void endPage();
}
