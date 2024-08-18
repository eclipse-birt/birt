/*************************************************************************************
 * Copyright (c) 2024 Eclipse contributors and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ************************************************************************************/

package org.eclipse.birt.report.viewer.utility;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axis.components.encoding.XMLEncoder;
import org.apache.axis.components.encoding.XMLEncoderFactory;
import org.eclipse.birt.report.viewer.util.BaseTestCase;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

@SuppressWarnings("javadoc")
public class AxisEncodingTest extends BaseTestCase {

	public void testEncoding() throws Exception {

		XMLEncoder encoder = XMLEncoderFactory.getEncoder(XMLEncoderFactory.ENCODING_UTF_8);
		// encoder = new org.apache.axis.components.encoding.DefaultXMLEncoder("UTF-8");

		String originalValue = "\ud800\udc00\uD83D\uDC7D";
		int codePointCount = originalValue.codePointCount(0, originalValue.length());
		Assert.assertEquals("The string represents two code points", 2, codePointCount);

		StringWriter writer = new StringWriter();
		encoder.writeEncoded(writer, originalValue);

		// An incorrect encoding would produce this:
		// &#xD800;&#xDC00;&#xD83D;&#xDC7D;
		//
		// The parser would fail as follows:
		// Character reference "&#xD800" is an invalid XML character.
		//
		String encodedValue = writer.toString();
		Assert.assertEquals("The two unicode code points should be encoded as two entities", "&#x10000;&#x1F47D;",
				encodedValue);

		String xml = new String("<?xml version='1.0' encoding='UTF-8'?>\n<document value='" + encodedValue + "'/>");

		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(new StringReader(xml)));
		Element element = document.getDocumentElement();
		String decodedValue = element.getAttribute("value");

		Assert.assertEquals("Parser XML with the entities should decode to the original value.", originalValue,
				decodedValue);
	}
}
