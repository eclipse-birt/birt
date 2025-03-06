/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.pdf.font;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.eclipse.birt.core.util.CommonUtil;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Reader to read the font configuration file (fontsConfig*.xml)
 *
 * @since 3.3
 *
 */
public class FontConfigReader {

	/**
	 * Constructor
	 *
	 * @param url file url
	 * @return the configured font mapping
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public FontMappingConfig parseConfig(URL url) throws IOException, ParserConfigurationException, SAXException {
		InputStream input = url.openStream();
		try (input) {
			FontMappingConfig config = new FontMappingConfig();
			InputSource source = new InputSource(url.openStream());
			SAXParser parser = CommonUtil.createSAXParser();
			parser.parse(source, new FontConfigHandler(config));
			return config;
		}
	}
}
