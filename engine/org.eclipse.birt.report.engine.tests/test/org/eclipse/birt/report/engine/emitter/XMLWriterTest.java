/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * Testcase for XMLWriter
 * 
 */
public class XMLWriterTest extends TestCase {

	public void test() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XMLWriter writer = new XMLWriter();
		writer.open(out);
		writer.startWriter();
		writer.openTag("fo:root");
		writer.attribute("xmlns:fo", "http://www.w3.org/1999/XSL/Format");
		writer.openTag("fo:layout-master-set");
		writer.openTag("fo:simple-page-master");
		writer.attribute("master-name", "example");
		writer.attribute("page-height", "16.8in");
		writer.attribute("page-width", "21.2in");
		writer.attribute("margin-left", "1cm");
		writer.attribute("margin-top", "1cm");
		writer.attribute("margin-bottom", "1cm");
		writer.attribute("margin-right", "1cm");
		writer.openTag("fo:region-body");
		writer.closeTag("fo:region-body");
		writer.closeTag("fo:simple-page-master");
		writer.closeTag("fo:layout-master-set");
		writer.openTag("fo:page-sequence");
		writer.attribute("master-reference", "example");
		writer.openTag("fo:flow");
		writer.attribute("flow-name", "xsl-region-body");
		writer.openTag("fo:table");
		writer.attribute("width", "518mm");
		writer.openTag("fo:table-column");
		writer.attribute("number-columns-repeated", 2);
		writer.closeTag("fo:table-column");

		writer.openTag("fo:table-body");
		writer.openTag("fo:table-row");
		writer.attribute("height", "32pt");
		writer.openTag("fo:table-cell");
		writer.openTag("fo:block");
		writer.attribute("color", "blue");
		writer.text("cell-value");
		writer.closeTag("fo:block");
		writer.closeTag("fo:table-cell");
		writer.openTag("fo:table-cell");
		writer.closeTag("fo:table-cell");
		writer.closeTag("fo:table-row");
		writer.closeTag("fo:table-body");
		writer.closeTag("fo:table");
		writer.closeTag("fo:flow");
		writer.closeTag("fo:page-sequence");
		writer.closeTag("fo:root");
		writer.close();

		String result = out.toString("UTF-8");
		// System.out.println(result);
		InputStream in = this.getClass().getResourceAsStream("xmlwriter-result.txt");
		assert (in != null);
		byte[] buffer = new byte[in.available()];
		in.read(buffer);
		String test = new String(buffer);
		test = test.replaceAll("[\\s|\\t]*\\n[\\s|\\t]*", "");
		result = result.replaceAll("[\\s|\\t]*\\n[\\s|\\t]*", "");

		// System.out.println(test);
		// System.out.println(result);
		assertEquals(test, result);
	}

	public void testCharacterEncoding() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XMLWriter writer = new XMLWriter();
		writer.open(out);
		writer.openTag("fo:block");
		writer.text("'\"&<>");
		writer.closeTag("fo:block");
		writer.close();

		assertEquals("<fo:block>'\"&amp;&lt;></fo:block>", out.toString("UTF-8").replaceAll("[\\r|\\n |\\t]", ""));
	}

}
