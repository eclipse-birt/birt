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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import org.eclipse.birt.report.engine.odf.style.StyleEntry;

/**
 * ODF document content writer.
 */
@SuppressWarnings("nls")
public class ContentWriter {
	protected ExtendedXMLWriter writer;
	private int reportDpi;

	public ContentWriter(OutputStream out, int reportDpi) {
		this.reportDpi = reportDpi;
		writer = new ExtendedXMLWriter();
		writer.open(out);
	}

	/**
	 * Write the whole document
	 * 
	 * @param styles     styles part
	 * @param bodyStream body part
	 * @throws IOException
	 */
	public void write(Collection<StyleEntry> styles, InputStream bodyStream) throws IOException {
		writer.startWriter();
		writeDocumentHeader();
		writeAutomaticStyles(styles);
		writeBody(bodyStream);
		writeDocumentFooter();
		writer.endWriter();
		writer.close();
	}

	private void writeDocumentHeader() {
		writer.literal("\n");
		writer.openTag("office:document-content");

		writer.attribute("xmlns:office", "urn:oasis:names:tc:opendocument:xmlns:office:1.0");
		writer.attribute("xmlns:style", "urn:oasis:names:tc:opendocument:xmlns:style:1.0");
		writer.attribute("xmlns:text", "urn:oasis:names:tc:opendocument:xmlns:text:1.0");
		writer.attribute("xmlns:table", "urn:oasis:names:tc:opendocument:xmlns:table:1.0");
		writer.attribute("xmlns:draw", "urn:oasis:names:tc:opendocument:xmlns:drawing:1.0");
		writer.attribute("xmlns:fo", "urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0");
		writer.attribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
		writer.attribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		writer.attribute("xmlns:meta", "urn:oasis:names:tc:opendocument:xmlns:meta:1.0");
		writer.attribute("xmlns:number", "urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0");
		writer.attribute("xmlns:svg", "urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0");
		writer.attribute("xmlns:chart", "urn:oasis:names:tc:opendocument:xmlns:chart:1.0");
		writer.attribute("xmlns:dr3d", "urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0");
		writer.attribute("xmlns:math", "http://www.w3.org/1998/Math/MathML");
		writer.attribute("xmlns:form", "urn:oasis:names:tc:opendocument:xmlns:form:1.0");
		writer.attribute("xmlns:script", "urn:oasis:names:tc:opendocument:xmlns:script:1.0");
		writer.attribute("xmlns:ooo", "http://openoffice.org/2004/office");
		writer.attribute("xmlns:ooow", "http://openoffice.org/2004/writer");
		writer.attribute("xmlns:oooc", "http://openoffice.org/2004/calc");
		writer.attribute("xmlns:dom", "http://www.w3.org/2001/xml-events");
		writer.attribute("xmlns:xforms", "http://www.w3.org/2002/xforms");
		writer.attribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		writer.attribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		writer.attribute("xmlns:rpt", "http://openoffice.org/2005/report");
		writer.attribute("xmlns:of", "urn:oasis:names:tc:opendocument:xmlns:of:1.2");
		writer.attribute("xmlns:xhtml", "http://www.w3.org/1999/xhtml");
		writer.attribute("xmlns:grddl", "http://www.w3.org/2003/g/data-view#");
		writer.attribute("xmlns:tableooo", "http://openoffice.org/2009/table");
		writer.attribute("xmlns:field", "urn:openoffice:names:experimental:ooo-ms-interop:xmlns:field:1.0");
		writer.attribute("xmlns:formx", "urn:openoffice:names:experimental:ooxml-odf-interop:xmlns:form:1.0");
		writer.attribute("office:version", "1.2");
		writer.attribute("grddl:transformation", "http://docs.oasis-open.org/office/1.2/xslt/odf2rdf.xsl");
	}

	private void writeDocumentFooter() {
		writer.closeTag("office:document-content");
	}

	/**
	 * Write the automatic styles to the document.
	 * 
	 * @param styles
	 */
	private void writeAutomaticStyles(Collection<StyleEntry> styles) {
		writer.openTag("office:automatic-styles");
		StyleEntryWriter styleWriter = new StyleEntryWriter(writer, reportDpi);
		styleWriter.writeStyles(styles);
		writer.closeTag("office:automatic-styles");
	}

	private void writeBody(InputStream bodyStream) throws IOException {
		writer.write(bodyStream);
	}
}
