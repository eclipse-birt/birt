/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.build;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tools.ant.BuildException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Internal representation of a plugin.xml.
 *
 * @author Rock Yu
 */

public class PluginWrapper {

	File pluginFile = null;
	String pluginID = null;
	String pluginVersion = null;

	public PluginWrapper() {
	}

	/**
	 * @param pluginXML
	 */
	public PluginWrapper(File pluginXML) {
		this.pluginFile = pluginXML;
		parse();
	}

	private void parse() {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(pluginFile);

			NodeList nodes = doc.getElementsByTagName("plugin"); //$NON-NLS-1$
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				NamedNodeMap map = node.getAttributes();

				Node idNode = map.getNamedItem("id"); //$NON-NLS-1$
				Node versionNode = map.getNamedItem("version"); //$NON-NLS-1$

				if (idNode != null)
					this.pluginID = StringUtil.trimString(idNode.getNodeValue()); // $NON-NLS-1$

				if (versionNode != null)
					this.pluginVersion = StringUtil.trimString(versionNode.getNodeValue());
			}
		} catch (Exception e) {
			throw new BuildException("Exception occured when parsing :" + pluginFile + ". " + e); //$NON-NLS-1$
		}
	}

	/**
	 * Load and parse an "plugin.xml file
	 * 
	 * @param pluginXML
	 */

	public void load(File pluginXML) {
		this.pluginFile = pluginXML;
		this.parse();
	}

	public String getPluginID() {
		return pluginID;
	}

	public String getPluginVersion() {
		return pluginVersion;
	}
}
