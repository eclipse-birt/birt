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

package org.eclipse.birt.report.engine.executor.css;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Test case
 * 
 */
public class HTMLProcessorTest extends TestCase {

	protected String getStyle(HashMap styles, Object element, String property) {
		HashMap style = (HashMap) styles.get(element);
		if (styles != null) {
			return (String) style.get(property);
		}
		return null;
	}

	protected String getStyleAttribute(Element element, String attribute) {
		return element.getAttribute(attribute);
	}

	public void testExecute() throws Exception {
		Document doc = getDomTree();
		HashMap styles = new HashMap();
		new HTMLProcessor((ReportDesignHandle) null, null).execute((Element) doc.getFirstChild(), styles);
		Element iEle = (Element) doc.getFirstChild().getFirstChild();
		assertEquals(iEle.getTagName(), "span"); //$NON-NLS-1$
		assertEquals("red", getStyle(styles, iEle, "color")); //$NON-NLS-1$ //$NON-NLS-2$

		Element fontEle = (Element) iEle.getNextSibling();
		assertEquals(fontEle.getTagName(), "font");
		assertEquals("blue", getStyleAttribute(fontEle, "color"));
		assertEquals("4", getStyleAttribute(fontEle, "size"));
		assertEquals("news", getStyleAttribute(fontEle, "face"));

		Element uEle = (Element) fontEle.getNextSibling();
		assertEquals(uEle.getTagName(), "span");
		assertEquals("overline underline", getStyle(styles, uEle, "text-decoration"));
	}

	private Document getDomTree() throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element body = doc.createElement("body");
		doc.appendChild(body);
		Element iEle = doc.createElement("i");
		iEle.setAttribute("style", "color:red ");
		body.appendChild(iEle);

		Element fontEle = doc.createElement("font");
		fontEle.setAttribute("color", "blue");
		fontEle.setAttribute("size", "4");
		fontEle.setAttribute("face", "news");
		body.appendChild(fontEle);

		Element uEle = doc.createElement("u");
		uEle.setAttribute("style", "text-decoration:overline");
		body.appendChild(uEle);
		return doc;
	}
}
