/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.util;

import java.io.InputStream;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DesignFileCompareUtil {

	private HashSet<String> ignoreAttrs = null;

	public DesignFileCompareUtil(HashSet<String> set) {
		ignoreAttrs = set;
	}

	public boolean compare(InputStream a, InputStream b) throws Exception {
		Document goldenFileDoc = getDocumentFromInputStream(a);
		Document designFileDoc = getDocumentFromInputStream(b);
		return compareDom(goldenFileDoc, designFileDoc);
	}

	private boolean compareDom(Document d1, Document d2) {
		return compareElement(d1.getDocumentElement(), d2.getDocumentElement());
	}

	private boolean compareElement(Node e1, Node e2) {
		if (!e1.getNodeName().equals(e2.getNodeName()) || !compareAttrs(e1.getAttributes(), e2.getAttributes())) {
			return false;
		}
		NodeList enl1 = e1.getChildNodes();
		NodeList enl2 = e2.getChildNodes();
		int nLength1 = enl1.getLength();
		int nLength2 = enl2.getLength();
		if (nLength1 != nLength2) {
			return false;
		}
		for (int i = 0; i < nLength1; i++) {
			Node currentChild1 = enl1.item(i);
			Node currentChild2 = enl2.item(i);
			compareElement(currentChild1, currentChild2);
		}
		return true;
	}

	private boolean compareAttrs(NamedNodeMap a1, NamedNodeMap a2) {
		int length1 = a1 == null ? 0 : a1.getLength();
		int length2 = a2 == null ? 0 : a2.getLength();
		if (length1 != length2) {
			return false;
		}
		for (int i = 0; i < length1; i++) {
			Attr attr1 = (Attr) a1.item(i);
			Attr attr2 = (Attr) a2.item(i);
			if (!attr1.getName().equals(attr2.getName())) {
				return false;
			}
			if (!ignoreAttrs.contains(attr1.getName())) {
				if (!attr1.getValue().equals(attr2.getValue())) {
					return false;
				}
			}
		}
		return true;
	}

	private Document getDocumentFromInputStream(InputStream is) throws Exception {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
			return doc;
		} catch (Exception e) {
			throw e;
		}
	}
}
