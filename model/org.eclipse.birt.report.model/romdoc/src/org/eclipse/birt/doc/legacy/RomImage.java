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

package org.eclipse.birt.doc.legacy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RomImage {
	Document document = null;

	public void open() throws RomException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(new File("orig/rom.def"));
		} catch (SAXException sxe) {
			// Error generated during parsing
			Exception x = sxe;
			if (sxe.getException() != null) {
				x = sxe.getException();
			}
			x.printStackTrace();
			throw new RomException(sxe);
		} catch (ParserConfigurationException | IOException ioe) {
			// I/O error
			ioe.printStackTrace();
			throw new RomException(ioe);
		}
	}

	public void write() throws RomException, IOException {
		// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RomException(e);
		}
		DOMSource source = new DOMSource(document);
		FileWriter writer = new FileWriter("docs/rom.def");
		StreamResult result = new StreamResult(writer);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new RomException(e1);
		}
		writer.close();
	}

	static class RomException extends Exception {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		RomException(Exception e) {
			super(e);
		}
	}

	public Element findElement(String name) {
		return findDomElement("Element", name);
	}

	private Element findDomElement(String tag, String name) {
		NodeList list = document.getElementsByTagName(tag);
		int n = list.getLength();
		for (int i = 0; i < n; i++) {
			Element e = (Element) list.item(i);
			if (e.getAttribute("name").equals(name)) {
				return e;
			}
		}
		return null;
	}

	public Element findClass(String name) {
		return findDomElement("Class", name);
	}

	public Element findProperty(Element element, String tag, String name) {
		NodeList list = element.getElementsByTagName(tag);
		int n = list.getLength();
		for (int i = 0; i < n; i++) {
			Element e = (Element) list.item(i);
			if (e.getAttribute("name").equals(name)) {
				return e;
			}
		}
		return null;
	}

	public Element findProperty(Element element, String name) {
		return findProperty(element, "Property", name);
	}

	public Element findMember(Element element, String name) {
		return findProperty(element, "Member", name);
	}

	public String getDefaultValue(Element romProp) {
		NodeList list = romProp.getElementsByTagName("Default");
		if (list.getLength() == 0) {
			return null;
		}
		Element valueNode = (Element) list.item(0);
		return valueNode.getNodeValue();
	}

	public void setDefaultValue(Element romProp, String defaultValue) {
		NodeList list = romProp.getElementsByTagName("Default");
		Element valueNode = null;
		if (list.getLength() == 0) {
			valueNode = document.createElement("Default");
			romProp.appendChild(valueNode);
		} else {
			valueNode = (Element) list.item(0);
		}
		valueNode.setNodeValue(defaultValue);
	}

	public Element findPropertyVisibility(Element romElement, String name) {
		NodeList list = romElement.getElementsByTagName("PropertyVisibility");
		if (list.getLength() == 0) {
			return null;
		}
		int n = list.getLength();
		for (int i = 0; i < n; i++) {
			Element e = (Element) list.item(i);
			if (e.getAttribute("name").equals(name)) {
				return e;
			}
		}
		return null;
	}

	public void setPropertyVisibility(Element romElement, String name, String newValue) {
		Element visNode = findPropertyVisibility(romElement, name);
		if (visNode == null) {
			visNode = document.createElement("PropertyVisibility");
			visNode.setAttribute("name", name);
			romElement.appendChild(visNode);
		}
		visNode.setAttribute("visibility", newValue);
	}

	public Element findStructure(String name) {
		return findDomElement("Structure", name);
	}

}
