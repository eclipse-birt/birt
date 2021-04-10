/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.docx.writer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.wpml.WordUtil;
import org.eclipse.birt.report.engine.layout.emitter.Image;

import org.eclipse.birt.report.engine.ooxml.IPart;
import org.eclipse.birt.report.engine.ooxml.ImageManager.ImagePart;

public class Header extends BasicComponent {

	private static Logger logger = Logger.getLogger(Header.class.getName());

	Document document;
	int headerHeight;
	int headerWidth;

	Header(IPart part, Document document, int headerHeight, int headerWidth) throws IOException {
		super(part);
		this.document = document;
		this.headerHeight = headerHeight;
		this.headerWidth = headerWidth;
	}

	void start() {
		writer.startWriter();
		writer.openTag("w:hdr");
		writeXmlns();
		startHeaderFooterContainer(headerHeight, headerWidth, true);
	}

	void end() {
		endHeaderFooterContainer();
		writer.closeTag("w:hdr");
		writer.endWriter();
		writer.close();
	}

	protected int getImageID() {
		return document.getImageID();
	}

	protected int getMhtTextId() {
		return document.getMhtTextId();
	}

	public void drawDocumentBackgroundImageWithSize(String backgroundImageUrl, String backgroundHeight,
			String backgroundWidth, double topMargin, double leftMargin, double pageHeight, double pageWidth) {
		int imageId = getImageID();
		IPart imagePart = null;
		if (backgroundImageUrl != null) {
			try {
				byte[] backgroundImageData = EmitterUtil.getImageData(backgroundImageUrl);

				ImagePart imgPart = imageManager.getImagePart(part, backgroundImageUrl, backgroundImageData);
				imagePart = imgPart.getPart();
				Image imageInfo = EmitterUtil.parseImage(null, IImageContent.IMAGE_URL, backgroundImageUrl, null, null);
				int imageWidth = imageInfo.getWidth();
				int imageHeight = imageInfo.getHeight();
				String[] realSize = WordUtil.parseBackgroundSize(backgroundHeight, backgroundWidth, imageWidth,
						imageHeight, pageWidth, pageHeight);
				drawBackgroundImageShape(realSize, topMargin, leftMargin, imageId, imagePart);
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}

	private void drawBackgroundImageShape(String[] size, double topMargin, double leftMargin, int imageId,
			IPart imagePart) {
		writer.openTag("w:sdt");
		writer.openTag("w:sdtPr");
		writer.openTag("w:id");
		writer.attribute("w:val", "90701258");
		writer.closeTag("w:id");
		writer.openTag("w:docPartObj");
		writer.openTag("w:docPartGallery");
		writer.attribute("w:val", "Cover Pages");
		writer.closeTag("w:docPartGallery");
		writer.openTag("w:docPartUnique");
		writer.closeTag("w:docPartUnique");
		writer.closeTag("w:docPartObj");
		writer.closeTag("w:sdtPr");
		writer.openTag("w:sdtContent");
		writer.openTag("w:p");
		writer.attribute("w:rsidR", "00182958");
		writer.attribute("w:rsidRDefault", "00182958");
		writer.attribute("w:rsidP", "00182958");
		writer.openTag("w:r");
		writer.openTag("w:rPr");
		writer.openTag("w:noProof");
		writer.closeTag("w:noProof");
//		writer.openTag("w:lang");
//		writer.attribute("w:eastAsia", "zh-TW");
//		writer.closeTag("w:lang");
		writer.closeTag("w:rPr");
		writer.openTag("w:pict");
		writer.openTag("v:rect");
		writer.attribute("id", "_x0000_s1041");
		String attrValue = "position:absolute;left:0;text-align:left;margin-left:" + 0// Seems leftMargin should not be
																						// used here.
				+ "pt;margin-top:" + 0// Seems topMargin should not be used here.
				+ "pt;width:" + size[1] + ";height:" + size[0]
				+ ";z-index:-1;mso-width-percent:1000;mso-position-horizontal-relative:page;mso-position-vertical-relative:page;mso-width-percent:1000";
		writer.attribute("style", attrValue);
		writer.attribute("o:allowincell", "f");
		writer.attribute("stroked", "f");
		writer.openTag("v:fill");
		writer.attribute("r:id", imagePart.getRelationshipId());
		writer.attribute("o:title", "exposure");
		writer.attribute("size", "0,0");
		writer.attribute("aspect", "atLeast");
		writer.attribute("origin", "-32767f,-32767f");
		writer.attribute("position", "-32767f,-32767f");
		writer.attribute("recolor", "t");
		writer.attribute("rotate", "t");
		writer.attribute("type", "frame");
		writer.closeTag("v:fill");
		writer.openTag("w10:wrap");
		writer.attribute("anchorx", "page");
		writer.attribute("anchory", "page");
		writer.closeTag("w10:wrap");
		writer.closeTag("v:rect");
		writer.closeTag("w:pict");
		writer.closeTag("w:r");
		writer.closeTag("w:p");
		writer.closeTag("w:sdtContent");
		writer.closeTag("w:sdt");
	}
}
