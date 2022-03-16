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
package org.eclipse.birt.build.framework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Bundle {

	protected BundleFile bundleFile;

	protected boolean fragment;
	protected String bundleID;
	protected String hostID;
	protected Document description;
	protected ArrayList<Bundle> fragments;

	public Bundle(File file) throws FrameworkException {
		try {
			if (file.isDirectory()) {
				this.bundleFile = new FileBundleFile(this, file);
			} else {
				this.bundleFile = new ZipBundleFile(this, file);
			}

			loadBundle();
		} catch (Exception ex) {
			close();
			throw new FrameworkException(ex);
		}
	}

	public void close() {
		if (bundleFile != null) {
			try {
				bundleFile.close();
			} catch (IOException ex) {
			}
			bundleFile = null;
		}
	}

	public BundleFile getBundleFile() {
		return bundleFile;
	}

	public List<Bundle> getFragments() {
		if (fragments == null || fragments.isEmpty()) {
			return null;
		}
		return fragments;
	}

	public void addFragment(Bundle bundle) {
		if (isFragment()) {
			throw new UnsupportedOperationException("only host bundle can be attached with fragment");
		}
		if (!bundle.isFragment()) {
			throw new UnsupportedOperationException("only fragment can be attached to the host bundle");
		}
		if (fragments == null) {
			fragments = new ArrayList<>();
		}
		fragments.add(bundle);
	}

	public boolean isFragment() {
		return fragment;
	}

	public String getHostID() {
		return hostID;
	}

	public String getBundleID() {
		return bundleID;
	}

	public Document getDescription() {
		return description;
	}

	protected void loadBundle() throws IOException, ParserConfigurationException, SAXException {
		Manifest manifest = loadManifest();
		Attributes mainAttrs = manifest.getMainAttributes();
		// first get the properties
		String propertyFile = mainAttrs.getValue("Bundle-Localization");
		Properties properties = null;
		if (propertyFile != null) {
			properties = loadProperties(propertyFile + ".properties");
		}
		// get the bundle id
		String symbolicName = mainAttrs.getValue("Bundle-SymbolicName");
		if (symbolicName != null) {
			bundleID = getSymbolicName(symbolicName);
		}
		// get the fragment host
		String patchFragment = mainAttrs.getValue("Eclipse-PatchFragment");
		if ("true".equals(patchFragment)) {
			fragment = true;
		}

		// get the fragment host
		String fragmentHost = mainAttrs.getValue("Fragment-Host");
		if (fragmentHost != null) {
			hostID = getSymbolicName(fragmentHost);
		}

		// load the fragment description
		description = loadDescription("plugin.xml");

		if (description != null) {
			if (properties != null) {
				localizeDocument(description, properties);
			}
			normalizeExtension(bundleID, description);
		}
	}

	private String getSymbolicName(String symbolicName) {
		if (symbolicName != null) {
			int charPos = symbolicName.indexOf(";");
			if (charPos != -1) {
				return symbolicName.substring(0, charPos);
			}
		}
		return symbolicName;
	}

	private Manifest loadManifest() throws IOException {
		BundleEntry entry = bundleFile.getEntry("/META-INF/MANIFEST.MF");
		if (entry != null) {
			InputStream in = entry.getInputStream();
			if (in != null) {
				try {
					Manifest manifest = new Manifest(in);
					return manifest;
				} finally {
					in.close();
				}
			}
		}
		return null;
	}

	private Properties loadProperties(String path) throws IOException {
		BundleEntry entry = bundleFile.getEntry(path);
		if (entry != null) {
			InputStream in = entry.getInputStream();
			if (in != null) {
				try {
					Properties properties = new Properties();
					properties.load(in);
					return properties;
				} finally {
					in.close();
				}
			}
		}
		return null;
	}

	private Document loadDescription(String path) throws IOException, ParserConfigurationException, SAXException {
		BundleEntry entry = bundleFile.getEntry(path);
		if (entry != null) {
			InputStream in = entry.getInputStream();
			if (in != null) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(in, path);
				return document;
			}
		}
		return null;

	}

	private void localizeDocument(Document document, Properties properties) {
		Element documentElement = document.getDocumentElement();
		localizeElement(documentElement, properties);
	}

	private void normalizeExtension(String bundleId, Document document) {
		Element documentElement = document.getDocumentElement();
		NodeList nodes = documentElement.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				String name = element.getTagName();
				if ("extension".equals(name) || "extension-point".equals(name)) {
					String id = element.getAttribute("id");
					if (id != null && id.length() > 0 && id.indexOf('.') == -1) {
						id = bundleId + '.' + id;
						element.setAttribute("id", id);
					}

				}
			}
		}
	}

	private void localizeElement(Element element, Properties properties) {
		// localize the attributes
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			if (attribute.getNodeType() == Node.ATTRIBUTE_NODE) {
				String value = attribute.getNodeValue();
				if (value != null) {
					String localizedValue = localize(value, properties);
					if (!value.equals(localizedValue)) {
						attribute.setNodeValue(localizedValue);
					}
				}
			}
		}
		// localize all the child element
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				localizeElement((Element) child, properties);
			}
		}
	}

	private String localize(String value, Properties properties) {
		if (value != null && value.length() > 1) {
			if (value.charAt(0) == '%') {
				String key = value.substring(1);
				String localizedValue = properties.getProperty(key);
				if (localizedValue != null) {
					return localizedValue;
				}
			}
		}
		return value;
	}

}
