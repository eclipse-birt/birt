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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.wpml.WordUtil;
import org.eclipse.birt.report.engine.ooxml.IPart;
import org.eclipse.birt.report.engine.ooxml.ImageManager.ImagePart;
import org.eclipse.birt.report.engine.ooxml.constants.ContentTypes;
import org.eclipse.birt.report.engine.ooxml.constants.NameSpaces;
import org.eclipse.birt.report.engine.ooxml.constants.RelationshipTypes;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

/**
 * Representation of document
 *
 * @since 3.3
 *
 */
public class Document extends BasicComponent {

	private static Logger logger = Logger.getLogger(Document.class.getName());

	private String backgroundColor;

	private String backgroundImageImgUrl;

	private String backgroundHeight;
	private String backgroundWidth;

	private int headerId = 1;

	private int footerId = 1;
	private int mhtId = 1;

	private int wordVersion;
	private String documentLanguage = "";

	Document(IPart part, String backgroundColor, String backgroundImageUrl, String backgroundHeight,
			String backgroundWidth, boolean rtl, int wordVersion, String documentLanguage) throws IOException {
		super(part);
		this.backgroundColor = backgroundColor;
		this.backgroundImageImgUrl = backgroundImageUrl;
		this.backgroundHeight = backgroundHeight;
		this.backgroundWidth = backgroundWidth;
		this.wordVersion = wordVersion;
		this.rtl = rtl;
		this.documentLanguage = documentLanguage;
		writeStylesPart();
		writeSettingsPart();
	}

	@Override
	void start() {
		writer.startWriter();
		writer.openTag("w:document");
		writeXmlns();
		drawDocumentBackground();
		writer.openTag("w:body");
	}

	private void writeStylesPart() throws IOException {
		String uri = "styles.xml";
		String type = ContentTypes.WORD_STYLES;
		String relationshipType = RelationshipTypes.STYLES;
		IPart stylesPart = part.getPart(uri, type, relationshipType);
		OOXmlWriter stylesPartWriter = null;
		try {
			stylesPartWriter = stylesPart.getWriter();
			stylesPartWriter.startWriter();
			stylesPartWriter.openTag("w:styles");
			stylesPartWriter.nameSpace("w", NameSpaces.WORD_PROCESSINGML);
			stylesPartWriter.openTag("w:docDefaults");
			stylesPartWriter.openTag("w:rPrDefault");
			stylesPartWriter.openTag("w:rPr");
			stylesPartWriter.openTag("w:rFonts");
			stylesPartWriter.attribute("w:ascii", "Times New Roman");
			stylesPartWriter.attribute("w:eastAsia", "Times New Roman");
			stylesPartWriter.attribute("w:hAnsi", "Times New Roman");
			stylesPartWriter.attribute("w:cs", "Times New Roman");
			stylesPartWriter.closeTag("w:rFonts");
			stylesPartWriter.openTag("w:lang");
			stylesPartWriter.attribute("w:val", this.documentLanguage);
			stylesPartWriter.attribute("w:eastAsia", "zh-CN");
			stylesPartWriter.attribute("w:bidi", "ar-SA");
			stylesPartWriter.closeTag("w:lang");
			stylesPartWriter.closeTag("w:rPr");
			stylesPartWriter.closeTag("w:rPrDefault");
			stylesPartWriter.openTag("w:pPrDefault");
			stylesPartWriter.closeTag("w:pPrDefault");
			stylesPartWriter.closeTag("w:docDefaults");
			stylesPartWriter.openTag("w:style");
			stylesPartWriter.attribute("w:type", "character");
			stylesPartWriter.attribute("w:styleId", "Hyperlink");
			stylesPartWriter.openTag("w:name");
			stylesPartWriter.attribute("w:val", "Hyperlink");
			stylesPartWriter.closeTag("w:name");
			stylesPartWriter.openTag("w:rPr");
			stylesPartWriter.openTag("w:u");
			stylesPartWriter.attribute("w:val", "single");
			stylesPartWriter.closeTag("w:u");
			stylesPartWriter.openTag("w:color");
			stylesPartWriter.attribute("w:val", "0000ff");
			stylesPartWriter.closeTag("w:color");
			stylesPartWriter.closeTag("w:rPr");
			stylesPartWriter.closeTag("w:style");

			stylesPartWriter.openTag("w:style");
			stylesPartWriter.attribute("w:type", "table");
			stylesPartWriter.attribute("w:default", 1);
			stylesPartWriter.attribute("w:styleId", "TableNormal");
			stylesPartWriter.openTag("w:name");
			stylesPartWriter.attribute("w:val", "Normal Table");
			stylesPartWriter.closeTag("w:name");
			stylesPartWriter.openTag("w:uiPriority");
			stylesPartWriter.attribute("w:val", 99);
			stylesPartWriter.closeTag("w:uiPriority");
			stylesPartWriter.openTag("w:semiHidden");
			stylesPartWriter.closeTag("w:semiHidden");
			stylesPartWriter.openTag("w:unhidenWhenUsed");
			stylesPartWriter.closeTag("w:unhidenWhenUsed");
			stylesPartWriter.openTag("w:qFormat");
			stylesPartWriter.closeTag("w:qFormat");
			stylesPartWriter.openTag("w:tblPr");
			stylesPartWriter.openTag("w:tblInd");
			stylesPartWriter.attribute("w:w", 0);
			stylesPartWriter.attribute("w:type", "dxa");
			stylesPartWriter.closeTag("w:tblInd");
			stylesPartWriter.openTag("w:tblCellMar");
			stylesPartWriter.openTag("w:top");
			stylesPartWriter.attribute("w:w", 0);
			stylesPartWriter.attribute("w:type", "dxa");
			stylesPartWriter.closeTag("w:top");
			stylesPartWriter.openTag("w:left");
			stylesPartWriter.attribute("w:w", 108);
			stylesPartWriter.attribute("w:type", "dxa");
			stylesPartWriter.closeTag("w:left");
			stylesPartWriter.openTag("w:bottom");
			stylesPartWriter.attribute("w:w", 0);
			stylesPartWriter.attribute("w:type", "dxa");
			stylesPartWriter.closeTag("w:bottom");
			stylesPartWriter.openTag("w:right");
			stylesPartWriter.attribute("w:w", 108);
			stylesPartWriter.attribute("w:type", "dxa");
			stylesPartWriter.closeTag("w:right");
			stylesPartWriter.closeTag("w:tblCellMar");
			stylesPartWriter.closeTag("w:tblPr");
			stylesPartWriter.closeTag("w:style");

			stylesPartWriter.closeTag("w:styles");
			stylesPartWriter.endWriter();
		} finally {
			if (stylesPartWriter != null) {
				stylesPartWriter.close();
			}
		}
	}

	private void writeSettingsPart() throws IOException {
		String uri = "settings.xml";
		String type = ContentTypes.WORD_SETTINGS;
		String relationshipType = RelationshipTypes.SETTINGS;
		IPart settingsPart = part.getPart(uri, type, relationshipType);
		OOXmlWriter settingsPartWriter = null;
		try {
			settingsPartWriter = settingsPart.getWriter();
			settingsPartWriter.startWriter();
			switch (wordVersion) {
			case 2010:
				settingsPartWriter.openTag("w:settings");
				settingsPartWriter.nameSpace("w", NameSpaces.WORD_PROCESSINGML);
				settingsPartWriter.nameSpace("o", NameSpaces.OFFICE);
				settingsPartWriter.nameSpace("r", NameSpaces.RELATIONSHIPS);
				settingsPartWriter.openTag("w:zoom");
				settingsPartWriter.attribute("w:percent", "100");
				settingsPartWriter.closeTag("w:zoom");
				settingsPartWriter.openTag("w:displayBackgroundShape");
				settingsPartWriter.closeTag("w:displayBackgroundShape");
				// settingsPartWriter.openTag( "w:proofState" );
				// settingsPartWriter.attribute( "w:spelling", "clean" );
				// settingsPartWriter.attribute( "w:grammar", "clean" );
				// settingsPartWriter.closeTag( "w:proofState" );
				settingsPartWriter.openTag("w:view");
				settingsPartWriter.attribute("w:val", "print");
				settingsPartWriter.closeTag("w:view");
				settingsPartWriter.openTag("w:compat");
				settingsPartWriter.openTag("w:compatSetting");
				settingsPartWriter.attribute("w:name", "w:compatibilityMode");
				settingsPartWriter.attribute("w:uri", "http://schemas.microsoft.com/office/word");
				settingsPartWriter.attribute("w:val", "12");
				settingsPartWriter.closeTag("w:compatSetting");
				settingsPartWriter.closeTag("w:compat");
				settingsPartWriter.closeTag("w:settings");
				break;
			default:
				settingsPartWriter.openTag("w:settings");
				settingsPartWriter.nameSpace("mc", NameSpaces.MARKUP_COMPATIBILITY);
				settingsPartWriter.nameSpace("o", NameSpaces.OFFICE);
				settingsPartWriter.nameSpace("r", NameSpaces.RELATIONSHIPS);
				settingsPartWriter.nameSpace("m", NameSpaces.MATH);
				settingsPartWriter.nameSpace("v", NameSpaces.VML);
				settingsPartWriter.nameSpace("w10", NameSpaces.W10);
				settingsPartWriter.nameSpace("w", NameSpaces.WORD_PROCESSINGML);
				settingsPartWriter.nameSpace("w14", NameSpaces.W14);
				settingsPartWriter.nameSpace("w15", NameSpaces.W15);
				settingsPartWriter.nameSpace("w16cex", NameSpaces.W16CEX);
				settingsPartWriter.nameSpace("w16cid", NameSpaces.W16CID);
				settingsPartWriter.nameSpace("w16", NameSpaces.W16);
				settingsPartWriter.nameSpace("w16se", NameSpaces.W16SE);
				settingsPartWriter.nameSpace("sl", NameSpaces.SCHEMA_LIBRARY);
				settingsPartWriter.attribute("mc:Ignorable", "w14 w15 w16se w16cid w16 w16cex");
				settingsPartWriter.openTag("w:zoom");
				settingsPartWriter.attribute("w:percent", "100");
				settingsPartWriter.closeTag("w:zoom");
				settingsPartWriter.openTag("w:displayBackgroundShape");
				settingsPartWriter.closeTag("w:displayBackgroundShape");
				// settingsPartWriter.openTag( "w:proofState" );
				// settingsPartWriter.attribute( "w:spelling", "clean" );
				// settingsPartWriter.attribute( "w:grammar", "clean" );
				// settingsPartWriter.closeTag( "w:proofState" );
				settingsPartWriter.openTag("w:view");
				settingsPartWriter.attribute("w:val", "print");
				settingsPartWriter.closeTag("w:view");
				settingsPartWriter.openTag("w:compat");
				settingsPartWriter.openTag("w:compatSetting");
				settingsPartWriter.attribute("w:name", "compatibilityMode");
				settingsPartWriter.attribute("w:uri", "http://schemas.microsoft.com/office/word");
				settingsPartWriter.attribute("w:val", "15");
				settingsPartWriter.closeTag("w:compatSetting");
				settingsPartWriter.closeTag("w:compat");
				settingsPartWriter.closeTag("w:settings");
			}
			settingsPartWriter.endWriter();
		} finally {
			if (settingsPartWriter != null) {
				settingsPartWriter.close();
			}
		}
	}

	@Override
	void end() {
		writer.closeTag("w:body");
		writer.closeTag("w:document");
		writer.endWriter();
		writer.close();
	}

	private void drawDocumentBackground() {
		if (backgroundImageImgUrl != null && backgroundHeight == null && backgroundWidth == null) {
			try {
				byte[] backgroundImageData = EmitterUtil.getImageData(backgroundImageImgUrl);
				ImagePart imagePart = imageManager.getImagePart(part, backgroundImageImgUrl, backgroundImageData);
				IPart part = imagePart.getPart();
				drawDocumentBackgroundImage(part);
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		} else {
			String color = WordUtil.parseColor(backgroundColor);
			if (color != null) {
				writer.openTag("w:background");
				writer.attribute("w:color", color);
				writer.closeTag("w:background");
			}
		}
	}

	private void drawDocumentBackgroundImage(IPart imagePart) {
		writer.openTag("w:background");
		writer.attribute("w:color", "FFFFFF");
		writer.openTag("v:background");
		writer.attribute("id", "");
		writer.openTag("v:fill");
		writer.attribute("r:id", imagePart.getRelationshipId());
		writer.attribute("recolor", "t");
		writer.attribute("type", "frame");
		writer.closeTag("v:fill");
		writer.closeTag("v:background");
		writer.closeTag("w:background");
	}

	void writeHeaderReference(BasicComponent header, boolean showHeaderOnFirst) {
		String type = showHeaderOnFirst ? "first" : "default";
		writer.openTag("w:headerReference");
		writer.attribute("w:type", type);
		writer.attribute("r:id", header.getRelationshipId());
		writer.closeTag("w:headerReference");
	}

	void writeFooterReference(BasicComponent footer) {
		writer.openTag("w:footerReference");
		writer.attribute("r:id", footer.getRelationshipId());
		writer.closeTag("w:footerReference");
	}

	Header createHeader(int headerHeight, int headerWidth) throws IOException {
		String uri = "header" + getHeaderID() + ".xml";
		String type = ContentTypes.WORD_HEADER;
		String relationshipType = RelationshipTypes.HEADER;
		IPart headerPart = part.getPart(uri, type, relationshipType);
		return new Header(headerPart, this, headerHeight, headerWidth);
	}

	Footer createFooter(int footerHeight, int footerWidth) throws IOException {
		String uri = "footer" + getFooterID() + ".xml";
		String type = ContentTypes.WORD_FOOTER;
		String relationshipType = RelationshipTypes.FOOTER;
		IPart footerPart = part.getPart(uri, type, relationshipType);
		return new Footer(footerPart, this, footerHeight, footerWidth);
	}

	private int getHeaderID() {
		return headerId++;
	}

	private int getFooterID() {
		return footerId++;
	}

	@Override
	protected int getImageID() {
		return imageId++;
	}

	@Override
	protected int getMhtTextId() {
		return mhtId++;
	}

	void writePageBorders(IStyle style, int topMargin, int bottomMargin, int leftMargin, int rightMargin) {
		writer.openTag("w:pgBorders");
		writer.attribute("w:offsetFrom", "page");
		writeBorders(style, topMargin, bottomMargin, leftMargin, rightMargin);
		writer.closeTag("w:pgBorders");
	}
}
