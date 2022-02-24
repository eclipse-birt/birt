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

package org.eclipse.birt.report.engine.parser;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Test case for DOM parser.
 * 
 * @version $Revision: 1.3 $ $Date: 2007/02/06 09:51:36 $
 */
public class TextParserTest extends TestCase {

	public void testParseHTMLstream() {

		Document doc = new TextParser().parse(getClass().getResourceAsStream("htmlparser_html.txt"), "Auto");
		Element root = (Element) doc.getFirstChild();
		Node child = root.getFirstChild();
		assertTrue(child.getNodeName().equals("script"));
		child = child.getNextSibling();
		String res = "";
		for (; child.getNodeType() == Node.TEXT_NODE; child = child.getNextSibling()) {
			res += child.getNodeValue();
		}

		assertEquals("&first", res);
		assertEquals("h6", child.getNodeName());
		assertEquals("h6-1", child.getFirstChild().getNodeValue());
		child = child.getNextSibling();
		assertEquals("b", child.getNodeName());
		assertEquals("bold text", child.getFirstChild().getNodeValue());

		res = "";
		child = child.getNextSibling();
		for (; child != null && child.getNodeType() == Node.TEXT_NODE; child = child.getNextSibling()) {
			res += child.getNodeValue();
		}
		// bug114821 supportAllTags
		// if not then
		// assertEquals( "secondcell value", res );
		assertEquals("second", res);
	}

	public void testParsePlainstream() {

		Document doc = new TextParser().parse(getClass().getResourceAsStream("htmlparser_plain.txt"), null);
		Element root = (Element) doc.getFirstChild();
		Node child = root.getFirstChild();
		String res = "";
		for (; child != null && child.getNodeType() == Node.TEXT_NODE; child = child.getNextSibling()) {
			res += child.getNodeValue();
		}
		assertEquals("plain text.", res);
	}

	/*
	 * Class under test for Document parsePlainText(String)
	 */
	public void testParsePlainTextString() {
		String text = "first \r\tsecond\nthird\r\n";
		Document doc = new TextParser().parse(text, "auto");
		Element root = (Element) doc.getFirstChild();
		Node child = root.getFirstChild();
		assertEquals("first ", child.getNodeValue());
		child = child.getNextSibling();
		assertEquals("br", child.getNodeName());
		child = child.getNextSibling();
		assertEquals("\tsecond", child.getNodeValue());
		child = child.getNextSibling();
		assertEquals("br", child.getNodeName());
		child = child.getNextSibling();
		assertEquals("third", child.getNodeValue());
	}

	public void testParseHTMLString() {
		String html = "first<div>div1</div>second";
		Document doc = new TextParser().parse(html, "Html");
		Element root = (Element) doc.getFirstChild();
		Node child = root.getFirstChild();
		String res = "";
		for (; child.getNodeType() == Node.TEXT_NODE; child = child.getNextSibling()) {
			res += child.getNodeValue();
		}
		assertEquals("first", res);
		assertEquals("div", child.getNodeName());
		assertEquals("div1", child.getFirstChild().getNodeValue());
		child = child.getNextSibling();
		assertEquals("second", child.getNodeValue());

	}

	public void testParseHTMLStringAuto() {
		String html = " 	 \r \r\n \t	 <html><body>first<div>div1</div>second</body></html>";
		Document doc = new TextParser().parse(html, null);
		Element root = (Element) doc.getFirstChild();
		Node child = root.getFirstChild();
		String res = "";
		for (; child.getNodeType() == Node.TEXT_NODE; child = child.getNextSibling()) {
			res += child.getNodeValue();
		}
		assertEquals("first", res);
		assertEquals("div", child.getNodeName());
		assertEquals("div1", child.getFirstChild().getNodeValue());
		child = child.getNextSibling();
		assertEquals("second", child.getNodeValue());

	}

	public void testParseEmptyHTML() {
		String html = "   	 ";
		Document doc = new TextParser().parse(html, "html");
		Element root = (Element) doc.getFirstChild();
		assertEquals(root.getChildNodes().getLength(), 0);

		// TextParser supports HTML comments now
		html = " <!--html><body></body>-->";
		doc = new TextParser().parse(html, "html");
		root = (Element) doc.getFirstChild();
		assertEquals(root.getChildNodes().getLength(), 1);
	}
}
