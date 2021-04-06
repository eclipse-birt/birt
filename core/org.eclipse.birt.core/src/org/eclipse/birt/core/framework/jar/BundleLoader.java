/*******************************************************************************
 * Copyright (c) 2010, 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.framework.jar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IConfigurationElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BundleLoader {

	static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
	static final String BUNDLE_VERSION = "Bundle-Version";

	static final String MANIFEST_ENTRY = "META-INF/MANIFEST.MF";
	static final String PLUGIN_ENTRY = "plugin.xml";
	static final String PROPERTIES_ENTRY = "plugin.properties";

	protected ServicePlatform platform;
	protected URL root;
	protected Bundle bundle;
	protected Properties properties;

	BundleLoader(ServicePlatform platform, URL root) {
		this.platform = platform;
		this.root = root;
	}

	Bundle load() throws IOException, ParserConfigurationException, SAXException {
		properties = loadProperties(root);
		bundle = loadManifest(root);
		if (bundle != null) {
			loadExtensions(bundle);
		}
		return bundle;
	}

	protected Bundle loadManifest(URL root) throws IOException {
		InputStream in = openInputStream(root, MANIFEST_ENTRY);
		if (in == null) {
			return null;
		}
		try {
			Manifest manifest = new Manifest(in);
			Attributes attr = manifest.getMainAttributes();
			if (attr != null) {

				String symbolicName = attr.getValue(BUNDLE_SYMBOLIC_NAME);
				if (symbolicName != null) {
					int dotPos = symbolicName.indexOf(';');
					if (dotPos != -1) {
						symbolicName = symbolicName.substring(0, dotPos);
					}
					symbolicName = loadProperty(symbolicName);
					String version = attr.getValue(BUNDLE_VERSION);
					version = loadProperty(version);
					Bundle bundle = new Bundle(platform, root, symbolicName);
					bundle.version = version;
					return bundle;
				}
			}
		} finally {
			in.close();
		}
		return null;
	}

	protected Properties loadProperties(URL root) {
		InputStream in = openInputStream(root, PROPERTIES_ENTRY);
		;
		if (in == null) {
			return null;
		}
		try {
			Properties properties = new Properties();
			properties.load(in);
			return properties;
		} catch (IOException ex) {
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
		}
		return null;
	}

	protected void loadExtensions(Bundle bundle) throws ParserConfigurationException, SAXException, IOException {

		InputStream in = openInputStream(bundle.root, PLUGIN_ENTRY);
		if (in == null) {
			return;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(in);

			Element root = document.getDocumentElement();

			ArrayList<ExtensionPoint> extensionPoints = new ArrayList<ExtensionPoint>();
			ArrayList<Extension> extensions = new ArrayList<Extension>();
			NodeList elements = root.getChildNodes();
			for (int i = 0; i < elements.getLength(); i++) {
				Node node = elements.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element child = (Element) node;
					String tagName = child.getTagName();
					if ("extension-point".equals(tagName)) {
						ExtensionPoint extPoint = parseExtensionPoint(child);
						extensionPoints.add(extPoint);
					} else if ("extension".equals(tagName)) {
						Extension extension = parseExtension(child);
						extensions.add(extension);
					}

				}
			}

			bundle.extensionPoints = extensionPoints.toArray(new ExtensionPoint[extensionPoints.size()]);
			bundle.extensions = extensions.toArray(new Extension[extensions.size()]);

		} finally {
			in.close();
		}
	}

	protected ExtensionPoint parseExtensionPoint(Element element) {

		// <extension-point id="FactoryService" name="FactoryService"
		// schema="schema/FactoryService.exsd"/>

		String name = element.getAttribute("name");
		name = loadProperty(name);
		String id = element.getAttribute("id");
		id = loadProperty(id);
		String schema = element.getAttribute("schema");

		ExtensionPoint point = new ExtensionPoint(bundle, id);
		point.schema = schema;

		return point;
	}

	protected Extension parseExtension(Element element) {
		String id = element.getAttribute("id");
		id = loadProperty(id);
		String name = element.getAttribute("name");
		name = loadProperty(name);
		String point = element.getAttribute("point");

		Extension extension = new Extension(bundle, id);
		extension.label = name;
		extension.extensionPointId = point;

		ArrayList<IConfigurationElement> configurations = new ArrayList<IConfigurationElement>();
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				IConfigurationElement config = parseConfiguration(extension, (Element) node);
				configurations.add(config);
			}
		}
		extension.configuration = configurations.toArray(new ConfigurationElement[configurations.size()]);

		return extension;
	}

	protected ConfigurationElement parseConfiguration(Object parent, Element element) {
		ConfigurationElement config = new ConfigurationElement();
		config.parent = parent;
		config.name = element.getNodeName();

		config.attributes = new HashMap<String, String>();
		NamedNodeMap nodeAttrs = element.getAttributes();
		for (int i = 0; i < nodeAttrs.getLength(); i++) {
			Node attr = nodeAttrs.item(i);
			String nodeName = attr.getNodeName();
			String nodeValue = attr.getNodeValue();
			nodeValue = loadProperty(nodeValue);
			config.attributes.put(nodeName, nodeValue);
		}

		ArrayList<ConfigurationElement> childConfigs = new ArrayList<ConfigurationElement>();
		NodeList children = element.getChildNodes();

		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				ConfigurationElement childConfig = parseConfiguration(config, (Element) node);
				childConfigs.add(childConfig);

			}
		}
		config.children = childConfigs.toArray(new ConfigurationElement[childConfigs.size()]);
		return config;
	}

	protected String loadProperty(String key) {
		if (key != null && key.length() > 0) {
			if (key.charAt(0) == '%') {
				key = key.substring(1);
				if (properties != null) {
					String value = properties.getProperty(key);
					if (value != null) {
						return value;
					}
				}
			}
		}
		return key;
	}

	protected InputStream openInputStream(URL root, String name) {
		try {
			URL manifestUrl = new URL(root, name);
			InputStream in = manifestUrl.openStream();
			if (in != null) {
				return in;
			}
		} catch (IOException ex) {
		}
		return null;
	}

}
