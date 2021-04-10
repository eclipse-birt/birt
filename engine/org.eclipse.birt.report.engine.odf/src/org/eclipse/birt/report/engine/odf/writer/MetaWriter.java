/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.odf.writer;

import java.io.OutputStream;
import java.util.Date;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.emitter.XMLWriter;

@SuppressWarnings("nls")
/**
 * Metadata writer. Writer for the "meta.xml" file.
 */
public class MetaWriter {
	protected XMLWriter writer;

	public MetaWriter(OutputStream out) {
		writer = new XMLWriter();
		writer.open(out);
	}

	public void start() {
		writer.startWriter();
		writer.openTag("office:document-meta");
		writer.attribute("xmlns:office", "urn:oasis:names:tc:opendocument:xmlns:office:1.0");
		writer.attribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
		writer.attribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		writer.attribute("xmlns:meta", "urn:oasis:names:tc:opendocument:xmlns:meta:1.0");
		writer.attribute("xmlns:ooo", "http://openoffice.org/2004/office");
		writer.attribute("xmlns:grddl", "http://www.w3.org/2003/g/data-view#");
		writer.attribute("office:version", "1.2");
		writer.attribute("grddl:transformation", "http://docs.oasis-open.org/office/1.2/xslt/odf2rdf.xsl");
	}

	public void writeMeta(String creator, String title, String description, String subject) throws BirtException {
		Date now = new Date();
		String nowString = DataTypeUtil.toString(now);
		writeTag("dc:title", title);
		writeTag("dc:description", description);
		writeTag("dc:subject", subject);
		writeTag("dc:date", nowString);
		writeTag("meta:creation-date", nowString);
		writeTag("meta:generator", "BIRT/2.6"); // TODO: get version from engine?
	}

	private void writeTag(String tag, String value) {
		if (value != null && !"".equals(value)) {
			writer.openTag(tag);
			writer.text(value);
			writer.closeTag(tag);
		}
	}

	public void end() {
		writer.closeTag("office:document-meta");
		writer.close();
	}

}
