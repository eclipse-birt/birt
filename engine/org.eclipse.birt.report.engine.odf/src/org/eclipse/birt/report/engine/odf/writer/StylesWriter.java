/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.odf.writer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;

/**
 * Style writer for the "styles.xml" file.
 */
@SuppressWarnings("nls")
public class StylesWriter {
	protected ExtendedXMLWriter writer;
	// private int reportDpi;
	protected StyleEntryWriter styleEntryWriter;

	public StylesWriter(OutputStream out, int reportDpi) {
		// this.reportDpi = reportDpi;
		writer = new ExtendedXMLWriter();
		writer.open(out);
		styleEntryWriter = new StyleEntryWriter(writer, reportDpi);
	}

	public void start() {
		writer.startWriter();
		writeDocumentHeader();
	}

	/**
	 * Write the whole document
	 *
	 * @param autoStyles styles part
	 * @throws IOException
	 */
	public void writeStyles(Collection<StyleEntry> autoStyles) throws IOException {

		// writeFontFaceDecls();

		writer.openTag("office:styles");
		writeDefaultStyles();
		writer.closeTag("office:styles");

		writer.openTag("office:automatic-styles");
		styleEntryWriter.writeStyles(autoStyles);
		writer.closeTag("office:automatic-styles");
	}

	public void writeMasterPage(InputStream masterPageContent) throws IOException {
		writer.write(masterPageContent);
	}

	public void end() {
		writeDocumentFooter();
		writer.endWriter();
		writer.close();
	}

	public void writeDefaultStyles() {
		// TODO: also write "style:default-style" entries?

		// imported from an ODF file saved by OpenOffice 3.2
		writer.openTag("style:style");
		writer.attribute("style:name", "Standard");
		writer.attribute("style:family", "paragraph");
		writer.attribute("style:class", "text");
		writer.closeTag("style:style");

		writer.openTag("style:style");
		writer.attribute("style:name", "Graphics");
		writer.attribute("style:family", "graphic");

		writer.openTag("style:graphic-properties");
		writer.attribute("text:anchor-type", "paragraph");
		writer.attribute("svg:x", "0in");
		writer.attribute("svg:y", "0in");
		writer.attribute("style:wrap", "dynamic");
		writer.attribute("style:number-wrapped-paragraphs", "no-limit");
		writer.attribute("style:wrap-contour", "false");
		writer.attribute("style:vertical-pos", "top");
		writer.attribute("style:vertical-rel", "paragraph");
		writer.attribute("style:horizontal-pos", "center");
		writer.attribute("style:horizontal-rel", "paragraph");
		writer.closeTag("style:graphic-properties");
		writer.closeTag("style:style");

		writer.openTag("style:style");
		writer.attribute("style:name", StyleConstant.HIDDEN_STYLE_NAME);
		writer.attribute("style:family", "paragraph");
		writer.openTag("style:paragraph-properties");
		writer.attribute("text:display", "none");
		writer.closeTag("style:paragraph-properties");
		writer.closeTag("style:style");
	}

	private void writeDocumentHeader() {
		writer.literal("\n");
		writer.openTag("office:document-styles");

		// TODO: refactor with content writer, and consider the differences
		// one idea is to create a namespace helper
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
		writer.attribute("xmlns:rpt", "http://openoffice.org/2005/report");
		writer.attribute("xmlns:of", "urn:oasis:names:tc:opendocument:xmlns:of:1.2");
		writer.attribute("xmlns:xhtml", "http://www.w3.org/1999/xhtml");
		writer.attribute("xmlns:grddl", "http://www.w3.org/2003/g/data-view#");
		writer.attribute("xmlns:officeooo", "http://openoffice.org/2009/office");
		writer.attribute("xmlns:tableooo", "http://openoffice.org/2009/table");
		writer.attribute("xmlns:css3t", "http://www.w3.org/TR/css3-text/");
		writer.attribute("office:version", "1.2");
		writer.attribute("grddl:transformation", "http://docs.oasis-open.org/office/1.2/xslt/odf2rdf.xsl");

		// TODO: default fonts
	}

	private void writeDocumentFooter() {
		writer.closeTag("office:document-styles");
	}

}
