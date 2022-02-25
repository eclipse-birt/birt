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

package org.eclipse.birt.report.engine.ooxml;

import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

public enum MimeType implements ContentType {
	GIF("gif", "image/gif"), PNG("png", "image/x-png"), JPG("jpg", "application/jpeg"), JPEG("jpeg", "image/jpeg"),
	JPE("jpe ", "image/jpeg"), TIFF("tiff", "image/tiff"), TIF("tif", "image/tiff"), BMP("bmp", "image/x-ms-bmp"),
	XML("xml", "application/xml"), RELS("rels", "application/vnd.openxmlformats-package.relationships+xml"),
	HTML("html", "text/html"), MHT("mht", "message/rfc822");

	private String extension;

	private String type;

	MimeType(String extension, String type) {
		this.extension = extension;
		this.type = type;
	}

	@Override
	public void write(OOXmlWriter writer) {
		writer.openTag("Default");
		writer.attribute("Extension", extension);
		writer.attribute("ContentType", type);
		writer.closeTag("Default");
	}

	@Override
	public String toString() {
		return extension;
	}
}
