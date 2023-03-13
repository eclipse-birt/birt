/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.docx.writer;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.wpml.AbstractEmitterImpl.InlineFlag;
import org.eclipse.birt.report.engine.emitter.wpml.AbstractEmitterImpl.TextFlag;
import org.eclipse.birt.report.engine.emitter.wpml.DiagonalLineInfo;
import org.eclipse.birt.report.engine.emitter.wpml.HyperlinkInfo;
import org.eclipse.birt.report.engine.emitter.wpml.IWordWriter;
import org.eclipse.birt.report.engine.emitter.wpml.SpanInfo;
import org.eclipse.birt.report.engine.ooxml.IPart;
import org.eclipse.birt.report.engine.ooxml.ImageManager;
import org.eclipse.birt.report.engine.ooxml.Package;
import org.eclipse.birt.report.engine.ooxml.constants.ContentTypes;
import org.eclipse.birt.report.engine.ooxml.constants.NameSpaces;
import org.eclipse.birt.report.engine.ooxml.constants.RelationshipTypes;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

public class DocxWriter implements IWordWriter {

	private Package pkg;

	private Document document;

	private BasicComponent currentComponent;

	private boolean rtl = false;

	private boolean showHeaderOnFirst;

	private int wordVersion;

	public DocxWriter(OutputStream out, String tempFileDir, int compressionMode, int wordVersion) {
		pkg = Package.createInstance(out, tempFileDir, compressionMode);
		pkg.setExtensionData(new ImageManager());
		this.wordVersion = wordVersion;
	}

	@Override
	public void start(boolean rtl, String creator, String title, String description, String subject)
			throws IOException {
		this.rtl = rtl;
		writeCorePart(creator, title, description, subject);
	}

	@Override
	public void drawDocumentBackground(String backgroundColor, String backgroundImageUrl, String backgroundHeight,
			String backgroundWidth) throws IOException {
		initializeDocumentPart(backgroundColor, backgroundImageUrl, backgroundHeight, backgroundWidth);
	}

	@Override
	public void end() throws IOException {
		document.end();
		pkg.close();
	}

	private void writeCorePart(String creator, String title, String description, String subject) throws IOException {
		String uri = "docProps/core.xml";
		String type = ContentTypes.CORE;
		String relationshipType = RelationshipTypes.CORE;
		IPart corePart = pkg.getPart(uri, type, relationshipType);
		OOXmlWriter corePartWriter = null;
		try {
			corePartWriter = corePart.getWriter();
			corePartWriter.startWriter();
			corePartWriter.openTag("cp:coreProperties");
			corePartWriter.nameSpace("cp", NameSpaces.CORE);
			corePartWriter.nameSpace("dc", NameSpaces.DC);

			corePartWriter.openTag("dc:creator");
			corePartWriter.text(creator);
			corePartWriter.closeTag("dc:creator");
			corePartWriter.openTag("dc:title ");
			corePartWriter.text(title);
			corePartWriter.closeTag("dc:title");
			corePartWriter.openTag("dc:description");
			corePartWriter.text(description);
			corePartWriter.closeTag("dc:description");
			corePartWriter.openTag("dc:subject");
			corePartWriter.text(subject);
			corePartWriter.closeTag("dc:subject");
			corePartWriter.closeTag("cp:coreProperties");
			corePartWriter.endWriter();
		} finally {
			if (corePartWriter != null) {
				corePartWriter.close();
			}
		}
	}

	private void initializeDocumentPart(String backgroundColor, String backgroundImageUrl, String backgroundHeight,
			String backgroundWidth) throws IOException {
		String uri = "word/document.xml";
		String type = ContentTypes.WORD_PROCESSINGML;
		String relationshipType = RelationshipTypes.DOCUMENT;
		IPart documentPart = pkg.getPart(uri, type, relationshipType);
		document = new Document(documentPart, backgroundColor, backgroundImageUrl, backgroundHeight, backgroundWidth,
				rtl, wordVersion);
		document.start();
		currentComponent = document;
	}

	@Override
	public void startSectionInParagraph() {
		document.startSectionInParagraph();
	}

	@Override
	public void endSectionInParagraph() {
		document.endSectionInParagraph();
	}

	@Override
	public void startSection() {
		document.startSection();
	}

	@Override
	public void endSection() {
		document.endSection();
	}

	@Override
	public void startHeader(boolean showHeaderOnFirst, int headerHeight, int headerWidth) throws IOException {
		currentComponent = document.createHeader(headerHeight, headerWidth);
		currentComponent.start();
		this.showHeaderOnFirst = showHeaderOnFirst;
	}

	@Override
	public void endHeader() {
		currentComponent.end();
		document.writeHeaderReference(currentComponent, showHeaderOnFirst);
		currentComponent = document;
	}

	@Override
	public void startFooter(int footerHeight, int footerWidth) throws IOException {
		currentComponent = document.createFooter(footerHeight, footerWidth);
		currentComponent.start();
	}

	@Override
	public void endFooter() {
		currentComponent.end();
		document.writeFooterReference(currentComponent);
		currentComponent = document;
	}

	@Override
	public void drawImage(byte[] data, double height, double width, HyperlinkInfo hyper, IStyle style,
			InlineFlag inlineFlag, String altText, String uri) {
		currentComponent.drawImage(data, height, width, hyper, style, inlineFlag, altText, uri);
	}

	@Override
	public void writePageProperties(int pageHeight, int pageWidth, int headerHeight, int footerHeight, int topMargin,
			int bottomMargin, int leftMargin, int rightMargin, String orient) {
		document.writePageProperties(pageHeight, pageWidth, headerHeight, footerHeight, topMargin, bottomMargin,
				leftMargin, rightMargin, orient);
	}

	@Override
	public void startTable(IStyle style, int tableWidth) {
		currentComponent.startTable(style, tableWidth, false);
	}

	@Override
	public void startTable(IStyle style, int tableWidth, boolean inForeign) {
		currentComponent.startTable(style, tableWidth, inForeign);
	}

	@Override
	public void endTable() {
		currentComponent.endTable();
	}

	@Override
	public void writeColumn(int[] cols) {
		currentComponent.writeColumn(cols);
	}

	@Override
	public void startTableRow(double height, boolean isHeader, boolean repeatHeader, boolean fixedLayout) {
		currentComponent.startTableRow(height, isHeader, repeatHeader, fixedLayout);
	}

	@Override
	public void startTableRow(double height) {
		currentComponent.startTableRow(height, false, false, false);
	}

	@Override
	public void endTableRow() {
		currentComponent.endTableRow();
	}

	@Override
	public void startTableCell(int width, IStyle style, SpanInfo info) {
		currentComponent.startTableCell(width, style, info);
	}

	@Override
	public void endTableCell(boolean needEmptyP) {
		currentComponent.endTableCell(needEmptyP);
	}

	@Override
	public void endTableCell(boolean needEmptyp, boolean inForeign) {
		currentComponent.endTableCell(needEmptyp, inForeign);
	}

	@Override
	public void writeSpanCell(SpanInfo info) {
		currentComponent.writeSpanCell(info);
	}

	@Override
	public void writeEmptyCell() {
		currentComponent.writeEmptyCell();
	}

	@Override
	public void writeContent(int type, String txt, IStyle style, IStyle inlineStyle, String fontFamily,
			HyperlinkInfo info, InlineFlag inlineFlag, TextFlag textFlag, int pargraphWidth, boolean runIsRtl,
			String textAlign) {
		if (inlineFlag == InlineFlag.BLOCK) {
			currentComponent.writeText(type, txt, style, fontFamily, info, textFlag, pargraphWidth, runIsRtl);
		} else {
			boolean isInline = true;
			if (inlineFlag == InlineFlag.FIRST_INLINE && textFlag == TextFlag.START) {
				currentComponent.startParagraph(style, isInline, pargraphWidth, textAlign);
			}
			currentComponent.writeTextInRun(type, txt, style, fontFamily, info, isInline, pargraphWidth, runIsRtl,
					textAlign);
		}
	}

	@Override
	public void writeTOC(String toc, int tocLevel) {
		currentComponent.writeTOC(toc, tocLevel);
	}

	@Override
	public void writeTOC(String toc, String color, int tocLevel, boolean middleInline) {
		currentComponent.writeTOC(toc, color, tocLevel, middleInline);
	}

	@Override
	public void insertHiddenParagraph() {
		currentComponent.insertHiddenParagraph();
	}

	@Override
	public void insertEmptyParagraph() {
		currentComponent.insertEmptyParagraph();
	}

	@Override
	public void endParagraph() {
		currentComponent.endParagraph();
	}

	@Override
	public void writeCaption(String txt) {
		currentComponent.writeCaption(txt);
	}

	@Override
	public void writeBookmark(String bm) {
		currentComponent.writeBookmark(bm);
	}

	@Override
	public void writeForeign(IForeignContent foreignContent) {
		currentComponent.writeForeign(foreignContent);
	}

	@Override
	public void endPage() {
	}

	@Override
	public void startPage() {
	}

	@Override
	public void writePageBorders(IStyle style, int topMargin, int bottomMargin, int leftMargin, int rightMargin) {
		document.writePageBorders(style, topMargin, bottomMargin, leftMargin, rightMargin);
	}

	@Override
	public void drawDiagonalLine(DiagonalLineInfo diagonalLineInfo) {
		document.drawDiagonalLine(diagonalLineInfo);
	}

	@Override
	public void drawDocumentBackgroundImage(String backgroundImageUrl, String backgroundHeight, String backgroundWidth,
			double topMargin, double leftMargin, double pageHeight, double pageWidth) throws IOException {
		((Header) currentComponent).drawDocumentBackgroundImageWithSize(backgroundImageUrl, backgroundHeight,
				backgroundWidth, topMargin, leftMargin, pageHeight, pageWidth);

	}
}
