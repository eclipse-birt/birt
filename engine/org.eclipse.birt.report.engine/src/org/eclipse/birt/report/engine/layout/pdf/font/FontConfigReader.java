/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

public class FontConfigReader {

	public FontMappingConfig parseConfig(URL url) throws IOException, ParserConfigurationException, SAXException {
		InputStream input = url.openStream();
		try {
			FontMappingConfig config = new FontMappingConfig();
			InputSource source = new InputSource(url.openStream());
			SAXParser parser = CommonUtil.createSAXParser();
			parser.parse(source, new FontConfigHandler(config));
			return config;
		} finally {
			input.close();
		}
	}
}
